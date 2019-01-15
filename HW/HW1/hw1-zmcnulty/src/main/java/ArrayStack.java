import java.util.EmptyStackException;

public class ArrayStack<T> implements Stack<T> {
    private static final int DEFAULT_CAPACITY = 10;
    
    private T[] data;
    private int startIndex;
    private int size;
    
    public ArrayStack() {
        this(DEFAULT_CAPACITY);
    }
    
    public ArrayStack(int capacity) {
        this.data = (T[]) new Object[capacity];
        this.startIndex = 0;
        this.size = 0;
    }
    
    @Override
    public void push(T item) {
        // If the stack is full, double its capacity
        if (this.size == this.data.length) {
           this.changeCapacity(this.data.length*2); 
        }
        
        // stores elements in a circular fashion: when end of the array is reached and if there
        // is still room in the stack, data begins being stored at front of array.
        int indexToFill = (this.startIndex + this.size) % this.data.length;
        this.data[indexToFill] = item;
        this.size++;
    }

    @Override
    public T pop() throws EmptyStackException {
        T value = this.peek(); // throws EmptyStackException if stack has no elements
        this.size--;
        
        // halves capacity of stack if stack < 1/4 full, but the
        // minimum capacity is set to the DEFAULT_CAPACITY
        if (4*this.size < this.data.length) {
            int newCapacity = Math.max(DEFAULT_CAPACITY, this.data.length/2);
            this.changeCapacity(newCapacity);
        }
        return value;
    }

    @Override
    public T peek() throws EmptyStackException {
        if (this.isEmpty()) {
            throw new EmptyStackException();
        }
        int index = (this.startIndex + this.size - 1) % this.data.length;
        return this.data[index];
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    // If the stack runs out of space, adjusts capacity to fit more data. If the stack is mostly empty,
    // reduces capacity to more efficiently store data
    // @param: newCapacity
    //             new capacity for the stack
    private void changeCapacity(int newCapacity) {
        if (newCapacity != this.data.length) {
            T[] newData = (T[]) new Object[newCapacity];
            
            // copies the elements of old array into new one, filling the new array from the front
            // while maintaining order of old array.
            for (int i = 0; i < this.size; i++) {
               int nextIndex = (this.startIndex + i) % this.data.length;
               newData[i] = this.data[nextIndex];
            }
            this.data = newData;
            this.startIndex = 0;
        }
    }
}