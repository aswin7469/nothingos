package com.android.systemui.statusbar.notification.row;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.animation.Interpolators;
/* loaded from: classes.dex */
public abstract class StackScrollerDecorView extends ExpandableView {
    protected View mContent;
    private boolean mContentAnimating;
    protected View mSecondaryView;
    private boolean mIsVisible = true;
    private boolean mContentVisible = true;
    private boolean mIsSecondaryVisible = true;
    private int mDuration = 260;
    private final Runnable mContentVisibilityEndRunnable = new Runnable() { // from class: com.android.systemui.statusbar.notification.row.StackScrollerDecorView$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            StackScrollerDecorView.this.lambda$new$0();
        }
    };
    private boolean mSecondaryAnimating = false;
    private final Runnable mSecondaryVisibilityEndRunnable = new Runnable() { // from class: com.android.systemui.statusbar.notification.row.StackScrollerDecorView$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            StackScrollerDecorView.this.lambda$new$1();
        }
    };

    protected abstract View findContentView();

    protected abstract View findSecondaryView();

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean isTransparent() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean needsClippingToShelf() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mContentAnimating = false;
        if (getVisibility() == 8 || this.mIsVisible) {
            return;
        }
        setVisibility(8);
        setWillBeGone(false);
        notifyHeightChanged(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.mSecondaryAnimating = false;
        if (this.mSecondaryView == null || getVisibility() == 8 || this.mSecondaryView.getVisibility() == 8 || this.mIsSecondaryVisible) {
            return;
        }
        this.mSecondaryView.setVisibility(8);
    }

    public StackScrollerDecorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setClipChildren(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContent = findContentView();
        this.mSecondaryView = findSecondaryView();
        setVisible(false, false);
        setSecondaryVisible(false, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setOutlineProvider(null);
    }

    public void setContentVisible(boolean z) {
        setContentVisible(z, true, null);
    }

    public void setContentVisible(boolean z, boolean z2, final Runnable runnable) {
        if (this.mContentVisible != z) {
            this.mContentAnimating = z2;
            this.mContentVisible = z;
            setViewVisible(this.mContent, z, z2, runnable == null ? this.mContentVisibilityEndRunnable : new Runnable() { // from class: com.android.systemui.statusbar.notification.row.StackScrollerDecorView$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    StackScrollerDecorView.this.lambda$setContentVisible$2(runnable);
                }
            });
        }
        if (!this.mContentAnimating) {
            this.mContentVisibilityEndRunnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setContentVisible$2(Runnable runnable) {
        this.mContentVisibilityEndRunnable.run();
        runnable.run();
    }

    public void setVisible(boolean z, boolean z2) {
        setVisible(z, z2, null);
    }

    public void setVisible(boolean z, boolean z2, Runnable runnable) {
        if (this.mIsVisible != z) {
            this.mIsVisible = z;
            if (z2) {
                if (z) {
                    setVisibility(0);
                    setWillBeGone(false);
                    notifyHeightChanged(false);
                } else {
                    setWillBeGone(true);
                }
                setContentVisible(z, true, runnable);
                return;
            }
            setVisibility(z ? 0 : 8);
            setContentVisible(z, false, runnable);
            setWillBeGone(false);
            notifyHeightChanged(false);
        }
    }

    public void setSecondaryVisible(boolean z, boolean z2) {
        if (this.mIsSecondaryVisible != z) {
            this.mSecondaryAnimating = z2;
            this.mIsSecondaryVisible = z;
            setViewVisible(this.mSecondaryView, z, z2, this.mSecondaryVisibilityEndRunnable);
        }
        if (!this.mSecondaryAnimating) {
            this.mSecondaryVisibilityEndRunnable.run();
        }
    }

    @VisibleForTesting
    boolean isSecondaryVisible() {
        return this.mIsSecondaryVisible;
    }

    public boolean isVisible() {
        return this.mIsVisible;
    }

    private void setViewVisible(View view, boolean z, boolean z2, Runnable runnable) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() != 0) {
            view.setVisibility(0);
        }
        view.animate().cancel();
        float f = z ? 1.0f : 0.0f;
        if (!z2) {
            view.setAlpha(f);
            if (runnable == null) {
                return;
            }
            runnable.run();
            return;
        }
        view.animate().alpha(f).setInterpolator(z ? Interpolators.ALPHA_IN : Interpolators.ALPHA_OUT).setDuration(this.mDuration).withEndAction(runnable);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public long performRemoveAnimation(long j, long j2, float f, boolean z, float f2, Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        setContentVisible(false);
        return 0L;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void performAddAnimation(long j, long j2, boolean z) {
        setContentVisible(true);
    }
}
