package android.net.wifi;

import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiManager$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Consumer f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ WifiManager$1$$ExternalSyntheticLambda0(Consumer consumer, boolean z) {
        this.f$0 = consumer;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.accept(Boolean.valueOf(this.f$1));
    }
}
