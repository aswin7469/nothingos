package com.android.settings.accessibility;

import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.SetupWizardUtils;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.display.FontSizePreferenceFragmentForSetupWizard;
import com.android.settingslib.core.instrumentation.Instrumentable;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.util.ThemeHelper;
/* loaded from: classes.dex */
public class AccessibilitySettingsForSetupWizardActivity extends SettingsActivity {
    static final String CLASS_NAME_FONT_SIZE_SETTINGS_FOR_SUW = "com.android.settings.FontSizeSettingsForSetupWizardActivity";

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.SettingsActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putCharSequence("activity_title", getTitle());
        super.onSaveInstanceState(bundle);
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        setTitle(bundle.getCharSequence("activity_title"));
    }

    @Override // com.android.settings.core.SettingsBaseActivity, android.app.Activity
    public boolean onNavigateUp() {
        onBackPressed();
        getWindow().getDecorView().sendAccessibilityEvent(32);
        return true;
    }

    @Override // com.android.settings.SettingsActivity, androidx.preference.PreferenceFragmentCompat.OnPreferenceStartFragmentCallback
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat preferenceFragmentCompat, Preference preference) {
        Bundle extras = preference.getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
        int i = 0;
        extras.putInt("help_uri_resource", 0);
        extras.putBoolean("need_search_icon_in_action_bar", false);
        SubSettingLauncher arguments = new SubSettingLauncher(this).setDestination(preference.getFragment()).setArguments(extras);
        if (preferenceFragmentCompat instanceof Instrumentable) {
            i = ((Instrumentable) preferenceFragmentCompat).getMetricsCategory();
        }
        arguments.setSourceMetricsCategory(i).setExtras(SetupWizardUtils.copyLifecycleExtra(getIntent().getExtras(), new Bundle())).setTransitionType(2).launch();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.SettingsActivity, com.android.settings.core.SettingsBaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        applyTheme();
        tryLaunchFontSizeSettings();
        findViewById(R.id.content_parent).setFitsSystemWindows(false);
    }

    private void applyTheme() {
        int i;
        if (ThemeHelper.trySetDynamicColor(this)) {
            if (ThemeHelper.isSetupWizardDayNightEnabled(this)) {
                i = R.style.SudDynamicColorThemeSettings_SetupWizard_DayNight;
            } else {
                i = R.style.SudDynamicColorThemeSettings_SetupWizard;
            }
            setTheme(i);
            return;
        }
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
    }

    void tryLaunchFontSizeSettings() {
        if (!WizardManagerHelper.isAnySetupWizard(getIntent()) || !new ComponentName(getPackageName(), CLASS_NAME_FONT_SIZE_SETTINGS_FOR_SUW).equals(getIntent().getComponent())) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("help_uri_resource", 0);
        bundle.putBoolean("need_search_icon_in_action_bar", false);
        SubSettingLauncher transitionType = new SubSettingLauncher(this).setDestination(FontSizePreferenceFragmentForSetupWizard.class.getName()).setArguments(bundle).setSourceMetricsCategory(0).setExtras(SetupWizardUtils.copyLifecycleExtra(getIntent().getExtras(), new Bundle())).setTransitionType(2);
        Log.d("A11ySettingsForSUW", "Launch font size settings");
        transitionType.launch();
        finish();
    }
}
