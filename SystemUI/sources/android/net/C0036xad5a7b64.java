package android.net;

import android.net.ConnectivityDiagnosticsManager;

/* renamed from: android.net.ConnectivityDiagnosticsManager$ConnectivityDiagnosticsBinder$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0036xad5a7b64 implements Runnable {
    public final /* synthetic */ ConnectivityDiagnosticsManager.ConnectivityDiagnosticsBinder f$0;
    public final /* synthetic */ ConnectivityDiagnosticsManager.DataStallReport f$1;

    public /* synthetic */ C0036xad5a7b64(ConnectivityDiagnosticsManager.ConnectivityDiagnosticsBinder connectivityDiagnosticsBinder, ConnectivityDiagnosticsManager.DataStallReport dataStallReport) {
        this.f$0 = connectivityDiagnosticsBinder;
        this.f$1 = dataStallReport;
    }

    public final void run() {
        this.f$0.mo1868x89b2b17c(this.f$1);
    }
}
