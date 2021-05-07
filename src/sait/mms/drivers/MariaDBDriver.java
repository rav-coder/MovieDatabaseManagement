package sait.mms.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sait.mms.contracts.DatabaseDriver;

/**
 * Description: Driver class to connect java to mariaDB databases so java statements to retrieve
 * and update the databse can be carried out.
 *
@Author: YunZe (David) Wei , Rafel Oporto, Saurav Adhikari
 */
public class MariaDBDriver implements DatabaseDriver
{
	private static final String SERVER = "localhost";
	private static final int PORT = 3306;
	private static final String DATABASE = "cprg251";
	private static final String USERNAME = "cprg251";
	private static final String PASSWORD = "password";
	
	private Connection connection = null;
	
	/**
	 * Initializes the newly created JdbcDriver.
	 */
	public MariaDBDriver() {
	}
	
	/**
	 * Gets the data source name with the class attributes to connect to the database.
	 * @return returns the data source name to connect to the database 
	 */
	private String getDsn() {
		// Data source name is formatted as follows (for MariaDB): jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword
		String dataSourceName = String.format("jdbc:mariadb://%s:%d/%s?user=%s&password=%s", SERVER, PORT, DATABASE, USERNAME, PASSWORD);
		return dataSourceName;
	}
	
	/**
	 * Connects to the database.
	 * @throws SQLException if connection cant be made
	 */
	@Override
	public void connect() throws SQLException
	{
		String dsn = this.getDsn();
		connection = DriverManager.getConnection(dsn);
	}
	
	/**
	 * Performs a retrieval from the database (ie: SELECT)
	 * @param query Query to send to database.
	 * @return Returns the results as a ResultSet
	 * @throws SQLException Thrown if problem performing query.
	 */
	@Override
	public ResultSet get(String query) throws SQLException
	{
		Statement statement = connection.createStatement();
		ResultSet results = statement.executeQuery(query);
		return results;
	}
	
	/**
	 * Performs an update query (UPDATE, DELETE, DROP, etc.) on the database.
	 * @param query Query to send to database.
	 * @return Number of rows modified.
	 * @throws SQLException
	 */
	@Override
	public int update(String query) throws SQLException
	{
		Statement statement = connection.createStatement();
		int updated = statement.executeUpdate(query);
	
		return updated;
	}
	
	/**
	 * Disconnects from the database.
	 * @throws SQLException if disconnecting from database cannot be done
	 */
	@Override
	public void disconnect() throws SQLException
	{
		if (connection != null && !connection.isClosed())
			connection.close();
	}

}
