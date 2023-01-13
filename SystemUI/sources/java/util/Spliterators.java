package java.util;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public final class Spliterators {
    private static final Spliterator.OfDouble EMPTY_DOUBLE_SPLITERATOR = new EmptySpliterator.OfDouble();
    private static final Spliterator.OfInt EMPTY_INT_SPLITERATOR = new EmptySpliterator.OfInt();
    private static final Spliterator.OfLong EMPTY_LONG_SPLITERATOR = new EmptySpliterator.OfLong();
    private static final Spliterator<Object> EMPTY_SPLITERATOR = new EmptySpliterator.OfRef();

    private Spliterators() {
    }

    public static <T> Spliterator<T> emptySpliterator() {
        return EMPTY_SPLITERATOR;
    }

    public static Spliterator.OfInt emptyIntSpliterator() {
        return EMPTY_INT_SPLITERATOR;
    }

    public static Spliterator.OfLong emptyLongSpliterator() {
        return EMPTY_LONG_SPLITERATOR;
    }

    public static Spliterator.OfDouble emptyDoubleSpliterator() {
        return EMPTY_DOUBLE_SPLITERATOR;
    }

    public static <T> Spliterator<T> spliterator(Object[] objArr, int i) {
        return new ArraySpliterator((Object[]) Objects.requireNonNull(objArr), i);
    }

    public static <T> Spliterator<T> spliterator(Object[] objArr, int i, int i2, int i3) {
        checkFromToBounds(((Object[]) Objects.requireNonNull(objArr)).length, i, i2);
        return new ArraySpliterator(objArr, i, i2, i3);
    }

    public static Spliterator.OfInt spliterator(int[] iArr, int i) {
        return new IntArraySpliterator((int[]) Objects.requireNonNull(iArr), i);
    }

    public static Spliterator.OfInt spliterator(int[] iArr, int i, int i2, int i3) {
        checkFromToBounds(((int[]) Objects.requireNonNull(iArr)).length, i, i2);
        return new IntArraySpliterator(iArr, i, i2, i3);
    }

    public static Spliterator.OfLong spliterator(long[] jArr, int i) {
        return new LongArraySpliterator((long[]) Objects.requireNonNull(jArr), i);
    }

    public static Spliterator.OfLong spliterator(long[] jArr, int i, int i2, int i3) {
        checkFromToBounds(((long[]) Objects.requireNonNull(jArr)).length, i, i2);
        return new LongArraySpliterator(jArr, i, i2, i3);
    }

    public static Spliterator.OfDouble spliterator(double[] dArr, int i) {
        return new DoubleArraySpliterator((double[]) Objects.requireNonNull(dArr), i);
    }

    public static Spliterator.OfDouble spliterator(double[] dArr, int i, int i2, int i3) {
        checkFromToBounds(((double[]) Objects.requireNonNull(dArr)).length, i, i2);
        return new DoubleArraySpliterator(dArr, i, i2, i3);
    }

    private static void checkFromToBounds(int i, int i2, int i3) {
        if (i2 > i3) {
            throw new ArrayIndexOutOfBoundsException("origin(" + i2 + ") > fence(" + i3 + NavigationBarInflaterView.KEY_CODE_END);
        } else if (i2 < 0) {
            throw new ArrayIndexOutOfBoundsException(i2);
        } else if (i3 > i) {
            throw new ArrayIndexOutOfBoundsException(i3);
        }
    }

    public static <T> Spliterator<T> spliterator(Collection<? extends T> collection, int i) {
        return new IteratorSpliterator((Collection) Objects.requireNonNull(collection), i);
    }

    public static <T> Spliterator<T> spliterator(Iterator<? extends T> it, long j, int i) {
        return new IteratorSpliterator((Iterator) Objects.requireNonNull(it), j, i);
    }

    public static <T> Spliterator<T> spliteratorUnknownSize(Iterator<? extends T> it, int i) {
        return new IteratorSpliterator((Iterator) Objects.requireNonNull(it), i);
    }

    public static Spliterator.OfInt spliterator(PrimitiveIterator.OfInt ofInt, long j, int i) {
        return new IntIteratorSpliterator((PrimitiveIterator.OfInt) Objects.requireNonNull(ofInt), j, i);
    }

    public static Spliterator.OfInt spliteratorUnknownSize(PrimitiveIterator.OfInt ofInt, int i) {
        return new IntIteratorSpliterator((PrimitiveIterator.OfInt) Objects.requireNonNull(ofInt), i);
    }

    public static Spliterator.OfLong spliterator(PrimitiveIterator.OfLong ofLong, long j, int i) {
        return new LongIteratorSpliterator((PrimitiveIterator.OfLong) Objects.requireNonNull(ofLong), j, i);
    }

    public static Spliterator.OfLong spliteratorUnknownSize(PrimitiveIterator.OfLong ofLong, int i) {
        return new LongIteratorSpliterator((PrimitiveIterator.OfLong) Objects.requireNonNull(ofLong), i);
    }

    public static Spliterator.OfDouble spliterator(PrimitiveIterator.OfDouble ofDouble, long j, int i) {
        return new DoubleIteratorSpliterator((PrimitiveIterator.OfDouble) Objects.requireNonNull(ofDouble), j, i);
    }

    public static Spliterator.OfDouble spliteratorUnknownSize(PrimitiveIterator.OfDouble ofDouble, int i) {
        return new DoubleIteratorSpliterator((PrimitiveIterator.OfDouble) Objects.requireNonNull(ofDouble), i);
    }

    public static <T> Iterator<T> iterator(final Spliterator<? extends T> spliterator) {
        Objects.requireNonNull(spliterator);
        return new Object() {
            T nextElement;
            boolean valueReady = false;

            public void accept(T t) {
                this.valueReady = true;
                this.nextElement = t;
            }

            public boolean hasNext() {
                if (!this.valueReady) {
                    Spliterator.this.tryAdvance(this);
                }
                return this.valueReady;
            }

            public T next() {
                if (this.valueReady || hasNext()) {
                    this.valueReady = false;
                    return this.nextElement;
                }
                throw new NoSuchElementException();
            }
        };
    }

    public static PrimitiveIterator.OfInt iterator(final Spliterator.OfInt ofInt) {
        Objects.requireNonNull(ofInt);
        return new Object() {
            int nextElement;
            boolean valueReady = false;

            public void accept(int i) {
                this.valueReady = true;
                this.nextElement = i;
            }

            public boolean hasNext() {
                if (!this.valueReady) {
                    Spliterator.OfInt.this.tryAdvance((IntConsumer) this);
                }
                return this.valueReady;
            }

            public int nextInt() {
                if (this.valueReady || hasNext()) {
                    this.valueReady = false;
                    return this.nextElement;
                }
                throw new NoSuchElementException();
            }
        };
    }

    public static PrimitiveIterator.OfLong iterator(final Spliterator.OfLong ofLong) {
        Objects.requireNonNull(ofLong);
        return new Object() {
            long nextElement;
            boolean valueReady = false;

            public void accept(long j) {
                this.valueReady = true;
                this.nextElement = j;
            }

            public boolean hasNext() {
                if (!this.valueReady) {
                    Spliterator.OfLong.this.tryAdvance((LongConsumer) this);
                }
                return this.valueReady;
            }

            public long nextLong() {
                if (this.valueReady || hasNext()) {
                    this.valueReady = false;
                    return this.nextElement;
                }
                throw new NoSuchElementException();
            }
        };
    }

    public static PrimitiveIterator.OfDouble iterator(final Spliterator.OfDouble ofDouble) {
        Objects.requireNonNull(ofDouble);
        return new Object() {
            double nextElement;
            boolean valueReady = false;

            public void accept(double d) {
                this.valueReady = true;
                this.nextElement = d;
            }

            public boolean hasNext() {
                if (!this.valueReady) {
                    Spliterator.OfDouble.this.tryAdvance((DoubleConsumer) this);
                }
                return this.valueReady;
            }

            public double nextDouble() {
                if (this.valueReady || hasNext()) {
                    this.valueReady = false;
                    return this.nextElement;
                }
                throw new NoSuchElementException();
            }
        };
    }

    private static abstract class EmptySpliterator<T, S extends Spliterator<T>, C> {
        public int characteristics() {
            return 16448;
        }

        public long estimateSize() {
            return 0;
        }

        public S trySplit() {
            return null;
        }

        EmptySpliterator() {
        }

        public boolean tryAdvance(C c) {
            Objects.requireNonNull(c);
            return false;
        }

        public void forEachRemaining(C c) {
            Objects.requireNonNull(c);
        }

        private static final class OfRef<T> extends EmptySpliterator<T, Spliterator<T>, Consumer<? super T>> implements Spliterator<T> {
            public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
                super.forEachRemaining(consumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(Consumer consumer) {
                return super.tryAdvance(consumer);
            }

            OfRef() {
            }
        }

        private static final class OfInt extends EmptySpliterator<Integer, Spliterator.OfInt, IntConsumer> implements Spliterator.OfInt {
            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining(intConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance(intConsumer);
            }

            OfInt() {
            }
        }

        private static final class OfLong extends EmptySpliterator<Long, Spliterator.OfLong, LongConsumer> implements Spliterator.OfLong {
            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining(longConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance(longConsumer);
            }

            OfLong() {
            }
        }

        private static final class OfDouble extends EmptySpliterator<Double, Spliterator.OfDouble, DoubleConsumer> implements Spliterator.OfDouble {
            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining(doubleConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance(doubleConsumer);
            }

            OfDouble() {
            }
        }
    }

    static final class ArraySpliterator<T> implements Spliterator<T> {
        private final Object[] array;
        private final int characteristics;
        private final int fence;
        private int index;

        public ArraySpliterator(Object[] objArr, int i) {
            this(objArr, 0, objArr.length, i);
        }

        public ArraySpliterator(Object[] objArr, int i, int i2, int i3) {
            this.array = objArr;
            this.index = i;
            this.fence = i2;
            this.characteristics = i3 | 64 | 16384;
        }

        public Spliterator<T> trySplit() {
            int i = this.index;
            int i2 = (this.fence + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            Object[] objArr = this.array;
            this.index = i2;
            return new ArraySpliterator(objArr, i, i2, this.characteristics);
        }

        public void forEachRemaining(Consumer<? super T> consumer) {
            int i;
            consumer.getClass();
            Object[] objArr = this.array;
            int length = objArr.length;
            int i2 = this.fence;
            if (length >= i2 && (i = this.index) >= 0) {
                this.index = i2;
                if (i < i2) {
                    do {
                        consumer.accept(objArr[i]);
                        i++;
                    } while (i < i2);
                }
            }
        }

        public boolean tryAdvance(Consumer<? super T> consumer) {
            consumer.getClass();
            int i = this.index;
            if (i < 0 || i >= this.fence) {
                return false;
            }
            Object[] objArr = this.array;
            this.index = i + 1;
            consumer.accept(objArr[i]);
            return true;
        }

        public long estimateSize() {
            return (long) (this.fence - this.index);
        }

        public int characteristics() {
            return this.characteristics;
        }

        public Comparator<? super T> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    static final class IntArraySpliterator implements Spliterator.OfInt {
        private final int[] array;
        private final int characteristics;
        private final int fence;
        private int index;

        public IntArraySpliterator(int[] iArr, int i) {
            this(iArr, 0, iArr.length, i);
        }

        public IntArraySpliterator(int[] iArr, int i, int i2, int i3) {
            this.array = iArr;
            this.index = i;
            this.fence = i2;
            this.characteristics = i3 | 64 | 16384;
        }

        public Spliterator.OfInt trySplit() {
            int i = this.index;
            int i2 = (this.fence + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            int[] iArr = this.array;
            this.index = i2;
            return new IntArraySpliterator(iArr, i, i2, this.characteristics);
        }

        public void forEachRemaining(IntConsumer intConsumer) {
            int i;
            intConsumer.getClass();
            int[] iArr = this.array;
            int length = iArr.length;
            int i2 = this.fence;
            if (length >= i2 && (i = this.index) >= 0) {
                this.index = i2;
                if (i < i2) {
                    do {
                        intConsumer.accept(iArr[i]);
                        i++;
                    } while (i < i2);
                }
            }
        }

        public boolean tryAdvance(IntConsumer intConsumer) {
            intConsumer.getClass();
            int i = this.index;
            if (i < 0 || i >= this.fence) {
                return false;
            }
            int[] iArr = this.array;
            this.index = i + 1;
            intConsumer.accept(iArr[i]);
            return true;
        }

        public long estimateSize() {
            return (long) (this.fence - this.index);
        }

        public int characteristics() {
            return this.characteristics;
        }

        public Comparator<? super Integer> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    static final class LongArraySpliterator implements Spliterator.OfLong {
        private final long[] array;
        private final int characteristics;
        private final int fence;
        private int index;

        public LongArraySpliterator(long[] jArr, int i) {
            this(jArr, 0, jArr.length, i);
        }

        public LongArraySpliterator(long[] jArr, int i, int i2, int i3) {
            this.array = jArr;
            this.index = i;
            this.fence = i2;
            this.characteristics = i3 | 64 | 16384;
        }

        public Spliterator.OfLong trySplit() {
            int i = this.index;
            int i2 = (this.fence + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            long[] jArr = this.array;
            this.index = i2;
            return new LongArraySpliterator(jArr, i, i2, this.characteristics);
        }

        public void forEachRemaining(LongConsumer longConsumer) {
            int i;
            longConsumer.getClass();
            long[] jArr = this.array;
            int length = jArr.length;
            int i2 = this.fence;
            if (length >= i2 && (i = this.index) >= 0) {
                this.index = i2;
                if (i < i2) {
                    do {
                        longConsumer.accept(jArr[i]);
                        i++;
                    } while (i < i2);
                }
            }
        }

        public boolean tryAdvance(LongConsumer longConsumer) {
            longConsumer.getClass();
            int i = this.index;
            if (i < 0 || i >= this.fence) {
                return false;
            }
            long[] jArr = this.array;
            this.index = i + 1;
            longConsumer.accept(jArr[i]);
            return true;
        }

        public long estimateSize() {
            return (long) (this.fence - this.index);
        }

        public int characteristics() {
            return this.characteristics;
        }

        public Comparator<? super Long> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    static final class DoubleArraySpliterator implements Spliterator.OfDouble {
        private final double[] array;
        private final int characteristics;
        private final int fence;
        private int index;

        public DoubleArraySpliterator(double[] dArr, int i) {
            this(dArr, 0, dArr.length, i);
        }

        public DoubleArraySpliterator(double[] dArr, int i, int i2, int i3) {
            this.array = dArr;
            this.index = i;
            this.fence = i2;
            this.characteristics = i3 | 64 | 16384;
        }

        public Spliterator.OfDouble trySplit() {
            int i = this.index;
            int i2 = (this.fence + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            double[] dArr = this.array;
            this.index = i2;
            return new DoubleArraySpliterator(dArr, i, i2, this.characteristics);
        }

        public void forEachRemaining(DoubleConsumer doubleConsumer) {
            int i;
            doubleConsumer.getClass();
            double[] dArr = this.array;
            int length = dArr.length;
            int i2 = this.fence;
            if (length >= i2 && (i = this.index) >= 0) {
                this.index = i2;
                if (i < i2) {
                    do {
                        doubleConsumer.accept(dArr[i]);
                        i++;
                    } while (i < i2);
                }
            }
        }

        public boolean tryAdvance(DoubleConsumer doubleConsumer) {
            doubleConsumer.getClass();
            int i = this.index;
            if (i < 0 || i >= this.fence) {
                return false;
            }
            double[] dArr = this.array;
            this.index = i + 1;
            doubleConsumer.accept(dArr[i]);
            return true;
        }

        public long estimateSize() {
            return (long) (this.fence - this.index);
        }

        public int characteristics() {
            return this.characteristics;
        }

        public Comparator<? super Double> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    public static abstract class AbstractSpliterator<T> implements Spliterator<T> {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private int batch;
        private final int characteristics;
        private long est;

        protected AbstractSpliterator(long j, int i) {
            this.est = j;
            this.characteristics = (i & 64) != 0 ? i | 16384 : i;
        }

        static final class HoldingConsumer<T> implements Consumer<T> {
            Object value;

            HoldingConsumer() {
            }

            public void accept(T t) {
                this.value = t;
            }
        }

        public Spliterator<T> trySplit() {
            HoldingConsumer holdingConsumer = new HoldingConsumer();
            long j = this.est;
            if (j <= 1 || !tryAdvance(holdingConsumer)) {
                return null;
            }
            int i = this.batch + 1024;
            if (((long) i) > j) {
                i = (int) j;
            }
            if (i > 33554432) {
                i = 33554432;
            }
            Object[] objArr = new Object[i];
            int i2 = 0;
            do {
                objArr[i2] = holdingConsumer.value;
                i2++;
                if (i2 >= i || !tryAdvance(holdingConsumer)) {
                    this.batch = i2;
                    long j2 = this.est;
                }
                objArr[i2] = holdingConsumer.value;
                i2++;
                break;
            } while (!tryAdvance(holdingConsumer));
            this.batch = i2;
            long j22 = this.est;
            if (j22 != Long.MAX_VALUE) {
                this.est = j22 - ((long) i2);
            }
            return new ArraySpliterator(objArr, 0, i2, characteristics());
        }

        public long estimateSize() {
            return this.est;
        }

        public int characteristics() {
            return this.characteristics;
        }
    }

    public static abstract class AbstractIntSpliterator implements Spliterator.OfInt {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private int batch;
        private final int characteristics;
        private long est;

        protected AbstractIntSpliterator(long j, int i) {
            this.est = j;
            this.characteristics = (i & 64) != 0 ? i | 16384 : i;
        }

        static final class HoldingIntConsumer implements IntConsumer {
            int value;

            HoldingIntConsumer() {
            }

            public void accept(int i) {
                this.value = i;
            }
        }

        public Spliterator.OfInt trySplit() {
            HoldingIntConsumer holdingIntConsumer = new HoldingIntConsumer();
            long j = this.est;
            if (j <= 1 || !tryAdvance((IntConsumer) holdingIntConsumer)) {
                return null;
            }
            int i = this.batch + 1024;
            if (((long) i) > j) {
                i = (int) j;
            }
            if (i > 33554432) {
                i = 33554432;
            }
            int[] iArr = new int[i];
            int i2 = 0;
            do {
                iArr[i2] = holdingIntConsumer.value;
                i2++;
                if (i2 >= i || !tryAdvance((IntConsumer) holdingIntConsumer)) {
                    this.batch = i2;
                    long j2 = this.est;
                }
                iArr[i2] = holdingIntConsumer.value;
                i2++;
                break;
            } while (!tryAdvance((IntConsumer) holdingIntConsumer));
            this.batch = i2;
            long j22 = this.est;
            if (j22 != Long.MAX_VALUE) {
                this.est = j22 - ((long) i2);
            }
            return new IntArraySpliterator(iArr, 0, i2, characteristics());
        }

        public long estimateSize() {
            return this.est;
        }

        public int characteristics() {
            return this.characteristics;
        }
    }

    public static abstract class AbstractLongSpliterator implements Spliterator.OfLong {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private int batch;
        private final int characteristics;
        private long est;

        protected AbstractLongSpliterator(long j, int i) {
            this.est = j;
            this.characteristics = (i & 64) != 0 ? i | 16384 : i;
        }

        static final class HoldingLongConsumer implements LongConsumer {
            long value;

            HoldingLongConsumer() {
            }

            public void accept(long j) {
                this.value = j;
            }
        }

        public Spliterator.OfLong trySplit() {
            HoldingLongConsumer holdingLongConsumer = new HoldingLongConsumer();
            long j = this.est;
            if (j <= 1 || !tryAdvance((LongConsumer) holdingLongConsumer)) {
                return null;
            }
            int i = this.batch + 1024;
            if (((long) i) > j) {
                i = (int) j;
            }
            if (i > 33554432) {
                i = 33554432;
            }
            long[] jArr = new long[i];
            int i2 = 0;
            do {
                jArr[i2] = holdingLongConsumer.value;
                i2++;
                if (i2 >= i || !tryAdvance((LongConsumer) holdingLongConsumer)) {
                    this.batch = i2;
                    long j2 = this.est;
                }
                jArr[i2] = holdingLongConsumer.value;
                i2++;
                break;
            } while (!tryAdvance((LongConsumer) holdingLongConsumer));
            this.batch = i2;
            long j22 = this.est;
            if (j22 != Long.MAX_VALUE) {
                this.est = j22 - ((long) i2);
            }
            return new LongArraySpliterator(jArr, 0, i2, characteristics());
        }

        public long estimateSize() {
            return this.est;
        }

        public int characteristics() {
            return this.characteristics;
        }
    }

    public static abstract class AbstractDoubleSpliterator implements Spliterator.OfDouble {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private int batch;
        private final int characteristics;
        private long est;

        protected AbstractDoubleSpliterator(long j, int i) {
            this.est = j;
            this.characteristics = (i & 64) != 0 ? i | 16384 : i;
        }

        static final class HoldingDoubleConsumer implements DoubleConsumer {
            double value;

            HoldingDoubleConsumer() {
            }

            public void accept(double d) {
                this.value = d;
            }
        }

        public Spliterator.OfDouble trySplit() {
            HoldingDoubleConsumer holdingDoubleConsumer = new HoldingDoubleConsumer();
            long j = this.est;
            if (j <= 1 || !tryAdvance((DoubleConsumer) holdingDoubleConsumer)) {
                return null;
            }
            int i = this.batch + 1024;
            if (((long) i) > j) {
                i = (int) j;
            }
            if (i > 33554432) {
                i = 33554432;
            }
            double[] dArr = new double[i];
            int i2 = 0;
            do {
                dArr[i2] = holdingDoubleConsumer.value;
                i2++;
                if (i2 >= i || !tryAdvance((DoubleConsumer) holdingDoubleConsumer)) {
                    this.batch = i2;
                    long j2 = this.est;
                }
                dArr[i2] = holdingDoubleConsumer.value;
                i2++;
                break;
            } while (!tryAdvance((DoubleConsumer) holdingDoubleConsumer));
            this.batch = i2;
            long j22 = this.est;
            if (j22 != Long.MAX_VALUE) {
                this.est = j22 - ((long) i2);
            }
            return new DoubleArraySpliterator(dArr, 0, i2, characteristics());
        }

        public long estimateSize() {
            return this.est;
        }

        public int characteristics() {
            return this.characteristics;
        }
    }

    static class IteratorSpliterator<T> implements Spliterator<T> {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private int batch;
        private final int characteristics;
        private final Collection<? extends T> collection;
        private long est;

        /* renamed from: it */
        private Iterator<? extends T> f704it;

        public IteratorSpliterator(Collection<? extends T> collection2, int i) {
            this.collection = collection2;
            this.f704it = null;
            this.characteristics = (i & 4096) == 0 ? i | 64 | 16384 : i;
        }

        public IteratorSpliterator(Iterator<? extends T> it, long j, int i) {
            this.collection = null;
            this.f704it = it;
            this.est = j;
            this.characteristics = (i & 4096) == 0 ? i | 64 | 16384 : i;
        }

        public IteratorSpliterator(Iterator<? extends T> it, int i) {
            this.collection = null;
            this.f704it = it;
            this.est = Long.MAX_VALUE;
            this.characteristics = i & -16449;
        }

        public Spliterator<T> trySplit() {
            long j;
            Iterator<? extends T> it = this.f704it;
            if (it == null) {
                it = this.collection.iterator();
                this.f704it = it;
                j = (long) this.collection.size();
                this.est = j;
            } else {
                j = this.est;
            }
            if (j <= 1 || !it.hasNext()) {
                return null;
            }
            int i = this.batch + 1024;
            if (((long) i) > j) {
                i = (int) j;
            }
            if (i > 33554432) {
                i = 33554432;
            }
            Object[] objArr = new Object[i];
            int i2 = 0;
            do {
                objArr[i2] = it.next();
                i2++;
                if (i2 >= i || !it.hasNext()) {
                    this.batch = i2;
                    long j2 = this.est;
                }
                objArr[i2] = it.next();
                i2++;
                break;
            } while (!it.hasNext());
            this.batch = i2;
            long j22 = this.est;
            if (j22 != Long.MAX_VALUE) {
                this.est = j22 - ((long) i2);
            }
            return new ArraySpliterator(objArr, 0, i2, this.characteristics);
        }

        public void forEachRemaining(Consumer<? super T> consumer) {
            consumer.getClass();
            Iterator<? extends T> it = this.f704it;
            if (it == null) {
                it = this.collection.iterator();
                this.f704it = it;
                this.est = (long) this.collection.size();
            }
            it.forEachRemaining(consumer);
        }

        public boolean tryAdvance(Consumer<? super T> consumer) {
            consumer.getClass();
            if (this.f704it == null) {
                this.f704it = this.collection.iterator();
                this.est = (long) this.collection.size();
            }
            if (!this.f704it.hasNext()) {
                return false;
            }
            consumer.accept(this.f704it.next());
            return true;
        }

        public long estimateSize() {
            if (this.f704it != null) {
                return this.est;
            }
            this.f704it = this.collection.iterator();
            long size = (long) this.collection.size();
            this.est = size;
            return size;
        }

        public int characteristics() {
            return this.characteristics;
        }

        public Comparator<? super T> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    static final class IntIteratorSpliterator implements Spliterator.OfInt {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private int batch;
        private final int characteristics;
        private long est;

        /* renamed from: it */
        private PrimitiveIterator.OfInt f703it;

        public IntIteratorSpliterator(PrimitiveIterator.OfInt ofInt, long j, int i) {
            this.f703it = ofInt;
            this.est = j;
            this.characteristics = (i & 4096) == 0 ? i | 64 | 16384 : i;
        }

        public IntIteratorSpliterator(PrimitiveIterator.OfInt ofInt, int i) {
            this.f703it = ofInt;
            this.est = Long.MAX_VALUE;
            this.characteristics = i & -16449;
        }

        public Spliterator.OfInt trySplit() {
            PrimitiveIterator.OfInt ofInt = this.f703it;
            long j = this.est;
            if (j <= 1 || !ofInt.hasNext()) {
                return null;
            }
            int i = this.batch + 1024;
            if (((long) i) > j) {
                i = (int) j;
            }
            if (i > 33554432) {
                i = 33554432;
            }
            int[] iArr = new int[i];
            int i2 = 0;
            do {
                iArr[i2] = ofInt.nextInt();
                i2++;
                if (i2 >= i || !ofInt.hasNext()) {
                    this.batch = i2;
                    long j2 = this.est;
                }
                iArr[i2] = ofInt.nextInt();
                i2++;
                break;
            } while (!ofInt.hasNext());
            this.batch = i2;
            long j22 = this.est;
            if (j22 != Long.MAX_VALUE) {
                this.est = j22 - ((long) i2);
            }
            return new IntArraySpliterator(iArr, 0, i2, this.characteristics);
        }

        public void forEachRemaining(IntConsumer intConsumer) {
            intConsumer.getClass();
            this.f703it.forEachRemaining(intConsumer);
        }

        public boolean tryAdvance(IntConsumer intConsumer) {
            intConsumer.getClass();
            if (!this.f703it.hasNext()) {
                return false;
            }
            intConsumer.accept(this.f703it.nextInt());
            return true;
        }

        public long estimateSize() {
            return this.est;
        }

        public int characteristics() {
            return this.characteristics;
        }

        public Comparator<? super Integer> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    static final class LongIteratorSpliterator implements Spliterator.OfLong {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private int batch;
        private final int characteristics;
        private long est;

        /* renamed from: it */
        private PrimitiveIterator.OfLong f705it;

        public LongIteratorSpliterator(PrimitiveIterator.OfLong ofLong, long j, int i) {
            this.f705it = ofLong;
            this.est = j;
            this.characteristics = (i & 4096) == 0 ? i | 64 | 16384 : i;
        }

        public LongIteratorSpliterator(PrimitiveIterator.OfLong ofLong, int i) {
            this.f705it = ofLong;
            this.est = Long.MAX_VALUE;
            this.characteristics = i & -16449;
        }

        public Spliterator.OfLong trySplit() {
            PrimitiveIterator.OfLong ofLong = this.f705it;
            long j = this.est;
            if (j <= 1 || !ofLong.hasNext()) {
                return null;
            }
            int i = this.batch + 1024;
            if (((long) i) > j) {
                i = (int) j;
            }
            if (i > 33554432) {
                i = 33554432;
            }
            long[] jArr = new long[i];
            int i2 = 0;
            do {
                jArr[i2] = ofLong.nextLong();
                i2++;
                if (i2 >= i || !ofLong.hasNext()) {
                    this.batch = i2;
                    long j2 = this.est;
                }
                jArr[i2] = ofLong.nextLong();
                i2++;
                break;
            } while (!ofLong.hasNext());
            this.batch = i2;
            long j22 = this.est;
            if (j22 != Long.MAX_VALUE) {
                this.est = j22 - ((long) i2);
            }
            return new LongArraySpliterator(jArr, 0, i2, this.characteristics);
        }

        public void forEachRemaining(LongConsumer longConsumer) {
            longConsumer.getClass();
            this.f705it.forEachRemaining(longConsumer);
        }

        public boolean tryAdvance(LongConsumer longConsumer) {
            longConsumer.getClass();
            if (!this.f705it.hasNext()) {
                return false;
            }
            longConsumer.accept(this.f705it.nextLong());
            return true;
        }

        public long estimateSize() {
            return this.est;
        }

        public int characteristics() {
            return this.characteristics;
        }

        public Comparator<? super Long> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    static final class DoubleIteratorSpliterator implements Spliterator.OfDouble {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private int batch;
        private final int characteristics;
        private long est;

        /* renamed from: it */
        private PrimitiveIterator.OfDouble f702it;

        public DoubleIteratorSpliterator(PrimitiveIterator.OfDouble ofDouble, long j, int i) {
            this.f702it = ofDouble;
            this.est = j;
            this.characteristics = (i & 4096) == 0 ? i | 64 | 16384 : i;
        }

        public DoubleIteratorSpliterator(PrimitiveIterator.OfDouble ofDouble, int i) {
            this.f702it = ofDouble;
            this.est = Long.MAX_VALUE;
            this.characteristics = i & -16449;
        }

        public Spliterator.OfDouble trySplit() {
            PrimitiveIterator.OfDouble ofDouble = this.f702it;
            long j = this.est;
            if (j <= 1 || !ofDouble.hasNext()) {
                return null;
            }
            int i = this.batch + 1024;
            if (((long) i) > j) {
                i = (int) j;
            }
            if (i > 33554432) {
                i = 33554432;
            }
            double[] dArr = new double[i];
            int i2 = 0;
            do {
                dArr[i2] = ofDouble.nextDouble();
                i2++;
                if (i2 >= i || !ofDouble.hasNext()) {
                    this.batch = i2;
                    long j2 = this.est;
                }
                dArr[i2] = ofDouble.nextDouble();
                i2++;
                break;
            } while (!ofDouble.hasNext());
            this.batch = i2;
            long j22 = this.est;
            if (j22 != Long.MAX_VALUE) {
                this.est = j22 - ((long) i2);
            }
            return new DoubleArraySpliterator(dArr, 0, i2, this.characteristics);
        }

        public void forEachRemaining(DoubleConsumer doubleConsumer) {
            doubleConsumer.getClass();
            this.f702it.forEachRemaining(doubleConsumer);
        }

        public boolean tryAdvance(DoubleConsumer doubleConsumer) {
            doubleConsumer.getClass();
            if (!this.f702it.hasNext()) {
                return false;
            }
            doubleConsumer.accept(this.f702it.nextDouble());
            return true;
        }

        public long estimateSize() {
            return this.est;
        }

        public int characteristics() {
            return this.characteristics;
        }

        public Comparator<? super Double> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }
}
