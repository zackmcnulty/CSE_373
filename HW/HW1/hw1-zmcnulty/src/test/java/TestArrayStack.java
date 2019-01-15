public class TestArrayStack extends TestStack {

    @Override
    Stack<Integer> getStack() {
        return new ArrayStack<Integer>();
    }

}
