package com.android.keyguard.clock;

import dagger.Module;
import dagger.Provides;
import java.util.List;

@Module
public abstract class ClockModule {
    @Provides
    public static List<ClockInfo> provideClockInfoList(ClockManager clockManager) {
        return clockManager.getClockInfos();
    }
}
