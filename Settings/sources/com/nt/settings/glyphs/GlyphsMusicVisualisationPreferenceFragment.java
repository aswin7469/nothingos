package com.nt.settings.glyphs;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
/* loaded from: classes2.dex */
public class GlyphsMusicVisualisationPreferenceFragment extends GlyphsSwitchSettingsPreferenceFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_glyphs_music_visualisation_settings);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment, com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "MusicVisualisation";
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    String getSwitchKey() {
        return "led_effect_music_enalbe";
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    String getFeatureString() {
        return getActivity().getString(R.string.nt_glyphs_music_visualisation_features);
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    String getSwitchTitle() {
        return getActivity().getString(R.string.nt_glyphs_music_visualisation_title);
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    int getLayoutId() {
        return R.xml.nt_glyphs_music_visualisation_settings;
    }
}
