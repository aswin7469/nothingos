package com.android.systemui.statusbar.notification.collection.coordinator;

import android.os.Handler;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder;
import com.android.systemui.statusbar.SmartReplyController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class RemoteInputCoordinator_Factory implements Factory<RemoteInputCoordinator> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Handler> mMainHandlerProvider;
    private final Provider<NotificationRemoteInputManager> mNotificationRemoteInputManagerProvider;
    private final Provider<RemoteInputNotificationRebuilder> mRebuilderProvider;
    private final Provider<SmartReplyController> mSmartReplyControllerProvider;

    public RemoteInputCoordinator_Factory(Provider<DumpManager> provider, Provider<RemoteInputNotificationRebuilder> provider2, Provider<NotificationRemoteInputManager> provider3, Provider<Handler> provider4, Provider<SmartReplyController> provider5) {
        this.dumpManagerProvider = provider;
        this.mRebuilderProvider = provider2;
        this.mNotificationRemoteInputManagerProvider = provider3;
        this.mMainHandlerProvider = provider4;
        this.mSmartReplyControllerProvider = provider5;
    }

    public RemoteInputCoordinator get() {
        return newInstance(this.dumpManagerProvider.get(), this.mRebuilderProvider.get(), this.mNotificationRemoteInputManagerProvider.get(), this.mMainHandlerProvider.get(), this.mSmartReplyControllerProvider.get());
    }

    public static RemoteInputCoordinator_Factory create(Provider<DumpManager> provider, Provider<RemoteInputNotificationRebuilder> provider2, Provider<NotificationRemoteInputManager> provider3, Provider<Handler> provider4, Provider<SmartReplyController> provider5) {
        return new RemoteInputCoordinator_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static RemoteInputCoordinator newInstance(DumpManager dumpManager, RemoteInputNotificationRebuilder remoteInputNotificationRebuilder, NotificationRemoteInputManager notificationRemoteInputManager, Handler handler, SmartReplyController smartReplyController) {
        return new RemoteInputCoordinator(dumpManager, remoteInputNotificationRebuilder, notificationRemoteInputManager, handler, smartReplyController);
    }
}
