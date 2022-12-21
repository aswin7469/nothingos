package java.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

public final class SplittableRandom {
    static final String BAD_BOUND = "bound must be positive";
    static final String BAD_RANGE = "bound must be greater than origin";
    static final String BAD_SIZE = "size must be non-negative";
    private static final double DOUBLE_UNIT = 1.1102230246251565E-16d;
    private static final long GOLDEN_GAMMA = -7046029254386353131L;
    private static final AtomicLong defaultGen = new AtomicLong(mix64(System.currentTimeMillis()) ^ mix64(System.nanoTime()));
    private final long gamma;
    private long seed;

    private static int mix32(long j) {
        long j2 = (j ^ (j >>> 33)) * 7109453100751455733L;
        return (int) (((j2 ^ (j2 >>> 28)) * -3808689974395783757L) >>> 32);
    }

    private static long mix64(long j) {
        long j2 = (j ^ (j >>> 30)) * -4658895280553007687L;
        long j3 = (j2 ^ (j2 >>> 27)) * -7723592293110705685L;
        return j3 ^ (j3 >>> 31);
    }

    private SplittableRandom(long j, long j2) {
        this.seed = j;
        this.gamma = j2;
    }

    private static long mixGamma(long j) {
        long j2 = (j ^ (j >>> 33)) * -49064778989728563L;
        long j3 = (j2 ^ (j2 >>> 33)) * -4265267296055464877L;
        long j4 = (j3 ^ (j3 >>> 33)) | 1;
        return Long.bitCount((j4 >>> 1) ^ j4) < 24 ? j4 ^ -6148914691236517206L : j4;
    }

    private long nextSeed() {
        long j = this.seed + this.gamma;
        this.seed = j;
        return j;
    }

    static {
        if (((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            public Boolean run() {
                return Boolean.valueOf(Boolean.getBoolean("java.util.secureRandomSeed"));
            }
        })).booleanValue()) {
            byte[] seed2 = SecureRandom.getSeed(8);
            long j = ((long) seed2[0]) & 255;
            for (int i = 1; i < 8; i++) {
                j = (j << 8) | (((long) seed2[i]) & 255);
            }
            defaultGen.set(j);
        }
    }

    /* access modifiers changed from: package-private */
    public final long internalNextLong(long j, long j2) {
        long mix64 = mix64(nextSeed());
        if (j >= j2) {
            return mix64;
        }
        long j3 = j2 - j;
        long j4 = j3 - 1;
        if ((j3 & j4) == 0) {
            return (mix64 & j4) + j;
        }
        if (j3 > 0) {
            while (true) {
                long j5 = mix64 >>> 1;
                long j6 = j5 + j4;
                long j7 = j5 % j3;
                if (j6 - j7 >= 0) {
                    return j7 + j;
                }
                mix64 = mix64(nextSeed());
            }
        } else {
            while (true) {
                if (mix64 >= j && mix64 < j2) {
                    return mix64;
                }
                mix64 = mix64(nextSeed());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final int internalNextInt(int i, int i2) {
        int mix32 = mix32(nextSeed());
        if (i >= i2) {
            return mix32;
        }
        int i3 = i2 - i;
        int i4 = i3 - 1;
        if ((i3 & i4) == 0) {
            return (mix32 & i4) + i;
        }
        if (i3 > 0) {
            int i5 = mix32 >>> 1;
            while (true) {
                int i6 = i5 + i4;
                int i7 = i5 % i3;
                if (i6 - i7 >= 0) {
                    return i7 + i;
                }
                i5 = mix32(nextSeed()) >>> 1;
            }
        } else {
            while (true) {
                if (mix32 >= i && mix32 < i2) {
                    return mix32;
                }
                mix32 = mix32(nextSeed());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final double internalNextDouble(double d, double d2) {
        double nextLong = ((double) (nextLong() >>> 11)) * DOUBLE_UNIT;
        if (d >= d2) {
            return nextLong;
        }
        double d3 = (nextLong * (d2 - d)) + d;
        return d3 >= d2 ? Double.longBitsToDouble(Double.doubleToLongBits(d2) - 1) : d3;
    }

    public SplittableRandom(long j) {
        this(j, GOLDEN_GAMMA);
    }

    public SplittableRandom() {
        long andAdd = defaultGen.getAndAdd(4354685564936845354L);
        this.seed = mix64(andAdd);
        this.gamma = mixGamma(andAdd + GOLDEN_GAMMA);
    }

    public SplittableRandom split() {
        return new SplittableRandom(nextLong(), mixGamma(nextSeed()));
    }

    public void nextBytes(byte[] bArr) {
        int length = bArr.length;
        int i = length >> 3;
        int i2 = 0;
        while (true) {
            int i3 = i - 1;
            if (i <= 0) {
                break;
            }
            long nextLong = nextLong();
            int i4 = 8;
            while (true) {
                int i5 = i4 - 1;
                if (i4 <= 0) {
                    break;
                }
                bArr[i2] = (byte) ((int) nextLong);
                nextLong >>>= 8;
                i2++;
                i4 = i5;
            }
            i = i3;
        }
        if (i2 < length) {
            long nextLong2 = nextLong();
            while (i2 < length) {
                bArr[i2] = (byte) ((int) nextLong2);
                nextLong2 >>>= 8;
                i2++;
            }
        }
    }

    public int nextInt() {
        return mix32(nextSeed());
    }

    public int nextInt(int i) {
        if (i > 0) {
            int mix32 = mix32(nextSeed());
            int i2 = i - 1;
            if ((i & i2) == 0) {
                return mix32 & i2;
            }
            while (true) {
                int i3 = mix32 >>> 1;
                int i4 = i3 + i2;
                int i5 = i3 % i;
                if (i4 - i5 >= 0) {
                    return i5;
                }
                mix32 = mix32(nextSeed());
            }
        } else {
            throw new IllegalArgumentException(BAD_BOUND);
        }
    }

    public int nextInt(int i, int i2) {
        if (i < i2) {
            return internalNextInt(i, i2);
        }
        throw new IllegalArgumentException(BAD_RANGE);
    }

    public long nextLong() {
        return mix64(nextSeed());
    }

    public long nextLong(long j) {
        if (j > 0) {
            long mix64 = mix64(nextSeed());
            long j2 = j - 1;
            if ((j & j2) == 0) {
                return mix64 & j2;
            }
            while (true) {
                long j3 = mix64 >>> 1;
                long j4 = j3 + j2;
                long j5 = j3 % j;
                if (j4 - j5 >= 0) {
                    return j5;
                }
                mix64 = mix64(nextSeed());
            }
        } else {
            throw new IllegalArgumentException(BAD_BOUND);
        }
    }

    public long nextLong(long j, long j2) {
        if (j < j2) {
            return internalNextLong(j, j2);
        }
        throw new IllegalArgumentException(BAD_RANGE);
    }

    public double nextDouble() {
        return ((double) (mix64(nextSeed()) >>> 11)) * DOUBLE_UNIT;
    }

    public double nextDouble(double d) {
        if (d > 0.0d) {
            double mix64 = ((double) (mix64(nextSeed()) >>> 11)) * DOUBLE_UNIT * d;
            return mix64 < d ? mix64 : Double.longBitsToDouble(Double.doubleToLongBits(d) - 1);
        }
        throw new IllegalArgumentException(BAD_BOUND);
    }

    public double nextDouble(double d, double d2) {
        if (d < d2) {
            return internalNextDouble(d, d2);
        }
        throw new IllegalArgumentException(BAD_RANGE);
    }

    public boolean nextBoolean() {
        return mix32(nextSeed()) < 0;
    }

    public IntStream ints(long j) {
        if (j >= 0) {
            return StreamSupport.intStream(new RandomIntsSpliterator(this, 0, j, Integer.MAX_VALUE, 0), false);
        }
        throw new IllegalArgumentException(BAD_SIZE);
    }

    public IntStream ints() {
        return StreamSupport.intStream(new RandomIntsSpliterator(this, 0, Long.MAX_VALUE, Integer.MAX_VALUE, 0), false);
    }

    public IntStream ints(long j, int i, int i2) {
        if (j < 0) {
            throw new IllegalArgumentException(BAD_SIZE);
        } else if (i < i2) {
            return StreamSupport.intStream(new RandomIntsSpliterator(this, 0, j, i, i2), false);
        } else {
            throw new IllegalArgumentException(BAD_RANGE);
        }
    }

    public IntStream ints(int i, int i2) {
        if (i < i2) {
            return StreamSupport.intStream(new RandomIntsSpliterator(this, 0, Long.MAX_VALUE, i, i2), false);
        }
        throw new IllegalArgumentException(BAD_RANGE);
    }

    public LongStream longs(long j) {
        if (j >= 0) {
            return StreamSupport.longStream(new RandomLongsSpliterator(this, 0, j, Long.MAX_VALUE, 0), false);
        }
        throw new IllegalArgumentException(BAD_SIZE);
    }

    public LongStream longs() {
        return StreamSupport.longStream(new RandomLongsSpliterator(this, 0, Long.MAX_VALUE, Long.MAX_VALUE, 0), false);
    }

    public LongStream longs(long j, long j2, long j3) {
        if (j < 0) {
            throw new IllegalArgumentException(BAD_SIZE);
        } else if (j2 < j3) {
            return StreamSupport.longStream(new RandomLongsSpliterator(this, 0, j, j2, j3), false);
        } else {
            throw new IllegalArgumentException(BAD_RANGE);
        }
    }

    public LongStream longs(long j, long j2) {
        if (j < j2) {
            return StreamSupport.longStream(new RandomLongsSpliterator(this, 0, Long.MAX_VALUE, j, j2), false);
        }
        throw new IllegalArgumentException(BAD_RANGE);
    }

    public DoubleStream doubles(long j) {
        if (j >= 0) {
            return StreamSupport.doubleStream(new RandomDoublesSpliterator(this, 0, j, Double.MAX_VALUE, 0.0d), false);
        }
        throw new IllegalArgumentException(BAD_SIZE);
    }

    public DoubleStream doubles() {
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(this, 0, Long.MAX_VALUE, Double.MAX_VALUE, 0.0d), false);
    }

    public DoubleStream doubles(long j, double d, double d2) {
        if (j < 0) {
            throw new IllegalArgumentException(BAD_SIZE);
        } else if (d < d2) {
            return StreamSupport.doubleStream(new RandomDoublesSpliterator(this, 0, j, d, d2), false);
        } else {
            throw new IllegalArgumentException(BAD_RANGE);
        }
    }

    public DoubleStream doubles(double d, double d2) {
        if (d < d2) {
            return StreamSupport.doubleStream(new RandomDoublesSpliterator(this, 0, Long.MAX_VALUE, d, d2), false);
        }
        throw new IllegalArgumentException(BAD_RANGE);
    }

    private static final class RandomIntsSpliterator implements Spliterator.OfInt {
        final int bound;
        final long fence;
        long index;
        final int origin;
        final SplittableRandom rng;

        public int characteristics() {
            return 17728;
        }

        RandomIntsSpliterator(SplittableRandom splittableRandom, long j, long j2, int i, int i2) {
            this.rng = splittableRandom;
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
            SplittableRandom split = this.rng.split();
            this.index = j2;
            return new RandomIntsSpliterator(split, j, j2, this.origin, this.bound);
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
                SplittableRandom splittableRandom = this.rng;
                int i = this.origin;
                int i2 = this.bound;
                do {
                    intConsumer.accept(splittableRandom.internalNextInt(i, i2));
                    j++;
                } while (j < j2);
            }
        }
    }

    private static final class RandomLongsSpliterator implements Spliterator.OfLong {
        final long bound;
        final long fence;
        long index;
        final long origin;
        final SplittableRandom rng;

        public int characteristics() {
            return 17728;
        }

        RandomLongsSpliterator(SplittableRandom splittableRandom, long j, long j2, long j3, long j4) {
            this.rng = splittableRandom;
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
            SplittableRandom split = this.rng.split();
            this.index = j2;
            return new RandomLongsSpliterator(split, j, j2, this.origin, this.bound);
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
                SplittableRandom splittableRandom = this.rng;
                long j3 = this.origin;
                long j4 = this.bound;
                do {
                    longConsumer.accept(splittableRandom.internalNextLong(j3, j4));
                    j++;
                } while (j < j2);
            }
        }
    }

    private static final class RandomDoublesSpliterator implements Spliterator.OfDouble {
        final double bound;
        final long fence;
        long index;
        final double origin;
        final SplittableRandom rng;

        public int characteristics() {
            return 17728;
        }

        RandomDoublesSpliterator(SplittableRandom splittableRandom, long j, long j2, double d, double d2) {
            this.rng = splittableRandom;
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
            SplittableRandom split = this.rng.split();
            this.index = j2;
            return new RandomDoublesSpliterator(split, j, j2, this.origin, this.bound);
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
                SplittableRandom splittableRandom = this.rng;
                double d = this.origin;
                double d2 = this.bound;
                do {
                    doubleConsumer.accept(splittableRandom.internalNextDouble(d, d2));
                    j++;
                } while (j < j2);
            }
        }
    }
}
