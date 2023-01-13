package com.android.systemui.controls.p010ui;

import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "Lkotlin/Pair;", "", "it", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.RenderInfoKt$deviceColorMap$1 */
/* compiled from: RenderInfo.kt */
final class RenderInfoKt$deviceColorMap$1 extends Lambda implements Function1<Integer, Pair<? extends Integer, ? extends Integer>> {
    public static final RenderInfoKt$deviceColorMap$1 INSTANCE = new RenderInfoKt$deviceColorMap$1();

    RenderInfoKt$deviceColorMap$1() {
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Number) obj).intValue());
    }

    public final Pair<Integer, Integer> invoke(int i) {
        return new Pair<>(Integer.valueOf((int) C1894R.C1895color.control_foreground), Integer.valueOf((int) C1894R.C1895color.control_enabled_default_background));
    }
}
