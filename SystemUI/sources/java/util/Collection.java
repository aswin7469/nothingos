package java.util;

import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Collection<E> extends Iterable<E> {
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

    <T> T[] toArray(IntFunction<T[]> intFunction) {
        return toArray((T[]) (Object[]) intFunction.apply(0));
    }

    boolean removeIf(Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        Iterator it = iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (predicate.test(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 0);
    }

    Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
