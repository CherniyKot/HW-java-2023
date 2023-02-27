package ru.itmo.mit.trie;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TrieTests {

    @Test
    public void testBasic(){
        Trie t = new TrieImpl();
        assertEquals(0, t.size());
        assertTrue( t.add("hello"));
        assertTrue(t.contains("hello"));
        assertFalse(t.contains("world"));
        assertEquals(5,t.size());
        assertFalse(t.remove("world"));
        assertTrue(t.remove("hello"));
        assertFalse(t.contains("hello"));
    }

    @Test
    public void testAddContainsSize(){
        Trie t = new TrieImpl();
        assertEquals(0,t.size());

        String testString="Random string";
        assertTrue(t.add(testString));
        assertEquals(testString.length(),t.size());

        for (int i=0;i<testString.length()-1;i++){
            assertFalse(t.contains(testString.substring(0,i)));
        }
        assertTrue(t.contains(testString));

        assertFalse(t.add(testString));
        assertEquals(testString.length(),t.size());

        String anotherTestString="Random string but better";
        assertTrue(t.add(anotherTestString));

        for (int i=0;i<testString.length()-1;i++){
            assertFalse(t.contains(testString.substring(0,i)));
        }
        assertTrue(t.contains(testString));
        assertTrue(t.contains(anotherTestString));

        String smallerTestString = "Random";
        assertTrue(t.add(smallerTestString));

        assertTrue(t.contains(smallerTestString));
        assertTrue(t.contains(testString));
        assertTrue(t.contains(anotherTestString));

        assertEquals(anotherTestString.length(),t.size());

        String branchingTestString="Random, so random";
        assertTrue(t.add(branchingTestString));
        assertTrue(t.contains(branchingTestString));
        assertTrue(t.contains(smallerTestString));
        assertTrue(t.contains(anotherTestString));
        assertEquals(anotherTestString.length()+(branchingTestString.length()-smallerTestString.length()),t.size());
    }

    @Test
    public void testRemove(){
        Trie t = new TrieImpl();
        assertEquals(0,t.size());

        String testString="Hola, mundo!";
        assertTrue(t.add(testString));
        assertTrue(t.contains(testString));

        assertTrue(t.remove(testString));
        assertFalse(t.remove(testString));

        assertFalse(t.contains(testString));
        assertTrue(t.add(testString));

        String anotherTestString="Hola, como estas?";

        assertTrue(t.add(anotherTestString));
        assertTrue(t.contains(anotherTestString));
        assertTrue(t.contains(testString));

        assertTrue(t.remove(testString));
        assertFalse(t.contains(testString));

        assertTrue(t.contains(anotherTestString));
        assertTrue(t.remove(anotherTestString));
        assertFalse(t.contains(anotherTestString));

        assertFalse(t.remove(anotherTestString));
        assertFalse(t.remove(testString));
    }
}
