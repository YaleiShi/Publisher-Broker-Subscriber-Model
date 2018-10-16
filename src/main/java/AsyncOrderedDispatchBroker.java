
public class AsyncOrderedDispatchBroker<T> extends BasicBroker<T>{
	private AmazonBlockingQueue<T> queue;
	private Thread deliver;
	private long timeOut;
	
	public AsyncOrderedDispatchBroker(int queueSize, long timeOut) {
		this.timeOut = timeOut;
		queue = new AmazonBlockingQueue<T>(queueSize);
		deliver = new Thread(new AOBWorker());
		deliver.start();
	}
	
	public void publish(T item) {
		if(ifOpen) {
			queue.put(item);
		}
	}
	
	public void shutdown() {
		this.ifOpen = false;
		
		try {
			this.deliver.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
