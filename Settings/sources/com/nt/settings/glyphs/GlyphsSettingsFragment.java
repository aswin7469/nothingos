package com.nt.settings.glyphs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nt.settings.glyphs.utils.GlyphsSettings;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsSettingsFragment extends DashboardFragment implements OnActivityResultListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.glyphs_settings_fragment) { // from class: com.nt.settings.glyphs.GlyphsSettingsFragment.1
        @Override // com.android.settings.search.BaseSearchIndexProvider
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return GlyphsSettingsFragment.buildPreferenceControllers(context);
        }
    };
    private static GlyphsNotificationSettingPreferenceController sNotificationController;
    private GlyphsSettings mGlyphsSettings;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "GlyphsSettingsFragment";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 2003;
    }

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return R.string.help_url_glyphs_dashboard;
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context);
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getPreferenceScreen().findPreference("key_glyphs_brightness_tip").setVisible(!isChecked());
        getPreferenceScreen().findPreference("glyphs_brightness").setVisible(isChecked());
        this.mGlyphsSettings = new GlyphsSettings(getActivity());
        initMusicItem();
    }

    public boolean isChecked() {
        return Settings.Global.getInt(getContentResolver(), "led_effect_enable", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.glyphs_settings_fragment;
    }

    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new GlyphsRingtonesPreferenceController(context));
        GlyphsNotificationSettingPreferenceController glyphsNotificationSettingPreferenceController = new GlyphsNotificationSettingPreferenceController(context, "key_glyphs_notification_setting");
        sNotificationController = glyphsNotificationSettingPreferenceController;
        arrayList.add(glyphsNotificationSettingPreferenceController);
        return arrayList;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        initMusicItem();
    }

    @Override // com.android.settings.SettingsPreferenceFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        sNotificationController = null;
    }

    private void initMusicItem() {
        getPreferenceScreen().findPreference("key_glyphs_music_visualisation").setVisible(shouldShowMusicItem());
    }

    private boolean shouldShowMusicItem() {
        return this.mGlyphsSettings.shouldShowMusicItem();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        GlyphsNotificationSettingPreferenceController glyphsNotificationSettingPreferenceController = sNotificationController;
        if (glyphsNotificationSettingPreferenceController != null) {
            glyphsNotificationSettingPreferenceController.onActivityControllerResult(i, i2, intent);
        }
    }
}
