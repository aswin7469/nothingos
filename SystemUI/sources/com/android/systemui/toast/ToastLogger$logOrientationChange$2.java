package com.android.systemui.toast;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: ToastLogger.kt */
/* loaded from: classes2.dex */
final class ToastLogger$logOrientationChange$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ToastLogger$logOrientationChange$2 INSTANCE = new ToastLogger$logOrientationChange$2();

    ToastLogger$logOrientationChange$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "Orientation change for toast. msg='" + ((Object) log.getStr1()) + "' isPortrait=" + log.getBool1();
    }
}
