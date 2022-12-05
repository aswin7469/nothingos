package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import java.util.Date;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
public final class DozeLogger$logTimeTickScheduled$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logTimeTickScheduled$2 INSTANCE = new DozeLogger$logTimeTickScheduled$2();

    DozeLogger$logTimeTickScheduled$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "Time tick scheduledAt=" + ((Object) DozeLoggerKt.getDATE_FORMAT().format(new Date(log.getLong1()))) + " triggerAt=" + ((Object) DozeLoggerKt.getDATE_FORMAT().format(new Date(log.getLong2())));
    }
}
