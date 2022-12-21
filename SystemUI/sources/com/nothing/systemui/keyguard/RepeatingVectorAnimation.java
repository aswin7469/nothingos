package com.nothing.systemui.keyguard;

import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import com.android.internal.util.Preconditions;
import java.util.Objects;

public class RepeatingVectorAnimation {
    /* access modifiers changed from: private */
    public final AnimatedVectorDrawable mAnimatedVectorDrawable;
    private final Animatable2.AnimationCallback mAnimationCallback;
    /* access modifiers changed from: private */
    public final boolean mShouldLoop;
    /* access modifiers changed from: private */
    public final Handler mUiThreadHandler;

    public RepeatingVectorAnimation(AnimatedVectorDrawable animatedVectorDrawable) {
        this(animatedVectorDrawable, true);
    }

    public RepeatingVectorAnimation(AnimatedVectorDrawable animatedVectorDrawable, boolean z) {
        this.mAnimationCallback = new Animatable2.AnimationCallback() {
            public void onAnimationEnd(Drawable drawable) {
                if (RepeatingVectorAnimation.this.mShouldLoop) {
                    Handler access$200 = RepeatingVectorAnimation.this.mUiThreadHandler;
                    AnimatedVectorDrawable access$100 = RepeatingVectorAnimation.this.mAnimatedVectorDrawable;
                    Objects.requireNonNull(access$100);
                    access$200.post(new RepeatingVectorAnimation$1$$ExternalSyntheticLambda0(access$100));
                }
            }
        };
        this.mUiThreadHandler = new Handler();
        this.mAnimatedVectorDrawable = (AnimatedVectorDrawable) Preconditions.checkNotNull(animatedVectorDrawable);
        this.mShouldLoop = z;
    }

    public void start() {
        this.mAnimatedVectorDrawable.unregisterAnimationCallback(this.mAnimationCallback);
        this.mAnimatedVectorDrawable.registerAnimationCallback(this.mAnimationCallback);
        this.mAnimatedVectorDrawable.reset();
        this.mAnimatedVectorDrawable.start();
    }

    public void stop() {
        this.mAnimatedVectorDrawable.stop();
        this.mAnimatedVectorDrawable.unregisterAnimationCallback(this.mAnimationCallback);
    }
}
