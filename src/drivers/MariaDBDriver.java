package drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sait.mms.contracts.DatabaseDriver;

public class MariaDBDriver implements DatabaseDriver, drivers.DatabaseDriver
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
	
	private String getDsn() {
		// Data source name is formatted as follows (for MariaDB): jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword
		String dataSourceName = String.format("jdbc:mariadb://%s:%d/%s?user=%s&password=%s", SERVER, PORT, DATABASE, USERNAME, PASSWORD);
		return dataSourceName;
	}

	@Override
	public void connect() throws SQLException
	{
		String dsn = this.getDsn();
		connection = DriverManager.getConnection(dsn);
	}

	@Override
	public ResultSet get(String query) throws SQLException
	{
		Statement statement = connection.createStatement();
		ResultSet results = statement.executeQuery(query);
		return results;
	}

	@Override
	public int update(String query) throws SQLException
	{
		Statement statement = connection.createStatement();
		int updated = statement.executeUpdate(query);
	
		return updated;
	}

	@Override
	public void disconnect() throws SQLException
	{
		if (connection != null && !connection.isClosed())
			connection.close();
	}

}
