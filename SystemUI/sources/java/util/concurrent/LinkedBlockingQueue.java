package java.util.concurrent;

import java.lang.reflect.Array;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LinkedBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {
    private static final long serialVersionUID = -6903933977591709194L;
    private final int capacity;
    private final AtomicInteger count;
    transient Node<E> head;
    private transient Node<E> last;
    private final Condition notEmpty;
    private final Condition notFull;
    private final ReentrantLock putLock;
    private final ReentrantLock takeLock;

    static class Node<E> {
        E item;
        Node<E> next;

        Node(E e) {
            this.item = e;
        }
    }

    private void signalNotEmpty() {
        ReentrantLock reentrantLock = this.takeLock;
        reentrantLock.lock();
        try {
            this.notEmpty.signal();
        } finally {
            reentrantLock.unlock();
        }
    }

    private void signalNotFull() {
        ReentrantLock reentrantLock = this.putLock;
        reentrantLock.lock();
        try {
            this.notFull.signal();
        } finally {
            reentrantLock.unlock();
        }
    }

    private void enqueue(Node<E> node) {
        this.last.next = node;
        this.last = node;
    }

    private E dequeue() {
        Node<E> node = this.head;
        Node<E> node2 = node.next;
        node.next = node;
        this.head = node2;
        E e = node2.item;
        node2.item = null;
        return e;
    }

    /* access modifiers changed from: package-private */
    public void fullyLock() {
        this.putLock.lock();
        this.takeLock.lock();
    }

    /* access modifiers changed from: package-private */
    public void fullyUnlock() {
        this.takeLock.unlock();
        this.putLock.unlock();
    }

    public LinkedBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    public LinkedBlockingQueue(int i) {
        this.count = new AtomicInteger();
        ReentrantLock reentrantLock = new ReentrantLock();
        this.takeLock = reentrantLock;
        this.notEmpty = reentrantLock.newCondition();
        ReentrantLock reentrantLock2 = new ReentrantLock();
        this.putLock = reentrantLock2;
        this.notFull = reentrantLock2.newCondition();
        if (i > 0) {
            this.capacity = i;
            Node<E> node = new Node<>(null);
            this.head = node;
            this.last = node;
            return;
        }
        throw new IllegalArgumentException();
    }

    public LinkedBlockingQueue(Collection<? extends E> collection) {
        this(Integer.MAX_VALUE);
        ReentrantLock reentrantLock = this.putLock;
        reentrantLock.lock();
        try {
            int i = 0;
            for (Object next : collection) {
                if (next == null) {
                    throw new NullPointerException();
                } else if (i != this.capacity) {
                    enqueue(new Node(next));
                    i++;
                } else {
                    throw new IllegalStateException("Queue full");
                }
            }
            this.count.set(i);
        } finally {
            reentrantLock.unlock();
        }
    }

    public int size() {
        return this.count.get();
    }

    public int remainingCapacity() {
        return this.capacity - this.count.get();
    }

    public void put(E e) throws InterruptedException {
        e.getClass();
        Node node = new Node(e);
        ReentrantLock reentrantLock = this.putLock;
        AtomicInteger atomicInteger = this.count;
        reentrantLock.lockInterruptibly();
        while (atomicInteger.get() == this.capacity) {
            try {
                this.notFull.await();
            } finally {
                reentrantLock.unlock();
            }
        }
        enqueue(node);
        int andIncrement = atomicInteger.getAndIncrement();
        if (andIncrement + 1 < this.capacity) {
            this.notFull.signal();
        }
        if (andIncrement == 0) {
            signalNotEmpty();
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean offer(E e, long j, TimeUnit timeUnit) throws InterruptedException {
        e.getClass();
        long nanos = timeUnit.toNanos(j);
        ReentrantLock reentrantLock = this.putLock;
        AtomicInteger atomicInteger = this.count;
        reentrantLock.lockInterruptibly();
        while (atomicInteger.get() == this.capacity) {
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
        enqueue(new Node(e));
        int andIncrement = atomicInteger.getAndIncrement();
        if (andIncrement + 1 < this.capacity) {
            this.notFull.signal();
        }
        reentrantLock.unlock();
        if (andIncrement != 0) {
            return true;
        }
        signalNotEmpty();
        return true;
    }

    public boolean offer(E e) {
        e.getClass();
        AtomicInteger atomicInteger = this.count;
        if (atomicInteger.get() == this.capacity) {
            return false;
        }
        Node node = new Node(e);
        ReentrantLock reentrantLock = this.putLock;
        reentrantLock.lock();
        try {
            if (atomicInteger.get() == this.capacity) {
                return false;
            }
            enqueue(node);
            int andIncrement = atomicInteger.getAndIncrement();
            if (andIncrement + 1 < this.capacity) {
                this.notFull.signal();
            }
            reentrantLock.unlock();
            if (andIncrement != 0) {
                return true;
            }
            signalNotEmpty();
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public E take() throws InterruptedException {
        AtomicInteger atomicInteger = this.count;
        ReentrantLock reentrantLock = this.takeLock;
        reentrantLock.lockInterruptibly();
        while (atomicInteger.get() == 0) {
            try {
                this.notEmpty.await();
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }
        E dequeue = dequeue();
        int andDecrement = atomicInteger.getAndDecrement();
        if (andDecrement > 1) {
            this.notEmpty.signal();
        }
        reentrantLock.unlock();
        if (andDecrement == this.capacity) {
            signalNotFull();
        }
        return dequeue;
    }

    /* JADX INFO: finally extract failed */
    public E poll(long j, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j);
        AtomicInteger atomicInteger = this.count;
        ReentrantLock reentrantLock = this.takeLock;
        reentrantLock.lockInterruptibly();
        while (atomicInteger.get() == 0) {
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
        int andDecrement = atomicInteger.getAndDecrement();
        if (andDecrement > 1) {
            this.notEmpty.signal();
        }
        reentrantLock.unlock();
        if (andDecrement == this.capacity) {
            signalNotFull();
        }
        return dequeue;
    }

    public E poll() {
        AtomicInteger atomicInteger = this.count;
        if (atomicInteger.get() == 0) {
            return null;
        }
        ReentrantLock reentrantLock = this.takeLock;
        reentrantLock.lock();
        try {
            if (atomicInteger.get() == 0) {
                return null;
            }
            E dequeue = dequeue();
            int andDecrement = atomicInteger.getAndDecrement();
            if (andDecrement > 1) {
                this.notEmpty.signal();
            }
            reentrantLock.unlock();
            if (andDecrement == this.capacity) {
                signalNotFull();
            }
            return dequeue;
        } finally {
            reentrantLock.unlock();
        }
    }

    public E peek() {
        AtomicInteger atomicInteger = this.count;
        E e = null;
        if (atomicInteger.get() == 0) {
            return null;
        }
        ReentrantLock reentrantLock = this.takeLock;
        reentrantLock.lock();
        try {
            if (atomicInteger.get() > 0) {
                e = this.head.next.item;
            }
            return e;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public void unlink(Node<E> node, Node<E> node2) {
        node.item = null;
        node2.next = node.next;
        if (this.last == node) {
            this.last = node2;
        }
        if (this.count.getAndDecrement() == this.capacity) {
            this.notFull.signal();
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }
        fullyLock();
        try {
            Node<E> node = this.head;
            Node<E> node2 = node.next;
            while (true) {
                Node<E> node3 = node2;
                Node<E> node4 = node;
                node = node3;
                if (node == null) {
                    fullyUnlock();
                    return false;
                } else if (obj.equals(node.item)) {
                    unlink(node, node4);
                    fullyUnlock();
                    return true;
                } else {
                    node2 = node.next;
                }
            }
        } catch (Throwable th) {
            fullyUnlock();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        fullyLock();
        try {
            Node<E> node = this.head;
            do {
                node = node.next;
                if (node == null) {
                    fullyUnlock();
                    return false;
                }
            } while (!obj.equals(node.item));
            fullyUnlock();
            return true;
        } catch (Throwable th) {
            fullyUnlock();
            throw th;
        }
    }

    public Object[] toArray() {
        fullyLock();
        try {
            Object[] objArr = new Object[this.count.get()];
            Node<E> node = this.head.next;
            int i = 0;
            while (node != null) {
                int i2 = i + 1;
                objArr[i] = node.item;
                node = node.next;
                i = i2;
            }
            return objArr;
        } finally {
            fullyUnlock();
        }
    }

    public <T> T[] toArray(T[] tArr) {
        fullyLock();
        try {
            int i = this.count.get();
            int length = tArr.length;
            T[] tArr2 = tArr;
            if (length < i) {
                tArr2 = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i);
            }
            Node<E> node = this.head.next;
            int i2 = 0;
            while (node != null) {
                tArr2[i2] = node.item;
                node = node.next;
                i2++;
            }
            if (tArr2.length > i2) {
                tArr2[i2] = null;
            }
            return tArr2;
        } finally {
            fullyUnlock();
        }
    }

    public String toString() {
        return Helpers.collectionToString(this);
    }

    public void clear() {
        fullyLock();
        try {
            Node<E> node = this.head;
            while (true) {
                Node<E> node2 = node.next;
                if (node2 == null) {
                    break;
                }
                node.next = node;
                node2.item = null;
                node = node2;
            }
            this.head = this.last;
            if (this.count.getAndSet(0) == this.capacity) {
                this.notFull.signal();
            }
        } finally {
            fullyUnlock();
        }
    }

    public int drainTo(Collection<? super E> collection) {
        return drainTo(collection, Integer.MAX_VALUE);
    }

    public int drainTo(Collection<? super E> collection, int i) {
        Node<E> node;
        int i2;
        Objects.requireNonNull(collection);
        if (collection != this) {
            boolean z = false;
            if (i <= 0) {
                return 0;
            }
            ReentrantLock reentrantLock = this.takeLock;
            reentrantLock.lock();
            try {
                int min = Math.min(i, this.count.get());
                node = this.head;
                i2 = 0;
                while (i2 < min) {
                    Node<E> node2 = node.next;
                    collection.add(node2.item);
                    node2.item = null;
                    node.next = node;
                    i2++;
                    node = node2;
                }
                if (i2 > 0) {
                    this.head = node;
                    if (this.count.getAndAdd(-i2) == this.capacity) {
                        z = true;
                    }
                }
                reentrantLock.unlock();
                if (z) {
                    signalNotFull();
                }
                return min;
            } catch (Throwable th) {
                reentrantLock.unlock();
                if (0 != 0) {
                    signalNotFull();
                }
                throw th;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /* access modifiers changed from: package-private */
    public Node<E> succ(Node<E> node) {
        Node<E> node2 = node.next;
        return node == node2 ? this.head.next : node2;
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        private Node<E> ancestor;
        private Node<E> lastRet;
        private Node<E> next;
        private E nextItem;

        Itr() {
            LinkedBlockingQueue.this.fullyLock();
            try {
                Node<E> node = LinkedBlockingQueue.this.head.next;
                this.next = node;
                if (node != null) {
                    this.nextItem = node.item;
                }
            } finally {
                LinkedBlockingQueue.this.fullyUnlock();
            }
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public E next() {
            Node<E> node = this.next;
            if (node != null) {
                this.lastRet = node;
                E e = this.nextItem;
                LinkedBlockingQueue.this.fullyLock();
                try {
                    Node<E> node2 = node.next;
                    E e2 = null;
                    while (node2 != null) {
                        e2 = node2.item;
                        if (e2 != null) {
                            break;
                        }
                        node2 = LinkedBlockingQueue.this.succ(node2);
                    }
                    this.next = node2;
                    this.nextItem = e2;
                    return e;
                } finally {
                    LinkedBlockingQueue.this.fullyUnlock();
                }
            } else {
                throw new NoSuchElementException();
            }
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            int i;
            Objects.requireNonNull(consumer);
            Node<E> node = this.next;
            if (node != null) {
                this.lastRet = node;
                this.next = null;
                Object[] objArr = null;
                int i2 = 1;
                do {
                    LinkedBlockingQueue.this.fullyLock();
                    if (objArr == null) {
                        try {
                            node = node.next;
                            Node<E> node2 = node;
                            while (true) {
                                if (node2 != null) {
                                    if (node2.item != null && (i2 = i2 + 1) == 64) {
                                        break;
                                    }
                                    node2 = LinkedBlockingQueue.this.succ(node2);
                                } else {
                                    break;
                                }
                            }
                            objArr = new Object[i2];
                            objArr[0] = this.nextItem;
                            this.nextItem = null;
                            i = 1;
                        } catch (Throwable th) {
                            LinkedBlockingQueue.this.fullyUnlock();
                            throw th;
                        }
                    } else {
                        i = 0;
                    }
                    while (node != null && i < i2) {
                        E e = node.item;
                        objArr[i] = e;
                        if (e != null) {
                            this.lastRet = node;
                            i++;
                        }
                        node = LinkedBlockingQueue.this.succ(node);
                    }
                    LinkedBlockingQueue.this.fullyUnlock();
                    for (int i3 = 0; i3 < i; i3++) {
                        consumer.accept(objArr[i3]);
                    }
                    if (i <= 0) {
                        return;
                    }
                } while (node != null);
            }
        }

        public void remove() {
            Node<E> node = this.lastRet;
            if (node != null) {
                this.lastRet = null;
                LinkedBlockingQueue.this.fullyLock();
                try {
                    if (node.item != null) {
                        if (this.ancestor == null) {
                            this.ancestor = LinkedBlockingQueue.this.head;
                        }
                        Node<E> findPred = LinkedBlockingQueue.this.findPred(node, this.ancestor);
                        this.ancestor = findPred;
                        LinkedBlockingQueue.this.unlink(node, findPred);
                    }
                } finally {
                    LinkedBlockingQueue.this.fullyUnlock();
                }
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private final class LBQSpliterator implements Spliterator<E> {
        static final int MAX_BATCH = 33554432;
        int batch;
        Node<E> current;
        long est;
        boolean exhausted;

        public int characteristics() {
            return 4368;
        }

        LBQSpliterator() {
            this.est = (long) LinkedBlockingQueue.this.size();
        }

        public long estimateSize() {
            return this.est;
        }

        /* JADX INFO: finally extract failed */
        /* JADX WARNING: Removed duplicated region for block: B:28:0x005e  */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x0063  */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x0071  */
        /* JADX WARNING: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.util.Spliterator<E> trySplit() {
            /*
                r10 = this;
                boolean r0 = r10.exhausted
                if (r0 != 0) goto L_0x0078
                java.util.concurrent.LinkedBlockingQueue$Node<E> r0 = r10.current
                if (r0 != 0) goto L_0x0010
                java.util.concurrent.LinkedBlockingQueue r0 = java.util.concurrent.LinkedBlockingQueue.this
                java.util.concurrent.LinkedBlockingQueue$Node<E> r0 = r0.head
                java.util.concurrent.LinkedBlockingQueue$Node<E> r0 = r0.next
                if (r0 == 0) goto L_0x0078
            L_0x0010:
                java.util.concurrent.LinkedBlockingQueue$Node<E> r0 = r0.next
                if (r0 == 0) goto L_0x0078
                int r0 = r10.batch
                r1 = 1
                int r0 = r0 + r1
                r2 = 33554432(0x2000000, float:9.403955E-38)
                int r0 = java.lang.Math.min((int) r0, (int) r2)
                r10.batch = r0
                java.lang.Object[] r2 = new java.lang.Object[r0]
                java.util.concurrent.LinkedBlockingQueue$Node<E> r3 = r10.current
                java.util.concurrent.LinkedBlockingQueue r4 = java.util.concurrent.LinkedBlockingQueue.this
                r4.fullyLock()
                r4 = 0
                if (r3 != 0) goto L_0x0039
                java.util.concurrent.LinkedBlockingQueue r3 = java.util.concurrent.LinkedBlockingQueue.this     // Catch:{ all -> 0x0037 }
                java.util.concurrent.LinkedBlockingQueue$Node<E> r3 = r3.head     // Catch:{ all -> 0x0037 }
                java.util.concurrent.LinkedBlockingQueue$Node<E> r3 = r3.next     // Catch:{ all -> 0x0037 }
                if (r3 == 0) goto L_0x0035
                goto L_0x0039
            L_0x0035:
                r5 = r4
                goto L_0x0053
            L_0x0037:
                r0 = move-exception
                goto L_0x004d
            L_0x0039:
                r5 = r4
            L_0x003a:
                if (r3 == 0) goto L_0x0053
                if (r5 >= r0) goto L_0x0053
                E r6 = r3.item     // Catch:{ all -> 0x0037 }
                r2[r5] = r6     // Catch:{ all -> 0x0037 }
                if (r6 == 0) goto L_0x0046
                int r5 = r5 + 1
            L_0x0046:
                java.util.concurrent.LinkedBlockingQueue r6 = java.util.concurrent.LinkedBlockingQueue.this     // Catch:{ all -> 0x0037 }
                java.util.concurrent.LinkedBlockingQueue$Node r3 = r6.succ(r3)     // Catch:{ all -> 0x0037 }
                goto L_0x003a
            L_0x004d:
                java.util.concurrent.LinkedBlockingQueue r10 = java.util.concurrent.LinkedBlockingQueue.this
                r10.fullyUnlock()
                throw r0
            L_0x0053:
                java.util.concurrent.LinkedBlockingQueue r0 = java.util.concurrent.LinkedBlockingQueue.this
                r0.fullyUnlock()
                r10.current = r3
                r6 = 0
                if (r3 != 0) goto L_0x0063
                r10.est = r6
                r10.exhausted = r1
                goto L_0x006f
            L_0x0063:
                long r0 = r10.est
                long r8 = (long) r5
                long r0 = r0 - r8
                r10.est = r0
                int r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
                if (r0 >= 0) goto L_0x006f
                r10.est = r6
            L_0x006f:
                if (r5 <= 0) goto L_0x0078
                r10 = 4368(0x1110, float:6.121E-42)
                java.util.Spliterator r10 = java.util.Spliterators.spliterator((java.lang.Object[]) r2, (int) r4, (int) r5, (int) r10)
                return r10
            L_0x0078:
                r10 = 0
                return r10
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedBlockingQueue.LBQSpliterator.trySplit():java.util.Spliterator");
        }

        /* JADX WARNING: Removed duplicated region for block: B:15:0x002c A[Catch:{ all -> 0x0039 }] */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0035  */
        /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryAdvance(java.util.function.Consumer<? super E> r4) {
            /*
                r3 = this;
                java.util.Objects.requireNonNull(r4)
                boolean r0 = r3.exhausted
                if (r0 != 0) goto L_0x0040
                java.util.concurrent.LinkedBlockingQueue r0 = java.util.concurrent.LinkedBlockingQueue.this
                r0.fullyLock()
                java.util.concurrent.LinkedBlockingQueue$Node<E> r0 = r3.current     // Catch:{ all -> 0x0039 }
                if (r0 != 0) goto L_0x001b
                java.util.concurrent.LinkedBlockingQueue r0 = java.util.concurrent.LinkedBlockingQueue.this     // Catch:{ all -> 0x0039 }
                java.util.concurrent.LinkedBlockingQueue$Node<E> r0 = r0.head     // Catch:{ all -> 0x0039 }
                java.util.concurrent.LinkedBlockingQueue$Node<E> r0 = r0.next     // Catch:{ all -> 0x0039 }
                if (r0 == 0) goto L_0x0019
                goto L_0x001b
            L_0x0019:
                r1 = 0
                goto L_0x0027
            L_0x001b:
                E r1 = r0.item     // Catch:{ all -> 0x0039 }
                java.util.concurrent.LinkedBlockingQueue r2 = java.util.concurrent.LinkedBlockingQueue.this     // Catch:{ all -> 0x0039 }
                java.util.concurrent.LinkedBlockingQueue$Node r0 = r2.succ(r0)     // Catch:{ all -> 0x0039 }
                if (r1 != 0) goto L_0x0027
                if (r0 != 0) goto L_0x001b
            L_0x0027:
                r3.current = r0     // Catch:{ all -> 0x0039 }
                r2 = 1
                if (r0 != 0) goto L_0x002e
                r3.exhausted = r2     // Catch:{ all -> 0x0039 }
            L_0x002e:
                java.util.concurrent.LinkedBlockingQueue r3 = java.util.concurrent.LinkedBlockingQueue.this
                r3.fullyUnlock()
                if (r1 == 0) goto L_0x0040
                r4.accept(r1)
                return r2
            L_0x0039:
                r4 = move-exception
                java.util.concurrent.LinkedBlockingQueue r3 = java.util.concurrent.LinkedBlockingQueue.this
                r3.fullyUnlock()
                throw r4
            L_0x0040:
                r3 = 0
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedBlockingQueue.LBQSpliterator.tryAdvance(java.util.function.Consumer):boolean");
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            if (!this.exhausted) {
                this.exhausted = true;
                Node<E> node = this.current;
                this.current = null;
                LinkedBlockingQueue.this.forEachFrom(consumer, node);
            }
        }
    }

    public Spliterator<E> spliterator() {
        return new LBQSpliterator();
    }

    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        forEachFrom(consumer, (Node) null);
    }

    /* access modifiers changed from: package-private */
    public void forEachFrom(Consumer<? super E> consumer, Node<E> node) {
        Object[] objArr = null;
        int i = 0;
        do {
            fullyLock();
            if (objArr == null) {
                if (node == null) {
                    try {
                        node = this.head.next;
                    } catch (Throwable th) {
                        fullyUnlock();
                        throw th;
                    }
                }
                Node<E> node2 = node;
                while (true) {
                    if (node2 != null) {
                        if (node2.item != null && (i = i + 1) == 64) {
                            break;
                        }
                        node2 = succ(node2);
                    } else {
                        break;
                    }
                }
                objArr = new Object[i];
            }
            int i2 = 0;
            while (node != null && i2 < i) {
                E e = node.item;
                objArr[i2] = e;
                if (e != null) {
                    i2++;
                }
                node = succ(node);
            }
            fullyUnlock();
            for (int i3 = 0; i3 < i2; i3++) {
                consumer.accept(objArr[i3]);
            }
            if (i2 <= 0) {
                return;
            }
        } while (node != null);
    }

    public boolean removeIf(Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return bulkRemove(predicate);
    }

    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new LinkedBlockingQueue$$ExternalSyntheticLambda0(collection));
    }

    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new LinkedBlockingQueue$$ExternalSyntheticLambda1(collection));
    }

    static /* synthetic */ boolean lambda$retainAll$1(Collection collection, Object obj) {
        return !collection.contains(obj);
    }

    /* access modifiers changed from: package-private */
    public Node<E> findPred(Node<E> node, Node<E> node2) {
        if (node2.item == null) {
            node2 = this.head;
        }
        while (true) {
            Node<E> node3 = node2.next;
            if (node3 == node) {
                return node2;
            }
            node2 = node3;
        }
    }

    private boolean bulkRemove(Predicate<? super E> predicate) {
        Node<E> node = this.head;
        Node[] nodeArr = null;
        Node<E> node2 = null;
        int i = 0;
        boolean z = false;
        do {
            fullyLock();
            if (nodeArr == null) {
                try {
                    node2 = this.head.next;
                    Node<E> node3 = node2;
                    while (true) {
                        if (node3 != null) {
                            if (node3.item != null && (i = i + 1) == 64) {
                                break;
                            }
                            node3 = succ(node3);
                        } else {
                            break;
                        }
                    }
                    nodeArr = new Node[i];
                } catch (Throwable th) {
                    fullyUnlock();
                    throw th;
                }
            }
            int i2 = 0;
            while (node2 != null && i2 < i) {
                int i3 = i2 + 1;
                nodeArr[i2] = node2;
                node2 = succ(node2);
                i2 = i3;
            }
            fullyUnlock();
            long j = 0;
            long j2 = 0;
            for (int i4 = 0; i4 < i2; i4++) {
                E e = nodeArr[i4].item;
                Predicate<? super E> predicate2 = predicate;
                if (e != null && predicate2.test(e)) {
                    j2 |= 1 << i4;
                }
            }
            Predicate<? super E> predicate3 = predicate;
            if (j2 != 0) {
                fullyLock();
                int i5 = 0;
                while (i5 < i2) {
                    if ((j2 & (1 << i5)) != j) {
                        try {
                            Node node4 = nodeArr[i5];
                            if (node4.item != null) {
                                node = findPred(node4, node);
                                unlink(node4, node);
                                z = true;
                            }
                        } catch (Throwable th2) {
                            fullyUnlock();
                            throw th2;
                        }
                    }
                    nodeArr[i5] = null;
                    i5++;
                    j = 0;
                }
                fullyUnlock();
            }
            if (i2 <= 0) {
                break;
            }
        } while (node2 != null);
        return z;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        fullyLock();
        try {
            objectOutputStream.defaultWriteObject();
            Node<E> node = this.head;
            while (true) {
                node = node.next;
                if (node != null) {
                    objectOutputStream.writeObject(node.item);
                } else {
                    objectOutputStream.writeObject((Object) null);
                    return;
                }
            }
        } finally {
            fullyUnlock();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.count.set(0);
        Node<E> node = new Node<>(null);
        this.head = node;
        this.last = node;
        while (true) {
            Object readObject = objectInputStream.readObject();
            if (readObject != null) {
                add(readObject);
            } else {
                return;
            }
        }
    }
}
