package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface DoubleUnaryOperator {
    static /* synthetic */ double lambda$identity$2(double d) {
        return d;
    }

    double applyAsDouble(double d);

    DoubleUnaryOperator compose(DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        return new DoubleUnaryOperator$$ExternalSyntheticLambda0(this, doubleUnaryOperator);
    }

    DoubleUnaryOperator andThen(DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        return new DoubleUnaryOperator$$ExternalSyntheticLambda1(this, doubleUnaryOperator);
    }

    static DoubleUnaryOperator identity() {
        return new DoubleUnaryOperator$$ExternalSyntheticLambda2();
    }
}
