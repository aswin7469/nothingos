package java.util.stream;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Sink;

final class FindOps {
    private FindOps() {
    }

    public static <T> TerminalOp<T, Optional<T>> makeRef(boolean z) {
        return new FindOp(z, StreamShape.REFERENCE, Optional.empty(), new FindOps$$ExternalSyntheticLambda4(), new FindOps$$ExternalSyntheticLambda5());
    }

    public static TerminalOp<Integer, OptionalInt> makeInt(boolean z) {
        return new FindOp(z, StreamShape.INT_VALUE, OptionalInt.empty(), new FindOps$$ExternalSyntheticLambda6(), new FindOps$$ExternalSyntheticLambda7());
    }

    public static TerminalOp<Long, OptionalLong> makeLong(boolean z) {
        return new FindOp(z, StreamShape.LONG_VALUE, OptionalLong.empty(), new FindOps$$ExternalSyntheticLambda2(), new FindOps$$ExternalSyntheticLambda3());
    }

    public static TerminalOp<Double, OptionalDouble> makeDouble(boolean z) {
        return new FindOp(z, StreamShape.DOUBLE_VALUE, OptionalDouble.empty(), new FindOps$$ExternalSyntheticLambda0(), new FindOps$$ExternalSyntheticLambda1());
    }

    private static final class FindOp<T, O> implements TerminalOp<T, O> {
        final O emptyValue;
        final boolean mustFindFirst;
        final Predicate<O> presentPredicate;
        private final StreamShape shape;
        final Supplier<TerminalSink<T, O>> sinkSupplier;

        FindOp(boolean z, StreamShape streamShape, O o, Predicate<O> predicate, Supplier<TerminalSink<T, O>> supplier) {
            this.mustFindFirst = z;
            this.shape = streamShape;
            this.emptyValue = o;
            this.presentPredicate = predicate;
            this.sinkSupplier = supplier;
        }

        public int getOpFlags() {
            return (this.mustFindFirst ? 0 : StreamOpFlag.NOT_ORDERED) | StreamOpFlag.IS_SHORT_CIRCUIT;
        }

        public StreamShape inputShape() {
            return this.shape;
        }

        public <S> O evaluateSequential(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator) {
            O o = ((TerminalSink) pipelineHelper.wrapAndCopyInto(this.sinkSupplier.get(), spliterator)).get();
            return o != null ? o : this.emptyValue;
        }

        public <P_IN> O evaluateParallel(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator) {
            return new FindTask(this, pipelineHelper, spliterator).invoke();
        }
    }

    private static abstract class FindSink<T, O> implements TerminalSink<T, O> {
        boolean hasValue;
        T value;

        FindSink() {
        }

        public void accept(T t) {
            if (!this.hasValue) {
                this.hasValue = true;
                this.value = t;
            }
        }

        public boolean cancellationRequested() {
            return this.hasValue;
        }

        static final class OfRef<T> extends FindSink<T, Optional<T>> {
            OfRef() {
            }

            public Optional<T> get() {
                if (this.hasValue) {
                    return Optional.m1751of(this.value);
                }
                return null;
            }
        }

        static final class OfInt extends FindSink<Integer, OptionalInt> implements Sink.OfInt {
            OfInt() {
            }

            public /* bridge */ /* synthetic */ void accept(Integer num) {
                super.accept(num);
            }

            public void accept(int i) {
                accept(Integer.valueOf(i));
            }

            public OptionalInt get() {
                if (this.hasValue) {
                    return OptionalInt.m1754of(((Integer) this.value).intValue());
                }
                return null;
            }
        }

        static final class OfLong extends FindSink<Long, OptionalLong> implements Sink.OfLong {
            OfLong() {
            }

            public /* bridge */ /* synthetic */ void accept(Long l) {
                super.accept(l);
            }

            public void accept(long j) {
                accept(Long.valueOf(j));
            }

            public OptionalLong get() {
                if (this.hasValue) {
                    return OptionalLong.m1755of(((Long) this.value).longValue());
                }
                return null;
            }
        }

        static final class OfDouble extends FindSink<Double, OptionalDouble> implements Sink.OfDouble {
            OfDouble() {
            }

            public /* bridge */ /* synthetic */ void accept(Double d) {
                super.accept(d);
            }

            public void accept(double d) {
                accept(Double.valueOf(d));
            }

            public OptionalDouble get() {
                if (this.hasValue) {
                    return OptionalDouble.m1753of(((Double) this.value).doubleValue());
                }
                return null;
            }
        }
    }

    private static final class FindTask<P_IN, P_OUT, O> extends AbstractShortCircuitTask<P_IN, P_OUT, O, FindTask<P_IN, P_OUT, O>> {

        /* renamed from: op */
        private final FindOp<P_OUT, O> f775op;

        FindTask(FindOp<P_OUT, O> findOp, PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator) {
            super(pipelineHelper, spliterator);
            this.f775op = findOp;
        }

        FindTask(FindTask<P_IN, P_OUT, O> findTask, Spliterator<P_IN> spliterator) {
            super(findTask, spliterator);
            this.f775op = findTask.f775op;
        }

        /* access modifiers changed from: protected */
        public FindTask<P_IN, P_OUT, O> makeChild(Spliterator<P_IN> spliterator) {
            return new FindTask<>(this, spliterator);
        }

        /* access modifiers changed from: protected */
        public O getEmptyResult() {
            return this.f775op.emptyValue;
        }

        private void foundResult(O o) {
            if (isLeftmostNode()) {
                shortCircuit(o);
            } else {
                cancelLaterNodes();
            }
        }

        /* access modifiers changed from: protected */
        public O doLeaf() {
            O o = ((TerminalSink) this.helper.wrapAndCopyInto(this.f775op.sinkSupplier.get(), this.spliterator)).get();
            if (!this.f775op.mustFindFirst) {
                if (o != null) {
                    shortCircuit(o);
                }
                return null;
            } else if (o == null) {
                return null;
            } else {
                foundResult(o);
                return o;
            }
        }

        public void onCompletion(CountedCompleter<?> countedCompleter) {
            if (this.f775op.mustFindFirst) {
                FindTask findTask = (FindTask) this.leftChild;
                FindTask findTask2 = null;
                while (true) {
                    if (findTask != findTask2) {
                        Object localResult = findTask.getLocalResult();
                        if (localResult != null && this.f775op.presentPredicate.test(localResult)) {
                            setLocalResult(localResult);
                            foundResult(localResult);
                            break;
                        }
                        findTask2 = findTask;
                        findTask = (FindTask) this.rightChild;
                    } else {
                        break;
                    }
                }
            }
            super.onCompletion(countedCompleter);
        }
    }
}
