package com.android.systemui.statusbar.charging;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\b\u0010&\u001a\u00020'H\u0014J\u0012\u0010(\u001a\u00020'2\b\u0010)\u001a\u0004\u0018\u00010*H\u0014J\u0012\u0010+\u001a\u00020'2\b\u0010,\u001a\u0004\u0018\u00010-H\u0014J\u000e\u0010.\u001a\u00020'2\u0006\u0010/\u001a\u00020\bJ\u0014\u00100\u001a\u00020'2\n\b\u0002\u00101\u001a\u0004\u0018\u000102H\u0007R\u000e\u0010\u0007\u001a\u00020\bXD¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR$\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R$\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u000f\u001a\u00020\u0016@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001a\u0010\u001c\u001a\u00020\u001dX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u000e\u0010\"\u001a\u00020#X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0004¢\u0006\u0002\n\u0000¨\u00063"}, mo65043d2 = {"Lcom/android/systemui/statusbar/charging/ChargingRippleView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defaultColor", "", "duration", "", "getDuration", "()J", "setDuration", "(J)V", "value", "Landroid/graphics/PointF;", "origin", "getOrigin", "()Landroid/graphics/PointF;", "setOrigin", "(Landroid/graphics/PointF;)V", "", "radius", "getRadius", "()F", "setRadius", "(F)V", "rippleInProgress", "", "getRippleInProgress", "()Z", "setRippleInProgress", "(Z)V", "ripplePaint", "Landroid/graphics/Paint;", "rippleShader", "Lcom/android/systemui/statusbar/charging/RippleShader;", "onAttachedToWindow", "", "onConfigurationChanged", "newConfig", "Landroid/content/res/Configuration;", "onDraw", "canvas", "Landroid/graphics/Canvas;", "setColor", "color", "startRipple", "onAnimationEnd", "Ljava/lang/Runnable;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ChargingRippleView.kt */
public final class ChargingRippleView extends View {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final int defaultColor;
    private long duration;
    private PointF origin;
    private float radius;
    private boolean rippleInProgress;
    private final Paint ripplePaint;
    private final RippleShader rippleShader;

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

    public final void startRipple() {
        startRipple$default(this, (Runnable) null, 1, (Object) null);
    }

    public ChargingRippleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        RippleShader rippleShader2 = new RippleShader();
        this.rippleShader = rippleShader2;
        this.defaultColor = -1;
        Paint paint = new Paint();
        this.ripplePaint = paint;
        this.origin = new PointF();
        this.duration = 1750;
        rippleShader2.setColor(-1);
        rippleShader2.setProgress(0.0f);
        rippleShader2.setSparkleStrength(0.3f);
        paint.setShader((Shader) rippleShader2);
    }

    public final boolean getRippleInProgress() {
        return this.rippleInProgress;
    }

    public final void setRippleInProgress(boolean z) {
        this.rippleInProgress = z;
    }

    public final float getRadius() {
        return this.radius;
    }

    public final void setRadius(float f) {
        this.rippleShader.setRadius(f);
        this.radius = f;
    }

    public final PointF getOrigin() {
        return this.origin;
    }

    public final void setOrigin(PointF pointF) {
        Intrinsics.checkNotNullParameter(pointF, "value");
        this.rippleShader.setOrigin(pointF);
        this.origin = pointF;
    }

    public final long getDuration() {
        return this.duration;
    }

    public final void setDuration(long j) {
        this.duration = j;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        this.rippleShader.setPixelDensity(getResources().getDisplayMetrics().density);
        super.onConfigurationChanged(configuration);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        this.rippleShader.setPixelDensity(getResources().getDisplayMetrics().density);
        super.onAttachedToWindow();
    }

    public static /* synthetic */ void startRipple$default(ChargingRippleView chargingRippleView, Runnable runnable, int i, Object obj) {
        if ((i & 1) != 0) {
            runnable = null;
        }
        chargingRippleView.startRipple(runnable);
    }

    public final void startRipple(Runnable runnable) {
        if (!this.rippleInProgress) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            ofFloat.setDuration(this.duration);
            ofFloat.addUpdateListener(new ChargingRippleView$$ExternalSyntheticLambda0(this));
            ofFloat.addListener(new ChargingRippleView$startRipple$2(this, runnable));
            ofFloat.start();
            this.rippleInProgress = true;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: startRipple$lambda-0  reason: not valid java name */
    public static final void m3052startRipple$lambda0(ChargingRippleView chargingRippleView, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(chargingRippleView, "this$0");
        long currentPlayTime = valueAnimator.getCurrentPlayTime();
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            float floatValue = ((Float) animatedValue).floatValue();
            chargingRippleView.rippleShader.setProgress(floatValue);
            chargingRippleView.rippleShader.setDistortionStrength(((float) 1) - floatValue);
            chargingRippleView.rippleShader.setTime((float) currentPlayTime);
            chargingRippleView.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public final void setColor(int i) {
        this.rippleShader.setColor(i);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (canvas != null && canvas.isHardwareAccelerated()) {
            float f = (float) 1;
            canvas.drawCircle(this.origin.x, this.origin.y, (f - (((f - this.rippleShader.getProgress()) * (f - this.rippleShader.getProgress())) * (f - this.rippleShader.getProgress()))) * this.radius * ((float) 2), this.ripplePaint);
        }
    }
}
