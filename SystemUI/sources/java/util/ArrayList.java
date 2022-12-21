package java.util;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable {
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = new Object[0];
    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENTDATA = new Object[0];
    private static final int MAX_ARRAY_SIZE = 2147483639;
    private static final long serialVersionUID = 8683452581122892189L;
    transient Object[] elementData;
    /* access modifiers changed from: private */
    public int size;

    public ArrayList(int i) {
        if (i > 0) {
            this.elementData = new Object[i];
        } else if (i == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + i);
        }
    }

    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    public ArrayList(Collection<? extends E> collection) {
        Object[] array = collection.toArray();
        this.elementData = array;
        int length = array.length;
        this.size = length;
        if (length == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else if (array.getClass() != Object[].class) {
            this.elementData = Arrays.copyOf(this.elementData, this.size, Object[].class);
        }
    }

    public void trimToSize() {
        Object[] objArr;
        this.modCount++;
        int i = this.size;
        Object[] objArr2 = this.elementData;
        if (i < objArr2.length) {
            if (i == 0) {
                objArr = EMPTY_ELEMENTDATA;
            } else {
                objArr = Arrays.copyOf((T[]) objArr2, i);
            }
            this.elementData = objArr;
        }
    }

    public void ensureCapacity(int i) {
        if (i > (this.elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA ? 0 : 10)) {
            ensureExplicitCapacity(i);
        }
    }

    private void ensureCapacityInternal(int i) {
        if (this.elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            i = Math.max(10, i);
        }
        ensureExplicitCapacity(i);
    }

    private void ensureExplicitCapacity(int i) {
        this.modCount++;
        if (i - this.elementData.length > 0) {
            grow(i);
        }
    }

    private void grow(int i) {
        int length = this.elementData.length;
        int i2 = length + (length >> 1);
        if (i2 - i < 0) {
            i2 = i;
        }
        if (i2 - MAX_ARRAY_SIZE > 0) {
            i2 = hugeCapacity(i);
        }
        this.elementData = Arrays.copyOf((T[]) this.elementData, i2);
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

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    public int indexOf(Object obj) {
        int i = 0;
        if (obj == null) {
            while (i < this.size) {
                if (this.elementData[i] == null) {
                    return i;
                }
                i++;
            }
            return -1;
        }
        while (i < this.size) {
            if (obj.equals(this.elementData[i])) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int lastIndexOf(Object obj) {
        if (obj == null) {
            for (int i = this.size - 1; i >= 0; i--) {
                if (this.elementData[i] == null) {
                    return i;
                }
            }
            return -1;
        }
        for (int i2 = this.size - 1; i2 >= 0; i2--) {
            if (obj.equals(this.elementData[i2])) {
                return i2;
            }
        }
        return -1;
    }

    public Object clone() {
        try {
            ArrayList arrayList = (ArrayList) super.clone();
            arrayList.elementData = Arrays.copyOf((T[]) this.elementData, this.size);
            arrayList.modCount = 0;
            return arrayList;
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    public Object[] toArray() {
        return Arrays.copyOf((T[]) this.elementData, this.size);
    }

    public <T> T[] toArray(T[] tArr) {
        int length = tArr.length;
        int i = this.size;
        if (length < i) {
            return Arrays.copyOf(this.elementData, i, tArr.getClass());
        }
        System.arraycopy((Object) this.elementData, 0, (Object) tArr, 0, i);
        int length2 = tArr.length;
        int i2 = this.size;
        if (length2 > i2) {
            tArr[i2] = null;
        }
        return tArr;
    }

    public E get(int i) {
        if (i < this.size) {
            return this.elementData[i];
        }
        throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
    }

    public E set(int i, E e) {
        if (i < this.size) {
            E[] eArr = this.elementData;
            E e2 = eArr[i];
            eArr[i] = e;
            return e2;
        }
        throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
    }

    public boolean add(E e) {
        ensureCapacityInternal(this.size + 1);
        Object[] objArr = this.elementData;
        int i = this.size;
        this.size = i + 1;
        objArr[i] = e;
        return true;
    }

    public void add(int i, E e) {
        int i2 = this.size;
        if (i > i2 || i < 0) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
        }
        ensureCapacityInternal(i2 + 1);
        Object[] objArr = this.elementData;
        System.arraycopy((Object) objArr, i, (Object) objArr, i + 1, this.size - i);
        this.elementData[i] = e;
        this.size++;
    }

    public E remove(int i) {
        if (i < this.size) {
            this.modCount++;
            E[] eArr = this.elementData;
            E e = eArr[i];
            int i2 = (this.size - i) - 1;
            if (i2 > 0) {
                System.arraycopy((Object) eArr, i + 1, (Object) eArr, i, i2);
            }
            Object[] objArr = this.elementData;
            int i3 = this.size - 1;
            this.size = i3;
            objArr[i3] = null;
            return e;
        }
        throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
    }

    public boolean remove(Object obj) {
        if (obj == null) {
            for (int i = 0; i < this.size; i++) {
                if (this.elementData[i] == null) {
                    fastRemove(i);
                    return true;
                }
            }
        } else {
            for (int i2 = 0; i2 < this.size; i2++) {
                if (obj.equals(this.elementData[i2])) {
                    fastRemove(i2);
                    return true;
                }
            }
        }
        return false;
    }

    private void fastRemove(int i) {
        this.modCount++;
        int i2 = (this.size - i) - 1;
        if (i2 > 0) {
            Object[] objArr = this.elementData;
            System.arraycopy((Object) objArr, i + 1, (Object) objArr, i, i2);
        }
        Object[] objArr2 = this.elementData;
        int i3 = this.size - 1;
        this.size = i3;
        objArr2[i3] = null;
    }

    public void clear() {
        this.modCount++;
        for (int i = 0; i < this.size; i++) {
            this.elementData[i] = null;
        }
        this.size = 0;
    }

    public boolean addAll(Collection<? extends E> collection) {
        Object[] array = collection.toArray();
        int length = array.length;
        ensureCapacityInternal(this.size + length);
        System.arraycopy((Object) array, 0, (Object) this.elementData, this.size, length);
        this.size += length;
        if (length != 0) {
            return true;
        }
        return false;
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        if (i > this.size || i < 0) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
        }
        Object[] array = collection.toArray();
        int length = array.length;
        ensureCapacityInternal(this.size + length);
        int i2 = this.size - i;
        if (i2 > 0) {
            Object[] objArr = this.elementData;
            System.arraycopy((Object) objArr, i, (Object) objArr, i + length, i2);
        }
        System.arraycopy((Object) array, 0, (Object) this.elementData, i, length);
        this.size += length;
        if (length != 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void removeRange(int i, int i2) {
        if (i2 >= i) {
            this.modCount++;
            Object[] objArr = this.elementData;
            System.arraycopy((Object) objArr, i2, (Object) objArr, i, this.size - i2);
            int i3 = this.size - (i2 - i);
            for (int i4 = i3; i4 < this.size; i4++) {
                this.elementData[i4] = null;
            }
            this.size = i3;
            return;
        }
        throw new IndexOutOfBoundsException("toIndex < fromIndex");
    }

    private String outOfBoundsMsg(int i) {
        return "Index: " + i + ", Size: " + this.size;
    }

    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return batchRemove(collection, false);
    }

    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return batchRemove(collection, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0055  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean batchRemove(java.util.Collection<?> r8, boolean r9) {
        /*
            r7 = this;
            java.lang.Object[] r0 = r7.elementData
            r1 = 0
            r2 = r1
            r3 = r2
        L_0x0005:
            r4 = 0
            int r5 = r7.size     // Catch:{ all -> 0x0044 }
            if (r2 >= r5) goto L_0x0020
            r5 = r0[r2]     // Catch:{ all -> 0x0044 }
            boolean r5 = r8.contains(r5)     // Catch:{ all -> 0x0044 }
            if (r5 != r9) goto L_0x001d
            int r5 = r3 + 1
            r6 = r0[r2]     // Catch:{ all -> 0x001a }
            r0[r3] = r6     // Catch:{ all -> 0x001a }
            r3 = r5
            goto L_0x001d
        L_0x001a:
            r8 = move-exception
            r3 = r5
            goto L_0x0045
        L_0x001d:
            int r2 = r2 + 1
            goto L_0x0005
        L_0x0020:
            if (r2 == r5) goto L_0x002a
            int r5 = r5 - r2
            java.lang.System.arraycopy((java.lang.Object) r0, (int) r2, (java.lang.Object) r0, (int) r3, (int) r5)
            int r8 = r7.size
            int r8 = r8 - r2
            int r3 = r3 + r8
        L_0x002a:
            int r8 = r7.size
            if (r3 == r8) goto L_0x0043
            r8 = r3
        L_0x002f:
            int r9 = r7.size
            if (r8 >= r9) goto L_0x0038
            r0[r8] = r4
            int r8 = r8 + 1
            goto L_0x002f
        L_0x0038:
            int r8 = r7.modCount
            int r9 = r7.size
            int r9 = r9 - r3
            int r8 = r8 + r9
            r7.modCount = r8
            r7.size = r3
            r1 = 1
        L_0x0043:
            return r1
        L_0x0044:
            r8 = move-exception
        L_0x0045:
            int r9 = r7.size
            if (r2 == r9) goto L_0x0051
            int r9 = r9 - r2
            java.lang.System.arraycopy((java.lang.Object) r0, (int) r2, (java.lang.Object) r0, (int) r3, (int) r9)
            int r9 = r7.size
            int r9 = r9 - r2
            int r3 = r3 + r9
        L_0x0051:
            int r9 = r7.size
            if (r3 == r9) goto L_0x0069
            r9 = r3
        L_0x0056:
            int r1 = r7.size
            if (r9 >= r1) goto L_0x005f
            r0[r9] = r4
            int r9 = r9 + 1
            goto L_0x0056
        L_0x005f:
            int r9 = r7.modCount
            int r0 = r7.size
            int r0 = r0 - r3
            int r9 = r9 + r0
            r7.modCount = r9
            r7.size = r3
        L_0x0069:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.ArrayList.batchRemove(java.util.Collection, boolean):boolean");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int i = this.modCount;
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.size);
        for (int i2 = 0; i2 < this.size; i2++) {
            objectOutputStream.writeObject(this.elementData[i2]);
        }
        if (this.modCount != i) {
            throw new ConcurrentModificationException();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.elementData = EMPTY_ELEMENTDATA;
        objectInputStream.defaultReadObject();
        objectInputStream.readInt();
        int i = this.size;
        if (i > 0) {
            ensureCapacityInternal(i);
            Object[] objArr = this.elementData;
            for (int i2 = 0; i2 < this.size; i2++) {
                objArr[i2] = objectInputStream.readObject();
            }
        }
    }

    public ListIterator<E> listIterator(int i) {
        if (i >= 0 && i <= this.size) {
            return new ListItr(i);
        }
        throw new IndexOutOfBoundsException("Index: " + i);
    }

    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        int cursor;
        int expectedModCount;
        int lastRet;
        protected int limit;

        private Itr() {
            this.limit = ArrayList.this.size;
            this.lastRet = -1;
            this.expectedModCount = ArrayList.this.modCount;
        }

        public boolean hasNext() {
            return this.cursor < this.limit;
        }

        public E next() {
            if (ArrayList.this.modCount == this.expectedModCount) {
                int i = this.cursor;
                if (i < this.limit) {
                    E[] eArr = ArrayList.this.elementData;
                    if (i < eArr.length) {
                        this.cursor = i + 1;
                        this.lastRet = i;
                        return eArr[i];
                    }
                    throw new ConcurrentModificationException();
                }
                throw new NoSuchElementException();
            }
            throw new ConcurrentModificationException();
        }

        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            } else if (ArrayList.this.modCount == this.expectedModCount) {
                try {
                    ArrayList.this.remove(this.lastRet);
                    this.cursor = this.lastRet;
                    this.lastRet = -1;
                    this.expectedModCount = ArrayList.this.modCount;
                    this.limit--;
                } catch (IndexOutOfBoundsException unused) {
                    throw new ConcurrentModificationException();
                }
            } else {
                throw new ConcurrentModificationException();
            }
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            int r0 = ArrayList.this.size;
            int i = this.cursor;
            if (i < r0) {
                Object[] objArr = ArrayList.this.elementData;
                if (i < objArr.length) {
                    while (i != r0 && ArrayList.this.modCount == this.expectedModCount) {
                        consumer.accept(objArr[i]);
                        i++;
                    }
                    this.cursor = i;
                    this.lastRet = i - 1;
                    if (ArrayList.this.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                    return;
                }
                throw new ConcurrentModificationException();
            }
        }
    }

    private class ListItr extends ArrayList<E>.Itr implements ListIterator<E> {
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
            if (ArrayList.this.modCount == this.expectedModCount) {
                int i = this.cursor - 1;
                if (i >= 0) {
                    E[] eArr = ArrayList.this.elementData;
                    if (i < eArr.length) {
                        this.cursor = i;
                        this.lastRet = i;
                        return eArr[i];
                    }
                    throw new ConcurrentModificationException();
                }
                throw new NoSuchElementException();
            }
            throw new ConcurrentModificationException();
        }

        public void set(E e) {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            } else if (ArrayList.this.modCount == this.expectedModCount) {
                try {
                    ArrayList.this.set(this.lastRet, e);
                } catch (IndexOutOfBoundsException unused) {
                    throw new ConcurrentModificationException();
                }
            } else {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            if (ArrayList.this.modCount == this.expectedModCount) {
                try {
                    int i = this.cursor;
                    ArrayList.this.add(i, e);
                    this.cursor = i + 1;
                    this.lastRet = -1;
                    this.expectedModCount = ArrayList.this.modCount;
                    this.limit++;
                } catch (IndexOutOfBoundsException unused) {
                    throw new ConcurrentModificationException();
                }
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    public List<E> subList(int i, int i2) {
        subListRangeCheck(i, i2, this.size);
        return new SubList(this, 0, i, i2);
    }

    static void subListRangeCheck(int i, int i2, int i3) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("fromIndex = " + i);
        } else if (i2 > i3) {
            throw new IndexOutOfBoundsException("toIndex = " + i2);
        } else if (i > i2) {
            throw new IllegalArgumentException("fromIndex(" + i + ") > toIndex(" + i2 + NavigationBarInflaterView.KEY_CODE_END);
        }
    }

    private class SubList extends AbstractList<E> implements RandomAccess {
        private final int offset;
        private final AbstractList<E> parent;
        private final int parentOffset;
        int size;

        SubList(AbstractList<E> abstractList, int i, int i2, int i3) {
            this.parent = abstractList;
            this.parentOffset = i2;
            this.offset = i + i2;
            this.size = i3 - i2;
            this.modCount = ArrayList.this.modCount;
        }

        public E set(int i, E e) {
            if (i < 0 || i >= this.size) {
                throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
            } else if (ArrayList.this.modCount == this.modCount) {
                E e2 = ArrayList.this.elementData[this.offset + i];
                ArrayList.this.elementData[this.offset + i] = e;
                return e2;
            } else {
                throw new ConcurrentModificationException();
            }
        }

        public E get(int i) {
            if (i < 0 || i >= this.size) {
                throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
            } else if (ArrayList.this.modCount == this.modCount) {
                return ArrayList.this.elementData[this.offset + i];
            } else {
                throw new ConcurrentModificationException();
            }
        }

        public int size() {
            if (ArrayList.this.modCount == this.modCount) {
                return this.size;
            }
            throw new ConcurrentModificationException();
        }

        public void add(int i, E e) {
            if (i < 0 || i > this.size) {
                throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
            } else if (ArrayList.this.modCount == this.modCount) {
                this.parent.add(this.parentOffset + i, e);
                this.modCount = this.parent.modCount;
                this.size++;
            } else {
                throw new ConcurrentModificationException();
            }
        }

        public E remove(int i) {
            if (i < 0 || i >= this.size) {
                throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
            } else if (ArrayList.this.modCount == this.modCount) {
                E remove = this.parent.remove(this.parentOffset + i);
                this.modCount = this.parent.modCount;
                this.size--;
                return remove;
            } else {
                throw new ConcurrentModificationException();
            }
        }

        /* access modifiers changed from: protected */
        public void removeRange(int i, int i2) {
            if (ArrayList.this.modCount == this.modCount) {
                AbstractList<E> abstractList = this.parent;
                int i3 = this.parentOffset;
                abstractList.removeRange(i3 + i, i3 + i2);
                this.modCount = this.parent.modCount;
                this.size -= i2 - i;
                return;
            }
            throw new ConcurrentModificationException();
        }

        public boolean addAll(Collection<? extends E> collection) {
            return addAll(this.size, collection);
        }

        public boolean addAll(int i, Collection<? extends E> collection) {
            if (i < 0 || i > this.size) {
                throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
            }
            int size2 = collection.size();
            if (size2 == 0) {
                return false;
            }
            if (ArrayList.this.modCount == this.modCount) {
                this.parent.addAll(this.parentOffset + i, collection);
                this.modCount = this.parent.modCount;
                this.size += size2;
                return true;
            }
            throw new ConcurrentModificationException();
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        public ListIterator<E> listIterator(int i) {
            if (ArrayList.this.modCount != this.modCount) {
                throw new ConcurrentModificationException();
            } else if (i >= 0 && i <= this.size) {
                return new ListIterator<E>(i, this.offset) {
                    int cursor;
                    int expectedModCount;
                    int lastRet = -1;
                    final /* synthetic */ int val$index;
                    final /* synthetic */ int val$offset;

                    {
                        this.val$index = r2;
                        this.val$offset = r3;
                        this.cursor = r2;
                        this.expectedModCount = ArrayList.this.modCount;
                    }

                    public boolean hasNext() {
                        return this.cursor != SubList.this.size;
                    }

                    public E next() {
                        if (this.expectedModCount == ArrayList.this.modCount) {
                            int i = this.cursor;
                            if (i < SubList.this.size) {
                                E[] eArr = ArrayList.this.elementData;
                                int i2 = this.val$offset;
                                if (i2 + i < eArr.length) {
                                    this.cursor = i + 1;
                                    this.lastRet = i;
                                    return eArr[i2 + i];
                                }
                                throw new ConcurrentModificationException();
                            }
                            throw new NoSuchElementException();
                        }
                        throw new ConcurrentModificationException();
                    }

                    public boolean hasPrevious() {
                        return this.cursor != 0;
                    }

                    public E previous() {
                        if (this.expectedModCount == ArrayList.this.modCount) {
                            int i = this.cursor - 1;
                            if (i >= 0) {
                                E[] eArr = ArrayList.this.elementData;
                                int i2 = this.val$offset;
                                if (i2 + i < eArr.length) {
                                    this.cursor = i;
                                    this.lastRet = i;
                                    return eArr[i2 + i];
                                }
                                throw new ConcurrentModificationException();
                            }
                            throw new NoSuchElementException();
                        }
                        throw new ConcurrentModificationException();
                    }

                    public void forEachRemaining(Consumer<? super E> consumer) {
                        Objects.requireNonNull(consumer);
                        int i = SubList.this.size;
                        int i2 = this.cursor;
                        if (i2 < i) {
                            Object[] objArr = ArrayList.this.elementData;
                            if (this.val$offset + i2 < objArr.length) {
                                while (i2 != i && SubList.this.modCount == this.expectedModCount) {
                                    consumer.accept(objArr[this.val$offset + i2]);
                                    i2++;
                                }
                                this.cursor = i2;
                                this.lastRet = i2;
                                if (this.expectedModCount != ArrayList.this.modCount) {
                                    throw new ConcurrentModificationException();
                                }
                                return;
                            }
                            throw new ConcurrentModificationException();
                        }
                    }

                    public int nextIndex() {
                        return this.cursor;
                    }

                    public int previousIndex() {
                        return this.cursor - 1;
                    }

                    public void remove() {
                        if (this.lastRet < 0) {
                            throw new IllegalStateException();
                        } else if (this.expectedModCount == ArrayList.this.modCount) {
                            try {
                                SubList.this.remove(this.lastRet);
                                this.cursor = this.lastRet;
                                this.lastRet = -1;
                                this.expectedModCount = ArrayList.this.modCount;
                            } catch (IndexOutOfBoundsException unused) {
                                throw new ConcurrentModificationException();
                            }
                        } else {
                            throw new ConcurrentModificationException();
                        }
                    }

                    public void set(E e) {
                        if (this.lastRet < 0) {
                            throw new IllegalStateException();
                        } else if (this.expectedModCount == ArrayList.this.modCount) {
                            try {
                                ArrayList.this.set(this.val$offset + this.lastRet, e);
                            } catch (IndexOutOfBoundsException unused) {
                                throw new ConcurrentModificationException();
                            }
                        } else {
                            throw new ConcurrentModificationException();
                        }
                    }

                    public void add(E e) {
                        if (this.expectedModCount == ArrayList.this.modCount) {
                            try {
                                int i = this.cursor;
                                SubList.this.add(i, e);
                                this.cursor = i + 1;
                                this.lastRet = -1;
                                this.expectedModCount = ArrayList.this.modCount;
                            } catch (IndexOutOfBoundsException unused) {
                                throw new ConcurrentModificationException();
                            }
                        } else {
                            throw new ConcurrentModificationException();
                        }
                    }
                };
            } else {
                throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
            }
        }

        public List<E> subList(int i, int i2) {
            subListRangeCheck(i, i2, this.size);
            return new SubList(this, this.offset, i, i2);
        }

        private String outOfBoundsMsg(int i) {
            return "Index: " + i + ", Size: " + this.size;
        }

        public Spliterator<E> spliterator() {
            if (this.modCount == ArrayList.this.modCount) {
                ArrayList arrayList = ArrayList.this;
                int i = this.offset;
                return new ArrayListSpliterator(arrayList, i, this.size + i, this.modCount);
            }
            throw new ConcurrentModificationException();
        }
    }

    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        int i = this.modCount;
        Object[] objArr = this.elementData;
        int i2 = this.size;
        int i3 = 0;
        while (this.modCount == i && i3 < i2) {
            consumer.accept(objArr[i3]);
            i3++;
        }
        if (this.modCount != i) {
            throw new ConcurrentModificationException();
        }
    }

    public Spliterator<E> spliterator() {
        return new ArrayListSpliterator(this, 0, -1, 0);
    }

    static final class ArrayListSpliterator<E> implements Spliterator<E> {
        private int expectedModCount;
        private int fence;
        private int index;
        private final ArrayList<E> list;

        public int characteristics() {
            return 16464;
        }

        ArrayListSpliterator(ArrayList<E> arrayList, int i, int i2, int i3) {
            this.list = arrayList;
            this.index = i;
            this.fence = i2;
            this.expectedModCount = i3;
        }

        private int getFence() {
            int i = this.fence;
            if (i >= 0) {
                return i;
            }
            ArrayList<E> arrayList = this.list;
            if (arrayList == null) {
                this.fence = 0;
                return 0;
            }
            this.expectedModCount = arrayList.modCount;
            int r0 = arrayList.size;
            this.fence = r0;
            return r0;
        }

        public ArrayListSpliterator<E> trySplit() {
            int fence2 = getFence();
            int i = this.index;
            int i2 = (fence2 + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            ArrayList<E> arrayList = this.list;
            this.index = i2;
            return new ArrayListSpliterator<>(arrayList, i, i2, this.expectedModCount);
        }

        public boolean tryAdvance(Consumer<? super E> consumer) {
            consumer.getClass();
            int fence2 = getFence();
            int i = this.index;
            if (i >= fence2) {
                return false;
            }
            this.index = i + 1;
            consumer.accept(this.list.elementData[i]);
            if (this.list.modCount == this.expectedModCount) {
                return true;
            }
            throw new ConcurrentModificationException();
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Object[] objArr;
            int i;
            consumer.getClass();
            ArrayList<E> arrayList = this.list;
            if (!(arrayList == null || (objArr = arrayList.elementData) == null)) {
                int i2 = this.fence;
                if (i2 < 0) {
                    int i3 = arrayList.modCount;
                    i = i3;
                    i2 = arrayList.size;
                } else {
                    i = this.expectedModCount;
                }
                int i4 = this.index;
                if (i4 >= 0) {
                    this.index = i2;
                    if (i2 <= objArr.length) {
                        while (i4 < i2) {
                            consumer.accept(objArr[i4]);
                            i4++;
                        }
                        if (arrayList.modCount == i) {
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

    public boolean removeIf(Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        BitSet bitSet = new BitSet(this.size);
        int i = this.modCount;
        int i2 = this.size;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (this.modCount == i && i4 < i2) {
            if (predicate.test(this.elementData[i4])) {
                bitSet.set(i4);
                i5++;
            }
            i4++;
        }
        if (this.modCount == i) {
            boolean z = i5 > 0;
            if (z) {
                int i6 = i2 - i5;
                int i7 = 0;
                while (i3 < i2 && i7 < i6) {
                    int nextClearBit = bitSet.nextClearBit(i3);
                    Object[] objArr = this.elementData;
                    objArr[i7] = objArr[nextClearBit];
                    i3 = nextClearBit + 1;
                    i7++;
                }
                for (int i8 = i6; i8 < i2; i8++) {
                    this.elementData[i8] = null;
                }
                this.size = i6;
                if (this.modCount == i) {
                    this.modCount++;
                } else {
                    throw new ConcurrentModificationException();
                }
            }
            return z;
        }
        throw new ConcurrentModificationException();
    }

    public void replaceAll(UnaryOperator<E> unaryOperator) {
        Objects.requireNonNull(unaryOperator);
        int i = this.modCount;
        int i2 = this.size;
        int i3 = 0;
        while (this.modCount == i && i3 < i2) {
            Object[] objArr = this.elementData;
            objArr[i3] = unaryOperator.apply(objArr[i3]);
            i3++;
        }
        if (this.modCount == i) {
            this.modCount++;
            return;
        }
        throw new ConcurrentModificationException();
    }

    public void sort(Comparator<? super E> comparator) {
        int i = this.modCount;
        Arrays.sort(this.elementData, 0, this.size, comparator);
        if (this.modCount == i) {
            this.modCount++;
            return;
        }
        throw new ConcurrentModificationException();
    }
}
