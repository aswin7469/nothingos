package com.android.systemui.statusbar.notification.people;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.os.UserManager;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class PeopleHubDataSourceImpl_Factory implements Factory<PeopleHubDataSourceImpl> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<Context> contextProvider;
    private final Provider<NotificationPersonExtractor> extractorProvider;
    private final Provider<LauncherApps> launcherAppsProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<CommonNotifCollection> notifCollectionProvider;
    private final Provider<NotificationLockscreenUserManager> notifLockscreenUserMgrProvider;
    private final Provider<NotificationListener> notificationListenerProvider;
    private final Provider<PackageManager> packageManagerProvider;
    private final Provider<PeopleNotificationIdentifier> peopleNotificationIdentifierProvider;
    private final Provider<UserManager> userManagerProvider;

    public PeopleHubDataSourceImpl_Factory(Provider<CommonNotifCollection> provider, Provider<NotificationPersonExtractor> provider2, Provider<UserManager> provider3, Provider<LauncherApps> provider4, Provider<PackageManager> provider5, Provider<Context> provider6, Provider<NotificationListener> provider7, Provider<Executor> provider8, Provider<Executor> provider9, Provider<NotificationLockscreenUserManager> provider10, Provider<PeopleNotificationIdentifier> provider11) {
        this.notifCollectionProvider = provider;
        this.extractorProvider = provider2;
        this.userManagerProvider = provider3;
        this.launcherAppsProvider = provider4;
        this.packageManagerProvider = provider5;
        this.contextProvider = provider6;
        this.notificationListenerProvider = provider7;
        this.bgExecutorProvider = provider8;
        this.mainExecutorProvider = provider9;
        this.notifLockscreenUserMgrProvider = provider10;
        this.peopleNotificationIdentifierProvider = provider11;
    }

    public PeopleHubDataSourceImpl get() {
        return newInstance(this.notifCollectionProvider.get(), this.extractorProvider.get(), this.userManagerProvider.get(), this.launcherAppsProvider.get(), this.packageManagerProvider.get(), this.contextProvider.get(), this.notificationListenerProvider.get(), this.bgExecutorProvider.get(), this.mainExecutorProvider.get(), this.notifLockscreenUserMgrProvider.get(), this.peopleNotificationIdentifierProvider.get());
    }

    public static PeopleHubDataSourceImpl_Factory create(Provider<CommonNotifCollection> provider, Provider<NotificationPersonExtractor> provider2, Provider<UserManager> provider3, Provider<LauncherApps> provider4, Provider<PackageManager> provider5, Provider<Context> provider6, Provider<NotificationListener> provider7, Provider<Executor> provider8, Provider<Executor> provider9, Provider<NotificationLockscreenUserManager> provider10, Provider<PeopleNotificationIdentifier> provider11) {
        return new PeopleHubDataSourceImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static PeopleHubDataSourceImpl newInstance(CommonNotifCollection commonNotifCollection, NotificationPersonExtractor notificationPersonExtractor, UserManager userManager, LauncherApps launcherApps, PackageManager packageManager, Context context, NotificationListener notificationListener, Executor executor, Executor executor2, NotificationLockscreenUserManager notificationLockscreenUserManager, PeopleNotificationIdentifier peopleNotificationIdentifier) {
        return new PeopleHubDataSourceImpl(commonNotifCollection, notificationPersonExtractor, userManager, launcherApps, packageManager, context, notificationListener, executor, executor2, notificationLockscreenUserManager, peopleNotificationIdentifier);
    }
}
