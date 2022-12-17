package com.android.settings.sar;

import android.os.Bundle;
import com.android.settings.R$xml;
import com.android.settings.SettingsPreferenceFragment;

public class WifiBtSarTest extends SettingsPreferenceFragment {
    public int getMetricsCategory() {
        return 89;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R$xml.testing_wifi_bt_sar);
    }
}
