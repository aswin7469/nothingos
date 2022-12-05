package com.nt.settings.glyphs;

import com.android.settings.R;
/* loaded from: classes2.dex */
public class GlyphsGAFBSettingPreferenceFragment extends GlyphsSwitchSettingsPreferenceFragment {
    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    String getSwitchKey() {
        return "led_effect_google_assistant_enalbe";
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    String getFeatureString() {
        return getActivity().getString(R.string.nt_glyphs_ga_feedback_features);
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    String getSwitchTitle() {
        return getActivity().getString(R.string.nt_glyphs_google_assistant_title);
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    int getLayoutId() {
        return R.xml.nt_glyphs_gafb_settings;
    }
}
