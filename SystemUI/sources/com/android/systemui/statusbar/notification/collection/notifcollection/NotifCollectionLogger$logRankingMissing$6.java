package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotifCollectionLogger.kt */
/* loaded from: classes.dex */
final class NotifCollectionLogger$logRankingMissing$6 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logRankingMissing$6 INSTANCE = new NotifCollectionLogger$logRankingMissing$6();

    NotifCollectionLogger$logRankingMissing$6() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return Intrinsics.stringPlus("  ", log.getStr1());
    }
}
