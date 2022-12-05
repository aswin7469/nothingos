package com.android.systemui.dump;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DumpHandler.kt */
/* loaded from: classes.dex */
public final class DumpHandler$parseArgs$2 extends Lambda implements Function1<String, Integer> {
    public static final DumpHandler$parseArgs$2 INSTANCE = new DumpHandler$parseArgs$2();

    DumpHandler$parseArgs$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Integer mo1949invoke(String str) {
        return Integer.valueOf(invoke2(str));
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final int invoke2(@NotNull String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Integer.parseInt(it);
    }
}
