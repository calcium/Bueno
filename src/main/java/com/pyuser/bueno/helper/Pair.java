package com.pyuser.bueno.helper;

/**
 * Class in lieu of using org.apache.commons.lang3.tuple.Pair
 * @param <L>
 * @param <R>
 */
public class Pair <L, R> {
    private L _left;
    private R _right;

    public Pair(L _left, R _right) {
        this._left = _left;
        this._right = _right;
    }

    public L getLeft() {
        return _left;
    }

    public R getRight() {
        return _right;
    }

    @Override
    public String toString() {
        return "("
                + _right +
                ", " + _left +
                '}';
    }
}
