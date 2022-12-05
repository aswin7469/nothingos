package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ShadeListBuilderLogger.kt */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logPromoterInvalidated$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logPromoterInvalidated$2 INSTANCE = new ShadeListBuilderLogger$logPromoterInvalidated$2();

    ShadeListBuilderLogger$logPromoterInvalidated$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "NotifPromoter \"" + ((Object) log.getStr1()) + "\" invalidated; pipeline state is " + log.getInt1();
    }
}
