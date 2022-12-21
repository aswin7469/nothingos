package androidx.leanback.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.StatsManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.constraintlayout.motion.widget.Key;
import androidx.leanback.C0742R;

public class MediaNowPlayingView extends LinearLayout {
    private final ImageView mImage1;
    private final ImageView mImage2;
    private final ImageView mImage3;
    protected final LinearInterpolator mLinearInterpolator;
    private final ObjectAnimator mObjectAnimator1;
    private final ObjectAnimator mObjectAnimator2;
    private final ObjectAnimator mObjectAnimator3;

    public MediaNowPlayingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        this.mLinearInterpolator = linearInterpolator;
        LayoutInflater.from(context).inflate(C0742R.layout.lb_playback_now_playing_bars, this, true);
        ImageView imageView = (ImageView) findViewById(C0742R.C0745id.bar1);
        this.mImage1 = imageView;
        ImageView imageView2 = (ImageView) findViewById(C0742R.C0745id.bar2);
        this.mImage2 = imageView2;
        ImageView imageView3 = (ImageView) findViewById(C0742R.C0745id.bar3);
        this.mImage3 = imageView3;
        imageView.setPivotY((float) imageView.getDrawable().getIntrinsicHeight());
        imageView2.setPivotY((float) imageView2.getDrawable().getIntrinsicHeight());
        imageView3.setPivotY((float) imageView3.getDrawable().getIntrinsicHeight());
        setDropScale(imageView);
        setDropScale(imageView2);
        setDropScale(imageView3);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(imageView, Key.SCALE_Y, new float[]{0.41666666f, 0.25f, 0.41666666f, 0.5833333f, 0.75f, 0.8333333f, 0.9166667f, 1.0f, 0.9166667f, 1.0f, 0.8333333f, 0.6666667f, 0.5f, 0.33333334f, 0.16666667f, 0.33333334f, 0.5f, 0.5833333f, 0.75f, 0.9166667f, 0.75f, 0.5833333f, 0.41666666f, 0.25f, 0.41666666f, 0.6666667f, 0.41666666f, 0.25f, 0.33333334f, 0.41666666f});
        this.mObjectAnimator1 = ofFloat;
        ofFloat.setRepeatCount(-1);
        ofFloat.setDuration(2320);
        ofFloat.setInterpolator(linearInterpolator);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(imageView2, Key.SCALE_Y, new float[]{1.0f, 0.9166667f, 0.8333333f, 0.9166667f, 1.0f, 0.9166667f, 0.75f, 0.5833333f, 0.75f, 0.9166667f, 1.0f, 0.8333333f, 0.6666667f, 0.8333333f, 1.0f, 0.9166667f, 0.75f, 0.41666666f, 0.25f, 0.41666666f, 0.6666667f, 0.8333333f, 1.0f, 0.8333333f, 0.75f, 0.6666667f, 1.0f});
        this.mObjectAnimator2 = ofFloat2;
        ofFloat2.setRepeatCount(-1);
        ofFloat2.setDuration(2080);
        ofFloat2.setInterpolator(linearInterpolator);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(imageView3, Key.SCALE_Y, new float[]{0.6666667f, 0.75f, 0.8333333f, 1.0f, 0.9166667f, 0.75f, 0.5833333f, 0.41666666f, 0.5833333f, 0.6666667f, 0.75f, 1.0f, 0.9166667f, 1.0f, 0.75f, 0.5833333f, 0.75f, 0.9166667f, 1.0f, 0.8333333f, 0.6666667f, 0.75f, 0.5833333f, 0.41666666f, 0.25f, 0.6666667f});
        this.mObjectAnimator3 = ofFloat3;
        ofFloat3.setRepeatCount(-1);
        ofFloat3.setDuration(StatsManager.DEFAULT_TIMEOUT_MILLIS);
        ofFloat3.setInterpolator(linearInterpolator);
    }

    static void setDropScale(View view) {
        view.setScaleY(0.083333336f);
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i == 8) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getVisibility() == 0) {
            startAnimation();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    private void startAnimation() {
        startAnimation(this.mObjectAnimator1);
        startAnimation(this.mObjectAnimator2);
        startAnimation(this.mObjectAnimator3);
        this.mImage1.setVisibility(0);
        this.mImage2.setVisibility(0);
        this.mImage3.setVisibility(0);
    }

    private void stopAnimation() {
        stopAnimation(this.mObjectAnimator1, this.mImage1);
        stopAnimation(this.mObjectAnimator2, this.mImage2);
        stopAnimation(this.mObjectAnimator3, this.mImage3);
        this.mImage1.setVisibility(8);
        this.mImage2.setVisibility(8);
        this.mImage3.setVisibility(8);
    }

    private void startAnimation(Animator animator) {
        if (!animator.isStarted()) {
            animator.start();
        }
    }

    private void stopAnimation(Animator animator, View view) {
        if (animator.isStarted()) {
            animator.cancel();
            setDropScale(view);
        }
    }
}
