package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface BiConsumer<T, U> {
    void accept(T t, U u);

    BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> biConsumer) {
        Objects.requireNonNull(biConsumer);
        return new BiConsumer$$ExternalSyntheticLambda0(this, biConsumer);
    }

    static /* synthetic */ void lambda$andThen$0(BiConsumer _this, BiConsumer biConsumer, Object obj, Object obj2) {
        _this.accept(obj, obj2);
        biConsumer.accept(obj, obj2);
    }
}
