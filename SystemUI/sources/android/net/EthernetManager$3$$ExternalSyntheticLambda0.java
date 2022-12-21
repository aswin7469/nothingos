package android.net;

import java.util.function.IntConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class EthernetManager$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ IntConsumer f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ EthernetManager$3$$ExternalSyntheticLambda0(IntConsumer intConsumer, int i) {
        this.f$0 = intConsumer;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.accept(this.f$1);
    }
}
