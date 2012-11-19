package util;

import java.util.Iterator;


/** 
 * Synchronized thread safe Queue.
 * Synchronized over both pushing and 
 * popping
 * @author Brendan Galea
 *
 */
class  SyncedQueue<E> {
	

	// Singly linked Queue
	  private Node head = new Node(null); 
	  private Node tail = head; 

	  // Synchronization Locks
	  private final Object pollLock = new Object();
	  private final Object putLock = new Object();

	  // push item onto queue
	  public void push(E x) {
	    Node node = new Node(x);
	    synchronized (putLock) {    
	      synchronized (tail) {
	        tail.next = node;         
	        tail = node;
	      }
	    }
	  }
	  
	 
	  // pop item off of queue
	  @SuppressWarnings("unchecked")
	public E pop() {       
	    synchronized (pollLock) {
	      synchronized (head) {
	        E obj = null;
	        Node first = head.next;  
	        if (first != null) {
	          obj = (E)first.object;
	          first.object = null;   
	          head = first;            
	        }
	        return obj;
	      }
	    }
	  }
	  
	  // peek at next item on queue
	  @SuppressWarnings("unchecked")
	public E peek() {       
	    synchronized (pollLock) {
	      synchronized (head) {
	        E obj = null;
	        Node first = head.next;  
	        if (first != null) 
	          obj = (E)first.object;
	        return obj;
	      }
	    }
	  }
	  
	  // Local node class
	  static class Node{          
	    Object object;
	    Node next = null;

	    Node(Object x) { object = x; }
	  }
	}


class CircularFloatArray {
	
	private int start, index; 
	
	private float[] array; 
	
        public int size(){
            if (index == start)
                return 0; 
            
            return (100+index-start)%100; 
        }
	
	public CircularFloatArray(){
		array = new float[100]; 
	}
	
	public void add(float f){
		array[index] = f; 
		index++; 
		index%=array.length; 
		
		if (index == start){
			start++;
			start%= array.length; 
		}	
	}
	
	public Iterator<Float> iterator(){
		return new Iterator<Float>(){

			int i = start; 
			
			public boolean hasNext() {
				return i != index; 
			}

			
			public Float next() {
				Float f = array[i]; 
				i++;
                                i%=100; 
				return f; 
			}

			public void remove() {}
		}; 
	}
	
	
	
}

public class GraphData {

	private final int LWMA = 0; 
	private final int EMA = 1; 
	private final int TMA = 2; 
	private final int SMA = 3;
	private final int PRICE = 8; 
 
	private int time = 0; 
	
	private SyncedQueue<Float>[]  queues; 
	private CircularFloatArray[] arrays;
	private Float[] next; 
	
	@SuppressWarnings("unchecked")
	public GraphData(){
		queues = new SyncedQueue[9]; 
		arrays = new CircularFloatArray[9]; 
		next = new Float[9]; 
		
		for (int i = 0; i < 9; i++){
			queues[i] = new SyncedQueue<Float>(); 
			arrays[i] = new CircularFloatArray(); 
		}
	}
	
	public void sync(){
		
		/*
		 * Tries to get equal number as many items as possible
		 *  get next item from all queues,
		 * if possible, adds them to circular array 
		 * and increments time otherwise
		 * r
		 */
		while (getNext()){
			for (int i = 0; i < arrays.length; i++)
				arrays[i].add(next[i]); 
			time++; 
			next = new Float[9]; 
		}
	}
	
        public int getSize() {
            return arrays[PRICE].size(); 
        }
        
        public int getTime(){
            return time; 
        }

	private boolean getNext(){
		
		// tries to get next for all queues to ensure synchronization
		boolean hasNext = true; 
		for (int i = 0; i < next.length; i++)
			if (next[i] == null){
				
				next[i] = queues[i].pop(); 
				if (next[i] == null)
					hasNext = false; 
			}
		
		return hasNext; 
	}
	
	// ** Linear Weighted Moving Average ** 
	public Iterator<Float> getFastLWMA(){
		return arrays[LWMA+4].iterator(); 
	}
	
	public Iterator<Float> getSlowLWMA(){
		
		return arrays[LWMA].iterator(); 
	}
	
	public void pushFastLWMA(Float f){
		queues[LWMA+4].push(f); 
	}
	
	public void pushSlowLWMA(Float f){
		queues[LWMA].push(f); 

	}
	
	
	// ** Exponential Moving Average ** 
	public Iterator<Float> getFastEMA(){
		
		return arrays[EMA+4].iterator();
	}
	
	public Iterator<Float> getSlowEMA(){
		
		return arrays[EMA].iterator();
	}

	public void pushFastEMA(Float f){
		queues[EMA+4].push(f); 

	}
	
	public void pushSlowEMA(Float f){
		queues[EMA].push(f);
	}
	
	// ** Simple Moving Average ** 
	public Iterator<Float> getFastSMA(){
		
		return arrays[SMA+4].iterator();
	}
	
	public Iterator<Float> getSlowSMA(){
		
		return arrays[SMA].iterator();
	}
	
	public void pushFastSMA(Float f){
		queues[SMA+4].push(f); 

	}
	
	public void pushSlowSMA(Float f){
		queues[SMA].push(f);
	}
	
	// ** Triangular Moving Average ** 
	public Iterator<Float> getFastTMA(){
		
		return arrays[TMA+4].iterator();
	}
	
	public Iterator<Float> getSlowTMA(){
		
		return arrays[TMA].iterator();
	}
	
	public void pushFastTMA(Float f){
		queues[TMA+4].push(f); 

	}
	
	public void pushSlowTMA(Float f){
		queues[TMA].push(f);
	}
	
	// ** Price **
	
	public Iterator<Float> getPrice(){
		return arrays[PRICE].iterator(); 
	}
	
	public void pushPrice(Float f){
		queues[PRICE].push(f); 
	}
        
       
}
