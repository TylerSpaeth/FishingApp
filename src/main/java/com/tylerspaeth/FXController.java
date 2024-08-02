package com.tylerspaeth;

import java.lang.Math;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import org.javatuples.Pair;

/**
 * This method provides the interactive functionality for the FXML GUI. 
 *
 * @author Tyler Spaeth
 */
public class FXController implements Initializable {

	private Catchlog catchlog;
	private WeatherAPI weatherapi;

	@FXML private ChoiceBox sortByChoiceBox;

	@FXML private ListView<Catch> myListView;

	@FXML private TextField zipcodeField;

	// Insertion form fields
	@FXML private TextField flyTypeField;
	@FXML private TextField flySizeField;
	@FXML private ChoiceBox waterConditionsChoiceBox;
	@FXML private ChoiceBox weatherConditionsChoiceBox;
	@FXML private ChoiceBox locationChoiceBox;
	@FXML private DatePicker date;
	@FXML private ChoiceBox timeChoiceBox;

	// Popup Pane fields
	@FXML private Label paneResultLabel;
	@FXML private Label paneMainLabel;
	@FXML private Pane popupPane;

	// Sorting order variables
	private Catchlog.Filter currentSorting = Catchlog.Filter.DEFAULT;
	private boolean currentAsc = true;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		catchlog = new Catchlog("CATCHLOG");
		weatherapi = new WeatherAPI();

		// Disable the popup by default
		disablePane(); 

		// Update the list with the Catches in the database
		setList(currentSorting, true);

		// Fill the ChoiceBoxes with the appropriate values

		// Water
		Catch.Water[] waters = Catch.Water.class.getEnumConstants();
		for(Catch.Water water : waters) {
			waterConditionsChoiceBox.getItems().add(water.getStr());
		}

		// Weather
		Catch.Weather[] weathers = Catch.Weather.class.getEnumConstants();
		for(Catch.Weather weather : weathers) {
			weatherConditionsChoiceBox.getItems().add(weather.getStr());
		}

		// Location
		Catch.Location[] locations = Catch.Location.class.getEnumConstants();
		for(Catch.Location location : locations) {
			locationChoiceBox.getItems().add(location.getStr());
		}

		// Time of day
		String[] times = new String[24];
		for(int i = 0; i < 24; i++) {
			times[i] = i + ":00";
			if(i < 10) {
				times[i] = "0" + times[i];
			}
		}
		timeChoiceBox.getItems().addAll(times);

		// Sorting order
		Catchlog.Filter[] filters = Catchlog.Filter.class.getEnumConstants();
		for(Catchlog.Filter filter : filters) {
			if(filter == Catchlog.Filter.DEFAULT) {
				sortByChoiceBox.getItems().add(filter.getStr());
			}
			else {
				sortByChoiceBox.getItems().add(filter.getStr() + " Ascending");
				sortByChoiceBox.getItems().add(filter.getStr() + " Descending");
			}
		}

		// Set the sorting order to default
		sortByChoiceBox.setValue(Catchlog.Filter.DEFAULT.getStr());

		// Add a listener to the sortByChoiceBox, so that when a user changes their sorting selection, the 
		// observable, oldValue, and newValue are values that come from the addListener method
		sortByChoiceBox.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
			// Make sure that a value is selected and that the value has changed
			if(newValue == null || newValue == oldValue) {
				return;
			}

			// If the value is for the default sorting order, then further breakdown is not
			// needed and it the sorting order values can be set
			if(Catchlog.Filter.strValueOf((String) newValue) == Catchlog.Filter.DEFAULT) {
				currentSorting = Catchlog.Filter.DEFAULT;
				currentAsc = true;
			}
			// If the value something that can be ascending or descending, utilize the
			// changeSorting method that will break down the individual pieces.
			else {
				changeSorting((String)newValue);
			}

			// Reset the list view appropriately for the new sorting order
			myListView.getItems().clear();
			setList(currentSorting, currentAsc);
		});
	}

	/**
	 * This method changes the currentSorting and currentAsc values appropriately based on 
	 * the string that is passed to it. This should be used on any sorting order strings that
	 * have ascending or descending appended to them. It is not needed for the default sorting order.
	 *
	 * @param sortingString the string containing the information about the appropriate sorting order
	 */
	private void changeSorting(String sortingString) {
		// Seperate all the words with spaces between
		String[] seperated = ((String)sortingString).split(" ");
			
		// The last string in the array will be the one that says if it is ascending or descending
		String ascendingString = seperated[seperated.length - 1];
		// Set the current ascending value to be this value that has been described
		if(ascendingString.equals("Ascending")) {
			currentAsc = true;
		}
		else {
			currentAsc = false;
		}

		// Join the remaining Strings in the array together with spaces between
		String filterString = "";
		for(int i = 0; i < seperated.length - 1; i++) {
			filterString += seperated[i];	
			if(i < seperated.length-2) {
				filterString += " ";
			}
		}	
		// Get the Filter value from this joined string
		Catchlog.Filter asFilter = Catchlog.Filter.strValueOf(filterString);
		// Set the current sorting criteria
		currentSorting = asFilter;
	}

	/**
	 * This method adds the string representation of each Catch to the ListView
	 * in the gui
	 */
	private void setList(Catchlog.Filter sortBy, boolean asc) {
		ArrayList<Catch> cArr = catchlog.getAllCatches(sortBy, asc);
		myListView.getItems().addAll(cArr);
	}

	/**
	 * This method checks all of the insert fields, creates a new Catch object,
	 * inserts the object into the database, refreshes the ListView, and then
	 * resets the insert fields.
	 */
	@FXML
	private void insert() {
		// Check if the insertion is valid	
		try {
			String flyType = flyTypeField.getText();
			int flySize = Integer.parseInt(flySizeField.getText());
			Catch.Water waterConditions = Catch.Water.strValueOf((String)waterConditionsChoiceBox.getValue());
			Catch.Weather weatherConditions = Catch.Weather.strValueOf((String)weatherConditionsChoiceBox.getValue());
			Catch.Location location = Catch.Location.strValueOf((String)locationChoiceBox.getValue());
			LocalDate tempDate = date.getValue(); 
			if(tempDate == null) throw new NullPointerException(); // Check if a valid date has been given
			int hourOfCatch = Integer.parseInt(String.valueOf(timeChoiceBox.getValue()).substring(0,2));
			Timestamp dateOfCatch = new Timestamp(tempDate.getYear()-1900, tempDate.getMonthValue()-1, tempDate.getDayOfMonth(), hourOfCatch, 0, 0, 0);
			catchlog.insertToDB(flyType, flySize, waterConditions, weatherConditions, location, dateOfCatch);
			myListView.getItems().clear();	
			setList(currentSorting, currentAsc);
			resetInsertFields();
		}
		catch(NumberFormatException e) {
			// If the fly size is not a valid integer
		}
		catch(NullPointerException e) {
			// If a valid date is not given
		}
			
	}

	/**
	 * This method resets all of the fields for a new insertion.
	 */
	private void resetInsertFields() {
		flyTypeField.clear();
		flySizeField.clear();
		waterConditionsChoiceBox.setValue(null);
		weatherConditionsChoiceBox.setValue(null);
		locationChoiceBox.setValue(null);
		timeChoiceBox.setValue(null);
		date.setValue(null);
	}

	/**
	 * This method deletes the item in the ListView that is currently selected and then 
	 * refreshes the ListView.
	 */
	@FXML
	private void deleteCurrent() {
		try {
			catchlog.removeFromDB(myListView.getSelectionModel().getSelectedItem().getRowId());
			myListView.getItems().clear();
			setList(currentSorting, currentAsc);
		}
		catch(NullPointerException e) {
			// Do nothing because that just means that nothing in the list has been selected so 
			// nothing needs to be done.
		}
	}

	/**
	 * This method sets the opacity of the popup pane to be one so
	 * that it can be seen.
	 */
	private void enablePane() {
		for(Node node : popupPane.getChildren()) {
			node.setOpacity(1);
		}
	}

	/**
	 * This method sets the opacity of the popup pane to be zero
	 * so it is not seen.
	 */
	@FXML
	private void disablePane() {
		for(Node node : popupPane.getChildren()) {
			node.setOpacity(0);
		}
	}

	/**
	 * This method creates the popup telling the user what their most successful
	 * fly type has been.
	 */
	@FXML
	private void bestFly() {
		paneMainLabel.setText("Your Most Successful Fly Has Been: ");
		paneResultLabel.setText(catchlog.getMostCommon(Catchlog.Filter.FLYTYPE));
		enablePane();
	}

	/**
	 * This method creates the popup telling the user what their most successful
	 * water conditon has been.
	 */
	@FXML
	private void bestWater() {
		paneMainLabel.setText("Your Most Successful Water Condition Has Been: ");
		paneResultLabel.setText(catchlog.getMostCommon(Catchlog.Filter.WATERCONDITIONS));
		enablePane();
	}

	/**
	 * This method creates the popup telling the user what their most successful
	 * weather condition has been.
	 */
	@FXML
	private void bestWeather() {
		paneMainLabel.setText("Your Most Successful Weather Condition Has Been: ");
		paneResultLabel.setText(catchlog.getMostCommon(Catchlog.Filter.WEATHERCONDITIONS));
		enablePane();
	}

	/**
	 * This method creates the popup telling the user what their most successful
	 * water location has been.
	 */
	@FXML
	private void bestLocation() {
		paneMainLabel.setText("Your Most Successful Water Location Has Been: ");
		paneResultLabel.setText(catchlog.getMostCommon(Catchlog.Filter.LOCATION));
		enablePane();
	}

	/**
	 * This method creates the popup telling the user what their most 
	 * successful time of day for fishing has been. It essentially tells
	 * them what time range 68% (1SD each direction from the mean) their 
	 * catches occur. It should be noted that this assumes the data will 
	 * somewhat normally distributed, if it is not, then this time range
	 * is much less significant.
	 */
	@FXML
	private void bestTOD() {
		paneMainLabel.setText("Your Most Successful Time of Day Has Been: ");
		
		// Get all of the catches in the database
		ArrayList<Catch> catches = catchlog.getAllCatches(Catchlog.Filter.DEFAULT, true);	
		
		// This array will store the hours the each catch was caught at
		float[] times = new float[catches.size()];

		// This will be the sum of all the hours, which will allow the mean
		// to be calculated
		float sum = 0;

		int index = 0;

		// For each catch add its hour to the sum and add it to the array of times
		for(Catch c : catches) {
			float hour = (float) c.getTimeOfCatch().toLocalDateTime().getHour();
			sum += hour;
			times[index] = hour;
			index++;
		}

	 	float mean = sum / catches.size();
		float sd = sdCalc(mean, times);
		
		int lowerBound = (int) (mean-sd);
		int upperBound = (int) (mean+sd);

		paneResultLabel.setText(lowerBound + ":00 - " + upperBound + ":00");
		enablePane();
	}

	/**
	 * This method calculates that standard deviation of an array of floats, given
	 * said array along with its mean. 
	 *
	 * @param mean a float the represents the mean of the array of integers
	 * @param vals an array of floats of which the standard deviation will be found
	 * @return a float value representing the standard deviation of the array
	 */
	private float sdCalc(float mean, float[] vals) {
		float sumOfSquares = 0;
		for(int i = 0; i < vals.length; i++) {
			// Subtract the mean from each value
			vals[i] = vals[i] - mean;
			// Square each deviation
			vals[i] = vals[i] * vals[i];
			// Compute the sum of all squared deviations
			sumOfSquares += vals[i];
		}

		float avgSquaredDev = sumOfSquares / vals.length;

		float sd = (float) Math.sqrt(avgSquaredDev);

		return sd;
	}

	/**
	 * This method checks the for the user input of a zipcode and if a valid zipcode is given, 
	 * a fly and a flysize to used based on the users previous catches and the weather
	 * at the location on a given day.
	 *
	 * Note that this currently only make reccomendations for the current day. In the 
	 * future, the functionality should be expanded to allow users to get reccomendations
	 * for days in the future where the forecast is available.
	 */
	@FXML
	private void recommend() {
		int zipcode = Integer.parseInt(zipcodeField.getText());

		// Call the weather api to get the weather condion and the city that the
		// zipcode corresponds to
		Catch.Weather daysWeather = null;
		String city = "";
		try {
			Pair<Catch.Weather, String> p = weatherapi.getWeather(zipcode, 0);
			daysWeather = p.getValue0();
			city = p.getValue1();
		}
		catch(IllegalArgumentException e) {
			// IF the user does not enter valid info just do nothing
			return;
		}

		// Create a subtable where all entries share the same weather condtions
		DeletableCatchlog subtable = catchlog.getSubtable(daysWeather);
		// Search that table for the most common flytype and flysize
		String flyTypeReccomendation = subtable.getMostCommon(Catchlog.Filter.FLYTYPE);
		String flySizeReccomendation = subtable.getMostCommon(Catchlog.Filter.FLYSIZE);

		paneMainLabel.setText("Because the weather is going to be " + daysWeather.getStr()
			       	+ " in " + city + " try using:");
		paneResultLabel.setText("Size " + flySizeReccomendation + " " + flyTypeReccomendation);
		enablePane();
		
		// Delete the table after use
		subtable.deleteCatchlog();
	}
		
}
