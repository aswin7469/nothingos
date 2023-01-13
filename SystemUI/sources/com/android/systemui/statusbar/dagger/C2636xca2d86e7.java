package com.android.systemui.statusbar.dagger;

import android.app.IActivityManager;
import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallFlags;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideOngoingCallControllerFactory */
public final class C2636xca2d86e7 implements Factory<OngoingCallController> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<IActivityManager> iActivityManagerProvider;
    private final Provider<OngoingCallLogger> loggerProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<CommonNotifCollection> notifCollectionProvider;
    private final Provider<OngoingCallFlags> ongoingCallFlagsProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<StatusBarWindowController> statusBarWindowControllerProvider;
    private final Provider<SwipeStatusBarAwayGestureHandler> swipeStatusBarAwayGestureHandlerProvider;
    private final Provider<SystemClock> systemClockProvider;

    public C2636xca2d86e7(Provider<Context> provider, Provider<CommonNotifCollection> provider2, Provider<SystemClock> provider3, Provider<ActivityStarter> provider4, Provider<Executor> provider5, Provider<IActivityManager> provider6, Provider<OngoingCallLogger> provider7, Provider<DumpManager> provider8, Provider<StatusBarWindowController> provider9, Provider<SwipeStatusBarAwayGestureHandler> provider10, Provider<StatusBarStateController> provider11, Provider<OngoingCallFlags> provider12) {
        this.contextProvider = provider;
        this.notifCollectionProvider = provider2;
        this.systemClockProvider = provider3;
        this.activityStarterProvider = provider4;
        this.mainExecutorProvider = provider5;
        this.iActivityManagerProvider = provider6;
        this.loggerProvider = provider7;
        this.dumpManagerProvider = provider8;
        this.statusBarWindowControllerProvider = provider9;
        this.swipeStatusBarAwayGestureHandlerProvider = provider10;
        this.statusBarStateControllerProvider = provider11;
        this.ongoingCallFlagsProvider = provider12;
    }

    public OngoingCallController get() {
        return provideOngoingCallController(this.contextProvider.get(), this.notifCollectionProvider.get(), this.systemClockProvider.get(), this.activityStarterProvider.get(), this.mainExecutorProvider.get(), this.iActivityManagerProvider.get(), this.loggerProvider.get(), this.dumpManagerProvider.get(), this.statusBarWindowControllerProvider.get(), this.swipeStatusBarAwayGestureHandlerProvider.get(), this.statusBarStateControllerProvider.get(), this.ongoingCallFlagsProvider.get());
    }

    public static C2636xca2d86e7 create(Provider<Context> provider, Provider<CommonNotifCollection> provider2, Provider<SystemClock> provider3, Provider<ActivityStarter> provider4, Provider<Executor> provider5, Provider<IActivityManager> provider6, Provider<OngoingCallLogger> provider7, Provider<DumpManager> provider8, Provider<StatusBarWindowController> provider9, Provider<SwipeStatusBarAwayGestureHandler> provider10, Provider<StatusBarStateController> provider11, Provider<OngoingCallFlags> provider12) {
        return new C2636xca2d86e7(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static OngoingCallController provideOngoingCallController(Context context, CommonNotifCollection commonNotifCollection, SystemClock systemClock, ActivityStarter activityStarter, Executor executor, IActivityManager iActivityManager, OngoingCallLogger ongoingCallLogger, DumpManager dumpManager, StatusBarWindowController statusBarWindowController, SwipeStatusBarAwayGestureHandler swipeStatusBarAwayGestureHandler, StatusBarStateController statusBarStateController, OngoingCallFlags ongoingCallFlags) {
        return (OngoingCallController) Preconditions.checkNotNullFromProvides(CentralSurfacesDependenciesModule.provideOngoingCallController(context, commonNotifCollection, systemClock, activityStarter, executor, iActivityManager, ongoingCallLogger, dumpManager, statusBarWindowController, swipeStatusBarAwayGestureHandler, statusBarStateController, ongoingCallFlags));
    }
}
