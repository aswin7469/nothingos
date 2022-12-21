package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface IntConsumer {
    void accept(int i);

    IntConsumer andThen(IntConsumer intConsumer) {
        Objects.requireNonNull(intConsumer);
        return new IntConsumer$$ExternalSyntheticLambda0(this, intConsumer);
    }

    static /* synthetic */ void lambda$andThen$0(IntConsumer _this, IntConsumer intConsumer, int i) {
        _this.accept(i);
        intConsumer.accept(i);
    }
}
