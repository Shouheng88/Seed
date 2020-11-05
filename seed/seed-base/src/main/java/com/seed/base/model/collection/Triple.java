package com.seed.base.model.collection;

/**
 * Custom collection for three elements.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @date 2020/6/23 22:59
 */
public class Triple<A, B, C> {

    private A first;

    private B second;

    private C third;

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

    public C getThird() {
        return third;
    }

    public void setThird(C third) {
        this.third = third;
    }

    @Override
    public String toString() {
        return "Triple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}
