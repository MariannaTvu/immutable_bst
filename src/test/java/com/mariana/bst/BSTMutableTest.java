package com.mariana.bst;

import com.mariana.bst.BSTMutable.Node;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BSTMutableTest {

    @Test
    void toStringTest() {
        BSTMutable bstMutable = getNewBst();
        String res = new Node(3, 5).toString() + "\n" +
                new Node(4, 6, new Node(3, 5), null).toString() + "\n" +
                new Node(5, 7, new Node(4, 6, new Node(3, 5), null), new Node(12, 12)).toString() + "\n" +
                new Node(12, 12).toString() + "\n";
        System.out.println(res);
        assertEquals(bstMutable.toString(), res);
    }

    @Test
    void testNodes() {
        BSTMutable bstMutable = getNewBst();
        assertThat(bstMutable.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12);
    }

    @Test
    void splitTest() {
        assertThat(getNewBst().split(4))
                .satisfies(res -> assertThat(res.left.traverse()).extracting(Node::getKey).containsExactly(3, 4))
                .satisfies(res -> assertThat(res.right.traverse()).extracting(Node::getKey).containsExactly(5, 12));
        assertThat(getNewBst().split(7))
                .satisfies(res -> assertThat(res.left.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5))
                .satisfies(res -> assertThat(res.right.traverse()).extracting(Node::getKey).containsExactly(12));
        assertThat(getNewBst().split(1))
                .satisfies(res -> assertThat(res.left.traverse()).extracting(Node::getKey).isEmpty())
                .satisfies(res -> assertThat(res.right.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12));
        assertThat(getNewBst().split(15))
                .satisfies(res -> assertThat(res.left.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12))
                .satisfies(res -> assertThat(res.right.traverse()).extracting(Node::getKey).isEmpty());
    }

    @Test
    void deleteTest(){
        BSTMutable bst = getNewBst();
        assertThat(bst.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12);
        assertThat(bst.delete(3).traverse()).extracting(Node::getKey).containsExactly(4, 5, 12);
        bst = getNewBst();
        assertThat(bst.delete(4).traverse()).extracting(Node::getKey).containsExactly(3, 5, 12);
        bst = getNewBst();
        assertThat(bst.delete(5).traverse()).extracting(Node::getKey).containsExactly(3, 4, 12);
        bst = getNewBst();
        assertThat(bst.delete(12).traverse()).extracting(Node::getKey).containsExactly(3, 4, 5);
    }

    @Test
    void sizeTest() {
        assertThat(getNewBst().getSize()).isEqualTo(4L);
        Pair<BSTMutable> pair = getNewBst().split(4);
        assertThat(pair)
                .satisfies(res -> assertThat(res.left.getSize()).isEqualTo(2L))
                .satisfies(res -> assertThat(res.right.getSize()).isEqualTo(2L));
    }

    @Test
    void mergeTest() {
        BSTMutable bst = getNewBst();
        Assertions.assertThat(
                bst.merge(getNewBstBigger()).traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12, 13, 14, 15, 22);
        bst = getNewBst();
        Assertions.assertThat(
                bst.merge(getNewBstSmaller()).traverse()).extracting(Node::getKey).containsExactly(1, 2, 3, 3, 4, 4, 5, 12);
       }

    @Test
    void subSetTest(){
        BSTMutable bst = getNewBst();
        assertThat(bst.subSet(4, 7).traverse()).extracting(Node::getKey).containsExactly(4, 5);
        assertThat(bst.subSet(0, 17).traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12);
        assertThat(bst.subSet(12, 17).traverse()).extracting(Node::getKey).containsExactly(12);
        assertThat(bst.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12);
    }

    @Test
    void insertTest(){
        BSTMutable bst = getNewBst();
        assertThat(bst.traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12);
        assertThat(bst.insert(new Node(1, 2)).traverse()).extracting(Node::getKey).containsExactly(1, 3, 4, 5, 12);
        bst = getNewBst();
        assertThat(bst.insert(new Node(120, 2)).traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 12, 120);
        bst = getNewBst();
        assertThat(bst.insert(new Node(7, 2)).traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 7, 12);
        bst = getNewBst();
        assertThat(bst.insert(new Node(5, 2)).traverse()).extracting(Node::getKey).containsExactly(3, 4, 5, 5, 12);
    }

    private BSTMutable getNewBst() {
        return new BSTMutable(
                new Node(5, 7,
                        new Node(4, 6,
                                new Node(3, 5),
                                null),
                        new Node(12, 12)
                )
        );
    }

    private BSTMutable getNewBstBigger() {
        return new BSTMutable(
                new Node(15, 72,
                        new Node(14, 61,
                                new Node(13, 51),
                                null),
                        new Node(22, 122)
                )
        );
    }

    private BSTMutable getNewBstSmaller() {
        return new BSTMutable(
                new Node(3, 7,
                        new Node(2, 6,
                                new Node(1, 5),
                                null),
                        new Node(4, 12)
                )
        );
    }
}