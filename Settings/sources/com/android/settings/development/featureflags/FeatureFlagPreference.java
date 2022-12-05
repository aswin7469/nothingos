package com.android.settings.development.featureflags;

import android.content.Context;
import android.util.FeatureFlagUtils;
import androidx.preference.SwitchPreference;
/* loaded from: classes.dex */
public class FeatureFlagPreference extends SwitchPreference {
    private final boolean mIsPersistent;
    private final String mKey;

    public FeatureFlagPreference(Context context, String str) {
        super(context);
        boolean isEnabled;
        this.mKey = str;
        setKey(str);
        setTitle(str);
        boolean isPersistent = FeatureFlagPersistent.isPersistent(str);
        this.mIsPersistent = isPersistent;
        if (isPersistent) {
            isEnabled = FeatureFlagPersistent.isEnabled(context, str);
        } else {
            isEnabled = FeatureFlagUtils.isEnabled(context, str);
        }
        super.setChecked(isEnabled);
    }

    @Override // androidx.preference.TwoStatePreference
    public void setChecked(boolean z) {
        super.setChecked(z);
        if (this.mIsPersistent) {
            FeatureFlagPersistent.setEnabled(getContext(), this.mKey, z);
        } else {
            FeatureFlagUtils.setEnabled(getContext(), this.mKey, z);
        }
    }
}
