package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.MathUtils;
import android.view.View;
import com.android.systemui.ExpandHelper;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.UdfpsKeyguardViewController;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.Utils;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: LockscreenShadeTransitionController.kt */
/* loaded from: classes.dex */
public final class LockscreenShadeTransitionController {
    @NotNull
    private final AmbientState ambientState;
    @Nullable
    private Function1<? super Long, Unit> animationHandlerOnKeyguardDismiss;
    @NotNull
    private final Context context;
    @NotNull
    private final NotificationShadeDepthController depthController;
    @NotNull
    private final DisplayMetrics displayMetrics;
    private float dragDownAmount;
    @Nullable
    private ValueAnimator dragDownAnimator;
    @Nullable
    private NotificationEntry draggedDownEntry;
    @NotNull
    private final FalsingCollector falsingCollector;
    @NotNull
    private final FeatureFlags featureFlags;
    private boolean forceApplyAmount;
    private int fullTransitionDistance;
    @NotNull
    private final KeyguardBypassController keyguardBypassController;
    @NotNull
    private final NotificationLockscreenUserManager lockScreenUserManager;
    @NotNull
    private final LockscreenGestureLogger lockscreenGestureLogger;
    @NotNull
    private final MediaHierarchyManager mediaHierarchyManager;
    private boolean nextHideKeyguardNeedsNoAnimation;
    public NotificationPanelViewController notificationPanelController;
    private NotificationStackScrollLayoutController nsslController;
    private float pulseHeight;
    @Nullable
    private ValueAnimator pulseHeightAnimator;
    public QS qS;
    @NotNull
    private final ScrimController scrimController;
    private int scrimTransitionDistance;
    @NotNull
    private final SysuiStatusBarStateController statusBarStateController;
    public StatusBar statusbar;
    @NotNull
    private final DragDownHelper touchHelper;
    @Nullable
    private UdfpsKeyguardViewController udfpsKeyguardViewController;
    private boolean useSplitShade;

    public static /* synthetic */ void getDragDownAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public static /* synthetic */ void getPulseHeightAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public final void goToLockedShade(@Nullable View view) {
        goToLockedShade$default(this, view, false, 2, null);
    }

    public LockscreenShadeTransitionController(@NotNull SysuiStatusBarStateController statusBarStateController, @NotNull LockscreenGestureLogger lockscreenGestureLogger, @NotNull KeyguardBypassController keyguardBypassController, @NotNull NotificationLockscreenUserManager lockScreenUserManager, @NotNull FalsingCollector falsingCollector, @NotNull AmbientState ambientState, @NotNull DisplayMetrics displayMetrics, @NotNull MediaHierarchyManager mediaHierarchyManager, @NotNull ScrimController scrimController, @NotNull NotificationShadeDepthController depthController, @NotNull FeatureFlags featureFlags, @NotNull Context context, @NotNull ConfigurationController configurationController, @NotNull FalsingManager falsingManager) {
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(lockscreenGestureLogger, "lockscreenGestureLogger");
        Intrinsics.checkNotNullParameter(keyguardBypassController, "keyguardBypassController");
        Intrinsics.checkNotNullParameter(lockScreenUserManager, "lockScreenUserManager");
        Intrinsics.checkNotNullParameter(falsingCollector, "falsingCollector");
        Intrinsics.checkNotNullParameter(ambientState, "ambientState");
        Intrinsics.checkNotNullParameter(displayMetrics, "displayMetrics");
        Intrinsics.checkNotNullParameter(mediaHierarchyManager, "mediaHierarchyManager");
        Intrinsics.checkNotNullParameter(scrimController, "scrimController");
        Intrinsics.checkNotNullParameter(depthController, "depthController");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        this.statusBarStateController = statusBarStateController;
        this.lockscreenGestureLogger = lockscreenGestureLogger;
        this.keyguardBypassController = keyguardBypassController;
        this.lockScreenUserManager = lockScreenUserManager;
        this.falsingCollector = falsingCollector;
        this.ambientState = ambientState;
        this.displayMetrics = displayMetrics;
        this.mediaHierarchyManager = mediaHierarchyManager;
        this.scrimController = scrimController;
        this.depthController = depthController;
        this.featureFlags = featureFlags;
        this.context = context;
        this.touchHelper = new DragDownHelper(falsingManager, falsingCollector, this, context);
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController.1
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(@Nullable Configuration configuration) {
                LockscreenShadeTransitionController.this.updateResources();
                LockscreenShadeTransitionController.this.getTouchHelper().updateResources(LockscreenShadeTransitionController.this.context);
            }
        });
    }

    @NotNull
    public final NotificationPanelViewController getNotificationPanelController() {
        NotificationPanelViewController notificationPanelViewController = this.notificationPanelController;
        if (notificationPanelViewController != null) {
            return notificationPanelViewController;
        }
        Intrinsics.throwUninitializedPropertyAccessException("notificationPanelController");
        throw null;
    }

    public final void setNotificationPanelController(@NotNull NotificationPanelViewController notificationPanelViewController) {
        Intrinsics.checkNotNullParameter(notificationPanelViewController, "<set-?>");
        this.notificationPanelController = notificationPanelViewController;
    }

    @NotNull
    public final StatusBar getStatusbar() {
        StatusBar statusBar = this.statusbar;
        if (statusBar != null) {
            return statusBar;
        }
        Intrinsics.throwUninitializedPropertyAccessException("statusbar");
        throw null;
    }

    public final void setStatusbar(@NotNull StatusBar statusBar) {
        Intrinsics.checkNotNullParameter(statusBar, "<set-?>");
        this.statusbar = statusBar;
    }

    @NotNull
    public final QS getQS() {
        QS qs = this.qS;
        if (qs != null) {
            return qs;
        }
        Intrinsics.throwUninitializedPropertyAccessException("qS");
        throw null;
    }

    public final void setQS(@NotNull QS qs) {
        Intrinsics.checkNotNullParameter(qs, "<set-?>");
        this.qS = qs;
    }

    public final int getDistanceUntilShowingPulsingNotifications() {
        return this.scrimTransitionDistance;
    }

    @Nullable
    public final UdfpsKeyguardViewController getUdfpsKeyguardViewController() {
        return this.udfpsKeyguardViewController;
    }

    public final void setUdfpsKeyguardViewController(@Nullable UdfpsKeyguardViewController udfpsKeyguardViewController) {
        this.udfpsKeyguardViewController = udfpsKeyguardViewController;
    }

    @NotNull
    public final DragDownHelper getTouchHelper() {
        return this.touchHelper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateResources() {
        this.scrimTransitionDistance = this.context.getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_scrim_transition_distance);
        this.fullTransitionDistance = this.context.getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_qs_transition_distance);
        this.useSplitShade = Utils.shouldUseSplitNotificationShade(this.featureFlags, this.context.getResources());
    }

    public final void setStackScroller(@NotNull NotificationStackScrollLayoutController nsslController) {
        Intrinsics.checkNotNullParameter(nsslController, "nsslController");
        this.nsslController = nsslController;
        DragDownHelper dragDownHelper = this.touchHelper;
        NotificationStackScrollLayout view = nsslController.getView();
        Intrinsics.checkNotNullExpressionValue(view, "nsslController.view");
        dragDownHelper.setHost(view);
        DragDownHelper dragDownHelper2 = this.touchHelper;
        ExpandHelper.Callback expandHelperCallback = nsslController.getExpandHelperCallback();
        Intrinsics.checkNotNullExpressionValue(expandHelperCallback, "nsslController.expandHelperCallback");
        dragDownHelper2.setExpandCallback(expandHelperCallback);
    }

    public final void bindController(@NotNull NotificationShelfController notificationShelfController) {
        Intrinsics.checkNotNullParameter(notificationShelfController, "notificationShelfController");
        notificationShelfController.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$bindController$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SysuiStatusBarStateController sysuiStatusBarStateController;
                sysuiStatusBarStateController = LockscreenShadeTransitionController.this.statusBarStateController;
                if (sysuiStatusBarStateController.getState() == 1) {
                    LockscreenShadeTransitionController.this.getStatusbar().wakeUpIfDozing(SystemClock.uptimeMillis(), view, "SHADE_CLICK");
                    LockscreenShadeTransitionController.goToLockedShade$default(LockscreenShadeTransitionController.this, view, false, 2, null);
                }
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0011, code lost:
        if (r0.isInLockedDownShade() != false) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean canDragDown$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        if (this.statusBarStateController.getState() != 1) {
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
            if (notificationStackScrollLayoutController == null) {
                Intrinsics.throwUninitializedPropertyAccessException("nsslController");
                throw null;
            }
        }
        return getQS().isFullyCollapsed();
    }

    public final void onDraggedDown$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@Nullable View view, int i) {
        if (canDragDown$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
            if (notificationStackScrollLayoutController == null) {
                Intrinsics.throwUninitializedPropertyAccessException("nsslController");
                throw null;
            } else if (notificationStackScrollLayoutController.isInLockedDownShade()) {
                this.statusBarStateController.setLeaveOpenOnKeyguardHide(true);
                getStatusbar().dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$onDraggedDown$1
                    @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                    public final boolean onDismiss() {
                        LockscreenShadeTransitionController.this.nextHideKeyguardNeedsNoAnimation = true;
                        return false;
                    }
                }, null, false);
                return;
            } else {
                this.lockscreenGestureLogger.write(187, (int) (i / this.displayMetrics.density), 0);
                this.lockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_PULL_SHADE_OPEN);
                if (this.ambientState.isDozing() && view == null) {
                    return;
                }
                goToLockedShadeInternal(view, new LockscreenShadeTransitionController$onDraggedDown$animationHandler$1(view, this), new Runnable() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        LockscreenShadeTransitionController.setDragDownAmountAnimated$default(LockscreenShadeTransitionController.this, 0.0f, 0L, null, 6, null);
                    }
                });
                return;
            }
        }
        setDragDownAmountAnimated$default(this, 0.0f, 0L, null, 6, null);
    }

    public final void onDragDownReset$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            throw null;
        }
        notificationStackScrollLayoutController.setDimmed(true, true);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.nsslController;
        if (notificationStackScrollLayoutController2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            throw null;
        }
        notificationStackScrollLayoutController2.resetScrollPosition();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = this.nsslController;
        if (notificationStackScrollLayoutController3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            throw null;
        }
        notificationStackScrollLayoutController3.resetCheckSnoozeLeavebehind();
        setDragDownAmountAnimated$default(this, 0.0f, 0L, null, 6, null);
    }

    public final void onCrossedThreshold$frameworks__base__packages__SystemUI__android_common__SystemUI_core(boolean z) {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController != null) {
            notificationStackScrollLayoutController.setDimmed(!z, true);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            throw null;
        }
    }

    public final void onDragDownStarted$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            throw null;
        }
        notificationStackScrollLayoutController.cancelLongPress();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.nsslController;
        if (notificationStackScrollLayoutController2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            throw null;
        }
        notificationStackScrollLayoutController2.checkSnoozeLeavebehind();
        ValueAnimator valueAnimator = this.dragDownAnimator;
        if (valueAnimator == null) {
            return;
        }
        valueAnimator.cancel();
    }

    public final boolean isFalsingCheckNeeded$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.statusBarStateController.getState() == 1;
    }

    public final boolean isDragDownEnabledForView$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@Nullable ExpandableView expandableView) {
        if (isDragDownAnywhereEnabled$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            return true;
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            throw null;
        } else if (!notificationStackScrollLayoutController.isInLockedDownShade()) {
            return false;
        } else {
            if (expandableView == null) {
                return true;
            }
            if (!(expandableView instanceof ExpandableNotificationRow)) {
                return false;
            }
            return ((ExpandableNotificationRow) expandableView).getEntry().isSensitive();
        }
    }

    public final boolean isDragDownAnywhereEnabled$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.statusBarStateController.getState() == 1 && !this.keyguardBypassController.getBypassEnabled() && getQS().isFullyCollapsed();
    }

    public final float getDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.dragDownAmount;
    }

    public final void setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(float f) {
        if (!(this.dragDownAmount == f) || this.forceApplyAmount) {
            this.dragDownAmount = f;
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
            if (notificationStackScrollLayoutController == null) {
                Intrinsics.throwUninitializedPropertyAccessException("nsslController");
                throw null;
            } else if (notificationStackScrollLayoutController.isInLockedDownShade() && !this.forceApplyAmount) {
            } else {
                NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.nsslController;
                if (notificationStackScrollLayoutController2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("nsslController");
                    throw null;
                }
                notificationStackScrollLayoutController2.setTransitionToFullShadeAmount(this.dragDownAmount);
                getNotificationPanelController().setTransitionToFullShadeAmount(this.dragDownAmount, false, 0L);
                float f2 = 0.0f;
                getQS().setTransitionToFullShadeAmount(this.useSplitShade ? 0.0f : this.dragDownAmount, false);
                if (!this.useSplitShade) {
                    f2 = this.dragDownAmount;
                }
                this.mediaHierarchyManager.setTransitionToFullShadeAmount(f2);
                transitionToShadeAmountCommon(this.dragDownAmount);
            }
        }
    }

    private final void transitionToShadeAmountCommon(float f) {
        float saturate = MathUtils.saturate(f / this.scrimTransitionDistance);
        this.scrimController.setTransitionToFullShadeProgress(saturate);
        getNotificationPanelController().setKeyguardOnlyContentAlpha(1.0f - saturate);
        this.depthController.setTransitionToFullShadeProgress(saturate);
        UdfpsKeyguardViewController udfpsKeyguardViewController = this.udfpsKeyguardViewController;
        if (udfpsKeyguardViewController == null) {
            return;
        }
        udfpsKeyguardViewController.setTransitionToFullShadeProgress(saturate);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void setDragDownAmountAnimated$default(LockscreenShadeTransitionController lockscreenShadeTransitionController, float f, long j, Function0 function0, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 0;
        }
        if ((i & 4) != 0) {
            function0 = null;
        }
        lockscreenShadeTransitionController.setDragDownAmountAnimated(f, j, function0);
    }

    private final void setDragDownAmountAnimated(float f, long j, final Function0<Unit> function0) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.dragDownAmount, f);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.setDuration(375L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$setDragDownAmountAnimated$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(@NotNull ValueAnimator animation) {
                Intrinsics.checkNotNullParameter(animation, "animation");
                LockscreenShadeTransitionController lockscreenShadeTransitionController = LockscreenShadeTransitionController.this;
                Object animatedValue = animation.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                lockscreenShadeTransitionController.setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(((Float) animatedValue).floatValue());
            }
        });
        if (j > 0) {
            ofFloat.setStartDelay(j);
        }
        if (function0 != null) {
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$setDragDownAmountAnimated$2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(@Nullable Animator animator) {
                    function0.mo1951invoke();
                }
            });
        }
        ofFloat.start();
        this.dragDownAnimator = ofFloat;
    }

    private final void animateAppear(long j) {
        this.forceApplyAmount = true;
        setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(1.0f);
        setDragDownAmountAnimated(this.fullTransitionDistance, j, new LockscreenShadeTransitionController$animateAppear$1(this));
    }

    public static /* synthetic */ void goToLockedShade$default(LockscreenShadeTransitionController lockscreenShadeTransitionController, View view, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        lockscreenShadeTransitionController.goToLockedShade(view, z);
    }

    public final void goToLockedShade(@Nullable View view, boolean z) {
        if (this.statusBarStateController.getState() == 1) {
            goToLockedShadeInternal(view, z ? null : new LockscreenShadeTransitionController$goToLockedShade$1(this), null);
        }
    }

    private final void goToLockedShadeInternal(View view, final Function1<? super Long, Unit> function1, final Runnable runnable) {
        NotificationEntry notificationEntry;
        if (getStatusbar().isShadeDisabled()) {
            if (runnable == null) {
                return;
            }
            runnable.run();
            return;
        }
        int currentUserId = this.lockScreenUserManager.getCurrentUserId();
        ActivityStarter.OnDismissAction onDismissAction = null;
        if (view instanceof ExpandableNotificationRow) {
            notificationEntry = ((ExpandableNotificationRow) view).getEntry();
            notificationEntry.setUserExpanded(true, true);
            notificationEntry.setGroupExpansionChanging(true);
            currentUserId = notificationEntry.getSbn().getUserId();
        } else {
            notificationEntry = null;
        }
        NotificationLockscreenUserManager notificationLockscreenUserManager = this.lockScreenUserManager;
        boolean z = false;
        boolean z2 = !notificationLockscreenUserManager.userAllowsPrivateNotificationsInPublic(notificationLockscreenUserManager.getCurrentUserId()) || !this.lockScreenUserManager.shouldShowLockscreenNotifications() || this.falsingCollector.shouldEnforceBouncer();
        if (!this.keyguardBypassController.getBypassEnabled()) {
            z = z2;
        }
        if (this.lockScreenUserManager.isLockscreenPublicMode(currentUserId) && z) {
            this.statusBarStateController.setLeaveOpenOnKeyguardHide(true);
            if (function1 != null) {
                onDismissAction = new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$goToLockedShadeInternal$1
                    @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                    public final boolean onDismiss() {
                        LockscreenShadeTransitionController.this.animationHandlerOnKeyguardDismiss = function1;
                        return false;
                    }
                };
            }
            getStatusbar().showBouncerWithDimissAndCancelIfKeyguard(onDismissAction, new Runnable() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$goToLockedShadeInternal$cancelHandler$1
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationEntry notificationEntry2;
                    notificationEntry2 = LockscreenShadeTransitionController.this.draggedDownEntry;
                    if (notificationEntry2 != null) {
                        LockscreenShadeTransitionController lockscreenShadeTransitionController = LockscreenShadeTransitionController.this;
                        notificationEntry2.setUserLocked(false);
                        notificationEntry2.notifyHeightChanged(false);
                        lockscreenShadeTransitionController.draggedDownEntry = null;
                    }
                    Runnable runnable2 = runnable;
                    if (runnable2 == null) {
                        return;
                    }
                    runnable2.run();
                }
            });
            this.draggedDownEntry = notificationEntry;
            return;
        }
        this.statusBarStateController.setState(2);
        if (function1 != null) {
            function1.mo1949invoke(0L);
        } else {
            performDefaultGoToFullShadeAnimation(0L);
        }
    }

    public final void onHideKeyguard(long j, int i) {
        Function1<? super Long, Unit> function1 = this.animationHandlerOnKeyguardDismiss;
        if (function1 != null) {
            Intrinsics.checkNotNull(function1);
            function1.mo1949invoke(Long.valueOf(j));
            this.animationHandlerOnKeyguardDismiss = null;
        } else if (this.nextHideKeyguardNeedsNoAnimation) {
            this.nextHideKeyguardNeedsNoAnimation = false;
        } else if (i != 2) {
            performDefaultGoToFullShadeAnimation(j);
        }
        NotificationEntry notificationEntry = this.draggedDownEntry;
        if (notificationEntry == null) {
            return;
        }
        notificationEntry.setUserLocked(false);
        this.draggedDownEntry = null;
    }

    private final void performDefaultGoToFullShadeAnimation(long j) {
        getNotificationPanelController().animateToFullShade(j);
        animateAppear(j);
    }

    public static /* synthetic */ void setPulseHeight$default(LockscreenShadeTransitionController lockscreenShadeTransitionController, float f, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        lockscreenShadeTransitionController.setPulseHeight(f, z);
    }

    public final void setPulseHeight(float f, boolean z) {
        if (z) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.pulseHeight, f);
            ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofFloat.setDuration(375L);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$setPulseHeight$1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(@NotNull ValueAnimator animation) {
                    Intrinsics.checkNotNullParameter(animation, "animation");
                    LockscreenShadeTransitionController lockscreenShadeTransitionController = LockscreenShadeTransitionController.this;
                    Object animatedValue = animation.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                    LockscreenShadeTransitionController.setPulseHeight$default(lockscreenShadeTransitionController, ((Float) animatedValue).floatValue(), false, 2, null);
                }
            });
            ofFloat.start();
            this.pulseHeightAnimator = ofFloat;
            return;
        }
        this.pulseHeight = f;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            throw null;
        }
        getNotificationPanelController().setOverStrechAmount(notificationStackScrollLayoutController.setPulseHeight(f));
        if (!this.keyguardBypassController.getBypassEnabled()) {
            f = 0.0f;
        }
        transitionToShadeAmountCommon(f);
    }

    public final void finishPulseAnimation(boolean z) {
        if (z) {
            setPulseHeight(0.0f, true);
            return;
        }
        getNotificationPanelController().onPulseExpansionFinished();
        setPulseHeight(0.0f, false);
    }

    public final void onPulseExpansionStarted() {
        ValueAnimator valueAnimator = this.pulseHeightAnimator;
        if (valueAnimator == null) {
            return;
        }
        valueAnimator.cancel();
    }
}
