package ru.itmo.mit.persistent;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unused")
public class VersionedPersistentArrayImpl<T> extends PersistentArrayImpl<T> implements VersionedPersistentArray<T> {

    private VersionedPersistentArrayImpl<T> previousVersion;

    @Override
    protected VersionedPersistentArrayImpl<T> createNewVersion(PersistentArrayImpl<T>.PersistentArrayNode n, int size) {
        VersionedPersistentArrayImpl<T> r = (VersionedPersistentArrayImpl<T>) super.createNewVersion(n, size);
        r.previousVersion = this;
        return r;
    }

    @Override
    public VersionedPersistentArrayImpl<T> set(int index, @Nullable T x) {
        return (VersionedPersistentArrayImpl<T>) super.set(index, x);
    }

    @Override
    public Iterator<PersistentArray<T>> versionsIterator() {
        return new Iterator<>() {
            VersionedPersistentArrayImpl<T> curr = VersionedPersistentArrayImpl.this;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public PersistentArray<T> next() {
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
    public PersistentArray<T> getPreviousVersion(int k) {
        var it = versionsIterator();
        for (int i = 0; i < k - 1; i++) {
            it.next();
        }
        return it.next();
    }
}
