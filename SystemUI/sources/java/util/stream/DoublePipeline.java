package java.util.stream;

import java.util.DoubleSummaryStatistics;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntFunction;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;
import java.util.stream.IntPipeline;
import java.util.stream.LongPipeline;
import java.util.stream.MatchOps;
import java.util.stream.Node;
import java.util.stream.ReferencePipeline;
import java.util.stream.Sink;
import java.util.stream.StreamSpliterators;

public abstract class DoublePipeline<E_IN> extends AbstractPipeline<E_IN, Double, DoubleStream> implements DoubleStream {
    static /* synthetic */ double[] lambda$average$4() {
        return new double[4];
    }

    static /* synthetic */ long lambda$count$7(double d) {
        return 1;
    }

    static /* synthetic */ double[] lambda$sum$1() {
        return new double[3];
    }

    public /* bridge */ /* synthetic */ DoubleStream parallel() {
        return (DoubleStream) super.parallel();
    }

    public /* bridge */ /* synthetic */ DoubleStream sequential() {
        return (DoubleStream) super.sequential();
    }

    DoublePipeline(Supplier<? extends Spliterator<Double>> supplier, int i, boolean z) {
        super((Supplier<? extends Spliterator<?>>) supplier, i, z);
    }

    DoublePipeline(Spliterator<Double> spliterator, int i, boolean z) {
        super((Spliterator<?>) spliterator, i, z);
    }

    DoublePipeline(AbstractPipeline<?, E_IN, ?> abstractPipeline, int i) {
        super(abstractPipeline, i);
    }

    private static DoubleConsumer adapt(Sink<Double> sink) {
        if (sink instanceof DoubleConsumer) {
            return (DoubleConsumer) sink;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using DoubleStream.adapt(Sink<Double> s)");
        }
        Objects.requireNonNull(sink);
        return new DoublePipeline$$ExternalSyntheticLambda12(sink);
    }

    /* access modifiers changed from: private */
    public static Spliterator.OfDouble adapt(Spliterator<Double> spliterator) {
        if (spliterator instanceof Spliterator.OfDouble) {
            return (Spliterator.OfDouble) spliterator;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using DoubleStream.adapt(Spliterator<Double> s)");
        }
        throw new UnsupportedOperationException("DoubleStream.adapt(Spliterator<Double> s)");
    }

    public final StreamShape getOutputShape() {
        return StreamShape.DOUBLE_VALUE;
    }

    public final <P_IN> Node<Double> evaluateToNode(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator, boolean z, IntFunction<Double[]> intFunction) {
        return Nodes.collectDouble(pipelineHelper, spliterator, z);
    }

    public final <P_IN> Spliterator<Double> wrap(PipelineHelper<Double> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z) {
        return new StreamSpliterators.DoubleWrappingSpliterator(pipelineHelper, supplier, z);
    }

    public final Spliterator.OfDouble lazySpliterator(Supplier<? extends Spliterator<Double>> supplier) {
        return new StreamSpliterators.DelegatingSpliterator.OfDouble(supplier);
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x0008 A[LOOP:0: B:1:0x0008->B:4:0x0012, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void forEachWithCancel(java.util.Spliterator<java.lang.Double> r2, java.util.stream.Sink<java.lang.Double> r3) {
        /*
            r1 = this;
            java.util.Spliterator$OfDouble r1 = adapt((java.util.Spliterator<java.lang.Double>) r2)
            java.util.function.DoubleConsumer r2 = adapt((java.util.stream.Sink<java.lang.Double>) r3)
        L_0x0008:
            boolean r0 = r3.cancellationRequested()
            if (r0 != 0) goto L_0x0014
            boolean r0 = r1.tryAdvance((java.util.function.DoubleConsumer) r2)
            if (r0 != 0) goto L_0x0008
        L_0x0014:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.DoublePipeline.forEachWithCancel(java.util.Spliterator, java.util.stream.Sink):void");
    }

    public final Node.Builder<Double> makeNodeBuilder(long j, IntFunction<Double[]> intFunction) {
        return Nodes.doubleBuilder(j);
    }

    public final PrimitiveIterator.OfDouble iterator() {
        return Spliterators.iterator(spliterator());
    }

    public final Spliterator.OfDouble spliterator() {
        return adapt((Spliterator<Double>) super.spliterator());
    }

    public final Stream<Double> boxed() {
        return mapToObj(new DoublePipeline$$ExternalSyntheticLambda13());
    }

    public final DoubleStream map(DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        final DoubleUnaryOperator doubleUnaryOperator2 = doubleUnaryOperator;
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Double> opWrapSink(int i, Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) {
                    public void accept(double d) {
                        this.downstream.accept(doubleUnaryOperator2.applyAsDouble(d));
                    }
                };
            }
        };
    }

    public final <U> Stream<U> mapToObj(DoubleFunction<? extends U> doubleFunction) {
        Objects.requireNonNull(doubleFunction);
        final DoubleFunction<? extends U> doubleFunction2 = doubleFunction;
        return new ReferencePipeline.StatelessOp<Double, U>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Double> opWrapSink(int i, Sink<U> sink) {
                return new Sink.ChainedDouble<U>(sink) {
                    public void accept(double d) {
                        this.downstream.accept(doubleFunction2.apply(d));
                    }
                };
            }
        };
    }

    public final IntStream mapToInt(DoubleToIntFunction doubleToIntFunction) {
        Objects.requireNonNull(doubleToIntFunction);
        final DoubleToIntFunction doubleToIntFunction2 = doubleToIntFunction;
        return new IntPipeline.StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Double> opWrapSink(int i, Sink<Integer> sink) {
                return new Sink.ChainedDouble<Integer>(sink) {
                    public void accept(double d) {
                        this.downstream.accept(doubleToIntFunction2.applyAsInt(d));
                    }
                };
            }
        };
    }

    public final LongStream mapToLong(DoubleToLongFunction doubleToLongFunction) {
        Objects.requireNonNull(doubleToLongFunction);
        final DoubleToLongFunction doubleToLongFunction2 = doubleToLongFunction;
        return new LongPipeline.StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<Double> opWrapSink(int i, Sink<Long> sink) {
                return new Sink.ChainedDouble<Long>(sink) {
                    public void accept(double d) {
                        this.downstream.accept(doubleToLongFunction2.applyAsLong(d));
                    }
                };
            }
        };
    }

    public final DoubleStream flatMap(DoubleFunction<? extends DoubleStream> doubleFunction) {
        final DoubleFunction<? extends DoubleStream> doubleFunction2 = doubleFunction;
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            public Sink<Double> opWrapSink(int i, Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) {
                    public void begin(long j) {
                        this.downstream.begin(-1);
                    }

                    public void accept(double d) {
                        DoubleStream doubleStream = (DoubleStream) doubleFunction2.apply(d);
                        if (doubleStream != null) {
                            try {
                                doubleStream.sequential().forEach(new DoublePipeline$5$1$$ExternalSyntheticLambda0(this));
                            } catch (Throwable th) {
                                th.addSuppressed(th);
                            }
                        }
                        if (doubleStream != null) {
                            doubleStream.close();
                            return;
                        }
                        return;
                        throw th;
                    }

                    /* access modifiers changed from: package-private */
                    /* renamed from: lambda$accept$0$java-util-stream-DoublePipeline$5$1  reason: not valid java name */
                    public /* synthetic */ void m3887lambda$accept$0$javautilstreamDoublePipeline$5$1(double d) {
                        this.downstream.accept(d);
                    }
                };
            }
        };
    }

    public DoubleStream unordered() {
        if (!isOrdered()) {
            return this;
        }
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_ORDERED) {
            public Sink<Double> opWrapSink(int i, Sink<Double> sink) {
                return sink;
            }
        };
    }

    public final DoubleStream filter(DoublePredicate doublePredicate) {
        Objects.requireNonNull(doublePredicate);
        final DoublePredicate doublePredicate2 = doublePredicate;
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SIZED) {
            public Sink<Double> opWrapSink(int i, Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) {
                    public void begin(long j) {
                        this.downstream.begin(-1);
                    }

                    public void accept(double d) {
                        if (doublePredicate2.test(d)) {
                            this.downstream.accept(d);
                        }
                    }
                };
            }
        };
    }

    public final DoubleStream peek(DoubleConsumer doubleConsumer) {
        Objects.requireNonNull(doubleConsumer);
        final DoubleConsumer doubleConsumer2 = doubleConsumer;
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, 0) {
            public Sink<Double> opWrapSink(int i, Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) {
                    public void accept(double d) {
                        doubleConsumer2.accept(d);
                        this.downstream.accept(d);
                    }
                };
            }
        };
    }

    public final DoubleStream limit(long j) {
        if (j >= 0) {
            return SliceOps.makeDouble(this, 0, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    public final DoubleStream skip(long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : SliceOps.makeDouble(this, j, -1);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    public final DoubleStream sorted() {
        return SortedOps.makeDouble(this);
    }

    public final DoubleStream distinct() {
        return boxed().distinct().mapToDouble(new DoublePipeline$$ExternalSyntheticLambda10());
    }

    public void forEach(DoubleConsumer doubleConsumer) {
        evaluate(ForEachOps.makeDouble(doubleConsumer, false));
    }

    public void forEachOrdered(DoubleConsumer doubleConsumer) {
        evaluate(ForEachOps.makeDouble(doubleConsumer, true));
    }

    public final double sum() {
        return Collectors.computeFinalSum((double[]) collect(new DoublePipeline$$ExternalSyntheticLambda1(), new DoublePipeline$$ExternalSyntheticLambda2(), new DoublePipeline$$ExternalSyntheticLambda3()));
    }

    static /* synthetic */ void lambda$sum$2(double[] dArr, double d) {
        Collectors.sumWithCompensation(dArr, d);
        dArr[2] = dArr[2] + d;
    }

    static /* synthetic */ void lambda$sum$3(double[] dArr, double[] dArr2) {
        Collectors.sumWithCompensation(dArr, dArr2[0]);
        Collectors.sumWithCompensation(dArr, dArr2[1]);
        dArr[2] = dArr[2] + dArr2[2];
    }

    public final OptionalDouble min() {
        return reduce(new DoublePipeline$$ExternalSyntheticLambda9());
    }

    public final OptionalDouble max() {
        return reduce(new DoublePipeline$$ExternalSyntheticLambda11());
    }

    public final OptionalDouble average() {
        double[] dArr = (double[]) collect(new DoublePipeline$$ExternalSyntheticLambda5(), new DoublePipeline$$ExternalSyntheticLambda6(), new DoublePipeline$$ExternalSyntheticLambda7());
        if (dArr[2] > 0.0d) {
            return OptionalDouble.m1753of(Collectors.computeFinalSum(dArr) / dArr[2]);
        }
        return OptionalDouble.empty();
    }

    static /* synthetic */ void lambda$average$5(double[] dArr, double d) {
        dArr[2] = dArr[2] + 1.0d;
        Collectors.sumWithCompensation(dArr, d);
        dArr[3] = dArr[3] + d;
    }

    static /* synthetic */ void lambda$average$6(double[] dArr, double[] dArr2) {
        Collectors.sumWithCompensation(dArr, dArr2[0]);
        Collectors.sumWithCompensation(dArr, dArr2[1]);
        dArr[2] = dArr[2] + dArr2[2];
        dArr[3] = dArr[3] + dArr2[3];
    }

    public final long count() {
        return mapToLong(new DoublePipeline$$ExternalSyntheticLambda4()).sum();
    }

    public final DoubleSummaryStatistics summaryStatistics() {
        return (DoubleSummaryStatistics) collect(new Collectors$$ExternalSyntheticLambda22(), new DoublePipeline$$ExternalSyntheticLambda14(), new DoublePipeline$$ExternalSyntheticLambda15());
    }

    public final double reduce(double d, DoubleBinaryOperator doubleBinaryOperator) {
        return ((Double) evaluate(ReduceOps.makeDouble(d, doubleBinaryOperator))).doubleValue();
    }

    public final OptionalDouble reduce(DoubleBinaryOperator doubleBinaryOperator) {
        return (OptionalDouble) evaluate(ReduceOps.makeDouble(doubleBinaryOperator));
    }

    public final <R> R collect(Supplier<R> supplier, ObjDoubleConsumer<R> objDoubleConsumer, BiConsumer<R, R> biConsumer) {
        return evaluate(ReduceOps.makeDouble(supplier, objDoubleConsumer, new DoublePipeline$$ExternalSyntheticLambda8(biConsumer)));
    }

    public final boolean anyMatch(DoublePredicate doublePredicate) {
        return ((Boolean) evaluate(MatchOps.makeDouble(doublePredicate, MatchOps.MatchKind.ANY))).booleanValue();
    }

    public final boolean allMatch(DoublePredicate doublePredicate) {
        return ((Boolean) evaluate(MatchOps.makeDouble(doublePredicate, MatchOps.MatchKind.ALL))).booleanValue();
    }

    public final boolean noneMatch(DoublePredicate doublePredicate) {
        return ((Boolean) evaluate(MatchOps.makeDouble(doublePredicate, MatchOps.MatchKind.NONE))).booleanValue();
    }

    public final OptionalDouble findFirst() {
        return (OptionalDouble) evaluate(FindOps.makeDouble(true));
    }

    public final OptionalDouble findAny() {
        return (OptionalDouble) evaluate(FindOps.makeDouble(false));
    }

    static /* synthetic */ Double[] lambda$toArray$9(int i) {
        return new Double[i];
    }

    public final double[] toArray() {
        return (double[]) Nodes.flattenDouble((Node.OfDouble) evaluateToArrayNode(new DoublePipeline$$ExternalSyntheticLambda0())).asPrimitiveArray();
    }

    public static class Head<E_IN> extends DoublePipeline<E_IN> {
        public /* bridge */ /* synthetic */ DoubleStream parallel() {
            return (DoubleStream) DoublePipeline.super.parallel();
        }

        public /* bridge */ /* synthetic */ DoubleStream sequential() {
            return (DoubleStream) DoublePipeline.super.sequential();
        }

        public Head(Supplier<? extends Spliterator<Double>> supplier, int i, boolean z) {
            super(supplier, i, z);
        }

        public Head(Spliterator<Double> spliterator, int i, boolean z) {
            super(spliterator, i, z);
        }

        public final boolean opIsStateful() {
            throw new UnsupportedOperationException();
        }

        public final Sink<E_IN> opWrapSink(int i, Sink<Double> sink) {
            throw new UnsupportedOperationException();
        }

        public void forEach(DoubleConsumer doubleConsumer) {
            if (!isParallel()) {
                DoublePipeline.adapt((Spliterator<Double>) sourceStageSpliterator()).forEachRemaining(doubleConsumer);
            } else {
                DoublePipeline.super.forEach(doubleConsumer);
            }
        }

        public void forEachOrdered(DoubleConsumer doubleConsumer) {
            if (!isParallel()) {
                DoublePipeline.adapt((Spliterator<Double>) sourceStageSpliterator()).forEachRemaining(doubleConsumer);
            } else {
                DoublePipeline.super.forEachOrdered(doubleConsumer);
            }
        }
    }

    public static abstract class StatelessOp<E_IN> extends DoublePipeline<E_IN> {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        public final boolean opIsStateful() {
            return false;
        }

        static {
            Class<DoublePipeline> cls = DoublePipeline.class;
        }

        public /* bridge */ /* synthetic */ DoubleStream parallel() {
            return (DoubleStream) DoublePipeline.super.parallel();
        }

        public /* bridge */ /* synthetic */ DoubleStream sequential() {
            return (DoubleStream) DoublePipeline.super.sequential();
        }

        public StatelessOp(AbstractPipeline<?, E_IN, ?> abstractPipeline, StreamShape streamShape, int i) {
            super(abstractPipeline, i);
        }
    }

    public static abstract class StatefulOp<E_IN> extends DoublePipeline<E_IN> {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        public abstract <P_IN> Node<Double> opEvaluateParallel(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<Double[]> intFunction);

        public final boolean opIsStateful() {
            return true;
        }

        static {
            Class<DoublePipeline> cls = DoublePipeline.class;
        }

        public /* bridge */ /* synthetic */ DoubleStream parallel() {
            return (DoubleStream) DoublePipeline.super.parallel();
        }

        public /* bridge */ /* synthetic */ DoubleStream sequential() {
            return (DoubleStream) DoublePipeline.super.sequential();
        }

        public StatefulOp(AbstractPipeline<?, E_IN, ?> abstractPipeline, StreamShape streamShape, int i) {
            super(abstractPipeline, i);
        }
    }
}
