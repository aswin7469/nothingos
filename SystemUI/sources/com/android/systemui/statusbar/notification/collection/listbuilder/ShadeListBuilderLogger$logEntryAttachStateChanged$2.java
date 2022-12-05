package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ShadeListBuilderLogger.kt */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logEntryAttachStateChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logEntryAttachStateChanged$2 INSTANCE = new ShadeListBuilderLogger$logEntryAttachStateChanged$2();

    ShadeListBuilderLogger$logEntryAttachStateChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        String str;
        Intrinsics.checkNotNullParameter(log, "$this$log");
        if (log.getStr2() == null && log.getStr3() != null) {
            str = "ATTACHED";
        } else if (log.getStr2() == null || log.getStr3() != null) {
            str = (log.getStr2() == null && log.getStr3() == null) ? "MODIFIED (DETACHED)" : "MODIFIED (ATTACHED)";
        } else {
            str = "DETACHED";
        }
        return "(Build " + log.getInt1() + ") " + str + " {" + ((Object) log.getStr1()) + '}';
    }
}
