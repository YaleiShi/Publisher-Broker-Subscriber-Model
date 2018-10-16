import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FilterEngine {
	private String config;
	private String input1, input2;
	private String output1, output2;
	private String timeFlag;
	private String brokerType;
	private int queueOrPoolSize;
	private long timeOut;
	
	private Broker b;
	private Thread t1;
	private Thread t2;
	private ReviewSubscriber s1;
	private ReviewSubscriber s2;
	
	public FilterEngine(String config) {
		this.queueOrPoolSize = -1;
		this.timeOut = -1;
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
			this.queueOrPoolSize = o.get("size").getAsInt();
			this.timeOut = o.get("timeOut").getAsLong();
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
		if(input1.equals("") || input2.equals("") || output1.equals("")
		|| output2.equals("") || timeFlag.equals("") || brokerType.equals("")) {
			System.out.println("not enough args");
			return false;
		}
		if(!brokerType.equals("sob") && !brokerType.equals("aob") && !brokerType.equals("aub")) {
			System.out.println("broker type invalid");
			return false;
		}
		if(brokerType.equals("aob") || brokerType.equals("aub")) {
			if(queueOrPoolSize == -1) {
				System.out.println("need queue size");
				return false;
			}
			if(brokerType.equals("aob") && timeOut == -1) {
				System.out.println("need max waiting time");
				return false;
			}
		}
		return true;
	}
	
	public void setUp() {
		if(this.brokerType.equals("sob")) {
			b = new SynchronousOrderedDispatchBroker();
		}else if(this.brokerType.equals("aob")) {
			b = new AsyncOrderedDispatchBroker(this.queueOrPoolSize, this.timeOut);
		}else {
			b = new AsyncUnorderedDispatchBroker(this.queueOrPoolSize);
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
		b.shutdown();
		long end = System.currentTimeMillis();
		System.out.println(brokerType + " run time: " + (end - start));
		s1.print();
		s2.print();
	}
}
