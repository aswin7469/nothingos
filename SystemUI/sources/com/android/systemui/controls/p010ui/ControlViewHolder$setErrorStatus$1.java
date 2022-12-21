package com.android.systemui.controls.p010ui;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlViewHolder$setErrorStatus$1 */
/* compiled from: ControlViewHolder.kt */
final class ControlViewHolder$setErrorStatus$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ String $text;
    final /* synthetic */ ControlViewHolder this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ControlViewHolder$setErrorStatus$1(ControlViewHolder controlViewHolder, String str) {
        super(0);
        this.this$0 = controlViewHolder;
        this.$text = str;
    }

    public final void invoke() {
        ControlViewHolder controlViewHolder = this.this$0;
        String str = this.$text;
        Intrinsics.checkNotNullExpressionValue(str, "text");
        controlViewHolder.setStatusText(str, true);
    }
}
