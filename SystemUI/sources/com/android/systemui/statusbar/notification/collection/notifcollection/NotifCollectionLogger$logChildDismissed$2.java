package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotifCollectionLogger.kt */
/* loaded from: classes.dex */
final class NotifCollectionLogger$logChildDismissed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logChildDismissed$2 INSTANCE = new NotifCollectionLogger$logChildDismissed$2();

    NotifCollectionLogger$logChildDismissed$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return Intrinsics.stringPlus("CHILD DISMISSED (inferred): ", log.getStr1());
    }
}
