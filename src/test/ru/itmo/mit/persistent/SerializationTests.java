package ru.itmo.mit.persistent;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializationTests {

    @Test
    public void standardSerializationTest() {
        var a = new PersistentArrayImpl<Integer>().set(3, 3).set(2, 2).set(9, 10).set(1, 0);
        var oStream = new ByteArrayOutputStream();
        a.serialize(oStream);
        var iStream = new ByteArrayInputStream(oStream.toByteArray());
        var b = new PersistentArrayImpl<Integer>().deserialize(iStream);

        assertEquals(a.getSize(), b.getSize());
        for (int i = 0; i < a.getSize(); i++) {
            assertEquals(a.get(i), b.get(i));
        }
    }


    @Test
    public void versionedSerializationTest() {
        var a = new VersionedPersistentArrayImpl<Integer>().set(3, 3).set(2, 2).set(9, 10).set(1, 0);
        var oStream = new ByteArrayOutputStream();
        a.serialize(oStream);
        var iStream = new ByteArrayInputStream(oStream.toByteArray());
        var b = new VersionedPersistentArrayImpl<Integer>().deserialize(iStream);

        for (int i = 0; i < 4; i++) {
            var a1 = a.getPreviousVersion(i);
            var b1 = b.getPreviousVersion(i);
            assertEquals(a1.getSize(), b1.getSize());
            for (int j = 0; j < a1.getSize(); j++) {
                assertEquals(a1.get(j), b1.get(j));
            }
        }
    }
}
