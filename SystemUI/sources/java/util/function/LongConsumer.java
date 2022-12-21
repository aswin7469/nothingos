package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface LongConsumer {
    void accept(long j);

    LongConsumer andThen(LongConsumer longConsumer) {
        Objects.requireNonNull(longConsumer);
        return new LongConsumer$$ExternalSyntheticLambda0(this, longConsumer);
    }

    static /* synthetic */ void lambda$andThen$0(LongConsumer _this, LongConsumer longConsumer, long j) {
        _this.accept(j);
        longConsumer.accept(j);
    }
}
