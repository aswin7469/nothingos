package java.util.function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BiFunction$$ExternalSyntheticLambda0 implements BiFunction {
    public final /* synthetic */ BiFunction f$0;
    public final /* synthetic */ Function f$1;

    public /* synthetic */ BiFunction$$ExternalSyntheticLambda0(BiFunction biFunction, Function function) {
        this.f$0 = biFunction;
        this.f$1 = function;
    }

    public final Object apply(Object obj, Object obj2) {
        return this.f$1.apply(this.f$0.apply(obj, obj2));
    }
}
