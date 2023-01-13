package com.android.systemui.statusbar.notification;

import com.android.systemui.util.DeviceConfigProxy;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0005H\u0002\"\u0012\u0010\u0000\u001a\u0004\u0018\u00010\u0001X\u000e¢\u0006\u0004\n\u0002\u0010\u0002¨\u0006\u0006"}, mo65043d2 = {"sUsePeopleFiltering", "", "Ljava/lang/Boolean;", "usePeopleFiltering", "proxy", "Lcom/android/systemui/util/DeviceConfigProxy;", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationSectionsFeatureManager.kt */
public final class NotificationSectionsFeatureManagerKt {
    /* access modifiers changed from: private */
    public static Boolean sUsePeopleFiltering;

    /* access modifiers changed from: private */
    public static final boolean usePeopleFiltering(DeviceConfigProxy deviceConfigProxy) {
        if (sUsePeopleFiltering == null) {
            sUsePeopleFiltering = Boolean.valueOf(deviceConfigProxy.getBoolean("systemui", "notifications_use_people_filtering", true));
        }
        Boolean bool = sUsePeopleFiltering;
        Intrinsics.checkNotNull(bool);
        return bool.booleanValue();
    }
}
