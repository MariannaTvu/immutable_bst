package com.mariana.bst;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BSTImmutable<K extends Comparable<? super K>> implements Comparable<BSTImmutable<K>> {

    private static final Random random = new Random(10);

    private final Node<K> root;

    protected BSTImmutable(Node<K> root) {
        this.root = root;
    }

    public BSTImmutable<K> insert(Node<K> newNode) {
        return insert(newNode.getKey());
    }

    public BSTImmutable<K> insert(K key) {
        Pair<BSTImmutable<K>> splitted = split(key);
        if (treeContains(key, splitted)) return this;
        Node<K> mergedWithNew = merge(splitted.left.root, new Node<>(key));
        Node<K> newRoot = merge(mergedWithNew, splitted.right.root);
        return new BSTImmutable<>(newRoot);
    }

    private boolean treeContains(K key, Pair<BSTImmutable<K>> splittedByNew) {
        if ((splittedByNew.getLeft().getRoot() != null && splittedByNew.getLeft().getRoot().getKey().equals(key))){
            return true;
        }
        return false;
    }

    public long count(K from, K to) {
        return count(root, from, to);
    }

    private long count(Node<K> node, K from, K to) {
        if (node == null) {
            return 0;
        }
        if (node.getKey().compareTo(from) < 0) {
            return count(node.getRight(), from, to);
        }
        if (node.getKey().compareTo(to) > 0) {
            return count(node.getLeft(), from, to);
        }
        return countRight(node.getLeft(), from) + 1 + countLeft(node.getRight(), to);
    }

    private long countRight(Node<K> node, K key) {
        if (node == null) {
            return 0;
        }
        if (node.getKey().compareTo(key) < 0) {
            return countRight(node.getRight(), key);
        }
        if (node.getKey().compareTo(key) > 0) {
            return countRight(node.getLeft(), key) + 1 + Node.size(node.getRight());
        }
        return 1 + Node.size(node.getRight());
    }

    private long countLeft(Node<K> node, K key) {
        if (node == null) {
            return 0;
        }
        if (node.getKey().compareTo(key) > 0) {
            return countLeft(node.getLeft(), key);
        }
        if (node.getKey().compareTo(key) < 0) {
            return Node.size(node.getLeft()) + 1 + countLeft(node.getRight(), key);
        }
        return 1 + countLeft(node.getRight(), key);
    }

    public BSTImmutable<K> delete(K key) {
        Pair<BSTImmutable<K>> splitted = split(key);
        Node<K> newRoot = merge(deleteHigher(splitted.left.root, key), splitted.right.root);
        return new BSTImmutable<>(newRoot);
    }

    private Node<K> deleteHigher(Node<K> node, K key) {
        if (node == null) {
            return null;
        }
        if (node.key.equals(key)) {
            return node.left;
        }
        return new Node<>(node.key, node.left, deleteHigher(node.right, key));
    }

    public Pair<BSTImmutable<K>> split(K key) {
        return split(root, key).map(BSTImmutable::new);
    }

    public BSTImmutable<K> merge(BSTImmutable<K> other) {
        Node<K> newRoot = merge(this.root, other.root);
        return new BSTImmutable<>(newRoot);
    }

    private Node<K> merge(Node<K> a, Node<K> b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (random.nextDouble() < (a.size / (double) (a.size + b.size))) {
            return new Node<>(a.key, a.getLeft(), new Node<>(merge(a.getRight(), b)));
        } else {
            return new Node<>(b.getKey(), new Node<>(merge(a, b.getLeft())), b.getRight());
        }
    }

    private Pair<Node<K>> split(Node<K> node, K key) {
        if (node == null) {
            return new Pair<>(null, null);
        }
        if (node.key.compareTo(key) <= 0) {
            Pair<Node<K>> pair = split(node.right, key);
            Node<K> newNode = new Node<>(node.getKey(), node.getLeft(), pair.getLeft());
            return new Pair<>(newNode, pair.right);
        } else {
            Pair<Node<K>> pair = split(node.left, key);
            Node<K> newNode = new Node<>(node.getKey(), pair.getRight(), node.getRight());
            return new Pair<>(pair.getLeft(), newNode);
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        print(root, sb);
        return sb.toString();
    }

    private void print(Node<K> node, StringBuffer sb) {
        if (node != null) {
            print(node.left, sb);
            sb.append(node + "\n");
            print(node.right, sb);
        }
    }

    List<Node<K>> traverse() {
        return traverse(root, new ArrayList<>());
    }

    private List<Node<K>> traverse(Node<K> node, List<Node<K>> holder) {
        if (node != null) {
            traverse(node.left, holder);
            holder.add(node);
            traverse(node.right, holder);
        }
        return holder;
    }

    @Override
    public int compareTo(BSTImmutable<K> o) {
        return Math.toIntExact(this.root.getSize() - o.root.getSize());
    }

    static final class Node<K extends Comparable<? super K>> {
        private final K key;
        private final Node<K> left;
        private final Node<K> right;
        private final long size;

        public Node(K key) {
            this(key, null, null);
        }

        public Node(K key, Node<K> left, Node<K> right) {
            this.key = key;
            this.left = left;
            this.right = right;
            this.size = size(left) + size(right) + 1;
        }

        Node(Node<K> other) {
            this.key = other.getKey();
            this.left = other.getLeft();
            this.right = other.getRight();
            this.size = other.getSize();
        }

        private static long size(Node<?> node) {
            if (node == null) {
                return 0;
            }
            return node.size;
        }

        public Node<K> getLeft() {
            return left;
        }

        public Node<K> getRight() {
            return right;
        }

        public K getKey() {
            return key;
        }

        public long getSize() {
            return size(left) + size(right) + 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(key, node.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    '}';
        }
    }

    public Node<K> getRoot() {
        return root;
    }

    public long getSize() {
        return root.getSize();
    }
}
