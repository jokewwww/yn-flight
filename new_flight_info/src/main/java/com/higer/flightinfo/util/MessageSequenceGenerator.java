package com.higer.flightinfo.util;

import java.util.concurrent.atomic.AtomicLong;

public class MessageSequenceGenerator {
    private static final AtomicLong sequence = new AtomicLong(0);

    public static long getNextSequence() {
        return sequence.incrementAndGet();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println("Message sequence: " + getNextSequence());
        }
    }
}