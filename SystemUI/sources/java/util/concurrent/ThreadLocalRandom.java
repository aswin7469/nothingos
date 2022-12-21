package java.util.concurrent;

import java.p026io.IOException;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.security.AccessControlContext;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;
import jdk.internal.misc.C4581VM;
import jdk.internal.misc.Unsafe;

public class ThreadLocalRandom extends Random {
    static final String BAD_BOUND = "bound must be positive";
    static final String BAD_RANGE = "bound must be greater than origin";
    static final String BAD_SIZE = "size must be non-negative";
    private static final double DOUBLE_UNIT = 1.1102230246251565E-16d;
    private static final float FLOAT_UNIT = 5.9604645E-8f;
    private static final long GAMMA = -7046029254386353131L;
    private static final long INHERITABLETHREADLOCALS;
    private static final long INHERITEDACCESSCONTROLCONTEXT;
    private static final long PROBE;
    private static final int PROBE_INCREMENT = -1640531527;
    private static final long SECONDARY;
    private static final long SEED;
    private static final long SEEDER_INCREMENT = -4942790177534073029L;
    private static final long THREADLOCALS;

    /* renamed from: U */
    private static final Unsafe f761U;
    static final ThreadLocalRandom instance = new ThreadLocalRandom();
    private static final ThreadLocal<Double> nextLocalGaussian = new ThreadLocal<>();
    private static final AtomicInteger probeGenerator = new AtomicInteger();
    private static final AtomicLong seeder = new AtomicLong(mix64(System.currentTimeMillis()) ^ mix64(System.nanoTime()));
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("rnd", Long.TYPE), new ObjectStreamField("initialized", Boolean.TYPE)};
    private static final long serialVersionUID = -5851777807851030925L;
    boolean initialized = true;

    private static int mix32(long j) {
        long j2 = (j ^ (j >>> 33)) * -49064778989728563L;
        return (int) (((j2 ^ (j2 >>> 33)) * -4265267296055464877L) >>> 32);
    }

    private static long mix64(long j) {
        long j2 = (j ^ (j >>> 33)) * -49064778989728563L;
        long j3 = (j2 ^ (j2 >>> 33)) * -4265267296055464877L;
        return j3 ^ (j3 >>> 33);
    }

    private ThreadLocalRandom() {
    }

    static final void localInit() {
        int addAndGet = probeGenerator.addAndGet(PROBE_INCREMENT);
        if (addAndGet == 0) {
            addAndGet = 1;
        }
        long mix64 = mix64(seeder.getAndAdd(SEEDER_INCREMENT));
        Thread currentThread = Thread.currentThread();
        Unsafe unsafe = f761U;
        unsafe.putLong(currentThread, SEED, mix64);
        unsafe.putInt(currentThread, PROBE, addAndGet);
    }

    public static ThreadLocalRandom current() {
        if (f761U.getInt(Thread.currentThread(), PROBE) == 0) {
            localInit();
        }
        return instance;
    }

    public void setSeed(long j) {
        if (this.initialized) {
            throw new UnsupportedOperationException();
        }
    }

    /* access modifiers changed from: package-private */
    public final long nextSeed() {
        Unsafe unsafe = f761U;
        Thread currentThread = Thread.currentThread();
        long j = SEED;
        long j2 = GAMMA + unsafe.getLong(currentThread, j);
        unsafe.putLong(currentThread, j, j2);
        return j2;
    }

    /* access modifiers changed from: protected */
    public int next(int i) {
        return nextInt() >>> (32 - i);
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

    public float nextFloat() {
        return ((float) (mix32(nextSeed()) >>> 8)) * FLOAT_UNIT;
    }

    public double nextGaussian() {
        ThreadLocal<Double> threadLocal = nextLocalGaussian;
        Double d = threadLocal.get();
        if (d != null) {
            threadLocal.set(null);
            return d.doubleValue();
        }
        while (true) {
            double nextDouble = (nextDouble() * 2.0d) - 1.0d;
            double nextDouble2 = (nextDouble() * 2.0d) - 1.0d;
            double d2 = (nextDouble * nextDouble) + (nextDouble2 * nextDouble2);
            if (d2 < 1.0d && d2 != 0.0d) {
                double sqrt = StrictMath.sqrt((StrictMath.log(d2) * -2.0d) / d2);
                nextLocalGaussian.set(Double.valueOf(nextDouble2 * sqrt));
                return nextDouble * sqrt;
            }
        }
    }

    public IntStream ints(long j) {
        if (j >= 0) {
            return StreamSupport.intStream(new RandomIntsSpliterator(0, j, Integer.MAX_VALUE, 0), false);
        }
        throw new IllegalArgumentException(BAD_SIZE);
    }

    public IntStream ints() {
        return StreamSupport.intStream(new RandomIntsSpliterator(0, Long.MAX_VALUE, Integer.MAX_VALUE, 0), false);
    }

    public IntStream ints(long j, int i, int i2) {
        if (j < 0) {
            throw new IllegalArgumentException(BAD_SIZE);
        } else if (i < i2) {
            return StreamSupport.intStream(new RandomIntsSpliterator(0, j, i, i2), false);
        } else {
            throw new IllegalArgumentException(BAD_RANGE);
        }
    }

    public IntStream ints(int i, int i2) {
        if (i < i2) {
            return StreamSupport.intStream(new RandomIntsSpliterator(0, Long.MAX_VALUE, i, i2), false);
        }
        throw new IllegalArgumentException(BAD_RANGE);
    }

    public LongStream longs(long j) {
        if (j >= 0) {
            return StreamSupport.longStream(new RandomLongsSpliterator(0, j, Long.MAX_VALUE, 0), false);
        }
        throw new IllegalArgumentException(BAD_SIZE);
    }

    public LongStream longs() {
        return StreamSupport.longStream(new RandomLongsSpliterator(0, Long.MAX_VALUE, Long.MAX_VALUE, 0), false);
    }

    public LongStream longs(long j, long j2, long j3) {
        if (j < 0) {
            throw new IllegalArgumentException(BAD_SIZE);
        } else if (j2 < j3) {
            return StreamSupport.longStream(new RandomLongsSpliterator(0, j, j2, j3), false);
        } else {
            throw new IllegalArgumentException(BAD_RANGE);
        }
    }

    public LongStream longs(long j, long j2) {
        if (j < j2) {
            return StreamSupport.longStream(new RandomLongsSpliterator(0, Long.MAX_VALUE, j, j2), false);
        }
        throw new IllegalArgumentException(BAD_RANGE);
    }

    public DoubleStream doubles(long j) {
        if (j >= 0) {
            return StreamSupport.doubleStream(new RandomDoublesSpliterator(0, j, Double.MAX_VALUE, 0.0d), false);
        }
        throw new IllegalArgumentException(BAD_SIZE);
    }

    public DoubleStream doubles() {
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(0, Long.MAX_VALUE, Double.MAX_VALUE, 0.0d), false);
    }

    public DoubleStream doubles(long j, double d, double d2) {
        if (j < 0) {
            throw new IllegalArgumentException(BAD_SIZE);
        } else if (d < d2) {
            return StreamSupport.doubleStream(new RandomDoublesSpliterator(0, j, d, d2), false);
        } else {
            throw new IllegalArgumentException(BAD_RANGE);
        }
    }

    public DoubleStream doubles(double d, double d2) {
        if (d < d2) {
            return StreamSupport.doubleStream(new RandomDoublesSpliterator(0, Long.MAX_VALUE, d, d2), false);
        }
        throw new IllegalArgumentException(BAD_RANGE);
    }

    private static final class RandomIntsSpliterator implements Spliterator.OfInt {
        final int bound;
        final long fence;
        long index;
        final int origin;

        public int characteristics() {
            return 17728;
        }

        RandomIntsSpliterator(long j, long j2, int i, int i2) {
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
            this.index = j2;
            return new RandomIntsSpliterator(j, j2, this.origin, this.bound);
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
            intConsumer.accept(ThreadLocalRandom.current().internalNextInt(this.origin, this.bound));
            this.index = j + 1;
            return true;
        }

        public void forEachRemaining(IntConsumer intConsumer) {
            intConsumer.getClass();
            long j = this.index;
            long j2 = this.fence;
            if (j < j2) {
                this.index = j2;
                int i = this.origin;
                int i2 = this.bound;
                ThreadLocalRandom current = ThreadLocalRandom.current();
                do {
                    intConsumer.accept(current.internalNextInt(i, i2));
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

        public int characteristics() {
            return 17728;
        }

        RandomLongsSpliterator(long j, long j2, long j3, long j4) {
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
            this.index = j2;
            return new RandomLongsSpliterator(j, j2, this.origin, this.bound);
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
            longConsumer.accept(ThreadLocalRandom.current().internalNextLong(this.origin, this.bound));
            this.index = j + 1;
            return true;
        }

        public void forEachRemaining(LongConsumer longConsumer) {
            longConsumer.getClass();
            long j = this.index;
            long j2 = this.fence;
            if (j < j2) {
                this.index = j2;
                long j3 = this.origin;
                long j4 = this.bound;
                ThreadLocalRandom current = ThreadLocalRandom.current();
                do {
                    longConsumer.accept(current.internalNextLong(j3, j4));
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

        public int characteristics() {
            return 17728;
        }

        RandomDoublesSpliterator(long j, long j2, double d, double d2) {
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
            this.index = j2;
            return new RandomDoublesSpliterator(j, j2, this.origin, this.bound);
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
            doubleConsumer.accept(ThreadLocalRandom.current().internalNextDouble(this.origin, this.bound));
            this.index = j + 1;
            return true;
        }

        public void forEachRemaining(DoubleConsumer doubleConsumer) {
            doubleConsumer.getClass();
            long j = this.index;
            long j2 = this.fence;
            if (j < j2) {
                this.index = j2;
                double d = this.origin;
                double d2 = this.bound;
                ThreadLocalRandom current = ThreadLocalRandom.current();
                do {
                    doubleConsumer.accept(current.internalNextDouble(d, d2));
                    j++;
                } while (j < j2);
            }
        }
    }

    static final int getProbe() {
        return f761U.getInt(Thread.currentThread(), PROBE);
    }

    static final int advanceProbe(int i) {
        int i2 = i ^ (i << 13);
        int i3 = i2 ^ (i2 >>> 17);
        int i4 = i3 ^ (i3 << 5);
        f761U.putInt(Thread.currentThread(), PROBE, i4);
        return i4;
    }

    static final int nextSecondarySeed() {
        int i;
        Thread currentThread = Thread.currentThread();
        Unsafe unsafe = f761U;
        long j = SECONDARY;
        int i2 = unsafe.getInt(currentThread, j);
        if (i2 != 0) {
            int i3 = i2 ^ (i2 << 13);
            int i4 = i3 ^ (i3 >>> 17);
            i = i4 ^ (i4 << 5);
        } else {
            i = mix32(seeder.getAndAdd(SEEDER_INCREMENT));
            if (i == 0) {
                i = 1;
            }
        }
        unsafe.putInt(currentThread, j, i);
        return i;
    }

    static final void eraseThreadLocals(Thread thread) {
        Unsafe unsafe = f761U;
        unsafe.putObject(thread, THREADLOCALS, (Object) null);
        unsafe.putObject(thread, INHERITABLETHREADLOCALS, (Object) null);
    }

    static final void setInheritedAccessControlContext(Thread thread, AccessControlContext accessControlContext) {
        f761U.putObjectRelease(thread, INHERITEDACCESSCONTROLCONTEXT, accessControlContext);
    }

    static {
        Unsafe unsafe = Unsafe.getUnsafe();
        f761U = unsafe;
        SEED = unsafe.objectFieldOffset(Thread.class, "threadLocalRandomSeed");
        PROBE = unsafe.objectFieldOffset(Thread.class, "threadLocalRandomProbe");
        SECONDARY = unsafe.objectFieldOffset(Thread.class, "threadLocalRandomSecondarySeed");
        THREADLOCALS = unsafe.objectFieldOffset(Thread.class, "threadLocals");
        INHERITABLETHREADLOCALS = unsafe.objectFieldOffset(Thread.class, "inheritableThreadLocals");
        INHERITEDACCESSCONTROLCONTEXT = unsafe.objectFieldOffset(Thread.class, "inheritedAccessControlContext");
        if (Boolean.parseBoolean(C4581VM.getSavedProperty("java.util.secureRandomSeed"))) {
            byte[] seed = SecureRandom.getSeed(8);
            long j = ((long) seed[0]) & 255;
            for (int i = 1; i < 8; i++) {
                j = (j << 8) | (((long) seed[i]) & 255);
            }
            seeder.set(j);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("rnd", f761U.getLong(Thread.currentThread(), SEED));
        putFields.put("initialized", true);
        objectOutputStream.writeFields();
    }

    private Object readResolve() {
        return current();
    }
}
