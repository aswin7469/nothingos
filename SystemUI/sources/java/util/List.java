package java.util;

import java.util.AbstractList;
import java.util.ImmutableCollections;
import java.util.function.UnaryOperator;

public interface List<E> extends Collection<E> {
    void add(int i, E e);

    boolean add(E e);

    boolean addAll(int i, Collection<? extends E> collection);

    boolean addAll(Collection<? extends E> collection);

    void clear();

    boolean contains(Object obj);

    boolean containsAll(Collection<?> collection);

    boolean equals(Object obj);

    E get(int i);

    int hashCode();

    int indexOf(Object obj);

    boolean isEmpty();

    Iterator<E> iterator();

    int lastIndexOf(Object obj);

    ListIterator<E> listIterator();

    ListIterator<E> listIterator(int i);

    E remove(int i);

    boolean remove(Object obj);

    boolean removeAll(Collection<?> collection);

    boolean retainAll(Collection<?> collection);

    E set(int i, E e);

    int size();

    List<E> subList(int i, int i2);

    Object[] toArray();

    <T> T[] toArray(T[] tArr);

    void replaceAll(UnaryOperator<E> unaryOperator) {
        Objects.requireNonNull(unaryOperator);
        ListIterator listIterator = listIterator();
        while (listIterator.hasNext()) {
            listIterator.set(unaryOperator.apply(listIterator.next()));
        }
    }

    void sort(Comparator<? super E> comparator) {
        Object[] array = toArray();
        Arrays.sort(array, comparator);
        ListIterator listIterator = listIterator();
        for (Object obj : array) {
            listIterator.next();
            listIterator.set(obj);
        }
    }

    Spliterator<E> spliterator() {
        if (this instanceof RandomAccess) {
            return new AbstractList.RandomAccessSpliterator(this);
        }
        return Spliterators.spliterator(this, 16);
    }

    /* renamed from: of */
    static <E> List<E> m1728of() {
        return ImmutableCollections.emptyList();
    }

    /* renamed from: of */
    static <E> List<E> m1729of(E e) {
        return new ImmutableCollections.List12(e);
    }

    /* renamed from: of */
    static <E> List<E> m1730of(E e, E e2) {
        return new ImmutableCollections.List12(e, e2);
    }

    /* renamed from: of */
    static <E> List<E> m1731of(E e, E e2, E e3) {
        return new ImmutableCollections.ListN(e, e2, e3);
    }

    /* renamed from: of */
    static <E> List<E> m1732of(E e, E e2, E e3, E e4) {
        return new ImmutableCollections.ListN(e, e2, e3, e4);
    }

    /* renamed from: of */
    static <E> List<E> m1733of(E e, E e2, E e3, E e4, E e5) {
        return new ImmutableCollections.ListN(e, e2, e3, e4, e5);
    }

    /* renamed from: of */
    static <E> List<E> m1734of(E e, E e2, E e3, E e4, E e5, E e6) {
        return new ImmutableCollections.ListN(e, e2, e3, e4, e5, e6);
    }

    /* renamed from: of */
    static <E> List<E> m1735of(E e, E e2, E e3, E e4, E e5, E e6, E e7) {
        return new ImmutableCollections.ListN(e, e2, e3, e4, e5, e6, e7);
    }

    /* renamed from: of */
    static <E> List<E> m1736of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        return new ImmutableCollections.ListN(e, e2, e3, e4, e5, e6, e7, e8);
    }

    /* renamed from: of */
    static <E> List<E> m1737of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
        return new ImmutableCollections.ListN(e, e2, e3, e4, e5, e6, e7, e8, e9);
    }

    /* renamed from: of */
    static <E> List<E> m1738of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
        return new ImmutableCollections.ListN(e, e2, e3, e4, e5, e6, e7, e8, e9, e10);
    }

    @SafeVarargs
    /* renamed from: of */
    static <E> List<E> m1739of(E... eArr) {
        int length = eArr.length;
        if (length == 0) {
            return ImmutableCollections.emptyList();
        }
        if (length == 1) {
            return new ImmutableCollections.List12(eArr[0]);
        }
        if (length != 2) {
            return new ImmutableCollections.ListN(eArr);
        }
        return new ImmutableCollections.List12(eArr[0], eArr[1]);
    }

    static <E> List<E> copyOf(Collection<? extends E> collection) {
        return ImmutableCollections.listCopy(collection);
    }
}
