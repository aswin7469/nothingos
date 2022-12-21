package java.util.stream;

import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.AbstractShortCircuitTask;

abstract class AbstractShortCircuitTask<P_IN, P_OUT, R, K extends AbstractShortCircuitTask<P_IN, P_OUT, R, K>> extends AbstractTask<P_IN, P_OUT, R, K> {
    protected volatile boolean canceled;
    protected final AtomicReference<R> sharedResult;

    /* access modifiers changed from: protected */
    public abstract R getEmptyResult();

    protected AbstractShortCircuitTask(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator) {
        super(pipelineHelper, spliterator);
        this.sharedResult = new AtomicReference<>(null);
    }

    protected AbstractShortCircuitTask(K k, Spliterator<P_IN> spliterator) {
        super(k, spliterator);
        this.sharedResult = k.sharedResult;
    }

    public void compute() {
        R r;
        Spliterator trySplit;
        Spliterator spliterator = this.spliterator;
        long estimateSize = spliterator.estimateSize();
        long targetSize = getTargetSize(estimateSize);
        AtomicReference<R> atomicReference = this.sharedResult;
        boolean z = false;
        while (true) {
            r = atomicReference.get();
            if (r != null) {
                break;
            } else if (r9.taskCanceled()) {
                r = r9.getEmptyResult();
                break;
            } else if (estimateSize <= targetSize || (trySplit = spliterator.trySplit()) == null) {
                r = r9.doLeaf();
            } else {
                AbstractShortCircuitTask abstractShortCircuitTask = (AbstractShortCircuitTask) r9.makeChild(trySplit);
                r9.leftChild = abstractShortCircuitTask;
                AbstractShortCircuitTask abstractShortCircuitTask2 = (AbstractShortCircuitTask) r9.makeChild(spliterator);
                r9.rightChild = abstractShortCircuitTask2;
                r9.setPendingCount(1);
                if (z) {
                    spliterator = trySplit;
                    r9 = abstractShortCircuitTask;
                    abstractShortCircuitTask = abstractShortCircuitTask2;
                } else {
                    r9 = abstractShortCircuitTask2;
                }
                z = !z;
                abstractShortCircuitTask.fork();
                estimateSize = spliterator.estimateSize();
            }
        }
        r9.setLocalResult(r);
        r9.tryComplete();
    }

    /* access modifiers changed from: protected */
    public void shortCircuit(R r) {
        if (r != null) {
            this.sharedResult.compareAndSet(null, r);
        }
    }

    /* access modifiers changed from: protected */
    public void setLocalResult(R r) {
        if (!isRoot()) {
            super.setLocalResult(r);
        } else if (r != null) {
            this.sharedResult.compareAndSet(null, r);
        }
    }

    public R getRawResult() {
        return getLocalResult();
    }

    public R getLocalResult() {
        if (!isRoot()) {
            return super.getLocalResult();
        }
        R r = this.sharedResult.get();
        return r == null ? getEmptyResult() : r;
    }

    /* access modifiers changed from: protected */
    public void cancel() {
        this.canceled = true;
    }

    /* access modifiers changed from: protected */
    public boolean taskCanceled() {
        boolean z = this.canceled;
        if (!z) {
            while (true) {
                this = (AbstractShortCircuitTask) this.getParent();
                if (z || this == null) {
                    break;
                }
                z = this.canceled;
            }
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void cancelLaterNodes() {
        AbstractTask parent = getParent();
        while (true) {
            AbstractShortCircuitTask abstractShortCircuitTask = (AbstractShortCircuitTask) parent;
            AbstractShortCircuitTask abstractShortCircuitTask2 = this;
            this = abstractShortCircuitTask;
            if (this != null) {
                if (this.leftChild == abstractShortCircuitTask2) {
                    AbstractShortCircuitTask abstractShortCircuitTask3 = (AbstractShortCircuitTask) this.rightChild;
                    if (!abstractShortCircuitTask3.canceled) {
                        abstractShortCircuitTask3.cancel();
                    }
                }
                parent = this.getParent();
            } else {
                return;
            }
        }
    }
}
