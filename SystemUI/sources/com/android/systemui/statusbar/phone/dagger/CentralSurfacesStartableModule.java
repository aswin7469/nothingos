package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import dagger.Module;
import dagger.multibindings.Multibinds;
import java.util.Set;

@Module
interface CentralSurfacesStartableModule {
    @Multibinds
    Set<CentralSurfacesComponent.Startable> multibindStartables();
}
