package com.android.systemui.statusbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.app.NotificationCompat;
import com.android.systemui.C1893R;
import com.android.systemui.ExpandHelper;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0010\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020'H\u0002J\u0018\u0010.\u001a\u00020,2\u0006\u0010/\u001a\u00020\f2\u0006\u00100\u001a\u00020\fH\u0002J\u001a\u00101\u001a\u0004\u0018\u00010'2\u0006\u0010/\u001a\u00020\f2\u0006\u00100\u001a\u00020\fH\u0002J\u0018\u00102\u001a\u00020,2\u0006\u00103\u001a\u00020\f2\u0006\u0010-\u001a\u00020'H\u0002J\u0010\u00104\u001a\u00020\u000e2\u0006\u00105\u001a\u000206H\u0016J\u0010\u00107\u001a\u00020\u000e2\u0006\u00105\u001a\u000206H\u0016J\b\u00108\u001a\u00020,H\u0002J\u000e\u00109\u001a\u00020,2\u0006\u0010\b\u001a\u00020\tR\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u00020\u0010X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u00020\u0016X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u000e\u0010\u001b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u001d\u001a\u00020\u000e8F¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u001eR\u001e\u0010 \u001a\u00020\u000e2\u0006\u0010\u001f\u001a\u00020\u000e@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u001eR\u0014\u0010!\u001a\u00020\u000e8BX\u0004¢\u0006\u0006\u001a\u0004\b!\u0010\u001eR\u000e\u0010\"\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010'X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000¨\u0006:"}, mo64987d2 = {"Lcom/android/systemui/statusbar/DragDownHelper;", "Lcom/android/systemui/Gefingerpoken;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "falsingCollector", "Lcom/android/systemui/classifier/FalsingCollector;", "dragDownCallback", "Lcom/android/systemui/statusbar/LockscreenShadeTransitionController;", "context", "Landroid/content/Context;", "(Lcom/android/systemui/plugins/FalsingManager;Lcom/android/systemui/classifier/FalsingCollector;Lcom/android/systemui/statusbar/LockscreenShadeTransitionController;Landroid/content/Context;)V", "dragDownAmountOnStart", "", "draggedFarEnough", "", "expandCallback", "Lcom/android/systemui/ExpandHelper$Callback;", "getExpandCallback", "()Lcom/android/systemui/ExpandHelper$Callback;", "setExpandCallback", "(Lcom/android/systemui/ExpandHelper$Callback;)V", "host", "Landroid/view/View;", "getHost", "()Landroid/view/View;", "setHost", "(Landroid/view/View;)V", "initialTouchX", "initialTouchY", "isDragDownEnabled", "()Z", "<set-?>", "isDraggingDown", "isFalseTouch", "lastHeight", "minDragDistance", "", "slopMultiplier", "startingChild", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "temp2", "", "touchSlop", "cancelChildExpansion", "", "child", "captureStartingChild", "x", "y", "findView", "handleExpansion", "heightDelta", "onInterceptTouchEvent", "event", "Landroid/view/MotionEvent;", "onTouchEvent", "stopDragging", "updateResources", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LockscreenShadeTransitionController.kt */
public final class DragDownHelper implements Gefingerpoken {
    private float dragDownAmountOnStart;
    private final LockscreenShadeTransitionController dragDownCallback;
    private boolean draggedFarEnough;
    public ExpandHelper.Callback expandCallback;
    private final FalsingCollector falsingCollector;
    private final FalsingManager falsingManager;
    public View host;
    private float initialTouchX;
    private float initialTouchY;
    private boolean isDraggingDown;
    private float lastHeight;
    private int minDragDistance;
    private float slopMultiplier;
    private ExpandableView startingChild;
    private final int[] temp2 = new int[2];
    private float touchSlop;

    public DragDownHelper(FalsingManager falsingManager2, FalsingCollector falsingCollector2, LockscreenShadeTransitionController lockscreenShadeTransitionController, Context context) {
        Intrinsics.checkNotNullParameter(falsingManager2, "falsingManager");
        Intrinsics.checkNotNullParameter(falsingCollector2, "falsingCollector");
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController, "dragDownCallback");
        Intrinsics.checkNotNullParameter(context, "context");
        this.falsingManager = falsingManager2;
        this.falsingCollector = falsingCollector2;
        this.dragDownCallback = lockscreenShadeTransitionController;
        updateResources(context);
    }

    public final ExpandHelper.Callback getExpandCallback() {
        ExpandHelper.Callback callback = this.expandCallback;
        if (callback != null) {
            return callback;
        }
        Intrinsics.throwUninitializedPropertyAccessException("expandCallback");
        return null;
    }

    public final void setExpandCallback(ExpandHelper.Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "<set-?>");
        this.expandCallback = callback;
    }

    public final View getHost() {
        View view = this.host;
        if (view != null) {
            return view;
        }
        Intrinsics.throwUninitializedPropertyAccessException("host");
        return null;
    }

    public final void setHost(View view) {
        Intrinsics.checkNotNullParameter(view, "<set-?>");
        this.host = view;
    }

    public final boolean isDraggingDown() {
        return this.isDraggingDown;
    }

    private final boolean isFalseTouch() {
        if (!this.dragDownCallback.isFalsingCheckNeeded$SystemUI_nothingRelease()) {
            return false;
        }
        if (this.falsingManager.isFalseTouch(2) || !this.draggedFarEnough) {
            return true;
        }
        return false;
    }

    public final boolean isDragDownEnabled() {
        return this.dragDownCallback.isDragDownEnabledForView$SystemUI_nothingRelease((ExpandableView) null);
    }

    public final void updateResources(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.minDragDistance = context.getResources().getDimensionPixelSize(C1893R.dimen.keyguard_drag_down_min_distance);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.touchSlop = (float) viewConfiguration.getScaledTouchSlop();
        this.slopMultiplier = viewConfiguration.getScaledAmbiguousGestureMultiplier();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        float f;
        Intrinsics.checkNotNullParameter(motionEvent, NotificationCompat.CATEGORY_EVENT);
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.draggedFarEnough = false;
            this.isDraggingDown = false;
            this.startingChild = null;
            this.initialTouchY = y;
            this.initialTouchX = x;
        } else if (actionMasked == 2) {
            float f2 = y - this.initialTouchY;
            if (motionEvent.getClassification() == 1) {
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
                this.dragDownCallback.onDragDownStarted$SystemUI_nothingRelease(this.startingChild);
                this.dragDownAmountOnStart = this.dragDownCallback.getDragDownAmount();
                if (this.startingChild != null || this.dragDownCallback.isDragDownAnywhereEnabled$SystemUI_nothingRelease()) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, NotificationCompat.CATEGORY_EVENT);
        if (!this.isDraggingDown) {
            return false;
        }
        motionEvent.getX();
        float y = motionEvent.getY();
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                float f = this.initialTouchY;
                this.lastHeight = y - f;
                captureStartingChild(this.initialTouchX, f);
                this.dragDownCallback.setDragDownAmount(this.lastHeight + this.dragDownAmountOnStart);
                ExpandableView expandableView = this.startingChild;
                if (expandableView != null) {
                    float f2 = this.lastHeight;
                    Intrinsics.checkNotNull(expandableView);
                    handleExpansion(f2, expandableView);
                }
                if (this.lastHeight > ((float) this.minDragDistance)) {
                    if (!this.draggedFarEnough) {
                        this.draggedFarEnough = true;
                        this.dragDownCallback.onCrossedThreshold$SystemUI_nothingRelease(true);
                    }
                } else if (this.draggedFarEnough) {
                    this.draggedFarEnough = false;
                    this.dragDownCallback.onCrossedThreshold$SystemUI_nothingRelease(false);
                }
                return true;
            } else if (actionMasked == 3) {
                stopDragging();
                return false;
            }
        } else if (this.falsingManager.isUnlockingDisabled() || isFalseTouch() || !this.dragDownCallback.canDragDown$SystemUI_nothingRelease()) {
            stopDragging();
            return false;
        } else {
            this.dragDownCallback.onDraggedDown$SystemUI_nothingRelease(this.startingChild, (int) (y - this.initialTouchY));
            if (this.startingChild != null) {
                getExpandCallback().setUserLockedChild(this.startingChild, false);
                this.startingChild = null;
            }
            this.isDraggingDown = false;
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
            if (this.dragDownCallback.isDragDownEnabledForView$SystemUI_nothingRelease(findView)) {
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
        if (isContentExpandable && ((float) expandableView.getCollapsedHeight()) + f2 > ((float) expandableView.getMaxContentHeight())) {
            f2 -= ((((float) expandableView.getCollapsedHeight()) + f2) - ((float) expandableView.getMaxContentHeight())) * 0.85f;
        }
        expandableView.setActualHeight((int) (((float) expandableView.getCollapsedHeight()) + f2));
    }

    private final void cancelChildExpansion(ExpandableView expandableView) {
        if (expandableView.getActualHeight() == expandableView.getCollapsedHeight()) {
            getExpandCallback().setUserLockedChild(expandableView, false);
            return;
        }
        ObjectAnimator ofInt = ObjectAnimator.ofInt(expandableView, "actualHeight", new int[]{expandableView.getActualHeight(), expandableView.getCollapsedHeight()});
        ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofInt.setDuration(375);
        ofInt.addListener(new DragDownHelper$cancelChildExpansion$1(this, expandableView));
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
        this.dragDownCallback.onDragDownReset$SystemUI_nothingRelease();
    }

    private final ExpandableView findView(float f, float f2) {
        getHost().getLocationOnScreen(this.temp2);
        ExpandHelper.Callback expandCallback2 = getExpandCallback();
        int[] iArr = this.temp2;
        return expandCallback2.getChildAtRawPosition(f + ((float) iArr[0]), f2 + ((float) iArr[1]));
    }
}
