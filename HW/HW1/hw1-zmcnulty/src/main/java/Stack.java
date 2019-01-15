
import java.util.EmptyStackException;

public interface Stack<T> {
    /**
     * Adds an item to the top of the stack.
     * 
     * @param item
     *            The item to add.
     */
    public void push(T item);

    /**
     * Remove and return the top item on the stack.
     * 
     * @return The top item on the stack
     * @throws EmptyStackException
     *             If the stack is empty.
     */
    public T pop() throws EmptyStackException;

    /**
     * Gets the top element of the stack without removing it.
     * 
     * @return The top element of the stack.
     * @throws EmptyStackException
     *             If the stack is empty.
     */
    public T peek() throws EmptyStackException;

    /**
     * Return the number of elements in the stack.
     * 
     * @return The number of elements in the stack.
     */
    public int size();

    /**
     * Returns true if the stack is empty, false otherwise.
     * 
     * @return true if size is 0, false otherwise.
     */
    public boolean isEmpty();
}
