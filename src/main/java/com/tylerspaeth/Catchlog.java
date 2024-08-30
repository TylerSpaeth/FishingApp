package com.tylerspaeth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * This class provides the functionality of a Catchlog object that connects to a SQLite database. 
 * Funcionalities include but are not limited to: creating, reading, and deleting from tables 
 * as well as creating the tables and and database iteslf.
 *
 * @author Tyler Spaeth
 * @see com.tylerspaeth.DeletableCatchlog
 */
public class Catchlog {

	private static final String databaseName = "catchlog.db"; 
	
	/**
	 * The name of the table as it is sorted in the database
	 */
	protected final String tableName;

	private static Connection c = null; // The connection to the database
																			
	// TODO potentially change db naming so that the additional strings are not needed
	
	/**	
	 * This enum represents different ways that the Catchlog can be filtered.
	 */
	public enum Filter {
		DEFAULT("default", "Default"),
		FLYTYPE("flyType", "Fly Type"),
		FLYSIZE("flySize", "Fly Size"),
		WATERCONDITIONS("water", "Water Conditions"),
		WEATHERCONDITIONS("weather", "Weather Conditions"),
		LOCATION("location", "Location");

		private String str; // The string representation to display to user
		private String dbStr; // The column name in the database
		
		/**
		 * This returns the appropriately capitalized and spaced representation 
		 * of the enum.
		 *
		 * @return a properly capitalized and spaced string representation
		 */
		public String getStr() {
			return this.str;
		}

		/**
		 * This returns the string that corresponds to the columns in the database tables.
		 *
		 * @return the string corresponding to a column in the database table
		 */
		public String getDbStr() {
			return this.dbStr;
		}

		/**
		 * This constructor sets both the formatted and database strings to null if 
		 *  no strings are given.
		 */
		private Filter() {
			this.str = null;
			this.dbStr = null;
		}

		/**
		 * This construtor sets the formatted and database string to the values that are
		 * passed.
		 *
		 * @param dbStr the string the corresponds to a database column
		 * @param str the string that is formatted for display to user
		 */
		private Filter(String dbStr, String str) {
			this.dbStr = dbStr;
			this.str = str;
		}

		/**
		 * This method converts a given string value into a Filter value.
		 *
		 * @param str the string that is to be converted into a Filter value.
		 * @return a corresponding Filter value if the given string is the formatted 
		 * or database string for a Filter constant. If the string does not match to any 
		 * Filter, then null will be returned.
		 */
		public static Filter strValueOf(String str) {
			if(str == null) return null;
			for(Filter filter : Filter.values()) {
				if(str.equals(filter.getStr()) || str.equals(filter.getDbStr())) {
					return filter;
				}
			}
			return null;
		}
	}

	/**
	 * This method is the constructor for a Catchlog object. It initiates a
	 * connection to the backend if there is not already an existing one,
	 * sets the tableName and creates a table for the tableName. 
	 *
	 * @param tableName the name for the table that corresponds to this Catchlog
	 * @throws NullPointerException if the given tableName is null
	 * @throws IllegalArgumentException if the tableName is blank
	 */
	public Catchlog(String tableName) throws NullPointerException, IllegalArgumentException {

		// If a connection to the database has not been initialized already, do so
		if(c == null) {
			initDB();
		}

		// Check to see if the tableName is null
		if(tableName == null) {
			throw new NullPointerException("tableName must not be null.");
		}

		// Check to see if the tableName is blank
		if(tableName.trim().equals("")) {
			throw new IllegalArgumentException("tableName must not be blank");
		}

		this.tableName = tableName;
		createTable();
	}

	/**
	 * This method initializes the connection to the database.
	 */
	private static void initDB() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + databaseName); 
		}
		catch(Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * This method creates a table in the database for the catches to be stored in.
	 */
	private void createTable() {
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
	 * @param flyType the type of fly that was used for the catch
	 * @param flySize the size of fly that was used for the catch
	 * @param waterConditions what the conditons of the water were like when the catch occured
	 * @param weatherConditions what the conditions of the weather were like when the catch occured 
	 * @param location the location in a body of water where the catch occured
	 * @param timeOfCatch the time that the catch occured
	 *
	 * @throws IllegalArgumentException if an empty string is given for the flyType or
	 * if the flySize is negative
	 * @throws NullPointerException if flyType, waterConditions, weatherConditions, location
	 * or timeOfCatch is null
	 */
	public void insertToDB(String flyType, int flySize, Catch.Water waterConditions, Catch.Weather weatherConditions,
			Catch.Location location, Timestamp timeOfCatch) throws IllegalArgumentException, NullPointerException {
		try {
			
			// This is used to check that all the values are valid. The constructor will throw an error if not
			// which will be propagated.
			Catch cat = new Catch(1, flyType, flySize, waterConditions, weatherConditions, location, timeOfCatch);

			String sql = "INSERT INTO " + tableName + "(flyType, flySize, water, weather, location, timeOfCatch) " +
									 "VALUES ('" + flyType + "', '" + flySize + "', '" + waterConditions + "', '"
									 + weatherConditions + "', '" + location + "', ?);";
			
			// Prepared statement allows for intersion of datatypes like a timestamp where ? is.
			PreparedStatement state = c.prepareStatement(sql);
			state.setTimestamp(1, timeOfCatch);
			state.executeUpdate();
			state.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * This method retreives all of the catches from the database and puts them into an ArrayList which is then returned.
	 * 
	 *
	 * @param sortBy the order in which the catches should be sorted
	 * @param asc whether or not the order should be ascending or descending
	 * @return a list of all catches that were in the database
	 * @throws NullPointerException if sortBy is null
	 */
	public ArrayList<Catch> getAllCatches(Filter sortBy, boolean asc) throws NullPointerException {

		if(sortBy == null) throw new NullPointerException("sortBy can not be null");

		ArrayList<Catch> toReturn = new ArrayList<Catch>();	
		ResultSet result = null;
		try {
			Statement state = c.createStatement();
			String sql = "";
			if(sortBy != Filter.DEFAULT) {
				sql += "SELECT rowid, * FROM " + tableName + " ORDER BY " + sortBy.getDbStr() + " COLLATE NOCASE ";
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
			e.printStackTrace();
			System.exit(1);
		}
		return toReturn;
	}

	/**
	 * This method removes a given catch from the database.
	 *
	 * @param rowid the rowid of the catch to be removed from the database 
	 */
	public void removeFromDB(long rowid) {
		try {
			Statement state = c.createStatement();
			String sql = "DELETE FROM " + tableName + " WHERE rowid= " + rowid + ";"; 
			state.executeUpdate(sql);
			state.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


	/**
	 * This method finds the most common occurence of a given filter and returns 
	 * it as a string. For example if you give it Filter.FLYTYPE, it will give 
	 * you the most common fly type in the database.
	 *
	 * @param filter an enum value corresponding what most common category to search for. 
	 * DEFAULT is not a valid fly type.
	 * @return a string the represents the most common occurence of the given filter
	 * @throws IllegalArgumentException if the given filter is DEFAULT
	 * @throws IllegalStateException if the log is empty
	 * @throws NullPointerException if filter is null
	 */
	public String getMostCommon(Filter filter) throws IllegalArgumentException, 
				 IllegalStateException, NullPointerException {

		if(filter == null) {
			throw new NullPointerException("filter can not be null");
		}

		if(this.getAllCatches(Filter.DEFAULT, true).size() == 0) {
			throw new IllegalStateException("There must be at least one catch in the log");
		}

		if(filter == Filter.DEFAULT) {
			throw new IllegalArgumentException("DEFAULT is not a valid Filter for this method");
		}
		
		String ret = "";
		try {
			Statement state = c.createStatement();
			ResultSet result;
			String sql = "SELECT " + filter.getDbStr() + ", " +
									 "COUNT(" + filter.getDbStr() + ") AS freq " +
									 "FROM " + tableName + " " + 
									 "GROUP BY " + filter.getDbStr() + " " +
									 "ORDER BY freq DESC;";
			result = state.executeQuery(sql);
			result.next();
			ret = result.getString(filter.getDbStr());
			state.close();
			result.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		switch(filter) {
			case WATERCONDITIONS: 
				return Catch.Water.valueOf(ret).getStr();
			case WEATHERCONDITIONS:
				return Catch.Weather.valueOf(ret).getStr();
			case LOCATION:
				return Catch.Location.valueOf(ret).getStr();
			default:
				return ret;
		}
	}
	
	/**
	 * This method returns the appropriate connection object for use in
	 * the DetachableCatchlog. Using this method any other way is not
	 * reccomended.
	 *
	 * @return the connection to the sql database
	 */
	protected static Connection getConnection() {
		return c;
	}

	// TODO add more methods like this for other Catch enums
	/**
	 * This method creates a DeletableCatchlog representing subsection
	 * of this Catchlog based on a certain Weather Condition. Does so by
	 * creating a subtable in the database
	 *
	 * @param weather the weather condition that all the 
	 * catches in the new DeletableCatchlog should share.
	 * @return a Deletable catchlog that corresponds to this subtable
	 * @throws NullPointerException if weather is null
	 */
	public DeletableCatchlog getSubtable(Catch.Weather weather) throws NullPointerException {

		if(weather == null) {
			throw new NullPointerException("weather should not be null");
		}

		try {
			Statement state = c.createStatement();
			String sql = "CREATE TABLE " + tableName + "wea " + 
									 "AS SELECT * FROM " + tableName + 
									 " WHERE weather= \"" + weather + "\";";
			state.executeUpdate(sql);
			state.close();
			// Create the return object passing the name of the table that was
			// just created in the database;
			DeletableCatchlog ret = new DeletableCatchlog(tableName+"wea");	
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null; // This will never be reached
	}

}
