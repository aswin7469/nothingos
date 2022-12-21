package java.util.function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Consumer$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ Consumer f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ Consumer$$ExternalSyntheticLambda0(Consumer consumer, Consumer consumer2) {
        this.f$0 = consumer;
        this.f$1 = consumer2;
    }

    public final void accept(Object obj) {
        Consumer.lambda$andThen$0(this.f$0, this.f$1, obj);
    }
}
