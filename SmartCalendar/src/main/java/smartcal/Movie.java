package smartcal;

import java.util.ArrayList;
import java.util.List;

public class Movie {
	
		private String imgUrl;
		
		public String getImgUrl() { return imgUrl; }
		
		private String title;
	
		public String getTitle() { return title; }
		
		private List<Showtime> showtimes;
		
		public List<Showtime> getShowtimes() { return showtimes; }
		
		public Movie() {
			this.title = null;
			this.showtimes = new ArrayList<Showtime>();
		}
		
		public Movie(String title, List<Showtime> showtimes, String imgUrl) {
			this.title = title;
			this.showtimes = showtimes;
			this.imgUrl = imgUrl;
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
