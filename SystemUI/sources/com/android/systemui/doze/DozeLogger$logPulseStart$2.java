package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
public final class DozeLogger$logPulseStart$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logPulseStart$2 INSTANCE = new DozeLogger$logPulseStart$2();

    DozeLogger$logPulseStart$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return Intrinsics.stringPlus("Pulse start, reason=", DozeLog.reasonToString(log.getInt1()));
    }
}
