package com.android.systemui.statusbar.charging;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ChargingRippleView.kt */
/* loaded from: classes.dex */
public final class ChargingRippleView extends View {
    private float radius;
    private boolean rippleInProgress;
    @NotNull
    private final Paint ripplePaint;
    @NotNull
    private final RippleShader rippleShader;
    private final int defaultColor = -1;
    @NotNull
    private PointF origin = new PointF();
    private long duration = 1750;

    public final void startRipple() {
        startRipple$default(this, null, 1, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1, types: [com.android.systemui.statusbar.charging.RippleShader, android.graphics.Shader] */
    public ChargingRippleView(@Nullable Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        ?? rippleShader = new RippleShader();
        this.rippleShader = rippleShader;
        Paint paint = new Paint();
        this.ripplePaint = paint;
        rippleShader.setColor(-1);
        rippleShader.setProgress(0.0f);
        rippleShader.setSparkleStrength(0.3f);
        paint.setShader(rippleShader);
        setVisibility(8);
    }

    public final boolean getRippleInProgress() {
        return this.rippleInProgress;
    }

    public final void setRippleInProgress(boolean z) {
        this.rippleInProgress = z;
    }

    public final void setRadius(float f) {
        this.rippleShader.setRadius(f);
        this.radius = f;
    }

    public final void setOrigin(@NotNull PointF value) {
        Intrinsics.checkNotNullParameter(value, "value");
        this.rippleShader.setOrigin(value);
        this.origin = value;
    }

    public final void setDuration(long j) {
        this.duration = j;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(@Nullable Configuration configuration) {
        this.rippleShader.setPixelDensity(getResources().getDisplayMetrics().density);
        super.onConfigurationChanged(configuration);
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        this.rippleShader.setPixelDensity(getResources().getDisplayMetrics().density);
        super.onAttachedToWindow();
    }

    public static /* synthetic */ void startRipple$default(ChargingRippleView chargingRippleView, Runnable runnable, int i, Object obj) {
        if ((i & 1) != 0) {
            runnable = null;
        }
        chargingRippleView.startRipple(runnable);
    }

    public final void startRipple(@Nullable final Runnable runnable) {
        if (this.rippleInProgress) {
            return;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(this.duration);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.charging.ChargingRippleView$startRipple$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                RippleShader rippleShader;
                RippleShader rippleShader2;
                RippleShader rippleShader3;
                long currentPlayTime = valueAnimator.getCurrentPlayTime();
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                float floatValue = ((Float) animatedValue).floatValue();
                rippleShader = ChargingRippleView.this.rippleShader;
                rippleShader.setProgress(floatValue);
                rippleShader2 = ChargingRippleView.this.rippleShader;
                rippleShader2.setDistortionStrength(1 - floatValue);
                rippleShader3 = ChargingRippleView.this.rippleShader;
                rippleShader3.setTime((float) currentPlayTime);
                ChargingRippleView.this.invalidate();
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.charging.ChargingRippleView$startRipple$2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@Nullable Animator animator) {
                ChargingRippleView.this.setRippleInProgress(false);
                ChargingRippleView.this.setVisibility(8);
                Runnable runnable2 = runnable;
                if (runnable2 == null) {
                    return;
                }
                runnable2.run();
            }
        });
        ofFloat.start();
        setVisibility(0);
        this.rippleInProgress = true;
    }

    public final void setColor(int i) {
        this.rippleShader.setColor(i);
    }

    @Override // android.view.View
    protected void onDraw(@Nullable Canvas canvas) {
        float f = 1;
        float progress = (f - (((f - this.rippleShader.getProgress()) * (f - this.rippleShader.getProgress())) * (f - this.rippleShader.getProgress()))) * this.radius * 2;
        if (canvas == null) {
            return;
        }
        PointF pointF = this.origin;
        canvas.drawCircle(pointF.x, pointF.y, progress, this.ripplePaint);
    }
}
