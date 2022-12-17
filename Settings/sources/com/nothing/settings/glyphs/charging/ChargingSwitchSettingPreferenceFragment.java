package com.nothing.settings.glyphs.charging;

import android.provider.Settings;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.search.BaseSearchIndexProvider;
import com.nothing.settings.glyphs.SwitchSettingsPreferenceFragment;
import com.nothing.settings.utils.NtUtils;
import java.util.Objects;

public class ChargingSwitchSettingPreferenceFragment extends SwitchSettingsPreferenceFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.glyphs_charging_settings);

    public String getSwitchKey() {
        return "led_effect_charging_enable";
    }

    public String getFeatureString() {
        FragmentActivity activity = getActivity();
        Objects.requireNonNull(activity);
        return activity.getString(R$string.nt_glyphs_charging_features);
    }

    public String getSwitchTitle() {
        FragmentActivity activity = getActivity();
        Objects.requireNonNull(activity);
        return activity.getString(R$string.nt_glyphs_charging_title);
    }

    public int getLayoutId() {
        return R$xml.glyphs_charging_settings;
    }

    public boolean isChecked() {
        return Settings.Global.getInt(getActivity().getContentResolver(), "led_effect_charging_enable", 1) == 1;
    }

    public boolean setChecked(boolean z) {
        NtUtils.trackIntGlyph(getContext(), "charging", z ? 1 : 0);
        return Settings.Global.putInt(getActivity().getContentResolver(), "led_effect_charging_enable", z);
    }

    public void onPause() {
        super.onPause();
        Log.d("ChargingFrg", "onPause");
        Settings.Global.putInt(getActivity().getContentResolver(), "led_effect_charging_enable", Settings.Global.getInt(getActivity().getContentResolver(), "led_effect_charging_enable", 1));
    }
}
