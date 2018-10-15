import java.util.ArrayList;

public class SynchronousOrderedDispatchBroker<T> extends BasicBroker<T> {
	private ArrayList<Subscriber<T>> subList;
	private boolean ifOpen;
	
	public SynchronousOrderedDispatchBroker() {
		this.subList = new ArrayList<Subscriber<T>>();
		this.ifOpen = true;
	}
	
	@Override
	public synchronized void publish(T item) {
		// TODO Auto-generated method stub
		if(ifOpen) {
			for(Subscriber<T> s: subList) {
				s.onEvent(item);
			}
		}
	}

	@Override
	public void subscribe(Subscriber<T> subscriber) {
		// TODO Auto-generated method stub
		this.subList.add(subscriber);
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		this.ifOpen = false;
	}

}
