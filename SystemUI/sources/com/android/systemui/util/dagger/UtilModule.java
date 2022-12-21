package com.android.systemui.util.dagger;

import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.RingerModeTrackerImpl;
import com.android.systemui.util.condition.dagger.MonitorComponent;
import com.android.systemui.util.wrapper.UtilWrapperModule;
import dagger.Binds;
import dagger.Module;

@Module(includes = {UtilWrapperModule.class}, subcomponents = {MonitorComponent.class})
public interface UtilModule {
    @Binds
    RingerModeTracker provideRingerModeTracker(RingerModeTrackerImpl ringerModeTrackerImpl);
}
