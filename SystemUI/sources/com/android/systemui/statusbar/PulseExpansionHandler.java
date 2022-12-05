package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: PulseExpansionHandler.kt */
/* loaded from: classes.dex */
public final class PulseExpansionHandler implements Gefingerpoken {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private static final int SPRING_BACK_ANIMATION_LENGTH_MS = 375;
    private boolean bouncerShowing;
    @NotNull
    private final KeyguardBypassController bypassController;
    @NotNull
    private final ConfigurationController configurationController;
    @NotNull
    private final FalsingCollector falsingCollector;
    @NotNull
    private final FalsingManager falsingManager;
    @NotNull
    private final HeadsUpManagerPhone headsUpManager;
    private boolean isExpanding;
    private boolean isWakingToShadeLocked;
    private boolean leavingLockscreen;
    @NotNull
    private final LockscreenShadeTransitionController lockscreenShadeTransitionController;
    private boolean mDraggedFarEnough;
    private float mInitialTouchX;
    private float mInitialTouchY;
    @Nullable
    private final PowerManager mPowerManager;
    private boolean mPulsing;
    @Nullable
    private ExpandableView mStartingChild;
    @NotNull
    private final int[] mTemp2 = new int[2];
    private int minDragDistance;
    @Nullable
    private Runnable pulseExpandAbortListener;
    private boolean qsExpanded;
    @NotNull
    private final NotificationRoundnessManager roundnessManager;
    private NotificationStackScrollLayoutController stackScrollerController;
    @NotNull
    private final StatusBarStateController statusBarStateController;
    private float touchSlop;
    @Nullable
    private VelocityTracker velocityTracker;
    @NotNull
    private final NotificationWakeUpCoordinator wakeUpCoordinator;

    public PulseExpansionHandler(@NotNull final Context context, @NotNull NotificationWakeUpCoordinator wakeUpCoordinator, @NotNull KeyguardBypassController bypassController, @NotNull HeadsUpManagerPhone headsUpManager, @NotNull NotificationRoundnessManager roundnessManager, @NotNull ConfigurationController configurationController, @NotNull StatusBarStateController statusBarStateController, @NotNull FalsingManager falsingManager, @NotNull LockscreenShadeTransitionController lockscreenShadeTransitionController, @NotNull FalsingCollector falsingCollector) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(wakeUpCoordinator, "wakeUpCoordinator");
        Intrinsics.checkNotNullParameter(bypassController, "bypassController");
        Intrinsics.checkNotNullParameter(headsUpManager, "headsUpManager");
        Intrinsics.checkNotNullParameter(roundnessManager, "roundnessManager");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController, "lockscreenShadeTransitionController");
        Intrinsics.checkNotNullParameter(falsingCollector, "falsingCollector");
        this.wakeUpCoordinator = wakeUpCoordinator;
        this.bypassController = bypassController;
        this.headsUpManager = headsUpManager;
        this.roundnessManager = roundnessManager;
        this.configurationController = configurationController;
        this.statusBarStateController = statusBarStateController;
        this.falsingManager = falsingManager;
        this.lockscreenShadeTransitionController = lockscreenShadeTransitionController;
        this.falsingCollector = falsingCollector;
        initResources(context);
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.PulseExpansionHandler.1
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(@Nullable Configuration configuration) {
                PulseExpansionHandler.this.initResources(context);
            }
        });
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
    }

    /* compiled from: PulseExpansionHandler.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final boolean isExpanding() {
        return this.isExpanding;
    }

    private final void setExpanding(boolean z) {
        boolean z2 = this.isExpanding != z;
        this.isExpanding = z;
        this.bypassController.setPulseExpanding(z);
        if (z2) {
            if (z) {
                NotificationEntry topEntry = this.headsUpManager.getTopEntry();
                if (topEntry != null) {
                    this.roundnessManager.setTrackingHeadsUp(topEntry.getRow());
                }
                this.lockscreenShadeTransitionController.onPulseExpansionStarted();
            } else {
                this.roundnessManager.setTrackingHeadsUp(null);
                if (!this.leavingLockscreen) {
                    this.bypassController.maybePerformPendingUnlock();
                    Runnable runnable = this.pulseExpandAbortListener;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }
            this.headsUpManager.unpinAll(true);
        }
    }

    public final boolean isWakingToShadeLocked() {
        return this.isWakingToShadeLocked;
    }

    private final boolean isFalseTouch() {
        return this.falsingManager.isFalseTouch(2);
    }

    public final void setQsExpanded(boolean z) {
        this.qsExpanded = z;
    }

    public final void setPulseExpandAbortListener(@Nullable Runnable runnable) {
        this.pulseExpandAbortListener = runnable;
    }

    public final void setBouncerShowing(boolean z) {
        this.bouncerShowing = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void initResources(Context context) {
        this.minDragDistance = context.getResources().getDimensionPixelSize(R$dimen.keyguard_drag_down_min_distance);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override // com.android.systemui.Gefingerpoken
    public boolean onInterceptTouchEvent(@NotNull MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        return canHandleMotionEvent() && startExpansion(event);
    }

    private final boolean canHandleMotionEvent() {
        return this.wakeUpCoordinator.getCanShowPulsingHuns() && !this.qsExpanded && !this.bouncerShowing;
    }

    private final boolean startExpansion(MotionEvent motionEvent) {
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        VelocityTracker velocityTracker = this.velocityTracker;
        Intrinsics.checkNotNull(velocityTracker);
        velocityTracker.addMovement(motionEvent);
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mDraggedFarEnough = false;
            setExpanding(false);
            this.leavingLockscreen = false;
            this.mStartingChild = null;
            this.mInitialTouchY = y;
            this.mInitialTouchX = x;
        } else if (actionMasked == 1) {
            recycleVelocityTracker();
            setExpanding(false);
        } else if (actionMasked == 2) {
            float f = y - this.mInitialTouchY;
            if (f > this.touchSlop && f > Math.abs(x - this.mInitialTouchX)) {
                this.falsingCollector.onStartExpandingFromPulse();
                setExpanding(true);
                captureStartingChild(this.mInitialTouchX, this.mInitialTouchY);
                this.mInitialTouchY = y;
                this.mInitialTouchX = x;
                return true;
            }
        } else if (actionMasked == 3) {
            recycleVelocityTracker();
            setExpanding(false);
        }
        return false;
    }

    private final void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
        this.velocityTracker = null;
    }

    @Override // com.android.systemui.Gefingerpoken
    public boolean onTouchEvent(@NotNull MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        boolean z = false;
        boolean z2 = (event.getAction() == 3 || event.getAction() == 1) && this.isExpanding;
        if (canHandleMotionEvent() || z2) {
            if (this.velocityTracker == null || !this.isExpanding || event.getActionMasked() == 0) {
                return startExpansion(event);
            }
            VelocityTracker velocityTracker = this.velocityTracker;
            Intrinsics.checkNotNull(velocityTracker);
            velocityTracker.addMovement(event);
            float y = event.getY() - this.mInitialTouchY;
            int actionMasked = event.getActionMasked();
            if (actionMasked == 1) {
                VelocityTracker velocityTracker2 = this.velocityTracker;
                Intrinsics.checkNotNull(velocityTracker2);
                velocityTracker2.computeCurrentVelocity(1000);
                if (y > 0.0f) {
                    VelocityTracker velocityTracker3 = this.velocityTracker;
                    Intrinsics.checkNotNull(velocityTracker3);
                    if (velocityTracker3.getYVelocity() > -1000.0f && this.statusBarStateController.getState() != 0) {
                        z = true;
                    }
                }
                if (!this.falsingManager.isUnlockingDisabled() && !isFalseTouch() && z) {
                    finishExpansion();
                } else {
                    cancelExpansion();
                }
                recycleVelocityTracker();
            } else if (actionMasked == 2) {
                updateExpansionHeight(y);
            } else if (actionMasked == 3) {
                cancelExpansion();
                recycleVelocityTracker();
            }
            return this.isExpanding;
        }
        return false;
    }

    private final void finishExpansion() {
        ExpandableView expandableView = this.mStartingChild;
        if (expandableView != null) {
            Intrinsics.checkNotNull(expandableView);
            setUserLocked(expandableView, false);
            this.mStartingChild = null;
        }
        if (this.statusBarStateController.isDozing()) {
            this.isWakingToShadeLocked = true;
            this.wakeUpCoordinator.setWillWakeUp(true);
            PowerManager powerManager = this.mPowerManager;
            Intrinsics.checkNotNull(powerManager);
            powerManager.wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:PULSEDRAG");
        }
        this.lockscreenShadeTransitionController.goToLockedShade(expandableView, false);
        this.lockscreenShadeTransitionController.finishPulseAnimation(false);
        this.leavingLockscreen = true;
        setExpanding(false);
        ExpandableView expandableView2 = this.mStartingChild;
        if (expandableView2 instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView2;
            Intrinsics.checkNotNull(expandableNotificationRow);
            expandableNotificationRow.onExpandedByGesture(true);
        }
    }

    private final void updateExpansionHeight(float f) {
        float max = Math.max(f, 0.0f);
        ExpandableView expandableView = this.mStartingChild;
        if (expandableView != null) {
            Intrinsics.checkNotNull(expandableView);
            expandableView.setActualHeight(Math.min((int) (expandableView.getCollapsedHeight() + max), expandableView.getMaxContentHeight()));
        } else {
            this.wakeUpCoordinator.setNotificationsVisibleForExpansion(f > ((float) this.lockscreenShadeTransitionController.getDistanceUntilShowingPulsingNotifications()), true, true);
        }
        this.lockscreenShadeTransitionController.setPulseHeight(max, false);
    }

    private final void captureStartingChild(float f, float f2) {
        if (this.mStartingChild != null || this.bypassController.getBypassEnabled()) {
            return;
        }
        ExpandableView findView = findView(f, f2);
        this.mStartingChild = findView;
        if (findView == null) {
            return;
        }
        Intrinsics.checkNotNull(findView);
        setUserLocked(findView, true);
    }

    private final void reset(final ExpandableView expandableView) {
        if (expandableView.getActualHeight() == expandableView.getCollapsedHeight()) {
            setUserLocked(expandableView, false);
            return;
        }
        ObjectAnimator ofInt = ObjectAnimator.ofInt(expandableView, "actualHeight", expandableView.getActualHeight(), expandableView.getCollapsedHeight());
        ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofInt.setDuration(SPRING_BACK_ANIMATION_LENGTH_MS);
        ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.PulseExpansionHandler$reset$1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@NotNull Animator animation) {
                Intrinsics.checkNotNullParameter(animation, "animation");
                PulseExpansionHandler.this.setUserLocked(expandableView, false);
            }
        });
        ofInt.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setUserLocked(ExpandableView expandableView, boolean z) {
        if (expandableView instanceof ExpandableNotificationRow) {
            ((ExpandableNotificationRow) expandableView).setUserLocked(z);
        }
    }

    private final void cancelExpansion() {
        setExpanding(false);
        this.falsingCollector.onExpansionFromPulseStopped();
        ExpandableView expandableView = this.mStartingChild;
        if (expandableView != null) {
            Intrinsics.checkNotNull(expandableView);
            reset(expandableView);
            this.mStartingChild = null;
        }
        this.lockscreenShadeTransitionController.finishPulseAnimation(true);
        this.wakeUpCoordinator.setNotificationsVisibleForExpansion(false, true, false);
    }

    private final ExpandableView findView(float f, float f2) {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.stackScrollerController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("stackScrollerController");
            throw null;
        }
        notificationStackScrollLayoutController.getLocationOnScreen(this.mTemp2);
        int[] iArr = this.mTemp2;
        float f3 = f + iArr[0];
        float f4 = f2 + iArr[1];
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.stackScrollerController;
        if (notificationStackScrollLayoutController2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("stackScrollerController");
            throw null;
        }
        ExpandableView childAtRawPosition = notificationStackScrollLayoutController2.getChildAtRawPosition(f3, f4);
        if (childAtRawPosition != null && childAtRawPosition.isContentExpandable()) {
            return childAtRawPosition;
        }
        return null;
    }

    public final void setUp(@NotNull NotificationStackScrollLayoutController stackScrollerController) {
        Intrinsics.checkNotNullParameter(stackScrollerController, "stackScrollerController");
        this.stackScrollerController = stackScrollerController;
    }

    public final void setPulsing(boolean z) {
        this.mPulsing = z;
    }

    public final void onStartedWakingUp() {
        this.isWakingToShadeLocked = false;
    }
}
