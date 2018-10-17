
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
/**
 * the publisher class to read the input file and parse the line
 * into object, then publish the item into the broker
 * @author yalei
 *
 * @param <T>
 */
public class Publisher<T> implements Runnable{
	private String input;
	private Broker<T> b;
	private int count;
	
	/**
	 * take the input file path and broker into the data member
	 * @param input
	 * @param b
	 */
	public Publisher(String input, Broker<T> b) {
		count = 0;
		this.input = input;
		this.b = b;
	}

	/**
	 * read the input file, parse the line into objects
	 * then publish the object item into broker
	 */
	@Override
	public void run() {
		JsonParser parser = new JsonParser();
		
		try(BufferedInputStream stream = new BufferedInputStream(new FileInputStream(new File(input)));
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "ISO-8859-1"), 10 * 1024 * 1024)){
			//test if the reading file is correct
			System.out.println("start parsing " + input);
			String line = reader.readLine();
			while(line != null) {
				JsonElement element;
				try {
					element = parser.parse(line);
				}catch(JsonSyntaxException jse) {
					line = reader.readLine();
					continue;
				}	
				if(element.isJsonObject()) {
					count++;
					b.publish((T) element);
				}	
				line = reader.readLine();
			}	
		}catch(FileNotFoundException fnfe) {
			System.out.println(fnfe.getMessage());
		}catch(IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		
	}
	
	/**
	 * get how many lines have been readed
	 * @return
	 */
	public int getCount() {
		return this.count;
	}
}
