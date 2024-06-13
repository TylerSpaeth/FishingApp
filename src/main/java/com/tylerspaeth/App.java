package com.tylerspaeth;

/**
 * This class contains the main method that is required to run the command line
 * version of this program.
 *
 */
public class App {// extends Application {

	public static void main(String[] args) {

		// Initialization of the database
		Backend.initDB();
		Backend.createTable();
		
		// For using the cmd line gui
		TestUI test = new TestUI();
		test.run();
		
		
		// Launch the javafx application
		//launch(args);
	}	

}
