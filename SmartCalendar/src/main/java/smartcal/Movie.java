package smartcal;

import java.util.List;

public class Movie {
		
		private String title;
	
		public String getTitle() { return title; }
		
		private int runHours;
		
		public int getRunHours() { return runHours; }
		
		private int runMinutes;
		
		public int getRunMinutes() { return runMinutes; }
		
		private List<Showtime> showtimes;
		
		public List<Showtime> getShowtimes() { return showtimes; }
		
		public Movie(String title, String runTime, List<Showtime> showtimes) {
			this.title = title;
			this.showtimes = showtimes;
			int hours = Integer.parseInt(runTime.substring(runTime.indexOf('T') + 1, runTime.indexOf('H')));
			int minutes = Integer.parseInt(runTime.substring(runTime.indexOf('H') + 1), runTime.indexOf('M'));
			runHours = hours;
			runMinutes = minutes;
		}
		
		@Override
		public String toString() {
			String res = title;
			for(Showtime s : showtimes) {
				res += "\n" + s.toString();
			}
			
			return res;
		}
	}
