package android.net.wifi;

import android.net.wifi.WifiManager;

/* renamed from: android.net.wifi.WifiManager$SubsystemRestartTrackingCallback$SubsystemRestartCallbackProxy$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0185x7e50bc9c implements Runnable {
    public final /* synthetic */ WifiManager.SubsystemRestartTrackingCallback f$0;

    public /* synthetic */ C0185x7e50bc9c(WifiManager.SubsystemRestartTrackingCallback subsystemRestartTrackingCallback) {
        this.f$0 = subsystemRestartTrackingCallback;
    }

    public final void run() {
        this.f$0.onSubsystemRestarted();
    }
}
