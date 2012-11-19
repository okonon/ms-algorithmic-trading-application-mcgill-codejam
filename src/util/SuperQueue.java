/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/** 
 * Synchronized thread safe Queue.
 * Synchronized over both pushing and 
 * popping
 * @author Brendan Galea
 *
 */
public class  SuperQueue<E> {
	

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
