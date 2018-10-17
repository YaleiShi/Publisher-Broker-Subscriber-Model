
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * AsyncUnorderedDispatchBroker class,
 * use thread pool to store and execute the task
 * @author yalei
 *
 * @param <T>
 */
public class AsyncUnorderedDispatchBroker<T> extends BasicBroker<T> {
	private ExecutorService pool;
	private long maxTime;
	
	/**
	 * take the int as size to initiate the thread pool
	 * @param size
	 */
	public AsyncUnorderedDispatchBroker(int size, long maxTime) {
		this.maxTime = maxTime;
		this.pool = Executors.newFixedThreadPool(size);
	}
	
	/**
	 * called by the publisher to put the task into the thread pool
	 */
	public void publish(T item) {
		if(ifOpen) {
			this.pool.execute(new AUBWorker(item));
		}
	}
	
	/**
	 * shut down the broker
	 * first change the ifOpen boolean,
	 * then call the pool to shutdown
	 * finally wait the pool to execute all the tasks
	 */
	public void shutdown() {
		ifOpen = false;
		System.out.println("shutting down");
		this.pool.shutdown();
		System.out.println("waiting termination");
		try {
			this.pool.awaitTermination(maxTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * the inner worker class to call onEvent of every subscribers
	 * @author yalei
	 *
	 */
	private class AUBWorker implements Runnable{
		private T item;
		public AUBWorker(T item) {
			this.item = item;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			for(Subscriber sub: subList) {
				sub.onEvent(item);
			}
		}
		
	}
}
