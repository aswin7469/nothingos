package com.android.systemui.statusbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Trace;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;
import com.android.systemui.util.ColorUtilKt;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u0012\u0010>\u001a\u00020?2\b\u0010@\u001a\u0004\u0018\u00010AH\u0014J\u0010\u0010B\u001a\u00020?2\u0006\u0010C\u001a\u00020\nH\u0016J\b\u0010D\u001a\u00020?H\u0002J&\u0010E\u001a\u00020?2\u0006\u0010F\u001a\u00020\n2\u0006\u0010G\u001a\u00020\n2\u0006\u0010H\u001a\u00020\n2\u0006\u0010I\u001a\u00020\nJ\u0010\u0010J\u001a\u00020?2\u0006\u0010K\u001a\u00020*H\u0016J\b\u0010L\u001a\u00020?H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u00108F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0011R$\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0010@BX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0011\"\u0004\b\u0014\u0010\u0015R \u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00100\u0017X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0018\"\u0004\b\u0019\u0010\u001aR$\u0010\u001b\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\n@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\f\"\u0004\b\u001d\u0010\u000eR$\u0010\u001f\u001a\u00020\u001e2\u0006\u0010\u0012\u001a\u00020\u001e@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u001a\u0010$\u001a\u00020%X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010'\"\u0004\b(\u0010)R$\u0010+\u001a\u00020*2\u0006\u0010\u0012\u001a\u00020*@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010-\"\u0004\b.\u0010/R$\u00100\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\n@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\f\"\u0004\b2\u0010\u000eR\u001a\u00103\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\f\"\u0004\b5\u0010\u000eR\u001a\u00106\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b7\u0010\f\"\u0004\b8\u0010\u000eR\u000e\u00109\u001a\u00020:X\u0004¢\u0006\u0002\n\u0000R$\u0010;\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\n@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b<\u0010\f\"\u0004\b=\u0010\u000e¨\u0006M"}, mo65043d2 = {"Lcom/android/systemui/statusbar/LightRevealScrim;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "gradientPaint", "Landroid/graphics/Paint;", "interpolatedRevealAmount", "", "getInterpolatedRevealAmount", "()F", "setInterpolatedRevealAmount", "(F)V", "isScrimAlmostOccludes", "", "()Z", "value", "isScrimOpaque", "setScrimOpaque", "(Z)V", "isScrimOpaqueChangedListener", "Ljava/util/function/Consumer;", "()Ljava/util/function/Consumer;", "setScrimOpaqueChangedListener", "(Ljava/util/function/Consumer;)V", "revealAmount", "getRevealAmount", "setRevealAmount", "Lcom/android/systemui/statusbar/LightRevealEffect;", "revealEffect", "getRevealEffect", "()Lcom/android/systemui/statusbar/LightRevealEffect;", "setRevealEffect", "(Lcom/android/systemui/statusbar/LightRevealEffect;)V", "revealGradientCenter", "Landroid/graphics/PointF;", "getRevealGradientCenter", "()Landroid/graphics/PointF;", "setRevealGradientCenter", "(Landroid/graphics/PointF;)V", "", "revealGradientEndColor", "getRevealGradientEndColor", "()I", "setRevealGradientEndColor", "(I)V", "revealGradientEndColorAlpha", "getRevealGradientEndColorAlpha", "setRevealGradientEndColorAlpha", "revealGradientHeight", "getRevealGradientHeight", "setRevealGradientHeight", "revealGradientWidth", "getRevealGradientWidth", "setRevealGradientWidth", "shaderGradientMatrix", "Landroid/graphics/Matrix;", "startColorAlpha", "getStartColorAlpha", "setStartColorAlpha", "onDraw", "", "canvas", "Landroid/graphics/Canvas;", "setAlpha", "alpha", "setPaintColorFilter", "setRevealGradientBounds", "left", "top", "right", "bottom", "setVisibility", "visibility", "updateScrimOpaque", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LightRevealScrim.kt */
public final class LightRevealScrim extends View {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final Paint gradientPaint;
    private float interpolatedRevealAmount;
    private boolean isScrimOpaque;
    public Consumer<Boolean> isScrimOpaqueChangedListener;
    private float revealAmount;
    private LightRevealEffect revealEffect;
    private PointF revealGradientCenter;
    private int revealGradientEndColor;
    private float revealGradientEndColorAlpha;
    private float revealGradientHeight;
    private float revealGradientWidth;
    private final Matrix shaderGradientMatrix;
    private float startColorAlpha;

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

    public LightRevealScrim(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.revealAmount = 1.0f;
        this.revealEffect = LiftReveal.INSTANCE;
        this.revealGradientCenter = new PointF();
        this.revealGradientEndColor = ViewCompat.MEASURED_STATE_MASK;
        this.interpolatedRevealAmount = 1.0f;
        Paint paint = new Paint();
        paint.setShader(new RadialGradient(0.0f, 0.0f, 1.0f, new int[]{0, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        this.gradientPaint = paint;
        this.shaderGradientMatrix = new Matrix();
        this.revealEffect.setRevealAmountOnScrim(this.revealAmount, this);
        setPaintColorFilter();
        invalidate();
    }

    public final Consumer<Boolean> isScrimOpaqueChangedListener() {
        Consumer<Boolean> consumer = this.isScrimOpaqueChangedListener;
        if (consumer != null) {
            return consumer;
        }
        Intrinsics.throwUninitializedPropertyAccessException("isScrimOpaqueChangedListener");
        return null;
    }

    public final void setScrimOpaqueChangedListener(Consumer<Boolean> consumer) {
        Intrinsics.checkNotNullParameter(consumer, "<set-?>");
        this.isScrimOpaqueChangedListener = consumer;
    }

    public final float getRevealAmount() {
        return this.revealAmount;
    }

    public final void setRevealAmount(float f) {
        if (!(this.revealAmount == f)) {
            this.revealAmount = f;
            this.revealEffect.setRevealAmountOnScrim(f, this);
            updateScrimOpaque();
            Trace.traceCounter(4096, "light_reveal_amount", (int) (this.revealAmount * ((float) 100)));
            invalidate();
        }
    }

    public final LightRevealEffect getRevealEffect() {
        return this.revealEffect;
    }

    public final void setRevealEffect(LightRevealEffect lightRevealEffect) {
        Intrinsics.checkNotNullParameter(lightRevealEffect, "value");
        if (!Intrinsics.areEqual((Object) this.revealEffect, (Object) lightRevealEffect)) {
            this.revealEffect = lightRevealEffect;
            lightRevealEffect.setRevealAmountOnScrim(this.revealAmount, this);
            invalidate();
        }
    }

    public final PointF getRevealGradientCenter() {
        return this.revealGradientCenter;
    }

    public final void setRevealGradientCenter(PointF pointF) {
        Intrinsics.checkNotNullParameter(pointF, "<set-?>");
        this.revealGradientCenter = pointF;
    }

    public final float getRevealGradientWidth() {
        return this.revealGradientWidth;
    }

    public final void setRevealGradientWidth(float f) {
        this.revealGradientWidth = f;
    }

    public final float getRevealGradientHeight() {
        return this.revealGradientHeight;
    }

    public final void setRevealGradientHeight(float f) {
        this.revealGradientHeight = f;
    }

    public final float getStartColorAlpha() {
        return this.startColorAlpha;
    }

    public final void setStartColorAlpha(float f) {
        if (!(this.startColorAlpha == f)) {
            this.startColorAlpha = f;
            invalidate();
        }
    }

    public final int getRevealGradientEndColor() {
        return this.revealGradientEndColor;
    }

    public final void setRevealGradientEndColor(int i) {
        if (this.revealGradientEndColor != i) {
            this.revealGradientEndColor = i;
            setPaintColorFilter();
        }
    }

    public final float getRevealGradientEndColorAlpha() {
        return this.revealGradientEndColorAlpha;
    }

    public final void setRevealGradientEndColorAlpha(float f) {
        if (!(this.revealGradientEndColorAlpha == f)) {
            this.revealGradientEndColorAlpha = f;
            setPaintColorFilter();
        }
    }

    public final boolean isScrimOpaque() {
        return this.isScrimOpaque;
    }

    private final void setScrimOpaque(boolean z) {
        if (this.isScrimOpaque != z) {
            this.isScrimOpaque = z;
            isScrimOpaqueChangedListener().accept(Boolean.valueOf(this.isScrimOpaque));
        }
    }

    public final float getInterpolatedRevealAmount() {
        return this.interpolatedRevealAmount;
    }

    public final void setInterpolatedRevealAmount(float f) {
        this.interpolatedRevealAmount = f;
    }

    public final boolean isScrimAlmostOccludes() {
        return this.interpolatedRevealAmount < 0.1f;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0021, code lost:
        if (getVisibility() == 0) goto L_0x0025;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void updateScrimOpaque() {
        /*
            r4 = this;
            float r0 = r4.revealAmount
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x000b
            r0 = r1
            goto L_0x000c
        L_0x000b:
            r0 = r2
        L_0x000c:
            if (r0 == 0) goto L_0x0024
            float r0 = r4.getAlpha()
            r3 = 1065353216(0x3f800000, float:1.0)
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 != 0) goto L_0x001a
            r0 = r1
            goto L_0x001b
        L_0x001a:
            r0 = r2
        L_0x001b:
            if (r0 == 0) goto L_0x0024
            int r0 = r4.getVisibility()
            if (r0 != 0) goto L_0x0024
            goto L_0x0025
        L_0x0024:
            r1 = r2
        L_0x0025:
            r4.setScrimOpaque(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.LightRevealScrim.updateScrimOpaque():void");
    }

    public void setAlpha(float f) {
        super.setAlpha(f);
        updateScrimOpaque();
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        updateScrimOpaque();
    }

    public final void setRevealGradientBounds(float f, float f2, float f3, float f4) {
        float f5 = f3 - f;
        this.revealGradientWidth = f5;
        this.revealGradientHeight = f4 - f2;
        this.revealGradientCenter.x = f + (f5 / 2.0f);
        this.revealGradientCenter.y = f2 + (this.revealGradientHeight / 2.0f);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (canvas != null && this.revealGradientWidth > 0.0f && this.revealGradientHeight > 0.0f) {
            if (!(this.revealAmount == 0.0f)) {
                float f = this.startColorAlpha;
                if (f > 0.0f) {
                    canvas.drawColor(ColorUtilKt.getColorWithAlpha(this.revealGradientEndColor, f));
                }
                Matrix matrix = this.shaderGradientMatrix;
                matrix.setScale(this.revealGradientWidth, this.revealGradientHeight, 0.0f, 0.0f);
                matrix.postTranslate(this.revealGradientCenter.x, this.revealGradientCenter.y);
                this.gradientPaint.getShader().setLocalMatrix(matrix);
                canvas.drawRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), this.gradientPaint);
                return;
            }
        }
        if (this.revealAmount < 1.0f && canvas != null) {
            canvas.drawColor(this.revealGradientEndColor);
        }
    }

    private final void setPaintColorFilter() {
        this.gradientPaint.setColorFilter(new PorterDuffColorFilter(ColorUtilKt.getColorWithAlpha(this.revealGradientEndColor, this.revealGradientEndColorAlpha), PorterDuff.Mode.MULTIPLY));
    }
}
