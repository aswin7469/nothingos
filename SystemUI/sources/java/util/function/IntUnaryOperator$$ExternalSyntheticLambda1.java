package java.util.function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class IntUnaryOperator$$ExternalSyntheticLambda1 implements IntUnaryOperator {
    public final /* synthetic */ IntUnaryOperator f$0;
    public final /* synthetic */ IntUnaryOperator f$1;

    public /* synthetic */ IntUnaryOperator$$ExternalSyntheticLambda1(IntUnaryOperator intUnaryOperator, IntUnaryOperator intUnaryOperator2) {
        this.f$0 = intUnaryOperator;
        this.f$1 = intUnaryOperator2;
    }

    public final int applyAsInt(int i) {
        return this.f$1.applyAsInt(this.f$0.applyAsInt(i));
    }
}