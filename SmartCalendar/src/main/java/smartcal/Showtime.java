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
		
		public Showtime(String theater, String dateTime, String movieTitle, String runTime) {
			this.theater = theater;
			this.dateTime = dateTime;
			this.movieTitle = movieTitle;
			time = dateTime.substring(dateTime.indexOf('T')+1);
			date = dateTime.substring(0, dateTime.indexOf('T'));
			int hours = Integer.parseInt(runTime.substring(runTime.indexOf('T') + 1, runTime.indexOf('H')));
			int minutes = Integer.parseInt(runTime.substring(runTime.indexOf('H') + 1), runTime.indexOf('M'));
			runHours = hours;
			runMinutes = minutes;
		}
		
		@Override
		public String toString() {
			return "Theater location: " + theater + "\nTime: " + dateTime;
		}
	}