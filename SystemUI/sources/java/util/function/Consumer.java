package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);

    Consumer<T> andThen(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        return new Consumer$$ExternalSyntheticLambda0(this, consumer);
    }

    static /* synthetic */ void lambda$andThen$0(Consumer _this, Consumer consumer, Object obj) {
        _this.accept(obj);
        consumer.accept(obj);
    }
}
