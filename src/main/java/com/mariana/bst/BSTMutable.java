package com.mariana.bst;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BSTMutable {
    protected Node root;
    private Random random = new Random(10);

    public BSTMutable(Node root) {
        this.root = root;
    }

    /**
     * @param from key
     * @param to   key
     * @return new BSTMutable object, including from and to Nodes, if they are present
     */
    public BSTMutable subSet(int from, int to) {
        BSTMutable subSet = new BSTMutable(root.deepCopy());
        Pair<BSTMutable> cutLeft = subSet.split(from - 1);
        Pair<BSTMutable> cutRight = cutLeft.right.split(to);
        return cutRight.left;
    }

    public BSTMutable insert(Node newNode) {
        Pair<BSTMutable> splittedByNew = split(newNode.getKey());
        Node mergedWithNew = merge(splittedByNew.left.root, newNode);
        this.root = merge(mergedWithNew, splittedByNew.right.root);
        return this;
    }

    public BSTMutable delete(int key) {
        Pair<BSTMutable> splitted = split(key);
        Pair<BSTMutable> newLeft = splitted.left.split(key - 1);
        this.root = merge(newLeft.left.root, splitted.right.root);
        return this;
    }

    public Pair<BSTMutable> split(int key) {
        return split(root.deepCopy(), key).map(BSTMutable::new);
    }

    public BSTMutable merge(BSTMutable other) {
        this.root = merge(this.root, other.root);
        return this;
    }

    private Node merge(Node left, Node right) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        if (left.key >= right.key) {
            Node temp = left;
            left = right;
            right = temp;
        }
        if (random.nextDouble() < (left.size / (double) (left.size + right.size))) {
            left.right = merge(left.right, right);
            left.updateSize();
            return left;
        } else {
            right.left = merge(left, right.left);
            right.updateSize();
            return right;
        }
    }

    private Pair<Node> split(Node node, int key) {
        if (node == null) {
            return new Pair<>(null, null);
        }
        if (node.key <= key) {
            Pair<Node> pair = split(node.right, key);
            node.right = pair.left;
            node.updateSize();
            return new Pair<>(node, pair.right);
        } else {
            Pair<Node> pair = split(node.left, key);
            node.left = pair.right;
            node.updateSize();
            return new Pair<>(pair.left, node);
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        print(root, sb);
        return sb.toString();
    }

    private void print(Node node, StringBuffer sb) {
        if (node != null) {
            print(node.left, sb);
            sb.append(node + "\n");
            print(node.right, sb);
        }
    }

    List<Node> traverse() {
        return traverse(root, new ArrayList<>());
    }

    private List<Node> traverse(Node node, List<Node> holder) {
        if (node != null) {
            traverse(node.left, holder);
            holder.add(node);
            traverse(node.right, holder);
        }
        return holder;
    }

    static class Node {
        int key;
        int value;
        Node left;
        Node right;
        long size;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.size = 1;
        }

        public Node(int key, int value, Node left, Node right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.updateSize();
        }

        private Node(Node other) {
            this.key = other.getKey();
            this.value = other.getValue();
            this.left = other.getLeft() == null ? null : other.getLeft().deepCopy();
            this.right = other.getRight() == null ? null : other.getRight().deepCopy();
            this.updateSize();
        }

        public void updateSize() {
            this.size = size(left) + size(right) + 1;
        }

        private static long size(Node node) {
            if (node == null) {
                return 0;
            }
            return node.size;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public int getKey() {
            return key;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }

        public Node deepCopy() {
            return new Node(this);
        }
    }

    public long getSize() {
        return root.size;
    }
}
