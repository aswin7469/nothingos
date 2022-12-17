package com.nothing.p006ui.support;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.SwitchPreference;
import com.nothing.p006ui.support.utils.NtUIVibrateUtils;

/* renamed from: com.nothing.ui.support.NtCustSwitchPreference */
public class NtCustSwitchPreference extends SwitchPreference {
    public NtCustSwitchPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public NtCustSwitchPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public NtCustSwitchPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NtCustSwitchPreference(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onClick() {
        super.onClick();
        NtUIVibrateUtils.getInstance(getContext()).playSwitchVibrate();
    }
}
