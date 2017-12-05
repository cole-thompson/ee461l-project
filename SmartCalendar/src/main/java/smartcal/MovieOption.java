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

	private Showtime showtime;
	private boolean searched;
	private boolean finished;
	private List<Movie> searchResults;
	
	public MovieOption() {
		super();
		setAllDay(false);
		setSearched(false);
		setFinished(false);
		setSearchResults(new ArrayList<Movie>());
	}
	
	
	public void setOption(Showtime showtime) {
		setShowtime(showtime);
		setLocation(showtime.getTheater());
		setOptionName(showtime.getMovieTitle());
		String starttime = showtime.getTime();
		String date = showtime.getDate();
		setStartTime(dayTimeStringToDate(date, starttime));
	}
	
	
	public void searchMovies(int zip, int radius, String startDay, String endDay) {
		//TODO
		
		Date start = dayStringToDate(startDay);
		//searchResults.add(new Movie());
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

	public boolean hasFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (finished ? 1231 : 1237);
		result = prime * result + (searched ? 1231 : 1237);
		result = prime * result + ((showtime == null) ? 0 : showtime.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovieOption other = (MovieOption) obj;
		if (finished != other.finished)
			return false;
		if (searched != other.searched)
			return false;
		if (showtime == null) {
			if (other.showtime != null)
				return false;
		} else if (!showtime.equals(other.showtime))
			return false;
		return true;
	}
}
