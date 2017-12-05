package smartcal;

public class Showtime {
	
		private String movieTitle;
		
		public String getMovieTitle() { return movieTitle; }
		
		private String theater;
		
		public String getTheater() { return theater; }
		
		private String dateTime;
		
		public String getDateTime() { return dateTime; }
		
		private String time;
		
		public String getTime() { return time; }
		
		private String date;
		
		public String getDate() { return date; }
		
		
		private int runMinutes;
		
		public int getRunMinutes() { return runMinutes; }
		
		private int runHours;
		
		public int getRunHours() { return runHours; }
		
		public Showtime() {
			this.theater = "";
			this.dateTime = "";
			this.movieTitle = "";
			this.time = "";
			this.date = "";
		}
		
		public Showtime(String theater, String dateTime, String movieTitle) {

			this.theater = theater;
			this.dateTime = dateTime;
			this.movieTitle = movieTitle;
			time = dateTime.substring(dateTime.indexOf('T')+1);
			date = dateTime.substring(0, dateTime.indexOf('T'));

		}
		
		@Override
		public String toString() {
			return "Theater location: " + theater + "\nTime: " + dateTime;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
			result = prime * result + ((movieTitle == null) ? 0 : movieTitle.hashCode());
			result = prime * result + ((theater == null) ? 0 : theater.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Showtime other = (Showtime) obj;
			if (dateTime == null) {
				if (other.dateTime != null)
					return false;
			} else if (!dateTime.equals(other.dateTime))
				return false;
			if (movieTitle == null) {
				if (other.movieTitle != null)
					return false;
			} else if (!movieTitle.equals(other.movieTitle))
				return false;
			if (theater == null) {
				if (other.theater != null)
					return false;
			} else if (!theater.equals(other.theater))
				return false;
			return true;
		}
		
	}