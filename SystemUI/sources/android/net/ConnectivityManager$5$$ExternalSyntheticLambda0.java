package android.net;

import android.net.ConnectivityManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ConnectivityManager$5$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ConnectivityManager.OnTetheringEntitlementResultListener f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ConnectivityManager$5$$ExternalSyntheticLambda0(ConnectivityManager.OnTetheringEntitlementResultListener onTetheringEntitlementResultListener, int i) {
        this.f$0 = onTetheringEntitlementResultListener;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onTetheringEntitlementResult(this.f$1);
    }
}
