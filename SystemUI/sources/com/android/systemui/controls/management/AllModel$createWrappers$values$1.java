package com.android.systemui.controls.management;

import com.android.systemui.controls.ControlStatus;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: AllModel.kt */
/* loaded from: classes.dex */
final class AllModel$createWrappers$values$1 extends Lambda implements Function1<ControlStatus, ControlStatusWrapper> {
    public static final AllModel$createWrappers$values$1 INSTANCE = new AllModel$createWrappers$values$1();

    AllModel$createWrappers$values$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final ControlStatusWrapper mo1949invoke(@NotNull ControlStatus it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return new ControlStatusWrapper(it);
    }
}
