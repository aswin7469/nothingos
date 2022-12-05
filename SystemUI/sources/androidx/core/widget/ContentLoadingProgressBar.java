package androidx.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
/* loaded from: classes.dex */
public class ContentLoadingProgressBar extends ProgressBar {
    long mStartTime = -1;
    boolean mPostedHide = false;
    boolean mPostedShow = false;
    boolean mDismissed = false;
    private final Runnable mDelayedHide = new Runnable() { // from class: androidx.core.widget.ContentLoadingProgressBar$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            ContentLoadingProgressBar.this.lambda$new$0();
        }
    };
    private final Runnable mDelayedShow = new Runnable() { // from class: androidx.core.widget.ContentLoadingProgressBar$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            ContentLoadingProgressBar.this.lambda$new$1();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mPostedHide = false;
        this.mStartTime = -1L;
        setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.mPostedShow = false;
        if (!this.mDismissed) {
            this.mStartTime = System.currentTimeMillis();
            setVisibility(0);
        }
    }

    public ContentLoadingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks();
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks();
    }

    private void removeCallbacks() {
        removeCallbacks(this.mDelayedHide);
        removeCallbacks(this.mDelayedShow);
    }
}
