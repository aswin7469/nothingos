package com.android.systemui.controls.ui;

import com.android.systemui.R$color;
import kotlin.Pair;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: RenderInfo.kt */
/* loaded from: classes.dex */
final class RenderInfoKt$deviceColorMap$1 extends Lambda implements Function1<Integer, Pair<? extends Integer, ? extends Integer>> {
    public static final RenderInfoKt$deviceColorMap$1 INSTANCE = new RenderInfoKt$deviceColorMap$1();

    RenderInfoKt$deviceColorMap$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Pair<? extends Integer, ? extends Integer> mo1949invoke(Integer num) {
        return invoke(num.intValue());
    }

    @NotNull
    public final Pair<Integer, Integer> invoke(int i) {
        return new Pair<>(Integer.valueOf(R$color.control_foreground), Integer.valueOf(R$color.control_enabled_default_background));
    }
}
