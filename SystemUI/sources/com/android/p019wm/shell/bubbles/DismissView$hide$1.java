package com.android.p019wm.shell.bubbles;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo65043d2 = {"<anonymous>", "", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.bubbles.DismissView$hide$1 */
/* compiled from: DismissView.kt */
final class DismissView$hide$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ DismissView this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    DismissView$hide$1(DismissView dismissView) {
        super(0);
        this.this$0 = dismissView;
    }

    public final void invoke() {
        this.this$0.setVisibility(4);
    }
}
