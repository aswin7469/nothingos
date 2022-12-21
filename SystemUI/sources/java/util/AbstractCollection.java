package java.util;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.reflect.Array;

public abstract class AbstractCollection<E> implements Collection<E> {
    private static final int MAX_ARRAY_SIZE = 2147483639;

    public abstract Iterator<E> iterator();

    public abstract int size();

    protected AbstractCollection() {
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Object obj) {
        Iterator it = iterator();
        if (obj == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    return true;
                }
            }
            return false;
        }
        while (it.hasNext()) {
            if (obj.equals(it.next())) {
                return true;
            }
        }
        return false;
    }

    public Object[] toArray() {
        int size = size();
        Object[] objArr = new Object[size];
        Iterator it = iterator();
        for (int i = 0; i < size; i++) {
            if (!it.hasNext()) {
                return Arrays.copyOf((T[]) objArr, i);
            }
            objArr[i] = it.next();
        }
        return it.hasNext() ? finishToArray(objArr, it) : objArr;
    }

    public <T> T[] toArray(T[] tArr) {
        T[] tArr2;
        int size = size();
        if (tArr.length >= size) {
            tArr2 = tArr;
        } else {
            tArr2 = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
        }
        Iterator it = iterator();
        for (int i = 0; i < tArr2.length; i++) {
            if (!it.hasNext()) {
                if (tArr == tArr2) {
                    tArr2[i] = null;
                } else if (tArr.length < i) {
                    return Arrays.copyOf(tArr2, i);
                } else {
                    System.arraycopy((Object) tArr2, 0, (Object) tArr, 0, i);
                    if (tArr.length > i) {
                        tArr[i] = null;
                    }
                }
                return tArr;
            }
            tArr2[i] = it.next();
        }
        return it.hasNext() ? finishToArray(tArr2, it) : tArr2;
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [java.util.Iterator, java.util.Iterator<?>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static <T> T[] finishToArray(T[] r4, java.util.Iterator<?> r5) {
        /*
            int r0 = r4.length
        L_0x0001:
            boolean r1 = r5.hasNext()
            if (r1 == 0) goto L_0x002a
            int r1 = r4.length
            if (r0 != r1) goto L_0x0020
            int r2 = r1 >> 1
            int r2 = r2 + r1
            int r2 = r2 + 1
            r3 = 2147483639(0x7ffffff7, float:NaN)
            int r3 = r2 - r3
            if (r3 <= 0) goto L_0x001c
            int r1 = r1 + 1
            int r2 = hugeCapacity(r1)
        L_0x001c:
            java.lang.Object[] r4 = java.util.Arrays.copyOf((T[]) r4, (int) r2)
        L_0x0020:
            int r1 = r0 + 1
            java.lang.Object r2 = r5.next()
            r4[r0] = r2
            r0 = r1
            goto L_0x0001
        L_0x002a:
            int r5 = r4.length
            if (r0 != r5) goto L_0x002e
            goto L_0x0032
        L_0x002e:
            java.lang.Object[] r4 = java.util.Arrays.copyOf((T[]) r4, (int) r0)
        L_0x0032:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.AbstractCollection.finishToArray(java.lang.Object[], java.util.Iterator):java.lang.Object[]");
    }

    private static int hugeCapacity(int i) {
        if (i < 0) {
            throw new OutOfMemoryError("Required array size too large");
        } else if (i > MAX_ARRAY_SIZE) {
            return Integer.MAX_VALUE;
        } else {
            return MAX_ARRAY_SIZE;
        }
    }

    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object obj) {
        Iterator it = iterator();
        if (obj == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }
        while (it.hasNext()) {
            if (obj.equals(it.next())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean containsAll(Collection<?> collection) {
        for (Object contains : collection) {
            if (!contains(contains)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> collection) {
        boolean z = false;
        for (Object add : collection) {
            if (add(add)) {
                z = true;
            }
        }
        return z;
    }

    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        Iterator it = iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (collection.contains(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        Iterator it = iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public void clear() {
        Iterator it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    public String toString() {
        Iterator it = iterator();
        if (!it.hasNext()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        while (true) {
            Object next = it.next();
            if (next == this) {
                next = "(this Collection)";
            }
            sb.append(next);
            if (!it.hasNext()) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
        }
    }
}
