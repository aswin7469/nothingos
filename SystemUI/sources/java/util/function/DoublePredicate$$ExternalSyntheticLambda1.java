package java.util.function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DoublePredicate$$ExternalSyntheticLambda1 implements DoublePredicate {
    public final /* synthetic */ DoublePredicate f$0;
    public final /* synthetic */ DoublePredicate f$1;

    public /* synthetic */ DoublePredicate$$ExternalSyntheticLambda1(DoublePredicate doublePredicate, DoublePredicate doublePredicate2) {
        this.f$0 = doublePredicate;
        this.f$1 = doublePredicate2;
    }

    public final boolean test(double d) {
        return DoublePredicate.lambda$and$0(this.f$0, this.f$1, d);
    }
}
