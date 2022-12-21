package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface BiFunction<T, U, R> {
    R apply(T t, U u);

    <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> function) {
        Objects.requireNonNull(function);
        return new BiFunction$$ExternalSyntheticLambda0(this, function);
    }
}
