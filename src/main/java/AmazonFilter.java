
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AmazonFilter {
	
	/**
	 * the main method of amazon filter
	 * pass the config file path into it and run
	 * @param args
	 */
	public static void main(String[] args) {
		// for demo, just use the defaultPath
		String[] defaultPath = {"config.json"};
		args = defaultPath;
		
		// check the args length
		if(args.length != 1) {
			System.out.println("need config file path");
			System.exit(1);
		}
		
		// start the engine -> read the config -> check config file
		FilterEngine engine = new FilterEngine(args[0]);
		engine.SetReadConfig();
		if(!engine.checkInput()) {
			System.exit(1);
		}
		
		// setup and start the engine
		engine.setUp();
		engine.start();
		
		// test if the output is right
		engine.testOutput();
		
	}
	
}
