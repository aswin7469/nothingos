package android.net;

import android.net.ConnectivityDiagnosticsManager;

/* renamed from: android.net.ConnectivityDiagnosticsManager$ConnectivityDiagnosticsBinder$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0035xad5a7b63 implements Runnable {
    public final /* synthetic */ ConnectivityDiagnosticsManager.ConnectivityDiagnosticsBinder f$0;
    public final /* synthetic */ ConnectivityDiagnosticsManager.ConnectivityReport f$1;

    public /* synthetic */ C0035xad5a7b63(ConnectivityDiagnosticsManager.ConnectivityDiagnosticsBinder connectivityDiagnosticsBinder, ConnectivityDiagnosticsManager.ConnectivityReport connectivityReport) {
        this.f$0 = connectivityDiagnosticsBinder;
        this.f$1 = connectivityReport;
    }

    public final void run() {
        this.f$0.mo1867xe6f06637(this.f$1);
    }
}
