package java.util;

import java.util.Comparators;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@FunctionalInterface
public interface Comparator<T> {
    int compare(T t, T t2);

    boolean equals(Object obj);

    Comparator<T> reversed() {
        return Collections.reverseOrder(this);
    }

    Comparator<T> thenComparing(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return new Comparator$$ExternalSyntheticLambda1(this, comparator);
    }

    static /* synthetic */ int lambda$thenComparing$36697e65$1(Comparator _this, Comparator comparator, Object obj, Object obj2) {
        int compare = _this.compare(obj, obj2);
        return compare != 0 ? compare : comparator.compare(obj, obj2);
    }

    <U> Comparator<T> thenComparing(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        return thenComparing(comparing(function, comparator));
    }

    <U extends Comparable<? super U>> Comparator<T> thenComparing(Function<? super T, ? extends U> function) {
        return thenComparing(comparing(function));
    }

    Comparator<T> thenComparingInt(ToIntFunction<? super T> toIntFunction) {
        return thenComparing(comparingInt(toIntFunction));
    }

    Comparator<T> thenComparingLong(ToLongFunction<? super T> toLongFunction) {
        return thenComparing(comparingLong(toLongFunction));
    }

    Comparator<T> thenComparingDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        return thenComparing(comparingDouble(toDoubleFunction));
    }

    static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
        return Collections.reverseOrder();
    }

    static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
        return Comparators.NaturalOrderComparator.INSTANCE;
    }

    static <T> Comparator<T> nullsFirst(Comparator<? super T> comparator) {
        return new Comparators.NullComparator(true, comparator);
    }

    static <T> Comparator<T> nullsLast(Comparator<? super T> comparator) {
        return new Comparators.NullComparator(false, comparator);
    }

    static <T, U> Comparator<T> comparing(Function<? super T, ? extends U> function, Comparator<? super U> comparator) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(comparator);
        return new Comparator$$ExternalSyntheticLambda2(comparator, function);
    }

    static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> function) {
        Objects.requireNonNull(function);
        return new Comparator$$ExternalSyntheticLambda5(function);
    }

    static <T> Comparator<T> comparingInt(ToIntFunction<? super T> toIntFunction) {
        Objects.requireNonNull(toIntFunction);
        return new Comparator$$ExternalSyntheticLambda3(toIntFunction);
    }

    static <T> Comparator<T> comparingLong(ToLongFunction<? super T> toLongFunction) {
        Objects.requireNonNull(toLongFunction);
        return new Comparator$$ExternalSyntheticLambda4(toLongFunction);
    }

    static <T> Comparator<T> comparingDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        return new Comparator$$ExternalSyntheticLambda0(toDoubleFunction);
    }
}
