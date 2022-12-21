package com.android.systemui.statusbar.dagger;

import com.android.systemui.statusbar.notification.dagger.NotificationsModule;
import com.android.systemui.statusbar.notification.row.NotificationRowModule;
import com.android.systemui.statusbar.phone.dagger.StatusBarPhoneModule;
import dagger.Module;

@Module(includes = {StatusBarPhoneModule.class, CentralSurfacesDependenciesModule.class, NotificationsModule.class, NotificationRowModule.class})
public interface CentralSurfacesModule {
}
