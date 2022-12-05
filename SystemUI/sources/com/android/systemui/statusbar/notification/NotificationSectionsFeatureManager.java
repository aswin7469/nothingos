package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.Utils;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationSectionsFeatureManager.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsFeatureManager {
    @NotNull
    private final Context context;
    @NotNull
    private final DeviceConfigProxy proxy;

    public NotificationSectionsFeatureManager(@NotNull DeviceConfigProxy proxy, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(proxy, "proxy");
        Intrinsics.checkNotNullParameter(context, "context");
        this.proxy = proxy;
        this.context = context;
    }

    public final boolean isFilteringEnabled() {
        boolean usePeopleFiltering;
        usePeopleFiltering = NotificationSectionsFeatureManagerKt.usePeopleFiltering(this.proxy);
        return usePeopleFiltering;
    }

    public final boolean isMediaControlsEnabled() {
        return Utils.useQsMediaPlayer(this.context);
    }

    @NotNull
    public final int[] getNotificationBuckets() {
        return (!isFilteringEnabled() || !isMediaControlsEnabled()) ? (isFilteringEnabled() || !isMediaControlsEnabled()) ? (!isFilteringEnabled() || isMediaControlsEnabled()) ? new int[]{5, 6} : new int[]{2, 3, 4, 5, 6} : new int[]{2, 3, 1, 5, 6} : new int[]{2, 3, 1, 4, 5, 6};
    }

    public final int getNumberOfBuckets() {
        return getNotificationBuckets().length;
    }

    @VisibleForTesting
    public final void clearCache() {
        NotificationSectionsFeatureManagerKt.sUsePeopleFiltering = null;
    }
}
