package com.tylerspaeth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CatchTests {

	// Begin Water Tests
	
	@Test
	public void waterStrValueOfStained() {
		assertTrue(Catch.Water.strValueOf("Stained") == Catch.Water.STAINED);
	}

	@Test
	public void waterStrValueOfClear() {
		assertTrue(Catch.Water.strValueOf("Clear") == Catch.Water.CLEAR);
	}

	@Test
	public void waterStrValueOfMuddy() {
		assertTrue(Catch.Water.strValueOf("Muddy") == Catch.Water.MUDDY);
	}

	@Test
	public void waterStrValueOfBadCapitalization() {
		assertEquals(Catch.Water.strValueOf("StAiNeD"), null);
	}

	@Test
	public void waterStrValueOfExtraSpace() {
		assertEquals(Catch.Water.strValueOf(" Stained"), null);
	}

	@Test
	public void waterStrValueofNull() {
		assertEquals(Catch.Water.strValueOf(null), null);
	}
	
	// End Water Tests
	
	// Begin Weather Tests
	
	@Test
	public void weatherStrValueOfSunny() {
		assertTrue(Catch.Weather.strValueOf("Sunny") == Catch.Weather.SUNNY);
	}

	@Test
	public void weatherStrValueOfPartlyCloudy() {
		assertTrue(Catch.Weather.strValueOf("Partly Cloudy") == Catch.Weather.PARTLY_CLOUDY);
	}

	@Test
	public void weatherStrValueOfCloudy() {
		assertTrue(Catch.Weather.strValueOf("Cloudy") == Catch.Weather.CLOUDY);
	}

	@Test
	public void weatherStrValueOfLightRain() {
		assertTrue(Catch.Weather.strValueOf("Light Rain") == Catch.Weather.LIGHT_RAIN);
	}

	@Test
	public void weatherStrValueOfRain() {
		assertTrue(Catch.Weather.strValueOf("Rain") == Catch.Weather.RAIN);
	}

	@Test
	public void weatherStrValueOfHeavyRain() {
		assertTrue(Catch.Weather.strValueOf("Heavy Rain") == Catch.Weather.HEAVY_RAIN);
	}

	@Test
	public void weatherStrValueOfLightSnow() {
		assertTrue(Catch.Weather.strValueOf("Light Snow") == Catch.Weather.LIGHT_SNOW);
	}

	@Test
	public void weatherStrValueOfSnow() {
		assertTrue(Catch.Weather.strValueOf("Snow") == Catch.Weather.SNOW);
	}

	@Test
	public void weatherStrValueOfHeavySnow() {
		assertTrue(Catch.Weather.strValueOf("Heavy Snow") == Catch.Weather.HEAVY_SNOW);
	}

	@Test
	public void weatherStrValueOfSevereWeather() {
		assertTrue(Catch.Weather.strValueOf("Severe Weather") == Catch.Weather.SEVERE_WEATHER);
	}
		
	@Test
	public void weatherStrValueOfBadCapitalization() {
		assertEquals(Catch.Weather.strValueOf("SuNnY"), null);
	}
	
	@Test
	public void weatherStrValueOfExtraSpace() {
		assertEquals(Catch.Weather.strValueOf("Sunny "), null);
	}

	@Test
	public void weatherStrValueOfNull() {
		assertEquals(Catch.Weather.strValueOf(null), null);
	}

	// End Weather Tests
	
	// Begin Location Tests

	@Test
	public void locationStrValueOfRiffle() {
		assertTrue(Catch.Location.strValueOf("Riffle") == Catch.Location.RIFFLE);
	}

	@Test
	public void locationStrValueOfPool() {
		assertTrue(Catch.Location.strValueOf("Pool") == Catch.Location.POOL);
	}

	@Test
	public void locationStrValueOfOverhang() {
		assertTrue(Catch.Location.strValueOf("Overhang") == Catch.Location.OVERHANG);
	}

	@Test
	public void locationStrValueOfOther() {
		assertTrue(Catch.Location.strValueOf("Other") == Catch.Location.OTHER);
	}

	@Test
	public void locationStrValueOfBadCapitalization() {
		assertEquals(Catch.Location.strValueOf("RiFfLe"), null);
	}

	@Test
	public void locationStrValueOfExtraSpace() {
		assertEquals(Catch.Location.strValueOf(" Riffle"), null);
	}

	@Test
	public void locationStrValueOfNull() {
		assertEquals(Catch.Location.strValueOf(null), null);
	}
	
	// End Location Tests
	
}
