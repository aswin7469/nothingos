package com.android.systemui.privacy.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: PrivacyLogger.kt */
/* loaded from: classes.dex */
final class PrivacyLogger$logUnfilteredPermGroupUsage$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logUnfilteredPermGroupUsage$2 INSTANCE = new PrivacyLogger$logUnfilteredPermGroupUsage$2();

    PrivacyLogger$logUnfilteredPermGroupUsage$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull LogMessage log) {
        Intrinsics.checkNotNullParameter(log, "$this$log");
        return Intrinsics.stringPlus("Perm group usage: ", log.getStr1());
    }
}
