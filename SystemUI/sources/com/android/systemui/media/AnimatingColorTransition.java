package com.android.systemui.media;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import com.android.systemui.monet.ColorScheme;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0016\u0018\u00002\u00020\u00012\u00020\u0002B5\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00040\u0006\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\t0\u0006¢\u0006\u0002\u0010\nJ\b\u0010\u001a\u001a\u00020\u0019H\u0017J\u0010\u0010\u001b\u001a\u00020\t2\u0006\u0010\u001c\u001a\u00020\u0019H\u0016J\u0012\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0007H\u0016R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\t0\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00040\u0006X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u000f\"\u0004\b\u0014\u0010\u0011R\u001a\u0010\u0015\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000f\"\u0004\b\u0017\u0010\u0011R\u000e\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0002\n\u0000¨\u0006 "}, mo65043d2 = {"Lcom/android/systemui/media/AnimatingColorTransition;", "Landroid/animation/ValueAnimator$AnimatorUpdateListener;", "Lcom/android/systemui/media/ColorTransition;", "defaultColor", "", "extractColor", "Lkotlin/Function1;", "Lcom/android/systemui/monet/ColorScheme;", "applyColor", "", "(ILkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "argbEvaluator", "Landroid/animation/ArgbEvaluator;", "currentColor", "getCurrentColor", "()I", "setCurrentColor", "(I)V", "sourceColor", "getSourceColor", "setSourceColor", "targetColor", "getTargetColor", "setTargetColor", "valueAnimator", "Landroid/animation/ValueAnimator;", "buildAnimator", "onAnimationUpdate", "animation", "updateColorScheme", "", "scheme", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ColorSchemeTransition.kt */
public class AnimatingColorTransition implements ValueAnimator.AnimatorUpdateListener, ColorTransition {
    private final Function1<Integer, Unit> applyColor;
    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private int currentColor;
    private final int defaultColor;
    private final Function1<ColorScheme, Integer> extractColor;
    private int sourceColor;
    private int targetColor;
    private final ValueAnimator valueAnimator = buildAnimator();

    public AnimatingColorTransition(int i, Function1<? super ColorScheme, Integer> function1, Function1<? super Integer, Unit> function12) {
        Intrinsics.checkNotNullParameter(function1, "extractColor");
        Intrinsics.checkNotNullParameter(function12, "applyColor");
        this.defaultColor = i;
        this.extractColor = function1;
        this.applyColor = function12;
        this.sourceColor = i;
        this.currentColor = i;
        this.targetColor = i;
        function12.invoke(Integer.valueOf(i));
    }

    public final int getSourceColor() {
        return this.sourceColor;
    }

    public final void setSourceColor(int i) {
        this.sourceColor = i;
    }

    public final int getCurrentColor() {
        return this.currentColor;
    }

    public final void setCurrentColor(int i) {
        this.currentColor = i;
    }

    public final int getTargetColor() {
        return this.targetColor;
    }

    public final void setTargetColor(int i) {
        this.targetColor = i;
    }

    public void onAnimationUpdate(ValueAnimator valueAnimator2) {
        Intrinsics.checkNotNullParameter(valueAnimator2, "animation");
        Object evaluate = this.argbEvaluator.evaluate(valueAnimator2.getAnimatedFraction(), Integer.valueOf(this.sourceColor), Integer.valueOf(this.targetColor));
        if (evaluate != null) {
            int intValue = ((Integer) evaluate).intValue();
            this.currentColor = intValue;
            this.applyColor.invoke(Integer.valueOf(intValue));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    public boolean updateColorScheme(ColorScheme colorScheme) {
        int intValue = colorScheme == null ? this.defaultColor : this.extractColor.invoke(colorScheme).intValue();
        if (intValue == this.targetColor) {
            return false;
        }
        this.sourceColor = this.currentColor;
        this.targetColor = intValue;
        this.valueAnimator.cancel();
        this.valueAnimator.start();
        return true;
    }

    public ValueAnimator buildAnimator() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(333);
        ofFloat.addUpdateListener(this);
        Intrinsics.checkNotNullExpressionValue(ofFloat, "animator");
        return ofFloat;
    }
}
