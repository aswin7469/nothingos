package com.android.settings.wallpaper;

import androidx.constraintlayout.widget.R$styleable;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
/* loaded from: classes.dex */
public class WallpaperTypeSettings extends DashboardFragment {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "WallpaperTypeSettings";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return R$styleable.Constraint_layout_goneMarginRight;
    }

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return R.string.help_uri_wallpaper;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.wallpaper_settings;
    }
}
