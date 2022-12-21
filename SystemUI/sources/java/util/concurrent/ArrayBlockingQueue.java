package java.util.concurrent;

import java.lang.ref.WeakReference;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class ArrayBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {
    private static final long serialVersionUID = -817911632652898426L;
    int count;
    final Object[] items;
    transient ArrayBlockingQueue<E>.Itrs itrs;
    final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;
    int putIndex;
    int takeIndex;

    static final int dec(int i, int i2) {
        int i3 = i - 1;
        return i3 < 0 ? i2 - 1 : i3;
    }

    static final int inc(int i, int i2) {
        int i3 = i + 1;
        if (i3 >= i2) {
            return 0;
        }
        return i3;
    }

    /* access modifiers changed from: package-private */
    public final E itemAt(int i) {
        return this.items[i];
    }

    static <E> E itemAt(Object[] objArr, int i) {
        return objArr[i];
    }

    private void enqueue(E e) {
        Object[] objArr = this.items;
        int i = this.putIndex;
        objArr[i] = e;
        int i2 = i + 1;
        this.putIndex = i2;
        if (i2 == objArr.length) {
            this.putIndex = 0;
        }
        this.count++;
        this.notEmpty.signal();
    }

    private E dequeue() {
        E[] eArr = this.items;
        int i = this.takeIndex;
        E e = eArr[i];
        eArr[i] = null;
        int i2 = i + 1;
        this.takeIndex = i2;
        if (i2 == eArr.length) {
            this.takeIndex = 0;
        }
        this.count--;
        ArrayBlockingQueue<E>.Itrs itrs2 = this.itrs;
        if (itrs2 != null) {
            itrs2.elementDequeued();
        }
        this.notFull.signal();
        return e;
    }

    /* access modifiers changed from: package-private */
    public void removeAt(int i) {
        Object[] objArr = this.items;
        int i2 = this.takeIndex;
        if (i == i2) {
            objArr[i2] = null;
            int i3 = i2 + 1;
            this.takeIndex = i3;
            if (i3 == objArr.length) {
                this.takeIndex = 0;
            }
            this.count--;
            ArrayBlockingQueue<E>.Itrs itrs2 = this.itrs;
            if (itrs2 != null) {
                itrs2.elementDequeued();
            }
        } else {
            int i4 = this.putIndex;
            int i5 = i;
            while (true) {
                int i6 = i5 + 1;
                if (i6 == objArr.length) {
                    i6 = 0;
                }
                if (i6 == i4) {
                    break;
                }
                objArr[i5] = objArr[i6];
                i5 = i6;
            }
            objArr[i5] = null;
            this.putIndex = i5;
            this.count--;
            ArrayBlockingQueue<E>.Itrs itrs3 = this.itrs;
            if (itrs3 != null) {
                itrs3.removedAt(i);
            }
        }
        this.notFull.signal();
    }

    public ArrayBlockingQueue(int i) {
        this(i, false);
    }

    public ArrayBlockingQueue(int i, boolean z) {
        if (i > 0) {
            this.items = new Object[i];
            ReentrantLock reentrantLock = new ReentrantLock(z);
            this.lock = reentrantLock;
            this.notEmpty = reentrantLock.newCondition();
            this.notFull = reentrantLock.newCondition();
            return;
        }
        throw new IllegalArgumentException();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:17|18|19) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0035, code lost:
        throw new java.lang.IllegalArgumentException();
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0030 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ArrayBlockingQueue(int r6, boolean r7, java.util.Collection<? extends E> r8) {
        /*
            r5 = this;
            r5.<init>(r6, r7)
            java.util.concurrent.locks.ReentrantLock r7 = r5.lock
            r7.lock()
            java.lang.Object[] r0 = r5.items     // Catch:{ all -> 0x0036 }
            java.util.Iterator r8 = r8.iterator()     // Catch:{ ArrayIndexOutOfBoundsException -> 0x0030 }
            r1 = 0
            r2 = r1
        L_0x0010:
            boolean r3 = r8.hasNext()     // Catch:{ ArrayIndexOutOfBoundsException -> 0x0030 }
            if (r3 == 0) goto L_0x0024
            java.lang.Object r3 = r8.next()     // Catch:{ ArrayIndexOutOfBoundsException -> 0x0030 }
            int r4 = r2 + 1
            java.lang.Object r3 = java.util.Objects.requireNonNull(r3)     // Catch:{ ArrayIndexOutOfBoundsException -> 0x0030 }
            r0[r2] = r3     // Catch:{ ArrayIndexOutOfBoundsException -> 0x0030 }
            r2 = r4
            goto L_0x0010
        L_0x0024:
            r5.count = r2     // Catch:{ all -> 0x0036 }
            if (r2 != r6) goto L_0x0029
            goto L_0x002a
        L_0x0029:
            r1 = r2
        L_0x002a:
            r5.putIndex = r1     // Catch:{ all -> 0x0036 }
            r7.unlock()
            return
        L_0x0030:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x0036 }
            r5.<init>()     // Catch:{ all -> 0x0036 }
            throw r5     // Catch:{ all -> 0x0036 }
        L_0x0036:
            r5 = move-exception
            r7.unlock()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ArrayBlockingQueue.<init>(int, boolean, java.util.Collection):void");
    }

    public boolean add(E e) {
        return super.add(e);
    }

    /* JADX INFO: finally extract failed */
    public boolean offer(E e) {
        Objects.requireNonNull(e);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            if (this.count == this.items.length) {
                reentrantLock.unlock();
                return false;
            }
            enqueue(e);
            reentrantLock.unlock();
            return true;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    public void put(E e) throws InterruptedException {
        Objects.requireNonNull(e);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (this.count == this.items.length) {
            try {
                this.notFull.await();
            } finally {
                reentrantLock.unlock();
            }
        }
        enqueue(e);
    }

    /* JADX INFO: finally extract failed */
    public boolean offer(E e, long j, TimeUnit timeUnit) throws InterruptedException {
        Objects.requireNonNull(e);
        long nanos = timeUnit.toNanos(j);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (this.count == this.items.length) {
            try {
                if (nanos <= 0) {
                    reentrantLock.unlock();
                    return false;
                }
                nanos = this.notFull.awaitNanos(nanos);
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }
        enqueue(e);
        reentrantLock.unlock();
        return true;
    }

    public E poll() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.count == 0 ? null : dequeue();
        } finally {
            reentrantLock.unlock();
        }
    }

    public E take() throws InterruptedException {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (this.count == 0) {
            try {
                this.notEmpty.await();
            } finally {
                reentrantLock.unlock();
            }
        }
        return dequeue();
    }

    /* JADX INFO: finally extract failed */
    public E poll(long j, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (this.count == 0) {
            try {
                if (nanos <= 0) {
                    reentrantLock.unlock();
                    return null;
                }
                nanos = this.notEmpty.awaitNanos(nanos);
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }
        E dequeue = dequeue();
        reentrantLock.unlock();
        return dequeue;
    }

    public E peek() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return itemAt(this.takeIndex);
        } finally {
            reentrantLock.unlock();
        }
    }

    public int size() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.count;
        } finally {
            reentrantLock.unlock();
        }
    }

    public int remainingCapacity() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.items.length - this.count;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002c, code lost:
        if (r5 != r4) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002f, code lost:
        r3 = 0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0029 A[LOOP:0: B:11:0x0017->B:17:0x0029, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x002c A[EDGE_INSN: B:26:0x002c->B:18:0x002c ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0021 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean remove(java.lang.Object r8) {
        /*
            r7 = this;
            r0 = 0
            if (r8 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.util.concurrent.locks.ReentrantLock r1 = r7.lock
            r1.lock()
            int r2 = r7.count     // Catch:{ all -> 0x0036 }
            if (r2 <= 0) goto L_0x0032
            java.lang.Object[] r2 = r7.items     // Catch:{ all -> 0x0036 }
            int r3 = r7.takeIndex     // Catch:{ all -> 0x0036 }
            int r4 = r7.putIndex     // Catch:{ all -> 0x0036 }
            if (r3 >= r4) goto L_0x0016
            goto L_0x0030
        L_0x0016:
            int r5 = r2.length     // Catch:{ all -> 0x0036 }
        L_0x0017:
            if (r3 >= r5) goto L_0x002c
            r6 = r2[r3]     // Catch:{ all -> 0x0036 }
            boolean r6 = r8.equals(r6)     // Catch:{ all -> 0x0036 }
            if (r6 == 0) goto L_0x0029
            r7.removeAt(r3)     // Catch:{ all -> 0x0036 }
            r1.unlock()
            r7 = 1
            return r7
        L_0x0029:
            int r3 = r3 + 1
            goto L_0x0017
        L_0x002c:
            if (r5 != r4) goto L_0x002f
            goto L_0x0032
        L_0x002f:
            r3 = r0
        L_0x0030:
            r5 = r4
            goto L_0x0017
        L_0x0032:
            r1.unlock()
            return r0
        L_0x0036:
            r7 = move-exception
            r1.unlock()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ArrayBlockingQueue.remove(java.lang.Object):boolean");
    }

    /* JADX INFO: finally extract failed */
    public boolean contains(Object obj) {
        int i;
        if (obj == null) {
            return false;
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            if (this.count > 0) {
                Object[] objArr = this.items;
                int i2 = this.takeIndex;
                int i3 = this.putIndex;
                if (i2 < i3) {
                    i = i3;
                } else {
                    i = objArr.length;
                }
                while (true) {
                    if (i2 < i) {
                        if (obj.equals(objArr[i2])) {
                            reentrantLock.unlock();
                            return true;
                        }
                        i2++;
                    } else if (i == i3) {
                        break;
                    } else {
                        i = i3;
                        i2 = 0;
                    }
                }
            }
            reentrantLock.unlock();
            return false;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    public Object[] toArray() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] objArr = this.items;
            int i = this.takeIndex;
            int i2 = this.count + i;
            Object[] copyOfRange = Arrays.copyOfRange((T[]) objArr, i, i2);
            int i3 = this.putIndex;
            if (i2 != i3) {
                System.arraycopy((Object) objArr, 0, (Object) copyOfRange, objArr.length - this.takeIndex, i3);
            }
            return copyOfRange;
        } finally {
            reentrantLock.unlock();
        }
    }

    public <T> T[] toArray(T[] tArr) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] objArr = this.items;
            int i = this.count;
            int min = Math.min(objArr.length - this.takeIndex, i);
            if (tArr.length < i) {
                int i2 = this.takeIndex;
                tArr = Arrays.copyOfRange(objArr, i2, i2 + i, tArr.getClass());
            } else {
                System.arraycopy((Object) objArr, this.takeIndex, (Object) tArr, 0, min);
                if (tArr.length > i) {
                    tArr[i] = null;
                }
            }
            if (min < i) {
                System.arraycopy((Object) objArr, 0, (Object) tArr, min, this.putIndex);
            }
            return tArr;
        } finally {
            reentrantLock.unlock();
        }
    }

    public String toString() {
        return Helpers.collectionToString(this);
    }

    public void clear() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int i = this.count;
            if (i > 0) {
                circularClear(this.items, this.takeIndex, this.putIndex);
                this.takeIndex = this.putIndex;
                this.count = 0;
                ArrayBlockingQueue<E>.Itrs itrs2 = this.itrs;
                if (itrs2 != null) {
                    itrs2.queueIsEmpty();
                }
                while (i > 0 && reentrantLock.hasWaiters(this.notFull)) {
                    this.notFull.signal();
                    i--;
                }
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x0006 A[LOOP:0: B:2:0x0004->B:3:0x0006, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x000f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void circularClear(java.lang.Object[] r2, int r3, int r4) {
        /*
            if (r3 >= r4) goto L_0x0003
            goto L_0x0010
        L_0x0003:
            int r0 = r2.length
        L_0x0004:
            if (r3 >= r0) goto L_0x000c
            r1 = 0
            r2[r3] = r1
            int r3 = r3 + 1
            goto L_0x0004
        L_0x000c:
            if (r0 != r4) goto L_0x000f
            return
        L_0x000f:
            r3 = 0
        L_0x0010:
            r0 = r4
            goto L_0x0004
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ArrayBlockingQueue.circularClear(java.lang.Object[], int, int):void");
    }

    public int drainTo(Collection<? super E> collection) {
        return drainTo(collection, Integer.MAX_VALUE);
    }

    public int drainTo(Collection<? super E> collection, int i) {
        int i2;
        int i3;
        Objects.requireNonNull(collection);
        if (collection == this) {
            throw new IllegalArgumentException();
        } else if (i <= 0) {
            return 0;
        } else {
            Object[] objArr = this.items;
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                int min = Math.min(i, this.count);
                i2 = this.takeIndex;
                i3 = 0;
                while (i3 < min) {
                    collection.add(objArr[i2]);
                    objArr[i2] = null;
                    i2++;
                    if (i2 == objArr.length) {
                        i2 = 0;
                    }
                    i3++;
                }
                if (i3 > 0) {
                    int i4 = this.count - i3;
                    this.count = i4;
                    this.takeIndex = i2;
                    ArrayBlockingQueue<E>.Itrs itrs2 = this.itrs;
                    if (itrs2 != null) {
                        if (i4 == 0) {
                            itrs2.queueIsEmpty();
                        } else if (i3 > i2) {
                            itrs2.takeIndexWrapped();
                        }
                    }
                    while (i3 > 0 && reentrantLock.hasWaiters(this.notFull)) {
                        this.notFull.signal();
                        i3--;
                    }
                }
                reentrantLock.unlock();
                return min;
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    class Itrs {
        private static final int LONG_SWEEP_PROBES = 16;
        private static final int SHORT_SWEEP_PROBES = 4;
        int cycles;
        private ArrayBlockingQueue<E>.Itrs.Node head;
        private ArrayBlockingQueue<E>.Itrs.Node sweeper;

        private class Node extends WeakReference<ArrayBlockingQueue<E>.Itr> {
            ArrayBlockingQueue<E>.Itrs.Node next;

            Node(ArrayBlockingQueue<E>.Itr itr, ArrayBlockingQueue<E>.Itrs.Node node) {
                super(itr);
                this.next = node;
            }
        }

        Itrs(ArrayBlockingQueue<E>.Itr itr) {
            register(itr);
        }

        /* access modifiers changed from: package-private */
        public void doSomeSweeping(boolean z) {
            boolean z2;
            ArrayBlockingQueue<E>.Itrs.Node node;
            ArrayBlockingQueue<E>.Itrs.Node node2;
            int i = z ? 16 : 4;
            ArrayBlockingQueue<E>.Itrs.Node node3 = this.sweeper;
            ArrayBlockingQueue<E>.Itrs.Node node4 = null;
            if (node3 == null) {
                node2 = this.head;
                z2 = true;
                node = null;
            } else {
                z2 = false;
                node = node3;
                node2 = node3.next;
            }
            while (i > 0) {
                if (node2 == null) {
                    if (z2) {
                        break;
                    }
                    node2 = this.head;
                    z2 = true;
                    node = null;
                }
                Itr itr = (Itr) node2.get();
                ArrayBlockingQueue<E>.Itrs.Node node5 = node2.next;
                if (itr == null || itr.isDetached()) {
                    node2.clear();
                    node2.next = null;
                    if (node == null) {
                        this.head = node5;
                        if (node5 == null) {
                            ArrayBlockingQueue.this.itrs = null;
                            return;
                        }
                    } else {
                        node.next = node5;
                    }
                    i = 16;
                } else {
                    node = node2;
                }
                i--;
                node2 = node5;
            }
            if (node2 != null) {
                node4 = node;
            }
            this.sweeper = node4;
        }

        /* access modifiers changed from: package-private */
        public void register(ArrayBlockingQueue<E>.Itr itr) {
            this.head = new Node(itr, this.head);
        }

        /* access modifiers changed from: package-private */
        public void takeIndexWrapped() {
            this.cycles++;
            Node node = this.head;
            Node node2 = null;
            while (node != null) {
                Itr itr = (Itr) node.get();
                Node node3 = node.next;
                if (itr == null || itr.takeIndexWrapped()) {
                    node.clear();
                    node.next = null;
                    if (node2 == null) {
                        this.head = node3;
                    } else {
                        node2.next = node3;
                    }
                } else {
                    node2 = node;
                }
                node = node3;
            }
            if (this.head == null) {
                ArrayBlockingQueue.this.itrs = null;
            }
        }

        /* access modifiers changed from: package-private */
        public void removedAt(int i) {
            Node node = this.head;
            Node node2 = null;
            while (node != null) {
                Itr itr = (Itr) node.get();
                Node node3 = node.next;
                if (itr == null || itr.removedAt(i)) {
                    node.clear();
                    node.next = null;
                    if (node2 == null) {
                        this.head = node3;
                    } else {
                        node2.next = node3;
                    }
                } else {
                    node2 = node;
                }
                node = node3;
            }
            if (this.head == null) {
                ArrayBlockingQueue.this.itrs = null;
            }
        }

        /* access modifiers changed from: package-private */
        public void queueIsEmpty() {
            for (ArrayBlockingQueue<E>.Itrs.Node node = this.head; node != null; node = node.next) {
                Itr itr = (Itr) node.get();
                if (itr != null) {
                    node.clear();
                    itr.shutdown();
                }
            }
            this.head = null;
            ArrayBlockingQueue.this.itrs = null;
        }

        /* access modifiers changed from: package-private */
        public void elementDequeued() {
            if (ArrayBlockingQueue.this.count == 0) {
                queueIsEmpty();
            } else if (ArrayBlockingQueue.this.takeIndex == 0) {
                takeIndexWrapped();
            }
        }
    }

    private class Itr implements Iterator<E> {
        private static final int DETACHED = -3;
        private static final int NONE = -1;
        private static final int REMOVED = -2;
        private int cursor;
        private E lastItem;
        private int lastRet = -1;
        private int nextIndex;
        private E nextItem;
        private int prevCycles;
        private int prevTakeIndex;

        private int distance(int i, int i2, int i3) {
            int i4 = i - i2;
            return i4 < 0 ? i4 + i3 : i4;
        }

        private boolean invalidated(int i, int i2, long j, int i3) {
            if (i < 0) {
                return false;
            }
            int i4 = i - i2;
            if (i4 < 0) {
                i4 += i3;
            }
            return j > ((long) i4);
        }

        Itr() {
            ReentrantLock reentrantLock = ArrayBlockingQueue.this.lock;
            reentrantLock.lock();
            try {
                if (ArrayBlockingQueue.this.count == 0) {
                    this.cursor = -1;
                    this.nextIndex = -1;
                    this.prevTakeIndex = -3;
                } else {
                    int i = ArrayBlockingQueue.this.takeIndex;
                    this.prevTakeIndex = i;
                    this.nextIndex = i;
                    this.nextItem = ArrayBlockingQueue.this.itemAt(i);
                    this.cursor = incCursor(i);
                    if (ArrayBlockingQueue.this.itrs == null) {
                        ArrayBlockingQueue.this.itrs = new Itrs(this);
                    } else {
                        ArrayBlockingQueue.this.itrs.register(this);
                        ArrayBlockingQueue.this.itrs.doSomeSweeping(false);
                    }
                    this.prevCycles = ArrayBlockingQueue.this.itrs.cycles;
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isDetached() {
            return this.prevTakeIndex < 0;
        }

        private int incCursor(int i) {
            int i2 = i + 1;
            if (i2 == ArrayBlockingQueue.this.items.length) {
                i2 = 0;
            }
            if (i2 == ArrayBlockingQueue.this.putIndex) {
                return -1;
            }
            return i2;
        }

        private void incorporateDequeues() {
            int i = ArrayBlockingQueue.this.itrs.cycles;
            int i2 = ArrayBlockingQueue.this.takeIndex;
            int i3 = this.prevCycles;
            int i4 = this.prevTakeIndex;
            if (i != i3 || i2 != i4) {
                int length = ArrayBlockingQueue.this.items.length;
                long j = (((long) (i - i3)) * ((long) length)) + ((long) (i2 - i4));
                if (invalidated(this.lastRet, i4, j, length)) {
                    this.lastRet = -2;
                }
                if (invalidated(this.nextIndex, i4, j, length)) {
                    this.nextIndex = -2;
                }
                if (invalidated(this.cursor, i4, j, length)) {
                    this.cursor = i2;
                }
                if (this.cursor >= 0 || this.nextIndex >= 0 || this.lastRet >= 0) {
                    this.prevCycles = i;
                    this.prevTakeIndex = i2;
                    return;
                }
                detach();
            }
        }

        private void detach() {
            if (this.prevTakeIndex >= 0) {
                this.prevTakeIndex = -3;
                ArrayBlockingQueue.this.itrs.doSomeSweeping(true);
            }
        }

        public boolean hasNext() {
            if (this.nextItem != null) {
                return true;
            }
            noNext();
            return false;
        }

        private void noNext() {
            ReentrantLock reentrantLock = ArrayBlockingQueue.this.lock;
            reentrantLock.lock();
            try {
                if (!isDetached()) {
                    incorporateDequeues();
                    int i = this.lastRet;
                    if (i >= 0) {
                        this.lastItem = ArrayBlockingQueue.this.itemAt(i);
                        detach();
                    }
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        public E next() {
            E e = this.nextItem;
            if (e != null) {
                ReentrantLock reentrantLock = ArrayBlockingQueue.this.lock;
                reentrantLock.lock();
                try {
                    if (!isDetached()) {
                        incorporateDequeues();
                    }
                    int i = this.nextIndex;
                    this.lastRet = i;
                    int i2 = this.cursor;
                    if (i2 >= 0) {
                        ArrayBlockingQueue arrayBlockingQueue = ArrayBlockingQueue.this;
                        this.nextIndex = i2;
                        this.nextItem = arrayBlockingQueue.itemAt(i2);
                        this.cursor = incCursor(i2);
                    } else {
                        this.nextIndex = -1;
                        this.nextItem = null;
                        if (i == -2) {
                            detach();
                        }
                    }
                    return e;
                } finally {
                    reentrantLock.unlock();
                }
            } else {
                throw new NoSuchElementException();
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:20:0x0048 A[Catch:{ all -> 0x0068 }] */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x0065  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void forEachRemaining(java.util.function.Consumer<? super E> r9) {
            /*
                r8 = this;
                java.util.Objects.requireNonNull(r9)
                java.util.concurrent.ArrayBlockingQueue r0 = java.util.concurrent.ArrayBlockingQueue.this
                java.util.concurrent.locks.ReentrantLock r0 = r0.lock
                r0.lock()
                r1 = 0
                r2 = -1
                E r3 = r8.nextItem     // Catch:{ all -> 0x0068 }
                if (r3 != 0) goto L_0x0021
            L_0x0010:
                r8.lastRet = r2
                r8.nextIndex = r2
                r8.cursor = r2
                r8.lastItem = r1
                r8.nextItem = r1
                r8.detach()
                r0.unlock()
                return
            L_0x0021:
                boolean r4 = r8.isDetached()     // Catch:{ all -> 0x0068 }
                if (r4 != 0) goto L_0x002a
                r8.incorporateDequeues()     // Catch:{ all -> 0x0068 }
            L_0x002a:
                r9.accept(r3)     // Catch:{ all -> 0x0068 }
                boolean r3 = r8.isDetached()     // Catch:{ all -> 0x0068 }
                if (r3 != 0) goto L_0x0010
                int r3 = r8.cursor     // Catch:{ all -> 0x0068 }
                if (r3 >= 0) goto L_0x0038
                goto L_0x0010
            L_0x0038:
                java.util.concurrent.ArrayBlockingQueue r3 = java.util.concurrent.ArrayBlockingQueue.this     // Catch:{ all -> 0x0068 }
                java.lang.Object[] r3 = r3.items     // Catch:{ all -> 0x0068 }
                int r4 = r8.cursor     // Catch:{ all -> 0x0068 }
                java.util.concurrent.ArrayBlockingQueue r5 = java.util.concurrent.ArrayBlockingQueue.this     // Catch:{ all -> 0x0068 }
                int r5 = r5.putIndex     // Catch:{ all -> 0x0068 }
                if (r4 >= r5) goto L_0x0045
                goto L_0x0066
            L_0x0045:
                int r6 = r3.length     // Catch:{ all -> 0x0068 }
            L_0x0046:
                if (r4 >= r6) goto L_0x0052
                java.lang.Object r7 = java.util.concurrent.ArrayBlockingQueue.itemAt(r3, r4)     // Catch:{ all -> 0x0068 }
                r9.accept(r7)     // Catch:{ all -> 0x0068 }
                int r4 = r4 + 1
                goto L_0x0046
            L_0x0052:
                if (r6 != r5) goto L_0x0065
                r8.lastRet = r2
                r8.nextIndex = r2
                r8.cursor = r2
                r8.lastItem = r1
                r8.nextItem = r1
                r8.detach()
                r0.unlock()
                return
            L_0x0065:
                r4 = 0
            L_0x0066:
                r6 = r5
                goto L_0x0046
            L_0x0068:
                r9 = move-exception
                r8.lastRet = r2
                r8.nextIndex = r2
                r8.cursor = r2
                r8.lastItem = r1
                r8.nextItem = r1
                r8.detach()
                r0.unlock()
                throw r9
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ArrayBlockingQueue.Itr.forEachRemaining(java.util.function.Consumer):void");
        }

        public void remove() {
            ReentrantLock reentrantLock = ArrayBlockingQueue.this.lock;
            reentrantLock.lock();
            try {
                if (!isDetached()) {
                    incorporateDequeues();
                }
                int i = this.lastRet;
                this.lastRet = -1;
                if (i >= 0) {
                    if (!isDetached()) {
                        ArrayBlockingQueue.this.removeAt(i);
                    } else {
                        E e = this.lastItem;
                        this.lastItem = null;
                        if (ArrayBlockingQueue.this.itemAt(i) == e) {
                            ArrayBlockingQueue.this.removeAt(i);
                        }
                    }
                } else if (i == -1) {
                    throw new IllegalStateException();
                }
                if (this.cursor < 0 && this.nextIndex < 0) {
                    detach();
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        /* access modifiers changed from: package-private */
        public void shutdown() {
            this.cursor = -1;
            if (this.nextIndex >= 0) {
                this.nextIndex = -2;
            }
            if (this.lastRet >= 0) {
                this.lastRet = -2;
                this.lastItem = null;
            }
            this.prevTakeIndex = -3;
        }

        /* access modifiers changed from: package-private */
        public boolean removedAt(int i) {
            if (isDetached()) {
                return true;
            }
            int i2 = ArrayBlockingQueue.this.takeIndex;
            int i3 = this.prevTakeIndex;
            int length = ArrayBlockingQueue.this.items.length;
            int i4 = (((ArrayBlockingQueue.this.itrs.cycles - this.prevCycles) + (i < i2 ? 1 : 0)) * length) + (i - i3);
            int i5 = this.cursor;
            if (i5 >= 0) {
                int distance = distance(i5, i3, length);
                if (distance == i4) {
                    if (i5 == ArrayBlockingQueue.this.putIndex) {
                        i5 = -1;
                        this.cursor = -1;
                    }
                } else if (distance > i4) {
                    i5 = ArrayBlockingQueue.dec(i5, length);
                    this.cursor = i5;
                }
            }
            int i6 = this.lastRet;
            int i7 = -2;
            if (i6 >= 0) {
                int distance2 = distance(i6, i3, length);
                if (distance2 == i4) {
                    this.lastRet = -2;
                    i6 = -2;
                } else if (distance2 > i4) {
                    i6 = ArrayBlockingQueue.dec(i6, length);
                    this.lastRet = i6;
                }
            }
            int i8 = this.nextIndex;
            if (i8 >= 0) {
                int distance3 = distance(i8, i3, length);
                if (distance3 == i4) {
                    this.nextIndex = -2;
                } else if (distance3 > i4) {
                    i7 = ArrayBlockingQueue.dec(i8, length);
                    this.nextIndex = i7;
                }
                if (i5 < 0 || i7 >= 0 || i6 >= 0) {
                    return false;
                }
                this.prevTakeIndex = -3;
                return true;
            }
            i7 = i8;
            if (i5 < 0) {
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean takeIndexWrapped() {
            if (isDetached()) {
                return true;
            }
            if (ArrayBlockingQueue.this.itrs.cycles - this.prevCycles <= 1) {
                return false;
            }
            shutdown();
            return true;
        }
    }

    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 4368);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0018 A[Catch:{ all -> 0x002c }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void forEach(java.util.function.Consumer<? super E> r6) {
        /*
            r5 = this;
            java.util.Objects.requireNonNull(r6)
            java.util.concurrent.locks.ReentrantLock r0 = r5.lock
            r0.lock()
            int r1 = r5.count     // Catch:{ all -> 0x002c }
            if (r1 <= 0) goto L_0x0028
            java.lang.Object[] r1 = r5.items     // Catch:{ all -> 0x002c }
            int r2 = r5.takeIndex     // Catch:{ all -> 0x002c }
            int r5 = r5.putIndex     // Catch:{ all -> 0x002c }
            if (r2 >= r5) goto L_0x0015
            goto L_0x0026
        L_0x0015:
            int r3 = r1.length     // Catch:{ all -> 0x002c }
        L_0x0016:
            if (r2 >= r3) goto L_0x0022
            java.lang.Object r4 = itemAt(r1, r2)     // Catch:{ all -> 0x002c }
            r6.accept(r4)     // Catch:{ all -> 0x002c }
            int r2 = r2 + 1
            goto L_0x0016
        L_0x0022:
            if (r3 != r5) goto L_0x0025
            goto L_0x0028
        L_0x0025:
            r2 = 0
        L_0x0026:
            r3 = r5
            goto L_0x0016
        L_0x0028:
            r0.unlock()
            return
        L_0x002c:
            r5 = move-exception
            r0.unlock()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ArrayBlockingQueue.forEach(java.util.function.Consumer):void");
    }

    public boolean removeIf(Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return bulkRemove(predicate);
    }

    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new ArrayBlockingQueue$$ExternalSyntheticLambda0(collection));
    }

    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new ArrayBlockingQueue$$ExternalSyntheticLambda1(collection));
    }

    static /* synthetic */ boolean lambda$retainAll$1(Collection collection, Object obj) {
        return !collection.contains(obj);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002f, code lost:
        if (r5 != r4) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0032, code lost:
        r3 = 0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x002c A[LOOP:0: B:10:0x0018->B:16:0x002c, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0024 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x002f A[EDGE_INSN: B:28:0x002f->B:17:0x002f ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean bulkRemove(java.util.function.Predicate<? super E> r8) {
        /*
            r7 = this;
            java.util.concurrent.locks.ReentrantLock r0 = r7.lock
            r0.lock()
            java.util.concurrent.ArrayBlockingQueue<E>$Itrs r1 = r7.itrs     // Catch:{ all -> 0x0041 }
            if (r1 != 0) goto L_0x0039
            int r1 = r7.count     // Catch:{ all -> 0x0041 }
            r2 = 0
            if (r1 <= 0) goto L_0x0035
            java.lang.Object[] r1 = r7.items     // Catch:{ all -> 0x0041 }
            int r3 = r7.takeIndex     // Catch:{ all -> 0x0041 }
            int r4 = r7.putIndex     // Catch:{ all -> 0x0041 }
            if (r3 >= r4) goto L_0x0017
            goto L_0x0033
        L_0x0017:
            int r5 = r1.length     // Catch:{ all -> 0x0041 }
        L_0x0018:
            if (r3 >= r5) goto L_0x002f
            java.lang.Object r6 = itemAt(r1, r3)     // Catch:{ all -> 0x0041 }
            boolean r6 = r8.test(r6)     // Catch:{ all -> 0x0041 }
            if (r6 == 0) goto L_0x002c
            boolean r7 = r7.bulkRemoveModified(r8, r3)     // Catch:{ all -> 0x0041 }
            r0.unlock()
            return r7
        L_0x002c:
            int r3 = r3 + 1
            goto L_0x0018
        L_0x002f:
            if (r5 != r4) goto L_0x0032
            goto L_0x0035
        L_0x0032:
            r3 = r2
        L_0x0033:
            r5 = r4
            goto L_0x0018
        L_0x0035:
            r0.unlock()
            return r2
        L_0x0039:
            r0.unlock()
            boolean r7 = super.removeIf(r8)
            return r7
        L_0x0041:
            r7 = move-exception
            r0.unlock()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ArrayBlockingQueue.bulkRemove(java.util.function.Predicate):boolean");
    }

    private static long[] nBits(int i) {
        return new long[(((i - 1) >> 6) + 1)];
    }

    private static void setBit(long[] jArr, int i) {
        int i2 = i >> 6;
        jArr[i2] = jArr[i2] | (1 << i);
    }

    private static boolean isClear(long[] jArr, int i) {
        return ((1 << i) & jArr[i >> 6]) == 0;
    }

    private int distanceNonEmpty(int i, int i2) {
        int i3 = i2 - i;
        return i3 <= 0 ? i3 + this.items.length : i3;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x006a, code lost:
        if (r4 == r1) goto L_0x006c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean bulkRemoveModified(java.util.function.Predicate<? super E> r11, int r12) {
        /*
            r10 = this;
            java.lang.Object[] r0 = r10.items
            int r1 = r0.length
            int r2 = r10.putIndex
            int r3 = r10.distanceNonEmpty(r12, r2)
            long[] r3 = nBits(r3)
            r4 = 1
            r6 = 0
            r3[r6] = r4
            int r4 = r12 + 1
            if (r4 > r2) goto L_0x0018
            r5 = r2
            goto L_0x0019
        L_0x0018:
            int r5 = r0.length
        L_0x0019:
            r8 = r12
            r7 = r4
        L_0x001b:
            if (r7 >= r5) goto L_0x002f
            java.lang.Object r9 = itemAt(r0, r7)
            boolean r9 = r11.test(r9)
            if (r9 == 0) goto L_0x002c
            int r9 = r7 - r8
            setBit(r3, r9)
        L_0x002c:
            int r7 = r7 + 1
            goto L_0x001b
        L_0x002f:
            if (r5 != r2) goto L_0x007f
            if (r4 > r2) goto L_0x0035
            r11 = r2
            goto L_0x0036
        L_0x0035:
            int r11 = r0.length
        L_0x0036:
            r5 = r4
            r4 = r12
        L_0x0038:
            if (r5 >= r11) goto L_0x004c
            int r7 = r5 - r12
            boolean r7 = isClear(r3, r7)
            if (r7 == 0) goto L_0x0049
            int r7 = r4 + 1
            r8 = r0[r5]
            r0[r4] = r8
            r4 = r7
        L_0x0049:
            int r5 = r5 + 1
            goto L_0x0038
        L_0x004c:
            if (r11 != r2) goto L_0x0050
        L_0x004e:
            r6 = r4
            goto L_0x006c
        L_0x0050:
            int r12 = r12 - r1
            r5 = r6
        L_0x0052:
            if (r5 >= r2) goto L_0x0068
            if (r4 >= r1) goto L_0x0068
            int r11 = r5 - r12
            boolean r11 = isClear(r3, r11)
            if (r11 == 0) goto L_0x0065
            int r11 = r4 + 1
            r7 = r0[r5]
            r0[r4] = r7
            r4 = r11
        L_0x0065:
            int r5 = r5 + 1
            goto L_0x0052
        L_0x0068:
            if (r5 < r2) goto L_0x007c
            if (r4 != r1) goto L_0x004e
        L_0x006c:
            int r11 = r10.count
            int r12 = r10.distanceNonEmpty(r6, r2)
            int r11 = r11 - r12
            r10.count = r11
            r10.putIndex = r6
            circularClear(r0, r6, r2)
            r10 = 1
            return r10
        L_0x007c:
            r11 = r2
            r4 = r6
            goto L_0x0038
        L_0x007f:
            int r8 = r8 - r1
            r5 = r2
            r7 = r6
            goto L_0x001b
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ArrayBlockingQueue.bulkRemoveModified(java.util.function.Predicate, int):boolean");
    }

    /* access modifiers changed from: package-private */
    public void checkInvariants() {
        if (!invariantsSatisfied()) {
            String format = String.format("takeIndex=%d putIndex=%d count=%d capacity=%d items=%s", Integer.valueOf(this.takeIndex), Integer.valueOf(this.putIndex), Integer.valueOf(this.count), Integer.valueOf(this.items.length), Arrays.toString(this.items));
            System.err.println(format);
            throw new AssertionError((Object) format);
        }
    }

    private boolean invariantsSatisfied() {
        Object[] objArr = this.items;
        int length = objArr.length;
        if (length > 0 && objArr.getClass() == Object[].class) {
            int i = this.takeIndex;
            int i2 = this.putIndex;
            int i3 = this.count;
            return ((i | i2) | i3) >= 0 && i < length && i2 < length && i3 <= length && ((i2 - i) - i3) % length == 0 && (i3 == 0 || this.items[i] != null) && ((i3 == length || this.items[i2] == null) && (i3 == 0 || this.items[dec(i2, length)] != null));
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (!invariantsSatisfied()) {
            throw new InvalidObjectException("invariants violated");
        }
    }
}
