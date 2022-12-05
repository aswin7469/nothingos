package com.android.systemui.qs.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: QSLogger.kt */
/* loaded from: classes.dex */
public final class QSLogger$logTileDestroyed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final QSLogger$logTileDestroyed$2 INSTANCE = new QSLogger$logTileDestroyed$2();

    QSLogger$logTileDestroyed$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return '[' + ((Object) log.getStr1()) + "] Tile destroyed. Reason: " + ((Object) log.getStr2());
    }
}
