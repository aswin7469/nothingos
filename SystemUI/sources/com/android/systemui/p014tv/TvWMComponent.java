package com.android.systemui.p014tv;

import com.android.p019wm.shell.dagger.TvWMShellModule;
import com.android.p019wm.shell.dagger.WMSingleton;
import com.android.systemui.dagger.WMComponent;
import dagger.Subcomponent;

@Subcomponent(modules = {TvWMShellModule.class})
@WMSingleton
/* renamed from: com.android.systemui.tv.TvWMComponent */
public interface TvWMComponent extends WMComponent {

    @Subcomponent.Builder
    /* renamed from: com.android.systemui.tv.TvWMComponent$Builder */
    public interface Builder extends WMComponent.Builder {
        TvWMComponent build();
    }
}
