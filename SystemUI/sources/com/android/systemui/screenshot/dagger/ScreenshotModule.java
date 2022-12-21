package com.android.systemui.screenshot.dagger;

import android.app.Service;
import com.android.systemui.screenshot.TakeScreenshotService;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class ScreenshotModule {
    @IntoMap
    @ClassKey(TakeScreenshotService.class)
    @Binds
    public abstract Service bindTakeScreenshotService(TakeScreenshotService takeScreenshotService);
}
