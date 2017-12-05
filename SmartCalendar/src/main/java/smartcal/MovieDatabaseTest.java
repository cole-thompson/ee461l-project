package smartcal;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Charsets;

public class MovieDatabaseTest {
	
	static String readFile(String path, Charset encoding) throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}

	public static void main(String[] args) {
		
		try {
			String json = readFile("input.txt", Charsets.UTF_8);
			json = json.replace("\n", "").replace("\r", "");
			String res = new String();
			if(json.length() > 1) {
				//assume it returned a valid JSON object
				JsonFactory factory = new JacksonFactory();
				JsonParser parser = factory.createJsonParser(json);
				
				if(parser.nextToken() == JsonToken.START_OBJECT) {
					//go to postfield
					
					parser.skipToKey("poster_path");
					res = parser.getText();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
