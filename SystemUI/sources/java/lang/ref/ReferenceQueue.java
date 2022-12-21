package java.lang.ref;

import java.util.concurrent.atomic.AtomicInteger;
import sun.misc.Cleaner;

public class ReferenceQueue<T> {
    private static ReferenceQueue currentQueue = null;
    private static final Reference sQueueNextUnenqueued = new PhantomReference(null, (ReferenceQueue) null);
    public static Reference<?> unenqueued = null;
    private Reference<? extends T> head = null;
    private final Object lock = new Object();
    private Reference<? extends T> tail = null;

    private boolean enqueueLocked(Reference<? extends T> reference) {
        if (reference.queueNext != null) {
            return false;
        }
        if (reference instanceof Cleaner) {
            ((Cleaner) reference).clean();
            reference.queueNext = sQueueNextUnenqueued;
            return true;
        }
        Reference<? extends T> reference2 = this.tail;
        if (reference2 == null) {
            this.head = reference;
        } else {
            reference2.queueNext = reference;
        }
        this.tail = reference;
        reference.queueNext = reference;
        return true;
    }

    public static ReferenceQueue getCurrentQueue() {
        return currentQueue;
    }

    /* access modifiers changed from: package-private */
    public boolean isEnqueued(Reference<? extends T> reference) {
        boolean z;
        synchronized (this.lock) {
            z = (reference.queueNext == null || reference.queueNext == sQueueNextUnenqueued) ? false : true;
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public boolean enqueue(Reference<? extends T> reference) {
        synchronized (this.lock) {
            if (!enqueueLocked(reference)) {
                return false;
            }
            this.lock.notifyAll();
            return true;
        }
    }

    private Reference<? extends T> reallyPollLocked() {
        Reference<? extends T> reference = this.head;
        if (reference == null) {
            return null;
        }
        if (reference == this.tail) {
            this.tail = null;
            this.head = null;
        } else {
            this.head = reference.queueNext;
        }
        reference.queueNext = sQueueNextUnenqueued;
        return reference;
    }

    public Reference<? extends T> poll() {
        synchronized (this.lock) {
            if (this.head == null) {
                return null;
            }
            Reference<? extends T> reallyPollLocked = reallyPollLocked();
            return reallyPollLocked;
        }
    }

    public Reference<? extends T> remove(long j) throws IllegalArgumentException, InterruptedException {
        long j2;
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i >= 0) {
            synchronized (this.lock) {
                Reference<? extends T> reallyPollLocked = reallyPollLocked();
                if (reallyPollLocked != null) {
                    return reallyPollLocked;
                }
                if (i == 0) {
                    j2 = 0;
                } else {
                    j2 = System.nanoTime();
                }
                while (true) {
                    this.lock.wait(j);
                    Reference<? extends T> reallyPollLocked2 = reallyPollLocked();
                    if (reallyPollLocked2 != null) {
                        return reallyPollLocked2;
                    }
                    if (j != 0) {
                        long nanoTime = System.nanoTime();
                        j -= (nanoTime - j2) / 1000000;
                        if (j <= 0) {
                            return null;
                        }
                        j2 = nanoTime;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Negative timeout value");
        }
    }

    public Reference<? extends T> remove() throws InterruptedException {
        return remove(0);
    }

    public static void enqueuePending(Reference<?> reference, AtomicInteger atomicInteger) {
        Reference<?> reference2;
        Reference<?> reference3 = reference;
        do {
            ReferenceQueue<? super T> referenceQueue = reference3.queue;
            currentQueue = referenceQueue;
            if (referenceQueue == null) {
                Reference<?> reference4 = reference3.pendingNext;
                reference3.pendingNext = reference3;
                reference3 = reference4;
            } else {
                synchronized (referenceQueue.lock) {
                    int i = 0;
                    while (true) {
                        reference2 = reference3.pendingNext;
                        reference3.pendingNext = reference3;
                        referenceQueue.enqueueLocked(reference3);
                        if (reference2 == reference || reference2.queue != referenceQueue) {
                            break;
                        }
                        i++;
                        if (i > 100) {
                            break;
                        }
                        reference3 = reference2;
                    }
                    referenceQueue.lock.notifyAll();
                }
                reference3 = reference2;
            }
            atomicInteger.incrementAndGet();
        } while (reference3 != reference);
    }

    static void add(Reference<?> reference) {
        synchronized (ReferenceQueue.class) {
            Reference<?> reference2 = unenqueued;
            if (reference2 == null) {
                unenqueued = reference;
            } else {
                while (reference2.pendingNext != unenqueued) {
                    reference2 = reference2.pendingNext;
                }
                reference2.pendingNext = reference;
                Reference<?> reference3 = reference;
                while (reference3.pendingNext != reference) {
                    reference3 = reference3.pendingNext;
                }
                reference3.pendingNext = unenqueued;
            }
            ReferenceQueue.class.notifyAll();
        }
    }
}
