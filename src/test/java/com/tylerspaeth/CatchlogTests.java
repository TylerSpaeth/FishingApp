package com.tylerspaeth;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CatchlogTests {

	// Begin Catchlog Filter Tests
	
	@Test
	public void filterStrValueOfDefaultDB() {
		assertEquals(Catchlog.Filter.strValueOf("default"), Catchlog.Filter.DEFAULT);
	}

	@Test
	public void filterStrValueOfDefaultStr() {
		assertEquals(Catchlog.Filter.strValueOf("Default"), Catchlog.Filter.DEFAULT);
	}

	@Test
	public void filterStrValueOfFlyTypeDB() {
		assertEquals(Catchlog.Filter.strValueOf("flyType"), Catchlog.Filter.FLYTYPE);
	}

	@Test
	public void filterStrValueOfFlyTypeStr() {
		assertEquals(Catchlog.Filter.strValueOf("Fly Type"), Catchlog.Filter.FLYTYPE);
	}

	@Test
	public void filterStrValueOfFlySizeDB() {
		assertEquals(Catchlog.Filter.strValueOf("flySize"), Catchlog.Filter.FLYSIZE);
	}

	@Test
	public void filterStrValueOfFlySizeStr() {
		assertEquals(Catchlog.Filter.strValueOf("Fly Size"), Catchlog.Filter.FLYSIZE);
	}

	@Test
	public void filterStrValueOfWaterConditionsDB() {
		assertEquals(Catchlog.Filter.strValueOf("water"), Catchlog.Filter.WATERCONDITIONS);
	}

	@Test
	public void filterStrValueOfWaterConditionsStr() {
		assertEquals(Catchlog.Filter.strValueOf("Water Conditions"), Catchlog.Filter.WATERCONDITIONS);
	}

	@Test
	public void filterStrValueOfWeatherConditionsDB() {
		assertEquals(Catchlog.Filter.strValueOf("weather"), Catchlog.Filter.WEATHERCONDITIONS);
	}

	@Test
	public void filterStrValueOfWeatherConditionsStr() {
		assertEquals(Catchlog.Filter.strValueOf("Weather Conditions"), Catchlog.Filter.WEATHERCONDITIONS);
	}

	@Test
	public void filterStrValueOfLocationDB() {
		assertEquals(Catchlog.Filter.strValueOf("location"), Catchlog.Filter.LOCATION);
	}

	@Test
	public void filterStrValueOfLocationStr() {
		assertEquals(Catchlog.Filter.strValueOf("Location"), Catchlog.Filter.LOCATION);
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
	
	/**
	 * Deletes the given table in the given connection
	 *
	 * @param c the connection to delete the tableName frome
	 * @param tableName the name of the table to be deleted
	 */
	private void deleteTable(Connection c, String tableName) {
		try {
			Statement state = c.createStatement();
			String sql = "DROP TABLE " + tableName;
			state.executeUpdate(sql);
			state.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	// Begin Constructor Tests
	// initDB(), createTable(), and getConnection() are also tested here

	@Test
	public void nullTableNameConstructor() {
		assertThrows(NullPointerException.class, () -> {new Catchlog(null);});
	}

	@Test
	public void blankTableNameConstructor() {
		assertThrows(IllegalArgumentException.class, () -> {new Catchlog("  ");});
	}

	@Test
	public void validConstructor() {
		assertAll(() -> {
			Catchlog catchlog = new Catchlog("test");
			deleteTable(catchlog.getConnection(), "test");	
		});
	}

	@Test
	public void checkValidConnection() {
		Connection c = new Catchlog("test").getConnection();
		assertNotEquals(c, null);
		deleteTable(c, "test");
	}

	// End Constructor Tests
		
	// Begin Test Insertion, Deletion, and Read 

	@Test
	public void invalidInsertionEmptyFlyType() {
		Catchlog cat = new Catchlog("test");
		assertThrows(IllegalArgumentException.class, () -> {
			cat.insertToDB("  ", 4, Catch.Water.CLEAR, Catch.Weather.SUNNY, 
					Catch.Location.POOL, new Timestamp(1000));
		});
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void invalidInsertionNegativeFlySize() {
		Catchlog cat = new Catchlog("test");
		assertThrows(IllegalArgumentException.class, () -> {
			cat.insertToDB("fly", -4, Catch.Water.CLEAR, Catch.Weather.SUNNY, 
					Catch.Location.POOL, new Timestamp(1000));
		});
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void invalidInsertionNullFlyType() {
		Catchlog cat = new Catchlog("test");
		assertThrows(NullPointerException.class, () -> {
			cat.insertToDB(null, 4, Catch.Water.CLEAR, Catch.Weather.SUNNY, 
					Catch.Location.POOL, new Timestamp(1000));
		});
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void invalidInsertionNullWaterConditions() {
		Catchlog cat = new Catchlog("test");
		assertThrows(NullPointerException.class, () -> {
			cat.insertToDB("test", 4, null, Catch.Weather.SUNNY, 
					Catch.Location.POOL, new Timestamp(1000));
		});
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void invalidInsertionNullWeatherConditions() {
		Catchlog cat = new Catchlog("test");
		assertThrows(NullPointerException.class, () -> {
			cat.insertToDB("test", 4, Catch.Water.CLEAR, null, 
					Catch.Location.POOL, new Timestamp(1000));
		});
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void invalidInsertionNullLocation() {
		Catchlog cat = new Catchlog("test");
		assertThrows(NullPointerException.class, () -> {
			cat.insertToDB("test", 4, Catch.Water.CLEAR, Catch.Weather.SUNNY, 
					null, new Timestamp(1000));
		});
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void invalidInsertionNullTimeOfCatch() {
		Catchlog cat = new Catchlog("test");
		assertThrows(NullPointerException.class, () -> {
			cat.insertToDB("test", 4, Catch.Water.CLEAR, Catch.Weather.SUNNY, 
					Catch.Location.POOL, null);
		});
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void invalidGetNullSortBy() {
		Catchlog cat = new Catchlog("test");
		assertThrows(NullPointerException.class, () -> {
			cat.getAllCatches(null, true);
		});
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getAndDeleteFromEmptyLog() {
		Catchlog cat = new Catchlog("test");
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);
		assertEquals(list.size(), 0);
		assertAll(() -> {cat.removeFromDB(1);});
		deleteTable(cat.getConnection(), "test");
	}
	
	@Test
	public void insertGetDeleteSingleLog() {
		Catchlog cat = new Catchlog("test");
		assertAll(() -> {
			cat.insertToDB("test", 4, Catch.Water.CLEAR, Catch.Weather.SUNNY, 
					Catch.Location.POOL, new Timestamp(1000));
		});
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);

		// Check that there is 1 element and that the values in the element match
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getFlyType(), "test");
		assertEquals(list.get(0).getFlySize(), 4);
		assertEquals(list.get(0).getWaterConditions(), Catch.Water.CLEAR);
		assertEquals(list.get(0).getWeatherConditions(), Catch.Weather.SUNNY);
		assertEquals(list.get(0).getLocation(), Catch.Location.POOL);
		assertEquals(list.get(0).getTimeOfCatch(), new Timestamp(1000));
		
		// Make sure there are no exceptions when removing from db
		long id = list.get(0).getRowId();
		assertAll(() -> {
			cat.removeFromDB(id);
		});
		
		// Check to make sure the database is empty now
		list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);
		assertEquals(list.size(), 0);
	
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void insertGetDeleteMultpleDeleteStart() {
		Catchlog cat = new Catchlog("test");
		assertAll(() -> {
			cat.insertToDB("first", 1, Catch.Water.CLEAR, Catch.Weather.SUNNY, 
					Catch.Location.POOL, new Timestamp(1000));
			cat.insertToDB("middle", 2, Catch.Water.STAINED, Catch.Weather.RAIN, 
					Catch.Location.RIFFLE, new Timestamp(1000));
			cat.insertToDB("end", 3, Catch.Water.MUDDY, Catch.Weather.SNOW, 
					Catch.Location.POOL, new Timestamp(1000));
		});
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);

		// Verify the size
		assertEquals(list.size(), 3);

		long id = list.get(0).getRowId();
		assertAll(() -> {cat.removeFromDB(id);});

		// Check what is in the database now and ensure it is as expected
		list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0).getFlySize(), 2);
		assertEquals(list.get(1).getFlySize(), 3);
	
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void insertGetDeleteMultipleDeleteMiddle() {
		Catchlog cat = new Catchlog("test");
		assertAll(() -> {
			cat.insertToDB("first", 1, Catch.Water.CLEAR, Catch.Weather.SUNNY, 
					Catch.Location.POOL, new Timestamp(1000));
			cat.insertToDB("middle", 2, Catch.Water.STAINED, Catch.Weather.RAIN, 
					Catch.Location.RIFFLE, new Timestamp(1000));
			cat.insertToDB("end", 3, Catch.Water.MUDDY, Catch.Weather.SNOW, 
					Catch.Location.POOL, new Timestamp(1000));
		});
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);

		// Verify the size
		assertEquals(list.size(), 3);

		long id = list.get(1).getRowId();
		assertAll(() -> {cat.removeFromDB(id);});

		// Check what is in the database now and ensure it is as expected
		list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0).getFlySize(), 1);
		assertEquals(list.get(1).getFlySize(), 3);
	
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void insertGetDeleteMultipleDeleteEnd() {
		Catchlog cat = new Catchlog("test");
		assertAll(() -> {
			cat.insertToDB("first", 1, Catch.Water.CLEAR, Catch.Weather.SUNNY, 
					Catch.Location.POOL, new Timestamp(1000));
			cat.insertToDB("middle", 2, Catch.Water.STAINED, Catch.Weather.RAIN, 
					Catch.Location.RIFFLE, new Timestamp(1000));
			cat.insertToDB("end", 3, Catch.Water.MUDDY, Catch.Weather.SNOW, 
					Catch.Location.POOL, new Timestamp(1000));
		});
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);

		// Verify the size
		assertEquals(list.size(), 3);

		long id = list.get(2).getRowId();
		assertAll(() -> {cat.removeFromDB(id);});

		// Check what is in the database now and ensure it is as expected
		list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0).getFlySize(), 1);
		assertEquals(list.get(1).getFlySize(), 2);
	
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void insertGetDeleteDeleteMultiple() {
		Catchlog cat = new Catchlog("test");
		assertAll(() -> {
			cat.insertToDB("first", 1, Catch.Water.CLEAR, Catch.Weather.SUNNY, 
					Catch.Location.POOL, new Timestamp(1000));
			cat.insertToDB("middle", 2, Catch.Water.STAINED, Catch.Weather.RAIN, 
					Catch.Location.RIFFLE, new Timestamp(1000));
			cat.insertToDB("end", 3, Catch.Water.MUDDY, Catch.Weather.SNOW, 
					Catch.Location.POOL, new Timestamp(1000));
		});
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);

		// Verify the size
		assertEquals(list.size(), 3);

		long id = list.get(0).getRowId();
		assertAll(() -> {cat.removeFromDB(id);});

		// Check what is in the database now and ensure it is as expected
		list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);
		assertEquals(list.size(), 2);
	
		long id2 = list.get(1).getRowId();
		assertAll(() -> {cat.removeFromDB(id2);});

		// Check what is in database again 
		list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getFlySize(), 2);
	
		deleteTable(cat.getConnection(), "test");
	}

	@Test 
	public void insertGetDeleteDeleteNotExist() {
		Catchlog cat = new Catchlog("test");
		assertAll(() -> {
			cat.insertToDB("first", 1, Catch.Water.CLEAR, Catch.Weather.SUNNY, 
					Catch.Location.POOL, new Timestamp(1000));
			cat.insertToDB("middle", 2, Catch.Water.STAINED, Catch.Weather.RAIN, 
					Catch.Location.RIFFLE, new Timestamp(1000));
			cat.insertToDB("end", 3, Catch.Water.MUDDY, Catch.Weather.SNOW, 
					Catch.Location.POOL, new Timestamp(1000));
		});
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);

		// Sum the IDs to guarentee that the rowid does not exist in the table
		long summedIDs = list.get(0).getRowId() + list.get(1).getRowId() + 
			list.get(2).getRowId();

		assertAll(() -> {cat.removeFromDB(summedIDs);});

		list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);
		assertEquals(list.size(), 3);
	
		deleteTable(cat.getConnection(), "test");
	}

	private Catchlog createLog() {
		Catchlog cat = new Catchlog("test");
		cat.insertToDB("Pink Squirrel", 12, Catch.Water.CLEAR, Catch.Weather.SUNNY,
				Catch.Location.POOL, new Timestamp(1000));
		cat.insertToDB("Pink Squirrel", 14, Catch.Water.MUDDY, Catch.Weather.SNOW,
				Catch.Location.RIFFLE, new Timestamp(1000));
		cat.insertToDB("Pheasant Tail", 10, Catch.Water.CLEAR, Catch.Weather.RAIN,
				Catch.Location.OVERHANG, new Timestamp(1000));
		cat.insertToDB("Zebra Midge", 20, Catch.Water.STAINED, Catch.Weather.SEVERE_WEATHER,
				Catch.Location.POOL, new Timestamp(1000));
		cat.insertToDB("Zebra Midge", 18, Catch.Water.STAINED, Catch.Weather.SUNNY,
				Catch.Location.POOL, new Timestamp(1000));
		return cat;
	}

	@Test
	public void getDefaultAsc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(0).getFlySize(), 12);

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(1).getFlySize(), 14);

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Pheasant Tail");

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Zebra Midge");
		assertEquals(list.get(3).getFlySize(), 20);

		// Check fifth catch
		assertEquals(list.get(4).getFlyType(), "Zebra Midge");
		assertEquals(list.get(4).getFlySize(), 18);

		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getDefaultDesc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.DEFAULT, false);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(0).getFlySize(), 12);

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(1).getFlySize(), 14);

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Pheasant Tail");

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Zebra Midge");
		assertEquals(list.get(3).getFlySize(), 20);

		// Check fifth catch
		assertEquals(list.get(4).getFlyType(), "Zebra Midge");
		assertEquals(list.get(4).getFlySize(), 18);

		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getFlyTypeAsc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.FLYTYPE, true);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Pheasant Tail");

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(1).getFlySize(), 12);

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(2).getFlySize(), 14);

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Zebra Midge");
		assertEquals(list.get(3).getFlySize(), 20);

		// Check fifth catch
		assertEquals(list.get(4).getFlyType(), "Zebra Midge");
		assertEquals(list.get(4).getFlySize(), 18);

		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getFlyTypeDesc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.FLYTYPE, false);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Zebra Midge");
		assertEquals(list.get(0).getFlySize(), 20);

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Zebra Midge");
		assertEquals(list.get(1).getFlySize(), 18);

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(2).getFlySize(), 12);

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(3).getFlySize(), 14);

		// Check fifth catch
		assertEquals(list.get(4).getFlyType(), "Pheasant Tail");

		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getFlySizeAsc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.FLYSIZE, true);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Pheasant Tail");

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(1).getFlySize(), 12);

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(2).getFlySize(), 14);

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Zebra Midge");
		assertEquals(list.get(3).getFlySize(), 18);

		// Check fifth catch
		assertEquals(list.get(4).getFlyType(), "Zebra Midge");
		assertEquals(list.get(4).getFlySize(), 20);

		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getFlySizeDesc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.FLYSIZE, false);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Zebra Midge");
		assertEquals(list.get(0).getFlySize(), 20);

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Zebra Midge");
		assertEquals(list.get(1).getFlySize(), 18);

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(2).getFlySize(), 14);

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(3).getFlySize(), 12);

		// Check fifth catch
		assertEquals(list.get(4).getFlyType(), "Pheasant Tail");

		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getWaterAsc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.WATERCONDITIONS, true);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(0).getFlySize(), 12);

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Pheasant Tail");

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(2).getFlySize(), 14);

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Zebra Midge");
		assertEquals(list.get(3).getFlySize(), 20);

		// Check fifth catch
		assertEquals(list.get(4).getFlyType(), "Zebra Midge");
		assertEquals(list.get(4).getFlySize(), 18);

		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getWaterDesc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.WATERCONDITIONS, false);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Zebra Midge");
		assertEquals(list.get(0).getFlySize(), 20);

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Zebra Midge");
		assertEquals(list.get(1).getFlySize(), 18);

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(2).getFlySize(), 14);

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(3).getFlySize(), 12);

		// Check fifth catch
		assertEquals(list.get(4).getFlyType(), "Pheasant Tail");

		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getWeatherAsc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.WEATHERCONDITIONS, true);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Pheasant Tail");

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Zebra Midge");
		assertEquals(list.get(1).getFlySize(), 20);

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(2).getFlySize(), 14);

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(3).getFlySize(), 12);

		// Check fifth catch
		assertEquals(list.get(4).getFlyType(), "Zebra Midge");
		assertEquals(list.get(4).getFlySize(), 18);

		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getWeatherDesc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.WEATHERCONDITIONS, false);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(0).getFlySize(), 12);

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Zebra Midge");
		assertEquals(list.get(1).getFlySize(), 18);

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(2).getFlySize(), 14);

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Zebra Midge");
		assertEquals(list.get(3).getFlySize(), 20);

		// Check fifth catch
		assertEquals(list.get(4).getFlyType(), "Pheasant Tail");

		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getLocationAsc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.LOCATION, true);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Pheasant Tail");

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(1).getFlySize(), 12);

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Zebra Midge");
		assertEquals(list.get(2).getFlySize(), 20);

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Zebra Midge");
		assertEquals(list.get(3).getFlySize(), 18);

		// Check fifth catch
		assertEquals(list.get(4).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(4).getFlySize(), 14);

		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getLocationDesc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.LOCATION, false);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(0).getFlySize(), 14);

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(1).getFlySize(), 12);

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Zebra Midge");
		assertEquals(list.get(2).getFlySize(), 20);

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Zebra Midge");
		assertEquals(list.get(3).getFlySize(), 18);

		// Check fifth catch
		assertEquals(list.get(4).getFlyType(), "Pheasant Tail");

		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void insertGetDeleteNonDefaultAsc() {
		Catchlog cat = createLog();
		ArrayList<Catch> list = cat.getAllCatches(Catchlog.Filter.FLYSIZE, false);

		// Delete the size 12 pink squirrel
		long id = list.get(3).getRowId();
		assertAll(() -> {cat.removeFromDB(id);});

		list = cat.getAllCatches(Catchlog.Filter.FLYTYPE, true);

		// Check first catch
		assertEquals(list.get(0).getFlyType(), "Pheasant Tail");

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Pink Squirrel");
		assertEquals(list.get(1).getFlySize(), 14);

		// Check third catch
		assertEquals(list.get(2).getFlyType(), "Zebra Midge");
		assertEquals(list.get(2).getFlySize(), 20);

		// Check fourth catch
		assertEquals(list.get(3).getFlyType(), "Zebra Midge");
		assertEquals(list.get(3).getFlySize(), 18);


		deleteTable(cat.getConnection(), "test");

	}

	// End Test Insertion, Deletion, and Read
	
	// Begin Test getMostCommon()
	
	private Catchlog commonLog() {
		Catchlog cat = new Catchlog("test");
		cat.insertToDB("Pink Squirrel", 14, Catch.Water.MUDDY, Catch.Weather.SUNNY,
				Catch.Location.OTHER, new Timestamp(1000));
		cat.insertToDB("Pink Squirrel", 12, Catch.Water.STAINED, Catch.Weather.RAIN,
				Catch.Location.POOL, new Timestamp(1000));
		cat.insertToDB("Pheasant Tail", 16, Catch.Water.MUDDY, Catch.Weather.SNOW,
				Catch.Location.RIFFLE, new Timestamp(1000));
		cat.insertToDB("Pink Squirrel", 12, Catch.Water.CLEAR, Catch.Weather.SUNNY,
				Catch.Location.POOL, new Timestamp(1000));
		cat.insertToDB("Zebra Midge", 18, Catch.Water.CLEAR, Catch.Weather.SEVERE_WEATHER,
				Catch.Location.RIFFLE, new Timestamp(1000));
		cat.insertToDB("Zebra Midge", 20, Catch.Water.CLEAR, Catch.Weather.SUNNY,
				Catch.Location.POOL, new Timestamp(1000));
		return cat;
	}
	
	@Test
	public void invalidMostCommonNull() {
		Catchlog cat = new Catchlog("test");
		assertThrows(NullPointerException.class, () -> {
			cat.getMostCommon(null);
		});
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void mostCommonEmptyLog() {
		Catchlog cat = new Catchlog("test");
		assertThrows(IllegalStateException.class, () -> {
			cat.getMostCommon(Catchlog.Filter.FLYTYPE);
		});
		deleteTable(cat.getConnection(), "test");
	}
	
	@Test
	public void mostCommonDefault() {
		Catchlog cat = commonLog();
		assertThrows(IllegalArgumentException.class, () -> {
			cat.getMostCommon(Catchlog.Filter.DEFAULT);
		});
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void mostCommonFlyType() {
		Catchlog cat = commonLog();
		assertEquals(cat.getMostCommon(Catchlog.Filter.FLYTYPE), "Pink Squirrel");
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void mostCommonFlySize() {
		Catchlog cat = commonLog(); 
		assertEquals(cat.getMostCommon(Catchlog.Filter.FLYSIZE), "12");
		deleteTable(cat.getConnection(), "test");
	}

	@Test 
	public void mostCommonWaterConditions() {
		Catchlog cat = commonLog();
		assertEquals(cat.getMostCommon(Catchlog.Filter.WATERCONDITIONS), "Clear");
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void mostCommonWeatherConditions() {
		Catchlog cat = commonLog();
		assertEquals(cat.getMostCommon(Catchlog.Filter.WEATHERCONDITIONS), "Sunny");
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void mostCommonLocation() {
		Catchlog cat = commonLog();
		assertEquals(cat.getMostCommon(Catchlog.Filter.LOCATION), "Pool");
		deleteTable(cat.getConnection(), "test");
	}

	// End Test getMostCommon() 
	
	// Begin Test getSubtable() 
	
	private Catchlog subtableLog() {
		Catchlog cat = new Catchlog("test");
		cat.insertToDB("Pink Squirrel", 14, Catch.Water.MUDDY, Catch.Weather.SUNNY,
				Catch.Location.OTHER, new Timestamp(1000));
		cat.insertToDB("Pink Squirrel", 12, Catch.Water.STAINED, Catch.Weather.RAIN,
				Catch.Location.POOL, new Timestamp(1000));
		cat.insertToDB("Pheasant Tail", 16, Catch.Water.MUDDY, Catch.Weather.SNOW,
				Catch.Location.RIFFLE, new Timestamp(1000));
		cat.insertToDB("Pink Squirrel", 12, Catch.Water.CLEAR, Catch.Weather.SNOW,
				Catch.Location.POOL, new Timestamp(1000));
		cat.insertToDB("Zebra Midge", 18, Catch.Water.CLEAR, Catch.Weather.SEVERE_WEATHER,
				Catch.Location.RIFFLE, new Timestamp(1000));
		cat.insertToDB("Zebra Midge", 20, Catch.Water.CLEAR, Catch.Weather.SUNNY,
				Catch.Location.POOL, new Timestamp(1000));
		cat.insertToDB("Zebra Midge", 22, Catch.Water.CLEAR, Catch.Weather.SEVERE_WEATHER,
				Catch.Location.RIFFLE, new Timestamp(1000));
		cat.insertToDB("Zebra Midge", 18, Catch.Water.CLEAR, Catch.Weather.SNOW,
				Catch.Location.RIFFLE, new Timestamp(1000));
		return cat;
	}
	
	@Test
	public void invalidGetSubtableNullWeather() {
		Catchlog cat = new Catchlog("test");
		assertThrows(NullPointerException.class, () -> {
			cat.getSubtable(null);
		});
		deleteTable(cat.getConnection(), "test");
	}

	@Test 
	public void getSubtableEmptyLog() {
		Catchlog cat = new Catchlog("test");
		DeletableCatchlog sub = cat.getSubtable(Catch.Weather.RAIN);
		ArrayList<Catch> list = sub.getAllCatches(Catchlog.Filter.DEFAULT, true);
		assertEquals(list.size(), 0);
		sub.deleteCatchlog();
		deleteTable(cat.getConnection(), "test");
	}

	@Test 
	public void getSubtableNoMatchingWeather() {
		Catchlog cat = subtableLog();
		DeletableCatchlog sub = cat.getSubtable(Catch.Weather.LIGHT_RAIN);
		ArrayList<Catch> list = sub.getAllCatches(Catchlog.Filter.DEFAULT, true);
		assertEquals(list.size(), 0);
		sub.deleteCatchlog();
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getSubtableAllMatchingWeather() {
		Catchlog cat = new Catchlog("test");
		cat.insertToDB("Pheasant Tail", 16, Catch.Water.MUDDY, Catch.Weather.SNOW,
				Catch.Location.RIFFLE, new Timestamp(1000));
		cat.insertToDB("Pink Squirrel", 12, Catch.Water.CLEAR, Catch.Weather.SNOW,
				Catch.Location.POOL, new Timestamp(1000));
		cat.insertToDB("Zebra Midge", 18, Catch.Water.CLEAR, Catch.Weather.SNOW,
				Catch.Location.RIFFLE, new Timestamp(1000));
		
		ArrayList<Catch> start = cat.getAllCatches(Catchlog.Filter.DEFAULT, true);

		DeletableCatchlog sub = cat.getSubtable(Catch.Weather.SNOW);
		ArrayList<Catch> end = sub.getAllCatches(Catchlog.Filter.DEFAULT, true);

		// Assure the size has not changed
		assertEquals(start.size(), end.size());	

		// Assure the same elements are in the same order
		for(int i = 0; i < start.size(); i++) {
			assertEquals(start.get(i).getFlyType(), end.get(i).getFlyType());
		}

		sub.deleteCatchlog();
		deleteTable(cat.getConnection(), "test");
	}

	@Test
	public void getSubtableSomeMatchingWeather() {
		Catchlog cat = subtableLog();
		DeletableCatchlog sub = cat.getSubtable(Catch.Weather.SEVERE_WEATHER);
		ArrayList<Catch> list = sub.getAllCatches(Catchlog.Filter.DEFAULT, true);
	 	assertEquals(list.size(), 2);
		
		// Check first catch
	  assertEquals(list.get(0).getFlyType(), "Zebra Midge");
		assertEquals(list.get(0).getFlySize(), 18);

		// Check second catch
		assertEquals(list.get(1).getFlyType(), "Zebra Midge");
		assertEquals(list.get(1).getFlySize(), 22);

		sub.deleteCatchlog();
		deleteTable(cat.getConnection(), "test");
	}

	// End Test getSubtable()
}

