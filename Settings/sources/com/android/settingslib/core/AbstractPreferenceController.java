package com.android.settingslib.core;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.p002os.BuildCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

public abstract class AbstractPreferenceController {
    private static final String TAG = "AbstractPrefController";
    /* access modifiers changed from: protected */
    public final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;

    public abstract String getPreferenceKey();

    public CharSequence getSummary() {
        return null;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        return false;
    }

    public abstract boolean isAvailable();

    public AbstractPreferenceController(Context context) {
        this.mContext = context;
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        String preferenceKey = getPreferenceKey();
        if (TextUtils.isEmpty(preferenceKey)) {
            Log.w(TAG, "Skipping displayPreference because key is empty:" + getClass().getName());
        } else if (isAvailable()) {
            setVisible(preferenceScreen, preferenceKey, true);
            if (this instanceof Preference.OnPreferenceChangeListener) {
                preferenceScreen.findPreference(preferenceKey).setOnPreferenceChangeListener((Preference.OnPreferenceChangeListener) this);
            }
        } else {
            setVisible(preferenceScreen, preferenceKey, false);
        }
    }

    public void updateState(Preference preference) {
        refreshSummary(preference);
    }

    /* access modifiers changed from: protected */
    public void refreshSummary(Preference preference) {
        CharSequence summary;
        if (preference != null && (summary = getSummary()) != null) {
            preference.setSummary(summary);
        }
    }

    /* access modifiers changed from: protected */
    public final void setVisible(PreferenceGroup preferenceGroup, String str, boolean z) {
        Preference findPreference = preferenceGroup.findPreference(str);
        if (findPreference != null) {
            findPreference.setVisible(z);
        }
    }

    /* access modifiers changed from: protected */
    public void replaceEnterpriseStringTitle(PreferenceScreen preferenceScreen, String str, String str2, int i) {
        if (BuildCompat.isAtLeastT() && this.mDevicePolicyManager != null) {
            Preference findPreference = preferenceScreen.findPreference(str);
            if (findPreference == null) {
                Log.d(TAG, "Could not find enterprise preference " + str);
                return;
            }
            findPreference.setTitle((CharSequence) this.mDevicePolicyManager.getResources().getString(str2, new AbstractPreferenceController$$ExternalSyntheticLambda1(this, i)));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$replaceEnterpriseStringTitle$0(int i) {
        return this.mContext.getString(i);
    }

    /* access modifiers changed from: protected */
    public void replaceEnterpriseStringSummary(PreferenceScreen preferenceScreen, String str, String str2, int i) {
        if (BuildCompat.isAtLeastT() && this.mDevicePolicyManager != null) {
            Preference findPreference = preferenceScreen.findPreference(str);
            if (findPreference == null) {
                Log.d(TAG, "Could not find enterprise preference " + str);
                return;
            }
            findPreference.setSummary((CharSequence) this.mDevicePolicyManager.getResources().getString(str2, new AbstractPreferenceController$$ExternalSyntheticLambda0(this, i)));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$replaceEnterpriseStringSummary$1(int i) {
        return this.mContext.getString(i);
    }
}
