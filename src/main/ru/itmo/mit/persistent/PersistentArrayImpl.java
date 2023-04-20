package ru.itmo.mit.persistent;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

@SuppressWarnings("unused")
public class PersistentArrayImpl<T> implements PersistentArray<T> {
    int size = 0;
    PersistentArrayNode root = new PersistentArrayNode();

    public PersistentArrayImpl() {
    }

    public PersistentArrayImpl(int size) {
        this.size = size;
        var node = root;
        for (int i = 0; i < size; i++) {
            node.next = new PersistentArrayNode();
            node = node.next;
        }
    }

    private PersistentArrayImpl(PersistentArrayNode n, int size) {
        this.size = size;
        root = n;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int indexOf(Object elem) {
        var node = root;
        int c = 0;
        while (node.next != null) {
            node = node.next;
            if (Objects.equals(node.value, elem)) {
                return c;
            }
            c++;
        }
        throw new NoSuchElementException();
    }

    @Override
    public boolean contains(Object elem) {
        var node = root;
        while (node.next != null) {
            node = node.next;
            if (Objects.equals(node.value, elem)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable T get(int index) {
        var node = root;
        for (int i = 0; i < index; i++) {
            node = node.next;
            if (node == null) {
                return null;
            }
        }
        return node.value;
    }

    @Override
    public PersistentArray<T> set(int index, @Nullable T x) {
        var r = new PersistentArrayNode();
        var n = r;
        var node = root;
        for (int i = 0; i < index; i++) {
            n.next = new PersistentArrayNode();
            if (node != null) {
                n.value = node.value;
                node = node.next;
            }
            n = n.next;
        }
        n.value = x;
        if (node != null) {
            n.next = node.next;
        }
        return new PersistentArrayImpl<>(r, Math.max(index, size));
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            PersistentArrayNode node = root;

            @Override
            public boolean hasNext() {
                return node.next != null;
            }

            @Override
            public T next() {
                node = node.next;
                return node.value;
            }
        };
    }

    private class PersistentArrayNode {
        PersistentArrayNode next;
        T value;
    }
}
