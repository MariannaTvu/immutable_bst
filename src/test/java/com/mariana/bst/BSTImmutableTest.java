package com.mariana.bst;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.mariana.bst.BSTImmutable.*;
import static org.assertj.core.api.Assertions.assertThat;

class BSTImmutableTest {

    @Test
    void testNodes(){
        BSTImmutable<Integer> bst = getNewBst();
        assertThat(bst.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12);
    }

    @Test
    void insertTest(){
        BSTImmutable<Integer> bst = getNewBst();
        assertThat(bst.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12);
        assertThat(bst.insert(1).traverse())
                .extracting(Node::getKey).containsExactly(1, 3, 4, 5, 12);
        assertThat(bst.insert(120).traverse())
                .extracting(Node::getKey).containsExactly(3, 4, 5, 12, 120);
        assertThat(bst.insert(7).traverse())
                .extracting(Node::getKey).containsExactly(3, 4, 5, 7, 12);
        assertThat(bst.insert(5).traverse())
                .extracting(Node::getKey).containsExactly(3, 4, 5, 12);
        assertThat(bst.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12);
    }

    @Test
    void deleteTest(){
        BSTImmutable<Integer> bst = getNewBst();
        assertThat(bst.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12);
        assertThat(bst.delete(3).traverse()).extracting(Node::getKey).containsExactly(4, 5, 12);
        assertThat(bst.delete(4).traverse()).extracting(Node::getKey).containsExactly(3, 5, 12);
        assertThat(bst.delete(5).traverse()).extracting(Node::getKey).containsExactly(3, 4, 12);
        assertThat(bst.delete(12).traverse()).extracting(Node::getKey).containsExactly(3, 4, 5);
        assertThat(bst.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12);
    }

    @Test
    void sizeTest() {
        assertThat(getNewBst().getSize()).isEqualTo(4L);
        assertThat(getNewBst().split(4))
                .satisfies(res -> assertThat(res.getLeft().getSize()).isEqualTo(2L))
                .satisfies(res -> assertThat(res.getRight().getSize()).isEqualTo(2L));
    }

    @Test
    void splitTest(){
        BSTImmutable<Integer> bst = getNewBst();
        assertThat(bst.split(4))
                .satisfies(res -> assertThat(res.left.traverse()).extracting(Node::getKey)
                        .containsExactly(3, 4))
                .satisfies(res -> assertThat(res.right.traverse()).extracting(Node::getKey)
                        .containsExactly(5, 12));
        assertThat(bst.split(7))
                .satisfies(res -> assertThat(res.left.traverse()).extracting(Node::getKey)
                        .containsExactly(3, 4, 5))
                .satisfies(res -> assertThat(res.right.traverse()).extracting(Node::getKey)
                        .containsExactly(12));
        assertThat(bst.split(1))
                .satisfies(res -> assertThat(res.left.traverse()).extracting(Node::getKey).isEmpty())
                .satisfies(res -> assertThat(res.right.traverse()).extracting(Node::getKey)
                        .containsExactly(3, 4, 5, 12));
        assertThat(bst.split(15))
                .satisfies(res -> assertThat(res.left.traverse()).extracting(Node::getKey)
                        .containsExactly(3, 4, 5, 12))
                .satisfies(res -> assertThat(res.right.traverse()).extracting(Node::getKey)
                        .isEmpty());
        assertThat(bst.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12);
    }

    @Test
    void mergeTest(){
        BSTImmutable<Integer> bst = getNewBst(); // 3 4 5 12
        Assertions.assertThat(
                bst.merge(getNewBstBigger()).traverse()).extracting(Node::getKey)
                .containsExactly(3, 4, 5, 12, 13, 14, 15, 22);
        Assertions.assertThat(
                getNewBstSmaller().merge(bst).traverse()).extracting(Node::getKey)
                .containsExactly(-4, -3, -2, -1, 3, 4, 5, 12);
        assertThat(bst.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12);
    }

    @Test
    void insertTestRandom(){
        BSTImmutable<Key> bst = new BSTImmutable<>(null);
        List<Key> sortedKeys = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Key key = new Key(random.nextInt(), i);
            sortedKeys.add(key);
            bst = bst.insert(key);
        }
        Collections.sort(sortedKeys);
        Assertions.assertThat(bst.traverse()).extracting(Node::getKey).containsExactlyElementsOf(sortedKeys);
    }

    @Test
    void countTest(){
        BSTImmutable<Integer> bst = getNewBst();
        Assertions.assertThat(bst.count(2, 10)).isEqualTo(3);
        Assertions.assertThat(bst.count(4, 10)).isEqualTo(2);
        Assertions.assertThat(bst.count(11, 10)).isEqualTo(0);
    }

    private BSTImmutable<Integer> getNewBst() {
        return new BSTImmutable<>(
                new Node<>(5,
                        new Node<>(4,
                                new Node<>(3),
                                null),
                        new Node<>(12)
                )
        );
    }

    private BSTImmutable<Integer> getNewBstBigger() {
        return new BSTImmutable<>(
                new Node<>(15,
                        new Node<>(14,
                                new Node<>(13),
                                null),
                        new Node<>(22)
                )
        );
    }

    private BSTImmutable<Integer> getNewBstSmaller() {
        return new BSTImmutable<>(
                new Node<>(-2,
                        new Node<>(-3,
                                new Node<>(-4),
                                null),
                        new Node<>(-1)
                )
        );
    }

}