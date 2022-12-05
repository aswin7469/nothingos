package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import androidx.annotation.Keep;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.R$styleable;
import com.android.systemui.animation.Interpolators;
import java.util.ArrayList;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xmlpull.v1.XmlPullParser;
/* compiled from: IlluminationDrawable.kt */
@Keep
/* loaded from: classes.dex */
public final class IlluminationDrawable extends Drawable {
    @Nullable
    private ValueAnimator backgroundAnimation;
    private int backgroundColor;
    private float cornerRadius;
    private float highlight;
    private int highlightColor;
    @Nullable
    private int[] themeAttrs;
    private float cornerRadiusOverride = -1.0f;
    @NotNull
    private float[] tmpHsl = {0.0f, 0.0f, 0.0f};
    @NotNull
    private Paint paint = new Paint();
    @NotNull
    private final ArrayList<LightSourceDrawable> lightSources = new ArrayList<>();

    @Override // android.graphics.drawable.Drawable
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
        if (i == this.backgroundColor) {
            return;
        }
        this.backgroundColor = i;
        animateBackground();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(@NotNull Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        canvas.drawRoundRect(0.0f, 0.0f, getBounds().width(), getBounds().height(), getCornerRadius(), getCornerRadius(), this.paint);
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(@NotNull Outline outline) {
        Intrinsics.checkNotNullParameter(outline, "outline");
        outline.setRoundRect(getBounds(), getCornerRadius());
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(@NotNull Resources r, @NotNull XmlPullParser parser, @NotNull AttributeSet attrs, @Nullable Resources.Theme theme) {
        Intrinsics.checkNotNullParameter(r, "r");
        Intrinsics.checkNotNullParameter(parser, "parser");
        Intrinsics.checkNotNullParameter(attrs, "attrs");
        TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R$styleable.IlluminationDrawable);
        this.themeAttrs = a.extractThemeAttrs();
        Intrinsics.checkNotNullExpressionValue(a, "a");
        updateStateFromTypedArray(a);
        a.recycle();
    }

    private final void updateStateFromTypedArray(TypedArray typedArray) {
        int i = R$styleable.IlluminationDrawable_cornerRadius;
        if (typedArray.hasValue(i)) {
            this.cornerRadius = typedArray.getDimension(i, getCornerRadius());
        }
        int i2 = R$styleable.IlluminationDrawable_highlight;
        if (typedArray.hasValue(i2)) {
            this.highlight = typedArray.getInteger(i2, 0) / 100.0f;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x0008, code lost:
        if (r0.length <= 0) goto L7;
     */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean canApplyTheme() {
        int[] iArr = this.themeAttrs;
        if (iArr != null) {
            Intrinsics.checkNotNull(iArr);
        }
        return super.canApplyTheme();
    }

    @Override // android.graphics.drawable.Drawable
    public void applyTheme(@NotNull Resources.Theme t) {
        Intrinsics.checkNotNullParameter(t, "t");
        super.applyTheme(t);
        int[] iArr = this.themeAttrs;
        if (iArr == null) {
            return;
        }
        TypedArray a = t.resolveAttributes(iArr, R$styleable.IlluminationDrawable);
        Intrinsics.checkNotNullExpressionValue(a, "a");
        updateStateFromTypedArray(a);
        a.recycle();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        throw new UnsupportedOperationException("Color filters are not supported");
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        if (i == this.paint.getAlpha()) {
            return;
        }
        this.paint.setAlpha(i);
        invalidateSelf();
        for (LightSourceDrawable lightSourceDrawable : this.lightSources) {
            lightSourceDrawable.setAlpha(i);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.paint.getAlpha();
    }

    public void setXfermode(@Nullable Xfermode xfermode) {
        if (Intrinsics.areEqual(xfermode, this.paint.getXfermode())) {
            return;
        }
        this.paint.setXfermode(xfermode);
        invalidateSelf();
    }

    private final void animateBackground() {
        ColorUtils.colorToHSL(this.backgroundColor, this.tmpHsl);
        float[] fArr = this.tmpHsl;
        float f = fArr[2];
        float f2 = this.highlight;
        fArr[2] = MathUtils.constrain(f < 1.0f - f2 ? f + f2 : f - f2, 0.0f, 1.0f);
        final int color = this.paint.getColor();
        final int i = this.highlightColor;
        final int HSLToColor = ColorUtils.HSLToColor(this.tmpHsl);
        ValueAnimator valueAnimator = this.backgroundAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(370L);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.media.IlluminationDrawable$animateBackground$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                Paint paint;
                int i2;
                ArrayList<LightSourceDrawable> arrayList;
                int i3;
                Object animatedValue = valueAnimator2.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                float floatValue = ((Float) animatedValue).floatValue();
                paint = IlluminationDrawable.this.paint;
                int i4 = color;
                i2 = IlluminationDrawable.this.backgroundColor;
                paint.setColor(ColorUtils.blendARGB(i4, i2, floatValue));
                IlluminationDrawable.this.highlightColor = ColorUtils.blendARGB(i, HSLToColor, floatValue);
                arrayList = IlluminationDrawable.this.lightSources;
                IlluminationDrawable illuminationDrawable = IlluminationDrawable.this;
                for (LightSourceDrawable lightSourceDrawable : arrayList) {
                    i3 = illuminationDrawable.highlightColor;
                    lightSourceDrawable.setHighlightColor(i3);
                }
                IlluminationDrawable.this.invalidateSelf();
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.media.IlluminationDrawable$animateBackground$1$2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@Nullable Animator animator) {
                IlluminationDrawable.this.backgroundAnimation = null;
            }
        });
        ofFloat.start();
        Unit unit = Unit.INSTANCE;
        this.backgroundAnimation = ofFloat;
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(@Nullable ColorStateList colorStateList) {
        super.setTintList(colorStateList);
        Intrinsics.checkNotNull(colorStateList);
        setBackgroundColor(colorStateList.getDefaultColor());
    }

    public final void registerLightSource(@NotNull View lightSource) {
        Intrinsics.checkNotNullParameter(lightSource, "lightSource");
        if (lightSource.getBackground() instanceof LightSourceDrawable) {
            Drawable background = lightSource.getBackground();
            Objects.requireNonNull(background, "null cannot be cast to non-null type com.android.systemui.media.LightSourceDrawable");
            registerLightSource((LightSourceDrawable) background);
        } else if (!(lightSource.getForeground() instanceof LightSourceDrawable)) {
        } else {
            Drawable foreground = lightSource.getForeground();
            Objects.requireNonNull(foreground, "null cannot be cast to non-null type com.android.systemui.media.LightSourceDrawable");
            registerLightSource((LightSourceDrawable) foreground);
        }
    }

    private final void registerLightSource(LightSourceDrawable lightSourceDrawable) {
        lightSourceDrawable.setAlpha(this.paint.getAlpha());
        this.lightSources.add(lightSourceDrawable);
    }

    public final void setCornerRadiusOverride(@Nullable Float f) {
        this.cornerRadiusOverride = f == null ? -1.0f : f.floatValue();
    }
}
