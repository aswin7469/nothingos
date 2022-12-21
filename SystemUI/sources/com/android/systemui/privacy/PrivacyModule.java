package com.android.systemui.privacy;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public interface PrivacyModule {
    @Binds
    @IntoSet
    PrivacyItemMonitor bindAppOpsPrivacyItemMonitor(AppOpsPrivacyItemMonitor appOpsPrivacyItemMonitor);
}
