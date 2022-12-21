package android.net.wifi;

import android.net.wifi.WifiManager;
import android.os.connectivity.WifiActivityEnergyInfo;

/* renamed from: android.net.wifi.WifiManager$OnWifiActivityEnergyInfoProxy$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0179x8981bbf8 implements Runnable {
    public final /* synthetic */ WifiManager.OnWifiActivityEnergyInfoListener f$0;
    public final /* synthetic */ WifiActivityEnergyInfo f$1;

    public /* synthetic */ C0179x8981bbf8(WifiManager.OnWifiActivityEnergyInfoListener onWifiActivityEnergyInfoListener, WifiActivityEnergyInfo wifiActivityEnergyInfo) {
        this.f$0 = onWifiActivityEnergyInfoListener;
        this.f$1 = wifiActivityEnergyInfo;
    }

    public final void run() {
        this.f$0.onWifiActivityEnergyInfo(this.f$1);
    }
}
