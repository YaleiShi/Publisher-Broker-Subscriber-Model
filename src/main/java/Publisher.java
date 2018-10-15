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

public class Publisher<T> implements Runnable{
	private String input;
	private Broker<T> b;
	
	public Publisher(String input, Broker<T> b) {
//		System.out.println(input);
		this.input = input;
		this.b = b;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		JsonParser parser = new JsonParser();
		//read the file
		try(BufferedInputStream stream = new BufferedInputStream(new FileInputStream(new File(input)));
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "ISO-8859-1"),10 * 1024 * 1024)){
//			System.out.println("gogogo");
			String line = reader.readLine();
//			System.out.println(line);
			while(line != null) {
				JsonElement element;
				try {
					element = parser.parse(line);
				}catch(JsonSyntaxException jse) {
					line = reader.readLine();
					continue;
				}	
				if(element.isJsonObject()) {
					JsonObject jo = (JsonObject) element;
					T t = (T) jo;
					b.publish(t);
				}	
				line = reader.readLine();
			}	
		}catch(FileNotFoundException fnfe) {
			System.out.println(fnfe.getMessage());
		}catch(IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		
	}
}
