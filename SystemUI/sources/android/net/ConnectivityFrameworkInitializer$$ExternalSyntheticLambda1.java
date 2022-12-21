package android.net;

import android.app.SystemServiceRegistry;
import android.content.Context;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ConnectivityFrameworkInitializer$$ExternalSyntheticLambda1 implements SystemServiceRegistry.ContextAwareServiceProducerWithoutBinder {
    public final Object createService(Context context) {
        return ((ConnectivityManager) context.getSystemService(ConnectivityManager.class)).createDiagnosticsManager();
    }
}
