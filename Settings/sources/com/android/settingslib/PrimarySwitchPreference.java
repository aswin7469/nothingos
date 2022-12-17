package com.android.settingslib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Keep;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.core.instrumentation.SettingsJankMonitor;
import com.nothing.p006ui.support.NtCustSwitch;

public class PrimarySwitchPreference extends RestrictedPreference {
    private boolean mChecked;
    private boolean mCheckedSet;
    private boolean mEnableSwitch = true;
    private NtCustSwitch mSwitch;

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
        return R$layout.preference_widget_primary_switch;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        NtCustSwitch ntCustSwitch = (NtCustSwitch) preferenceViewHolder.findViewById(R$id.switchWidget);
        this.mSwitch = ntCustSwitch;
        if (ntCustSwitch != null) {
            ntCustSwitch.setOnClickListener(new PrimarySwitchPreference$$ExternalSyntheticLambda0(this));
            this.mSwitch.setOnTouchListener(new PrimarySwitchPreference$$ExternalSyntheticLambda1());
            this.mSwitch.setContentDescription(getTitle());
            this.mSwitch.setChecked(this.mChecked);
            this.mSwitch.setEnabled(this.mEnableSwitch);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        NtCustSwitch ntCustSwitch = this.mSwitch;
        if (ntCustSwitch == null || ntCustSwitch.isEnabled()) {
            boolean z = !this.mChecked;
            if (callChangeListener(Boolean.valueOf(z))) {
                SettingsJankMonitor.detectToggleJank(getKey(), this.mSwitch);
                setChecked(z);
                persistBoolean(z);
            }
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onBindViewHolder$1(View view, MotionEvent motionEvent) {
        return motionEvent.getActionMasked() == 2;
    }

    public boolean isChecked() {
        return this.mSwitch != null && this.mChecked;
    }

    @Keep
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
            NtCustSwitch ntCustSwitch = this.mSwitch;
            if (ntCustSwitch != null) {
                ntCustSwitch.setChecked(z);
            }
        }
    }

    public void setSwitchEnabled(boolean z) {
        this.mEnableSwitch = z;
        NtCustSwitch ntCustSwitch = this.mSwitch;
        if (ntCustSwitch != null) {
            ntCustSwitch.setEnabled(z);
        }
    }

    public void setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        super.setDisabledByAdmin(enforcedAdmin);
        setSwitchEnabled(enforcedAdmin == null);
    }

    public NtCustSwitch getSwitch() {
        return this.mSwitch;
    }

    /* access modifiers changed from: protected */
    public boolean shouldHideSecondTarget() {
        return getSecondTargetResId() == 0;
    }
}
