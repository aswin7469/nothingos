package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.service.notification.StatusBarNotification;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.SwipeHelper;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import java.lang.ref.WeakReference;
import javax.inject.Inject;

class NotificationSwipeHelper extends SwipeHelper implements NotificationSwipeActionHelper {
    protected static final long COVER_MENU_DELAY = 4000;
    private static final long SWIPE_MENU_TIMING = 200;
    private static final String TAG = "NotificationSwipeHelper";
    private final NotificationCallback mCallback;
    private WeakReference<NotificationMenuRowPlugin> mCurrMenuRowRef;
    private final Runnable mFalsingCheck = new NotificationSwipeHelper$$ExternalSyntheticLambda0(this);
    private boolean mIsExpanded;
    private View mMenuExposedView;
    private final NotificationMenuRowPlugin.OnMenuEventListener mMenuListener;
    private boolean mPulsing;
    private View mTranslatingParentView;

    public interface NotificationCallback extends SwipeHelper.Callback {
        float getTotalTranslationLength(View view);

        void handleChildViewDismissed(View view);

        void onDismiss();

        void onSnooze(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption);

        boolean shouldDismissQuickly();
    }

    NotificationSwipeHelper(Resources resources, ViewConfiguration viewConfiguration, FalsingManager falsingManager, FeatureFlags featureFlags, int i, NotificationCallback notificationCallback, NotificationMenuRowPlugin.OnMenuEventListener onMenuEventListener) {
        super(i, notificationCallback, resources, viewConfiguration, falsingManager, featureFlags);
        this.mMenuListener = onMenuEventListener;
        this.mCallback = notificationCallback;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-notification-stack-NotificationSwipeHelper */
    public /* synthetic */ void mo42574x9fcf074d() {
        resetExposedMenuView(true, true);
    }

    public View getTranslatingParentView() {
        return this.mTranslatingParentView;
    }

    public void clearTranslatingParentView() {
        setTranslatingParentView((View) null);
    }

    /* access modifiers changed from: protected */
    public void setTranslatingParentView(View view) {
        this.mTranslatingParentView = view;
    }

    public void setExposedMenuView(View view) {
        this.mMenuExposedView = view;
    }

    public void clearExposedMenuView() {
        setExposedMenuView((View) null);
    }

    public void clearCurrentMenuRow() {
        setCurrentMenuRow((NotificationMenuRowPlugin) null);
    }

    public View getExposedMenuView() {
        return this.mMenuExposedView;
    }

    /* access modifiers changed from: package-private */
    public void setCurrentMenuRow(NotificationMenuRowPlugin notificationMenuRowPlugin) {
        this.mCurrMenuRowRef = notificationMenuRowPlugin != null ? new WeakReference<>(notificationMenuRowPlugin) : null;
    }

    public NotificationMenuRowPlugin getCurrentMenuRow() {
        WeakReference<NotificationMenuRowPlugin> weakReference = this.mCurrMenuRowRef;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    /* access modifiers changed from: protected */
    public Handler getHandler() {
        return this.mHandler;
    }

    /* access modifiers changed from: protected */
    public Runnable getFalsingCheck() {
        return this.mFalsingCheck;
    }

    public void setIsExpanded(boolean z) {
        this.mIsExpanded = z;
    }

    /* access modifiers changed from: protected */
    public void onChildSnappedBack(View view, float f) {
        NotificationMenuRowPlugin currentMenuRow = getCurrentMenuRow();
        if (currentMenuRow != null && f == 0.0f) {
            currentMenuRow.resetMenu();
            clearCurrentMenuRow();
        }
    }

    public void onDownUpdate(View view, MotionEvent motionEvent) {
        this.mTranslatingParentView = view;
        NotificationMenuRowPlugin currentMenuRow = getCurrentMenuRow();
        if (currentMenuRow != null) {
            currentMenuRow.onTouchStart();
        }
        clearCurrentMenuRow();
        getHandler().removeCallbacks(getFalsingCheck());
        resetExposedMenuView(true, false);
        if (view instanceof SwipeableView) {
            initializeRow((SwipeableView) view);
        }
    }

    /* access modifiers changed from: protected */
    public void initializeRow(SwipeableView swipeableView) {
        if (swipeableView.hasFinishedInitialization()) {
            NotificationMenuRowPlugin createMenu = swipeableView.createMenu();
            setCurrentMenuRow(createMenu);
            if (createMenu != null) {
                createMenu.setMenuClickListener(this.mMenuListener);
                createMenu.onTouchStart();
            }
        }
    }

    private boolean swipedEnoughToShowMenu(NotificationMenuRowPlugin notificationMenuRowPlugin) {
        return !swipedFarEnough() && notificationMenuRowPlugin.isSwipedEnoughToShowMenu();
    }

    public void onMoveUpdate(View view, MotionEvent motionEvent, float f, float f2) {
        getHandler().removeCallbacks(getFalsingCheck());
        NotificationMenuRowPlugin currentMenuRow = getCurrentMenuRow();
        if (currentMenuRow != null) {
            currentMenuRow.onTouchMove(f2);
        }
    }

    public boolean handleUpEvent(MotionEvent motionEvent, View view, float f, float f2) {
        NotificationMenuRowPlugin currentMenuRow = getCurrentMenuRow();
        if (currentMenuRow == null) {
            return false;
        }
        currentMenuRow.onTouchEnd();
        handleMenuRowSwipe(motionEvent, view, f, currentMenuRow);
        return true;
    }

    /* access modifiers changed from: protected */
    public void handleMenuRowSwipe(MotionEvent motionEvent, View view, float f, NotificationMenuRowPlugin notificationMenuRowPlugin) {
        if (!notificationMenuRowPlugin.shouldShowMenu()) {
            if (isDismissGesture(motionEvent)) {
                dismiss(view, f);
                return;
            }
            snapClosed(view, f);
            notificationMenuRowPlugin.onSnapClosed();
        } else if (notificationMenuRowPlugin.isSnappedAndOnSameSide()) {
            handleSwipeFromOpenState(motionEvent, view, f, notificationMenuRowPlugin);
        } else {
            handleSwipeFromClosedState(motionEvent, view, f, notificationMenuRowPlugin);
        }
    }

    private void handleSwipeFromClosedState(MotionEvent motionEvent, View view, float f, NotificationMenuRowPlugin notificationMenuRowPlugin) {
        boolean isDismissGesture = isDismissGesture(motionEvent);
        boolean isTowardsMenu = notificationMenuRowPlugin.isTowardsMenu(f);
        boolean z = true;
        boolean z2 = getEscapeVelocity() <= Math.abs(f);
        boolean z3 = !notificationMenuRowPlugin.canBeDismissed() && ((double) (motionEvent.getEventTime() - motionEvent.getDownTime())) >= 200.0d;
        boolean z4 = isTowardsMenu && !isDismissGesture;
        boolean z5 = (swipedEnoughToShowMenu(notificationMenuRowPlugin) && (!z2 || z3)) || ((z2 && !isTowardsMenu && !isDismissGesture) && (notificationMenuRowPlugin.shouldShowGutsOnSnapOpen() || (this.mIsExpanded && !this.mPulsing)));
        int menuSnapTarget = notificationMenuRowPlugin.getMenuSnapTarget();
        if (isFalseGesture() || !z5) {
            z = false;
        }
        if ((z4 || z) && menuSnapTarget != 0) {
            snapOpen(view, menuSnapTarget, f);
            notificationMenuRowPlugin.onSnapOpen();
        } else if (!isDismissGesture(motionEvent) || isTowardsMenu) {
            snapClosed(view, f);
            notificationMenuRowPlugin.onSnapClosed();
        } else {
            dismiss(view, f);
            notificationMenuRowPlugin.onDismiss();
        }
    }

    private void handleSwipeFromOpenState(MotionEvent motionEvent, View view, float f, NotificationMenuRowPlugin notificationMenuRowPlugin) {
        boolean isDismissGesture = isDismissGesture(motionEvent);
        if (notificationMenuRowPlugin.isWithinSnapMenuThreshold() && !isDismissGesture) {
            notificationMenuRowPlugin.onSnapOpen();
            snapOpen(view, notificationMenuRowPlugin.getMenuSnapTarget(), f);
        } else if (!isDismissGesture || notificationMenuRowPlugin.shouldSnapBack()) {
            snapClosed(view, f);
            notificationMenuRowPlugin.onSnapClosed();
        } else {
            dismiss(view, f);
            notificationMenuRowPlugin.onDismiss();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean isSwiping = isSwiping();
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
        View swipedView = getSwipedView();
        if (!isSwiping && swipedView != null) {
            InteractionJankMonitor.getInstance().begin(swipedView, 4);
        }
        return onInterceptTouchEvent;
    }

    /* access modifiers changed from: protected */
    public void onDismissChildWithAnimationFinished() {
        InteractionJankMonitor.getInstance().end(4);
    }

    public void dismissChild(View view, float f, boolean z) {
        superDismissChild(view, f, z);
        if (this.mCallback.shouldDismissQuickly()) {
            this.mCallback.handleChildViewDismissed(view);
        }
        this.mCallback.onDismiss();
        handleMenuCoveredOrDismissed();
    }

    /* access modifiers changed from: protected */
    public void superDismissChild(View view, float f, boolean z) {
        super.dismissChild(view, f, z);
    }

    /* access modifiers changed from: protected */
    public void onSnapChildWithAnimationFinished() {
        InteractionJankMonitor.getInstance().end(4);
    }

    /* access modifiers changed from: protected */
    public void superSnapChild(View view, float f, float f2) {
        super.snapChild(view, f, f2);
    }

    public void snapChild(View view, float f, float f2) {
        superSnapChild(view, f, f2);
        this.mCallback.onDragCancelled(view);
        if (f == 0.0f) {
            handleMenuCoveredOrDismissed();
        }
    }

    public void snooze(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
        this.mCallback.onSnooze(statusBarNotification, snoozeOption);
    }

    /* access modifiers changed from: protected */
    public void handleMenuCoveredOrDismissed() {
        View exposedMenuView = getExposedMenuView();
        if (exposedMenuView != null && exposedMenuView == this.mTranslatingParentView) {
            clearExposedMenuView();
        }
    }

    /* access modifiers changed from: protected */
    public Animator superGetViewTranslationAnimator(View view, float f, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        return super.getViewTranslationAnimator(view, f, animatorUpdateListener);
    }

    public Animator getViewTranslationAnimator(View view, float f, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        if (view instanceof ExpandableNotificationRow) {
            return ((ExpandableNotificationRow) view).getTranslateViewAnimator(f, animatorUpdateListener);
        }
        return superGetViewTranslationAnimator(view, f, animatorUpdateListener);
    }

    /* access modifiers changed from: protected */
    public float getTotalTranslationLength(View view) {
        return this.mCallback.getTotalTranslationLength(view);
    }

    public void setTranslation(View view, float f) {
        if (view instanceof SwipeableView) {
            ((SwipeableView) view).setTranslation(f);
        }
    }

    public float getTranslation(View view) {
        if (view instanceof SwipeableView) {
            return ((SwipeableView) view).getTranslation();
        }
        return 0.0f;
    }

    public boolean swipedFastEnough(float f, float f2) {
        return swipedFastEnough();
    }

    /* access modifiers changed from: protected */
    public boolean swipedFastEnough() {
        return super.swipedFastEnough();
    }

    public boolean swipedFarEnough(float f, float f2) {
        return swipedFarEnough();
    }

    /* access modifiers changed from: protected */
    public boolean swipedFarEnough() {
        return super.swipedFarEnough();
    }

    public void dismiss(View view, float f) {
        dismissChild(view, f, !swipedFastEnough());
    }

    public void snapOpen(View view, int i, float f) {
        snapChild(view, (float) i, f);
    }

    /* access modifiers changed from: protected */
    public void snapClosed(View view, float f) {
        snapChild(view, 0.0f, f);
    }

    /* access modifiers changed from: protected */
    public float getEscapeVelocity() {
        return super.getEscapeVelocity();
    }

    public float getMinDismissVelocity() {
        return getEscapeVelocity();
    }

    public void onMenuShown(View view) {
        setExposedMenuView(getTranslatingParentView());
        this.mCallback.onDragCancelled(view);
        Handler handler = getHandler();
        if (this.mCallback.isAntiFalsingNeeded()) {
            handler.removeCallbacks(getFalsingCheck());
            handler.postDelayed(getFalsingCheck(), COVER_MENU_DELAY);
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldResetMenu(boolean z) {
        View view = this.mMenuExposedView;
        if (view != null) {
            return z || view != this.mTranslatingParentView;
        }
        return false;
    }

    public void resetExposedMenuView(boolean z, boolean z2) {
        if (shouldResetMenu(z2)) {
            View exposedMenuView = getExposedMenuView();
            if (z) {
                Animator viewTranslationAnimator = getViewTranslationAnimator(exposedMenuView, 0.0f, (ValueAnimator.AnimatorUpdateListener) null);
                if (viewTranslationAnimator != null) {
                    viewTranslationAnimator.start();
                }
            } else if (exposedMenuView instanceof SwipeableView) {
                SwipeableView swipeableView = (SwipeableView) exposedMenuView;
                if (!swipeableView.isRemoved()) {
                    swipeableView.resetTranslation();
                }
            }
            clearExposedMenuView();
        }
    }

    public static boolean isTouchInView(MotionEvent motionEvent, View view) {
        int i;
        if (view == null) {
            return false;
        }
        if (view instanceof ExpandableView) {
            i = ((ExpandableView) view).getActualHeight();
        } else {
            i = view.getHeight();
        }
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int i2 = iArr[0];
        int i3 = iArr[1];
        return new Rect(i2, i3, view.getWidth() + i2, i + i3).contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
    }

    public void setPulsing(boolean z) {
        this.mPulsing = z;
    }

    static class Builder {
        private final FalsingManager mFalsingManager;
        private final FeatureFlags mFeatureFlags;
        private NotificationCallback mNotificationCallback;
        private NotificationMenuRowPlugin.OnMenuEventListener mOnMenuEventListener;
        private final Resources mResources;
        private int mSwipeDirection;
        private final ViewConfiguration mViewConfiguration;

        @Inject
        Builder(@Main Resources resources, ViewConfiguration viewConfiguration, FalsingManager falsingManager, FeatureFlags featureFlags) {
            this.mResources = resources;
            this.mViewConfiguration = viewConfiguration;
            this.mFalsingManager = falsingManager;
            this.mFeatureFlags = featureFlags;
        }

        /* access modifiers changed from: package-private */
        public Builder setSwipeDirection(int i) {
            this.mSwipeDirection = i;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setNotificationCallback(NotificationCallback notificationCallback) {
            this.mNotificationCallback = notificationCallback;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setOnMenuEventListener(NotificationMenuRowPlugin.OnMenuEventListener onMenuEventListener) {
            this.mOnMenuEventListener = onMenuEventListener;
            return this;
        }

        /* access modifiers changed from: package-private */
        public NotificationSwipeHelper build() {
            return new NotificationSwipeHelper(this.mResources, this.mViewConfiguration, this.mFalsingManager, this.mFeatureFlags, this.mSwipeDirection, this.mNotificationCallback, this.mOnMenuEventListener);
        }
    }
}
