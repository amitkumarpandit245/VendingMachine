package com.deloitte.bucket;

/**
 * Generic class to store Item and Coin for a Transaction
 * @param <B1>
 * @param <B2>
 */
public class Bucket<B1, B2> {
    private B1 first;
    private B2 second;

    public Bucket(B1 first, B2 second) {
        this.first = first;
        this.second = second;
    }

    public B1 getFirst() {
        return first;
    }

    public B2 getSecond() {
        return second;
    }
}
