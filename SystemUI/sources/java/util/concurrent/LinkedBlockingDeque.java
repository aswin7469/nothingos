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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LinkedBlockingDeque<E> extends AbstractQueue<E> implements BlockingDeque<E>, Serializable {
    private static final long serialVersionUID = -387911632671998426L;
    private final int capacity;
    private transient int count;
    transient Node<E> first;
    transient Node<E> last;
    final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    static final class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(E e) {
            this.item = e;
        }
    }

    public LinkedBlockingDeque() {
        this(Integer.MAX_VALUE);
    }

    public LinkedBlockingDeque(int i) {
        ReentrantLock reentrantLock = new ReentrantLock();
        this.lock = reentrantLock;
        this.notEmpty = reentrantLock.newCondition();
        this.notFull = reentrantLock.newCondition();
        if (i > 0) {
            this.capacity = i;
            return;
        }
        throw new IllegalArgumentException();
    }

    public LinkedBlockingDeque(Collection<? extends E> collection) {
        this(Integer.MAX_VALUE);
        addAll(collection);
    }

    private boolean linkFirst(Node<E> node) {
        if (this.count >= this.capacity) {
            return false;
        }
        Node<E> node2 = this.first;
        node.next = node2;
        this.first = node;
        if (this.last == null) {
            this.last = node;
        } else {
            node2.prev = node;
        }
        this.count++;
        this.notEmpty.signal();
        return true;
    }

    private boolean linkLast(Node<E> node) {
        if (this.count >= this.capacity) {
            return false;
        }
        Node<E> node2 = this.last;
        node.prev = node2;
        this.last = node;
        if (this.first == null) {
            this.first = node;
        } else {
            node2.next = node;
        }
        this.count++;
        this.notEmpty.signal();
        return true;
    }

    private E unlinkFirst() {
        Node<E> node = this.first;
        if (node == null) {
            return null;
        }
        Node<E> node2 = node.next;
        E e = node.item;
        node.item = null;
        node.next = node;
        this.first = node2;
        if (node2 == null) {
            this.last = null;
        } else {
            node2.prev = null;
        }
        this.count--;
        this.notFull.signal();
        return e;
    }

    private E unlinkLast() {
        Node<E> node = this.last;
        if (node == null) {
            return null;
        }
        Node<E> node2 = node.prev;
        E e = node.item;
        node.item = null;
        node.prev = node;
        this.last = node2;
        if (node2 == null) {
            this.first = null;
        } else {
            node2.next = null;
        }
        this.count--;
        this.notFull.signal();
        return e;
    }

    /* access modifiers changed from: package-private */
    public void unlink(Node<E> node) {
        Node<E> node2 = node.prev;
        Node<E> node3 = node.next;
        if (node2 == null) {
            unlinkFirst();
        } else if (node3 == null) {
            unlinkLast();
        } else {
            node2.next = node3;
            node3.prev = node2;
            node.item = null;
            this.count--;
            this.notFull.signal();
        }
    }

    public void addFirst(E e) {
        if (!offerFirst(e)) {
            throw new IllegalStateException("Deque full");
        }
    }

    public void addLast(E e) {
        if (!offerLast(e)) {
            throw new IllegalStateException("Deque full");
        }
    }

    public boolean offerFirst(E e) {
        e.getClass();
        Node node = new Node(e);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return linkFirst(node);
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean offerLast(E e) {
        e.getClass();
        Node node = new Node(e);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return linkLast(node);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void putFirst(E e) throws InterruptedException {
        e.getClass();
        Node node = new Node(e);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        while (!linkFirst(node)) {
            try {
                this.notFull.await();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public void putLast(E e) throws InterruptedException {
        e.getClass();
        Node node = new Node(e);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        while (!linkLast(node)) {
            try {
                this.notFull.await();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean offerFirst(E e, long j, TimeUnit timeUnit) throws InterruptedException {
        e.getClass();
        Node node = new Node(e);
        long nanos = timeUnit.toNanos(j);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (!linkFirst(node)) {
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
        reentrantLock.unlock();
        return true;
    }

    /* JADX INFO: finally extract failed */
    public boolean offerLast(E e, long j, TimeUnit timeUnit) throws InterruptedException {
        e.getClass();
        Node node = new Node(e);
        long nanos = timeUnit.toNanos(j);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (!linkLast(node)) {
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
        reentrantLock.unlock();
        return true;
    }

    public E removeFirst() {
        E pollFirst = pollFirst();
        if (pollFirst != null) {
            return pollFirst;
        }
        throw new NoSuchElementException();
    }

    public E removeLast() {
        E pollLast = pollLast();
        if (pollLast != null) {
            return pollLast;
        }
        throw new NoSuchElementException();
    }

    public E pollFirst() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return unlinkFirst();
        } finally {
            reentrantLock.unlock();
        }
    }

    public E pollLast() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return unlinkLast();
        } finally {
            reentrantLock.unlock();
        }
    }

    public E takeFirst() throws InterruptedException {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        while (true) {
            try {
                E unlinkFirst = unlinkFirst();
                if (unlinkFirst != null) {
                    return unlinkFirst;
                }
                this.notEmpty.await();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public E takeLast() throws InterruptedException {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        while (true) {
            try {
                E unlinkLast = unlinkLast();
                if (unlinkLast != null) {
                    return unlinkLast;
                }
                this.notEmpty.await();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public E pollFirst(long j, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (true) {
            try {
                E unlinkFirst = unlinkFirst();
                if (unlinkFirst != null) {
                    reentrantLock.unlock();
                    return unlinkFirst;
                } else if (nanos <= 0) {
                    reentrantLock.unlock();
                    return null;
                } else {
                    nanos = this.notEmpty.awaitNanos(nanos);
                }
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public E pollLast(long j, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (true) {
            try {
                E unlinkLast = unlinkLast();
                if (unlinkLast != null) {
                    reentrantLock.unlock();
                    return unlinkLast;
                } else if (nanos <= 0) {
                    reentrantLock.unlock();
                    return null;
                } else {
                    nanos = this.notEmpty.awaitNanos(nanos);
                }
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }
    }

    public E getFirst() {
        E peekFirst = peekFirst();
        if (peekFirst != null) {
            return peekFirst;
        }
        throw new NoSuchElementException();
    }

    public E getLast() {
        E peekLast = peekLast();
        if (peekLast != null) {
            return peekLast;
        }
        throw new NoSuchElementException();
    }

    public E peekFirst() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Node<E> node = this.first;
            return node == null ? null : node.item;
        } finally {
            reentrantLock.unlock();
        }
    }

    public E peekLast() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Node<E> node = this.last;
            return node == null ? null : node.item;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean removeFirstOccurrence(Object obj) {
        if (obj == null) {
            return false;
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            for (Node<E> node = this.first; node != null; node = node.next) {
                if (obj.equals(node.item)) {
                    unlink(node);
                    reentrantLock.unlock();
                    return true;
                }
            }
            reentrantLock.unlock();
            return false;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean removeLastOccurrence(Object obj) {
        if (obj == null) {
            return false;
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            for (Node<E> node = this.last; node != null; node = node.prev) {
                if (obj.equals(node.item)) {
                    unlink(node);
                    reentrantLock.unlock();
                    return true;
                }
            }
            reentrantLock.unlock();
            return false;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public boolean offer(E e) {
        return offerLast(e);
    }

    public void put(E e) throws InterruptedException {
        putLast(e);
    }

    public boolean offer(E e, long j, TimeUnit timeUnit) throws InterruptedException {
        return offerLast(e, j, timeUnit);
    }

    public E remove() {
        return removeFirst();
    }

    public E poll() {
        return pollFirst();
    }

    public E take() throws InterruptedException {
        return takeFirst();
    }

    public E poll(long j, TimeUnit timeUnit) throws InterruptedException {
        return pollFirst(j, timeUnit);
    }

    public E element() {
        return getFirst();
    }

    public E peek() {
        return peekFirst();
    }

    public int remainingCapacity() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.capacity - this.count;
        } finally {
            reentrantLock.unlock();
        }
    }

    public int drainTo(Collection<? super E> collection) {
        return drainTo(collection, Integer.MAX_VALUE);
    }

    public int drainTo(Collection<? super E> collection, int i) {
        Objects.requireNonNull(collection);
        if (collection != this) {
            if (i <= 0) {
                return 0;
            }
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                int min = Math.min(i, this.count);
                for (int i2 = 0; i2 < min; i2++) {
                    collection.add(this.first.item);
                    unlinkFirst();
                }
                return min;
            } finally {
                reentrantLock.unlock();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void push(E e) {
        addFirst(e);
    }

    public E pop() {
        return removeFirst();
    }

    public boolean remove(Object obj) {
        return removeFirstOccurrence(obj);
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

    /* JADX INFO: finally extract failed */
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            for (Node<E> node = this.first; node != null; node = node.next) {
                if (obj.equals(node.item)) {
                    reentrantLock.unlock();
                    return true;
                }
            }
            reentrantLock.unlock();
            return false;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean addAll(Collection<? extends E> collection) {
        if (collection != this) {
            Node<E> node = null;
            int i = 0;
            Node<E> node2 = null;
            for (Object next : collection) {
                Objects.requireNonNull(next);
                i++;
                Node<E> node3 = new Node<>(next);
                if (node == null) {
                    node = node3;
                } else {
                    node2.next = node3;
                    node3.prev = node2;
                }
                node2 = node3;
            }
            if (node == null) {
                return false;
            }
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                if (this.count + i <= this.capacity) {
                    node.prev = this.last;
                    if (this.first == null) {
                        this.first = node;
                    } else {
                        this.last.next = node;
                    }
                    this.last = node2;
                    this.count += i;
                    this.notEmpty.signalAll();
                    reentrantLock.unlock();
                    return true;
                }
                reentrantLock.unlock();
                return super.addAll(collection);
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Object[] toArray() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] objArr = new Object[this.count];
            Node<E> node = this.first;
            int i = 0;
            while (node != null) {
                int i2 = i + 1;
                objArr[i] = node.item;
                node = node.next;
                i = i2;
            }
            return objArr;
        } finally {
            reentrantLock.unlock();
        }
    }

    public <T> T[] toArray(T[] tArr) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int length = tArr.length;
            T[] tArr2 = tArr;
            if (length < this.count) {
                tArr2 = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), this.count);
            }
            Node<E> node = this.first;
            int i = 0;
            while (node != null) {
                tArr2[i] = node.item;
                node = node.next;
                i++;
            }
            if (tArr2.length > i) {
                tArr2[i] = null;
            }
            return tArr2;
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
            Node<E> node = this.first;
            while (node != null) {
                node.item = null;
                Node<E> node2 = node.next;
                node.prev = null;
                node.next = null;
                node = node2;
            }
            this.last = null;
            this.first = null;
            this.count = 0;
            this.notFull.signalAll();
        } finally {
            reentrantLock.unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public Node<E> succ(Node<E> node) {
        Node<E> node2 = node.next;
        return node == node2 ? this.first : node2;
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    public Iterator<E> descendingIterator() {
        return new DescendingItr();
    }

    private abstract class AbstractItr implements Iterator<E> {
        private Node<E> lastRet;
        Node<E> next;
        E nextItem;

        /* access modifiers changed from: package-private */
        public abstract Node<E> firstNode();

        /* access modifiers changed from: package-private */
        public abstract Node<E> nextNode(Node<E> node);

        private Node<E> succ(Node<E> node) {
            Node<E> nextNode = nextNode(node);
            return node == nextNode ? firstNode() : nextNode;
        }

        AbstractItr() {
            ReentrantLock reentrantLock = LinkedBlockingDeque.this.lock;
            reentrantLock.lock();
            try {
                Node<E> firstNode = firstNode();
                this.next = firstNode;
                if (firstNode != null) {
                    this.nextItem = firstNode.item;
                }
            } finally {
                reentrantLock.unlock();
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
                ReentrantLock reentrantLock = LinkedBlockingDeque.this.lock;
                reentrantLock.lock();
                try {
                    Node<E> nextNode = nextNode(node);
                    E e2 = null;
                    while (nextNode != null) {
                        e2 = nextNode.item;
                        if (e2 != null) {
                            break;
                        }
                        nextNode = succ(nextNode);
                    }
                    this.next = nextNode;
                    this.nextItem = e2;
                    return e;
                } finally {
                    reentrantLock.unlock();
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
                ReentrantLock reentrantLock = LinkedBlockingDeque.this.lock;
                Object[] objArr = null;
                int i2 = 1;
                do {
                    reentrantLock.lock();
                    if (objArr == null) {
                        try {
                            node = nextNode(node);
                            Node<E> node2 = node;
                            while (true) {
                                if (node2 != null) {
                                    if (node2.item != null && (i2 = i2 + 1) == 64) {
                                        break;
                                    }
                                    node2 = succ(node2);
                                } else {
                                    break;
                                }
                            }
                            objArr = new Object[i2];
                            objArr[0] = this.nextItem;
                            this.nextItem = null;
                            i = 1;
                        } catch (Throwable th) {
                            reentrantLock.unlock();
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
                        node = succ(node);
                    }
                    reentrantLock.unlock();
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
                ReentrantLock reentrantLock = LinkedBlockingDeque.this.lock;
                reentrantLock.lock();
                try {
                    if (node.item != null) {
                        LinkedBlockingDeque.this.unlink(node);
                    }
                } finally {
                    reentrantLock.unlock();
                }
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private class Itr extends LinkedBlockingDeque<E>.AbstractItr {
        Itr() {
            super();
        }

        /* access modifiers changed from: package-private */
        public Node<E> firstNode() {
            return LinkedBlockingDeque.this.first;
        }

        /* access modifiers changed from: package-private */
        public Node<E> nextNode(Node<E> node) {
            return node.next;
        }
    }

    private class DescendingItr extends LinkedBlockingDeque<E>.AbstractItr {
        DescendingItr() {
            super();
        }

        /* access modifiers changed from: package-private */
        public Node<E> firstNode() {
            return LinkedBlockingDeque.this.last;
        }

        /* access modifiers changed from: package-private */
        public Node<E> nextNode(Node<E> node) {
            return node.prev;
        }
    }

    private final class LBDSpliterator implements Spliterator<E> {
        static final int MAX_BATCH = 33554432;
        int batch;
        Node<E> current;
        long est;
        boolean exhausted;

        public int characteristics() {
            return 4368;
        }

        LBDSpliterator() {
            this.est = (long) LinkedBlockingDeque.this.size();
        }

        public long estimateSize() {
            return this.est;
        }

        /* JADX INFO: finally extract failed */
        /* JADX WARNING: Removed duplicated region for block: B:28:0x0058  */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x005d  */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x006b  */
        /* JADX WARNING: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.util.Spliterator<E> trySplit() {
            /*
                r9 = this;
                boolean r0 = r9.exhausted
                if (r0 != 0) goto L_0x0072
                java.util.concurrent.LinkedBlockingDeque$Node<E> r0 = r9.current
                if (r0 != 0) goto L_0x000e
                java.util.concurrent.LinkedBlockingDeque r0 = java.util.concurrent.LinkedBlockingDeque.this
                java.util.concurrent.LinkedBlockingDeque$Node<E> r0 = r0.first
                if (r0 == 0) goto L_0x0072
            L_0x000e:
                java.util.concurrent.LinkedBlockingDeque$Node<E> r0 = r0.next
                if (r0 == 0) goto L_0x0072
                int r0 = r9.batch
                r1 = 1
                int r0 = r0 + r1
                r2 = 33554432(0x2000000, float:9.403955E-38)
                int r0 = java.lang.Math.min((int) r0, (int) r2)
                r9.batch = r0
                java.lang.Object[] r2 = new java.lang.Object[r0]
                java.util.concurrent.LinkedBlockingDeque r3 = java.util.concurrent.LinkedBlockingDeque.this
                java.util.concurrent.locks.ReentrantLock r3 = r3.lock
                java.util.concurrent.LinkedBlockingDeque$Node<E> r4 = r9.current
                r3.lock()
                r5 = 0
                if (r4 != 0) goto L_0x0037
                java.util.concurrent.LinkedBlockingDeque r4 = java.util.concurrent.LinkedBlockingDeque.this     // Catch:{ all -> 0x0035 }
                java.util.concurrent.LinkedBlockingDeque$Node<E> r4 = r4.first     // Catch:{ all -> 0x0035 }
                if (r4 == 0) goto L_0x0033
                goto L_0x0037
            L_0x0033:
                r6 = r5
                goto L_0x004f
            L_0x0035:
                r9 = move-exception
                goto L_0x004b
            L_0x0037:
                r6 = r5
            L_0x0038:
                if (r4 == 0) goto L_0x004f
                if (r6 >= r0) goto L_0x004f
                E r7 = r4.item     // Catch:{ all -> 0x0035 }
                r2[r6] = r7     // Catch:{ all -> 0x0035 }
                if (r7 == 0) goto L_0x0044
                int r6 = r6 + 1
            L_0x0044:
                java.util.concurrent.LinkedBlockingDeque r7 = java.util.concurrent.LinkedBlockingDeque.this     // Catch:{ all -> 0x0035 }
                java.util.concurrent.LinkedBlockingDeque$Node r4 = r7.succ(r4)     // Catch:{ all -> 0x0035 }
                goto L_0x0038
            L_0x004b:
                r3.unlock()
                throw r9
            L_0x004f:
                r3.unlock()
                r9.current = r4
                r7 = 0
                if (r4 != 0) goto L_0x005d
                r9.est = r7
                r9.exhausted = r1
                goto L_0x0069
            L_0x005d:
                long r0 = r9.est
                long r3 = (long) r6
                long r0 = r0 - r3
                r9.est = r0
                int r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
                if (r0 >= 0) goto L_0x0069
                r9.est = r7
            L_0x0069:
                if (r6 <= 0) goto L_0x0072
                r9 = 4368(0x1110, float:6.121E-42)
                java.util.Spliterator r9 = java.util.Spliterators.spliterator((java.lang.Object[]) r2, (int) r5, (int) r6, (int) r9)
                return r9
            L_0x0072:
                r9 = 0
                return r9
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedBlockingDeque.LBDSpliterator.trySplit():java.util.Spliterator");
        }

        /* JADX WARNING: Removed duplicated region for block: B:15:0x002c A[Catch:{ all -> 0x0037 }] */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0033  */
        /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryAdvance(java.util.function.Consumer<? super E> r5) {
            /*
                r4 = this;
                java.util.Objects.requireNonNull(r5)
                boolean r0 = r4.exhausted
                if (r0 != 0) goto L_0x003c
                java.util.concurrent.LinkedBlockingDeque r0 = java.util.concurrent.LinkedBlockingDeque.this
                java.util.concurrent.locks.ReentrantLock r0 = r0.lock
                r0.lock()
                java.util.concurrent.LinkedBlockingDeque$Node<E> r1 = r4.current     // Catch:{ all -> 0x0037 }
                if (r1 != 0) goto L_0x001b
                java.util.concurrent.LinkedBlockingDeque r1 = java.util.concurrent.LinkedBlockingDeque.this     // Catch:{ all -> 0x0037 }
                java.util.concurrent.LinkedBlockingDeque$Node<E> r1 = r1.first     // Catch:{ all -> 0x0037 }
                if (r1 == 0) goto L_0x0019
                goto L_0x001b
            L_0x0019:
                r2 = 0
                goto L_0x0027
            L_0x001b:
                E r2 = r1.item     // Catch:{ all -> 0x0037 }
                java.util.concurrent.LinkedBlockingDeque r3 = java.util.concurrent.LinkedBlockingDeque.this     // Catch:{ all -> 0x0037 }
                java.util.concurrent.LinkedBlockingDeque$Node r1 = r3.succ(r1)     // Catch:{ all -> 0x0037 }
                if (r2 != 0) goto L_0x0027
                if (r1 != 0) goto L_0x001b
            L_0x0027:
                r4.current = r1     // Catch:{ all -> 0x0037 }
                r3 = 1
                if (r1 != 0) goto L_0x002e
                r4.exhausted = r3     // Catch:{ all -> 0x0037 }
            L_0x002e:
                r0.unlock()
                if (r2 == 0) goto L_0x003c
                r5.accept(r2)
                return r3
            L_0x0037:
                r4 = move-exception
                r0.unlock()
                throw r4
            L_0x003c:
                r4 = 0
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedBlockingDeque.LBDSpliterator.tryAdvance(java.util.function.Consumer):boolean");
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            if (!this.exhausted) {
                this.exhausted = true;
                Node<E> node = this.current;
                this.current = null;
                LinkedBlockingDeque.this.forEachFrom(consumer, node);
            }
        }
    }

    public Spliterator<E> spliterator() {
        return new LBDSpliterator();
    }

    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        forEachFrom(consumer, (Node) null);
    }

    /* access modifiers changed from: package-private */
    public void forEachFrom(Consumer<? super E> consumer, Node<E> node) {
        ReentrantLock reentrantLock = this.lock;
        Object[] objArr = null;
        int i = 0;
        do {
            reentrantLock.lock();
            if (objArr == null) {
                if (node == null) {
                    try {
                        node = this.first;
                    } catch (Throwable th) {
                        reentrantLock.unlock();
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
            reentrantLock.unlock();
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
        return bulkRemove(new LinkedBlockingDeque$$ExternalSyntheticLambda0(collection));
    }

    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new LinkedBlockingDeque$$ExternalSyntheticLambda1(collection));
    }

    static /* synthetic */ boolean lambda$retainAll$1(Collection collection, Object obj) {
        return !collection.contains(obj);
    }

    private boolean bulkRemove(Predicate<? super E> predicate) {
        ReentrantLock reentrantLock = this.lock;
        Node[] nodeArr = null;
        Node<E> node = null;
        int i = 0;
        boolean z = false;
        do {
            reentrantLock.lock();
            if (nodeArr == null) {
                try {
                    node = this.first;
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
                    nodeArr = new Node[i];
                } catch (Throwable th) {
                    reentrantLock.unlock();
                    throw th;
                }
            }
            int i2 = 0;
            while (node != null && i2 < i) {
                int i3 = i2 + 1;
                nodeArr[i2] = node;
                node = succ(node);
                i2 = i3;
            }
            reentrantLock.unlock();
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
                reentrantLock.lock();
                int i5 = 0;
                while (i5 < i2) {
                    if ((j2 & (1 << i5)) != j) {
                        try {
                            Node node3 = nodeArr[i5];
                            if (node3.item != null) {
                                unlink(node3);
                                z = true;
                            }
                        } catch (Throwable th2) {
                            reentrantLock.unlock();
                            throw th2;
                        }
                    }
                    nodeArr[i5] = null;
                    i5++;
                    j = 0;
                }
                reentrantLock.unlock();
            }
            if (i2 <= 0) {
                break;
            }
        } while (node != null);
        return z;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            objectOutputStream.defaultWriteObject();
            for (Node<E> node = this.first; node != null; node = node.next) {
                objectOutputStream.writeObject(node.item);
            }
            objectOutputStream.writeObject((Object) null);
        } finally {
            reentrantLock.unlock();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.count = 0;
        this.first = null;
        this.last = null;
        while (true) {
            Object readObject = objectInputStream.readObject();
            if (readObject != null) {
                add(readObject);
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void checkInvariants() {
        for (Node<E> node = this.first; node != null; node = node.next) {
        }
    }
}
