package com.android.systemui.statusbar.notification;

import android.animation.ObjectAnimator;
import android.view.animation.Interpolator;
import androidx.core.app.NotificationCompat;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.JvmDefault;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0016*\u0001!\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003:\u0001hB/\b\u0007\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u000e\u0010B\u001a\u00020C2\u0006\u0010D\u001a\u00020:J\b\u0010E\u001a\u00020CH\u0002J\u0006\u0010F\u001a\u00020\u0010J\u0010\u0010G\u001a\u00020C2\u0006\u0010H\u001a\u00020\u0010H\u0002J\u0018\u0010I\u001a\u00020C2\u0006\u0010J\u001a\u00020\u001a2\u0006\u0010K\u001a\u00020\u001aH\u0016J\u0010\u0010L\u001a\u00020C2\u0006\u0010M\u001a\u00020\u0010H\u0016J\u0018\u0010N\u001a\u00020C2\u0006\u0010O\u001a\u00020\u001d2\u0006\u0010P\u001a\u00020\u0010H\u0016J\u0010\u0010Q\u001a\u00020C2\u0006\u0010R\u001a\u00020SH\u0016J\u0010\u0010T\u001a\u00020C2\u0006\u0010U\u001a\u000207H\u0016J\u0010\u0010V\u001a\u00020\u00102\u0006\u0010W\u001a\u00020\u001aH\u0002J\b\u0010X\u001a\u00020\u0010H\u0002J\u000e\u0010Y\u001a\u00020C2\u0006\u0010D\u001a\u00020:J\u0016\u0010Z\u001a\u00020C2\u0006\u0010J\u001a\u00020\u001a2\u0006\u0010K\u001a\u00020\u001aJ \u0010[\u001a\u00020C2\u0006\u0010\\\u001a\u00020\u00102\u0006\u0010]\u001a\u00020\u00102\u0006\u0010^\u001a\u00020\u0010H\u0002J\u001e\u0010_\u001a\u00020C2\u0006\u0010\\\u001a\u00020\u00102\u0006\u0010]\u001a\u00020\u00102\u0006\u0010^\u001a\u00020\u0010J\u000e\u0010`\u001a\u00020C2\u0006\u0010a\u001a\u00020'J\u0010\u0010b\u001a\u00020C2\u0006\u0010c\u001a\u00020\u001aH\u0002J\b\u0010d\u001a\u00020\u0010H\u0002J\u0010\u0010e\u001a\u00020C2\u0006\u0010^\u001a\u00020\u0010H\u0002J\b\u0010f\u001a\u00020CH\u0002J\u0018\u0010g\u001a\u00020C2\u0006\u0010]\u001a\u00020\u00102\u0006\u0010^\u001a\u00020\u0010H\u0002R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R \u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u00108F@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u00020\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0013\"\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u00020!X\u0004¢\u0006\u0004\n\u0002\u0010\"R\u000e\u0010#\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020'X.¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010+\u001a\n -*\u0004\u0018\u00010,0,X\u000e¢\u0006\u0002\n\u0000R$\u0010/\u001a\u00020\u00102\u0006\u0010.\u001a\u00020\u0010@BX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b0\u0010\u0013\"\u0004\b1\u0010\u0018R\u000e\u00102\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R$\u00103\u001a\u00020\u00102\u0006\u0010.\u001a\u00020\u0010@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\u0013\"\u0004\b5\u0010\u0018R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u00106\u001a\u000207X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u001e\u00108\u001a\u0012\u0012\u0004\u0012\u00020:09j\b\u0012\u0004\u0012\u00020:`;X\u0004¢\u0006\u0002\n\u0000R$\u0010<\u001a\u00020\u00102\u0006\u0010.\u001a\u00020\u0010@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b=\u0010\u0013\"\u0004\b>\u0010\u0018R$\u0010?\u001a\u00020\u00102\u0006\u0010.\u001a\u00020\u0010@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b@\u0010\u0013\"\u0004\bA\u0010\u0018¨\u0006i"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/NotificationWakeUpCoordinator;", "Lcom/android/systemui/statusbar/policy/OnHeadsUpChangedListener;", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController$StateListener;", "Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionListener;", "mHeadsUpManager", "Lcom/android/systemui/statusbar/policy/HeadsUpManager;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "bypassController", "Lcom/android/systemui/statusbar/phone/KeyguardBypassController;", "dozeParameters", "Lcom/android/systemui/statusbar/phone/DozeParameters;", "screenOffAnimationController", "Lcom/android/systemui/statusbar/phone/ScreenOffAnimationController;", "(Lcom/android/systemui/statusbar/policy/HeadsUpManager;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/statusbar/phone/KeyguardBypassController;Lcom/android/systemui/statusbar/phone/DozeParameters;Lcom/android/systemui/statusbar/phone/ScreenOffAnimationController;)V", "<set-?>", "", "canShowPulsingHuns", "getCanShowPulsingHuns", "()Z", "collapsedEnoughToHide", "fullyAwake", "getFullyAwake", "setFullyAwake", "(Z)V", "mDozeAmount", "", "mEntrySetToClearWhenFinished", "", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "mLinearDozeAmount", "mLinearVisibilityAmount", "mNotificationVisibility", "com/android/systemui/statusbar/notification/NotificationWakeUpCoordinator$mNotificationVisibility$1", "Lcom/android/systemui/statusbar/notification/NotificationWakeUpCoordinator$mNotificationVisibility$1;", "mNotificationVisibleAmount", "mNotificationsVisible", "mNotificationsVisibleForExpansion", "mStackScrollerController", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;", "mVisibilityAmount", "mVisibilityAnimator", "Landroid/animation/ObjectAnimator;", "mVisibilityInterpolator", "Landroid/view/animation/Interpolator;", "kotlin.jvm.PlatformType", "value", "notificationsFullyHidden", "getNotificationsFullyHidden", "setNotificationsFullyHidden", "pulseExpanding", "pulsing", "getPulsing", "setPulsing", "state", "", "wakeUpListeners", "Ljava/util/ArrayList;", "Lcom/android/systemui/statusbar/notification/NotificationWakeUpCoordinator$WakeUpListener;", "Lkotlin/collections/ArrayList;", "wakingUp", "getWakingUp", "setWakingUp", "willWakeUp", "getWillWakeUp", "setWillWakeUp", "addListener", "", "listener", "handleAnimationFinished", "isPulseExpanding", "notifyAnimationStart", "awake", "onDozeAmountChanged", "linear", "eased", "onDozingChanged", "isDozing", "onHeadsUpStateChanged", "entry", "isHeadsUp", "onPanelExpansionChanged", "event", "Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionChangeEvent;", "onStateChanged", "newState", "overrideDozeAmountIfAnimatingScreenOff", "linearDozeAmount", "overrideDozeAmountIfBypass", "removeListener", "setDozeAmount", "setNotificationsVisible", "visible", "animate", "increaseSpeed", "setNotificationsVisibleForExpansion", "setStackScroller", "stackScrollerController", "setVisibilityAmount", "visibilityAmount", "shouldAnimateVisibility", "startVisibilityAnimation", "updateHideAmount", "updateNotificationVisibility", "WakeUpListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationWakeUpCoordinator.kt */
public final class NotificationWakeUpCoordinator implements OnHeadsUpChangedListener, StatusBarStateController.StateListener, PanelExpansionListener {
    private final KeyguardBypassController bypassController;
    private boolean canShowPulsingHuns;
    private boolean collapsedEnoughToHide;
    private final DozeParameters dozeParameters;
    private boolean fullyAwake;
    private float mDozeAmount;
    private final Set<NotificationEntry> mEntrySetToClearWhenFinished = new LinkedHashSet();
    private final HeadsUpManager mHeadsUpManager;
    private float mLinearDozeAmount;
    /* access modifiers changed from: private */
    public float mLinearVisibilityAmount;
    private final NotificationWakeUpCoordinator$mNotificationVisibility$1 mNotificationVisibility = new NotificationWakeUpCoordinator$mNotificationVisibility$1();
    private float mNotificationVisibleAmount;
    private boolean mNotificationsVisible;
    /* access modifiers changed from: private */
    public boolean mNotificationsVisibleForExpansion;
    private NotificationStackScrollLayoutController mStackScrollerController;
    private float mVisibilityAmount;
    private ObjectAnimator mVisibilityAnimator;
    private Interpolator mVisibilityInterpolator = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
    private boolean notificationsFullyHidden;
    private boolean pulseExpanding;
    private boolean pulsing;
    private final ScreenOffAnimationController screenOffAnimationController;
    private int state = 1;
    private final StatusBarStateController statusBarStateController;
    private final ArrayList<WakeUpListener> wakeUpListeners = new ArrayList<>();
    private boolean wakingUp;
    private boolean willWakeUp;

    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0017J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0005H\u0017ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\bÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/NotificationWakeUpCoordinator$WakeUpListener;", "", "onFullyHiddenChanged", "", "isFullyHidden", "", "onPulseExpansionChanged", "expandingChanged", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: NotificationWakeUpCoordinator.kt */
    public interface WakeUpListener {
        @JvmDefault
        void onFullyHiddenChanged(boolean z) {
        }

        @JvmDefault
        void onPulseExpansionChanged(boolean z) {
        }
    }

    @Inject
    public NotificationWakeUpCoordinator(HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController2, KeyguardBypassController keyguardBypassController, DozeParameters dozeParameters2, ScreenOffAnimationController screenOffAnimationController2) {
        Intrinsics.checkNotNullParameter(headsUpManager, "mHeadsUpManager");
        Intrinsics.checkNotNullParameter(statusBarStateController2, "statusBarStateController");
        Intrinsics.checkNotNullParameter(keyguardBypassController, "bypassController");
        Intrinsics.checkNotNullParameter(dozeParameters2, "dozeParameters");
        Intrinsics.checkNotNullParameter(screenOffAnimationController2, "screenOffAnimationController");
        this.mHeadsUpManager = headsUpManager;
        this.statusBarStateController = statusBarStateController2;
        this.bypassController = keyguardBypassController;
        this.dozeParameters = dozeParameters2;
        this.screenOffAnimationController = screenOffAnimationController2;
        headsUpManager.addListener(this);
        statusBarStateController2.addCallback(this);
        addListener(new WakeUpListener(this) {
            final /* synthetic */ NotificationWakeUpCoordinator this$0;

            {
                this.this$0 = r1;
            }

            public void onFullyHiddenChanged(boolean z) {
                if (z && this.this$0.mNotificationsVisibleForExpansion) {
                    this.this$0.setNotificationsVisibleForExpansion(false, false, false);
                }
            }
        });
    }

    public final boolean getFullyAwake() {
        return this.fullyAwake;
    }

    public final void setFullyAwake(boolean z) {
        this.fullyAwake = z;
    }

    public final boolean getWakingUp() {
        return this.wakingUp;
    }

    public final void setWakingUp(boolean z) {
        this.wakingUp = z;
        setWillWakeUp(false);
        if (z) {
            if (this.mNotificationsVisible && !this.mNotificationsVisibleForExpansion && !this.bypassController.getBypassEnabled()) {
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
                if (notificationStackScrollLayoutController == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
                    notificationStackScrollLayoutController = null;
                }
                notificationStackScrollLayoutController.wakeUpFromPulse();
            }
            if (this.bypassController.getBypassEnabled() && !this.mNotificationsVisible) {
                updateNotificationVisibility(shouldAnimateVisibility(), false);
            }
        }
    }

    public final boolean getWillWakeUp() {
        return this.willWakeUp;
    }

    public final void setWillWakeUp(boolean z) {
        if (z) {
            if (this.mDozeAmount == 0.0f) {
                return;
            }
        }
        this.willWakeUp = z;
    }

    public final boolean getPulsing() {
        return this.pulsing;
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
        if (!this.bypassController.getBypassEnabled()) {
            return z;
        }
        boolean z2 = z || ((this.wakingUp || this.willWakeUp || this.fullyAwake) && this.statusBarStateController.getState() == 1);
        if (this.collapsedEnoughToHide) {
            return false;
        }
        return z2;
    }

    public final void setStackScroller(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        Intrinsics.checkNotNullParameter(notificationStackScrollLayoutController, "stackScrollerController");
        this.mStackScrollerController = notificationStackScrollLayoutController;
        this.pulseExpanding = notificationStackScrollLayoutController.isPulseExpanding();
        notificationStackScrollLayoutController.setOnPulseHeightChangedListener(new NotificationWakeUpCoordinator$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: setStackScroller$lambda-0  reason: not valid java name */
    public static final void m3093setStackScroller$lambda0(NotificationWakeUpCoordinator notificationWakeUpCoordinator) {
        Intrinsics.checkNotNullParameter(notificationWakeUpCoordinator, "this$0");
        boolean isPulseExpanding = notificationWakeUpCoordinator.isPulseExpanding();
        boolean z = isPulseExpanding != notificationWakeUpCoordinator.pulseExpanding;
        notificationWakeUpCoordinator.pulseExpanding = isPulseExpanding;
        Iterator<WakeUpListener> it = notificationWakeUpCoordinator.wakeUpListeners.iterator();
        while (it.hasNext()) {
            it.next().onPulseExpansionChanged(z);
        }
    }

    public final boolean isPulseExpanding() {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
            notificationStackScrollLayoutController = null;
        }
        return notificationStackScrollLayoutController.isPulseExpanding();
    }

    public final void setNotificationsVisibleForExpansion(boolean z, boolean z2, boolean z3) {
        this.mNotificationsVisibleForExpansion = z;
        updateNotificationVisibility(z2, z3);
        if (!z && this.mNotificationsVisible) {
            this.mHeadsUpManager.releaseAllImmediately();
        }
    }

    public final void addListener(WakeUpListener wakeUpListener) {
        Intrinsics.checkNotNullParameter(wakeUpListener, "listener");
        this.wakeUpListeners.add(wakeUpListener);
    }

    public final void removeListener(WakeUpListener wakeUpListener) {
        Intrinsics.checkNotNullParameter(wakeUpListener, "listener");
        this.wakeUpListeners.remove((Object) wakeUpListener);
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
        if (this.mNotificationsVisible != z) {
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
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0037, code lost:
        if ((r4 == 1.0f) != false) goto L_0x0039;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDozeAmountChanged(float r6, float r7) {
        /*
            r5 = this;
            boolean r0 = r5.overrideDozeAmountIfAnimatingScreenOff(r6)
            if (r0 == 0) goto L_0x0007
            return
        L_0x0007:
            boolean r0 = r5.overrideDozeAmountIfBypass()
            if (r0 == 0) goto L_0x000e
            return
        L_0x000e:
            r0 = 1065353216(0x3f800000, float:1.0)
            int r1 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x0018
            r1 = r2
            goto L_0x0019
        L_0x0018:
            r1 = r3
        L_0x0019:
            if (r1 != 0) goto L_0x0042
            r1 = 0
            int r4 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1))
            if (r4 != 0) goto L_0x0022
            r4 = r2
            goto L_0x0023
        L_0x0022:
            r4 = r3
        L_0x0023:
            if (r4 != 0) goto L_0x0042
            float r4 = r5.mLinearDozeAmount
            int r1 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1))
            if (r1 != 0) goto L_0x002d
            r1 = r2
            goto L_0x002e
        L_0x002d:
            r1 = r3
        L_0x002e:
            if (r1 != 0) goto L_0x0039
            int r1 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r1 != 0) goto L_0x0036
            r1 = r2
            goto L_0x0037
        L_0x0036:
            r1 = r3
        L_0x0037:
            if (r1 == 0) goto L_0x0042
        L_0x0039:
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 != 0) goto L_0x003e
            goto L_0x003f
        L_0x003e:
            r2 = r3
        L_0x003f:
            r5.notifyAnimationStart(r2)
        L_0x0042:
            r5.setDozeAmount(r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.onDozeAmountChanged(float, float):void");
    }

    public final void setDozeAmount(float f, float f2) {
        boolean z = true;
        boolean z2 = !(f == this.mLinearDozeAmount);
        this.mLinearDozeAmount = f;
        this.mDozeAmount = f2;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
            notificationStackScrollLayoutController = null;
        }
        notificationStackScrollLayoutController.setDozeAmount(this.mDozeAmount);
        updateHideAmount();
        if (z2) {
            if (f != 0.0f) {
                z = false;
            }
            if (z) {
                setNotificationsVisible(false, false, false);
                setNotificationsVisibleForExpansion(false, false, false);
            }
        }
    }

    public void onStateChanged(int i) {
        if (this.state == 0 && i == 0) {
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

    public void onPanelExpansionChanged(PanelExpansionChangeEvent panelExpansionChangeEvent) {
        Intrinsics.checkNotNullParameter(panelExpansionChangeEvent, NotificationCompat.CATEGORY_EVENT);
        boolean z = panelExpansionChangeEvent.getFraction() <= 0.9f;
        if (z != this.collapsedEnoughToHide) {
            boolean canShowPulsingHuns2 = getCanShowPulsingHuns();
            this.collapsedEnoughToHide = z;
            if (canShowPulsingHuns2 && !getCanShowPulsingHuns()) {
                updateNotificationVisibility(true, true);
                this.mHeadsUpManager.releaseAllImmediately();
            }
        }
    }

    private final boolean overrideDozeAmountIfBypass() {
        if (!this.bypassController.getBypassEnabled()) {
            return false;
        }
        float f = (this.statusBarStateController.getState() == 0 || this.statusBarStateController.getState() == 2) ? 0.0f : 1.0f;
        setDozeAmount(f, f);
        return true;
    }

    private final boolean overrideDozeAmountIfAnimatingScreenOff(float f) {
        if (!this.screenOffAnimationController.overrideNotificationsFullyDozingOnKeyguard()) {
            return false;
        }
        setDozeAmount(1.0f, 1.0f);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0017, code lost:
        if ((r0 == 1.0f) != false) goto L_0x0019;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void startVisibilityAnimation(boolean r7) {
        /*
            r6 = this;
            float r0 = r6.mNotificationVisibleAmount
            r1 = 0
            int r2 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            r3 = 1
            r4 = 0
            if (r2 != 0) goto L_0x000b
            r2 = r3
            goto L_0x000c
        L_0x000b:
            r2 = r4
        L_0x000c:
            r5 = 1065353216(0x3f800000, float:1.0)
            if (r2 != 0) goto L_0x0019
            int r0 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r0 != 0) goto L_0x0016
            r0 = r3
            goto L_0x0017
        L_0x0016:
            r0 = r4
        L_0x0017:
            if (r0 == 0) goto L_0x0024
        L_0x0019:
            boolean r0 = r6.mNotificationsVisible
            if (r0 == 0) goto L_0x0020
            android.view.animation.Interpolator r0 = com.android.systemui.animation.Interpolators.TOUCH_RESPONSE
            goto L_0x0022
        L_0x0020:
            android.view.animation.Interpolator r0 = com.android.systemui.animation.Interpolators.FAST_OUT_SLOW_IN_REVERSE
        L_0x0022:
            r6.mVisibilityInterpolator = r0
        L_0x0024:
            boolean r0 = r6.mNotificationsVisible
            if (r0 == 0) goto L_0x0029
            r1 = r5
        L_0x0029:
            com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator$mNotificationVisibility$1 r0 = r6.mNotificationVisibility
            android.util.Property r0 = (android.util.Property) r0
            float[] r2 = new float[r3]
            r2[r4] = r1
            android.animation.ObjectAnimator r0 = android.animation.ObjectAnimator.ofFloat(r6, r0, r2)
            android.view.animation.Interpolator r1 = com.android.systemui.animation.Interpolators.LINEAR
            android.animation.TimeInterpolator r1 = (android.animation.TimeInterpolator) r1
            r0.setInterpolator(r1)
            r1 = 500(0x1f4, double:2.47E-321)
            if (r7 == 0) goto L_0x0045
            float r7 = (float) r1
            r1 = 1069547520(0x3fc00000, float:1.5)
            float r7 = r7 / r1
            long r1 = (long) r7
        L_0x0045:
            r0.setDuration(r1)
            r0.start()
            r6.mVisibilityAnimator = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.startVisibilityAnimation(boolean):void");
    }

    /* access modifiers changed from: private */
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
        for (NotificationEntry headsUpAnimatingAway : this.mEntrySetToClearWhenFinished) {
            headsUpAnimatingAway.setHeadsUpAnimatingAway(false);
        }
        this.mEntrySetToClearWhenFinished.clear();
    }

    private final void updateHideAmount() {
        float min = Math.min(1.0f - this.mLinearVisibilityAmount, this.mLinearDozeAmount);
        float min2 = Math.min(1.0f - this.mVisibilityAmount, this.mDozeAmount);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
            notificationStackScrollLayoutController = null;
        }
        notificationStackScrollLayoutController.setHideAmount(min, min2);
        setNotificationsFullyHidden(min == 1.0f);
    }

    private final void notifyAnimationStart(boolean z) {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
            notificationStackScrollLayoutController = null;
        }
        notificationStackScrollLayoutController.notifyHideAnimationStart(!z);
    }

    public void onDozingChanged(boolean z) {
        if (z) {
            setNotificationsVisible(false, false, false);
        }
    }

    public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        boolean shouldAnimateVisibility = shouldAnimateVisibility();
        if (!z) {
            if (!(this.mLinearDozeAmount == 0.0f)) {
                if (!(this.mLinearVisibilityAmount == 0.0f)) {
                    if (notificationEntry.isRowDismissed()) {
                        shouldAnimateVisibility = false;
                    } else if (!this.wakingUp && !this.willWakeUp) {
                        notificationEntry.setHeadsUpAnimatingAway(true);
                        this.mEntrySetToClearWhenFinished.add(notificationEntry);
                    }
                }
            }
        } else if (this.mEntrySetToClearWhenFinished.contains(notificationEntry)) {
            this.mEntrySetToClearWhenFinished.remove(notificationEntry);
            notificationEntry.setHeadsUpAnimatingAway(false);
        }
        updateNotificationVisibility(shouldAnimateVisibility, false);
    }

    private final boolean shouldAnimateVisibility() {
        return this.dozeParameters.getAlwaysOn() && !this.dozeParameters.getDisplayNeedsBlanking();
    }
}
