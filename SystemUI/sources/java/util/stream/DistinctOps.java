package java.util.stream;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.ReferencePipeline;
import java.util.stream.Sink;

final class DistinctOps {
    private DistinctOps() {
    }

    static <T> ReferencePipeline<T, T> makeRef(AbstractPipeline<?, T, ?> abstractPipeline) {
        return new ReferencePipeline.StatefulOp<T, T>(abstractPipeline, StreamShape.REFERENCE, StreamOpFlag.IS_DISTINCT | StreamOpFlag.NOT_SIZED) {
            /* access modifiers changed from: package-private */
            public <P_IN> Node<T> reduce(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator) {
                return Nodes.node((Collection) ReduceOps.makeRef(new DistinctOps$1$$ExternalSyntheticLambda1(), new DistinctOps$1$$ExternalSyntheticLambda2(), new DistinctOps$1$$ExternalSyntheticLambda3()).evaluateParallel(pipelineHelper, spliterator));
            }

            /* JADX WARNING: type inference failed for: r4v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
            /* JADX WARNING: type inference failed for: r5v0, types: [java.util.function.IntFunction<T[]>, java.util.function.IntFunction] */
            /* JADX WARNING: Unknown variable types count: 2 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public <P_IN> java.util.stream.Node<T> opEvaluateParallel(java.util.stream.PipelineHelper<T> r3, java.util.Spliterator<P_IN> r4, java.util.function.IntFunction<T[]> r5) {
                /*
                    r2 = this;
                    java.util.stream.StreamOpFlag r0 = java.util.stream.StreamOpFlag.DISTINCT
                    int r1 = r3.getStreamAndOpFlags()
                    boolean r0 = r0.isKnown(r1)
                    r1 = 0
                    if (r0 == 0) goto L_0x0012
                    java.util.stream.Node r2 = r3.evaluate(r4, r1, r5)
                    return r2
                L_0x0012:
                    java.util.stream.StreamOpFlag r5 = java.util.stream.StreamOpFlag.ORDERED
                    int r0 = r3.getStreamAndOpFlags()
                    boolean r5 = r5.isKnown(r0)
                    if (r5 == 0) goto L_0x0023
                    java.util.stream.Node r2 = r2.reduce(r3, r4)
                    return r2
                L_0x0023:
                    java.util.concurrent.atomic.AtomicBoolean r2 = new java.util.concurrent.atomic.AtomicBoolean
                    r2.<init>(r1)
                    java.util.concurrent.ConcurrentHashMap r5 = new java.util.concurrent.ConcurrentHashMap
                    r5.<init>()
                    java.util.stream.DistinctOps$1$$ExternalSyntheticLambda0 r0 = new java.util.stream.DistinctOps$1$$ExternalSyntheticLambda0
                    r0.<init>(r2, r5)
                    java.util.stream.TerminalOp r0 = java.util.stream.ForEachOps.makeRef(r0, r1)
                    r0.evaluateParallel(r3, r4)
                    java.util.Set r3 = r5.keySet()
                    boolean r2 = r2.get()
                    if (r2 == 0) goto L_0x004d
                    java.util.HashSet r2 = new java.util.HashSet
                    r2.<init>(r3)
                    r3 = 0
                    r2.add(r3)
                    r3 = r2
                L_0x004d:
                    java.util.stream.Node r2 = java.util.stream.Nodes.node(r3)
                    return r2
                */
                throw new UnsupportedOperationException("Method not decompiled: java.util.stream.DistinctOps.C44401.opEvaluateParallel(java.util.stream.PipelineHelper, java.util.Spliterator, java.util.function.IntFunction):java.util.stream.Node");
            }

            static /* synthetic */ void lambda$opEvaluateParallel$0(AtomicBoolean atomicBoolean, ConcurrentHashMap concurrentHashMap, Object obj) {
                if (obj == null) {
                    atomicBoolean.set(true);
                } else {
                    concurrentHashMap.putIfAbsent(obj, Boolean.TRUE);
                }
            }

            /* JADX WARNING: type inference failed for: r4v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public <P_IN> java.util.Spliterator<T> opEvaluateParallelLazy(java.util.stream.PipelineHelper<T> r3, java.util.Spliterator<P_IN> r4) {
                /*
                    r2 = this;
                    java.util.stream.StreamOpFlag r0 = java.util.stream.StreamOpFlag.DISTINCT
                    int r1 = r3.getStreamAndOpFlags()
                    boolean r0 = r0.isKnown(r1)
                    if (r0 == 0) goto L_0x0011
                    java.util.Spliterator r2 = r3.wrapSpliterator(r4)
                    return r2
                L_0x0011:
                    java.util.stream.StreamOpFlag r0 = java.util.stream.StreamOpFlag.ORDERED
                    int r1 = r3.getStreamAndOpFlags()
                    boolean r0 = r0.isKnown(r1)
                    if (r0 == 0) goto L_0x0026
                    java.util.stream.Node r2 = r2.reduce(r3, r4)
                    java.util.Spliterator r2 = r2.spliterator()
                    return r2
                L_0x0026:
                    java.util.stream.StreamSpliterators$DistinctSpliterator r2 = new java.util.stream.StreamSpliterators$DistinctSpliterator
                    java.util.Spliterator r3 = r3.wrapSpliterator(r4)
                    r2.<init>(r3)
                    return r2
                */
                throw new UnsupportedOperationException("Method not decompiled: java.util.stream.DistinctOps.C44401.opEvaluateParallelLazy(java.util.stream.PipelineHelper, java.util.Spliterator):java.util.Spliterator");
            }

            public Sink<T> opWrapSink(int i, Sink<T> sink) {
                Objects.requireNonNull(sink);
                if (StreamOpFlag.DISTINCT.isKnown(i)) {
                    return sink;
                }
                return StreamOpFlag.SORTED.isKnown(i) ? new Sink.ChainedReference<T, T>(sink) {
                    T lastSeen;
                    boolean seenNull;

                    public void begin(long j) {
                        this.seenNull = false;
                        this.lastSeen = null;
                        this.downstream.begin(-1);
                    }

                    public void end() {
                        this.seenNull = false;
                        this.lastSeen = null;
                        this.downstream.end();
                    }

                    public void accept(T t) {
                        if (t != null) {
                            T t2 = this.lastSeen;
                            if (t2 == null || !t.equals(t2)) {
                                Sink sink = this.downstream;
                                this.lastSeen = t;
                                sink.accept(t);
                            }
                        } else if (!this.seenNull) {
                            this.seenNull = true;
                            Sink sink2 = this.downstream;
                            this.lastSeen = null;
                            sink2.accept(null);
                        }
                    }
                } : new Sink.ChainedReference<T, T>(sink) {
                    Set<T> seen;

                    public void begin(long j) {
                        this.seen = new HashSet();
                        this.downstream.begin(-1);
                    }

                    public void end() {
                        this.seen = null;
                        this.downstream.end();
                    }

                    public void accept(T t) {
                        if (!this.seen.contains(t)) {
                            this.seen.add(t);
                            this.downstream.accept(t);
                        }
                    }
                };
            }
        };
    }
}
