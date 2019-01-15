package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified.
 *
 * This test _extends_ your TestDoubleLinkedList class. This means that when
 * you run this test, not only will your tests run, all of the ones in
 * TestDoubleLinkedList will also run.
 *
 * This also means that you can use any helper methods defined within
 * TestDoubleLinkedList here. In particular, you may find using the
 * 'assertListMatches' and 'makeBasicList' helper methods to be useful.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteFunctionality extends TestDoubleLinkedList {
    @Test(timeout=SECOND)
    public void testExample() {
        // Feel free to modify or delete this dummy test.
        assertTrue(true);
        assertEquals(3, 3);
    }
    
    @Test(timeout=SECOND)
    public void testBasicDelete() {
        IList<String> list = this.makeBasicList();
        String data = list.delete(0);
        assertListMatches(new String[] {"b", "c"}, list);
        assertEquals(data, "a");

        IList<String> list2 = this.makeBasicList();
        String data2 = list2.delete(1);
        assertListMatches(new String[] {"a", "c"}, list2);
        assertEquals(data2, "b");

        IList<String> list3 = this.makeBasicList();
        String data3 = list3.delete(2);
        assertListMatches(new String[] {"a", "b"}, list3);
        assertEquals(data3, "c");
    }
    
    @Test(timeout=SECOND)
    public void testDeleteManyFromFront() {
        IList<Integer> list = new DoubleLinkedList<>();
        int max = 1000;
        for (int i = 0; i < max; i++) {
            list.add(i);
        }
        
        for (int j = 0; j < max; j++) {
            assertEquals(list.delete(0), j);
        }
    }
    
    @Test(timeout=SECOND)
    public void testDeleteManyFromBack() {
        IList<Integer> list = new DoubleLinkedList<>();
        int max = 1000;
        for (int i = 0; i < max; i++) {
            list.add(i);
        }
        
        for (int j = 0; j < max; j++) {
            assertEquals(list.delete(list.size() - 1), max - 1 - j);
        }
    }
    
    @Test(timeout=SECOND)
    public void testDeleteManyFromMiddle() {
        IList<Integer> list = new DoubleLinkedList<>();
        int max = 1000;
        for (int i = 0; i < max; i++) {
            list.add(i);
        }
        
        for (int j = 0; j < max; j++) {
            list.delete(list.size()/2);
            assertEquals(list.size(), max - j - 1);
        }
    }
    
    @Test(timeout=SECOND)
    public void testDeleteIndexOutOfBoundsThrowsException() {
        IList<String> list = this.makeBasicList();

        try {
            list.delete(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }

        try {
            list.delete(4);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testDeleteEmptyListThrowsException() {
        IList<String> list = new DoubleLinkedList<>();

        try {
            list.delete(0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }

        try {
            list.delete(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }  
    
    @Test(timeout=10*SECOND)
    public void testRepeatedAddBackThenDelete() {
        IList<Integer> list = new DoubleLinkedList<>();
        int max = 1000;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < max; j++) {
                list.add(j);
            }
            for (int j = 0; j < max; j++) {
                assertEquals(list.delete(list.size() - 1), max - j - 1);
            }
            for (int j = 0; j < max; j++) {
                list.add(j);
            }
            for (int j = 0; j < max; j++) {
                assertEquals(list.delete(0), j);
            }
            for(int j = 0; j < max; j++) {
                list.add(j);
                assertEquals(list.delete(0), j);
            }
            
        }
    }
    
    @Test(timeout=10*SECOND)
    public void testRepeatedInsertThenDeleteFront() {
        IList<Integer> list = new DoubleLinkedList<>();
        int max = 1000;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < max; j++) {
                list.insert(0, j);
            }
            for (int j = 0; j < max; j++) {
                assertEquals(list.delete(list.size() - 1), j);
            }
            for (int j = 0; j < max; j++) {
                list.insert(0,j);
            }
            for (int j = 0; j < max; j++) {
                assertEquals(list.delete(0), max - j - 1);
            }
            for(int j = 0; j < max; j++) {
                list.insert(0, j);
                assertEquals(list.delete(0), j);
            } 
        }
    }
}
