package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\b&\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\bH\u0004J\u0010\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0004J\u0010\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\u000e\u0010\u001c\u001a\u00020\b2\u0006\u0010\u001d\u001a\u00020\u0016J\u000e\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u001f\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u001bJ\u0010\u0010 \u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0004J\u0018\u0010!\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u00162\u0006\u0010#\u001a\u00020\u0016H&J\u0016\u0010$\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u00162\u0006\u0010#\u001a\u00020\u0016R\u0014\u0010\u0007\u001a\u00020\bXD¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\n\"\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006%"}, mo64987d2 = {"Lcom/android/systemui/biometrics/AuthIconController;", "Landroid/graphics/drawable/Animatable2$AnimationCallback;", "context", "Landroid/content/Context;", "iconView", "Landroid/widget/ImageView;", "(Landroid/content/Context;Landroid/widget/ImageView;)V", "actsAsConfirmButton", "", "getActsAsConfirmButton", "()Z", "getContext", "()Landroid/content/Context;", "deactivated", "getDeactivated", "setDeactivated", "(Z)V", "getIconView", "()Landroid/widget/ImageView;", "animateIcon", "", "iconRes", "", "repeat", "animateIconOnce", "handleAnimationEnd", "drawable", "Landroid/graphics/drawable/Drawable;", "iconTapSendsRetryWhen", "currentState", "onAnimationEnd", "onAnimationStart", "showStaticDrawable", "updateIcon", "lastState", "newState", "updateState", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AuthBiometricIconController.kt */
public abstract class AuthIconController extends Animatable2.AnimationCallback {
    private final boolean actsAsConfirmButton;
    private final Context context;
    private boolean deactivated;
    private final ImageView iconView;

    public void handleAnimationEnd(Drawable drawable) {
        Intrinsics.checkNotNullParameter(drawable, "drawable");
    }

    public final boolean iconTapSendsRetryWhen(int i) {
        return false;
    }

    public abstract void updateIcon(int i, int i2);

    /* access modifiers changed from: protected */
    public final Context getContext() {
        return this.context;
    }

    /* access modifiers changed from: protected */
    public final ImageView getIconView() {
        return this.iconView;
    }

    public AuthIconController(Context context2, ImageView imageView) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(imageView, "iconView");
        this.context = context2;
        this.iconView = imageView;
    }

    public final boolean getDeactivated() {
        return this.deactivated;
    }

    public final void setDeactivated(boolean z) {
        this.deactivated = z;
    }

    public boolean getActsAsConfirmButton() {
        return this.actsAsConfirmButton;
    }

    public final void onAnimationStart(Drawable drawable) {
        Intrinsics.checkNotNullParameter(drawable, "drawable");
        super.onAnimationStart(drawable);
    }

    public final void onAnimationEnd(Drawable drawable) {
        Intrinsics.checkNotNullParameter(drawable, "drawable");
        super.onAnimationEnd(drawable);
        if (!this.deactivated) {
            handleAnimationEnd(drawable);
        }
    }

    /* access modifiers changed from: protected */
    public final void showStaticDrawable(int i) {
        this.iconView.setImageDrawable(this.context.getDrawable(i));
    }

    /* access modifiers changed from: protected */
    public final void animateIconOnce(int i) {
        animateIcon(i, false);
    }

    /* access modifiers changed from: protected */
    public final void animateIcon(int i, boolean z) {
        if (!this.deactivated) {
            Drawable drawable = this.context.getDrawable(i);
            if (drawable != null) {
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                this.iconView.setImageDrawable(animatedVectorDrawable);
                animatedVectorDrawable.forceAnimationOnUI();
                if (z) {
                    animatedVectorDrawable.registerAnimationCallback(this);
                }
                animatedVectorDrawable.start();
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.AnimatedVectorDrawable");
        }
    }

    public final void updateState(int i, int i2) {
        if (this.deactivated) {
            Log.w("AuthIconController", "Ignoring updateState when deactivated: " + i2);
        } else {
            updateIcon(i, i2);
        }
    }
}
