package java.util.function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BiPredicate$$ExternalSyntheticLambda2 implements BiPredicate {
    public final /* synthetic */ BiPredicate f$0;
    public final /* synthetic */ BiPredicate f$1;

    public /* synthetic */ BiPredicate$$ExternalSyntheticLambda2(BiPredicate biPredicate, BiPredicate biPredicate2) {
        this.f$0 = biPredicate;
        this.f$1 = biPredicate2;
    }

    public final boolean test(Object obj, Object obj2) {
        return BiPredicate.lambda$or$2(this.f$0, this.f$1, obj, obj2);
    }
}
