package ru.itmo.mit.trie;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TrieTests {

    @Test
    public void testBasic() {
        Trie t = new TrieImpl();
        assertEquals(0, t.size());
        assertTrue(t.add("hello"));
        assertTrue(t.contains("hello"));
        assertFalse(t.contains("world"));
        assertEquals(1, t.size());
        assertFalse(t.remove("world"));
        assertTrue(t.remove("hello"));
        assertFalse(t.contains("hello"));
        assertEquals(0, t.size());
    }

    @Test
    public void testAddContainsSize() {
        Trie t = new TrieImpl();
        assertEquals(0, t.size());

        String testString = "Random string";
        assertTrue(t.add(testString));
        assertEquals(1, t.size());

        for (int i = 0; i < testString.length() - 1; i++) {
            assertFalse(t.contains(testString.substring(0, i)));
        }
        assertTrue(t.contains(testString));

        assertFalse(t.add(testString));
        assertEquals(1, t.size());

        String anotherTestString = "Random string but better";
        assertTrue(t.add(anotherTestString));

        for (int i = 0; i < testString.length() - 1; i++) {
            assertFalse(t.contains(testString.substring(0, i)));
        }
        assertTrue(t.contains(testString));
        assertTrue(t.contains(anotherTestString));

        String smallerTestString = "Random";
        assertTrue(t.add(smallerTestString));

        assertTrue(t.contains(smallerTestString));
        assertTrue(t.contains(testString));
        assertTrue(t.contains(anotherTestString));

        assertEquals(3, t.size());

        String branchingTestString = "Random, so random";
        assertTrue(t.add(branchingTestString));
        assertTrue(t.contains(branchingTestString));
        assertTrue(t.contains(smallerTestString));
        assertTrue(t.contains(anotherTestString));
        assertEquals(4, t.size());
    }

    @Test
    public void testRemove() {
        Trie t = new TrieImpl();
        assertEquals(0, t.size());

        String testString = "Hola, mundo!";
        assertTrue(t.add(testString));
        assertTrue(t.contains(testString));
        assertEquals(1, t.size());

        assertTrue(t.remove(testString));
        assertFalse(t.remove(testString));
        assertEquals(0, t.size());

        assertFalse(t.contains(testString));
        assertTrue(t.add(testString));
        assertEquals(1, t.size());

        String anotherTestString = "Hola, como estas?";

        assertTrue(t.add(anotherTestString));
        assertTrue(t.contains(anotherTestString));
        assertTrue(t.contains(testString));
        assertEquals(2, t.size());

        assertTrue(t.remove(testString));
        assertFalse(t.contains(testString));
        assertEquals(1, t.size());

        assertTrue(t.contains(anotherTestString));
        assertTrue(t.remove(anotherTestString));
        assertFalse(t.contains(anotherTestString));
        assertEquals(0, t.size());

        assertFalse(t.remove(anotherTestString));
        assertFalse(t.remove(testString));
        assertEquals(0, t.size());
    }

    @Test
    public void testHowManyStartsWithPrefix() {
        Trie t = new TrieImpl();
        assertEquals(0, t.size());

        String testString1 = "Hello, world!";
        String testString2 = "Hello, hi!";
        String testString3 = "Hello, why!";
        String testString4 = "Hello, goodbye!";

        t.add(testString1);
        t.add(testString2);
        t.add(testString3);
        t.add(testString4);
        assertEquals(4, t.howManyStartsWithPrefix("Hello"));

        String testString5 = "Hello";
        t.add(testString5);
        assertEquals(5, t.howManyStartsWithPrefix("Hello"));

        String testString6 = "No Hello, world!";
        t.add(testString6);
        assertEquals(5, t.howManyStartsWithPrefix("Hello"));
    }

    @Test
    public void testNextString() {
        Trie t = new TrieImpl();
        assertEquals(0, t.size());

        String testString1 = "Hello, world!";
        String testString2 = "Hello, hi!";
        String testString3 = "Hello, why!";
        String testString4 = "Hello, goodbye!";

        t.add(testString1);
        t.add(testString2);
        t.add(testString3);
        t.add(testString4);

        assertEquals(testString4, t.nextString(testString4, 0));
        assertEquals(testString2, t.nextString(testString4, 1));
        assertEquals(testString3, t.nextString(testString4, 2));
        assertEquals(testString1, t.nextString(testString4, 3));
        assertNull(t.nextString(testString4, 4));

        assertEquals(testString4, t.nextString("Hello", 1));
        assertNull(t.nextString("Hello", 0));
        assertNull(t.nextString("ZZZ", 1));
    }

}
