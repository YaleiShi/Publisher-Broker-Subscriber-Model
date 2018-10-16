import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonObject;

public class ReviewSubscriber<T> implements Subscriber<T> {
	private String timeFlag;
	private String output;
	private BufferedWriter writer;
//	private StringBuffer sb;
	private int newTime;
	private boolean ifNew;
	
	public ReviewSubscriber(String timeFlag, String output, boolean ifNew) {
		newTime = 0;
		this.timeFlag = timeFlag;
		this.output = output;
		this.ifNew = ifNew;
//		sb = new StringBuffer();
		try {
			writer = new BufferedWriter(new FileWriter(this.output));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void onEvent(T item) {
		// TODO Auto-generated method stub
		JsonObject review = (JsonObject) item;
		String time = review.get("unixReviewTime").getAsString();
		if((ifNew && time.compareTo(this.timeFlag) >= 0) || 
		   (!ifNew && time.compareTo(this.timeFlag) < 0)) {
			try {
				this.writer.write(review.toString() + "\n");
				newTime++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			sb.append(review.toString() + "\n");
		}
	}
	
//	public void write() {
//		try(BufferedWriter writer = new BufferedWriter(new FileWriter(this.output))){
//			writer.write(sb.toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
	public void print() {
		System.out.println("new: " + newTime);
	}
	
	public void closeWriter() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	
	public void subscribe(Broker<T> b) {
		b.subscribe(this);
	}

}
