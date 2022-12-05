package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ShadeListBuilderLogger.kt */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logParentChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logParentChanged$2 INSTANCE = new ShadeListBuilderLogger$logParentChanged$2();

    ShadeListBuilderLogger$logParentChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        if (log.getStr1() == null && log.getStr2() != null) {
            return "(Build " + log.getInt1() + ")     Parent is {" + ((Object) log.getStr2()) + '}';
        } else if (log.getStr1() != null && log.getStr2() == null) {
            return "(Build " + log.getInt1() + ")     Parent was {" + ((Object) log.getStr1()) + '}';
        } else {
            return "(Build " + log.getInt1() + ")     Reparent: {" + ((Object) log.getStr1()) + "} -> {" + ((Object) log.getStr2()) + '}';
        }
    }
}
