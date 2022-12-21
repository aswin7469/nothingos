package com.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.WMComponent;
import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {GlobalModule.class})
public interface GlobalRootComponent {

    @Component.Builder
    public interface Builder {
        GlobalRootComponent build();

        @BindsInstance
        Builder context(Context context);
    }

    SysUIComponent.Builder getSysUIComponent();

    WMComponent.Builder getWMComponentBuilder();
}
