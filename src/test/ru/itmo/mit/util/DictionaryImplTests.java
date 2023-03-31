package ru.itmo.mit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Set;

@SuppressWarnings("ALL")
public class DictionaryImplTests {

    @SuppressWarnings("unused")
    @Test
    public void dictionaryCreateTest() {
        var d1 = new DictionaryImpl<Integer, Integer>();
        var d2 = new DictionaryImpl<Dictionary<String, String>, Integer>(0.5);
        var d3 = new DictionaryImpl<Integer, String>(100);
        var d4 = new DictionaryImpl<String, Double>(0.4, 30);
    }

    @Test
    public void dictionaryBasicTest() {
        var d = new DictionaryImpl<Integer, String>();
        Assertions.assertEquals(0, d.size());
        Assertions.assertTrue(d.isEmpty());

        addWithAssertions(d, 10, "Hello");
        addWithAssertions(d, 12, "World");

        Assertions.assertFalse(d.isEmpty());

        deleteNonExistingWithAssertions(d, 42);
        deleteWithAssertions(d, "Hello", 10);

        Assertions.assertTrue(d.containsKey(12));
        Assertions.assertFalse(d.containsKey(10));
        Assertions.assertFalse(d.containsKey(11));
        Assertions.assertFalse(d.containsKey(42));

        addWithAssertions(d, 10000, "Wow, that's a big number!");
        Assertions.assertTrue(d.containsKey(10000));
        Assertions.assertTrue(d.containsValue("Wow, that's a big number!"));
        changeWithAssertions(d, "Wow, that's a big number!", 10000, "");
        Assertions.assertTrue(d.containsKey(10000));
        Assertions.assertFalse(d.containsValue("Wow, that's a big number!"));
        Assertions.assertTrue(d.containsValue(""));

        Assertions.assertEquals("World", d.get(12));
        d.clear();
        Assertions.assertEquals(0, d.size());
        Assertions.assertTrue(d.isEmpty());
    }

    @Test
    public void dictionaryNullTest() {
        var d = new DictionaryImpl<Integer, String>();

        addWithAssertions(d, null, "Help");
        Assertions.assertTrue(d.containsKey(null));
        Assertions.assertTrue(d.containsValue("Help"));

        changeWithAssertions(d, "Help", null, "Ok, I'm helping");
        Assertions.assertTrue(d.containsKey(null));
        Assertions.assertTrue(d.containsValue("Ok, I'm helping"));
        Assertions.assertFalse(d.containsValue("Help"));

        deleteWithAssertions(d, "Ok, I'm helping", null);
        Assertions.assertTrue(d.isEmpty());

        Assertions.assertFalse(d.containsValue(null));
        addWithAssertions(d, -100, null);
        Assertions.assertTrue(d.containsValue(null));
        changeWithAssertions(d, null, -100, "");
        Assertions.assertFalse(d.containsValue(null));

        Assertions.assertFalse(d.containsKey(null));
        addWithAssertions(d, null, null);
        Assertions.assertTrue(d.containsValue(null));
        Assertions.assertTrue(d.containsKey(null));
    }

    @Test
    public void dictionarySetsTest() {
        var d = new DictionaryImpl<Integer, String>();
        Assertions.assertEquals(0, d.size());
        Assertions.assertTrue(d.isEmpty());

        addWithAssertions(d, 1, "hello");
        addWithAssertions(d, 2, "world");
        addWithAssertions(d, 3, "this");
        addWithAssertions(d, 4, "is");
        addWithAssertions(d, 5, "test");

        var values = d.values();
        var entries = d.entrySet();
        var keys = d.keySet();

        Assertions.assertEquals(Set.of(1, 2, 3, 4, 5), keys);
        Assertions.assertEquals(Set.of("hello", "world", "this", "is", "test"), Set.copyOf(values));
        Assertions.assertEquals(Set.of(
                new AbstractMap.SimpleEntry<>(1, "hello"),
                new AbstractMap.SimpleEntry<>(2, "world"),
                new AbstractMap.SimpleEntry<>(3, "this"),
                new AbstractMap.SimpleEntry<>(4, "is"),
                new AbstractMap.SimpleEntry<>(5, "test")
        ), entries);
    }

    @Test
    public void entriesIteratorRemoveTest() {
        var d = new DictionaryImpl<Integer, String>();
        Assertions.assertEquals(0, d.size());
        Assertions.assertTrue(d.isEmpty());

        addWithAssertions(d, 1, "hello");
        addWithAssertions(d, 2, "world");
        addWithAssertions(d, 3, "this");
        addWithAssertions(d, 4, "is");
        addWithAssertions(d, 5, "test");

        var values = d.values();
        var entries = d.entrySet();
        var keys = d.keySet();

        var initialKeys = new java.util.HashSet<>(Set.of(1, 2, 3, 4, 5));
        var initialValues = new java.util.HashSet<>(Set.of("hello", "world", "this", "is", "test"));
        var initialEntries = new java.util.HashSet<>(Set.of(
                new AbstractMap.SimpleEntry<>(1, "hello"),
                new AbstractMap.SimpleEntry<>(2, "world"),
                new AbstractMap.SimpleEntry<>(3, "this"),
                new AbstractMap.SimpleEntry<>(4, "is"),
                new AbstractMap.SimpleEntry<>(5, "test")));

        Assertions.assertEquals(initialEntries, entries);

        var entriesIt = entries.iterator();

        while (true) {
            var t = entriesIt.next();
            if (t.getKey() == 3) {
                entriesIt.remove();
                break;
            }
        }

        initialKeys.remove(3);
        initialValues.remove("this");
        initialEntries.remove(new AbstractMap.SimpleEntry<>(3, "this"));

        Assertions.assertEquals(initialKeys, keys);
        Assertions.assertEquals(initialValues, Set.copyOf(values));
        Assertions.assertEquals(initialEntries, entries);


        Assertions.assertThrows(IllegalStateException.class, entriesIt::remove);

        var t = entriesIt.next();
        initialKeys.remove(t.getKey());
        initialValues.remove(t.getValue());
        initialEntries.remove(t);

        Assertions.assertDoesNotThrow(entriesIt::remove);

        Assertions.assertEquals(initialKeys, keys);
        Assertions.assertEquals(initialValues, Set.copyOf(values));
        Assertions.assertEquals(initialEntries, entries);
    }

    @Test
    public void keysIteratorRemoveTest() {
        var d = new DictionaryImpl<Integer, String>();
        Assertions.assertEquals(0, d.size());
        Assertions.assertTrue(d.isEmpty());

        addWithAssertions(d, 1, "hello");
        addWithAssertions(d, 2, "world");
        addWithAssertions(d, 3, "this");
        addWithAssertions(d, 4, "is");
        addWithAssertions(d, 5, "test");

        var values = d.values();
        var entries = d.entrySet();
        var keys = d.keySet();

        var initialKeys = new java.util.HashSet<>(Set.of(1, 2, 3, 4, 5));
        var initialValues = new java.util.HashSet<>(Set.of("hello", "world", "this", "is", "test"));
        var initialEntries = new java.util.HashSet<>(Set.of(
                new AbstractMap.SimpleEntry<>(1, "hello"),
                new AbstractMap.SimpleEntry<>(2, "world"),
                new AbstractMap.SimpleEntry<>(3, "this"),
                new AbstractMap.SimpleEntry<>(4, "is"),
                new AbstractMap.SimpleEntry<>(5, "test")));

        Assertions.assertEquals(initialEntries, entries);

        var keysIt = keys.iterator();

        while (true) {
            var t = keysIt.next();
            if (t == 3) {
                keysIt.remove();
                break;
            }
        }

        initialKeys.remove(3);
        initialValues.remove("this");
        initialEntries.remove(new AbstractMap.SimpleEntry<>(3, "this"));

        Assertions.assertEquals(initialKeys, keys);
        Assertions.assertEquals(initialValues, Set.copyOf(values));
        Assertions.assertEquals(initialEntries, entries);


        Assertions.assertThrows(IllegalStateException.class, keysIt::remove);

        var k = keysIt.next();
        var v = d.get(k);
        initialKeys.remove(k);
        initialValues.remove(v);
        initialEntries.remove(new AbstractMap.SimpleEntry<>(k, v));

        Assertions.assertDoesNotThrow(keysIt::remove);

        Assertions.assertEquals(initialKeys, keys);
        Assertions.assertEquals(initialValues, Set.copyOf(values));
        Assertions.assertEquals(initialEntries, entries);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void valuesIteratorRemoveTest() {
        var d = new DictionaryImpl<Integer, String>();
        Assertions.assertEquals(0, d.size());
        Assertions.assertTrue(d.isEmpty());

        addWithAssertions(d, 1, "hello");
        addWithAssertions(d, 2, "world");
        addWithAssertions(d, 3, "this");
        addWithAssertions(d, 4, "is");
        addWithAssertions(d, 5, "test");

        var values = d.values();
        var entries = d.entrySet();
        var keys = d.keySet();

        var initialKeys = new java.util.HashSet<>(Set.of(1, 2, 3, 4, 5));
        var initialValues = new java.util.HashSet<>(Set.of("hello", "world", "this", "is", "test"));
        var initialEntries = new java.util.HashSet<>(Set.of(
                new AbstractMap.SimpleEntry<>(1, "hello"),
                new AbstractMap.SimpleEntry<>(2, "world"),
                new AbstractMap.SimpleEntry<>(3, "this"),
                new AbstractMap.SimpleEntry<>(4, "is"),
                new AbstractMap.SimpleEntry<>(5, "test")));

        Assertions.assertEquals(initialEntries, entries);

        var valuesIt = values.iterator();

        while (true) {
            var t = valuesIt.next();
            if (t.equals("this")) {
                valuesIt.remove();
                break;
            }
        }

        initialKeys.remove(3);
        initialValues.remove("this");
        initialEntries.remove(new AbstractMap.SimpleEntry<>(3, "this"));

        Assertions.assertEquals(initialKeys, keys);
        Assertions.assertEquals(initialValues, Set.copyOf(values));
        Assertions.assertEquals(initialEntries, entries);


        Assertions.assertThrows(IllegalStateException.class, valuesIt::remove);

        var v = valuesIt.next();
        var e = entries.stream().filter(x -> x.getValue().equals(v)).findFirst().get();
        initialKeys.remove(e.getKey());
        initialValues.remove(v);
        initialEntries.remove(e);

        Assertions.assertDoesNotThrow(valuesIt::remove);

        Assertions.assertEquals(initialKeys, keys);
        Assertions.assertEquals(initialValues, Set.copyOf(values));
        Assertions.assertEquals(initialEntries, entries);
    }

    private <K, V> void addWithAssertions(Dictionary<K, V> dict, K key, V value) {
        var size = dict.size();
        Assertions.assertNull(dict.put(key, value));
        Assertions.assertEquals(size + 1, dict.size());
    }

    private <K, V> void changeWithAssertions(Dictionary<K, V> dict, V prev, K key, V value) {
        var size = dict.size();
        Assertions.assertEquals(prev, dict.put(key, value));
        Assertions.assertEquals(size, dict.size());
    }

    private <K, V> void deleteWithAssertions(Dictionary<K, V> dict, V prev, K key) {
        var size = dict.size();
        Assertions.assertEquals(prev, dict.remove(key));
        Assertions.assertEquals(size - 1, dict.size());
    }

    private <K, V> void deleteNonExistingWithAssertions(Dictionary<K, V> dict, K key) {
        var size = dict.size();
        Assertions.assertNull(dict.remove(key));
        Assertions.assertEquals(size, dict.size());
    }
}
