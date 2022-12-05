package com.android.systemui.controls.ui;

import com.android.systemui.R$drawable;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: RenderInfo.kt */
/* loaded from: classes.dex */
final class RenderInfoKt$deviceIconMap$1 extends Lambda implements Function1<Integer, Integer> {
    public static final RenderInfoKt$deviceIconMap$1 INSTANCE = new RenderInfoKt$deviceIconMap$1();

    RenderInfoKt$deviceIconMap$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Integer mo1949invoke(Integer num) {
        return Integer.valueOf(invoke(num.intValue()));
    }

    public final int invoke(int i) {
        return R$drawable.ic_device_unknown;
    }
}
