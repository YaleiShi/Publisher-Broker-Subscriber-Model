import java.util.ArrayList;

public class BasicBroker<T> implements Broker<T> {
	protected ArrayList<Subscriber<T>> subList;
	protected boolean ifOpen;
	
	public BasicBroker() {
		this.subList = new ArrayList<Subscriber<T>>();
		this.ifOpen = true;
	}
	
	@Override
	public void publish(T item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribe(Subscriber<T> subscriber) {
		// TODO Auto-generated method stub
		this.subList.add(subscriber);
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

}
