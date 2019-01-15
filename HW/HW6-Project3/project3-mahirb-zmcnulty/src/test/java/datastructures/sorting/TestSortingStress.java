package datastructures.sorting;

import misc.BaseTest;
import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.IList;
import datastructures.concrete.DoubleLinkedList;

import misc.Searcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    
    // topKSort Stress Tests
    @Test(timeout=5*SECOND)
    public void testKEquals1() {
        IList<Double> bigList = makeShuffledList(1000000); 
        Searcher.topKSort(1, bigList);
    }
    
    @Test(timeout=5*SECOND)
    public void testKEquals10() {
        IList<Double> bigList = makeShuffledList(1000000); 
        Searcher.topKSort(10, bigList);
    }
    
    @Test(timeout = 5*SECOND)
    public void testLargeK() {
        IList<Double> bigList = makeShuffledList(1000000); 
        Searcher.topKSort(bigList.size() / 2, bigList);
    }
    
    @Test(timeout = 5*SECOND)
    public void testKisListLength() {
        IList<Double> bigList = makeShuffledList(500000); 
        Searcher.topKSort(bigList.size(), bigList);   
    }
    

    // ArrayHeap Stress Tests
    
    // creates new ArrayHeap
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=5*SECOND)
    public void insertIsEfficient() {
        IPriorityQueue<Integer> bigHeap = makeInstance();
        for (int i = 0; i < 5000000; i++) {
            bigHeap.insert(i);
        } 
    }
    
    @Test(timeout=5*SECOND)
    public void insertShuffledIsEfficient() {
        makeShuffledHeap(5000000); 
    }
    
    @Test(timeout=5*SECOND)
    public void insertAndPercolateToTopEfficient() {
        IPriorityQueue<Integer> bigHeap = makeInstance();
        for (int i = 0; i < 2000000; i++) {
            bigHeap.insert(2000000 - i);
        }  
    }
    
    @Test(timeout=5*SECOND)
    public void peekMinIsEfficient() {
        IPriorityQueue<Integer> bigHeap = makeInstance();
        for (int i = 0; i < 5000000; i++) {
            bigHeap.insert(i);
        }
        for (int i = 0; i < 5000000; i++) {
            bigHeap.peekMin();
        }
    }
    
    @Test(timeout=5*SECOND)
    public void removeMinIsEfficient() {
        IPriorityQueue<Integer> bigHeap = makeInstance();
        for (int i = 0; i < 2500000; i++) {
            bigHeap.insert(i);
        }
        for (int i = 0; i < 2500000; i++) {
            bigHeap.removeMin();
            bigHeap.insert(i);
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
    
    // creates a shuffled list of the given size with random elements
    private IList<Double> makeShuffledList(int size) {
        IList<Double> l = new DoubleLinkedList<Double>();
        for (int i = 0; i < size; i++) {
            l.add(Math.random());
        }
        return l;
    }
}
