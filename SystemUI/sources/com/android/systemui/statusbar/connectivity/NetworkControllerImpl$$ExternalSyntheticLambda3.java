package com.android.systemui.statusbar.connectivity;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkControllerImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ WifiSignalController f$0;

    public /* synthetic */ NetworkControllerImpl$$ExternalSyntheticLambda3(WifiSignalController wifiSignalController) {
        this.f$0 = wifiSignalController;
    }

    public final void run() {
        this.f$0.fetchInitialState();
    }
}
