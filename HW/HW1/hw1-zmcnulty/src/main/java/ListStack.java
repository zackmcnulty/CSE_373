import java.util.EmptyStackException;

public class ListStack<T> implements Stack<T> {

    private StackNode top;
    private int size;

    private class StackNode {
        public T value;
        public StackNode next;

        public StackNode(T value, StackNode next) {
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void push(T item) {
        this.top = new StackNode(item, this.top);
        this.size++;
    }

    @Override
    public T pop() throws EmptyStackException {
        // We don't need to explicitly throw EmptyStackException
        // since peek() already does.
        T temp = this.peek();
        this.top = this.top.next;
        this.size--;
        return temp;
    }

    @Override
    public T peek() throws EmptyStackException {
        if (isEmpty())
            throw new EmptyStackException();
        return this.top.value;
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
