package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.Utils;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u000b\u001a\u00020\fH\u0007J\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\u0012R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0014"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/NotificationSectionsFeatureManager;", "", "proxy", "Lcom/android/systemui/util/DeviceConfigProxy;", "context", "Landroid/content/Context;", "(Lcom/android/systemui/util/DeviceConfigProxy;Landroid/content/Context;)V", "getContext", "()Landroid/content/Context;", "getProxy", "()Lcom/android/systemui/util/DeviceConfigProxy;", "clearCache", "", "getNotificationBuckets", "", "getNumberOfBuckets", "", "isFilteringEnabled", "", "isMediaControlsEnabled", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationSectionsFeatureManager.kt */
public final class NotificationSectionsFeatureManager {
    private final Context context;
    private final DeviceConfigProxy proxy;

    @Inject
    public NotificationSectionsFeatureManager(DeviceConfigProxy deviceConfigProxy, Context context2) {
        Intrinsics.checkNotNullParameter(deviceConfigProxy, "proxy");
        Intrinsics.checkNotNullParameter(context2, "context");
        this.proxy = deviceConfigProxy;
        this.context = context2;
    }

    public final DeviceConfigProxy getProxy() {
        return this.proxy;
    }

    public final Context getContext() {
        return this.context;
    }

    public final boolean isFilteringEnabled() {
        return NotificationSectionsFeatureManagerKt.usePeopleFiltering(this.proxy);
    }

    public final boolean isMediaControlsEnabled() {
        return Utils.useQsMediaPlayer(this.context);
    }

    public final int[] getNotificationBuckets() {
        if (isFilteringEnabled() && isMediaControlsEnabled()) {
            return new int[]{2, 3, 1, 4, 5, 6};
        }
        if (!isFilteringEnabled() && isMediaControlsEnabled()) {
            return new int[]{2, 3, 1, 5, 6};
        }
        if (!isFilteringEnabled() || isMediaControlsEnabled()) {
            return new int[]{5, 6};
        }
        return new int[]{2, 3, 4, 5, 6};
    }

    public final int getNumberOfBuckets() {
        return getNotificationBuckets().length;
    }

    public final void clearCache() {
        NotificationSectionsFeatureManagerKt.sUsePeopleFiltering = null;
    }
}
