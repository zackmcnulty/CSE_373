package datastructures.concrete;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }
    
    public DoubleLinkedList(Node<T> start) {
        this.front = start;
        this.back = start;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (this.isEmpty()) {
            this.front = new Node<T>(item);
            this.back = this.front;
        } else {
            this.back.next = new Node<T>(this.back, item, null);
            this.back = this.back.next;
        }
        this.size++;
    }

    @Override
    public T remove() throws EmptyContainerException {
        if (this.isEmpty()) {
            throw new EmptyContainerException();
        }
        T item = this.back.data;
        if (this.size() == 1) {
            this.front = null;
            this.back = null;
        } else {
            this.back = this.back.prev;
            this.back.next = null;
        }
        this.size--;
        return item;
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        return getNode(index).data;
    }
    
    // returns the node associated with the given index; does not throw
    private Node<T> getNode(int index) {
        Node<T> current;
        if (index < this.size() / 2) { 
            current = this.front;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = this.back;
            for (int i = this.size() - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    @Override
    public void set(int index, T item) throws IndexOutOfBoundsException {
        this.delete(index); // delete checks indexOutOfBounds internally
        this.insert(index, item);
    }

    @Override
    public void insert(int index, T item) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size() + 1) {
            throw new IndexOutOfBoundsException();
        }
        if (index == this.size()) {
            this.add(item); //increments size on its own
        } else { 
            if (index == 0) {
                // note: never enters this case if list is empty (enters first if)
                // don't have to worry about updating back or null pointer exceptions
                this.front = new Node<T>(null, item, front);
                this.front.next.prev = this.front;
            } else {
                // entering this, I know any node I add will have a node on either side.
                // don't have to worry about null-pointer exceptions;
                Node<T> last = this.getNode(index - 1);
                Node<T> node = new Node<T>(last, item, last.next);
                last.next = node;
                node.next.prev = node;
            }
            this.size++;
        }
    }

    @Override
    public T delete(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        T temp;
        if (index == this.size() - 1) {
            return this.remove(); //decrements size on its own, so we can exit method early
        } else if (index == 0) {
            temp = this.front.data;
            this.front = this.front.next;
            if (this.front != null) {
                this.front.prev = null;
            } else {
                this.back = null; // if front is null, you deleted the only element in list
            }
        } else {
            // If we enter this case, we know the node at the given index is not the first
            // or last node in list; thus has nodes to either side of it.
            // make previous node point to next, and next to previous, removing all
            // pointers to node of interest and thus deleting it.
            Node<T> node = this.getNode(index);
            temp = node.data;
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        this.size--;
        return temp;
    }

    @Override
    public int indexOf(T item) {
        Iterator<T> it = iterator();
        int index = 0;
        while (it.hasNext()) {
            T next = it.next();
            // cannot compare null values with .equals
            if (item == null && next == null || item != null && item.equals(next)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        return indexOf(other) != -1;
    }
    

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return this.current != null; 
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() throws NoSuchElementException {
            if (this.current == null) {
                throw new NoSuchElementException();
            }
            T temp = this.current.data;
            this.current = this.current.next;
            return temp;
        }
    }
}