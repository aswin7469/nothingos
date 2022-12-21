package com.android.keyguard;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.core.graphics.drawable.DrawableCompat;
import com.android.systemui.C1893R;

public class KeyguardSimPinView extends KeyguardPinBasedInputView {
    public static final String TAG = "KeyguardSimPinView";
    private ImageView mSimImageView;

    /* access modifiers changed from: protected */
    public int getPasswordTextViewId() {
        return C1893R.C1897id.simPinEntry;
    }

    /* access modifiers changed from: protected */
    public int getPromptReasonStringRes(int i) {
        return 0;
    }

    public void startAppearAnimation() {
    }

    public KeyguardSimPinView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardSimPinView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setEsimLocked(boolean z, int i) {
        KeyguardEsimArea keyguardEsimArea = (KeyguardEsimArea) findViewById(C1893R.C1897id.keyguard_esim_area);
        keyguardEsimArea.setSubscriptionId(i);
        keyguardEsimArea.setVisibility(z ? 0 : 8);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        resetState();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        this.mSimImageView = (ImageView) findViewById(C1893R.C1897id.keyguard_sim);
        super.onFinishInflate();
        if (this.mEcaView instanceof EmergencyCarrierArea) {
            ((EmergencyCarrierArea) this.mEcaView).setCarrierTextVisible(true);
        }
    }

    public CharSequence getTitle() {
        return getContext().getString(17040522);
    }

    public void reloadColors() {
        super.reloadColors();
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{16842808});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        DrawableCompat.setTint(DrawableCompat.wrap(this.mSimImageView.getDrawable()), color);
    }
}
