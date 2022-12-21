package android.net.wifi;

import android.net.wifi.WifiManager;

/* renamed from: android.net.wifi.WifiManager$SubsystemRestartTrackingCallback$SubsystemRestartCallbackProxy$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0186x7e50bc9d implements Runnable {
    public final /* synthetic */ WifiManager.SubsystemRestartTrackingCallback f$0;

    public /* synthetic */ C0186x7e50bc9d(WifiManager.SubsystemRestartTrackingCallback subsystemRestartTrackingCallback) {
        this.f$0 = subsystemRestartTrackingCallback;
    }

    public final void run() {
        this.f$0.onSubsystemRestarting();
    }
}
