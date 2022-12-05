package com.android.systemui.statusbar;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ActionClickLogger.kt */
/* loaded from: classes.dex */
public final class ActionClickLogger$logInitialClick$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ActionClickLogger$logInitialClick$2 INSTANCE = new ActionClickLogger$logInitialClick$2();

    ActionClickLogger$logInitialClick$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "ACTION CLICK " + ((Object) log.getStr1()) + " (channel=" + ((Object) log.getStr2()) + ") for pending intent " + ((Object) log.getStr3());
    }
}
