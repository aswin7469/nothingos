package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: PreparationCoordinatorLogger.kt */
/* loaded from: classes.dex */
public final class PreparationCoordinatorLogger$logDelayingGroupRelease$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PreparationCoordinatorLogger$logDelayingGroupRelease$2 INSTANCE = new PreparationCoordinatorLogger$logDelayingGroupRelease$2();

    PreparationCoordinatorLogger$logDelayingGroupRelease$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "Delaying release of group " + ((Object) log.getStr1()) + " because child " + ((Object) log.getStr2()) + " is still inflating";
    }
}
