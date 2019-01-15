import static org.junit.Assert.*;

import java.util.EmptyStackException;

import org.junit.Test;


public abstract class TestStack {

    abstract Stack<Integer> getStack();
    
    @Test
    public final void testAddOne() {
        Stack<Integer> stack = getStack();
        stack.push(1);
        assertEquals(1, stack.size());
    }

    @Test
    public final void testAddSeveral() {
        Stack<Integer> stack = getStack();
        stack.push(3);
        stack.push(2);
        stack.push(1);
        assertEquals(3, stack.size());
    }

    @Test
    public final void testRemoveOne() {
        Stack<Integer> stack = getStack();
        stack.push(1);
        assertEquals(1, (int) stack.pop());
    }

    @Test
    public final void testRemoveSeveral() {
        Stack<Integer> stack = getStack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.pop();
        stack.pop();
        assertEquals(1, (int) stack.pop());
    }

    @Test
    public final void testRemoveEmpty() {
        Stack<Integer> stack = getStack();
        boolean threwException = false;
        try {
            stack.pop();
        } catch (EmptyStackException e) {
            threwException = true;
        }
        assertTrue(threwException);
    }
    
    @Test
    public final void testRemoveMany() {
        Stack<Integer> stack = getStack();
        for (int i = 0; i < 100; ++i) {
            stack.push(i);
        }
        for (int i = 0; i < 100; ++i) {
            stack.pop();
        }
        assertEquals(0, stack.size());
    }
    
    @Test
    public final void testRemoveTooManyThrows() {
        Stack<Integer> stack = getStack();
        for (int i = 0; i < 20; ++i) {
            stack.push(i);
        }
        for (int i = 0; i < 20; ++i) {
            stack.pop();
        }
        boolean threwException = false;
        try {
            stack.pop();
        } catch (EmptyStackException e) {
            threwException = true;
        }
        assertTrue(threwException);
    }
    
    @Test
    public final void testRemoveTooManySize() {
        Stack<Integer> stack = getStack();
        for (int i = 0; i < 20; ++i) {
            stack.push(i);
        }
        for (int i = 0; i < 50; ++i) {
            try {
                stack.pop();
            } catch (Exception e) {
                // Do nothing - we want to test if size stays at 0
            }
        }
        assertEquals(0, stack.size());
    }
    
    @Test
    public final void testAddAfterRemoveTooMany() {
        Stack<Integer> stack = getStack();
        for (int i = 0; i < 20; ++i) {
            stack.push(i);
        }
        for (int i = 0; i < 50; ++i) {
            try {
                stack.pop();
            } catch (Exception e) {
                // Do nothing
            }
        }
        for (int i = 0; i < 10; ++i) {
            stack.push(i);
        }
        assertEquals(10, stack.size());
    }
    
    @Test
    public final void testAddAfterRemoveTooManyValue() {
        Stack<Integer> stack = getStack();
        for (int i = 0; i < 20; ++i) {
            stack.push(i);
        }
        for (int i = 0; i < 50; ++i) {
            try {
                stack.pop();
            } catch (Exception e) {
                // Do nothing
            }
        }
        for (int i = 0; i < 10; ++i) {
            stack.push(i);
        }
        assertEquals(9, (int) stack.peek());
    }
    
    @Test(expected = EmptyStackException.class)
    public final void testPeekEmpty() {
        Stack<Integer> stack = getStack();
        stack.peek();
    }
    

    @Test
    public final void testIsEmptyTrue() {
        Stack<Integer> stack = getStack();
        assertTrue(stack.isEmpty());
    }
    
    @Test
    public final void testIsEmptyFalse() {
        Stack<Integer> stack = getStack();
        stack.push(1);
        assertFalse(stack.isEmpty());
    }

    @Test
    public final void testAddRemoveSequence() {
        Stack<Integer> stack = getStack();

        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals((int) stack.pop(), 3);
        assertEquals(stack.size(), 2);
        assertEquals((int) stack.pop(), 2);
        assertEquals(stack.size(), 1);
        assertEquals((int) stack.pop(), 1);
        assertEquals(stack.size(), 0);
        assertTrue(stack.isEmpty());
    }


    @Test
    public final void testPeek() {
        Stack<Integer> stack = getStack();
        // The top should change every time
        for (int i = 0; i < 10; ++i) {
            stack.push(i);
            assertEquals((int) stack.peek(), i);
        }
    }
    
    @Test
    public final void testPeekThrows() {
        Stack<Integer> stack = getStack();
        boolean threwException = false;
        try {
            stack.peek();
        } catch (EmptyStackException e) {
            threwException = true;
        }
        assertTrue(threwException);
    }

    @Test
    public final void testAddMany() {
        Stack<Integer> stack = getStack();
        for (int i = 0; i < 1000; ++i) {
            stack.push(0);
        }
        assertEquals(stack.size(), 1000);
    }
    
    // Tests I have added:
    
    @Test
    public final void testLIFO() {
        Stack<Integer> stack = getStack();
        for (int i = 0; i < 10; i++) {
            stack.push(i);
        }
        int cur = 9;
        boolean stackWorks = true;
        while (!stack.isEmpty()) {
            if(stack.pop() != cur) {
                stackWorks = false;
            }
            cur--;
        }
        assertTrue(stackWorks);
    }
    
    @Test
    public final void testLIFOafterResize() {
        Stack<Integer> stack = getStack();
        int max = 1000;
        for (int i = 0; i < max + 1; i++) {
            stack.push(i);
        }
        int cur = max;
        boolean stackWorks = true;
        while (!stack.isEmpty()) {
            if(stack.pop() != cur) {
                stackWorks = false;
            }
            cur--;
        }
        assertTrue(stackWorks);
    }
}
