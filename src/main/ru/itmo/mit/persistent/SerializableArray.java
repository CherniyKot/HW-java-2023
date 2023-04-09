package ru.itmo.mit.persistent;

import java.io.InputStream;
import java.io.OutputStream;

public interface SerializableArray<T> {
    void serialize(OutputStream outputStream);
    PersistentArray<T> deserialize(InputStream inputStream);
}
