package com.android.systemui.util.leak;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import dagger.Module;
import dagger.Provides;

@Module
public class LeakModule {
    /* access modifiers changed from: package-private */
    @SysUISingleton
    @Provides
    public LeakDetector providesLeakDetector(DumpManager dumpManager, TrackedCollections trackedCollections) {
        return new LeakDetector((TrackedCollections) null, (TrackedGarbage) null, (TrackedObjects) null, dumpManager);
    }
}
