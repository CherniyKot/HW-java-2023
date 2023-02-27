package ru.itmo.mit.trie;

import java.util.HashMap;

public class TrieImpl implements Trie {

    Node root = new Node();
    int size = 0;

    private Pair<Integer, Node> tryGetElement(String element) {
        Node t = root;
        int n = 0;
        for (char c : element.toCharArray()) {
            Node t1 = t.getNext(c);
            if (t1 == null) {
                var result = new Pair<Integer, Node>();
                result.first = n;
                result.second = t;
                return result;
            }
            t = t1;
            n++;
        }
        var result = new Pair<Integer, Node>();
        result.first = n;
        result.second = t;
        return result;
    }

    @Override
    public boolean add(String element) {
//        Node t = root;
//        if (contains(element)) { // I know, i traverse it twice, but it's Java, who cares
//            return false;
//        }
//        for (char c :
//                element.toCharArray()) {
//            t = t.AddNext(c, false);
//        }
//        t.isTerminal = true;
//        return true;

        var t = tryGetElement(element);
        if (t.first == element.length() && t.second.isTerminal) {
            return false;
        }
        Node node = t.second;
        for (char c : element.toCharArray()) {
            if (t.first-- > 0) {
                continue;
            }
            node = node.addNext(c);
        }
        node.isTerminal = true;
        return true;
    }

    @Override
    public boolean contains(String element) {
//        Node t = root;
//        for (char c :
//                element.toCharArray()) {
//            t = t.GetNext(c);
//            if(t == null){
//                return false;
//            }
//        }
        var t = tryGetElement(element);
        return t.first == element.length() && t.second.isTerminal;
    }

    @Override
    public boolean remove(String element) {
        var t = tryGetElement(element);
        if (t.first == element.length() && t.second.isTerminal) {
            t.second.isTerminal = false;
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        return 0;
    }

    @Override
    public String nextString(String element, int k) {
        return null;
    }

    private class Node {
        public char c;
        public boolean isTerminal;
        HashMap<Character, Node> next = new HashMap<>();

        public Node getNext(char c) {
            return next.get(c);
        }

        public Node addNext(char c) {
            if (next.containsKey(c)) {
                return next.get(c);
            }
            size++;
            Node t = new Node();
            t.c = c;
            next.put(c, t);
            return t;
        }
    }

    private static class Pair<T1, T2> { //why doesn't java have it???
        public T1 first;
        public T2 second;
    }
}
