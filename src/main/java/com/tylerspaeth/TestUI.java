package com.tylerspaeth;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class contains the functionality for a text based ui that is strictly
 * used for testing and prototype purposes.
 *
 * @author Tyler Spaeth
 */
public class TestUI {

	/**
	 * This method runs a TestUI application.
	 */
	public void run() {
		Scanner scanner = new Scanner(System.in);
		while(true) {
			mainMenu(scanner);
		}
	}
	
	
	/**
	 * This method prints out the main menu for the program and leads the user
	 * into different methods.
	 *
	 * @param scanner a scanner object to be used as the means of input for this method
	 */
	private void mainMenu(Scanner scanner) {
		System.out.println("--------------------------------------------------------------------------------\n"+
											 "Please select an option from those provided below.\n"+
											 "[V]iew all previous input\n"+
											 "[I]nsert a new catch\n" +
											 "View most successful [F]ly\n"+
											 "View most successful w[A]ter conditions\n"+
											 "View most successful w[E]ather conditions\n"+
											 "View most successful water [L]ocation\n"+ 
											 "[Q]uit");
		String input = scanner.nextLine();
		switch(input) {
			case("V"):
				showAll(scanner);
				break;
			case("I"):
				insert(scanner);
				break;
			case("F"):
				System.out.println("Most Successful fly: " + Backend.getMostCommon(Backend.Filter.FLYTYPE));
				break;
			case("A"):
				System.out.println("Most Successful water conditions: " + Backend.getMostCommon(Backend.Filter.WATERCONDITIONS));
				break;
			case("E"):
				System.out.println("Most Successful weather conditions: " + Backend.getMostCommon(Backend.Filter.WEATHERCONDITIONS));
				break;
			case("L"):
				System.out.println("Most Successful water location: " + Backend.getMostCommon(Backend.Filter.LOCATION));
				break;
			case("Q"):
				scanner.close(); // Close scanner here because this is really the only way that the program will ever close 
				System.exit(0);
			default:
				System.out.println("Invalid Input.");
				mainMenu(scanner);
		}
	}

	/**
	 * This method prints out all of catches that the Backend gives it and then allows the user to
	 * delete catches if they so choose.
	 *
	 * @param scanner a scanner object to be used as the means of input for this method
	 */
	private void showAll(Scanner scanner) {
		ArrayList<Catch> catches = Backend.getAllCatches(Backend.Filter.DEFAULT, true);
		int counter = 1;
		for(Catch each : catches) {
			System.out.println("--------------------------------------------------------------------------------\n"+
											   counter + ". " + each);
			counter++;
		}

		System.out.print("If you would like to delete a catch, enter the corresponding number.\n" +
											 "Otherwise enter anything else to continue: ");
		String input = scanner.nextLine();
		try{
			int index = Integer.parseInt(input);
			if(index > 0 && index < counter) {
				Backend.removeFromDB(catches.get(index-1).getRowId());
				showAll(scanner);
			}
		}
		catch(Exception e) {
		}
		
	}

	/**
	 * This method handles the insertion of a new catch into the database from this cmd line ui.
	 * It prompts the user for all the needed information to add the new addition.
	 *
	 * @param scanner a scanner object to be used as the means of input for this method
	 */
	private void insert(Scanner scanner) {

		// Variables that will be used to create the new addition
		String flyType = "";
		int flySize = 0;
		Catch.Water waterConditions = null;
		Catch.Weather weatherConditions = null;
		Catch.Location location = null;
		Timestamp timeOfCatch = null;

		// The do-while combined with try-catch ensures that the user keeps giving inputs until
		// they have given something that will at least enter into the database. Any other input
		// check is not done since this ui is mainly just for testing purposes.
		boolean go = false;
		do {
			try	{

				// Get fly type
				System.out.print("Enter the fly type: ");
				flyType = scanner.nextLine();

				// Get fly size
				System.out.print("Enter the fly size: ");
				flySize = Integer.parseInt(scanner.nextLine());

				String temp; // Used for the switch statements

				// Get the water conditons
				System.out.print("Select an option for the water conditions from the ones below.\n" +
													 "[S]tained, [C]lear, [M]uddy: ");
				temp = scanner.nextLine();
				switch(temp) {
					case "S":
						waterConditions = Catch.Water.STAINED;
						break;
					case "C":
						waterConditions = Catch.Water.CLEAR;
						break;
					case "M":
						waterConditions = Catch.Water.MUDDY;
						break;
					default:
						// Exception is thrown to indicate that bad input was given
						throw new Exception();
				}

				// Get the weather conditions
				System.out.print("Select an option for the weather conditions from the ones below.\n" +
													 "[S]unny, [O]vercast, [R]ainy: ");
				temp = scanner.nextLine();
				switch(temp) {
					case "S":
						weatherConditions = Catch.Weather.SUNNY;
						break;
					case "O":
						weatherConditions = Catch.Weather.OVERCAST;
						break;
					case "R":
						weatherConditions = Catch.Weather.RAINY;
						break;
					default:
						// Excpetion is thrown to indicate that bad input was given
						throw new Exception();
				}

				// Get the water location
				System.out.print("Select an option for the water location from the ones below.\n" +
													 "[R]iffle, [P]ool, [O]verhang, o[T]her: ");
				temp = scanner.nextLine();
				switch(temp) {
					case "R":
						location = Catch.Location.RIFFLE;
						break;
					case "P":
						location = Catch.Location.POOL;
						break;
					case "O":
						location = Catch.Location.OVERHANG;
						break;
					case "T":
						location = Catch.Location.OTHER;
						break;
					default:
						// Exception is thrown to indicate that bad input was given
						throw new Exception();
				}	

				// Get the information needed to create a timestamp
				System.out.print("Enter the year that the fish was caught: ");
				// Subtracting 1900 is necessary based on how the Timestamp constructor works. 
				// Without the subtraction the year is incorrect. The reasoning is the same
				// for the other adjustments to the input values.
				int year = Integer.parseInt(scanner.nextLine()) - 1900;  
				System.out.print("Enter the month that the fish was caught (3 is march): ");
				int month = Integer.parseInt(scanner.nextLine()) - 1;
				System.out.print("Enter the day that the fish was caught: ");
				int day = Integer.parseInt(scanner.nextLine());
				System.out.print("Enter the approximate hour that the fish was caught (1PM = 13): ");
				int hour = Integer.parseInt(scanner.nextLine());
				timeOfCatch = new Timestamp(year, month, day, hour, 0, 0, 0);

				go = true;
			}
			catch(Exception e) {
				System.out.println("Invalid input, try again.");
			}
		}
		while(!go);

		Backend.insertToDB(flyType, flySize, waterConditions, weatherConditions, location, timeOfCatch);
	}

}
