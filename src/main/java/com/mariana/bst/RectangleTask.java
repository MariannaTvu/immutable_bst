package com.mariana.bst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.mariana.bst.BSTImmutable.*;

public class RectangleTask {

    private static final Logger logger = LoggerFactory.getLogger(RectangleTask.class);

    private TreeMap<Integer, BSTImmutable<Key>> snapshots = new TreeMap<>();

    void addPoints(List<Point> points) {
        if (points.isEmpty()) {
            throw new RuntimeException("Empty points set");
        }
        points.sort(Comparator.comparingInt(Point::getX));                          
        int id = 0;
        BSTImmutable<Key> bst =
                new BSTImmutable<>(
                        new Node<>(new Key(points.get(0).y, id)));
        snapshots.put(points.get(0).x, bst);
        for (Point point : points.subList(1, points.size())) {
            id++;
            bst = bst.insert(new Node<>(new Key(point.y, id)));
            snapshots.put(point.x, bst);
        }
    }

    /**
     * @param a point of diagonally. Inclusive
     * @param b opposite point of rectangle diagonally. Inclusive
     * @return long - number of points inside rectangle
     */
    long calculate(Point a, Point b) {
        Pair<Point> sortedPoints = sortInputPoints(a, b);
        Point topLeft = sortedPoints.left;
        Point bottomRight = sortedPoints.right;
        long countFirst = countFirst(topLeft, bottomRight, topLeft.x);
        long countLast = countLast(topLeft, bottomRight, bottomRight.x);
        return countLast - countFirst;
    }

    private Pair<Point> sortInputPoints(Point a, Point b) {
        if (a == null || b == null || a.equals(b)) {
            throw new RuntimeException("Incorrect input");
        }
        return (a.x > b.x) ? new Pair<>(b, a) : new Pair<>(a, b);
    }

    private Long countFirst(Point bottomLeft, Point topRight, int x) {
        if (x < snapshots.firstKey()) {
            return 0L;
        }
        BSTImmutable<Key> bst = snapshots.lowerEntry(x).getValue();
        return bst.count(new Key(topRight.getY(), Integer.MIN_VALUE), new Key(bottomLeft.getY(), Integer.MAX_VALUE));
    }

    private Long countLast(Point bottomLeft, Point topRight, int x) {
        if (x < snapshots.firstKey()) {
            return 0L;
        }
        BSTImmutable<Key> bst = snapshots.floorEntry(x).getValue();
        return bst.count(new Key(topRight.getY(), Integer.MIN_VALUE), new Key(bottomLeft.getY(), Integer.MAX_VALUE));
    }

    public TreeMap<Integer, BSTImmutable<Key>> getSnapshots() {
        return snapshots;
    }

    static class Point {
        final int x;
        final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

}
