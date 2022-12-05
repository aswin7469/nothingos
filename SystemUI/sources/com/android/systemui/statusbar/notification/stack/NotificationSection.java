package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.view.View;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.row.ExpandableView;
/* loaded from: classes.dex */
public class NotificationSection {
    private int mBucket;
    private ExpandableView mFirstVisibleChild;
    private ExpandableView mLastVisibleChild;
    private View mOwningView;
    private Rect mBounds = new Rect();
    private Rect mCurrentBounds = new Rect(-1, -1, -1, -1);
    private Rect mStartAnimationRect = new Rect();
    private Rect mEndAnimationRect = new Rect();
    private ObjectAnimator mTopAnimator = null;
    private ObjectAnimator mBottomAnimator = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public NotificationSection(View view, int i) {
        this.mOwningView = view;
        this.mBucket = i;
    }

    public void cancelAnimators() {
        ObjectAnimator objectAnimator = this.mBottomAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        ObjectAnimator objectAnimator2 = this.mTopAnimator;
        if (objectAnimator2 != null) {
            objectAnimator2.cancel();
        }
    }

    public Rect getCurrentBounds() {
        return this.mCurrentBounds;
    }

    public Rect getBounds() {
        return this.mBounds;
    }

    public boolean didBoundsChange() {
        return !this.mCurrentBounds.equals(this.mBounds);
    }

    public boolean areBoundsAnimating() {
        return (this.mBottomAnimator == null && this.mTopAnimator == null) ? false : true;
    }

    public int getBucket() {
        return this.mBucket;
    }

    public void startBackgroundAnimation(boolean z, boolean z2) {
        Rect rect = this.mCurrentBounds;
        Rect rect2 = this.mBounds;
        rect.left = rect2.left;
        rect.right = rect2.right;
        startBottomAnimation(z2);
        startTopAnimation(z);
    }

    private void startTopAnimation(boolean z) {
        int i = this.mEndAnimationRect.top;
        int i2 = this.mBounds.top;
        ObjectAnimator objectAnimator = this.mTopAnimator;
        if (objectAnimator == null || i != i2) {
            if (!z) {
                if (objectAnimator != null) {
                    int i3 = this.mStartAnimationRect.top;
                    objectAnimator.getValues()[0].setIntValues(i3, i2);
                    this.mStartAnimationRect.top = i3;
                    this.mEndAnimationRect.top = i2;
                    objectAnimator.setCurrentPlayTime(objectAnimator.getCurrentPlayTime());
                    return;
                }
                setBackgroundTop(i2);
                return;
            }
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "backgroundTop", this.mCurrentBounds.top, i2);
            ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofInt.setDuration(360L);
            ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.stack.NotificationSection.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    NotificationSection.this.mStartAnimationRect.top = -1;
                    NotificationSection.this.mEndAnimationRect.top = -1;
                    NotificationSection.this.mTopAnimator = null;
                }
            });
            ofInt.start();
            this.mStartAnimationRect.top = this.mCurrentBounds.top;
            this.mEndAnimationRect.top = i2;
            this.mTopAnimator = ofInt;
        }
    }

    private void startBottomAnimation(boolean z) {
        int i = this.mStartAnimationRect.bottom;
        int i2 = this.mEndAnimationRect.bottom;
        int i3 = this.mBounds.bottom;
        ObjectAnimator objectAnimator = this.mBottomAnimator;
        if (objectAnimator == null || i2 != i3) {
            if (!z) {
                if (objectAnimator != null) {
                    objectAnimator.getValues()[0].setIntValues(i, i3);
                    this.mStartAnimationRect.bottom = i;
                    this.mEndAnimationRect.bottom = i3;
                    objectAnimator.setCurrentPlayTime(objectAnimator.getCurrentPlayTime());
                    return;
                }
                setBackgroundBottom(i3);
                return;
            }
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "backgroundBottom", this.mCurrentBounds.bottom, i3);
            ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofInt.setDuration(360L);
            ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.stack.NotificationSection.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    NotificationSection.this.mStartAnimationRect.bottom = -1;
                    NotificationSection.this.mEndAnimationRect.bottom = -1;
                    NotificationSection.this.mBottomAnimator = null;
                }
            });
            ofInt.start();
            this.mStartAnimationRect.bottom = this.mCurrentBounds.bottom;
            this.mEndAnimationRect.bottom = i3;
            this.mBottomAnimator = ofInt;
        }
    }

    private void setBackgroundTop(int i) {
        this.mCurrentBounds.top = i;
        this.mOwningView.invalidate();
    }

    private void setBackgroundBottom(int i) {
        this.mCurrentBounds.bottom = i;
        this.mOwningView.invalidate();
    }

    public ExpandableView getFirstVisibleChild() {
        return this.mFirstVisibleChild;
    }

    public ExpandableView getLastVisibleChild() {
        return this.mLastVisibleChild;
    }

    public boolean setFirstVisibleChild(ExpandableView expandableView) {
        boolean z = this.mFirstVisibleChild != expandableView;
        this.mFirstVisibleChild = expandableView;
        return z;
    }

    public boolean setLastVisibleChild(ExpandableView expandableView) {
        boolean z = this.mLastVisibleChild != expandableView;
        this.mLastVisibleChild = expandableView;
        return z;
    }

    public void resetCurrentBounds() {
        this.mCurrentBounds.set(this.mBounds);
    }

    public boolean isTargetTop(int i) {
        ObjectAnimator objectAnimator = this.mTopAnimator;
        return (objectAnimator == null && this.mCurrentBounds.top == i) || (objectAnimator != null && this.mEndAnimationRect.top == i);
    }

    public boolean isTargetBottom(int i) {
        ObjectAnimator objectAnimator = this.mBottomAnimator;
        return (objectAnimator == null && this.mCurrentBounds.bottom == i) || (objectAnimator != null && this.mEndAnimationRect.bottom == i);
    }

    public int updateBounds(int i, int i2, boolean z) {
        int i3;
        int i4;
        ExpandableView firstVisibleChild = getFirstVisibleChild();
        if (firstVisibleChild != null) {
            int ceil = (int) Math.ceil(ViewState.getFinalTranslationY(firstVisibleChild));
            i4 = Math.max(isTargetTop(ceil) ? ceil : (int) Math.ceil(firstVisibleChild.getTranslationY()), i);
            if (firstVisibleChild.showingPulsing()) {
                i3 = Math.max(i, ceil + ExpandableViewState.getFinalActualHeight(firstVisibleChild));
                if (z) {
                    Rect rect = this.mBounds;
                    rect.left = (int) (rect.left + Math.max(firstVisibleChild.getTranslation(), 0.0f));
                    Rect rect2 = this.mBounds;
                    rect2.right = (int) (rect2.right + Math.min(firstVisibleChild.getTranslation(), 0.0f));
                }
            } else {
                i3 = i;
            }
        } else {
            i3 = i;
            i4 = i3;
        }
        int max = Math.max(i, i4);
        ExpandableView lastVisibleChild = getLastVisibleChild();
        if (lastVisibleChild != null) {
            int floor = (int) Math.floor((ViewState.getFinalTranslationY(lastVisibleChild) + ExpandableViewState.getFinalActualHeight(lastVisibleChild)) - lastVisibleChild.getClipBottomAmount());
            if (!isTargetBottom(floor)) {
                floor = (int) ((lastVisibleChild.getTranslationY() + lastVisibleChild.getActualHeight()) - lastVisibleChild.getClipBottomAmount());
                i2 = (int) Math.min(lastVisibleChild.getTranslationY() + lastVisibleChild.getActualHeight(), i2);
            }
            i3 = Math.max(i3, Math.max(floor, i2));
        }
        int max2 = Math.max(max, i3);
        Rect rect3 = this.mBounds;
        rect3.top = max;
        rect3.bottom = max2;
        return max2;
    }

    public boolean needsBackground() {
        return (this.mFirstVisibleChild == null || this.mBucket == 1) ? false : true;
    }
}
