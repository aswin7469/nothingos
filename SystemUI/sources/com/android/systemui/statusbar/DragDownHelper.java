package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.android.systemui.ExpandHelper;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: LockscreenShadeTransitionController.kt */
/* loaded from: classes.dex */
public final class DragDownHelper implements Gefingerpoken {
    private float dragDownAmountOnStart;
    @NotNull
    private final LockscreenShadeTransitionController dragDownCallback;
    private boolean draggedFarEnough;
    public ExpandHelper.Callback expandCallback;
    @NotNull
    private final FalsingCollector falsingCollector;
    @NotNull
    private final FalsingManager falsingManager;
    public View host;
    private float initialTouchX;
    private float initialTouchY;
    private boolean isDraggingDown;
    private float lastHeight;
    private int minDragDistance;
    private float slopMultiplier;
    @Nullable
    private ExpandableView startingChild;
    @NotNull
    private final int[] temp2 = new int[2];
    private float touchSlop;

    public DragDownHelper(@NotNull FalsingManager falsingManager, @NotNull FalsingCollector falsingCollector, @NotNull LockscreenShadeTransitionController dragDownCallback, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(falsingCollector, "falsingCollector");
        Intrinsics.checkNotNullParameter(dragDownCallback, "dragDownCallback");
        Intrinsics.checkNotNullParameter(context, "context");
        this.falsingManager = falsingManager;
        this.falsingCollector = falsingCollector;
        this.dragDownCallback = dragDownCallback;
        updateResources(context);
    }

    @NotNull
    public final ExpandHelper.Callback getExpandCallback() {
        ExpandHelper.Callback callback = this.expandCallback;
        if (callback != null) {
            return callback;
        }
        Intrinsics.throwUninitializedPropertyAccessException("expandCallback");
        throw null;
    }

    public final void setExpandCallback(@NotNull ExpandHelper.Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "<set-?>");
        this.expandCallback = callback;
    }

    @NotNull
    public final View getHost() {
        View view = this.host;
        if (view != null) {
            return view;
        }
        Intrinsics.throwUninitializedPropertyAccessException("host");
        throw null;
    }

    public final void setHost(@NotNull View view) {
        Intrinsics.checkNotNullParameter(view, "<set-?>");
        this.host = view;
    }

    public final boolean isDraggingDown() {
        return this.isDraggingDown;
    }

    private final boolean isFalseTouch() {
        if (!this.dragDownCallback.isFalsingCheckNeeded$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            return false;
        }
        return this.falsingManager.isFalseTouch(2) || !this.draggedFarEnough;
    }

    public final boolean isDragDownEnabled() {
        return this.dragDownCallback.isDragDownEnabledForView$frameworks__base__packages__SystemUI__android_common__SystemUI_core(null);
    }

    public final void updateResources(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.minDragDistance = context.getResources().getDimensionPixelSize(R$dimen.keyguard_drag_down_min_distance);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.touchSlop = viewConfiguration.getScaledTouchSlop();
        this.slopMultiplier = viewConfiguration.getScaledAmbiguousGestureMultiplier();
    }

    @Override // com.android.systemui.Gefingerpoken
    public boolean onInterceptTouchEvent(@NotNull MotionEvent event) {
        float f;
        Intrinsics.checkNotNullParameter(event, "event");
        float x = event.getX();
        float y = event.getY();
        int actionMasked = event.getActionMasked();
        if (actionMasked == 0) {
            this.draggedFarEnough = false;
            this.isDraggingDown = false;
            this.startingChild = null;
            this.initialTouchY = y;
            this.initialTouchX = x;
        } else if (actionMasked == 2) {
            float f2 = y - this.initialTouchY;
            if (event.getClassification() == 1) {
                f = this.touchSlop * this.slopMultiplier;
            } else {
                f = this.touchSlop;
            }
            if (f2 > f && f2 > Math.abs(x - this.initialTouchX)) {
                this.falsingCollector.onNotificationStartDraggingDown();
                this.isDraggingDown = true;
                captureStartingChild(this.initialTouchX, this.initialTouchY);
                this.initialTouchY = y;
                this.initialTouchX = x;
                this.dragDownCallback.onDragDownStarted$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                this.dragDownAmountOnStart = this.dragDownCallback.getDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                return this.startingChild != null || this.dragDownCallback.isDragDownAnywhereEnabled$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
            }
        }
        return false;
    }

    @Override // com.android.systemui.Gefingerpoken
    public boolean onTouchEvent(@NotNull MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (!this.isDraggingDown) {
            return false;
        }
        event.getX();
        float y = event.getY();
        int actionMasked = event.getActionMasked();
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                float f = this.initialTouchY;
                this.lastHeight = y - f;
                captureStartingChild(this.initialTouchX, f);
                this.dragDownCallback.setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(this.lastHeight + this.dragDownAmountOnStart);
                ExpandableView expandableView = this.startingChild;
                if (expandableView != null) {
                    float f2 = this.lastHeight;
                    Intrinsics.checkNotNull(expandableView);
                    handleExpansion(f2, expandableView);
                }
                if (this.lastHeight > this.minDragDistance) {
                    if (!this.draggedFarEnough) {
                        this.draggedFarEnough = true;
                        this.dragDownCallback.onCrossedThreshold$frameworks__base__packages__SystemUI__android_common__SystemUI_core(true);
                    }
                } else if (this.draggedFarEnough) {
                    this.draggedFarEnough = false;
                    this.dragDownCallback.onCrossedThreshold$frameworks__base__packages__SystemUI__android_common__SystemUI_core(false);
                }
                return true;
            } else if (actionMasked == 3) {
                stopDragging();
                return false;
            }
        } else if (!this.falsingManager.isUnlockingDisabled() && !isFalseTouch() && this.dragDownCallback.canDragDown$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            this.dragDownCallback.onDraggedDown$frameworks__base__packages__SystemUI__android_common__SystemUI_core(this.startingChild, (int) (y - this.initialTouchY));
            if (this.startingChild != null) {
                getExpandCallback().setUserLockedChild(this.startingChild, false);
                this.startingChild = null;
            }
            this.isDraggingDown = false;
        } else {
            stopDragging();
            return false;
        }
        return false;
    }

    private final void captureStartingChild(float f, float f2) {
        if (this.startingChild == null) {
            ExpandableView findView = findView(f, f2);
            this.startingChild = findView;
            if (findView == null) {
                return;
            }
            if (this.dragDownCallback.isDragDownEnabledForView$frameworks__base__packages__SystemUI__android_common__SystemUI_core(findView)) {
                getExpandCallback().setUserLockedChild(this.startingChild, true);
            } else {
                this.startingChild = null;
            }
        }
    }

    private final void handleExpansion(float f, ExpandableView expandableView) {
        if (f < 0.0f) {
            f = 0.0f;
        }
        boolean isContentExpandable = expandableView.isContentExpandable();
        float f2 = f * (isContentExpandable ? 0.5f : 0.15f);
        if (isContentExpandable && expandableView.getCollapsedHeight() + f2 > expandableView.getMaxContentHeight()) {
            f2 -= ((expandableView.getCollapsedHeight() + f2) - expandableView.getMaxContentHeight()) * 0.85f;
        }
        expandableView.setActualHeight((int) (expandableView.getCollapsedHeight() + f2));
    }

    private final void cancelChildExpansion(final ExpandableView expandableView) {
        if (expandableView.getActualHeight() == expandableView.getCollapsedHeight()) {
            getExpandCallback().setUserLockedChild(expandableView, false);
            return;
        }
        ObjectAnimator ofInt = ObjectAnimator.ofInt(expandableView, "actualHeight", expandableView.getActualHeight(), expandableView.getCollapsedHeight());
        ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofInt.setDuration(375L);
        ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.DragDownHelper$cancelChildExpansion$1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@NotNull Animator animation) {
                Intrinsics.checkNotNullParameter(animation, "animation");
                DragDownHelper.this.getExpandCallback().setUserLockedChild(expandableView, false);
            }
        });
        ofInt.start();
    }

    private final void stopDragging() {
        this.falsingCollector.onNotificationStopDraggingDown();
        ExpandableView expandableView = this.startingChild;
        if (expandableView != null) {
            Intrinsics.checkNotNull(expandableView);
            cancelChildExpansion(expandableView);
            this.startingChild = null;
        }
        this.isDraggingDown = false;
        this.dragDownCallback.onDragDownReset$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
    }

    private final ExpandableView findView(float f, float f2) {
        getHost().getLocationOnScreen(this.temp2);
        ExpandHelper.Callback expandCallback = getExpandCallback();
        int[] iArr = this.temp2;
        return expandCallback.getChildAtRawPosition(f + iArr[0], f2 + iArr[1]);
    }
}
