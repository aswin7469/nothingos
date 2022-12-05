package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotifCollectionLogger.kt */
/* loaded from: classes.dex */
final class NotifCollectionLogger$logLifetimeExtended$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logLifetimeExtended$2 INSTANCE = new NotifCollectionLogger$logLifetimeExtended$2();

    NotifCollectionLogger$logLifetimeExtended$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "LIFETIME EXTENDED: " + ((Object) log.getStr1()) + " by " + ((Object) log.getStr2());
    }
}
