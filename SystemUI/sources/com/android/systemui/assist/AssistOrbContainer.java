package com.android.systemui.assist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
/* loaded from: classes.dex */
public class AssistOrbContainer extends FrameLayout {
    private boolean mAnimatingOut;
    private View mNavbarScrim;
    private AssistOrbView mOrb;
    private View mScrim;

    public AssistOrbContainer(Context context) {
        this(context, null);
    }

    public AssistOrbContainer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AssistOrbContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mScrim = findViewById(R$id.assist_orb_scrim);
        this.mNavbarScrim = findViewById(R$id.assist_orb_navbar_scrim);
        this.mOrb = (AssistOrbView) findViewById(R$id.assist_orb);
    }

    public void show(boolean z, boolean z2, final Runnable runnable) {
        if (!z) {
            if (z2) {
                startExitAnimation(new Runnable() { // from class: com.android.systemui.assist.AssistOrbContainer.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AssistOrbContainer.this.mAnimatingOut = false;
                        AssistOrbContainer.this.setVisibility(8);
                        Runnable runnable2 = runnable;
                        if (runnable2 != null) {
                            runnable2.run();
                        }
                    }
                });
                return;
            }
            setVisibility(8);
            if (runnable == null) {
                return;
            }
            runnable.run();
        } else if (getVisibility() == 0) {
        } else {
            setVisibility(0);
            if (z2) {
                startEnterAnimation(runnable);
                return;
            }
            reset();
            if (runnable == null) {
                return;
            }
            runnable.run();
        }
    }

    private void reset() {
        this.mAnimatingOut = false;
        this.mOrb.reset();
        this.mScrim.setAlpha(1.0f);
        this.mNavbarScrim.setAlpha(1.0f);
    }

    private void startEnterAnimation(final Runnable runnable) {
        if (this.mAnimatingOut) {
            return;
        }
        this.mOrb.startEnterAnimation();
        this.mScrim.setAlpha(0.0f);
        this.mNavbarScrim.setAlpha(0.0f);
        post(new Runnable() { // from class: com.android.systemui.assist.AssistOrbContainer.2
            @Override // java.lang.Runnable
            public void run() {
                ViewPropertyAnimator startDelay = AssistOrbContainer.this.mScrim.animate().alpha(1.0f).setDuration(300L).setStartDelay(0L);
                Interpolator interpolator = Interpolators.LINEAR_OUT_SLOW_IN;
                startDelay.setInterpolator(interpolator);
                AssistOrbContainer.this.mNavbarScrim.animate().alpha(1.0f).setDuration(300L).setStartDelay(0L).setInterpolator(interpolator).withEndAction(runnable);
            }
        });
    }

    private void startExitAnimation(Runnable runnable) {
        if (this.mAnimatingOut) {
            if (runnable == null) {
                return;
            }
            runnable.run();
            return;
        }
        this.mAnimatingOut = true;
        this.mOrb.startExitAnimation(150L);
        ViewPropertyAnimator startDelay = this.mScrim.animate().alpha(0.0f).setDuration(250L).setStartDelay(150L);
        Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
        startDelay.setInterpolator(interpolator);
        this.mNavbarScrim.animate().alpha(0.0f).setDuration(250L).setStartDelay(150L).setInterpolator(interpolator).withEndAction(runnable);
    }

    public boolean isShowing() {
        return getVisibility() == 0 && !this.mAnimatingOut;
    }

    public AssistOrbView getOrb() {
        return this.mOrb;
    }
}
