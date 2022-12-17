package com.nothing.settings.glyphs.music;

import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.search.BaseSearchIndexProvider;
import com.nothing.settings.glyphs.SwitchSettingsPreferenceFragment;

public class MusicVisualisationPreferenceFragment extends SwitchSettingsPreferenceFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.glyphs_music_visualisation_settings);

    public String getLogTag() {
        return "MusicVisualisation";
    }

    public String getSwitchKey() {
        return "led_effect_music_enalbe";
    }

    public String getFeatureString() {
        return getActivity().getString(R$string.nt_glyphs_music_visualisation_features);
    }

    public String getSwitchTitle() {
        return getActivity().getString(R$string.nt_glyphs_music_visualisation_title);
    }

    public int getLayoutId() {
        return R$xml.glyphs_music_visualisation_settings;
    }
}
