package com.nothing.p006ui.support;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;
import com.nothing.p006ui.support.utils.NtUIVibrateUtils;

/* renamed from: com.nothing.ui.support.NtCustSwitch */
public class NtCustSwitch extends Switch {
    public NtCustSwitch(Context context) {
        super(context);
    }

    public NtCustSwitch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NtCustSwitch(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public NtCustSwitch(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void toggle() {
        super.toggle();
        NtUIVibrateUtils.getInstance(getContext()).playSwitchVibrate();
    }
}
