package com.mariana.bst;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.mariana.bst.BSTImmutable.*;
import static com.mariana.bst.RectangleTask.Point;

class RectangleTaskTest {

    private static final Logger logger = LoggerFactory.getLogger(RectangleTask.class);

    @Test
    void addPoints() {
        RectangleTask task = new RectangleTask();
        task.addPoints(getPoints());
        Assertions.assertThat(task.getSnapshots().size()).isEqualTo(4);
    }

    @Test
    void calculateNumPoints() {
        RectangleTask task = new RectangleTask();
        task.addPoints(getPoints());
        Assertions.assertThat(task.calculate(new Point(-4, 3), new Point(2, 1)))
                .isEqualTo(3);
        Assertions.assertThat(task.calculate(new Point(-6, 3), new Point(2, 1)))
                .isEqualTo(4);
        Assertions.assertThat(task.calculate(new Point(-4, 5), new Point(2, 1)))
                .isEqualTo(4);
        Assertions.assertThat(task.calculate(new Point(-3, 3), new Point(1, 0)))
                .isEqualTo(2);
    }

    @Disabled
    @Test
    void lotsOfPointsTest() {
        RectangleTask task = new RectangleTask();
        System.out.println(newTask(task));
    }

    private long newTask(RectangleTask task) {
        Random random = new Random();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 1_000_000; i++) {
            if (i % 10_000 == 0) {
                logger.debug("Generate i = " + i);
            }
            int x = random.nextInt();
            int y = random.nextInt();
           // System.out.println(x);
            points.add(new Point(x, y));
        }
        logger.info("Add to task");
        task.addPoints(points);
        long start = System.currentTimeMillis();
        logger.info("started");
        logger.info("Result: {}", task.calculate(new Point(Integer.MIN_VALUE, Integer.MAX_VALUE),
                new Point(Integer.MAX_VALUE, Integer.MIN_VALUE)));
        long end = System.currentTimeMillis();
        logger.info("Time: {} ms", end - start);
        return end - start;
    }

    @Disabled
    @Test
    void presentationTest1(){
        RectangleTask task = new RectangleTask();
        List<Point> testPoints = new ArrayList<>(getTestPoints());
        task.addPoints(testPoints);
        task.calculate(new Point(Integer.MIN_VALUE, Integer.MAX_VALUE),
                new Point(Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    @Disabled
    @Test
    void presentationTest2(){
        List<Point> testPoints = new ArrayList<>(getTestPoints());
        RectangleTask task1 = new RectangleTask();
        task1.addPoints(testPoints);
        testPoints.add(new Point(16, 16));
        List<Key> sortedPoints = testPoints.stream().map(p -> new Key(p.getY(), new Random().nextInt())).sorted().collect(Collectors.toList());
        BSTImmutable<Key> value = task1.getSnapshots().lastEntry().getValue();
        value = value.insert(new Key(16, 16));
        Assertions.assertThat(value.traverse()).extracting(Node::getKey).containsExactlyElementsOf(sortedPoints);

    }

    private List<Point> getPoints() {
        return Arrays.asList(
                new Point(1, 2),
                new Point(-5, 1),
                new Point(-4, 2),
                new Point(1, 1),
                new Point(3, 2),
                new Point(1, 4)
        );
    }

    private List<Point> getTestPoints() {
        return Arrays.asList(
                new Point(12, 12),
                new Point(-5, 8),
                new Point(-4, 9),
                new Point(1, 10),
                new Point(3, 4),
                new Point(3, 15),
                new Point(3, 23),
                new Point(3, 19),
                new Point(3, 25),
                new Point(1, 17)
        );
    }
}