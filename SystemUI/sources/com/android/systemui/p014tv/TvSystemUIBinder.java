package com.android.systemui.p014tv;

import com.android.systemui.dagger.GlobalRootComponent;
import dagger.Binds;
import dagger.Module;

@Module
/* renamed from: com.android.systemui.tv.TvSystemUIBinder */
interface TvSystemUIBinder {
    @Binds
    GlobalRootComponent bindGlobalRootComponent(TvGlobalRootComponent tvGlobalRootComponent);
}
