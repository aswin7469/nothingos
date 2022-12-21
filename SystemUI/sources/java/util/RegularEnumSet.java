package java.util;

import java.lang.Enum;

class RegularEnumSet<E extends Enum<E>> extends EnumSet<E> {
    private static final long serialVersionUID = 3411599620347842686L;
    /* access modifiers changed from: private */
    public long elements = 0;

    RegularEnumSet(Class<E> cls, Enum<?>[] enumArr) {
        super(cls, enumArr);
    }

    /* access modifiers changed from: package-private */
    public void addRange(E e, E e2) {
        this.elements = (-1 >>> ((e.ordinal() - e2.ordinal()) - 1)) << e.ordinal();
    }

    /* access modifiers changed from: package-private */
    public void addAll() {
        if (this.universe.length != 0) {
            this.elements = -1 >>> (-this.universe.length);
        }
    }

    /* access modifiers changed from: package-private */
    public void complement() {
        if (this.universe.length != 0) {
            long j = ~this.elements;
            this.elements = j;
            this.elements = j & (-1 >>> (-this.universe.length));
        }
    }

    public Iterator<E> iterator() {
        return new EnumSetIterator();
    }

    private class EnumSetIterator<E extends Enum<E>> implements Iterator<E> {
        long lastReturned = 0;
        long unseen;

        EnumSetIterator() {
            this.unseen = RegularEnumSet.this.elements;
        }

        public boolean hasNext() {
            return this.unseen != 0;
        }

        public E next() {
            long j = this.unseen;
            if (j != 0) {
                long j2 = (-j) & j;
                this.lastReturned = j2;
                this.unseen = j - j2;
                return RegularEnumSet.this.universe[Long.numberOfTrailingZeros(this.lastReturned)];
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            if (this.lastReturned != 0) {
                RegularEnumSet regularEnumSet = RegularEnumSet.this;
                regularEnumSet.elements = regularEnumSet.elements & (~this.lastReturned);
                this.lastReturned = 0;
                return;
            }
            throw new IllegalStateException();
        }
    }

    public int size() {
        return Long.bitCount(this.elements);
    }

    public boolean isEmpty() {
        return this.elements == 0;
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> cls = obj.getClass();
        if (cls != this.elementType && cls.getSuperclass() != this.elementType) {
            return false;
        }
        if (((1 << ((Enum) obj).ordinal()) & this.elements) != 0) {
            return true;
        }
        return false;
    }

    public boolean add(E e) {
        typeCheck(e);
        long j = this.elements;
        long ordinal = (1 << e.ordinal()) | j;
        this.elements = ordinal;
        return ordinal != j;
    }

    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> cls = obj.getClass();
        if (cls != this.elementType && cls.getSuperclass() != this.elementType) {
            return false;
        }
        long j = this.elements;
        long j2 = (~(1 << ((Enum) obj).ordinal())) & j;
        this.elements = j2;
        if (j2 != j) {
            return true;
        }
        return false;
    }

    public boolean containsAll(Collection<?> collection) {
        if (!(collection instanceof RegularEnumSet)) {
            return super.containsAll(collection);
        }
        RegularEnumSet regularEnumSet = (RegularEnumSet) collection;
        if (regularEnumSet.elementType != this.elementType) {
            return regularEnumSet.isEmpty();
        }
        return ((~this.elements) & regularEnumSet.elements) == 0;
    }

    public boolean addAll(Collection<? extends E> collection) {
        if (!(collection instanceof RegularEnumSet)) {
            return super.addAll(collection);
        }
        RegularEnumSet regularEnumSet = (RegularEnumSet) collection;
        if (regularEnumSet.elementType == this.elementType) {
            long j = this.elements;
            long j2 = regularEnumSet.elements | j;
            this.elements = j2;
            if (j2 != j) {
                return true;
            }
            return false;
        } else if (regularEnumSet.isEmpty()) {
            return false;
        } else {
            throw new ClassCastException(regularEnumSet.elementType + " != " + this.elementType);
        }
    }

    public boolean removeAll(Collection<?> collection) {
        if (!(collection instanceof RegularEnumSet)) {
            return super.removeAll(collection);
        }
        RegularEnumSet regularEnumSet = (RegularEnumSet) collection;
        if (regularEnumSet.elementType != this.elementType) {
            return false;
        }
        long j = this.elements;
        long j2 = (~regularEnumSet.elements) & j;
        this.elements = j2;
        if (j2 != j) {
            return true;
        }
        return false;
    }

    public boolean retainAll(Collection<?> collection) {
        if (!(collection instanceof RegularEnumSet)) {
            return super.retainAll(collection);
        }
        RegularEnumSet regularEnumSet = (RegularEnumSet) collection;
        boolean z = true;
        if (regularEnumSet.elementType != this.elementType) {
            if (this.elements == 0) {
                z = false;
            }
            this.elements = 0;
            return z;
        }
        long j = this.elements;
        long j2 = regularEnumSet.elements & j;
        this.elements = j2;
        if (j2 != j) {
            return true;
        }
        return false;
    }

    public void clear() {
        this.elements = 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof RegularEnumSet)) {
            return super.equals(obj);
        }
        RegularEnumSet regularEnumSet = (RegularEnumSet) obj;
        if (regularEnumSet.elementType != this.elementType) {
            if (this.elements == 0 && regularEnumSet.elements == 0) {
                return true;
            }
            return false;
        } else if (regularEnumSet.elements == this.elements) {
            return true;
        } else {
            return false;
        }
    }
}
