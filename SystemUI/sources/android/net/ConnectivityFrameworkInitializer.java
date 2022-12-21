package android.net;

import android.annotation.SystemApi;
import android.app.SystemServiceRegistry;
import android.content.Context;
import android.net.IConnectivityManager;
import android.os.IBinder;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class ConnectivityFrameworkInitializer {
    private ConnectivityFrameworkInitializer() {
    }

    public static void registerServiceWrappers() {
        SystemServiceRegistry.registerContextAwareService("connectivity", ConnectivityManager.class, new ConnectivityFrameworkInitializer$$ExternalSyntheticLambda0());
        SystemServiceRegistry.registerContextAwareService("connectivity_diagnostics", ConnectivityDiagnosticsManager.class, new ConnectivityFrameworkInitializer$$ExternalSyntheticLambda1());
        SystemServiceRegistry.registerContextAwareService("test_network", TestNetworkManager.class, new ConnectivityFrameworkInitializer$$ExternalSyntheticLambda2());
        SystemServiceRegistry.registerContextAwareService(DnsResolverServiceManager.DNS_RESOLVER_SERVICE, DnsResolverServiceManager.class, new ConnectivityFrameworkInitializer$$ExternalSyntheticLambda3());
    }

    static /* synthetic */ ConnectivityManager lambda$registerServiceWrappers$0(Context context, IBinder iBinder) {
        return new ConnectivityManager(context, IConnectivityManager.Stub.asInterface(iBinder));
    }

    static /* synthetic */ DnsResolverServiceManager lambda$registerServiceWrappers$3(Context context, IBinder iBinder) {
        return new DnsResolverServiceManager(iBinder);
    }
}
