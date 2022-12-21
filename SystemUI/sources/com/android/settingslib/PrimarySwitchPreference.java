package com.android.settingslib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.core.instrumentation.SettingsJankMonitor;

public class PrimarySwitchPreference extends RestrictedPreference {
    private boolean mChecked;
    private boolean mCheckedSet;
    private boolean mEnableSwitch = true;
    private Switch mSwitch;

    public PrimarySwitchPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public PrimarySwitchPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public PrimarySwitchPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PrimarySwitchPreference(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public int getSecondTargetResId() {
        return C1757R.layout.preference_widget_primary_switch;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Switch switchR = (Switch) preferenceViewHolder.findViewById(C1757R.C1760id.switchWidget);
        this.mSwitch = switchR;
        if (switchR != null) {
            switchR.setOnClickListener(new PrimarySwitchPreference$$ExternalSyntheticLambda0(this));
            this.mSwitch.setOnTouchListener(new PrimarySwitchPreference$$ExternalSyntheticLambda1());
            this.mSwitch.setContentDescription(getTitle());
            this.mSwitch.setChecked(this.mChecked);
            this.mSwitch.setEnabled(this.mEnableSwitch);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$com-android-settingslib-PrimarySwitchPreference */
    public /* synthetic */ void mo27864x7457526e(View view) {
        Switch switchR = this.mSwitch;
        if (switchR == null || switchR.isEnabled()) {
            boolean z = !this.mChecked;
            if (callChangeListener(Boolean.valueOf(z))) {
                SettingsJankMonitor.detectToggleJank(getKey(), this.mSwitch);
                setChecked(z);
                persistBoolean(z);
            }
        }
    }

    static /* synthetic */ boolean lambda$onBindViewHolder$1(View view, MotionEvent motionEvent) {
        return motionEvent.getActionMasked() == 2;
    }

    public boolean isChecked() {
        return this.mSwitch != null && this.mChecked;
    }

    public Boolean getCheckedState() {
        if (this.mCheckedSet) {
            return Boolean.valueOf(this.mChecked);
        }
        return null;
    }

    public void setChecked(boolean z) {
        if ((this.mChecked != z) || !this.mCheckedSet) {
            this.mChecked = z;
            this.mCheckedSet = true;
            Switch switchR = this.mSwitch;
            if (switchR != null) {
                switchR.setChecked(z);
            }
        }
    }

    public void setSwitchEnabled(boolean z) {
        this.mEnableSwitch = z;
        Switch switchR = this.mSwitch;
        if (switchR != null) {
            switchR.setEnabled(z);
        }
    }

    public void setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        super.setDisabledByAdmin(enforcedAdmin);
        setSwitchEnabled(enforcedAdmin == null);
    }

    public Switch getSwitch() {
        return this.mSwitch;
    }

    /* access modifiers changed from: protected */
    public boolean shouldHideSecondTarget() {
        return getSecondTargetResId() == 0;
    }
}
