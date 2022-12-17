package com.nothing.settings.glyphs.googleassistant;

import android.provider.Settings;
import android.util.Log;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.search.BaseSearchIndexProvider;
import com.nothing.settings.glyphs.SwitchSettingsPreferenceFragment;
import com.nothing.settings.utils.NtUtils;

public class GoogleAssistantFeedbackPreferenceFragment extends SwitchSettingsPreferenceFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.glyphs_googleassistant_feedback_settings);

    public String getSwitchKey() {
        return "led_effect_google_assistant_enalbe";
    }

    public String getFeatureString() {
        return getActivity().getString(R$string.nt_glyphs_ga_feedback_features);
    }

    public String getSwitchTitle() {
        return getActivity().getString(R$string.nt_glyphs_google_assistant_title);
    }

    public int getLayoutId() {
        return R$xml.glyphs_googleassistant_feedback_settings;
    }

    public boolean isChecked() {
        return Settings.Global.getInt(getActivity().getContentResolver(), "led_effect_google_assistant_enalbe", 1) == 1;
    }

    public boolean setChecked(boolean z) {
        NtUtils.trackIntGlyph(getContext(), "google_fb", z ? 1 : 0);
        return Settings.Global.putInt(getActivity().getContentResolver(), "led_effect_google_assistant_enalbe", z);
    }

    public void onPause() {
        super.onPause();
        Log.d("GoogleAssistantFrg", "onPause");
        Settings.Global.putInt(getActivity().getContentResolver(), "led_effect_google_assistant_enalbe", Settings.Global.getInt(getActivity().getContentResolver(), "led_effect_google_assistant_enalbe", 1));
    }
}
