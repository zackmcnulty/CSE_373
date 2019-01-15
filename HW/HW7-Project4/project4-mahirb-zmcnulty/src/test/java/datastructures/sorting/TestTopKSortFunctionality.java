package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testKEqual0() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(0, list);
        assertEquals(0, top.size());
    }
    
    @Test(timeout=SECOND)
    public void testKEqualListSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(20, list);
        assertEquals(20, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(0 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testKLessThan0() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        try {
            Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testKGreaterThanListSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(25, list);
        assertEquals(20, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(0 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testEmptyList() {
        IList<Integer> list = new DoubleLinkedList<>();
        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(0, top.size());
    }
    
    @Test(timeout=SECOND)
    public void testUnsortedList() {
        List<Double> list = new ArrayList<Double>();
        IList<Double> iList = new DoubleLinkedList<>();
        for (int i = 0; i < 300; i++) {
            double rand = Math.random();
            list.add(rand);
            iList.add(rand);
        }
        IList<Double> top = Searcher.topKSort(200, iList);
        assertEquals(200, top.size());
        Collections.sort(list);
        
        for (int i = 100; i < 300; i++) {
            assertEquals(list.get(i), top.get(i - 100));
        } 
    }
    
    @Test(timeout=SECOND)
    public void testLeavesListUnchanged() {
        IList<Integer> l1 = new DoubleLinkedList<>();
        IList<Integer> copy = new DoubleLinkedList<>();
        for (int i = 0; i < 300; i++) {
            l1.add(i);
            copy.add(i);
        }
        Searcher.topKSort(50, l1);
        assertEquals(l1.size(), copy.size());
        for (int i = 0; i < 300; i++) {
            assertEquals(l1.get(i), copy.get(i));
        }
    }
}