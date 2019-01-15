package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.NoSuchElementException;

import java.util.Iterator;

public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    private static final int DEFAULT_CAPACITY = 10;
    
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;
    private int capacity;

    // You're encouraged to add extra fields (and helper methods) though!
    
    public ArrayDictionary() {
        this(DEFAULT_CAPACITY);
    }
    
    public ArrayDictionary(int capacity) {
        this.pairs = makeArrayOfPairs(capacity);
        this.capacity = capacity;
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    @Override
    public V get(K key) throws NoSuchKeyException{
        int index = this.getIndex(key); // throws NoSuchKeyException if key is not present in pairs.
        if (index == -1) {
            throw new NoSuchKeyException();
        }
        return pairs[index].value;
    }
    
    // Returns the index in the array of the Pair that has a key matching the given key, 
    // and returns -1 if no such pair exists.
    private int getIndex(K key) throws NoSuchKeyException {
        for (int i = 0; i < this.size; i++) {
            Pair<K, V> next = this.pairs[i];
            if (next.matchesKey(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void put(K key, V value) {
        int index = this.getIndex(key);
        if (index != -1) {
            Pair<K, V> pair = this.pairs[index];
            pair.value = value; 
        } else {
            if (this.size == this.capacity) {       
                this.changeCapacity(2*this.capacity);
            }
            this.pairs[this.size] = new Pair<K, V>(key, value);
            this.size++;
        }
    }
    
    // changes the capacity of the array that stores all the Pairs to be newCapacity.
    private void changeCapacity(int newCapacity) {
        Pair<K, V>[] newPairs = makeArrayOfPairs(newCapacity);
        for (int i = 0; i < this.size; i++) {
            newPairs[i] = this.pairs[i];
        }
        this.pairs = newPairs;
        this.capacity = newCapacity;
    }

    @Override
    public V remove(K key) throws NoSuchKeyException{        
        int index = this.getIndex(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        }
        V value = this.pairs[index].value;
        
        // When an element is removed, moves the last element to take its place. If the
        // element is the last element, it removes that pairs reference to avoid letting
        // the pair stick around in memory.
        if (index == this.size - 1) {
            this.pairs[index] = null;
        } else {
            this.pairs[index] = this.pairs[this.size - 1]; 
        }
        this.size--;
        return value;
    }

    @Override
    public boolean containsKey(K key) {
        return this.getIndex(key) != -1;
    }

    @Override
    public int size() {
        return this.size;
    }
    
    public Iterator<KVPair<K, V>> iterator() {
        return new DictionaryIterator<>(this.size, this.pairs);
    }
    
    private static class DictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private int position;
        private int size;
        private Pair<K, V>[] pairs;
        
        public DictionaryIterator(int size, Pair<K, V>[] pairs) {
            this.size = size;
            this.position = 0;
            this.pairs = pairs;
        }
        
        public boolean hasNext() {
            return this.position < this.size;
        }
        
        public KVPair<K, V> next() throws NoSuchElementException {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            Pair<K, V> next = pairs[this.position];
            position++;
            return new KVPair<K, V>(next.key, next.value);
        }
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        // returns true if the given key is equal to this pair's key, false otherwise
        public boolean matchesKey(K other) {
            if (this.key == null) {
                return this.key == other; // handles null key case, which is a valid key
            } else {
                return this.key.equals(other);
            }
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
