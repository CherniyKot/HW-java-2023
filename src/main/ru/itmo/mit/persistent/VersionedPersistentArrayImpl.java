package ru.itmo.mit.persistent;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

public class VersionedPersistentArrayImpl<T extends Serializable> extends PersistentArrayImpl<T> implements VersionedPersistentArray<T> {

    private VersionedPersistentArrayImpl<T> previousVersion;

    @Override
    protected VersionedPersistentArrayImpl<T> createNewVersion(PersistentArrayImpl.PersistentArrayNode<T> n, int size) {
        var newVersion = super.createNewVersion(n, size);
        VersionedPersistentArrayImpl<T> r = new VersionedPersistentArrayImpl<>();
        r.root = newVersion.root;
        r.size = newVersion.size;
        r.previousVersion = this;
        return r;
    }

    @Override
    public VersionedPersistentArrayImpl<T> set(int index, @Nullable T x) {
        return (VersionedPersistentArrayImpl<T>) super.set(index, x);
    }


    @Override
    public void serialize(OutputStream outputStream) {
        Set<PersistentArrayNode<T>> nodes = new HashSet<>();
        Map<Integer, T> values = new HashMap<>();
        int numberOfVersions = -1;
        var curr = this;
        do {
            var node = curr.root;
            do {
                nodes.add(node);
                values.put(Objects.hashCode(node.value), node.value);
                node = node.next;
            } while (node != null);
            curr = curr.previousVersion;
            numberOfVersions++;
        } while (curr != null);

        try (var stream = new ObjectOutputStream(outputStream)) {
            stream.writeInt(values.size());
            for (var value : values.entrySet()) {
                stream.writeInt(value.getKey());
                stream.writeObject(value.getValue());
            }

            stream.writeInt(nodes.size());
            for (var node : nodes) {
                stream.writeInt(node.hashCode());
                stream.writeInt(Objects.hashCode(node.next));
                stream.writeInt(Objects.hashCode(node.value));
            }

            stream.writeInt(numberOfVersions);
            curr = this;
            do {
                stream.writeInt(curr.root.hashCode());
                curr = curr.previousVersion;
            } while (curr != null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public VersionedPersistentArray<T> deserialize(InputStream inputStream) {
        Map<Integer, PersistentArrayNode<T>> nodes = new HashMap<>();
        Map<Integer, T> values = new HashMap<>();
        Map<Integer, Integer> order = new HashMap<>();
        VersionedPersistentArrayImpl<T> result = null;

        try (var stream = new ObjectInputStream(inputStream)) {
            int valuesCount = stream.readInt();
            for (int i = 0; i < valuesCount; i++) {
                var hash = stream.readInt();
                T value = (T) stream.readObject();
                values.put(hash, value);
            }

            int nodeCount = stream.readInt();
            for (int i = 0; i < nodeCount; i++) {
                var node = new PersistentArrayNode<T>();
                int curr = stream.readInt();
                int next = stream.readInt();
                int valueHash = stream.readInt();
                node.value = values.get(valueHash);
                nodes.put(curr, node);
                order.put(curr, next);
            }

            var version = new VersionedPersistentArrayImpl<T>();
            int numberOfVersions = stream.readInt();
            for (int i = 0; i < numberOfVersions; i++) {
                int rootId = stream.readInt();
                var rootNode = nodes.get(rootId);
                var currNode = rootNode;
                int versionSize = -1;
                int currId = rootId;
                do {
                    currId = order.get(currId);
                    currNode.next = nodes.get(currId);
                    currNode = currNode.next;
                    versionSize++;
                } while (nodes.containsKey(currId));

                version.size = versionSize;
                version.root = rootNode;
                if (result == null) {
                    result = version;
                }
                var prevVersion = version;
                version = new VersionedPersistentArrayImpl<>();
                prevVersion.previousVersion = version;
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Iterator<VersionedPersistentArray<T>> versionsIterator() {
        return new Iterator<>() {
            VersionedPersistentArrayImpl<T> curr = VersionedPersistentArrayImpl.this;

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
}
