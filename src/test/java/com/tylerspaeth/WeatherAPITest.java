package com.tylerspaeth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherAPITest {

	// FIXME Note that some of these tests may be faulty in the event that there is not internet connection

	// TODO in all assertThrows, check the throw message

	public static WeatherAPI w;

	@BeforeAll
	public static void setupWeatherAPI() {
		w = new WeatherAPI();
	}

	@Test
	public void daysFromNowTooLow() {
		assertThrows(IllegalArgumentException.class, () -> w.getWeather(10001, -1));
	}

	@Test
	public void daysFromNowTooHigh() {
		assertThrows(IllegalArgumentException.class, () -> w.getWeather(10001, 3));
	}

	@Test
	public void sixDigitZipCode() {
		assertThrows(IllegalArgumentException.class, () -> w.getWeather(100000, 3));
	}

	@Test 
	public void negativeZipCode() {
		assertThrows(IllegalArgumentException.class, () -> w.getWeather(-10001, 2));
	}

	@Test
	public void validZipCodeAndDays() {
		assertDoesNotThrow(() -> w.getWeather(10001, 1));
		assertNotEquals(w.getWeather(10001, 1), null);
	}

	// Tests To ensure values are being loaded into the apiToEnum hashtable properly
	
	@Test
	public void code1000Sunny() {
		assertEquals(w.checkHashtableValue(1000), Catch.Weather.SUNNY);
	}

	@Test 
	public void code1282SEVERE_WEATHER() {
		assertEquals(w.checkHashtableValue(1282), Catch.Weather.SEVERE_WEATHER);
	}

	@Test
	public void code1261LIGHT_RAIN() {
		assertEquals(w.checkHashtableValue(1261), Catch.Weather.LIGHT_RAIN);
	}

}
