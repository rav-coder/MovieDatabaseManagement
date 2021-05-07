package sait.mms.application;

import java.sql.SQLException;

import sait.mms.managers.MovieManagementSystem;

/**
 * Description: Main method class to run the driver manager
 *
@Author: YunZe (David) Wei , Rafel Oporto, Saurav Adhikari
 */
public class appDriver
{

	public static void main(String[] args) throws SQLException
	{
		new MovieManagementSystem();
	}

}
