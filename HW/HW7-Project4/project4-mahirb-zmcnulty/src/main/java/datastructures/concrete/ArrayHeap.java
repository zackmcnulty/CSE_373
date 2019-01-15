package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    private static final int DEFAULT_CAPACITY = 10;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int count;
    private int capacity;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this(DEFAULT_CAPACITY);
    }
    
    public ArrayHeap(int capacity) {
        this.capacity = capacity;
        this.count = 0;
        this.heap = makeArrayOfT(capacity);
    }

    /**
     * This method will return a new, empty array of the given count
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        if (this.isEmpty()) {
            throw new EmptyContainerException("Heap is empty");
        }
        T value = heap[0];
        // if we only have 1 element, then there is no need to percolate on the empty list after removal
        heap[0] = heap[count - 1];
        this.count--;       
        if (this.count > 1) {
            percolateDown();
        }
        return value;
    }
    
    // reorders heap to maintain heap property which may have been damaged as a result of remove
    private void percolateDown() {
        int currIndex = 0;
        int nextSwap = minAtIndex(currIndex);
        while (nextSwap != currIndex) {
            T temp = this.heap[currIndex];
            this.heap[currIndex] = this.heap[nextSwap];
            this.heap[nextSwap] = temp;
            
            currIndex = nextSwap;
            nextSwap = minAtIndex(currIndex); 
        }
    }
    
    // returns index of min element between parent and all of its children
    private int minAtIndex(int currIndex) {
        T min = this.heap[currIndex];
        int minIndex = currIndex;
        for (int i = 1; i <= NUM_CHILDREN; i++) {
            int nextIndex = currIndex * NUM_CHILDREN + i;
            if (nextIndex < this.count && lessThan(this.heap[nextIndex], min)) {
                min = this.heap[nextIndex];
                minIndex = nextIndex;
            }
        }
        return minIndex;
    }

    @Override
    public T peekMin() {
        if (this.isEmpty()) {
            throw new EmptyContainerException("Heap is empty");
        }
        return heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Heap does not accept null entries");
        }
        this.heap[this.count] = item;
        this.percolateUp();
        this.count++;
        if (this.count == this.capacity) {
            changeHeapCapacity(2*this.capacity);
        }
    }
    
    // reorders heap to maintain heap property which may have been damaged as a result of insert
    private void percolateUp() {
        int currIndex = this.count;
        while (currIndex != 0 && lessThan(this.heap[currIndex], this.heap[(currIndex - 1) / NUM_CHILDREN])) {
            T temp = this.heap[(currIndex - 1) / NUM_CHILDREN];
            this.heap[(currIndex - 1) / NUM_CHILDREN] = this.heap[currIndex];
            this.heap[currIndex] = temp;
            currIndex = (currIndex - 1) / NUM_CHILDREN;
        }
    }

    @Override
    public int size() {
        return this.count;
    }
    
    // changes capacity of heap to be newCapacity
    private void changeHeapCapacity(int newCapacity) {
        T[] newHeap = makeArrayOfT(newCapacity);
        for (int i = 0; i < this.count; i++) {
            newHeap[i] = this.heap[i];
        }
        this.capacity = newCapacity;
        this.heap = newHeap;
    }
    
    // returns true if a is strictly less than b, returns false otherwise.
    private boolean lessThan(T a, T b) {
        return a.compareTo(b) < 0;
    }
}
