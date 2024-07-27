package com.tylerspaeth;

import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * The class provides all of the necessary functionality to interact with
 * WeatherAPI.
 */
public class WeatherAPI {

	//TODO right now the data that is being returned from the httprequest is being
	//limited from my account on the weatherapi website. Reducing the amount
	//of data transfer since we only need a small amount of data
	
	//TODO add a method to return the location where the zipcode is for so that 
	//it can be added to the popup recomendation
	

	private final String APIKEY =	"786761e9541f479a9d8175515241606";	
	private String baseURL = "http://api.weatherapi.com/v1";
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

		// Prints out the code to enum combinations
		//System.out.println(apiToEnum); 
	}

	/**
	 * This method gets the forcasted weather condition in a given zipcode a certain number 
	 * of days from now (0 is current day). As of the current version of this program, the
	 * maximum number of days into the future that works is 2 days.
	 *
	 * @param zipCode the 5 digit zipcode.
	 * @param daysFromNow the number of days from the current day that the weather should be 
	 * checked. 0 means the current day.
	 * @return a Backend.Weather enum corresponding to the expected weather conditions
	 */
	public Catch.Weather getWeather(int zipCode, int daysFromNow) throws IllegalArgumentException {
		//TODO change the way that we are returning in this method

		// Verify that a valid number of days was given
		if(daysFromNow > 2 || daysFromNow < 0)
			throw new IllegalArgumentException("daysFromNow must be in the range of 0-2");

		// TODO this is not a perfect way to verify.
		// Verify that the zipcode that was given was at least the right size
		if(zipCode > 99999 || zipCode < 0)
			throw new IllegalArgumentException("Invalid zipCode, must be a 5 digit number");

		// TODO do not use try-catch like this
		try {
			// By default this is a get request
			// // TODO should switch this to using .headers instead of hardcoded
			HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(new URI(baseURL + "/forecast.json?key=" + APIKEY + "&q=" + zipCode + "&days=3&aqi=no&alerts=no"))
				.build();

			HttpClient httpClient = HttpClient.newHttpClient();

			HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

			int code = 0;

			try {
				// Retrieve the condition corresponding to a Weather enum value in the apiToEnum HashTable
				code = new JSONObject(getResponse.body())
					.getJSONObject("forecast")
					.getJSONArray("forecastday")
					.getJSONObject(daysFromNow)
					.getJSONObject("day")
					.getJSONObject("condition")
					.getInt("code");
			}
			catch(JSONException e) {
				// If there is a json exception, that it was because it was an invalid zipcode
				throw new IllegalArgumentException("This is an invalid zipcode. It must be a 5 digit number corresponding to a U.S. zipcode");
			}
			
			return apiToEnum.get(code);

		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);	
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
