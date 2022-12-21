package com.android.systemui.statusbar.dagger;

import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideSmartReplyControllerFactory */
public final class C2631xc35bee5b implements Factory<SmartReplyController> {
    private final Provider<NotificationClickNotifier> clickNotifierProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<IStatusBarService> statusBarServiceProvider;
    private final Provider<NotificationVisibilityProvider> visibilityProvider;

    public C2631xc35bee5b(Provider<DumpManager> provider, Provider<NotificationVisibilityProvider> provider2, Provider<IStatusBarService> provider3, Provider<NotificationClickNotifier> provider4) {
        this.dumpManagerProvider = provider;
        this.visibilityProvider = provider2;
        this.statusBarServiceProvider = provider3;
        this.clickNotifierProvider = provider4;
    }

    public SmartReplyController get() {
        return provideSmartReplyController(this.dumpManagerProvider.get(), this.visibilityProvider.get(), this.statusBarServiceProvider.get(), this.clickNotifierProvider.get());
    }

    public static C2631xc35bee5b create(Provider<DumpManager> provider, Provider<NotificationVisibilityProvider> provider2, Provider<IStatusBarService> provider3, Provider<NotificationClickNotifier> provider4) {
        return new C2631xc35bee5b(provider, provider2, provider3, provider4);
    }

    public static SmartReplyController provideSmartReplyController(DumpManager dumpManager, NotificationVisibilityProvider notificationVisibilityProvider, IStatusBarService iStatusBarService, NotificationClickNotifier notificationClickNotifier) {
        return (SmartReplyController) Preconditions.checkNotNullFromProvides(CentralSurfacesDependenciesModule.provideSmartReplyController(dumpManager, notificationVisibilityProvider, iStatusBarService, notificationClickNotifier));
    }
}
