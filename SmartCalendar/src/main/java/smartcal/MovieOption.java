package smartcal;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MovieOption extends InvitationOption {

	private Showtime showtime;
	private boolean searched;
	private boolean finished;
	private List<Movie> searchResults;
	private MovieDatabase mdb;
	
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
		Date startdate = dayTimeStringToDate(date, starttime);
		
		Date enddate = (Date) startdate.clone();

		int hours = startdate.getHours();
		
		hours +=3;
		
		if(hours >= 24) {
			hours -= 24;
			enddate.setDate(enddate.getDate()+1);
			enddate.setHours(hours);
		} else { enddate.setHours(hours); }
		
		setStartTime(startdate);
		setEndTime(enddate);
	}
	
	
	public void searchMovies(int zip, int radius, String startDay, String endDay) {
		Date start = dayStringToDate(startDay);
		Date end = dayStringToDate(startDay);
		
		int days = (int) ( (end.getTime() - start.getTime()) / (1000*60*60*24) );
		
		mdb = new MovieDatabase(startDay, Integer.toString(zip), days, radius);
		
		searchResults = mdb.getMovies();
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
