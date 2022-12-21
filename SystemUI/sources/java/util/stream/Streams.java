package java.util.stream;

import android.net.wifi.WifiManager;
import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.SpinedBuffer;
import java.util.stream.Stream;

final class Streams {
    static final Object NONE = new Object();

    private Streams() {
        throw new Error("no instances");
    }

    static final class RangeIntSpliterator implements Spliterator.OfInt {
        private static final int BALANCED_SPLIT_THRESHOLD = 16777216;
        private static final int RIGHT_BALANCED_SPLIT_RATIO = 8;
        private int from;
        private int last;
        private final int upTo;

        public int characteristics() {
            return 17749;
        }

        public Comparator<? super Integer> getComparator() {
            return null;
        }

        RangeIntSpliterator(int i, int i2, boolean z) {
            this(i, i2, z ? 1 : 0);
        }

        private RangeIntSpliterator(int i, int i2, int i3) {
            this.from = i;
            this.upTo = i2;
            this.last = i3;
        }

        public boolean tryAdvance(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            int i = this.from;
            if (i < this.upTo) {
                this.from = i + 1;
                intConsumer.accept(i);
                return true;
            } else if (this.last <= 0) {
                return false;
            } else {
                this.last = 0;
                intConsumer.accept(i);
                return true;
            }
        }

        public void forEachRemaining(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            int i = this.from;
            int i2 = this.upTo;
            int i3 = this.last;
            this.from = i2;
            this.last = 0;
            while (i < i2) {
                intConsumer.accept(i);
                i++;
            }
            if (i3 > 0) {
                intConsumer.accept(i);
            }
        }

        public long estimateSize() {
            return (((long) this.upTo) - ((long) this.from)) + ((long) this.last);
        }

        public Spliterator.OfInt trySplit() {
            long estimateSize = estimateSize();
            if (estimateSize <= 1) {
                return null;
            }
            int i = this.from;
            int splitPoint = splitPoint(estimateSize) + i;
            this.from = splitPoint;
            return new RangeIntSpliterator(i, splitPoint, 0);
        }

        private int splitPoint(long j) {
            return (int) (j / ((long) (j < WifiManager.WIFI_FEATURE_IE_WHITELIST ? 2 : 8)));
        }
    }

    static final class RangeLongSpliterator implements Spliterator.OfLong {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final long BALANCED_SPLIT_THRESHOLD = 16777216;
        private static final long RIGHT_BALANCED_SPLIT_RATIO = 8;
        private long from;
        private int last;
        private final long upTo;

        public int characteristics() {
            return 17749;
        }

        public Comparator<? super Long> getComparator() {
            return null;
        }

        static {
            Class<Streams> cls = Streams.class;
        }

        RangeLongSpliterator(long j, long j2, boolean z) {
            this(j, j2, z ? 1 : 0);
        }

        private RangeLongSpliterator(long j, long j2, int i) {
            this.from = j;
            this.upTo = j2;
            this.last = i;
        }

        public boolean tryAdvance(LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            long j = this.from;
            if (j < this.upTo) {
                this.from = 1 + j;
                longConsumer.accept(j);
                return true;
            } else if (this.last <= 0) {
                return false;
            } else {
                this.last = 0;
                longConsumer.accept(j);
                return true;
            }
        }

        public void forEachRemaining(LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            long j = this.from;
            long j2 = this.upTo;
            int i = this.last;
            this.from = j2;
            this.last = 0;
            while (j < j2) {
                longConsumer.accept(j);
                j = 1 + j;
            }
            if (i > 0) {
                longConsumer.accept(j);
            }
        }

        public long estimateSize() {
            return (this.upTo - this.from) + ((long) this.last);
        }

        public Spliterator.OfLong trySplit() {
            long estimateSize = estimateSize();
            if (estimateSize <= 1) {
                return null;
            }
            long j = this.from;
            long splitPoint = j + splitPoint(estimateSize);
            this.from = splitPoint;
            return new RangeLongSpliterator(j, splitPoint, 0);
        }

        private long splitPoint(long j) {
            return j / (j < 16777216 ? 2 : 8);
        }
    }

    private static abstract class AbstractStreamBuilderImpl<T, S extends Spliterator<T>> implements Spliterator<T> {
        int count;

        public int characteristics() {
            return 17488;
        }

        public S trySplit() {
            return null;
        }

        private AbstractStreamBuilderImpl() {
        }

        public long estimateSize() {
            return (long) ((-this.count) - 1);
        }
    }

    static final class StreamBuilderImpl<T> extends AbstractStreamBuilderImpl<T, Spliterator<T>> implements Stream.Builder<T> {
        SpinedBuffer<T> buffer;
        T first;

        StreamBuilderImpl() {
            super();
        }

        StreamBuilderImpl(T t) {
            super();
            this.first = t;
            this.count = -2;
        }

        public void accept(T t) {
            if (this.count == 0) {
                this.first = t;
                this.count++;
            } else if (this.count > 0) {
                if (this.buffer == null) {
                    SpinedBuffer<T> spinedBuffer = new SpinedBuffer<>();
                    this.buffer = spinedBuffer;
                    spinedBuffer.accept(this.first);
                    this.count++;
                }
                this.buffer.accept(t);
            } else {
                throw new IllegalStateException();
            }
        }

        public Stream.Builder<T> add(T t) {
            accept(t);
            return this;
        }

        public Stream<T> build() {
            int i = this.count;
            if (i >= 0) {
                this.count = (-this.count) - 1;
                this = this;
                if (i >= 2) {
                    this = this.buffer.spliterator();
                }
                return StreamSupport.stream(this, false);
            }
            throw new IllegalStateException();
        }

        public boolean tryAdvance(Consumer<? super T> consumer) {
            Objects.requireNonNull(consumer);
            if (this.count != -2) {
                return false;
            }
            consumer.accept(this.first);
            this.count = -1;
            return true;
        }

        public void forEachRemaining(Consumer<? super T> consumer) {
            Objects.requireNonNull(consumer);
            if (this.count == -2) {
                consumer.accept(this.first);
                this.count = -1;
            }
        }
    }

    static final class IntStreamBuilderImpl extends AbstractStreamBuilderImpl<Integer, Spliterator.OfInt> implements IntStream.Builder, Spliterator.OfInt {
        SpinedBuffer.OfInt buffer;
        int first;

        IntStreamBuilderImpl() {
            super();
        }

        IntStreamBuilderImpl(int i) {
            super();
            this.first = i;
            this.count = -2;
        }

        public void accept(int i) {
            if (this.count == 0) {
                this.first = i;
                this.count++;
            } else if (this.count > 0) {
                if (this.buffer == null) {
                    SpinedBuffer.OfInt ofInt = new SpinedBuffer.OfInt();
                    this.buffer = ofInt;
                    ofInt.accept(this.first);
                    this.count++;
                }
                this.buffer.accept(i);
            } else {
                throw new IllegalStateException();
            }
        }

        public IntStream build() {
            int i = this.count;
            if (i >= 0) {
                this.count = (-this.count) - 1;
                this = this;
                if (i >= 2) {
                    this = this.buffer.spliterator();
                }
                return StreamSupport.intStream(this, false);
            }
            throw new IllegalStateException();
        }

        public boolean tryAdvance(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            if (this.count != -2) {
                return false;
            }
            intConsumer.accept(this.first);
            this.count = -1;
            return true;
        }

        public void forEachRemaining(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            if (this.count == -2) {
                intConsumer.accept(this.first);
                this.count = -1;
            }
        }
    }

    static final class LongStreamBuilderImpl extends AbstractStreamBuilderImpl<Long, Spliterator.OfLong> implements LongStream.Builder, Spliterator.OfLong {
        SpinedBuffer.OfLong buffer;
        long first;

        LongStreamBuilderImpl() {
            super();
        }

        LongStreamBuilderImpl(long j) {
            super();
            this.first = j;
            this.count = -2;
        }

        public void accept(long j) {
            if (this.count == 0) {
                this.first = j;
                this.count++;
            } else if (this.count > 0) {
                if (this.buffer == null) {
                    SpinedBuffer.OfLong ofLong = new SpinedBuffer.OfLong();
                    this.buffer = ofLong;
                    ofLong.accept(this.first);
                    this.count++;
                }
                this.buffer.accept(j);
            } else {
                throw new IllegalStateException();
            }
        }

        public LongStream build() {
            int i = this.count;
            if (i >= 0) {
                this.count = (-this.count) - 1;
                this = this;
                if (i >= 2) {
                    this = this.buffer.spliterator();
                }
                return StreamSupport.longStream(this, false);
            }
            throw new IllegalStateException();
        }

        public boolean tryAdvance(LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            if (this.count != -2) {
                return false;
            }
            longConsumer.accept(this.first);
            this.count = -1;
            return true;
        }

        public void forEachRemaining(LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            if (this.count == -2) {
                longConsumer.accept(this.first);
                this.count = -1;
            }
        }
    }

    static final class DoubleStreamBuilderImpl extends AbstractStreamBuilderImpl<Double, Spliterator.OfDouble> implements DoubleStream.Builder, Spliterator.OfDouble {
        SpinedBuffer.OfDouble buffer;
        double first;

        DoubleStreamBuilderImpl() {
            super();
        }

        DoubleStreamBuilderImpl(double d) {
            super();
            this.first = d;
            this.count = -2;
        }

        public void accept(double d) {
            if (this.count == 0) {
                this.first = d;
                this.count++;
            } else if (this.count > 0) {
                if (this.buffer == null) {
                    SpinedBuffer.OfDouble ofDouble = new SpinedBuffer.OfDouble();
                    this.buffer = ofDouble;
                    ofDouble.accept(this.first);
                    this.count++;
                }
                this.buffer.accept(d);
            } else {
                throw new IllegalStateException();
            }
        }

        public DoubleStream build() {
            int i = this.count;
            if (i >= 0) {
                this.count = (-this.count) - 1;
                this = this;
                if (i >= 2) {
                    this = this.buffer.spliterator();
                }
                return StreamSupport.doubleStream(this, false);
            }
            throw new IllegalStateException();
        }

        public boolean tryAdvance(DoubleConsumer doubleConsumer) {
            Objects.requireNonNull(doubleConsumer);
            if (this.count != -2) {
                return false;
            }
            doubleConsumer.accept(this.first);
            this.count = -1;
            return true;
        }

        public void forEachRemaining(DoubleConsumer doubleConsumer) {
            Objects.requireNonNull(doubleConsumer);
            if (this.count == -2) {
                doubleConsumer.accept(this.first);
                this.count = -1;
            }
        }
    }

    static abstract class ConcatSpliterator<T, T_SPLITR extends Spliterator<T>> implements Spliterator<T> {
        protected final T_SPLITR aSpliterator;
        protected final T_SPLITR bSpliterator;
        boolean beforeSplit = true;
        final boolean unsized;

        public ConcatSpliterator(T_SPLITR t_splitr, T_SPLITR t_splitr2) {
            this.aSpliterator = t_splitr;
            this.bSpliterator = t_splitr2;
            boolean z = true;
            this.unsized = t_splitr.estimateSize() + t_splitr2.estimateSize() >= 0 ? false : z;
        }

        public T_SPLITR trySplit() {
            T_SPLITR trySplit = this.beforeSplit ? this.aSpliterator : this.bSpliterator.trySplit();
            this.beforeSplit = false;
            return trySplit;
        }

        public boolean tryAdvance(Consumer<? super T> consumer) {
            if (!this.beforeSplit) {
                return this.bSpliterator.tryAdvance(consumer);
            }
            boolean tryAdvance = this.aSpliterator.tryAdvance(consumer);
            if (tryAdvance) {
                return tryAdvance;
            }
            this.beforeSplit = false;
            return this.bSpliterator.tryAdvance(consumer);
        }

        public void forEachRemaining(Consumer<? super T> consumer) {
            if (this.beforeSplit) {
                this.aSpliterator.forEachRemaining(consumer);
            }
            this.bSpliterator.forEachRemaining(consumer);
        }

        public long estimateSize() {
            if (!this.beforeSplit) {
                return this.bSpliterator.estimateSize();
            }
            long estimateSize = this.aSpliterator.estimateSize() + this.bSpliterator.estimateSize();
            if (estimateSize >= 0) {
                return estimateSize;
            }
            return Long.MAX_VALUE;
        }

        public int characteristics() {
            if (!this.beforeSplit) {
                return this.bSpliterator.characteristics();
            }
            return (~((this.unsized ? 16448 : 0) | 5)) & this.aSpliterator.characteristics() & this.bSpliterator.characteristics();
        }

        public Comparator<? super T> getComparator() {
            if (!this.beforeSplit) {
                return this.bSpliterator.getComparator();
            }
            throw new IllegalStateException();
        }

        static class OfRef<T> extends ConcatSpliterator<T, Spliterator<T>> {
            OfRef(Spliterator<T> spliterator, Spliterator<T> spliterator2) {
                super(spliterator, spliterator2);
            }
        }

        private static abstract class OfPrimitive<T, T_CONS, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>> extends ConcatSpliterator<T, T_SPLITR> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR> {
            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            private OfPrimitive(T_SPLITR t_splitr, T_SPLITR t_splitr2) {
                super(t_splitr, t_splitr2);
            }

            public boolean tryAdvance(T_CONS t_cons) {
                if (!this.beforeSplit) {
                    return ((Spliterator.OfPrimitive) this.bSpliterator).tryAdvance(t_cons);
                }
                boolean tryAdvance = ((Spliterator.OfPrimitive) this.aSpliterator).tryAdvance(t_cons);
                if (tryAdvance) {
                    return tryAdvance;
                }
                this.beforeSplit = false;
                return ((Spliterator.OfPrimitive) this.bSpliterator).tryAdvance(t_cons);
            }

            public void forEachRemaining(T_CONS t_cons) {
                if (this.beforeSplit) {
                    ((Spliterator.OfPrimitive) this.aSpliterator).forEachRemaining(t_cons);
                }
                ((Spliterator.OfPrimitive) this.bSpliterator).forEachRemaining(t_cons);
            }
        }

        static class OfInt extends OfPrimitive<Integer, IntConsumer, Spliterator.OfInt> implements Spliterator.OfInt {
            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining(intConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance(intConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                return (Spliterator.OfInt) super.trySplit();
            }

            OfInt(Spliterator.OfInt ofInt, Spliterator.OfInt ofInt2) {
                super(ofInt, ofInt2);
            }
        }

        static class OfLong extends OfPrimitive<Long, LongConsumer, Spliterator.OfLong> implements Spliterator.OfLong {
            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining(longConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance(longConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                return (Spliterator.OfLong) super.trySplit();
            }

            OfLong(Spliterator.OfLong ofLong, Spliterator.OfLong ofLong2) {
                super(ofLong, ofLong2);
            }
        }

        static class OfDouble extends OfPrimitive<Double, DoubleConsumer, Spliterator.OfDouble> implements Spliterator.OfDouble {
            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining(doubleConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance(doubleConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                return (Spliterator.OfDouble) super.trySplit();
            }

            OfDouble(Spliterator.OfDouble ofDouble, Spliterator.OfDouble ofDouble2) {
                super(ofDouble, ofDouble2);
            }
        }
    }

    static Runnable composeWithExceptions(final Runnable runnable, final Runnable runnable2) {
        return new Runnable() {
            public void run() {
                try {
                    Runnable.this.run();
                    runnable2.run();
                    return;
                } catch (Throwable th) {
                    try {
                        th.addSuppressed(th);
                    } catch (Throwable unused) {
                    }
                }
                throw th;
            }
        };
    }

    static Runnable composedClose(final BaseStream<?, ?> baseStream, final BaseStream<?, ?> baseStream2) {
        return new Runnable() {
            public void run() {
                try {
                    BaseStream.this.close();
                    baseStream2.close();
                    return;
                } catch (Throwable th) {
                    try {
                        th.addSuppressed(th);
                    } catch (Throwable unused) {
                    }
                }
                throw th;
            }
        };
    }
}
