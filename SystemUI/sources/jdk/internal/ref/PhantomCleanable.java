package jdk.internal.ref;

import java.lang.ref.Cleaner;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Objects;

public abstract class PhantomCleanable<T> extends PhantomReference<T> implements Cleaner.Cleanable {
    private final PhantomCleanable<?> list;
    PhantomCleanable<?> next;
    PhantomCleanable<?> prev;

    /* access modifiers changed from: protected */
    public abstract void performCleanup();

    public PhantomCleanable(T t, Cleaner cleaner) {
        super(Objects.requireNonNull(t), CleanerImpl.getCleanerImpl(cleaner).queue);
        this.prev = this;
        this.next = this;
        this.list = CleanerImpl.getCleanerImpl(cleaner).phantomCleanableList;
        insert();
        Reference.reachabilityFence(t);
        Reference.reachabilityFence(cleaner);
    }

    PhantomCleanable() {
        super(null, (ReferenceQueue) null);
        this.prev = this;
        this.next = this;
        this.list = this;
    }

    private void insert() {
        synchronized (this.list) {
            PhantomCleanable<?> phantomCleanable = this.list;
            this.prev = phantomCleanable;
            PhantomCleanable<?> phantomCleanable2 = phantomCleanable.next;
            this.next = phantomCleanable2;
            phantomCleanable2.prev = this;
            phantomCleanable.next = this;
        }
    }

    private boolean remove() {
        synchronized (this.list) {
            PhantomCleanable<?> phantomCleanable = this.next;
            if (phantomCleanable == this) {
                return false;
            }
            phantomCleanable.prev = this.prev;
            this.prev.next = phantomCleanable;
            this.prev = this;
            this.next = this;
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isListEmpty() {
        boolean z;
        synchronized (this.list) {
            PhantomCleanable<?> phantomCleanable = this.list;
            z = phantomCleanable == phantomCleanable.next;
        }
        return z;
    }

    public final void clean() {
        if (remove()) {
            super.clear();
            performCleanup();
        }
    }

    public void clear() {
        if (remove()) {
            super.clear();
        }
    }

    public final boolean isEnqueued() {
        throw new UnsupportedOperationException("isEnqueued");
    }

    public final boolean enqueue() {
        throw new UnsupportedOperationException("enqueue");
    }
}
