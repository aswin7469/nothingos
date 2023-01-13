package java.util.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.DoublePipeline;
import java.util.stream.IntPipeline;
import java.util.stream.LongPipeline;
import java.util.stream.ReferencePipeline;
import java.util.stream.Sink;
import java.util.stream.SpinedBuffer;

final class SortedOps {
    private SortedOps() {
    }

    static <T> Stream<T> makeRef(AbstractPipeline<?, T, ?> abstractPipeline) {
        return new OfRef(abstractPipeline);
    }

    static <T> Stream<T> makeRef(AbstractPipeline<?, T, ?> abstractPipeline, Comparator<? super T> comparator) {
        return new OfRef(abstractPipeline, comparator);
    }

    static <T> IntStream makeInt(AbstractPipeline<?, Integer, ?> abstractPipeline) {
        return new OfInt(abstractPipeline);
    }

    static <T> LongStream makeLong(AbstractPipeline<?, Long, ?> abstractPipeline) {
        return new OfLong(abstractPipeline);
    }

    static <T> DoubleStream makeDouble(AbstractPipeline<?, Double, ?> abstractPipeline) {
        return new OfDouble(abstractPipeline);
    }

    private static final class OfRef<T> extends ReferencePipeline.StatefulOp<T, T> {
        private final Comparator<? super T> comparator;
        private final boolean isNaturalSort;

        OfRef(AbstractPipeline<?, T, ?> abstractPipeline) {
            super(abstractPipeline, StreamShape.REFERENCE, StreamOpFlag.IS_ORDERED | StreamOpFlag.IS_SORTED);
            this.isNaturalSort = true;
            this.comparator = Comparator.naturalOrder();
        }

        OfRef(AbstractPipeline<?, T, ?> abstractPipeline, Comparator<? super T> comparator2) {
            super(abstractPipeline, StreamShape.REFERENCE, StreamOpFlag.IS_ORDERED | StreamOpFlag.NOT_SORTED);
            this.isNaturalSort = false;
            this.comparator = (Comparator) Objects.requireNonNull(comparator2);
        }

        public Sink<T> opWrapSink(int i, Sink<T> sink) {
            Objects.requireNonNull(sink);
            if (StreamOpFlag.SORTED.isKnown(i) && this.isNaturalSort) {
                return sink;
            }
            if (StreamOpFlag.SIZED.isKnown(i)) {
                return new SizedRefSortingSink(sink, this.comparator);
            }
            return new RefSortingSink(sink, this.comparator);
        }

        /* JADX WARNING: type inference failed for: r4v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
        /* JADX WARNING: type inference failed for: r5v0, types: [java.util.function.IntFunction<T[]>, java.util.function.IntFunction] */
        /* JADX WARNING: Unknown variable types count: 2 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public <P_IN> java.util.stream.Node<T> opEvaluateParallel(java.util.stream.PipelineHelper<T> r3, java.util.Spliterator<P_IN> r4, java.util.function.IntFunction<T[]> r5) {
            /*
                r2 = this;
                java.util.stream.StreamOpFlag r0 = java.util.stream.StreamOpFlag.SORTED
                int r1 = r3.getStreamAndOpFlags()
                boolean r0 = r0.isKnown(r1)
                if (r0 == 0) goto L_0x0016
                boolean r0 = r2.isNaturalSort
                if (r0 == 0) goto L_0x0016
                r2 = 0
                java.util.stream.Node r2 = r3.evaluate(r4, r2, r5)
                return r2
            L_0x0016:
                r0 = 1
                java.util.stream.Node r3 = r3.evaluate(r4, r0, r5)
                java.lang.Object[] r3 = r3.asArray(r5)
                java.util.Comparator<? super T> r2 = r2.comparator
                java.util.Arrays.parallelSort(r3, r2)
                java.util.stream.Node r2 = java.util.stream.Nodes.node((T[]) r3)
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SortedOps.OfRef.opEvaluateParallel(java.util.stream.PipelineHelper, java.util.Spliterator, java.util.function.IntFunction):java.util.stream.Node");
        }
    }

    private static final class OfInt extends IntPipeline.StatefulOp<Integer> {
        OfInt(AbstractPipeline<?, Integer, ?> abstractPipeline) {
            super(abstractPipeline, StreamShape.INT_VALUE, StreamOpFlag.IS_ORDERED | StreamOpFlag.IS_SORTED);
        }

        public Sink<Integer> opWrapSink(int i, Sink<Integer> sink) {
            Objects.requireNonNull(sink);
            if (StreamOpFlag.SORTED.isKnown(i)) {
                return sink;
            }
            if (StreamOpFlag.SIZED.isKnown(i)) {
                return new SizedIntSortingSink(sink);
            }
            return new IntSortingSink(sink);
        }

        /* JADX WARNING: type inference failed for: r3v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
        /* JADX WARNING: type inference failed for: r4v0, types: [java.util.function.IntFunction<java.lang.Integer[]>, java.util.function.IntFunction] */
        /* JADX WARNING: Unknown variable types count: 2 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public <P_IN> java.util.stream.Node<java.lang.Integer> opEvaluateParallel(java.util.stream.PipelineHelper<java.lang.Integer> r2, java.util.Spliterator<P_IN> r3, java.util.function.IntFunction<java.lang.Integer[]> r4) {
            /*
                r1 = this;
                java.util.stream.StreamOpFlag r1 = java.util.stream.StreamOpFlag.SORTED
                int r0 = r2.getStreamAndOpFlags()
                boolean r1 = r1.isKnown(r0)
                if (r1 == 0) goto L_0x0012
                r1 = 0
                java.util.stream.Node r1 = r2.evaluate(r3, r1, r4)
                return r1
            L_0x0012:
                r1 = 1
                java.util.stream.Node r1 = r2.evaluate(r3, r1, r4)
                java.util.stream.Node$OfInt r1 = (java.util.stream.Node.OfInt) r1
                java.lang.Object r1 = r1.asPrimitiveArray()
                int[] r1 = (int[]) r1
                java.util.Arrays.parallelSort((int[]) r1)
                java.util.stream.Node$OfInt r1 = java.util.stream.Nodes.node((int[]) r1)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SortedOps.OfInt.opEvaluateParallel(java.util.stream.PipelineHelper, java.util.Spliterator, java.util.function.IntFunction):java.util.stream.Node");
        }
    }

    private static final class OfLong extends LongPipeline.StatefulOp<Long> {
        OfLong(AbstractPipeline<?, Long, ?> abstractPipeline) {
            super(abstractPipeline, StreamShape.LONG_VALUE, StreamOpFlag.IS_ORDERED | StreamOpFlag.IS_SORTED);
        }

        public Sink<Long> opWrapSink(int i, Sink<Long> sink) {
            Objects.requireNonNull(sink);
            if (StreamOpFlag.SORTED.isKnown(i)) {
                return sink;
            }
            if (StreamOpFlag.SIZED.isKnown(i)) {
                return new SizedLongSortingSink(sink);
            }
            return new LongSortingSink(sink);
        }

        /* JADX WARNING: type inference failed for: r3v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
        /* JADX WARNING: type inference failed for: r4v0, types: [java.util.function.IntFunction<java.lang.Long[]>, java.util.function.IntFunction] */
        /* JADX WARNING: Unknown variable types count: 2 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public <P_IN> java.util.stream.Node<java.lang.Long> opEvaluateParallel(java.util.stream.PipelineHelper<java.lang.Long> r2, java.util.Spliterator<P_IN> r3, java.util.function.IntFunction<java.lang.Long[]> r4) {
            /*
                r1 = this;
                java.util.stream.StreamOpFlag r1 = java.util.stream.StreamOpFlag.SORTED
                int r0 = r2.getStreamAndOpFlags()
                boolean r1 = r1.isKnown(r0)
                if (r1 == 0) goto L_0x0012
                r1 = 0
                java.util.stream.Node r1 = r2.evaluate(r3, r1, r4)
                return r1
            L_0x0012:
                r1 = 1
                java.util.stream.Node r1 = r2.evaluate(r3, r1, r4)
                java.util.stream.Node$OfLong r1 = (java.util.stream.Node.OfLong) r1
                java.lang.Object r1 = r1.asPrimitiveArray()
                long[] r1 = (long[]) r1
                java.util.Arrays.parallelSort((long[]) r1)
                java.util.stream.Node$OfLong r1 = java.util.stream.Nodes.node((long[]) r1)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SortedOps.OfLong.opEvaluateParallel(java.util.stream.PipelineHelper, java.util.Spliterator, java.util.function.IntFunction):java.util.stream.Node");
        }
    }

    private static final class OfDouble extends DoublePipeline.StatefulOp<Double> {
        OfDouble(AbstractPipeline<?, Double, ?> abstractPipeline) {
            super(abstractPipeline, StreamShape.DOUBLE_VALUE, StreamOpFlag.IS_ORDERED | StreamOpFlag.IS_SORTED);
        }

        public Sink<Double> opWrapSink(int i, Sink<Double> sink) {
            Objects.requireNonNull(sink);
            if (StreamOpFlag.SORTED.isKnown(i)) {
                return sink;
            }
            if (StreamOpFlag.SIZED.isKnown(i)) {
                return new SizedDoubleSortingSink(sink);
            }
            return new DoubleSortingSink(sink);
        }

        /* JADX WARNING: type inference failed for: r3v0, types: [java.util.Spliterator, java.util.Spliterator<P_IN>] */
        /* JADX WARNING: type inference failed for: r4v0, types: [java.util.function.IntFunction<java.lang.Double[]>, java.util.function.IntFunction] */
        /* JADX WARNING: Unknown variable types count: 2 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public <P_IN> java.util.stream.Node<java.lang.Double> opEvaluateParallel(java.util.stream.PipelineHelper<java.lang.Double> r2, java.util.Spliterator<P_IN> r3, java.util.function.IntFunction<java.lang.Double[]> r4) {
            /*
                r1 = this;
                java.util.stream.StreamOpFlag r1 = java.util.stream.StreamOpFlag.SORTED
                int r0 = r2.getStreamAndOpFlags()
                boolean r1 = r1.isKnown(r0)
                if (r1 == 0) goto L_0x0012
                r1 = 0
                java.util.stream.Node r1 = r2.evaluate(r3, r1, r4)
                return r1
            L_0x0012:
                r1 = 1
                java.util.stream.Node r1 = r2.evaluate(r3, r1, r4)
                java.util.stream.Node$OfDouble r1 = (java.util.stream.Node.OfDouble) r1
                java.lang.Object r1 = r1.asPrimitiveArray()
                double[] r1 = (double[]) r1
                java.util.Arrays.parallelSort((double[]) r1)
                java.util.stream.Node$OfDouble r1 = java.util.stream.Nodes.node((double[]) r1)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.stream.SortedOps.OfDouble.opEvaluateParallel(java.util.stream.PipelineHelper, java.util.Spliterator, java.util.function.IntFunction):java.util.stream.Node");
        }
    }

    private static abstract class AbstractRefSortingSink<T> extends Sink.ChainedReference<T, T> {
        protected boolean cancellationWasRequested;
        protected final Comparator<? super T> comparator;

        AbstractRefSortingSink(Sink<? super T> sink, Comparator<? super T> comparator2) {
            super(sink);
            this.comparator = comparator2;
        }

        public final boolean cancellationRequested() {
            this.cancellationWasRequested = true;
            return false;
        }
    }

    private static final class SizedRefSortingSink<T> extends AbstractRefSortingSink<T> {
        private T[] array;
        private int offset;

        SizedRefSortingSink(Sink<? super T> sink, Comparator<? super T> comparator) {
            super(sink, comparator);
        }

        public void begin(long j) {
            if (j < 2147483639) {
                this.array = new Object[((int) j)];
                return;
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }

        public void end() {
            int i = 0;
            Arrays.sort(this.array, 0, this.offset, this.comparator);
            this.downstream.begin((long) this.offset);
            if (!this.cancellationWasRequested) {
                while (i < this.offset) {
                    this.downstream.accept(this.array[i]);
                    i++;
                }
            } else {
                while (i < this.offset && !this.downstream.cancellationRequested()) {
                    this.downstream.accept(this.array[i]);
                    i++;
                }
            }
            this.downstream.end();
            this.array = null;
        }

        public void accept(T t) {
            T[] tArr = this.array;
            int i = this.offset;
            this.offset = i + 1;
            tArr[i] = t;
        }
    }

    private static final class RefSortingSink<T> extends AbstractRefSortingSink<T> {
        private ArrayList<T> list;

        RefSortingSink(Sink<? super T> sink, Comparator<? super T> comparator) {
            super(sink, comparator);
        }

        public void begin(long j) {
            if (j < 2147483639) {
                this.list = j >= 0 ? new ArrayList<>((int) j) : new ArrayList<>();
                return;
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }

        public void end() {
            this.list.sort(this.comparator);
            this.downstream.begin((long) this.list.size());
            if (!this.cancellationWasRequested) {
                ArrayList<T> arrayList = this.list;
                Sink sink = this.downstream;
                Objects.requireNonNull(sink);
                arrayList.forEach(new SortedOps$RefSortingSink$$ExternalSyntheticLambda0(sink));
            } else {
                Iterator<T> it = this.list.iterator();
                while (it.hasNext()) {
                    T next = it.next();
                    if (this.downstream.cancellationRequested()) {
                        break;
                    }
                    this.downstream.accept(next);
                }
            }
            this.downstream.end();
            this.list = null;
        }

        public void accept(T t) {
            this.list.add(t);
        }
    }

    private static abstract class AbstractIntSortingSink extends Sink.ChainedInt<Integer> {
        protected boolean cancellationWasRequested;

        AbstractIntSortingSink(Sink<? super Integer> sink) {
            super(sink);
        }

        public final boolean cancellationRequested() {
            this.cancellationWasRequested = true;
            return false;
        }
    }

    private static final class SizedIntSortingSink extends AbstractIntSortingSink {
        private int[] array;
        private int offset;

        SizedIntSortingSink(Sink<? super Integer> sink) {
            super(sink);
        }

        public void begin(long j) {
            if (j < 2147483639) {
                this.array = new int[((int) j)];
                return;
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }

        public void end() {
            int i = 0;
            Arrays.sort(this.array, 0, this.offset);
            this.downstream.begin((long) this.offset);
            if (!this.cancellationWasRequested) {
                while (i < this.offset) {
                    this.downstream.accept(this.array[i]);
                    i++;
                }
            } else {
                while (i < this.offset && !this.downstream.cancellationRequested()) {
                    this.downstream.accept(this.array[i]);
                    i++;
                }
            }
            this.downstream.end();
            this.array = null;
        }

        public void accept(int i) {
            int[] iArr = this.array;
            int i2 = this.offset;
            this.offset = i2 + 1;
            iArr[i2] = i;
        }
    }

    private static final class IntSortingSink extends AbstractIntSortingSink {

        /* renamed from: b */
        private SpinedBuffer.OfInt f791b;

        IntSortingSink(Sink<? super Integer> sink) {
            super(sink);
        }

        public void begin(long j) {
            if (j < 2147483639) {
                this.f791b = j > 0 ? new SpinedBuffer.OfInt((int) j) : new SpinedBuffer.OfInt();
                return;
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }

        public void end() {
            int[] iArr = (int[]) this.f791b.asPrimitiveArray();
            Arrays.sort(iArr);
            this.downstream.begin((long) iArr.length);
            int i = 0;
            if (!this.cancellationWasRequested) {
                int length = iArr.length;
                while (i < length) {
                    this.downstream.accept(iArr[i]);
                    i++;
                }
            } else {
                int length2 = iArr.length;
                while (i < length2) {
                    int i2 = iArr[i];
                    if (this.downstream.cancellationRequested()) {
                        break;
                    }
                    this.downstream.accept(i2);
                    i++;
                }
            }
            this.downstream.end();
        }

        public void accept(int i) {
            this.f791b.accept(i);
        }
    }

    private static abstract class AbstractLongSortingSink extends Sink.ChainedLong<Long> {
        protected boolean cancellationWasRequested;

        AbstractLongSortingSink(Sink<? super Long> sink) {
            super(sink);
        }

        public final boolean cancellationRequested() {
            this.cancellationWasRequested = true;
            return false;
        }
    }

    private static final class SizedLongSortingSink extends AbstractLongSortingSink {
        private long[] array;
        private int offset;

        SizedLongSortingSink(Sink<? super Long> sink) {
            super(sink);
        }

        public void begin(long j) {
            if (j < 2147483639) {
                this.array = new long[((int) j)];
                return;
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }

        public void end() {
            int i = 0;
            Arrays.sort(this.array, 0, this.offset);
            this.downstream.begin((long) this.offset);
            if (!this.cancellationWasRequested) {
                while (i < this.offset) {
                    this.downstream.accept(this.array[i]);
                    i++;
                }
            } else {
                while (i < this.offset && !this.downstream.cancellationRequested()) {
                    this.downstream.accept(this.array[i]);
                    i++;
                }
            }
            this.downstream.end();
            this.array = null;
        }

        public void accept(long j) {
            long[] jArr = this.array;
            int i = this.offset;
            this.offset = i + 1;
            jArr[i] = j;
        }
    }

    private static final class LongSortingSink extends AbstractLongSortingSink {

        /* renamed from: b */
        private SpinedBuffer.OfLong f792b;

        LongSortingSink(Sink<? super Long> sink) {
            super(sink);
        }

        public void begin(long j) {
            if (j < 2147483639) {
                this.f792b = j > 0 ? new SpinedBuffer.OfLong((int) j) : new SpinedBuffer.OfLong();
                return;
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }

        public void end() {
            long[] jArr = (long[]) this.f792b.asPrimitiveArray();
            Arrays.sort(jArr);
            this.downstream.begin((long) jArr.length);
            int i = 0;
            if (!this.cancellationWasRequested) {
                int length = jArr.length;
                while (i < length) {
                    this.downstream.accept(jArr[i]);
                    i++;
                }
            } else {
                int length2 = jArr.length;
                while (i < length2) {
                    long j = jArr[i];
                    if (this.downstream.cancellationRequested()) {
                        break;
                    }
                    this.downstream.accept(j);
                    i++;
                }
            }
            this.downstream.end();
        }

        public void accept(long j) {
            this.f792b.accept(j);
        }
    }

    private static abstract class AbstractDoubleSortingSink extends Sink.ChainedDouble<Double> {
        protected boolean cancellationWasRequested;

        AbstractDoubleSortingSink(Sink<? super Double> sink) {
            super(sink);
        }

        public final boolean cancellationRequested() {
            this.cancellationWasRequested = true;
            return false;
        }
    }

    private static final class SizedDoubleSortingSink extends AbstractDoubleSortingSink {
        private double[] array;
        private int offset;

        SizedDoubleSortingSink(Sink<? super Double> sink) {
            super(sink);
        }

        public void begin(long j) {
            if (j < 2147483639) {
                this.array = new double[((int) j)];
                return;
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }

        public void end() {
            int i = 0;
            Arrays.sort(this.array, 0, this.offset);
            this.downstream.begin((long) this.offset);
            if (!this.cancellationWasRequested) {
                while (i < this.offset) {
                    this.downstream.accept(this.array[i]);
                    i++;
                }
            } else {
                while (i < this.offset && !this.downstream.cancellationRequested()) {
                    this.downstream.accept(this.array[i]);
                    i++;
                }
            }
            this.downstream.end();
            this.array = null;
        }

        public void accept(double d) {
            double[] dArr = this.array;
            int i = this.offset;
            this.offset = i + 1;
            dArr[i] = d;
        }
    }

    private static final class DoubleSortingSink extends AbstractDoubleSortingSink {

        /* renamed from: b */
        private SpinedBuffer.OfDouble f790b;

        DoubleSortingSink(Sink<? super Double> sink) {
            super(sink);
        }

        public void begin(long j) {
            if (j < 2147483639) {
                this.f790b = j > 0 ? new SpinedBuffer.OfDouble((int) j) : new SpinedBuffer.OfDouble();
                return;
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }

        public void end() {
            double[] dArr = (double[]) this.f790b.asPrimitiveArray();
            Arrays.sort(dArr);
            this.downstream.begin((long) dArr.length);
            int i = 0;
            if (!this.cancellationWasRequested) {
                int length = dArr.length;
                while (i < length) {
                    this.downstream.accept(dArr[i]);
                    i++;
                }
            } else {
                int length2 = dArr.length;
                while (i < length2) {
                    double d = dArr[i];
                    if (this.downstream.cancellationRequested()) {
                        break;
                    }
                    this.downstream.accept(d);
                    i++;
                }
            }
            this.downstream.end();
        }

        public void accept(double d) {
            this.f790b.accept(d);
        }
    }
}
