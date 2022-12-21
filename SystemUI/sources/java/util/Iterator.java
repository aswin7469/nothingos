package java.util;

import java.util.function.Consumer;

public interface Iterator<E> {
    boolean hasNext();

    E next();

    void remove() {
        throw new UnsupportedOperationException("remove");
    }

    void forEachRemaining(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        while (hasNext()) {
            consumer.accept(next());
        }
    }
}
