package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\b\u0010\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\u0010\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0003H\u0016J*\u0010\u0011\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000b0\nR\u000e\u0010\u0004\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\bR\u0016\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\nX\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\nX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000¨\u0006\u0014"}, mo65043d2 = {"Lcom/android/systemui/media/MetadataAnimationHandler;", "Landroid/animation/AnimatorListenerAdapter;", "exitAnimator", "Landroid/animation/Animator;", "enterAnimator", "(Landroid/animation/Animator;Landroid/animation/Animator;)V", "isRunning", "", "()Z", "postEnterUpdate", "Lkotlin/Function0;", "", "postExitUpdate", "targetData", "", "onAnimationEnd", "anim", "setNext", "postExit", "postEnter", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MetadataAnimationHandler.kt */
public class MetadataAnimationHandler extends AnimatorListenerAdapter {
    private final Animator enterAnimator;
    private final Animator exitAnimator;
    private Function0<Unit> postEnterUpdate;
    private Function0<Unit> postExitUpdate;
    private Object targetData;

    public MetadataAnimationHandler(Animator animator, Animator animator2) {
        Intrinsics.checkNotNullParameter(animator, "exitAnimator");
        Intrinsics.checkNotNullParameter(animator2, "enterAnimator");
        this.exitAnimator = animator;
        this.enterAnimator = animator2;
        Animator.AnimatorListener animatorListener = this;
        animator.addListener(animatorListener);
        animator2.addListener(animatorListener);
    }

    public final boolean isRunning() {
        return this.enterAnimator.isRunning() || this.exitAnimator.isRunning();
    }

    public final boolean setNext(Object obj, Function0<Unit> function0, Function0<Unit> function02) {
        Intrinsics.checkNotNullParameter(obj, "targetData");
        Intrinsics.checkNotNullParameter(function0, "postExit");
        Intrinsics.checkNotNullParameter(function02, "postEnter");
        if (Intrinsics.areEqual(obj, this.targetData)) {
            return false;
        }
        this.targetData = obj;
        this.postExitUpdate = function0;
        this.postEnterUpdate = function02;
        if (isRunning()) {
            return true;
        }
        this.exitAnimator.start();
        return true;
    }

    public void onAnimationEnd(Animator animator) {
        Intrinsics.checkNotNullParameter(animator, "anim");
        if (animator == this.exitAnimator) {
            Function0<Unit> function0 = this.postExitUpdate;
            if (function0 != null) {
                function0.invoke();
            }
            this.postExitUpdate = null;
            this.enterAnimator.start();
        }
        if (animator != this.enterAnimator) {
            return;
        }
        if (this.postExitUpdate != null) {
            this.exitAnimator.start();
            return;
        }
        Function0<Unit> function02 = this.postEnterUpdate;
        if (function02 != null) {
            function02.invoke();
        }
        this.postEnterUpdate = null;
    }
}
