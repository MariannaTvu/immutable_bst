package com.mariana.bst;

import java.util.Comparator;
import java.util.Objects;

public class Key implements Comparable<Key>{
    private final Integer y;
    private final Integer id;

    public Key(Integer y, Integer id) {
        this.y = y;
        this.id = id;
    }

    public Integer getY() {
        return y;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public int compareTo(Key o) {
        return Comparator.comparingInt(Key::getY)
                .thenComparingInt(Key::getId)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return "Key{" +
                "y=" + y +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return Objects.equals(y, key.y) &&
                Objects.equals(id, key.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, id);
    }
}
