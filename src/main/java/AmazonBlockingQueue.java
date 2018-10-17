
/**
 * the blocking queue class
 * maintain a fixed queue of tasks to be executed
 * @author yalei
 *
 * @param <T>
 */
public class AmazonBlockingQueue<T> {
	private T[] items;
	private int start;
	private int end;
	private int size;

	/**
	 * take the int as the size of the queue
	 * @param size
	 */
	public AmazonBlockingQueue(int size) {
		this.items = (T[]) new Object[size];
		this.start = 0;
		this.end = -1;
		this.size = 0;
	}

	/**
	 * put an item into the queue
	 * if the queue is full, wait until an item is took out of the queue
	 * @param item
	 */
	public synchronized void put(T item) {
		
		while(size == items.length) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}				
		int next = (end+1)%items.length;
		items[next] = item;
		end = next;		
		size++;
		if(size == 1) {
			this.notifyAll();
		}
		
	}


	/**
	 * take an item out of the queue
	 * if the queue is empty, wait until an item is put into it
	 * this method must return an item
	 * @return
	 */
	public synchronized T take() {
		
		while(size == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}

		T item = items[start];
		start = (start+1)%items.length;
		size--;
		if(size == items.length-1) {
			this.notifyAll();
		}
		return item;
	}
	
	/**
	 * try to take an item out of the queue
	 * if the queue is empty, wait for a while or until being waked up
	 * once wake up, check if the queue is still empty
	 * if not empty, take an item and return
	 * if empty, check the wait time
	 * if wait enough time, return null
	 * if not enough time, go back to wait
	 * @param timeOut
	 * @return
	 */
	public synchronized T poll(long timeOut) {
		while(size == 0) {
			try {
				long start = System.currentTimeMillis();
				this.wait(timeOut);
				long end = System.currentTimeMillis();
				if((end - start) >= timeOut && size == 0) {
					return null;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}

		T item = items[start];
		start = (start+1)%items.length;
		size--;
		if(size == items.length-1) {
			this.notifyAll();
		}
		return item;
	}


	/**
	 * return if the queue is empty
	 * @return
	 */
	public synchronized boolean isEmpty() {
		return size == 0;
	}
}
