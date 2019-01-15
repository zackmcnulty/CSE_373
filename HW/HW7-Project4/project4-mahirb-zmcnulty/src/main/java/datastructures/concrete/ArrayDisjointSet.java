package datastructures.concrete;

import datastructures.interfaces.IDisjointSet;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;
    private IDictionary<T, Integer> converter;
    private int n; //number of sets/index position in pointers
    private static final int DEFAULT_NUMBER_OF_SETS = 10;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    public ArrayDisjointSet() {
        this(DEFAULT_NUMBER_OF_SETS);
    }
    
    public ArrayDisjointSet(int initialSize) {
        this.pointers = new int[initialSize];
        this.n = 0;
        this.converter = new ChainedHashDictionary<>(); //stores list of value --> index (of pointers)
    }

    @Override
    public void makeSet(T item) {
        if (this.contains(item)) {
            throw new IllegalArgumentException();
        } 
        // expands capacity
        if (this.n >= this.pointers.length) {
            int[] newPointers = new int[2 * this.pointers.length];
            for (KVPair<T, Integer> singlePair : this.converter) {
                newPointers[singlePair.getValue()] = this.pointers[singlePair.getValue()];
            }
            this.pointers = newPointers;
        }
        this.pointers[this.n] = -1;
        this.converter.put(item, this.n);
        this.n++;
    }

    // returns true if item is part of any disjoint set, false otherwise
    private boolean contains(T item) {
        return this.converter.containsKey(item);
    }

    // returns index in set of representative item
    @Override
    public int findSet(T item) {
        if (!contains(item)) {
            throw new IllegalArgumentException();
        } else {
            int index = this.converter.get(item);
            return findSetConverted(index);
        }
    }

    // If current index contains a representative element, returns that elements index; else continues
    // searching for representative element.
    private int findSetConverted(int index) {
        if (this.pointers[index] < 0) {
            return index;
        } 
        // assumes this.pointers[index] stores the index of parent;
        // keeps recursing until overall parent found
        int representativeIndex = findSetConverted(this.pointers[index]);
        this.pointers[index] = representativeIndex;
        return representativeIndex;
    }

    @Override
    public void union(T item1, T item2) {
        if (!contains(item1) || !contains(item2)) { // item1 or item2 is not contained
            throw new IllegalArgumentException();
        }
        int parent1 = findSet(item1);
        int parent2 = findSet(item2);
        if (parent1 == parent2) { // item1 and item2 are already a part of the same set
            throw new IllegalArgumentException();
        }
        int item1Size = -1 * this.pointers[parent1];
        int item2Size = -1 * this.pointers[parent2];
        // if items are same size, first parent becomes representative of whole
        if (item1Size < item2Size) {
            this.pointers[parent1] = parent2;
            this.pointers[parent2] = -item2Size - item1Size; 
        } else {
            this.pointers[parent2] = parent1;
            this.pointers[parent1] = -item2Size - item1Size;
        }
    }
}
