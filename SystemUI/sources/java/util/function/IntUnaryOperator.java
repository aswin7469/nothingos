package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface IntUnaryOperator {
    static /* synthetic */ int lambda$identity$2(int i) {
        return i;
    }

    int applyAsInt(int i);

    IntUnaryOperator compose(IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        return new IntUnaryOperator$$ExternalSyntheticLambda0(this, intUnaryOperator);
    }

    IntUnaryOperator andThen(IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        return new IntUnaryOperator$$ExternalSyntheticLambda1(this, intUnaryOperator);
    }

    static IntUnaryOperator identity() {
        return new IntUnaryOperator$$ExternalSyntheticLambda2();
    }
}
