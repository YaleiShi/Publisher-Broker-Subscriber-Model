
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonObject;

/**
 * the class to set the config file
 * @author yalei
 *
 */
public class ConfigSetter {
	private static String input1, input2;
	private static String output1, output2;
	private static String time;
	private static String brokerType;
	private static String size;
	private static String timeOut;

	/**
	 * read the input from system.in
	 * guide the user to setup config
	 * @param config
	 */
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
			System.out.println("Please enter the queue or thread pool size");
			size = s.nextLine();
			System.out.println("Please enter the max waiting time");
			timeOut = s.nextLine();
		}
		
		write(config);
	}
	
	/**
	 * write the content to the path
	 * @param configFile
	 */
	public static void write(String configFile) {
		System.out.println("Writing...");
		JsonObject config = new JsonObject();
		config.addProperty("input1", input1);
		config.addProperty("input2", input2);
		config.addProperty("output1", output1);
		config.addProperty("output2", output2);
		config.addProperty("timeFlag", time);
		config.addProperty("brokerType", brokerType);
		config.addProperty("size", size);
		config.addProperty("timeOut", timeOut);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))){
			writer.write(config.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Wrtting complete");
	}
}
