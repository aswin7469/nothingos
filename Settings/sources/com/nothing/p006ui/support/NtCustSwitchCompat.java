package com.nothing.p006ui.support;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.SwitchCompat;
import com.nothing.p006ui.support.utils.NtUIVibrateUtils;

/* renamed from: com.nothing.ui.support.NtCustSwitchCompat */
public class NtCustSwitchCompat extends SwitchCompat {
    public NtCustSwitchCompat(Context context) {
        super(context);
    }

    public NtCustSwitchCompat(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NtCustSwitchCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void toggle() {
        super.toggle();
        NtUIVibrateUtils.getInstance(getContext()).playSwitchVibrate();
    }
}
