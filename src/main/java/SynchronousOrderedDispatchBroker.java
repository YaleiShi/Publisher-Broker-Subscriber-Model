import java.util.ArrayList;

public class SynchronousOrderedDispatchBroker<T> extends BasicBroker<T> {
	
	public SynchronousOrderedDispatchBroker() {
		super();
	}
	
	public synchronized void publish(T item) {
		// TODO Auto-generated method stub
		if(ifOpen) {
			for(Subscriber<T> s: subList) {
				s.onEvent(item);
			}
		}
	}


	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		this.ifOpen = false;
	}

}
