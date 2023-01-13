package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.os.Process;
import android.provider.DeviceConfig;
import androidx.core.app.NotificationCompat;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\b\u0007\u0018\u0000 <2\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001<B9\b\u0007\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\b\b\u0001\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\u0010\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0002H\u0016J\b\u0010%\u001a\u00020#H\u0002J\b\u0010&\u001a\u00020'H\u0002J\b\u0010(\u001a\u00020'H\u0002J%\u0010)\u001a\u00020#2\u0006\u0010*\u001a\u00020+2\u000e\u0010,\u001a\n\u0012\u0006\b\u0001\u0012\u00020.0-H\u0016¢\u0006\u0002\u0010/J\b\u00100\u001a\u00020\u001aH\u0002J\b\u00101\u001a\u00020\u001aH\u0002J\n\u00102\u001a\u0004\u0018\u000103H\u0002J\n\u00104\u001a\u0004\u0018\u000103H\u0002J\u000e\u00105\u001a\u00020#2\u0006\u00106\u001a\u00020!J\u0010\u00107\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0002H\u0016J\b\u00108\u001a\u00020#H\u0002J\u0010\u00109\u001a\u00020#2\u0006\u00106\u001a\u00020!H\u0002J\u000e\u0010:\u001a\u00020#2\u0006\u0010;\u001a\u00020\u001aR$\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u0012@BX\u000e¢\u0006\u000e\n\u0000\u0012\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0016\u0010\u0017R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u0011\u001a\u00020\u001a@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0014\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00020\u001fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000¨\u0006="}, mo65043d2 = {"Lcom/android/systemui/statusbar/events/SystemStatusAnimationScheduler;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/statusbar/events/SystemStatusAnimationCallback;", "Lcom/android/systemui/Dumpable;", "coordinator", "Lcom/android/systemui/statusbar/events/SystemEventCoordinator;", "chipAnimationController", "Lcom/android/systemui/statusbar/events/SystemEventChipAnimationController;", "statusBarWindowController", "Lcom/android/systemui/statusbar/window/StatusBarWindowController;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "executor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "(Lcom/android/systemui/statusbar/events/SystemEventCoordinator;Lcom/android/systemui/statusbar/events/SystemEventChipAnimationController;Lcom/android/systemui/statusbar/window/StatusBarWindowController;Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/util/concurrency/DelayableExecutor;)V", "<set-?>", "", "animationState", "getAnimationState$annotations", "()V", "getAnimationState", "()I", "cancelExecutionRunnable", "Ljava/lang/Runnable;", "", "hasPersistentDot", "getHasPersistentDot", "()Z", "listeners", "", "scheduledEvent", "Lcom/android/systemui/statusbar/events/StatusEvent;", "addCallback", "", "listener", "clearDotIfVisible", "collectFinishAnimations", "Landroid/animation/AnimatorSet;", "collectStartAnimations", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "isImmersiveIndicatorEnabled", "isTooEarly", "notifyHidePersistentDot", "Landroid/animation/Animator;", "notifyTransitionToPersistentDot", "onStatusEvent", "event", "removeCallback", "runChipAnimation", "scheduleEvent", "setShouldShowPersistentPrivacyIndicator", "should", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SystemStatusAnimationScheduler.kt */
public final class SystemStatusAnimationScheduler implements CallbackController<SystemStatusAnimationCallback>, Dumpable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String PROPERTY_ENABLE_IMMERSIVE_INDICATOR = "enable_immersive_indicator";
    /* access modifiers changed from: private */
    public int animationState;
    private Runnable cancelExecutionRunnable;
    private final SystemEventChipAnimationController chipAnimationController;
    private final SystemEventCoordinator coordinator;
    private final DumpManager dumpManager;
    /* access modifiers changed from: private */
    public final DelayableExecutor executor;
    private boolean hasPersistentDot;
    private final Set<SystemStatusAnimationCallback> listeners = new LinkedHashSet();
    private StatusEvent scheduledEvent;
    /* access modifiers changed from: private */
    public final StatusBarWindowController statusBarWindowController;
    private final SystemClock systemClock;

    public static /* synthetic */ void getAnimationState$annotations() {
    }

    @Inject
    public SystemStatusAnimationScheduler(SystemEventCoordinator systemEventCoordinator, SystemEventChipAnimationController systemEventChipAnimationController, StatusBarWindowController statusBarWindowController2, DumpManager dumpManager2, SystemClock systemClock2, @Main DelayableExecutor delayableExecutor) {
        Intrinsics.checkNotNullParameter(systemEventCoordinator, "coordinator");
        Intrinsics.checkNotNullParameter(systemEventChipAnimationController, "chipAnimationController");
        Intrinsics.checkNotNullParameter(statusBarWindowController2, "statusBarWindowController");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
        Intrinsics.checkNotNullParameter(delayableExecutor, "executor");
        this.coordinator = systemEventCoordinator;
        this.chipAnimationController = systemEventChipAnimationController;
        this.statusBarWindowController = statusBarWindowController2;
        this.dumpManager = dumpManager2;
        this.systemClock = systemClock2;
        this.executor = delayableExecutor;
        systemEventCoordinator.attachScheduler(this);
        dumpManager2.registerDumpable("SystemStatusAnimationScheduler", this);
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/statusbar/events/SystemStatusAnimationScheduler$Companion;", "", "()V", "PROPERTY_ENABLE_IMMERSIVE_INDICATOR", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: SystemStatusAnimationScheduler.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final boolean isImmersiveIndicatorEnabled() {
        return DeviceConfig.getBoolean("privacy", PROPERTY_ENABLE_IMMERSIVE_INDICATOR, true);
    }

    public final int getAnimationState() {
        return this.animationState;
    }

    public final boolean getHasPersistentDot() {
        return this.hasPersistentDot;
    }

    public final void onStatusEvent(StatusEvent statusEvent) {
        Intrinsics.checkNotNullParameter(statusEvent, NotificationCompat.CATEGORY_EVENT);
        if (!isTooEarly() && isImmersiveIndicatorEnabled()) {
            Assert.isMainThread();
            int priority = statusEvent.getPriority();
            StatusEvent statusEvent2 = this.scheduledEvent;
            if (priority <= (statusEvent2 != null ? statusEvent2.getPriority() : -1)) {
                StatusEvent statusEvent3 = this.scheduledEvent;
                boolean z = false;
                if (statusEvent3 != null && statusEvent3.shouldUpdateFromEvent(statusEvent)) {
                    z = true;
                }
                if (!z) {
                    return;
                }
            }
            if (statusEvent.getShowAnimation()) {
                scheduleEvent(statusEvent);
            } else if (statusEvent.getForceVisible()) {
                this.hasPersistentDot = true;
                if (this.animationState == 0) {
                    notifyTransitionToPersistentDot();
                }
            }
        }
    }

    private final void clearDotIfVisible() {
        notifyHidePersistentDot();
    }

    public final void setShouldShowPersistentPrivacyIndicator(boolean z) {
        if (this.hasPersistentDot != z && isImmersiveIndicatorEnabled()) {
            this.hasPersistentDot = z;
            if (!z) {
                clearDotIfVisible();
            }
        }
    }

    private final boolean isTooEarly() {
        return this.systemClock.uptimeMillis() - Process.getStartUptimeMillis() < 5000;
    }

    private final void scheduleEvent(StatusEvent statusEvent) {
        int i = this.animationState;
        if (i == 4) {
            return;
        }
        if (i != 5 || !statusEvent.getForceVisible()) {
            StatusEvent statusEvent2 = this.scheduledEvent;
            boolean z = false;
            if (statusEvent2 != null && statusEvent2.shouldUpdateFromEvent(statusEvent)) {
                z = true;
            }
            if (z) {
                StatusEvent statusEvent3 = this.scheduledEvent;
                if (statusEvent3 != null) {
                    statusEvent3.updateFromEvent(statusEvent);
                    return;
                }
                return;
            }
            this.scheduledEvent = statusEvent;
            Intrinsics.checkNotNull(statusEvent);
            if (statusEvent.getForceVisible()) {
                this.hasPersistentDot = true;
            }
            SystemEventChipAnimationController systemEventChipAnimationController = this.chipAnimationController;
            StatusEvent statusEvent4 = this.scheduledEvent;
            Intrinsics.checkNotNull(statusEvent4);
            systemEventChipAnimationController.prepareChipAnimation(statusEvent4.getViewCreator());
            this.animationState = 1;
            this.executor.executeDelayed(new SystemStatusAnimationScheduler$$ExternalSyntheticLambda0(this), 100);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: scheduleEvent$lambda-0  reason: not valid java name */
    public static final void m3081scheduleEvent$lambda0(SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        Intrinsics.checkNotNullParameter(systemStatusAnimationScheduler, "this$0");
        systemStatusAnimationScheduler.runChipAnimation();
    }

    private final void runChipAnimation() {
        this.executor.executeDelayed(new SystemStatusAnimationScheduler$$ExternalSyntheticLambda1(this), 50);
        this.animationState = 2;
        AnimatorSet collectStartAnimations = collectStartAnimations();
        if (collectStartAnimations.getTotalDuration() <= 500) {
            collectStartAnimations.addListener(new SystemStatusAnimationScheduler$runChipAnimation$2(this));
            collectStartAnimations.start();
            this.executor.executeDelayed(new SystemStatusAnimationScheduler$$ExternalSyntheticLambda2(this), 1000);
            return;
        }
        throw new IllegalStateException("System animation total length exceeds budget. Expected: 500, actual: " + collectStartAnimations.getTotalDuration());
    }

    /* access modifiers changed from: private */
    /* renamed from: runChipAnimation$lambda-1  reason: not valid java name */
    public static final void m3079runChipAnimation$lambda1(SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        Intrinsics.checkNotNullParameter(systemStatusAnimationScheduler, "this$0");
        systemStatusAnimationScheduler.statusBarWindowController.setForceStatusBarVisible(true);
    }

    /* access modifiers changed from: private */
    /* renamed from: runChipAnimation$lambda-2  reason: not valid java name */
    public static final void m3080runChipAnimation$lambda2(SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        Intrinsics.checkNotNullParameter(systemStatusAnimationScheduler, "this$0");
        AnimatorSet collectFinishAnimations = systemStatusAnimationScheduler.collectFinishAnimations();
        systemStatusAnimationScheduler.animationState = 4;
        collectFinishAnimations.addListener(new SystemStatusAnimationScheduler$runChipAnimation$3$1(systemStatusAnimationScheduler));
        collectFinishAnimations.start();
        systemStatusAnimationScheduler.scheduledEvent = null;
    }

    private final AnimatorSet collectStartAnimations() {
        List arrayList = new ArrayList();
        for (SystemStatusAnimationCallback onSystemEventAnimationBegin : this.listeners) {
            Animator onSystemEventAnimationBegin2 = onSystemEventAnimationBegin.onSystemEventAnimationBegin();
            if (onSystemEventAnimationBegin2 != null) {
                arrayList.add(onSystemEventAnimationBegin2);
            }
        }
        arrayList.add(this.chipAnimationController.onSystemEventAnimationBegin());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    private final AnimatorSet collectFinishAnimations() {
        Animator notifyTransitionToPersistentDot;
        List arrayList = new ArrayList();
        for (SystemStatusAnimationCallback onSystemEventAnimationFinish : this.listeners) {
            Animator onSystemEventAnimationFinish2 = onSystemEventAnimationFinish.onSystemEventAnimationFinish(this.hasPersistentDot);
            if (onSystemEventAnimationFinish2 != null) {
                arrayList.add(onSystemEventAnimationFinish2);
            }
        }
        arrayList.add(this.chipAnimationController.onSystemEventAnimationFinish(this.hasPersistentDot));
        if (this.hasPersistentDot && (notifyTransitionToPersistentDot = notifyTransitionToPersistentDot()) != null) {
            arrayList.add(notifyTransitionToPersistentDot);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    private final Animator notifyTransitionToPersistentDot() {
        Collection arrayList = new ArrayList();
        for (SystemStatusAnimationCallback onSystemStatusAnimationTransitionToPersistentDot : this.listeners) {
            Animator onSystemStatusAnimationTransitionToPersistentDot2 = onSystemStatusAnimationTransitionToPersistentDot.onSystemStatusAnimationTransitionToPersistentDot();
            if (onSystemStatusAnimationTransitionToPersistentDot2 != null) {
                arrayList.add(onSystemStatusAnimationTransitionToPersistentDot2);
            }
        }
        Collection collection = (List) arrayList;
        if (!(!collection.isEmpty())) {
            return null;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(collection);
        return animatorSet;
    }

    private final Animator notifyHidePersistentDot() {
        Collection arrayList = new ArrayList();
        for (SystemStatusAnimationCallback onHidePersistentDot : this.listeners) {
            Animator onHidePersistentDot2 = onHidePersistentDot.onHidePersistentDot();
            if (onHidePersistentDot2 != null) {
                arrayList.add(onHidePersistentDot2);
            }
        }
        List list = (List) arrayList;
        if (this.animationState == 5) {
            this.animationState = 0;
        }
        Collection collection = list;
        if (!(!collection.isEmpty())) {
            return null;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(collection);
        return animatorSet;
    }

    public void addCallback(SystemStatusAnimationCallback systemStatusAnimationCallback) {
        Intrinsics.checkNotNullParameter(systemStatusAnimationCallback, "listener");
        Assert.isMainThread();
        if (this.listeners.isEmpty()) {
            this.coordinator.startObserving();
        }
        this.listeners.add(systemStatusAnimationCallback);
    }

    public void removeCallback(SystemStatusAnimationCallback systemStatusAnimationCallback) {
        Intrinsics.checkNotNullParameter(systemStatusAnimationCallback, "listener");
        Assert.isMainThread();
        this.listeners.remove(systemStatusAnimationCallback);
        if (this.listeners.isEmpty()) {
            this.coordinator.stopObserving();
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("Scheduled event: " + this.scheduledEvent);
        printWriter.println("Has persistent privacy dot: " + this.hasPersistentDot);
        printWriter.println("Animation state: " + this.animationState);
        printWriter.println("Listeners:");
        if (this.listeners.isEmpty()) {
            printWriter.println("(none)");
            return;
        }
        for (SystemStatusAnimationCallback systemStatusAnimationCallback : this.listeners) {
            printWriter.println("  " + systemStatusAnimationCallback);
        }
    }
}
