package ru.itmo.mit.persistent;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class PersistentArrayImpl<T extends Serializable> implements PersistentArray<T>, SerializableArray<T> {
    protected int size = 0;
    protected PersistentArrayNode<T> root = new PersistentArrayNode<>();

    public PersistentArrayImpl() {
    }

    public PersistentArrayImpl(int size) {
        this.size = size;
        var node = root;
        for (int i = 0; i < size; i++) {
            node.next = new PersistentArrayNode<>();
            node = node.next;
        }
    }

    protected PersistentArrayImpl<T> createNewVersion(PersistentArrayNode<T> n, int size) {
        var r = new PersistentArrayImpl<T>();
        r.root = n;
        r.size = size;
        return r;
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
        for (int i = 0; i < index + 1; i++) {
            node = node.next;
            if (node == null) {
                return null;
            }
        }
        return node.value;
    }

    @Override
    public PersistentArrayImpl<T> set(int index, @Nullable T x) {
        var r = new PersistentArrayNode<T>();
        var n = r;
        var node = root;
        for (int i = 0; i < index + 1; i++) {
            if (node != null) {
                n.value = node.value;
                node = node.next;
            }
            n.next = new PersistentArrayNode<>();
            n = n.next;
        }
        n.value = x;
        if (node != null) {
            n.next = node.next;
        }
        return createNewVersion(r, Math.max(index + 1, size));
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private PersistentArrayNode<T> node = root;

            @Override
            public boolean hasNext() {
                return node.next != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                node = node.next;
                return node.value;
            }
        };
    }

    @Override
    public void serialize(OutputStream outputStream) {
        var node = root;
        try (ObjectOutputStream stream = new ObjectOutputStream(outputStream)) {
            stream.writeInt(size);
            while (node != null) {
                stream.writeObject(node.value);
                node = node.next;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public PersistentArray<T> deserialize(InputStream inputStream) {
        try (var stream = new ObjectInputStream(inputStream)) {
            int s = stream.readInt();
            var r = new PersistentArrayImpl<T>(s);
            var node = r.root;
            for (int i = 0; i < s + 1; i++) {
                node.value = (T) stream.readObject();
                node = node.next;
            }
            return r;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected static class PersistentArrayNode<T> {
        PersistentArrayNode<T> next;
        T value;
    }
}
