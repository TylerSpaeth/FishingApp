package com.tylerspaeth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * This class handles a variety of backend functionalities for the program including
 * database connection, sorting, and filtering. SQLite is utilized as the database.
 *
 * @author Tyler Spaeth
 */
public class Backend {

	private static final String databaseName = "test.db";
	private static final String tableName = "CATCHLOG";

	//TODO ensure this connection is closed upon shutdown of the program	
	private static Connection c = null; // The connection to the database
																			
	// TODO we should setup the db so that the naming is more consistent and 
	// constructors are not needed
	public enum Filter {
		DEFAULT,
		FLYTYPE("flyType"),
		FLYSIZE("flySize"),
		WATERCONDITIONS("water"),
		WEATHERCONDITIONS("weather"),
		LOCATION("location");

		private String str;
		
		public String getStr() {
			return this.str;
		}

		private Filter() {
			this.str = null;
		}

		private Filter(String str) {
			this.str = str;
		}
	}

	/**
	 * This method initializes the connection to the database.
	 */
	public static void initDB() {
		// TODO this needs to be understood better
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + databaseName); //TODO rename db
		}
		catch(Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * This method creates a table in the database for the catches to be stored in.
	 */
	public static void createTable() {
		try {
			Statement state = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS " + tableName + 
									" (" +
									"flyType TEXT NOT NULL," +
									"flySize INT NOT NULL," +
									"water TEXT NOT NULL," +
									"weather TEXT NOT NULL," +
									"location TEXT NOT NULL," +
									"timeOfCatch TIMESTAMP)";
			state.executeUpdate(sql);
			state.close();
		}
		catch(Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * This method inserts a new catch into the database.
	 *
	 * @param catch an object that represents all of the information that the user has given about the catch
	 */
	public static void insertToDB(String flyType, int flySize, Catch.Water waterConditions, Catch.Weather weatherConditions,
			Catch.Location location, Timestamp timeOfCatch) {
		try {
			String sql = "INSERT INTO " + tableName + "(flyType, flySize, water, weather, location, timeOfCatch) " +
									 "VALUES ('" + flyType + "', '" + flySize + "', '" + waterConditions + "', '"
									 + weatherConditions + "', '" + location + "', ?);";
			PreparedStatement state = c.prepareStatement(sql); // Prepared statement allows us to insert datatypes like a timestamp where ? is.
			state.setTimestamp(1, timeOfCatch);
			state.executeUpdate();
			state.close();
		}
		catch(Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * This method retreives all of the catches from the database and puts them into an ArrayList which is then returned.
	 *
	 * @return a list of all catches that were in the database
	 */
	public static ArrayList<Catch> getAllCatches(Filter sortBy, boolean asc) {
		ArrayList<Catch> toReturn = new ArrayList<Catch>();	
		ResultSet result = null;
		try {
			Statement state = c.createStatement();
			String sql = "";
			if(sortBy != Filter.DEFAULT) {
				sql += "SELECT rowid, * FROM " + tableName + " ORDER BY " + sortBy.getStr() + " ";
				if(asc) {
					sql += "ASC;";
				} 
				else {
					sql += "DESC;";
				}
			}
			else {
				sql += "SELECT rowid, * FROM " + tableName + ";";
			}
			result = state.executeQuery(sql);

			// Interate through the ResultSet put each result into the ArrayList
			while(result.next()) {

				// Get the values from the ResultSet
				long rowid = result.getLong(1);
				String flyType = result.getString("flyType");
				int flySize = result.getInt("flySize");
				// Using ENUM.valueof because they are stored in the database with the exact same format, just as a string
				Catch.Water waterConditions = Catch.Water.valueOf(result.getString("water"));
				Catch.Weather weatherConditions = Catch.Weather.valueOf(result.getString("weather"));
				Catch.Location location = Catch.Location.valueOf(result.getString("location"));
				Timestamp timeOfCatch = result.getTimestamp("timeOfCatch");

				// Create and insert the new Catch
				Catch temp = new Catch(rowid, flyType, flySize, waterConditions, weatherConditions, location, timeOfCatch);
				toReturn.add(temp);
			}
			
			state.close();
			result.close();
		}
		catch(Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return toReturn;
	}

	/**
	 * This method removes a given catch from the database.
	 *
	 * @param row the rowid of the catch to be removed from the databse 
	 */
	public static void removeFromDB(long rowid) {
		try {
			Statement state = c.createStatement();
			String sql = "DELETE FROM " + tableName + " WHERE rowid= " + rowid + ";"; 
			state.executeUpdate(sql);
			state.close();
		}
		catch(Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}


	/**
	 * This method finds the most common occurence of a given filter and returns 
	 * it as a string. For example if you give it Filter.FLYTYPE, it will give 
	 * you the most common fly type in the database.
	 *
	 * @param filter an enum value corresponding what most common category to search for
	 * @return a string the represents the most common occurence of the given filter
	 */
	public static String getMostCommon(Filter filter) {
		String ret = "";
		try {
			Statement state = c.createStatement();
			ResultSet result;
			String sql = "SELECT " + filter.getStr() + ", " +
									 "COUNT(" + filter.getStr() + ") AS freq " +
									 "FROM " + tableName + " " + 
									 "GROUP BY " + filter.getStr() + " " +
									 "ORDER BY freq DESC;";
			result = state.executeQuery(sql);
			result.next();
			ret = result.getString(filter.getStr());
			state.close();
			result.close();
		}
		catch(Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return ret;
	}
}
