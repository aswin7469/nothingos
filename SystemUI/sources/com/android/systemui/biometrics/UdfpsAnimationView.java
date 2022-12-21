package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;

public abstract class UdfpsAnimationView extends FrameLayout {
    private int mAlpha;
    private float mDialogSuggestedAlpha = 1.0f;
    private float mNotificationShadeExpansion = 0.0f;
    boolean mPauseAuth;

    private int expansionToAlpha(float f) {
        if (f >= 0.4f) {
            return 0;
        }
        return (int) ((1.0f - (f / 0.4f)) * 255.0f);
    }

    /* access modifiers changed from: package-private */
    public boolean dozeTimeTick() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public abstract UdfpsDrawable getDrawable();

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public UdfpsAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: package-private */
    public void onSensorRectUpdated(RectF rectF) {
        getDrawable().onSensorRectUpdated(rectF);
    }

    /* access modifiers changed from: package-private */
    public void onIlluminationStarting() {
        getDrawable().setIlluminationShowing(true);
        getDrawable().invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    public void onIlluminationStopped() {
        getDrawable().setIlluminationShowing(false);
        getDrawable().invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    public boolean setPauseAuth(boolean z) {
        if (z == this.mPauseAuth) {
            return false;
        }
        this.mPauseAuth = z;
        updateAlpha();
        return true;
    }

    /* access modifiers changed from: protected */
    public int updateAlpha() {
        int calculateAlpha = calculateAlpha();
        getDrawable().setAlpha(calculateAlpha);
        if (!this.mPauseAuth || calculateAlpha != 0 || getParent() == null) {
            ((ViewGroup) getParent()).setVisibility(0);
        } else {
            ((ViewGroup) getParent()).setVisibility(4);
        }
        return calculateAlpha;
    }

    /* access modifiers changed from: package-private */
    public int calculateAlpha() {
        int expansionToAlpha = (int) (((float) expansionToAlpha(this.mNotificationShadeExpansion)) * this.mDialogSuggestedAlpha);
        this.mAlpha = expansionToAlpha;
        if (this.mPauseAuth) {
            return expansionToAlpha;
        }
        return 255;
    }

    /* access modifiers changed from: package-private */
    public boolean isPauseAuth() {
        return this.mPauseAuth;
    }

    public void setDialogSuggestedAlpha(float f) {
        this.mDialogSuggestedAlpha = f;
        updateAlpha();
    }

    public float getDialogSuggestedAlpha() {
        return this.mDialogSuggestedAlpha;
    }

    public void onExpansionChanged(float f) {
        this.mNotificationShadeExpansion = f;
        updateAlpha();
    }
}
