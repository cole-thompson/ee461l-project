package smartcal;

public class Showtime {
		private String theater;
		
		public String getTheater() { return theater; }
		
		private String dateTime;
		
		public String getDateTime() { return dateTime; }
		
		private String time;
		
		public String getTime() { return time; }
		
		public Showtime(String theater, String dateTime) {
			this.theater = theater;
			this.dateTime = dateTime;
			time = dateTime.substring(dateTime.indexOf('T')+1);
		}
		
		@Override
		public String toString() {
			return "Theater location: " + theater + "\nTime: " + dateTime;
		}
	}