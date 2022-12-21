package com.android.systemui.dagger;

import com.android.systemui.broadcast.BroadcastDispatcherModule;
import com.android.systemui.theme.ThemeModule;
import com.android.systemui.util.leak.LeakModule;
import dagger.Module;

@Deprecated
@Module(includes = {BroadcastDispatcherModule.class, LeakModule.class, NightDisplayListenerModule.class, SharedLibraryModule.class, SettingsLibraryModule.class, ThemeModule.class})
public class DependencyProvider {
}
