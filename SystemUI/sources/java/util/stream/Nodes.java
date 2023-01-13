package java.util.stream;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.CountedCompleter;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.stream.Node;
import java.util.stream.Sink;
import java.util.stream.SpinedBuffer;

final class Nodes {
    static final String BAD_SIZE = "Stream size exceeds max array size";
    /* access modifiers changed from: private */
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    private static final Node.OfDouble EMPTY_DOUBLE_NODE = new EmptyNode.OfDouble();
    /* access modifiers changed from: private */
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    private static final Node.OfInt EMPTY_INT_NODE = new EmptyNode.OfInt();
    /* access modifiers changed from: private */
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    private static final Node.OfLong EMPTY_LONG_NODE = new EmptyNode.OfLong();
    private static final Node EMPTY_NODE = new EmptyNode.OfRef();
    static final long MAX_ARRAY_SIZE = 2147483639;

    private Nodes() {
        throw new Error("no instances");
    }

    /* renamed from: java.util.stream.Nodes$1 */
    static /* synthetic */ class C45091 {
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
            throw new UnsupportedOperationException("Method not decompiled: java.util.stream.Nodes.C45091.<clinit>():void");
        }
    }

    static <T> Node<T> emptyNode(StreamShape streamShape) {
        int i = C45091.$SwitchMap$java$util$stream$StreamShape[streamShape.ordinal()];
        if (i == 1) {
            return EMPTY_NODE;
        }
        if (i == 2) {
            return EMPTY_INT_NODE;
        }
        if (i == 3) {
            return EMPTY_LONG_NODE;
        }
        if (i == 4) {
            return EMPTY_DOUBLE_NODE;
        }
        throw new IllegalStateException("Unknown shape " + streamShape);
    }

    static <T> Node<T> conc(StreamShape streamShape, Node<T> node, Node<T> node2) {
        int i = C45091.$SwitchMap$java$util$stream$StreamShape[streamShape.ordinal()];
        if (i == 1) {
            return new ConcNode(node, node2);
        }
        if (i == 2) {
            return new ConcNode.OfInt((Node.OfInt) node, (Node.OfInt) node2);
        }
        if (i == 3) {
            return new ConcNode.OfLong((Node.OfLong) node, (Node.OfLong) node2);
        }
        if (i == 4) {
            return new ConcNode.OfDouble((Node.OfDouble) node, (Node.OfDouble) node2);
        }
        throw new IllegalStateException("Unknown shape " + streamShape);
    }

    static <T> Node<T> node(T[] tArr) {
        return new ArrayNode(tArr);
    }

    static <T> Node<T> node(Collection<T> collection) {
        return new CollectionNode(collection);
    }

    /* access modifiers changed from: package-private */
    public static <T> Node.Builder<T> builder(long j, IntFunction<T[]> intFunction) {
        if (j < 0 || j >= MAX_ARRAY_SIZE) {
            return builder();
        }
        return new FixedNodeBuilder(j, intFunction);
    }

    static <T> Node.Builder<T> builder() {
        return new SpinedNodeBuilder();
    }

    static Node.OfInt node(int[] iArr) {
        return new IntArrayNode(iArr);
    }

    static Node.Builder.OfInt intBuilder(long j) {
        if (j < 0 || j >= MAX_ARRAY_SIZE) {
            return intBuilder();
        }
        return new IntFixedNodeBuilder(j);
    }

    static Node.Builder.OfInt intBuilder() {
        return new IntSpinedNodeBuilder();
    }

    static Node.OfLong node(long[] jArr) {
        return new LongArrayNode(jArr);
    }

    static Node.Builder.OfLong longBuilder(long j) {
        if (j < 0 || j >= MAX_ARRAY_SIZE) {
            return longBuilder();
        }
        return new LongFixedNodeBuilder(j);
    }

    static Node.Builder.OfLong longBuilder() {
        return new LongSpinedNodeBuilder();
    }

    static Node.OfDouble node(double[] dArr) {
        return new DoubleArrayNode(dArr);
    }

    static Node.Builder.OfDouble doubleBuilder(long j) {
        if (j < 0 || j >= MAX_ARRAY_SIZE) {
            return doubleBuilder();
        }
        return new DoubleFixedNodeBuilder(j);
    }

    static Node.Builder.OfDouble doubleBuilder() {
        return new DoubleSpinedNodeBuilder();
    }

    public static <P_IN, P_OUT> Node<P_OUT> collect(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator, boolean z, IntFunction<P_OUT[]> intFunction) {
        long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
        if (exactOutputSizeIfKnown < 0 || !spliterator.hasCharacteristics(16384)) {
            Node<P_OUT> node = (Node) new CollectorTask.OfRef(pipelineHelper, intFunction, spliterator).invoke();
            return z ? flatten(node, intFunction) : node;
        } else if (exactOutputSizeIfKnown < MAX_ARRAY_SIZE) {
            Object[] objArr = (Object[]) intFunction.apply((int) exactOutputSizeIfKnown);
            new SizedCollectorTask.OfRef(spliterator, pipelineHelper, objArr).invoke();
            return node((T[]) objArr);
        } else {
            throw new IllegalArgumentException(BAD_SIZE);
        }
    }

    public static <P_IN> Node.OfInt collectInt(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator, boolean z) {
        long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
        if (exactOutputSizeIfKnown < 0 || !spliterator.hasCharacteristics(16384)) {
            Node.OfInt ofInt = (Node.OfInt) new CollectorTask.OfInt(pipelineHelper, spliterator).invoke();
            return z ? flattenInt(ofInt) : ofInt;
        } else if (exactOutputSizeIfKnown < MAX_ARRAY_SIZE) {
            int[] iArr = new int[((int) exactOutputSizeIfKnown)];
            new SizedCollectorTask.OfInt(spliterator, pipelineHelper, iArr).invoke();
            return node(iArr);
        } else {
            throw new IllegalArgumentException(BAD_SIZE);
        }
    }

    public static <P_IN> Node.OfLong collectLong(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator, boolean z) {
        long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
        if (exactOutputSizeIfKnown < 0 || !spliterator.hasCharacteristics(16384)) {
            Node.OfLong ofLong = (Node.OfLong) new CollectorTask.OfLong(pipelineHelper, spliterator).invoke();
            return z ? flattenLong(ofLong) : ofLong;
        } else if (exactOutputSizeIfKnown < MAX_ARRAY_SIZE) {
            long[] jArr = new long[((int) exactOutputSizeIfKnown)];
            new SizedCollectorTask.OfLong(spliterator, pipelineHelper, jArr).invoke();
            return node(jArr);
        } else {
            throw new IllegalArgumentException(BAD_SIZE);
        }
    }

    public static <P_IN> Node.OfDouble collectDouble(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator, boolean z) {
        long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
        if (exactOutputSizeIfKnown < 0 || !spliterator.hasCharacteristics(16384)) {
            Node.OfDouble ofDouble = (Node.OfDouble) new CollectorTask.OfDouble(pipelineHelper, spliterator).invoke();
            return z ? flattenDouble(ofDouble) : ofDouble;
        } else if (exactOutputSizeIfKnown < MAX_ARRAY_SIZE) {
            double[] dArr = new double[((int) exactOutputSizeIfKnown)];
            new SizedCollectorTask.OfDouble(spliterator, pipelineHelper, dArr).invoke();
            return node(dArr);
        } else {
            throw new IllegalArgumentException(BAD_SIZE);
        }
    }

    public static <T> Node<T> flatten(Node<T> node, IntFunction<T[]> intFunction) {
        if (node.getChildCount() <= 0) {
            return node;
        }
        long count = node.count();
        if (count < MAX_ARRAY_SIZE) {
            Object[] objArr = (Object[]) intFunction.apply((int) count);
            new ToArrayTask.OfRef(node, objArr, 0).invoke();
            return node((T[]) objArr);
        }
        throw new IllegalArgumentException(BAD_SIZE);
    }

    public static Node.OfInt flattenInt(Node.OfInt ofInt) {
        if (ofInt.getChildCount() <= 0) {
            return ofInt;
        }
        long count = ofInt.count();
        if (count < MAX_ARRAY_SIZE) {
            int[] iArr = new int[((int) count)];
            new ToArrayTask.OfInt(ofInt, iArr, 0).invoke();
            return node(iArr);
        }
        throw new IllegalArgumentException(BAD_SIZE);
    }

    public static Node.OfLong flattenLong(Node.OfLong ofLong) {
        if (ofLong.getChildCount() <= 0) {
            return ofLong;
        }
        long count = ofLong.count();
        if (count < MAX_ARRAY_SIZE) {
            long[] jArr = new long[((int) count)];
            new ToArrayTask.OfLong(ofLong, jArr, 0).invoke();
            return node(jArr);
        }
        throw new IllegalArgumentException(BAD_SIZE);
    }

    public static Node.OfDouble flattenDouble(Node.OfDouble ofDouble) {
        if (ofDouble.getChildCount() <= 0) {
            return ofDouble;
        }
        long count = ofDouble.count();
        if (count < MAX_ARRAY_SIZE) {
            double[] dArr = new double[((int) count)];
            new ToArrayTask.OfDouble(ofDouble, dArr, 0).invoke();
            return node(dArr);
        }
        throw new IllegalArgumentException(BAD_SIZE);
    }

    private static abstract class EmptyNode<T, T_ARR, T_CONS> implements Node<T> {
        public void copyInto(T_ARR t_arr, int i) {
        }

        public long count() {
            return 0;
        }

        public void forEach(T_CONS t_cons) {
        }

        EmptyNode() {
        }

        public T[] asArray(IntFunction<T[]> intFunction) {
            return (Object[]) intFunction.apply(0);
        }

        private static class OfRef<T> extends EmptyNode<T, T[], Consumer<? super T>> {
            public /* bridge */ /* synthetic */ void copyInto(Object[] objArr, int i) {
                super.copyInto(objArr, i);
            }

            public /* bridge */ /* synthetic */ void forEach(Consumer consumer) {
                super.forEach(consumer);
            }

            private OfRef() {
            }

            public Spliterator<T> spliterator() {
                return Spliterators.emptySpliterator();
            }
        }

        private static final class OfInt extends EmptyNode<Integer, int[], IntConsumer> implements Node.OfInt {
            OfInt() {
            }

            public Spliterator.OfInt spliterator() {
                return Spliterators.emptyIntSpliterator();
            }

            public int[] asPrimitiveArray() {
                return Nodes.EMPTY_INT_ARRAY;
            }
        }

        private static final class OfLong extends EmptyNode<Long, long[], LongConsumer> implements Node.OfLong {
            OfLong() {
            }

            public Spliterator.OfLong spliterator() {
                return Spliterators.emptyLongSpliterator();
            }

            public long[] asPrimitiveArray() {
                return Nodes.EMPTY_LONG_ARRAY;
            }
        }

        private static final class OfDouble extends EmptyNode<Double, double[], DoubleConsumer> implements Node.OfDouble {
            OfDouble() {
            }

            public Spliterator.OfDouble spliterator() {
                return Spliterators.emptyDoubleSpliterator();
            }

            public double[] asPrimitiveArray() {
                return Nodes.EMPTY_DOUBLE_ARRAY;
            }
        }
    }

    private static class ArrayNode<T> implements Node<T> {
        final T[] array;
        int curSize;

        ArrayNode(long j, IntFunction<T[]> intFunction) {
            if (j < Nodes.MAX_ARRAY_SIZE) {
                this.array = (Object[]) intFunction.apply((int) j);
                this.curSize = 0;
                return;
            }
            throw new IllegalArgumentException(Nodes.BAD_SIZE);
        }

        ArrayNode(T[] tArr) {
            this.array = tArr;
            this.curSize = tArr.length;
        }

        public Spliterator<T> spliterator() {
            return Arrays.spliterator(this.array, 0, this.curSize);
        }

        public void copyInto(T[] tArr, int i) {
            System.arraycopy((Object) this.array, 0, (Object) tArr, i, this.curSize);
        }

        public T[] asArray(IntFunction<T[]> intFunction) {
            T[] tArr = this.array;
            if (tArr.length == this.curSize) {
                return tArr;
            }
            throw new IllegalStateException();
        }

        public long count() {
            return (long) this.curSize;
        }

        public void forEach(Consumer<? super T> consumer) {
            for (int i = 0; i < this.curSize; i++) {
                consumer.accept(this.array[i]);
            }
        }

        public String toString() {
            return String.format("ArrayNode[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString((Object[]) this.array));
        }
    }

    private static final class CollectionNode<T> implements Node<T> {

        /* renamed from: c */
        private final Collection<T> f779c;

        CollectionNode(Collection<T> collection) {
            this.f779c = collection;
        }

        public Spliterator<T> spliterator() {
            return this.f779c.stream().spliterator();
        }

        public void copyInto(T[] tArr, int i) {
            for (T t : this.f779c) {
                tArr[i] = t;
                i++;
            }
        }

        public T[] asArray(IntFunction<T[]> intFunction) {
            Collection<T> collection = this.f779c;
            return collection.toArray((T[]) (Object[]) intFunction.apply(collection.size()));
        }

        public long count() {
            return (long) this.f779c.size();
        }

        public void forEach(Consumer<? super T> consumer) {
            this.f779c.forEach(consumer);
        }

        public String toString() {
            return String.format("CollectionNode[%d][%s]", Integer.valueOf(this.f779c.size()), this.f779c);
        }
    }

    private static abstract class AbstractConcNode<T, T_NODE extends Node<T>> implements Node<T> {
        protected final T_NODE left;
        protected final T_NODE right;
        private final long size;

        public int getChildCount() {
            return 2;
        }

        AbstractConcNode(T_NODE t_node, T_NODE t_node2) {
            this.left = t_node;
            this.right = t_node2;
            this.size = t_node.count() + t_node2.count();
        }

        public T_NODE getChild(int i) {
            if (i == 0) {
                return this.left;
            }
            if (i == 1) {
                return this.right;
            }
            throw new IndexOutOfBoundsException();
        }

        public long count() {
            return this.size;
        }
    }

    static final class ConcNode<T> extends AbstractConcNode<T, Node<T>> implements Node<T> {
        ConcNode(Node<T> node, Node<T> node2) {
            super(node, node2);
        }

        public Spliterator<T> spliterator() {
            return new InternalNodeSpliterator.OfRef(this);
        }

        public void copyInto(T[] tArr, int i) {
            Objects.requireNonNull(tArr);
            this.left.copyInto(tArr, i);
            this.right.copyInto(tArr, i + ((int) this.left.count()));
        }

        public T[] asArray(IntFunction<T[]> intFunction) {
            long count = count();
            if (count < Nodes.MAX_ARRAY_SIZE) {
                T[] tArr = (Object[]) intFunction.apply((int) count);
                copyInto(tArr, 0);
                return tArr;
            }
            throw new IllegalArgumentException(Nodes.BAD_SIZE);
        }

        public void forEach(Consumer<? super T> consumer) {
            this.left.forEach(consumer);
            this.right.forEach(consumer);
        }

        public Node<T> truncate(long j, long j2, IntFunction<T[]> intFunction) {
            if (j == 0 && j2 == count()) {
                return this;
            }
            long count = this.left.count();
            if (j >= count) {
                return this.right.truncate(j - count, j2 - count, intFunction);
            }
            if (j2 <= count) {
                return this.left.truncate(j, j2, intFunction);
            }
            IntFunction<T[]> intFunction2 = intFunction;
            return Nodes.conc(getShape(), this.left.truncate(j, count, intFunction2), this.right.truncate(0, j2 - count, intFunction2));
        }

        public String toString() {
            if (count() < 32) {
                return String.format("ConcNode[%s.%s]", this.left, this.right);
            }
            return String.format("ConcNode[size=%d]", Long.valueOf(count()));
        }

        private static abstract class OfPrimitive<E, T_CONS, T_ARR, T_SPLITR extends Spliterator.OfPrimitive<E, T_CONS, T_SPLITR>, T_NODE extends Node.OfPrimitive<E, T_CONS, T_ARR, T_SPLITR, T_NODE>> extends AbstractConcNode<E, T_NODE> implements Node.OfPrimitive<E, T_CONS, T_ARR, T_SPLITR, T_NODE> {
            public /* bridge */ /* synthetic */ Node.OfPrimitive getChild(int i) {
                return (Node.OfPrimitive) super.getChild(i);
            }

            OfPrimitive(T_NODE t_node, T_NODE t_node2) {
                super(t_node, t_node2);
            }

            public void forEach(T_CONS t_cons) {
                ((Node.OfPrimitive) this.left).forEach(t_cons);
                ((Node.OfPrimitive) this.right).forEach(t_cons);
            }

            public void copyInto(T_ARR t_arr, int i) {
                ((Node.OfPrimitive) this.left).copyInto(t_arr, i);
                ((Node.OfPrimitive) this.right).copyInto(t_arr, i + ((int) ((Node.OfPrimitive) this.left).count()));
            }

            public T_ARR asPrimitiveArray() {
                long count = count();
                if (count < Nodes.MAX_ARRAY_SIZE) {
                    T_ARR newArray = newArray((int) count);
                    copyInto(newArray, 0);
                    return newArray;
                }
                throw new IllegalArgumentException(Nodes.BAD_SIZE);
            }

            public String toString() {
                if (count() < 32) {
                    return String.format("%s[%s.%s]", getClass().getName(), this.left, this.right);
                }
                return String.format("%s[size=%d]", getClass().getName(), Long.valueOf(count()));
            }
        }

        static final class OfInt extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, Node.OfInt> implements Node.OfInt {
            OfInt(Node.OfInt ofInt, Node.OfInt ofInt2) {
                super(ofInt, ofInt2);
            }

            public Spliterator.OfInt spliterator() {
                return new InternalNodeSpliterator.OfInt(this);
            }
        }

        static final class OfLong extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, Node.OfLong> implements Node.OfLong {
            OfLong(Node.OfLong ofLong, Node.OfLong ofLong2) {
                super(ofLong, ofLong2);
            }

            public Spliterator.OfLong spliterator() {
                return new InternalNodeSpliterator.OfLong(this);
            }
        }

        static final class OfDouble extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, Node.OfDouble> implements Node.OfDouble {
            OfDouble(Node.OfDouble ofDouble, Node.OfDouble ofDouble2) {
                super(ofDouble, ofDouble2);
            }

            public Spliterator.OfDouble spliterator() {
                return new InternalNodeSpliterator.OfDouble(this);
            }
        }
    }

    private static abstract class InternalNodeSpliterator<T, S extends Spliterator<T>, N extends Node<T>> implements Spliterator<T> {
        int curChildIndex;
        N curNode;
        S lastNodeSpliterator;
        S tryAdvanceSpliterator;
        Deque<N> tryAdvanceStack;

        public final int characteristics() {
            return 64;
        }

        InternalNodeSpliterator(N n) {
            this.curNode = n;
        }

        /* access modifiers changed from: protected */
        public final Deque<N> initStack() {
            ArrayDeque arrayDeque = new ArrayDeque(8);
            int childCount = this.curNode.getChildCount();
            while (true) {
                childCount--;
                if (childCount < this.curChildIndex) {
                    return arrayDeque;
                }
                arrayDeque.addFirst(this.curNode.getChild(childCount));
            }
        }

        /* access modifiers changed from: protected */
        public final N findNextLeafNode(Deque<N> deque) {
            while (true) {
                N n = (Node) deque.pollFirst();
                if (n == null) {
                    return null;
                }
                if (n.getChildCount() != 0) {
                    for (int childCount = n.getChildCount() - 1; childCount >= 0; childCount--) {
                        deque.addFirst(n.getChild(childCount));
                    }
                } else if (n.count() > 0) {
                    return n;
                }
            }
        }

        /* access modifiers changed from: protected */
        public final boolean initTryAdvance() {
            if (this.curNode == null) {
                return false;
            }
            if (this.tryAdvanceSpliterator != null) {
                return true;
            }
            S s = this.lastNodeSpliterator;
            if (s == null) {
                Deque<N> initStack = initStack();
                this.tryAdvanceStack = initStack;
                Node findNextLeafNode = findNextLeafNode(initStack);
                if (findNextLeafNode != null) {
                    this.tryAdvanceSpliterator = findNextLeafNode.spliterator();
                    return true;
                }
                this.curNode = null;
                return false;
            }
            this.tryAdvanceSpliterator = s;
            return true;
        }

        public final S trySplit() {
            N n = this.curNode;
            if (n == null || this.tryAdvanceSpliterator != null) {
                return null;
            }
            S s = this.lastNodeSpliterator;
            if (s != null) {
                return s.trySplit();
            }
            if (this.curChildIndex < n.getChildCount() - 1) {
                N n2 = this.curNode;
                int i = this.curChildIndex;
                this.curChildIndex = i + 1;
                return n2.getChild(i).spliterator();
            }
            N child = this.curNode.getChild(this.curChildIndex);
            this.curNode = child;
            if (child.getChildCount() == 0) {
                S spliterator = this.curNode.spliterator();
                this.lastNodeSpliterator = spliterator;
                return spliterator.trySplit();
            }
            N n3 = this.curNode;
            this.curChildIndex = 0 + 1;
            return n3.getChild(0).spliterator();
        }

        public final long estimateSize() {
            long j = 0;
            if (this.curNode == null) {
                return 0;
            }
            S s = this.lastNodeSpliterator;
            if (s != null) {
                return s.estimateSize();
            }
            for (int i = this.curChildIndex; i < this.curNode.getChildCount(); i++) {
                j += this.curNode.getChild(i).count();
            }
            return j;
        }

        private static final class OfRef<T> extends InternalNodeSpliterator<T, Spliterator<T>, Node<T>> {
            OfRef(Node<T> node) {
                super(node);
            }

            public boolean tryAdvance(Consumer<? super T> consumer) {
                Node findNextLeafNode;
                if (!initTryAdvance()) {
                    return false;
                }
                boolean tryAdvance = this.tryAdvanceSpliterator.tryAdvance(consumer);
                if (!tryAdvance) {
                    if (this.lastNodeSpliterator != null || (findNextLeafNode = findNextLeafNode(this.tryAdvanceStack)) == null) {
                        this.curNode = null;
                    } else {
                        this.tryAdvanceSpliterator = findNextLeafNode.spliterator();
                        return this.tryAdvanceSpliterator.tryAdvance(consumer);
                    }
                }
                return tryAdvance;
            }

            public void forEachRemaining(Consumer<? super T> consumer) {
                if (this.curNode != null) {
                    if (this.tryAdvanceSpliterator != null) {
                        do {
                        } while (tryAdvance(consumer));
                    } else if (this.lastNodeSpliterator == null) {
                        Deque initStack = initStack();
                        while (true) {
                            Node findNextLeafNode = findNextLeafNode(initStack);
                            if (findNextLeafNode != null) {
                                findNextLeafNode.forEach(consumer);
                            } else {
                                this.curNode = null;
                                return;
                            }
                        }
                    } else {
                        this.lastNodeSpliterator.forEachRemaining(consumer);
                    }
                }
            }
        }

        private static abstract class OfPrimitive<T, T_CONS, T_ARR, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>, N extends Node.OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, N>> extends InternalNodeSpliterator<T, T_SPLITR, N> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR> {
            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            OfPrimitive(N n) {
                super(n);
            }

            public boolean tryAdvance(T_CONS t_cons) {
                Node.OfPrimitive ofPrimitive;
                if (!initTryAdvance()) {
                    return false;
                }
                boolean tryAdvance = ((Spliterator.OfPrimitive) this.tryAdvanceSpliterator).tryAdvance(t_cons);
                if (!tryAdvance) {
                    if (this.lastNodeSpliterator != null || (ofPrimitive = (Node.OfPrimitive) findNextLeafNode(this.tryAdvanceStack)) == null) {
                        this.curNode = null;
                    } else {
                        this.tryAdvanceSpliterator = ofPrimitive.spliterator();
                        return ((Spliterator.OfPrimitive) this.tryAdvanceSpliterator).tryAdvance(t_cons);
                    }
                }
                return tryAdvance;
            }

            public void forEachRemaining(T_CONS t_cons) {
                if (this.curNode != null) {
                    if (this.tryAdvanceSpliterator != null) {
                        do {
                        } while (tryAdvance(t_cons));
                    } else if (this.lastNodeSpliterator == null) {
                        Deque initStack = initStack();
                        while (true) {
                            Node.OfPrimitive ofPrimitive = (Node.OfPrimitive) findNextLeafNode(initStack);
                            if (ofPrimitive != null) {
                                ofPrimitive.forEach(t_cons);
                            } else {
                                this.curNode = null;
                                return;
                            }
                        }
                    } else {
                        ((Spliterator.OfPrimitive) this.lastNodeSpliterator).forEachRemaining(t_cons);
                    }
                }
            }
        }

        private static final class OfInt extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, Node.OfInt> implements Spliterator.OfInt {
            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining(intConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance(intConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                return (Spliterator.OfInt) super.trySplit();
            }

            OfInt(Node.OfInt ofInt) {
                super(ofInt);
            }
        }

        private static final class OfLong extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, Node.OfLong> implements Spliterator.OfLong {
            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining(longConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance(longConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                return (Spliterator.OfLong) super.trySplit();
            }

            OfLong(Node.OfLong ofLong) {
                super(ofLong);
            }
        }

        private static final class OfDouble extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, Node.OfDouble> implements Spliterator.OfDouble {
            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining(doubleConsumer);
            }

            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance(doubleConsumer);
            }

            public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                return (Spliterator.OfDouble) super.trySplit();
            }

            OfDouble(Node.OfDouble ofDouble) {
                super(ofDouble);
            }
        }
    }

    private static final class FixedNodeBuilder<T> extends ArrayNode<T> implements Node.Builder<T> {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        static {
            Class<Nodes> cls = Nodes.class;
        }

        FixedNodeBuilder(long j, IntFunction<T[]> intFunction) {
            super(j, intFunction);
        }

        public Node<T> build() {
            if (this.curSize >= this.array.length) {
                return this;
            }
            throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
        }

        public void begin(long j) {
            if (j == ((long) this.array.length)) {
                this.curSize = 0;
            } else {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", Long.valueOf(j), Integer.valueOf(this.array.length)));
            }
        }

        public void accept(T t) {
            if (this.curSize < this.array.length) {
                Object[] objArr = this.array;
                int i = this.curSize;
                this.curSize = i + 1;
                objArr[i] = t;
                return;
            }
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", Integer.valueOf(this.array.length)));
        }

        public void end() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
            }
        }

        public String toString() {
            return String.format("FixedNodeBuilder[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    private static final class SpinedNodeBuilder<T> extends SpinedBuffer<T> implements Node<T>, Node.Builder<T> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private boolean building = false;

        public Node<T> build() {
            return this;
        }

        static {
            Class<Nodes> cls = Nodes.class;
        }

        SpinedNodeBuilder() {
        }

        public Spliterator<T> spliterator() {
            return super.spliterator();
        }

        public void forEach(Consumer<? super T> consumer) {
            super.forEach(consumer);
        }

        public void begin(long j) {
            this.building = true;
            clear();
            ensureCapacity(j);
        }

        public void accept(T t) {
            super.accept(t);
        }

        public void end() {
            this.building = false;
        }

        public void copyInto(T[] tArr, int i) {
            super.copyInto(tArr, i);
        }

        public T[] asArray(IntFunction<T[]> intFunction) {
            return super.asArray(intFunction);
        }
    }

    private static class IntArrayNode implements Node.OfInt {
        final int[] array;
        int curSize;

        IntArrayNode(long j) {
            if (j < Nodes.MAX_ARRAY_SIZE) {
                this.array = new int[((int) j)];
                this.curSize = 0;
                return;
            }
            throw new IllegalArgumentException(Nodes.BAD_SIZE);
        }

        IntArrayNode(int[] iArr) {
            this.array = iArr;
            this.curSize = iArr.length;
        }

        public Spliterator.OfInt spliterator() {
            return Arrays.spliterator(this.array, 0, this.curSize);
        }

        public int[] asPrimitiveArray() {
            int[] iArr = this.array;
            int length = iArr.length;
            int i = this.curSize;
            if (length == i) {
                return iArr;
            }
            return Arrays.copyOf(iArr, i);
        }

        public void copyInto(int[] iArr, int i) {
            System.arraycopy((Object) this.array, 0, (Object) iArr, i, this.curSize);
        }

        public long count() {
            return (long) this.curSize;
        }

        public void forEach(IntConsumer intConsumer) {
            for (int i = 0; i < this.curSize; i++) {
                intConsumer.accept(this.array[i]);
            }
        }

        public String toString() {
            return String.format("IntArrayNode[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    private static class LongArrayNode implements Node.OfLong {
        final long[] array;
        int curSize;

        LongArrayNode(long j) {
            if (j < Nodes.MAX_ARRAY_SIZE) {
                this.array = new long[((int) j)];
                this.curSize = 0;
                return;
            }
            throw new IllegalArgumentException(Nodes.BAD_SIZE);
        }

        LongArrayNode(long[] jArr) {
            this.array = jArr;
            this.curSize = jArr.length;
        }

        public Spliterator.OfLong spliterator() {
            return Arrays.spliterator(this.array, 0, this.curSize);
        }

        public long[] asPrimitiveArray() {
            long[] jArr = this.array;
            int length = jArr.length;
            int i = this.curSize;
            if (length == i) {
                return jArr;
            }
            return Arrays.copyOf(jArr, i);
        }

        public void copyInto(long[] jArr, int i) {
            System.arraycopy((Object) this.array, 0, (Object) jArr, i, this.curSize);
        }

        public long count() {
            return (long) this.curSize;
        }

        public void forEach(LongConsumer longConsumer) {
            for (int i = 0; i < this.curSize; i++) {
                longConsumer.accept(this.array[i]);
            }
        }

        public String toString() {
            return String.format("LongArrayNode[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    private static class DoubleArrayNode implements Node.OfDouble {
        final double[] array;
        int curSize;

        DoubleArrayNode(long j) {
            if (j < Nodes.MAX_ARRAY_SIZE) {
                this.array = new double[((int) j)];
                this.curSize = 0;
                return;
            }
            throw new IllegalArgumentException(Nodes.BAD_SIZE);
        }

        DoubleArrayNode(double[] dArr) {
            this.array = dArr;
            this.curSize = dArr.length;
        }

        public Spliterator.OfDouble spliterator() {
            return Arrays.spliterator(this.array, 0, this.curSize);
        }

        public double[] asPrimitiveArray() {
            double[] dArr = this.array;
            int length = dArr.length;
            int i = this.curSize;
            if (length == i) {
                return dArr;
            }
            return Arrays.copyOf(dArr, i);
        }

        public void copyInto(double[] dArr, int i) {
            System.arraycopy((Object) this.array, 0, (Object) dArr, i, this.curSize);
        }

        public long count() {
            return (long) this.curSize;
        }

        public void forEach(DoubleConsumer doubleConsumer) {
            for (int i = 0; i < this.curSize; i++) {
                doubleConsumer.accept(this.array[i]);
            }
        }

        public String toString() {
            return String.format("DoubleArrayNode[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    private static final class IntFixedNodeBuilder extends IntArrayNode implements Node.Builder.OfInt {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        static {
            Class<Nodes> cls = Nodes.class;
        }

        IntFixedNodeBuilder(long j) {
            super(j);
        }

        public Node.OfInt build() {
            if (this.curSize >= this.array.length) {
                return this;
            }
            throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
        }

        public void begin(long j) {
            if (j == ((long) this.array.length)) {
                this.curSize = 0;
            } else {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", Long.valueOf(j), Integer.valueOf(this.array.length)));
            }
        }

        public void accept(int i) {
            if (this.curSize < this.array.length) {
                int[] iArr = this.array;
                int i2 = this.curSize;
                this.curSize = i2 + 1;
                iArr[i2] = i;
                return;
            }
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", Integer.valueOf(this.array.length)));
        }

        public void end() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
            }
        }

        public String toString() {
            return String.format("IntFixedNodeBuilder[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    private static final class LongFixedNodeBuilder extends LongArrayNode implements Node.Builder.OfLong {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        static {
            Class<Nodes> cls = Nodes.class;
        }

        LongFixedNodeBuilder(long j) {
            super(j);
        }

        public Node.OfLong build() {
            if (this.curSize >= this.array.length) {
                return this;
            }
            throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
        }

        public void begin(long j) {
            if (j == ((long) this.array.length)) {
                this.curSize = 0;
            } else {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", Long.valueOf(j), Integer.valueOf(this.array.length)));
            }
        }

        public void accept(long j) {
            if (this.curSize < this.array.length) {
                long[] jArr = this.array;
                int i = this.curSize;
                this.curSize = i + 1;
                jArr[i] = j;
                return;
            }
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", Integer.valueOf(this.array.length)));
        }

        public void end() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
            }
        }

        public String toString() {
            return String.format("LongFixedNodeBuilder[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    private static final class DoubleFixedNodeBuilder extends DoubleArrayNode implements Node.Builder.OfDouble {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        static {
            Class<Nodes> cls = Nodes.class;
        }

        DoubleFixedNodeBuilder(long j) {
            super(j);
        }

        public Node.OfDouble build() {
            if (this.curSize >= this.array.length) {
                return this;
            }
            throw new IllegalStateException(String.format("Current size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
        }

        public void begin(long j) {
            if (j == ((long) this.array.length)) {
                this.curSize = 0;
            } else {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d", Long.valueOf(j), Integer.valueOf(this.array.length)));
            }
        }

        public void accept(double d) {
            if (this.curSize < this.array.length) {
                double[] dArr = this.array;
                int i = this.curSize;
                this.curSize = i + 1;
                dArr[i] = d;
                return;
            }
            throw new IllegalStateException(String.format("Accept exceeded fixed size of %d", Integer.valueOf(this.array.length)));
        }

        public void end() {
            if (this.curSize < this.array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d", Integer.valueOf(this.curSize), Integer.valueOf(this.array.length)));
            }
        }

        public String toString() {
            return String.format("DoubleFixedNodeBuilder[%d][%s]", Integer.valueOf(this.array.length - this.curSize), Arrays.toString(this.array));
        }
    }

    private static final class IntSpinedNodeBuilder extends SpinedBuffer.OfInt implements Node.OfInt, Node.Builder.OfInt {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private boolean building = false;

        public Node.OfInt build() {
            return this;
        }

        static {
            Class<Nodes> cls = Nodes.class;
        }

        IntSpinedNodeBuilder() {
        }

        public Spliterator.OfInt spliterator() {
            return super.spliterator();
        }

        public void forEach(IntConsumer intConsumer) {
            super.forEach((Object) intConsumer);
        }

        public void begin(long j) {
            this.building = true;
            clear();
            ensureCapacity(j);
        }

        public void accept(int i) {
            super.accept(i);
        }

        public void end() {
            this.building = false;
        }

        public void copyInto(int[] iArr, int i) throws IndexOutOfBoundsException {
            super.copyInto(iArr, i);
        }

        public int[] asPrimitiveArray() {
            return (int[]) super.asPrimitiveArray();
        }
    }

    private static final class LongSpinedNodeBuilder extends SpinedBuffer.OfLong implements Node.OfLong, Node.Builder.OfLong {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private boolean building = false;

        public Node.OfLong build() {
            return this;
        }

        static {
            Class<Nodes> cls = Nodes.class;
        }

        LongSpinedNodeBuilder() {
        }

        public Spliterator.OfLong spliterator() {
            return super.spliterator();
        }

        public void forEach(LongConsumer longConsumer) {
            super.forEach((Object) longConsumer);
        }

        public void begin(long j) {
            this.building = true;
            clear();
            ensureCapacity(j);
        }

        public void accept(long j) {
            super.accept(j);
        }

        public void end() {
            this.building = false;
        }

        public void copyInto(long[] jArr, int i) {
            super.copyInto(jArr, i);
        }

        public long[] asPrimitiveArray() {
            return (long[]) super.asPrimitiveArray();
        }
    }

    private static final class DoubleSpinedNodeBuilder extends SpinedBuffer.OfDouble implements Node.OfDouble, Node.Builder.OfDouble {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private boolean building = false;

        public Node.OfDouble build() {
            return this;
        }

        static {
            Class<Nodes> cls = Nodes.class;
        }

        DoubleSpinedNodeBuilder() {
        }

        public Spliterator.OfDouble spliterator() {
            return super.spliterator();
        }

        public void forEach(DoubleConsumer doubleConsumer) {
            super.forEach((Object) doubleConsumer);
        }

        public void begin(long j) {
            this.building = true;
            clear();
            ensureCapacity(j);
        }

        public void accept(double d) {
            super.accept(d);
        }

        public void end() {
            this.building = false;
        }

        public void copyInto(double[] dArr, int i) {
            super.copyInto(dArr, i);
        }

        public double[] asPrimitiveArray() {
            return (double[]) super.asPrimitiveArray();
        }
    }

    private static abstract class SizedCollectorTask<P_IN, P_OUT, T_SINK extends Sink<P_OUT>, K extends SizedCollectorTask<P_IN, P_OUT, T_SINK, K>> extends CountedCompleter<Void> implements Sink<P_OUT> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        protected int fence;
        protected final PipelineHelper<P_OUT> helper;
        protected int index;
        protected long length;
        protected long offset;
        protected final Spliterator<P_IN> spliterator;
        protected final long targetSize;

        /* access modifiers changed from: package-private */
        public abstract K makeChild(Spliterator<P_IN> spliterator2, long j, long j2);

        static {
            Class<Nodes> cls = Nodes.class;
        }

        SizedCollectorTask(Spliterator<P_IN> spliterator2, PipelineHelper<P_OUT> pipelineHelper, int i) {
            this.spliterator = spliterator2;
            this.helper = pipelineHelper;
            this.targetSize = AbstractTask.suggestTargetSize(spliterator2.estimateSize());
            this.offset = 0;
            this.length = (long) i;
        }

        SizedCollectorTask(K k, Spliterator<P_IN> spliterator2, long j, long j2, int i) {
            super(k);
            this.spliterator = spliterator2;
            this.helper = k.helper;
            this.targetSize = k.targetSize;
            this.offset = j;
            this.length = j2;
            if (j < 0 || j2 < 0 || (j + j2) - 1 >= ((long) i)) {
                throw new IllegalArgumentException(String.format("offset and length interval [%d, %d + %d) is not within array size interval [0, %d)", Long.valueOf(j), Long.valueOf(j), Long.valueOf(j2), Integer.valueOf(i)));
            }
        }

        public void compute() {
            Spliterator<P_IN> trySplit;
            Spliterator<P_IN> spliterator2 = this.spliterator;
            while (spliterator2.estimateSize() > this.targetSize && (trySplit = spliterator2.trySplit()) != null) {
                this.setPendingCount(1);
                long estimateSize = trySplit.estimateSize();
                this.makeChild(trySplit, this.offset, estimateSize).fork();
                this = this.makeChild(spliterator2, this.offset + estimateSize, this.length - estimateSize);
            }
            this.helper.wrapAndCopyInto(this, spliterator2);
            this.propagateCompletion();
        }

        public void begin(long j) {
            long j2 = this.length;
            if (j <= j2) {
                int i = (int) this.offset;
                this.index = i;
                this.fence = i + ((int) j2);
                return;
            }
            throw new IllegalStateException("size passed to Sink.begin exceeds array length");
        }

        static final class OfRef<P_IN, P_OUT> extends SizedCollectorTask<P_IN, P_OUT, Sink<P_OUT>, OfRef<P_IN, P_OUT>> implements Sink<P_OUT> {
            private final P_OUT[] array;

            OfRef(Spliterator<P_IN> spliterator, PipelineHelper<P_OUT> pipelineHelper, P_OUT[] p_outArr) {
                super(spliterator, pipelineHelper, p_outArr.length);
                this.array = p_outArr;
            }

            OfRef(OfRef<P_IN, P_OUT> ofRef, Spliterator<P_IN> spliterator, long j, long j2) {
                super(ofRef, spliterator, j, j2, ofRef.array.length);
                this.array = ofRef.array;
            }

            /* access modifiers changed from: package-private */
            public OfRef<P_IN, P_OUT> makeChild(Spliterator<P_IN> spliterator, long j, long j2) {
                return new OfRef(this, spliterator, j, j2);
            }

            public void accept(P_OUT p_out) {
                if (this.index < this.fence) {
                    P_OUT[] p_outArr = this.array;
                    int i = this.index;
                    this.index = i + 1;
                    p_outArr[i] = p_out;
                    return;
                }
                throw new IndexOutOfBoundsException(Integer.toString(this.index));
            }
        }

        static final class OfInt<P_IN> extends SizedCollectorTask<P_IN, Integer, Sink.OfInt, OfInt<P_IN>> implements Sink.OfInt {
            private final int[] array;

            OfInt(Spliterator<P_IN> spliterator, PipelineHelper<Integer> pipelineHelper, int[] iArr) {
                super(spliterator, pipelineHelper, iArr.length);
                this.array = iArr;
            }

            OfInt(OfInt<P_IN> ofInt, Spliterator<P_IN> spliterator, long j, long j2) {
                super(ofInt, spliterator, j, j2, ofInt.array.length);
                this.array = ofInt.array;
            }

            /* access modifiers changed from: package-private */
            public OfInt<P_IN> makeChild(Spliterator<P_IN> spliterator, long j, long j2) {
                return new OfInt(this, spliterator, j, j2);
            }

            public void accept(int i) {
                if (this.index < this.fence) {
                    int[] iArr = this.array;
                    int i2 = this.index;
                    this.index = i2 + 1;
                    iArr[i2] = i;
                    return;
                }
                throw new IndexOutOfBoundsException(Integer.toString(this.index));
            }
        }

        static final class OfLong<P_IN> extends SizedCollectorTask<P_IN, Long, Sink.OfLong, OfLong<P_IN>> implements Sink.OfLong {
            private final long[] array;

            OfLong(Spliterator<P_IN> spliterator, PipelineHelper<Long> pipelineHelper, long[] jArr) {
                super(spliterator, pipelineHelper, jArr.length);
                this.array = jArr;
            }

            OfLong(OfLong<P_IN> ofLong, Spliterator<P_IN> spliterator, long j, long j2) {
                super(ofLong, spliterator, j, j2, ofLong.array.length);
                this.array = ofLong.array;
            }

            /* access modifiers changed from: package-private */
            public OfLong<P_IN> makeChild(Spliterator<P_IN> spliterator, long j, long j2) {
                return new OfLong(this, spliterator, j, j2);
            }

            public void accept(long j) {
                if (this.index < this.fence) {
                    long[] jArr = this.array;
                    int i = this.index;
                    this.index = i + 1;
                    jArr[i] = j;
                    return;
                }
                throw new IndexOutOfBoundsException(Integer.toString(this.index));
            }
        }

        static final class OfDouble<P_IN> extends SizedCollectorTask<P_IN, Double, Sink.OfDouble, OfDouble<P_IN>> implements Sink.OfDouble {
            private final double[] array;

            OfDouble(Spliterator<P_IN> spliterator, PipelineHelper<Double> pipelineHelper, double[] dArr) {
                super(spliterator, pipelineHelper, dArr.length);
                this.array = dArr;
            }

            OfDouble(OfDouble<P_IN> ofDouble, Spliterator<P_IN> spliterator, long j, long j2) {
                super(ofDouble, spliterator, j, j2, ofDouble.array.length);
                this.array = ofDouble.array;
            }

            /* access modifiers changed from: package-private */
            public OfDouble<P_IN> makeChild(Spliterator<P_IN> spliterator, long j, long j2) {
                return new OfDouble(this, spliterator, j, j2);
            }

            public void accept(double d) {
                if (this.index < this.fence) {
                    double[] dArr = this.array;
                    int i = this.index;
                    this.index = i + 1;
                    dArr[i] = d;
                    return;
                }
                throw new IndexOutOfBoundsException(Integer.toString(this.index));
            }
        }
    }

    private static abstract class ToArrayTask<T, T_NODE extends Node<T>, K extends ToArrayTask<T, T_NODE, K>> extends CountedCompleter<Void> {
        protected final T_NODE node;
        protected final int offset;

        /* access modifiers changed from: package-private */
        public abstract void copyNodeToArray();

        /* access modifiers changed from: package-private */
        public abstract K makeChild(int i, int i2);

        ToArrayTask(T_NODE t_node, int i) {
            this.node = t_node;
            this.offset = i;
        }

        ToArrayTask(K k, T_NODE t_node, int i) {
            super(k);
            this.node = t_node;
            this.offset = i;
        }

        public void compute() {
            while (this.node.getChildCount() != 0) {
                this.setPendingCount(this.node.getChildCount() - 1);
                int i = 0;
                int i2 = 0;
                while (i < this.node.getChildCount() - 1) {
                    ToArrayTask makeChild = this.makeChild(i, this.offset + i2);
                    i2 = (int) (((long) i2) + makeChild.node.count());
                    makeChild.fork();
                    i++;
                }
                this = this.makeChild(i, this.offset + i2);
            }
            this.copyNodeToArray();
            this.propagateCompletion();
        }

        private static final class OfRef<T> extends ToArrayTask<T, Node<T>, OfRef<T>> {
            private final T[] array;

            private OfRef(Node<T> node, T[] tArr, int i) {
                super(node, i);
                this.array = tArr;
            }

            private OfRef(OfRef<T> ofRef, Node<T> node, int i) {
                super(ofRef, node, i);
                this.array = ofRef.array;
            }

            /* access modifiers changed from: package-private */
            public OfRef<T> makeChild(int i, int i2) {
                return new OfRef<>(this, this.node.getChild(i), i2);
            }

            /* access modifiers changed from: package-private */
            public void copyNodeToArray() {
                this.node.copyInto(this.array, this.offset);
            }
        }

        private static class OfPrimitive<T, T_CONS, T_ARR, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>, T_NODE extends Node.OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE>> extends ToArrayTask<T, T_NODE, OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE>> {
            private final T_ARR array;

            private OfPrimitive(T_NODE t_node, T_ARR t_arr, int i) {
                super(t_node, i);
                this.array = t_arr;
            }

            private OfPrimitive(OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE> ofPrimitive, T_NODE t_node, int i) {
                super(ofPrimitive, t_node, i);
                this.array = ofPrimitive.array;
            }

            /* access modifiers changed from: package-private */
            public OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE> makeChild(int i, int i2) {
                return new OfPrimitive<>(this, ((Node.OfPrimitive) this.node).getChild(i), i2);
            }

            /* access modifiers changed from: package-private */
            public void copyNodeToArray() {
                ((Node.OfPrimitive) this.node).copyInto(this.array, this.offset);
            }
        }

        private static final class OfInt extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, Node.OfInt> {
            private OfInt(Node.OfInt ofInt, int[] iArr, int i) {
                super(ofInt, iArr, i);
            }
        }

        private static final class OfLong extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, Node.OfLong> {
            private OfLong(Node.OfLong ofLong, long[] jArr, int i) {
                super(ofLong, jArr, i);
            }
        }

        private static final class OfDouble extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, Node.OfDouble> {
            private OfDouble(Node.OfDouble ofDouble, double[] dArr, int i) {
                super(ofDouble, dArr, i);
            }
        }
    }

    private static class CollectorTask<P_IN, P_OUT, T_NODE extends Node<P_OUT>, T_BUILDER extends Node.Builder<P_OUT>> extends AbstractTask<P_IN, P_OUT, T_NODE, CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER>> {
        protected final LongFunction<T_BUILDER> builderFactory;
        protected final BinaryOperator<T_NODE> concFactory;
        protected final PipelineHelper<P_OUT> helper;

        CollectorTask(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator, LongFunction<T_BUILDER> longFunction, BinaryOperator<T_NODE> binaryOperator) {
            super(pipelineHelper, spliterator);
            this.helper = pipelineHelper;
            this.builderFactory = longFunction;
            this.concFactory = binaryOperator;
        }

        CollectorTask(CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER> collectorTask, Spliterator<P_IN> spliterator) {
            super(collectorTask, spliterator);
            this.helper = collectorTask.helper;
            this.builderFactory = collectorTask.builderFactory;
            this.concFactory = collectorTask.concFactory;
        }

        /* access modifiers changed from: protected */
        public CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER> makeChild(Spliterator<P_IN> spliterator) {
            return new CollectorTask<>(this, spliterator);
        }

        /* access modifiers changed from: protected */
        public T_NODE doLeaf() {
            return ((Node.Builder) this.helper.wrapAndCopyInto((Node.Builder) this.builderFactory.apply(this.helper.exactOutputSizeIfKnown(this.spliterator)), this.spliterator)).build();
        }

        public void onCompletion(CountedCompleter<?> countedCompleter) {
            if (!isLeaf()) {
                setLocalResult((Node) this.concFactory.apply((Node) ((CollectorTask) this.leftChild).getLocalResult(), (Node) ((CollectorTask) this.rightChild).getLocalResult()));
            }
            super.onCompletion(countedCompleter);
        }

        private static final class OfRef<P_IN, P_OUT> extends CollectorTask<P_IN, P_OUT, Node<P_OUT>, Node.Builder<P_OUT>> {
            OfRef(PipelineHelper<P_OUT> pipelineHelper, IntFunction<P_OUT[]> intFunction, Spliterator<P_IN> spliterator) {
                super(pipelineHelper, spliterator, new Nodes$CollectorTask$OfRef$$ExternalSyntheticLambda0(intFunction), new Nodes$CollectorTask$OfRef$$ExternalSyntheticLambda1());
            }
        }

        private static final class OfInt<P_IN> extends CollectorTask<P_IN, Integer, Node.OfInt, Node.Builder.OfInt> {
            OfInt(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator) {
                super(pipelineHelper, spliterator, new Nodes$CollectorTask$OfInt$$ExternalSyntheticLambda0(), new Nodes$CollectorTask$OfInt$$ExternalSyntheticLambda1());
            }
        }

        private static final class OfLong<P_IN> extends CollectorTask<P_IN, Long, Node.OfLong, Node.Builder.OfLong> {
            OfLong(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator) {
                super(pipelineHelper, spliterator, new Nodes$CollectorTask$OfLong$$ExternalSyntheticLambda0(), new Nodes$CollectorTask$OfLong$$ExternalSyntheticLambda1());
            }
        }

        private static final class OfDouble<P_IN> extends CollectorTask<P_IN, Double, Node.OfDouble, Node.Builder.OfDouble> {
            OfDouble(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator) {
                super(pipelineHelper, spliterator, new Nodes$CollectorTask$OfDouble$$ExternalSyntheticLambda0(), new Nodes$CollectorTask$OfDouble$$ExternalSyntheticLambda1());
            }
        }
    }
}
