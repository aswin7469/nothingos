package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationClickerLogger.kt */
/* loaded from: classes.dex */
final class NotificationClickerLogger$logOnClick$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationClickerLogger$logOnClick$2 INSTANCE = new NotificationClickerLogger$logOnClick$2();

    NotificationClickerLogger$logOnClick$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "CLICK " + ((Object) log.getStr1()) + " (channel=" + ((Object) log.getStr2()) + ')';
    }
}
