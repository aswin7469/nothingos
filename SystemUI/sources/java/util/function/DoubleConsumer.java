package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface DoubleConsumer {
    void accept(double d);

    DoubleConsumer andThen(DoubleConsumer doubleConsumer) {
        Objects.requireNonNull(doubleConsumer);
        return new DoubleConsumer$$ExternalSyntheticLambda0(this, doubleConsumer);
    }

    static /* synthetic */ void lambda$andThen$0(DoubleConsumer _this, DoubleConsumer doubleConsumer, double d) {
        _this.accept(d);
        doubleConsumer.accept(d);
    }
}
