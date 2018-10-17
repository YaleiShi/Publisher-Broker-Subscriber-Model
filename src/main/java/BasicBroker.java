
import java.util.ArrayList;

/**
 * the basic broker class which store the common function of all three brokers
 * @author yalei
 *
 * @param <T>
 */
public class BasicBroker<T> implements Broker<T> {
	protected ArrayList<Subscriber<T>> subList;
	protected boolean ifOpen;
	
	/**
	 * the constructor of basic broker
	 * just initiate the array list of subscribers and the boolean of ifOpen
	 */
	public BasicBroker() {
		this.subList = new ArrayList<Subscriber<T>>();
		this.ifOpen = true;
	}
	
	/**
	 * basic broker publish method
	 * no comment, need to be implemented in the specific broker
	 */
	@Override
	public void publish(T item) {
		// TODO Auto-generated method stub	
	}

	/**
	 * add the subscrber into the subscriber list
	 */
	@Override
	public void subscribe(Subscriber<T> subscriber) {
		// TODO Auto-generated method stub
		this.subList.add(subscriber);
	}

	/**
	 * shut down need to be initiated in the specific broker
	 */
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
	}

}
