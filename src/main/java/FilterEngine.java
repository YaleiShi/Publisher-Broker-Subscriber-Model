import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FilterEngine {
	private String config;
	private String input1;
	private String input2;
	private String output1;
	private String output2;
	private String timeFlag;
	private String brokerType;
	
	private Broker b;
	private Thread t1;
	private Thread t2;
	private ReviewSubscriber s1;
	private ReviewSubscriber s2;
	
	public FilterEngine(String config) {
		this.config = config;
	}
	
	public void SetReadConfig() {
		try(Scanner s = new Scanner(System.in)){
			System.out.println("Welcome, do you want to set up the config? "
				+ "If you choose no, you will use the default setting. Y/N");
			String input = s.nextLine();
			input = input.toLowerCase();
			if(input.equals("y")) {
			ConfigSetter.set(this.config);
			}
		}
		this.readConfig();
	}
	
	private void readConfig() {
		JsonParser jp = new JsonParser();
		try(BufferedReader reader = new BufferedReader(new FileReader(this.config))){
			String file = reader.readLine();
			JsonElement e = jp.parse(file);
			JsonObject o = (JsonObject) e;
			this.input1 = o.get("input1").getAsString();
			this.input2 = o.get("input2").getAsString();
			this.output1 = o.get("output1").getAsString();
			this.output2 = o.get("output2").getAsString();
			this.timeFlag = o.get("timeFlag").getAsString();
			this.brokerType = o.get("brokerType").getAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean checkInput() {
		if(input1 == null || input2 == null || output1 == null 
		|| output2 == null || timeFlag == null || brokerType == null) {
			System.out.println("not enough args");
			return false;
		}
		if(!brokerType.equals("sob") && !brokerType.equals("aob") && !brokerType.equals("aub")) {
			System.out.println("broker type invalid");
			return false;
		}
		return true;
	}
	
	public void setUp() {
		if(this.brokerType.equals("sob")) {
			b = new SynchronousOrderedDispatchBroker();
		}else if(this.brokerType.equals("aob")) {
			b = new AsyncOrderedDispatchBroker();
		}else {
			b = new AsyncUnorderedDispatchBroker();
		}
		t1 = new Thread(new Publisher(input1, b));
		t2 = new Thread(new Publisher(input2, b));
		s1 = new ReviewSubscriber(timeFlag, output1, true);
		s2 = new ReviewSubscriber(timeFlag, output2, false);
		s1.subscribe(b);
		s2.subscribe(b);
	}
	
	public void start() {
		long start = System.currentTimeMillis();
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("run time: " + (end - start));
	}
}
