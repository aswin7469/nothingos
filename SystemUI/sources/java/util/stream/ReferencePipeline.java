package java.util.stream;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.DoublePipeline;
import java.util.stream.IntPipeline;
import java.util.stream.LongPipeline;
import java.util.stream.Node;
import java.util.stream.Sink;
import java.util.stream.StreamSpliterators;

public abstract class ReferencePipeline<P_IN, P_OUT> extends AbstractPipeline<P_IN, P_OUT, Stream<P_OUT>> implements Stream<P_OUT> {
    static /* synthetic */ long lambda$count$2(Object obj) {
        return 1;
    }

    ReferencePipeline(Supplier<? extends Spliterator<?>> supplier, int i, boolean z) {
        super(supplier, i, z);
    }

    ReferencePipeline(Spliterator<?> spliterator, int i, boolean z) {
        super(spliterator, i, z);
    }

    ReferencePipeline(AbstractPipeline<?, P_IN, ?> abstractPipeline, int i) {
        super(abstractPipeline, i);
    }

    public final StreamShape getOutputShape() {
        return StreamShape.REFERENCE;
    }

    public final <P_IN> Node<P_OUT> evaluateToNode(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator, boolean z, IntFunction<P_OUT[]> intFunction) {
        return Nodes.collect(pipelineHelper, spliterator, z, intFunction);
    }

    public final <P_IN> Spliterator<P_OUT> wrap(PipelineHelper<P_OUT> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z) {
        return new StreamSpliterators.WrappingSpliterator(pipelineHelper, supplier, z);
    }

    public final Spliterator<P_OUT> lazySpliterator(Supplier<? extends Spliterator<P_OUT>> supplier) {
        return new StreamSpliterators.DelegatingSpliterator(supplier);
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP:0: B:0:0x0000->B:3:0x000a, LOOP_START, MTH_ENTER_BLOCK] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void forEachWithCancel(java.util.Spliterator<P_OUT> r1, java.util.stream.Sink<P_OUT> r2) {
        /*
            r0 = this;
        L_0x0000:
            boolean r0 = r2.cancellationRequested()
            if (r0 != 0) goto L_0x000c
            boolean r0 = r1.tryAdvance(r2)
            if (r0 != 0) goto L_0x0000
        L_0x000c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.ReferencePipeline.forEachWithCancel(java.util.Spliterator, java.util.stream.Sink):void");
    }

    public final Node.Builder<P_OUT> makeNodeBuilder(long j, IntFunction<P_OUT[]> intFunction) {
        return Nodes.builder(j, intFunction);
    }

    public final Iterator<P_OUT> iterator() {
        return Spliterators.iterator(spliterator());
    }

    public Stream<P_OUT> unordered() {
        if (!isOrdered()) {
            return this;
        }
        return new StatelessOp<P_OUT, P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_ORDERED) {
            public Sink<P_OUT> opWrapSink(int i, Sink<P_OUT> sink) {
                return sink;
            }
        };
    }

    public final Stream<P_OUT> filter(Predicate<? super P_OUT> predicate) {
        Objects.requireNonNull(predicate);
        final Predicate<? super P_OUT> predicate2 = predicate;
        return new StatelessOp<P_OUT, P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SIZED) {
            public Sink<P_OUT> opWrapSink(int i, Sink<P_OUT> sink) {
                return new Sink.ChainedReference<P_OUT, P_OUT>(sink) {
                    public void begin(long j) {
                        this.downstream.begin(-1);
                    }

                    public void accept(P_OUT p_out) {
                        if (predicate2.test(p_out)) {
                            this.downstream.accept(p_out);
                        }
                    }
                };
            }
        };
    }

    public final <R> Stream<R> map(Function<? super P_OUT, ? extends R> function) {
        Objects.requireNonNull(function);
        final Function<? super P_OUT, ? extends R> function2 = function;
        return new StatelessOp<P_OUT, R>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<P_OUT> opWrapSink(int i, Sink<R> sink) {
                return new Sink.ChainedReference<P_OUT, R>(sink) {
                    public void accept(P_OUT p_out) {
                        this.downstream.accept(function2.apply(p_out));
                    }
                };
            }
        };
    }

    public final IntStream mapToInt(ToIntFunction<? super P_OUT> toIntFunction) {
        Objects.requireNonNull(toIntFunction);
        final ToIntFunction<? super P_OUT> toIntFunction2 = toIntFunction;
        return new IntPipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<P_OUT> opWrapSink(int i, Sink<Integer> sink) {
                return new Sink.ChainedReference<P_OUT, Integer>(sink) {
                    public void accept(P_OUT p_out) {
                        this.downstream.accept(toIntFunction2.applyAsInt(p_out));
                    }
                };
            }
        };
    }

    public final LongStream mapToLong(ToLongFunction<? super P_OUT> toLongFunction) {
        Objects.requireNonNull(toLongFunction);
        final ToLongFunction<? super P_OUT> toLongFunction2 = toLongFunction;
        return new LongPipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<P_OUT> opWrapSink(int i, Sink<Long> sink) {
                return new Sink.ChainedReference<P_OUT, Long>(sink) {
                    public void accept(P_OUT p_out) {
                        this.downstream.accept(toLongFunction2.applyAsLong(p_out));
                    }
                };
            }
        };
    }

    public final DoubleStream mapToDouble(ToDoubleFunction<? super P_OUT> toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        final ToDoubleFunction<? super P_OUT> toDoubleFunction2 = toDoubleFunction;
        return new DoublePipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            public Sink<P_OUT> opWrapSink(int i, Sink<Double> sink) {
                return new Sink.ChainedReference<P_OUT, Double>(sink) {
                    public void accept(P_OUT p_out) {
                        this.downstream.accept(toDoubleFunction2.applyAsDouble(p_out));
                    }
                };
            }
        };
    }

    public final <R> Stream<R> flatMap(Function<? super P_OUT, ? extends Stream<? extends R>> function) {
        Objects.requireNonNull(function);
        final Function<? super P_OUT, ? extends Stream<? extends R>> function2 = function;
        return new StatelessOp<P_OUT, R>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            public Sink<P_OUT> opWrapSink(int i, Sink<R> sink) {
                return new Sink.ChainedReference<P_OUT, R>(sink) {
                    public void begin(long j) {
                        this.downstream.begin(-1);
                    }

                    public void accept(P_OUT p_out) {
                        Stream stream = (Stream) function2.apply(p_out);
                        if (stream != null) {
                            try {
                                ((Stream) stream.sequential()).forEach(this.downstream);
                            } catch (Throwable th) {
                                th.addSuppressed(th);
                            }
                        }
                        if (stream != null) {
                            stream.close();
                            return;
                        }
                        return;
                        throw th;
                    }
                };
            }
        };
    }

    public final IntStream flatMapToInt(Function<? super P_OUT, ? extends IntStream> function) {
        Objects.requireNonNull(function);
        final Function<? super P_OUT, ? extends IntStream> function2 = function;
        return new IntPipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            public Sink<P_OUT> opWrapSink(int i, Sink<Integer> sink) {
                return new Sink.ChainedReference<P_OUT, Integer>(sink) {
                    IntConsumer downstreamAsInt;

                    {
                        Sink sink = this.downstream;
                        Objects.requireNonNull(sink);
                        this.downstreamAsInt = new IntPipeline$$ExternalSyntheticLambda6(sink);
                    }

                    public void begin(long j) {
                        this.downstream.begin(-1);
                    }

                    public void accept(P_OUT p_out) {
                        IntStream intStream = (IntStream) function2.apply(p_out);
                        if (intStream != null) {
                            try {
                                intStream.sequential().forEach(this.downstreamAsInt);
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
                };
            }
        };
    }

    public final DoubleStream flatMapToDouble(Function<? super P_OUT, ? extends DoubleStream> function) {
        Objects.requireNonNull(function);
        final Function<? super P_OUT, ? extends DoubleStream> function2 = function;
        return new DoublePipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            public Sink<P_OUT> opWrapSink(int i, Sink<Double> sink) {
                return new Sink.ChainedReference<P_OUT, Double>(sink) {
                    DoubleConsumer downstreamAsDouble;

                    {
                        Sink sink = this.downstream;
                        Objects.requireNonNull(sink);
                        this.downstreamAsDouble = new DoublePipeline$$ExternalSyntheticLambda12(sink);
                    }

                    public void begin(long j) {
                        this.downstream.begin(-1);
                    }

                    public void accept(P_OUT p_out) {
                        DoubleStream doubleStream = (DoubleStream) function2.apply(p_out);
                        if (doubleStream != null) {
                            try {
                                doubleStream.sequential().forEach(this.downstreamAsDouble);
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
                };
            }
        };
    }

    public final LongStream flatMapToLong(Function<? super P_OUT, ? extends LongStream> function) {
        Objects.requireNonNull(function);
        final Function<? super P_OUT, ? extends LongStream> function2 = function;
        return new LongPipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            public Sink<P_OUT> opWrapSink(int i, Sink<Long> sink) {
                return new Sink.ChainedReference<P_OUT, Long>(sink) {
                    LongConsumer downstreamAsLong;

                    {
                        Sink sink = this.downstream;
                        Objects.requireNonNull(sink);
                        this.downstreamAsLong = new LongPipeline$$ExternalSyntheticLambda3(sink);
                    }

                    public void begin(long j) {
                        this.downstream.begin(-1);
                    }

                    public void accept(P_OUT p_out) {
                        LongStream longStream = (LongStream) function2.apply(p_out);
                        if (longStream != null) {
                            try {
                                longStream.sequential().forEach(this.downstreamAsLong);
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
                };
            }
        };
    }

    public final Stream<P_OUT> peek(Consumer<? super P_OUT> consumer) {
        Objects.requireNonNull(consumer);
        final Consumer<? super P_OUT> consumer2 = consumer;
        return new StatelessOp<P_OUT, P_OUT>(this, StreamShape.REFERENCE, 0) {
            public Sink<P_OUT> opWrapSink(int i, Sink<P_OUT> sink) {
                return new Sink.ChainedReference<P_OUT, P_OUT>(sink) {
                    public void accept(P_OUT p_out) {
                        consumer2.accept(p_out);
                        this.downstream.accept(p_out);
                    }
                };
            }
        };
    }

    public final Stream<P_OUT> distinct() {
        return DistinctOps.makeRef(this);
    }

    public final Stream<P_OUT> sorted() {
        return SortedOps.makeRef(this);
    }

    public final Stream<P_OUT> sorted(Comparator<? super P_OUT> comparator) {
        return SortedOps.makeRef(this, comparator);
    }

    public final Stream<P_OUT> limit(long j) {
        if (j >= 0) {
            return SliceOps.makeRef(this, 0, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    public final Stream<P_OUT> skip(long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : SliceOps.makeRef(this, j, -1);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    public void forEach(Consumer<? super P_OUT> consumer) {
        evaluate(ForEachOps.makeRef(consumer, false));
    }

    public void forEachOrdered(Consumer<? super P_OUT> consumer) {
        evaluate(ForEachOps.makeRef(consumer, true));
    }

    public final <A> A[] toArray(IntFunction<A[]> intFunction) {
        return Nodes.flatten(evaluateToArrayNode(intFunction), intFunction).asArray(intFunction);
    }

    static /* synthetic */ Object[] lambda$toArray$0(int i) {
        return new Object[i];
    }

    public final Object[] toArray() {
        return toArray(new ReferencePipeline$$ExternalSyntheticLambda0());
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [java.util.function.Predicate<? super P_OUT>, java.util.function.Predicate] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean anyMatch(java.util.function.Predicate<? super P_OUT> r2) {
        /*
            r1 = this;
            java.util.stream.MatchOps$MatchKind r0 = java.util.stream.MatchOps.MatchKind.ANY
            java.util.stream.TerminalOp r2 = java.util.stream.MatchOps.makeRef(r2, r0)
            java.lang.Object r1 = r1.evaluate(r2)
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.ReferencePipeline.anyMatch(java.util.function.Predicate):boolean");
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [java.util.function.Predicate<? super P_OUT>, java.util.function.Predicate] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean allMatch(java.util.function.Predicate<? super P_OUT> r2) {
        /*
            r1 = this;
            java.util.stream.MatchOps$MatchKind r0 = java.util.stream.MatchOps.MatchKind.ALL
            java.util.stream.TerminalOp r2 = java.util.stream.MatchOps.makeRef(r2, r0)
            java.lang.Object r1 = r1.evaluate(r2)
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.ReferencePipeline.allMatch(java.util.function.Predicate):boolean");
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [java.util.function.Predicate<? super P_OUT>, java.util.function.Predicate] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean noneMatch(java.util.function.Predicate<? super P_OUT> r2) {
        /*
            r1 = this;
            java.util.stream.MatchOps$MatchKind r0 = java.util.stream.MatchOps.MatchKind.NONE
            java.util.stream.TerminalOp r2 = java.util.stream.MatchOps.makeRef(r2, r0)
            java.lang.Object r1 = r1.evaluate(r2)
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.ReferencePipeline.noneMatch(java.util.function.Predicate):boolean");
    }

    public final Optional<P_OUT> findFirst() {
        return (Optional) evaluate(FindOps.makeRef(true));
    }

    public final Optional<P_OUT> findAny() {
        return (Optional) evaluate(FindOps.makeRef(false));
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [java.util.function.BinaryOperator<P_OUT>, java.util.function.BiFunction, java.util.function.BinaryOperator] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final P_OUT reduce(P_OUT r1, java.util.function.BinaryOperator<P_OUT> r2) {
        /*
            r0 = this;
            java.util.stream.TerminalOp r1 = java.util.stream.ReduceOps.makeRef(r1, r2, r2)
            java.lang.Object r0 = r0.evaluate(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.ReferencePipeline.reduce(java.lang.Object, java.util.function.BinaryOperator):java.lang.Object");
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [java.util.function.BinaryOperator<P_OUT>, java.util.function.BinaryOperator] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.Optional<P_OUT> reduce(java.util.function.BinaryOperator<P_OUT> r1) {
        /*
            r0 = this;
            java.util.stream.TerminalOp r1 = java.util.stream.ReduceOps.makeRef(r1)
            java.lang.Object r0 = r0.evaluate(r1)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.ReferencePipeline.reduce(java.util.function.BinaryOperator):java.util.Optional");
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [java.util.function.BinaryOperator<R>, java.util.function.BinaryOperator] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R> R reduce(R r1, java.util.function.BiFunction<R, ? super P_OUT, R> r2, java.util.function.BinaryOperator<R> r3) {
        /*
            r0 = this;
            java.util.stream.TerminalOp r1 = java.util.stream.ReduceOps.makeRef(r1, r2, r3)
            java.lang.Object r0 = r0.evaluate(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.ReferencePipeline.reduce(java.lang.Object, java.util.function.BiFunction, java.util.function.BinaryOperator):java.lang.Object");
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [java.util.stream.Collector<? super P_OUT, A, R>, java.util.stream.Collector] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <R, A> R collect(java.util.stream.Collector<? super P_OUT, A, R> r4) {
        /*
            r3 = this;
            boolean r0 = r3.isParallel()
            if (r0 == 0) goto L_0x0039
            java.util.Set r0 = r4.characteristics()
            java.util.stream.Collector$Characteristics r1 = java.util.stream.Collector.Characteristics.CONCURRENT
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto L_0x0039
            boolean r0 = r3.isOrdered()
            if (r0 == 0) goto L_0x0024
            java.util.Set r0 = r4.characteristics()
            java.util.stream.Collector$Characteristics r1 = java.util.stream.Collector.Characteristics.UNORDERED
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto L_0x0039
        L_0x0024:
            java.util.function.Supplier r0 = r4.supplier()
            java.lang.Object r0 = r0.get()
            java.util.function.BiConsumer r1 = r4.accumulator()
            java.util.stream.ReferencePipeline$$ExternalSyntheticLambda1 r2 = new java.util.stream.ReferencePipeline$$ExternalSyntheticLambda1
            r2.<init>(r1, r0)
            r3.forEach(r2)
            goto L_0x0041
        L_0x0039:
            java.util.stream.TerminalOp r0 = java.util.stream.ReduceOps.makeRef(r4)
            java.lang.Object r0 = r3.evaluate(r0)
        L_0x0041:
            java.util.Set r3 = r4.characteristics()
            java.util.stream.Collector$Characteristics r1 = java.util.stream.Collector.Characteristics.IDENTITY_FINISH
            boolean r3 = r3.contains(r1)
            if (r3 == 0) goto L_0x004e
            goto L_0x0056
        L_0x004e:
            java.util.function.Function r3 = r4.finisher()
            java.lang.Object r0 = r3.apply(r0)
        L_0x0056:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.ReferencePipeline.collect(java.util.stream.Collector):java.lang.Object");
    }

    public final <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super P_OUT> biConsumer, BiConsumer<R, R> biConsumer2) {
        return evaluate(ReduceOps.makeRef(supplier, biConsumer, biConsumer2));
    }

    public final Optional<P_OUT> max(Comparator<? super P_OUT> comparator) {
        return reduce(BinaryOperator.maxBy(comparator));
    }

    public final Optional<P_OUT> min(Comparator<? super P_OUT> comparator) {
        return reduce(BinaryOperator.minBy(comparator));
    }

    public final long count() {
        return mapToLong(new ReferencePipeline$$ExternalSyntheticLambda2()).sum();
    }

    public static class Head<E_IN, E_OUT> extends ReferencePipeline<E_IN, E_OUT> {
        public Head(Supplier<? extends Spliterator<?>> supplier, int i, boolean z) {
            super(supplier, i, z);
        }

        public Head(Spliterator<?> spliterator, int i, boolean z) {
            super(spliterator, i, z);
        }

        public final boolean opIsStateful() {
            throw new UnsupportedOperationException();
        }

        public final Sink<E_IN> opWrapSink(int i, Sink<E_OUT> sink) {
            throw new UnsupportedOperationException();
        }

        public void forEach(Consumer<? super E_OUT> consumer) {
            if (!isParallel()) {
                sourceStageSpliterator().forEachRemaining(consumer);
            } else {
                ReferencePipeline.super.forEach(consumer);
            }
        }

        public void forEachOrdered(Consumer<? super E_OUT> consumer) {
            if (!isParallel()) {
                sourceStageSpliterator().forEachRemaining(consumer);
            } else {
                ReferencePipeline.super.forEachOrdered(consumer);
            }
        }
    }

    public static abstract class StatelessOp<E_IN, E_OUT> extends ReferencePipeline<E_IN, E_OUT> {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        public final boolean opIsStateful() {
            return false;
        }

        static {
            Class<ReferencePipeline> cls = ReferencePipeline.class;
        }

        public StatelessOp(AbstractPipeline<?, E_IN, ?> abstractPipeline, StreamShape streamShape, int i) {
            super(abstractPipeline, i);
        }
    }

    public static abstract class StatefulOp<E_IN, E_OUT> extends ReferencePipeline<E_IN, E_OUT> {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        public abstract <P_IN> Node<E_OUT> opEvaluateParallel(PipelineHelper<E_OUT> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<E_OUT[]> intFunction);

        public final boolean opIsStateful() {
            return true;
        }

        static {
            Class<ReferencePipeline> cls = ReferencePipeline.class;
        }

        public StatefulOp(AbstractPipeline<?, E_IN, ?> abstractPipeline, StreamShape streamShape, int i) {
            super(abstractPipeline, i);
        }
    }
}
