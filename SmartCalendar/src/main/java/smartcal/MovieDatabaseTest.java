package smartcal;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
			List<Movie> movies = new MovieDatabase().getMovies(json);
			
			for(Movie m: movies)  { System.out.println(m.toString()); }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
