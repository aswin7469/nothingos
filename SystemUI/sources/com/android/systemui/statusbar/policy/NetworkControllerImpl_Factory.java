package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.CarrierConfigTracker;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class NetworkControllerImpl_Factory implements Factory<NetworkControllerImpl> {
    private final Provider<AccessPointControllerImpl> accessPointControllerProvider;
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<Looper> bgLooperProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<CallbackHandler> callbackHandlerProvider;
    private final Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
    private final Provider<ConnectivityManager> connectivityManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DemoModeController> demoModeControllerProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<InternetDialogFactory> internetDialogFactoryProvider;
    private final Provider<NetworkScoreManager> networkScoreManagerProvider;
    private final Provider<SubscriptionManager> subscriptionManagerProvider;
    private final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    private final Provider<TelephonyManager> telephonyManagerProvider;
    private final Provider<WifiManager> wifiManagerProvider;

    public NetworkControllerImpl_Factory(Provider<Context> provider, Provider<Looper> provider2, Provider<Executor> provider3, Provider<SubscriptionManager> provider4, Provider<CallbackHandler> provider5, Provider<DeviceProvisionedController> provider6, Provider<BroadcastDispatcher> provider7, Provider<ConnectivityManager> provider8, Provider<TelephonyManager> provider9, Provider<TelephonyListenerManager> provider10, Provider<WifiManager> provider11, Provider<NetworkScoreManager> provider12, Provider<AccessPointControllerImpl> provider13, Provider<DemoModeController> provider14, Provider<CarrierConfigTracker> provider15, Provider<FeatureFlags> provider16, Provider<DumpManager> provider17, Provider<Handler> provider18, Provider<InternetDialogFactory> provider19) {
        this.contextProvider = provider;
        this.bgLooperProvider = provider2;
        this.bgExecutorProvider = provider3;
        this.subscriptionManagerProvider = provider4;
        this.callbackHandlerProvider = provider5;
        this.deviceProvisionedControllerProvider = provider6;
        this.broadcastDispatcherProvider = provider7;
        this.connectivityManagerProvider = provider8;
        this.telephonyManagerProvider = provider9;
        this.telephonyListenerManagerProvider = provider10;
        this.wifiManagerProvider = provider11;
        this.networkScoreManagerProvider = provider12;
        this.accessPointControllerProvider = provider13;
        this.demoModeControllerProvider = provider14;
        this.carrierConfigTrackerProvider = provider15;
        this.featureFlagsProvider = provider16;
        this.dumpManagerProvider = provider17;
        this.handlerProvider = provider18;
        this.internetDialogFactoryProvider = provider19;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public NetworkControllerImpl mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.bgLooperProvider.mo1933get(), this.bgExecutorProvider.mo1933get(), this.subscriptionManagerProvider.mo1933get(), this.callbackHandlerProvider.mo1933get(), this.deviceProvisionedControllerProvider.mo1933get(), this.broadcastDispatcherProvider.mo1933get(), this.connectivityManagerProvider.mo1933get(), this.telephonyManagerProvider.mo1933get(), this.telephonyListenerManagerProvider.mo1933get(), this.wifiManagerProvider.mo1933get(), this.networkScoreManagerProvider.mo1933get(), this.accessPointControllerProvider.mo1933get(), this.demoModeControllerProvider.mo1933get(), this.carrierConfigTrackerProvider.mo1933get(), this.featureFlagsProvider.mo1933get(), this.dumpManagerProvider.mo1933get(), this.handlerProvider.mo1933get(), this.internetDialogFactoryProvider.mo1933get());
    }

    public static NetworkControllerImpl_Factory create(Provider<Context> provider, Provider<Looper> provider2, Provider<Executor> provider3, Provider<SubscriptionManager> provider4, Provider<CallbackHandler> provider5, Provider<DeviceProvisionedController> provider6, Provider<BroadcastDispatcher> provider7, Provider<ConnectivityManager> provider8, Provider<TelephonyManager> provider9, Provider<TelephonyListenerManager> provider10, Provider<WifiManager> provider11, Provider<NetworkScoreManager> provider12, Provider<AccessPointControllerImpl> provider13, Provider<DemoModeController> provider14, Provider<CarrierConfigTracker> provider15, Provider<FeatureFlags> provider16, Provider<DumpManager> provider17, Provider<Handler> provider18, Provider<InternetDialogFactory> provider19) {
        return new NetworkControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19);
    }

    public static NetworkControllerImpl newInstance(Context context, Looper looper, Executor executor, SubscriptionManager subscriptionManager, CallbackHandler callbackHandler, DeviceProvisionedController deviceProvisionedController, BroadcastDispatcher broadcastDispatcher, ConnectivityManager connectivityManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WifiManager wifiManager, NetworkScoreManager networkScoreManager, AccessPointControllerImpl accessPointControllerImpl, DemoModeController demoModeController, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags, DumpManager dumpManager, Handler handler, InternetDialogFactory internetDialogFactory) {
        return new NetworkControllerImpl(context, looper, executor, subscriptionManager, callbackHandler, deviceProvisionedController, broadcastDispatcher, connectivityManager, telephonyManager, telephonyListenerManager, wifiManager, networkScoreManager, accessPointControllerImpl, demoModeController, carrierConfigTracker, featureFlags, dumpManager, handler, internetDialogFactory);
    }
}
