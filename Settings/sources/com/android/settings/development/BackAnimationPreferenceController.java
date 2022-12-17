package com.android.settings.development;

import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;
import java.util.Objects;

public class BackAnimationPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    private final DevelopmentSettingsDashboardFragment mFragment;

    public String getPreferenceKey() {
        return "back_navigation_animation";
    }

    @VisibleForTesting
    BackAnimationPreferenceController(Context context) {
        super(context);
        this.mFragment = null;
    }

    public BackAnimationPreferenceController(Context context, DevelopmentSettingsDashboardFragment developmentSettingsDashboardFragment) {
        super(context);
        Objects.requireNonNull(developmentSettingsDashboardFragment);
        this.mFragment = developmentSettingsDashboardFragment;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        Settings.Global.putInt(this.mContext.getContentResolver(), "enable_back_animation", booleanValue ? 1 : 0);
        DevelopmentSettingsDashboardFragment developmentSettingsDashboardFragment = this.mFragment;
        if (developmentSettingsDashboardFragment == null || !booleanValue) {
            return true;
        }
        BackAnimationPreferenceDialog.show(developmentSettingsDashboardFragment);
        return true;
    }

    public void updateState(Preference preference) {
        boolean z = false;
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "enable_back_animation", 0);
        SwitchPreference switchPreference = (SwitchPreference) this.mPreference;
        if (i != 0) {
            z = true;
        }
        switchPreference.setChecked(z);
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        Settings.Global.putInt(this.mContext.getContentResolver(), "enable_back_animation", 0);
        ((SwitchPreference) this.mPreference).setChecked(false);
    }
}
