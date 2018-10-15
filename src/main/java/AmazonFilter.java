import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AmazonFilter {
	
	public static void main(String[] args) {
		String[] test = {"config.json"};
		args = test;
		if(!checkArgs(args)) {
			System.exit(1);
		}
		
		FilterEngine engine = new FilterEngine(args[0]);
		engine.SetReadConfig();
		if(!engine.checkInput()) {
			System.exit(1);
		}
		
		engine.setUp();
		
		engine.start();
		
	}
	
	public static boolean checkArgs(String[] args) {
		if(args.length != 1) {
			System.out.println("not enough args");
			return false;
		}
		return true;
	}
}
