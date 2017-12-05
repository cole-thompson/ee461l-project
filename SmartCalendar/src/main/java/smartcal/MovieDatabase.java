package smartcal;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.json.jackson.JacksonFactory;

public class MovieDatabase {
	
	private final String baseUrl = "http://data.tmsapi.com/v1.1/movies/showings?";
	private final String apiKey = "mtve7zepqf8ctr9bupb7cbzs"; 
	private final String imgApiKey = "01d1cc7ad34031e1841237b9c246a391";
	private final String baseImgUrl = "https://api.themoviedb.org/3/search/movie?api_key=01d1cc7ad34031e1841237b9c246a391&query=";
	private final String baseImgLink = "https://image.tmdb.org/t/p/w500/";
	private List<Movie> movies;
	
	public List<Movie> getMovies() { return movies; }
	
	public int getNumMovies() {return movies.size();}
	
	public int getNumTheatresByMovieName(String movieName) {
		int numTheaters = 0;
		for(Movie m: movies) {
			if(m.getTitle().equals(movieName)) {
				List<String> theaters = new ArrayList<String>();
				for(Showtime s: m.getShowtimes()) {
					if(!theaters.contains(s.getTheater())) {
						numTheaters++;
						theaters.add(s.getTheater());
					}
				}
				break;
			}
		}
		
		return numTheaters;
	}
	
	public List<String> getMovieTitles() {
		List<String> titles = new ArrayList<String>();
		
		for(Movie m : movies) {
			titles.add(m.getTitle());
		}
		
		return titles;
	}
	
	public List<String> getTheatreNamesByMovieName(String movieName) {
		List<String> theatreNames = new ArrayList<String>();
		
		for(Movie m: movies) {
			if(m.getTitle().equals(movieName)) {
				for(Showtime s : m.getShowtimes()) {
					if(!theatreNames.contains(s.getTheater())) theatreNames.add(s.getTheater());
				}
			}
		}
		
		return theatreNames;
	}
	
	public int getNumShowtimesByMovieTheatre(String movie, String theatre) {
		int numShowtimes = 0;
		for(Movie m : movies) {
			if(m.getTitle().equals(movie)) {
				for(Showtime s : m.getShowtimes()) {
					if(s.getTheater().equals(theatre)) numShowtimes++;
				}
				break;
			}
		}
		return numShowtimes;
	}
	
	public List<String> getShowtimesByMovieTheatre(String movie, String theatre) {
		List<String> showtimes = new ArrayList<String>();
		
		for(Movie m : movies) {
			if(m.getTitle().equals(movie)) {
				for(Showtime s : m.getShowtimes()) {
					if(s.getTheater().equals(theatre)) showtimes.add(s.getTime());
				}
			}
		}
		
		return showtimes;
	}
	private void makeRadiusRequest(String date, String zip, String radius) {
		String requestUrl = String.format("%sstartDate=%s&zip=%s&radius=%s&api_key=%s", baseUrl, date, zip, radius, apiKey);
		makeMoviesRequest(requestUrl);
	}
	private void makeRequest(String date, String zip, int days) {
		String requestUrl = String.format("%sstartDate=%s&numDays=%d&zip=%s&api_key=%s", baseUrl, date, days, zip, apiKey);
		makeMoviesRequest(requestUrl);
	}
	
	private void makeRequest(String date, String zip, int days, int radius) {
		String requestUrl = String.format("%sstartDate=%s&numDays=%d&zip=%s&radius=%d&api_key=%s", baseUrl, date, days, zip, radius, apiKey);
		makeMoviesRequest(requestUrl);
	}
	
	private void makeRequest(String date, String zip) {
		String requestUrl = String.format("%sstartDate=%s&zip=%s&api_key=%s", baseUrl, date, zip, apiKey);
		makeMoviesRequest(requestUrl);
	}
	
	
	/* static public String makeMoviesRequest
	 * this function sends requests to gracenote's website
	 * inputs: dateZip (String) -- contains date and zip formatted "YYYY-MM-DD" and zip formatted "XXXXX"
	 */
	private void makeMoviesRequest(String url) {
		
		//these are required by gracenote to send requests for movies	
		
		try {
			//we're building this string http://data.tmsapi.com/v1.1/movies/showings?startDate=<date>&zip=<zip>&api_key=<styt25a56dg574x5x29yaws4>
			//to send to gracenote to get our results
			
			
			URLFetchService urlfetchservice = URLFetchServiceFactory.getURLFetchService(); //app engine's HTTP request service
			
			HTTPResponse response = urlfetchservice.fetch(new URL(url));
			
			//we need to format the response to add a proper key for the movie listings so google json parser can correctly place it into the database fields
			getMovies(new String(response.getContent()));
			//response = new HTTPResponse(response.getResponseCode(), content.getBytes(StandardCharsets.UTF_8), response.getFinalUrl(), response.getHeaders());
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String getImageUrl(String title) {
		String res = null;
		String queryString = title.replace(' ', '+');
		
		try {
			//gonna try to get these damn images eh? let's make the URL
			URLFetchService urlfetchservice = URLFetchServiceFactory.getURLFetchService(); //app engine's HTTP request service
			HTTPResponse response = urlfetchservice.fetch(new URL(baseImgUrl + queryString));
			
			String json = new String(response.getContent());
			
			if(json.length() > 1) {
				//assume it returned a valid JSON object
				JsonFactory factory = new JacksonFactory();
				JsonParser parser = factory.createJsonParser(json);
				
				if(parser.nextToken() == JsonToken.START_OBJECT) {
					//go to postfield
					
					parser.skipToKey("poster_path");
					res = parser.getText();
					res = baseImgLink + res;
				}
				
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return res;
	}
	
	
	/*  MovieDatabase(String fullJson)
	 *  builds a movie database by parsing the entire response json object into corresponding movie objects
	*/
	public List<Movie> getMovies(String fullJson) throws IOException {
		
		JsonFactory factory = new JacksonFactory();
		JsonParser parser = factory.createJsonParser(fullJson);
		movies = new ArrayList<Movie>();
		
		while(true) {
			JsonToken token = parser.nextToken();
			
			if(token == null) break;
			
			//go into a movie object
			if(token.equals(JsonToken.START_OBJECT)) {
				
				//movie object
				
				//get title string
				parser.skipToKey("title");
				String title = parser.getText();
				
				//trying to get an image
				//String imgUrl = getImageUrl(title);
				
				parser.nextToken();
				
				//showtimes array
				ArrayList<Showtime> showtimes = new ArrayList<Showtime>();
				parser.skipToKey("showtimes");
				boolean multiTime = false;
				while(true) {
		
					if(!multiTime) { while(parser.nextToken() != JsonToken.START_OBJECT) {} }
					
					parser.skipToKey("theatre");
					parser.skipToKey("name");
					String name = parser.getText();
					while(parser.nextToken() != JsonToken.END_OBJECT) {}
					parser.nextToken();
					parser.skipToKey("dateTime");
					String dateTime = parser.getText();
					while(parser.nextToken() != JsonToken.END_OBJECT) {}	
					JsonToken t = parser.nextToken();
					//make new showtime obj and add it to the list
					showtimes.add(new Showtime(name, dateTime, title));
					if(t.equals(JsonToken.END_ARRAY)) break; else { multiTime = true; }
				}
				movies.add(new Movie(title, showtimes, null));
			}
		}
		
		return movies;
	}
	
	public MovieDatabase() {}
	
	public MovieDatabase(String date, String zip) {
		makeRequest(date, zip);
	}
	
	public MovieDatabase(String date, String zip, int days, int radius) {
		makeRequest(date, zip, days, radius);
	}
	
	public MovieDatabase(String date, String zip, int days) {
		makeRequest(date, zip, days);
	}
	
	public MovieDatabase(String date, String zip, String radius) {
		makeRadiusRequest(date, zip, radius);
	}
	
	@Override
	public String toString() {
		String res = new String();
		for(Movie m: movies) {
			res += m.toString() + "\n";
		}
		return res; 
	}
}