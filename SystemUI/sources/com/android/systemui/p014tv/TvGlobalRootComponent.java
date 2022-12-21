package com.android.systemui.p014tv;

import com.android.systemui.dagger.GlobalModule;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.p014tv.TvSysUIComponent;
import com.android.systemui.p014tv.TvWMComponent;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {GlobalModule.class})
/* renamed from: com.android.systemui.tv.TvGlobalRootComponent */
public interface TvGlobalRootComponent extends GlobalRootComponent {

    @Component.Builder
    /* renamed from: com.android.systemui.tv.TvGlobalRootComponent$Builder */
    public interface Builder extends GlobalRootComponent.Builder {
        TvGlobalRootComponent build();
    }

    TvSysUIComponent.Builder getSysUIComponent();

    TvWMComponent.Builder getWMComponentBuilder();
}
