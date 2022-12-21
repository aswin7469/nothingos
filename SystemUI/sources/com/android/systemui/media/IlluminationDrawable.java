package com.android.systemui.media;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.View;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.xmlpull.p032v1.XmlPullParser;

@Metadata(mo64986d1 = {"\u0000 \u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\u0014\u0010\u001f\u001a\u00020\u001e2\n\u0010 \u001a\u00060!R\u00020\"H\u0016J\b\u0010#\u001a\u00020$H\u0016J\u0010\u0010%\u001a\u00020\u001e2\u0006\u0010&\u001a\u00020'H\u0016J\b\u0010(\u001a\u00020\u0006H\u0016J\b\u0010)\u001a\u00020\u0006H\u0016J\u0010\u0010*\u001a\u00020\u001e2\u0006\u0010+\u001a\u00020,H\u0016J.\u0010-\u001a\u00020\u001e2\u0006\u0010.\u001a\u00020\"2\u0006\u0010/\u001a\u0002002\u0006\u00101\u001a\u0002022\f\u00103\u001a\b\u0018\u00010!R\u00020\"H\u0016J\u000e\u00104\u001a\u00020\u001e2\u0006\u00105\u001a\u000206J\u0010\u00104\u001a\u00020\u001e2\u0006\u00105\u001a\u00020\u0015H\u0002J\u0010\u00107\u001a\u00020\u001e2\u0006\u00108\u001a\u00020\u0006H\u0016J\u0012\u00109\u001a\u00020\u001e2\b\u0010:\u001a\u0004\u0018\u00010;H\u0016J\u0015\u0010<\u001a\u00020\u001e2\b\u0010\n\u001a\u0004\u0018\u00010\u000b¢\u0006\u0002\u0010=J\u0012\u0010>\u001a\u00020\u001e2\b\u0010?\u001a\u0004\u0018\u00010@H\u0016J\u0012\u0010A\u001a\u00020\u001e2\b\u0010B\u001a\u0004\u0018\u00010CH\u0016J\u0010\u0010D\u001a\u00020\u001e2\u0006\u0010E\u001a\u00020FH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\b\u0010\tR\u001c\u0010\n\u001a\u00020\u000b8FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u000bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0013\u001a\u0012\u0012\u0004\u0012\u00020\u00150\u0014j\b\u0012\u0004\u0012\u00020\u0015`\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u000e¢\u0006\u0002\n\u0000¨\u0006G"}, mo64987d2 = {"Lcom/android/systemui/media/IlluminationDrawable;", "Landroid/graphics/drawable/Drawable;", "()V", "backgroundAnimation", "Landroid/animation/ValueAnimator;", "value", "", "backgroundColor", "setBackgroundColor", "(I)V", "cornerRadius", "", "getCornerRadius", "()F", "setCornerRadius", "(F)V", "cornerRadiusOverride", "highlight", "highlightColor", "lightSources", "Ljava/util/ArrayList;", "Lcom/android/systemui/media/LightSourceDrawable;", "Lkotlin/collections/ArrayList;", "paint", "Landroid/graphics/Paint;", "themeAttrs", "", "tmpHsl", "", "animateBackground", "", "applyTheme", "t", "Landroid/content/res/Resources$Theme;", "Landroid/content/res/Resources;", "canApplyTheme", "", "draw", "canvas", "Landroid/graphics/Canvas;", "getAlpha", "getOpacity", "getOutline", "outline", "Landroid/graphics/Outline;", "inflate", "r", "parser", "Lorg/xmlpull/v1/XmlPullParser;", "attrs", "Landroid/util/AttributeSet;", "theme", "registerLightSource", "lightSource", "Landroid/view/View;", "setAlpha", "alpha", "setColorFilter", "p0", "Landroid/graphics/ColorFilter;", "setCornerRadiusOverride", "(Ljava/lang/Float;)V", "setTintList", "tint", "Landroid/content/res/ColorStateList;", "setXfermode", "mode", "Landroid/graphics/Xfermode;", "updateStateFromTypedArray", "a", "Landroid/content/res/TypedArray;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: IlluminationDrawable.kt */
public final class IlluminationDrawable extends Drawable {
    /* access modifiers changed from: private */
    public ValueAnimator backgroundAnimation;
    private int backgroundColor;
    private float cornerRadius;
    private float cornerRadiusOverride = -1.0f;
    private float highlight;
    private int highlightColor;
    private final ArrayList<LightSourceDrawable> lightSources = new ArrayList<>();
    private Paint paint = new Paint();
    private int[] themeAttrs;
    private float[] tmpHsl = {0.0f, 0.0f, 0.0f};

    public int getOpacity() {
        return -2;
    }

    public final void setCornerRadius(float f) {
        this.cornerRadius = f;
    }

    public final float getCornerRadius() {
        float f = this.cornerRadiusOverride;
        return f >= 0.0f ? f : this.cornerRadius;
    }

    private final void setBackgroundColor(int i) {
        if (i != this.backgroundColor) {
            this.backgroundColor = i;
            animateBackground();
        }
    }

    public void draw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        canvas.drawRoundRect(0.0f, 0.0f, (float) getBounds().width(), (float) getBounds().height(), getCornerRadius(), getCornerRadius(), this.paint);
    }

    public void getOutline(Outline outline) {
        Intrinsics.checkNotNullParameter(outline, "outline");
        outline.setRoundRect(getBounds(), getCornerRadius());
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
        if (typedArray.hasValue(0)) {
            this.cornerRadius = typedArray.getDimension(0, getCornerRadius());
        }
        if (typedArray.hasValue(1)) {
            this.highlight = ((float) typedArray.getInteger(1, 0)) / 100.0f;
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
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.IlluminationDrawable.canApplyTheme():boolean");
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
            for (LightSourceDrawable alpha : this.lightSources) {
                alpha.setAlpha(i);
            }
        }
    }

    public int getAlpha() {
        return this.paint.getAlpha();
    }

    public void setXfermode(Xfermode xfermode) {
        if (!Intrinsics.areEqual((Object) xfermode, (Object) this.paint.getXfermode())) {
            this.paint.setXfermode(xfermode);
            invalidateSelf();
        }
    }

    private final void animateBackground() {
        ColorUtils.colorToHSL(this.backgroundColor, this.tmpHsl);
        float[] fArr = this.tmpHsl;
        float f = fArr[2];
        float f2 = this.highlight;
        fArr[2] = MathUtils.constrain(f < 1.0f - f2 ? f + f2 : f - f2, 0.0f, 1.0f);
        int color = this.paint.getColor();
        int i = this.highlightColor;
        int HSLToColor = ColorUtils.HSLToColor(this.tmpHsl);
        ValueAnimator valueAnimator = this.backgroundAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(370);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
        ofFloat.addUpdateListener(new IlluminationDrawable$$ExternalSyntheticLambda0(this, color, i, HSLToColor));
        ofFloat.addListener(new IlluminationDrawable$animateBackground$1$2(this));
        ofFloat.start();
        this.backgroundAnimation = ofFloat;
    }

    /* access modifiers changed from: private */
    /* renamed from: animateBackground$lambda-4$lambda-3  reason: not valid java name */
    public static final void m2762animateBackground$lambda4$lambda3(IlluminationDrawable illuminationDrawable, int i, int i2, int i3, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(illuminationDrawable, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            float floatValue = ((Float) animatedValue).floatValue();
            illuminationDrawable.paint.setColor(ColorUtils.blendARGB(i, illuminationDrawable.backgroundColor, floatValue));
            illuminationDrawable.highlightColor = ColorUtils.blendARGB(i2, i3, floatValue);
            for (LightSourceDrawable highlightColor2 : illuminationDrawable.lightSources) {
                highlightColor2.setHighlightColor(illuminationDrawable.highlightColor);
            }
            illuminationDrawable.invalidateSelf();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public void setTintList(ColorStateList colorStateList) {
        super.setTintList(colorStateList);
        Intrinsics.checkNotNull(colorStateList);
        setBackgroundColor(colorStateList.getDefaultColor());
    }

    public final void registerLightSource(View view) {
        Intrinsics.checkNotNullParameter(view, "lightSource");
        if (view.getBackground() instanceof LightSourceDrawable) {
            Drawable background = view.getBackground();
            if (background != null) {
                registerLightSource((LightSourceDrawable) background);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.media.LightSourceDrawable");
        } else if (view.getForeground() instanceof LightSourceDrawable) {
            Drawable foreground = view.getForeground();
            if (foreground != null) {
                registerLightSource((LightSourceDrawable) foreground);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.media.LightSourceDrawable");
        }
    }

    private final void registerLightSource(LightSourceDrawable lightSourceDrawable) {
        lightSourceDrawable.setAlpha(this.paint.getAlpha());
        this.lightSources.add(lightSourceDrawable);
    }

    public final void setCornerRadiusOverride(Float f) {
        this.cornerRadiusOverride = f != null ? f.floatValue() : -1.0f;
    }
}
