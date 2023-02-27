package ru.itmo.mit.trie;

import java.util.SortedMap;
import java.util.TreeMap;

public class TrieImpl implements Trie {

    Node root = new Node();

    private Pair<Integer, Node> tryGetElement(String element) {
        Node t = root;
        int n = 0;
        for (char c : element.toCharArray()) {
            Node t1 = t.getNext(c);
            if (t1 == null) {
                break;
            }
            t = t1;
            n++;
        }
        return new Pair<>(n, t);
    }

    @Override
    public boolean add(String element) {
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

        do {
            node.size++;
            node = node.parent;
        } while (node != null);

        return true;
    }

    @Override
    public boolean contains(String element) {
        var t = tryGetElement(element);
        return t.first == element.length() && t.second.isTerminal;
    }

    @Override
    public boolean remove(String element) {
        var t = tryGetElement(element);
        if (t.first == element.length() && t.second.isTerminal) {
            t.second.isTerminal = false;
            Node node = t.second;
            Node node1 = node.parent;
            while (node1 != null) {
                node1.children.remove(node.c);
                if (node1.children.size() == 0 && !node1.isTerminal) {
                    node = node1;
                    node1 = node.parent;
                } else {
                    break;
                }
            }

            do {
                node.size--;
                node = node.parent;
            } while (node != null);
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return root.size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        var t = tryGetElement(prefix);
        if (t.first == prefix.length()) {
            return t.second.size;
        }
        return 0;
    }

    @Override
    public String nextString(String element, int k) {
        if (contains(element)) {
            k++;
        } else {
            if (k == 0) {
                return null;
            }
        }
        var t = root.traverse(element, 0, k);

        Node node = t.second;
        if (node == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        while (node != root) {
            result.append(node.c);
            node = node.parent;
        }
        return result.reverse().toString();
    }

    private static class Pair<T1, T2> { //why doesn't java have it???
        public T1 first;
        public T2 second;

        Pair(T1 fst, T2 snd) {
            first = fst;
            second = snd;
        }
    }

    private static class Node {
        public char c;
        public boolean isTerminal;
        int size;
        SortedMap<Character, Node> children = new TreeMap<>();
        Node parent;

        public Node getNext(char c) {
            return children.get(c);
        }

        public Node addNext(char c) {
            if (children.containsKey(c)) {
                return children.get(c);
            }
            Node t = new Node();
            t.c = c;
            t.parent = this;
            children.put(c, t);
            return t;
        }

        private Pair<Integer, Node> traverse(String element, int pos, int k) {
            if (isTerminal) k--;
            if (k == 0) {
                return new Pair<>(k, this);
            }
            char curr = 0;
            boolean flag = pos < element.length();
            if (flag) {
                curr = element.charAt(pos);
            }
            for (var entry : children.entrySet()) {
                if (flag) {
                    if (entry.getKey() != curr) {
                        continue;
                    }
                    flag = false;
                }

                var t = entry.getValue().traverse(element, pos + 1, k);
                pos = element.length();
                if (t.first == 0) {
                    return t;
                }
                k = t.first;
            }
            return new Pair<>(k, null);
        }
    }
}
