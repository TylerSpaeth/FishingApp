package com.tylerspaeth;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

//TODO may want to unitlize built-in temporary tables for this class
/**
 * This class provides the fuctionality of a DeleteableCatchlog object that
 * connects to a SQLite database. Funcionalities are identical to a standard 
 * {@link com.tylerspaeth.Catchlog Catchlog}, with the exception that the 
 * corresponding table for this class can be deleted.
 *
 * @author Tyler Spaeth
 */
public class DeletableCatchlog extends Catchlog {

	/**
	 * This method is the constructor for a DeleteableCatchlog object. It behaves
	 * identically to the constructor for a normal Catchlog object. It initates a
	 * connection to the backend if there is not already an existing one, sets the
	 * tableName and creates a table for the tableName.
	 *
	 * @param tableName the name for the table that corresponds to this Catchlog
	 * @throws NullPointerException if the given tableName is null 
	 * @throws IllegalArgumentExceeption if the tableName is blank
	 */
	protected DeletableCatchlog(String tableName) throws NullPointerException, 
					IllegalArgumentException {
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
