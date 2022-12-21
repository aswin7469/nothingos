package com.android.systemui.theme;

import android.content.res.Resources;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module(includes = {SysUIConcurrencyModule.class})
public class ThemeModule {
    static final String LAUNCHER_PACKAGE = "theme_launcher_package";
    static final String THEME_PICKER_PACKAGE = "theme_picker_package";

    @Provides
    @Named("theme_launcher_package")
    static String provideLauncherPackage(@Main Resources resources) {
        return resources.getString(C1893R.string.launcher_overlayable_package);
    }

    @Provides
    @Named("theme_picker_package")
    static String provideThemePickerPackage(@Main Resources resources) {
        return resources.getString(C1893R.string.themepicker_overlayable_package);
    }
}
