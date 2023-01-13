package java.util.concurrent;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import jdk.internal.misc.SharedSecrets;

public class PriorityBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {
    private static final VarHandle ALLOCATIONSPINLOCK;
    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    private static final int MAX_ARRAY_SIZE = 2147483639;
    private static final long serialVersionUID = 5595510919245408276L;
    private volatile transient int allocationSpinLock;
    private transient Comparator<? super E> comparator;
    private final ReentrantLock lock;
    private final Condition notEmpty;

    /* renamed from: q */
    private PriorityQueue<E> f757q;
    private transient Object[] queue;
    private transient int size;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.PriorityBlockingQueue.tryGrow(java.lang.Object[], int):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 4 more
        */
    private void tryGrow(java.lang.Object[] r1, int r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.PriorityBlockingQueue.tryGrow(java.lang.Object[], int):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.PriorityBlockingQueue.tryGrow(java.lang.Object[], int):void");
    }

    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    public PriorityBlockingQueue() {
        this(11, (Comparator) null);
    }

    public PriorityBlockingQueue(int i) {
        this(i, (Comparator) null);
    }

    public PriorityBlockingQueue(int i, Comparator<? super E> comparator2) {
        ReentrantLock reentrantLock = new ReentrantLock();
        this.lock = reentrantLock;
        this.notEmpty = reentrantLock.newCondition();
        if (i >= 1) {
            this.comparator = comparator2;
            this.queue = new Object[Math.max(1, i)];
            return;
        }
        throw new IllegalArgumentException();
    }

    public PriorityBlockingQueue(Collection<? extends E> collection) {
        boolean z;
        boolean z2;
        ReentrantLock reentrantLock = new ReentrantLock();
        this.lock = reentrantLock;
        this.notEmpty = reentrantLock.newCondition();
        if (collection instanceof SortedSet) {
            this.comparator = ((SortedSet) collection).comparator();
            z = false;
            z2 = true;
        } else {
            if (collection instanceof PriorityBlockingQueue) {
                PriorityBlockingQueue priorityBlockingQueue = (PriorityBlockingQueue) collection;
                this.comparator = priorityBlockingQueue.comparator();
                if (priorityBlockingQueue.getClass() == PriorityBlockingQueue.class) {
                    z2 = false;
                } else {
                    z2 = false;
                    z = true;
                }
            } else {
                z2 = true;
            }
            z = z2;
        }
        Object[] array = collection.toArray();
        int length = array.length;
        array = array.getClass() != Object[].class ? Arrays.copyOf(array, length, Object[].class) : array;
        if (z2 && (length == 1 || this.comparator != null)) {
            for (Object obj : array) {
                obj.getClass();
            }
        }
        this.queue = ensureNonEmpty(array);
        this.size = length;
        if (z) {
            heapify();
        }
    }

    private static Object[] ensureNonEmpty(Object[] objArr) {
        return objArr.length > 0 ? objArr : new Object[1];
    }

    private E dequeue() {
        E[] eArr = this.queue;
        E e = eArr[0];
        if (e != null) {
            int i = this.size - 1;
            this.size = i;
            E e2 = eArr[i];
            eArr[i] = null;
            if (i > 0) {
                Comparator<? super E> comparator2 = this.comparator;
                if (comparator2 == null) {
                    siftDownComparable(0, e2, eArr, i);
                } else {
                    siftDownUsingComparator(0, e2, eArr, i, comparator2);
                }
            }
        }
        return e;
    }

    private static <T> void siftUpComparable(int i, T t, Object[] objArr) {
        Comparable comparable = (Comparable) t;
        while (i > 0) {
            int i2 = (i - 1) >>> 1;
            Object obj = objArr[i2];
            if (comparable.compareTo(obj) >= 0) {
                break;
            }
            objArr[i] = obj;
            i = i2;
        }
        objArr[i] = comparable;
    }

    private static <T> void siftUpUsingComparator(int i, T t, Object[] objArr, Comparator<? super T> comparator2) {
        while (i > 0) {
            int i2 = (i - 1) >>> 1;
            Object obj = objArr[i2];
            if (comparator2.compare(t, obj) >= 0) {
                break;
            }
            objArr[i] = obj;
            i = i2;
        }
        objArr[i] = t;
    }

    private static <T> void siftDownComparable(int i, T t, Object[] objArr, int i2) {
        Comparable comparable = (Comparable) t;
        int i3 = i2 >>> 1;
        while (i < i3) {
            int i4 = (i << 1) + 1;
            Comparable comparable2 = objArr[i4];
            int i5 = i4 + 1;
            if (i5 < i2 && comparable2.compareTo(objArr[i5]) > 0) {
                comparable2 = objArr[i5];
                i4 = i5;
            }
            if (comparable.compareTo(comparable2) <= 0) {
                break;
            }
            objArr[i] = comparable2;
            i = i4;
        }
        objArr[i] = comparable;
    }

    private static <T> void siftDownUsingComparator(int i, T t, Object[] objArr, int i2, Comparator<? super T> comparator2) {
        int i3 = i2 >>> 1;
        while (i < i3) {
            int i4 = (i << 1) + 1;
            Object obj = objArr[i4];
            int i5 = i4 + 1;
            if (i5 < i2 && comparator2.compare(obj, objArr[i5]) > 0) {
                obj = objArr[i5];
                i4 = i5;
            }
            if (comparator2.compare(t, obj) <= 0) {
                break;
            }
            objArr[i] = obj;
            i = i4;
        }
        objArr[i] = t;
    }

    private void heapify() {
        Object[] objArr = this.queue;
        int i = this.size;
        int i2 = (i >>> 1) - 1;
        Comparator<? super E> comparator2 = this.comparator;
        if (comparator2 == null) {
            while (i2 >= 0) {
                siftDownComparable(i2, objArr[i2], objArr, i);
                i2--;
            }
            return;
        }
        while (i2 >= 0) {
            siftDownUsingComparator(i2, objArr[i2], objArr, i, comparator2);
            i2--;
        }
    }

    public boolean add(E e) {
        return offer(e);
    }

    public boolean offer(E e) {
        int i;
        Object[] objArr;
        e.getClass();
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        while (true) {
            i = this.size;
            objArr = this.queue;
            int length = objArr.length;
            if (i >= length) {
                tryGrow(objArr, length);
            } else {
                try {
                    break;
                } finally {
                    reentrantLock.unlock();
                }
            }
        }
        Comparator<? super E> comparator2 = this.comparator;
        if (comparator2 == null) {
            siftUpComparable(i, e, objArr);
        } else {
            siftUpUsingComparator(i, e, objArr, comparator2);
        }
        this.size = i + 1;
        this.notEmpty.signal();
        return true;
    }

    public void put(E e) {
        offer(e);
    }

    public boolean offer(E e, long j, TimeUnit timeUnit) {
        return offer(e);
    }

    public E poll() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return dequeue();
        } finally {
            reentrantLock.unlock();
        }
    }

    public E take() throws InterruptedException {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (true) {
            try {
                E dequeue = dequeue();
                if (dequeue != null) {
                    return dequeue;
                }
                this.notEmpty.await();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public E poll(long j, TimeUnit timeUnit) throws InterruptedException {
        E dequeue;
        long nanos = timeUnit.toNanos(j);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (true) {
            try {
                dequeue = dequeue();
                if (dequeue == null && nanos > 0) {
                    nanos = this.notEmpty.awaitNanos(nanos);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        return dequeue;
    }

    public E peek() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.queue[0];
        } finally {
            reentrantLock.unlock();
        }
    }

    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    public int size() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.size;
        } finally {
            reentrantLock.unlock();
        }
    }

    private int indexOf(Object obj) {
        if (obj == null) {
            return -1;
        }
        Object[] objArr = this.queue;
        int i = this.size;
        for (int i2 = 0; i2 < i; i2++) {
            if (obj.equals(objArr[i2])) {
                return i2;
            }
        }
        return -1;
    }

    private void removeAt(int i) {
        Object[] objArr = this.queue;
        int i2 = this.size - 1;
        if (i2 == i) {
            objArr[i] = null;
        } else {
            Object obj = objArr[i2];
            objArr[i2] = null;
            Comparator<? super E> comparator2 = this.comparator;
            if (comparator2 == null) {
                siftDownComparable(i, obj, objArr, i2);
            } else {
                siftDownUsingComparator(i, obj, objArr, i2, comparator2);
            }
            if (objArr[i] == obj) {
                if (comparator2 == null) {
                    siftUpComparable(i, obj, objArr);
                } else {
                    siftUpUsingComparator(i, obj, objArr, comparator2);
                }
            }
        }
        this.size = i2;
    }

    /* JADX INFO: finally extract failed */
    public boolean remove(Object obj) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int indexOf = indexOf(obj);
            if (indexOf == -1) {
                reentrantLock.unlock();
                return false;
            }
            removeAt(indexOf);
            reentrantLock.unlock();
            return true;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    /* access modifiers changed from: package-private */
    public void removeEq(Object obj) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] objArr = this.queue;
            int i = this.size;
            int i2 = 0;
            while (true) {
                if (i2 >= i) {
                    break;
                } else if (obj == objArr[i2]) {
                    removeAt(i2);
                    break;
                } else {
                    i2++;
                }
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean contains(Object obj) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return indexOf(obj) != -1;
        } finally {
            reentrantLock.unlock();
        }
    }

    public String toString() {
        return Helpers.collectionToString(this);
    }

    public int drainTo(Collection<? super E> collection) {
        return drainTo(collection, Integer.MAX_VALUE);
    }

    public int drainTo(Collection<? super E> collection, int i) {
        Objects.requireNonNull(collection);
        if (collection == this) {
            throw new IllegalArgumentException();
        } else if (i <= 0) {
            return 0;
        } else {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                int min = Math.min(this.size, i);
                for (int i2 = 0; i2 < min; i2++) {
                    collection.add(this.queue[0]);
                    dequeue();
                }
                return min;
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public void clear() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] objArr = this.queue;
            int i = this.size;
            for (int i2 = 0; i2 < i; i2++) {
                objArr[i2] = null;
            }
            this.size = 0;
        } finally {
            reentrantLock.unlock();
        }
    }

    public Object[] toArray() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return Arrays.copyOf((T[]) this.queue, this.size);
        } finally {
            reentrantLock.unlock();
        }
    }

    public <T> T[] toArray(T[] tArr) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int i = this.size;
            if (tArr.length < i) {
                return Arrays.copyOf(this.queue, i, tArr.getClass());
            }
            System.arraycopy((Object) this.queue, 0, (Object) tArr, 0, i);
            if (tArr.length > i) {
                tArr[i] = null;
            }
            reentrantLock.unlock();
            return tArr;
        } finally {
            reentrantLock.unlock();
        }
    }

    public Iterator<E> iterator() {
        return new Itr(toArray());
    }

    final class Itr implements Iterator<E> {
        final Object[] array;
        int cursor;
        int lastRet = -1;

        Itr(Object[] objArr) {
            this.array = objArr;
        }

        public boolean hasNext() {
            return this.cursor < this.array.length;
        }

        public E next() {
            int i = this.cursor;
            E[] eArr = this.array;
            if (i < eArr.length) {
                this.cursor = i + 1;
                this.lastRet = i;
                return eArr[i];
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            int i = this.lastRet;
            if (i >= 0) {
                PriorityBlockingQueue.this.removeEq(this.array[i]);
                this.lastRet = -1;
                return;
            }
            throw new IllegalStateException();
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            Object[] objArr = this.array;
            int i = this.cursor;
            if (i < objArr.length) {
                this.lastRet = -1;
                this.cursor = objArr.length;
                while (i < objArr.length) {
                    consumer.accept(objArr[i]);
                    i++;
                }
                this.lastRet = objArr.length - 1;
            }
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        this.lock.lock();
        try {
            PriorityQueue<E> priorityQueue = new PriorityQueue<>(Math.max(this.size, 1), this.comparator);
            this.f757q = priorityQueue;
            priorityQueue.addAll(this);
            objectOutputStream.defaultWriteObject();
        } finally {
            this.f757q = null;
            this.lock.unlock();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            objectInputStream.defaultReadObject();
            int size2 = this.f757q.size();
            SharedSecrets.getJavaObjectInputStreamAccess().checkArray(objectInputStream, Object[].class, size2);
            this.queue = new Object[Math.max(1, size2)];
            this.comparator = this.f757q.comparator();
            addAll(this.f757q);
        } finally {
            this.f757q = null;
        }
    }

    final class PBQSpliterator implements Spliterator<E> {
        Object[] array;
        int fence;
        int index;

        public int characteristics() {
            return 16704;
        }

        PBQSpliterator() {
        }

        PBQSpliterator(Object[] objArr, int i, int i2) {
            this.array = objArr;
            this.index = i;
            this.fence = i2;
        }

        private int getFence() {
            if (this.array == null) {
                Object[] array2 = PriorityBlockingQueue.this.toArray();
                this.array = array2;
                this.fence = array2.length;
            }
            return this.fence;
        }

        public PriorityBlockingQueue<E>.PBQSpliterator trySplit() {
            int fence2 = getFence();
            int i = this.index;
            int i2 = (fence2 + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            PriorityBlockingQueue priorityBlockingQueue = PriorityBlockingQueue.this;
            Object[] objArr = this.array;
            this.index = i2;
            return new PBQSpliterator(objArr, i, i2);
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            int fence2 = getFence();
            Object[] objArr = this.array;
            this.index = fence2;
            for (int i = this.index; i < fence2; i++) {
                consumer.accept(objArr[i]);
            }
        }

        public boolean tryAdvance(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            int fence2 = getFence();
            int i = this.index;
            if (fence2 <= i || i < 0) {
                return false;
            }
            Object[] objArr = this.array;
            this.index = i + 1;
            consumer.accept(objArr[i]);
            return true;
        }

        public long estimateSize() {
            return (long) (getFence() - this.index);
        }
    }

    public Spliterator<E> spliterator() {
        return new PBQSpliterator();
    }

    public boolean removeIf(Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return bulkRemove(predicate);
    }

    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new PriorityBlockingQueue$$ExternalSyntheticLambda1(collection));
    }

    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new PriorityBlockingQueue$$ExternalSyntheticLambda0(collection));
    }

    static /* synthetic */ boolean lambda$retainAll$1(Collection collection, Object obj) {
        return !collection.contains(obj);
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

    private boolean bulkRemove(Predicate<? super E> predicate) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] objArr = this.queue;
            int i = this.size;
            int i2 = 0;
            while (i2 < i && !predicate.test(objArr[i2])) {
                i2++;
            }
            if (i2 >= i) {
                return false;
            }
            long[] nBits = nBits(i - i2);
            nBits[0] = 1;
            for (int i3 = i2 + 1; i3 < i; i3++) {
                if (predicate.test(objArr[i3])) {
                    setBit(nBits, i3 - i2);
                }
            }
            int i4 = i2;
            int i5 = i4;
            while (i4 < i) {
                if (isClear(nBits, i4 - i2)) {
                    objArr[i5] = objArr[i4];
                    i5++;
                }
                i4++;
            }
            this.size = i5;
            while (i5 < i) {
                objArr[i5] = null;
                i5++;
            }
            heapify();
            reentrantLock.unlock();
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] objArr = this.queue;
            int i = this.size;
            for (int i2 = 0; i2 < i; i2++) {
                consumer.accept(objArr[i2]);
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    static {
        try {
            ALLOCATIONSPINLOCK = MethodHandles.lookup().findVarHandle(PriorityBlockingQueue.class, "allocationSpinLock", Integer.TYPE);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }
}
