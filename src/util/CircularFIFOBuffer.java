package util;

import java.util.LinkedList;

/**
 * "First in first out" buffer that removes the head to create space when it is full
 * 
 * Adapted from
 * stackoverflow.com/questions/5498865/size-limited-queue-that-holds-last-n-elements-in-java
 *
 * @author Team Gredona
 */
public class CircularFIFOBuffer<E> extends LinkedList<E> {

    private int limit;

    public CircularFIFOBuffer(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) {
            super.remove();
        }
        return true;
    }
}
