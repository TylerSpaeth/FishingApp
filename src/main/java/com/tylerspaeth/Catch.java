package com.tylerspaeth;

import java.sql.Timestamp;

/**
 * This class stores all of the information about a catch that a user enters
 * @author Tyler Spaeth
 */
public class Catch {
	
 	private long rowid; // The rowid in the database 
	
	// Fly used
	private String flyType;
	private int flySize;
	
	// Conditions
	public enum Water {
		STAINED("Stained"),
		CLEAR("Clear"),
		MUDDY("Muddy");

		private String str;

		public String getStr() {
			return this.str;
		}

		private Water() {
			this.str = null;
		}

		private Water(String str) {
			this.str = str;
		}

		public static Water strValueOf(String str) {
			for(Water water : Water.values()) {
				if(str.equals(water.getStr())) {
					return water;
				}
			}
			return null;
		}
	};
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

		public String getStr() {
			return this.str;
		}
		
		private Weather() {
			 this.str = null;
		}

		private Weather(String str) {
			this.str = str;
		}

		public static Weather strValueOf(String str) {
			for(Weather weather : Weather.values()) {
				if(str.equals(weather.getStr())) {
					return weather;
				}
			}
			return null;
		}
	};
	private Water waterConditions;
	private Weather weatherConditions;

	// Water Location
	enum Location {
		RIFFLE("Riffle"),
		POOL("Pool"),
		OVERHANG("Overhang"),
		OTHER("Other");
		
		private String str;

		public String getStr() {
			return this.str;
		}
		
		private Location() {
			this.str = null;
		}

		private Location(String str) {
			this.str = str;
		}

		public static Location strValueOf(String str) {
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
	 * This method constructs a Catch object without being given a timeOfCatch. In this case,
	 * the date and time are set to null.
	 *
	 * @param rowid the rowid number in the database
	 * @param flyType the type of fly that was used
	 * @param flySize the size of fly that was used
	 * @param waterConditions what the water conditions were when the fish was caught
	 * @param weatherConditions what the weather conditions were when the fish was caught
	 * @param location what type of water the fish was caught in
	 */
	public Catch(long rowid, String flyType, int flySize, Water waterConditions, Weather weatherConditions,
			Location location) {
		this.rowid = rowid;
		this.flyType = flyType;
		this.flySize = flySize;
		this.waterConditions = waterConditions;
		this.weatherConditions = weatherConditions;
		this.location = location;
		this.timeOfCatch = null;
	}

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
	 */
	public Catch(long rowid, String flyType, int flySize, Water waterConditions, Weather weatherConditions,
			Location location, Timestamp timeOfCatch) {
		this.rowid = rowid;
		this.flyType = flyType;
		this.flySize = flySize;
		this.waterConditions = waterConditions;
		this.weatherConditions = weatherConditions;
		this.location = location;
		this.timeOfCatch = timeOfCatch;
	}

	/**
	 * This method returns the string representation of a Catch object
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
