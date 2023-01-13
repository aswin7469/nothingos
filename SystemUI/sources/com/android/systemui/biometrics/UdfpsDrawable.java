package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b&\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\u0002\u0010\bJ\b\u0010\u001d\u001a\u00020\nH\u0016J\b\u0010\u001e\u001a\u00020\nH\u0016J\u0010\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"H\u0016J\u0010\u0010#\u001a\u00020 2\u0006\u0010$\u001a\u00020\nH\u0016J\u0012\u0010%\u001a\u00020 2\b\u0010&\u001a\u0004\u0018\u00010'H\u0016J\u0010\u0010(\u001a\u00020 2\u0006\u0010)\u001a\u00020*H\u0014R\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR$\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u0011@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R$\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u0017@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001c¨\u0006+"}, mo65043d2 = {"Lcom/android/systemui/biometrics/UdfpsDrawable;", "Landroid/graphics/drawable/Drawable;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "drawableFactory", "Lkotlin/Function1;", "Landroid/graphics/drawable/ShapeDrawable;", "(Landroid/content/Context;Lkotlin/jvm/functions/Function1;)V", "_alpha", "", "getContext", "()Landroid/content/Context;", "fingerprintDrawable", "getFingerprintDrawable", "()Landroid/graphics/drawable/ShapeDrawable;", "showing", "", "isIlluminationShowing", "()Z", "setIlluminationShowing", "(Z)V", "value", "", "strokeWidth", "getStrokeWidth", "()F", "setStrokeWidth", "(F)V", "getAlpha", "getOpacity", "onSensorRectUpdated", "", "sensorRect", "Landroid/graphics/RectF;", "setAlpha", "alpha", "setColorFilter", "colorFilter", "Landroid/graphics/ColorFilter;", "updateFingerprintIconBounds", "bounds", "Landroid/graphics/Rect;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UdfpsDrawable.kt */
public abstract class UdfpsDrawable extends Drawable {
    private int _alpha;
    private final Context context;
    private final ShapeDrawable fingerprintDrawable;
    private boolean isIlluminationShowing;
    private float strokeWidth;

    public int getOpacity() {
        return 0;
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    /* access modifiers changed from: protected */
    public final Context getContext() {
        return this.context;
    }

    public UdfpsDrawable(Context context2, Function1<? super Context, ? extends ShapeDrawable> function1) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(function1, "drawableFactory");
        this.context = context2;
        ShapeDrawable shapeDrawable = (ShapeDrawable) function1.invoke(context2);
        this.fingerprintDrawable = shapeDrawable;
        this._alpha = 255;
        this.strokeWidth = shapeDrawable.getPaint().getStrokeWidth();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public UdfpsDrawable(Context context2) {
        this(context2, UdfpsDrawableKt.access$getDefaultFactory$p());
        Intrinsics.checkNotNullParameter(context2, "context");
    }

    public final ShapeDrawable getFingerprintDrawable() {
        return this.fingerprintDrawable;
    }

    public final float getStrokeWidth() {
        return this.strokeWidth;
    }

    public final void setStrokeWidth(float f) {
        this.strokeWidth = f;
        this.fingerprintDrawable.getPaint().setStrokeWidth(f);
        invalidateSelf();
    }

    public final boolean isIlluminationShowing() {
        return this.isIlluminationShowing;
    }

    public final void setIlluminationShowing(boolean z) {
        if (this.isIlluminationShowing != z) {
            this.isIlluminationShowing = z;
            invalidateSelf();
        }
    }

    public void onSensorRectUpdated(RectF rectF) {
        Intrinsics.checkNotNullParameter(rectF, "sensorRect");
        int height = ((int) rectF.height()) / 8;
        updateFingerprintIconBounds(new Rect(((int) rectF.left) + height, ((int) rectF.top) + height, ((int) rectF.right) - height, ((int) rectF.bottom) - height));
    }

    /* access modifiers changed from: protected */
    public void updateFingerprintIconBounds(Rect rect) {
        Intrinsics.checkNotNullParameter(rect, "bounds");
        this.fingerprintDrawable.setBounds(rect);
        invalidateSelf();
    }

    public int getAlpha() {
        return this._alpha;
    }

    public void setAlpha(int i) {
        this._alpha = i;
        this.fingerprintDrawable.setAlpha(i);
        invalidateSelf();
    }
}
