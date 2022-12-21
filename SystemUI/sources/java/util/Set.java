package java.util;

import java.util.ImmutableCollections;

public interface Set<E> extends Collection<E> {
    boolean add(E e);

    boolean addAll(Collection<? extends E> collection);

    void clear();

    boolean contains(Object obj);

    boolean containsAll(Collection<?> collection);

    boolean equals(Object obj);

    int hashCode();

    boolean isEmpty();

    Iterator<E> iterator();

    boolean remove(Object obj);

    boolean removeAll(Collection<?> collection);

    boolean retainAll(Collection<?> collection);

    int size();

    Object[] toArray();

    <T> T[] toArray(T[] tArr);

    Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 1);
    }

    /* renamed from: of */
    static <E> Set<E> m1750of() {
        return ImmutableCollections.Set0.instance();
    }

    /* renamed from: of */
    static <E> Set<E> m1751of(E e) {
        return new ImmutableCollections.Set1(e);
    }

    /* renamed from: of */
    static <E> Set<E> m1752of(E e, E e2) {
        return new ImmutableCollections.Set2(e, e2);
    }

    /* renamed from: of */
    static <E> Set<E> m1753of(E e, E e2, E e3) {
        return new ImmutableCollections.SetN(e, e2, e3);
    }

    /* renamed from: of */
    static <E> Set<E> m1754of(E e, E e2, E e3, E e4) {
        return new ImmutableCollections.SetN(e, e2, e3, e4);
    }

    /* renamed from: of */
    static <E> Set<E> m1755of(E e, E e2, E e3, E e4, E e5) {
        return new ImmutableCollections.SetN(e, e2, e3, e4, e5);
    }

    /* renamed from: of */
    static <E> Set<E> m1756of(E e, E e2, E e3, E e4, E e5, E e6) {
        return new ImmutableCollections.SetN(e, e2, e3, e4, e5, e6);
    }

    /* renamed from: of */
    static <E> Set<E> m1757of(E e, E e2, E e3, E e4, E e5, E e6, E e7) {
        return new ImmutableCollections.SetN(e, e2, e3, e4, e5, e6, e7);
    }

    /* renamed from: of */
    static <E> Set<E> m1758of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        return new ImmutableCollections.SetN(e, e2, e3, e4, e5, e6, e7, e8);
    }

    /* renamed from: of */
    static <E> Set<E> m1759of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
        return new ImmutableCollections.SetN(e, e2, e3, e4, e5, e6, e7, e8, e9);
    }

    /* renamed from: of */
    static <E> Set<E> m1760of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
        return new ImmutableCollections.SetN(e, e2, e3, e4, e5, e6, e7, e8, e9, e10);
    }

    @SafeVarargs
    /* renamed from: of */
    static <E> Set<E> m1761of(E... eArr) {
        int length = eArr.length;
        if (length == 0) {
            return ImmutableCollections.Set0.instance();
        }
        if (length == 1) {
            return new ImmutableCollections.Set1(eArr[0]);
        }
        if (length != 2) {
            return new ImmutableCollections.SetN(eArr);
        }
        return new ImmutableCollections.Set2(eArr[0], eArr[1]);
    }

    static <E> Set<E> copyOf(Collection<? extends E> collection) {
        if (collection instanceof ImmutableCollections.AbstractImmutableSet) {
            return (Set) collection;
        }
        return m1761of((E[]) new HashSet(collection).toArray());
    }
}
