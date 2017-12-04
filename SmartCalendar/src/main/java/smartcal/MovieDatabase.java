package smartcal;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.json.jackson.JacksonFactory;

public class MovieDatabase {
	
	private List<Movie> movies;
	
	public List<Movie> getMovies() { return movies; }
	
	/* static public String makeMoviesRequest
	 * this function sends requests to gracenote's website
	 * inputs: dateZip (String) -- contains date and zip formatted "YYYY-MM-DD" and zip formatted "XXXXX"
	 */
	private void makeMoviesRequest(String date, String zip) {
		String baseUrl = "http://data.tmsapi.com/v1.1/movies/showings?";
		String apiKey = "styt25a56dg574x5x29yaws4"; 
		//these are required by gracenote to send requests for movies	
		
		try {
			//we're building this string http://data.tmsapi.com/v1.1/movies/showings?startDate=<date>&zip=<zip>&api_key=<styt25a56dg574x5x29yaws4>
			//to send to gracenote to get our results
			String requestUrl = String.format("%sstartDate=%s&zip=%s&api_key=%s", baseUrl, date, zip, apiKey);
			
			URLFetchService urlfetchservice = URLFetchServiceFactory.getURLFetchService(); //app engine's HTTP request service
			
			HTTPResponse response = urlfetchservice.fetch(new URL(requestUrl));
			
			//we need to format the response to add a proper key for the movie listings so google json parser can correctly place it into the database fields
			getMovies(new String(response.getContent()));
			//response = new HTTPResponse(response.getResponseCode(), content.getBytes(StandardCharsets.UTF_8), response.getFinalUrl(), response.getHeaders());
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String getTheatreName(JsonFactory fac, String theatreJson) throws IOException {
		JsonParser parser = fac.createJsonParser(theatreJson);
		while(parser.nextToken() != JsonToken.START_OBJECT) {}
		parser.skipToKey("name");
		return parser.getText();
	}
	
	private List<Showtime> parseShowtimes(JsonFactory fac, String showtimes) throws IOException {
		JsonParser parser = fac.createJsonParser(showtimes);
		
		List<Showtime> shows = new ArrayList<Showtime>();
		
		while(true) {
			JsonToken token = parser.nextToken();
			
			if(token == null) break;
			
			if(token.equals(JsonToken.START_OBJECT)) {
				//parse the theatre key to get the theater name
				parser.skipToKey("theatre");
				String name = getTheatreName(fac, parser.getText());
				parser.skipToKey("dateTime");
				String dateTime = parser.getText();
				shows.add(new Showtime(name, dateTime));
			}
		}
		
		return shows;
	}
	
	/*  MovieDatabse(String fullJson)
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
					String cur = parser.getCurrentName();
					while(parser.nextToken() != JsonToken.END_OBJECT) {}
					parser.nextToken();
					parser.skipToKey("dateTime");
					String dateTime = parser.getText();
					while(parser.nextToken() != JsonToken.END_OBJECT) {}	
					JsonToken t = parser.nextToken();
					//make new showtime obj and add it to the list
					showtimes.add(new Showtime(name, dateTime));
					if(t.equals(JsonToken.END_ARRAY)) break; else { multiTime = true; }
				}
				movies.add(new Movie(title, showtimes));
			}
		}
		
		return movies;
	}
	
	public MovieDatabase() {}
	
	public MovieDatabase(String date, String zip) {
		makeMoviesRequest(date, zip);
	}
}