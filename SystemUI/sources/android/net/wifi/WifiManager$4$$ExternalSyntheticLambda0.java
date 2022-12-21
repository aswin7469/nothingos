package android.net.wifi;

import java.util.function.BiConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiManager$4$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BiConsumer f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ WifiManager$4$$ExternalSyntheticLambda0(BiConsumer biConsumer, String str, boolean z) {
        this.f$0 = biConsumer;
        this.f$1 = str;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.accept(this.f$1, Boolean.valueOf(this.f$2));
    }
}
