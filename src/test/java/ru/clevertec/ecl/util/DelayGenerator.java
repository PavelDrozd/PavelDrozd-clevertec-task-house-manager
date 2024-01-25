package ru.clevertec.ecl.util;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DelayGenerator {

    private final TimeUnit timeUnit;

    private final long from;

    private final long to;

    private final Random random = new Random();

    public DelayGenerator(long from, long to, TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        this.from = from;
        this.to = to;
    }

    public static DelayGenerator getRandomDelayGenerator(long from, long to, TimeUnit timeUnit) {
        return new DelayGenerator(from, to, timeUnit);
    }

    public void delay() {
        long range = random.nextLong(from, to);
        try {
            timeUnit.sleep(range);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
