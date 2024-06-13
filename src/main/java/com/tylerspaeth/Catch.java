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
		STAINED,
		CLEAR,
		MUDDY
	};
	public enum Weather {
		SUNNY,
		OVERCAST,
		RAINY
	};
	private Water waterConditions;
	private Weather weatherConditions;

	// Water Location
	enum Location {
		RIFFLE,
		POOL,
		OVERHANG,
		OTHER
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
					 " - Water Conditions: " + this.waterConditions +
					 " - Weather Conditions: " + this.weatherConditions +
					 " - Water Location: " + this.location + 
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
