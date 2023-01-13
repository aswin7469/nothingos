package com.android.systemui.tuner;

import android.content.Context;
import android.content.res.TypedArray;
import android.provider.Settings;
import android.util.AttributeSet;
import androidx.preference.SwitchPreference;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.tuner.TunerService;

public class TunerSwitch extends SwitchPreference implements TunerService.Tunable {
    private final int mAction;
    private final boolean mDefault;

    public TunerSwitch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1894R.styleable.TunerSwitch);
        this.mDefault = obtainStyledAttributes.getBoolean(0, false);
        this.mAction = obtainStyledAttributes.getInt(1, -1);
        obtainStyledAttributes.recycle();
    }

    public void onAttached() {
        super.onAttached();
        ((TunerService) Dependency.get(TunerService.class)).addTunable(this, getKey().split(NavigationBarInflaterView.BUTTON_SEPARATOR));
    }

    public void onDetached() {
        ((TunerService) Dependency.get(TunerService.class)).removeTunable(this);
        super.onDetached();
    }

    public void onTuningChanged(String str, String str2) {
        setChecked(TunerService.parseIntegerSwitch(str2, this.mDefault));
    }

    /* access modifiers changed from: protected */
    public void onClick() {
        super.onClick();
        if (this.mAction != -1) {
            MetricsLogger.action(getContext(), this.mAction, isChecked());
        }
    }

    /* access modifiers changed from: protected */
    public boolean persistBoolean(boolean z) {
        for (String putString : getKey().split(NavigationBarInflaterView.BUTTON_SEPARATOR)) {
            Settings.Secure.putString(getContext().getContentResolver(), putString, z ? "1" : "0");
        }
        return true;
    }
}
