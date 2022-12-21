package java.util.function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LongPredicate$$ExternalSyntheticLambda0 implements LongPredicate {
    public final /* synthetic */ LongPredicate f$0;
    public final /* synthetic */ LongPredicate f$1;

    public /* synthetic */ LongPredicate$$ExternalSyntheticLambda0(LongPredicate longPredicate, LongPredicate longPredicate2) {
        this.f$0 = longPredicate;
        this.f$1 = longPredicate2;
    }

    public final boolean test(long j) {
        return LongPredicate.lambda$or$2(this.f$0, this.f$1, j);
    }
}
