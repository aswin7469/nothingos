package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
abstract class UdfpsAnimationView extends FrameLayout {
    private int mAlpha;
    boolean mPauseAuth;

    private int expansionToAlpha(float f) {
        if (f >= 0.4f) {
            return 0;
        }
        return (int) ((1.0f - (f / 0.4f)) * 255.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean dozeTimeTick() {
        return false;
    }

    abstract UdfpsDrawable getDrawable();

    public UdfpsAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSensorRectUpdated(RectF rectF) {
        getDrawable().onSensorRectUpdated(rectF);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onIlluminationStarting() {
        getDrawable().setIlluminationShowing(true);
        getDrawable().invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onIlluminationStopped() {
        getDrawable().setIlluminationShowing(false);
        getDrawable().invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setPauseAuth(boolean z) {
        if (z != this.mPauseAuth) {
            this.mPauseAuth = z;
            updateAlpha();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int updateAlpha() {
        int calculateAlpha = calculateAlpha();
        getDrawable().setAlpha(calculateAlpha);
        if (this.mPauseAuth && calculateAlpha == 0 && getParent() != null) {
            ((ViewGroup) getParent()).setVisibility(4);
        } else {
            ((ViewGroup) getParent()).setVisibility(0);
        }
        return calculateAlpha;
    }

    int calculateAlpha() {
        if (this.mPauseAuth) {
            return this.mAlpha;
        }
        return 255;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPauseAuth() {
        return this.mPauseAuth;
    }

    public void onExpansionChanged(float f, boolean z) {
        this.mAlpha = expansionToAlpha(f);
        updateAlpha();
    }
}
