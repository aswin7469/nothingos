package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: BroadcastDispatcherLogger.kt */
/* loaded from: classes.dex */
final class BroadcastDispatcherLogger$logReceiverUnregistered$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logReceiverUnregistered$2 INSTANCE = new BroadcastDispatcherLogger$logReceiverUnregistered$2();

    BroadcastDispatcherLogger$logReceiverUnregistered$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "Receiver " + ((Object) log.getStr1()) + " unregistered for user " + log.getInt1();
    }
}
