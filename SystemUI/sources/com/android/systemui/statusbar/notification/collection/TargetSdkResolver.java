package com.android.systemui.statusbar.notification.collection;

import android.content.Context;
import android.content.pm.PackageManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.phone.StatusBar;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: TargetSdkResolver.kt */
/* loaded from: classes.dex */
public final class TargetSdkResolver {
    @NotNull
    private final String TAG = "TargetSdkResolver";
    @NotNull
    private final Context context;

    public TargetSdkResolver(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    public final void initialize(@NotNull CommonNotifCollection collection) {
        Intrinsics.checkNotNullParameter(collection, "collection");
        collection.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.collection.TargetSdkResolver$initialize$1
            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryBind(@NotNull NotificationEntry entry, @NotNull StatusBarNotification sbn) {
                int resolveNotificationSdk;
                Intrinsics.checkNotNullParameter(entry, "entry");
                Intrinsics.checkNotNullParameter(sbn, "sbn");
                resolveNotificationSdk = TargetSdkResolver.this.resolveNotificationSdk(sbn);
                entry.targetSdk = resolveNotificationSdk;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int resolveNotificationSdk(StatusBarNotification statusBarNotification) {
        try {
            return StatusBar.getPackageManagerForUser(this.context, statusBarNotification.getUser().getIdentifier()).getApplicationInfo(statusBarNotification.getPackageName(), 0).targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(this.TAG, Intrinsics.stringPlus("Failed looking up ApplicationInfo for ", statusBarNotification.getPackageName()), e);
            return 0;
        }
    }
}
