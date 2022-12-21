package com.android.p019wm.shell.pip.p020tv;

import android.graphics.Rect;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo64987d2 = {"<anonymous>", "", "it", "Landroid/graphics/Rect;", "invoke", "(Landroid/graphics/Rect;)Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$findFreeMovePosition$2 */
/* compiled from: TvPipKeepClearAlgorithm.kt */
final class TvPipKeepClearAlgorithm$findFreeMovePosition$2 extends Lambda implements Function1<Rect, Boolean> {
    final /* synthetic */ int $minLeft;
    final /* synthetic */ TvPipKeepClearAlgorithm this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    TvPipKeepClearAlgorithm$findFreeMovePosition$2(TvPipKeepClearAlgorithm tvPipKeepClearAlgorithm, int i) {
        super(1);
        this.this$0 = tvPipKeepClearAlgorithm;
        this.$minLeft = i;
    }

    public final Boolean invoke(Rect rect) {
        Intrinsics.checkNotNullParameter(rect, "it");
        return Boolean.valueOf(rect.left - this.this$0.getPipAreaPadding() > this.$minLeft);
    }
}
