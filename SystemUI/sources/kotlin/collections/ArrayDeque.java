package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ArrayDeque.kt */
/* loaded from: classes2.dex */
public final class ArrayDeque<E> extends AbstractMutableList<E> {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private static final Object[] emptyElementData = new Object[0];
    private Object[] elementData = emptyElementData;
    private int head;
    private int size;

    @Override // kotlin.collections.AbstractMutableList
    public int getSize() {
        return this.size;
    }

    private final void ensureCapacity(int i) {
        int coerceAtLeast;
        if (i < 0) {
            throw new IllegalStateException("Deque is too big.");
        }
        Object[] objArr = this.elementData;
        if (i <= objArr.length) {
            return;
        }
        if (objArr == emptyElementData) {
            coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(i, 10);
            this.elementData = new Object[coerceAtLeast];
            return;
        }
        copyElements(Companion.newCapacity$kotlin_stdlib(objArr.length, i));
    }

    private final void copyElements(int i) {
        Object[] objArr = new Object[i];
        Object[] objArr2 = this.elementData;
        ArraysKt___ArraysJvmKt.copyInto(objArr2, objArr, 0, this.head, objArr2.length);
        Object[] objArr3 = this.elementData;
        int length = objArr3.length;
        int i2 = this.head;
        ArraysKt___ArraysJvmKt.copyInto(objArr3, objArr, length - i2, 0, i2);
        this.head = 0;
        this.elementData = objArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int positiveMod(int i) {
        Object[] objArr = this.elementData;
        return i >= objArr.length ? i - objArr.length : i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int negativeMod(int i) {
        return i < 0 ? i + this.elementData.length : i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int incremented(int i) {
        if (i == ArraysKt___ArraysKt.getLastIndex(this.elementData)) {
            return 0;
        }
        return i + 1;
    }

    private final int decremented(int i) {
        return i == 0 ? ArraysKt___ArraysKt.getLastIndex(this.elementData) : i - 1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        return size() == 0;
    }

    public final void addFirst(E e) {
        ensureCapacity(size() + 1);
        int decremented = decremented(this.head);
        this.head = decremented;
        this.elementData[decremented] = e;
        this.size = size() + 1;
    }

    public final void addLast(E e) {
        ensureCapacity(size() + 1);
        this.elementData[positiveMod(this.head + size())] = e;
        this.size = size() + 1;
    }

    public final E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty.");
        }
        E e = (E) this.elementData[this.head];
        Object[] objArr = this.elementData;
        int i = this.head;
        objArr[i] = null;
        this.head = incremented(i);
        this.size = size() - 1;
        return e;
    }

    @Nullable
    public final E removeFirstOrNull() {
        if (isEmpty()) {
            return null;
        }
        return removeFirst();
    }

    public final E removeLast() {
        int lastIndex;
        if (isEmpty()) {
            throw new NoSuchElementException("ArrayDeque is empty.");
        }
        lastIndex = CollectionsKt__CollectionsKt.getLastIndex(this);
        int positiveMod = positiveMod(this.head + lastIndex);
        E e = (E) this.elementData[positiveMod];
        this.elementData[positiveMod] = null;
        this.size = size() - 1;
        return e;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int i, E e) {
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(i, size());
        if (i == size()) {
            addLast(e);
        } else if (i == 0) {
            addFirst(e);
        } else {
            ensureCapacity(size() + 1);
            int positiveMod = positiveMod(this.head + i);
            if (i < ((size() + 1) >> 1)) {
                int decremented = decremented(positiveMod);
                int decremented2 = decremented(this.head);
                int i2 = this.head;
                if (decremented >= i2) {
                    Object[] objArr = this.elementData;
                    objArr[decremented2] = objArr[i2];
                    ArraysKt___ArraysJvmKt.copyInto(objArr, objArr, i2, i2 + 1, decremented + 1);
                } else {
                    Object[] objArr2 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr2, objArr2, i2 - 1, i2, objArr2.length);
                    Object[] objArr3 = this.elementData;
                    objArr3[objArr3.length - 1] = objArr3[0];
                    ArraysKt___ArraysJvmKt.copyInto(objArr3, objArr3, 0, 1, decremented + 1);
                }
                this.elementData[decremented] = e;
                this.head = decremented2;
            } else {
                int positiveMod2 = positiveMod(this.head + size());
                if (positiveMod < positiveMod2) {
                    Object[] objArr4 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr4, objArr4, positiveMod + 1, positiveMod, positiveMod2);
                } else {
                    Object[] objArr5 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr5, objArr5, 1, 0, positiveMod2);
                    Object[] objArr6 = this.elementData;
                    objArr6[0] = objArr6[objArr6.length - 1];
                    ArraysKt___ArraysJvmKt.copyInto(objArr6, objArr6, positiveMod + 1, positiveMod, objArr6.length - 1);
                }
                this.elementData[positiveMod] = e;
            }
            this.size = size() + 1;
        }
    }

    private final void copyCollectionElements(int i, Collection<? extends E> collection) {
        Iterator<? extends E> it = collection.iterator();
        int length = this.elementData.length;
        while (i < length && it.hasNext()) {
            this.elementData[i] = it.next();
            i++;
        }
        int i2 = this.head;
        for (int i3 = 0; i3 < i2 && it.hasNext(); i3++) {
            this.elementData[i3] = it.next();
        }
        this.size = size() + collection.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(@NotNull Collection<? extends E> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        if (elements.isEmpty()) {
            return false;
        }
        ensureCapacity(size() + elements.size());
        copyCollectionElements(positiveMod(this.head + size()), elements);
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int i, @NotNull Collection<? extends E> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(i, size());
        if (elements.isEmpty()) {
            return false;
        }
        if (i == size()) {
            return addAll(elements);
        }
        ensureCapacity(size() + elements.size());
        int positiveMod = positiveMod(this.head + size());
        int positiveMod2 = positiveMod(this.head + i);
        int size = elements.size();
        if (i < ((size() + 1) >> 1)) {
            int i2 = this.head;
            int i3 = i2 - size;
            if (positiveMod2 < i2) {
                Object[] objArr = this.elementData;
                ArraysKt___ArraysJvmKt.copyInto(objArr, objArr, i3, i2, objArr.length);
                if (size >= positiveMod2) {
                    Object[] objArr2 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr2, objArr2, objArr2.length - size, 0, positiveMod2);
                } else {
                    Object[] objArr3 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr3, objArr3, objArr3.length - size, 0, size);
                    Object[] objArr4 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr4, objArr4, 0, size, positiveMod2);
                }
            } else if (i3 >= 0) {
                Object[] objArr5 = this.elementData;
                ArraysKt___ArraysJvmKt.copyInto(objArr5, objArr5, i3, i2, positiveMod2);
            } else {
                Object[] objArr6 = this.elementData;
                i3 += objArr6.length;
                int i4 = positiveMod2 - i2;
                int length = objArr6.length - i3;
                if (length >= i4) {
                    ArraysKt___ArraysJvmKt.copyInto(objArr6, objArr6, i3, i2, positiveMod2);
                } else {
                    ArraysKt___ArraysJvmKt.copyInto(objArr6, objArr6, i3, i2, i2 + length);
                    Object[] objArr7 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr7, objArr7, 0, this.head + length, positiveMod2);
                }
            }
            this.head = i3;
            copyCollectionElements(negativeMod(positiveMod2 - size), elements);
        } else {
            int i5 = positiveMod2 + size;
            if (positiveMod2 < positiveMod) {
                int i6 = size + positiveMod;
                Object[] objArr8 = this.elementData;
                if (i6 <= objArr8.length) {
                    ArraysKt___ArraysJvmKt.copyInto(objArr8, objArr8, i5, positiveMod2, positiveMod);
                } else if (i5 >= objArr8.length) {
                    ArraysKt___ArraysJvmKt.copyInto(objArr8, objArr8, i5 - objArr8.length, positiveMod2, positiveMod);
                } else {
                    int length2 = positiveMod - (i6 - objArr8.length);
                    ArraysKt___ArraysJvmKt.copyInto(objArr8, objArr8, 0, length2, positiveMod);
                    Object[] objArr9 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr9, objArr9, i5, positiveMod2, length2);
                }
            } else {
                Object[] objArr10 = this.elementData;
                ArraysKt___ArraysJvmKt.copyInto(objArr10, objArr10, size, 0, positiveMod);
                Object[] objArr11 = this.elementData;
                if (i5 >= objArr11.length) {
                    ArraysKt___ArraysJvmKt.copyInto(objArr11, objArr11, i5 - objArr11.length, positiveMod2, objArr11.length);
                } else {
                    ArraysKt___ArraysJvmKt.copyInto(objArr11, objArr11, 0, objArr11.length - size, objArr11.length);
                    Object[] objArr12 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr12, objArr12, i5, positiveMod2, objArr12.length - size);
                }
            }
            copyCollectionElements(positiveMod2, elements);
        }
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, size());
        return (E) this.elementData[positiveMod(this.head + i)];
    }

    @Override // java.util.AbstractList, java.util.List
    public E set(int i, E e) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, size());
        int positiveMod = positiveMod(this.head + i);
        E e2 = (E) this.elementData[positiveMod];
        this.elementData[positiveMod] = e;
        return e2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object obj) {
        int i;
        int positiveMod = positiveMod(this.head + size());
        int i2 = this.head;
        if (i2 < positiveMod) {
            while (i2 < positiveMod) {
                if (Intrinsics.areEqual(obj, this.elementData[i2])) {
                    i = this.head;
                } else {
                    i2++;
                }
            }
            return -1;
        } else if (i2 < positiveMod) {
            return -1;
        } else {
            int length = this.elementData.length;
            while (true) {
                if (i2 >= length) {
                    for (int i3 = 0; i3 < positiveMod; i3++) {
                        if (Intrinsics.areEqual(obj, this.elementData[i3])) {
                            i2 = i3 + this.elementData.length;
                            i = this.head;
                        }
                    }
                    return -1;
                } else if (Intrinsics.areEqual(obj, this.elementData[i2])) {
                    i = this.head;
                    break;
                } else {
                    i2++;
                }
            }
        }
        return i2 - i;
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object obj) {
        int lastIndex;
        int i;
        int positiveMod = positiveMod(this.head + size());
        int i2 = this.head;
        if (i2 < positiveMod) {
            lastIndex = positiveMod - 1;
            if (lastIndex < i2) {
                return -1;
            }
            while (!Intrinsics.areEqual(obj, this.elementData[lastIndex])) {
                if (lastIndex == i2) {
                    return -1;
                }
                lastIndex--;
            }
            i = this.head;
        } else if (i2 <= positiveMod) {
            return -1;
        } else {
            int i3 = positiveMod - 1;
            while (true) {
                if (i3 >= 0) {
                    if (Intrinsics.areEqual(obj, this.elementData[i3])) {
                        lastIndex = i3 + this.elementData.length;
                        i = this.head;
                        break;
                    }
                    i3--;
                } else {
                    lastIndex = ArraysKt___ArraysKt.getLastIndex(this.elementData);
                    int i4 = this.head;
                    if (lastIndex < i4) {
                        return -1;
                    }
                    while (!Intrinsics.areEqual(obj, this.elementData[lastIndex])) {
                        if (lastIndex == i4) {
                            return -1;
                        }
                        lastIndex--;
                    }
                    i = this.head;
                }
            }
        }
        return lastIndex - i;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(Object obj) {
        int indexOf = indexOf(obj);
        if (indexOf == -1) {
            return false;
        }
        remove(indexOf);
        return true;
    }

    @Override // kotlin.collections.AbstractMutableList
    public E removeAt(int i) {
        int lastIndex;
        int lastIndex2;
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, size());
        lastIndex = CollectionsKt__CollectionsKt.getLastIndex(this);
        if (i == lastIndex) {
            return removeLast();
        }
        if (i != 0) {
            int positiveMod = positiveMod(this.head + i);
            E e = (E) this.elementData[positiveMod];
            if (i < (size() >> 1)) {
                int i2 = this.head;
                if (positiveMod >= i2) {
                    Object[] objArr = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr, objArr, i2 + 1, i2, positiveMod);
                } else {
                    Object[] objArr2 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr2, objArr2, 1, 0, positiveMod);
                    Object[] objArr3 = this.elementData;
                    objArr3[0] = objArr3[objArr3.length - 1];
                    int i3 = this.head;
                    ArraysKt___ArraysJvmKt.copyInto(objArr3, objArr3, i3 + 1, i3, objArr3.length - 1);
                }
                Object[] objArr4 = this.elementData;
                int i4 = this.head;
                objArr4[i4] = null;
                this.head = incremented(i4);
            } else {
                lastIndex2 = CollectionsKt__CollectionsKt.getLastIndex(this);
                int positiveMod2 = positiveMod(this.head + lastIndex2);
                if (positiveMod <= positiveMod2) {
                    Object[] objArr5 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr5, objArr5, positiveMod, positiveMod + 1, positiveMod2 + 1);
                } else {
                    Object[] objArr6 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto(objArr6, objArr6, positiveMod, positiveMod + 1, objArr6.length);
                    Object[] objArr7 = this.elementData;
                    objArr7[objArr7.length - 1] = objArr7[0];
                    ArraysKt___ArraysJvmKt.copyInto(objArr7, objArr7, 0, 1, positiveMod2 + 1);
                }
                this.elementData[positiveMod2] = null;
            }
            this.size = size() - 1;
            return e;
        }
        return removeFirst();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean removeAll(@NotNull Collection<? extends Object> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        boolean z = false;
        z = false;
        z = false;
        if (!isEmpty()) {
            if ((this.elementData.length == 0 ? 1 : null) == null) {
                int positiveMod = positiveMod(this.head + size());
                int i = this.head;
                if (this.head < positiveMod) {
                    for (int i2 = this.head; i2 < positiveMod; i2++) {
                        Object obj = this.elementData[i2];
                        if (!elements.contains(obj)) {
                            this.elementData[i] = obj;
                            i++;
                        } else {
                            z = true;
                        }
                    }
                    ArraysKt___ArraysJvmKt.fill(this.elementData, null, i, positiveMod);
                } else {
                    int length = this.elementData.length;
                    boolean z2 = false;
                    for (int i3 = this.head; i3 < length; i3++) {
                        Object obj2 = this.elementData[i3];
                        this.elementData[i3] = null;
                        if (!elements.contains(obj2)) {
                            this.elementData[i] = obj2;
                            i++;
                        } else {
                            z2 = true;
                        }
                    }
                    i = positiveMod(i);
                    for (int i4 = 0; i4 < positiveMod; i4++) {
                        Object obj3 = this.elementData[i4];
                        this.elementData[i4] = null;
                        if (!elements.contains(obj3)) {
                            this.elementData[i] = obj3;
                            i = incremented(i);
                        } else {
                            z2 = true;
                        }
                    }
                    z = z2;
                }
                if (z) {
                    this.size = negativeMod(i - this.head);
                }
            }
        }
        return z;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean retainAll(@NotNull Collection<? extends Object> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        boolean z = false;
        z = false;
        z = false;
        if (!isEmpty()) {
            if ((this.elementData.length == 0 ? 1 : null) == null) {
                int positiveMod = positiveMod(this.head + size());
                int i = this.head;
                if (this.head < positiveMod) {
                    for (int i2 = this.head; i2 < positiveMod; i2++) {
                        Object obj = this.elementData[i2];
                        if (elements.contains(obj)) {
                            this.elementData[i] = obj;
                            i++;
                        } else {
                            z = true;
                        }
                    }
                    ArraysKt___ArraysJvmKt.fill(this.elementData, null, i, positiveMod);
                } else {
                    int length = this.elementData.length;
                    boolean z2 = false;
                    for (int i3 = this.head; i3 < length; i3++) {
                        Object obj2 = this.elementData[i3];
                        this.elementData[i3] = null;
                        if (elements.contains(obj2)) {
                            this.elementData[i] = obj2;
                            i++;
                        } else {
                            z2 = true;
                        }
                    }
                    i = positiveMod(i);
                    for (int i4 = 0; i4 < positiveMod; i4++) {
                        Object obj3 = this.elementData[i4];
                        this.elementData[i4] = null;
                        if (elements.contains(obj3)) {
                            this.elementData[i] = obj3;
                            i = incremented(i);
                        } else {
                            z2 = true;
                        }
                    }
                    z = z2;
                }
                if (z) {
                    this.size = negativeMod(i - this.head);
                }
            }
        }
        return z;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        int positiveMod = positiveMod(this.head + size());
        int i = this.head;
        if (i < positiveMod) {
            ArraysKt___ArraysJvmKt.fill(this.elementData, null, i, positiveMod);
        } else if (!isEmpty()) {
            Object[] objArr = this.elementData;
            ArraysKt___ArraysJvmKt.fill(objArr, null, this.head, objArr.length);
            ArraysKt___ArraysJvmKt.fill(this.elementData, null, 0, positiveMod);
        }
        this.head = 0;
        this.size = 0;
    }

    /* compiled from: ArrayDeque.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public final int newCapacity$kotlin_stdlib(int i, int i2) {
            int i3 = i + (i >> 1);
            if (i3 - i2 < 0) {
                i3 = i2;
            }
            int i4 = 2147483639;
            if (i3 - 2147483639 > 0) {
                if (i2 > 2147483639) {
                    i4 = Integer.MAX_VALUE;
                }
                return i4;
            }
            return i3;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}
