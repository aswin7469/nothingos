package com.android.systemui.settings.dagger;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserContentResolverProvider;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.settings.UserTrackerImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class SettingsModule {
    /* access modifiers changed from: package-private */
    @SysUISingleton
    @Binds
    public abstract UserContentResolverProvider bindUserContentResolverProvider(UserTracker userTracker);

    /* access modifiers changed from: package-private */
    @SysUISingleton
    @Binds
    public abstract UserContextProvider bindUserContextProvider(UserTracker userTracker);

    @SysUISingleton
    @Provides
    static UserTracker provideUserTracker(Context context, UserManager userManager, DumpManager dumpManager, @Background Handler handler) {
        int currentUser = ActivityManager.getCurrentUser();
        UserTrackerImpl userTrackerImpl = new UserTrackerImpl(context, userManager, dumpManager, handler);
        userTrackerImpl.initialize(currentUser);
        return userTrackerImpl;
    }
}
