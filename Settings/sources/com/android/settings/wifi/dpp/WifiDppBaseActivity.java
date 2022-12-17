package com.android.settings.wifi.dpp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.android.settings.R$layout;
import com.android.settings.R$style;
import com.android.settings.SetupWizardUtils;
import com.android.settings.core.InstrumentedActivity;
import com.google.android.setupdesign.util.ThemeHelper;

public abstract class WifiDppBaseActivity extends InstrumentedActivity {
    protected FragmentManager mFragmentManager;

    /* access modifiers changed from: protected */
    public abstract void handleIntent(Intent intent);

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        applyTheme();
        setContentView(R$layout.wifi_dpp_activity);
        this.mFragmentManager = getSupportFragmentManager();
        if (bundle == null) {
            handleIntent(getIntent());
        }
    }

    /* access modifiers changed from: protected */
    public void onApplyThemeResource(Resources.Theme theme, int i, boolean z) {
        theme.applyStyle(R$style.SetupWizardPartnerResource, true);
        super.onApplyThemeResource(theme, i, z);
    }

    private void applyTheme() {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        setTheme(R$style.SettingsPreferenceTheme_SetupWizard);
        ThemeHelper.trySetDynamicColor(this);
    }
}
