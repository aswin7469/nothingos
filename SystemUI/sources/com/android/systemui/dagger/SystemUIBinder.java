package com.android.systemui.dagger;

import com.android.systemui.keyguard.dagger.KeyguardModule;
import com.android.systemui.recents.RecentsModule;
import com.android.systemui.statusbar.dagger.CentralSurfacesModule;
import dagger.Module;

@Module(includes = {RecentsModule.class, CentralSurfacesModule.class, KeyguardModule.class})
public abstract class SystemUIBinder {
}
