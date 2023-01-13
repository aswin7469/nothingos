package com.android.systemui.statusbar.phone.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.view.View;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.events.SystemStatusAnimationCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationSchedulerKt;
import com.android.systemui.util.animation.AnimationUtil;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\f\u001a\u00020\rH\u0016J\u0010\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/fragment/StatusBarSystemEventAnimator;", "Lcom/android/systemui/statusbar/events/SystemStatusAnimationCallback;", "animatedView", "Landroid/view/View;", "resources", "Landroid/content/res/Resources;", "(Landroid/view/View;Landroid/content/res/Resources;)V", "getAnimatedView", "()Landroid/view/View;", "translationXIn", "", "translationXOut", "onSystemEventAnimationBegin", "Landroid/animation/Animator;", "onSystemEventAnimationFinish", "hasPersistentDot", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusBarSystemEventAnimator.kt */
public final class StatusBarSystemEventAnimator implements SystemStatusAnimationCallback {
    private final View animatedView;
    private final int translationXIn;
    private final int translationXOut;

    public StatusBarSystemEventAnimator(View view, Resources resources) {
        Intrinsics.checkNotNullParameter(view, "animatedView");
        Intrinsics.checkNotNullParameter(resources, "resources");
        this.animatedView = view;
        this.translationXIn = resources.getDimensionPixelSize(C1894R.dimen.ongoing_appops_chip_animation_in_status_bar_translation_x);
        this.translationXOut = resources.getDimensionPixelSize(C1894R.dimen.ongoing_appops_chip_animation_out_status_bar_translation_x);
    }

    public final View getAnimatedView() {
        return this.animatedView;
    }

    public Animator onSystemEventAnimationBegin() {
        ValueAnimator duration = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f}).setDuration(AnimationUtil.Companion.getFrames(23));
        duration.setInterpolator(SystemStatusAnimationSchedulerKt.STATUS_BAR_X_MOVE_OUT);
        duration.addUpdateListener(new StatusBarSystemEventAnimator$$ExternalSyntheticLambda0(this));
        ValueAnimator duration2 = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f}).setDuration(50);
        duration2.setInterpolator((TimeInterpolator) null);
        duration2.addUpdateListener(new StatusBarSystemEventAnimator$$ExternalSyntheticLambda1(this));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{duration, duration2});
        return animatorSet;
    }

    /* access modifiers changed from: private */
    /* renamed from: onSystemEventAnimationBegin$lambda-0  reason: not valid java name */
    public static final void m3203onSystemEventAnimationBegin$lambda0(StatusBarSystemEventAnimator statusBarSystemEventAnimator, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(statusBarSystemEventAnimator, "this$0");
        Intrinsics.checkNotNullParameter(valueAnimator, "animation");
        View view = statusBarSystemEventAnimator.animatedView;
        float f = (float) statusBarSystemEventAnimator.translationXIn;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            view.setTranslationX(-(f * ((Float) animatedValue).floatValue()));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    /* access modifiers changed from: private */
    /* renamed from: onSystemEventAnimationBegin$lambda-1  reason: not valid java name */
    public static final void m3204onSystemEventAnimationBegin$lambda1(StatusBarSystemEventAnimator statusBarSystemEventAnimator, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(statusBarSystemEventAnimator, "this$0");
        Intrinsics.checkNotNullParameter(valueAnimator, "animation");
        View view = statusBarSystemEventAnimator.animatedView;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            view.setAlpha(((Float) animatedValue).floatValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public Animator onSystemEventAnimationFinish(boolean z) {
        this.animatedView.setTranslationX((float) this.translationXOut);
        ValueAnimator duration = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f}).setDuration(AnimationUtil.Companion.getFrames(28));
        duration.setStartDelay(AnimationUtil.Companion.getFrames(2));
        duration.setInterpolator(SystemStatusAnimationSchedulerKt.STATUS_BAR_X_MOVE_IN);
        duration.addUpdateListener(new StatusBarSystemEventAnimator$$ExternalSyntheticLambda2(this));
        ValueAnimator duration2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f}).setDuration(AnimationUtil.Companion.getFrames(10));
        duration2.setStartDelay(AnimationUtil.Companion.getFrames(4));
        duration2.setInterpolator((TimeInterpolator) null);
        duration2.addUpdateListener(new StatusBarSystemEventAnimator$$ExternalSyntheticLambda3(this));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{duration, duration2});
        return animatorSet;
    }

    /* access modifiers changed from: private */
    /* renamed from: onSystemEventAnimationFinish$lambda-2  reason: not valid java name */
    public static final void m3205onSystemEventAnimationFinish$lambda2(StatusBarSystemEventAnimator statusBarSystemEventAnimator, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(statusBarSystemEventAnimator, "this$0");
        Intrinsics.checkNotNullParameter(valueAnimator, "animation");
        View view = statusBarSystemEventAnimator.animatedView;
        float f = (float) statusBarSystemEventAnimator.translationXOut;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            view.setTranslationX(f * ((Float) animatedValue).floatValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    /* access modifiers changed from: private */
    /* renamed from: onSystemEventAnimationFinish$lambda-3  reason: not valid java name */
    public static final void m3206onSystemEventAnimationFinish$lambda3(StatusBarSystemEventAnimator statusBarSystemEventAnimator, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(statusBarSystemEventAnimator, "this$0");
        Intrinsics.checkNotNullParameter(valueAnimator, "animation");
        View view = statusBarSystemEventAnimator.animatedView;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            view.setAlpha(((Float) animatedValue).floatValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
