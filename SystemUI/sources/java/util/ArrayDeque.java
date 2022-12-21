package java.util;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.function.Consumer;

public class ArrayDeque<E> extends AbstractCollection<E> implements Deque<E>, Cloneable, Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int MIN_INITIAL_CAPACITY = 8;
    private static final long serialVersionUID = 2340985798034038923L;
    transient Object[] elements;
    transient int head;
    transient int tail;

    private void checkInvariants() {
    }

    private void allocateElements(int i) {
        int i2 = 8;
        if (i >= 8) {
            int i3 = i | (i >>> 1);
            int i4 = i3 | (i3 >>> 2);
            int i5 = i4 | (i4 >>> 4);
            int i6 = i5 | (i5 >>> 8);
            i2 = (i6 | (i6 >>> 16)) + 1;
            if (i2 < 0) {
                i2 >>>= 1;
            }
        }
        this.elements = new Object[i2];
    }

    private void doubleCapacity() {
        int i = this.head;
        Object[] objArr = this.elements;
        int length = objArr.length;
        int i2 = length - i;
        int i3 = length << 1;
        if (i3 >= 0) {
            Object[] objArr2 = new Object[i3];
            System.arraycopy((Object) objArr, i, (Object) objArr2, 0, i2);
            System.arraycopy((Object) this.elements, 0, (Object) objArr2, i2, i);
            Arrays.fill(this.elements, (Object) null);
            this.elements = objArr2;
            this.head = 0;
            this.tail = length;
            return;
        }
        throw new IllegalStateException("Sorry, deque too big");
    }

    public ArrayDeque() {
        this.elements = new Object[16];
    }

    public ArrayDeque(int i) {
        allocateElements(i);
    }

    public ArrayDeque(Collection<? extends E> collection) {
        allocateElements(collection.size());
        addAll(collection);
    }

    public void addFirst(E e) {
        e.getClass();
        Object[] objArr = this.elements;
        int length = (this.head - 1) & (objArr.length - 1);
        this.head = length;
        objArr[length] = e;
        if (length == this.tail) {
            doubleCapacity();
        }
    }

    public void addLast(E e) {
        e.getClass();
        Object[] objArr = this.elements;
        int i = this.tail;
        objArr[i] = e;
        int length = (objArr.length - 1) & (i + 1);
        this.tail = length;
        if (length == this.head) {
            doubleCapacity();
        }
    }

    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    public boolean offerLast(E e) {
        addLast(e);
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
        E[] eArr = this.elements;
        int i = this.head;
        E e = eArr[i];
        if (e != null) {
            eArr[i] = null;
            this.head = (eArr.length - 1) & (i + 1);
        }
        return e;
    }

    public E pollLast() {
        E[] eArr = this.elements;
        int length = (this.tail - 1) & (eArr.length - 1);
        E e = eArr[length];
        if (e != null) {
            eArr[length] = null;
            this.tail = length;
        }
        return e;
    }

    public E getFirst() {
        E e = this.elements[this.head];
        if (e != null) {
            return e;
        }
        throw new NoSuchElementException();
    }

    public E getLast() {
        E[] eArr = this.elements;
        E e = eArr[(this.tail - 1) & (eArr.length - 1)];
        if (e != null) {
            return e;
        }
        throw new NoSuchElementException();
    }

    public E peekFirst() {
        return this.elements[this.head];
    }

    public E peekLast() {
        E[] eArr = this.elements;
        return eArr[(this.tail - 1) & (eArr.length - 1)];
    }

    public boolean removeFirstOccurrence(Object obj) {
        if (obj == null) {
            return false;
        }
        int length = this.elements.length - 1;
        int i = this.head;
        while (true) {
            Object obj2 = this.elements[i];
            if (obj2 == null) {
                return false;
            }
            if (obj.equals(obj2)) {
                delete(i);
                return true;
            }
            i = (i + 1) & length;
        }
    }

    public boolean removeLastOccurrence(Object obj) {
        if (obj == null) {
            return false;
        }
        int length = this.elements.length - 1;
        int i = this.tail - 1;
        while (true) {
            int i2 = i & length;
            Object obj2 = this.elements[i2];
            if (obj2 == null) {
                return false;
            }
            if (obj.equals(obj2)) {
                delete(i2);
                return true;
            }
            i = i2 - 1;
        }
    }

    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public boolean offer(E e) {
        return offerLast(e);
    }

    public E remove() {
        return removeFirst();
    }

    public E poll() {
        return pollFirst();
    }

    public E element() {
        return getFirst();
    }

    public E peek() {
        return peekFirst();
    }

    public void push(E e) {
        addFirst(e);
    }

    public E pop() {
        return removeFirst();
    }

    /* access modifiers changed from: package-private */
    public boolean delete(int i) {
        checkInvariants();
        Object[] objArr = this.elements;
        int length = objArr.length - 1;
        int i2 = this.head;
        int i3 = this.tail;
        int i4 = (i - i2) & length;
        int i5 = (i3 - i) & length;
        if (i4 >= ((i3 - i2) & length)) {
            throw new ConcurrentModificationException();
        } else if (i4 < i5) {
            if (i2 <= i) {
                System.arraycopy((Object) objArr, i2, (Object) objArr, i2 + 1, i4);
            } else {
                System.arraycopy((Object) objArr, 0, (Object) objArr, 1, i);
                objArr[0] = objArr[length];
                System.arraycopy((Object) objArr, i2, (Object) objArr, i2 + 1, length - i2);
            }
            objArr[i2] = null;
            this.head = (i2 + 1) & length;
            return false;
        } else {
            if (i < i3) {
                System.arraycopy((Object) objArr, i + 1, (Object) objArr, i, i5);
                this.tail = i3 - 1;
            } else {
                System.arraycopy((Object) objArr, i + 1, (Object) objArr, i, length - i);
                objArr[length] = objArr[0];
                System.arraycopy((Object) objArr, 1, (Object) objArr, 0, i3);
                this.tail = (i3 - 1) & length;
            }
            return true;
        }
    }

    public int size() {
        return (this.elements.length - 1) & (this.tail - this.head);
    }

    public boolean isEmpty() {
        return this.head == this.tail;
    }

    public Iterator<E> iterator() {
        return new DeqIterator();
    }

    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    private class DeqIterator implements Iterator<E> {
        private int cursor;
        private int fence;
        private int lastRet;

        private DeqIterator() {
            this.cursor = ArrayDeque.this.head;
            this.fence = ArrayDeque.this.tail;
            this.lastRet = -1;
        }

        public boolean hasNext() {
            return this.cursor != this.fence;
        }

        public E next() {
            if (this.cursor != this.fence) {
                E e = ArrayDeque.this.elements[this.cursor];
                if (ArrayDeque.this.tail != this.fence || e == null) {
                    throw new ConcurrentModificationException();
                }
                int i = this.cursor;
                this.lastRet = i;
                this.cursor = (i + 1) & (ArrayDeque.this.elements.length - 1);
                return e;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            int i = this.lastRet;
            if (i >= 0) {
                if (ArrayDeque.this.delete(i)) {
                    this.cursor = (this.cursor - 1) & (ArrayDeque.this.elements.length - 1);
                    this.fence = ArrayDeque.this.tail;
                }
                this.lastRet = -1;
                return;
            }
            throw new IllegalStateException();
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            Object[] objArr = ArrayDeque.this.elements;
            int length = objArr.length - 1;
            int i = this.fence;
            int i2 = this.cursor;
            this.cursor = i;
            while (i2 != i) {
                Object obj = objArr[i2];
                i2 = (i2 + 1) & length;
                if (obj != null) {
                    consumer.accept(obj);
                } else {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    private class DescendingIterator implements Iterator<E> {
        private int cursor;
        private int fence;
        private int lastRet;

        private DescendingIterator() {
            this.cursor = ArrayDeque.this.tail;
            this.fence = ArrayDeque.this.head;
            this.lastRet = -1;
        }

        public boolean hasNext() {
            return this.cursor != this.fence;
        }

        public E next() {
            int i = this.cursor;
            if (i != this.fence) {
                this.cursor = (i - 1) & (ArrayDeque.this.elements.length - 1);
                E e = ArrayDeque.this.elements[this.cursor];
                if (ArrayDeque.this.head != this.fence || e == null) {
                    throw new ConcurrentModificationException();
                }
                this.lastRet = this.cursor;
                return e;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            int i = this.lastRet;
            if (i >= 0) {
                if (!ArrayDeque.this.delete(i)) {
                    this.cursor = (this.cursor + 1) & (ArrayDeque.this.elements.length - 1);
                    this.fence = ArrayDeque.this.head;
                }
                this.lastRet = -1;
                return;
            }
            throw new IllegalStateException();
        }
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        int length = this.elements.length - 1;
        int i = this.head;
        while (true) {
            Object obj2 = this.elements[i];
            if (obj2 == null) {
                return false;
            }
            if (obj.equals(obj2)) {
                return true;
            }
            i = (i + 1) & length;
        }
    }

    public boolean remove(Object obj) {
        return removeFirstOccurrence(obj);
    }

    public void clear() {
        int i = this.head;
        int i2 = this.tail;
        if (i != i2) {
            this.tail = 0;
            this.head = 0;
            int length = this.elements.length - 1;
            do {
                this.elements[i] = null;
                i = (i + 1) & length;
            } while (i != i2);
        }
    }

    public Object[] toArray() {
        int i = this.head;
        int i2 = this.tail;
        boolean z = i2 < i;
        Object[] copyOfRange = Arrays.copyOfRange((T[]) this.elements, i, z ? this.elements.length + i2 : i2);
        if (z) {
            Object[] objArr = this.elements;
            System.arraycopy((Object) objArr, 0, (Object) copyOfRange, objArr.length - i, i2);
        }
        return copyOfRange;
    }

    public <T> T[] toArray(T[] tArr) {
        int i = this.head;
        int i2 = this.tail;
        boolean z = i2 < i;
        int length = (i2 - i) + (z ? this.elements.length : 0);
        int i3 = length - (z ? i2 : 0);
        int length2 = tArr.length;
        if (length > length2) {
            tArr = Arrays.copyOfRange(this.elements, i, length + i, tArr.getClass());
        } else {
            System.arraycopy((Object) this.elements, i, (Object) tArr, 0, i3);
            if (length < length2) {
                tArr[length] = null;
            }
        }
        if (z) {
            System.arraycopy((Object) this.elements, 0, (Object) tArr, i3, i2);
        }
        return tArr;
    }

    public ArrayDeque<E> clone() {
        try {
            ArrayDeque<E> arrayDeque = (ArrayDeque) super.clone();
            Object[] objArr = this.elements;
            arrayDeque.elements = Arrays.copyOf((T[]) objArr, objArr.length);
            return arrayDeque;
        } catch (CloneNotSupportedException unused) {
            throw new AssertionError();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(size());
        int length = this.elements.length - 1;
        for (int i = this.head; i != this.tail; i = (i + 1) & length) {
            objectOutputStream.writeObject(this.elements[i]);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        allocateElements(readInt);
        this.head = 0;
        this.tail = readInt;
        for (int i = 0; i < readInt; i++) {
            this.elements[i] = objectInputStream.readObject();
        }
    }

    public Spliterator<E> spliterator() {
        return new DeqSpliterator(this, -1, -1);
    }

    static final class DeqSpliterator<E> implements Spliterator<E> {
        private final ArrayDeque<E> deq;
        private int fence;
        private int index;

        public int characteristics() {
            return 16720;
        }

        DeqSpliterator(ArrayDeque<E> arrayDeque, int i, int i2) {
            this.deq = arrayDeque;
            this.index = i;
            this.fence = i2;
        }

        private int getFence() {
            int i = this.fence;
            if (i >= 0) {
                return i;
            }
            int i2 = this.deq.tail;
            this.fence = i2;
            this.index = this.deq.head;
            return i2;
        }

        public DeqSpliterator<E> trySplit() {
            int fence2 = getFence();
            int i = this.index;
            int length = this.deq.elements.length;
            if (i == fence2) {
                return null;
            }
            int i2 = length - 1;
            if (((i + 1) & i2) == fence2) {
                return null;
            }
            if (i > fence2) {
                fence2 += length;
            }
            int i3 = ((fence2 + i) >>> 1) & i2;
            ArrayDeque<E> arrayDeque = this.deq;
            this.index = i3;
            return new DeqSpliterator<>(arrayDeque, i, i3);
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            consumer.getClass();
            Object[] objArr = this.deq.elements;
            int length = objArr.length - 1;
            int fence2 = getFence();
            int i = this.index;
            this.index = fence2;
            while (i != fence2) {
                Object obj = objArr[i];
                i = (i + 1) & length;
                if (obj != null) {
                    consumer.accept(obj);
                } else {
                    throw new ConcurrentModificationException();
                }
            }
        }

        public boolean tryAdvance(Consumer<? super E> consumer) {
            consumer.getClass();
            Object[] objArr = this.deq.elements;
            int length = objArr.length - 1;
            int fence2 = getFence();
            int i = this.index;
            if (i == fence2) {
                return false;
            }
            Object obj = objArr[i];
            this.index = length & (i + 1);
            if (obj != null) {
                consumer.accept(obj);
                return true;
            }
            throw new ConcurrentModificationException();
        }

        public long estimateSize() {
            int fence2 = getFence() - this.index;
            if (fence2 < 0) {
                fence2 += this.deq.elements.length;
            }
            return (long) fence2;
        }
    }
}
