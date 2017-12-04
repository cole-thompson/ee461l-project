package smartcal;

public class Showtime {
		private String theater;
		
		public String getTheater() { return theater; }
		
		private String dateTime;
		
		public String getDateTime() { return dateTime; }
		
		public Showtime(String theater, String dateTime) {
			this.theater = theater;
			this.dateTime = dateTime;
		}
		
		@Override
		public String toString() {
			return "Theater location: " + theater + "\nTime: " + dateTime;
		}
	}