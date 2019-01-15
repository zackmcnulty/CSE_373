import static org.junit.Assert.*;
import java.util.NoSuchElementException;

import org.junit.Test;


public abstract class TestQueue {

    abstract Queue<Integer> getQueue();
    
    @Test
    public final void testAddOne() {
        Queue<Integer> queue = getQueue();
        queue.add(1);
        assertEquals(1, queue.size());
    }

    @Test
    public final void testAddSeveral() {
        Queue<Integer> queue = getQueue();
        queue.add(3);
        queue.add(2);
        queue.add(1);
        assertEquals(3, queue.size());
    }

    @Test
    public final void testRemoveOne() {
        Queue<Integer> queue = getQueue();
        queue.add(1);
        assertEquals(1, (int) queue.remove());
    }

    @Test
    public final void testRemoveSeveral() {
        Queue<Integer> queue = getQueue();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.remove();
        queue.remove();
        assertEquals(3, (int) queue.remove());
    }

    @Test
    public final void testRemoveEmpty() {
        Queue<Integer> queue = getQueue();
        boolean threwException = false;
        try {
            queue.remove();
        } catch (NoSuchElementException e) {
            threwException = true;
        }
        assertTrue(threwException);
    }
    
    @Test
    public final void testRemoveMany() {
        Queue<Integer> queue = getQueue();
        for (int i = 0; i < 100; ++i) {
            queue.add(i);
        }
        for (int i = 0; i < 100; ++i) {
            queue.remove();
        }
        assertEquals(0, queue.size());
    }
    
    @Test
    public final void testRemoveTooManyThrows() {
        Queue<Integer> queue = getQueue();
        for (int i = 0; i < 20; ++i) {
            queue.add(i);
        }
        for (int i = 0; i < 20; ++i) {
            queue.remove();
        }
        boolean threwException = false;
        try {
            queue.remove();
        } catch (NoSuchElementException e) {
            threwException = true;
        }
        assertTrue(threwException);
    }
    
    @Test
    public final void testRemoveTooManySize() {
        Queue<Integer> queue = getQueue();
        for (int i = 0; i < 20; ++i) {
            queue.add(i);
        }
        for (int i = 0; i < 50; ++i) {
            try {
                queue.remove();
            } catch (Exception e) {
                // Do nothing - we want to test if size stays at 0
            }
        }
        assertEquals(0, queue.size());
    }
    
    @Test
    public final void testAddAfterRemoveTooMany() {
        Queue<Integer> queue = getQueue();
        for (int i = 0; i < 20; ++i) {
            queue.add(i);
        }
        for (int i = 0; i < 50; ++i) {
            try {
                queue.remove();
            } catch (Exception e) {
                // Do nothing
            }
        }
        for (int i = 0; i < 10; ++i) {
            queue.add(i);
        }
        assertEquals(10, queue.size());
    }
    
    @Test
    public final void testAddAfterRemoveTooManyValue() {
        Queue<Integer> queue = getQueue();
        for (int i = 0; i < 20; ++i) {
            queue.add(i);
        }
        for (int i = 0; i < 50; ++i) {
            try {
                queue.remove();
            } catch (Exception e) {
                // Do nothing
            }
        }
        for (int i = 0; i < 10; ++i) {
            queue.add(i);
        }
        assertEquals(0, (int) queue.peek());
    }
    
    @Test(expected = NoSuchElementException.class)
    public final void testPeekEmpty() {
        Queue<Integer> queue = getQueue();
        queue.peek();
    }
    

    @Test
    public final void testIsEmptyTrue() {
        Queue<Integer> queue = getQueue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public final void testIsEmptyFalse() {
        Queue<Integer> queue = getQueue();
        queue.add(1);
        assertFalse(queue.isEmpty());
    }

    @Test
    public final void testAddRemoveSequence() {
        Queue<Integer> queue = getQueue();

        queue.add(1);
        queue.add(2);
        queue.add(3);
        assertEquals((int) queue.remove(), 1);
        assertEquals(queue.size(), 2);
        assertEquals((int) queue.remove(), 2);
        assertEquals(queue.size(), 1);
        assertEquals((int) queue.remove(), 3);
        assertEquals(queue.size(), 0);
        assertTrue(queue.isEmpty());
    }


    @Test
    public final void testPeek() {
        Queue<Integer> queue = getQueue();
        // No matter how much we add, the front (peek) should not change
        for (int i = 0; i < 10; ++i) {
            queue.add(i);
            assertEquals((int) queue.peek(), 0);
        }
    }
    
    @Test
    public final void testPeekThrows() {
        Queue<Integer> queue = getQueue();
        boolean threwException = false;
        try {
            queue.peek();
        } catch (NoSuchElementException e) {
            threwException = true;
        }
        assertTrue(threwException);
    }

    @Test
    public final void testAddMany() {
        Queue<Integer> queue = getQueue();
        for (int i = 0; i < 1000; ++i) {
            queue.add(0);
        }
        assertEquals(queue.size(), 1000);
    }
    
    // Tests I have added
    @Test
    public final void testFIFO() {
        Queue<Integer> queue = getQueue();
        for (int i = 0; i < 1000; ++i) {
            queue.add(i);
        }
        boolean fifo = true;
        for (int i = 0; i < 1000; i++) {
            if (queue.remove() != i) {
                fifo = false;   
            }
        }
        assertTrue(fifo);
    }

}
