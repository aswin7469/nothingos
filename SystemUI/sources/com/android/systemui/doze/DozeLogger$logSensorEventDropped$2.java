package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
final class DozeLogger$logSensorEventDropped$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logSensorEventDropped$2 INSTANCE = new DozeLogger$logSensorEventDropped$2();

    DozeLogger$logSensorEventDropped$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return "SensorEvent [" + log.getInt1() + "] dropped, reason=" + ((Object) log.getStr1());
    }
}
