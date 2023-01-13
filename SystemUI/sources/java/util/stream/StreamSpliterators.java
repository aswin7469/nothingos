package java.util.stream;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.SpinedBuffer;

class StreamSpliterators {
    StreamSpliterators() {
    }

    private static abstract class AbstractWrappingSpliterator<P_IN, P_OUT, T_BUFFER extends AbstractSpinedBuffer> implements Spliterator<P_OUT> {
        T_BUFFER buffer;
        Sink<P_IN> bufferSink;
        boolean finished;
        final boolean isParallel;
        long nextToConsume;

        /* renamed from: ph */
        final PipelineHelper<P_OUT> f795ph;
        BooleanSupplier pusher;
        Spliterator<P_IN> spliterator;
        private Supplier<Spliterator<P_IN>> spliteratorSupplier;

        /* access modifiers changed from: package-private */
        public abstract void initPartialTraversalState();

        /* access modifiers changed from: package-private */
        public abstract AbstractWrappingSpliterator<P_IN, P_OUT, ?> wrap(Spliterator<P_IN> spliterator2);

        AbstractWrappingSpliterator(PipelineHelper<P_OUT> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z) {
            this.f795ph = pipelineHelper;
            this.spliteratorSupplier = supplier;
            this.spliterator = null;
            this.isParallel = z;
        }

        AbstractWrappingSpliterator(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator2, boolean z) {
            this.f795ph = pipelineHelper;
            this.spliteratorSupplier = null;
            this.spliterator = spliterator2;
            this.isParallel = z;
        }

        /* access modifiers changed from: package-private */
        public final void init() {
            if (this.spliterator == null) {
                this.spliterator = this.spliteratorSupplier.get();
                this.spliteratorSupplier = null;
            }
        }

        /* access modifiers changed from: package-private */
        public final boolean doAdvance() {
            T_BUFFER t_buffer = this.buffer;
            boolean z = false;
            if (t_buffer != null) {
                long j = this.nextToConsume + 1;
                this.nextToConsume = j;
                if (j < t_buffer.count()) {
                    z = true;
                }
                if (z) {
                    return z;
                }
                this.nextToConsume = 0;
                this.buffer.clear();
                return fillBuffer();
            } else if (this.finished) {
                return false;
            } else {
                init();
                initPartialTraversalState();
                this.nextToConsume = 0;
                this.bufferSink.begin(this.spliterator.getExactSizeIfKnown());
                return fillBuffer();
            }
        }

        public Spliterator<P_OUT> trySplit() {
            if (!this.isParallel || this.finished) {
                return null;
            }
            init();
            Spliterator<P_IN> trySplit = this.spliterator.trySplit();
            if (trySplit == null) {
                return null;
            }
            return wrap(trySplit);
        }

        private boolean fillBuffer() {
            while (this.buffer.count() == 0) {
                if (this.bufferSink.cancellationRequested() || !this.pusher.getAsBoolean()) {
                    if (this.finished) {
                        return false;
                    }
                    this.bufferSink.end();
                    this.finished = true;
                }
            }
            return true;
        }

        public final long estimateSize() {
            init();
            return this.spliterator.estimateSize();
        }

        public final long getExactSizeIfKnown() {
            init();
            if (StreamOpFlag.SIZED.isKnown(this.f795ph.getStreamAndOpFlags())) {
                return this.spliterator.getExactSizeIfKnown();
            }
            return -1;
        }

        public final int characteristics() {
            init();
            int characteristics = StreamOpFlag.toCharacteristics(StreamOpFlag.toStreamFlags(this.f795ph.getStreamAndOpFlags()));
            return (characteristics & 64) != 0 ? (characteristics & -16449) | (this.spliterator.characteristics() & 16448) : characteristics;
        }

        public Comparator<? super P_OUT> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }

        public final String toString() {
            return String.format("%s[%s]", getClass().getName(), this.spliterator);
        }
    }

    static final class WrappingSpliterator<P_IN, P_OUT> extends AbstractWrappingSpliterator<P_IN, P_OUT, SpinedBuffer<P_OUT>> {
        WrappingSpliterator(PipelineHelper<P_OUT> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z) {
            super(pipelineHelper, supplier, z);
        }

        WrappingSpliterator(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator, boolean z) {
            super(pipelineHelper, spliterator, z);
        }

        /* access modifiers changed from: package-private */
        public WrappingSpliterator<P_IN, P_OUT> wrap(Spliterator<P_IN> spliterator) {
            return new WrappingSpliterator<>(this.f795ph, spliterator, this.isParallel);
        }

        /* access modifiers changed from: package-private */
        public void initPartialTraversalState() {
            SpinedBuffer spinedBuffer = new SpinedBuffer();
            this.buffer = spinedBuffer;
            this.bufferSink = this.f795ph.wrapSink(new StreamSpliterators$WrappingSpliterator$$ExternalSyntheticLambda0(spinedBuffer));
            this.pusher = new StreamSpliterators$WrappingSpliterator$$ExternalSyntheticLambda1(this);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$initPartialTraversalState$0$java-util-stream-StreamSpliterators$WrappingSpliterator */
        public /* synthetic */ boolean mo63875xf58cc34f() {
            return this.spliterator.tryAdvance(this.bufferSink);
        }

        public boolean tryAdvance(Consumer<? super P_OUT> consumer) {
            Objects.requireNonNull(consumer);
            boolean doAdvance = doAdvance();
            if (doAdvance) {
                consumer.accept(((SpinedBuffer) this.buffer).get(this.nextToConsume));
            }
            return doAdvance;
        }

        public void forEachRemaining(Consumer<? super P_OUT> consumer) {
            if (this.buffer != null || this.finished) {
                do {
                } while (tryAdvance(consumer));
                return;
            }
            Objects.requireNonNull(consumer);
            init();
            PipelineHelper pipelineHelper = this.f795ph;
            Objects.requireNonNull(consumer);
            pipelineHelper.wrapAndCopyInto(new StreamSpliterators$WrappingSpliterator$$ExternalSyntheticLambda2(consumer), this.spliterator);
            this.finished = true;
        }
    }

    static final class IntWrappingSpliterator<P_IN> extends AbstractWrappingSpliterator<P_IN, Integer, SpinedBuffer.OfInt> implements Spliterator.OfInt {
        IntWrappingSpliterator(PipelineHelper<Integer> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z) {
            super(pipelineHelper, supplier, z);
        }

        IntWrappingSpliterator(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator, boolean z) {
            super(pipelineHelper, spliterator, z);
        }

        /* access modifiers changed from: package-private */
        public AbstractWrappingSpliterator<P_IN, Integer, ?> wrap(Spliterator<P_IN> spliterator) {
            return new IntWrappingSpliterator((PipelineHelper<Integer>) this.f795ph, spliterator, this.isParallel);
        }

        /* access modifiers changed from: package-private */
        public void initPartialTraversalState() {
            SpinedBuffer.OfInt ofInt = new SpinedBuffer.OfInt();
            this.buffer = ofInt;
            this.bufferSink = this.f795ph.wrapSink(new C4557x808788ef(ofInt));
            this.pusher = new C4558x808788f0(this);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$initPartialTraversalState$0$java-util-stream-StreamSpliterators$IntWrappingSpliterator */
        public /* synthetic */ boolean mo63851x68714704() {
            return this.spliterator.tryAdvance(this.bufferSink);
        }

        public Spliterator.OfInt trySplit() {
            return (Spliterator.OfInt) super.trySplit();
        }

        public boolean tryAdvance(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            boolean doAdvance = doAdvance();
            if (doAdvance) {
                intConsumer.accept(((SpinedBuffer.OfInt) this.buffer).get(this.nextToConsume));
            }
            return doAdvance;
        }

        public void forEachRemaining(IntConsumer intConsumer) {
            if (this.buffer != null || this.finished) {
                do {
                } while (tryAdvance(intConsumer));
                return;
            }
            Objects.requireNonNull(intConsumer);
            init();
            PipelineHelper pipelineHelper = this.f795ph;
            Objects.requireNonNull(intConsumer);
            pipelineHelper.wrapAndCopyInto(new C4559x808788f1(intConsumer), this.spliterator);
            this.finished = true;
        }
    }

    static final class LongWrappingSpliterator<P_IN> extends AbstractWrappingSpliterator<P_IN, Long, SpinedBuffer.OfLong> implements Spliterator.OfLong {
        LongWrappingSpliterator(PipelineHelper<Long> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z) {
            super(pipelineHelper, supplier, z);
        }

        LongWrappingSpliterator(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator, boolean z) {
            super(pipelineHelper, spliterator, z);
        }

        /* access modifiers changed from: package-private */
        public AbstractWrappingSpliterator<P_IN, Long, ?> wrap(Spliterator<P_IN> spliterator) {
            return new LongWrappingSpliterator((PipelineHelper<Long>) this.f795ph, spliterator, this.isParallel);
        }

        /* access modifiers changed from: package-private */
        public void initPartialTraversalState() {
            SpinedBuffer.OfLong ofLong = new SpinedBuffer.OfLong();
            this.buffer = ofLong;
            this.bufferSink = this.f795ph.wrapSink(new C4561xa6d810a7(ofLong));
            this.pusher = new C4562xa6d810a8(this);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$initPartialTraversalState$0$java-util-stream-StreamSpliterators$LongWrappingSpliterator */
        public /* synthetic */ boolean mo63852x44d1e433() {
            return this.spliterator.tryAdvance(this.bufferSink);
        }

        public Spliterator.OfLong trySplit() {
            return (Spliterator.OfLong) super.trySplit();
        }

        public boolean tryAdvance(LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            boolean doAdvance = doAdvance();
            if (doAdvance) {
                longConsumer.accept(((SpinedBuffer.OfLong) this.buffer).get(this.nextToConsume));
            }
            return doAdvance;
        }

        public void forEachRemaining(LongConsumer longConsumer) {
            if (this.buffer != null || this.finished) {
                do {
                } while (tryAdvance(longConsumer));
                return;
            }
            Objects.requireNonNull(longConsumer);
            init();
            PipelineHelper pipelineHelper = this.f795ph;
            Objects.requireNonNull(longConsumer);
            pipelineHelper.wrapAndCopyInto(new C4560xa6d810a6(longConsumer), this.spliterator);
            this.finished = true;
        }
    }

    static final class DoubleWrappingSpliterator<P_IN> extends AbstractWrappingSpliterator<P_IN, Double, SpinedBuffer.OfDouble> implements Spliterator.OfDouble {
        DoubleWrappingSpliterator(PipelineHelper<Double> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z) {
            super(pipelineHelper, supplier, z);
        }

        DoubleWrappingSpliterator(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator, boolean z) {
            super(pipelineHelper, spliterator, z);
        }

        /* access modifiers changed from: package-private */
        public AbstractWrappingSpliterator<P_IN, Double, ?> wrap(Spliterator<P_IN> spliterator) {
            return new DoubleWrappingSpliterator((PipelineHelper<Double>) this.f795ph, spliterator, this.isParallel);
        }

        /* access modifiers changed from: package-private */
        public void initPartialTraversalState() {
            SpinedBuffer.OfDouble ofDouble = new SpinedBuffer.OfDouble();
            this.buffer = ofDouble;
            this.bufferSink = this.f795ph.wrapSink(new C4554xc57d6cf1(ofDouble));
            this.pusher = new C4555xc57d6cf2(this);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$initPartialTraversalState$0$java-util-stream-StreamSpliterators$DoubleWrappingSpliterator */
        public /* synthetic */ boolean mo63850xbf8f913e() {
            return this.spliterator.tryAdvance(this.bufferSink);
        }

        public Spliterator.OfDouble trySplit() {
            return (Spliterator.OfDouble) super.trySplit();
        }

        public boolean tryAdvance(DoubleConsumer doubleConsumer) {
            Objects.requireNonNull(doubleConsumer);
            boolean doAdvance = doAdvance();
            if (doAdvance) {
                doubleConsumer.accept(((SpinedBuffer.OfDouble) this.buffer).get(this.nextToConsume));
            }
            return doAdvance;
        }

        public void forEachRemaining(DoubleConsumer doubleConsumer) {
            if (this.buffer != null || this.finished) {
                do {
                } while (tryAdvance(doubleConsumer));
                return;
            }
            Objects.requireNonNull(doubleConsumer);
            init();
            PipelineHelper pipelineHelper = this.f795ph;
            Objects.requireNonNull(doubleConsumer);
            pipelineHelper.wrapAndCopyInto(new C4556xc57d6cf3(doubleConsumer), this.spliterator);
            this.finished = true;
        }
    }

    static class DelegatingSpliterator<T, T_SPLITR extends Spliterator<T>> implements Spliterator<T> {

        /* renamed from: s */
        private T_SPLITR f796s;
        private final Supplier<? extends T_SPLITR> supplier;

        DelegatingSpliterator(Supplier<? extends T_SPLITR> supplier2) {
            this.supplier = supplier2;
        }

        /* access modifiers changed from: package-private */
        public T_SPLITR get() {
            if (this.f796s == null) {
                this.f796s = (Spliterator) this.supplier.get();
            }
            return this.f796s;
        }

        public T_SPLITR trySplit() {
            return get().trySplit();
        }

        public boolean tryAdvance(Consumer<? super T> consumer) {
            return get().tryAdvance(consumer);
        }

        public void forEachRemaining(Consumer<? super T> consumer) {
            get().forEachRemaining(consumer);
        }

        public long estimateSize() {
            return get().estimateSize();
        }

        public int characteristics() {
            return get().characteristics();
        }

        public Comparator<? super T> getComparator() {
            return get().getComparator();
        }

        public long getExactSizeIfKnown() {
            return get().getExactSizeIfKnown();
        }

        public String toString() {
            return getClass().getName() + NavigationBarInflaterView.SIZE_MOD_START + get() + NavigationBarInflaterView.SIZE_MOD_END;
        }

        static class OfPrimitive<T, T_CONS, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>> extends DelegatingSpliterator<T, T_SPLITR> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR> {
            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            OfPrimitive(Supplier<? extends T_SPLITR> supplier) {
                super(supplier);
            }

            public boolean tryAdvance(T_CONS t_cons) {
                return ((Spliterator.OfPrimitive) get()).tryAdvance(t_cons);
            }

            public void forEachRemaining(T_CONS t_cons) {
                ((Spliterator.OfPrimitive) get()).forEachRemaining(t_cons);
            }
        }

        static final class OfInt extends OfPrimitive<Integer, IntConsumer, Spliterator.OfInt> implements Spliterator.OfInt {
            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining(intConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance(intConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                return (Spliterator.OfInt) super.trySplit();
            }

            OfInt(Supplier<Spliterator.OfInt> supplier) {
                super(supplier);
            }
        }

        static final class OfLong extends OfPrimitive<Long, LongConsumer, Spliterator.OfLong> implements Spliterator.OfLong {
            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining(longConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance(longConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                return (Spliterator.OfLong) super.trySplit();
            }

            OfLong(Supplier<Spliterator.OfLong> supplier) {
                super(supplier);
            }
        }

        static final class OfDouble extends OfPrimitive<Double, DoubleConsumer, Spliterator.OfDouble> implements Spliterator.OfDouble {
            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining(doubleConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance(doubleConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                return (Spliterator.OfDouble) super.trySplit();
            }

            OfDouble(Supplier<Spliterator.OfDouble> supplier) {
                super(supplier);
            }
        }
    }

    static abstract class SliceSpliterator<T, T_SPLITR extends Spliterator<T>> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        long fence;
        long index;

        /* renamed from: s */
        T_SPLITR f802s;
        final long sliceFence;
        final long sliceOrigin;

        /* access modifiers changed from: protected */
        public abstract T_SPLITR makeSpliterator(T_SPLITR t_splitr, long j, long j2, long j3, long j4);

        static {
            Class<StreamSpliterators> cls = StreamSpliterators.class;
        }

        SliceSpliterator(T_SPLITR t_splitr, long j, long j2, long j3, long j4) {
            this.f802s = t_splitr;
            this.sliceOrigin = j;
            this.sliceFence = j2;
            this.index = j3;
            this.fence = j4;
        }

        public T_SPLITR trySplit() {
            long j = this.sliceOrigin;
            long j2 = this.fence;
            if (j >= j2 || this.index >= j2) {
                return null;
            }
            while (true) {
                T_SPLITR trySplit = this.f802s.trySplit();
                if (trySplit == null) {
                    return null;
                }
                long estimateSize = this.index + trySplit.estimateSize();
                long min = Math.min(estimateSize, this.sliceFence);
                long j3 = this.sliceOrigin;
                if (j3 >= min) {
                    this.index = min;
                } else {
                    long j4 = this.sliceFence;
                    if (min >= j4) {
                        this.f802s = trySplit;
                        this.fence = min;
                    } else {
                        long j5 = this.index;
                        if (j5 < j3 || estimateSize > j4) {
                            this.index = min;
                            return makeSpliterator(trySplit, j3, j4, j5, min);
                        }
                        this.index = min;
                        return trySplit;
                    }
                }
            }
        }

        public long estimateSize() {
            long j = this.sliceOrigin;
            long j2 = this.fence;
            if (j < j2) {
                return j2 - Math.max(j, this.index);
            }
            return 0;
        }

        public int characteristics() {
            return this.f802s.characteristics();
        }

        static final class OfRef<T> extends SliceSpliterator<T, Spliterator<T>> implements Spliterator<T> {
            static /* synthetic */ void lambda$forEachRemaining$1(Object obj) {
            }

            static /* synthetic */ void lambda$tryAdvance$0(Object obj) {
            }

            OfRef(Spliterator<T> spliterator, long j, long j2) {
                this(spliterator, j, j2, 0, Math.min(spliterator.estimateSize(), j2));
            }

            private OfRef(Spliterator<T> spliterator, long j, long j2, long j3, long j4) {
                super(spliterator, j, j2, j3, j4);
            }

            /* access modifiers changed from: protected */
            public Spliterator<T> makeSpliterator(Spliterator<T> spliterator, long j, long j2, long j3, long j4) {
                return new OfRef(spliterator, j, j2, j3, j4);
            }

            public boolean tryAdvance(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                if (this.sliceOrigin >= this.fence) {
                    return false;
                }
                while (this.sliceOrigin > this.index) {
                    this.f802s.tryAdvance(new C4567xaa59d8c1());
                    this.index++;
                }
                if (this.index >= this.fence) {
                    return false;
                }
                this.index++;
                return this.f802s.tryAdvance(consumer);
            }

            public void forEachRemaining(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                if (this.sliceOrigin >= this.fence || this.index >= this.fence) {
                    return;
                }
                if (this.index < this.sliceOrigin || this.index + this.f802s.estimateSize() > this.sliceFence) {
                    while (this.sliceOrigin > this.index) {
                        this.f802s.tryAdvance(new C4566xaa59d8c0());
                        this.index++;
                    }
                    while (this.index < this.fence) {
                        this.f802s.tryAdvance(consumer);
                        this.index++;
                    }
                    return;
                }
                this.f802s.forEachRemaining(consumer);
                this.index = this.fence;
            }
        }

        static abstract class OfPrimitive<T, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>, T_CONS> extends SliceSpliterator<T, T_SPLITR> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR> {
            /* access modifiers changed from: protected */
            public abstract T_CONS emptyConsumer();

            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            OfPrimitive(T_SPLITR t_splitr, long j, long j2) {
                this(t_splitr, j, j2, 0, Math.min(t_splitr.estimateSize(), j2));
            }

            private OfPrimitive(T_SPLITR t_splitr, long j, long j2, long j3, long j4) {
                super(t_splitr, j, j2, j3, j4);
            }

            public boolean tryAdvance(T_CONS t_cons) {
                Objects.requireNonNull(t_cons);
                if (this.sliceOrigin >= this.fence) {
                    return false;
                }
                while (this.sliceOrigin > this.index) {
                    ((Spliterator.OfPrimitive) this.f802s).tryAdvance(emptyConsumer());
                    this.index++;
                }
                if (this.index >= this.fence) {
                    return false;
                }
                this.index++;
                return ((Spliterator.OfPrimitive) this.f802s).tryAdvance(t_cons);
            }

            public void forEachRemaining(T_CONS t_cons) {
                Objects.requireNonNull(t_cons);
                if (this.sliceOrigin >= this.fence || this.index >= this.fence) {
                    return;
                }
                if (this.index < this.sliceOrigin || this.index + ((Spliterator.OfPrimitive) this.f802s).estimateSize() > this.sliceFence) {
                    while (this.sliceOrigin > this.index) {
                        ((Spliterator.OfPrimitive) this.f802s).tryAdvance(emptyConsumer());
                        this.index++;
                    }
                    while (this.index < this.fence) {
                        ((Spliterator.OfPrimitive) this.f802s).tryAdvance(t_cons);
                        this.index++;
                    }
                    return;
                }
                ((Spliterator.OfPrimitive) this.f802s).forEachRemaining(t_cons);
                this.index = this.fence;
            }
        }

        static final class OfInt extends OfPrimitive<Integer, Spliterator.OfInt, IntConsumer> implements Spliterator.OfInt {
            static /* synthetic */ void lambda$emptyConsumer$0(int i) {
            }

            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining(intConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance(intConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                return (Spliterator.OfInt) super.trySplit();
            }

            OfInt(Spliterator.OfInt ofInt, long j, long j2) {
                super(ofInt, j, j2);
            }

            OfInt(Spliterator.OfInt ofInt, long j, long j2, long j3, long j4) {
                super(ofInt, j, j2, j3, j4);
            }

            /* access modifiers changed from: protected */
            public Spliterator.OfInt makeSpliterator(Spliterator.OfInt ofInt, long j, long j2, long j3, long j4) {
                return new OfInt(ofInt, j, j2, j3, j4);
            }

            /* access modifiers changed from: protected */
            public IntConsumer emptyConsumer() {
                return new C4564xd052fd1c();
            }
        }

        static final class OfLong extends OfPrimitive<Long, Spliterator.OfLong, LongConsumer> implements Spliterator.OfLong {
            static /* synthetic */ void lambda$emptyConsumer$0(long j) {
            }

            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining(longConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance(longConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                return (Spliterator.OfLong) super.trySplit();
            }

            OfLong(Spliterator.OfLong ofLong, long j, long j2) {
                super(ofLong, j, j2);
            }

            OfLong(Spliterator.OfLong ofLong, long j, long j2, long j3, long j4) {
                super(ofLong, j, j2, j3, j4);
            }

            /* access modifiers changed from: protected */
            public Spliterator.OfLong makeSpliterator(Spliterator.OfLong ofLong, long j, long j2, long j3, long j4) {
                return new OfLong(ofLong, j, j2, j3, j4);
            }

            /* access modifiers changed from: protected */
            public LongConsumer emptyConsumer() {
                return new C4565xec23d6ed();
            }
        }

        static final class OfDouble extends OfPrimitive<Double, Spliterator.OfDouble, DoubleConsumer> implements Spliterator.OfDouble {
            static /* synthetic */ void lambda$emptyConsumer$0(double d) {
            }

            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining(doubleConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance(doubleConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                return (Spliterator.OfDouble) super.trySplit();
            }

            OfDouble(Spliterator.OfDouble ofDouble, long j, long j2) {
                super(ofDouble, j, j2);
            }

            OfDouble(Spliterator.OfDouble ofDouble, long j, long j2, long j3, long j4) {
                super(ofDouble, j, j2, j3, j4);
            }

            /* access modifiers changed from: protected */
            public Spliterator.OfDouble makeSpliterator(Spliterator.OfDouble ofDouble, long j, long j2, long j3, long j4) {
                return new OfDouble(ofDouble, j, j2, j3, j4);
            }

            /* access modifiers changed from: protected */
            public DoubleConsumer emptyConsumer() {
                return new C4563x1d92bb82();
            }
        }
    }

    static abstract class UnorderedSliceSpliterator<T, T_SPLITR extends Spliterator<T>> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        static final int CHUNK_SIZE = 128;
        private final AtomicLong permits;

        /* renamed from: s */
        protected final T_SPLITR f803s;
        private final long skipThreshold;
        protected final boolean unlimited;

        enum PermitStatus {
            NO_MORE,
            MAYBE_MORE,
            UNLIMITED
        }

        /* access modifiers changed from: protected */
        public abstract T_SPLITR makeSpliterator(T_SPLITR t_splitr);

        static {
            Class<StreamSpliterators> cls = StreamSpliterators.class;
        }

        UnorderedSliceSpliterator(T_SPLITR t_splitr, long j, long j2) {
            this.f803s = t_splitr;
            long j3 = 0;
            int i = (j2 > 0 ? 1 : (j2 == 0 ? 0 : -1));
            this.unlimited = i < 0;
            this.skipThreshold = i >= 0 ? j2 : j3;
            this.permits = new AtomicLong(i >= 0 ? j + j2 : j);
        }

        UnorderedSliceSpliterator(T_SPLITR t_splitr, UnorderedSliceSpliterator<T, T_SPLITR> unorderedSliceSpliterator) {
            this.f803s = t_splitr;
            this.unlimited = unorderedSliceSpliterator.unlimited;
            this.permits = unorderedSliceSpliterator.permits;
            this.skipThreshold = unorderedSliceSpliterator.skipThreshold;
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START, MTH_ENTER_BLOCK] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final long acquirePermits(long r10) {
            /*
                r9 = this;
            L_0x0000:
                java.util.concurrent.atomic.AtomicLong r0 = r9.permits
                long r0 = r0.get()
                r2 = 0
                int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r4 != 0) goto L_0x0013
                boolean r9 = r9.unlimited
                if (r9 == 0) goto L_0x0011
                goto L_0x0012
            L_0x0011:
                r10 = r2
            L_0x0012:
                return r10
            L_0x0013:
                long r4 = java.lang.Math.min((long) r0, (long) r10)
                int r6 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
                if (r6 <= 0) goto L_0x0025
                java.util.concurrent.atomic.AtomicLong r6 = r9.permits
                long r7 = r0 - r4
                boolean r6 = r6.compareAndSet(r0, r7)
                if (r6 == 0) goto L_0x0000
            L_0x0025:
                boolean r6 = r9.unlimited
                if (r6 == 0) goto L_0x002f
                long r10 = r10 - r4
                long r9 = java.lang.Math.max((long) r10, (long) r2)
                return r9
            L_0x002f:
                long r9 = r9.skipThreshold
                int r11 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1))
                if (r11 <= 0) goto L_0x003c
                long r0 = r0 - r9
                long r4 = r4 - r0
                long r9 = java.lang.Math.max((long) r4, (long) r2)
                return r9
            L_0x003c:
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.stream.StreamSpliterators.UnorderedSliceSpliterator.acquirePermits(long):long");
        }

        /* access modifiers changed from: protected */
        public final PermitStatus permitStatus() {
            if (this.permits.get() > 0) {
                return PermitStatus.MAYBE_MORE;
            }
            return this.unlimited ? PermitStatus.UNLIMITED : PermitStatus.NO_MORE;
        }

        public final T_SPLITR trySplit() {
            Spliterator trySplit;
            if (this.permits.get() == 0 || (trySplit = this.f803s.trySplit()) == null) {
                return null;
            }
            return makeSpliterator(trySplit);
        }

        public final long estimateSize() {
            return this.f803s.estimateSize();
        }

        public final int characteristics() {
            return this.f803s.characteristics() & -16465;
        }

        static final class OfRef<T> extends UnorderedSliceSpliterator<T, Spliterator<T>> implements Spliterator<T>, Consumer<T> {
            T tmpSlot;

            OfRef(Spliterator<T> spliterator, long j, long j2) {
                super(spliterator, j, j2);
            }

            OfRef(Spliterator<T> spliterator, OfRef<T> ofRef) {
                super(spliterator, ofRef);
            }

            public final void accept(T t) {
                this.tmpSlot = t;
            }

            public boolean tryAdvance(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                while (permitStatus() != PermitStatus.NO_MORE && this.f803s.tryAdvance(this)) {
                    if (acquirePermits(1) == 1) {
                        consumer.accept(this.tmpSlot);
                        this.tmpSlot = null;
                        return true;
                    }
                }
                return false;
            }

            public void forEachRemaining(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                ArrayBuffer.OfRef ofRef = null;
                while (true) {
                    PermitStatus permitStatus = permitStatus();
                    if (permitStatus == PermitStatus.NO_MORE) {
                        return;
                    }
                    if (permitStatus == PermitStatus.MAYBE_MORE) {
                        if (ofRef == null) {
                            ofRef = new ArrayBuffer.OfRef(128);
                        } else {
                            ofRef.reset();
                        }
                        long j = 0;
                        while (this.f803s.tryAdvance(ofRef)) {
                            j++;
                            if (j >= 128) {
                                break;
                            }
                        }
                        if (j != 0) {
                            ofRef.forEach(consumer, acquirePermits(j));
                        } else {
                            return;
                        }
                    } else {
                        this.f803s.forEachRemaining(consumer);
                        return;
                    }
                }
            }

            /* access modifiers changed from: protected */
            public Spliterator<T> makeSpliterator(Spliterator<T> spliterator) {
                return new OfRef(spliterator, this);
            }
        }

        static abstract class OfPrimitive<T, T_CONS, T_BUFF extends ArrayBuffer.OfPrimitive<T_CONS>, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>> extends UnorderedSliceSpliterator<T, T_SPLITR> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR> {
            /* access modifiers changed from: protected */
            public abstract void acceptConsumed(T_CONS t_cons);

            /* access modifiers changed from: protected */
            public abstract T_BUFF bufferCreate(int i);

            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            OfPrimitive(T_SPLITR t_splitr, long j, long j2) {
                super(t_splitr, j, j2);
            }

            OfPrimitive(T_SPLITR t_splitr, OfPrimitive<T, T_CONS, T_BUFF, T_SPLITR> ofPrimitive) {
                super(t_splitr, ofPrimitive);
            }

            public boolean tryAdvance(T_CONS t_cons) {
                Objects.requireNonNull(t_cons);
                while (permitStatus() != PermitStatus.NO_MORE && ((Spliterator.OfPrimitive) this.f803s).tryAdvance(this)) {
                    if (acquirePermits(1) == 1) {
                        acceptConsumed(t_cons);
                        return true;
                    }
                }
                return false;
            }

            public void forEachRemaining(T_CONS t_cons) {
                Objects.requireNonNull(t_cons);
                ArrayBuffer.OfPrimitive ofPrimitive = null;
                while (true) {
                    PermitStatus permitStatus = permitStatus();
                    if (permitStatus == PermitStatus.NO_MORE) {
                        return;
                    }
                    if (permitStatus == PermitStatus.MAYBE_MORE) {
                        if (ofPrimitive == null) {
                            ofPrimitive = bufferCreate(128);
                        } else {
                            ofPrimitive.reset();
                        }
                        long j = 0;
                        while (((Spliterator.OfPrimitive) this.f803s).tryAdvance(ofPrimitive)) {
                            j++;
                            if (j >= 128) {
                                break;
                            }
                        }
                        if (j != 0) {
                            ofPrimitive.forEach(t_cons, acquirePermits(j));
                        } else {
                            return;
                        }
                    } else {
                        ((Spliterator.OfPrimitive) this.f803s).forEachRemaining(t_cons);
                        return;
                    }
                }
            }
        }

        static final class OfInt extends OfPrimitive<Integer, IntConsumer, ArrayBuffer.OfInt, Spliterator.OfInt> implements Spliterator.OfInt, IntConsumer {
            int tmpValue;

            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining(intConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance(intConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                return (Spliterator.OfInt) super.trySplit();
            }

            OfInt(Spliterator.OfInt ofInt, long j, long j2) {
                super(ofInt, j, j2);
            }

            OfInt(Spliterator.OfInt ofInt, OfInt ofInt2) {
                super(ofInt, ofInt2);
            }

            public void accept(int i) {
                this.tmpValue = i;
            }

            /* access modifiers changed from: protected */
            public void acceptConsumed(IntConsumer intConsumer) {
                intConsumer.accept(this.tmpValue);
            }

            /* access modifiers changed from: protected */
            public ArrayBuffer.OfInt bufferCreate(int i) {
                return new ArrayBuffer.OfInt(i);
            }

            /* access modifiers changed from: protected */
            public Spliterator.OfInt makeSpliterator(Spliterator.OfInt ofInt) {
                return new OfInt(ofInt, this);
            }
        }

        static final class OfLong extends OfPrimitive<Long, LongConsumer, ArrayBuffer.OfLong, Spliterator.OfLong> implements Spliterator.OfLong, LongConsumer {
            long tmpValue;

            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining(longConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance(longConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                return (Spliterator.OfLong) super.trySplit();
            }

            OfLong(Spliterator.OfLong ofLong, long j, long j2) {
                super(ofLong, j, j2);
            }

            OfLong(Spliterator.OfLong ofLong, OfLong ofLong2) {
                super(ofLong, ofLong2);
            }

            public void accept(long j) {
                this.tmpValue = j;
            }

            /* access modifiers changed from: protected */
            public void acceptConsumed(LongConsumer longConsumer) {
                longConsumer.accept(this.tmpValue);
            }

            /* access modifiers changed from: protected */
            public ArrayBuffer.OfLong bufferCreate(int i) {
                return new ArrayBuffer.OfLong(i);
            }

            /* access modifiers changed from: protected */
            public Spliterator.OfLong makeSpliterator(Spliterator.OfLong ofLong) {
                return new OfLong(ofLong, this);
            }
        }

        static final class OfDouble extends OfPrimitive<Double, DoubleConsumer, ArrayBuffer.OfDouble, Spliterator.OfDouble> implements Spliterator.OfDouble, DoubleConsumer {
            double tmpValue;

            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining(doubleConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance(doubleConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                return (Spliterator.OfDouble) super.trySplit();
            }

            OfDouble(Spliterator.OfDouble ofDouble, long j, long j2) {
                super(ofDouble, j, j2);
            }

            OfDouble(Spliterator.OfDouble ofDouble, OfDouble ofDouble2) {
                super(ofDouble, ofDouble2);
            }

            public void accept(double d) {
                this.tmpValue = d;
            }

            /* access modifiers changed from: protected */
            public void acceptConsumed(DoubleConsumer doubleConsumer) {
                doubleConsumer.accept(this.tmpValue);
            }

            /* access modifiers changed from: protected */
            public ArrayBuffer.OfDouble bufferCreate(int i) {
                return new ArrayBuffer.OfDouble(i);
            }

            /* access modifiers changed from: protected */
            public Spliterator.OfDouble makeSpliterator(Spliterator.OfDouble ofDouble) {
                return new OfDouble(ofDouble, this);
            }
        }
    }

    static final class DistinctSpliterator<T> implements Spliterator<T>, Consumer<T> {
        private static final Object NULL_VALUE = new Object();

        /* renamed from: s */
        private final Spliterator<T> f797s;
        private final ConcurrentHashMap<T, Boolean> seen;
        private T tmpSlot;

        DistinctSpliterator(Spliterator<T> spliterator) {
            this(spliterator, new ConcurrentHashMap());
        }

        private DistinctSpliterator(Spliterator<T> spliterator, ConcurrentHashMap<T, Boolean> concurrentHashMap) {
            this.f797s = spliterator;
            this.seen = concurrentHashMap;
        }

        public void accept(T t) {
            this.tmpSlot = t;
        }

        private T mapNull(T t) {
            return t != null ? t : NULL_VALUE;
        }

        public boolean tryAdvance(Consumer<? super T> consumer) {
            while (this.f797s.tryAdvance(this)) {
                if (this.seen.putIfAbsent(mapNull(this.tmpSlot), Boolean.TRUE) == null) {
                    consumer.accept(this.tmpSlot);
                    this.tmpSlot = null;
                    return true;
                }
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super T> consumer) {
            this.f797s.forEachRemaining(new StreamSpliterators$DistinctSpliterator$$ExternalSyntheticLambda0(this, consumer));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$forEachRemaining$0$java-util-stream-StreamSpliterators$DistinctSpliterator */
        public /* synthetic */ void mo63849xb9bff3f1(Consumer consumer, Object obj) {
            if (this.seen.putIfAbsent(mapNull(obj), Boolean.TRUE) == null) {
                consumer.accept(obj);
            }
        }

        public Spliterator<T> trySplit() {
            Spliterator<T> trySplit = this.f797s.trySplit();
            if (trySplit != null) {
                return new DistinctSpliterator(trySplit, this.seen);
            }
            return null;
        }

        public long estimateSize() {
            return this.f797s.estimateSize();
        }

        public int characteristics() {
            return (this.f797s.characteristics() & -16469) | 1;
        }

        public Comparator<? super T> getComparator() {
            return this.f797s.getComparator();
        }
    }

    static abstract class InfiniteSupplyingSpliterator<T> implements Spliterator<T> {
        long estimate;

        public int characteristics() {
            return 1024;
        }

        protected InfiniteSupplyingSpliterator(long j) {
            this.estimate = j;
        }

        public long estimateSize() {
            return this.estimate;
        }

        static final class OfRef<T> extends InfiniteSupplyingSpliterator<T> {

            /* renamed from: s */
            final Supplier<T> f801s;

            OfRef(long j, Supplier<T> supplier) {
                super(j);
                this.f801s = supplier;
            }

            public boolean tryAdvance(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                consumer.accept(this.f801s.get());
                return true;
            }

            public Spliterator<T> trySplit() {
                if (this.estimate == 0) {
                    return null;
                }
                long j = this.estimate >>> 1;
                this.estimate = j;
                return new OfRef(j, this.f801s);
            }
        }

        static final class OfInt extends InfiniteSupplyingSpliterator<Integer> implements Spliterator.OfInt {

            /* renamed from: s */
            final IntSupplier f799s;

            OfInt(long j, IntSupplier intSupplier) {
                super(j);
                this.f799s = intSupplier;
            }

            public boolean tryAdvance(IntConsumer intConsumer) {
                Objects.requireNonNull(intConsumer);
                intConsumer.accept(this.f799s.getAsInt());
                return true;
            }

            public Spliterator.OfInt trySplit() {
                if (this.estimate == 0) {
                    return null;
                }
                long j = this.estimate >>> 1;
                this.estimate = j;
                return new OfInt(j, this.f799s);
            }
        }

        static final class OfLong extends InfiniteSupplyingSpliterator<Long> implements Spliterator.OfLong {

            /* renamed from: s */
            final LongSupplier f800s;

            OfLong(long j, LongSupplier longSupplier) {
                super(j);
                this.f800s = longSupplier;
            }

            public boolean tryAdvance(LongConsumer longConsumer) {
                Objects.requireNonNull(longConsumer);
                longConsumer.accept(this.f800s.getAsLong());
                return true;
            }

            public Spliterator.OfLong trySplit() {
                if (this.estimate == 0) {
                    return null;
                }
                long j = this.estimate >>> 1;
                this.estimate = j;
                return new OfLong(j, this.f800s);
            }
        }

        static final class OfDouble extends InfiniteSupplyingSpliterator<Double> implements Spliterator.OfDouble {

            /* renamed from: s */
            final DoubleSupplier f798s;

            OfDouble(long j, DoubleSupplier doubleSupplier) {
                super(j);
                this.f798s = doubleSupplier;
            }

            public boolean tryAdvance(DoubleConsumer doubleConsumer) {
                Objects.requireNonNull(doubleConsumer);
                doubleConsumer.accept(this.f798s.getAsDouble());
                return true;
            }

            public Spliterator.OfDouble trySplit() {
                if (this.estimate == 0) {
                    return null;
                }
                long j = this.estimate >>> 1;
                this.estimate = j;
                return new OfDouble(j, this.f798s);
            }
        }
    }

    static abstract class ArrayBuffer {
        int index;

        ArrayBuffer() {
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            this.index = 0;
        }

        static final class OfRef<T> extends ArrayBuffer implements Consumer<T> {
            final Object[] array;

            OfRef(int i) {
                this.array = new Object[i];
            }

            public void accept(T t) {
                Object[] objArr = this.array;
                int i = this.index;
                this.index = i + 1;
                objArr[i] = t;
            }

            public void forEach(Consumer<? super T> consumer, long j) {
                for (int i = 0; ((long) i) < j; i++) {
                    consumer.accept(this.array[i]);
                }
            }
        }

        static abstract class OfPrimitive<T_CONS> extends ArrayBuffer {
            int index;

            /* access modifiers changed from: package-private */
            public abstract void forEach(T_CONS t_cons, long j);

            OfPrimitive() {
            }

            /* access modifiers changed from: package-private */
            public void reset() {
                this.index = 0;
            }
        }

        static final class OfInt extends OfPrimitive<IntConsumer> implements IntConsumer {
            final int[] array;

            OfInt(int i) {
                this.array = new int[i];
            }

            public void accept(int i) {
                int[] iArr = this.array;
                int i2 = this.index;
                this.index = i2 + 1;
                iArr[i2] = i;
            }

            public void forEach(IntConsumer intConsumer, long j) {
                for (int i = 0; ((long) i) < j; i++) {
                    intConsumer.accept(this.array[i]);
                }
            }
        }

        static final class OfLong extends OfPrimitive<LongConsumer> implements LongConsumer {
            final long[] array;

            OfLong(int i) {
                this.array = new long[i];
            }

            public void accept(long j) {
                long[] jArr = this.array;
                int i = this.index;
                this.index = i + 1;
                jArr[i] = j;
            }

            public void forEach(LongConsumer longConsumer, long j) {
                for (int i = 0; ((long) i) < j; i++) {
                    longConsumer.accept(this.array[i]);
                }
            }
        }

        static final class OfDouble extends OfPrimitive<DoubleConsumer> implements DoubleConsumer {
            final double[] array;

            OfDouble(int i) {
                this.array = new double[i];
            }

            public void accept(double d) {
                double[] dArr = this.array;
                int i = this.index;
                this.index = i + 1;
                dArr[i] = d;
            }

            /* access modifiers changed from: package-private */
            public void forEach(DoubleConsumer doubleConsumer, long j) {
                for (int i = 0; ((long) i) < j; i++) {
                    doubleConsumer.accept(this.array[i]);
                }
            }
        }
    }
}
