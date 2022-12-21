package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface Function<T, R> {
    static /* synthetic */ Object lambda$identity$2(Object obj) {
        return obj;
    }

    R apply(T t);

    <V> Function<V, R> compose(Function<? super V, ? extends T> function) {
        Objects.requireNonNull(function);
        return new Function$$ExternalSyntheticLambda2(this, function);
    }

    <V> Function<T, V> andThen(Function<? super R, ? extends V> function) {
        Objects.requireNonNull(function);
        return new Function$$ExternalSyntheticLambda0(this, function);
    }

    static <T> Function<T, T> identity() {
        return new Function$$ExternalSyntheticLambda1();
    }
}
