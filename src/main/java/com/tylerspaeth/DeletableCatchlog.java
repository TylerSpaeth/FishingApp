package com.tylerspaeth;

import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

//TODO may want to unitlize built-in temporary tables for this class
/**
 *
 */
public class DeletableCatchlog extends Catchlog {

	//TODO header
	protected DeletableCatchlog(String tableName) throws IllegalArgumentException {
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
