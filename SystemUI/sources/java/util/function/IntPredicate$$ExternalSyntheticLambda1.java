package java.util.function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class IntPredicate$$ExternalSyntheticLambda1 implements IntPredicate {
    public final /* synthetic */ IntPredicate f$0;
    public final /* synthetic */ IntPredicate f$1;

    public /* synthetic */ IntPredicate$$ExternalSyntheticLambda1(IntPredicate intPredicate, IntPredicate intPredicate2) {
        this.f$0 = intPredicate;
        this.f$1 = intPredicate2;
    }

    public final boolean test(int i) {
        return IntPredicate.lambda$or$2(this.f$0, this.f$1, i);
    }
}
