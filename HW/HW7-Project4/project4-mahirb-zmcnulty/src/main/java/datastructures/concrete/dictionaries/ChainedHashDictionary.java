package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private static final int DEFAULT_CAPACITY = 13;
    
    private int capacity;
    private IDictionary<K, V>[] chains;
    private int totalSize;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this(DEFAULT_CAPACITY);
    }
    
    public ChainedHashDictionary(int capacity) {
        this.totalSize = 0;
        this.capacity = capacity;
        this.chains = makeArrayOfChains(capacity);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    private int hashKey(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode());
    }
    
    //throws noSuchKeyException
    @Override
    public V get(K key) throws NoSuchKeyException{
        int position = hashKey(key) % this.capacity;
        IDictionary<K, V> dict = chains[position];
        if (dict == null) { // no keys have hashed here, so certainly the key does not exist
            throw new NoSuchKeyException();
        }
        return dict.get(key); //throws noSuchKeyException if key not found in given chain
    }

    @Override
    public void put(K key, V value) {
        int position = hashKey(key) % this.capacity; //resize maybe? ask later
        IDictionary<K, V> dict = chains[position];
        if (dict == null) {
            chains[position] = new ArrayDictionary<K, V>();
            dict = chains[position];
        }
        int sizeBefore = dict.size();
        dict.put(key, value);
        if (sizeBefore != dict.size()) {
            this.totalSize++;
        }
        double loadFactor = this.totalSize / this.capacity;
        if (loadFactor >= 1) {
            changeCapacity(this.capacity*3 + 7);
        }
    }
    
    private void changeCapacity(int newCapacity) {
        IDictionary<K, V>[] newChains = makeArrayOfChains(newCapacity);
        Iterator<KVPair<K, V>> it = iterator();
        while (it.hasNext()) {
            KVPair<K, V> next = it.next();
            int pos = hashKey(next.getKey()) % newChains.length;
            if (newChains[pos] == null) {
                newChains[pos] = new ArrayDictionary<K, V>();
            }
            newChains[pos].put(next.getKey(), next.getValue());
        }
        this.chains = newChains;
        this.capacity =  newCapacity;
    }

    //throws noSuchKeyException
    @Override
    public V remove(K key) throws NoSuchKeyException {
        int position = hashKey(key) % this.capacity;
        IDictionary<K, V> dict = chains[position];
        if (dict == null) {
            throw new NoSuchKeyException();
        }
        this.totalSize--;
        return dict.remove(key); //throws NoSuchKeyException
    }
    
    @Override
    public boolean containsKey(K key) {
        int position = hashKey(key) % this.capacity;
        IDictionary<K, V> dict = chains[position];
        if (dict == null) {
            return false;
        }
        return dict.containsKey(key);
    }

    @Override
    public int size() {
        return this.totalSize;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains, this.totalSize); //are we allowed to add extra parameter?
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *    
     *    Invariants:
     *    step < totalSize
     *    currentDict != null
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int totalSize;
        private int step;
        private Iterator<KVPair<K, V>> currentDict;
        private int currentIndex; // keeps track of where latest dictionary was found

        public ChainedIterator(IDictionary<K, V>[] chains, int totalSize) {
            this.chains = chains;
            this.totalSize = totalSize;
            this.step = 0; //how many elements we have returned already
            this.currentDict = null; //current bucket we are looking through
            this.currentIndex = -1; //current index in array of dictionaries (i.e bucket index)
            
            // finds first dictionary of elements to iterate through; remains null if no
            // such dictionary exists
            for (int i = 0; i < chains.length; i++) {
                this.currentIndex++;
                if (chains[i] != null) {
                    this.currentDict = chains[i].iterator();
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return this.step < this.totalSize;
        }

        @Override
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.step++;
            // this hasNext is true, there is at least 1 element left to find;
            // if my current iterator/dictionary does not have it, I have to find
            // a new dictionary
            // I use the while loop in case I happen to find a dictionary that was 
            // initiated (not null) but happens to be empty and has nothing to return
            while (!this.currentDict.hasNext()) {
                for (int i = currentIndex + 1; i < chains.length; i++) {
                    this.currentIndex++;
                    if (chains[i] != null) {
                        this.currentDict = chains[i].iterator();
                        break;
                    }  
                }
            }
            return currentDict.next();  
        }
    }
}