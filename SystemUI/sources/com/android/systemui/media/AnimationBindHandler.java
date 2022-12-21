package com.android.systemui.media;

import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0014\u0010\u0012\u001a\u00020\t2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\t0\bJ\u0010\u0010\u0014\u001a\u00020\t2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011J\u0006\u0010\u0015\u001a\u00020\tJ\u0015\u0010\u0016\u001a\u00020\u00042\b\u0010\u0017\u001a\u0004\u0018\u00010\u000b¢\u0006\u0002\u0010\u0018R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0005R\u001a\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u0007X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u0004\n\u0002\u0010\fR\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo64987d2 = {"Lcom/android/systemui/media/AnimationBindHandler;", "Landroid/graphics/drawable/Animatable2$AnimationCallback;", "()V", "isAnimationRunning", "", "()Z", "onAnimationsComplete", "", "Lkotlin/Function0;", "", "rebindId", "", "Ljava/lang/Integer;", "registrations", "Landroid/graphics/drawable/Animatable2;", "onAnimationEnd", "drawable", "Landroid/graphics/drawable/Drawable;", "tryExecute", "action", "tryRegister", "unregisterAll", "updateRebindId", "newRebindId", "(Ljava/lang/Integer;)Z", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AnimationBindHandler.kt */
public final class AnimationBindHandler extends Animatable2.AnimationCallback {
    private final List<Function0<Unit>> onAnimationsComplete = new ArrayList();
    private Integer rebindId;
    private final List<Animatable2> registrations = new ArrayList();

    public final boolean isAnimationRunning() {
        Iterable<Animatable2> iterable = this.registrations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (Animatable2 isRunning : iterable) {
            if (isRunning.isRunning()) {
                return true;
            }
        }
        return false;
    }

    public final boolean updateRebindId(Integer num) {
        Integer num2 = this.rebindId;
        if (num2 != null && num != null && Intrinsics.areEqual((Object) num2, (Object) num)) {
            return false;
        }
        this.rebindId = num;
        return true;
    }

    public final void tryRegister(Drawable drawable) {
        if (drawable instanceof Animatable2) {
            Animatable2 animatable2 = (Animatable2) drawable;
            animatable2.registerAnimationCallback(this);
            this.registrations.add(animatable2);
        }
    }

    public final void unregisterAll() {
        for (Animatable2 unregisterAnimationCallback : this.registrations) {
            unregisterAnimationCallback.unregisterAnimationCallback(this);
        }
        this.registrations.clear();
    }

    public final void tryExecute(Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "action");
        if (isAnimationRunning()) {
            this.onAnimationsComplete.add(function0);
        } else {
            function0.invoke();
        }
    }

    public void onAnimationEnd(Drawable drawable) {
        Intrinsics.checkNotNullParameter(drawable, "drawable");
        super.onAnimationEnd(drawable);
        if (!isAnimationRunning()) {
            for (Function0 invoke : this.onAnimationsComplete) {
                invoke.invoke();
            }
            this.onAnimationsComplete.clear();
        }
    }
}
