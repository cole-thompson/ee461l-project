package smartcal;

import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.google.appengine.api.urlfetch.*;

public class GracenoteMovieManager {
	
	static protected String sha256Encode(String StringToEncode) throws NoSuchAlgorithmException {
	        
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] buffUtf8Msg = digest.digest(StringToEncode.getBytes());
	        String result = String.format("%0" + (buffUtf8Msg.length*2) + "X", new BigInteger(1, buffUtf8Msg));
	        
	        return result;
	}

    static protected String buildAuthorizationParameters(String apiKey, String sharedSecret) throws NoSuchAlgorithmException {
        
        long seconds =  System.currentTimeMillis() / 1000;
        String paramsToEncode = apiKey + sharedSecret + seconds;
        String encodedParams = sha256Encode(paramsToEncode);

        String result = String.format("api_key=%s&sig=%s", apiKey, encodedParams);

        return result;
    }
	
	
	/* static public String makeMoviesRequest
	 * this function sends requests to gracenote's website
	 * inputs: dateZip (String) -- contains date and zip formatted "YYYY-MM-DD" and zip formatted "XXXXX"
	 */
	public MovieDatabase makeMoviesRequest(String date, String zip) {
		String baseUrl = "http://data.tmsapi.com/v1.1/movies/showings?";
		String apiVer = "1";
		String apiKey = "styt25a56dg574x5x29yaws4"; 
		String sharedSecret = "aUhDUZzNPp";
		String result = null; 
		MovieDatabase mdb = new MovieDatabase();
		//these are required by gracenote to send requests for movies	
		
		try {
			//we're building this string http://data.tmsapi.com/v1.1/movies/showings?startDate=<date>&zip=<zip>&api_key=<styt25a56dg574x5x29yaws4>
			//to send to gracenote to get our results
			String requestUrl = String.format("%sstartDate=%s&zip=%s&api_key=%s", baseUrl, date, zip, apiKey);
			
			URLFetchService urlfetchservice = URLFetchServiceFactory.getURLFetchService(); //app engine's HTTP request service
			
			HTTPResponse response = urlfetchservice.fetch(new URL(requestUrl));
			
			//we need to format the response to add a proper key for the movie listings so google json parser can correctly place it into the database fields
			mdb.getMovies(new String(response.getContent()));
			//response = new HTTPResponse(response.getResponseCode(), content.getBytes(StandardCharsets.UTF_8), response.getFinalUrl(), response.getHeaders());
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mdb;
	}
	
}
