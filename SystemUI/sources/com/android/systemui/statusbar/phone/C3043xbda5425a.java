package com.android.systemui.statusbar.phone;

import kotlin.Metadata;
import kotlin.jvm.internal.MutablePropertyReference0Impl;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.phone.NotificationsQSContainerController$updateResources$footerOffsetChanged$1 */
/* compiled from: NotificationsQSContainerController.kt */
/* synthetic */ class C3043xbda5425a extends MutablePropertyReference0Impl {
    C3043xbda5425a(Object obj) {
        super(obj, NotificationsQSContainerController.class, "footerActionsOffset", "getFooterActionsOffset()I", 0);
    }

    public Object get() {
        return Integer.valueOf(((NotificationsQSContainerController) this.receiver).footerActionsOffset);
    }

    public void set(Object obj) {
        ((NotificationsQSContainerController) this.receiver).footerActionsOffset = ((Number) obj).intValue();
    }
}
