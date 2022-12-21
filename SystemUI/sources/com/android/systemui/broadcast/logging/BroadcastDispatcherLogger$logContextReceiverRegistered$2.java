package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.text.StringsKt;

@Metadata(mo64986d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n¢\u0006\u0002\b\u0003"}, mo64987d2 = {"<anonymous>", "", "Lcom/android/systemui/log/LogMessage;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BroadcastDispatcherLogger.kt */
final class BroadcastDispatcherLogger$logContextReceiverRegistered$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logContextReceiverRegistered$2 INSTANCE = new BroadcastDispatcherLogger$logContextReceiverRegistered$2();

    BroadcastDispatcherLogger$logContextReceiverRegistered$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return StringsKt.trimIndent("\n                Receiver registered with Context for user " + logMessage.getInt1() + ". Flags=" + logMessage.getStr2() + "\n                " + logMessage.getStr1() + "\n            ");
    }
}
