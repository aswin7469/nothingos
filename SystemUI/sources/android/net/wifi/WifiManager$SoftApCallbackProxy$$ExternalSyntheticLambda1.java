package android.net.wifi;

import android.net.wifi.WifiManager;
import java.util.Map;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiManager$SoftApCallbackProxy$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WifiManager.SoftApCallbackProxy f$0;
    public final /* synthetic */ SoftApInfo f$1;
    public final /* synthetic */ Map f$2;

    public /* synthetic */ WifiManager$SoftApCallbackProxy$$ExternalSyntheticLambda1(WifiManager.SoftApCallbackProxy softApCallbackProxy, SoftApInfo softApInfo, Map map) {
        this.f$0 = softApCallbackProxy;
        this.f$1 = softApInfo;
        this.f$2 = map;
    }

    public final void run() {
        this.f$0.mo5101x55fb2da9(this.f$1, this.f$2);
    }
}
