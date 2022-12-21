package com.android.systemui.statusbar.phone;

import kotlin.Metadata;
import kotlin.reflect.KMutableProperty0;

@Metadata(mo64986d1 = {"\u0000\u001a\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u001a\u001a\u0010\u0004\u001a\u00020\u0005*\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\u0007H\u0002\"\u0016\u0010\u0000\u001a\u00020\u00018\u0000XT¢\u0006\b\n\u0000\u0012\u0004\b\u0002\u0010\u0003¨\u0006\t"}, mo64987d2 = {"INSET_DEBOUNCE_MILLIS", "", "getINSET_DEBOUNCE_MILLIS$annotations", "()V", "setAndReportChange", "", "Lkotlin/reflect/KMutableProperty0;", "", "newValue", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationsQSContainerController.kt */
public final class NotificationsQSContainerControllerKt {
    public static final long INSET_DEBOUNCE_MILLIS = 500;

    public static /* synthetic */ void getINSET_DEBOUNCE_MILLIS$annotations() {
    }

    /* access modifiers changed from: private */
    public static final boolean setAndReportChange(KMutableProperty0<Integer> kMutableProperty0, int i) {
        int intValue = kMutableProperty0.get().intValue();
        kMutableProperty0.set(Integer.valueOf(i));
        return intValue != i;
    }
}
