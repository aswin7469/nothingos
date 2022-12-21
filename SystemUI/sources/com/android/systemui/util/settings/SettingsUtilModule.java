package com.android.systemui.util.settings;

import dagger.Binds;
import dagger.Module;

@Module
public interface SettingsUtilModule {
    @Binds
    GlobalSettings bindsGlobalSettings(GlobalSettingsImpl globalSettingsImpl);

    @Binds
    SecureSettings bindsSecureSettings(SecureSettingsImpl secureSettingsImpl);

    @Binds
    SystemSettings bindsSystemSettings(SystemSettingsImpl systemSettingsImpl);
}
