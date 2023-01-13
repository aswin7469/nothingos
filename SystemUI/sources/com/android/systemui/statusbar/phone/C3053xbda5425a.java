package com.android.systemui.statusbar.phone;

import kotlin.Metadata;
import kotlin.jvm.internal.MutablePropertyReference0Impl;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.phone.NotificationsQSContainerController$updateResources$footerOffsetChanged$1 */
/* compiled from: NotificationsQSContainerController.kt */
/* synthetic */ class C3053xbda5425a extends MutablePropertyReference0Impl {
    C3053xbda5425a(Object obj) {
        super(obj, NotificationsQSContainerController.class, "footerActionsOffset", "getFooterActionsOffset()I", 0);
    }

    public Object get() {
        return Integer.valueOf(((NotificationsQSContainerController) this.receiver).footerActionsOffset);
    }

    public void set(Object obj) {
        ((NotificationsQSContainerController) this.receiver).footerActionsOffset = ((Number) obj).intValue();
    }
}
