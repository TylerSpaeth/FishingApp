package com.tylerspaeth;


import java.sql.Timestamp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CatchTests {

	// Begin Water Tests
	
	@Test
	public void waterStrValueOfStained() {
		assertEquals(Catch.Water.strValueOf("Stained"), Catch.Water.STAINED);
	}

	@Test
	public void waterStrValueOfClear() {
		assertEquals(Catch.Water.strValueOf("Clear"), Catch.Water.CLEAR);
	}

	@Test
	public void waterStrValueOfMuddy() {
		assertEquals(Catch.Water.strValueOf("Muddy"), Catch.Water.MUDDY);
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
		assertEquals(Catch.Weather.strValueOf("Sunny"), Catch.Weather.SUNNY);
	}

	@Test
	public void weatherStrValueOfPartlyCloudy() {
		assertEquals(Catch.Weather.strValueOf("Partly Cloudy"), Catch.Weather.PARTLY_CLOUDY);
	}

	@Test
	public void weatherStrValueOfCloudy() {
		assertEquals(Catch.Weather.strValueOf("Cloudy"), Catch.Weather.CLOUDY);
	}

	@Test
	public void weatherStrValueOfLightRain() {
		assertEquals(Catch.Weather.strValueOf("Light Rain"), Catch.Weather.LIGHT_RAIN);
	}

	@Test
	public void weatherStrValueOfRain() {
		assertEquals(Catch.Weather.strValueOf("Rain"), Catch.Weather.RAIN);
	}

	@Test
	public void weatherStrValueOfHeavyRain() {
		assertEquals(Catch.Weather.strValueOf("Heavy Rain"), Catch.Weather.HEAVY_RAIN);
	}

	@Test
	public void weatherStrValueOfLightSnow() {
		assertEquals(Catch.Weather.strValueOf("Light Snow"), Catch.Weather.LIGHT_SNOW);
	}

	@Test
	public void weatherStrValueOfSnow() {
		assertEquals(Catch.Weather.strValueOf("Snow"), Catch.Weather.SNOW);
	}

	@Test
	public void weatherStrValueOfHeavySnow() {
		assertEquals(Catch.Weather.strValueOf("Heavy Snow"), Catch.Weather.HEAVY_SNOW);
	}

	@Test
	public void weatherStrValueOfSevereWeather() {
		assertEquals(Catch.Weather.strValueOf("Severe Weather"), Catch.Weather.SEVERE_WEATHER);
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
		assertEquals(Catch.Location.strValueOf("Riffle"), Catch.Location.RIFFLE);
	}

	@Test
	public void locationStrValueOfPool() {
		assertEquals(Catch.Location.strValueOf("Pool"), Catch.Location.POOL);
	}

	@Test
	public void locationStrValueOfOverhang() {
		assertEquals(Catch.Location.strValueOf("Overhang"), Catch.Location.OVERHANG);
	}

	@Test
	public void locationStrValueOfOther() {
		assertEquals(Catch.Location.strValueOf("Other"), Catch.Location.OTHER);
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
	
	// Begin Constructor and Getter Tests
	
	@Test
	public void validCatchConstructor() {
		assertAll(() -> { 
			Catch c = new Catch(1, "Pink Squirrel", 4, Catch.Water.CLEAR, Catch.Weather.SUNNY,
					Catch.Location.POOL, new Timestamp(12000));
		});
	}

	@Test
	public void validCatchConstructorGetters() {
		Timestamp t = new Timestamp(12000);
		Catch c = new Catch(1, "Pink Squirrel", 4, Catch.Water.CLEAR, Catch.Weather.SUNNY,
					Catch.Location.POOL, t);
		assertEquals(c.getRowId(), 1);
		assertEquals(c.getFlyType(), ("Pink Squirrel"));
		assertEquals(c.getFlySize(), 4);
		assertEquals(c.getWaterConditions(), Catch.Water.CLEAR);
		assertEquals(c.getWeatherConditions(), Catch.Weather.SUNNY);
		assertEquals(c.getLocation(),Catch.Location.POOL);
		assertEquals(c.getTimeOfCatch(), t);
	}

	@Test
	public void invalidCatchConstructorNullFlyType() {
		assertThrows(NullPointerException.class, () -> {
				Catch c = new Catch(1, null, 4, Catch.Water.CLEAR, Catch.Weather.SUNNY,
					Catch.Location.POOL, new Timestamp(1200));
		});
	}

	@Test
	public void invalidCatchConstructorEmptyFlyType() {
		assertThrows(IllegalArgumentException.class, () -> {
				Catch c = new Catch(1, "   ", 4, Catch.Water.CLEAR, Catch.Weather.SUNNY,
					Catch.Location.POOL, new Timestamp(1200));
		});
	}

	@Test
	public void invalidCatchConstructorNegativeFlySize() {
		assertThrows(IllegalArgumentException.class, () -> {
				Catch c = new Catch(1, "Pink Squirrel", -4, Catch.Water.CLEAR, Catch.Weather.SUNNY,
					Catch.Location.POOL, new Timestamp(1200));
		});
	}

	@Test
	public void invalidCatchConstructorNullWaterConditions() {
		assertThrows(NullPointerException.class, () -> {
				Catch c = new Catch(1, "Pink Squirrel", 4, null, Catch.Weather.SUNNY,
					Catch.Location.POOL, new Timestamp(1200));
		});
	}
		
	@Test
	public void invalidCatchConstructorNullWeatherConditions() {
		assertThrows(NullPointerException.class, () -> {
				Catch c = new Catch(1, "Pink Squirrel", 4, Catch.Water.CLEAR, null,
					Catch.Location.POOL, new Timestamp(1200));
		});
	}
	
	@Test
	public void invalidCatchConstructorNullLocation() {
		assertThrows(NullPointerException.class, () -> {
				Catch c = new Catch(1, "Pink Squirrel", 4, Catch.Water.CLEAR, Catch.Weather.SUNNY,
					null, new Timestamp(1200));
		});
	}

	@Test
	public void invalidCatchConstructorNullTimeOfCatch() {
		assertThrows(NullPointerException.class, () -> {
				Catch c = new Catch(1, "Pink Squirrel", 4, Catch.Water.CLEAR, Catch.Weather.SUNNY,
					Catch.Location.POOL, null);
		});
	}

	@Test
	public void testToString() {
		Catch c = new Catch(1, "Pink Squirrel", 4, Catch.Water.CLEAR, Catch.Weather.SUNNY,
					Catch.Location.POOL, new Timestamp(2024-1900, 2, 10, 0, 0, 0, 0));
		String expectedString = "Size 4 Pink Squirrel - Water Conditions: Clear - "
			+ "Weather Conditions: Sunny - Water Location: Pool - Time of Catch: "
			+ "2024-03-10 00:00:00.0";
		assertEquals(c.toString(), expectedString);
	}

	// End Constructor and Getter Tests

}
