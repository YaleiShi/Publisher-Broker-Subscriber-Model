
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * the main engine of the amazon reviews filter
 * would setup config file, read config file,
 * setup the engine, start the engine, and measure the running time
 * @author yalei
 *
 */
public class FilterEngine {
	private String config;
	private String input1, input2;
	private String output1, output2;
	private long timeFlag;
	private String brokerType;
	private int queueOrPoolSize;
	private long timeOut;
	
	private Broker b;
	private Publisher p1, p2;
	private Thread t1, t2;
	private ReviewSubscriber s1, s2;
	
	/**
	 * read the config file path
	 * @param config
	 */
	public FilterEngine(String config) {
		this.queueOrPoolSize = -1;
		this.timeOut = -1;
		this.config = config;
	}
	
	/**
	 * set and read the config file
	 */
	public void SetReadConfig() {
		try(Scanner s = new Scanner(System.in)){
			System.out.println("Welcome, do you want to set up the config? \n"
				+ "If you choose no, you will use the default setting. Y/N");
			String input = s.nextLine();
			input = input.toLowerCase();
			if(input.equals("y")) {
			ConfigSetter.set(this.config);
			}
		}
		this.readConfig();
	}
	
	/**
	 * read the config file, get all the arguments
	 * then save into the data member
	 */
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
			this.timeFlag = o.get("timeFlag").getAsLong();
			this.brokerType = o.get("brokerType").getAsString();
			this.queueOrPoolSize = o.get("size").getAsInt();
			this.timeOut = o.get("timeOut").getAsLong();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * check if the args are valid, step by step
	 * @return
	 */
	public boolean checkInput() {
		if(input1.equals("") || input2.equals("") || output1.equals("")
		|| output2.equals("") || timeFlag == -1 || brokerType.equals("")) {
			System.out.println("not enough args");
			return false;
		}
		if(!brokerType.equals("sob") && !brokerType.equals("aob") && !brokerType.equals("aub")) {
			System.out.println("broker type invalid");
			return false;
		}
		if(brokerType.equals("aob") || brokerType.equals("aub")) {
			if(queueOrPoolSize == -1 || timeOut == -1) {
				System.out.println("need queue size and Max waiting time");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * setup the engine,
	 * initiate the broker according to the broker type
	 * initiate the thread and subscribers
	 * add the subscribers into the broker
	 */
	public void setUp() {
		if(this.brokerType.equals("sob")) {
			b = new SynchronousOrderedDispatchBroker();
		}else if(this.brokerType.equals("aob")) {
			b = new AsyncOrderedDispatchBroker(this.queueOrPoolSize, this.timeOut);
		}else {
			b = new AsyncUnorderedDispatchBroker(this.queueOrPoolSize, this.timeOut);
		}
		p1 = new Publisher(input1, b);
		p2 = new Publisher(input2, b);
		t1 = new Thread(p1);
		t2 = new Thread(p2);
		s1 = new ReviewSubscriber(timeFlag, output1, true);
		s2 = new ReviewSubscriber(timeFlag, output2, false);
		s1.subscribe(b);
		s2.subscribe(b);
	}
	
	/**
	 * start all the thread,
	 * after finish the work, shutdown the thread and measure the running time
	 */
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
		s1.closeWriter();
		s2.closeWriter();	
	}
	
	/**
	 * out put the total read and total output
	 * to see if they are equal;
	 */
	public void testOutput() {
		System.out.println("total read: " + (p1.getCount() + p2.getCount()));
		int newReviewLines = LineCounter.countLine(output1);
		int oldReviewLines = LineCounter.countLine(output2);
		System.out.println("new review lines: " + newReviewLines);
		System.out.println("old review lines: " + oldReviewLines);
		System.out.println("total write: " + (newReviewLines + oldReviewLines));
	}
}
