
import java.util.NoSuchElementException;

public class ListQueue<T> implements Queue<T> {
    private QueueNode front;
    private QueueNode back;
    private int size;
    
    private class QueueNode {
        public QueueNode next;
        public T data;
        
        public QueueNode(T data, QueueNode next) {
            this.data = data;
            this.next = next;
        }
    }
    
    public ListQueue() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }
    
    @Override
    public void add(T item) {
        if (this.isEmpty()) {
            this.front = new QueueNode(item, null);
            this.back = this.front;
        } else {
            this.back.next = new QueueNode(item, null);
            this.back = this.back.next;
        }
        this.size++; 
    }

    @Override
    public T remove() throws NoSuchElementException {
        T data = this.peek(); //throws NoSuchElementException if queue is empty
        this.front = this.front.next;
        this.size--;
        if (this.isEmpty()) {
            this.back = null;
        }
        return data;
    }

    @Override
    public T peek() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        } 
        return this.front.data;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
}