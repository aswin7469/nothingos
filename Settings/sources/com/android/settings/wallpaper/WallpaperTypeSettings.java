package com.android.settings.wallpaper;

import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;

public class WallpaperTypeSettings extends DashboardFragment {
    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "WallpaperTypeSettings";
    }

    public int getMetricsCategory() {
        return 101;
    }

    public int getHelpResource() {
        return R$string.help_uri_wallpaper;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.wallpaper_settings;
    }
}
