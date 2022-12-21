package android.net.wifi;

import android.net.wifi.WifiManager;
import java.util.List;

/* renamed from: android.net.wifi.WifiManager$PnoScanResultsCallbackProxy$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0183xef8a1ac7 implements Runnable {
    public final /* synthetic */ WifiManager.PnoScanResultsCallbackProxy f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ C0183xef8a1ac7(WifiManager.PnoScanResultsCallbackProxy pnoScanResultsCallbackProxy, List list) {
        this.f$0 = pnoScanResultsCallbackProxy;
        this.f$1 = list;
    }

    public final void run() {
        this.f$0.mo5074x532c20d7(this.f$1);
    }
}
