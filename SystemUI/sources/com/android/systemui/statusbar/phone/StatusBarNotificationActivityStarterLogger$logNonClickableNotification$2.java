package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
/* loaded from: classes.dex */
final class StatusBarNotificationActivityStarterLogger$logNonClickableNotification$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StatusBarNotificationActivityStarterLogger$logNonClickableNotification$2 INSTANCE = new StatusBarNotificationActivityStarterLogger$logNonClickableNotification$2();

    StatusBarNotificationActivityStarterLogger$logNonClickableNotification$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return Intrinsics.stringPlus("onNotificationClicked called for non-clickable notification! ", log.getStr1());
    }
}
