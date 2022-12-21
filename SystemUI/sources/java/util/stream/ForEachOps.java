package java.util.stream;

import java.util.Objects;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountedCompleter;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.Node;
import java.util.stream.Sink;

final class ForEachOps {
    private ForEachOps() {
    }

    public static <T> TerminalOp<T, Void> makeRef(Consumer<? super T> consumer, boolean z) {
        Objects.requireNonNull(consumer);
        return new ForEachOp.OfRef(consumer, z);
    }

    public static TerminalOp<Integer, Void> makeInt(IntConsumer intConsumer, boolean z) {
        Objects.requireNonNull(intConsumer);
        return new ForEachOp.OfInt(intConsumer, z);
    }

    public static TerminalOp<Long, Void> makeLong(LongConsumer longConsumer, boolean z) {
        Objects.requireNonNull(longConsumer);
        return new ForEachOp.OfLong(longConsumer, z);
    }

    public static TerminalOp<Double, Void> makeDouble(DoubleConsumer doubleConsumer, boolean z) {
        Objects.requireNonNull(doubleConsumer);
        return new ForEachOp.OfDouble(doubleConsumer, z);
    }

    static abstract class ForEachOp<T> implements TerminalOp<T, Void>, TerminalSink<T, Void> {
        private final boolean ordered;

        public Void get() {
            return null;
        }

        protected ForEachOp(boolean z) {
            this.ordered = z;
        }

        public int getOpFlags() {
            if (this.ordered) {
                return 0;
            }
            return StreamOpFlag.NOT_ORDERED;
        }

        public <S> Void evaluateSequential(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator) {
            return ((ForEachOp) pipelineHelper.wrapAndCopyInto(this, spliterator)).get();
        }

        public <S> Void evaluateParallel(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator) {
            if (this.ordered) {
                new ForEachOrderedTask(pipelineHelper, spliterator, this).invoke();
                return null;
            }
            new ForEachTask(pipelineHelper, spliterator, pipelineHelper.wrapSink(this)).invoke();
            return null;
        }

        static final class OfRef<T> extends ForEachOp<T> {
            final Consumer<? super T> consumer;

            OfRef(Consumer<? super T> consumer2, boolean z) {
                super(z);
                this.consumer = consumer2;
            }

            public void accept(T t) {
                this.consumer.accept(t);
            }
        }

        static final class OfInt extends ForEachOp<Integer> implements Sink.OfInt {
            final IntConsumer consumer;

            OfInt(IntConsumer intConsumer, boolean z) {
                super(z);
                this.consumer = intConsumer;
            }

            public StreamShape inputShape() {
                return StreamShape.INT_VALUE;
            }

            public void accept(int i) {
                this.consumer.accept(i);
            }
        }

        static final class OfLong extends ForEachOp<Long> implements Sink.OfLong {
            final LongConsumer consumer;

            OfLong(LongConsumer longConsumer, boolean z) {
                super(z);
                this.consumer = longConsumer;
            }

            public StreamShape inputShape() {
                return StreamShape.LONG_VALUE;
            }

            public void accept(long j) {
                this.consumer.accept(j);
            }
        }

        static final class OfDouble extends ForEachOp<Double> implements Sink.OfDouble {
            final DoubleConsumer consumer;

            OfDouble(DoubleConsumer doubleConsumer, boolean z) {
                super(z);
                this.consumer = doubleConsumer;
            }

            public StreamShape inputShape() {
                return StreamShape.DOUBLE_VALUE;
            }

            public void accept(double d) {
                this.consumer.accept(d);
            }
        }
    }

    static final class ForEachTask<S, T> extends CountedCompleter<Void> {
        private final PipelineHelper<T> helper;
        private final Sink<S> sink;
        private Spliterator<S> spliterator;
        private long targetSize;

        ForEachTask(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator2, Sink<S> sink2) {
            super((CountedCompleter<?>) null);
            this.sink = sink2;
            this.helper = pipelineHelper;
            this.spliterator = spliterator2;
            this.targetSize = 0;
        }

        ForEachTask(ForEachTask<S, T> forEachTask, Spliterator<S> spliterator2) {
            super(forEachTask);
            this.spliterator = spliterator2;
            this.sink = forEachTask.sink;
            this.targetSize = forEachTask.targetSize;
            this.helper = forEachTask.helper;
        }

        public void compute() {
            Spliterator<S> trySplit;
            Spliterator<S> spliterator2 = this.spliterator;
            long estimateSize = spliterator2.estimateSize();
            long j = this.targetSize;
            if (j == 0) {
                j = AbstractTask.suggestTargetSize(estimateSize);
                this.targetSize = j;
            }
            boolean isKnown = StreamOpFlag.SHORT_CIRCUIT.isKnown(this.helper.getStreamAndOpFlags());
            Sink<S> sink2 = this.sink;
            boolean z = false;
            while (true) {
                if (isKnown && sink2.cancellationRequested()) {
                    break;
                } else if (estimateSize <= j || (trySplit = spliterator2.trySplit()) == null) {
                    this.helper.copyInto(sink2, spliterator2);
                } else {
                    ForEachTask forEachTask = new ForEachTask(this, trySplit);
                    this.addToPendingCount(1);
                    if (z) {
                        spliterator2 = trySplit;
                    } else {
                        ForEachTask forEachTask2 = forEachTask;
                        forEachTask = this;
                        this = forEachTask2;
                    }
                    z = !z;
                    this.fork();
                    this = forEachTask;
                    estimateSize = spliterator2.estimateSize();
                }
            }
            this.helper.copyInto(sink2, spliterator2);
            this.spliterator = null;
            this.propagateCompletion();
        }
    }

    static final class ForEachOrderedTask<S, T> extends CountedCompleter<Void> {
        private final Sink<T> action;
        private final ConcurrentHashMap<ForEachOrderedTask<S, T>, ForEachOrderedTask<S, T>> completionMap;
        private final PipelineHelper<T> helper;
        private final ForEachOrderedTask<S, T> leftPredecessor;
        private Node<T> node;
        private Spliterator<S> spliterator;
        private final long targetSize;

        protected ForEachOrderedTask(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator2, Sink<T> sink) {
            super((CountedCompleter<?>) null);
            this.helper = pipelineHelper;
            this.spliterator = spliterator2;
            this.targetSize = AbstractTask.suggestTargetSize(spliterator2.estimateSize());
            this.completionMap = new ConcurrentHashMap<>(Math.max(16, AbstractTask.LEAF_TARGET << 1));
            this.action = sink;
            this.leftPredecessor = null;
        }

        ForEachOrderedTask(ForEachOrderedTask<S, T> forEachOrderedTask, Spliterator<S> spliterator2, ForEachOrderedTask<S, T> forEachOrderedTask2) {
            super(forEachOrderedTask);
            this.helper = forEachOrderedTask.helper;
            this.spliterator = spliterator2;
            this.targetSize = forEachOrderedTask.targetSize;
            this.completionMap = forEachOrderedTask.completionMap;
            this.action = forEachOrderedTask.action;
            this.leftPredecessor = forEachOrderedTask2;
        }

        public final void compute() {
            doCompute(this);
        }

        private static <S, T> void doCompute(ForEachOrderedTask<S, T> forEachOrderedTask) {
            Spliterator<S> trySplit;
            Spliterator<S> spliterator2 = forEachOrderedTask.spliterator;
            long j = forEachOrderedTask.targetSize;
            boolean z = false;
            while (spliterator2.estimateSize() > j && (trySplit = spliterator2.trySplit()) != null) {
                ForEachOrderedTask<S, T> forEachOrderedTask2 = new ForEachOrderedTask<>(forEachOrderedTask, trySplit, forEachOrderedTask.leftPredecessor);
                ForEachOrderedTask<S, T> forEachOrderedTask3 = new ForEachOrderedTask<>(forEachOrderedTask, spliterator2, forEachOrderedTask2);
                forEachOrderedTask.addToPendingCount(1);
                forEachOrderedTask3.addToPendingCount(1);
                forEachOrderedTask.completionMap.put(forEachOrderedTask2, forEachOrderedTask3);
                if (forEachOrderedTask.leftPredecessor != null) {
                    forEachOrderedTask2.addToPendingCount(1);
                    if (forEachOrderedTask.completionMap.replace(forEachOrderedTask.leftPredecessor, forEachOrderedTask, forEachOrderedTask2)) {
                        forEachOrderedTask.addToPendingCount(-1);
                    } else {
                        forEachOrderedTask2.addToPendingCount(-1);
                    }
                }
                if (z) {
                    spliterator2 = trySplit;
                    forEachOrderedTask = forEachOrderedTask2;
                    forEachOrderedTask2 = forEachOrderedTask3;
                } else {
                    forEachOrderedTask = forEachOrderedTask3;
                }
                z = !z;
                forEachOrderedTask2.fork();
            }
            if (forEachOrderedTask.getPendingCount() > 0) {
                ForEachOps$ForEachOrderedTask$$ExternalSyntheticLambda0 forEachOps$ForEachOrderedTask$$ExternalSyntheticLambda0 = new ForEachOps$ForEachOrderedTask$$ExternalSyntheticLambda0();
                PipelineHelper<T> pipelineHelper = forEachOrderedTask.helper;
                forEachOrderedTask.node = ((Node.Builder) forEachOrderedTask.helper.wrapAndCopyInto(pipelineHelper.makeNodeBuilder(pipelineHelper.exactOutputSizeIfKnown(spliterator2), forEachOps$ForEachOrderedTask$$ExternalSyntheticLambda0), spliterator2)).build();
                forEachOrderedTask.spliterator = null;
            }
            forEachOrderedTask.tryComplete();
        }

        static /* synthetic */ Object[] lambda$doCompute$0(int i) {
            return new Object[i];
        }

        public void onCompletion(CountedCompleter<?> countedCompleter) {
            Node<T> node2 = this.node;
            if (node2 != null) {
                node2.forEach(this.action);
                this.node = null;
            } else {
                Spliterator<S> spliterator2 = this.spliterator;
                if (spliterator2 != null) {
                    this.helper.wrapAndCopyInto(this.action, spliterator2);
                    this.spliterator = null;
                }
            }
            ForEachOrderedTask remove = this.completionMap.remove(this);
            if (remove != null) {
                remove.tryComplete();
            }
        }
    }
}
