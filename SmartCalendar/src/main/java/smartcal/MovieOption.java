package smartcal;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


public class MovieOption extends InvitationOption {
	
	private Movie movie;
	private Showtime showtime;
	private boolean searched;
	private List<Movie> searchResults;
	private MovieDatabase mdb;
	
	public MovieOption() {
		super();
		setAllDay(false);
		setMovie(null);
		setSearched(false);
		setSearchResults(new ArrayList<Movie>());
	}
	
	public MovieOption(Movie movie) {
		this();
		setMovie(movie);
	}
	
	public void setOption(Showtime showtime) {
		setShowtime(showtime);
		setLocation(showtime.getTheater());
		String starttime = showtime.getTime();
		String date = showtime.getDate();
		Date startdate = dayTimeStringToDate(date, starttime);
		Date enddate = (Date) startdate.clone();
		int newMinutes = startdate.getMinutes() + showtime.getRunMinutes();
		int hours = startdate.getHours();
		boolean newDate = false;
		if(newMinutes >= 60) {
			hours+=1;
			newMinutes -= 60; 
		} 
		enddate.setMinutes(newMinutes);
		hours += movie.getRunHours();
		
		if(hours >= 24) {
			hours -=24;
			newDate = true;
		}
		
		if(newDate) {
			enddate.setDate(enddate.getDate()+1);
		} 
		
		enddate.setHours(hours);
		
		setStartTime(startdate);
		setEndTime(enddate);
	}
	
	
	public void searchMovies(int zip, int radius, String startDay, String endDay) {
		//TODO
		
		Date start = dayStringToDate(startDay);
		Date end = dayStringToDate(startDay);
		
		int days = (int) ( (end.getTime() - start.getTime()) / (1000*60*60*24) );
		
		mdb = new MovieDatabase(startDay, Integer.toString(zip), days, radius);
		
		searchResults = mdb.getMovies();
	}
	
	
	public Movie getMovie() {
		return movie;
	}
	
	private void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Showtime getShowtime() {
		return showtime;
	}

	private void setShowtime(Showtime showtime) {
		this.showtime = showtime;
	}

	public boolean hasSearched() {
		return searched;
	}

	public void setSearched(boolean searched) {
		this.searched = searched;
	}

	public List<Movie> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(List<Movie> searchResults) {
		this.searchResults = searchResults;
	}
	
}
