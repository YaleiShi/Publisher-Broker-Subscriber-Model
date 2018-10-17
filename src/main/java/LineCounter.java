
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * the class used to count how many lines are in the file
 * @author yalei
 *
 */
public class LineCounter {
	
	/**
	 * return the number of line in the file
	 * @param input
	 * @return
	 */
	public static int countLine(String input) {
		int count = 0;
		try(BufferedInputStream stream = new BufferedInputStream(new FileInputStream(new File(input)));
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "ISO-8859-1"),10 * 1024 * 1024)){
				String line = reader.readLine();
				while(line != null) {
					count++;
					line = reader.readLine();
				}	
			}catch(FileNotFoundException fnfe) {
				System.out.println(fnfe.getMessage());
			}catch(IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		return count;
	}
}
