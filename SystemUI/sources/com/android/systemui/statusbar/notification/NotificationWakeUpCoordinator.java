package com.android.systemui.statusbar.notification;

import android.animation.ObjectAnimator;
import android.util.FloatProperty;
import android.view.animation.Interpolator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.PanelExpansionListener;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: NotificationWakeUpCoordinator.kt */
/* loaded from: classes.dex */
public final class NotificationWakeUpCoordinator implements OnHeadsUpChangedListener, StatusBarStateController.StateListener, PanelExpansionListener {
    @NotNull
    private final KeyguardBypassController bypassController;
    private boolean collapsedEnoughToHide;
    @NotNull
    private final DozeParameters dozeParameters;
    private boolean fullyAwake;
    private float mDozeAmount;
    @NotNull
    private final HeadsUpManager mHeadsUpManager;
    private float mLinearDozeAmount;
    private float mLinearVisibilityAmount;
    private float mNotificationVisibleAmount;
    private boolean mNotificationsVisible;
    private boolean mNotificationsVisibleForExpansion;
    private NotificationStackScrollLayoutController mStackScrollerController;
    private float mVisibilityAmount;
    @Nullable
    private ObjectAnimator mVisibilityAnimator;
    private boolean notificationsFullyHidden;
    private boolean pulseExpanding;
    private boolean pulsing;
    @NotNull
    private final StatusBarStateController statusBarStateController;
    @NotNull
    private final UnlockedScreenOffAnimationController unlockedScreenOffAnimationController;
    private boolean wakingUp;
    private boolean willWakeUp;
    @NotNull
    private final NotificationWakeUpCoordinator$mNotificationVisibility$1 mNotificationVisibility = new FloatProperty<NotificationWakeUpCoordinator>() { // from class: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator$mNotificationVisibility$1
        @Override // android.util.FloatProperty
        public void setValue(@NotNull NotificationWakeUpCoordinator coordinator, float f) {
            Intrinsics.checkNotNullParameter(coordinator, "coordinator");
            coordinator.setVisibilityAmount(f);
        }

        @Override // android.util.Property
        @Nullable
        public Float get(@NotNull NotificationWakeUpCoordinator coordinator) {
            float f;
            Intrinsics.checkNotNullParameter(coordinator, "coordinator");
            f = coordinator.mLinearVisibilityAmount;
            return Float.valueOf(f);
        }
    };
    private Interpolator mVisibilityInterpolator = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
    @NotNull
    private final Set<NotificationEntry> mEntrySetToClearWhenFinished = new LinkedHashSet();
    @NotNull
    private final ArrayList<WakeUpListener> wakeUpListeners = new ArrayList<>();
    private int state = 1;

    /* compiled from: NotificationWakeUpCoordinator.kt */
    /* loaded from: classes.dex */
    public interface WakeUpListener {
        default void onFullyHiddenChanged(boolean z) {
        }

        default void onPulseExpansionChanged(boolean z) {
        }
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator$mNotificationVisibility$1] */
    public NotificationWakeUpCoordinator(@NotNull HeadsUpManager mHeadsUpManager, @NotNull StatusBarStateController statusBarStateController, @NotNull KeyguardBypassController bypassController, @NotNull DozeParameters dozeParameters, @NotNull UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        Intrinsics.checkNotNullParameter(mHeadsUpManager, "mHeadsUpManager");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(bypassController, "bypassController");
        Intrinsics.checkNotNullParameter(dozeParameters, "dozeParameters");
        Intrinsics.checkNotNullParameter(unlockedScreenOffAnimationController, "unlockedScreenOffAnimationController");
        this.mHeadsUpManager = mHeadsUpManager;
        this.statusBarStateController = statusBarStateController;
        this.bypassController = bypassController;
        this.dozeParameters = dozeParameters;
        this.unlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        mHeadsUpManager.addListener(this);
        statusBarStateController.addCallback(this);
        addListener(new WakeUpListener() { // from class: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.1
            @Override // com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.WakeUpListener
            public void onFullyHiddenChanged(boolean z) {
                if (!z || !NotificationWakeUpCoordinator.this.mNotificationsVisibleForExpansion) {
                    return;
                }
                NotificationWakeUpCoordinator.this.setNotificationsVisibleForExpansion(false, false, false);
            }
        });
    }

    public final void setFullyAwake(boolean z) {
        this.fullyAwake = z;
    }

    public final void setWakingUp(boolean z) {
        this.wakingUp = z;
        setWillWakeUp(false);
        if (z) {
            if (this.mNotificationsVisible && !this.mNotificationsVisibleForExpansion && !this.bypassController.getBypassEnabled()) {
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
                if (notificationStackScrollLayoutController == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
                    throw null;
                }
                notificationStackScrollLayoutController.wakeUpFromPulse();
            }
            if (!this.bypassController.getBypassEnabled() || this.mNotificationsVisible) {
                return;
            }
            updateNotificationVisibility(shouldAnimateVisibility(), false);
        }
    }

    public final void setWillWakeUp(boolean z) {
        if (z) {
            if (this.mDozeAmount == 0.0f) {
                return;
            }
        }
        this.willWakeUp = z;
    }

    public final void setPulsing(boolean z) {
        this.pulsing = z;
        if (z) {
            updateNotificationVisibility(shouldAnimateVisibility(), false);
        }
    }

    public final boolean getNotificationsFullyHidden() {
        return this.notificationsFullyHidden;
    }

    private final void setNotificationsFullyHidden(boolean z) {
        if (this.notificationsFullyHidden != z) {
            this.notificationsFullyHidden = z;
            Iterator<WakeUpListener> it = this.wakeUpListeners.iterator();
            while (it.hasNext()) {
                it.next().onFullyHiddenChanged(z);
            }
        }
    }

    public final boolean getCanShowPulsingHuns() {
        boolean z = this.pulsing;
        if (this.bypassController.getBypassEnabled()) {
            boolean z2 = z || ((this.wakingUp || this.willWakeUp || this.fullyAwake) && this.statusBarStateController.getState() == 1);
            if (!this.collapsedEnoughToHide) {
                return z2;
            }
            return false;
        }
        return z;
    }

    public final void setStackScroller(@NotNull NotificationStackScrollLayoutController stackScrollerController) {
        Intrinsics.checkNotNullParameter(stackScrollerController, "stackScrollerController");
        this.mStackScrollerController = stackScrollerController;
        this.pulseExpanding = stackScrollerController.isPulseExpanding();
        stackScrollerController.setOnPulseHeightChangedListener(new Runnable() { // from class: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator$setStackScroller$1
            @Override // java.lang.Runnable
            public final void run() {
                boolean z;
                ArrayList arrayList;
                boolean isPulseExpanding = NotificationWakeUpCoordinator.this.isPulseExpanding();
                z = NotificationWakeUpCoordinator.this.pulseExpanding;
                boolean z2 = isPulseExpanding != z;
                NotificationWakeUpCoordinator.this.pulseExpanding = isPulseExpanding;
                arrayList = NotificationWakeUpCoordinator.this.wakeUpListeners;
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    ((NotificationWakeUpCoordinator.WakeUpListener) it.next()).onPulseExpansionChanged(z2);
                }
            }
        });
    }

    public final boolean isPulseExpanding() {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController != null) {
            return notificationStackScrollLayoutController.isPulseExpanding();
        }
        Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
        throw null;
    }

    public final void setNotificationsVisibleForExpansion(boolean z, boolean z2, boolean z3) {
        this.mNotificationsVisibleForExpansion = z;
        updateNotificationVisibility(z2, z3);
        if (z || !this.mNotificationsVisible) {
            return;
        }
        this.mHeadsUpManager.releaseAllImmediately();
    }

    public final void addListener(@NotNull WakeUpListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.wakeUpListeners.add(listener);
    }

    public final void removeListener(@NotNull WakeUpListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.wakeUpListeners.remove(listener);
    }

    private final void updateNotificationVisibility(boolean z, boolean z2) {
        boolean z3 = false;
        boolean z4 = (this.mNotificationsVisibleForExpansion || this.mHeadsUpManager.hasNotifications()) && getCanShowPulsingHuns();
        if (!z4 && this.mNotificationsVisible && (this.wakingUp || this.willWakeUp)) {
            if (this.mDozeAmount == 0.0f) {
                z3 = true;
            }
            if (!z3) {
                return;
            }
        }
        setNotificationsVisible(z4, z, z2);
    }

    private final void setNotificationsVisible(boolean z, boolean z2, boolean z3) {
        if (this.mNotificationsVisible == z) {
            return;
        }
        this.mNotificationsVisible = z;
        ObjectAnimator objectAnimator = this.mVisibilityAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        if (z2) {
            notifyAnimationStart(z);
            startVisibilityAnimation(z3);
            return;
        }
        setVisibilityAmount(z ? 1.0f : 0.0f);
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0037, code lost:
        if ((r4 == 1.0f) != false) goto L25;
     */
    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDozeAmountChanged(float f, float f2) {
        if (!overrideDozeAmountIfAnimatingScreenOff(f) && !overrideDozeAmountIfBypass()) {
            boolean z = true;
            if (!(f == 1.0f)) {
                if (!(f == 0.0f)) {
                    float f3 = this.mLinearDozeAmount;
                    if (!(f3 == 0.0f)) {
                    }
                    if (f3 != 1.0f) {
                        z = false;
                    }
                    notifyAnimationStart(z);
                }
            }
            setDozeAmount(f, f2);
        }
    }

    public final void setDozeAmount(float f, float f2) {
        boolean z = true;
        boolean z2 = !(f == this.mLinearDozeAmount);
        this.mLinearDozeAmount = f;
        this.mDozeAmount = f2;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
            throw null;
        }
        notificationStackScrollLayoutController.setDozeAmount(f2);
        updateHideAmount();
        if (!z2) {
            return;
        }
        if (f != 0.0f) {
            z = false;
        }
        if (!z) {
            return;
        }
        setNotificationsVisible(false, false, false);
        setNotificationsVisibleForExpansion(false, false, false);
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStateChanged(int i) {
        if (this.dozeParameters.shouldControlUnlockedScreenOff() && this.unlockedScreenOffAnimationController.isScreenOffAnimationPlaying() && this.state == 1 && i == 0) {
            setDozeAmount(0.0f, 0.0f);
        }
        if (!overrideDozeAmountIfAnimatingScreenOff(this.mLinearDozeAmount) && !overrideDozeAmountIfBypass()) {
            if (this.bypassController.getBypassEnabled() && i == 1 && this.state == 2 && (!this.statusBarStateController.isDozing() || shouldAnimateVisibility())) {
                setNotificationsVisible(true, false, false);
                setNotificationsVisible(false, true, false);
            }
            this.state = i;
        }
    }

    @Override // com.android.systemui.statusbar.phone.PanelExpansionListener
    public void onPanelExpansionChanged(float f, boolean z) {
        boolean z2 = f <= 0.9f;
        if (z2 != this.collapsedEnoughToHide) {
            boolean canShowPulsingHuns = getCanShowPulsingHuns();
            this.collapsedEnoughToHide = z2;
            if (!canShowPulsingHuns || getCanShowPulsingHuns()) {
                return;
            }
            updateNotificationVisibility(true, true);
            this.mHeadsUpManager.releaseAllImmediately();
        }
    }

    private final boolean overrideDozeAmountIfBypass() {
        if (this.bypassController.getBypassEnabled()) {
            float f = 1.0f;
            if (this.statusBarStateController.getState() == 0 || this.statusBarStateController.getState() == 2) {
                f = 0.0f;
            }
            setDozeAmount(f, f);
            return true;
        }
        return false;
    }

    private final boolean overrideDozeAmountIfAnimatingScreenOff(float f) {
        if (this.unlockedScreenOffAnimationController.isScreenOffAnimationPlaying()) {
            setDozeAmount(1.0f, 1.0f);
            return true;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0017, code lost:
        if ((r0 == 1.0f) != false) goto L20;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void startVisibilityAnimation(boolean z) {
        Interpolator interpolator;
        float f = this.mNotificationVisibleAmount;
        float f2 = 0.0f;
        if (!(f == 0.0f)) {
        }
        if (this.mNotificationsVisible) {
            interpolator = Interpolators.TOUCH_RESPONSE;
        } else {
            interpolator = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
        }
        this.mVisibilityInterpolator = interpolator;
        if (this.mNotificationsVisible) {
            f2 = 1.0f;
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, this.mNotificationVisibility, f2);
        ofFloat.setInterpolator(Interpolators.LINEAR);
        long j = 500;
        if (z) {
            j = ((float) 500) / 1.5f;
        }
        ofFloat.setDuration(j);
        ofFloat.start();
        this.mVisibilityAnimator = ofFloat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setVisibilityAmount(float f) {
        this.mLinearVisibilityAmount = f;
        this.mVisibilityAmount = this.mVisibilityInterpolator.getInterpolation(f);
        handleAnimationFinished();
        updateHideAmount();
    }

    private final void handleAnimationFinished() {
        boolean z = true;
        if (!(this.mLinearDozeAmount == 0.0f)) {
            if (this.mLinearVisibilityAmount != 0.0f) {
                z = false;
            }
            if (!z) {
                return;
            }
        }
        for (NotificationEntry notificationEntry : this.mEntrySetToClearWhenFinished) {
            notificationEntry.setHeadsUpAnimatingAway(false);
        }
        this.mEntrySetToClearWhenFinished.clear();
    }

    private final void updateHideAmount() {
        float min = Math.min(1.0f - this.mLinearVisibilityAmount, this.mLinearDozeAmount);
        float min2 = Math.min(1.0f - this.mVisibilityAmount, this.mDozeAmount);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
            throw null;
        }
        notificationStackScrollLayoutController.setHideAmount(min, min2);
        setNotificationsFullyHidden(min == 1.0f);
    }

    private final void notifyAnimationStart(boolean z) {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController != null) {
            notificationStackScrollLayoutController.notifyHideAnimationStart(!z);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
            throw null;
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozingChanged(boolean z) {
        if (z) {
            setNotificationsVisible(false, false, false);
        }
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public void onHeadsUpStateChanged(@NotNull NotificationEntry entry, boolean z) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        boolean shouldAnimateVisibility = shouldAnimateVisibility();
        if (!z) {
            if (!(this.mLinearDozeAmount == 0.0f)) {
                if (!(this.mLinearVisibilityAmount == 0.0f)) {
                    if (entry.isRowDismissed()) {
                        shouldAnimateVisibility = false;
                    } else if (!this.wakingUp && !this.willWakeUp) {
                        entry.setHeadsUpAnimatingAway(true);
                        this.mEntrySetToClearWhenFinished.add(entry);
                    }
                }
            }
        } else if (this.mEntrySetToClearWhenFinished.contains(entry)) {
            this.mEntrySetToClearWhenFinished.remove(entry);
            entry.setHeadsUpAnimatingAway(false);
        }
        updateNotificationVisibility(shouldAnimateVisibility, false);
    }

    private final boolean shouldAnimateVisibility() {
        return this.dozeParameters.getAlwaysOn() && !this.dozeParameters.getDisplayNeedsBlanking();
    }
}
