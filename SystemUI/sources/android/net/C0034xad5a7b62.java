package android.net;

import android.net.ConnectivityDiagnosticsManager;

/* renamed from: android.net.ConnectivityDiagnosticsManager$ConnectivityDiagnosticsBinder$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0034xad5a7b62 implements Runnable {
    public final /* synthetic */ ConnectivityDiagnosticsManager.ConnectivityDiagnosticsBinder f$0;
    public final /* synthetic */ Network f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ C0034xad5a7b62(ConnectivityDiagnosticsManager.ConnectivityDiagnosticsBinder connectivityDiagnosticsBinder, Network network, boolean z) {
        this.f$0 = connectivityDiagnosticsBinder;
        this.f$1 = network;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.mo1869x912b327b(this.f$1, this.f$2);
    }
}
