package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import com.android.p019wm.shell.startingsurface.p021tv.TvStartingWindowTypeAlgorithm;
import dagger.Module;
import dagger.Provides;

@Module(includes = {TvPipModule.class})
/* renamed from: com.android.wm.shell.dagger.TvWMShellModule */
public class TvWMShellModule {
    @WMSingleton
    @DynamicOverride
    @Provides
    static StartingWindowTypeAlgorithm provideStartingWindowTypeAlgorithm() {
        return new TvStartingWindowTypeAlgorithm();
    }
}
