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

public class FXController implements Initializable {

	private Catchlog catchlog;
	private WeatherAPI weatherapi;

	@FXML private ChoiceBox sortByChoiceBox;

	@FXML private ListView<Catch> myListView;

	@FXML private TextField zipcodeField;// TODO implement this

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

	// TODO streamline these
	private Catchlog.Filter currentSorting = Catchlog.Filter.DEFAULT;
	private boolean currentAsc = true;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		catchlog = new Catchlog("CATCHLOG");
		weatherapi = new WeatherAPI();

		// Disable the popup by default
		disablePane(); 

		// Update the list with the Catches in the database
		setList(currentSorting, true);

		// Fill the ChoiceBoxes with the appropriate values

		Catch.Water[] waters = Catch.Water.class.getEnumConstants();
		for(Catch.Water water : waters) {
			waterConditionsChoiceBox.getItems().add(water.getStr());
		}

		Catch.Weather[] weathers = Catch.Weather.class.getEnumConstants();
		for(Catch.Weather weather : weathers) {
			weatherConditionsChoiceBox.getItems().add(weather.getStr());
		}

		Catch.Location[] locations = Catch.Location.class.getEnumConstants();
		for(Catch.Location location : locations) {
			locationChoiceBox.getItems().add(location.getStr());
		}

		String[] times = new String[24];
		for(int i = 0; i < 24; i++) {
			times[i] = i + ":00";
			if(i < 10) {
				times[i] = "0" + times[i];
			}
		}
		timeChoiceBox.getItems().addAll(times);

		Catchlog.Filter[] filters = Catchlog.Filter.class.getEnumConstants();
		for(Catchlog.Filter filter : filters) {
			sortByChoiceBox.getItems().add(filter.getStr());
		}
		//sortByChoiceBox.getItems().addAll(filters);
		sortByChoiceBox.setValue(Catchlog.Filter.DEFAULT.getStr());
		// TODO this may be inefficient, should verify this is correct
		// observable, oldValue, and newValue are values that come from the addListener method
		sortByChoiceBox.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
			if(newValue != null && newValue != oldValue) {
				myListView.getItems().clear();	
				setList(Catchlog.Filter.strValueOf((String)newValue), currentAsc);
				currentSorting = Catchlog.Filter.strValueOf((String)newValue); // TODO streamline this
			}
		});
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

	//TODO get it setup so that it can be for more than just the current day
	/**
	 * This method checks the for the user input of a zipcode and if a valid zipcode is given, 
	 * a fly and a flysize to used based on the users previous catches and the weather
	 * at the location on a given day.
	 */
	@FXML
	private void recommend() {
		int zipcode = Integer.parseInt(zipcodeField.getText());
		Catch.Weather daysWeather = null;
		try {
			daysWeather = weatherapi.getWeather(zipcode, 0);
		}
		catch(IllegalArgumentException e) {
			// IF the user does not enter valid info just do nothing
			return;
		}
		DeletableCatchlog subtable = catchlog.getSubtable(daysWeather);
		String flyTypeReccomendation = subtable.getMostCommon(Catchlog.Filter.FLYTYPE);
		String flySizeReccomendation = subtable.getMostCommon(Catchlog.Filter.FLYSIZE);

		paneMainLabel.setText("Because the weather is going to be " + daysWeather.getStr() +
													" try using:");
		paneResultLabel.setText("Size " + flySizeReccomendation + " " + flyTypeReccomendation);
		enablePane();
		subtable.deleteCatchlog();
	}
		
}
