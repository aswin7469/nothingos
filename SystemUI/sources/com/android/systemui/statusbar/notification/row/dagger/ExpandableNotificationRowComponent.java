package com.android.systemui.statusbar.notification.row.dagger;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Binds;
import dagger.BindsInstance;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

@NotificationRowScope
@Subcomponent(modules = {ActivatableNotificationViewModule.class, ExpandableNotificationRowModule.class, RemoteInputViewModule.class})
public interface ExpandableNotificationRowComponent {

    @Subcomponent.Builder
    public interface Builder {
        ExpandableNotificationRowComponent build();

        @BindsInstance
        Builder expandableNotificationRow(ExpandableNotificationRow expandableNotificationRow);

        @BindsInstance
        Builder listContainer(NotificationListContainer notificationListContainer);

        @BindsInstance
        Builder notificationEntry(NotificationEntry notificationEntry);

        @BindsInstance
        Builder onExpandClickListener(ExpandableNotificationRow.OnExpandClickListener onExpandClickListener);
    }

    @NotificationRowScope
    ExpandableNotificationRowController getExpandableNotificationRowController();

    @Module
    public static abstract class ExpandableNotificationRowModule {
        /* access modifiers changed from: package-private */
        @Binds
        public abstract ActivatableNotificationView bindExpandableView(ExpandableNotificationRow expandableNotificationRow);

        @Provides
        static StatusBarNotification provideStatusBarNotification(NotificationEntry notificationEntry) {
            return notificationEntry.getSbn();
        }

        @NotificationKey
        @Provides
        static String provideNotificationKey(StatusBarNotification statusBarNotification) {
            return statusBarNotification.getKey();
        }

        @AppName
        @Provides
        static String provideAppName(Context context, StatusBarNotification statusBarNotification) {
            PackageManager packageManagerForUser = CentralSurfaces.getPackageManagerForUser(context, statusBarNotification.getUser().getIdentifier());
            String packageName = statusBarNotification.getPackageName();
            try {
                ApplicationInfo applicationInfo = packageManagerForUser.getApplicationInfo(packageName, 8704);
                if (applicationInfo != null) {
                    return String.valueOf((Object) packageManagerForUser.getApplicationLabel(applicationInfo));
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
            return packageName;
        }
    }
}
