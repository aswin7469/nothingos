package com.android.systemui.p012qs.external;

import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.external.CustomTile;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.external.CustomTile_Builder_Factory */
public final class CustomTile_Builder_Factory implements Factory<CustomTile.Builder> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Looper> backgroundLooperProvider;
    private final Provider<CustomTileStatePersister> customTileStatePersisterProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<QSHost> hostLazyProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<QSLogger> qsLoggerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<TileServices> tileServicesProvider;

    public CustomTile_Builder_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<CustomTileStatePersister> provider9, Provider<TileServices> provider10) {
        this.hostLazyProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.customTileStatePersisterProvider = provider9;
        this.tileServicesProvider = provider10;
    }

    public CustomTile.Builder get() {
        return newInstance(DoubleCheck.lazy(this.hostLazyProvider), this.backgroundLooperProvider.get(), this.mainHandlerProvider.get(), this.falsingManagerProvider.get(), this.metricsLoggerProvider.get(), this.statusBarStateControllerProvider.get(), this.activityStarterProvider.get(), this.qsLoggerProvider.get(), this.customTileStatePersisterProvider.get(), this.tileServicesProvider.get());
    }

    public static CustomTile_Builder_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<CustomTileStatePersister> provider9, Provider<TileServices> provider10) {
        return new CustomTile_Builder_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static CustomTile.Builder newInstance(Lazy<QSHost> lazy, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, CustomTileStatePersister customTileStatePersister, TileServices tileServices) {
        return new CustomTile.Builder(lazy, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, customTileStatePersister, tileServices);
    }
}
