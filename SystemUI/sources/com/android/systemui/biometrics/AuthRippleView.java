package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.charging.RippleShader;
import java.util.Objects;
import kotlin.Unit;
import kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: AuthRippleView.kt */
/* loaded from: classes.dex */
public final class AuthRippleView extends View {
    private long alphaInDuration;
    @Nullable
    private Animator dwellPulseOutAnimator;
    private float radius;
    @Nullable
    private Animator retractAnimator;
    @NotNull
    private final Paint ripplePaint;
    @NotNull
    private final RippleShader rippleShader;
    private boolean unlockedRippleInProgress;
    @NotNull
    private final PathInterpolator retractInterpolator = new PathInterpolator(0.05f, 0.93f, 0.1f, 1.0f);
    private final long dwellPulseDuration = 50;
    private final long dwellAlphaDuration = 50;
    private final float dwellAlpha = 1.0f;
    private final long dwellExpandDuration = 1200 - 50;
    private final long aodDwellPulseDuration = 50;
    private long aodDwellAlphaDuration = 50;
    private float aodDwellAlpha = 0.8f;
    private long aodDwellExpandDuration = 1200 - 50;
    private final long retractDuration = 400;
    @NotNull
    private PointF origin = new PointF();

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v4, types: [com.android.systemui.statusbar.charging.RippleShader, android.graphics.Shader] */
    public AuthRippleView(@Nullable Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        ?? rippleShader = new RippleShader();
        this.rippleShader = rippleShader;
        Paint paint = new Paint();
        this.ripplePaint = paint;
        rippleShader.setColor(-1);
        rippleShader.setProgress(0.0f);
        rippleShader.setSparkleStrength(0.4f);
        paint.setShader(rippleShader);
        setVisibility(8);
    }

    private final void setRadius(float f) {
        this.rippleShader.setRadius(f);
        this.radius = f;
    }

    private final void setOrigin(PointF pointF) {
        this.rippleShader.setOrigin(pointF);
        this.origin = pointF;
    }

    public final void setSensorLocation(@NotNull PointF location) {
        float maxOf;
        Intrinsics.checkNotNullParameter(location, "location");
        setOrigin(location);
        maxOf = ComparisonsKt___ComparisonsJvmKt.maxOf(location.x, location.y, getWidth() - location.x, getHeight() - location.y);
        setRadius(maxOf);
    }

    public final void setAlphaInDuration(long j) {
        this.alphaInDuration = j;
    }

    public final void retractRipple() {
        Animator animator = this.retractAnimator;
        Boolean bool = null;
        Boolean valueOf = animator == null ? null : Boolean.valueOf(animator.isRunning());
        Boolean bool2 = Boolean.TRUE;
        if (Intrinsics.areEqual(valueOf, bool2)) {
            return;
        }
        Animator animator2 = this.dwellPulseOutAnimator;
        if (animator2 != null) {
            bool = Boolean.valueOf(animator2.isRunning());
        }
        if (!Intrinsics.areEqual(bool, bool2)) {
            return;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.rippleShader.getProgress(), 0.0f);
        ofFloat.setInterpolator(this.retractInterpolator);
        ofFloat.setDuration(this.retractDuration);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleView$retractRipple$retractRippleAnimator$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                RippleShader rippleShader;
                RippleShader rippleShader2;
                long currentPlayTime = valueAnimator.getCurrentPlayTime();
                rippleShader = AuthRippleView.this.rippleShader;
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                rippleShader.setProgress(((Float) animatedValue).floatValue());
                rippleShader2 = AuthRippleView.this.rippleShader;
                rippleShader2.setTime((float) currentPlayTime);
                AuthRippleView.this.invalidate();
            }
        });
        ValueAnimator ofInt = ValueAnimator.ofInt(255, 0);
        ofInt.setInterpolator(Interpolators.LINEAR);
        ofInt.setDuration(this.retractDuration);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleView$retractRipple$retractAlphaAnimator$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                RippleShader rippleShader;
                RippleShader rippleShader2;
                rippleShader = AuthRippleView.this.rippleShader;
                rippleShader2 = AuthRippleView.this.rippleShader;
                int color = rippleShader2.getColor();
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                rippleShader.setColor(ColorUtils.setAlphaComponent(color, ((Integer) animatedValue).intValue()));
                AuthRippleView.this.invalidate();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofInt);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.biometrics.AuthRippleView$retractRipple$1$1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(@Nullable Animator animator3) {
                Animator animator4;
                RippleShader rippleShader;
                animator4 = AuthRippleView.this.dwellPulseOutAnimator;
                if (animator4 != null) {
                    animator4.cancel();
                }
                rippleShader = AuthRippleView.this.rippleShader;
                rippleShader.setShouldFadeOutRipple(false);
                AuthRippleView.this.setVisibility(0);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@Nullable Animator animator3) {
                AuthRippleView.this.setVisibility(8);
                AuthRippleView.this.resetRippleAlpha();
            }
        });
        animatorSet.start();
        Unit unit = Unit.INSTANCE;
        this.retractAnimator = animatorSet;
    }

    public final void startDwellRipple(float f, float f2, float f3, boolean z) {
        if (!this.unlockedRippleInProgress) {
            Animator animator = this.dwellPulseOutAnimator;
            if (Intrinsics.areEqual(animator == null ? null : Boolean.valueOf(animator.isRunning()), Boolean.TRUE)) {
                return;
            }
            float f4 = this.radius;
            float f5 = (f / f4) / 4.0f;
            float f6 = (f2 / f4) / 4.0f;
            float f7 = (f3 / f4) / 4.0f;
            float f8 = z ? this.aodDwellAlpha : this.dwellAlpha;
            float f9 = 255;
            int i = (int) (f9 * f8);
            int min = Math.min((int) (f9 * (f8 + 0.25f)), 255);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(f5, f6);
            Interpolator interpolator = Interpolators.LINEAR_OUT_SLOW_IN;
            ofFloat.setInterpolator(interpolator);
            ofFloat.setDuration(z ? this.aodDwellPulseDuration : this.dwellPulseDuration);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleView$startDwellRipple$dwellPulseOutRippleAnimator$1$1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    RippleShader rippleShader;
                    RippleShader rippleShader2;
                    long currentPlayTime = valueAnimator.getCurrentPlayTime();
                    rippleShader = AuthRippleView.this.rippleShader;
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                    rippleShader.setProgress(((Float) animatedValue).floatValue());
                    rippleShader2 = AuthRippleView.this.rippleShader;
                    rippleShader2.setTime((float) currentPlayTime);
                    AuthRippleView.this.invalidate();
                }
            });
            ValueAnimator ofInt = ValueAnimator.ofInt(0, i);
            Interpolator interpolator2 = Interpolators.LINEAR;
            ofInt.setInterpolator(interpolator2);
            ofInt.setDuration(z ? this.aodDwellAlphaDuration : this.dwellAlphaDuration);
            ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleView$startDwellRipple$dwellPulseOutAlphaAnimator$1$1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    RippleShader rippleShader;
                    RippleShader rippleShader2;
                    rippleShader = AuthRippleView.this.rippleShader;
                    rippleShader2 = AuthRippleView.this.rippleShader;
                    int color = rippleShader2.getColor();
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                    rippleShader.setColor(ColorUtils.setAlphaComponent(color, ((Integer) animatedValue).intValue()));
                    AuthRippleView.this.invalidate();
                }
            });
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(f6, f7);
            ofFloat2.setInterpolator(interpolator);
            ofFloat2.setDuration(z ? this.aodDwellExpandDuration : this.dwellExpandDuration);
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleView$startDwellRipple$expandDwellRippleAnimator$1$1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    RippleShader rippleShader;
                    RippleShader rippleShader2;
                    long currentPlayTime = valueAnimator.getCurrentPlayTime();
                    rippleShader = AuthRippleView.this.rippleShader;
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                    rippleShader.setProgress(((Float) animatedValue).floatValue());
                    rippleShader2 = AuthRippleView.this.rippleShader;
                    rippleShader2.setTime((float) currentPlayTime);
                    AuthRippleView.this.invalidate();
                }
            });
            ValueAnimator ofInt2 = ValueAnimator.ofInt(i, min);
            ofInt2.setInterpolator(interpolator2);
            ofInt2.setDuration(z ? this.aodDwellExpandDuration : this.dwellExpandDuration);
            ofInt2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleView$startDwellRipple$expandDwellAlphaAnimator$1$1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    RippleShader rippleShader;
                    RippleShader rippleShader2;
                    rippleShader = AuthRippleView.this.rippleShader;
                    rippleShader2 = AuthRippleView.this.rippleShader;
                    int color = rippleShader2.getColor();
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                    rippleShader.setColor(ColorUtils.setAlphaComponent(color, ((Integer) animatedValue).intValue()));
                    AuthRippleView.this.invalidate();
                }
            });
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ofFloat, ofInt);
            AnimatorSet animatorSet2 = new AnimatorSet();
            animatorSet2.playTogether(ofFloat2, ofInt2);
            AnimatorSet animatorSet3 = new AnimatorSet();
            animatorSet3.playSequentially(animatorSet, animatorSet2);
            animatorSet3.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.biometrics.AuthRippleView$startDwellRipple$1$1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(@Nullable Animator animator2) {
                    Animator animator3;
                    RippleShader rippleShader;
                    animator3 = AuthRippleView.this.retractAnimator;
                    if (animator3 != null) {
                        animator3.cancel();
                    }
                    rippleShader = AuthRippleView.this.rippleShader;
                    rippleShader.setShouldFadeOutRipple(false);
                    AuthRippleView.this.setVisibility(0);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(@Nullable Animator animator2) {
                    AuthRippleView.this.setVisibility(8);
                    AuthRippleView.this.resetRippleAlpha();
                }
            });
            animatorSet3.start();
            Unit unit = Unit.INSTANCE;
            this.dwellPulseOutAnimator = animatorSet3;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0030, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r4, r5) != false) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void startUnlockedRipple(@Nullable final Runnable runnable) {
        if (this.unlockedRippleInProgress) {
            return;
        }
        float f = 0.0f;
        long j = this.alphaInDuration;
        Animator animator = this.dwellPulseOutAnimator;
        Boolean bool = null;
        Boolean valueOf = animator == null ? null : Boolean.valueOf(animator.isRunning());
        Boolean bool2 = Boolean.TRUE;
        if (!Intrinsics.areEqual(valueOf, bool2)) {
            Animator animator2 = this.retractAnimator;
            if (animator2 != null) {
                bool = Boolean.valueOf(animator2.isRunning());
            }
        }
        f = this.rippleShader.getProgress();
        j = 0;
        Animator animator3 = this.dwellPulseOutAnimator;
        if (animator3 != null) {
            animator3.cancel();
        }
        Animator animator4 = this.retractAnimator;
        if (animator4 != null) {
            animator4.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(f, 1.0f);
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ofFloat.setDuration(1533L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleView$startUnlockedRipple$rippleAnimator$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                RippleShader rippleShader;
                RippleShader rippleShader2;
                long currentPlayTime = valueAnimator.getCurrentPlayTime();
                rippleShader = AuthRippleView.this.rippleShader;
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                rippleShader.setProgress(((Float) animatedValue).floatValue());
                rippleShader2 = AuthRippleView.this.rippleShader;
                rippleShader2.setTime((float) currentPlayTime);
                AuthRippleView.this.invalidate();
            }
        });
        ValueAnimator ofInt = ValueAnimator.ofInt(0, 255);
        ofInt.setDuration(j);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleView$startUnlockedRipple$alphaInAnimator$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                RippleShader rippleShader;
                RippleShader rippleShader2;
                rippleShader = AuthRippleView.this.rippleShader;
                rippleShader2 = AuthRippleView.this.rippleShader;
                int color = rippleShader2.getColor();
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                rippleShader.setColor(ColorUtils.setAlphaComponent(color, ((Integer) animatedValue).intValue()));
                AuthRippleView.this.invalidate();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofInt);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.biometrics.AuthRippleView$startUnlockedRipple$animatorSet$1$1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(@Nullable Animator animator5) {
                RippleShader rippleShader;
                AuthRippleView.this.unlockedRippleInProgress = true;
                rippleShader = AuthRippleView.this.rippleShader;
                rippleShader.setShouldFadeOutRipple(true);
                AuthRippleView.this.setVisibility(0);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@Nullable Animator animator5) {
                Runnable runnable2 = runnable;
                if (runnable2 != null) {
                    runnable2.run();
                }
                AuthRippleView.this.unlockedRippleInProgress = false;
                AuthRippleView.this.setVisibility(8);
            }
        });
        animatorSet.start();
    }

    public final void resetRippleAlpha() {
        RippleShader rippleShader = this.rippleShader;
        rippleShader.setColor(ColorUtils.setAlphaComponent(rippleShader.getColor(), 255));
    }

    public final void setColor(int i) {
        this.rippleShader.setColor(i);
        resetRippleAlpha();
    }

    @Override // android.view.View
    protected void onDraw(@Nullable Canvas canvas) {
        float f = 1;
        float progress = (f - (((f - this.rippleShader.getProgress()) * (f - this.rippleShader.getProgress())) * (f - this.rippleShader.getProgress()))) * this.radius * 2.0f;
        if (canvas == null) {
            return;
        }
        PointF pointF = this.origin;
        canvas.drawCircle(pointF.x, pointF.y, progress, this.ripplePaint);
    }
}
