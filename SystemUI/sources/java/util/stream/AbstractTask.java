package java.util.stream;

import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.AbstractTask;

abstract class AbstractTask<P_IN, P_OUT, R, K extends AbstractTask<P_IN, P_OUT, R, K>> extends CountedCompleter<R> {
    static final int LEAF_TARGET = (ForkJoinPool.getCommonPoolParallelism() << 2);
    protected final PipelineHelper<P_OUT> helper;
    protected K leftChild;
    private R localResult;
    protected K rightChild;
    protected Spliterator<P_IN> spliterator;
    protected long targetSize;

    /* access modifiers changed from: protected */
    public abstract R doLeaf();

    /* access modifiers changed from: protected */
    public abstract K makeChild(Spliterator<P_IN> spliterator2);

    protected AbstractTask(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator2) {
        super((CountedCompleter<?>) null);
        this.helper = pipelineHelper;
        this.spliterator = spliterator2;
        this.targetSize = 0;
    }

    protected AbstractTask(K k, Spliterator<P_IN> spliterator2) {
        super(k);
        this.spliterator = spliterator2;
        this.helper = k.helper;
        this.targetSize = k.targetSize;
    }

    public static long suggestTargetSize(long j) {
        long j2 = j / ((long) LEAF_TARGET);
        if (j2 > 0) {
            return j2;
        }
        return 1;
    }

    /* access modifiers changed from: protected */
    public final long getTargetSize(long j) {
        long j2 = this.targetSize;
        if (j2 != 0) {
            return j2;
        }
        long suggestTargetSize = suggestTargetSize(j);
        this.targetSize = suggestTargetSize;
        return suggestTargetSize;
    }

    public R getRawResult() {
        return this.localResult;
    }

    /* access modifiers changed from: protected */
    public void setRawResult(R r) {
        if (r != null) {
            throw new IllegalStateException();
        }
    }

    /* access modifiers changed from: protected */
    public R getLocalResult() {
        return this.localResult;
    }

    /* access modifiers changed from: protected */
    public void setLocalResult(R r) {
        this.localResult = r;
    }

    /* access modifiers changed from: protected */
    public boolean isLeaf() {
        return this.leftChild == null;
    }

    /* access modifiers changed from: protected */
    public boolean isRoot() {
        return getParent() == null;
    }

    /* access modifiers changed from: protected */
    public K getParent() {
        return (AbstractTask) getCompleter();
    }

    public void compute() {
        Spliterator<P_IN> trySplit;
        Spliterator<P_IN> spliterator2 = this.spliterator;
        long estimateSize = spliterator2.estimateSize();
        long targetSize2 = getTargetSize(estimateSize);
        boolean z = false;
        while (estimateSize > targetSize2 && (trySplit = spliterator2.trySplit()) != null) {
            K makeChild = r8.makeChild(trySplit);
            r8.leftChild = makeChild;
            K makeChild2 = r8.makeChild(spliterator2);
            r8.rightChild = makeChild2;
            r8.setPendingCount(1);
            if (z) {
                spliterator2 = trySplit;
                r8 = makeChild;
                makeChild = makeChild2;
            } else {
                r8 = makeChild2;
            }
            z = !z;
            makeChild.fork();
            estimateSize = spliterator2.estimateSize();
        }
        r8.setLocalResult(r8.doLeaf());
        r8.tryComplete();
    }

    public void onCompletion(CountedCompleter<?> countedCompleter) {
        this.spliterator = null;
        this.rightChild = null;
        this.leftChild = null;
    }

    /* access modifiers changed from: protected */
    public boolean isLeftmostNode() {
        while (this != null) {
            K parent = this.getParent();
            if (parent != null && parent.leftChild != this) {
                return false;
            }
            this = parent;
        }
        return true;
    }
}
