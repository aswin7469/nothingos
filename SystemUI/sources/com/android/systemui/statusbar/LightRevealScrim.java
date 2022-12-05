package com.android.systemui.statusbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: LightRevealScrim.kt */
/* loaded from: classes.dex */
public final class LightRevealScrim extends View {
    @NotNull
    private final Paint gradientPaint;
    private boolean isScrimOpaque;
    public Consumer<Boolean> isScrimOpaqueChangedListener;
    private float revealGradientEndColorAlpha;
    private float revealGradientHeight;
    private float revealGradientWidth;
    private float revealAmount = 1.0f;
    @NotNull
    private LightRevealEffect revealEffect = LiftReveal.INSTANCE;
    @NotNull
    private PointF revealGradientCenter = new PointF();
    private int revealGradientEndColor = -16777216;
    @NotNull
    private final Matrix shaderGradientMatrix = new Matrix();

    public LightRevealScrim(@Nullable Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        paint.setShader(new RadialGradient(0.0f, 0.0f, 1.0f, new int[]{0, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        Unit unit = Unit.INSTANCE;
        this.gradientPaint = paint;
        this.revealEffect.setRevealAmountOnScrim(this.revealAmount, this);
        setPaintColorFilter();
        invalidate();
    }

    @NotNull
    public final Consumer<Boolean> isScrimOpaqueChangedListener() {
        Consumer<Boolean> consumer = this.isScrimOpaqueChangedListener;
        if (consumer != null) {
            return consumer;
        }
        Intrinsics.throwUninitializedPropertyAccessException("isScrimOpaqueChangedListener");
        throw null;
    }

    public final void setScrimOpaqueChangedListener(@NotNull Consumer<Boolean> consumer) {
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
            invalidate();
        }
    }

    @NotNull
    public final LightRevealEffect getRevealEffect() {
        return this.revealEffect;
    }

    public final void setRevealEffect(@NotNull LightRevealEffect value) {
        Intrinsics.checkNotNullParameter(value, "value");
        if (!Intrinsics.areEqual(this.revealEffect, value)) {
            this.revealEffect = value;
            value.setRevealAmountOnScrim(this.revealAmount, this);
            invalidate();
        }
    }

    @NotNull
    public final PointF getRevealGradientCenter() {
        return this.revealGradientCenter;
    }

    public final float getRevealGradientWidth() {
        return this.revealGradientWidth;
    }

    public final float getRevealGradientHeight() {
        return this.revealGradientHeight;
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

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0021, code lost:
        if (getVisibility() == 0) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void updateScrimOpaque() {
        boolean z = true;
        if (this.revealAmount == 0.0f) {
            if (getAlpha() == 1.0f) {
            }
        }
        z = false;
        setScrimOpaque(z);
    }

    @Override // android.view.View
    public void setAlpha(float f) {
        super.setAlpha(f);
        updateScrimOpaque();
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        updateScrimOpaque();
    }

    public final void setRevealGradientBounds(float f, float f2, float f3, float f4) {
        float f5 = f3 - f;
        this.revealGradientWidth = f5;
        float f6 = f4 - f2;
        this.revealGradientHeight = f6;
        PointF pointF = this.revealGradientCenter;
        pointF.x = f + (f5 / 2.0f);
        pointF.y = f2 + (f6 / 2.0f);
    }

    @Override // android.view.View
    protected void onDraw(@Nullable Canvas canvas) {
        if (canvas == null || this.revealGradientWidth <= 0.0f || this.revealGradientHeight <= 0.0f) {
            if (this.revealAmount >= 1.0f || canvas == null) {
                return;
            }
            canvas.drawColor(this.revealGradientEndColor);
            return;
        }
        Matrix matrix = this.shaderGradientMatrix;
        matrix.setScale(getRevealGradientWidth(), getRevealGradientHeight(), 0.0f, 0.0f);
        matrix.postTranslate(getRevealGradientCenter().x, getRevealGradientCenter().y);
        this.gradientPaint.getShader().setLocalMatrix(matrix);
        canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), this.gradientPaint);
    }

    private final void setPaintColorFilter() {
        this.gradientPaint.setColorFilter(new PorterDuffColorFilter(Color.argb((int) (this.revealGradientEndColorAlpha * 255), Color.red(this.revealGradientEndColor), Color.green(this.revealGradientEndColor), Color.blue(this.revealGradientEndColor)), PorterDuff.Mode.MULTIPLY));
    }
}
