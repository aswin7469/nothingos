package java.util.function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LongUnaryOperator$$ExternalSyntheticLambda1 implements LongUnaryOperator {
    public final /* synthetic */ LongUnaryOperator f$0;
    public final /* synthetic */ LongUnaryOperator f$1;

    public /* synthetic */ LongUnaryOperator$$ExternalSyntheticLambda1(LongUnaryOperator longUnaryOperator, LongUnaryOperator longUnaryOperator2) {
        this.f$0 = longUnaryOperator;
        this.f$1 = longUnaryOperator2;
    }

    public final long applyAsLong(long j) {
        return this.f$1.applyAsLong(this.f$0.applyAsLong(j));
    }
}
