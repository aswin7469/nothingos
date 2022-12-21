package java.util.concurrent.atomic;

import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.util.concurrent.atomic.Striped64;
import java.util.function.DoubleBinaryOperator;

public class DoubleAdder extends Striped64 implements Serializable {
    private static final long serialVersionUID = 7249069246863182397L;

    public void add(double d) {
        int length;
        Striped64.Cell cell;
        Striped64.Cell[] cellArr = this.cells;
        if (cellArr == null) {
            long j = this.base;
            if (casBase(j, Double.doubleToRawLongBits(Double.longBitsToDouble(j) + d))) {
                return;
            }
        }
        boolean z = true;
        if (!(cellArr == null || (length = cellArr.length - 1) < 0 || (cell = cellArr[length & getProbe()]) == null)) {
            long j2 = cell.value;
            z = cell.cas(j2, Double.doubleToRawLongBits(Double.longBitsToDouble(j2) + d));
            if (z) {
                return;
            }
        }
        doubleAccumulate(d, (DoubleBinaryOperator) null, z);
    }

    public double sum() {
        Striped64.Cell[] cellArr = this.cells;
        double longBitsToDouble = Double.longBitsToDouble(this.base);
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    longBitsToDouble += Double.longBitsToDouble(cell.value);
                }
            }
        }
        return longBitsToDouble;
    }

    public void reset() {
        Striped64.Cell[] cellArr = this.cells;
        this.base = 0;
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    cell.reset();
                }
            }
        }
    }

    public double sumThenReset() {
        Striped64.Cell[] cellArr = this.cells;
        double longBitsToDouble = Double.longBitsToDouble(getAndSetBase(0));
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    longBitsToDouble += Double.longBitsToDouble(cell.getAndSet(0));
                }
            }
        }
        return longBitsToDouble;
    }

    public String toString() {
        return Double.toString(sum());
    }

    public double doubleValue() {
        return sum();
    }

    public long longValue() {
        return (long) sum();
    }

    public int intValue() {
        return (int) sum();
    }

    public float floatValue() {
        return (float) sum();
    }

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 7249069246863182397L;
        private final double value;

        SerializationProxy(DoubleAdder doubleAdder) {
            this.value = doubleAdder.sum();
        }

        private Object readResolve() {
            DoubleAdder doubleAdder = new DoubleAdder();
            doubleAdder.base = Double.doubleToRawLongBits(this.value);
            return doubleAdder;
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}
