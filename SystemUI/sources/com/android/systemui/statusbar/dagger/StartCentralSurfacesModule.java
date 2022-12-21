package com.android.systemui.statusbar.dagger;

import com.android.systemui.CoreStartable;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.CentralSurfacesImpl;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H'ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/dagger/StartCentralSurfacesModule;", "", "bindsCentralSurfaces", "Lcom/android/systemui/CoreStartable;", "centralSurfaces", "Lcom/android/systemui/statusbar/phone/CentralSurfacesImpl;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@Module
/* compiled from: StartCentralSurfacesModule.kt */
public interface StartCentralSurfacesModule {
    @IntoMap
    @ClassKey(CentralSurfaces.class)
    @Binds
    CoreStartable bindsCentralSurfaces(CentralSurfacesImpl centralSurfacesImpl);
}
