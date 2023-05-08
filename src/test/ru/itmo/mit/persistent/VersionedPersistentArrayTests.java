package ru.itmo.mit.persistent;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class VersionedPersistentArrayTests {
    @Test
    public void basicVersionedPersistentArrayTest() {
        var a1 = new VersionedPersistentArrayImpl<Integer>();
        var a2 = a1.set(0, 0);
        var a3 = a2.set(1, 1);
        var a4 = a2.set(3, 3);

        assertNull(a1.get(0));

        assertEquals(0, a2.get(0));
        assertNull(a2.get(1));

        assertEquals(1, a3.get(1));
        assertNull(a3.get(3));

        assertEquals(3, a4.get(3));

        assertEquals(0, a1.getSize());
        assertEquals(1, a2.getSize());
        assertEquals(2, a3.getSize());
        assertEquals(4, a4.getSize());

        assertFalse(a1.contains(null));
        assertFalse(a2.contains(null));
        assertTrue(a2.contains(0));
        assertFalse(a1.contains(0));
        assertFalse(a2.contains(1));
        assertTrue(a3.contains(1));
        assertTrue(a3.contains(0));

        assertTrue(a4.contains(null));
        assertTrue(a4.contains(0));
        assertFalse(a4.contains(1));
        assertTrue(a4.contains(3));
        assertFalse(a4.contains(2));

        assertEquals(0, a2.indexOf(0));
        assertEquals(1, a3.indexOf(1));
        assertEquals(3, a4.indexOf(3));
        assertEquals(1, a4.indexOf(null));
        assertThrows(NoSuchElementException.class, () -> a4.indexOf(5));
    }

    @Test
    public void versionedPersistentArrayIteratorTest() {
        var a = new VersionedPersistentArrayImpl<Integer>().set(3, 3).set(2, 2).set(9, 10).set(1, 0);

        var it = a.iterator();
        assertNull(it.next());
        assertEquals(0, it.next());
        assertEquals(2, it.next());
        assertEquals(3, it.next());
        assertNull(it.next());
        assertNull(it.next());
        assertNull(it.next());
        assertNull(it.next());
        assertNull(it.next());
        assertEquals(10, it.next());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void versionedPersistentArrayVersionedTest(){
        var a = new VersionedPersistentArrayImpl<Integer>().set(3, 3).set(2, 2).set(9, 10).set(1, 0);
        var a0 = a.getPreviousVersion(0);
        assertEquals(a,a0);
        var a1 = a.getPreviousVersion(1);
        assertFalse(a1.contains(0));
        assertTrue(a1.contains(10));
        var a2 = a.getPreviousVersion(3);
        assertEquals(4, a2.getSize());
        assertEquals(a1.getPreviousVersion(1),a.getPreviousVersion(2));

        var it = a.versionsIterator();
        var i0 = it.next();
        var i1 = it.next();
        var i2=it.next();
        var i3=it.next();
        var i4=it.next();
        assertThrows(NoSuchElementException.class, it::next);

        assertEquals(a,i0);
        assertEquals(a1,i1);
        assertEquals(a.getPreviousVersion(2), i2);
        assertEquals(a2, i3);
        assertEquals(a2.getPreviousVersion(1), i4);
    }

    @Test
    public void versionedPersistentArrayGetHistoryTest(){
        var a = new VersionedPersistentArrayImpl<Integer>().set(3, 3).set(2, 2).set(9, 10).set(1, 0);
        var history = a.getHistory();
        var hit = history.iterator();
        var m0 = hit.next();
        var m1 = hit.next();
        var m2 = hit.next();
        var m3 = hit.next();
        assertThrows(NoSuchElementException.class, hit::next);

        assertEquals(1,m0.getPosition());
        assertEquals(0,m0.getValue());

        assertEquals(9,m1.getPosition());
        assertEquals(10,m1.getValue());

        assertEquals(2,m2.getPosition());
        assertEquals(2,m2.getValue());

        assertEquals(3,m3.getPosition());
        assertEquals(3,m3.getValue());
    }
}
