package ru.itmo.mit.trie;

import java.util.SortedMap;
import java.util.TreeMap;

public class TrieImpl implements Trie {

    Node root = new Node();

    private Pair<Integer, Node> tryGetElement(String element) {
        var node = root;
        int nodeDepth = 0;
        for (char c : element.toCharArray()) {
            Node nextNode = node.getNext(c);
            if (nextNode == null) {
                break;
            }
            node = nextNode;
            nodeDepth++;
        }
        return new Pair<>(nodeDepth, node);
    }

    @Override
    public boolean add(String element) {
        var truGetElementResult = tryGetElement(element);
        if (truGetElementResult.first == element.length() && truGetElementResult.second.isTerminal) {
            return false;
        }
        var node = truGetElementResult.second;
        for (char c : element.toCharArray()) {
            if (truGetElementResult.first-- > 0) {
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
        var tryGetElementResult = tryGetElement(element);
        return tryGetElementResult.first == element.length() && tryGetElementResult.second.isTerminal;
    }

    @Override
    public boolean remove(String element) {
        var tryGetElementResult = tryGetElement(element);
        if (tryGetElementResult.first == element.length() && tryGetElementResult.second.isTerminal) {
            tryGetElementResult.second.isTerminal = false;
            Node node = tryGetElementResult.second;
            Node parentNode = node.parent;
            while (parentNode != null) {
                parentNode.children.remove(node.c);
                if (parentNode.children.size() == 0 && !parentNode.isTerminal) {
                    node = parentNode;
                    parentNode = node.parent;
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
        var tryGetElementResult = tryGetElement(prefix);
        if (tryGetElementResult.first == prefix.length()) {
            return tryGetElementResult.second.size;
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
        var traverseResult = root.traverse(element, 0, k);

        Node node = traverseResult.second;
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
            var nextNode = new Node();
            nextNode.c = c;
            nextNode.parent = this;
            children.put(c, nextNode);
            return nextNode;
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

                var traverseResult = entry.getValue().traverse(element, pos + 1, k);
                pos = element.length();
                if (traverseResult.first == 0) {
                    return traverseResult;
                }
                k = traverseResult.first;
            }
            return new Pair<>(k, null);
        }
    }
}
