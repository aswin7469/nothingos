package com.android.systemui.dagger;

import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.shared.system.WindowManagerWrapper;
import dagger.Module;
import dagger.Provides;

@Module
public class SharedLibraryModule {
    @SysUISingleton
    @Provides
    public ActivityManagerWrapper provideActivityManagerWrapper() {
        return ActivityManagerWrapper.getInstance();
    }

    @SysUISingleton
    @Provides
    public DevicePolicyManagerWrapper provideDevicePolicyManagerWrapper() {
        return DevicePolicyManagerWrapper.getInstance();
    }

    @SysUISingleton
    @Provides
    public TaskStackChangeListeners provideTaskStackChangeListeners() {
        return TaskStackChangeListeners.getInstance();
    }

    @Provides
    public WindowManagerWrapper providesWindowManagerWrapper() {
        return WindowManagerWrapper.getInstance();
    }
}
