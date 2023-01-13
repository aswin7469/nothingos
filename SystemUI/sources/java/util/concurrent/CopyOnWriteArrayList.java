package java.util.concurrent;

import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import jdk.internal.misc.SharedSecrets;

public class CopyOnWriteArrayList<E> implements List<E>, RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = 8673264195747942595L;
    private volatile transient Object[] array;
    final transient Object lock = new Object();

    /* access modifiers changed from: package-private */
    public final Object[] getArray() {
        return this.array;
    }

    /* access modifiers changed from: package-private */
    public final void setArray(Object[] objArr) {
        this.array = objArr;
    }

    public CopyOnWriteArrayList() {
        setArray(new Object[0]);
    }

    public CopyOnWriteArrayList(Collection<? extends E> collection) {
        Object[] objArr;
        if (collection.getClass() == CopyOnWriteArrayList.class) {
            objArr = ((CopyOnWriteArrayList) collection).getArray();
        } else {
            objArr = collection.toArray();
            if (objArr.getClass() != Object[].class) {
                objArr = Arrays.copyOf(objArr, objArr.length, Object[].class);
            }
        }
        setArray(objArr);
    }

    public CopyOnWriteArrayList(E[] eArr) {
        setArray(Arrays.copyOf(eArr, eArr.length, Object[].class));
    }

    public int size() {
        return getArray().length;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /* access modifiers changed from: private */
    public static int indexOfRange(Object obj, Object[] objArr, int i, int i2) {
        if (obj == null) {
            while (i < i2) {
                if (objArr[i] == null) {
                    return i;
                }
                i++;
            }
            return -1;
        }
        while (i < i2) {
            if (obj.equals(objArr[i])) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /* access modifiers changed from: private */
    public static int lastIndexOfRange(Object obj, Object[] objArr, int i, int i2) {
        if (obj == null) {
            for (int i3 = i2 - 1; i3 >= i; i3--) {
                if (objArr[i3] == null) {
                    return i3;
                }
            }
            return -1;
        }
        for (int i4 = i2 - 1; i4 >= i; i4--) {
            if (obj.equals(objArr[i4])) {
                return i4;
            }
        }
        return -1;
    }

    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    public int indexOf(Object obj) {
        Object[] array2 = getArray();
        return indexOfRange(obj, array2, 0, array2.length);
    }

    public int indexOf(E e, int i) {
        Object[] array2 = getArray();
        return indexOfRange(e, array2, i, array2.length);
    }

    public int lastIndexOf(Object obj) {
        Object[] array2 = getArray();
        return lastIndexOfRange(obj, array2, 0, array2.length);
    }

    public int lastIndexOf(E e, int i) {
        return lastIndexOfRange(e, getArray(), 0, i + 1);
    }

    public Object clone() {
        try {
            CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) super.clone();
            copyOnWriteArrayList.resetLock();
            VarHandle.releaseFence();
            return copyOnWriteArrayList;
        } catch (CloneNotSupportedException unused) {
            throw new InternalError();
        }
    }

    public Object[] toArray() {
        return (Object[]) getArray().clone();
    }

    public <T> T[] toArray(T[] tArr) {
        Object[] array2 = getArray();
        int length = array2.length;
        if (tArr.length < length) {
            return Arrays.copyOf(array2, length, tArr.getClass());
        }
        System.arraycopy((Object) array2, 0, (Object) tArr, 0, length);
        if (tArr.length > length) {
            tArr[length] = null;
        }
        return tArr;
    }

    static <E> E elementAt(Object[] objArr, int i) {
        return objArr[i];
    }

    static String outOfBounds(int i, int i2) {
        return "Index: " + i + ", Size: " + i2;
    }

    public E get(int i) {
        return elementAt(getArray(), i);
    }

    public E set(int i, E e) {
        E elementAt;
        synchronized (this.lock) {
            Object[] array2 = getArray();
            elementAt = elementAt(array2, i);
            if (elementAt != e) {
                array2 = (Object[]) array2.clone();
                array2[i] = e;
            }
            setArray(array2);
        }
        return elementAt;
    }

    public boolean add(E e) {
        synchronized (this.lock) {
            Object[] array2 = getArray();
            int length = array2.length;
            Object[] copyOf = Arrays.copyOf((T[]) array2, length + 1);
            copyOf[length] = e;
            setArray(copyOf);
        }
        return true;
    }

    public void add(int i, E e) {
        Object[] objArr;
        synchronized (this.lock) {
            Object[] array2 = getArray();
            int length = array2.length;
            if (i > length || i < 0) {
                throw new IndexOutOfBoundsException(outOfBounds(i, length));
            }
            int i2 = length - i;
            if (i2 == 0) {
                objArr = Arrays.copyOf((T[]) array2, length + 1);
            } else {
                Object[] objArr2 = new Object[(length + 1)];
                System.arraycopy((Object) array2, 0, (Object) objArr2, 0, i);
                System.arraycopy((Object) array2, i, (Object) objArr2, i + 1, i2);
                objArr = objArr2;
            }
            objArr[i] = e;
            setArray(objArr);
        }
    }

    public E remove(int i) {
        E elementAt;
        Object[] objArr;
        synchronized (this.lock) {
            Object[] array2 = getArray();
            int length = array2.length;
            elementAt = elementAt(array2, i);
            int i2 = (length - i) - 1;
            if (i2 == 0) {
                objArr = Arrays.copyOf((T[]) array2, length - 1);
            } else {
                Object[] objArr2 = new Object[(length - 1)];
                System.arraycopy((Object) array2, 0, (Object) objArr2, 0, i);
                System.arraycopy((Object) array2, i + 1, (Object) objArr2, i, i2);
                objArr = objArr2;
            }
            setArray(objArr);
        }
        return elementAt;
    }

    public boolean remove(Object obj) {
        Object[] array2 = getArray();
        int indexOfRange = indexOfRange(obj, array2, 0, array2.length);
        if (indexOfRange < 0 || !remove(obj, array2, indexOfRange)) {
            return false;
        }
        return true;
    }

    private boolean remove(Object obj, Object[] objArr, int i) {
        synchronized (this.lock) {
            Object[] array2 = getArray();
            int length = array2.length;
            if (objArr != array2) {
                int min = Math.min(i, length);
                int i2 = 0;
                while (true) {
                    if (i2 < min) {
                        Object obj2 = array2[i2];
                        if (obj2 != objArr[i2] && Objects.equals(obj, obj2)) {
                            i = i2;
                            break;
                        }
                        i2++;
                    } else if (i >= length) {
                        return false;
                    } else {
                        if (array2[i] != obj) {
                            i = indexOfRange(obj, array2, i, length);
                            if (i < 0) {
                                return false;
                            }
                        }
                    }
                }
            }
            Object[] objArr2 = new Object[(length - 1)];
            System.arraycopy((Object) array2, 0, (Object) objArr2, 0, i);
            System.arraycopy((Object) array2, i + 1, (Object) objArr2, i, (length - i) - 1);
            setArray(objArr2);
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public void removeRange(int i, int i2) {
        synchronized (this.lock) {
            Object[] array2 = getArray();
            int length = array2.length;
            if (i < 0 || i2 > length || i2 < i) {
                throw new IndexOutOfBoundsException();
            }
            int i3 = length - (i2 - i);
            int i4 = length - i2;
            if (i4 == 0) {
                setArray(Arrays.copyOf((T[]) array2, i3));
            } else {
                Object[] objArr = new Object[i3];
                System.arraycopy((Object) array2, 0, (Object) objArr, 0, i);
                System.arraycopy((Object) array2, i2, (Object) objArr, i, i4);
                setArray(objArr);
            }
        }
    }

    public boolean addIfAbsent(E e) {
        Object[] array2 = getArray();
        if (indexOfRange(e, array2, 0, array2.length) >= 0 || !addIfAbsent(e, array2)) {
            return false;
        }
        return true;
    }

    private boolean addIfAbsent(E e, Object[] objArr) {
        synchronized (this.lock) {
            Object[] array2 = getArray();
            int length = array2.length;
            if (objArr != array2) {
                int min = Math.min(objArr.length, length);
                for (int i = 0; i < min; i++) {
                    Object obj = array2[i];
                    if (obj != objArr[i] && Objects.equals(e, obj)) {
                        return false;
                    }
                }
                if (indexOfRange(e, array2, min, length) >= 0) {
                    return false;
                }
            }
            Object[] copyOf = Arrays.copyOf((T[]) array2, length + 1);
            copyOf[length] = e;
            setArray(copyOf);
            return true;
        }
    }

    public boolean containsAll(Collection<?> collection) {
        Object[] array2 = getArray();
        int length = array2.length;
        for (Object indexOfRange : collection) {
            if (indexOfRange(indexOfRange, array2, 0, length) < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new CopyOnWriteArrayList$$ExternalSyntheticLambda2(collection));
    }

    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new CopyOnWriteArrayList$$ExternalSyntheticLambda1(collection));
    }

    static /* synthetic */ boolean lambda$retainAll$1(Collection collection, Object obj) {
        return !collection.contains(obj);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int addAllAbsent(java.util.Collection<? extends E> r9) {
        /*
            r8 = this;
            java.lang.Object[] r0 = r9.toArray()
            java.lang.Class r9 = r9.getClass()
            java.lang.Class<java.util.ArrayList> r1 = java.util.ArrayList.class
            if (r9 == r1) goto L_0x0013
            java.lang.Object r9 = r0.clone()
            r0 = r9
            java.lang.Object[] r0 = (java.lang.Object[]) r0
        L_0x0013:
            int r9 = r0.length
            r1 = 0
            if (r9 != 0) goto L_0x0018
            return r1
        L_0x0018:
            java.lang.Object r9 = r8.lock
            monitor-enter(r9)
            java.lang.Object[] r2 = r8.getArray()     // Catch:{ all -> 0x004b }
            int r3 = r2.length     // Catch:{ all -> 0x004b }
            r4 = r1
            r5 = r4
        L_0x0022:
            int r6 = r0.length     // Catch:{ all -> 0x004b }
            if (r4 >= r6) goto L_0x003b
            r6 = r0[r4]     // Catch:{ all -> 0x004b }
            int r7 = indexOfRange(r6, r2, r1, r3)     // Catch:{ all -> 0x004b }
            if (r7 >= 0) goto L_0x0038
            int r7 = indexOfRange(r6, r0, r1, r5)     // Catch:{ all -> 0x004b }
            if (r7 >= 0) goto L_0x0038
            int r7 = r5 + 1
            r0[r5] = r6     // Catch:{ all -> 0x004b }
            r5 = r7
        L_0x0038:
            int r4 = r4 + 1
            goto L_0x0022
        L_0x003b:
            if (r5 <= 0) goto L_0x0049
            int r4 = r3 + r5
            java.lang.Object[] r2 = java.util.Arrays.copyOf((T[]) r2, (int) r4)     // Catch:{ all -> 0x004b }
            java.lang.System.arraycopy((java.lang.Object) r0, (int) r1, (java.lang.Object) r2, (int) r3, (int) r5)     // Catch:{ all -> 0x004b }
            r8.setArray(r2)     // Catch:{ all -> 0x004b }
        L_0x0049:
            monitor-exit(r9)     // Catch:{ all -> 0x004b }
            return r5
        L_0x004b:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x004b }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CopyOnWriteArrayList.addAllAbsent(java.util.Collection):int");
    }

    public void clear() {
        synchronized (this.lock) {
            setArray(new Object[0]);
        }
    }

    public boolean addAll(Collection<? extends E> collection) {
        Object[] array2 = collection.getClass() == CopyOnWriteArrayList.class ? ((CopyOnWriteArrayList) collection).getArray() : collection.toArray();
        if (array2.length == 0) {
            return false;
        }
        synchronized (this.lock) {
            Object[] array3 = getArray();
            int length = array3.length;
            if (length == 0) {
                if (collection.getClass() != CopyOnWriteArrayList.class) {
                    if (collection.getClass() == ArrayList.class) {
                    }
                }
                setArray(array2);
            }
            Object[] copyOf = Arrays.copyOf((T[]) array3, array2.length + length);
            System.arraycopy((Object) array2, 0, (Object) copyOf, length, array2.length);
            array2 = copyOf;
            setArray(array2);
        }
        return true;
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        Object[] objArr;
        Object[] array2 = collection.toArray();
        synchronized (this.lock) {
            Object[] array3 = getArray();
            int length = array3.length;
            if (i > length || i < 0) {
                throw new IndexOutOfBoundsException(outOfBounds(i, length));
            } else if (array2.length == 0) {
                return false;
            } else {
                int i2 = length - i;
                if (i2 == 0) {
                    objArr = Arrays.copyOf((T[]) array3, length + array2.length);
                } else {
                    Object[] objArr2 = new Object[(length + array2.length)];
                    System.arraycopy((Object) array3, 0, (Object) objArr2, 0, i);
                    System.arraycopy((Object) array3, i, (Object) objArr2, array2.length + i, i2);
                    objArr = objArr2;
                }
                System.arraycopy((Object) array2, 0, (Object) objArr, i, array2.length);
                setArray(objArr);
                return true;
            }
        }
    }

    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        for (Object accept : getArray()) {
            consumer.accept(accept);
        }
    }

    public boolean removeIf(Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return bulkRemove(predicate);
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
        boolean bulkRemove;
        synchronized (this.lock) {
            bulkRemove = bulkRemove(predicate, 0, getArray().length);
        }
        return bulkRemove;
    }

    /* access modifiers changed from: package-private */
    public boolean bulkRemove(Predicate<? super E> predicate, int i, int i2) {
        Object[] array2 = getArray();
        while (i < i2 && !predicate.test(elementAt(array2, i))) {
            i++;
        }
        if (i < i2) {
            long[] nBits = nBits(i2 - i);
            nBits[0] = 1;
            int i3 = 1;
            for (int i4 = i + 1; i4 < i2; i4++) {
                if (predicate.test(elementAt(array2, i4))) {
                    setBit(nBits, i4 - i);
                    i3++;
                }
            }
            if (array2 == getArray()) {
                Object[] copyOf = Arrays.copyOf((T[]) array2, array2.length - i3);
                int i5 = i;
                int i6 = i5;
                while (i5 < i2) {
                    if (isClear(nBits, i5 - i)) {
                        copyOf[i6] = array2[i5];
                        i6++;
                    }
                    i5++;
                }
                System.arraycopy((Object) array2, i5, (Object) copyOf, i6, array2.length - i5);
                setArray(copyOf);
                return true;
            }
            throw new ConcurrentModificationException();
        } else if (array2 == getArray()) {
            return false;
        } else {
            throw new ConcurrentModificationException();
        }
    }

    public void replaceAll(UnaryOperator<E> unaryOperator) {
        synchronized (this.lock) {
            replaceAllRange(unaryOperator, 0, getArray().length);
        }
    }

    /* access modifiers changed from: package-private */
    public void replaceAllRange(UnaryOperator<E> unaryOperator, int i, int i2) {
        Objects.requireNonNull(unaryOperator);
        Object[] objArr = (Object[]) getArray().clone();
        while (i < i2) {
            objArr[i] = unaryOperator.apply(elementAt(objArr, i));
            i++;
        }
        setArray(objArr);
    }

    public void sort(Comparator<? super E> comparator) {
        synchronized (this.lock) {
            sortRange(comparator, 0, getArray().length);
        }
    }

    /* access modifiers changed from: package-private */
    public void sortRange(Comparator<? super E> comparator, int i, int i2) {
        Object[] objArr = (Object[]) getArray().clone();
        Arrays.sort(objArr, i, i2, comparator);
        setArray(objArr);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Object[] array2 = getArray();
        objectOutputStream.writeInt(array2.length);
        for (Object writeObject : array2) {
            objectOutputStream.writeObject(writeObject);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        resetLock();
        int readInt = objectInputStream.readInt();
        SharedSecrets.getJavaObjectInputStreamAccess().checkArray(objectInputStream, Object[].class, readInt);
        Object[] objArr = new Object[readInt];
        for (int i = 0; i < readInt; i++) {
            objArr[i] = objectInputStream.readObject();
        }
        setArray(objArr);
    }

    public String toString() {
        return Arrays.toString(getArray());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        Iterator it = ((List) obj).iterator();
        for (Object obj2 : getArray()) {
            if (!it.hasNext() || !Objects.equals(obj2, it.next())) {
                return false;
            }
        }
        return !it.hasNext();
    }

    /* access modifiers changed from: private */
    public static int hashCodeOfRange(Object[] objArr, int i, int i2) {
        int i3;
        int i4 = 1;
        while (i < i2) {
            Object obj = objArr[i];
            int i5 = i4 * 31;
            if (obj == null) {
                i3 = 0;
            } else {
                i3 = obj.hashCode();
            }
            i4 = i5 + i3;
            i++;
        }
        return i4;
    }

    public int hashCode() {
        Object[] array2 = getArray();
        return hashCodeOfRange(array2, 0, array2.length);
    }

    public Iterator<E> iterator() {
        return new COWIterator(getArray(), 0);
    }

    public ListIterator<E> listIterator() {
        return new COWIterator(getArray(), 0);
    }

    public ListIterator<E> listIterator(int i) {
        Object[] array2 = getArray();
        int length = array2.length;
        if (i >= 0 && i <= length) {
            return new COWIterator(array2, i);
        }
        throw new IndexOutOfBoundsException(outOfBounds(i, length));
    }

    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(getArray(), 1040);
    }

    static final class COWIterator<E> implements ListIterator<E> {
        private int cursor;
        private final Object[] snapshot;

        COWIterator(Object[] objArr, int i) {
            this.cursor = i;
            this.snapshot = objArr;
        }

        public boolean hasNext() {
            return this.cursor < this.snapshot.length;
        }

        public boolean hasPrevious() {
            return this.cursor > 0;
        }

        public E next() {
            if (hasNext()) {
                E[] eArr = this.snapshot;
                int i = this.cursor;
                this.cursor = i + 1;
                return eArr[i];
            }
            throw new NoSuchElementException();
        }

        public E previous() {
            if (hasPrevious()) {
                E[] eArr = this.snapshot;
                int i = this.cursor - 1;
                this.cursor = i;
                return eArr[i];
            }
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return this.cursor;
        }

        public int previousIndex() {
            return this.cursor - 1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        public void add(E e) {
            throw new UnsupportedOperationException();
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            int length = this.snapshot.length;
            this.cursor = length;
            for (int i = this.cursor; i < length; i++) {
                consumer.accept(CopyOnWriteArrayList.elementAt(this.snapshot, i));
            }
        }
    }

    public List<E> subList(int i, int i2) {
        COWSubList cOWSubList;
        synchronized (this.lock) {
            Object[] array2 = getArray();
            int length = array2.length;
            int i3 = i2 - i;
            if (i < 0 || i2 > length || i3 < 0) {
                throw new IndexOutOfBoundsException();
            }
            cOWSubList = new COWSubList(array2, i, i3);
        }
        return cOWSubList;
    }

    private class COWSubList implements List<E>, RandomAccess {
        private Object[] expectedArray;
        private final int offset;
        private int size;

        COWSubList(Object[] objArr, int i, int i2) {
            this.expectedArray = objArr;
            this.offset = i;
            this.size = i2;
        }

        private void checkForComodification() {
            if (CopyOnWriteArrayList.this.getArray() != this.expectedArray) {
                throw new ConcurrentModificationException();
            }
        }

        private Object[] getArrayChecked() {
            Object[] array = CopyOnWriteArrayList.this.getArray();
            if (array == this.expectedArray) {
                return array;
            }
            throw new ConcurrentModificationException();
        }

        private void rangeCheck(int i) {
            if (i < 0 || i >= this.size) {
                throw new IndexOutOfBoundsException(CopyOnWriteArrayList.outOfBounds(i, this.size));
            }
        }

        private void rangeCheckForAdd(int i) {
            if (i < 0 || i > this.size) {
                throw new IndexOutOfBoundsException(CopyOnWriteArrayList.outOfBounds(i, this.size));
            }
        }

        public Object[] toArray() {
            Object[] arrayChecked;
            int i;
            int i2;
            synchronized (CopyOnWriteArrayList.this.lock) {
                arrayChecked = getArrayChecked();
                i = this.offset;
                i2 = this.size;
            }
            return Arrays.copyOfRange((T[]) arrayChecked, i, i2 + i);
        }

        public <T> T[] toArray(T[] tArr) {
            Object[] arrayChecked;
            int i;
            int i2;
            synchronized (CopyOnWriteArrayList.this.lock) {
                arrayChecked = getArrayChecked();
                i = this.offset;
                i2 = this.size;
            }
            if (tArr.length < i2) {
                return Arrays.copyOfRange(arrayChecked, i, i2 + i, tArr.getClass());
            }
            System.arraycopy((Object) arrayChecked, i, (Object) tArr, 0, i2);
            if (tArr.length > i2) {
                tArr[i2] = null;
            }
            return tArr;
        }

        public int indexOf(Object obj) {
            Object[] arrayChecked;
            int i;
            int i2;
            synchronized (CopyOnWriteArrayList.this.lock) {
                arrayChecked = getArrayChecked();
                i = this.offset;
                i2 = this.size;
            }
            int r3 = CopyOnWriteArrayList.indexOfRange(obj, arrayChecked, i, i2 + i);
            if (r3 == -1) {
                return -1;
            }
            return r3 - i;
        }

        public int lastIndexOf(Object obj) {
            Object[] arrayChecked;
            int i;
            int i2;
            synchronized (CopyOnWriteArrayList.this.lock) {
                arrayChecked = getArrayChecked();
                i = this.offset;
                i2 = this.size;
            }
            int r3 = CopyOnWriteArrayList.lastIndexOfRange(obj, arrayChecked, i, i2 + i);
            if (r3 == -1) {
                return -1;
            }
            return r3 - i;
        }

        public boolean contains(Object obj) {
            return indexOf(obj) >= 0;
        }

        public boolean containsAll(Collection<?> collection) {
            Object[] arrayChecked;
            int i;
            int i2;
            synchronized (CopyOnWriteArrayList.this.lock) {
                arrayChecked = getArrayChecked();
                i = this.offset;
                i2 = this.size;
            }
            for (Object r0 : collection) {
                if (CopyOnWriteArrayList.indexOfRange(r0, arrayChecked, i, i + i2) < 0) {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public int hashCode() {
            Object[] arrayChecked;
            int i;
            int i2;
            synchronized (CopyOnWriteArrayList.this.lock) {
                arrayChecked = getArrayChecked();
                i = this.offset;
                i2 = this.size;
            }
            return CopyOnWriteArrayList.hashCodeOfRange(arrayChecked, i, i2 + i);
        }

        public boolean equals(Object obj) {
            Object[] arrayChecked;
            int i;
            int i2;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof List)) {
                return false;
            }
            Iterator it = ((List) obj).iterator();
            synchronized (CopyOnWriteArrayList.this.lock) {
                arrayChecked = getArrayChecked();
                i = this.offset;
                i2 = this.size;
            }
            int i3 = i2 + i;
            while (i < i3) {
                if (!it.hasNext() || !Objects.equals(arrayChecked[i], it.next())) {
                    return false;
                }
                i++;
            }
            return !it.hasNext();
        }

        public E set(int i, E e) {
            E e2;
            synchronized (CopyOnWriteArrayList.this.lock) {
                rangeCheck(i);
                checkForComodification();
                e2 = CopyOnWriteArrayList.this.set(this.offset + i, e);
                this.expectedArray = CopyOnWriteArrayList.this.getArray();
            }
            return e2;
        }

        public E get(int i) {
            E e;
            synchronized (CopyOnWriteArrayList.this.lock) {
                rangeCheck(i);
                checkForComodification();
                e = CopyOnWriteArrayList.this.get(this.offset + i);
            }
            return e;
        }

        public int size() {
            int i;
            synchronized (CopyOnWriteArrayList.this.lock) {
                checkForComodification();
                i = this.size;
            }
            return i;
        }

        public boolean add(E e) {
            synchronized (CopyOnWriteArrayList.this.lock) {
                checkForComodification();
                CopyOnWriteArrayList.this.add(this.offset + this.size, e);
                this.expectedArray = CopyOnWriteArrayList.this.getArray();
                this.size++;
            }
            return true;
        }

        public void add(int i, E e) {
            synchronized (CopyOnWriteArrayList.this.lock) {
                checkForComodification();
                rangeCheckForAdd(i);
                CopyOnWriteArrayList.this.add(this.offset + i, e);
                this.expectedArray = CopyOnWriteArrayList.this.getArray();
                this.size++;
            }
        }

        public boolean addAll(Collection<? extends E> collection) {
            boolean addAll;
            synchronized (CopyOnWriteArrayList.this.lock) {
                Object[] arrayChecked = getArrayChecked();
                addAll = CopyOnWriteArrayList.this.addAll(this.offset + this.size, collection);
                int i = this.size;
                Object[] array = CopyOnWriteArrayList.this.getArray();
                this.expectedArray = array;
                this.size = i + (array.length - arrayChecked.length);
            }
            return addAll;
        }

        public boolean addAll(int i, Collection<? extends E> collection) {
            boolean addAll;
            synchronized (CopyOnWriteArrayList.this.lock) {
                rangeCheckForAdd(i);
                Object[] arrayChecked = getArrayChecked();
                addAll = CopyOnWriteArrayList.this.addAll(this.offset + i, collection);
                int i2 = this.size;
                Object[] array = CopyOnWriteArrayList.this.getArray();
                this.expectedArray = array;
                this.size = i2 + (array.length - arrayChecked.length);
            }
            return addAll;
        }

        public void clear() {
            synchronized (CopyOnWriteArrayList.this.lock) {
                checkForComodification();
                CopyOnWriteArrayList copyOnWriteArrayList = CopyOnWriteArrayList.this;
                int i = this.offset;
                copyOnWriteArrayList.removeRange(i, this.size + i);
                this.expectedArray = CopyOnWriteArrayList.this.getArray();
                this.size = 0;
            }
        }

        public E remove(int i) {
            E remove;
            synchronized (CopyOnWriteArrayList.this.lock) {
                rangeCheck(i);
                checkForComodification();
                remove = CopyOnWriteArrayList.this.remove(this.offset + i);
                this.expectedArray = CopyOnWriteArrayList.this.getArray();
                this.size--;
            }
            return remove;
        }

        public boolean remove(Object obj) {
            synchronized (CopyOnWriteArrayList.this.lock) {
                checkForComodification();
                int indexOf = indexOf(obj);
                if (indexOf == -1) {
                    return false;
                }
                remove(indexOf);
                return true;
            }
        }

        public Iterator<E> iterator() {
            return listIterator(0);
        }

        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        public ListIterator<E> listIterator(int i) {
            COWSubListIterator cOWSubListIterator;
            synchronized (CopyOnWriteArrayList.this.lock) {
                checkForComodification();
                rangeCheckForAdd(i);
                cOWSubListIterator = new COWSubListIterator(CopyOnWriteArrayList.this, i, this.offset, this.size);
            }
            return cOWSubListIterator;
        }

        public List<E> subList(int i, int i2) {
            COWSubList cOWSubList;
            synchronized (CopyOnWriteArrayList.this.lock) {
                checkForComodification();
                if (i < 0 || i2 > this.size || i > i2) {
                    throw new IndexOutOfBoundsException();
                }
                cOWSubList = new COWSubList(this.expectedArray, this.offset + i, i2 - i);
            }
            return cOWSubList;
        }

        public void forEach(Consumer<? super E> consumer) {
            Object[] arrayChecked;
            int i;
            int i2;
            Objects.requireNonNull(consumer);
            synchronized (CopyOnWriteArrayList.this.lock) {
                arrayChecked = getArrayChecked();
                i = this.offset;
                i2 = this.size + i;
            }
            while (i < i2) {
                consumer.accept(CopyOnWriteArrayList.elementAt(arrayChecked, i));
                i++;
            }
        }

        public void replaceAll(UnaryOperator<E> unaryOperator) {
            synchronized (CopyOnWriteArrayList.this.lock) {
                checkForComodification();
                CopyOnWriteArrayList copyOnWriteArrayList = CopyOnWriteArrayList.this;
                int i = this.offset;
                copyOnWriteArrayList.replaceAllRange(unaryOperator, i, this.size + i);
                this.expectedArray = CopyOnWriteArrayList.this.getArray();
            }
        }

        public void sort(Comparator<? super E> comparator) {
            synchronized (CopyOnWriteArrayList.this.lock) {
                checkForComodification();
                CopyOnWriteArrayList copyOnWriteArrayList = CopyOnWriteArrayList.this;
                int i = this.offset;
                copyOnWriteArrayList.sortRange(comparator, i, this.size + i);
                this.expectedArray = CopyOnWriteArrayList.this.getArray();
            }
        }

        public boolean removeAll(Collection<?> collection) {
            Objects.requireNonNull(collection);
            return bulkRemove(new CopyOnWriteArrayList$COWSubList$$ExternalSyntheticLambda1(collection));
        }

        public boolean retainAll(Collection<?> collection) {
            Objects.requireNonNull(collection);
            return bulkRemove(new CopyOnWriteArrayList$COWSubList$$ExternalSyntheticLambda0(collection));
        }

        static /* synthetic */ boolean lambda$retainAll$1(Collection collection, Object obj) {
            return !collection.contains(obj);
        }

        public boolean removeIf(Predicate<? super E> predicate) {
            Objects.requireNonNull(predicate);
            return bulkRemove(predicate);
        }

        private boolean bulkRemove(Predicate<? super E> predicate) {
            boolean bulkRemove;
            synchronized (CopyOnWriteArrayList.this.lock) {
                Object[] arrayChecked = getArrayChecked();
                CopyOnWriteArrayList copyOnWriteArrayList = CopyOnWriteArrayList.this;
                int i = this.offset;
                bulkRemove = copyOnWriteArrayList.bulkRemove(predicate, i, this.size + i);
                int i2 = this.size;
                Object[] array = CopyOnWriteArrayList.this.getArray();
                this.expectedArray = array;
                this.size = i2 + (array.length - arrayChecked.length);
            }
            return bulkRemove;
        }

        public Spliterator<E> spliterator() {
            Spliterator<E> spliterator;
            synchronized (CopyOnWriteArrayList.this.lock) {
                Object[] arrayChecked = getArrayChecked();
                int i = this.offset;
                spliterator = Spliterators.spliterator(arrayChecked, i, this.size + i, 1040);
            }
            return spliterator;
        }
    }

    private static class COWSubListIterator<E> implements ListIterator<E> {

        /* renamed from: it */
        private final ListIterator<E> f747it;
        private final int offset;
        private final int size;

        COWSubListIterator(List<E> list, int i, int i2, int i3) {
            this.offset = i2;
            this.size = i3;
            this.f747it = list.listIterator(i + i2);
        }

        public boolean hasNext() {
            return nextIndex() < this.size;
        }

        public E next() {
            if (hasNext()) {
                return this.f747it.next();
            }
            throw new NoSuchElementException();
        }

        public boolean hasPrevious() {
            return previousIndex() >= 0;
        }

        public E previous() {
            if (hasPrevious()) {
                return this.f747it.previous();
            }
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return this.f747it.nextIndex() - this.offset;
        }

        public int previousIndex() {
            return this.f747it.previousIndex() - this.offset;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        public void add(E e) {
            throw new UnsupportedOperationException();
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            while (hasNext()) {
                consumer.accept(this.f747it.next());
            }
        }
    }

    private void resetLock() {
        try {
            ((Field) AccessController.doPrivileged(new CopyOnWriteArrayList$$ExternalSyntheticLambda0())).set(this, new Object());
        } catch (IllegalAccessException e) {
            throw new Error((Throwable) e);
        }
    }

    static /* synthetic */ Field lambda$resetLock$2() {
        try {
            Field declaredField = CopyOnWriteArrayList.class.getDeclaredField("lock");
            declaredField.setAccessible(true);
            return declaredField;
        } catch (ReflectiveOperationException e) {
            throw new Error((Throwable) e);
        }
    }
}
