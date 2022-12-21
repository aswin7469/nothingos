package java.util;

import java.lang.Enum;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;

public abstract class EnumSet<E extends Enum<E>> extends AbstractSet<E> implements Cloneable, Serializable {
    private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];
    final Class<E> elementType;
    final Enum<?>[] universe;

    static Enum<?>[] access$000() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public abstract void addAll();

    /* access modifiers changed from: package-private */
    public abstract void addRange(E e, E e2);

    /* access modifiers changed from: package-private */
    public abstract void complement();

    EnumSet(Class<E> cls, Enum<?>[] enumArr) {
        this.elementType = cls;
        this.universe = enumArr;
    }

    public static <E extends Enum<E>> EnumSet<E> noneOf(Class<E> cls) {
        Enum[] universe2 = getUniverse(cls);
        if (universe2 == null) {
            throw new ClassCastException(cls + " not an enum");
        } else if (universe2.length <= 64) {
            return new RegularEnumSet(cls, universe2);
        } else {
            return new JumboEnumSet(cls, universe2);
        }
    }

    public static <E extends Enum<E>> EnumSet<E> allOf(Class<E> cls) {
        EnumSet<E> noneOf = noneOf(cls);
        noneOf.addAll();
        return noneOf;
    }

    public static <E extends Enum<E>> EnumSet<E> copyOf(EnumSet<E> enumSet) {
        return enumSet.clone();
    }

    public static <E extends Enum<E>> EnumSet<E> copyOf(Collection<E> collection) {
        if (collection instanceof EnumSet) {
            return ((EnumSet) collection).clone();
        }
        if (!collection.isEmpty()) {
            Iterator<E> it = collection.iterator();
            EnumSet<E> of = m1716of((Enum) it.next());
            while (it.hasNext()) {
                of.add((Enum) it.next());
            }
            return of;
        }
        throw new IllegalArgumentException("Collection is empty");
    }

    public static <E extends Enum<E>> EnumSet<E> complementOf(EnumSet<E> enumSet) {
        EnumSet<E> copyOf = copyOf(enumSet);
        copyOf.complement();
        return copyOf;
    }

    /* renamed from: of */
    public static <E extends Enum<E>> EnumSet<E> m1716of(E e) {
        EnumSet<E> noneOf = noneOf(e.getDeclaringClass());
        noneOf.add(e);
        return noneOf;
    }

    /* renamed from: of */
    public static <E extends Enum<E>> EnumSet<E> m1717of(E e, E e2) {
        EnumSet<E> noneOf = noneOf(e.getDeclaringClass());
        noneOf.add(e);
        noneOf.add(e2);
        return noneOf;
    }

    /* renamed from: of */
    public static <E extends Enum<E>> EnumSet<E> m1718of(E e, E e2, E e3) {
        EnumSet<E> noneOf = noneOf(e.getDeclaringClass());
        noneOf.add(e);
        noneOf.add(e2);
        noneOf.add(e3);
        return noneOf;
    }

    /* renamed from: of */
    public static <E extends Enum<E>> EnumSet<E> m1719of(E e, E e2, E e3, E e4) {
        EnumSet<E> noneOf = noneOf(e.getDeclaringClass());
        noneOf.add(e);
        noneOf.add(e2);
        noneOf.add(e3);
        noneOf.add(e4);
        return noneOf;
    }

    /* renamed from: of */
    public static <E extends Enum<E>> EnumSet<E> m1720of(E e, E e2, E e3, E e4, E e5) {
        EnumSet<E> noneOf = noneOf(e.getDeclaringClass());
        noneOf.add(e);
        noneOf.add(e2);
        noneOf.add(e3);
        noneOf.add(e4);
        noneOf.add(e5);
        return noneOf;
    }

    @SafeVarargs
    /* renamed from: of */
    public static <E extends Enum<E>> EnumSet<E> m1721of(E e, E... eArr) {
        EnumSet<E> noneOf = noneOf(e.getDeclaringClass());
        noneOf.add(e);
        for (E add : eArr) {
            noneOf.add(add);
        }
        return noneOf;
    }

    public static <E extends Enum<E>> EnumSet<E> range(E e, E e2) {
        if (e.compareTo(e2) <= 0) {
            EnumSet<E> noneOf = noneOf(e.getDeclaringClass());
            noneOf.addRange(e, e2);
            return noneOf;
        }
        throw new IllegalArgumentException(e + " > " + e2);
    }

    public EnumSet<E> clone() {
        try {
            return (EnumSet) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError((Object) e);
        }
    }

    /* access modifiers changed from: package-private */
    public final void typeCheck(E e) {
        Class<?> cls = e.getClass();
        if (cls != this.elementType && cls.getSuperclass() != this.elementType) {
            throw new ClassCastException(cls + " != " + this.elementType);
        }
    }

    private static <E extends Enum<E>> E[] getUniverse(Class<E> cls) {
        return (Enum[]) cls.getEnumConstantsShared();
    }

    private static class SerializationProxy<E extends Enum<E>> implements Serializable {
        private static final Enum<?>[] ZERO_LENGTH_ENUM_ARRAY = new Enum[0];
        private static final long serialVersionUID = 362491234563181265L;
        private final Class<E> elementType;
        private final Enum<?>[] elements;

        SerializationProxy(EnumSet<E> enumSet) {
            this.elementType = enumSet.elementType;
            this.elements = (Enum[]) enumSet.toArray(ZERO_LENGTH_ENUM_ARRAY);
        }

        private Object readResolve() {
            EnumSet<E> noneOf = EnumSet.noneOf(this.elementType);
            for (Enum<?> add : this.elements) {
                noneOf.add(add);
            }
            return noneOf;
        }
    }

    /* access modifiers changed from: package-private */
    public Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}
