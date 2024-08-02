package com.tylerspaeth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CatchlogTests {

	// Begin Catchlog Filter Tests
	
	@Test
	public void filterStrValueOfDefaultDB() {
		assertTrue(Catchlog.Filter.strValueOf("default") == Catchlog.Filter.DEFAULT);
	}

	@Test
	public void filterStrValueOfDefaultStr() {
		assertTrue(Catchlog.Filter.strValueOf("Default") == Catchlog.Filter.DEFAULT);
	}

	@Test
	public void filterStrValueOfFlyTypeDB() {
		assertTrue(Catchlog.Filter.strValueOf("flyType") == Catchlog.Filter.FLYTYPE);
	}

	@Test
	public void filterStrValueOfFlyTypeStr() {
		assertTrue(Catchlog.Filter.strValueOf("Fly Type") == Catchlog.Filter.FLYTYPE);
	}

	@Test
	public void filterStrValueOfFlySizeDB() {
		assertTrue(Catchlog.Filter.strValueOf("flySize") == Catchlog.Filter.FLYSIZE);
	}

	@Test
	public void filterStrValueOfFlySizeStr() {
		assertTrue(Catchlog.Filter.strValueOf("Fly Size") == Catchlog.Filter.FLYSIZE);
	}

	@Test
	public void filterStrValueOfWaterConditionsDB() {
		assertTrue(Catchlog.Filter.strValueOf("water") == Catchlog.Filter.WATERCONDITIONS);
	}

	@Test
	public void filterStrValueOfWaterConditionsStr() {
		assertTrue(Catchlog.Filter.strValueOf("Water Conditions") == Catchlog.Filter.WATERCONDITIONS);
	}

	@Test
	public void filterStrValueOfWeatherConditionsDB() {
		assertTrue(Catchlog.Filter.strValueOf("weather") == Catchlog.Filter.WEATHERCONDITIONS);
	}

	@Test
	public void filterStrValueOfWeatherConditionsStr() {
		assertTrue(Catchlog.Filter.strValueOf("Weather Conditions") == Catchlog.Filter.WEATHERCONDITIONS);
	}

	@Test
	public void filterStrValueOfLocationDB() {
		assertTrue(Catchlog.Filter.strValueOf("location") == Catchlog.Filter.LOCATION);
	}

	@Test
	public void filterStrValueOfLocationStr() {
		assertTrue(Catchlog.Filter.strValueOf("Location") == Catchlog.Filter.LOCATION);
	}

	@Test
	public void filterStrValueOfBadCapitalization() {
		assertEquals(Catchlog.Filter.strValueOf("DeFaUlT"), null);
	}

	@Test
       	public void filterStrValueOfExtraSpace() {
		assertEquals(Catchlog.Filter.strValueOf("  default  "), null);
	}

	@Test 
	public void filterStrValueOfNull() {
		assertEquals(Catchlog.Filter.strValueOf(null), null);
	}
	
	// End Filter Tests
}

