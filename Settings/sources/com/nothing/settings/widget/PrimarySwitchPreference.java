package com.nothing.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Keep;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedPreference;
import com.nothing.p006ui.support.NtCustSwitch;

public class PrimarySwitchPreference extends RestrictedPreference {
    /* access modifiers changed from: private */
    public boolean mChecked;
    private boolean mCheckedSet;
    private boolean mEnableSwitch = true;
    /* access modifiers changed from: private */
    public NtCustSwitch mSwitch;

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
        return R$layout.restricted_preference_widget_primary_switch;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        int i = R$id.switchWidget;
        View findViewById = preferenceViewHolder.findViewById(i);
        View findViewById2 = preferenceViewHolder.findViewById(R$id.restricted_icon);
        int i2 = 8;
        if (findViewById2 != null) {
            findViewById2.setVisibility(8);
        }
        if (findViewById != null) {
            if (!isDisabledByAdmin()) {
                i2 = 0;
            }
            findViewById.setVisibility(i2);
            findViewById.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (PrimarySwitchPreference.this.mSwitch == null || PrimarySwitchPreference.this.mSwitch.isEnabled()) {
                        PrimarySwitchPreference primarySwitchPreference = PrimarySwitchPreference.this;
                        primarySwitchPreference.setChecked(!primarySwitchPreference.mChecked);
                        PrimarySwitchPreference primarySwitchPreference2 = PrimarySwitchPreference.this;
                        if (!primarySwitchPreference2.callChangeListener(Boolean.valueOf(primarySwitchPreference2.mChecked))) {
                            PrimarySwitchPreference primarySwitchPreference3 = PrimarySwitchPreference.this;
                            primarySwitchPreference3.setChecked(!primarySwitchPreference3.mChecked);
                            return;
                        }
                        PrimarySwitchPreference primarySwitchPreference4 = PrimarySwitchPreference.this;
                        boolean unused = primarySwitchPreference4.persistBoolean(primarySwitchPreference4.mChecked);
                    }
                }
            });
            findViewById.setOnTouchListener(new PrimarySwitchPreference$$ExternalSyntheticLambda0());
        }
        NtCustSwitch ntCustSwitch = (NtCustSwitch) preferenceViewHolder.findViewById(i);
        this.mSwitch = ntCustSwitch;
        if (ntCustSwitch != null) {
            ntCustSwitch.setContentDescription(getTitle());
            this.mSwitch.setChecked(this.mChecked);
            this.mSwitch.setEnabled(this.mEnableSwitch);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onBindViewHolder$0(View view, MotionEvent motionEvent) {
        return motionEvent.getActionMasked() == 2;
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

    /* access modifiers changed from: protected */
    public boolean shouldHideSecondTarget() {
        return getSecondTargetResId() == 0;
    }
}
