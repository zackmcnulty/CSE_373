package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;
import misc.exceptions.EmptyContainerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    // tests we added below here
    
    public void testInsertBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(0);
        heap.insert(2);
        heap.insert(-1);
        heap.insert(5);
        assertEquals(heap.size(), 4);
    }
    
    public void testRemoveBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(5);
        heap.insert(1);
        assertEquals(heap.removeMin(), 1);
        assertEquals(heap.removeMin(), 5);
    }
     
    // adding/removing elements should update the size
    @Test(timeout=SECOND)
    public void testUpdateSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 100; i++) {
            heap.insert(i);
            assertEquals(i+1, heap.size());
        }
        for (int i = 0; i < 100; i++) {
            heap.removeMin();
            assertEquals(100 - i - 1, heap.size());
        }
        for (int i = 0; i < 100; i++) {
            heap.insert(i);
            assertEquals(heap.size(), 1);
            assertEquals(heap.removeMin(), i);
            assertEquals(heap.size(), 0);
        }
    }
    
    //peak should not change size or current min (requires insert to work as well)
    @Test(timeout=SECOND)
    public void peekLeavesStateUnchanged() {
        int cap = 100;
        IPriorityQueue<Integer> heap = this.makeShuffledHeap(cap);
        int min = heap.peekMin();
        for (int i = 0; i < 100; i++) {
            assertEquals(heap.peekMin(), min);
            assertEquals(heap.size(), cap);
        }
    }
    
    //should throw emptyContainedException if we attempt to remove from empty heap
    @Test(timeout=SECOND)
    public void removeOnEmptyHeapThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // do nothing; exception was properly thrown
        } 
    }
    
    //should throw emptyContainedException if we attempt to peek from empty heap
    @Test(timeout=SECOND)
    public void peekOnEmptyHeapThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // do nothing; exception was properly thrown
        } 
    }
    
    //should throw IllegalArgumentException if we attempt add a null key
    @Test(timeout=SECOND)
    public void insertNullThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // do nothing; exception was properly thrown
        } 
    }
    
    // if I call peak, and then call remove, I should get the same element.
    @Test(timeout=SECOND)
    public void peakAndRemoveAreSame() {
        int cap = 100;
        IPriorityQueue<Integer> heap = this.makeShuffledHeap(cap);
        for (int j = 0; j < cap; j++) {
            int temp = heap.peekMin();
            assertEquals(temp, heap.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void insertAndRemoveFew() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1; i < 10; i++) {
            for (int j = 0; j < i; j++) {
                heap.insert(10*i + j);
            }
            for (int j = 0; j < i; j++) {
                assertEquals(heap.removeMin(), 10*i + j);
            }
        }
    }
    
    // tests if heap can handle being emptied repeatedly.
    @Test(timeout=SECOND)
    public void repeatedAddAndRemove() {
        int cap = 100;
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < cap; i++) {
            heap.insert(i);
            assertEquals(heap.size(), 1);
            assertEquals(heap.removeMin(), i);
            assertEquals(heap.size(), 0);
        }
        for (int i = 0; i < 10; i++) {
            heap = this.makeShuffledHeap(cap);
            for (int j = 0; j < cap; j++) {
                assertEquals(heap.size(), cap - j);
                assertEquals(heap.removeMin(), j);
                assertEquals(heap.size(), cap - j - 1);
            }
        }

    }
    
    //updates size in case of repeated elements; does not delete repeated elements from array
    @Test(timeout=SECOND)
    public void handlesRepeatedElements() {
        IPriorityQueue<Integer> heap = this.makeShuffledHeap(100);
        for (int i = 0; i < 100; i++) {
            heap.insert(0);
            assertEquals(heap.size(), 101 + i);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(heap.removeMin(), 0);
        }
    }
    
    
    // tests whether percolate up works properly all the way to top
    @Test(timeout=SECOND)
    public void insertNewSmallestElements() {
        IPriorityQueue<Integer> heap = this.makeShuffledHeap(100);
        for (int i = -1; i > -50; i--) {
            heap.insert(i);
            assertEquals(heap.peekMin(), i);
        }
    }
    
    
    // creates a heap of the given size, where elements 0 through size - 1 are added in random order
    private IPriorityQueue<Integer> makeShuffledHeap(int size) {
        IPriorityQueue<Integer> heap = this.makeInstance();
        List<Integer> l = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            l.add(i);
        }
        Collections.shuffle(l);
        for (int next : l) {
            heap.insert(next);
        }
        return heap;
    }   
}