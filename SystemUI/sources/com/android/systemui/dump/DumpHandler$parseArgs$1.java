package com.android.systemui.dump;

import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DumpHandler.kt */
/* loaded from: classes.dex */
public final class DumpHandler$parseArgs$1 extends Lambda implements Function1<String, String> {
    public static final DumpHandler$parseArgs$1 INSTANCE = new DumpHandler$parseArgs$1();

    DumpHandler$parseArgs$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull String it) {
        String[] strArr;
        boolean contains;
        Intrinsics.checkNotNullParameter(it, "it");
        strArr = DumpHandlerKt.PRIORITY_OPTIONS;
        contains = ArraysKt___ArraysKt.contains(strArr, it);
        if (contains) {
            return it;
        }
        throw new IllegalArgumentException();
    }
}
