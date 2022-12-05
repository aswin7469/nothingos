package com.android.systemui.statusbar.dagger;

import android.app.IActivityManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarDependenciesModule_ProvideOngoingCallControllerFactory implements Factory<OngoingCallController> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<IActivityManager> iActivityManagerProvider;
    private final Provider<OngoingCallLogger> loggerProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<CommonNotifCollection> notifCollectionProvider;
    private final Provider<SystemClock> systemClockProvider;

    public StatusBarDependenciesModule_ProvideOngoingCallControllerFactory(Provider<CommonNotifCollection> provider, Provider<FeatureFlags> provider2, Provider<SystemClock> provider3, Provider<ActivityStarter> provider4, Provider<Executor> provider5, Provider<IActivityManager> provider6, Provider<OngoingCallLogger> provider7) {
        this.notifCollectionProvider = provider;
        this.featureFlagsProvider = provider2;
        this.systemClockProvider = provider3;
        this.activityStarterProvider = provider4;
        this.mainExecutorProvider = provider5;
        this.iActivityManagerProvider = provider6;
        this.loggerProvider = provider7;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public OngoingCallController mo1933get() {
        return provideOngoingCallController(this.notifCollectionProvider.mo1933get(), this.featureFlagsProvider.mo1933get(), this.systemClockProvider.mo1933get(), this.activityStarterProvider.mo1933get(), this.mainExecutorProvider.mo1933get(), this.iActivityManagerProvider.mo1933get(), this.loggerProvider.mo1933get());
    }

    public static StatusBarDependenciesModule_ProvideOngoingCallControllerFactory create(Provider<CommonNotifCollection> provider, Provider<FeatureFlags> provider2, Provider<SystemClock> provider3, Provider<ActivityStarter> provider4, Provider<Executor> provider5, Provider<IActivityManager> provider6, Provider<OngoingCallLogger> provider7) {
        return new StatusBarDependenciesModule_ProvideOngoingCallControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static OngoingCallController provideOngoingCallController(CommonNotifCollection commonNotifCollection, FeatureFlags featureFlags, SystemClock systemClock, ActivityStarter activityStarter, Executor executor, IActivityManager iActivityManager, OngoingCallLogger ongoingCallLogger) {
        return (OngoingCallController) Preconditions.checkNotNullFromProvides(StatusBarDependenciesModule.provideOngoingCallController(commonNotifCollection, featureFlags, systemClock, activityStarter, executor, iActivityManager, ongoingCallLogger));
    }
}
