package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.MathUtils;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.xmlpull.p032v1.XmlPullParser;

@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0017\u001a\u00020\u00182\n\u0010\u0019\u001a\u00060\u001aR\u00020\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u0004H\u0016J\u0010\u0010\u001d\u001a\u00020\u00182\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\b\u0010 \u001a\u00020!H\u0016J\b\u0010\"\u001a\u00020\bH\u0016J\u0010\u0010#\u001a\u00020\u00182\u0006\u0010$\u001a\u00020%H\u0016J\b\u0010&\u001a\u00020\u0004H\u0016J\b\u0010'\u001a\u00020\u0018H\u0002J.\u0010(\u001a\u00020\u00182\u0006\u0010)\u001a\u00020\u001b2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-2\f\u0010.\u001a\b\u0018\u00010\u001aR\u00020\u001bH\u0016J\b\u0010/\u001a\u00020\u0004H\u0016J\b\u00100\u001a\u00020\u0004H\u0016J\u0012\u00101\u001a\u00020\u00042\b\u00102\u001a\u0004\u0018\u00010\u0016H\u0014J\u0010\u00103\u001a\u00020\u00182\u0006\u00104\u001a\u00020\bH\u0016J\u0012\u00105\u001a\u00020\u00182\b\u00106\u001a\u0004\u0018\u000107H\u0016J\u0018\u00108\u001a\u00020\u00182\u0006\u00109\u001a\u00020:2\u0006\u0010;\u001a\u00020:H\u0016J\u0010\u0010<\u001a\u00020\u00182\u0006\u0010=\u001a\u00020>H\u0002R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\u0006\u0010\u0007R$\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0002\n\u0000¨\u0006?"}, mo64987d2 = {"Lcom/android/systemui/media/LightSourceDrawable;", "Landroid/graphics/drawable/Drawable;", "()V", "value", "", "active", "setActive", "(Z)V", "", "highlightColor", "getHighlightColor", "()I", "setHighlightColor", "(I)V", "paint", "Landroid/graphics/Paint;", "pressed", "rippleAnimation", "Landroid/animation/Animator;", "rippleData", "Lcom/android/systemui/media/RippleData;", "themeAttrs", "", "applyTheme", "", "t", "Landroid/content/res/Resources$Theme;", "Landroid/content/res/Resources;", "canApplyTheme", "draw", "canvas", "Landroid/graphics/Canvas;", "getDirtyBounds", "Landroid/graphics/Rect;", "getOpacity", "getOutline", "outline", "Landroid/graphics/Outline;", "hasFocusStateSpecified", "illuminate", "inflate", "r", "parser", "Lorg/xmlpull/v1/XmlPullParser;", "attrs", "Landroid/util/AttributeSet;", "theme", "isProjected", "isStateful", "onStateChange", "stateSet", "setAlpha", "alpha", "setColorFilter", "p0", "Landroid/graphics/ColorFilter;", "setHotspot", "x", "", "y", "updateStateFromTypedArray", "a", "Landroid/content/res/TypedArray;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LightSourceDrawable.kt */
public final class LightSourceDrawable extends Drawable {
    private boolean active;
    private int highlightColor = -1;
    private Paint paint = new Paint();
    private boolean pressed;
    /* access modifiers changed from: private */
    public Animator rippleAnimation;
    /* access modifiers changed from: private */
    public final RippleData rippleData = new RippleData(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private int[] themeAttrs;

    public int getOpacity() {
        return -2;
    }

    public void getOutline(Outline outline) {
        Intrinsics.checkNotNullParameter(outline, "outline");
    }

    public boolean hasFocusStateSpecified() {
        return true;
    }

    public boolean isProjected() {
        return true;
    }

    public boolean isStateful() {
        return true;
    }

    public final int getHighlightColor() {
        return this.highlightColor;
    }

    public final void setHighlightColor(int i) {
        if (this.highlightColor != i) {
            this.highlightColor = i;
            invalidateSelf();
        }
    }

    private final void setActive(boolean z) {
        if (z != this.active) {
            this.active = z;
            if (z) {
                Animator animator = this.rippleAnimation;
                if (animator != null) {
                    animator.cancel();
                }
                this.rippleData.setAlpha(1.0f);
                this.rippleData.setProgress(0.05f);
            } else {
                Animator animator2 = this.rippleAnimation;
                if (animator2 != null) {
                    animator2.cancel();
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.rippleData.getAlpha(), 0.0f});
                ofFloat.setDuration(200);
                ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
                ofFloat.addUpdateListener(new LightSourceDrawable$$ExternalSyntheticLambda2(this));
                ofFloat.addListener(new LightSourceDrawable$active$1$2(this));
                ofFloat.start();
                this.rippleAnimation = ofFloat;
            }
            invalidateSelf();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: _set_active_$lambda-1$lambda-0  reason: not valid java name */
    public static final void m2765_set_active_$lambda1$lambda0(LightSourceDrawable lightSourceDrawable, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(lightSourceDrawable, "this$0");
        RippleData rippleData2 = lightSourceDrawable.rippleData;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            rippleData2.setAlpha(((Float) animatedValue).floatValue());
            lightSourceDrawable.invalidateSelf();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public void draw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        float lerp = MathUtils.lerp(this.rippleData.getMinSize(), this.rippleData.getMaxSize(), this.rippleData.getProgress());
        int alphaComponent = ColorUtils.setAlphaComponent(this.highlightColor, (int) (this.rippleData.getAlpha() * ((float) 255)));
        float f = lerp;
        this.paint.setShader(new RadialGradient(this.rippleData.getX(), this.rippleData.getY(), f, new int[]{alphaComponent, 0}, LightSourceDrawableKt.GRADIENT_STOPS, Shader.TileMode.CLAMP));
        canvas.drawCircle(this.rippleData.getX(), this.rippleData.getY(), lerp, this.paint);
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        Intrinsics.checkNotNullParameter(resources, "r");
        Intrinsics.checkNotNullParameter(xmlPullParser, "parser");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
        TypedArray obtainAttributes = Drawable.obtainAttributes(resources, theme, attributeSet, C1893R.styleable.IlluminationDrawable);
        this.themeAttrs = obtainAttributes.extractThemeAttrs();
        Intrinsics.checkNotNullExpressionValue(obtainAttributes, "a");
        updateStateFromTypedArray(obtainAttributes);
        obtainAttributes.recycle();
    }

    private final void updateStateFromTypedArray(TypedArray typedArray) {
        if (typedArray.hasValue(3)) {
            this.rippleData.setMinSize(typedArray.getDimension(3, 0.0f));
        }
        if (typedArray.hasValue(2)) {
            this.rippleData.setMaxSize(typedArray.getDimension(2, 0.0f));
        }
        if (typedArray.hasValue(1)) {
            this.rippleData.setHighlight(((float) typedArray.getInteger(1, 0)) / 100.0f);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0008, code lost:
        if (r0.length <= 0) goto L_0x000a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean canApplyTheme() {
        /*
            r1 = this;
            int[] r0 = r1.themeAttrs
            if (r0 == 0) goto L_0x000a
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            int r0 = r0.length
            if (r0 > 0) goto L_0x0010
        L_0x000a:
            boolean r1 = super.canApplyTheme()
            if (r1 == 0) goto L_0x0012
        L_0x0010:
            r1 = 1
            goto L_0x0013
        L_0x0012:
            r1 = 0
        L_0x0013:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.LightSourceDrawable.canApplyTheme():boolean");
    }

    public void applyTheme(Resources.Theme theme) {
        Intrinsics.checkNotNullParameter(theme, "t");
        super.applyTheme(theme);
        int[] iArr = this.themeAttrs;
        if (iArr != null) {
            TypedArray resolveAttributes = theme.resolveAttributes(iArr, C1893R.styleable.IlluminationDrawable);
            Intrinsics.checkNotNullExpressionValue(resolveAttributes, "a");
            updateStateFromTypedArray(resolveAttributes);
            resolveAttributes.recycle();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        throw new UnsupportedOperationException("Color filters are not supported");
    }

    public void setAlpha(int i) {
        if (i != this.paint.getAlpha()) {
            this.paint.setAlpha(i);
            invalidateSelf();
        }
    }

    private final void illuminate() {
        this.rippleData.setAlpha(1.0f);
        invalidateSelf();
        Animator animator = this.rippleAnimation;
        if (animator != null) {
            animator.cancel();
        }
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        ofFloat.setStartDelay(133);
        ofFloat.setDuration(800 - ofFloat.getStartDelay());
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ofFloat.addUpdateListener(new LightSourceDrawable$$ExternalSyntheticLambda0(this));
        Unit unit = Unit.INSTANCE;
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{this.rippleData.getProgress(), 1.0f});
        ofFloat2.setDuration(800);
        ofFloat2.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ofFloat2.addUpdateListener(new LightSourceDrawable$$ExternalSyntheticLambda1(this));
        Unit unit2 = Unit.INSTANCE;
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        animatorSet.addListener(new LightSourceDrawable$illuminate$1$3(this));
        animatorSet.start();
        this.rippleAnimation = animatorSet;
    }

    /* access modifiers changed from: private */
    /* renamed from: illuminate$lambda-7$lambda-4$lambda-3  reason: not valid java name */
    public static final void m2766illuminate$lambda7$lambda4$lambda3(LightSourceDrawable lightSourceDrawable, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(lightSourceDrawable, "this$0");
        RippleData rippleData2 = lightSourceDrawable.rippleData;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            rippleData2.setAlpha(((Float) animatedValue).floatValue());
            lightSourceDrawable.invalidateSelf();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    /* access modifiers changed from: private */
    /* renamed from: illuminate$lambda-7$lambda-6$lambda-5  reason: not valid java name */
    public static final void m2767illuminate$lambda7$lambda6$lambda5(LightSourceDrawable lightSourceDrawable, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(lightSourceDrawable, "this$0");
        RippleData rippleData2 = lightSourceDrawable.rippleData;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            rippleData2.setProgress(((Float) animatedValue).floatValue());
            lightSourceDrawable.invalidateSelf();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public void setHotspot(float f, float f2) {
        this.rippleData.setX(f);
        this.rippleData.setY(f2);
        if (this.active) {
            invalidateSelf();
        }
    }

    public Rect getDirtyBounds() {
        float lerp = MathUtils.lerp(this.rippleData.getMinSize(), this.rippleData.getMaxSize(), this.rippleData.getProgress());
        Rect rect = new Rect((int) (this.rippleData.getX() - lerp), (int) (this.rippleData.getY() - lerp), (int) (this.rippleData.getX() + lerp), (int) (this.rippleData.getY() + lerp));
        rect.union(super.getDirtyBounds());
        return rect;
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] iArr) {
        boolean onStateChange = super.onStateChange(iArr);
        if (iArr == null) {
            return onStateChange;
        }
        boolean z = this.pressed;
        boolean z2 = false;
        this.pressed = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        for (int i : iArr) {
            switch (i) {
                case 16842908:
                    z4 = true;
                    break;
                case 16842910:
                    z3 = true;
                    break;
                case 16842919:
                    this.pressed = true;
                    break;
                case 16843623:
                    z5 = true;
                    break;
            }
        }
        if (z3 && (this.pressed || z4 || z5)) {
            z2 = true;
        }
        setActive(z2);
        if (z && !this.pressed) {
            illuminate();
        }
        return onStateChange;
    }
}
