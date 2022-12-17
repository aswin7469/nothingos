package com.nothing.settings.glyphs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nothing.settings.glyphs.notification.NotificationSettingPreferenceController;
import com.nothing.settings.glyphs.ringtone.RingtonesPreferenceController;
import com.nothing.settings.glyphs.utils.GlyphsSettings;
import java.util.ArrayList;
import java.util.List;

public class GlyphsMainSettingsFragment extends DashboardFragment implements OnActivityResultListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.glyphs_main_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return GlyphsMainSettingsFragment.buildPreferenceControllers(context);
        }
    };
    private static NotificationSettingPreferenceController sNotificationController;
    private GlyphsSettings mGlyphsSettings;

    public String getLogTag() {
        return "GlyphsMainSettingsFragment";
    }

    public int getMetricsCategory() {
        return 2003;
    }

    public int getHelpResource() {
        return R$string.help_url_glyphs_dashboard;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context);
    }

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

    public int getPreferenceScreenResId() {
        return R$xml.glyphs_main_settings;
    }

    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new RingtonesPreferenceController(context));
        NotificationSettingPreferenceController notificationSettingPreferenceController = new NotificationSettingPreferenceController(context, "key_glyphs_notification_setting");
        sNotificationController = notificationSettingPreferenceController;
        arrayList.add(notificationSettingPreferenceController);
        return arrayList;
    }

    public void onResume() {
        super.onResume();
        initMusicItem();
    }

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

    public void onActivityResult(int i, int i2, Intent intent) {
        NotificationSettingPreferenceController notificationSettingPreferenceController = sNotificationController;
        if (notificationSettingPreferenceController != null) {
            notificationSettingPreferenceController.onActivityControllerResult(i, i2, intent);
        }
    }
}
