
public class AmazonBlockingQueue<T> {
	private T[] items;
	private int start;
	private int end;
	private int size;

	public AmazonBlockingQueue(int size) {
		this.items = (T[]) new Object[size];
		this.start = 0;
		this.end = -1;
		this.size = 0;
	}

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


	public synchronized boolean isEmpty() {
		return size == 0;
	}
}
