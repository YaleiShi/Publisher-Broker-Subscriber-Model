import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncUnorderedDispatchBroker<T> extends BasicBroker<T> {
	private ExecutorService pool;
//	private int count;
	
	public AsyncUnorderedDispatchBroker(int size) {
//		count = 0;
		this.pool = Executors.newFixedThreadPool(size);
	}
	
	public void publish(T item) {
		if(ifOpen) {
//			System.out.print("");
			this.pool.execute(new AUBWorker(item));
		}
	}
	
	public void shutdown() {
		ifOpen = false;
		System.out.println("shutting down");
		this.pool.shutdown();
		System.out.println("wait termination");
		try {
			this.pool.awaitTermination(10000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
