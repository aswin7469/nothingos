package java.util.function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Function$$ExternalSyntheticLambda0 implements Function {
    public final /* synthetic */ Function f$0;
    public final /* synthetic */ Function f$1;

    public /* synthetic */ Function$$ExternalSyntheticLambda0(Function function, Function function2) {
        this.f$0 = function;
        this.f$1 = function2;
    }

    public final Object apply(Object obj) {
        return this.f$1.apply(this.f$0.apply(obj));
    }
}
