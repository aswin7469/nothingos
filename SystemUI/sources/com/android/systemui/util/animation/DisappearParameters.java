package com.android.systemui.util.animation;

import android.graphics.PointF;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u001b\u001a\u00020\u0000J\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001H\u0002J\b\u0010\u001f\u001a\u00020 H\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR\u001a\u0010\u0012\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\f\"\u0004\b\u0014\u0010\u000eR\u001a\u0010\u0015\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\f\"\u0004\b\u0017\u0010\u000eR\u001a\u0010\u0018\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0006\"\u0004\b\u001a\u0010\b¨\u0006!"}, mo65043d2 = {"Lcom/android/systemui/util/animation/DisappearParameters;", "", "()V", "contentTranslationFraction", "Landroid/graphics/PointF;", "getContentTranslationFraction", "()Landroid/graphics/PointF;", "setContentTranslationFraction", "(Landroid/graphics/PointF;)V", "disappearEnd", "", "getDisappearEnd", "()F", "setDisappearEnd", "(F)V", "disappearSize", "getDisappearSize", "setDisappearSize", "disappearStart", "getDisappearStart", "setDisappearStart", "fadeStartPosition", "getFadeStartPosition", "setFadeStartPosition", "gonePivot", "getGonePivot", "setGonePivot", "deepCopy", "equals", "", "other", "hashCode", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: TransitionLayoutController.kt */
public final class DisappearParameters {
    private PointF contentTranslationFraction = new PointF(0.0f, 0.8f);
    private float disappearEnd = 1.0f;
    private PointF disappearSize = new PointF(1.0f, 0.0f);
    private float disappearStart;
    private float fadeStartPosition = 0.9f;
    private PointF gonePivot = new PointF(0.0f, 1.0f);

    public final PointF getGonePivot() {
        return this.gonePivot;
    }

    public final void setGonePivot(PointF pointF) {
        Intrinsics.checkNotNullParameter(pointF, "<set-?>");
        this.gonePivot = pointF;
    }

    public final PointF getDisappearSize() {
        return this.disappearSize;
    }

    public final void setDisappearSize(PointF pointF) {
        Intrinsics.checkNotNullParameter(pointF, "<set-?>");
        this.disappearSize = pointF;
    }

    public final PointF getContentTranslationFraction() {
        return this.contentTranslationFraction;
    }

    public final void setContentTranslationFraction(PointF pointF) {
        Intrinsics.checkNotNullParameter(pointF, "<set-?>");
        this.contentTranslationFraction = pointF;
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

    public boolean equals(Object obj) {
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
        if (!(this.fadeStartPosition == disappearParameters.fadeStartPosition)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((((((this.disappearSize.hashCode() * 31) + this.gonePivot.hashCode()) * 31) + this.contentTranslationFraction.hashCode()) * 31) + Float.hashCode(this.disappearStart)) * 31) + Float.hashCode(this.disappearEnd)) * 31) + Float.hashCode(this.fadeStartPosition);
    }

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
