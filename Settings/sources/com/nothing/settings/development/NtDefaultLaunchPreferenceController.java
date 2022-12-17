package com.nothing.settings.development;

import android.content.Context;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.display.DensityPreference;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class NtDefaultLaunchPreferenceController extends DeveloperOptionsPreferenceController implements PreferenceControllerMixin {
    private final String mPreferenceKey;

    public NtDefaultLaunchPreferenceController(Context context, String str) {
        super(context);
        this.mPreferenceKey = str;
    }

    public String getPreferenceKey() {
        return this.mPreferenceKey;
    }

    public void updateSummary() {
        ((DensityPreference) this.mPreference).updateSummary();
    }
}
