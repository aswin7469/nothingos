package android.net.wifi;

import android.net.wifi.WifiScanner;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiScanner$ServiceHandler$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WifiScanner.ActionListener f$0;
    public final /* synthetic */ WifiScanner.OperationResult f$1;

    public /* synthetic */ WifiScanner$ServiceHandler$$ExternalSyntheticLambda1(WifiScanner.ActionListener actionListener, WifiScanner.OperationResult operationResult) {
        this.f$0 = actionListener;
        this.f$1 = operationResult;
    }

    public final void run() {
        this.f$0.onFailure(this.f$1.reason, this.f$1.description);
    }
}
