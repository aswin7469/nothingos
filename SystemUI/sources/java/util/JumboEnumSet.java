package java.util;

import java.lang.Enum;

class JumboEnumSet<E extends Enum<E>> extends EnumSet<E> {
    private static final long serialVersionUID = 334349849919042784L;
    /* access modifiers changed from: private */
    public long[] elements;
    /* access modifiers changed from: private */
    public int size = 0;

    JumboEnumSet(Class<E> cls, Enum<?>[] enumArr) {
        super(cls, enumArr);
        this.elements = new long[((enumArr.length + 63) >>> 6)];
    }

    /* access modifiers changed from: package-private */
    public void addRange(E e, E e2) {
        int ordinal = e.ordinal() >>> 6;
        int ordinal2 = e2.ordinal() >>> 6;
        if (ordinal == ordinal2) {
            this.elements[ordinal] = (-1 >>> ((e.ordinal() - e2.ordinal()) - 1)) << e.ordinal();
        } else {
            this.elements[ordinal] = -1 << e.ordinal();
            while (true) {
                ordinal++;
                if (ordinal >= ordinal2) {
                    break;
                }
                this.elements[ordinal] = -1;
            }
            this.elements[ordinal2] = -1 >>> (63 - e2.ordinal());
        }
        this.size = (e2.ordinal() - e.ordinal()) + 1;
    }

    /* access modifiers changed from: package-private */
    public void addAll() {
        int i = 0;
        while (true) {
            long[] jArr = this.elements;
            if (i < jArr.length) {
                jArr[i] = -1;
                i++;
            } else {
                int length = jArr.length - 1;
                jArr[length] = jArr[length] >>> (-this.universe.length);
                this.size = this.universe.length;
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void complement() {
        int i = 0;
        while (true) {
            long[] jArr = this.elements;
            if (i < jArr.length) {
                jArr[i] = ~jArr[i];
                i++;
            } else {
                int length = jArr.length - 1;
                jArr[length] = jArr[length] & (-1 >>> (-this.universe.length));
                this.size = this.universe.length - this.size;
                return;
            }
        }
    }

    public Iterator<E> iterator() {
        return new EnumSetIterator();
    }

    private class EnumSetIterator<E extends Enum<E>> implements Iterator<E> {
        long lastReturned = 0;
        int lastReturnedIndex = 0;
        long unseen;
        int unseenIndex = 0;

        EnumSetIterator() {
            this.unseen = JumboEnumSet.this.elements[0];
        }

        public boolean hasNext() {
            while (this.unseen == 0 && this.unseenIndex < JumboEnumSet.this.elements.length - 1) {
                long[] r0 = JumboEnumSet.this.elements;
                int i = this.unseenIndex + 1;
                this.unseenIndex = i;
                this.unseen = r0[i];
            }
            if (this.unseen != 0) {
                return true;
            }
            return false;
        }

        public E next() {
            if (hasNext()) {
                long j = this.unseen;
                long j2 = (-j) & j;
                this.lastReturned = j2;
                this.lastReturnedIndex = this.unseenIndex;
                this.unseen = j - j2;
                return JumboEnumSet.this.universe[(this.lastReturnedIndex << 6) + Long.numberOfTrailingZeros(this.lastReturned)];
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            if (this.lastReturned != 0) {
                long j = JumboEnumSet.this.elements[this.lastReturnedIndex];
                long[] r4 = JumboEnumSet.this.elements;
                int i = this.lastReturnedIndex;
                r4[i] = r4[i] & (~this.lastReturned);
                if (j != JumboEnumSet.this.elements[this.lastReturnedIndex]) {
                    JumboEnumSet jumboEnumSet = JumboEnumSet.this;
                    jumboEnumSet.size = jumboEnumSet.size - 1;
                }
                this.lastReturned = 0;
                return;
            }
            throw new IllegalStateException();
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> cls = obj.getClass();
        if (cls != this.elementType && cls.getSuperclass() != this.elementType) {
            return false;
        }
        int ordinal = ((Enum) obj).ordinal();
        if (((1 << ordinal) & this.elements[ordinal >>> 6]) != 0) {
            return true;
        }
        return false;
    }

    public boolean add(E e) {
        typeCheck(e);
        int ordinal = e.ordinal();
        int i = ordinal >>> 6;
        long[] jArr = this.elements;
        long j = jArr[i];
        long j2 = (1 << ordinal) | j;
        jArr[i] = j2;
        boolean z = j2 != j;
        if (z) {
            this.size++;
        }
        return z;
    }

    public boolean remove(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        Class<?> cls = obj.getClass();
        if (cls != this.elementType && cls.getSuperclass() != this.elementType) {
            return false;
        }
        int ordinal = ((Enum) obj).ordinal();
        int i = ordinal >>> 6;
        long[] jArr = this.elements;
        long j = jArr[i];
        long j2 = (~(1 << ordinal)) & j;
        jArr[i] = j2;
        if (j2 != j) {
            z = true;
        }
        if (z) {
            this.size--;
        }
        return z;
    }

    public boolean containsAll(Collection<?> collection) {
        if (!(collection instanceof JumboEnumSet)) {
            return super.containsAll(collection);
        }
        JumboEnumSet jumboEnumSet = (JumboEnumSet) collection;
        if (jumboEnumSet.elementType != this.elementType) {
            return jumboEnumSet.isEmpty();
        }
        int i = 0;
        while (true) {
            long[] jArr = this.elements;
            if (i >= jArr.length) {
                return true;
            }
            if ((jumboEnumSet.elements[i] & (~jArr[i])) != 0) {
                return false;
            }
            i++;
        }
    }

    public boolean addAll(Collection<? extends E> collection) {
        if (!(collection instanceof JumboEnumSet)) {
            return super.addAll(collection);
        }
        JumboEnumSet jumboEnumSet = (JumboEnumSet) collection;
        int i = 0;
        if (jumboEnumSet.elementType == this.elementType) {
            while (true) {
                long[] jArr = this.elements;
                if (i >= jArr.length) {
                    return recalculateSize();
                }
                jArr[i] = jArr[i] | jumboEnumSet.elements[i];
                i++;
            }
        } else if (jumboEnumSet.isEmpty()) {
            return false;
        } else {
            throw new ClassCastException(jumboEnumSet.elementType + " != " + this.elementType);
        }
    }

    public boolean removeAll(Collection<?> collection) {
        if (!(collection instanceof JumboEnumSet)) {
            return super.removeAll(collection);
        }
        JumboEnumSet jumboEnumSet = (JumboEnumSet) collection;
        int i = 0;
        if (jumboEnumSet.elementType != this.elementType) {
            return false;
        }
        while (true) {
            long[] jArr = this.elements;
            if (i >= jArr.length) {
                return recalculateSize();
            }
            jArr[i] = jArr[i] & (~jumboEnumSet.elements[i]);
            i++;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v1, types: [int] */
    /* JADX WARNING: type inference failed for: r2v4 */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean retainAll(java.util.Collection<?> r8) {
        /*
            r7 = this;
            boolean r0 = r8 instanceof java.util.JumboEnumSet
            if (r0 != 0) goto L_0x0009
            boolean r7 = super.retainAll(r8)
            return r7
        L_0x0009:
            java.util.JumboEnumSet r8 = (java.util.JumboEnumSet) r8
            java.lang.Class r0 = r8.elementType
            java.lang.Class r1 = r7.elementType
            r2 = 0
            if (r0 == r1) goto L_0x001b
            int r8 = r7.size
            if (r8 == 0) goto L_0x0017
            r2 = 1
        L_0x0017:
            r7.clear()
            return r2
        L_0x001b:
            long[] r0 = r7.elements
            int r1 = r0.length
            if (r2 >= r1) goto L_0x002c
            r3 = r0[r2]
            long[] r1 = r8.elements
            r5 = r1[r2]
            long r3 = r3 & r5
            r0[r2] = r3
            int r2 = r2 + 1
            goto L_0x001b
        L_0x002c:
            boolean r7 = r7.recalculateSize()
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.JumboEnumSet.retainAll(java.util.Collection):boolean");
    }

    public void clear() {
        Arrays.fill(this.elements, 0);
        this.size = 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof JumboEnumSet)) {
            return super.equals(obj);
        }
        JumboEnumSet jumboEnumSet = (JumboEnumSet) obj;
        if (jumboEnumSet.elementType != this.elementType) {
            return this.size == 0 && jumboEnumSet.size == 0;
        }
        return Arrays.equals(jumboEnumSet.elements, this.elements);
    }

    private boolean recalculateSize() {
        int i = this.size;
        this.size = 0;
        long[] jArr = this.elements;
        int length = jArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            this.size += Long.bitCount(jArr[i2]);
        }
        if (this.size != i) {
            return true;
        }
        return false;
    }

    public EnumSet<E> clone() {
        JumboEnumSet jumboEnumSet = (JumboEnumSet) super.clone();
        jumboEnumSet.elements = (long[]) jumboEnumSet.elements.clone();
        return jumboEnumSet;
    }
}
