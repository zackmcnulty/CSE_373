public class TestListStack extends TestStack {

    @Override
    Stack<Integer> getStack() {
        return new ListStack<Integer>();
    }

}
