package ru.itmo.mit.persistent;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

public class VersionedPersistentArrayImpl<T extends Serializable> extends PersistentArrayImpl<T> implements VersionedPersistentArray<T> {

    private VersionedPersistentArrayImpl<T> previousVersion;
    private int numberOfVersions = 0;

    @Override
    protected VersionedPersistentArrayImpl<T> createNewVersion(PersistentArrayImpl.PersistentArrayNode<T> n, int size) {
        var newVersion = super.createNewVersion(n, size);
        VersionedPersistentArrayImpl<T> r = new VersionedPersistentArrayImpl<>();
        r.root = newVersion.root;
        r.size = newVersion.size;
        r.previousVersion = this;
        r.numberOfVersions = this.numberOfVersions + 1;
        return r;
    }

    @Override
    public VersionedPersistentArrayImpl<T> set(int index, @Nullable T x) {
        return (VersionedPersistentArrayImpl<T>) super.set(index, x);
    }

    @Override
    public void serialize(OutputStream outputStream) {

        try (var stream = new ObjectOutputStream(outputStream)) {
            var history = getHistory();
            stream.writeInt(history.size());
            for (var mod : history) {
                stream.writeInt(mod.getPosition());
                stream.writeObject(mod.getValue());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public VersionedPersistentArray<T> deserialize(InputStream inputStream) {
        VersionedPersistentArrayImpl<T> result = new VersionedPersistentArrayImpl<>();
        Deque<Modification<T>> history = new ArrayDeque<>();
        try (var stream = new ObjectInputStream(inputStream)) {
            var size = stream.readInt();
            for (int i = 0; i < size; i++) {
                history.addFirst(new Modification<>(stream.readInt(), (T) stream.readObject()));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (var mod : history) {
            result = result.set(mod.getPosition(), mod.getValue());
        }

        return result;
    }

    @Override
    public Iterator<VersionedPersistentArray<T>> versionsIterator() {
        return new Iterator<>() {
            private VersionedPersistentArrayImpl<T> curr = VersionedPersistentArrayImpl.this;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public VersionedPersistentArray<T> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                var r = curr;
                curr = curr.previousVersion;
                return r;
            }
        };
    }

    @Override
    public VersionedPersistentArray<T> getPreviousVersion(int k) {
        var it = versionsIterator();
        for (int i = 0; i < k; i++) {
            it.next();
        }
        return it.next();
    }

    public static class Modification<T> {
        private final int position;
        private final T value;

        public int getPosition() {
            return position;
        }

        public T getValue() {
            return value;
        }

        Modification(int position, T value) {
            this.position = position;
            this.value = value;
        }
    }

    public Collection<Modification<T>> getHistory() {
        return new AbstractCollection<>() {

            private VersionedPersistentArrayImpl<T> curr = VersionedPersistentArrayImpl.this;

            @Override
            public Iterator<Modification<T>> iterator() {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return curr.previousVersion != null;
                    }

                    private Modification<T> getDifferenceWithPrevious() {
                        if (curr.previousVersion == null) {
                            return new Modification<>(curr.size - 1, curr.get(curr.size - 1));
                        }

                        var n1 = curr.root;
                        var n2 = curr.previousVersion.root;
                        int c = -1;
                        while (n1 != null && n2 != null && n1.next != n2.next) {
                            c++;
                            n1 = n1.next;
                            n2 = n2.next;
                        }
                        if (n1 == null || n2 == null) {
                            return new Modification<>(curr.size - 1, curr.get(curr.size - 1));
                        }
                        return new Modification<>(c, curr.get(c));
                    }

                    @Override
                    public Modification<T> next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        var r = getDifferenceWithPrevious();
                        curr = curr.previousVersion;
                        return r;
                    }
                };
            }

            @Override
            public int size() {
                return numberOfVersions;
            }
        };
    }
}
