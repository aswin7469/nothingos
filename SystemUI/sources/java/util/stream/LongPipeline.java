package java.util.stream;

import java.util.LongSummaryStatistics;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.stream.DoublePipeline;
import java.util.stream.IntPipeline;
import java.util.stream.MatchOps;
import java.util.stream.Node;
import java.util.stream.ReferencePipeline;
import java.util.stream.Sink;
import java.util.stream.StreamSpliterators;

public abstract class LongPipeline<E_IN> extends AbstractPipeline<E_IN, Long, LongStream> implements LongStream {
    static /* synthetic */ long[] lambda$average$1() {
        return new long[2];
    }

    static /* synthetic */ long lambda$count$4(long j) {
        return 1;
    }

    public /* bridge */ /* synthetic */ LongStream parallel() {
        return (LongStream) super.parallel();
    }

    public /* bridge */ /* synthetic */ LongStream sequential() {
        return (LongStream) super.sequential();
    }

    LongPipeline(Supplier<? extends Spliterator<Long>> supplier, int i, boolean z) {
        super((Supplier<? extends Spliterator<?>>) supplier, i, z);
    }

    LongPipeline(Spliterator<Long> spliterator, int i, boolean z) {
        super((Spliterator<?>) spliterator, i, z);
    }

    LongPipeline(AbstractPipeline<?, E_IN, ?> abstractPipeline, int i) {
        super(abstractPipeline, i);
    }

    private static LongConsumer adapt(Sink<Long> sink) {
        if (sink instanceof LongConsumer) {
            return (LongConsumer) sink;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using LongStream.adapt(Sink<Long> s)");
        }
        Objects.requireNonNull(sink);
        return new LongPipeline$$ExternalSyntheticLambda3(sink);
    }

    /* access modifiers changed from: private */
    public static Spliterator.OfLong adapt(Spliterator<Long> spliterator) {
        if (spliterator instanceof Spliterator.OfLong) {
            return (Spliterator.OfLong) spliterator;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using LongStream.adapt(Spliterator<Long> s)");
        }
        throw new UnsupportedOperationException("LongStream.adapt(Spliterator<Long> s)");
    }

    public final StreamShape getOutputShape() {
        return StreamShape.LONG_VALUE;
    }

    public final <P_IN> Node<Long> evaluateToNode(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator, boolean z, IntFunction<Long[]> intFunction) {
        return Nodes.collectLong(pipelineHelper, spliterator, z);
    }

    public final <P_IN> Spliterator<Long> wrap(PipelineHelper<Long> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z) {
        return new StreamSpliterators.LongWrappingSpliterator(pipelineHelper, supplier, z);
    }

    public final Spliterator.OfLong lazySpliterator(Supplier<? extends Spliterator<Long>> supplier) {
        return new StreamSpliterators.DelegatingSpliterator.OfLong(supplier);
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x0008 A[LOOP:0: B:1:0x0008->B:4:0x0012, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void forEachWithCancel(java.util.Spliterator<java.lang.Long> r2, java.util.stream.Sink<java.lang.Long> r3) {
        /*
            r1 = this;
            java.util.Spliterator$OfLong r1 = adapt((java.util.Spliterator<java.lang.Long>) r2)
            java.util.function.LongConsumer r2 = adapt((java.util.stream.Sink<java.lang.Long>) r3)
        L_0x0008:
            boolean r0 = r3.cancellationRequested()
            if (r0 != 0) goto L_0x0014
            boolean r0 = r1.tryAdvance((java.util.function.LongConsumer) r2)
            if (r0 != 0) goto L_0x0008
        L_0x0014:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.LongPipeline.forEachWithCancel(java.util.Spliterator, java.util.stream.Sink):void");
    }

    public final Node.Builder<Long> makeNodeBuilder(long j, IntFunction<Long[]> intFunction) {
        return Nodes.longBuilder(j);
    }

    public final PrimitiveIterator.OfLong iterator() {
        return Spliterators.iterator(spliterator());
    }

    public final Spliterator.OfLong spliterator() {
        return adapt((Spliterator<Long>) super.spliterator());
    }

    public final DoubleStream asDoubleStream() {
        return new DoublePipeline.StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Long> opWrapSink(int i, Sink<Double> sink) {
                return new Sink.ChainedLong<Double>(sink) {
                    public void accept(long j) {
                        this.downstream.accept((double) j);
                    }
                };
            }
        };
    }

    public final Stream<Long> boxed() {
        return mapToObj(new LongPipeline$$ExternalSyntheticLambda7());
    }

    public final LongStream map(LongUnaryOperator longUnaryOperator) {
        Objects.requireNonNull(longUnaryOperator);
        final LongUnaryOperator longUnaryOperator2 = longUnaryOperator;
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Long> opWrapSink(int i, Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) {
                    public void accept(long j) {
                        this.downstream.accept(longUnaryOperator2.applyAsLong(j));
                    }
                };
            }
        };
    }

    public final <U> Stream<U> mapToObj(LongFunction<? extends U> longFunction) {
        Objects.requireNonNull(longFunction);
        final LongFunction<? extends U> longFunction2 = longFunction;
        return new ReferencePipeline.StatelessOp<Long, U>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Long> opWrapSink(int i, Sink<U> sink) {
                return new Sink.ChainedLong<U>(sink) {
                    public void accept(long j) {
                        this.downstream.accept(longFunction2.apply(j));
                    }
                };
            }
        };
    }

    public final IntStream mapToInt(LongToIntFunction longToIntFunction) {
        Objects.requireNonNull(longToIntFunction);
        final LongToIntFunction longToIntFunction2 = longToIntFunction;
        return new IntPipeline.StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Long> opWrapSink(int i, Sink<Integer> sink) {
                return new Sink.ChainedLong<Integer>(sink) {
                    public void accept(long j) {
                        this.downstream.accept(longToIntFunction2.applyAsInt(j));
                    }
                };
            }
        };
    }

    public final DoubleStream mapToDouble(LongToDoubleFunction longToDoubleFunction) {
        Objects.requireNonNull(longToDoubleFunction);
        final LongToDoubleFunction longToDoubleFunction2 = longToDoubleFunction;
        return new DoublePipeline.StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Long> opWrapSink(int i, Sink<Double> sink) {
                return new Sink.ChainedLong<Double>(sink) {
                    public void accept(long j) {
                        this.downstream.accept(longToDoubleFunction2.applyAsDouble(j));
                    }
                };
            }
        };
    }

    public final LongStream flatMap(LongFunction<? extends LongStream> longFunction) {
        final LongFunction<? extends LongStream> longFunction2 = longFunction;
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            public Sink<Long> opWrapSink(int i, Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) {
                    public void begin(long j) {
                        this.downstream.begin(-1);
                    }

                    public void accept(long j) {
                        LongStream longStream = (LongStream) longFunction2.apply(j);
                        if (longStream != null) {
                            try {
                                longStream.sequential().forEach(new LongPipeline$6$1$$ExternalSyntheticLambda0(this));
                            } catch (Throwable th) {
                                th.addSuppressed(th);
                            }
                        }
                        if (longStream != null) {
                            longStream.close();
                            return;
                        }
                        return;
                        throw th;
                    }

                    /* access modifiers changed from: package-private */
                    /* renamed from: lambda$accept$0$java-util-stream-LongPipeline$6$1  reason: not valid java name */
                    public /* synthetic */ void m3873lambda$accept$0$javautilstreamLongPipeline$6$1(long j) {
                        this.downstream.accept(j);
                    }
                };
            }
        };
    }

    public LongStream unordered() {
        if (!isOrdered()) {
            return this;
        }
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_ORDERED) {
            public Sink<Long> opWrapSink(int i, Sink<Long> sink) {
                return sink;
            }
        };
    }

    public final LongStream filter(LongPredicate longPredicate) {
        Objects.requireNonNull(longPredicate);
        final LongPredicate longPredicate2 = longPredicate;
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SIZED) {
            public Sink<Long> opWrapSink(int i, Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) {
                    public void begin(long j) {
                        this.downstream.begin(-1);
                    }

                    public void accept(long j) {
                        if (longPredicate2.test(j)) {
                            this.downstream.accept(j);
                        }
                    }
                };
            }
        };
    }

    public final LongStream peek(LongConsumer longConsumer) {
        Objects.requireNonNull(longConsumer);
        final LongConsumer longConsumer2 = longConsumer;
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, 0) {
            public Sink<Long> opWrapSink(int i, Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) {
                    public void accept(long j) {
                        longConsumer2.accept(j);
                        this.downstream.accept(j);
                    }
                };
            }
        };
    }

    public final LongStream limit(long j) {
        if (j >= 0) {
            return SliceOps.makeLong(this, 0, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    public final LongStream skip(long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : SliceOps.makeLong(this, j, -1);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    public final LongStream sorted() {
        return SortedOps.makeLong(this);
    }

    public final LongStream distinct() {
        return boxed().distinct().mapToLong(new LongPipeline$$ExternalSyntheticLambda4());
    }

    public void forEach(LongConsumer longConsumer) {
        evaluate(ForEachOps.makeLong(longConsumer, false));
    }

    public void forEachOrdered(LongConsumer longConsumer) {
        evaluate(ForEachOps.makeLong(longConsumer, true));
    }

    public final long sum() {
        return reduce(0, new LongPipeline$$ExternalSyntheticLambda12());
    }

    public final OptionalLong min() {
        return reduce(new LongPipeline$$ExternalSyntheticLambda2());
    }

    public final OptionalLong max() {
        return reduce(new LongPipeline$$ExternalSyntheticLambda6());
    }

    public final OptionalDouble average() {
        long[] jArr = (long[]) collect(new LongPipeline$$ExternalSyntheticLambda9(), new LongPipeline$$ExternalSyntheticLambda10(), new LongPipeline$$ExternalSyntheticLambda11());
        long j = jArr[0];
        if (j > 0) {
            return OptionalDouble.m1747of(((double) jArr[1]) / ((double) j));
        }
        return OptionalDouble.empty();
    }

    static /* synthetic */ void lambda$average$2(long[] jArr, long j) {
        jArr[0] = jArr[0] + 1;
        jArr[1] = jArr[1] + j;
    }

    static /* synthetic */ void lambda$average$3(long[] jArr, long[] jArr2) {
        jArr[0] = jArr[0] + jArr2[0];
        jArr[1] = jArr[1] + jArr2[1];
    }

    public final long count() {
        return map(new LongPipeline$$ExternalSyntheticLambda0()).sum();
    }

    public final LongSummaryStatistics summaryStatistics() {
        return (LongSummaryStatistics) collect(new Collectors$$ExternalSyntheticLambda55(), new LongPipeline$$ExternalSyntheticLambda13(), new LongPipeline$$ExternalSyntheticLambda1());
    }

    public final long reduce(long j, LongBinaryOperator longBinaryOperator) {
        return ((Long) evaluate(ReduceOps.makeLong(j, longBinaryOperator))).longValue();
    }

    public final OptionalLong reduce(LongBinaryOperator longBinaryOperator) {
        return (OptionalLong) evaluate(ReduceOps.makeLong(longBinaryOperator));
    }

    public final <R> R collect(Supplier<R> supplier, ObjLongConsumer<R> objLongConsumer, BiConsumer<R, R> biConsumer) {
        return evaluate(ReduceOps.makeLong(supplier, objLongConsumer, new LongPipeline$$ExternalSyntheticLambda5(biConsumer)));
    }

    public final boolean anyMatch(LongPredicate longPredicate) {
        return ((Boolean) evaluate(MatchOps.makeLong(longPredicate, MatchOps.MatchKind.ANY))).booleanValue();
    }

    public final boolean allMatch(LongPredicate longPredicate) {
        return ((Boolean) evaluate(MatchOps.makeLong(longPredicate, MatchOps.MatchKind.ALL))).booleanValue();
    }

    public final boolean noneMatch(LongPredicate longPredicate) {
        return ((Boolean) evaluate(MatchOps.makeLong(longPredicate, MatchOps.MatchKind.NONE))).booleanValue();
    }

    public final OptionalLong findFirst() {
        return (OptionalLong) evaluate(FindOps.makeLong(true));
    }

    public final OptionalLong findAny() {
        return (OptionalLong) evaluate(FindOps.makeLong(false));
    }

    static /* synthetic */ Long[] lambda$toArray$6(int i) {
        return new Long[i];
    }

    public final long[] toArray() {
        return (long[]) Nodes.flattenLong((Node.OfLong) evaluateToArrayNode(new LongPipeline$$ExternalSyntheticLambda8())).asPrimitiveArray();
    }

    public static class Head<E_IN> extends LongPipeline<E_IN> {
        public /* bridge */ /* synthetic */ LongStream parallel() {
            return (LongStream) LongPipeline.super.parallel();
        }

        public /* bridge */ /* synthetic */ LongStream sequential() {
            return (LongStream) LongPipeline.super.sequential();
        }

        public Head(Supplier<? extends Spliterator<Long>> supplier, int i, boolean z) {
            super(supplier, i, z);
        }

        public Head(Spliterator<Long> spliterator, int i, boolean z) {
            super(spliterator, i, z);
        }

        public final boolean opIsStateful() {
            throw new UnsupportedOperationException();
        }

        public final Sink<E_IN> opWrapSink(int i, Sink<Long> sink) {
            throw new UnsupportedOperationException();
        }

        public void forEach(LongConsumer longConsumer) {
            if (!isParallel()) {
                LongPipeline.adapt((Spliterator<Long>) sourceStageSpliterator()).forEachRemaining(longConsumer);
            } else {
                LongPipeline.super.forEach(longConsumer);
            }
        }

        public void forEachOrdered(LongConsumer longConsumer) {
            if (!isParallel()) {
                LongPipeline.adapt((Spliterator<Long>) sourceStageSpliterator()).forEachRemaining(longConsumer);
            } else {
                LongPipeline.super.forEachOrdered(longConsumer);
            }
        }
    }

    public static abstract class StatelessOp<E_IN> extends LongPipeline<E_IN> {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        public final boolean opIsStateful() {
            return false;
        }

        static {
            Class<LongPipeline> cls = LongPipeline.class;
        }

        public /* bridge */ /* synthetic */ LongStream parallel() {
            return (LongStream) LongPipeline.super.parallel();
        }

        public /* bridge */ /* synthetic */ LongStream sequential() {
            return (LongStream) LongPipeline.super.sequential();
        }

        public StatelessOp(AbstractPipeline<?, E_IN, ?> abstractPipeline, StreamShape streamShape, int i) {
            super(abstractPipeline, i);
        }
    }

    public static abstract class StatefulOp<E_IN> extends LongPipeline<E_IN> {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        public abstract <P_IN> Node<Long> opEvaluateParallel(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<Long[]> intFunction);

        public final boolean opIsStateful() {
            return true;
        }

        static {
            Class<LongPipeline> cls = LongPipeline.class;
        }

        public /* bridge */ /* synthetic */ LongStream parallel() {
            return (LongStream) LongPipeline.super.parallel();
        }

        public /* bridge */ /* synthetic */ LongStream sequential() {
            return (LongStream) LongPipeline.super.sequential();
        }

        public StatefulOp(AbstractPipeline<?, E_IN, ?> abstractPipeline, StreamShape streamShape, int i) {
            super(abstractPipeline, i);
        }
    }
}
