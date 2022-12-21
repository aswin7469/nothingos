package java.util;

import java.util.function.IntConsumer;

public class IntSummaryStatistics implements IntConsumer {
    private long count;
    private int max = Integer.MIN_VALUE;
    private int min = Integer.MAX_VALUE;
    private long sum;

    public IntSummaryStatistics() {
    }

    public IntSummaryStatistics(long j, int i, int i2, long j2) throws IllegalArgumentException {
        int i3 = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i3 < 0) {
            throw new IllegalArgumentException("Negative count value");
        } else if (i3 <= 0) {
        } else {
            if (i <= i2) {
                this.count = j;
                this.sum = j2;
                this.min = i;
                this.max = i2;
                return;
            }
            throw new IllegalArgumentException("Minimum greater than maximum");
        }
    }

    public void accept(int i) {
        this.count++;
        this.sum += (long) i;
        this.min = Math.min(this.min, i);
        this.max = Math.max(this.max, i);
    }

    public void combine(IntSummaryStatistics intSummaryStatistics) {
        this.count += intSummaryStatistics.count;
        this.sum += intSummaryStatistics.sum;
        this.min = Math.min(this.min, intSummaryStatistics.min);
        this.max = Math.max(this.max, intSummaryStatistics.max);
    }

    public final long getCount() {
        return this.count;
    }

    public final long getSum() {
        return this.sum;
    }

    public final int getMin() {
        return this.min;
    }

    public final int getMax() {
        return this.max;
    }

    public final double getAverage() {
        if (getCount() > 0) {
            return ((double) getSum()) / ((double) getCount());
        }
        return 0.0d;
    }

    public String toString() {
        return String.format("%s{count=%d, sum=%d, min=%d, average=%f, max=%d}", getClass().getSimpleName(), Long.valueOf(getCount()), Long.valueOf(getSum()), Integer.valueOf(getMin()), Double.valueOf(getAverage()), Integer.valueOf(getMax()));
    }
}
