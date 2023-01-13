package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.StatsManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.PathInterpolator;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.charging.DwellRippleShader;
import com.android.systemui.statusbar.charging.RippleShader;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.biometrics.AuthRippleControllerEx;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010-\u001a\u00020.J\u0012\u0010/\u001a\u00020.2\b\u00100\u001a\u0004\u0018\u000101H\u0014J\u0006\u00102\u001a\u00020.J\u0006\u00103\u001a\u00020.J\u0006\u00104\u001a\u00020.J\u000e\u00105\u001a\u00020.2\u0006\u00106\u001a\u00020\bJ\u0016\u00107\u001a\u00020.2\u0006\u00108\u001a\u00020\u000e2\u0006\u00109\u001a\u00020\u0017J\u000e\u0010:\u001a\u00020.2\u0006\u0010;\u001a\u00020 J\u000e\u0010<\u001a\u00020.2\u0006\u00108\u001a\u00020\u000eJ\u000e\u0010=\u001a\u00020.2\u0006\u0010>\u001a\u00020\nJ\u0010\u0010?\u001a\u00020.2\b\u0010@\u001a\u0004\u0018\u00010AJ\u000e\u0010B\u001a\u00020.2\u0006\u0010>\u001a\u00020\nR\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bXD¢\u0006\u0002\n\u0000R\u001e\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\r\u001a\u00020\u000e@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\bXD¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0018\u001a\u00020\u00172\u0006\u0010\r\u001a\u00020\u0017@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\u0019\u0010\u001aR\u000e\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\bXD¢\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010!\u001a\u00020\u000e2\u0006\u0010\r\u001a\u00020\u000e@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\"\u0010\u0011R\u001e\u0010#\u001a\u00020\u00172\u0006\u0010\r\u001a\u00020\u0017@BX\u000e¢\u0006\b\n\u0000\"\u0004\b$\u0010\u001aR\u000e\u0010%\u001a\u00020\bXD¢\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000¨\u0006C"}, mo65043d2 = {"Lcom/android/systemui/biometrics/AuthRippleView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "alphaInDuration", "", "drawDwell", "", "drawRipple", "dwellExpandDuration", "value", "Landroid/graphics/PointF;", "dwellOrigin", "setDwellOrigin", "(Landroid/graphics/PointF;)V", "dwellPaint", "Landroid/graphics/Paint;", "dwellPulseDuration", "dwellPulseOutAnimator", "Landroid/animation/Animator;", "", "dwellRadius", "setDwellRadius", "(F)V", "dwellShader", "Lcom/android/systemui/statusbar/charging/DwellRippleShader;", "fadeDuration", "fadeDwellAnimator", "lockScreenColorVal", "", "origin", "setOrigin", "radius", "setRadius", "retractDuration", "retractDwellAnimator", "retractInterpolator", "Landroid/view/animation/PathInterpolator;", "ripplePaint", "rippleShader", "Lcom/android/systemui/statusbar/charging/RippleShader;", "unlockedRippleInProgress", "fadeDwellRipple", "", "onDraw", "canvas", "Landroid/graphics/Canvas;", "resetDwellAlpha", "resetRippleAlpha", "retractDwellRipple", "setAlphaInDuration", "duration", "setFingerprintSensorLocation", "location", "sensorRadius", "setLockScreenColor", "color", "setSensorLocation", "startDwellRipple", "isDozing", "startUnlockedRipple", "onAnimationEnd", "Ljava/lang/Runnable;", "updateDwellRippleColor", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AuthRippleView.kt */
public final class AuthRippleView extends View {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private long alphaInDuration;
    /* access modifiers changed from: private */
    public boolean drawDwell;
    /* access modifiers changed from: private */
    public boolean drawRipple;
    private final long dwellExpandDuration;
    private PointF dwellOrigin;
    private final Paint dwellPaint;
    private final long dwellPulseDuration;
    /* access modifiers changed from: private */
    public Animator dwellPulseOutAnimator;
    private float dwellRadius;
    private final DwellRippleShader dwellShader;
    private final long fadeDuration;
    /* access modifiers changed from: private */
    public Animator fadeDwellAnimator;
    private int lockScreenColorVal;
    private PointF origin;
    private float radius;
    private final long retractDuration;
    /* access modifiers changed from: private */
    public Animator retractDwellAnimator;
    private final PathInterpolator retractInterpolator;
    private final Paint ripplePaint;
    /* access modifiers changed from: private */
    public final RippleShader rippleShader;
    /* access modifiers changed from: private */
    public boolean unlockedRippleInProgress;

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public AuthRippleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.retractInterpolator = new PathInterpolator(0.05f, 0.93f, 0.1f, 1.0f);
        this.dwellPulseDuration = 100;
        this.dwellExpandDuration = StatsManager.DEFAULT_TIMEOUT_MILLIS - 100;
        this.lockScreenColorVal = -1;
        this.fadeDuration = 83;
        this.retractDuration = 400;
        DwellRippleShader dwellRippleShader = new DwellRippleShader();
        this.dwellShader = dwellRippleShader;
        Paint paint = new Paint();
        this.dwellPaint = paint;
        RippleShader rippleShader2 = new RippleShader();
        this.rippleShader = rippleShader2;
        Paint paint2 = new Paint();
        this.ripplePaint = paint2;
        this.dwellOrigin = new PointF();
        this.origin = new PointF();
        rippleShader2.setColor(-1);
        rippleShader2.setProgress(0.0f);
        rippleShader2.setSparkleStrength(0.4f);
        paint2.setShader((Shader) rippleShader2);
        dwellRippleShader.setColor(-1);
        dwellRippleShader.setProgress(0.0f);
        dwellRippleShader.setDistortionStrength(0.4f);
        paint.setShader((Shader) dwellRippleShader);
        setVisibility(8);
    }

    private final void setDwellRadius(float f) {
        this.dwellShader.setMaxRadius(f);
        this.dwellRadius = f;
    }

    private final void setDwellOrigin(PointF pointF) {
        this.dwellShader.setOrigin(pointF);
        this.dwellOrigin = pointF;
    }

    private final void setRadius(float f) {
        this.rippleShader.setRadius(f);
        this.radius = f;
    }

    private final void setOrigin(PointF pointF) {
        this.rippleShader.setOrigin(pointF);
        this.origin = pointF;
    }

    public final void setSensorLocation(PointF pointF) {
        Intrinsics.checkNotNullParameter(pointF, "location");
        setOrigin(pointF);
        setRadius(ComparisonsKt.maxOf(pointF.x, pointF.y, ((float) getWidth()) - pointF.x, ((float) getHeight()) - pointF.y));
    }

    public final void setFingerprintSensorLocation(PointF pointF, float f) {
        Intrinsics.checkNotNullParameter(pointF, "location");
        setOrigin(pointF);
        setRadius(ComparisonsKt.maxOf(pointF.x, pointF.y, ((float) getWidth()) - pointF.x, ((float) getHeight()) - pointF.y));
        setDwellOrigin(pointF);
        setDwellRadius(f * 1.5f);
    }

    public final void setAlphaInDuration(long j) {
        this.alphaInDuration = j;
    }

    public final void retractDwellRipple() {
        Animator animator = this.retractDwellAnimator;
        if (!(animator != null && animator.isRunning())) {
            Animator animator2 = this.fadeDwellAnimator;
            if (!(animator2 != null && animator2.isRunning())) {
                Animator animator3 = this.dwellPulseOutAnimator;
                if (animator3 != null && animator3.isRunning()) {
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.dwellShader.getProgress(), 0.0f});
                    ofFloat.setInterpolator(this.retractInterpolator);
                    ofFloat.setDuration(this.retractDuration);
                    ofFloat.addUpdateListener(new AuthRippleView$$ExternalSyntheticLambda0(this));
                    ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{255, 0});
                    ofInt.setInterpolator(Interpolators.LINEAR);
                    ofInt.setDuration(this.retractDuration);
                    ofInt.addUpdateListener(new AuthRippleView$$ExternalSyntheticLambda1(this));
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(new Animator[]{ofFloat, ofInt});
                    animatorSet.addListener(new AuthRippleView$retractDwellRipple$1$1(this));
                    animatorSet.start();
                    this.retractDwellAnimator = animatorSet;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: retractDwellRipple$lambda-1$lambda-0  reason: not valid java name */
    public static final void m2572retractDwellRipple$lambda1$lambda0(AuthRippleView authRippleView, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(authRippleView, "this$0");
        long currentPlayTime = valueAnimator.getCurrentPlayTime();
        DwellRippleShader dwellRippleShader = authRippleView.dwellShader;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            dwellRippleShader.setProgress(((Float) animatedValue).floatValue());
            authRippleView.dwellShader.setTime((float) currentPlayTime);
            authRippleView.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    /* access modifiers changed from: private */
    /* renamed from: retractDwellRipple$lambda-3$lambda-2  reason: not valid java name */
    public static final void m2573retractDwellRipple$lambda3$lambda2(AuthRippleView authRippleView, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(authRippleView, "this$0");
        DwellRippleShader dwellRippleShader = authRippleView.dwellShader;
        int color = dwellRippleShader.getColor();
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            dwellRippleShader.setColor(ColorUtils.setAlphaComponent(color, ((Integer) animatedValue).intValue()));
            authRippleView.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    public final void fadeDwellRipple() {
        Animator animator = this.fadeDwellAnimator;
        if (!(animator != null && animator.isRunning())) {
            Animator animator2 = this.dwellPulseOutAnimator;
            if (!(animator2 != null && animator2.isRunning())) {
                Animator animator3 = this.retractDwellAnimator;
                if (!(animator3 != null && animator3.isRunning())) {
                    return;
                }
            }
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{Color.alpha(this.dwellShader.getColor()), 0});
            ofInt.setInterpolator(Interpolators.LINEAR);
            ofInt.setDuration(this.fadeDuration);
            ofInt.addUpdateListener(new AuthRippleView$$ExternalSyntheticLambda6(this));
            ofInt.addListener(new AuthRippleView$fadeDwellRipple$1$2(this));
            ofInt.start();
            this.fadeDwellAnimator = ofInt;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: fadeDwellRipple$lambda-6$lambda-5  reason: not valid java name */
    public static final void m2571fadeDwellRipple$lambda6$lambda5(AuthRippleView authRippleView, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(authRippleView, "this$0");
        DwellRippleShader dwellRippleShader = authRippleView.dwellShader;
        int color = dwellRippleShader.getColor();
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            dwellRippleShader.setColor(ColorUtils.setAlphaComponent(color, ((Integer) animatedValue).intValue()));
            authRippleView.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    public final void startDwellRipple(boolean z) {
        if (!this.unlockedRippleInProgress) {
            Animator animator = this.dwellPulseOutAnimator;
            if (!(animator != null && animator.isRunning())) {
                updateDwellRippleColor(z);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 0.8f});
                ofFloat.setInterpolator(Interpolators.LINEAR);
                ofFloat.setDuration(this.dwellPulseDuration);
                ofFloat.addUpdateListener(new AuthRippleView$$ExternalSyntheticLambda2(this));
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.8f, 1.0f});
                ofFloat2.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
                ofFloat2.setDuration(this.dwellExpandDuration);
                ofFloat2.addUpdateListener(new AuthRippleView$$ExternalSyntheticLambda3(this));
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(new Animator[]{ofFloat, ofFloat2});
                animatorSet.addListener(new AuthRippleView$startDwellRipple$1$1(this));
                animatorSet.start();
                this.dwellPulseOutAnimator = animatorSet;
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: startDwellRipple$lambda-8$lambda-7  reason: not valid java name */
    public static final void m2575startDwellRipple$lambda8$lambda7(AuthRippleView authRippleView, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(authRippleView, "this$0");
        long currentPlayTime = valueAnimator.getCurrentPlayTime();
        DwellRippleShader dwellRippleShader = authRippleView.dwellShader;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            dwellRippleShader.setProgress(((Float) animatedValue).floatValue());
            authRippleView.dwellShader.setTime((float) currentPlayTime);
            authRippleView.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    /* access modifiers changed from: private */
    /* renamed from: startDwellRipple$lambda-10$lambda-9  reason: not valid java name */
    public static final void m2574startDwellRipple$lambda10$lambda9(AuthRippleView authRippleView, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(authRippleView, "this$0");
        long currentPlayTime = valueAnimator.getCurrentPlayTime();
        DwellRippleShader dwellRippleShader = authRippleView.dwellShader;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            dwellRippleShader.setProgress(((Float) animatedValue).floatValue());
            authRippleView.dwellShader.setTime((float) currentPlayTime);
            authRippleView.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public final void startUnlockedRipple(Runnable runnable) {
        if (!this.unlockedRippleInProgress) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
            long j = 0;
            ofFloat.setDuration(((AuthRippleControllerEx) NTDependencyEx.get(AuthRippleControllerEx.class)).isDeviceLandscape() ? 0 : AuthRippleController.RIPPLE_ANIMATION_DURATION);
            ofFloat.addUpdateListener(new AuthRippleView$$ExternalSyntheticLambda4(this));
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{0, 255});
            if (!((AuthRippleControllerEx) NTDependencyEx.get(AuthRippleControllerEx.class)).isDeviceLandscape()) {
                j = this.alphaInDuration;
            }
            ofInt.setDuration(j);
            ofInt.addUpdateListener(new AuthRippleView$$ExternalSyntheticLambda5(this));
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(new Animator[]{ofFloat, ofInt});
            animatorSet.addListener(new AuthRippleView$startUnlockedRipple$animatorSet$1$1(this, runnable));
            animatorSet.start();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: startUnlockedRipple$lambda-13$lambda-12  reason: not valid java name */
    public static final void m2576startUnlockedRipple$lambda13$lambda12(AuthRippleView authRippleView, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(authRippleView, "this$0");
        long currentPlayTime = valueAnimator.getCurrentPlayTime();
        RippleShader rippleShader2 = authRippleView.rippleShader;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            rippleShader2.setProgress(((Float) animatedValue).floatValue());
            authRippleView.rippleShader.setTime((float) currentPlayTime);
            authRippleView.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    /* access modifiers changed from: private */
    /* renamed from: startUnlockedRipple$lambda-15$lambda-14  reason: not valid java name */
    public static final void m2577startUnlockedRipple$lambda15$lambda14(AuthRippleView authRippleView, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(authRippleView, "this$0");
        RippleShader rippleShader2 = authRippleView.rippleShader;
        int color = rippleShader2.getColor();
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            rippleShader2.setColor(ColorUtils.setAlphaComponent(color, ((Integer) animatedValue).intValue()));
            authRippleView.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    public final void resetRippleAlpha() {
        RippleShader rippleShader2 = this.rippleShader;
        rippleShader2.setColor(ColorUtils.setAlphaComponent(rippleShader2.getColor(), 255));
    }

    public final void setLockScreenColor(int i) {
        this.lockScreenColorVal = i;
        this.rippleShader.setColor(i);
        resetRippleAlpha();
    }

    public final void updateDwellRippleColor(boolean z) {
        if (z) {
            this.dwellShader.setColor(-1);
        } else {
            this.dwellShader.setColor(this.lockScreenColorVal);
        }
        resetDwellAlpha();
    }

    public final void resetDwellAlpha() {
        DwellRippleShader dwellRippleShader = this.dwellShader;
        dwellRippleShader.setColor(ColorUtils.setAlphaComponent(dwellRippleShader.getColor(), 255));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.drawDwell) {
            float f = (float) 1;
            float progress = (f - (((f - this.dwellShader.getProgress()) * (f - this.dwellShader.getProgress())) * (f - this.dwellShader.getProgress()))) * this.dwellRadius * 2.0f;
            if (canvas != null) {
                canvas.drawCircle(this.dwellOrigin.x, this.dwellOrigin.y, progress, this.dwellPaint);
            }
        }
        if (this.drawRipple) {
            float f2 = (float) 1;
            float progress2 = (f2 - (((f2 - this.rippleShader.getProgress()) * (f2 - this.rippleShader.getProgress())) * (f2 - this.rippleShader.getProgress()))) * this.radius * 2.0f;
            if (canvas != null) {
                canvas.drawCircle(this.origin.x, this.origin.y, progress2, this.ripplePaint);
            }
        }
    }
}
