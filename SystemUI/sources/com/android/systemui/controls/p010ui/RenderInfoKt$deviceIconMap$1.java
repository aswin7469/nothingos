package com.android.systemui.controls.p010ui;

import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, mo65043d2 = {"<anonymous>", "", "it", "invoke", "(I)Ljava/lang/Integer;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.RenderInfoKt$deviceIconMap$1 */
/* compiled from: RenderInfo.kt */
final class RenderInfoKt$deviceIconMap$1 extends Lambda implements Function1<Integer, Integer> {
    public static final RenderInfoKt$deviceIconMap$1 INSTANCE = new RenderInfoKt$deviceIconMap$1();

    RenderInfoKt$deviceIconMap$1() {
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Number) obj).intValue());
    }

    public final Integer invoke(int i) {
        return Integer.valueOf((int) C1894R.C1896drawable.ic_device_unknown);
    }
}
