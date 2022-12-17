package com.android.settings.notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.widget.FooterPreference;

public class SpatialAudioSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.spatial_audio_settings);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "SpatialAudioSettings";
    }

    public int getMetricsCategory() {
        return 1921;
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        FooterPreference footerPreference = (FooterPreference) findPreference("spatial_audio_footer");
        if (footerPreference != null) {
            footerPreference.setLearnMoreText(getString(R$string.spatial_audio_footer_learn_more_text));
            footerPreference.setLearnMoreAction(new SpatialAudioSettings$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreatePreferences$0(View view) {
        startActivity(new Intent("android.settings.BLUETOOTH_SETTINGS"));
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.spatial_audio_settings;
    }
}
