package smartcal;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class MovieOption extends InvitationOption {
	
	private Movie movie;
	private Showtime showtime;
	
	public MovieOption() {
		super();
		setAllDay(false);
		this.movie = null;
	}
	
	public MovieOption(Movie movie) {
		this();
		this.movie = movie;
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
	
}
