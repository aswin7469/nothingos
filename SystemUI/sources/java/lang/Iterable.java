package java.lang;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public interface Iterable<T> {
    Iterator<T> iterator();

    void forEach(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        for (Object accept : this) {
            consumer.accept(accept);
        }
    }

    Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
