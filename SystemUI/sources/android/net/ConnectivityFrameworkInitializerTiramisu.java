package android.net;

import android.annotation.SystemApi;
import android.app.SystemServiceRegistry;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.IEthernetManager;
import android.net.IIpSecService;
import android.net.INetworkStatsService;
import android.net.connectivity.android.net.mdns.aidl.IMDns;
import android.net.nsd.INsdManager;
import android.net.nsd.MDnsManager;
import android.net.nsd.NsdManager;
import android.os.IBinder;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class ConnectivityFrameworkInitializerTiramisu {
    private ConnectivityFrameworkInitializerTiramisu() {
    }

    public static void registerServiceWrappers() {
        SystemServiceRegistry.registerContextAwareService("servicediscovery", NsdManager.class, new C0037xf13d088c());
        SystemServiceRegistry.registerContextAwareService("ipsec", IpSecManager.class, new C0038xf13d088d());
        SystemServiceRegistry.registerContextAwareService("netstats", NetworkStatsManager.class, new C0039xf13d088e());
        SystemServiceRegistry.registerContextAwareService("ethernet", EthernetManager.class, new C0040xf13d088f());
        SystemServiceRegistry.registerStaticService(MDnsManager.MDNS_SERVICE, MDnsManager.class, new C0041xf13d0890());
    }

    static /* synthetic */ NsdManager lambda$registerServiceWrappers$0(Context context, IBinder iBinder) {
        return new NsdManager(context, INsdManager.Stub.asInterface(iBinder));
    }

    static /* synthetic */ IpSecManager lambda$registerServiceWrappers$1(Context context, IBinder iBinder) {
        return new IpSecManager(context, IIpSecService.Stub.asInterface(iBinder));
    }

    static /* synthetic */ NetworkStatsManager lambda$registerServiceWrappers$2(Context context, IBinder iBinder) {
        return new NetworkStatsManager(context, INetworkStatsService.Stub.asInterface(iBinder));
    }

    static /* synthetic */ EthernetManager lambda$registerServiceWrappers$3(Context context, IBinder iBinder) {
        return new EthernetManager(context, IEthernetManager.Stub.asInterface(iBinder));
    }

    static /* synthetic */ MDnsManager lambda$registerServiceWrappers$4(IBinder iBinder) {
        return new MDnsManager(IMDns.Stub.asInterface(iBinder));
    }
}
