package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: SharedCoordinatorLogger.kt */
/* loaded from: classes.dex */
final class SharedCoordinatorLogger$logUserOrProfileChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final SharedCoordinatorLogger$logUserOrProfileChanged$2 INSTANCE = new SharedCoordinatorLogger$logUserOrProfileChanged$2();

    SharedCoordinatorLogger$logUserOrProfileChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "Current user or profiles changed. Current user is " + log.getInt1() + "; profiles are " + ((Object) log.getStr1());
    }
}
