package com.seed.base.model.collection;

/**
 * Custom collection for two elements.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @date 2020/6/23 22:59
 */
public class Pair<A, B> {

    private A first;

    private B second;

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
