package java.util;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.function.Consumer;

public class PriorityQueue<E> extends AbstractQueue<E> implements Serializable {
    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    private static final int MAX_ARRAY_SIZE = 2147483639;
    private static final long serialVersionUID = -7720805057305804111L;
    private final Comparator<? super E> comparator;
    transient int modCount;
    transient Object[] queue;
    int size;

    public PriorityQueue() {
        this(11, (Comparator) null);
    }

    public PriorityQueue(int i) {
        this(i, (Comparator) null);
    }

    public PriorityQueue(Comparator<? super E> comparator2) {
        this(11, comparator2);
    }

    public PriorityQueue(int i, Comparator<? super E> comparator2) {
        if (i >= 1) {
            this.queue = new Object[i];
            this.comparator = comparator2;
            return;
        }
        throw new IllegalArgumentException();
    }

    public PriorityQueue(Collection<? extends E> collection) {
        if (collection instanceof SortedSet) {
            SortedSet sortedSet = (SortedSet) collection;
            this.comparator = sortedSet.comparator();
            initElementsFromCollection(sortedSet);
        } else if (collection instanceof PriorityQueue) {
            PriorityQueue priorityQueue = (PriorityQueue) collection;
            this.comparator = priorityQueue.comparator();
            initFromPriorityQueue(priorityQueue);
        } else {
            this.comparator = null;
            initFromCollection(collection);
        }
    }

    public PriorityQueue(PriorityQueue<? extends E> priorityQueue) {
        this.comparator = priorityQueue.comparator();
        initFromPriorityQueue(priorityQueue);
    }

    public PriorityQueue(SortedSet<? extends E> sortedSet) {
        this.comparator = sortedSet.comparator();
        initElementsFromCollection(sortedSet);
    }

    private void initFromPriorityQueue(PriorityQueue<? extends E> priorityQueue) {
        if (priorityQueue.getClass() == PriorityQueue.class) {
            this.queue = priorityQueue.toArray();
            this.size = priorityQueue.size();
            return;
        }
        initFromCollection(priorityQueue);
    }

    private void initElementsFromCollection(Collection<? extends E> collection) {
        Object[] array = collection.toArray();
        if (array.getClass() != Object[].class) {
            array = Arrays.copyOf(array, array.length, Object[].class);
        }
        if (array.length == 1 || this.comparator != null) {
            for (Object obj : array) {
                obj.getClass();
            }
        }
        this.queue = array;
        this.size = array.length;
    }

    private void initFromCollection(Collection<? extends E> collection) {
        initElementsFromCollection(collection);
        heapify();
    }

    private void grow(int i) {
        int length = this.queue.length;
        int i2 = length + (length < 64 ? length + 2 : length >> 1);
        if (i2 - MAX_ARRAY_SIZE > 0) {
            i2 = hugeCapacity(i);
        }
        this.queue = Arrays.copyOf((T[]) this.queue, i2);
    }

    private static int hugeCapacity(int i) {
        if (i < 0) {
            throw new OutOfMemoryError();
        } else if (i > MAX_ARRAY_SIZE) {
            return Integer.MAX_VALUE;
        } else {
            return MAX_ARRAY_SIZE;
        }
    }

    public boolean add(E e) {
        return offer(e);
    }

    public boolean offer(E e) {
        e.getClass();
        this.modCount++;
        int i = this.size;
        if (i >= this.queue.length) {
            grow(i + 1);
        }
        this.size = i + 1;
        if (i == 0) {
            this.queue[0] = e;
        } else {
            siftUp(i, e);
        }
        return true;
    }

    public E peek() {
        if (this.size == 0) {
            return null;
        }
        return this.queue[0];
    }

    private int indexOf(Object obj) {
        if (obj == null) {
            return -1;
        }
        for (int i = 0; i < this.size; i++) {
            if (obj.equals(this.queue[i])) {
                return i;
            }
        }
        return -1;
    }

    public boolean remove(Object obj) {
        int indexOf = indexOf(obj);
        if (indexOf == -1) {
            return false;
        }
        removeAt(indexOf);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean removeEq(Object obj) {
        for (int i = 0; i < this.size; i++) {
            if (obj == this.queue[i]) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    public Object[] toArray() {
        return Arrays.copyOf((T[]) this.queue, this.size);
    }

    public <T> T[] toArray(T[] tArr) {
        int i = this.size;
        if (tArr.length < i) {
            return Arrays.copyOf(this.queue, i, tArr.getClass());
        }
        System.arraycopy((Object) this.queue, 0, (Object) tArr, 0, i);
        if (tArr.length > i) {
            tArr[i] = null;
        }
        return tArr;
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    private final class Itr implements Iterator<E> {
        private int cursor;
        private int expectedModCount;
        private ArrayDeque<E> forgetMeNot;
        private int lastRet;
        private E lastRetElt;

        private Itr() {
            this.lastRet = -1;
            this.expectedModCount = PriorityQueue.this.modCount;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
            r2 = r2.forgetMeNot;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean hasNext() {
            /*
                r2 = this;
                int r0 = r2.cursor
                java.util.PriorityQueue r1 = java.util.PriorityQueue.this
                int r1 = r1.size
                if (r0 < r1) goto L_0x0015
                java.util.ArrayDeque<E> r2 = r2.forgetMeNot
                if (r2 == 0) goto L_0x0013
                boolean r2 = r2.isEmpty()
                if (r2 != 0) goto L_0x0013
                goto L_0x0015
            L_0x0013:
                r2 = 0
                goto L_0x0016
            L_0x0015:
                r2 = 1
            L_0x0016:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.PriorityQueue.Itr.hasNext():boolean");
        }

        public E next() {
            if (this.expectedModCount != PriorityQueue.this.modCount) {
                throw new ConcurrentModificationException();
            } else if (this.cursor < PriorityQueue.this.size) {
                E[] eArr = PriorityQueue.this.queue;
                int i = this.cursor;
                this.cursor = i + 1;
                this.lastRet = i;
                return eArr[i];
            } else {
                ArrayDeque<E> arrayDeque = this.forgetMeNot;
                if (arrayDeque != null) {
                    this.lastRet = -1;
                    E poll = arrayDeque.poll();
                    this.lastRetElt = poll;
                    if (poll != null) {
                        return poll;
                    }
                }
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (this.expectedModCount == PriorityQueue.this.modCount) {
                int i = this.lastRet;
                if (i != -1) {
                    Object removeAt = PriorityQueue.this.removeAt(i);
                    this.lastRet = -1;
                    if (removeAt == null) {
                        this.cursor--;
                    } else {
                        if (this.forgetMeNot == null) {
                            this.forgetMeNot = new ArrayDeque<>();
                        }
                        this.forgetMeNot.add(removeAt);
                    }
                } else {
                    E e = this.lastRetElt;
                    if (e != null) {
                        PriorityQueue.this.removeEq(e);
                        this.lastRetElt = null;
                    } else {
                        throw new IllegalStateException();
                    }
                }
                this.expectedModCount = PriorityQueue.this.modCount;
                return;
            }
            throw new ConcurrentModificationException();
        }
    }

    public int size() {
        return this.size;
    }

    public void clear() {
        this.modCount++;
        for (int i = 0; i < this.size; i++) {
            this.queue[i] = null;
        }
        this.size = 0;
    }

    public E poll() {
        int i = this.size;
        if (i == 0) {
            return null;
        }
        int i2 = i - 1;
        this.size = i2;
        this.modCount++;
        E[] eArr = this.queue;
        E e = eArr[0];
        E e2 = eArr[i2];
        eArr[i2] = null;
        if (i2 != 0) {
            siftDown(0, e2);
        }
        return e;
    }

    /* access modifiers changed from: package-private */
    public E removeAt(int i) {
        this.modCount++;
        int i2 = this.size - 1;
        this.size = i2;
        if (i2 == i) {
            this.queue[i] = null;
        } else {
            E[] eArr = this.queue;
            E e = eArr[i2];
            eArr[i2] = null;
            siftDown(i, e);
            if (this.queue[i] == e) {
                siftUp(i, e);
                if (this.queue[i] != e) {
                    return e;
                }
            }
        }
        return null;
    }

    private void siftUp(int i, E e) {
        if (this.comparator != null) {
            siftUpUsingComparator(i, e);
        } else {
            siftUpComparable(i, e);
        }
    }

    private void siftUpComparable(int i, E e) {
        Comparable comparable = (Comparable) e;
        while (i > 0) {
            int i2 = (i - 1) >>> 1;
            Object obj = this.queue[i2];
            if (comparable.compareTo(obj) >= 0) {
                break;
            }
            this.queue[i] = obj;
            i = i2;
        }
        this.queue[i] = comparable;
    }

    private void siftUpUsingComparator(int i, E e) {
        while (i > 0) {
            int i2 = (i - 1) >>> 1;
            Object obj = this.queue[i2];
            if (this.comparator.compare(e, obj) >= 0) {
                break;
            }
            this.queue[i] = obj;
            i = i2;
        }
        this.queue[i] = e;
    }

    private void siftDown(int i, E e) {
        if (this.comparator != null) {
            siftDownUsingComparator(i, e);
        } else {
            siftDownComparable(i, e);
        }
    }

    private void siftDownComparable(int i, E e) {
        Comparable comparable = (Comparable) e;
        int i2 = this.size >>> 1;
        while (i < i2) {
            int i3 = (i << 1) + 1;
            Object[] objArr = this.queue;
            Object obj = objArr[i3];
            int i4 = i3 + 1;
            if (i4 < this.size && ((Comparable) obj).compareTo(objArr[i4]) > 0) {
                obj = this.queue[i4];
                i3 = i4;
            }
            if (comparable.compareTo(obj) <= 0) {
                break;
            }
            this.queue[i] = obj;
            i = i3;
        }
        this.queue[i] = comparable;
    }

    private void siftDownUsingComparator(int i, E e) {
        int i2 = this.size >>> 1;
        while (i < i2) {
            int i3 = (i << 1) + 1;
            Object[] objArr = this.queue;
            Object obj = objArr[i3];
            int i4 = i3 + 1;
            if (i4 < this.size && this.comparator.compare(obj, objArr[i4]) > 0) {
                obj = this.queue[i4];
                i3 = i4;
            }
            if (this.comparator.compare(e, obj) <= 0) {
                break;
            }
            this.queue[i] = obj;
            i = i3;
        }
        this.queue[i] = e;
    }

    private void heapify() {
        for (int i = (this.size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, this.queue[i]);
        }
    }

    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(Math.max(2, this.size + 1));
        for (int i = 0; i < this.size; i++) {
            objectOutputStream.writeObject(this.queue[i]);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        objectInputStream.readInt();
        this.queue = new Object[this.size];
        for (int i = 0; i < this.size; i++) {
            this.queue[i] = objectInputStream.readObject();
        }
        heapify();
    }

    public final Spliterator<E> spliterator() {
        return new PriorityQueueSpliterator(this, 0, -1, 0);
    }

    static final class PriorityQueueSpliterator<E> implements Spliterator<E> {
        private int expectedModCount;
        private int fence;
        private int index;

        /* renamed from: pq */
        private final PriorityQueue<E> f703pq;

        public int characteristics() {
            return 16704;
        }

        PriorityQueueSpliterator(PriorityQueue<E> priorityQueue, int i, int i2, int i3) {
            this.f703pq = priorityQueue;
            this.index = i;
            this.fence = i2;
            this.expectedModCount = i3;
        }

        private int getFence() {
            int i = this.fence;
            if (i >= 0) {
                return i;
            }
            this.expectedModCount = this.f703pq.modCount;
            int i2 = this.f703pq.size;
            this.fence = i2;
            return i2;
        }

        public PriorityQueueSpliterator<E> trySplit() {
            int fence2 = getFence();
            int i = this.index;
            int i2 = (fence2 + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            PriorityQueue<E> priorityQueue = this.f703pq;
            this.index = i2;
            return new PriorityQueueSpliterator<>(priorityQueue, i, i2, this.expectedModCount);
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Object[] objArr;
            int i;
            consumer.getClass();
            PriorityQueue<E> priorityQueue = this.f703pq;
            if (priorityQueue != null && (objArr = priorityQueue.queue) != null) {
                int i2 = this.fence;
                if (i2 < 0) {
                    int i3 = priorityQueue.modCount;
                    i = i3;
                    i2 = priorityQueue.size;
                } else {
                    i = this.expectedModCount;
                }
                int i4 = this.index;
                if (i4 >= 0) {
                    this.index = i2;
                    if (i2 <= objArr.length) {
                        while (true) {
                            if (i4 < i2) {
                                Object obj = objArr[i4];
                                if (obj == null) {
                                    break;
                                }
                                consumer.accept(obj);
                                i4++;
                            } else if (priorityQueue.modCount == i) {
                                return;
                            }
                        }
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        public boolean tryAdvance(Consumer<? super E> consumer) {
            consumer.getClass();
            int fence2 = getFence();
            int i = this.index;
            if (i < 0 || i >= fence2) {
                return false;
            }
            this.index = i + 1;
            Object obj = this.f703pq.queue[i];
            if (obj != null) {
                consumer.accept(obj);
                if (this.f703pq.modCount == this.expectedModCount) {
                    return true;
                }
                throw new ConcurrentModificationException();
            }
            throw new ConcurrentModificationException();
        }

        public long estimateSize() {
            return (long) (getFence() - this.index);
        }
    }
}
