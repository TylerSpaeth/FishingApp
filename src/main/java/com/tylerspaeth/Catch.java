package com.tylerspaeth;

import java.sql.Timestamp;

/**
 * This class stores all of the information about a catch that a user enters
 *
 * @author Tyler Spaeth
 */
public class Catch {
	
 	private long rowid; // The rowid in the database 
	
	// Fly used
	private String flyType;
	private int flySize;
	
	/**
	 * This enum represents the different water conditions that a catch could occur in.
	 */
	public enum Water {
		STAINED("Stained"),
		CLEAR("Clear"),
		MUDDY("Muddy");

		private String str;

		/**
		 * This method returns a properly formatted string representation of the enum.
		 *
		 * @return a formatted string representation of the enum.
		 */
		public String getStr() {
			return this.str;
		}

		/**
		 * This construtor sets the string representation of the enum to null
		 */
		private Water() {
			this.str = null;
		}

		/**
		 * This constructor sets the string represenation of the enum to the given string
		 * 
		 * @param str the string representation of the enum
		 */
		private Water(String str) {
			this.str = str;
		}

		/**
		 * This methods convers a string value into a Water value.
		 *
		 * @param str the string that is to be converted into a Water value.
		 * @return a corresponding Water value if the given string is the same as 
		 * the corresponding formatted string for a Water constant. If the string
		 * does not match to any Water, then null will be returned.
		 */
		public static Water strValueOf(String str) {
			if(str == null) return null;
			for(Water water : Water.values()) {
				if(str.equals(water.getStr())) {
					return water;
				}
			}
			return null;
		}
	};
	private Water waterConditions;
	
	/**
	 * This enum represents the different weather conditions that a catch could occur in.
	 */
	public enum Weather {
		SUNNY("Sunny"),
		PARTLY_CLOUDY("Partly Cloudy"),
		CLOUDY("Cloudy"),
		LIGHT_RAIN("Light Rain"),
		RAIN("Rain"),
		HEAVY_RAIN("Heavy Rain"),
		LIGHT_SNOW("Light Snow"),
		SNOW("Snow"),
		HEAVY_SNOW("Heavy Snow"),
		SEVERE_WEATHER("Severe Weather");

		private String str;

		/**
		 * This method returns a properly formatted string representation of the enum.
		 *
		 * @return a formatted string representation of the enum.
		 */
		public String getStr() {
			return this.str;
		}
		
		/**
		 * This construtor sets the string representation of the enum to null
		 */
		private Weather() {
			 this.str = null;
		}

		/**
		 * This constructor sets the string represenation of the enum to the given string
		 * 
		 * @param str the string representation of the enum
		 */
		private Weather(String str) {
			this.str = str;
		}

		/**
		 * This methods convers a string value into a Weather value.
		 *
		 * @param str the string that is to be converted into a Weather value.
		 * @return a corresponding Weather value if the given string is the same as 
		 * the corresponding formatted string for a Weather constant. If the string
		 * does not match to any Weather, then null will be returned.
		 */
		public static Weather strValueOf(String str) {
			if(str == null) return null;
			for(Weather weather : Weather.values()) {
				if(str.equals(weather.getStr())) {
					return weather;
				}
			}
			return null;
		}
	};
	private Weather weatherConditions;

	/**
	 * This enum represents the different water locations that a catch could occur in.
	 */
	public enum Location {
		RIFFLE("Riffle"),
		POOL("Pool"),
		OVERHANG("Overhang"),
		OTHER("Other");
		
		private String str;

		/**
		 * This method returns a properly formatted string representation of the enum.
		 *
		 * @return a formatted string representation of the enum.
		 */
		public String getStr() {
			return this.str;
		}
		
		/**
		 * This construtor sets the string representation of the enum to null
		 */
		private Location() {
			this.str = null;
		}

		/**
		 * This constructor sets the string represenation of the enum to the given string
		 * 
		 * @param str the string representation of the enum
		 */
		private Location(String str) {
			this.str = str;
		}

		/**
		 * This methods convers a string value into a Location value.
		 *
		 * @param str the string that is to be converted into a Location value.
		 * @return a corresponding Location value if the given string is the same as 
		 * the corresponding formatted string for a Location constant. If the string
		 * does not match to any Location, then null will be returned.
		 */
		public static Location strValueOf(String str) {
			if(str == null) return null;
			for(Location location : Location.values()) {
				if(str.equals(location.getStr())) {
					return location;
				}
			}
			return null;
		}
	}
	private Location location;

	private Timestamp timeOfCatch; // Date and time of the catch

	/**
	 * This method constructs a Catch object.
	 *
	 * @param rowid the rowid number in the database
	 * @param flyType the type of fly that was used
	 * @param flySize the size of fly that was used
	 * @param waterConditions what the water conditions were when the fish was caught
	 * @param weatherConditions what the weather conditions were when the fish was caught
	 * @param location what type of water the fish was caught in
	 * @param timeOfCatch the date and time in which the fish was caught
	 *
	 * @throws IllegalArgumentException if an empty string is given for the flyType or
	 * if the flySize is negative
	 * @throws NullPointerException if flyType, waterConditions, weatherConditions, location
	 * or timeOfCatch is null
	 */
	public Catch(long rowid, String flyType, int flySize, Water waterConditions, 
			Weather weatherConditions, Location location, Timestamp timeOfCatch) 
			throws IllegalArgumentException, NullPointerException {
		
		if(flyType == null) {
			throw new NullPointerException("flyType can not be null");
		}

		if(flyType.trim().equals("")) {
			throw new IllegalArgumentException("flyType must not be an empty string");
		}

		if(flySize < 0) {
			throw new IllegalArgumentException("flySize must not be negative");
		}
		
		if(waterConditions == null) {
			throw new NullPointerException("waterConditions can not be null");
		}

		if(weatherConditions == null) {
			throw new NullPointerException("weatherConditions can not be null");
		}

		if(location == null) {
			throw new NullPointerException("location can not be null");
		}

		if(timeOfCatch == null) {
			throw new NullPointerException("timeOfCatch can not be null");
		}

		this.rowid = rowid;
		this.flyType = flyType;
		this.flySize = flySize;
		this.waterConditions = waterConditions;
		this.weatherConditions = weatherConditions;
		this.location = location;
		this.timeOfCatch = timeOfCatch;
	}

	/**
	 * This method returns the string representation of a Catch object.
	 *
	 * Size {flySize} {flyType} 
	 * - Water Conditions: {waterConditions String}
	 * - Weather Conditions: {weatherConditions String} 
	 * - Water Location: {location String} 
	 * - Time of Catch: {timeOfCatch}
	 *
	 * @return the string representation of a Catch object
	 */
	@Override
	public String toString() {
		return "Size "+ this.flySize + " " + this.flyType +
					 " - Water Conditions: " + this.waterConditions.getStr() +
					 " - Weather Conditions: " + this.weatherConditions.getStr() +
					 " - Water Location: " + this.location.getStr() + 
					 " - Time of Catch: " + this.timeOfCatch;
	}

	/////////////////////////////////Getter Methods///////////////////////////////

	/**
	 * Getter for the rowid variable
	 *
	 * @return the rowid associated with this Catch object
	 */
	public long getRowId() {
		return this.rowid;
	}
		
	/**
	 * Getter for the flyType variable
	 *
	 * @return the flyType associated with this Catch object
	 */
	public String getFlyType() {
		return this.flyType;
	}

	/**
	 * Getter for the flySize variable
	 *
	 * @return the flySize associated with this Catch object
	 */
	public int getFlySize() {
		return this.flySize;
	}

	/**
	 * Getter for the waterConditions variable
	 *
	 * @return the waterConditions associated with this Catch object
	 */
	public Water getWaterConditions() {
		return this.waterConditions;
	}

	/**
	 * Getter for the weatherConditions variable
	 *
	 * @return the weatherConditions associated with this Catch object
	 */
	public Weather getWeatherConditions() {
		return this.weatherConditions;
	}

	/**
	 * Getter for the location variable
	 *
	 * @return the location associated with this Catch object
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * Getter for the timeOfCatch variable
	 *
	 * @return the timeOfCatch associated with this Catch object
	 */
	public Timestamp getTimeOfCatch() {
		return this.timeOfCatch;
	}
}
