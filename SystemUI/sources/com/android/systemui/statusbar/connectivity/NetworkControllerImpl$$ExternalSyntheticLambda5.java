package com.android.systemui.statusbar.connectivity;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkControllerImpl$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ NetworkControllerImpl f$0;

    public /* synthetic */ NetworkControllerImpl$$ExternalSyntheticLambda5(NetworkControllerImpl networkControllerImpl) {
        this.f$0 = networkControllerImpl;
    }

    public final void run() {
        this.f$0.recalculateEmergency();
    }
}
