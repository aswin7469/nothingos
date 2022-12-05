package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.text.StringsKt__IndentKt;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: BroadcastDispatcherLogger.kt */
/* loaded from: classes.dex */
public final class BroadcastDispatcherLogger$logContextReceiverRegistered$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logContextReceiverRegistered$2 INSTANCE = new BroadcastDispatcherLogger$logContextReceiverRegistered$2();

    BroadcastDispatcherLogger$logContextReceiverRegistered$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        String trimIndent;
        Intrinsics.checkNotNullParameter(log, "$this$log");
        trimIndent = StringsKt__IndentKt.trimIndent("\n                Receiver registered with Context for user " + log.getInt1() + ".\n                " + ((Object) log.getStr1()) + "\n            ");
        return trimIndent;
    }
}
