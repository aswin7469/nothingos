package java.util.stream;

import java.util.IntSummaryStatistics;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.DoublePipeline;
import java.util.stream.LongPipeline;
import java.util.stream.MatchOps;
import java.util.stream.Node;
import java.util.stream.ReferencePipeline;
import java.util.stream.Sink;
import java.util.stream.StreamSpliterators;

public abstract class IntPipeline<E_IN> extends AbstractPipeline<E_IN, Integer, IntStream> implements IntStream {
    static /* synthetic */ long[] lambda$average$2() {
        return new long[2];
    }

    static /* synthetic */ long lambda$count$1(int i) {
        return 1;
    }

    public /* bridge */ /* synthetic */ IntStream parallel() {
        return (IntStream) super.parallel();
    }

    public /* bridge */ /* synthetic */ IntStream sequential() {
        return (IntStream) super.sequential();
    }

    IntPipeline(Supplier<? extends Spliterator<Integer>> supplier, int i, boolean z) {
        super((Supplier<? extends Spliterator<?>>) supplier, i, z);
    }

    IntPipeline(Spliterator<Integer> spliterator, int i, boolean z) {
        super((Spliterator<?>) spliterator, i, z);
    }

    IntPipeline(AbstractPipeline<?, E_IN, ?> abstractPipeline, int i) {
        super(abstractPipeline, i);
    }

    private static IntConsumer adapt(Sink<Integer> sink) {
        if (sink instanceof IntConsumer) {
            return (IntConsumer) sink;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using IntStream.adapt(Sink<Integer> s)");
        }
        Objects.requireNonNull(sink);
        return new IntPipeline$$ExternalSyntheticLambda6(sink);
    }

    /* access modifiers changed from: private */
    public static Spliterator.OfInt adapt(Spliterator<Integer> spliterator) {
        if (spliterator instanceof Spliterator.OfInt) {
            return (Spliterator.OfInt) spliterator;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using IntStream.adapt(Spliterator<Integer> s)");
        }
        throw new UnsupportedOperationException("IntStream.adapt(Spliterator<Integer> s)");
    }

    public final StreamShape getOutputShape() {
        return StreamShape.INT_VALUE;
    }

    public final <P_IN> Node<Integer> evaluateToNode(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator, boolean z, IntFunction<Integer[]> intFunction) {
        return Nodes.collectInt(pipelineHelper, spliterator, z);
    }

    public final <P_IN> Spliterator<Integer> wrap(PipelineHelper<Integer> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z) {
        return new StreamSpliterators.IntWrappingSpliterator(pipelineHelper, supplier, z);
    }

    public final Spliterator.OfInt lazySpliterator(Supplier<? extends Spliterator<Integer>> supplier) {
        return new StreamSpliterators.DelegatingSpliterator.OfInt(supplier);
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x0008 A[LOOP:0: B:1:0x0008->B:4:0x0012, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void forEachWithCancel(java.util.Spliterator<java.lang.Integer> r2, java.util.stream.Sink<java.lang.Integer> r3) {
        /*
            r1 = this;
            java.util.Spliterator$OfInt r1 = adapt((java.util.Spliterator<java.lang.Integer>) r2)
            java.util.function.IntConsumer r2 = adapt((java.util.stream.Sink<java.lang.Integer>) r3)
        L_0x0008:
            boolean r0 = r3.cancellationRequested()
            if (r0 != 0) goto L_0x0014
            boolean r0 = r1.tryAdvance((java.util.function.IntConsumer) r2)
            if (r0 != 0) goto L_0x0008
        L_0x0014:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.IntPipeline.forEachWithCancel(java.util.Spliterator, java.util.stream.Sink):void");
    }

    public final Node.Builder<Integer> makeNodeBuilder(long j, IntFunction<Integer[]> intFunction) {
        return Nodes.intBuilder(j);
    }

    public final PrimitiveIterator.OfInt iterator() {
        return Spliterators.iterator(spliterator());
    }

    public final Spliterator.OfInt spliterator() {
        return adapt((Spliterator<Integer>) super.spliterator());
    }

    public final LongStream asLongStream() {
        return new LongPipeline.StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Integer> opWrapSink(int i, Sink<Long> sink) {
                return new Sink.ChainedInt<Long>(sink) {
                    public void accept(int i) {
                        this.downstream.accept((long) i);
                    }
                };
            }
        };
    }

    public final DoubleStream asDoubleStream() {
        return new DoublePipeline.StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Integer> opWrapSink(int i, Sink<Double> sink) {
                return new Sink.ChainedInt<Double>(sink) {
                    public void accept(int i) {
                        this.downstream.accept((double) i);
                    }
                };
            }
        };
    }

    public final Stream<Integer> boxed() {
        return mapToObj(new IntPipeline$$ExternalSyntheticLambda13());
    }

    public final IntStream map(IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        final IntUnaryOperator intUnaryOperator2 = intUnaryOperator;
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Integer> opWrapSink(int i, Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) {
                    public void accept(int i) {
                        this.downstream.accept(intUnaryOperator2.applyAsInt(i));
                    }
                };
            }
        };
    }

    public final <U> Stream<U> mapToObj(IntFunction<? extends U> intFunction) {
        Objects.requireNonNull(intFunction);
        final IntFunction<? extends U> intFunction2 = intFunction;
        return new ReferencePipeline.StatelessOp<Integer, U>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Integer> opWrapSink(int i, Sink<U> sink) {
                return new Sink.ChainedInt<U>(sink) {
                    public void accept(int i) {
                        this.downstream.accept(intFunction2.apply(i));
                    }
                };
            }
        };
    }

    public final LongStream mapToLong(IntToLongFunction intToLongFunction) {
        Objects.requireNonNull(intToLongFunction);
        final IntToLongFunction intToLongFunction2 = intToLongFunction;
        return new LongPipeline.StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Integer> opWrapSink(int i, Sink<Long> sink) {
                return new Sink.ChainedInt<Long>(sink) {
                    public void accept(int i) {
                        this.downstream.accept(intToLongFunction2.applyAsLong(i));
                    }
                };
            }
        };
    }

    public final DoubleStream mapToDouble(IntToDoubleFunction intToDoubleFunction) {
        Objects.requireNonNull(intToDoubleFunction);
        final IntToDoubleFunction intToDoubleFunction2 = intToDoubleFunction;
        return new DoublePipeline.StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Integer> opWrapSink(int i, Sink<Double> sink) {
                return new Sink.ChainedInt<Double>(sink) {
                    public void accept(int i) {
                        this.downstream.accept(intToDoubleFunction2.applyAsDouble(i));
                    }
                };
            }
        };
    }

    public final IntStream flatMap(IntFunction<? extends IntStream> intFunction) {
        final IntFunction<? extends IntStream> intFunction2 = intFunction;
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            public Sink<Integer> opWrapSink(int i, Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) {
                    public void begin(long j) {
                        this.downstream.begin(-1);
                    }

                    public void accept(int i) {
                        IntStream intStream = (IntStream) intFunction2.apply(i);
                        if (intStream != null) {
                            try {
                                intStream.sequential().forEach(new IntPipeline$7$1$$ExternalSyntheticLambda0(this));
                            } catch (Throwable th) {
                                th.addSuppressed(th);
                            }
                        }
                        if (intStream != null) {
                            intStream.close();
                            return;
                        }
                        return;
                        throw th;
                    }

                    /* access modifiers changed from: package-private */
                    /* renamed from: lambda$accept$0$java-util-stream-IntPipeline$7$1  reason: not valid java name */
                    public /* synthetic */ void m3889lambda$accept$0$javautilstreamIntPipeline$7$1(int i) {
                        this.downstream.accept(i);
                    }
                };
            }
        };
    }

    public IntStream unordered() {
        if (!isOrdered()) {
            return this;
        }
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_ORDERED) {
            public Sink<Integer> opWrapSink(int i, Sink<Integer> sink) {
                return sink;
            }
        };
    }

    public final IntStream filter(IntPredicate intPredicate) {
        Objects.requireNonNull(intPredicate);
        final IntPredicate intPredicate2 = intPredicate;
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SIZED) {
            public Sink<Integer> opWrapSink(int i, Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) {
                    public void begin(long j) {
                        this.downstream.begin(-1);
                    }

                    public void accept(int i) {
                        if (intPredicate2.test(i)) {
                            this.downstream.accept(i);
                        }
                    }
                };
            }
        };
    }

    public final IntStream peek(IntConsumer intConsumer) {
        Objects.requireNonNull(intConsumer);
        final IntConsumer intConsumer2 = intConsumer;
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, 0) {
            public Sink<Integer> opWrapSink(int i, Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) {
                    public void accept(int i) {
                        intConsumer2.accept(i);
                        this.downstream.accept(i);
                    }
                };
            }
        };
    }

    public final IntStream limit(long j) {
        if (j >= 0) {
            return SliceOps.makeInt(this, 0, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    public final IntStream skip(long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : SliceOps.makeInt(this, j, -1);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    public final IntStream sorted() {
        return SortedOps.makeInt(this);
    }

    public final IntStream distinct() {
        return boxed().distinct().mapToInt(new IntPipeline$$ExternalSyntheticLambda0());
    }

    public void forEach(IntConsumer intConsumer) {
        evaluate(ForEachOps.makeInt(intConsumer, false));
    }

    public void forEachOrdered(IntConsumer intConsumer) {
        evaluate(ForEachOps.makeInt(intConsumer, true));
    }

    public final int sum() {
        return reduce(0, new IntPipeline$$ExternalSyntheticLambda5());
    }

    public final OptionalInt min() {
        return reduce(new IntPipeline$$ExternalSyntheticLambda4());
    }

    public final OptionalInt max() {
        return reduce(new IntPipeline$$ExternalSyntheticLambda1());
    }

    public final long count() {
        return mapToLong(new IntPipeline$$ExternalSyntheticLambda2()).sum();
    }

    public final OptionalDouble average() {
        long[] jArr = (long[]) collect(new IntPipeline$$ExternalSyntheticLambda9(), new IntPipeline$$ExternalSyntheticLambda10(), new IntPipeline$$ExternalSyntheticLambda11());
        long j = jArr[0];
        if (j > 0) {
            return OptionalDouble.m1753of(((double) jArr[1]) / ((double) j));
        }
        return OptionalDouble.empty();
    }

    static /* synthetic */ void lambda$average$3(long[] jArr, int i) {
        jArr[0] = jArr[0] + 1;
        jArr[1] = jArr[1] + ((long) i);
    }

    static /* synthetic */ void lambda$average$4(long[] jArr, long[] jArr2) {
        jArr[0] = jArr[0] + jArr2[0];
        jArr[1] = jArr[1] + jArr2[1];
    }

    public final IntSummaryStatistics summaryStatistics() {
        return (IntSummaryStatistics) collect(new Collectors$$ExternalSyntheticLambda53(), new IntPipeline$$ExternalSyntheticLambda7(), new IntPipeline$$ExternalSyntheticLambda8());
    }

    public final int reduce(int i, IntBinaryOperator intBinaryOperator) {
        return ((Integer) evaluate(ReduceOps.makeInt(i, intBinaryOperator))).intValue();
    }

    public final OptionalInt reduce(IntBinaryOperator intBinaryOperator) {
        return (OptionalInt) evaluate(ReduceOps.makeInt(intBinaryOperator));
    }

    public final <R> R collect(Supplier<R> supplier, ObjIntConsumer<R> objIntConsumer, BiConsumer<R, R> biConsumer) {
        return evaluate(ReduceOps.makeInt(supplier, objIntConsumer, new IntPipeline$$ExternalSyntheticLambda3(biConsumer)));
    }

    public final boolean anyMatch(IntPredicate intPredicate) {
        return ((Boolean) evaluate(MatchOps.makeInt(intPredicate, MatchOps.MatchKind.ANY))).booleanValue();
    }

    public final boolean allMatch(IntPredicate intPredicate) {
        return ((Boolean) evaluate(MatchOps.makeInt(intPredicate, MatchOps.MatchKind.ALL))).booleanValue();
    }

    public final boolean noneMatch(IntPredicate intPredicate) {
        return ((Boolean) evaluate(MatchOps.makeInt(intPredicate, MatchOps.MatchKind.NONE))).booleanValue();
    }

    public final OptionalInt findFirst() {
        return (OptionalInt) evaluate(FindOps.makeInt(true));
    }

    public final OptionalInt findAny() {
        return (OptionalInt) evaluate(FindOps.makeInt(false));
    }

    static /* synthetic */ Integer[] lambda$toArray$6(int i) {
        return new Integer[i];
    }

    public final int[] toArray() {
        return (int[]) Nodes.flattenInt((Node.OfInt) evaluateToArrayNode(new IntPipeline$$ExternalSyntheticLambda12())).asPrimitiveArray();
    }

    public static class Head<E_IN> extends IntPipeline<E_IN> {
        public /* bridge */ /* synthetic */ IntStream parallel() {
            return (IntStream) IntPipeline.super.parallel();
        }

        public /* bridge */ /* synthetic */ IntStream sequential() {
            return (IntStream) IntPipeline.super.sequential();
        }

        public Head(Supplier<? extends Spliterator<Integer>> supplier, int i, boolean z) {
            super(supplier, i, z);
        }

        public Head(Spliterator<Integer> spliterator, int i, boolean z) {
            super(spliterator, i, z);
        }

        public final boolean opIsStateful() {
            throw new UnsupportedOperationException();
        }

        public final Sink<E_IN> opWrapSink(int i, Sink<Integer> sink) {
            throw new UnsupportedOperationException();
        }

        public void forEach(IntConsumer intConsumer) {
            if (!isParallel()) {
                IntPipeline.adapt((Spliterator<Integer>) sourceStageSpliterator()).forEachRemaining(intConsumer);
            } else {
                IntPipeline.super.forEach(intConsumer);
            }
        }

        public void forEachOrdered(IntConsumer intConsumer) {
            if (!isParallel()) {
                IntPipeline.adapt((Spliterator<Integer>) sourceStageSpliterator()).forEachRemaining(intConsumer);
            } else {
                IntPipeline.super.forEachOrdered(intConsumer);
            }
        }
    }

    public static abstract class StatelessOp<E_IN> extends IntPipeline<E_IN> {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        public final boolean opIsStateful() {
            return false;
        }

        static {
            Class<IntPipeline> cls = IntPipeline.class;
        }

        public /* bridge */ /* synthetic */ IntStream parallel() {
            return (IntStream) IntPipeline.super.parallel();
        }

        public /* bridge */ /* synthetic */ IntStream sequential() {
            return (IntStream) IntPipeline.super.sequential();
        }

        public StatelessOp(AbstractPipeline<?, E_IN, ?> abstractPipeline, StreamShape streamShape, int i) {
            super(abstractPipeline, i);
        }
    }

    public static abstract class StatefulOp<E_IN> extends IntPipeline<E_IN> {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        public abstract <P_IN> Node<Integer> opEvaluateParallel(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<Integer[]> intFunction);

        public final boolean opIsStateful() {
            return true;
        }

        static {
            Class<IntPipeline> cls = IntPipeline.class;
        }

        public /* bridge */ /* synthetic */ IntStream parallel() {
            return (IntStream) IntPipeline.super.parallel();
        }

        public /* bridge */ /* synthetic */ IntStream sequential() {
            return (IntStream) IntPipeline.super.sequential();
        }

        public StatefulOp(AbstractPipeline<?, E_IN, ?> abstractPipeline, StreamShape streamShape, int i) {
            super(abstractPipeline, i);
        }
    }
}
