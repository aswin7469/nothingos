package com.android.p019wm.shell.compatui.letterboxedu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.IntProperty;
import android.util.Log;
import android.util.Property;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.Animation;
import androidx.constraintlayout.motion.widget.Key;
import com.android.internal.R;
import com.android.internal.policy.TransitionAnimation;
import com.android.p019wm.shell.transition.Transitions;

/* renamed from: com.android.wm.shell.compatui.letterboxedu.LetterboxEduAnimationController */
class LetterboxEduAnimationController {
    private static final Property<Drawable, Integer> DRAWABLE_ALPHA = new IntProperty<Drawable>(Key.ALPHA) {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Integer) obj2);
        }

        public void setValue(Drawable drawable, int i) {
            drawable.setAlpha(i);
        }

        public Integer get(Drawable drawable) {
            return Integer.valueOf(drawable.getAlpha());
        }
    };
    private static final int ENTER_ANIM_START_DELAY_MILLIS = (Transitions.ENABLE_SHELL_TRANSITIONS ? 300 : 500);
    private static final String TAG = "LetterboxEduAnimation";
    private final int mAnimStyleResId;
    /* access modifiers changed from: private */
    public Animator mBackgroundDimAnimator;
    private Animation mDialogAnimation;
    private final String mPackageName;
    private final TransitionAnimation mTransitionAnimation;

    static /* synthetic */ void lambda$startExitAnimation$2() {
    }

    LetterboxEduAnimationController(Context context) {
        this.mTransitionAnimation = new TransitionAnimation(context, false, TAG);
        this.mAnimStyleResId = new ContextThemeWrapper(context, 16974550).getTheme().obtainStyledAttributes(R.styleable.Window).getResourceId(8, 0);
        this.mPackageName = context.getPackageName();
    }

    /* access modifiers changed from: package-private */
    public void startEnterAnimation(LetterboxEduDialogLayout letterboxEduDialogLayout, Runnable runnable) {
        cancelAnimation();
        View dialogContainer = letterboxEduDialogLayout.getDialogContainer();
        Animation loadAnimation = loadAnimation(0);
        this.mDialogAnimation = loadAnimation;
        if (loadAnimation == null) {
            runnable.run();
            return;
        }
        loadAnimation.setAnimationListener(getAnimationListener(new LetterboxEduAnimationController$$ExternalSyntheticLambda2(dialogContainer), new LetterboxEduAnimationController$$ExternalSyntheticLambda3(this, runnable)));
        Animator alphaAnimator = getAlphaAnimator(letterboxEduDialogLayout.getBackgroundDim(), 204, this.mDialogAnimation.getDuration());
        this.mBackgroundDimAnimator = alphaAnimator;
        alphaAnimator.addListener(getDimAnimatorListener());
        Animation animation = this.mDialogAnimation;
        int i = ENTER_ANIM_START_DELAY_MILLIS;
        animation.setStartOffset((long) i);
        this.mBackgroundDimAnimator.setStartDelay((long) i);
        dialogContainer.startAnimation(this.mDialogAnimation);
        this.mBackgroundDimAnimator.start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startEnterAnimation$1$com-android-wm-shell-compatui-letterboxedu-LetterboxEduAnimationController */
    public /* synthetic */ void mo49433xa0979e06(Runnable runnable) {
        this.mDialogAnimation = null;
        runnable.run();
    }

    /* access modifiers changed from: package-private */
    public void startExitAnimation(LetterboxEduDialogLayout letterboxEduDialogLayout, Runnable runnable) {
        cancelAnimation();
        View dialogContainer = letterboxEduDialogLayout.getDialogContainer();
        Animation loadAnimation = loadAnimation(1);
        this.mDialogAnimation = loadAnimation;
        if (loadAnimation == null) {
            runnable.run();
            return;
        }
        loadAnimation.setAnimationListener(getAnimationListener(new LetterboxEduAnimationController$$ExternalSyntheticLambda0(), new LetterboxEduAnimationController$$ExternalSyntheticLambda1(this, dialogContainer, runnable)));
        Animator alphaAnimator = getAlphaAnimator(letterboxEduDialogLayout.getBackgroundDim(), 0, this.mDialogAnimation.getDuration());
        this.mBackgroundDimAnimator = alphaAnimator;
        alphaAnimator.addListener(getDimAnimatorListener());
        dialogContainer.startAnimation(this.mDialogAnimation);
        this.mBackgroundDimAnimator.start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startExitAnimation$3$com-android-wm-shell-compatui-letterboxedu-LetterboxEduAnimationController */
    public /* synthetic */ void mo49434xbcf86834(View view, Runnable runnable) {
        view.setAlpha(0.0f);
        this.mDialogAnimation = null;
        runnable.run();
    }

    /* access modifiers changed from: package-private */
    public void cancelAnimation() {
        Animation animation = this.mDialogAnimation;
        if (animation != null) {
            animation.cancel();
            this.mDialogAnimation = null;
        }
        Animator animator = this.mBackgroundDimAnimator;
        if (animator != null) {
            animator.cancel();
            this.mBackgroundDimAnimator = null;
        }
    }

    private Animation loadAnimation(int i) {
        Animation loadAnimationAttr = this.mTransitionAnimation.loadAnimationAttr(this.mPackageName, this.mAnimStyleResId, i, false);
        if (loadAnimationAttr == null) {
            Log.e(TAG, "Failed to load animation " + i);
        }
        return loadAnimationAttr;
    }

    private Animation.AnimationListener getAnimationListener(final Runnable runnable, final Runnable runnable2) {
        return new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                runnable.run();
            }

            public void onAnimationEnd(Animation animation) {
                runnable2.run();
            }
        };
    }

    private AnimatorListenerAdapter getDimAnimatorListener() {
        return new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                Animator unused = LetterboxEduAnimationController.this.mBackgroundDimAnimator = null;
            }
        };
    }

    private static Animator getAlphaAnimator(Drawable drawable, int i, long j) {
        ObjectAnimator ofInt = ObjectAnimator.ofInt(drawable, DRAWABLE_ALPHA, new int[]{i});
        ofInt.setDuration(j);
        return ofInt;
    }
}
