package com.nt.settings.glyphs;

import com.android.settings.R;
/* loaded from: classes2.dex */
public class GlyphsChargingSwitchSettingPreferenceFragment extends GlyphsSwitchSettingsPreferenceFragment {
    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    String getSwitchKey() {
        return "led_effect_charging_enable";
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    String getFeatureString() {
        return getActivity().getString(R.string.nt_glyphs_charging_features);
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    String getSwitchTitle() {
        return getActivity().getString(R.string.nt_glyphs_charging_title);
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    int getLayoutId() {
        return R.xml.nt_glyphs_charging_settings;
    }
}
