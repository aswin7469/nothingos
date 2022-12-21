package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.IActivityManager;
import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class OngoingCallController_Factory implements Factory<OngoingCallController> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<IActivityManager> iActivityManagerProvider;
    private final Provider<OngoingCallLogger> loggerProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<CommonNotifCollection> notifCollectionProvider;
    private final Provider<OngoingCallFlags> ongoingCallFlagsProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<Optional<StatusBarWindowController>> statusBarWindowControllerProvider;
    private final Provider<Optional<SwipeStatusBarAwayGestureHandler>> swipeStatusBarAwayGestureHandlerProvider;
    private final Provider<SystemClock> systemClockProvider;

    public OngoingCallController_Factory(Provider<Context> provider, Provider<CommonNotifCollection> provider2, Provider<OngoingCallFlags> provider3, Provider<SystemClock> provider4, Provider<ActivityStarter> provider5, Provider<Executor> provider6, Provider<IActivityManager> provider7, Provider<OngoingCallLogger> provider8, Provider<DumpManager> provider9, Provider<Optional<StatusBarWindowController>> provider10, Provider<Optional<SwipeStatusBarAwayGestureHandler>> provider11, Provider<StatusBarStateController> provider12) {
        this.contextProvider = provider;
        this.notifCollectionProvider = provider2;
        this.ongoingCallFlagsProvider = provider3;
        this.systemClockProvider = provider4;
        this.activityStarterProvider = provider5;
        this.mainExecutorProvider = provider6;
        this.iActivityManagerProvider = provider7;
        this.loggerProvider = provider8;
        this.dumpManagerProvider = provider9;
        this.statusBarWindowControllerProvider = provider10;
        this.swipeStatusBarAwayGestureHandlerProvider = provider11;
        this.statusBarStateControllerProvider = provider12;
    }

    public OngoingCallController get() {
        return newInstance(this.contextProvider.get(), this.notifCollectionProvider.get(), this.ongoingCallFlagsProvider.get(), this.systemClockProvider.get(), this.activityStarterProvider.get(), this.mainExecutorProvider.get(), this.iActivityManagerProvider.get(), this.loggerProvider.get(), this.dumpManagerProvider.get(), this.statusBarWindowControllerProvider.get(), this.swipeStatusBarAwayGestureHandlerProvider.get(), this.statusBarStateControllerProvider.get());
    }

    public static OngoingCallController_Factory create(Provider<Context> provider, Provider<CommonNotifCollection> provider2, Provider<OngoingCallFlags> provider3, Provider<SystemClock> provider4, Provider<ActivityStarter> provider5, Provider<Executor> provider6, Provider<IActivityManager> provider7, Provider<OngoingCallLogger> provider8, Provider<DumpManager> provider9, Provider<Optional<StatusBarWindowController>> provider10, Provider<Optional<SwipeStatusBarAwayGestureHandler>> provider11, Provider<StatusBarStateController> provider12) {
        return new OngoingCallController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static OngoingCallController newInstance(Context context, CommonNotifCollection commonNotifCollection, OngoingCallFlags ongoingCallFlags, SystemClock systemClock, ActivityStarter activityStarter, Executor executor, IActivityManager iActivityManager, OngoingCallLogger ongoingCallLogger, DumpManager dumpManager, Optional<StatusBarWindowController> optional, Optional<SwipeStatusBarAwayGestureHandler> optional2, StatusBarStateController statusBarStateController) {
        return new OngoingCallController(context, commonNotifCollection, ongoingCallFlags, systemClock, activityStarter, executor, iActivityManager, ongoingCallLogger, dumpManager, optional, optional2, statusBarStateController);
    }
}
