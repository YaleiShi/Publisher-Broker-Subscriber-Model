import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonObject;

public class ConfigSetter {
	private static String input1;
	private static String input2;
	private static String output1;
	private static String output2;
	private static String time;
	private static String brokerType;

	public static void set(String config) {
		try(Scanner s = new Scanner(System.in)){
			System.out.println("Please enter the first input path");
			input1 = s.nextLine();
			System.out.println("Please enter the secound input path");
			input2 = s.nextLine();
			System.out.println("Please enter the first output path");
			output1 = s.nextLine();
			System.out.println("Please enter the secound output path");
			output2 = s.nextLine();
			System.out.println("Please enter the filter time");
			time = s.nextLine();
			System.out.println("Please enter the brokerType:\n"
				+ "sob - SynchronousOrderedDispatchBroker\n"
				+ "aob - AsyncOrderedDispatchBroker\n"
				+ "aub - AsyncUnorderedDispatchBroker");
			brokerType = s.nextLine();
		}
		
		write(config);
	}
	
	public static void write(String configFile) {
		System.out.println("Writing...");
		JsonObject config = new JsonObject();
		config.addProperty("input1", input1);
		config.addProperty("input2", input2);
		config.addProperty("output1", output1);
		config.addProperty("output2", output2);
		config.addProperty("timeFlag", time);
		config.addProperty("brokerType", brokerType);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))){
			writer.write(config.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Wrtting complete");
	}
	
	public static void read(FilterEngine engine) {
		
	}
}
