package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.IActivityManager;
import android.app.IUidObserver;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.view.View;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.time.SystemClock;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000´\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0002'*\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0002JKBu\b\u0007\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\b\b\u0001\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017\u0012\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0017\u0012\u0006\u0010\u001b\u001a\u00020\u001c¢\u0006\u0002\u0010\u001dJ\u0010\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020\u0002H\u0016J%\u00101\u001a\u00020/2\u0006\u00102\u001a\u0002032\u000e\u00104\u001a\n\u0012\u0006\b\u0001\u0012\u00020605H\u0016¢\u0006\u0002\u00107J\u0006\u00108\u001a\u00020#J\u0006\u00109\u001a\u00020/J\u0010\u0010:\u001a\u00020#2\u0006\u0010;\u001a\u00020<H\u0002J\u000e\u0010=\u001a\u00020/2\u0006\u0010>\u001a\u00020#J\b\u0010?\u001a\u00020/H\u0002J\u0010\u0010@\u001a\u00020/2\u0006\u00100\u001a\u00020\u0002H\u0016J\b\u0010A\u001a\u00020/H\u0002J\u000e\u0010B\u001a\u00020/2\u0006\u0010 \u001a\u00020!J\u000f\u0010C\u001a\u0004\u0018\u00010/H\u0007¢\u0006\u0002\u0010DJ\b\u0010E\u001a\u00020/H\u0002J\b\u0010F\u001a\u00020/H\u0002J\b\u0010G\u001a\u00020/H\u0002J\u000e\u0010H\u001a\u0004\u0018\u00010I*\u00020!H\u0002R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00020%X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u00020'X\u0004¢\u0006\u0004\n\u0002\u0010(R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u00020*X\u0004¢\u0006\u0004\n\u0002\u0010+R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0017X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0012\u0010,\u001a\u00060-R\u00020\u0000X\u0004¢\u0006\u0002\n\u0000¨\u0006L"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallController;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallListener;", "Lcom/android/systemui/Dumpable;", "context", "Landroid/content/Context;", "notifCollection", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;", "ongoingCallFlags", "Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallFlags;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "mainExecutor", "Ljava/util/concurrent/Executor;", "iActivityManager", "Landroid/app/IActivityManager;", "logger", "Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallLogger;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "statusBarWindowController", "Ljava/util/Optional;", "Lcom/android/systemui/statusbar/window/StatusBarWindowController;", "swipeStatusBarAwayGestureHandler", "Lcom/android/systemui/statusbar/gesture/SwipeStatusBarAwayGestureHandler;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallFlags;Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/plugins/ActivityStarter;Ljava/util/concurrent/Executor;Landroid/app/IActivityManager;Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallLogger;Lcom/android/systemui/dump/DumpManager;Ljava/util/Optional;Ljava/util/Optional;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;)V", "callNotificationInfo", "Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallController$CallNotificationInfo;", "chipView", "Landroid/view/View;", "isFullscreen", "", "mListeners", "", "notifListener", "com/android/systemui/statusbar/phone/ongoingcall/OngoingCallController$notifListener$1", "Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallController$notifListener$1;", "statusBarStateListener", "com/android/systemui/statusbar/phone/ongoingcall/OngoingCallController$statusBarStateListener$1", "Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallController$statusBarStateListener$1;", "uidObserver", "Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallController$CallAppUidObserver;", "addCallback", "", "listener", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "hasOngoingCall", "init", "isProcessVisibleToUser", "procState", "", "notifyChipVisibilityChanged", "chipIsVisible", "onSwipeAwayGestureDetected", "removeCallback", "removeChip", "setChipView", "tearDownChipView", "()Lkotlin/Unit;", "updateChip", "updateChipClickListener", "updateGestureListening", "getTimeView", "Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallChronometer;", "CallAppUidObserver", "CallNotificationInfo", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: OngoingCallController.kt */
public final class OngoingCallController implements CallbackController<OngoingCallListener>, Dumpable {
    private final ActivityStarter activityStarter;
    /* access modifiers changed from: private */
    public CallNotificationInfo callNotificationInfo;
    private View chipView;
    /* access modifiers changed from: private */
    public final Context context;
    private final DumpManager dumpManager;
    /* access modifiers changed from: private */
    public final IActivityManager iActivityManager;
    /* access modifiers changed from: private */
    public boolean isFullscreen;
    private final OngoingCallLogger logger;
    /* access modifiers changed from: private */
    public final List<OngoingCallListener> mListeners = new ArrayList();
    /* access modifiers changed from: private */
    public final Executor mainExecutor;
    private final CommonNotifCollection notifCollection;
    private final OngoingCallController$notifListener$1 notifListener = new OngoingCallController$notifListener$1(this);
    private final OngoingCallFlags ongoingCallFlags;
    private final StatusBarStateController statusBarStateController;
    private final OngoingCallController$statusBarStateListener$1 statusBarStateListener = new OngoingCallController$statusBarStateListener$1(this);
    private final Optional<StatusBarWindowController> statusBarWindowController;
    private final Optional<SwipeStatusBarAwayGestureHandler> swipeStatusBarAwayGestureHandler;
    private final SystemClock systemClock;
    /* access modifiers changed from: private */
    public final CallAppUidObserver uidObserver = new CallAppUidObserver();

    /* access modifiers changed from: private */
    public final boolean isProcessVisibleToUser(int i) {
        return i <= 2;
    }

    @Inject
    public OngoingCallController(Context context2, CommonNotifCollection commonNotifCollection, OngoingCallFlags ongoingCallFlags2, SystemClock systemClock2, ActivityStarter activityStarter2, @Main Executor executor, IActivityManager iActivityManager2, OngoingCallLogger ongoingCallLogger, DumpManager dumpManager2, Optional<StatusBarWindowController> optional, Optional<SwipeStatusBarAwayGestureHandler> optional2, StatusBarStateController statusBarStateController2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(commonNotifCollection, "notifCollection");
        Intrinsics.checkNotNullParameter(ongoingCallFlags2, "ongoingCallFlags");
        Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        Intrinsics.checkNotNullParameter(iActivityManager2, "iActivityManager");
        Intrinsics.checkNotNullParameter(ongoingCallLogger, "logger");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(optional, "statusBarWindowController");
        Intrinsics.checkNotNullParameter(optional2, "swipeStatusBarAwayGestureHandler");
        Intrinsics.checkNotNullParameter(statusBarStateController2, "statusBarStateController");
        this.context = context2;
        this.notifCollection = commonNotifCollection;
        this.ongoingCallFlags = ongoingCallFlags2;
        this.systemClock = systemClock2;
        this.activityStarter = activityStarter2;
        this.mainExecutor = executor;
        this.iActivityManager = iActivityManager2;
        this.logger = ongoingCallLogger;
        this.dumpManager = dumpManager2;
        this.statusBarWindowController = optional;
        this.swipeStatusBarAwayGestureHandler = optional2;
        this.statusBarStateController = statusBarStateController2;
    }

    public final void init() {
        this.dumpManager.registerDumpable(this);
        if (this.ongoingCallFlags.isStatusBarChipEnabled()) {
            this.notifCollection.addCollectionListener(this.notifListener);
            this.statusBarStateController.addCallback(this.statusBarStateListener);
        }
    }

    public final void setChipView(View view) {
        Intrinsics.checkNotNullParameter(view, "chipView");
        tearDownChipView();
        this.chipView = view;
        if (hasOngoingCall()) {
            updateChip();
        }
    }

    public final void notifyChipVisibilityChanged(boolean z) {
        this.logger.logChipVisibilityChanged(z);
    }

    public final boolean hasOngoingCall() {
        CallNotificationInfo callNotificationInfo2 = this.callNotificationInfo;
        if (!(callNotificationInfo2 != null && callNotificationInfo2.isOngoing()) || this.uidObserver.isCallAppVisible()) {
            return false;
        }
        return true;
    }

    public void addCallback(OngoingCallListener ongoingCallListener) {
        Intrinsics.checkNotNullParameter(ongoingCallListener, "listener");
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(ongoingCallListener)) {
                this.mListeners.add(ongoingCallListener);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public void removeCallback(OngoingCallListener ongoingCallListener) {
        Intrinsics.checkNotNullParameter(ongoingCallListener, "listener");
        synchronized (this.mListeners) {
            this.mListeners.remove((Object) ongoingCallListener);
        }
    }

    /* access modifiers changed from: private */
    public final void updateChip() {
        CallNotificationInfo callNotificationInfo2 = this.callNotificationInfo;
        if (callNotificationInfo2 != null) {
            View view = this.chipView;
            OngoingCallChronometer timeView = view != null ? getTimeView(view) : null;
            if (view == null || timeView == null) {
                this.callNotificationInfo = null;
                if (OngoingCallControllerKt.DEBUG) {
                    Log.w("OngoingCallController", "Ongoing call chip view could not be found; Not displaying chip in status bar");
                    return;
                }
                return;
            }
            if (callNotificationInfo2.hasValidStartTime()) {
                timeView.setShouldHideText(false);
                timeView.setBase((callNotificationInfo2.getCallStartTime() - this.systemClock.currentTimeMillis()) + this.systemClock.elapsedRealtime());
                timeView.start();
            } else {
                timeView.setShouldHideText(true);
                timeView.stop();
            }
            updateChipClickListener();
            this.uidObserver.registerWithUid(callNotificationInfo2.getUid());
            if (!callNotificationInfo2.getStatusBarSwipedAway()) {
                this.statusBarWindowController.ifPresent(new OngoingCallController$$ExternalSyntheticLambda5());
            }
            updateGestureListening();
            for (OngoingCallListener onOngoingCallStateChanged : this.mListeners) {
                onOngoingCallStateChanged.onOngoingCallStateChanged(true);
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: updateChip$lambda-2  reason: not valid java name */
    public static final void m3214updateChip$lambda2(StatusBarWindowController statusBarWindowController2) {
        Intrinsics.checkNotNullParameter(statusBarWindowController2, "it");
        statusBarWindowController2.setOngoingProcessRequiresStatusBarVisible(true);
    }

    /* access modifiers changed from: private */
    public final void updateChipClickListener() {
        if (this.callNotificationInfo != null) {
            PendingIntent pendingIntent = null;
            if (!this.isFullscreen || this.ongoingCallFlags.isInImmersiveChipTapEnabled()) {
                View view = this.chipView;
                View findViewById = view != null ? view.findViewById(C1894R.C1898id.ongoing_call_chip_background) : null;
                CallNotificationInfo callNotificationInfo2 = this.callNotificationInfo;
                if (callNotificationInfo2 != null) {
                    pendingIntent = callNotificationInfo2.getIntent();
                }
                if (view != null && findViewById != null && pendingIntent != null) {
                    view.setOnClickListener(new OngoingCallController$$ExternalSyntheticLambda2(this, pendingIntent, findViewById));
                    return;
                }
                return;
            }
            View view2 = this.chipView;
            if (view2 != null) {
                view2.setOnClickListener((View.OnClickListener) null);
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: updateChipClickListener$lambda-4  reason: not valid java name */
    public static final void m3215updateChipClickListener$lambda4(OngoingCallController ongoingCallController, PendingIntent pendingIntent, View view, View view2) {
        Intrinsics.checkNotNullParameter(ongoingCallController, "this$0");
        ongoingCallController.logger.logChipClicked();
        ongoingCallController.activityStarter.postStartActivityDismissingKeyguard(pendingIntent, ActivityLaunchAnimator.Controller.Companion.fromView(view, 34));
    }

    /* access modifiers changed from: private */
    public final void updateGestureListening() {
        CallNotificationInfo callNotificationInfo2 = this.callNotificationInfo;
        if (callNotificationInfo2 != null) {
            boolean z = false;
            if (callNotificationInfo2 != null && callNotificationInfo2.getStatusBarSwipedAway()) {
                z = true;
            }
            if (!z && this.isFullscreen) {
                this.swipeStatusBarAwayGestureHandler.ifPresent(new OngoingCallController$$ExternalSyntheticLambda4(this));
                return;
            }
        }
        this.swipeStatusBarAwayGestureHandler.ifPresent(new OngoingCallController$$ExternalSyntheticLambda3());
    }

    /* access modifiers changed from: private */
    /* renamed from: updateGestureListening$lambda-5  reason: not valid java name */
    public static final void m3216updateGestureListening$lambda5(SwipeStatusBarAwayGestureHandler swipeStatusBarAwayGestureHandler2) {
        Intrinsics.checkNotNullParameter(swipeStatusBarAwayGestureHandler2, "it");
        swipeStatusBarAwayGestureHandler2.removeOnGestureDetectedCallback("OngoingCallController");
    }

    /* access modifiers changed from: private */
    /* renamed from: updateGestureListening$lambda-6  reason: not valid java name */
    public static final void m3217updateGestureListening$lambda6(OngoingCallController ongoingCallController, SwipeStatusBarAwayGestureHandler swipeStatusBarAwayGestureHandler2) {
        Intrinsics.checkNotNullParameter(ongoingCallController, "this$0");
        Intrinsics.checkNotNullParameter(swipeStatusBarAwayGestureHandler2, "it");
        swipeStatusBarAwayGestureHandler2.addOnGestureDetectedCallback("OngoingCallController", new OngoingCallController$updateGestureListening$2$1(ongoingCallController));
    }

    /* access modifiers changed from: private */
    public final void removeChip() {
        this.callNotificationInfo = null;
        tearDownChipView();
        this.statusBarWindowController.ifPresent(new OngoingCallController$$ExternalSyntheticLambda0());
        this.swipeStatusBarAwayGestureHandler.ifPresent(new OngoingCallController$$ExternalSyntheticLambda1());
        for (OngoingCallListener onOngoingCallStateChanged : this.mListeners) {
            onOngoingCallStateChanged.onOngoingCallStateChanged(true);
        }
        this.uidObserver.unregister();
    }

    /* access modifiers changed from: private */
    /* renamed from: removeChip$lambda-7  reason: not valid java name */
    public static final void m3212removeChip$lambda7(StatusBarWindowController statusBarWindowController2) {
        Intrinsics.checkNotNullParameter(statusBarWindowController2, "it");
        statusBarWindowController2.setOngoingProcessRequiresStatusBarVisible(false);
    }

    /* access modifiers changed from: private */
    /* renamed from: removeChip$lambda-8  reason: not valid java name */
    public static final void m3213removeChip$lambda8(SwipeStatusBarAwayGestureHandler swipeStatusBarAwayGestureHandler2) {
        Intrinsics.checkNotNullParameter(swipeStatusBarAwayGestureHandler2, "it");
        swipeStatusBarAwayGestureHandler2.removeOnGestureDetectedCallback("OngoingCallController");
    }

    public final Unit tearDownChipView() {
        OngoingCallChronometer timeView;
        View view = this.chipView;
        if (view == null || (timeView = getTimeView(view)) == null) {
            return null;
        }
        timeView.stop();
        return Unit.INSTANCE;
    }

    private final OngoingCallChronometer getTimeView(View view) {
        return (OngoingCallChronometer) view.findViewById(C1894R.C1898id.ongoing_call_chip_time);
    }

    /* access modifiers changed from: private */
    public final void onSwipeAwayGestureDetected() {
        if (OngoingCallControllerKt.DEBUG) {
            Log.d("OngoingCallController", "Swipe away gesture detected");
        }
        CallNotificationInfo callNotificationInfo2 = this.callNotificationInfo;
        this.callNotificationInfo = callNotificationInfo2 != null ? CallNotificationInfo.copy$default(callNotificationInfo2, (String) null, 0, (PendingIntent) null, 0, false, true, 31, (Object) null) : null;
        this.statusBarWindowController.ifPresent(new OngoingCallController$$ExternalSyntheticLambda6());
        this.swipeStatusBarAwayGestureHandler.ifPresent(new OngoingCallController$$ExternalSyntheticLambda7());
    }

    /* access modifiers changed from: private */
    /* renamed from: onSwipeAwayGestureDetected$lambda-10  reason: not valid java name */
    public static final void m3210onSwipeAwayGestureDetected$lambda10(StatusBarWindowController statusBarWindowController2) {
        Intrinsics.checkNotNullParameter(statusBarWindowController2, "it");
        statusBarWindowController2.setOngoingProcessRequiresStatusBarVisible(false);
    }

    /* access modifiers changed from: private */
    /* renamed from: onSwipeAwayGestureDetected$lambda-11  reason: not valid java name */
    public static final void m3211onSwipeAwayGestureDetected$lambda11(SwipeStatusBarAwayGestureHandler swipeStatusBarAwayGestureHandler2) {
        Intrinsics.checkNotNullParameter(swipeStatusBarAwayGestureHandler2, "it");
        swipeStatusBarAwayGestureHandler2.removeOnGestureDetectedCallback("OngoingCallController");
    }

    @Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0019\b\b\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\u000b¢\u0006\u0002\u0010\rJ\t\u0010\u0018\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0005HÆ\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\t\u0010\u001b\u001a\u00020\tHÆ\u0003J\t\u0010\u001c\u001a\u00020\u000bHÆ\u0003J\t\u0010\u001d\u001a\u00020\u000bHÆ\u0003JG\u0010\u001e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000bHÆ\u0001J\u0013\u0010\u001f\u001a\u00020\u000b2\b\u0010 \u001a\u0004\u0018\u00010\u0001HÖ\u0003J\u0006\u0010!\u001a\u00020\u000bJ\t\u0010\"\u001a\u00020\tHÖ\u0001J\t\u0010#\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0012R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\f\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017¨\u0006$"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallController$CallNotificationInfo;", "", "key", "", "callStartTime", "", "intent", "Landroid/app/PendingIntent;", "uid", "", "isOngoing", "", "statusBarSwipedAway", "(Ljava/lang/String;JLandroid/app/PendingIntent;IZZ)V", "getCallStartTime", "()J", "getIntent", "()Landroid/app/PendingIntent;", "()Z", "getKey", "()Ljava/lang/String;", "getStatusBarSwipedAway", "getUid", "()I", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hasValidStartTime", "hashCode", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: OngoingCallController.kt */
    private static final class CallNotificationInfo {
        private final long callStartTime;
        private final PendingIntent intent;
        private final boolean isOngoing;
        private final String key;
        private final boolean statusBarSwipedAway;
        private final int uid;

        public static /* synthetic */ CallNotificationInfo copy$default(CallNotificationInfo callNotificationInfo, String str, long j, PendingIntent pendingIntent, int i, boolean z, boolean z2, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                str = callNotificationInfo.key;
            }
            if ((i2 & 2) != 0) {
                j = callNotificationInfo.callStartTime;
            }
            long j2 = j;
            if ((i2 & 4) != 0) {
                pendingIntent = callNotificationInfo.intent;
            }
            PendingIntent pendingIntent2 = pendingIntent;
            if ((i2 & 8) != 0) {
                i = callNotificationInfo.uid;
            }
            int i3 = i;
            if ((i2 & 16) != 0) {
                z = callNotificationInfo.isOngoing;
            }
            boolean z3 = z;
            if ((i2 & 32) != 0) {
                z2 = callNotificationInfo.statusBarSwipedAway;
            }
            return callNotificationInfo.copy(str, j2, pendingIntent2, i3, z3, z2);
        }

        public final String component1() {
            return this.key;
        }

        public final long component2() {
            return this.callStartTime;
        }

        public final PendingIntent component3() {
            return this.intent;
        }

        public final int component4() {
            return this.uid;
        }

        public final boolean component5() {
            return this.isOngoing;
        }

        public final boolean component6() {
            return this.statusBarSwipedAway;
        }

        public final CallNotificationInfo copy(String str, long j, PendingIntent pendingIntent, int i, boolean z, boolean z2) {
            Intrinsics.checkNotNullParameter(str, "key");
            return new CallNotificationInfo(str, j, pendingIntent, i, z, z2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CallNotificationInfo)) {
                return false;
            }
            CallNotificationInfo callNotificationInfo = (CallNotificationInfo) obj;
            return Intrinsics.areEqual((Object) this.key, (Object) callNotificationInfo.key) && this.callStartTime == callNotificationInfo.callStartTime && Intrinsics.areEqual((Object) this.intent, (Object) callNotificationInfo.intent) && this.uid == callNotificationInfo.uid && this.isOngoing == callNotificationInfo.isOngoing && this.statusBarSwipedAway == callNotificationInfo.statusBarSwipedAway;
        }

        public int hashCode() {
            int hashCode = ((this.key.hashCode() * 31) + Long.hashCode(this.callStartTime)) * 31;
            PendingIntent pendingIntent = this.intent;
            int hashCode2 = (((hashCode + (pendingIntent == null ? 0 : pendingIntent.hashCode())) * 31) + Integer.hashCode(this.uid)) * 31;
            boolean z = this.isOngoing;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int i = (hashCode2 + (z ? 1 : 0)) * 31;
            boolean z3 = this.statusBarSwipedAway;
            if (!z3) {
                z2 = z3;
            }
            return i + (z2 ? 1 : 0);
        }

        public String toString() {
            return "CallNotificationInfo(key=" + this.key + ", callStartTime=" + this.callStartTime + ", intent=" + this.intent + ", uid=" + this.uid + ", isOngoing=" + this.isOngoing + ", statusBarSwipedAway=" + this.statusBarSwipedAway + ')';
        }

        public CallNotificationInfo(String str, long j, PendingIntent pendingIntent, int i, boolean z, boolean z2) {
            Intrinsics.checkNotNullParameter(str, "key");
            this.key = str;
            this.callStartTime = j;
            this.intent = pendingIntent;
            this.uid = i;
            this.isOngoing = z;
            this.statusBarSwipedAway = z2;
        }

        public final String getKey() {
            return this.key;
        }

        public final long getCallStartTime() {
            return this.callStartTime;
        }

        public final PendingIntent getIntent() {
            return this.intent;
        }

        public final int getUid() {
            return this.uid;
        }

        public final boolean isOngoing() {
            return this.isOngoing;
        }

        public final boolean getStatusBarSwipedAway() {
            return this.statusBarSwipedAway;
        }

        public final boolean hasValidStartTime() {
            return this.callStartTime > 0;
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("Active call notification: " + this.callNotificationInfo);
        printWriter.println("Call app visible: " + this.uidObserver.isCallAppVisible());
    }

    @Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010\t\n\u0002\b\u0004\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0004H\u0016J\u0018\u0010\u000e\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0007H\u0016J\u0018\u0010\u0010\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0007H\u0016J\u0018\u0010\u0012\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0007H\u0016J\u0010\u0010\u0013\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0004H\u0016J(\u0010\u0014\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0004H\u0016J\u000e\u0010\u0019\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0004J\u0006\u0010\u001a\u001a\u00020\fR\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0004\n\u0002\u0010\u0005R\u001e\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0007@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001b"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallController$CallAppUidObserver;", "Landroid/app/IUidObserver$Stub;", "(Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallController;)V", "callAppUid", "", "Ljava/lang/Integer;", "<set-?>", "", "isCallAppVisible", "()Z", "isRegistered", "onUidActive", "", "uid", "onUidCachedChanged", "cached", "onUidGone", "disabled", "onUidIdle", "onUidProcAdjChanged", "onUidStateChanged", "procState", "procStateSeq", "", "capability", "registerWithUid", "unregister", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: OngoingCallController.kt */
    public final class CallAppUidObserver extends IUidObserver.Stub {
        private Integer callAppUid;
        private boolean isCallAppVisible;
        private boolean isRegistered;

        public void onUidActive(int i) {
        }

        public void onUidCachedChanged(int i, boolean z) {
        }

        public void onUidGone(int i, boolean z) {
        }

        public void onUidIdle(int i, boolean z) {
        }

        public void onUidProcAdjChanged(int i) {
        }

        public CallAppUidObserver() {
        }

        public final boolean isCallAppVisible() {
            return this.isCallAppVisible;
        }

        public final void registerWithUid(int i) {
            Integer num = this.callAppUid;
            if (num == null || num.intValue() != i) {
                this.callAppUid = Integer.valueOf(i);
                try {
                    OngoingCallController ongoingCallController = OngoingCallController.this;
                    this.isCallAppVisible = ongoingCallController.isProcessVisibleToUser(ongoingCallController.iActivityManager.getUidProcessState(i, OngoingCallController.this.context.getOpPackageName()));
                    if (!this.isRegistered) {
                        OngoingCallController.this.iActivityManager.registerUidObserver(OngoingCallController.this.uidObserver, 1, -1, OngoingCallController.this.context.getOpPackageName());
                        this.isRegistered = true;
                    }
                } catch (SecurityException e) {
                    Log.e("OngoingCallController", "Security exception when trying to set up uid observer: " + e);
                }
            }
        }

        public final void unregister() {
            this.callAppUid = null;
            this.isRegistered = false;
            OngoingCallController.this.iActivityManager.unregisterUidObserver(OngoingCallController.this.uidObserver);
        }

        public void onUidStateChanged(int i, int i2, long j, int i3) {
            Integer num = this.callAppUid;
            if (num != null && i == num.intValue()) {
                boolean z = this.isCallAppVisible;
                boolean access$isProcessVisibleToUser = OngoingCallController.this.isProcessVisibleToUser(i2);
                this.isCallAppVisible = access$isProcessVisibleToUser;
                if (z != access$isProcessVisibleToUser) {
                    OngoingCallController.this.mainExecutor.execute(new C3139x48738f01(OngoingCallController.this));
                }
            }
        }

        /* access modifiers changed from: private */
        /* renamed from: onUidStateChanged$lambda-1  reason: not valid java name */
        public static final void m3219onUidStateChanged$lambda1(OngoingCallController ongoingCallController) {
            Intrinsics.checkNotNullParameter(ongoingCallController, "this$0");
            for (OngoingCallListener onOngoingCallStateChanged : ongoingCallController.mListeners) {
                onOngoingCallStateChanged.onOngoingCallStateChanged(true);
            }
        }
    }
}
