package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.CentralSurfacesImpl;
import dagger.Binds;
import dagger.Module;

@Module
public interface StatusBarPhoneModule {
    @SysUISingleton
    @Binds
    CentralSurfaces bindsCentralSurfaces(CentralSurfacesImpl centralSurfacesImpl);
}
