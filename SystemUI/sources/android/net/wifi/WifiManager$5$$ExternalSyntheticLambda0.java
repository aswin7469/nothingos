package android.net.wifi;

import java.util.Set;
import java.util.function.BiConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiManager$5$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BiConsumer f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ Set f$2;

    public /* synthetic */ WifiManager$5$$ExternalSyntheticLambda0(BiConsumer biConsumer, boolean z, Set set) {
        this.f$0 = biConsumer;
        this.f$1 = z;
        this.f$2 = set;
    }

    public final void run() {
        this.f$0.accept(Boolean.valueOf(this.f$1), this.f$2);
    }
}
