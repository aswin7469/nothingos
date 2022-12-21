package android.net.wifi;

import android.net.wifi.WifiScanner;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiScanner$ServiceHandler$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WifiScanner.ActionListener f$0;

    public /* synthetic */ WifiScanner$ServiceHandler$$ExternalSyntheticLambda0(WifiScanner.ActionListener actionListener) {
        this.f$0 = actionListener;
    }

    public final void run() {
        this.f$0.onSuccess();
    }
}
