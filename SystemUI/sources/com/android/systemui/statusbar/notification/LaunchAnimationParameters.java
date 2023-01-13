package com.android.systemui.statusbar.notification;

import android.util.MathUtils;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.animation.LaunchAnimator;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b!\n\u0002\u0010\t\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0017¢\u0006\u0002\u0010\u0002B9\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0004\u0012\u0006\u0010\u0007\u001a\u00020\u0004\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\t¢\u0006\u0002\u0010\u000bJ\u0016\u0010\u001a\u001a\u00020\t2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020+R\u001a\u0010\f\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0013\"\u0004\b\u0018\u0010\u0015R\u001a\u0010\u0019\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u000e\"\u0004\b\u001b\u0010\u0010R\u001a\u0010\u001c\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0013\"\u0004\b\u001e\u0010\u0015R\u001a\u0010\u001f\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u000e\"\u0004\b!\u0010\u0010R\u001a\u0010\"\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u0013\"\u0004\b$\u0010\u0015R\u001a\u0010%\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u000e\"\u0004\b'\u0010\u0010R\u0014\u0010(\u001a\u00020\u00048VX\u0004¢\u0006\u0006\u001a\u0004\b)\u0010\u0013¨\u0006-"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/LaunchAnimationParameters;", "Lcom/android/systemui/animation/LaunchAnimator$State;", "()V", "top", "", "bottom", "left", "right", "topCornerRadius", "", "bottomCornerRadius", "(IIIIFF)V", "linearProgress", "getLinearProgress", "()F", "setLinearProgress", "(F)V", "parentStartClipTopAmount", "getParentStartClipTopAmount", "()I", "setParentStartClipTopAmount", "(I)V", "parentStartRoundedTopClipping", "getParentStartRoundedTopClipping", "setParentStartRoundedTopClipping", "progress", "getProgress", "setProgress", "startClipTopAmount", "getStartClipTopAmount", "setStartClipTopAmount", "startNotificationTop", "getStartNotificationTop", "setStartNotificationTop", "startRoundedTopClipping", "getStartRoundedTopClipping", "setStartRoundedTopClipping", "startTranslationZ", "getStartTranslationZ", "setStartTranslationZ", "topChange", "getTopChange", "delay", "", "duration", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LaunchAnimationParameters.kt */
public final class LaunchAnimationParameters extends LaunchAnimator.State {
    private float linearProgress;
    private int parentStartClipTopAmount;
    private int parentStartRoundedTopClipping;
    private float progress;
    private int startClipTopAmount;
    private float startNotificationTop;
    private int startRoundedTopClipping;
    private float startTranslationZ;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ LaunchAnimationParameters(int i, int i2, int i3, int i4, float f, float f2, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2, i3, i4, (i5 & 16) != 0 ? 0.0f : f, (i5 & 32) != 0 ? 0.0f : f2);
    }

    public LaunchAnimationParameters(int i, int i2, int i3, int i4, float f, float f2) {
        super(i, i2, i3, i4, f, f2);
    }

    public LaunchAnimationParameters() {
        this(0, 0, 0, 0, 0.0f, 0.0f);
    }

    public final float getStartTranslationZ() {
        return this.startTranslationZ;
    }

    public final void setStartTranslationZ(float f) {
        this.startTranslationZ = f;
    }

    public final float getStartNotificationTop() {
        return this.startNotificationTop;
    }

    public final void setStartNotificationTop(float f) {
        this.startNotificationTop = f;
    }

    public final int getStartClipTopAmount() {
        return this.startClipTopAmount;
    }

    public final void setStartClipTopAmount(int i) {
        this.startClipTopAmount = i;
    }

    public final int getParentStartClipTopAmount() {
        return this.parentStartClipTopAmount;
    }

    public final void setParentStartClipTopAmount(int i) {
        this.parentStartClipTopAmount = i;
    }

    public final float getProgress() {
        return this.progress;
    }

    public final void setProgress(float f) {
        this.progress = f;
    }

    public final float getLinearProgress() {
        return this.linearProgress;
    }

    public final void setLinearProgress(float f) {
        this.linearProgress = f;
    }

    public final int getStartRoundedTopClipping() {
        return this.startRoundedTopClipping;
    }

    public final void setStartRoundedTopClipping(int i) {
        this.startRoundedTopClipping = i;
    }

    public final int getParentStartRoundedTopClipping() {
        return this.parentStartRoundedTopClipping;
    }

    public final void setParentStartRoundedTopClipping(int i) {
        this.parentStartRoundedTopClipping = i;
    }

    public int getTopChange() {
        int i = this.startClipTopAmount;
        return Math.min(super.getTopChange() - (!((((float) i) > 0.0f ? 1 : (((float) i) == 0.0f ? 0 : -1)) == 0) ? (int) MathUtils.lerp(0.0f, (float) i, Interpolators.FAST_OUT_SLOW_IN.getInterpolation(this.linearProgress)) : 0), 0);
    }

    public final float getProgress(long j, long j2) {
        return LaunchAnimator.Companion.getProgress(ActivityLaunchAnimator.TIMINGS, this.linearProgress, j, j2);
    }
}
