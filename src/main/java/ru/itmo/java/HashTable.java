package ru.itmo.java;

public class HashTable {
    private static final int DEFAULT_INITIAL_SIZE = 10;
    private static final double RESIZE_MULTIPLY = 2;
    private static final double DEFAULT_LOAD_FACTOR = 0.5;

    private Entry[] hashTable;
    private final double loadFactor;
    private int size = 0;
    private int threshold;

    public HashTable(int initialCapacity, double loadFactor) {
        this.hashTable = new Entry[initialCapacity];
        this.loadFactor = loadFactor;
        this.threshold = (int) (hashTable.length * loadFactor);
    }

    public HashTable(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTable() {
        this(DEFAULT_INITIAL_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public Object put(Object key, Object value) {
        int hash = hash(key);
        while (hashTable[hash] != null) {
            if (key.equals(hashTable[hash].getKey())) {
                Object oldValue = hashTable[hash].getValue();
                hashTable[hash] = new Entry(key, value);
                return oldValue;
            }

            if (++hash == hashTable.length) {
                hash = 0;
            }
        }

        hashTable[hash] = new Entry(key, value);
        size++;

        if (size > threshold) {
            resize();
        }

        return null;
    }

    public Object get(Object key) {
        int hash = hash(key);
        while (hashTable[hash] != null) {
            if (key.equals(hashTable[hash].getKey())) {
                return hashTable[hash].getValue();
            }

            if (++hash == hashTable.length) {
                hash = 0;
            }
        }

        return null;
    }

    public Object remove(Object key) {
        int hash = hash(key);
        Object value = null;

        while (hashTable[hash] != null) {
            if (key.equals(hashTable[hash].getKey())) {
                value = hashTable[hash].getValue();
                hashTable[hash] = null;
                size--;
            } else {
                Object tempKey = hashTable[hash].getKey();
                Object tempValue = hashTable[hash].getValue();
                hashTable[hash] = null;
                size--;
                put(tempKey, tempValue);
            }

            if (++hash == hashTable.length) {
                hash = 0;
            }
        }

        return value;
    }

    public int size() {
        return size;
    }

    private int hash(Object key) {
        int capacity = hashTable.length;
        return (key.hashCode() % capacity + capacity) % capacity;
    }

    private void resize() {
        Entry[] oldHashTable = hashTable;
        int newSize = (int) (oldHashTable.length * RESIZE_MULTIPLY);
        hashTable = new Entry[newSize];
        this.size = 0;
        this.threshold = (int) (hashTable.length * loadFactor);

        for (var entry : oldHashTable) {
            if (entry != null) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    private static final class Entry {
        private final Object key;
        private final Object value;

        private Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        private Object getKey() {
            return key;
        }

        private Object getValue() {
            return value;
        }
    }
}