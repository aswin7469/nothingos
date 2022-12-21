package java.util;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;
import java.p026io.StreamCorruptedException;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;
import jdk.internal.misc.Unsafe;

public class Random implements Serializable {
    static final String BadBound = "bound must be positive";
    static final String BadRange = "bound must be greater than origin";
    static final String BadSize = "size must be non-negative";
    private static final double DOUBLE_UNIT = 1.1102230246251565E-16d;
    private static final long addend = 11;
    private static final long mask = 281474976710655L;
    private static final long multiplier = 25214903917L;
    private static final long seedOffset;
    private static final AtomicLong seedUniquifier = new AtomicLong(8682522807148012L);
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("seed", Long.TYPE), new ObjectStreamField("nextNextGaussian", Double.TYPE), new ObjectStreamField("haveNextNextGaussian", Boolean.TYPE)};
    static final long serialVersionUID = 3905348978240129619L;
    private static final Unsafe unsafe;
    private boolean haveNextNextGaussian;
    private double nextNextGaussian;
    private final AtomicLong seed;

    private static long initialScramble(long j) {
        return (j ^ multiplier) & mask;
    }

    public Random() {
        this(seedUniquifier() ^ System.nanoTime());
    }

    private static long seedUniquifier() {
        AtomicLong atomicLong;
        long j;
        long j2;
        do {
            atomicLong = seedUniquifier;
            j = atomicLong.get();
            j2 = 1181783497276652981L * j;
        } while (!atomicLong.compareAndSet(j, j2));
        return j2;
    }

    static {
        Unsafe unsafe2 = Unsafe.getUnsafe();
        unsafe = unsafe2;
        try {
            seedOffset = unsafe2.objectFieldOffset(Random.class.getDeclaredField("seed"));
        } catch (Exception e) {
            throw new Error((Throwable) e);
        }
    }

    public Random(long j) {
        this.haveNextNextGaussian = false;
        if (getClass() == Random.class) {
            this.seed = new AtomicLong(initialScramble(j));
            return;
        }
        this.seed = new AtomicLong();
        setSeed(j);
    }

    public synchronized void setSeed(long j) {
        this.seed.set(initialScramble(j));
        this.haveNextNextGaussian = false;
    }

    /* access modifiers changed from: protected */
    public int next(int i) {
        long j;
        long j2;
        AtomicLong atomicLong = this.seed;
        do {
            j = atomicLong.get();
            j2 = ((multiplier * j) + addend) & mask;
        } while (!atomicLong.compareAndSet(j, j2));
        return (int) (j2 >>> (48 - i));
    }

    public void nextBytes(byte[] bArr) {
        int length = bArr.length;
        int i = 0;
        while (i < length) {
            int nextInt = nextInt();
            int min = Math.min(length - i, 4);
            while (true) {
                int i2 = min - 1;
                if (min > 0) {
                    bArr[i] = (byte) nextInt;
                    nextInt >>= 8;
                    i++;
                    min = i2;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final long internalNextLong(long j, long j2) {
        long nextLong = nextLong();
        if (j >= j2) {
            return nextLong;
        }
        long j3 = j2 - j;
        long j4 = j3 - 1;
        if ((j3 & j4) == 0) {
            return (nextLong & j4) + j;
        }
        if (j3 > 0) {
            while (true) {
                long j5 = nextLong >>> 1;
                long j6 = j5 + j4;
                long j7 = j5 % j3;
                if (j6 - j7 >= 0) {
                    return j7 + j;
                }
                nextLong = nextLong();
            }
        } else {
            while (true) {
                if (nextLong >= j && nextLong < j2) {
                    return nextLong;
                }
                nextLong = nextLong();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final int internalNextInt(int i, int i2) {
        if (i >= i2) {
            return nextInt();
        }
        int i3 = i2 - i;
        if (i3 > 0) {
            return nextInt(i3) + i;
        }
        while (true) {
            int nextInt = nextInt();
            if (nextInt >= i && nextInt < i2) {
                return nextInt;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final double internalNextDouble(double d, double d2) {
        double nextDouble = nextDouble();
        if (d >= d2) {
            return nextDouble;
        }
        double d3 = (nextDouble * (d2 - d)) + d;
        return d3 >= d2 ? Double.longBitsToDouble(Double.doubleToLongBits(d2) - 1) : d3;
    }

    public int nextInt() {
        return next(32);
    }

    public int nextInt(int i) {
        if (i > 0) {
            int next = next(31);
            int i2 = i - 1;
            if ((i & i2) == 0) {
                return (int) ((((long) i) * ((long) next)) >> 31);
            }
            while (true) {
                int i3 = next % i;
                if ((next - i3) + i2 >= 0) {
                    return i3;
                }
                next = next(31);
            }
        } else {
            throw new IllegalArgumentException(BadBound);
        }
    }

    public long nextLong() {
        return (((long) next(32)) << 32) + ((long) next(32));
    }

    public boolean nextBoolean() {
        return next(1) != 0;
    }

    public float nextFloat() {
        return ((float) next(24)) / 1.6777216E7f;
    }

    public double nextDouble() {
        return ((double) ((((long) next(26)) << 27) + ((long) next(27)))) * DOUBLE_UNIT;
    }

    public synchronized double nextGaussian() {
        if (this.haveNextNextGaussian) {
            this.haveNextNextGaussian = false;
            return this.nextNextGaussian;
        }
        while (true) {
            double nextDouble = (nextDouble() * 2.0d) - 1.0d;
            double nextDouble2 = (nextDouble() * 2.0d) - 1.0d;
            double d = (nextDouble * nextDouble) + (nextDouble2 * nextDouble2);
            if (d < 1.0d && d != 0.0d) {
                double sqrt = StrictMath.sqrt((StrictMath.log(d) * -2.0d) / d);
                this.nextNextGaussian = nextDouble2 * sqrt;
                this.haveNextNextGaussian = true;
                return nextDouble * sqrt;
            }
        }
    }

    public IntStream ints(long j) {
        if (j >= 0) {
            return StreamSupport.intStream(new RandomIntsSpliterator(this, 0, j, Integer.MAX_VALUE, 0), false);
        }
        throw new IllegalArgumentException(BadSize);
    }

    public IntStream ints() {
        return StreamSupport.intStream(new RandomIntsSpliterator(this, 0, Long.MAX_VALUE, Integer.MAX_VALUE, 0), false);
    }

    public IntStream ints(long j, int i, int i2) {
        if (j < 0) {
            throw new IllegalArgumentException(BadSize);
        } else if (i < i2) {
            return StreamSupport.intStream(new RandomIntsSpliterator(this, 0, j, i, i2), false);
        } else {
            throw new IllegalArgumentException(BadRange);
        }
    }

    public IntStream ints(int i, int i2) {
        if (i < i2) {
            return StreamSupport.intStream(new RandomIntsSpliterator(this, 0, Long.MAX_VALUE, i, i2), false);
        }
        throw new IllegalArgumentException(BadRange);
    }

    public LongStream longs(long j) {
        if (j >= 0) {
            return StreamSupport.longStream(new RandomLongsSpliterator(this, 0, j, Long.MAX_VALUE, 0), false);
        }
        throw new IllegalArgumentException(BadSize);
    }

    public LongStream longs() {
        return StreamSupport.longStream(new RandomLongsSpliterator(this, 0, Long.MAX_VALUE, Long.MAX_VALUE, 0), false);
    }

    public LongStream longs(long j, long j2, long j3) {
        if (j < 0) {
            throw new IllegalArgumentException(BadSize);
        } else if (j2 < j3) {
            return StreamSupport.longStream(new RandomLongsSpliterator(this, 0, j, j2, j3), false);
        } else {
            throw new IllegalArgumentException(BadRange);
        }
    }

    public LongStream longs(long j, long j2) {
        if (j < j2) {
            return StreamSupport.longStream(new RandomLongsSpliterator(this, 0, Long.MAX_VALUE, j, j2), false);
        }
        throw new IllegalArgumentException(BadRange);
    }

    public DoubleStream doubles(long j) {
        if (j >= 0) {
            return StreamSupport.doubleStream(new RandomDoublesSpliterator(this, 0, j, Double.MAX_VALUE, 0.0d), false);
        }
        throw new IllegalArgumentException(BadSize);
    }

    public DoubleStream doubles() {
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(this, 0, Long.MAX_VALUE, Double.MAX_VALUE, 0.0d), false);
    }

    public DoubleStream doubles(long j, double d, double d2) {
        if (j < 0) {
            throw new IllegalArgumentException(BadSize);
        } else if (d < d2) {
            return StreamSupport.doubleStream(new RandomDoublesSpliterator(this, 0, j, d, d2), false);
        } else {
            throw new IllegalArgumentException(BadRange);
        }
    }

    public DoubleStream doubles(double d, double d2) {
        if (d < d2) {
            return StreamSupport.doubleStream(new RandomDoublesSpliterator(this, 0, Long.MAX_VALUE, d, d2), false);
        }
        throw new IllegalArgumentException(BadRange);
    }

    static final class RandomIntsSpliterator implements Spliterator.OfInt {
        final int bound;
        final long fence;
        long index;
        final int origin;
        final Random rng;

        public int characteristics() {
            return 17728;
        }

        RandomIntsSpliterator(Random random, long j, long j2, int i, int i2) {
            this.rng = random;
            this.index = j;
            this.fence = j2;
            this.origin = i;
            this.bound = i2;
        }

        public RandomIntsSpliterator trySplit() {
            long j = this.index;
            long j2 = (this.fence + j) >>> 1;
            if (j2 <= j) {
                return null;
            }
            Random random = this.rng;
            this.index = j2;
            return new RandomIntsSpliterator(random, j, j2, this.origin, this.bound);
        }

        public long estimateSize() {
            return this.fence - this.index;
        }

        public boolean tryAdvance(IntConsumer intConsumer) {
            intConsumer.getClass();
            long j = this.index;
            if (j >= this.fence) {
                return false;
            }
            intConsumer.accept(this.rng.internalNextInt(this.origin, this.bound));
            this.index = j + 1;
            return true;
        }

        public void forEachRemaining(IntConsumer intConsumer) {
            intConsumer.getClass();
            long j = this.index;
            long j2 = this.fence;
            if (j < j2) {
                this.index = j2;
                Random random = this.rng;
                int i = this.origin;
                int i2 = this.bound;
                do {
                    intConsumer.accept(random.internalNextInt(i, i2));
                    j++;
                } while (j < j2);
            }
        }
    }

    static final class RandomLongsSpliterator implements Spliterator.OfLong {
        final long bound;
        final long fence;
        long index;
        final long origin;
        final Random rng;

        public int characteristics() {
            return 17728;
        }

        RandomLongsSpliterator(Random random, long j, long j2, long j3, long j4) {
            this.rng = random;
            this.index = j;
            this.fence = j2;
            this.origin = j3;
            this.bound = j4;
        }

        public RandomLongsSpliterator trySplit() {
            long j = this.index;
            long j2 = (this.fence + j) >>> 1;
            if (j2 <= j) {
                return null;
            }
            Random random = this.rng;
            this.index = j2;
            return new RandomLongsSpliterator(random, j, j2, this.origin, this.bound);
        }

        public long estimateSize() {
            return this.fence - this.index;
        }

        public boolean tryAdvance(LongConsumer longConsumer) {
            longConsumer.getClass();
            long j = this.index;
            if (j >= this.fence) {
                return false;
            }
            longConsumer.accept(this.rng.internalNextLong(this.origin, this.bound));
            this.index = j + 1;
            return true;
        }

        public void forEachRemaining(LongConsumer longConsumer) {
            longConsumer.getClass();
            long j = this.index;
            long j2 = this.fence;
            if (j < j2) {
                this.index = j2;
                Random random = this.rng;
                long j3 = this.origin;
                long j4 = this.bound;
                do {
                    longConsumer.accept(random.internalNextLong(j3, j4));
                    j++;
                } while (j < j2);
            }
        }
    }

    static final class RandomDoublesSpliterator implements Spliterator.OfDouble {
        final double bound;
        final long fence;
        long index;
        final double origin;
        final Random rng;

        public int characteristics() {
            return 17728;
        }

        RandomDoublesSpliterator(Random random, long j, long j2, double d, double d2) {
            this.rng = random;
            this.index = j;
            this.fence = j2;
            this.origin = d;
            this.bound = d2;
        }

        public RandomDoublesSpliterator trySplit() {
            long j = this.index;
            long j2 = (this.fence + j) >>> 1;
            if (j2 <= j) {
                return null;
            }
            Random random = this.rng;
            this.index = j2;
            return new RandomDoublesSpliterator(random, j, j2, this.origin, this.bound);
        }

        public long estimateSize() {
            return this.fence - this.index;
        }

        public boolean tryAdvance(DoubleConsumer doubleConsumer) {
            doubleConsumer.getClass();
            long j = this.index;
            if (j >= this.fence) {
                return false;
            }
            doubleConsumer.accept(this.rng.internalNextDouble(this.origin, this.bound));
            this.index = j + 1;
            return true;
        }

        public void forEachRemaining(DoubleConsumer doubleConsumer) {
            doubleConsumer.getClass();
            long j = this.index;
            long j2 = this.fence;
            if (j < j2) {
                this.index = j2;
                Random random = this.rng;
                double d = this.origin;
                double d2 = this.bound;
                do {
                    doubleConsumer.accept(random.internalNextDouble(d, d2));
                    j++;
                } while (j < j2);
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        long j = readFields.get("seed", -1);
        if (j >= 0) {
            resetSeed(j);
            this.nextNextGaussian = readFields.get("nextNextGaussian", 0.0d);
            this.haveNextNextGaussian = readFields.get("haveNextNextGaussian", false);
            return;
        }
        throw new StreamCorruptedException("Random: invalid seed");
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("seed", this.seed.get());
        putFields.put("nextNextGaussian", this.nextNextGaussian);
        putFields.put("haveNextNextGaussian", this.haveNextNextGaussian);
        objectOutputStream.writeFields();
    }

    private void resetSeed(long j) {
        unsafe.putObjectVolatile(this, seedOffset, new AtomicLong(j));
    }
}
