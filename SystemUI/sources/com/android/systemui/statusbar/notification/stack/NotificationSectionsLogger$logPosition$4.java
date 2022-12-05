package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationSectionsLogger.kt */
/* loaded from: classes.dex */
final class NotificationSectionsLogger$logPosition$4 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationSectionsLogger$logPosition$4 INSTANCE = new NotificationSectionsLogger$logPosition$4();

    NotificationSectionsLogger$logPosition$4() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return log.getInt1() + ": " + ((Object) log.getStr1());
    }
}
