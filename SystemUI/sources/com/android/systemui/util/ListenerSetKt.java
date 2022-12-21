package com.android.systemui.util;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0016\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003¨\u0006\u0004"}, mo64987d2 = {"isNotEmpty", "", "T", "Lcom/android/systemui/util/ListenerSet;", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ListenerSet.kt */
public final class ListenerSetKt {
    public static final <T> boolean isNotEmpty(ListenerSet<T> listenerSet) {
        Intrinsics.checkNotNullParameter(listenerSet, "<this>");
        return !listenerSet.isEmpty();
    }
}
