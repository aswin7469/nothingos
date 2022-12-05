package com.android.systemui.log;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: LogMessageImpl.kt */
/* loaded from: classes.dex */
final class LogMessageImplKt$DEFAULT_RENDERER$1 extends Lambda implements Function1<LogMessage, String> {
    public static final LogMessageImplKt$DEFAULT_RENDERER$1 INSTANCE = new LogMessageImplKt$DEFAULT_RENDERER$1();

    LogMessageImplKt$DEFAULT_RENDERER$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$null");
        return Intrinsics.stringPlus("Unknown message: ", logMessage);
    }
}
