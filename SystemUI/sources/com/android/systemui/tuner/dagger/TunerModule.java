package com.android.systemui.tuner.dagger;

import com.android.systemui.tuner.TunerService;
import com.android.systemui.tuner.TunerServiceImpl;
import dagger.Binds;
import dagger.Module;

@Module
public interface TunerModule {
    @Binds
    TunerService provideTunerService(TunerServiceImpl tunerServiceImpl);
}
