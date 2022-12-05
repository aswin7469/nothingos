package com.android.systemui.util.animation;

import android.graphics.PointF;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: TransitionLayoutController.kt */
/* loaded from: classes2.dex */
public final class DisappearParameters {
    private float disappearStart;
    @NotNull
    private PointF gonePivot = new PointF(0.0f, 1.0f);
    @NotNull
    private PointF disappearSize = new PointF(1.0f, 0.0f);
    @NotNull
    private PointF contentTranslationFraction = new PointF(0.0f, 0.8f);
    private float disappearEnd = 1.0f;
    private float fadeStartPosition = 0.9f;

    @NotNull
    public final PointF getGonePivot() {
        return this.gonePivot;
    }

    @NotNull
    public final PointF getDisappearSize() {
        return this.disappearSize;
    }

    @NotNull
    public final PointF getContentTranslationFraction() {
        return this.contentTranslationFraction;
    }

    public final float getDisappearStart() {
        return this.disappearStart;
    }

    public final void setDisappearStart(float f) {
        this.disappearStart = f;
    }

    public final float getDisappearEnd() {
        return this.disappearEnd;
    }

    public final void setDisappearEnd(float f) {
        this.disappearEnd = f;
    }

    public final float getFadeStartPosition() {
        return this.fadeStartPosition;
    }

    public final void setFadeStartPosition(float f) {
        this.fadeStartPosition = f;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof DisappearParameters)) {
            return false;
        }
        DisappearParameters disappearParameters = (DisappearParameters) obj;
        if (!this.disappearSize.equals(disappearParameters.disappearSize) || !this.gonePivot.equals(disappearParameters.gonePivot) || !this.contentTranslationFraction.equals(disappearParameters.contentTranslationFraction)) {
            return false;
        }
        if (!(this.disappearStart == disappearParameters.disappearStart)) {
            return false;
        }
        if (!(this.disappearEnd == disappearParameters.disappearEnd)) {
            return false;
        }
        return (this.fadeStartPosition > disappearParameters.fadeStartPosition ? 1 : (this.fadeStartPosition == disappearParameters.fadeStartPosition ? 0 : -1)) == 0;
    }

    public int hashCode() {
        return (((((((((this.disappearSize.hashCode() * 31) + this.gonePivot.hashCode()) * 31) + this.contentTranslationFraction.hashCode()) * 31) + Float.hashCode(this.disappearStart)) * 31) + Float.hashCode(this.disappearEnd)) * 31) + Float.hashCode(this.fadeStartPosition);
    }

    @NotNull
    public final DisappearParameters deepCopy() {
        DisappearParameters disappearParameters = new DisappearParameters();
        disappearParameters.disappearSize.set(this.disappearSize);
        disappearParameters.gonePivot.set(this.gonePivot);
        disappearParameters.contentTranslationFraction.set(this.contentTranslationFraction);
        disappearParameters.disappearStart = this.disappearStart;
        disappearParameters.disappearEnd = this.disappearEnd;
        disappearParameters.fadeStartPosition = this.fadeStartPosition;
        return disappearParameters;
    }
}
