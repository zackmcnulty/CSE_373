
import java.util.NoSuchElementException;

public interface Queue<T> {
    /**
     * Adds an item to the back of the queue.
     * 
     * @param item
     *            The item to add to the queue.
     */
    public void add(T item);

    /**
     * Removes an item from the front of the queue.
     * 
     * @return The item at the front of the queue.
     * @throws NoSuchElementException
     *             If the queue is empty.
     */
    public T remove() throws NoSuchElementException;

    /**
     * Returns the item at the front of the queue without removing it.
     * 
     * @return The item at the front of the queue.
     * @throws NoSuchElementException
     */
    public T peek() throws NoSuchElementException;

    /**
     * Returns the number of items in the queue.
     * 
     * @return The number of items in the queue.
     */
    public int size();

    /**
     * Returns whether or not the queue is empty.
     * 
     * @return false in the size of the queue is 0, true otherwise
     */
    public boolean isEmpty();
}
