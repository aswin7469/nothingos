package com.android.systemui.tuner;

import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import com.android.systemui.C1894R;

public class OtherPrefs extends PreferenceFragment {
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(C1894R.C1902xml.other_settings);
    }
}
