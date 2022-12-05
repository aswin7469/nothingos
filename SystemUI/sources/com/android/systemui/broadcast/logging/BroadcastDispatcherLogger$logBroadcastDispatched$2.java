package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: BroadcastDispatcherLogger.kt */
/* loaded from: classes.dex */
final class BroadcastDispatcherLogger$logBroadcastDispatched$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logBroadcastDispatched$2 INSTANCE = new BroadcastDispatcherLogger$logBroadcastDispatched$2();

    BroadcastDispatcherLogger$logBroadcastDispatched$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "Broadcast " + log.getInt1() + " (" + ((Object) log.getStr1()) + ") dispatched to " + ((Object) log.getStr2());
    }
}
