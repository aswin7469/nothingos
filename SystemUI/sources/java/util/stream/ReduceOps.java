package java.util.stream;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;

final class ReduceOps {

    private interface AccumulatingSink<T, R, K extends AccumulatingSink<T, R, K>> extends TerminalSink<T, R> {
        void combine(K k);
    }

    private ReduceOps() {
    }

    public static <T, U> TerminalOp<T, U> makeRef(final U u, final BiFunction<U, ? super T, U> biFunction, final BinaryOperator<U> binaryOperator) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<T, U, AnonymousClass1ReducingSink>(StreamShape.REFERENCE) {
            public AnonymousClass1ReducingSink makeSink() {
                return new AccumulatingSink<T, U, AnonymousClass1ReducingSink>(biFunction, binaryOperator) {
                    final /* synthetic */ BinaryOperator val$combiner;
                    final /* synthetic */ BiFunction val$reducer;

                    {
                        this.val$reducer = r2;
                        this.val$combiner = r3;
                    }

                    public void begin(long j) {
                        this.state = Object.this;
                    }

                    public void accept(T t) {
                        this.state = this.val$reducer.apply(this.state, t);
                    }

                    public void combine(AnonymousClass1ReducingSink r3) {
                        this.state = this.val$combiner.apply(this.state, r3.state);
                    }
                };
            }
        };
    }

    public static <T> TerminalOp<T, Optional<T>> makeRef(final BinaryOperator<T> binaryOperator) {
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<T, Optional<T>, AnonymousClass2ReducingSink>(StreamShape.REFERENCE) {
            public AnonymousClass2ReducingSink makeSink() {
                return new AccumulatingSink<T, Optional<T>, AnonymousClass2ReducingSink>() {
                    private boolean empty;
                    private T state;

                    public void begin(long j) {
                        this.empty = true;
                        this.state = null;
                    }

                    public void accept(T t) {
                        if (this.empty) {
                            this.empty = false;
                            this.state = t;
                            return;
                        }
                        this.state = BinaryOperator.this.apply(this.state, t);
                    }

                    public Optional<T> get() {
                        return this.empty ? Optional.empty() : Optional.m1745of(this.state);
                    }

                    public void combine(AnonymousClass2ReducingSink r2) {
                        if (!r2.empty) {
                            accept(r2.state);
                        }
                    }
                };
            }
        };
    }

    public static <T, I> TerminalOp<T, I> makeRef(Collector<? super T, I, ?> collector) {
        final Supplier supplier = ((Collector) Objects.requireNonNull(collector)).supplier();
        final BiConsumer<I, ? super T> accumulator = collector.accumulator();
        final BinaryOperator<I> combiner = collector.combiner();
        final Collector<? super T, I, ?> collector2 = collector;
        return new ReduceOp<T, I, AnonymousClass3ReducingSink>(StreamShape.REFERENCE) {
            public AnonymousClass3ReducingSink makeSink() {
                return new AccumulatingSink<T, I, AnonymousClass3ReducingSink>(accumulator, combiner) {
                    final /* synthetic */ BiConsumer val$accumulator;
                    final /* synthetic */ BinaryOperator val$combiner;

                    {
                        this.val$accumulator = r2;
                        this.val$combiner = r3;
                    }

                    public void begin(long j) {
                        this.state = Supplier.this.get();
                    }

                    public void accept(T t) {
                        this.val$accumulator.accept(this.state, t);
                    }

                    public void combine(AnonymousClass3ReducingSink r3) {
                        this.state = this.val$combiner.apply(this.state, r3.state);
                    }
                };
            }

            public int getOpFlags() {
                if (collector2.characteristics().contains(Collector.Characteristics.UNORDERED)) {
                    return StreamOpFlag.NOT_ORDERED;
                }
                return 0;
            }
        };
    }

    public static <T, R> TerminalOp<T, R> makeRef(final Supplier<R> supplier, final BiConsumer<R, ? super T> biConsumer, final BiConsumer<R, R> biConsumer2) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(biConsumer);
        Objects.requireNonNull(biConsumer2);
        return new ReduceOp<T, R, AnonymousClass4ReducingSink>(StreamShape.REFERENCE) {
            public AnonymousClass4ReducingSink makeSink() {
                return new AccumulatingSink<T, R, AnonymousClass4ReducingSink>(biConsumer, biConsumer2) {
                    final /* synthetic */ BiConsumer val$accumulator;
                    final /* synthetic */ BiConsumer val$reducer;

                    {
                        this.val$accumulator = r2;
                        this.val$reducer = r3;
                    }

                    public void begin(long j) {
                        this.state = Supplier.this.get();
                    }

                    public void accept(T t) {
                        this.val$accumulator.accept(this.state, t);
                    }

                    public void combine(AnonymousClass4ReducingSink r2) {
                        this.val$reducer.accept(this.state, r2.state);
                    }
                };
            }
        };
    }

    public static TerminalOp<Integer, Integer> makeInt(final int i, final IntBinaryOperator intBinaryOperator) {
        Objects.requireNonNull(intBinaryOperator);
        return new ReduceOp<Integer, Integer, AnonymousClass5ReducingSink>(StreamShape.INT_VALUE) {
            public AnonymousClass5ReducingSink makeSink() {
                return new Object(i, intBinaryOperator) {
                    private int state;
                    final /* synthetic */ int val$identity;
                    final /* synthetic */ IntBinaryOperator val$operator;

                    {
                        this.val$identity = r1;
                        this.val$operator = r2;
                    }

                    public void begin(long j) {
                        this.state = this.val$identity;
                    }

                    public void accept(int i) {
                        this.state = this.val$operator.applyAsInt(this.state, i);
                    }

                    public Integer get() {
                        return Integer.valueOf(this.state);
                    }

                    public void combine(AnonymousClass5ReducingSink r1) {
                        accept(r1.state);
                    }
                };
            }
        };
    }

    public static TerminalOp<Integer, OptionalInt> makeInt(final IntBinaryOperator intBinaryOperator) {
        Objects.requireNonNull(intBinaryOperator);
        return new ReduceOp<Integer, OptionalInt, AnonymousClass6ReducingSink>(StreamShape.INT_VALUE) {
            public AnonymousClass6ReducingSink makeSink() {
                return new Object() {
                    private boolean empty;
                    private int state;

                    public void begin(long j) {
                        this.empty = true;
                        this.state = 0;
                    }

                    public void accept(int i) {
                        if (this.empty) {
                            this.empty = false;
                            this.state = i;
                            return;
                        }
                        this.state = IntBinaryOperator.this.applyAsInt(this.state, i);
                    }

                    public OptionalInt get() {
                        return this.empty ? OptionalInt.empty() : OptionalInt.m1748of(this.state);
                    }

                    public void combine(AnonymousClass6ReducingSink r2) {
                        if (!r2.empty) {
                            accept(r2.state);
                        }
                    }
                };
            }
        };
    }

    public static <R> TerminalOp<Integer, R> makeInt(final Supplier<R> supplier, final ObjIntConsumer<R> objIntConsumer, final BinaryOperator<R> binaryOperator) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(objIntConsumer);
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<Integer, R, AnonymousClass7ReducingSink>(StreamShape.INT_VALUE) {
            public AnonymousClass7ReducingSink makeSink() {
                return new Box<R>(objIntConsumer, binaryOperator) {
                    final /* synthetic */ ObjIntConsumer val$accumulator;
                    final /* synthetic */ BinaryOperator val$combiner;

                    {
                        this.val$accumulator = r2;
                        this.val$combiner = r3;
                    }

                    public void begin(long j) {
                        this.state = Supplier.this.get();
                    }

                    public void accept(int i) {
                        this.val$accumulator.accept(this.state, i);
                    }

                    public void combine(AnonymousClass7ReducingSink r3) {
                        this.state = this.val$combiner.apply(this.state, r3.state);
                    }
                };
            }
        };
    }

    public static TerminalOp<Long, Long> makeLong(final long j, final LongBinaryOperator longBinaryOperator) {
        Objects.requireNonNull(longBinaryOperator);
        return new ReduceOp<Long, Long, AnonymousClass8ReducingSink>(StreamShape.LONG_VALUE) {
            public AnonymousClass8ReducingSink makeSink() {
                return new Object(j, longBinaryOperator) {
                    private long state;
                    final /* synthetic */ long val$identity;
                    final /* synthetic */ LongBinaryOperator val$operator;

                    {
                        this.val$identity = r1;
                        this.val$operator = r3;
                    }

                    public void begin(long j) {
                        this.state = this.val$identity;
                    }

                    public void accept(long j) {
                        this.state = this.val$operator.applyAsLong(this.state, j);
                    }

                    public Long get() {
                        return Long.valueOf(this.state);
                    }

                    public void combine(AnonymousClass8ReducingSink r3) {
                        accept(r3.state);
                    }
                };
            }
        };
    }

    public static TerminalOp<Long, OptionalLong> makeLong(final LongBinaryOperator longBinaryOperator) {
        Objects.requireNonNull(longBinaryOperator);
        return new ReduceOp<Long, OptionalLong, AnonymousClass9ReducingSink>(StreamShape.LONG_VALUE) {
            public AnonymousClass9ReducingSink makeSink() {
                return new Object() {
                    private boolean empty;
                    private long state;

                    public void begin(long j) {
                        this.empty = true;
                        this.state = 0;
                    }

                    public void accept(long j) {
                        if (this.empty) {
                            this.empty = false;
                            this.state = j;
                            return;
                        }
                        this.state = LongBinaryOperator.this.applyAsLong(this.state, j);
                    }

                    public OptionalLong get() {
                        return this.empty ? OptionalLong.empty() : OptionalLong.m1749of(this.state);
                    }

                    public void combine(AnonymousClass9ReducingSink r3) {
                        if (!r3.empty) {
                            accept(r3.state);
                        }
                    }
                };
            }
        };
    }

    public static <R> TerminalOp<Long, R> makeLong(final Supplier<R> supplier, final ObjLongConsumer<R> objLongConsumer, final BinaryOperator<R> binaryOperator) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(objLongConsumer);
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<Long, R, AnonymousClass10ReducingSink>(StreamShape.LONG_VALUE) {
            public AnonymousClass10ReducingSink makeSink() {
                return new Box<R>(objLongConsumer, binaryOperator) {
                    final /* synthetic */ ObjLongConsumer val$accumulator;
                    final /* synthetic */ BinaryOperator val$combiner;

                    {
                        this.val$accumulator = r2;
                        this.val$combiner = r3;
                    }

                    public void begin(long j) {
                        this.state = Supplier.this.get();
                    }

                    public void accept(long j) {
                        this.val$accumulator.accept(this.state, j);
                    }

                    public void combine(AnonymousClass10ReducingSink r3) {
                        this.state = this.val$combiner.apply(this.state, r3.state);
                    }
                };
            }
        };
    }

    public static TerminalOp<Double, Double> makeDouble(final double d, final DoubleBinaryOperator doubleBinaryOperator) {
        Objects.requireNonNull(doubleBinaryOperator);
        return new ReduceOp<Double, Double, AnonymousClass11ReducingSink>(StreamShape.DOUBLE_VALUE) {
            public AnonymousClass11ReducingSink makeSink() {
                return new Object(d, doubleBinaryOperator) {
                    private double state;
                    final /* synthetic */ double val$identity;
                    final /* synthetic */ DoubleBinaryOperator val$operator;

                    {
                        this.val$identity = r1;
                        this.val$operator = r3;
                    }

                    public void begin(long j) {
                        this.state = this.val$identity;
                    }

                    public void accept(double d) {
                        this.state = this.val$operator.applyAsDouble(this.state, d);
                    }

                    public Double get() {
                        return Double.valueOf(this.state);
                    }

                    public void combine(AnonymousClass11ReducingSink r3) {
                        accept(r3.state);
                    }
                };
            }
        };
    }

    public static TerminalOp<Double, OptionalDouble> makeDouble(final DoubleBinaryOperator doubleBinaryOperator) {
        Objects.requireNonNull(doubleBinaryOperator);
        return new ReduceOp<Double, OptionalDouble, AnonymousClass12ReducingSink>(StreamShape.DOUBLE_VALUE) {
            public AnonymousClass12ReducingSink makeSink() {
                return new Object() {
                    private boolean empty;
                    private double state;

                    public void begin(long j) {
                        this.empty = true;
                        this.state = 0.0d;
                    }

                    public void accept(double d) {
                        if (this.empty) {
                            this.empty = false;
                            this.state = d;
                            return;
                        }
                        this.state = DoubleBinaryOperator.this.applyAsDouble(this.state, d);
                    }

                    public OptionalDouble get() {
                        return this.empty ? OptionalDouble.empty() : OptionalDouble.m1747of(this.state);
                    }

                    public void combine(AnonymousClass12ReducingSink r3) {
                        if (!r3.empty) {
                            accept(r3.state);
                        }
                    }
                };
            }
        };
    }

    public static <R> TerminalOp<Double, R> makeDouble(final Supplier<R> supplier, final ObjDoubleConsumer<R> objDoubleConsumer, final BinaryOperator<R> binaryOperator) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(objDoubleConsumer);
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<Double, R, AnonymousClass13ReducingSink>(StreamShape.DOUBLE_VALUE) {
            public AnonymousClass13ReducingSink makeSink() {
                return new Box<R>(objDoubleConsumer, binaryOperator) {
                    final /* synthetic */ ObjDoubleConsumer val$accumulator;
                    final /* synthetic */ BinaryOperator val$combiner;

                    {
                        this.val$accumulator = r2;
                        this.val$combiner = r3;
                    }

                    public void begin(long j) {
                        this.state = Supplier.this.get();
                    }

                    public void accept(double d) {
                        this.val$accumulator.accept(this.state, d);
                    }

                    public void combine(AnonymousClass13ReducingSink r3) {
                        this.state = this.val$combiner.apply(this.state, r3.state);
                    }
                };
            }
        };
    }

    private static abstract class Box<U> {
        U state;

        Box() {
        }

        public U get() {
            return this.state;
        }
    }

    private static abstract class ReduceOp<T, R, S extends AccumulatingSink<T, R, S>> implements TerminalOp<T, R> {
        private final StreamShape inputShape;

        public abstract S makeSink();

        ReduceOp(StreamShape streamShape) {
            this.inputShape = streamShape;
        }

        public StreamShape inputShape() {
            return this.inputShape;
        }

        public <P_IN> R evaluateSequential(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator) {
            return ((AccumulatingSink) pipelineHelper.wrapAndCopyInto(makeSink(), spliterator)).get();
        }

        public <P_IN> R evaluateParallel(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator) {
            return ((AccumulatingSink) new ReduceTask(this, pipelineHelper, spliterator).invoke()).get();
        }
    }

    private static final class ReduceTask<P_IN, P_OUT, R, S extends AccumulatingSink<P_OUT, R, S>> extends AbstractTask<P_IN, P_OUT, S, ReduceTask<P_IN, P_OUT, R, S>> {

        /* renamed from: op */
        private final ReduceOp<P_OUT, R, S> f782op;

        ReduceTask(ReduceOp<P_OUT, R, S> reduceOp, PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator) {
            super(pipelineHelper, spliterator);
            this.f782op = reduceOp;
        }

        ReduceTask(ReduceTask<P_IN, P_OUT, R, S> reduceTask, Spliterator<P_IN> spliterator) {
            super(reduceTask, spliterator);
            this.f782op = reduceTask.f782op;
        }

        /* access modifiers changed from: protected */
        public ReduceTask<P_IN, P_OUT, R, S> makeChild(Spliterator<P_IN> spliterator) {
            return new ReduceTask<>(this, spliterator);
        }

        /* access modifiers changed from: protected */
        public S doLeaf() {
            return (AccumulatingSink) this.helper.wrapAndCopyInto(this.f782op.makeSink(), this.spliterator);
        }

        public void onCompletion(CountedCompleter<?> countedCompleter) {
            if (!isLeaf()) {
                AccumulatingSink accumulatingSink = (AccumulatingSink) ((ReduceTask) this.leftChild).getLocalResult();
                accumulatingSink.combine((AccumulatingSink) ((ReduceTask) this.rightChild).getLocalResult());
                setLocalResult(accumulatingSink);
            }
            super.onCompletion(countedCompleter);
        }
    }
}
