package com.android.systemui.qs.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: QSLogger.kt */
/* loaded from: classes.dex */
final class QSLogger$logTileUpdated$2 extends Lambda implements Function1<LogMessage, String> {
    public static final QSLogger$logTileUpdated$2 INSTANCE = new QSLogger$logTileUpdated$2();

    QSLogger$logTileUpdated$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        String str;
        Intrinsics.checkNotNullParameter(log, "$this$log");
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append((Object) log.getStr1());
        sb.append("] Tile updated. Label=");
        sb.append((Object) log.getStr2());
        sb.append(". State=");
        sb.append(log.getInt1());
        sb.append(". Icon=");
        sb.append((Object) log.getStr3());
        sb.append('.');
        if (log.getBool1()) {
            str = " Activity in/out=" + log.getBool2() + '/' + log.getBool3();
        } else {
            str = "";
        }
        sb.append(str);
        return sb.toString();
    }
}
