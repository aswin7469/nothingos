package java.util.stream;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.BaseStream;
import java.util.stream.Node;

public abstract class AbstractPipeline<E_IN, E_OUT, S extends BaseStream<E_OUT, S>> extends PipelineHelper<E_OUT> implements BaseStream<E_OUT, S> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String MSG_CONSUMED = "source already consumed or closed";
    private static final String MSG_STREAM_LINKED = "stream has already been operated upon or closed";
    private int combinedFlags;
    private int depth;
    private boolean linkedOrConsumed;
    private AbstractPipeline nextStage;
    private boolean parallel;
    private final AbstractPipeline previousStage;
    private boolean sourceAnyStateful;
    private Runnable sourceCloseAction;
    protected final int sourceOrOpFlags;
    private Spliterator<?> sourceSpliterator;
    private final AbstractPipeline sourceStage;
    private Supplier<? extends Spliterator<?>> sourceSupplier;

    static /* synthetic */ Spliterator lambda$wrapSpliterator$1(Spliterator spliterator) {
        return spliterator;
    }

    public abstract <P_IN> Node<E_OUT> evaluateToNode(PipelineHelper<E_OUT> pipelineHelper, Spliterator<P_IN> spliterator, boolean z, IntFunction<E_OUT[]> intFunction);

    public abstract void forEachWithCancel(Spliterator<E_OUT> spliterator, Sink<E_OUT> sink);

    public abstract StreamShape getOutputShape();

    public abstract Spliterator<E_OUT> lazySpliterator(Supplier<? extends Spliterator<E_OUT>> supplier);

    public abstract Node.Builder<E_OUT> makeNodeBuilder(long j, IntFunction<E_OUT[]> intFunction);

    public abstract boolean opIsStateful();

    public abstract Sink<E_IN> opWrapSink(int i, Sink<E_OUT> sink);

    public abstract <P_IN> Spliterator<E_OUT> wrap(PipelineHelper<E_OUT> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z);

    AbstractPipeline(Supplier<? extends Spliterator<?>> supplier, int i, boolean z) {
        this.previousStage = null;
        this.sourceSupplier = supplier;
        this.sourceStage = this;
        int i2 = StreamOpFlag.STREAM_MASK & i;
        this.sourceOrOpFlags = i2;
        this.combinedFlags = (~(i2 << 1)) & StreamOpFlag.INITIAL_OPS_VALUE;
        this.depth = 0;
        this.parallel = z;
    }

    AbstractPipeline(Spliterator<?> spliterator, int i, boolean z) {
        this.previousStage = null;
        this.sourceSpliterator = spliterator;
        this.sourceStage = this;
        int i2 = StreamOpFlag.STREAM_MASK & i;
        this.sourceOrOpFlags = i2;
        this.combinedFlags = (~(i2 << 1)) & StreamOpFlag.INITIAL_OPS_VALUE;
        this.depth = 0;
        this.parallel = z;
    }

    AbstractPipeline(AbstractPipeline<?, E_IN, ?> abstractPipeline, int i) {
        if (!abstractPipeline.linkedOrConsumed) {
            abstractPipeline.linkedOrConsumed = true;
            abstractPipeline.nextStage = this;
            this.previousStage = abstractPipeline;
            this.sourceOrOpFlags = StreamOpFlag.OP_MASK & i;
            this.combinedFlags = StreamOpFlag.combineOpFlags(i, abstractPipeline.combinedFlags);
            AbstractPipeline abstractPipeline2 = abstractPipeline.sourceStage;
            this.sourceStage = abstractPipeline2;
            if (opIsStateful()) {
                abstractPipeline2.sourceAnyStateful = true;
            }
            this.depth = abstractPipeline.depth + 1;
            return;
        }
        throw new IllegalStateException(MSG_STREAM_LINKED);
    }

    /* access modifiers changed from: package-private */
    public final <R> R evaluate(TerminalOp<E_OUT, R> terminalOp) {
        if (!this.linkedOrConsumed) {
            this.linkedOrConsumed = true;
            if (isParallel()) {
                return terminalOp.evaluateParallel(this, sourceSpliterator(terminalOp.getOpFlags()));
            }
            return terminalOp.evaluateSequential(this, sourceSpliterator(terminalOp.getOpFlags()));
        }
        throw new IllegalStateException(MSG_STREAM_LINKED);
    }

    public final Node<E_OUT> evaluateToArrayNode(IntFunction<E_OUT[]> intFunction) {
        if (!this.linkedOrConsumed) {
            this.linkedOrConsumed = true;
            if (!isParallel() || this.previousStage == null || !opIsStateful()) {
                return evaluate(sourceSpliterator(0), true, intFunction);
            }
            this.depth = 0;
            AbstractPipeline abstractPipeline = this.previousStage;
            return opEvaluateParallel(abstractPipeline, abstractPipeline.sourceSpliterator(0), intFunction);
        }
        throw new IllegalStateException(MSG_STREAM_LINKED);
    }

    /* access modifiers changed from: package-private */
    public final Spliterator<E_OUT> sourceStageSpliterator() {
        AbstractPipeline abstractPipeline = this.sourceStage;
        if (this != abstractPipeline) {
            throw new IllegalStateException();
        } else if (!this.linkedOrConsumed) {
            this.linkedOrConsumed = true;
            Spliterator<?> spliterator = abstractPipeline.sourceSpliterator;
            if (spliterator != null) {
                abstractPipeline.sourceSpliterator = null;
                return spliterator;
            }
            Supplier<? extends Spliterator<?>> supplier = abstractPipeline.sourceSupplier;
            if (supplier != null) {
                Spliterator<E_OUT> spliterator2 = (Spliterator) supplier.get();
                this.sourceStage.sourceSupplier = null;
                return spliterator2;
            }
            throw new IllegalStateException(MSG_CONSUMED);
        } else {
            throw new IllegalStateException(MSG_STREAM_LINKED);
        }
    }

    public final S sequential() {
        this.sourceStage.parallel = false;
        return this;
    }

    public final S parallel() {
        this.sourceStage.parallel = true;
        return this;
    }

    public void close() {
        this.linkedOrConsumed = true;
        this.sourceSupplier = null;
        this.sourceSpliterator = null;
        AbstractPipeline abstractPipeline = this.sourceStage;
        Runnable runnable = abstractPipeline.sourceCloseAction;
        if (runnable != null) {
            abstractPipeline.sourceCloseAction = null;
            runnable.run();
        }
    }

    public S onClose(Runnable runnable) {
        AbstractPipeline abstractPipeline = this.sourceStage;
        Runnable runnable2 = abstractPipeline.sourceCloseAction;
        if (runnable2 != null) {
            runnable = Streams.composeWithExceptions(runnable2, runnable);
        }
        abstractPipeline.sourceCloseAction = runnable;
        return this;
    }

    public Spliterator<E_OUT> spliterator() {
        if (!this.linkedOrConsumed) {
            this.linkedOrConsumed = true;
            AbstractPipeline abstractPipeline = this.sourceStage;
            if (this != abstractPipeline) {
                return wrap(this, new AbstractPipeline$$ExternalSyntheticLambda2(this), isParallel());
            }
            Spliterator<?> spliterator = abstractPipeline.sourceSpliterator;
            if (spliterator != null) {
                abstractPipeline.sourceSpliterator = null;
                return spliterator;
            }
            Supplier<? extends Spliterator<?>> supplier = abstractPipeline.sourceSupplier;
            if (supplier != null) {
                abstractPipeline.sourceSupplier = null;
                return lazySpliterator(supplier);
            }
            throw new IllegalStateException(MSG_CONSUMED);
        }
        throw new IllegalStateException(MSG_STREAM_LINKED);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$spliterator$0$java-util-stream-AbstractPipeline  reason: not valid java name */
    public /* synthetic */ Spliterator m3884lambda$spliterator$0$javautilstreamAbstractPipeline() {
        return sourceSpliterator(0);
    }

    public final boolean isParallel() {
        return this.sourceStage.parallel;
    }

    public final int getStreamFlags() {
        return StreamOpFlag.toStreamFlags(this.combinedFlags);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: java.util.Spliterator<?>} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.Spliterator<?> sourceSpliterator(int r8) {
        /*
            r7 = this;
            java.util.stream.AbstractPipeline r0 = r7.sourceStage
            java.util.Spliterator<?> r1 = r0.sourceSpliterator
            r2 = 0
            if (r1 == 0) goto L_0x000a
            r0.sourceSpliterator = r2
            goto L_0x0019
        L_0x000a:
            java.util.function.Supplier<? extends java.util.Spliterator<?>> r0 = r0.sourceSupplier
            if (r0 == 0) goto L_0x0078
            java.lang.Object r0 = r0.get()
            r1 = r0
            java.util.Spliterator r1 = (java.util.Spliterator) r1
            java.util.stream.AbstractPipeline r0 = r7.sourceStage
            r0.sourceSupplier = r2
        L_0x0019:
            boolean r0 = r7.isParallel()
            if (r0 == 0) goto L_0x006d
            java.util.stream.AbstractPipeline r0 = r7.sourceStage
            boolean r2 = r0.sourceAnyStateful
            if (r2 == 0) goto L_0x006d
            java.util.stream.AbstractPipeline r2 = r0.nextStage
            r3 = 1
        L_0x0028:
            if (r0 == r7) goto L_0x006d
            int r4 = r2.sourceOrOpFlags
            boolean r5 = r2.opIsStateful()
            if (r5 == 0) goto L_0x005a
            java.util.stream.StreamOpFlag r3 = java.util.stream.StreamOpFlag.SHORT_CIRCUIT
            boolean r3 = r3.isKnown(r4)
            if (r3 == 0) goto L_0x003e
            int r3 = java.util.stream.StreamOpFlag.IS_SHORT_CIRCUIT
            int r3 = ~r3
            r4 = r4 & r3
        L_0x003e:
            java.util.Spliterator r1 = r2.opEvaluateParallelLazy(r0, r1)
            r3 = 64
            boolean r3 = r1.hasCharacteristics(r3)
            if (r3 == 0) goto L_0x0051
            int r3 = java.util.stream.StreamOpFlag.NOT_SIZED
            int r3 = ~r3
            r3 = r3 & r4
            int r4 = java.util.stream.StreamOpFlag.IS_SIZED
            goto L_0x0057
        L_0x0051:
            int r3 = java.util.stream.StreamOpFlag.IS_SIZED
            int r3 = ~r3
            r3 = r3 & r4
            int r4 = java.util.stream.StreamOpFlag.NOT_SIZED
        L_0x0057:
            r3 = r3 | r4
            r4 = r3
            r3 = 0
        L_0x005a:
            int r5 = r3 + 1
            r2.depth = r3
            int r0 = r0.combinedFlags
            int r0 = java.util.stream.StreamOpFlag.combineOpFlags(r4, r0)
            r2.combinedFlags = r0
            java.util.stream.AbstractPipeline r0 = r2.nextStage
            r3 = r5
            r6 = r2
            r2 = r0
            r0 = r6
            goto L_0x0028
        L_0x006d:
            if (r8 == 0) goto L_0x0077
            int r0 = r7.combinedFlags
            int r8 = java.util.stream.StreamOpFlag.combineOpFlags(r8, r0)
            r7.combinedFlags = r8
        L_0x0077:
            return r1
        L_0x0078:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.String r8 = "source already consumed or closed"
            r7.<init>((java.lang.String) r8)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.stream.AbstractPipeline.sourceSpliterator(int):java.util.Spliterator");
    }

    /* access modifiers changed from: package-private */
    public final StreamShape getSourceShape() {
        while (this.depth > 0) {
            this = this.previousStage;
        }
        return this.getOutputShape();
    }

    /* access modifiers changed from: package-private */
    public final <P_IN> long exactOutputSizeIfKnown(Spliterator<P_IN> spliterator) {
        if (StreamOpFlag.SIZED.isKnown(getStreamAndOpFlags())) {
            return spliterator.getExactSizeIfKnown();
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public final <P_IN, S extends Sink<E_OUT>> S wrapAndCopyInto(S s, Spliterator<P_IN> spliterator) {
        copyInto(wrapSink((Sink) Objects.requireNonNull(s)), spliterator);
        return s;
    }

    /* access modifiers changed from: package-private */
    public final <P_IN> void copyInto(Sink<P_IN> sink, Spliterator<P_IN> spliterator) {
        Objects.requireNonNull(sink);
        if (!StreamOpFlag.SHORT_CIRCUIT.isKnown(getStreamAndOpFlags())) {
            sink.begin(spliterator.getExactSizeIfKnown());
            spliterator.forEachRemaining(sink);
            sink.end();
            return;
        }
        copyIntoWithCancel(sink, spliterator);
    }

    /* access modifiers changed from: package-private */
    public final <P_IN> void copyIntoWithCancel(Sink<P_IN> sink, Spliterator<P_IN> spliterator) {
        while (this.depth > 0) {
            this = this.previousStage;
        }
        sink.begin(spliterator.getExactSizeIfKnown());
        this.forEachWithCancel(spliterator, sink);
        sink.end();
    }

    public final int getStreamAndOpFlags() {
        return this.combinedFlags;
    }

    /* access modifiers changed from: package-private */
    public final boolean isOrdered() {
        return StreamOpFlag.ORDERED.isKnown(this.combinedFlags);
    }

    public final <P_IN> Sink<P_IN> wrapSink(Sink<E_OUT> sink) {
        Objects.requireNonNull(sink);
        Sink<E_OUT> sink2 = sink;
        while (this.depth > 0) {
            Sink<E_OUT> opWrapSink = this.opWrapSink(this.previousStage.combinedFlags, sink2);
            this = this.previousStage;
            sink2 = opWrapSink;
        }
        return sink2;
    }

    /* access modifiers changed from: package-private */
    public final <P_IN> Spliterator<E_OUT> wrapSpliterator(Spliterator<P_IN> spliterator) {
        if (this.depth == 0) {
            return spliterator;
        }
        return wrap(this, new AbstractPipeline$$ExternalSyntheticLambda0(spliterator), isParallel());
    }

    public final <P_IN> Node<E_OUT> evaluate(Spliterator<P_IN> spliterator, boolean z, IntFunction<E_OUT[]> intFunction) {
        if (isParallel()) {
            return evaluateToNode(this, spliterator, z, intFunction);
        }
        return ((Node.Builder) wrapAndCopyInto(makeNodeBuilder(exactOutputSizeIfKnown(spliterator), intFunction), spliterator)).build();
    }

    public <P_IN> Node<E_OUT> opEvaluateParallel(PipelineHelper<E_OUT> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<E_OUT[]> intFunction) {
        throw new UnsupportedOperationException("Parallel evaluation is not supported");
    }

    static /* synthetic */ Object[] lambda$opEvaluateParallelLazy$2(int i) {
        return new Object[i];
    }

    public <P_IN> Spliterator<E_OUT> opEvaluateParallelLazy(PipelineHelper<E_OUT> pipelineHelper, Spliterator<P_IN> spliterator) {
        return opEvaluateParallel(pipelineHelper, spliterator, new AbstractPipeline$$ExternalSyntheticLambda1()).spliterator();
    }
}
