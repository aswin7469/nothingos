package com.android.systemui.p012qs.tiles.dialog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.connectivity.AccessPointController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.toast.ToastFactory;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.CarrierNameCustomization;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController_Factory */
public final class InternetDialogController_Factory implements Factory<InternetDialogController> {
    private final Provider<AccessPointController> accessPointControllerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
    private final Provider<CarrierNameCustomization> carrierNameCustomizationProvider;
    private final Provider<ConnectivityManager> connectivityManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    private final Provider<GlobalSettings> globalSettingsProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<LocationController> locationControllerProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<ActivityStarter> starterProvider;
    private final Provider<SubscriptionManager> subscriptionManagerProvider;
    private final Provider<TelephonyManager> telephonyManagerProvider;
    private final Provider<ToastFactory> toastFactoryProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<WifiManager> wifiManagerProvider;
    private final Provider<WifiStateWorker> wifiStateWorkerProvider;
    private final Provider<WindowManager> windowManagerProvider;
    private final Provider<Handler> workerHandlerProvider;

    public InternetDialogController_Factory(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<ActivityStarter> provider3, Provider<AccessPointController> provider4, Provider<SubscriptionManager> provider5, Provider<TelephonyManager> provider6, Provider<WifiManager> provider7, Provider<ConnectivityManager> provider8, Provider<Handler> provider9, Provider<Executor> provider10, Provider<BroadcastDispatcher> provider11, Provider<KeyguardUpdateMonitor> provider12, Provider<GlobalSettings> provider13, Provider<KeyguardStateController> provider14, Provider<WindowManager> provider15, Provider<ToastFactory> provider16, Provider<Handler> provider17, Provider<CarrierConfigTracker> provider18, Provider<LocationController> provider19, Provider<DialogLaunchAnimator> provider20, Provider<WifiStateWorker> provider21, Provider<CarrierNameCustomization> provider22) {
        this.contextProvider = provider;
        this.uiEventLoggerProvider = provider2;
        this.starterProvider = provider3;
        this.accessPointControllerProvider = provider4;
        this.subscriptionManagerProvider = provider5;
        this.telephonyManagerProvider = provider6;
        this.wifiManagerProvider = provider7;
        this.connectivityManagerProvider = provider8;
        this.handlerProvider = provider9;
        this.mainExecutorProvider = provider10;
        this.broadcastDispatcherProvider = provider11;
        this.keyguardUpdateMonitorProvider = provider12;
        this.globalSettingsProvider = provider13;
        this.keyguardStateControllerProvider = provider14;
        this.windowManagerProvider = provider15;
        this.toastFactoryProvider = provider16;
        this.workerHandlerProvider = provider17;
        this.carrierConfigTrackerProvider = provider18;
        this.locationControllerProvider = provider19;
        this.dialogLaunchAnimatorProvider = provider20;
        this.wifiStateWorkerProvider = provider21;
        this.carrierNameCustomizationProvider = provider22;
    }

    public InternetDialogController get() {
        return newInstance(this.contextProvider.get(), this.uiEventLoggerProvider.get(), this.starterProvider.get(), this.accessPointControllerProvider.get(), this.subscriptionManagerProvider.get(), this.telephonyManagerProvider.get(), this.wifiManagerProvider.get(), this.connectivityManagerProvider.get(), this.handlerProvider.get(), this.mainExecutorProvider.get(), this.broadcastDispatcherProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.globalSettingsProvider.get(), this.keyguardStateControllerProvider.get(), this.windowManagerProvider.get(), this.toastFactoryProvider.get(), this.workerHandlerProvider.get(), this.carrierConfigTrackerProvider.get(), this.locationControllerProvider.get(), this.dialogLaunchAnimatorProvider.get(), this.wifiStateWorkerProvider.get(), this.carrierNameCustomizationProvider.get());
    }

    public static InternetDialogController_Factory create(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<ActivityStarter> provider3, Provider<AccessPointController> provider4, Provider<SubscriptionManager> provider5, Provider<TelephonyManager> provider6, Provider<WifiManager> provider7, Provider<ConnectivityManager> provider8, Provider<Handler> provider9, Provider<Executor> provider10, Provider<BroadcastDispatcher> provider11, Provider<KeyguardUpdateMonitor> provider12, Provider<GlobalSettings> provider13, Provider<KeyguardStateController> provider14, Provider<WindowManager> provider15, Provider<ToastFactory> provider16, Provider<Handler> provider17, Provider<CarrierConfigTracker> provider18, Provider<LocationController> provider19, Provider<DialogLaunchAnimator> provider20, Provider<WifiStateWorker> provider21, Provider<CarrierNameCustomization> provider22) {
        return new InternetDialogController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22);
    }

    public static InternetDialogController newInstance(Context context, UiEventLogger uiEventLogger, ActivityStarter activityStarter, AccessPointController accessPointController, SubscriptionManager subscriptionManager, TelephonyManager telephonyManager, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Executor executor, BroadcastDispatcher broadcastDispatcher, KeyguardUpdateMonitor keyguardUpdateMonitor, GlobalSettings globalSettings, KeyguardStateController keyguardStateController, WindowManager windowManager, ToastFactory toastFactory, Handler handler2, CarrierConfigTracker carrierConfigTracker, LocationController locationController, DialogLaunchAnimator dialogLaunchAnimator, WifiStateWorker wifiStateWorker, CarrierNameCustomization carrierNameCustomization) {
        return new InternetDialogController(context, uiEventLogger, activityStarter, accessPointController, subscriptionManager, telephonyManager, wifiManager, connectivityManager, handler, executor, broadcastDispatcher, keyguardUpdateMonitor, globalSettings, keyguardStateController, windowManager, toastFactory, handler2, carrierConfigTracker, locationController, dialogLaunchAnimator, wifiStateWorker, carrierNameCustomization);
    }
}
