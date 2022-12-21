package android.net.wifi;

import android.annotation.SystemApi;
import android.app.SystemServiceRegistry;
import android.content.Context;
import android.net.wifi.IWifiManager;
import android.net.wifi.IWifiScanner;
import android.net.wifi.aware.IWifiAwareManager;
import android.net.wifi.aware.WifiAwareManager;
import android.net.wifi.p2p.IWifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.rtt.IWifiRttManager;
import android.net.wifi.rtt.WifiRttManager;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;

@SystemApi
public class WifiFrameworkInitializer {

    private static class NoPreloadHolder {
        /* access modifiers changed from: private */
        public static final HandlerThread INSTANCE = createInstance();

        private NoPreloadHolder() {
        }

        private static HandlerThread createInstance() {
            HandlerThread handlerThread = new HandlerThread("WifiManagerThread");
            handlerThread.start();
            return handlerThread;
        }
    }

    public static Looper getInstanceLooper() {
        return NoPreloadHolder.INSTANCE.getLooper();
    }

    private WifiFrameworkInitializer() {
    }

    public static void registerServiceWrappers() {
        SystemServiceRegistry.registerContextAwareService("wifi", WifiManager.class, new WifiFrameworkInitializer$$ExternalSyntheticLambda0());
        SystemServiceRegistry.registerContextAwareService("wifip2p", WifiP2pManager.class, new WifiFrameworkInitializer$$ExternalSyntheticLambda1());
        SystemServiceRegistry.registerContextAwareService("wifiaware", WifiAwareManager.class, new WifiFrameworkInitializer$$ExternalSyntheticLambda2());
        SystemServiceRegistry.registerContextAwareService("wifiscanner", WifiScanner.class, new WifiFrameworkInitializer$$ExternalSyntheticLambda3());
        SystemServiceRegistry.registerContextAwareService("wifirtt", WifiRttManager.class, new WifiFrameworkInitializer$$ExternalSyntheticLambda4());
        SystemServiceRegistry.registerContextAwareService("rttmanager", RttManager.class, new WifiFrameworkInitializer$$ExternalSyntheticLambda5());
    }

    static /* synthetic */ WifiManager lambda$registerServiceWrappers$0(Context context, IBinder iBinder) {
        if (!context.getPackageManager().hasSystemFeature("android.hardware.wifi")) {
            return null;
        }
        return new WifiManager(context, IWifiManager.Stub.asInterface(iBinder), getInstanceLooper());
    }

    static /* synthetic */ WifiP2pManager lambda$registerServiceWrappers$1(Context context, IBinder iBinder) {
        if (!context.getPackageManager().hasSystemFeature("android.hardware.wifi.direct")) {
            return null;
        }
        return new WifiP2pManager(IWifiP2pManager.Stub.asInterface(iBinder));
    }

    static /* synthetic */ WifiAwareManager lambda$registerServiceWrappers$2(Context context, IBinder iBinder) {
        if (!context.getPackageManager().hasSystemFeature("android.hardware.wifi.aware")) {
            return null;
        }
        return new WifiAwareManager(context, IWifiAwareManager.Stub.asInterface(iBinder));
    }

    static /* synthetic */ WifiScanner lambda$registerServiceWrappers$3(Context context, IBinder iBinder) {
        if (!context.getPackageManager().hasSystemFeature("android.hardware.wifi")) {
            return null;
        }
        return new WifiScanner(context, IWifiScanner.Stub.asInterface(iBinder), getInstanceLooper());
    }

    static /* synthetic */ WifiRttManager lambda$registerServiceWrappers$4(Context context, IBinder iBinder) {
        if (!context.getPackageManager().hasSystemFeature("android.hardware.wifi.rtt")) {
            return null;
        }
        return new WifiRttManager(context, IWifiRttManager.Stub.asInterface(iBinder));
    }

    static /* synthetic */ RttManager lambda$registerServiceWrappers$5(Context context) {
        if (!context.getPackageManager().hasSystemFeature("android.hardware.wifi.rtt")) {
            return null;
        }
        return new RttManager(context, (WifiRttManager) context.getSystemService(WifiRttManager.class));
    }
}
