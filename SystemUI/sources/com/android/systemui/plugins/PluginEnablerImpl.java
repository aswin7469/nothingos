package com.android.systemui.plugins;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import com.android.systemui.shared.plugins.PluginEnabler;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PluginEnablerImpl implements PluginEnabler {
    private static final String CRASH_DISABLED_PLUGINS_PREF_FILE = "auto_disabled_plugins_prefs";
    private final SharedPreferences mAutoDisabledPrefs;
    private final PackageManager mPm;

    public PluginEnablerImpl(Context context) {
        this(context, context.getPackageManager());
    }

    @Inject
    public PluginEnablerImpl(Context context, PackageManager packageManager) {
        this.mAutoDisabledPrefs = context.getSharedPreferences(CRASH_DISABLED_PLUGINS_PREF_FILE, 0);
        this.mPm = packageManager;
    }

    public void setEnabled(ComponentName componentName) {
        setDisabled(componentName, 0);
    }

    public void setDisabled(ComponentName componentName, int i) {
        boolean z = i == 0;
        this.mPm.setComponentEnabledSetting(componentName, z ? 1 : 2, 1);
        if (z) {
            this.mAutoDisabledPrefs.edit().remove(componentName.flattenToString()).apply();
        } else {
            this.mAutoDisabledPrefs.edit().putInt(componentName.flattenToString(), i).apply();
        }
    }

    public boolean isEnabled(ComponentName componentName) {
        return this.mPm.getComponentEnabledSetting(componentName) != 2;
    }

    public int getDisableReason(ComponentName componentName) {
        if (isEnabled(componentName)) {
            return 0;
        }
        return this.mAutoDisabledPrefs.getInt(componentName.flattenToString(), 1);
    }
}
