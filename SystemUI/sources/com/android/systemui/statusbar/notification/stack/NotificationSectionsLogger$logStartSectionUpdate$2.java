package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationSectionsLogger.kt */
/* loaded from: classes.dex */
final class NotificationSectionsLogger$logStartSectionUpdate$2 extends Lambda implements Function1<LogMessage, String> {
    final /* synthetic */ String $reason;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationSectionsLogger$logStartSectionUpdate$2(String str) {
        super(1);
        this.$reason = str;
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return Intrinsics.stringPlus("Updating section boundaries: ", this.$reason);
    }
}
