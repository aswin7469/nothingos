package com.google.android.setupcompat.internal;

import java.util.concurrent.TimeUnit;

public class ClockProvider {
    private static final Ticker SYSTEM_TICKER;
    private static Ticker ticker;

    public interface Supplier<T> {
        T get();
    }

    public static long timeInNanos() {
        return ticker.read();
    }

    public static long timeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(timeInNanos());
    }

    public static void resetInstance() {
        ticker = SYSTEM_TICKER;
    }

    public static void setInstance(Supplier<Long> supplier) {
        ticker = new ClockProvider$$ExternalSyntheticLambda1(supplier);
    }

    static {
        ClockProvider$$ExternalSyntheticLambda0 clockProvider$$ExternalSyntheticLambda0 = new ClockProvider$$ExternalSyntheticLambda0();
        SYSTEM_TICKER = clockProvider$$ExternalSyntheticLambda0;
        ticker = clockProvider$$ExternalSyntheticLambda0;
    }
}
