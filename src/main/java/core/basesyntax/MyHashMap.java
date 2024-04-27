package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }

        if (key == null) {
            putForNullKey(value);
            return;
        }

        int index = getIndex(key);
        Node<K, V> entry = new Node<>(key, value);

        if (table[index] == null) {
            table[index] = entry;
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current.next != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (Objects.equals(current.key, key)) {
                current.value = value;
            } else {
                current.next = entry;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }

        int index = getIndex(key);
        Node<K, V> entry = table[index];

        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                return entry.value;
            }
            entry = entry.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private V getForNullKey() {
        Node<K, V> entry = table[0];
        while (entry != null) {
            if (entry.key == null) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    private void putForNullKey(V value) {
        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }

        Node<K, V> entry = new Node<>(null, value);
        if (table[0] == null) {
            table[0] = entry;
            size++;
        } else {
            Node<K, V> current = table[0];
            while (current.next != null) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (current.key == null) {
                current.value = value;
            } else {
                current.next = entry;
                size++;
            }
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

