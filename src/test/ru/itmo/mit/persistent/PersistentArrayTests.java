package ru.itmo.mit.persistent;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class PersistentArrayTests {
    @Test
    public void basicPersistentArrayTest() {
        var a1 = new PersistentArrayImpl<Integer>();
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
    public void persistentArrayIteratorTest() {
        var a = new PersistentArrayImpl<Integer>().set(3, 3).set(2, 2).set(9, 10).set(1, 0);

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
}
