package java.util;

import java.p026io.IOException;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Vector<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable {
    private static final int MAX_ARRAY_SIZE = 2147483639;
    private static final long serialVersionUID = -2767605614048989439L;
    protected int capacityIncrement;
    protected int elementCount;
    protected Object[] elementData;

    public Vector(int i, int i2) {
        if (i >= 0) {
            this.elementData = new Object[i];
            this.capacityIncrement = i2;
            return;
        }
        throw new IllegalArgumentException("Illegal Capacity: " + i);
    }

    public Vector(int i) {
        this(i, 0);
    }

    public Vector() {
        this(10);
    }

    public Vector(Collection<? extends E> collection) {
        Object[] array = collection.toArray();
        this.elementData = array;
        this.elementCount = array.length;
        if (array.getClass() != Object[].class) {
            this.elementData = Arrays.copyOf(this.elementData, this.elementCount, Object[].class);
        }
    }

    public synchronized void copyInto(Object[] objArr) {
        System.arraycopy((Object) this.elementData, 0, (Object) objArr, 0, this.elementCount);
    }

    public synchronized void trimToSize() {
        this.modCount++;
        Object[] objArr = this.elementData;
        int length = objArr.length;
        int i = this.elementCount;
        if (i < length) {
            this.elementData = Arrays.copyOf((T[]) objArr, i);
        }
    }

    public synchronized void ensureCapacity(int i) {
        if (i > 0) {
            this.modCount++;
            ensureCapacityHelper(i);
        }
    }

    private void ensureCapacityHelper(int i) {
        if (i - this.elementData.length > 0) {
            grow(i);
        }
    }

    private void grow(int i) {
        int length = this.elementData.length;
        int i2 = this.capacityIncrement;
        if (i2 <= 0) {
            i2 = length;
        }
        int i3 = length + i2;
        if (i3 - i < 0) {
            i3 = i;
        }
        if (i3 - MAX_ARRAY_SIZE > 0) {
            i3 = hugeCapacity(i);
        }
        this.elementData = Arrays.copyOf((T[]) this.elementData, i3);
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

    public synchronized void setSize(int i) {
        this.modCount++;
        if (i > this.elementCount) {
            ensureCapacityHelper(i);
        } else {
            for (int i2 = i; i2 < this.elementCount; i2++) {
                this.elementData[i2] = null;
            }
        }
        this.elementCount = i;
    }

    public synchronized int capacity() {
        return this.elementData.length;
    }

    public synchronized int size() {
        return this.elementCount;
    }

    public synchronized boolean isEmpty() {
        return this.elementCount == 0;
    }

    public Enumeration<E> elements() {
        return new Enumeration<E>() {
            int count = 0;

            public boolean hasMoreElements() {
                return this.count < Vector.this.elementCount;
            }

            public E nextElement() {
                synchronized (Vector.this) {
                    if (this.count < Vector.this.elementCount) {
                        Vector vector = Vector.this;
                        int i = this.count;
                        this.count = i + 1;
                        E elementData = vector.elementData(i);
                        return elementData;
                    }
                    throw new NoSuchElementException("Vector Enumeration");
                }
            }
        };
    }

    public boolean contains(Object obj) {
        return indexOf(obj, 0) >= 0;
    }

    public int indexOf(Object obj) {
        return indexOf(obj, 0);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0026, code lost:
        return -1;
     */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:10:0x0012=Splitter:B:10:0x0012, B:2:0x0003=Splitter:B:2:0x0003} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int indexOf(java.lang.Object r2, int r3) {
        /*
            r1 = this;
            monitor-enter(r1)
            if (r2 != 0) goto L_0x0012
        L_0x0003:
            int r2 = r1.elementCount     // Catch:{ all -> 0x0028 }
            if (r3 >= r2) goto L_0x0025
            java.lang.Object[] r2 = r1.elementData     // Catch:{ all -> 0x0028 }
            r2 = r2[r3]     // Catch:{ all -> 0x0028 }
            if (r2 != 0) goto L_0x000f
            monitor-exit(r1)
            return r3
        L_0x000f:
            int r3 = r3 + 1
            goto L_0x0003
        L_0x0012:
            int r0 = r1.elementCount     // Catch:{ all -> 0x0028 }
            if (r3 >= r0) goto L_0x0025
            java.lang.Object[] r0 = r1.elementData     // Catch:{ all -> 0x0028 }
            r0 = r0[r3]     // Catch:{ all -> 0x0028 }
            boolean r0 = r2.equals(r0)     // Catch:{ all -> 0x0028 }
            if (r0 == 0) goto L_0x0022
            monitor-exit(r1)
            return r3
        L_0x0022:
            int r3 = r3 + 1
            goto L_0x0012
        L_0x0025:
            monitor-exit(r1)
            r1 = -1
            return r1
        L_0x0028:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Vector.indexOf(java.lang.Object, int):int");
    }

    public synchronized int lastIndexOf(Object obj) {
        return lastIndexOf(obj, this.elementCount - 1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0026, code lost:
        return -1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int lastIndexOf(java.lang.Object r2, int r3) {
        /*
            r1 = this;
            monitor-enter(r1)
            int r0 = r1.elementCount     // Catch:{ all -> 0x0044 }
            if (r3 >= r0) goto L_0x0028
            if (r2 != 0) goto L_0x0014
        L_0x0007:
            if (r3 < 0) goto L_0x0025
            java.lang.Object[] r2 = r1.elementData     // Catch:{ all -> 0x0044 }
            r2 = r2[r3]     // Catch:{ all -> 0x0044 }
            if (r2 != 0) goto L_0x0011
            monitor-exit(r1)
            return r3
        L_0x0011:
            int r3 = r3 + -1
            goto L_0x0007
        L_0x0014:
            if (r3 < 0) goto L_0x0025
            java.lang.Object[] r0 = r1.elementData     // Catch:{ all -> 0x0044 }
            r0 = r0[r3]     // Catch:{ all -> 0x0044 }
            boolean r0 = r2.equals(r0)     // Catch:{ all -> 0x0044 }
            if (r0 == 0) goto L_0x0022
            monitor-exit(r1)
            return r3
        L_0x0022:
            int r3 = r3 + -1
            goto L_0x0014
        L_0x0025:
            monitor-exit(r1)
            r1 = -1
            return r1
        L_0x0028:
            java.lang.IndexOutOfBoundsException r2 = new java.lang.IndexOutOfBoundsException     // Catch:{ all -> 0x0044 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0044 }
            r0.<init>()     // Catch:{ all -> 0x0044 }
            r0.append((int) r3)     // Catch:{ all -> 0x0044 }
            java.lang.String r3 = " >= "
            r0.append((java.lang.String) r3)     // Catch:{ all -> 0x0044 }
            int r3 = r1.elementCount     // Catch:{ all -> 0x0044 }
            r0.append((int) r3)     // Catch:{ all -> 0x0044 }
            java.lang.String r3 = r0.toString()     // Catch:{ all -> 0x0044 }
            r2.<init>((java.lang.String) r3)     // Catch:{ all -> 0x0044 }
            throw r2     // Catch:{ all -> 0x0044 }
        L_0x0044:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Vector.lastIndexOf(java.lang.Object, int):int");
    }

    public synchronized E elementAt(int i) {
        if (i < this.elementCount) {
        } else {
            throw new ArrayIndexOutOfBoundsException(i + " >= " + this.elementCount);
        }
        return elementData(i);
    }

    public synchronized E firstElement() {
        if (this.elementCount != 0) {
        } else {
            throw new NoSuchElementException();
        }
        return elementData(0);
    }

    public synchronized E lastElement() {
        int i;
        i = this.elementCount;
        if (i != 0) {
        } else {
            throw new NoSuchElementException();
        }
        return elementData(i - 1);
    }

    public synchronized void setElementAt(E e, int i) {
        if (i < this.elementCount) {
            this.elementData[i] = e;
        } else {
            throw new ArrayIndexOutOfBoundsException(i + " >= " + this.elementCount);
        }
    }

    public synchronized void removeElementAt(int i) {
        this.modCount++;
        int i2 = this.elementCount;
        if (i >= i2) {
            throw new ArrayIndexOutOfBoundsException(i + " >= " + this.elementCount);
        } else if (i >= 0) {
            int i3 = (i2 - i) - 1;
            if (i3 > 0) {
                Object[] objArr = this.elementData;
                System.arraycopy((Object) objArr, i + 1, (Object) objArr, i, i3);
            }
            int i4 = this.elementCount - 1;
            this.elementCount = i4;
            this.elementData[i4] = null;
        } else {
            throw new ArrayIndexOutOfBoundsException(i);
        }
    }

    public synchronized void insertElementAt(E e, int i) {
        this.modCount++;
        int i2 = this.elementCount;
        if (i <= i2) {
            ensureCapacityHelper(i2 + 1);
            Object[] objArr = this.elementData;
            System.arraycopy((Object) objArr, i, (Object) objArr, i + 1, this.elementCount - i);
            this.elementData[i] = e;
            this.elementCount++;
        } else {
            throw new ArrayIndexOutOfBoundsException(i + " > " + this.elementCount);
        }
    }

    public synchronized void addElement(E e) {
        this.modCount++;
        ensureCapacityHelper(this.elementCount + 1);
        Object[] objArr = this.elementData;
        int i = this.elementCount;
        this.elementCount = i + 1;
        objArr[i] = e;
    }

    public synchronized boolean removeElement(Object obj) {
        this.modCount++;
        int indexOf = indexOf(obj);
        if (indexOf < 0) {
            return false;
        }
        removeElementAt(indexOf);
        return true;
    }

    public synchronized void removeAllElements() {
        this.modCount++;
        for (int i = 0; i < this.elementCount; i++) {
            this.elementData[i] = null;
        }
        this.elementCount = 0;
    }

    public synchronized Object clone() {
        Vector vector;
        try {
            vector = (Vector) super.clone();
            vector.elementData = Arrays.copyOf((T[]) this.elementData, this.elementCount);
            vector.modCount = 0;
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
        return vector;
    }

    public synchronized Object[] toArray() {
        return Arrays.copyOf((T[]) this.elementData, this.elementCount);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0021, code lost:
        return r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized <T> T[] toArray(T[] r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            int r0 = r4.length     // Catch:{ all -> 0x0022 }
            int r1 = r3.elementCount     // Catch:{ all -> 0x0022 }
            if (r0 >= r1) goto L_0x0012
            java.lang.Object[] r0 = r3.elementData     // Catch:{ all -> 0x0022 }
            java.lang.Class r4 = r4.getClass()     // Catch:{ all -> 0x0022 }
            java.lang.Object[] r4 = java.util.Arrays.copyOf(r0, r1, r4)     // Catch:{ all -> 0x0022 }
            monitor-exit(r3)
            return r4
        L_0x0012:
            java.lang.Object[] r0 = r3.elementData     // Catch:{ all -> 0x0022 }
            r2 = 0
            java.lang.System.arraycopy((java.lang.Object) r0, (int) r2, (java.lang.Object) r4, (int) r2, (int) r1)     // Catch:{ all -> 0x0022 }
            int r0 = r4.length     // Catch:{ all -> 0x0022 }
            int r1 = r3.elementCount     // Catch:{ all -> 0x0022 }
            if (r0 <= r1) goto L_0x0020
            r0 = 0
            r4[r1] = r0     // Catch:{ all -> 0x0022 }
        L_0x0020:
            monitor-exit(r3)
            return r4
        L_0x0022:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Vector.toArray(java.lang.Object[]):java.lang.Object[]");
    }

    /* access modifiers changed from: package-private */
    public E elementData(int i) {
        return this.elementData[i];
    }

    public synchronized E get(int i) {
        if (i < this.elementCount) {
        } else {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        return elementData(i);
    }

    public synchronized E set(int i, E e) {
        E elementData2;
        if (i < this.elementCount) {
            elementData2 = elementData(i);
            this.elementData[i] = e;
        } else {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        return elementData2;
    }

    public synchronized boolean add(E e) {
        this.modCount++;
        ensureCapacityHelper(this.elementCount + 1);
        Object[] objArr = this.elementData;
        int i = this.elementCount;
        this.elementCount = i + 1;
        objArr[i] = e;
        return true;
    }

    public boolean remove(Object obj) {
        return removeElement(obj);
    }

    public void add(int i, E e) {
        insertElementAt(e, i);
    }

    public synchronized E remove(int i) {
        E elementData2;
        this.modCount++;
        if (i < this.elementCount) {
            elementData2 = elementData(i);
            int i2 = (this.elementCount - i) - 1;
            if (i2 > 0) {
                Object[] objArr = this.elementData;
                System.arraycopy((Object) objArr, i + 1, (Object) objArr, i, i2);
            }
            Object[] objArr2 = this.elementData;
            int i3 = this.elementCount - 1;
            this.elementCount = i3;
            objArr2[i3] = null;
        } else {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        return elementData2;
    }

    public void clear() {
        removeAllElements();
    }

    public synchronized boolean containsAll(Collection<?> collection) {
        return super.containsAll(collection);
    }

    public synchronized boolean addAll(Collection<? extends E> collection) {
        boolean z;
        z = true;
        this.modCount++;
        Object[] array = collection.toArray();
        int length = array.length;
        ensureCapacityHelper(this.elementCount + length);
        System.arraycopy((Object) array, 0, (Object) this.elementData, this.elementCount, length);
        this.elementCount += length;
        if (length == 0) {
            z = false;
        }
        return z;
    }

    public synchronized boolean removeAll(Collection<?> collection) {
        return super.removeAll(collection);
    }

    public synchronized boolean retainAll(Collection<?> collection) {
        return super.retainAll(collection);
    }

    public synchronized boolean addAll(int i, Collection<? extends E> collection) {
        boolean z;
        z = true;
        this.modCount++;
        if (i < 0 || i > this.elementCount) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        Object[] array = collection.toArray();
        int length = array.length;
        ensureCapacityHelper(this.elementCount + length);
        int i2 = this.elementCount - i;
        if (i2 > 0) {
            Object[] objArr = this.elementData;
            System.arraycopy((Object) objArr, i, (Object) objArr, i + length, i2);
        }
        System.arraycopy((Object) array, 0, (Object) this.elementData, i, length);
        this.elementCount += length;
        if (length == 0) {
            z = false;
        }
        return z;
    }

    public synchronized boolean equals(Object obj) {
        return super.equals(obj);
    }

    public synchronized int hashCode() {
        return super.hashCode();
    }

    public synchronized String toString() {
        return super.toString();
    }

    public synchronized List<E> subList(int i, int i2) {
        return Collections.synchronizedList(super.subList(i, i2), this);
    }

    /* access modifiers changed from: protected */
    public synchronized void removeRange(int i, int i2) {
        this.modCount++;
        Object[] objArr = this.elementData;
        System.arraycopy((Object) objArr, i2, (Object) objArr, i, this.elementCount - i2);
        int i3 = this.elementCount - (i2 - i);
        while (true) {
            int i4 = this.elementCount;
            if (i4 != i3) {
                Object[] objArr2 = this.elementData;
                int i5 = i4 - 1;
                this.elementCount = i5;
                objArr2[i5] = null;
            }
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Object[] objArr;
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        synchronized (this) {
            putFields.put("capacityIncrement", this.capacityIncrement);
            putFields.put("elementCount", this.elementCount);
            objArr = (Object[]) this.elementData.clone();
        }
        putFields.put("elementData", (Object) objArr);
        objectOutputStream.writeFields();
    }

    public synchronized ListIterator<E> listIterator(int i) {
        if (i >= 0) {
            if (i <= this.elementCount) {
            }
        }
        throw new IndexOutOfBoundsException("Index: " + i);
        return new ListItr(i);
    }

    public synchronized ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    public synchronized Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        int cursor;
        int expectedModCount;
        int lastRet;
        protected int limit;

        private Itr() {
            this.limit = Vector.this.elementCount;
            this.lastRet = -1;
            this.expectedModCount = Vector.this.modCount;
        }

        public boolean hasNext() {
            return this.cursor < this.limit;
        }

        public E next() {
            E elementData;
            synchronized (Vector.this) {
                checkForComodification();
                int i = this.cursor;
                if (i < this.limit) {
                    this.cursor = i + 1;
                    Vector vector = Vector.this;
                    this.lastRet = i;
                    elementData = vector.elementData(i);
                } else {
                    throw new NoSuchElementException();
                }
            }
            return elementData;
        }

        public void remove() {
            if (this.lastRet != -1) {
                synchronized (Vector.this) {
                    checkForComodification();
                    Vector.this.remove(this.lastRet);
                    this.expectedModCount = Vector.this.modCount;
                    this.limit--;
                }
                this.cursor = this.lastRet;
                this.lastRet = -1;
                return;
            }
            throw new IllegalStateException();
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            synchronized (Vector.this) {
                int i = this.limit;
                int i2 = this.cursor;
                if (i2 < i) {
                    Object[] objArr = Vector.this.elementData;
                    if (i2 < objArr.length) {
                        while (i2 != i && Vector.this.modCount == this.expectedModCount) {
                            consumer.accept(objArr[i2]);
                            i2++;
                        }
                        this.cursor = i2;
                        this.lastRet = i2 - 1;
                        checkForComodification();
                        return;
                    }
                    throw new ConcurrentModificationException();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public final void checkForComodification() {
            if (Vector.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    final class ListItr extends Vector<E>.Itr implements ListIterator<E> {
        ListItr(int i) {
            super();
            this.cursor = i;
        }

        public boolean hasPrevious() {
            return this.cursor != 0;
        }

        public int nextIndex() {
            return this.cursor;
        }

        public int previousIndex() {
            return this.cursor - 1;
        }

        public E previous() {
            E elementData;
            synchronized (Vector.this) {
                checkForComodification();
                int i = this.cursor - 1;
                if (i >= 0) {
                    this.cursor = i;
                    Vector vector = Vector.this;
                    this.lastRet = i;
                    elementData = vector.elementData(i);
                } else {
                    throw new NoSuchElementException();
                }
            }
            return elementData;
        }

        public void set(E e) {
            if (this.lastRet != -1) {
                synchronized (Vector.this) {
                    checkForComodification();
                    Vector.this.set(this.lastRet, e);
                }
                return;
            }
            throw new IllegalStateException();
        }

        public void add(E e) {
            int i = this.cursor;
            synchronized (Vector.this) {
                checkForComodification();
                Vector.this.add(i, e);
                this.expectedModCount = Vector.this.modCount;
                this.limit++;
            }
            this.cursor = i + 1;
            this.lastRet = -1;
        }
    }

    public synchronized void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        int i = this.modCount;
        Object[] objArr = this.elementData;
        int i2 = this.elementCount;
        int i3 = 0;
        while (this.modCount == i && i3 < i2) {
            consumer.accept(objArr[i3]);
            i3++;
        }
        if (this.modCount != i) {
            throw new ConcurrentModificationException();
        }
    }

    public synchronized boolean removeIf(Predicate<? super E> predicate) {
        boolean z;
        Objects.requireNonNull(predicate);
        int i = this.elementCount;
        BitSet bitSet = new BitSet(i);
        int i2 = this.modCount;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (this.modCount == i2 && i4 < i) {
            if (predicate.test(this.elementData[i4])) {
                bitSet.set(i4);
                i5++;
            }
            i4++;
        }
        if (this.modCount == i2) {
            z = i5 > 0;
            if (z) {
                int i6 = i - i5;
                int i7 = 0;
                while (i3 < i && i7 < i6) {
                    int nextClearBit = bitSet.nextClearBit(i3);
                    Object[] objArr = this.elementData;
                    objArr[i7] = objArr[nextClearBit];
                    i3 = nextClearBit + 1;
                    i7++;
                }
                for (int i8 = i6; i8 < i; i8++) {
                    this.elementData[i8] = null;
                }
                this.elementCount = i6;
                if (this.modCount == i2) {
                    this.modCount++;
                } else {
                    throw new ConcurrentModificationException();
                }
            }
        } else {
            throw new ConcurrentModificationException();
        }
        return z;
    }

    public synchronized void replaceAll(UnaryOperator<E> unaryOperator) {
        Objects.requireNonNull(unaryOperator);
        int i = this.modCount;
        int i2 = this.elementCount;
        int i3 = 0;
        while (this.modCount == i && i3 < i2) {
            Object[] objArr = this.elementData;
            objArr[i3] = unaryOperator.apply(objArr[i3]);
            i3++;
        }
        if (this.modCount == i) {
            this.modCount++;
        } else {
            throw new ConcurrentModificationException();
        }
    }

    public synchronized void sort(Comparator<? super E> comparator) {
        int i = this.modCount;
        Arrays.sort(this.elementData, 0, this.elementCount, comparator);
        if (this.modCount == i) {
            this.modCount++;
        } else {
            throw new ConcurrentModificationException();
        }
    }

    public Spliterator<E> spliterator() {
        return new VectorSpliterator(this, (Object[]) null, 0, -1, 0);
    }

    static final class VectorSpliterator<E> implements Spliterator<E> {
        private Object[] array;
        private int expectedModCount;
        private int fence;
        private int index;
        private final Vector<E> list;

        public int characteristics() {
            return 16464;
        }

        VectorSpliterator(Vector<E> vector, Object[] objArr, int i, int i2, int i3) {
            this.list = vector;
            this.array = objArr;
            this.index = i;
            this.fence = i2;
            this.expectedModCount = i3;
        }

        private int getFence() {
            int i = this.fence;
            if (i < 0) {
                synchronized (this.list) {
                    this.array = this.list.elementData;
                    this.expectedModCount = this.list.modCount;
                    i = this.list.elementCount;
                    this.fence = i;
                }
            }
            return i;
        }

        public Spliterator<E> trySplit() {
            int fence2 = getFence();
            int i = this.index;
            int i2 = (fence2 + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            Vector<E> vector = this.list;
            Object[] objArr = this.array;
            this.index = i2;
            return new VectorSpliterator(vector, objArr, i, i2, this.expectedModCount);
        }

        public boolean tryAdvance(Consumer<? super E> consumer) {
            consumer.getClass();
            int fence2 = getFence();
            int i = this.index;
            if (fence2 <= i) {
                return false;
            }
            this.index = i + 1;
            consumer.accept(this.array[i]);
            if (this.list.modCount == this.expectedModCount) {
                return true;
            }
            throw new ConcurrentModificationException();
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            int i;
            Object[] objArr;
            int i2;
            consumer.getClass();
            Vector<E> vector = this.list;
            if (vector != null) {
                int i3 = this.fence;
                if (i3 < 0) {
                    synchronized (vector) {
                        this.expectedModCount = vector.modCount;
                        objArr = vector.elementData;
                        this.array = objArr;
                        i = vector.elementCount;
                        this.fence = i;
                    }
                } else {
                    i = i3;
                    objArr = this.array;
                }
                if (objArr != null && (i2 = this.index) >= 0) {
                    this.index = i;
                    if (i <= objArr.length) {
                        for (i2 = this.index; i2 < i; i2++) {
                            consumer.accept(objArr[i2]);
                        }
                        if (vector.modCount == this.expectedModCount) {
                            return;
                        }
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        public long estimateSize() {
            return (long) (getFence() - this.index);
        }
    }
}
