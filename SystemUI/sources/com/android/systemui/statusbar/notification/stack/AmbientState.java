package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.util.MathUtils;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import java.p026io.PrintWriter;
import javax.inject.Inject;

@SysUISingleton
public class AmbientState implements Dumpable {
    private static final float MAX_PULSE_HEIGHT = 100000.0f;
    private static final boolean NOTIFICATIONS_HAVE_SHADOWS = false;
    private ActivatableNotificationView mActivatedChild;
    private float mAppearFraction;
    private boolean mAppearing;
    private int mBaseZHeight;
    private final StackScrollAlgorithm.BypassController mBypassController;
    private boolean mClearAllInProgress;
    private int mContentHeight;
    private float mCurrentScrollVelocity;
    private boolean mDimmed;
    private float mDozeAmount = 0.0f;
    private boolean mDozing;
    private float mExpandingVelocity;
    private boolean mExpansionChanging;
    private float mExpansionFraction;
    private float mFractionToShade;
    private boolean mHasAlertEntries;
    private float mHideAmount;
    private boolean mHideSensitive;
    private boolean mIsFlinging;
    private boolean mIsSwipingUp;
    private ExpandableView mLastVisibleBackgroundChild;
    private int mLayoutHeight;
    private int mLayoutMaxHeight;
    private int mLayoutMinHeight;
    private float mMaxHeadsUpTranslation;
    private boolean mNeedFlingAfterLockscreenSwipeUp = false;
    private Runnable mOnPulseHeightChangedListener;
    private float mOverExpansion;
    private float mOverScrollBottomAmount;
    private float mOverScrollTopAmount;
    private boolean mPanelFullWidth;
    private boolean mPanelTracking;
    private float mPulseHeight = MAX_PULSE_HEIGHT;
    private boolean mPulsing;
    private int mScrollY;
    private final StackScrollAlgorithm.SectionProvider mSectionProvider;
    private boolean mShadeExpanded;
    private NotificationShelf mShelf;
    private float mStackEndHeight;
    private float mStackHeight = 0.0f;
    private int mStackTopMargin;
    private float mStackTranslation;
    private float mStackY = 0.0f;
    private StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private int mStatusBarState;
    private int mTopPadding;
    private ExpandableNotificationRow mTrackedHeadsUpRow;
    private boolean mUnlockHintRunning;
    private int mZDistanceBetweenElements;

    private static int getBaseHeight(int i) {
        return 0;
    }

    public void setFractionToShade(float f) {
        this.mFractionToShade = f;
    }

    public float getFractionToShade() {
        return this.mFractionToShade;
    }

    public float getStackEndHeight() {
        return this.mStackEndHeight;
    }

    public void setStackEndHeight(float f) {
        this.mStackEndHeight = f;
    }

    public void setStackY(float f) {
        this.mStackY = f;
    }

    public float getStackY() {
        return this.mStackY;
    }

    public void setExpansionFraction(float f) {
        this.mExpansionFraction = f;
    }

    public void setSwipingUp(boolean z) {
        if (!z && this.mIsSwipingUp) {
            this.mNeedFlingAfterLockscreenSwipeUp = true;
        }
        this.mIsSwipingUp = z;
    }

    public boolean isSwipingUp() {
        return this.mIsSwipingUp;
    }

    public void setIsFlinging(boolean z) {
        if (isOnKeyguard() && !z && this.mIsFlinging) {
            this.mNeedFlingAfterLockscreenSwipeUp = false;
        }
        this.mIsFlinging = z;
    }

    public float getExpansionFraction() {
        return this.mExpansionFraction;
    }

    public void setStackHeight(float f) {
        this.mStackHeight = f;
    }

    public float getStackHeight() {
        return this.mStackHeight;
    }

    @Inject
    public AmbientState(Context context, DumpManager dumpManager, StackScrollAlgorithm.SectionProvider sectionProvider, StackScrollAlgorithm.BypassController bypassController, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        this.mSectionProvider = sectionProvider;
        this.mBypassController = bypassController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        reload(context);
        dumpManager.registerDumpable(this);
    }

    public void reload(Context context) {
        int zDistanceBetweenElements = getZDistanceBetweenElements(context);
        this.mZDistanceBetweenElements = zDistanceBetweenElements;
        this.mBaseZHeight = getBaseHeight(zDistanceBetweenElements);
    }

    /* access modifiers changed from: package-private */
    public void setOverExpansion(float f) {
        this.mOverExpansion = f;
    }

    /* access modifiers changed from: package-private */
    public float getOverExpansion() {
        return this.mOverExpansion;
    }

    private static int getZDistanceBetweenElements(Context context) {
        return Math.max(1, context.getResources().getDimensionPixelSize(C1894R.dimen.z_distance_between_notifications));
    }

    public static int getNotificationLaunchHeight(Context context) {
        return getZDistanceBetweenElements(context) * 4;
    }

    public int getBaseZHeight() {
        return this.mBaseZHeight;
    }

    public int getZDistanceBetweenElements() {
        return this.mZDistanceBetweenElements;
    }

    public int getScrollY() {
        return this.mScrollY;
    }

    public void setScrollY(int i) {
        this.mScrollY = Math.max(i, 0);
    }

    public void setDimmed(boolean z) {
        this.mDimmed = z;
    }

    public void setDozing(boolean z) {
        this.mDozing = z;
    }

    public void setHideAmount(float f) {
        if (f == 1.0f && this.mHideAmount != f) {
            setPulseHeight(MAX_PULSE_HEIGHT);
        }
        this.mHideAmount = f;
    }

    public float getHideAmount() {
        return this.mHideAmount;
    }

    public void setHideSensitive(boolean z) {
        this.mHideSensitive = z;
    }

    public void setActivatedChild(ActivatableNotificationView activatableNotificationView) {
        this.mActivatedChild = activatableNotificationView;
    }

    public boolean isDimmed() {
        return this.mDimmed && (!isPulseExpanding() || this.mDozeAmount != 1.0f);
    }

    public boolean isDozing() {
        return this.mDozing;
    }

    public boolean isHideSensitive() {
        return this.mHideSensitive;
    }

    public ActivatableNotificationView getActivatedChild() {
        return this.mActivatedChild;
    }

    public void setOverScrollAmount(float f, boolean z) {
        if (z) {
            this.mOverScrollTopAmount = f;
        } else {
            this.mOverScrollBottomAmount = f;
        }
    }

    public boolean isBypassEnabled() {
        return this.mBypassController.isBypassEnabled();
    }

    public float getOverScrollAmount(boolean z) {
        return z ? this.mOverScrollTopAmount : this.mOverScrollBottomAmount;
    }

    public StackScrollAlgorithm.SectionProvider getSectionProvider() {
        return this.mSectionProvider;
    }

    public float getStackTranslation() {
        return this.mStackTranslation;
    }

    public void setStackTranslation(float f) {
        this.mStackTranslation = f;
    }

    public void setLayoutHeight(int i) {
        this.mLayoutHeight = i;
    }

    public void setLayoutMaxHeight(int i) {
        this.mLayoutMaxHeight = i;
    }

    public int getLayoutMaxHeight() {
        return this.mLayoutMaxHeight;
    }

    public float getTopPadding() {
        return (float) this.mTopPadding;
    }

    public void setTopPadding(int i) {
        this.mTopPadding = i;
    }

    public int getInnerHeight() {
        return getInnerHeight(false);
    }

    public int getInnerHeight(boolean z) {
        if (this.mDozeAmount == 1.0f && !isPulseExpanding()) {
            return this.mShelf.getHeight();
        }
        int max = Math.max(this.mLayoutMinHeight, Math.min(this.mLayoutHeight, this.mContentHeight) - this.mTopPadding);
        if (z) {
            return max;
        }
        float f = (float) max;
        return (int) MathUtils.lerp(f, Math.min(this.mPulseHeight, f), this.mDozeAmount);
    }

    public boolean isPulseExpanding() {
        return (this.mPulseHeight == MAX_PULSE_HEIGHT || this.mDozeAmount == 0.0f || this.mHideAmount == 1.0f) ? false : true;
    }

    public boolean isShadeExpanded() {
        return this.mShadeExpanded;
    }

    public void setShadeExpanded(boolean z) {
        this.mShadeExpanded = z;
    }

    public void setMaxHeadsUpTranslation(float f) {
        this.mMaxHeadsUpTranslation = f;
    }

    public float getMaxHeadsUpTranslation() {
        return this.mMaxHeadsUpTranslation;
    }

    public void setClearAllInProgress(boolean z) {
        this.mClearAllInProgress = z;
    }

    public boolean isClearAllInProgress() {
        return this.mClearAllInProgress;
    }

    public void setLayoutMinHeight(int i) {
        this.mLayoutMinHeight = i;
    }

    public void setShelf(NotificationShelf notificationShelf) {
        this.mShelf = notificationShelf;
    }

    public NotificationShelf getShelf() {
        return this.mShelf;
    }

    public void setContentHeight(int i) {
        this.mContentHeight = i;
    }

    public float getContentHeight() {
        return (float) this.mContentHeight;
    }

    public void setLastVisibleBackgroundChild(ExpandableView expandableView) {
        this.mLastVisibleBackgroundChild = expandableView;
    }

    public ExpandableView getLastVisibleBackgroundChild() {
        return this.mLastVisibleBackgroundChild;
    }

    public void setCurrentScrollVelocity(float f) {
        this.mCurrentScrollVelocity = f;
    }

    public float getCurrentScrollVelocity() {
        return this.mCurrentScrollVelocity;
    }

    public boolean isOnKeyguard() {
        return this.mStatusBarState == 1;
    }

    public void setStatusBarState(int i) {
        if (this.mStatusBarState != 1) {
            this.mNeedFlingAfterLockscreenSwipeUp = false;
        }
        this.mStatusBarState = i;
    }

    public void setExpandingVelocity(float f) {
        this.mExpandingVelocity = f;
    }

    public void setExpansionChanging(boolean z) {
        this.mExpansionChanging = z;
    }

    public boolean isExpansionChanging() {
        return this.mExpansionChanging;
    }

    public float getExpandingVelocity() {
        return this.mExpandingVelocity;
    }

    public void setPanelTracking(boolean z) {
        this.mPanelTracking = z;
    }

    public boolean hasPulsingNotifications() {
        return this.mPulsing && this.mHasAlertEntries;
    }

    public void setPulsing(boolean z) {
        this.mPulsing = z;
    }

    public boolean isPulsing() {
        return this.mPulsing;
    }

    public boolean isPulsing(NotificationEntry notificationEntry) {
        return this.mPulsing && notificationEntry.isAlerting();
    }

    public boolean isPanelTracking() {
        return this.mPanelTracking;
    }

    public boolean isPanelFullWidth() {
        return this.mPanelFullWidth;
    }

    public void setPanelFullWidth(boolean z) {
        this.mPanelFullWidth = z;
    }

    public void setUnlockHintRunning(boolean z) {
        this.mUnlockHintRunning = z;
    }

    public boolean isUnlockHintRunning() {
        return this.mUnlockHintRunning;
    }

    public boolean isFlingingAfterSwipeUpOnLockscreen() {
        return this.mIsFlinging && this.mNeedFlingAfterLockscreenSwipeUp;
    }

    public boolean isDozingAndNotPulsing(ExpandableView expandableView) {
        if (expandableView instanceof ExpandableNotificationRow) {
            return isDozingAndNotPulsing((ExpandableNotificationRow) expandableView);
        }
        return false;
    }

    public boolean isDozingAndNotPulsing(ExpandableNotificationRow expandableNotificationRow) {
        return isDozing() && !isPulsing(expandableNotificationRow.getEntry());
    }

    public boolean isFullyHidden() {
        return this.mHideAmount == 1.0f;
    }

    public boolean isHiddenAtAll() {
        return this.mHideAmount != 0.0f;
    }

    public void setAppearing(boolean z) {
        this.mAppearing = z;
    }

    public boolean isAppearing() {
        return this.mAppearing;
    }

    public void setPulseHeight(float f) {
        if (f != this.mPulseHeight) {
            this.mPulseHeight = f;
            Runnable runnable = this.mOnPulseHeightChangedListener;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public float getPulseHeight() {
        float f = this.mPulseHeight;
        if (f == MAX_PULSE_HEIGHT) {
            return 0.0f;
        }
        return f;
    }

    public void setDozeAmount(float f) {
        if (f != this.mDozeAmount) {
            this.mDozeAmount = f;
            if (f == 0.0f || f == 1.0f) {
                setPulseHeight(MAX_PULSE_HEIGHT);
            }
        }
    }

    public float getDozeAmount() {
        return this.mDozeAmount;
    }

    public boolean isFullyAwake() {
        return this.mDozeAmount == 0.0f;
    }

    public void setOnPulseHeightChangedListener(Runnable runnable) {
        this.mOnPulseHeightChangedListener = runnable;
    }

    public void setTrackedHeadsUpRow(ExpandableNotificationRow expandableNotificationRow) {
        this.mTrackedHeadsUpRow = expandableNotificationRow;
    }

    public ExpandableNotificationRow getTrackedHeadsUpRow() {
        ExpandableNotificationRow expandableNotificationRow = this.mTrackedHeadsUpRow;
        if (expandableNotificationRow == null || !expandableNotificationRow.isAboveShelf()) {
            return null;
        }
        return this.mTrackedHeadsUpRow;
    }

    public void setAppearFraction(float f) {
        this.mAppearFraction = f;
    }

    public float getAppearFraction() {
        return this.mAppearFraction;
    }

    public void setHasAlertEntries(boolean z) {
        this.mHasAlertEntries = z;
    }

    public void setStackTopMargin(int i) {
        this.mStackTopMargin = i;
    }

    public int getStackTopMargin() {
        return this.mStackTopMargin;
    }

    public boolean isBouncerInTransit() {
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        return statusBarKeyguardViewManager != null && statusBarKeyguardViewManager.isBouncerInTransit();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("mTopPadding=" + this.mTopPadding);
        printWriter.println("mStackTopMargin=" + this.mStackTopMargin);
        printWriter.println("mStackTranslation=" + this.mStackTranslation);
        printWriter.println("mLayoutMinHeight=" + this.mLayoutMinHeight);
        printWriter.println("mLayoutMaxHeight=" + this.mLayoutMaxHeight);
        printWriter.println("mLayoutHeight=" + this.mLayoutHeight);
        printWriter.println("mContentHeight=" + this.mContentHeight);
        printWriter.println("mHideSensitive=" + this.mHideSensitive);
        printWriter.println("mShadeExpanded=" + this.mShadeExpanded);
        printWriter.println("mClearAllInProgress=" + this.mClearAllInProgress);
        printWriter.println("mDimmed=" + this.mDimmed);
        printWriter.println("mStatusBarState=" + this.mStatusBarState);
        printWriter.println("mExpansionChanging=" + this.mExpansionChanging);
        printWriter.println("mPanelFullWidth=" + this.mPanelFullWidth);
        printWriter.println("mPulsing=" + this.mPulsing);
        printWriter.println("mPulseHeight=" + this.mPulseHeight);
        printWriter.println("mTrackedHeadsUpRow.key=" + NotificationUtils.logKey(this.mTrackedHeadsUpRow));
        printWriter.println("mMaxHeadsUpTranslation=" + this.mMaxHeadsUpTranslation);
        printWriter.println("mUnlockHintRunning=" + this.mUnlockHintRunning);
        printWriter.println("mDozeAmount=" + this.mDozeAmount);
        printWriter.println("mDozing=" + this.mDozing);
        printWriter.println("mFractionToShade=" + this.mFractionToShade);
        printWriter.println("mHideAmount=" + this.mHideAmount);
        printWriter.println("mAppearFraction=" + this.mAppearFraction);
        printWriter.println("mAppearing=" + this.mAppearing);
        printWriter.println("mExpansionFraction=" + this.mExpansionFraction);
        printWriter.println("mExpandingVelocity=" + this.mExpandingVelocity);
        printWriter.println("mOverScrollTopAmount=" + this.mOverScrollTopAmount);
        printWriter.println("mOverScrollBottomAmount=" + this.mOverScrollBottomAmount);
        printWriter.println("mOverExpansion=" + this.mOverExpansion);
        printWriter.println("mStackHeight=" + this.mStackHeight);
        printWriter.println("mStackEndHeight=" + this.mStackEndHeight);
        printWriter.println("mStackY=" + this.mStackY);
        printWriter.println("mScrollY=" + this.mScrollY);
        printWriter.println("mCurrentScrollVelocity=" + this.mCurrentScrollVelocity);
        printWriter.println("mIsSwipingUp=" + this.mIsSwipingUp);
        printWriter.println("mPanelTracking=" + this.mPanelTracking);
        printWriter.println("mIsFlinging=" + this.mIsFlinging);
        printWriter.println("mNeedFlingAfterLockscreenSwipeUp=" + this.mNeedFlingAfterLockscreenSwipeUp);
        printWriter.println("mZDistanceBetweenElements=" + this.mZDistanceBetweenElements);
        printWriter.println("mBaseZHeight=" + this.mBaseZHeight);
    }
}
