package java.util.function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DoubleUnaryOperator$$ExternalSyntheticLambda0 implements DoubleUnaryOperator {
    public final /* synthetic */ DoubleUnaryOperator f$0;
    public final /* synthetic */ DoubleUnaryOperator f$1;

    public /* synthetic */ DoubleUnaryOperator$$ExternalSyntheticLambda0(DoubleUnaryOperator doubleUnaryOperator, DoubleUnaryOperator doubleUnaryOperator2) {
        this.f$0 = doubleUnaryOperator;
        this.f$1 = doubleUnaryOperator2;
    }

    public final double applyAsDouble(double d) {
        return this.f$0.applyAsDouble(this.f$1.applyAsDouble(d));
    }
}
