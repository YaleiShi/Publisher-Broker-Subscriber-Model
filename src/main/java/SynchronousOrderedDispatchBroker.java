
import java.util.ArrayList;

/**
 * SynchronousOrderedDispatchBroker class,
 * let the publish directly call onEvent
 * after finish the onEvent, the publish will return
 * @author yalei
 *
 * @param <T>
 */
public class SynchronousOrderedDispatchBroker<T> extends BasicBroker<T> {
	
	/**
	 * take no parameter into the constructor
	 * only call super class constructor to initiate the subscriber list and ifOpen
	 */
	public SynchronousOrderedDispatchBroker() {
		super();
	}
	
	/**
	 * synchronized method
	 * avoid disturbed by other publisher
	 * directly call onEvent, after finish it, return
	 */
	public synchronized void publish(T item) {
		// TODO Auto-generated method stub
		if(ifOpen) {
			for(Subscriber<T> s: subList) {
				s.onEvent(item);
			}
		}
	}


	/**
	 * shutdown just change the boolean of ifOpen
	 */
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		this.ifOpen = false;
	}

}
