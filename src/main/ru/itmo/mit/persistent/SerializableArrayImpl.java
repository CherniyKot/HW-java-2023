package ru.itmo.mit.persistent;

import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("unused")
public class SerializableArrayImpl<T> implements SerializableArray<T> {
    @Override
    public void serialize(OutputStream outputStream) {

    }

    @Override
    public PersistentArray<T> deserialize(InputStream inputStream) {
        return null;
    }
}
