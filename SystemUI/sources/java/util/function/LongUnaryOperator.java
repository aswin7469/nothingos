package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface LongUnaryOperator {
    static /* synthetic */ long lambda$identity$2(long j) {
        return j;
    }

    long applyAsLong(long j);

    LongUnaryOperator compose(LongUnaryOperator longUnaryOperator) {
        Objects.requireNonNull(longUnaryOperator);
        return new LongUnaryOperator$$ExternalSyntheticLambda2(this, longUnaryOperator);
    }

    LongUnaryOperator andThen(LongUnaryOperator longUnaryOperator) {
        Objects.requireNonNull(longUnaryOperator);
        return new LongUnaryOperator$$ExternalSyntheticLambda1(this, longUnaryOperator);
    }

    static LongUnaryOperator identity() {
        return new LongUnaryOperator$$ExternalSyntheticLambda0();
    }
}
