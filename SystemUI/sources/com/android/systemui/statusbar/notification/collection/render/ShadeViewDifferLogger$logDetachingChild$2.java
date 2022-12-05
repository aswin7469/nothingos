package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: ShadeViewDifferLogger.kt */
/* loaded from: classes.dex */
final class ShadeViewDifferLogger$logDetachingChild$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeViewDifferLogger$logDetachingChild$2 INSTANCE = new ShadeViewDifferLogger$logDetachingChild$2();

    ShadeViewDifferLogger$logDetachingChild$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "Detach " + ((Object) log.getStr1()) + " isTransfer=" + log.getBool1() + " oldParent=" + ((Object) log.getStr2()) + " newParent=" + ((Object) log.getStr3());
    }
}
