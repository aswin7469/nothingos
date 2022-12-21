package com.android.systemui.p012qs;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.p012qs.external.CustomTileStatePersister;
import com.android.systemui.p012qs.external.TileLifecycleManager;
import com.android.systemui.p012qs.external.TileServiceRequestController;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.plugins.p011qs.QSFactory;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.QSTileHost_Factory */
public final class QSTileHost_Factory implements Factory<QSTileHost> {
    private final Provider<AutoTileManager> autoTilesProvider;
    private final Provider<Looper> bgLooperProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalProvider;
    private final Provider<Context> contextProvider;
    private final Provider<CustomTileStatePersister> customTileStatePersisterProvider;
    private final Provider<QSFactory> defaultFactoryProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<StatusBarIconController> iconControllerProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<PluginManager> pluginManagerProvider;
    private final Provider<QSLogger> qsLoggerProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<TileLifecycleManager.Factory> tileLifecycleManagerFactoryProvider;
    private final Provider<TileServiceRequestController.Builder> tileServiceRequestControllerBuilderProvider;
    private final Provider<TunerService> tunerServiceProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public QSTileHost_Factory(Provider<Context> provider, Provider<StatusBarIconController> provider2, Provider<QSFactory> provider3, Provider<Handler> provider4, Provider<Looper> provider5, Provider<PluginManager> provider6, Provider<TunerService> provider7, Provider<AutoTileManager> provider8, Provider<DumpManager> provider9, Provider<BroadcastDispatcher> provider10, Provider<Optional<CentralSurfaces>> provider11, Provider<QSLogger> provider12, Provider<UiEventLogger> provider13, Provider<UserTracker> provider14, Provider<SecureSettings> provider15, Provider<CustomTileStatePersister> provider16, Provider<TileServiceRequestController.Builder> provider17, Provider<TileLifecycleManager.Factory> provider18) {
        this.contextProvider = provider;
        this.iconControllerProvider = provider2;
        this.defaultFactoryProvider = provider3;
        this.mainHandlerProvider = provider4;
        this.bgLooperProvider = provider5;
        this.pluginManagerProvider = provider6;
        this.tunerServiceProvider = provider7;
        this.autoTilesProvider = provider8;
        this.dumpManagerProvider = provider9;
        this.broadcastDispatcherProvider = provider10;
        this.centralSurfacesOptionalProvider = provider11;
        this.qsLoggerProvider = provider12;
        this.uiEventLoggerProvider = provider13;
        this.userTrackerProvider = provider14;
        this.secureSettingsProvider = provider15;
        this.customTileStatePersisterProvider = provider16;
        this.tileServiceRequestControllerBuilderProvider = provider17;
        this.tileLifecycleManagerFactoryProvider = provider18;
    }

    public QSTileHost get() {
        return newInstance(this.contextProvider.get(), this.iconControllerProvider.get(), this.defaultFactoryProvider.get(), this.mainHandlerProvider.get(), this.bgLooperProvider.get(), this.pluginManagerProvider.get(), this.tunerServiceProvider.get(), this.autoTilesProvider, this.dumpManagerProvider.get(), this.broadcastDispatcherProvider.get(), this.centralSurfacesOptionalProvider.get(), this.qsLoggerProvider.get(), this.uiEventLoggerProvider.get(), this.userTrackerProvider.get(), this.secureSettingsProvider.get(), this.customTileStatePersisterProvider.get(), this.tileServiceRequestControllerBuilderProvider.get(), this.tileLifecycleManagerFactoryProvider.get());
    }

    public static QSTileHost_Factory create(Provider<Context> provider, Provider<StatusBarIconController> provider2, Provider<QSFactory> provider3, Provider<Handler> provider4, Provider<Looper> provider5, Provider<PluginManager> provider6, Provider<TunerService> provider7, Provider<AutoTileManager> provider8, Provider<DumpManager> provider9, Provider<BroadcastDispatcher> provider10, Provider<Optional<CentralSurfaces>> provider11, Provider<QSLogger> provider12, Provider<UiEventLogger> provider13, Provider<UserTracker> provider14, Provider<SecureSettings> provider15, Provider<CustomTileStatePersister> provider16, Provider<TileServiceRequestController.Builder> provider17, Provider<TileLifecycleManager.Factory> provider18) {
        return new QSTileHost_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18);
    }

    public static QSTileHost newInstance(Context context, StatusBarIconController statusBarIconController, QSFactory qSFactory, Handler handler, Looper looper, PluginManager pluginManager, TunerService tunerService, Provider<AutoTileManager> provider, DumpManager dumpManager, BroadcastDispatcher broadcastDispatcher, Optional<CentralSurfaces> optional, QSLogger qSLogger, UiEventLogger uiEventLogger, UserTracker userTracker, SecureSettings secureSettings, CustomTileStatePersister customTileStatePersister, TileServiceRequestController.Builder builder, TileLifecycleManager.Factory factory) {
        return new QSTileHost(context, statusBarIconController, qSFactory, handler, looper, pluginManager, tunerService, provider, dumpManager, broadcastDispatcher, optional, qSLogger, uiEventLogger, userTracker, secureSettings, customTileStatePersister, builder, factory);
    }
}
