package com.android.systemui.statusbar;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J.\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003R\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\u0004\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\t\"\u0004\b\r\u0010\u000bR\u001a\u0010\u0006\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\t\"\u0004\b\u000f\u0010\u000bR\u001a\u0010\u0005\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\t\"\u0004\b\u0011\u0010\u000b¨\u0006\u0018"}, mo65043d2 = {"Lcom/android/systemui/statusbar/CircleReveal;", "Lcom/android/systemui/statusbar/LightRevealEffect;", "centerX", "", "centerY", "startRadius", "endRadius", "(FFFF)V", "getCenterX", "()F", "setCenterX", "(F)V", "getCenterY", "setCenterY", "getEndRadius", "setEndRadius", "getStartRadius", "setStartRadius", "setRevealAmountOnScrim", "", "amount", "scrim", "Lcom/android/systemui/statusbar/LightRevealScrim;", "updateCircleReveal", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LightRevealScrim.kt */
public final class CircleReveal implements LightRevealEffect {
    private float centerX;
    private float centerY;
    private float endRadius;
    private float startRadius;

    public CircleReveal(float f, float f2, float f3, float f4) {
        this.centerX = f;
        this.centerY = f2;
        this.startRadius = f3;
        this.endRadius = f4;
    }

    public final float getCenterX() {
        return this.centerX;
    }

    public final void setCenterX(float f) {
        this.centerX = f;
    }

    public final float getCenterY() {
        return this.centerY;
    }

    public final void setCenterY(float f) {
        this.centerY = f;
    }

    public final float getStartRadius() {
        return this.startRadius;
    }

    public final void setStartRadius(float f) {
        this.startRadius = f;
    }

    public final float getEndRadius() {
        return this.endRadius;
    }

    public final void setEndRadius(float f) {
        this.endRadius = f;
    }

    public void setRevealAmountOnScrim(float f, LightRevealScrim lightRevealScrim) {
        Intrinsics.checkNotNullParameter(lightRevealScrim, "scrim");
        float percentPastThreshold = LightRevealEffect.Companion.getPercentPastThreshold(f, 0.5f);
        float f2 = this.startRadius;
        float f3 = f2 + ((this.endRadius - f2) * f);
        lightRevealScrim.setInterpolatedRevealAmount(f);
        lightRevealScrim.setRevealGradientEndColorAlpha(1.0f - percentPastThreshold);
        float f4 = this.centerX;
        float f5 = this.centerY;
        lightRevealScrim.setRevealGradientBounds(f4 - f3, f5 - f3, f4 + f3, f5 + f3);
    }

    public final void updateCircleReveal(LightRevealScrim lightRevealScrim, float f, float f2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(lightRevealScrim, "scrim");
        this.centerX = f;
        this.centerY = f2;
        this.startRadius = f3;
        this.endRadius = f4;
        float interpolatedRevealAmount = f3 + ((f4 - f3) * lightRevealScrim.getInterpolatedRevealAmount());
        float f5 = this.centerX;
        float f6 = this.centerY;
        lightRevealScrim.setRevealGradientBounds(f5 - interpolatedRevealAmount, f6 - interpolatedRevealAmount, f5 + interpolatedRevealAmount, f6 + interpolatedRevealAmount);
    }
}
