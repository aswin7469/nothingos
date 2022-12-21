package com.android.systemui.appops.dagger;

import com.android.systemui.appops.AppOpsController;
import com.android.systemui.appops.AppOpsControllerImpl;
import dagger.Binds;
import dagger.Module;

@Module
public interface AppOpsModule {
    @Binds
    AppOpsController provideAppOpsController(AppOpsControllerImpl appOpsControllerImpl);
}
