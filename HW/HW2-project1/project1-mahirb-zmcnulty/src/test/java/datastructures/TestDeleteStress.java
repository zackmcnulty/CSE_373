package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.interfaces.IList;
import datastructures.concrete.DoubleLinkedList;

import static org.junit.Assert.assertTrue;

/**
 * This file should contain any tests that check and make sure your
 * delete method is efficient.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteStress extends TestDoubleLinkedList {
    
    @Test(timeout = 15 * SECOND)
    public void testDeleteAtEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5000000; i++) {
            list.add(i);
        }
        for (int i = 0; i < 5000000; i++) {
            list.delete(list.size() - 1);
        }
        assertEquals(0, list.size());
    }
    
    @Test(timeout = 5 * SECOND)
    public void testDeleteInMiddleIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        for (int i = 0; i < 10000; i++) {
            list.delete(list.size()/2);
        }
        assertEquals(0, list.size());
    }
    
    @Test(timeout = 15 * SECOND)
    public void testDeleteInFrontIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5000000; i++) {
            list.add(i);
        }
        for (int i = 0; i < 5000000; i++) {
            list.delete(0);
        }
        assertEquals(0, list.size());
    }
}
