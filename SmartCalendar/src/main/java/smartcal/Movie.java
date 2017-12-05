package smartcal;

import java.util.List;

public class Movie {
		
		private String title;
	
		public String getTitle() { return title; }
		
		private List<Showtime> showtimes;
		
		public List<Showtime> getShowtimes() { return showtimes; }
		
		public Movie(String title, List<Showtime> showtimes) {
			this.title = title;
			this.showtimes = showtimes;
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
