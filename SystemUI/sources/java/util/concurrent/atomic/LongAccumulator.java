package java.util.concurrent.atomic;

import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.util.concurrent.atomic.Striped64;
import java.util.function.LongBinaryOperator;

public class LongAccumulator extends Striped64 implements Serializable {
    private static final long serialVersionUID = 7249069246863182397L;
    private final LongBinaryOperator function;
    private final long identity;

    public LongAccumulator(LongBinaryOperator longBinaryOperator, long j) {
        this.function = longBinaryOperator;
        this.identity = j;
        this.base = j;
    }

    public void accumulate(long j) {
        int length;
        Striped64.Cell cell;
        Striped64.Cell[] cellArr = this.cells;
        if (cellArr == null) {
            LongBinaryOperator longBinaryOperator = this.function;
            long j2 = this.base;
            long applyAsLong = longBinaryOperator.applyAsLong(j2, j);
            if (applyAsLong == j2 || casBase(j2, applyAsLong)) {
                return;
            }
        }
        boolean z = true;
        if (!(cellArr == null || (length = cellArr.length - 1) < 0 || (cell = cellArr[length & getProbe()]) == null)) {
            LongBinaryOperator longBinaryOperator2 = this.function;
            long j3 = cell.value;
            long applyAsLong2 = longBinaryOperator2.applyAsLong(j3, j);
            if (applyAsLong2 != j3 && !cell.cas(j3, applyAsLong2)) {
                z = false;
            }
            if (z) {
                return;
            }
        }
        longAccumulate(j, this.function, z);
    }

    public long get() {
        Striped64.Cell[] cellArr = this.cells;
        long j = this.base;
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    j = this.function.applyAsLong(j, cell.value);
                }
            }
        }
        return j;
    }

    public void reset() {
        Striped64.Cell[] cellArr = this.cells;
        this.base = this.identity;
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    cell.reset(this.identity);
                }
            }
        }
    }

    public long getThenReset() {
        Striped64.Cell[] cellArr = this.cells;
        long andSetBase = getAndSetBase(this.identity);
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    andSetBase = this.function.applyAsLong(andSetBase, cell.getAndSet(this.identity));
                }
            }
        }
        return andSetBase;
    }

    public String toString() {
        return Long.toString(get());
    }

    public long longValue() {
        return get();
    }

    public int intValue() {
        return (int) get();
    }

    public float floatValue() {
        return (float) get();
    }

    public double doubleValue() {
        return (double) get();
    }

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 7249069246863182397L;
        private final LongBinaryOperator function;
        private final long identity;
        private final long value;

        SerializationProxy(long j, LongBinaryOperator longBinaryOperator, long j2) {
            this.value = j;
            this.function = longBinaryOperator;
            this.identity = j2;
        }

        private Object readResolve() {
            LongAccumulator longAccumulator = new LongAccumulator(this.function, this.identity);
            longAccumulator.base = this.value;
            return longAccumulator;
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(get(), this.function, this.identity);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}
