package androidx.leanback.app;

import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public final class ProgressBarManager {
    private static final long DEFAULT_PROGRESS_BAR_DELAY = 1000;
    boolean mEnableProgressBar = true;
    private Handler mHandler = new Handler();
    private long mInitialDelay = 1000;
    boolean mIsShowing;
    View mProgressBarView;
    boolean mUserProvidedProgressBar;
    ViewGroup rootView;
    private Runnable runnable = new Runnable() {
        public void run() {
            if (!ProgressBarManager.this.mEnableProgressBar) {
                return;
            }
            if ((!ProgressBarManager.this.mUserProvidedProgressBar && ProgressBarManager.this.rootView == null) || !ProgressBarManager.this.mIsShowing) {
                return;
            }
            if (ProgressBarManager.this.mProgressBarView == null) {
                ProgressBarManager.this.mProgressBarView = new ProgressBar(ProgressBarManager.this.rootView.getContext(), (AttributeSet) null, 16842874);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
                layoutParams.gravity = 17;
                ProgressBarManager.this.rootView.addView(ProgressBarManager.this.mProgressBarView, layoutParams);
            } else if (ProgressBarManager.this.mUserProvidedProgressBar) {
                ProgressBarManager.this.mProgressBarView.setVisibility(0);
            }
        }
    };

    public void setRootView(ViewGroup viewGroup) {
        this.rootView = viewGroup;
    }

    public void show() {
        if (this.mEnableProgressBar) {
            this.mIsShowing = true;
            this.mHandler.postDelayed(this.runnable, this.mInitialDelay);
        }
    }

    public void hide() {
        this.mIsShowing = false;
        if (this.mUserProvidedProgressBar) {
            this.mProgressBarView.setVisibility(4);
        } else {
            View view = this.mProgressBarView;
            if (view != null) {
                this.rootView.removeView(view);
                this.mProgressBarView = null;
            }
        }
        this.mHandler.removeCallbacks(this.runnable);
    }

    public void setProgressBarView(View view) {
        if (view == null || view.getParent() != null) {
            this.mProgressBarView = view;
            if (view != null) {
                view.setVisibility(4);
                this.mUserProvidedProgressBar = true;
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Must have a parent");
    }

    public long getInitialDelay() {
        return this.mInitialDelay;
    }

    public void setInitialDelay(long j) {
        this.mInitialDelay = j;
    }

    public void disableProgressBar() {
        this.mEnableProgressBar = false;
    }

    public void enableProgressBar() {
        this.mEnableProgressBar = true;
    }
}
