package com.nothingos.keyguard;

import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import com.android.internal.util.Preconditions;
import java.util.Objects;
/* loaded from: classes2.dex */
public class RepeatingVectorAnimation {
    private final AnimatedVectorDrawable mAnimatedVectorDrawable;
    private final Animatable2.AnimationCallback mAnimationCallback;
    private final boolean mShouldLoop;
    private final Handler mUiThreadHandler;

    public RepeatingVectorAnimation(AnimatedVectorDrawable animatedVectorDrawable) {
        this(animatedVectorDrawable, true);
    }

    public RepeatingVectorAnimation(AnimatedVectorDrawable animatedVectorDrawable, boolean z) {
        this.mAnimationCallback = new Animatable2.AnimationCallback() { // from class: com.nothingos.keyguard.RepeatingVectorAnimation.1
            @Override // android.graphics.drawable.Animatable2.AnimationCallback
            public void onAnimationEnd(Drawable drawable) {
                if (RepeatingVectorAnimation.this.mShouldLoop) {
                    Handler handler = RepeatingVectorAnimation.this.mUiThreadHandler;
                    final AnimatedVectorDrawable animatedVectorDrawable2 = RepeatingVectorAnimation.this.mAnimatedVectorDrawable;
                    Objects.requireNonNull(animatedVectorDrawable2);
                    handler.post(new Runnable() { // from class: com.nothingos.keyguard.RepeatingVectorAnimation$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            animatedVectorDrawable2.start();
                        }
                    });
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
