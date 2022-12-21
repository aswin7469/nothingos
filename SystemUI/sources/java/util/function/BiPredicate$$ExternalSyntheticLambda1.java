package java.util.function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BiPredicate$$ExternalSyntheticLambda1 implements BiPredicate {
    public final /* synthetic */ BiPredicate f$0;

    public /* synthetic */ BiPredicate$$ExternalSyntheticLambda1(BiPredicate biPredicate) {
        this.f$0 = biPredicate;
    }

    public final boolean test(Object obj, Object obj2) {
        return BiPredicate.lambda$negate$1(this.f$0, obj, obj2);
    }
}
