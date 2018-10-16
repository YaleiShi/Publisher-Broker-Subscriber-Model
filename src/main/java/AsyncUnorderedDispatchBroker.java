import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncUnorderedDispatchBroker<T> extends BasicBroker<T> {
	private ExecutorService pool;
	
	public AsyncUnorderedDispatchBroker(int size) {
		this.pool = Executors.newFixedThreadPool(size);
	}
	
	
	
	private class AUBWorker implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
