package com.android.systemui.wallet.ui;

import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WalletActivity_Factory implements Factory<WalletActivity> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Executor> executorProvider;
    private final Provider<FalsingCollector> falsingCollectorProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<StatusBarKeyguardViewManager> keyguardViewManagerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public WalletActivity_Factory(Provider<KeyguardStateController> provider, Provider<KeyguardDismissUtil> provider2, Provider<ActivityStarter> provider3, Provider<Executor> provider4, Provider<Handler> provider5, Provider<FalsingManager> provider6, Provider<FalsingCollector> provider7, Provider<UserTracker> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<StatusBarKeyguardViewManager> provider10, Provider<UiEventLogger> provider11) {
        this.keyguardStateControllerProvider = provider;
        this.keyguardDismissUtilProvider = provider2;
        this.activityStarterProvider = provider3;
        this.executorProvider = provider4;
        this.handlerProvider = provider5;
        this.falsingManagerProvider = provider6;
        this.falsingCollectorProvider = provider7;
        this.userTrackerProvider = provider8;
        this.keyguardUpdateMonitorProvider = provider9;
        this.keyguardViewManagerProvider = provider10;
        this.uiEventLoggerProvider = provider11;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public WalletActivity mo1933get() {
        return newInstance(this.keyguardStateControllerProvider.mo1933get(), this.keyguardDismissUtilProvider.mo1933get(), this.activityStarterProvider.mo1933get(), this.executorProvider.mo1933get(), this.handlerProvider.mo1933get(), this.falsingManagerProvider.mo1933get(), this.falsingCollectorProvider.mo1933get(), this.userTrackerProvider.mo1933get(), this.keyguardUpdateMonitorProvider.mo1933get(), this.keyguardViewManagerProvider.mo1933get(), this.uiEventLoggerProvider.mo1933get());
    }

    public static WalletActivity_Factory create(Provider<KeyguardStateController> provider, Provider<KeyguardDismissUtil> provider2, Provider<ActivityStarter> provider3, Provider<Executor> provider4, Provider<Handler> provider5, Provider<FalsingManager> provider6, Provider<FalsingCollector> provider7, Provider<UserTracker> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<StatusBarKeyguardViewManager> provider10, Provider<UiEventLogger> provider11) {
        return new WalletActivity_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static WalletActivity newInstance(KeyguardStateController keyguardStateController, KeyguardDismissUtil keyguardDismissUtil, ActivityStarter activityStarter, Executor executor, Handler handler, FalsingManager falsingManager, FalsingCollector falsingCollector, UserTracker userTracker, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarKeyguardViewManager statusBarKeyguardViewManager, UiEventLogger uiEventLogger) {
        return new WalletActivity(keyguardStateController, keyguardDismissUtil, activityStarter, executor, handler, falsingManager, falsingCollector, userTracker, keyguardUpdateMonitor, statusBarKeyguardViewManager, uiEventLogger);
    }
}
