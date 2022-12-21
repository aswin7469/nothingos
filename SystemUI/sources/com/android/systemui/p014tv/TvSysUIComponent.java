package com.android.systemui.p014tv;

import com.android.systemui.dagger.DefaultComponentBinder;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.SystemUIModule;
import com.android.systemui.keyguard.dagger.KeyguardModule;
import com.android.systemui.recents.RecentsModule;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule;
import com.android.systemui.statusbar.notification.row.NotificationRowModule;
import dagger.Subcomponent;

@SysUISingleton
@Subcomponent(modules = {CentralSurfacesDependenciesModule.class, DefaultComponentBinder.class, DependencyProvider.class, KeyguardModule.class, NotificationRowModule.class, NotificationsModule.class, RecentsModule.class, SystemUIModule.class, TvSystemUIBinder.class, TVSystemUICoreStartableModule.class, TvSystemUIModule.class})
/* renamed from: com.android.systemui.tv.TvSysUIComponent */
public interface TvSysUIComponent extends SysUIComponent {

    @Subcomponent.Builder
    /* renamed from: com.android.systemui.tv.TvSysUIComponent$Builder */
    public interface Builder extends SysUIComponent.Builder {
        TvSysUIComponent build();
    }
}
