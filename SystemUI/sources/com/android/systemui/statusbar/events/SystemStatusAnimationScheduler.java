package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Process;
import android.provider.DeviceConfig;
import android.view.View;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.phone.StatusBarWindowController;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SystemStatusAnimationScheduler.kt */
/* loaded from: classes.dex */
public final class SystemStatusAnimationScheduler implements CallbackController<SystemStatusAnimationCallback>, Dumpable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private int animationState;
    @Nullable
    private Runnable cancelExecutionRunnable;
    @NotNull
    private final SystemEventChipAnimationController chipAnimationController;
    @NotNull
    private final SystemEventCoordinator coordinator;
    @NotNull
    private final DumpManager dumpManager;
    @NotNull
    private final DelayableExecutor executor;
    private boolean hasPersistentDot;
    @Nullable
    private StatusEvent scheduledEvent;
    @NotNull
    private final StatusBarWindowController statusBarWindowController;
    @NotNull
    private final SystemClock systemClock;
    @NotNull
    private final Set<SystemStatusAnimationCallback> listeners = new LinkedHashSet();
    @NotNull
    private final ValueAnimator.AnimatorUpdateListener systemUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$systemUpdateListener$1
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator anim) {
            SystemStatusAnimationScheduler systemStatusAnimationScheduler = SystemStatusAnimationScheduler.this;
            Intrinsics.checkNotNullExpressionValue(anim, "anim");
            systemStatusAnimationScheduler.notifySystemAnimationUpdate(anim);
        }
    };
    @NotNull
    private final SystemStatusAnimationScheduler$systemAnimatorAdapter$1 systemAnimatorAdapter = new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$systemAnimatorAdapter$1
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(@Nullable Animator animator) {
            SystemStatusAnimationScheduler.this.notifySystemFinish();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(@Nullable Animator animator) {
            SystemStatusAnimationScheduler.this.notifySystemStart();
        }
    };
    @NotNull
    private final ValueAnimator.AnimatorUpdateListener chipUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$chipUpdateListener$1
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator anim) {
            SystemEventChipAnimationController systemEventChipAnimationController = SystemStatusAnimationScheduler.this.chipAnimationController;
            Intrinsics.checkNotNullExpressionValue(anim, "anim");
            systemEventChipAnimationController.onChipAnimationUpdate(anim, SystemStatusAnimationScheduler.this.getAnimationState());
        }
    };

    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$systemAnimatorAdapter$1] */
    public SystemStatusAnimationScheduler(@NotNull SystemEventCoordinator coordinator, @NotNull SystemEventChipAnimationController chipAnimationController, @NotNull StatusBarWindowController statusBarWindowController, @NotNull DumpManager dumpManager, @NotNull SystemClock systemClock, @NotNull DelayableExecutor executor) {
        Intrinsics.checkNotNullParameter(coordinator, "coordinator");
        Intrinsics.checkNotNullParameter(chipAnimationController, "chipAnimationController");
        Intrinsics.checkNotNullParameter(statusBarWindowController, "statusBarWindowController");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(executor, "executor");
        this.coordinator = coordinator;
        this.chipAnimationController = chipAnimationController;
        this.statusBarWindowController = statusBarWindowController;
        this.dumpManager = dumpManager;
        this.systemClock = systemClock;
        this.executor = executor;
        coordinator.attachScheduler(this);
        dumpManager.registerDumpable("SystemStatusAnimationScheduler", this);
    }

    /* compiled from: SystemStatusAnimationScheduler.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final boolean isImmersiveIndicatorEnabled() {
        return DeviceConfig.getBoolean("privacy", "enable_immersive_indicator", true);
    }

    public final int getAnimationState() {
        return this.animationState;
    }

    public final boolean getHasPersistentDot() {
        return this.hasPersistentDot;
    }

    public final void onStatusEvent(@NotNull StatusEvent event) {
        int i;
        Intrinsics.checkNotNullParameter(event, "event");
        if (isTooEarly() || !isImmersiveIndicatorEnabled()) {
            return;
        }
        Assert.isMainThread();
        int priority = event.getPriority();
        StatusEvent statusEvent = this.scheduledEvent;
        if (priority > (statusEvent == null ? -1 : statusEvent.getPriority()) && (i = this.animationState) != 3 && i != 4 && event.getForceVisible()) {
            scheduleEvent(event);
            return;
        }
        StatusEvent statusEvent2 = this.scheduledEvent;
        if (!Intrinsics.areEqual(statusEvent2 == null ? null : Boolean.valueOf(statusEvent2.shouldUpdateFromEvent(event)), Boolean.TRUE)) {
            return;
        }
        StatusEvent statusEvent3 = this.scheduledEvent;
        if (statusEvent3 != null) {
            statusEvent3.updateFromEvent(event);
        }
        if (!event.getForceVisible()) {
            return;
        }
        this.hasPersistentDot = true;
        notifyTransitionToPersistentDot();
    }

    private final void clearDotIfVisible() {
        notifyHidePersistentDot();
    }

    public final void setShouldShowPersistentPrivacyIndicator(boolean z) {
        if (this.hasPersistentDot == z || !isImmersiveIndicatorEnabled()) {
            return;
        }
        this.hasPersistentDot = z;
        if (z) {
            return;
        }
        clearDotIfVisible();
    }

    private final boolean isTooEarly() {
        return this.systemClock.uptimeMillis() - Process.getStartUptimeMillis() < 5000;
    }

    private final void scheduleEvent(StatusEvent statusEvent) {
        this.scheduledEvent = statusEvent;
        if (statusEvent.getForceVisible()) {
            this.hasPersistentDot = true;
        }
        if (!statusEvent.getShowAnimation() && statusEvent.getForceVisible()) {
            notifyTransitionToPersistentDot();
            this.scheduledEvent = null;
            return;
        }
        this.cancelExecutionRunnable = this.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$scheduleEvent$1
            @Override // java.lang.Runnable
            public final void run() {
                StatusBarWindowController statusBarWindowController;
                SystemStatusAnimationScheduler$systemAnimatorAdapter$1 systemStatusAnimationScheduler$systemAnimatorAdapter$1;
                ValueAnimator.AnimatorUpdateListener animatorUpdateListener;
                StatusEvent statusEvent2;
                ValueAnimator.AnimatorUpdateListener animatorUpdateListener2;
                DelayableExecutor delayableExecutor;
                SystemStatusAnimationScheduler.this.cancelExecutionRunnable = null;
                SystemStatusAnimationScheduler.this.animationState = 1;
                statusBarWindowController = SystemStatusAnimationScheduler.this.statusBarWindowController;
                statusBarWindowController.setForceStatusBarVisible(true);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
                ofFloat.setDuration(250L);
                systemStatusAnimationScheduler$systemAnimatorAdapter$1 = SystemStatusAnimationScheduler.this.systemAnimatorAdapter;
                ofFloat.addListener(systemStatusAnimationScheduler$systemAnimatorAdapter$1);
                animatorUpdateListener = SystemStatusAnimationScheduler.this.systemUpdateListener;
                ofFloat.addUpdateListener(animatorUpdateListener);
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat2.setDuration(250L);
                SystemStatusAnimationScheduler systemStatusAnimationScheduler = SystemStatusAnimationScheduler.this;
                statusEvent2 = systemStatusAnimationScheduler.scheduledEvent;
                Intrinsics.checkNotNull(statusEvent2);
                ofFloat2.addListener(new SystemStatusAnimationScheduler.ChipAnimatorAdapter(systemStatusAnimationScheduler, 2, statusEvent2.getViewCreator()));
                animatorUpdateListener2 = SystemStatusAnimationScheduler.this.chipUpdateListener;
                ofFloat2.addUpdateListener(animatorUpdateListener2);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(ofFloat, ofFloat2);
                animatorSet.start();
                delayableExecutor = SystemStatusAnimationScheduler.this.executor;
                final SystemStatusAnimationScheduler systemStatusAnimationScheduler2 = SystemStatusAnimationScheduler.this;
                delayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$scheduleEvent$1.1
                    /* JADX WARN: Code restructure failed: missing block: B:6:0x0071, code lost:
                        r4 = r1.notifyTransitionToPersistentDot();
                     */
                    @Override // java.lang.Runnable
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                    */
                    public final void run() {
                        SystemStatusAnimationScheduler$systemAnimatorAdapter$1 systemStatusAnimationScheduler$systemAnimatorAdapter$12;
                        ValueAnimator.AnimatorUpdateListener animatorUpdateListener3;
                        StatusEvent statusEvent3;
                        ValueAnimator.AnimatorUpdateListener animatorUpdateListener4;
                        StatusBarWindowController statusBarWindowController2;
                        Animator notifyTransitionToPersistentDot;
                        SystemStatusAnimationScheduler.this.animationState = 3;
                        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(0.0f, 1.0f);
                        ofFloat3.setDuration(250L);
                        systemStatusAnimationScheduler$systemAnimatorAdapter$12 = SystemStatusAnimationScheduler.this.systemAnimatorAdapter;
                        ofFloat3.addListener(systemStatusAnimationScheduler$systemAnimatorAdapter$12);
                        animatorUpdateListener3 = SystemStatusAnimationScheduler.this.systemUpdateListener;
                        ofFloat3.addUpdateListener(animatorUpdateListener3);
                        ValueAnimator ofFloat4 = ValueAnimator.ofFloat(1.0f, 0.0f);
                        ofFloat4.setDuration(250L);
                        int i = SystemStatusAnimationScheduler.this.getHasPersistentDot() ? 4 : 0;
                        SystemStatusAnimationScheduler systemStatusAnimationScheduler3 = SystemStatusAnimationScheduler.this;
                        statusEvent3 = systemStatusAnimationScheduler3.scheduledEvent;
                        Intrinsics.checkNotNull(statusEvent3);
                        ofFloat4.addListener(new SystemStatusAnimationScheduler.ChipAnimatorAdapter(systemStatusAnimationScheduler3, i, statusEvent3.getViewCreator()));
                        animatorUpdateListener4 = SystemStatusAnimationScheduler.this.chipUpdateListener;
                        ofFloat4.addUpdateListener(animatorUpdateListener4);
                        AnimatorSet animatorSet2 = new AnimatorSet();
                        animatorSet2.play(ofFloat4).before(ofFloat3);
                        if (SystemStatusAnimationScheduler.this.getHasPersistentDot() && notifyTransitionToPersistentDot != null) {
                            animatorSet2.playTogether(ofFloat3, notifyTransitionToPersistentDot);
                        }
                        animatorSet2.start();
                        statusBarWindowController2 = SystemStatusAnimationScheduler.this.statusBarWindowController;
                        statusBarWindowController2.setForceStatusBarVisible(false);
                        SystemStatusAnimationScheduler.this.scheduledEvent = null;
                    }
                }, 1500L);
            }
        }, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Animator notifyTransitionToPersistentDot() {
        Set<SystemStatusAnimationCallback> set = this.listeners;
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = set.iterator();
        while (true) {
            String str = null;
            if (!it.hasNext()) {
                break;
            }
            SystemStatusAnimationCallback systemStatusAnimationCallback = (SystemStatusAnimationCallback) it.next();
            StatusEvent statusEvent = this.scheduledEvent;
            if (statusEvent != null) {
                str = statusEvent.getContentDescription();
            }
            Animator onSystemStatusAnimationTransitionToPersistentDot = systemStatusAnimationCallback.onSystemStatusAnimationTransitionToPersistentDot(str);
            if (onSystemStatusAnimationTransitionToPersistentDot != null) {
                arrayList.add(onSystemStatusAnimationTransitionToPersistentDot);
            }
        }
        if (!arrayList.isEmpty()) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(arrayList);
            return animatorSet;
        }
        return null;
    }

    private final Animator notifyHidePersistentDot() {
        Set<SystemStatusAnimationCallback> set = this.listeners;
        ArrayList arrayList = new ArrayList();
        for (SystemStatusAnimationCallback systemStatusAnimationCallback : set) {
            Animator onHidePersistentDot = systemStatusAnimationCallback.onHidePersistentDot();
            if (onHidePersistentDot != null) {
                arrayList.add(onHidePersistentDot);
            }
        }
        if (this.animationState == 4) {
            this.animationState = 0;
        }
        if (!arrayList.isEmpty()) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(arrayList);
            return animatorSet;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void notifySystemStart() {
        for (SystemStatusAnimationCallback systemStatusAnimationCallback : this.listeners) {
            systemStatusAnimationCallback.onSystemChromeAnimationStart();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void notifySystemFinish() {
        for (SystemStatusAnimationCallback systemStatusAnimationCallback : this.listeners) {
            systemStatusAnimationCallback.onSystemChromeAnimationEnd();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void notifySystemAnimationUpdate(ValueAnimator valueAnimator) {
        for (SystemStatusAnimationCallback systemStatusAnimationCallback : this.listeners) {
            systemStatusAnimationCallback.onSystemChromeAnimationUpdate(valueAnimator);
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(@NotNull SystemStatusAnimationCallback listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        Assert.isMainThread();
        if (this.listeners.isEmpty()) {
            this.coordinator.startObserving();
        }
        this.listeners.add(listener);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(@NotNull SystemStatusAnimationCallback listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        Assert.isMainThread();
        this.listeners.remove(listener);
        if (this.listeners.isEmpty()) {
            this.coordinator.stopObserving();
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println(Intrinsics.stringPlus("Scheduled event: ", this.scheduledEvent));
        pw.println(Intrinsics.stringPlus("Has persistent privacy dot: ", Boolean.valueOf(this.hasPersistentDot)));
        pw.println(Intrinsics.stringPlus("Animation state: ", Integer.valueOf(this.animationState)));
        pw.println("Listeners:");
        if (this.listeners.isEmpty()) {
            pw.println("(none)");
            return;
        }
        for (SystemStatusAnimationCallback systemStatusAnimationCallback : this.listeners) {
            pw.println(Intrinsics.stringPlus("  ", systemStatusAnimationCallback));
        }
    }

    /* compiled from: SystemStatusAnimationScheduler.kt */
    /* loaded from: classes.dex */
    public final class ChipAnimatorAdapter extends AnimatorListenerAdapter {
        private final int endState;
        final /* synthetic */ SystemStatusAnimationScheduler this$0;
        @NotNull
        private final Function1<Context, View> viewCreator;

        /* JADX WARN: Multi-variable type inference failed */
        public ChipAnimatorAdapter(SystemStatusAnimationScheduler this$0, @NotNull int i, Function1<? super Context, ? extends View> viewCreator) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(viewCreator, "viewCreator");
            this.this$0 = this$0;
            this.endState = i;
            this.viewCreator = viewCreator;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(@Nullable Animator animator) {
            this.this$0.chipAnimationController.onChipAnimationEnd(this.this$0.getAnimationState());
            SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.this$0;
            systemStatusAnimationScheduler.animationState = (this.endState != 4 || systemStatusAnimationScheduler.getHasPersistentDot()) ? this.endState : 0;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(@Nullable Animator animator) {
            this.this$0.chipAnimationController.onChipAnimationStart(this.viewCreator, this.this$0.getAnimationState());
        }
    }
}
