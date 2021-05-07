package sait.mms.problemdomain;
/**
 * Movie class with duration, id, title, year attributes
 *
 @Author: YunZe (David) Wei , Rafel Oporto, Saurav Adhikari
 */
public class Movie
{
	private int duration;
	private int id;
	private String title;
	private int year;
	
	/**
	 * gets movies duration in minutes
	 * @return returns movies duration
	 */
	public int getDuration()
	{
		return duration;
	}
	
	/**
	 * sets the movies duration in minutes
	 * @param duration movies duration in minutes
	 */
	public void setDuration(int duration)
	{
		this.duration = duration;
	}
	
	/**
	 * gets the movies id
	 * @return returns the movies id
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * sets the movies id
	 * @param id movies id
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * gets the movies title
	 * @return returns the movies title
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * sets the movies title
	 * @param title movies title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * gets the year of release of the movie
	 * @return returns the year the movie was released
	 */
	public int getYear()
	{
		return year;
	}
	
	/**
	 * sets the year the movie was released
	 * @param year year movie was released
	 */
	public void setYear(int year)
	{
		this.year = year;
	}
	
	/**
	 * custom to string method
	 */
	@Override
	public String toString()
	{
		return "Movie [duration=" + duration + ", id=" + id + ", title=" + title + ", year=" + year + "]";
	}
	
	

}
