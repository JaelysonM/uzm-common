package com.uzm.common.java.types;

public final class Pair<X, Y> {

    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    private X x;
    private Y y;

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }
}