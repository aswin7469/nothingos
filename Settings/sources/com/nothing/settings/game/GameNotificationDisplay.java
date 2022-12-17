package com.nothing.settings.game;

import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.R$array;
import com.android.settings.R$raw;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.IllustrationPreference;
import com.nothing.settings.game.GameNotifactionDisplaySettingsController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameNotificationDisplay extends DashboardFragment implements GameNotifactionDisplaySettingsController.OnChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.game_notification_display);
    private static final List<AbstractPreferenceController> sControllers = new ArrayList();
    private IllustrationPreference mVideoPreference;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "GameNotificationDisplay";
    }

    public int getMetricsCategory() {
        return 1886;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        return getCurrentSystemNavigationMode();
    }

    private String getCurrentSystemNavigationMode() {
        boolean z = false;
        if (Settings.Secure.getInt(getPrefContext().getContentResolver(), "nt_game_mode_notification_display_mode", 0) == 0) {
            z = true;
        }
        return z ? "nt_notifacation_display_default" : "nt_notifacation_display_minimal";
    }

    public void onCheckedChanged(Preference preference) {
        for (AbstractPreferenceController updateState : sControllers) {
            updateState.updateState(preference);
        }
        setIllustrationVideo(this.mVideoPreference, preference.getKey(), getPrefContext(), false);
    }

    public void onResume() {
        super.onResume();
        IllustrationPreference illustrationPreference = (IllustrationPreference) getPreferenceScreen().findPreference("nt_notification_display_menu_video");
        this.mVideoPreference = illustrationPreference;
        setIllustrationVideo(illustrationPreference, getDefaultKey(), getPrefContext(), true);
        Iterator<AbstractPreferenceController> it = buildPreferenceControllers(getPrefContext(), getSettingsLifecycle()).iterator();
        while (it.hasNext()) {
            ((GameNotifactionDisplaySettingsController) it.next()).setOnChangeListener(this);
        }
    }

    public void onPause() {
        super.onPause();
        Iterator<AbstractPreferenceController> it = buildPreferenceControllers(getPrefContext(), getSettingsLifecycle()).iterator();
        while (it.hasNext()) {
            ((GameNotifactionDisplaySettingsController) it.next()).setOnChangeListener((GameNotifactionDisplaySettingsController.OnChangeListener) null);
        }
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle());
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle) {
        if (sControllers.size() == 0) {
            String[] stringArray = context.getResources().getStringArray(R$array.nt_notifacation_display_keys);
            for (String gameNotifactionDisplaySettingsController : stringArray) {
                sControllers.add(new GameNotifactionDisplaySettingsController(context, lifecycle, gameNotifactionDisplaySettingsController));
            }
        }
        return sControllers;
    }

    private void setIllustrationVideo(IllustrationPreference illustrationPreference, String str, Context context, boolean z) {
        str.hashCode();
        if (str.equals("nt_notifacation_display_minimal")) {
            illustrationPreference.setLottieAnimationResId(R$raw.nt_game_settings_notification_display_minimal);
        } else if (str.equals("nt_notifacation_display_default")) {
            illustrationPreference.setLottieAnimationResId(R$raw.nt_game_settings_notification_display_default);
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.game_notification_display;
    }
}
