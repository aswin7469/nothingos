package java.util.stream;

import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.function.IntFunction;
import java.util.stream.DoublePipeline;
import java.util.stream.IntPipeline;
import java.util.stream.LongPipeline;
import java.util.stream.Node;
import java.util.stream.ReferencePipeline;
import java.util.stream.Sink;
import java.util.stream.StreamSpliterators;

final class SliceOps {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    /* access modifiers changed from: private */
    public static long calcSliceFence(long j, long j2) {
        long j3 = j2 >= 0 ? j + j2 : Long.MAX_VALUE;
        if (j3 >= 0) {
            return j3;
        }
        return Long.MAX_VALUE;
    }

    private SliceOps() {
    }

    /* access modifiers changed from: private */
    public static long calcSize(long j, long j2, long j3) {
        if (j >= 0) {
            return Math.max(-1, Math.min(j - j2, j3));
        }
        return -1;
    }

    /* renamed from: java.util.stream.SliceOps$5 */
    static /* synthetic */ class C45405 {
        static final /* synthetic */ int[] $SwitchMap$java$util$stream$StreamShape;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                java.util.stream.StreamShape[] r0 = java.util.stream.StreamShape.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$util$stream$StreamShape = r0
                java.util.stream.StreamShape r1 = java.util.stream.StreamShape.REFERENCE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$util$stream$StreamShape     // Catch:{ NoSuchFieldError -> 0x001d }
                java.util.stream.StreamShape r1 = java.util.stream.StreamShape.INT_VALUE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$util$stream$StreamShape     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.util.stream.StreamShape r1 = java.util.stream.StreamShape.LONG_VALUE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$java$util$stream$StreamShape     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.util.stream.StreamShape r1 = java.util.stream.StreamShape.DOUBLE_VALUE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SliceOps.C45405.<clinit>():void");
        }
    }

    /* access modifiers changed from: private */
    public static <P_IN> Spliterator<P_IN> sliceSpliterator(StreamShape streamShape, Spliterator<P_IN> spliterator, long j, long j2) {
        long calcSliceFence = calcSliceFence(j, j2);
        int i = C45405.$SwitchMap$java$util$stream$StreamShape[streamShape.ordinal()];
        if (i == 1) {
            return new StreamSpliterators.SliceSpliterator.OfRef(spliterator, j, calcSliceFence);
        }
        if (i == 2) {
            return new StreamSpliterators.SliceSpliterator.OfInt((Spliterator.OfInt) spliterator, j, calcSliceFence);
        }
        if (i == 3) {
            return new StreamSpliterators.SliceSpliterator.OfLong((Spliterator.OfLong) spliterator, j, calcSliceFence);
        }
        if (i == 4) {
            return new StreamSpliterators.SliceSpliterator.OfDouble((Spliterator.OfDouble) spliterator, j, calcSliceFence);
        }
        throw new IllegalStateException("Unknown shape " + streamShape);
    }

    /* access modifiers changed from: private */
    public static <T> IntFunction<T[]> castingArray() {
        return new SliceOps$$ExternalSyntheticLambda0();
    }

    static /* synthetic */ Object[] lambda$castingArray$0(int i) {
        return new Object[i];
    }

    public static <T> Stream<T> makeRef(AbstractPipeline<?, T, ?> abstractPipeline, long j, long j2) {
        if (j >= 0) {
            final long j3 = j;
            final long j4 = j2;
            return new ReferencePipeline.StatefulOp<T, T>(abstractPipeline, StreamShape.REFERENCE, flags(j2)) {
                /* access modifiers changed from: package-private */
                public Spliterator<T> unorderedSkipLimitSpliterator(Spliterator<T> spliterator, long j, long j2, long j3) {
                    long j4;
                    long j5;
                    if (j <= j3) {
                        long j6 = j3 - j;
                        j4 = j2 >= 0 ? Math.min(j2, j6) : j6;
                        j5 = 0;
                    } else {
                        j5 = j;
                        j4 = j2;
                    }
                    return new StreamSpliterators.UnorderedSliceSpliterator.OfRef(spliterator, j5, j4);
                }

                /* JADX WARNING: type inference failed for: r16v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
                /* JADX WARNING: Unknown variable types count: 1 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public <P_IN> java.util.Spliterator<T> opEvaluateParallelLazy(java.util.stream.PipelineHelper<T> r15, java.util.Spliterator<P_IN> r16) {
                    /*
                        r14 = this;
                        r1 = r14
                        long r6 = r15.exactOutputSizeIfKnown(r16)
                        r2 = 0
                        int r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
                        if (r0 <= 0) goto L_0x0028
                        r0 = 16384(0x4000, float:2.2959E-41)
                        r3 = r16
                        boolean r0 = r3.hasCharacteristics(r0)
                        if (r0 == 0) goto L_0x002a
                        java.util.stream.StreamSpliterators$SliceSpliterator$OfRef r0 = new java.util.stream.StreamSpliterators$SliceSpliterator$OfRef
                        java.util.Spliterator r9 = r15.wrapSpliterator(r16)
                        long r10 = r5
                        long r1 = r7
                        long r12 = java.util.stream.SliceOps.calcSliceFence(r10, r1)
                        r8 = r0
                        r8.<init>(r9, r10, r12)
                        return r0
                    L_0x0028:
                        r3 = r16
                    L_0x002a:
                        java.util.stream.StreamOpFlag r0 = java.util.stream.StreamOpFlag.ORDERED
                        int r2 = r15.getStreamAndOpFlags()
                        boolean r0 = r0.isKnown(r2)
                        if (r0 != 0) goto L_0x0047
                        java.util.Spliterator r2 = r15.wrapSpliterator(r16)
                        long r3 = r5
                        long r8 = r7
                        r0 = r14
                        r1 = r2
                        r2 = r3
                        r4 = r8
                        java.util.Spliterator r0 = r0.unorderedSkipLimitSpliterator(r1, r2, r4, r6)
                        return r0
                    L_0x0047:
                        java.util.stream.SliceOps$SliceTask r9 = new java.util.stream.SliceOps$SliceTask
                        java.util.function.IntFunction r4 = java.util.stream.SliceOps.castingArray()
                        long r5 = r5
                        long r7 = r7
                        r0 = r9
                        r1 = r14
                        r2 = r15
                        r3 = r16
                        r0.<init>(r1, r2, r3, r4, r5, r7)
                        java.lang.Object r0 = r9.invoke()
                        java.util.stream.Node r0 = (java.util.stream.Node) r0
                        java.util.Spliterator r0 = r0.spliterator()
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SliceOps.C45321.opEvaluateParallelLazy(java.util.stream.PipelineHelper, java.util.Spliterator):java.util.Spliterator");
                }

                /* JADX WARNING: type inference failed for: r19v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
                /* JADX WARNING: Unknown variable types count: 1 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public <P_IN> java.util.stream.Node<T> opEvaluateParallel(java.util.stream.PipelineHelper<T> r18, java.util.Spliterator<P_IN> r19, java.util.function.IntFunction<T[]> r20) {
                    /*
                        r17 = this;
                        r8 = r17
                        r9 = r20
                        long r6 = r18.exactOutputSizeIfKnown(r19)
                        r0 = 0
                        int r0 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                        r10 = 1
                        if (r0 <= 0) goto L_0x0032
                        r0 = 16384(0x4000, float:2.2959E-41)
                        r3 = r19
                        boolean r0 = r3.hasCharacteristics(r0)
                        if (r0 == 0) goto L_0x002f
                        java.util.stream.StreamShape r11 = r18.getSourceShape()
                        long r13 = r5
                        long r0 = r7
                        r12 = r19
                        r15 = r0
                        java.util.Spliterator r0 = java.util.stream.SliceOps.sliceSpliterator(r11, r12, r13, r15)
                        r2 = r18
                        java.util.stream.Node r0 = java.util.stream.Nodes.collect(r2, r0, r10, r9)
                        return r0
                    L_0x002f:
                        r2 = r18
                        goto L_0x0036
                    L_0x0032:
                        r2 = r18
                        r3 = r19
                    L_0x0036:
                        java.util.stream.StreamOpFlag r0 = java.util.stream.StreamOpFlag.ORDERED
                        int r1 = r18.getStreamAndOpFlags()
                        boolean r0 = r0.isKnown(r1)
                        if (r0 != 0) goto L_0x0055
                        java.util.Spliterator r1 = r18.wrapSpliterator(r19)
                        long r2 = r5
                        long r4 = r7
                        r0 = r17
                        java.util.Spliterator r0 = r0.unorderedSkipLimitSpliterator(r1, r2, r4, r6)
                        java.util.stream.Node r0 = java.util.stream.Nodes.collect(r8, r0, r10, r9)
                        return r0
                    L_0x0055:
                        java.util.stream.SliceOps$SliceTask r10 = new java.util.stream.SliceOps$SliceTask
                        long r5 = r5
                        long r11 = r7
                        r0 = r10
                        r1 = r17
                        r2 = r18
                        r3 = r19
                        r4 = r20
                        r7 = r11
                        r0.<init>(r1, r2, r3, r4, r5, r7)
                        java.lang.Object r0 = r10.invoke()
                        java.util.stream.Node r0 = (java.util.stream.Node) r0
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SliceOps.C45321.opEvaluateParallel(java.util.stream.PipelineHelper, java.util.Spliterator, java.util.function.IntFunction):java.util.stream.Node");
                }

                public Sink<T> opWrapSink(int i, Sink<T> sink) {
                    return new Sink.ChainedReference<T, T>(sink) {

                        /* renamed from: m */
                        long f783m;

                        /* renamed from: n */
                        long f784n;

                        {
                            this.f784n = j3;
                            this.f783m = j4 >= 0 ? j4 : Long.MAX_VALUE;
                        }

                        public void begin(long j) {
                            this.downstream.begin(SliceOps.calcSize(j, j3, this.f783m));
                        }

                        public void accept(T t) {
                            long j = this.f784n;
                            if (j == 0) {
                                long j2 = this.f783m;
                                if (j2 > 0) {
                                    this.f783m = j2 - 1;
                                    this.downstream.accept(t);
                                    return;
                                }
                                return;
                            }
                            this.f784n = j - 1;
                        }

                        public boolean cancellationRequested() {
                            return this.f783m == 0 || this.downstream.cancellationRequested();
                        }
                    };
                }
            };
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static IntStream makeInt(AbstractPipeline<?, Integer, ?> abstractPipeline, long j, long j2) {
        if (j >= 0) {
            final long j3 = j;
            final long j4 = j2;
            return new IntPipeline.StatefulOp<Integer>(abstractPipeline, StreamShape.INT_VALUE, flags(j2)) {
                /* access modifiers changed from: package-private */
                public Spliterator.OfInt unorderedSkipLimitSpliterator(Spliterator.OfInt ofInt, long j, long j2, long j3) {
                    long j4;
                    long j5;
                    if (j <= j3) {
                        long j6 = j3 - j;
                        j4 = j2 >= 0 ? Math.min(j2, j6) : j6;
                        j5 = 0;
                    } else {
                        j5 = j;
                        j4 = j2;
                    }
                    return new StreamSpliterators.UnorderedSliceSpliterator.OfInt(ofInt, j5, j4);
                }

                /* JADX WARNING: type inference failed for: r16v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
                /* JADX WARNING: Unknown variable types count: 1 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public <P_IN> java.util.Spliterator<java.lang.Integer> opEvaluateParallelLazy(java.util.stream.PipelineHelper<java.lang.Integer> r15, java.util.Spliterator<P_IN> r16) {
                    /*
                        r14 = this;
                        r1 = r14
                        long r6 = r15.exactOutputSizeIfKnown(r16)
                        r2 = 0
                        int r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
                        if (r0 <= 0) goto L_0x002b
                        r0 = 16384(0x4000, float:2.2959E-41)
                        r3 = r16
                        boolean r0 = r3.hasCharacteristics(r0)
                        if (r0 == 0) goto L_0x002d
                        java.util.stream.StreamSpliterators$SliceSpliterator$OfInt r0 = new java.util.stream.StreamSpliterators$SliceSpliterator$OfInt
                        java.util.Spliterator r2 = r15.wrapSpliterator(r16)
                        r9 = r2
                        java.util.Spliterator$OfInt r9 = (java.util.Spliterator.OfInt) r9
                        long r10 = r5
                        long r1 = r7
                        long r12 = java.util.stream.SliceOps.calcSliceFence(r10, r1)
                        r8 = r0
                        r8.<init>(r9, r10, r12)
                        return r0
                    L_0x002b:
                        r3 = r16
                    L_0x002d:
                        java.util.stream.StreamOpFlag r0 = java.util.stream.StreamOpFlag.ORDERED
                        int r2 = r15.getStreamAndOpFlags()
                        boolean r0 = r0.isKnown(r2)
                        if (r0 != 0) goto L_0x004d
                        java.util.Spliterator r0 = r15.wrapSpliterator(r16)
                        r2 = r0
                        java.util.Spliterator$OfInt r2 = (java.util.Spliterator.OfInt) r2
                        long r3 = r5
                        long r8 = r7
                        r0 = r14
                        r1 = r2
                        r2 = r3
                        r4 = r8
                        java.util.Spliterator$OfInt r0 = r0.unorderedSkipLimitSpliterator(r1, r2, r4, r6)
                        return r0
                    L_0x004d:
                        java.util.stream.SliceOps$SliceTask r9 = new java.util.stream.SliceOps$SliceTask
                        java.util.stream.SliceOps$2$$ExternalSyntheticLambda0 r4 = new java.util.stream.SliceOps$2$$ExternalSyntheticLambda0
                        r4.<init>()
                        long r5 = r5
                        long r7 = r7
                        r0 = r9
                        r1 = r14
                        r2 = r15
                        r3 = r16
                        r0.<init>(r1, r2, r3, r4, r5, r7)
                        java.lang.Object r0 = r9.invoke()
                        java.util.stream.Node r0 = (java.util.stream.Node) r0
                        java.util.Spliterator r0 = r0.spliterator()
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SliceOps.C45342.opEvaluateParallelLazy(java.util.stream.PipelineHelper, java.util.Spliterator):java.util.Spliterator");
                }

                static /* synthetic */ Integer[] lambda$opEvaluateParallelLazy$0(int i) {
                    return new Integer[i];
                }

                /* JADX WARNING: type inference failed for: r18v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
                /* JADX WARNING: Unknown variable types count: 1 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public <P_IN> java.util.stream.Node<java.lang.Integer> opEvaluateParallel(java.util.stream.PipelineHelper<java.lang.Integer> r17, java.util.Spliterator<P_IN> r18, java.util.function.IntFunction<java.lang.Integer[]> r19) {
                    /*
                        r16 = this;
                        r8 = r16
                        long r6 = r17.exactOutputSizeIfKnown(r18)
                        r0 = 0
                        int r0 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                        r9 = 1
                        if (r0 <= 0) goto L_0x002f
                        r0 = 16384(0x4000, float:2.2959E-41)
                        r3 = r18
                        boolean r0 = r3.hasCharacteristics(r0)
                        if (r0 == 0) goto L_0x002c
                        java.util.stream.StreamShape r10 = r17.getSourceShape()
                        long r12 = r5
                        long r14 = r7
                        r11 = r18
                        java.util.Spliterator r0 = java.util.stream.SliceOps.sliceSpliterator(r10, r11, r12, r14)
                        r2 = r17
                        java.util.stream.Node$OfInt r0 = java.util.stream.Nodes.collectInt(r2, r0, r9)
                        return r0
                    L_0x002c:
                        r2 = r17
                        goto L_0x0033
                    L_0x002f:
                        r2 = r17
                        r3 = r18
                    L_0x0033:
                        java.util.stream.StreamOpFlag r0 = java.util.stream.StreamOpFlag.ORDERED
                        int r1 = r17.getStreamAndOpFlags()
                        boolean r0 = r0.isKnown(r1)
                        if (r0 != 0) goto L_0x0055
                        java.util.Spliterator r0 = r17.wrapSpliterator(r18)
                        r1 = r0
                        java.util.Spliterator$OfInt r1 = (java.util.Spliterator.OfInt) r1
                        long r2 = r5
                        long r4 = r7
                        r0 = r16
                        java.util.Spliterator$OfInt r0 = r0.unorderedSkipLimitSpliterator(r1, r2, r4, r6)
                        java.util.stream.Node$OfInt r0 = java.util.stream.Nodes.collectInt(r8, r0, r9)
                        return r0
                    L_0x0055:
                        java.util.stream.SliceOps$SliceTask r9 = new java.util.stream.SliceOps$SliceTask
                        long r5 = r5
                        long r10 = r7
                        r0 = r9
                        r1 = r16
                        r2 = r17
                        r3 = r18
                        r4 = r19
                        r7 = r10
                        r0.<init>(r1, r2, r3, r4, r5, r7)
                        java.lang.Object r0 = r9.invoke()
                        java.util.stream.Node r0 = (java.util.stream.Node) r0
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SliceOps.C45342.opEvaluateParallel(java.util.stream.PipelineHelper, java.util.Spliterator, java.util.function.IntFunction):java.util.stream.Node");
                }

                public Sink<Integer> opWrapSink(int i, Sink<Integer> sink) {
                    return new Sink.ChainedInt<Integer>(sink) {

                        /* renamed from: m */
                        long f785m;

                        /* renamed from: n */
                        long f786n;

                        {
                            this.f786n = j3;
                            this.f785m = j4 >= 0 ? j4 : Long.MAX_VALUE;
                        }

                        public void begin(long j) {
                            this.downstream.begin(SliceOps.calcSize(j, j3, this.f785m));
                        }

                        public void accept(int i) {
                            long j = this.f786n;
                            if (j == 0) {
                                long j2 = this.f785m;
                                if (j2 > 0) {
                                    this.f785m = j2 - 1;
                                    this.downstream.accept(i);
                                    return;
                                }
                                return;
                            }
                            this.f786n = j - 1;
                        }

                        public boolean cancellationRequested() {
                            return this.f785m == 0 || this.downstream.cancellationRequested();
                        }
                    };
                }
            };
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static LongStream makeLong(AbstractPipeline<?, Long, ?> abstractPipeline, long j, long j2) {
        if (j >= 0) {
            final long j3 = j;
            final long j4 = j2;
            return new LongPipeline.StatefulOp<Long>(abstractPipeline, StreamShape.LONG_VALUE, flags(j2)) {
                /* access modifiers changed from: package-private */
                public Spliterator.OfLong unorderedSkipLimitSpliterator(Spliterator.OfLong ofLong, long j, long j2, long j3) {
                    long j4;
                    long j5;
                    if (j <= j3) {
                        long j6 = j3 - j;
                        j4 = j2 >= 0 ? Math.min(j2, j6) : j6;
                        j5 = 0;
                    } else {
                        j5 = j;
                        j4 = j2;
                    }
                    return new StreamSpliterators.UnorderedSliceSpliterator.OfLong(ofLong, j5, j4);
                }

                /* JADX WARNING: type inference failed for: r16v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
                /* JADX WARNING: Unknown variable types count: 1 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public <P_IN> java.util.Spliterator<java.lang.Long> opEvaluateParallelLazy(java.util.stream.PipelineHelper<java.lang.Long> r15, java.util.Spliterator<P_IN> r16) {
                    /*
                        r14 = this;
                        r1 = r14
                        long r6 = r15.exactOutputSizeIfKnown(r16)
                        r2 = 0
                        int r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
                        if (r0 <= 0) goto L_0x002b
                        r0 = 16384(0x4000, float:2.2959E-41)
                        r3 = r16
                        boolean r0 = r3.hasCharacteristics(r0)
                        if (r0 == 0) goto L_0x002d
                        java.util.stream.StreamSpliterators$SliceSpliterator$OfLong r0 = new java.util.stream.StreamSpliterators$SliceSpliterator$OfLong
                        java.util.Spliterator r2 = r15.wrapSpliterator(r16)
                        r9 = r2
                        java.util.Spliterator$OfLong r9 = (java.util.Spliterator.OfLong) r9
                        long r10 = r5
                        long r1 = r7
                        long r12 = java.util.stream.SliceOps.calcSliceFence(r10, r1)
                        r8 = r0
                        r8.<init>(r9, r10, r12)
                        return r0
                    L_0x002b:
                        r3 = r16
                    L_0x002d:
                        java.util.stream.StreamOpFlag r0 = java.util.stream.StreamOpFlag.ORDERED
                        int r2 = r15.getStreamAndOpFlags()
                        boolean r0 = r0.isKnown(r2)
                        if (r0 != 0) goto L_0x004d
                        java.util.Spliterator r0 = r15.wrapSpliterator(r16)
                        r2 = r0
                        java.util.Spliterator$OfLong r2 = (java.util.Spliterator.OfLong) r2
                        long r3 = r5
                        long r8 = r7
                        r0 = r14
                        r1 = r2
                        r2 = r3
                        r4 = r8
                        java.util.Spliterator$OfLong r0 = r0.unorderedSkipLimitSpliterator(r1, r2, r4, r6)
                        return r0
                    L_0x004d:
                        java.util.stream.SliceOps$SliceTask r9 = new java.util.stream.SliceOps$SliceTask
                        java.util.stream.SliceOps$3$$ExternalSyntheticLambda0 r4 = new java.util.stream.SliceOps$3$$ExternalSyntheticLambda0
                        r4.<init>()
                        long r5 = r5
                        long r7 = r7
                        r0 = r9
                        r1 = r14
                        r2 = r15
                        r3 = r16
                        r0.<init>(r1, r2, r3, r4, r5, r7)
                        java.lang.Object r0 = r9.invoke()
                        java.util.stream.Node r0 = (java.util.stream.Node) r0
                        java.util.Spliterator r0 = r0.spliterator()
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SliceOps.C45363.opEvaluateParallelLazy(java.util.stream.PipelineHelper, java.util.Spliterator):java.util.Spliterator");
                }

                static /* synthetic */ Long[] lambda$opEvaluateParallelLazy$0(int i) {
                    return new Long[i];
                }

                /* JADX WARNING: type inference failed for: r18v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
                /* JADX WARNING: Unknown variable types count: 1 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public <P_IN> java.util.stream.Node<java.lang.Long> opEvaluateParallel(java.util.stream.PipelineHelper<java.lang.Long> r17, java.util.Spliterator<P_IN> r18, java.util.function.IntFunction<java.lang.Long[]> r19) {
                    /*
                        r16 = this;
                        r8 = r16
                        long r6 = r17.exactOutputSizeIfKnown(r18)
                        r0 = 0
                        int r0 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                        r9 = 1
                        if (r0 <= 0) goto L_0x002f
                        r0 = 16384(0x4000, float:2.2959E-41)
                        r3 = r18
                        boolean r0 = r3.hasCharacteristics(r0)
                        if (r0 == 0) goto L_0x002c
                        java.util.stream.StreamShape r10 = r17.getSourceShape()
                        long r12 = r5
                        long r14 = r7
                        r11 = r18
                        java.util.Spliterator r0 = java.util.stream.SliceOps.sliceSpliterator(r10, r11, r12, r14)
                        r2 = r17
                        java.util.stream.Node$OfLong r0 = java.util.stream.Nodes.collectLong(r2, r0, r9)
                        return r0
                    L_0x002c:
                        r2 = r17
                        goto L_0x0033
                    L_0x002f:
                        r2 = r17
                        r3 = r18
                    L_0x0033:
                        java.util.stream.StreamOpFlag r0 = java.util.stream.StreamOpFlag.ORDERED
                        int r1 = r17.getStreamAndOpFlags()
                        boolean r0 = r0.isKnown(r1)
                        if (r0 != 0) goto L_0x0055
                        java.util.Spliterator r0 = r17.wrapSpliterator(r18)
                        r1 = r0
                        java.util.Spliterator$OfLong r1 = (java.util.Spliterator.OfLong) r1
                        long r2 = r5
                        long r4 = r7
                        r0 = r16
                        java.util.Spliterator$OfLong r0 = r0.unorderedSkipLimitSpliterator(r1, r2, r4, r6)
                        java.util.stream.Node$OfLong r0 = java.util.stream.Nodes.collectLong(r8, r0, r9)
                        return r0
                    L_0x0055:
                        java.util.stream.SliceOps$SliceTask r9 = new java.util.stream.SliceOps$SliceTask
                        long r5 = r5
                        long r10 = r7
                        r0 = r9
                        r1 = r16
                        r2 = r17
                        r3 = r18
                        r4 = r19
                        r7 = r10
                        r0.<init>(r1, r2, r3, r4, r5, r7)
                        java.lang.Object r0 = r9.invoke()
                        java.util.stream.Node r0 = (java.util.stream.Node) r0
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SliceOps.C45363.opEvaluateParallel(java.util.stream.PipelineHelper, java.util.Spliterator, java.util.function.IntFunction):java.util.stream.Node");
                }

                public Sink<Long> opWrapSink(int i, Sink<Long> sink) {
                    return new Sink.ChainedLong<Long>(sink) {

                        /* renamed from: m */
                        long f787m;

                        /* renamed from: n */
                        long f788n;

                        {
                            this.f788n = j3;
                            this.f787m = j4 >= 0 ? j4 : Long.MAX_VALUE;
                        }

                        public void begin(long j) {
                            this.downstream.begin(SliceOps.calcSize(j, j3, this.f787m));
                        }

                        public void accept(long j) {
                            long j2 = this.f788n;
                            if (j2 == 0) {
                                long j3 = this.f787m;
                                if (j3 > 0) {
                                    this.f787m = j3 - 1;
                                    this.downstream.accept(j);
                                    return;
                                }
                                return;
                            }
                            this.f788n = j2 - 1;
                        }

                        public boolean cancellationRequested() {
                            return this.f787m == 0 || this.downstream.cancellationRequested();
                        }
                    };
                }
            };
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static DoubleStream makeDouble(AbstractPipeline<?, Double, ?> abstractPipeline, long j, long j2) {
        if (j >= 0) {
            final long j3 = j;
            final long j4 = j2;
            return new DoublePipeline.StatefulOp<Double>(abstractPipeline, StreamShape.DOUBLE_VALUE, flags(j2)) {
                /* access modifiers changed from: package-private */
                public Spliterator.OfDouble unorderedSkipLimitSpliterator(Spliterator.OfDouble ofDouble, long j, long j2, long j3) {
                    long j4;
                    long j5;
                    if (j <= j3) {
                        long j6 = j3 - j;
                        j4 = j2 >= 0 ? Math.min(j2, j6) : j6;
                        j5 = 0;
                    } else {
                        j5 = j;
                        j4 = j2;
                    }
                    return new StreamSpliterators.UnorderedSliceSpliterator.OfDouble(ofDouble, j5, j4);
                }

                /* JADX WARNING: type inference failed for: r16v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
                /* JADX WARNING: Unknown variable types count: 1 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public <P_IN> java.util.Spliterator<java.lang.Double> opEvaluateParallelLazy(java.util.stream.PipelineHelper<java.lang.Double> r15, java.util.Spliterator<P_IN> r16) {
                    /*
                        r14 = this;
                        r1 = r14
                        long r6 = r15.exactOutputSizeIfKnown(r16)
                        r2 = 0
                        int r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
                        if (r0 <= 0) goto L_0x002b
                        r0 = 16384(0x4000, float:2.2959E-41)
                        r3 = r16
                        boolean r0 = r3.hasCharacteristics(r0)
                        if (r0 == 0) goto L_0x002d
                        java.util.stream.StreamSpliterators$SliceSpliterator$OfDouble r0 = new java.util.stream.StreamSpliterators$SliceSpliterator$OfDouble
                        java.util.Spliterator r2 = r15.wrapSpliterator(r16)
                        r9 = r2
                        java.util.Spliterator$OfDouble r9 = (java.util.Spliterator.OfDouble) r9
                        long r10 = r5
                        long r1 = r7
                        long r12 = java.util.stream.SliceOps.calcSliceFence(r10, r1)
                        r8 = r0
                        r8.<init>(r9, r10, r12)
                        return r0
                    L_0x002b:
                        r3 = r16
                    L_0x002d:
                        java.util.stream.StreamOpFlag r0 = java.util.stream.StreamOpFlag.ORDERED
                        int r2 = r15.getStreamAndOpFlags()
                        boolean r0 = r0.isKnown(r2)
                        if (r0 != 0) goto L_0x004d
                        java.util.Spliterator r0 = r15.wrapSpliterator(r16)
                        r2 = r0
                        java.util.Spliterator$OfDouble r2 = (java.util.Spliterator.OfDouble) r2
                        long r3 = r5
                        long r8 = r7
                        r0 = r14
                        r1 = r2
                        r2 = r3
                        r4 = r8
                        java.util.Spliterator$OfDouble r0 = r0.unorderedSkipLimitSpliterator(r1, r2, r4, r6)
                        return r0
                    L_0x004d:
                        java.util.stream.SliceOps$SliceTask r9 = new java.util.stream.SliceOps$SliceTask
                        java.util.stream.SliceOps$4$$ExternalSyntheticLambda0 r4 = new java.util.stream.SliceOps$4$$ExternalSyntheticLambda0
                        r4.<init>()
                        long r5 = r5
                        long r7 = r7
                        r0 = r9
                        r1 = r14
                        r2 = r15
                        r3 = r16
                        r0.<init>(r1, r2, r3, r4, r5, r7)
                        java.lang.Object r0 = r9.invoke()
                        java.util.stream.Node r0 = (java.util.stream.Node) r0
                        java.util.Spliterator r0 = r0.spliterator()
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SliceOps.C45384.opEvaluateParallelLazy(java.util.stream.PipelineHelper, java.util.Spliterator):java.util.Spliterator");
                }

                static /* synthetic */ Double[] lambda$opEvaluateParallelLazy$0(int i) {
                    return new Double[i];
                }

                /* JADX WARNING: type inference failed for: r18v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
                /* JADX WARNING: Unknown variable types count: 1 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public <P_IN> java.util.stream.Node<java.lang.Double> opEvaluateParallel(java.util.stream.PipelineHelper<java.lang.Double> r17, java.util.Spliterator<P_IN> r18, java.util.function.IntFunction<java.lang.Double[]> r19) {
                    /*
                        r16 = this;
                        r8 = r16
                        long r6 = r17.exactOutputSizeIfKnown(r18)
                        r0 = 0
                        int r0 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                        r9 = 1
                        if (r0 <= 0) goto L_0x002f
                        r0 = 16384(0x4000, float:2.2959E-41)
                        r3 = r18
                        boolean r0 = r3.hasCharacteristics(r0)
                        if (r0 == 0) goto L_0x002c
                        java.util.stream.StreamShape r10 = r17.getSourceShape()
                        long r12 = r5
                        long r14 = r7
                        r11 = r18
                        java.util.Spliterator r0 = java.util.stream.SliceOps.sliceSpliterator(r10, r11, r12, r14)
                        r2 = r17
                        java.util.stream.Node$OfDouble r0 = java.util.stream.Nodes.collectDouble(r2, r0, r9)
                        return r0
                    L_0x002c:
                        r2 = r17
                        goto L_0x0033
                    L_0x002f:
                        r2 = r17
                        r3 = r18
                    L_0x0033:
                        java.util.stream.StreamOpFlag r0 = java.util.stream.StreamOpFlag.ORDERED
                        int r1 = r17.getStreamAndOpFlags()
                        boolean r0 = r0.isKnown(r1)
                        if (r0 != 0) goto L_0x0055
                        java.util.Spliterator r0 = r17.wrapSpliterator(r18)
                        r1 = r0
                        java.util.Spliterator$OfDouble r1 = (java.util.Spliterator.OfDouble) r1
                        long r2 = r5
                        long r4 = r7
                        r0 = r16
                        java.util.Spliterator$OfDouble r0 = r0.unorderedSkipLimitSpliterator(r1, r2, r4, r6)
                        java.util.stream.Node$OfDouble r0 = java.util.stream.Nodes.collectDouble(r8, r0, r9)
                        return r0
                    L_0x0055:
                        java.util.stream.SliceOps$SliceTask r9 = new java.util.stream.SliceOps$SliceTask
                        long r5 = r5
                        long r10 = r7
                        r0 = r9
                        r1 = r16
                        r2 = r17
                        r3 = r18
                        r4 = r19
                        r7 = r10
                        r0.<init>(r1, r2, r3, r4, r5, r7)
                        java.lang.Object r0 = r9.invoke()
                        java.util.stream.Node r0 = (java.util.stream.Node) r0
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SliceOps.C45384.opEvaluateParallel(java.util.stream.PipelineHelper, java.util.Spliterator, java.util.function.IntFunction):java.util.stream.Node");
                }

                public Sink<Double> opWrapSink(int i, Sink<Double> sink) {
                    return new Sink.ChainedDouble<Double>(sink) {

                        /* renamed from: m */
                        long f789m;

                        /* renamed from: n */
                        long f790n;

                        {
                            this.f790n = j3;
                            this.f789m = j4 >= 0 ? j4 : Long.MAX_VALUE;
                        }

                        public void begin(long j) {
                            this.downstream.begin(SliceOps.calcSize(j, j3, this.f789m));
                        }

                        public void accept(double d) {
                            long j = this.f790n;
                            if (j == 0) {
                                long j2 = this.f789m;
                                if (j2 > 0) {
                                    this.f789m = j2 - 1;
                                    this.downstream.accept(d);
                                    return;
                                }
                                return;
                            }
                            this.f790n = j - 1;
                        }

                        public boolean cancellationRequested() {
                            return this.f789m == 0 || this.downstream.cancellationRequested();
                        }
                    };
                }
            };
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    private static int flags(long j) {
        return (j != -1 ? StreamOpFlag.IS_SHORT_CIRCUIT : 0) | StreamOpFlag.NOT_SIZED;
    }

    private static final class SliceTask<P_IN, P_OUT> extends AbstractShortCircuitTask<P_IN, P_OUT, Node<P_OUT>, SliceTask<P_IN, P_OUT>> {
        private volatile boolean completed;
        private final IntFunction<P_OUT[]> generator;

        /* renamed from: op */
        private final AbstractPipeline<P_OUT, P_OUT, ?> f791op;
        private final long targetOffset;
        private final long targetSize;
        private long thisNodeSize;

        SliceTask(AbstractPipeline<P_OUT, P_OUT, ?> abstractPipeline, PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<P_OUT[]> intFunction, long j, long j2) {
            super(pipelineHelper, spliterator);
            this.f791op = abstractPipeline;
            this.generator = intFunction;
            this.targetOffset = j;
            this.targetSize = j2;
        }

        SliceTask(SliceTask<P_IN, P_OUT> sliceTask, Spliterator<P_IN> spliterator) {
            super(sliceTask, spliterator);
            this.f791op = sliceTask.f791op;
            this.generator = sliceTask.generator;
            this.targetOffset = sliceTask.targetOffset;
            this.targetSize = sliceTask.targetSize;
        }

        /* access modifiers changed from: protected */
        public SliceTask<P_IN, P_OUT> makeChild(Spliterator<P_IN> spliterator) {
            return new SliceTask<>(this, spliterator);
        }

        /* access modifiers changed from: protected */
        public final Node<P_OUT> getEmptyResult() {
            return Nodes.emptyNode(this.f791op.getOutputShape());
        }

        /* access modifiers changed from: protected */
        public final Node<P_OUT> doLeaf() {
            long j = -1;
            if (isRoot()) {
                if (StreamOpFlag.SIZED.isPreserved(this.f791op.sourceOrOpFlags)) {
                    j = this.f791op.exactOutputSizeIfKnown(this.spliterator);
                }
                Node.Builder<P_OUT> makeNodeBuilder = this.f791op.makeNodeBuilder(j, this.generator);
                this.helper.copyIntoWithCancel(this.helper.wrapSink(this.f791op.opWrapSink(this.helper.getStreamAndOpFlags(), makeNodeBuilder)), this.spliterator);
                return makeNodeBuilder.build();
            }
            Node<P_OUT> build = ((Node.Builder) this.helper.wrapAndCopyInto(this.helper.makeNodeBuilder(-1, this.generator), this.spliterator)).build();
            this.thisNodeSize = build.count();
            this.completed = true;
            this.spliterator = null;
            return build;
        }

        public final void onCompletion(CountedCompleter<?> countedCompleter) {
            Node node;
            if (!isLeaf()) {
                this.thisNodeSize = ((SliceTask) this.leftChild).thisNodeSize + ((SliceTask) this.rightChild).thisNodeSize;
                if (this.canceled) {
                    this.thisNodeSize = 0;
                    node = getEmptyResult();
                } else if (this.thisNodeSize == 0) {
                    node = getEmptyResult();
                } else if (((SliceTask) this.leftChild).thisNodeSize == 0) {
                    node = (Node) ((SliceTask) this.rightChild).getLocalResult();
                } else {
                    node = Nodes.conc(this.f791op.getOutputShape(), (Node) ((SliceTask) this.leftChild).getLocalResult(), (Node) ((SliceTask) this.rightChild).getLocalResult());
                }
                if (isRoot()) {
                    node = doTruncate(node);
                }
                setLocalResult(node);
                this.completed = true;
            }
            if (this.targetSize >= 0 && !isRoot() && isLeftCompleted(this.targetOffset + this.targetSize)) {
                cancelLaterNodes();
            }
            super.onCompletion(countedCompleter);
        }

        /* access modifiers changed from: protected */
        public void cancel() {
            super.cancel();
            if (this.completed) {
                setLocalResult(getEmptyResult());
            }
        }

        private Node<P_OUT> doTruncate(Node<P_OUT> node) {
            return node.truncate(this.targetOffset, this.targetSize >= 0 ? Math.min(node.count(), this.targetOffset + this.targetSize) : this.thisNodeSize, this.generator);
        }

        private boolean isLeftCompleted(long j) {
            SliceTask sliceTask;
            long completedSize = this.completed ? this.thisNodeSize : completedSize(j);
            if (completedSize >= j) {
                return true;
            }
            long j2 = completedSize;
            SliceTask sliceTask2 = this;
            long j3 = j2;
            for (SliceTask sliceTask3 = (SliceTask) getParent(); sliceTask3 != null; sliceTask3 = (SliceTask) sliceTask3.getParent()) {
                if (sliceTask2 == sliceTask3.rightChild && (sliceTask = (SliceTask) sliceTask3.leftChild) != null) {
                    j3 += sliceTask.completedSize(j);
                    if (j3 >= j) {
                        return true;
                    }
                }
                sliceTask2 = sliceTask3;
            }
            if (j3 >= j) {
                return true;
            }
            return false;
        }

        private long completedSize(long j) {
            if (this.completed) {
                return this.thisNodeSize;
            }
            SliceTask sliceTask = (SliceTask) this.leftChild;
            SliceTask sliceTask2 = (SliceTask) this.rightChild;
            if (sliceTask == null || sliceTask2 == null) {
                return this.thisNodeSize;
            }
            long completedSize = sliceTask.completedSize(j);
            return completedSize >= j ? completedSize : completedSize + sliceTask2.completedSize(j);
        }
    }
}
