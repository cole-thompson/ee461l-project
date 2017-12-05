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
	
	public void setOption(Movie movie, Showtime showtime) {
		setMovie(movie);
		setShowtime(showtime);
		setLocation(showtime.getTheater());
		setOptionName(movie.getTitle());
		String starttime = showtime.getTime();
		String date = showtime.getDate();
		setStartTime(dayTimeStringToDate(date, starttime));
	}
	
	
	public void searchMovies(int zip, int radius, String startDay, String endDay) {
		//TODO
		
		Date start = dayStringToDate(startDay);
		//searchResults.add(new Movie());
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
