package com.android.systemui.statusbar.dagger;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.statusbar.MediaArtworkProcessor;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideNotificationMediaManagerFactory */
public final class C2633xbcfb28e4 implements Factory<NotificationMediaManager> {
    private final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalLazyProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;
    private final Provider<MediaArtworkProcessor> mediaArtworkProcessorProvider;
    private final Provider<MediaDataManager> mediaDataManagerProvider;
    private final Provider<NotifCollection> notifCollectionProvider;
    private final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    private final Provider<NotifPipeline> notifPipelineProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<NotificationVisibilityProvider> visibilityProvider;

    public C2633xbcfb28e4(Provider<Context> provider, Provider<Optional<CentralSurfaces>> provider2, Provider<NotificationShadeWindowController> provider3, Provider<NotificationVisibilityProvider> provider4, Provider<NotificationEntryManager> provider5, Provider<MediaArtworkProcessor> provider6, Provider<KeyguardBypassController> provider7, Provider<NotifPipeline> provider8, Provider<NotifCollection> provider9, Provider<NotifPipelineFlags> provider10, Provider<DelayableExecutor> provider11, Provider<MediaDataManager> provider12, Provider<DumpManager> provider13) {
        this.contextProvider = provider;
        this.centralSurfacesOptionalLazyProvider = provider2;
        this.notificationShadeWindowControllerProvider = provider3;
        this.visibilityProvider = provider4;
        this.notificationEntryManagerProvider = provider5;
        this.mediaArtworkProcessorProvider = provider6;
        this.keyguardBypassControllerProvider = provider7;
        this.notifPipelineProvider = provider8;
        this.notifCollectionProvider = provider9;
        this.notifPipelineFlagsProvider = provider10;
        this.mainExecutorProvider = provider11;
        this.mediaDataManagerProvider = provider12;
        this.dumpManagerProvider = provider13;
    }

    public NotificationMediaManager get() {
        return provideNotificationMediaManager(this.contextProvider.get(), DoubleCheck.lazy(this.centralSurfacesOptionalLazyProvider), DoubleCheck.lazy(this.notificationShadeWindowControllerProvider), this.visibilityProvider.get(), this.notificationEntryManagerProvider.get(), this.mediaArtworkProcessorProvider.get(), this.keyguardBypassControllerProvider.get(), this.notifPipelineProvider.get(), this.notifCollectionProvider.get(), this.notifPipelineFlagsProvider.get(), this.mainExecutorProvider.get(), this.mediaDataManagerProvider.get(), this.dumpManagerProvider.get());
    }

    public static C2633xbcfb28e4 create(Provider<Context> provider, Provider<Optional<CentralSurfaces>> provider2, Provider<NotificationShadeWindowController> provider3, Provider<NotificationVisibilityProvider> provider4, Provider<NotificationEntryManager> provider5, Provider<MediaArtworkProcessor> provider6, Provider<KeyguardBypassController> provider7, Provider<NotifPipeline> provider8, Provider<NotifCollection> provider9, Provider<NotifPipelineFlags> provider10, Provider<DelayableExecutor> provider11, Provider<MediaDataManager> provider12, Provider<DumpManager> provider13) {
        return new C2633xbcfb28e4(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13);
    }

    public static NotificationMediaManager provideNotificationMediaManager(Context context, Lazy<Optional<CentralSurfaces>> lazy, Lazy<NotificationShadeWindowController> lazy2, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, MediaArtworkProcessor mediaArtworkProcessor, KeyguardBypassController keyguardBypassController, NotifPipeline notifPipeline, NotifCollection notifCollection, NotifPipelineFlags notifPipelineFlags, DelayableExecutor delayableExecutor, MediaDataManager mediaDataManager, DumpManager dumpManager) {
        return (NotificationMediaManager) Preconditions.checkNotNullFromProvides(CentralSurfacesDependenciesModule.provideNotificationMediaManager(context, lazy, lazy2, notificationVisibilityProvider, notificationEntryManager, mediaArtworkProcessor, keyguardBypassController, notifPipeline, notifCollection, notifPipelineFlags, delayableExecutor, mediaDataManager, dumpManager));
    }
}
