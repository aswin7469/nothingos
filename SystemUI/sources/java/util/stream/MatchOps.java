package java.util.stream;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Sink;

final class MatchOps {
    private MatchOps() {
    }

    enum MatchKind {
        ANY(true, true),
        ALL(false, false),
        NONE(true, false);
        
        /* access modifiers changed from: private */
        public final boolean shortCircuitResult;
        /* access modifiers changed from: private */
        public final boolean stopOnPredicateMatches;

        private MatchKind(boolean z, boolean z2) {
            this.stopOnPredicateMatches = z;
            this.shortCircuitResult = z2;
        }
    }

    public static <T> TerminalOp<T, Boolean> makeRef(Predicate<? super T> predicate, MatchKind matchKind) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(matchKind);
        return new MatchOp(StreamShape.REFERENCE, matchKind, new MatchOps$$ExternalSyntheticLambda1(matchKind, predicate));
    }

    static /* synthetic */ BooleanTerminalSink lambda$makeRef$0(MatchKind matchKind, Predicate predicate) {
        return new BooleanTerminalSink<T>(predicate) {
            final /* synthetic */ Predicate val$predicate;

            {
                this.val$predicate = r2;
            }

            public void accept(T t) {
                if (!this.stop && this.val$predicate.test(t) == MatchKind.this.stopOnPredicateMatches) {
                    this.stop = true;
                    this.value = MatchKind.this.shortCircuitResult;
                }
            }
        };
    }

    public static TerminalOp<Integer, Boolean> makeInt(IntPredicate intPredicate, MatchKind matchKind) {
        Objects.requireNonNull(intPredicate);
        Objects.requireNonNull(matchKind);
        return new MatchOp(StreamShape.INT_VALUE, matchKind, new MatchOps$$ExternalSyntheticLambda0(matchKind, intPredicate));
    }

    static /* synthetic */ BooleanTerminalSink lambda$makeInt$1(MatchKind matchKind, IntPredicate intPredicate) {
        return new Sink.OfInt(intPredicate) {
            final /* synthetic */ IntPredicate val$predicate;

            {
                this.val$predicate = r2;
            }

            public void accept(int i) {
                if (!this.stop && this.val$predicate.test(i) == MatchKind.this.stopOnPredicateMatches) {
                    this.stop = true;
                    this.value = MatchKind.this.shortCircuitResult;
                }
            }
        };
    }

    public static TerminalOp<Long, Boolean> makeLong(LongPredicate longPredicate, MatchKind matchKind) {
        Objects.requireNonNull(longPredicate);
        Objects.requireNonNull(matchKind);
        return new MatchOp(StreamShape.LONG_VALUE, matchKind, new MatchOps$$ExternalSyntheticLambda3(matchKind, longPredicate));
    }

    static /* synthetic */ BooleanTerminalSink lambda$makeLong$2(MatchKind matchKind, LongPredicate longPredicate) {
        return new Sink.OfLong(longPredicate) {
            final /* synthetic */ LongPredicate val$predicate;

            {
                this.val$predicate = r2;
            }

            public void accept(long j) {
                if (!this.stop && this.val$predicate.test(j) == MatchKind.this.stopOnPredicateMatches) {
                    this.stop = true;
                    this.value = MatchKind.this.shortCircuitResult;
                }
            }
        };
    }

    public static TerminalOp<Double, Boolean> makeDouble(DoublePredicate doublePredicate, MatchKind matchKind) {
        Objects.requireNonNull(doublePredicate);
        Objects.requireNonNull(matchKind);
        return new MatchOp(StreamShape.DOUBLE_VALUE, matchKind, new MatchOps$$ExternalSyntheticLambda2(matchKind, doublePredicate));
    }

    static /* synthetic */ BooleanTerminalSink lambda$makeDouble$3(MatchKind matchKind, DoublePredicate doublePredicate) {
        return new Sink.OfDouble(doublePredicate) {
            final /* synthetic */ DoublePredicate val$predicate;

            {
                this.val$predicate = r2;
            }

            public void accept(double d) {
                if (!this.stop && this.val$predicate.test(d) == MatchKind.this.stopOnPredicateMatches) {
                    this.stop = true;
                    this.value = MatchKind.this.shortCircuitResult;
                }
            }
        };
    }

    private static final class MatchOp<T> implements TerminalOp<T, Boolean> {
        private final StreamShape inputShape;
        final MatchKind matchKind;
        final Supplier<BooleanTerminalSink<T>> sinkSupplier;

        MatchOp(StreamShape streamShape, MatchKind matchKind2, Supplier<BooleanTerminalSink<T>> supplier) {
            this.inputShape = streamShape;
            this.matchKind = matchKind2;
            this.sinkSupplier = supplier;
        }

        public int getOpFlags() {
            return StreamOpFlag.IS_SHORT_CIRCUIT | StreamOpFlag.NOT_ORDERED;
        }

        public StreamShape inputShape() {
            return this.inputShape;
        }

        public <S> Boolean evaluateSequential(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator) {
            return Boolean.valueOf(((BooleanTerminalSink) pipelineHelper.wrapAndCopyInto(this.sinkSupplier.get(), spliterator)).getAndClearState());
        }

        public <S> Boolean evaluateParallel(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator) {
            return (Boolean) new MatchTask(this, pipelineHelper, spliterator).invoke();
        }
    }

    private static abstract class BooleanTerminalSink<T> implements Sink<T> {
        boolean stop;
        boolean value;

        BooleanTerminalSink(MatchKind matchKind) {
            this.value = !matchKind.shortCircuitResult;
        }

        public boolean getAndClearState() {
            return this.value;
        }

        public boolean cancellationRequested() {
            return this.stop;
        }
    }

    private static final class MatchTask<P_IN, P_OUT> extends AbstractShortCircuitTask<P_IN, P_OUT, Boolean, MatchTask<P_IN, P_OUT>> {

        /* renamed from: op */
        private final MatchOp<P_OUT> f780op;

        MatchTask(MatchOp<P_OUT> matchOp, PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator) {
            super(pipelineHelper, spliterator);
            this.f780op = matchOp;
        }

        MatchTask(MatchTask<P_IN, P_OUT> matchTask, Spliterator<P_IN> spliterator) {
            super(matchTask, spliterator);
            this.f780op = matchTask.f780op;
        }

        /* access modifiers changed from: protected */
        public MatchTask<P_IN, P_OUT> makeChild(Spliterator<P_IN> spliterator) {
            return new MatchTask<>(this, spliterator);
        }

        /* access modifiers changed from: protected */
        public Boolean doLeaf() {
            boolean andClearState = ((BooleanTerminalSink) this.helper.wrapAndCopyInto(this.f780op.sinkSupplier.get(), this.spliterator)).getAndClearState();
            if (andClearState != this.f780op.matchKind.shortCircuitResult) {
                return null;
            }
            shortCircuit(Boolean.valueOf(andClearState));
            return null;
        }

        /* access modifiers changed from: protected */
        public Boolean getEmptyResult() {
            return Boolean.valueOf(!this.f780op.matchKind.shortCircuitResult);
        }
    }
}
