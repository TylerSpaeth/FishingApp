package com.tylerspaeth;

import java.sql.Statement;

/**
 *
 */
public class DeletableCatchlog extends Catchlog {

	// TODO not sure if this is correct
	public DeletableCatchlog(String tableName) {
		super(tableName);
	}

	/**
	 * This method removes the table associated with this object from the database.
	 * After this method has been called, the object is useless and should no
	 * longer be used
	 */
	public void deleteCatchlog() {
		try {
			Statement state = Catchlog.getConnection().createStatement();
			String sql = "DROP TABLE " + tableName;
			state.executeUpdate(sql);
			state.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
