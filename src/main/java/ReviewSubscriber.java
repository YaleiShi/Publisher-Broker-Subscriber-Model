
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonObject;

/**
 * ReveiwSubscriber class
 * take the item and judge if the item is needed
 * write the item into the output file
 * @author yalei
 *
 * @param <T>
 */
public class ReviewSubscriber<T> implements Subscriber<T> {
	private long timeFlag;
	private String output;
	private BufferedWriter writer;
	private boolean ifNew;
	
	/**
	 * take the timeFlag as the flag to decide if the item is needed
	 * take the output file as the file path to write to file
	 * take ifNew to decide if the subscriber need new reviews
	 * @param timeFlag
	 * @param output
	 * @param ifNew
	 */
	public ReviewSubscriber(long timeFlag, String output, boolean ifNew) {
		this.timeFlag = timeFlag;
		this.output = output;
		this.ifNew = ifNew;
		try {
			writer = new BufferedWriter(new FileWriter(this.output));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * get the item and decide if write the item to the file
	 */
	@Override
	public void onEvent(T item) {
		// TODO Auto-generated method stub
		JsonObject review = (JsonObject) item;
		long time = review.get("unixReviewTime").getAsLong();
		if(ifNew) {
			if(time >= this.timeFlag) {
				this.write(review);
			}
		}else{
			if(time < this.timeFlag) {
				this.write(review);
			}
		}
	}
	
	/**
	 * write the review into the file
	 * @param review
	 */
	public void write(JsonObject review) {
		try {
			writer.write(review.toString() + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * close the buffered writer
	 */
	public void closeWriter() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * subscribe the subscriber into the broker
	 * @param b
	 */
	public void subscribe(Broker<T> b) {
		b.subscribe(this);
	}

}
