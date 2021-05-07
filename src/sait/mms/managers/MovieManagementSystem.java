package sait.mms.managers;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

import sait.mms.drivers.MariaDBDriver;

/**
 * Description: Movie Management system that connects to the movies database and allows
 * users to add, delete, and print movies.
 * 
 * @author YunZe (David) Wei, Rafael Oporto, Saurav Adhikari
 */
public class MovieManagementSystem
{
	private Scanner in;
	public MariaDBDriver mb = new MariaDBDriver();

	public MovieManagementSystem() throws SQLException
	{
		displayMenu();
	}

	/**
	 * Method that loops to keep displaying user options till the user enters 5.
	 * 
	 * @throws SQLException
	 *             throws an error statement if there is a problem running the sql query
	 * @author YunZe (David) Wei, Rafael Oporto, Saurav Adhikari
	 */
	public void displayMenu() throws SQLException
	{
		in = new Scanner(System.in);
		int counter = 0; // this counter is for error check on the user input
		int choice = 0; // choice is used to select the 5 menu options

		do
		{
			// print statements for the menu
			System.out.println("Jim's Movie Manager");
			System.out.printf("1%s%s\n", "    ", "Add New Movie");
			System.out.printf("2%s%s\n", "    ", "Print movies released in year");
			System.out.printf("3%s%s\n", "    ", "Print random list of movies");
			System.out.printf("4%s%s\n", "    ", "Delete a movie");
			System.out.printf("5%s%s\n", "    ", "Exit");
			System.out.print("Enter option: ");
			String option = in.nextLine();

			// Error check when user inputs more than 1 character
			if (option.length() != 1)
			{
				System.out.println("Invalid option. Please select a valid option between 1 and 5.");
				counter++;
			}
			// Error check when the character length is one and is a numeric value between 1
			// and 5
			else if (option.length() == 1 && !(option.charAt(0) >= '1' && option.charAt(0) <= '5'))
			{
				System.out.println("Invalid option. Please select a valid option between 1 and 5.");
				counter++;
			} else
			{
				choice = Integer.parseInt(option);
				counter = 0;

				// switch statements to call different methods based on user choice
				switch (choice)
				{
					case 1:
						addMovie();
						break;
					case 2:
						printMoviesInYear();
						break;
					case 3:
						printRandomMovies();
						break;
					case 4:
						deleteMovie();
						break;
					case 5:
						exit();
						break;
				}
			}
		} while (counter != 0); // reprints the menu if user inputs an invalid option

	}

	/**
	 * Asks the user for the movie title, duration and release year to add to the database.
	 * Error checking to allow numbers only for duration and release year entry. Try catch to
	 * catch any SQLException If all user entry is satisfactory the movie is added on a new
	 * row with their entries.
	 * 
	 * @throws SQLException
	 *             throws an error statement if there is a problem running the sql query
	 * 
	 * @author YunZe (David) Wei
	 */
	public void addMovie() throws SQLException
	{
		mb.connect();
		try
		{
			// pattern for checking for digit entry only
			Pattern pattern = Pattern.compile("\\d+");

			// Attributes for sql statement
			String movieEntry;
			String movieDuration;
			String year;

			System.out.print("Enter movie title: ");
			movieEntry = in.nextLine();

			System.out.print("Enter duration: ");
			movieDuration = in.nextLine();
			// pattern error check to make sure the user is entering digits only
			if (!movieDuration.matches("[0-9]+"))
			{
				// loop to keep asking for a valid movie duration entry, numbers only
				do
				{
					System.out.println("Invalid movie duration, please enter numbers only");
					System.out.print("Enter duration: ");
					movieDuration = in.nextLine();
				} while (!movieDuration.matches("[0-9]+"));
			}

			System.out.print("Enter year: ");
			year = in.nextLine();
			if (!year.matches("[0-9]+") || year.length() != 4)
			{
				do
				{
					// loop to keep asking for a valid movie release year entry, numbers only
					System.out.println("Invalid year entry, please enter positive numbers only. 4 digits long");
					System.out.print("Enter year: ");
					year = in.nextLine();
				} while (!year.matches("[0-9]+"));
			}

			// Create a string with an INSERT statement.
			// insert statement will insert a new row of data into the database based on user entry.
			String sqlStatement = "INSERT INTO movies " + "(id, duration, title, year) " + "VALUES ('" + (count() + 1)
					+ "', '" + Integer.parseInt(movieDuration) + "', '" + movieEntry + "', '" + Integer.parseInt(year)
					+ "')";

			int rows = mb.update(sqlStatement);

			// gives user status update and closes all methods used.
			System.out.println("Added movie to database.");
			mb.disconnect();
			displayMenu();
			in.close();
		}
		// catch all SQL exceptions and displays the error message and gets the user to try to add
		// a movie again.
		catch (SQLException e)
		{
			System.out.println("Error adding movie: " + e.getMessage());
			addMovie();
		}
	}

	/**
	 * This method gets the user to enter a year and retrieves all the movies released in that
	 * year from the movies table. Try catch to catch any SQLException.
	 * 
	 * @author Saurav Adhikari
	 */
	public void printMoviesInYear()
	{
		int year = 0;
		System.out.print("Enter the year: ");
		String yearIn = in.next(); // gets input string form the user
		// check if the user inputted a integer or not, show error otherwise and
		// reprompt
		try
		{
			year = Integer.parseInt(yearIn);
		} catch (NumberFormatException ex)
		{
			System.out.println("\nPlease enter a number.");
			printMoviesInYear();
		}
		try
		{
			// Create a connection to the database.
			mb.connect();
			ResultSet result = mb.get("SELECT duration, year, title FROM movies WHERE year = " + year);
			int durationCount = 0; // to count the total duration
			if (result.next() == false)
			{
				// shows error if no rows were found in the query
				System.out.println("\nNo movies were found for that year.");

			} else
			{
				ResultSetMetaData md = (ResultSetMetaData) result.getMetaData();
				System.out.println("Movie List");
				// prints the column names
				System.out.printf("%-10s %-8s %-30s\n", md.getColumnName(1), md.getColumnName(2), md.getColumnName(3));
				do
				{
					// Display a row from the result set.
					System.out.printf("%-10d %-8d %-30s\n", result.getInt("Duration"), result.getInt("Year"),
							result.getString("Title")); // prints the rows from the query
					
				} while (result.next());
				
			}
			// Close the connection, resultset and also display the menu
			result.close();
			ResultSet totalDuration = mb.get("SELECT SUM(duration) FROM movies WHERE YEAR =" + year);
			if (totalDuration.next())
			{
				System.out.println("\nTotal duration: " + totalDuration.getInt(1) + " minutes\n");
				totalDuration.close();
			}
			mb.disconnect();
			displayMenu();
		} catch (SQLException ex)
		{
			System.out.println("ERROR: " + ex.getMessage());
			printMoviesInYear();
		}

	}

	/**
	 * This method gets the user to enter a number and retrieves a list of random movies from
	 * the movies table adding up to that number. Try catch to catch any SQLException.
	 * 
	 * @author Saurav Adhikari
	 * @throws SQLException
	 */
	public void printRandomMovies() throws SQLException
	{
		mb.connect();
		int randomAmount = 0;

		System.out.print("Enter number of movies: ");
		String randomIn = in.next(); // gets input string form the user
		// check if the user inputed a integer or not, show error otherwise and re-prompt
		try
		{
			randomAmount = Integer.parseInt(randomIn);
			if (randomAmount <= 0)
			{
				System.out.println("Please enter a number between 1 and " + count());
				printRandomMovies();
			}
		} catch (NumberFormatException ex)
		{
			System.out.println("\nPlease enter a number.");
			printRandomMovies();
		}
		if (randomAmount < count())
		{
			try
			{
				// orders the rows in the resultant query randomly and limits the query to the number
				// specified by the user
				ResultSet result = mb
						.get("SELECT id, duration, year, title FROM movies ORDER BY RAND() LIMIT " + randomAmount);
				int durationCount = 0; // to count the total duration
				if (result.next() == false)
				{
					System.out.println("\nInvalid Entry."); // shows error if no rows were found in the query
				} else
				{
					ResultSetMetaData md = (ResultSetMetaData) result.getMetaData();
					System.out.println("Movie List");
					// prints the column names
					System.out.printf("%-10s %-8s %-30s\n", md.getColumnName(2), md.getColumnName(3),
							md.getColumnName(4));
					do
					{
						// Display a row from the result set.
						System.out.printf("%-10d %-8d %-30s\n", result.getInt("Duration"), result.getInt("Year"),
								result.getString("Title")); // prints the rows from the query
						// Increment the counter.
						durationCount += result.getInt("Duration");
					} while (result.next());
					System.out.println("\nTotal duration: " + durationCount + " minutes\n");
				}
				// Close the connection, resultset and also display the menu
				result.close();
				mb.disconnect();
				displayMenu();
				in.close();
			} catch (SQLException ex)
			{
				System.out.println("ERROR: " + ex.getMessage());
				printRandomMovies();
			}
		} else
		{
			System.out.println("Please enter a number between 1 and " + count());
			printRandomMovies();
		}

	}

	/**
	 * Deletes a movie entry from the movies database using a movie id typed in by the user.
	 * 
	 * @throws SQLException
	 * @author Rafael Oporto
	 */
	public void deleteMovie() throws SQLException
	{
		try
		{
			mb.connect();
			Scanner in = new Scanner(System.in);
			int movieID = -1;

			System.out.print("Enter movie ID you would like to delete: ");
			String userInput = in.nextLine();

			try
			{
				movieID = Integer.parseInt(userInput);
			} catch (NumberFormatException e)
			{
				System.out.println("Invalid movie format, please make sure it is an integer value.");
				deleteMovie();
			}

			if (movieID > 0 && movieID <= count())
			{
				// Create a statement to delete the specified movie ID number.
				String sqlStatement = "DELETE FROM movies " + "WHERE id = '" + movieID + "'";

				// Send the DELETE statement to the DBMS.
				int rows = mb.update(sqlStatement);

				// Display the results.

				System.out.println("Movie " + userInput + " is deleted.");
			} else
			{
				System.out.println("No movies deleted, movie ID not found");
			}

			mb.disconnect();
			displayMenu();
			in.close();
		} catch (SQLException e)
		{
			System.out.println("Error removing movie: " + e.getMessage());
			deleteMovie();
		}
	}

	/**
	 * Counts how many rows there are in the database for error checking purposes
	 * 
	 * @return total count of database rows
	 * @throws SQLException
	 * @Author: YunZe (David) Wei , Rafel Oporto, Saurav Adhikari
	 */
	public int count() throws SQLException
	{
		int counter = 0;
		ResultSet rowCount = mb.get("SELECT COUNT(*) as count FROM movies");
		if (rowCount.next())
		{
			counter = rowCount.getInt("count");
			rowCount.close();
		}
		return counter;
	}

	/**
	 * exits the program
	 * 
	 * @Author: YunZe (David) Wei , Rafel Oporto, Saurav Adhikari
	 */
	public void exit()
	{
		System.out.println("Goodbye!");
		System.exit(0);
	}

}
