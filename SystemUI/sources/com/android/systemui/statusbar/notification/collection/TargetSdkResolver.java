package com.android.systemui.statusbar.notification.collection;

import android.content.Context;
import android.content.pm.PackageManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/TargetSdkResolver;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "TAG", "", "initialize", "", "collection", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;", "resolveNotificationSdk", "", "sbn", "Landroid/service/notification/StatusBarNotification;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: TargetSdkResolver.kt */
public final class TargetSdkResolver {
    private final String TAG = "TargetSdkResolver";
    private final Context context;

    @Inject
    public TargetSdkResolver(Context context2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        this.context = context2;
    }

    public final void initialize(CommonNotifCollection commonNotifCollection) {
        Intrinsics.checkNotNullParameter(commonNotifCollection, "collection");
        commonNotifCollection.addCollectionListener(new TargetSdkResolver$initialize$1(this));
    }

    /* access modifiers changed from: private */
    public final int resolveNotificationSdk(StatusBarNotification statusBarNotification) {
        try {
            return CentralSurfaces.getPackageManagerForUser(this.context, statusBarNotification.getUser().getIdentifier()).getApplicationInfo(statusBarNotification.getPackageName(), 0).targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(this.TAG, "Failed looking up ApplicationInfo for " + statusBarNotification.getPackageName(), e);
            return 0;
        }
    }
}
