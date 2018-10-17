
/**
 * AsyncOrderedDispatchBroker class
 * implement the blocking queue to store the item
 * use one thread to keep delivering the item to the subscribers
 * @author yalei
 *
 * @param <T>
 */
public class AsyncOrderedDispatchBroker<T> extends BasicBroker<T>{
	private AmazonBlockingQueue<T> queue;
	private Thread deliver;
	private long timeOut;
	
	/**
	 * take the queue size to new the blocking queue
	 * and store the max waiting time info
	 * start the delivery thread
	 * @param queueSize
	 * @param timeOut
	 */
	public AsyncOrderedDispatchBroker(int queueSize, long timeOut) {
		this.timeOut = timeOut;
		queue = new AmazonBlockingQueue<T>(queueSize);
		deliver = new Thread(new AOBWorker());
		deliver.start();
	}
	
	/**
	 * called by the publisher,
	 * check if the broker is open
	 * and put the item into the queue
	 * if the queue is full, the publisher will block and wait
	 */
	public void publish(T item) {
		if(ifOpen) {
			queue.put(item);
		}
	}
	
	/**
	 * close the broker by change the if open
	 * then call the deliver.join to wait for the delivery complete
	 */
	public void shutdown() {
		this.ifOpen = false;
		
		try {
			this.deliver.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * the inner class of worker which would deliver the item to the subscribers
	 * @author yalei
	 *
	 */
	private class AOBWorker implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(ifOpen || !queue.isEmpty()) {
				T item = queue.poll(timeOut);
				if(item != null) {
					for(Subscriber<T> sub: subList) {
						sub.onEvent(item);
					}
				}
				
			}
		}
		
	}
}
