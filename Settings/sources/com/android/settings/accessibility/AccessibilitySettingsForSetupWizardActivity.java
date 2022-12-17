package com.android.settings.accessibility;

import android.os.Bundle;
import android.view.Menu;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.android.settings.R$id;
import com.android.settings.R$style;
import com.android.settings.SettingsActivity;
import com.android.settings.SetupWizardUtils;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.core.instrumentation.Instrumentable;
import com.google.android.setupdesign.util.ThemeHelper;

public class AccessibilitySettingsForSetupWizardActivity extends SettingsActivity {
    static final String CLASS_NAME_FONT_SIZE_SETTINGS_FOR_SUW = "com.android.settings.FontSizeSettingsForSetupWizardActivity";

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putCharSequence("activity_title", getTitle());
        super.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        setTitle(bundle.getCharSequence("activity_title"));
    }

    public boolean onNavigateUp() {
        onBackPressed();
        getWindow().getDecorView().sendAccessibilityEvent(32);
        return true;
    }

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

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        applyTheme();
        findViewById(R$id.content_parent).setFitsSystemWindows(false);
    }

    private void applyTheme() {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        setTheme(R$style.SettingsPreferenceTheme_SetupWizard);
        ThemeHelper.trySetDynamicColor(this);
    }
}
