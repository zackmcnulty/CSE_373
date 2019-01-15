public class TestListQueue extends TestQueue {

    @Override
    Queue<Integer> getQueue() {
        return new ListQueue<Integer>();
    }

}
