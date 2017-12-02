package smartcal;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.google.appengine.api.urlfetch.*;

public class FandangoManager {
	
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
	
	
	/*
	 * this function sends requests to fandango's website
	 * inputs: opString (String) -- contains operation and parameter
	 */
	static public String makeRequest(String opString) {
		String baseUrl = "http://api.fandango.com/";
		String apiVer = "1";
		String apiKey = "q763vgth9rru2b66g9pspsf3"; 
		String sharedSecret = "aUhDUZzNPp";
		String result = null; 
		//these are required by fandango to send requests for movies	
		
		try {
			//we're building this string http://api.fandango.com/<version>?op=<operation>&<parameter list>&apikey=<apikey>&sig=<sig>
			//to send to fandango to get our XML formatted results
			String authParams = buildAuthorizationParameters(apiKey, sharedSecret);
			String requestUrl = String.format("%s/v%s/?%s&%s", baseUrl, apiVer, opString, authParams);
			
			URLFetchService urlfetchservice = URLFetchServiceFactory.getURLFetchService(); //app engine's HTTP request service
			
			HTTPResponse response = urlfetchservice.fetch(new HTTPRequest(new URL(requestUrl)));
			result = new String(response.getContent());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
