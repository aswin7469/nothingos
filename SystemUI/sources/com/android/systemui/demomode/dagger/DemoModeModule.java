package com.android.systemui.demomode.dagger;

import android.content.Context;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class DemoModeModule {
    @SysUISingleton
    @Provides
    static DemoModeController provideDemoModeController(Context context, DumpManager dumpManager, GlobalSettings globalSettings) {
        DemoModeController demoModeController = new DemoModeController(context, dumpManager, globalSettings);
        demoModeController.initialize();
        return demoModeController;
    }
}
