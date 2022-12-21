package android.net.wifi;

import android.net.wifi.WifiManager;
import java.util.List;

/* renamed from: android.net.wifi.WifiManager$CoexCallback$CoexCallbackProxy$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0165x6ae08e5 implements Runnable {
    public final /* synthetic */ WifiManager.CoexCallback f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ C0165x6ae08e5(WifiManager.CoexCallback coexCallback, List list, int i) {
        this.f$0 = coexCallback;
        this.f$1 = list;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.onCoexUnsafeChannelsChanged(this.f$1, this.f$2);
    }
}
