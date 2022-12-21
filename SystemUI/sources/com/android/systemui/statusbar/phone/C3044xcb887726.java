package com.android.systemui.statusbar.phone;

import kotlin.Metadata;
import kotlin.jvm.internal.MutablePropertyReference0Impl;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.phone.NotificationsQSContainerController$updateResources$scrimMarginChanged$1 */
/* compiled from: NotificationsQSContainerController.kt */
/* synthetic */ class C3044xcb887726 extends MutablePropertyReference0Impl {
    C3044xcb887726(Object obj) {
        super(obj, NotificationsQSContainerController.class, "scrimShadeBottomMargin", "getScrimShadeBottomMargin()I", 0);
    }

    public Object get() {
        return Integer.valueOf(((NotificationsQSContainerController) this.receiver).scrimShadeBottomMargin);
    }

    public void set(Object obj) {
        ((NotificationsQSContainerController) this.receiver).scrimShadeBottomMargin = ((Number) obj).intValue();
    }
}
