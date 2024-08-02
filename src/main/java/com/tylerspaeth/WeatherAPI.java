package com.tylerspaeth;

import java.lang.InterruptedException;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.javatuples.Pair;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * The class provides all of the necessary functionality to interact with
 * WeatherAPI.
 */
public class WeatherAPI {

	//Right now the data that is being returned from the httprequest is being
	//limited from my account on the weatherapi website. Reducing the amount
	//of data transfer since we only need a small amount of data
	
	private String baseURL = "https://us-central1-personal-416521.cloudfunctions.net/fishing-app-func";
	// Maps the weatherapi weather condition codes to the appropriate Catch.Weather enum value	
	private Hashtable<Integer, Catch.Weather> apiToEnum;

	/**
	 * This method is the default constructor for a WeatherAPI object. Its main purpose 
	 * is to intialize the apiToEnum Hashtable which utilizes a csv file containing
	 * weather condition codes along with corresponding enum values to be used
	 * by the program.
	 */
	public WeatherAPI() {

		this.apiToEnum = new Hashtable<Integer, Catch.Weather>();

		Scanner scanner = null;
		File file = null;
		try {
			file = new File("src/main/resources/WeatherAPIWeatherConditions.csv");
			scanner = new Scanner(file);
		}
		catch(FileNotFoundException e) {
			System.out.println(file.getAbsolutePath());
			e.printStackTrace();
			System.exit(0);

		}
		scanner.useDelimiter(",|\\n");// TODO not sure what delimiters to use
																	
		int code = 0; // The weather api condition code for the current line
		int index = 0; // The index for number of items read from file

		while(scanner.hasNext()) {
			String input = scanner.next();
			// If we are on the first element of the line
			// and it is not the first line
			if(index % 4 == 0 && index > 3) { 
				code = Integer.parseInt(input);
			}
			// If we are on the last element of the line
			// and it is not the first line
			else if(index % 4 == 3 && index > 3) { 
				apiToEnum.put(code, Catch.Weather.valueOf(input.trim()));
			}

			index++;
		}
		
		scanner.close();

	}

	/**
	 * This method gets the forcasted weather condition in a given zipcode a certain number 
	 * of days from now (0 is current day). As of the current version of this program, the
	 * maximum number of days into the future that works is 2 days.
	 *
	 * @param zipCode the 5 digit zipcode.
	 * @param daysFromNow the number of days from the current day that the weather should be 
	 * checked. 0 means the current day.
	 * @return a pair containing a Catch.Weather object corresponding to the weather in the
	 * location along with a String containing the name of the city. 
	 */
	public Pair<Catch.Weather, String> getWeather(int zipCode, int daysFromNow) throws IllegalArgumentException {

		// Verify that a valid number of days was given
		if(daysFromNow > 2 || daysFromNow < 0)
			throw new IllegalArgumentException("daysFromNow must be in the range of 0-2");

		// TODO this is not a perfect way to verify.
		// Verify that the zipcode that was given was at least the right size
		if(zipCode > 99999 || zipCode < 0)
			throw new IllegalArgumentException("Invalid zipCode, must be a 5 digit number");

		try {
			// By default this is a get request
			HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(new URI(baseURL + "?zipcode=" + zipCode))
				.build();

			HttpClient httpClient = HttpClient.newHttpClient();

			HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

			int code = 0;
			String city = "";

			try {
				// Retrieve the condition corresponding to a Weather enum value in the apiToEnum HashTable
				code = new JSONObject(getResponse.body())
					.getJSONObject("forecast")
					.getJSONArray("forecastday")
					.getJSONObject(daysFromNow)
					.getJSONObject("day")
					.getJSONObject("condition")
					.getInt("code");
				city = new JSONObject(getResponse.body())
					.getJSONObject("location")
					.getString("name");
				
			}
			catch(JSONException e) {
				// If there is a json exception, that it was because it was an invalid zipcode
				throw new IllegalArgumentException("This is an invalid zipcode. It must be a 5 digit number corresponding to a U.S. zipcode");
			}

			Pair<Catch.Weather, String> p = new Pair<Catch.Weather, String>(apiToEnum.get(code), city);
			return p;

		}
		catch(URISyntaxException e) {
			e.printStackTrace();
			System.exit(1);	
		}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}

	/**
	 * This method is purely for testing purposes. It gives the Catch.Weather enum value that corresponds to the 
	 * given weather conditon code.
	 *
	 * @param code the weather condition code to search the table for
	 * @return a Catch.Weather enum value from the hashtable that corresponds to the given code
	 */
	public Catch.Weather checkHashtableValue(int code) {
		return apiToEnum.get(code);
	}

}
