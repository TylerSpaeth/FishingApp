package com.tylerspaeth;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherAPITests {
	
	@Test
	public void construtorNoExceptions() {
		assertAll(() -> {WeatherAPI w = new WeatherAPI();});
	}

	@Test
	public void sixDigitZipCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new WeatherAPI().getWeather(111111, 0);
		});
	}

	@Test
	public void negativeZipCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new WeatherAPI().getWeather(-11111, 0);
		});
	}

	@Test
	public void fourDigitZipCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new WeatherAPI().getWeather(1111, 0);
		});
	}

	@Test
	public void fiveDigitZipCodeNotExists() {
		assertThrows(IllegalArgumentException.class, () -> {
			new WeatherAPI().getWeather(99999, 0);
		});
	}

	@Test
	public void negativeDaysFromNow() {
		assertThrows(IllegalArgumentException.class, () -> {
			new WeatherAPI().getWeather(10001, -1);
		});
	}

	@Test
	public void threeDaysFromNow() {
		assertThrows(IllegalArgumentException.class, () -> {
			new WeatherAPI().getWeather(10001, 3);
		});
	}

	@Test
	public void getWeatherNewYorkZeroDays() {
		Pair<Catch.Weather, String> result = new WeatherAPI().getWeather(10001, 0);
		assertTrue(result.getValue0() != null);
		assertEquals(result.getValue1(), "New York");
	}

	@Test
	public void getWeatherDallasTwoDays() {
		Pair<Catch.Weather, String> result = new WeatherAPI().getWeather(75201, 2);
		assertTrue(result.getValue0() != null);
		assertEquals(result.getValue1(), "Dallas");
	}


}
