package com.android.systemui.controls.management;

import com.android.systemui.controls.TooltipManager;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo64987d2 = {"<anonymous>", "", "it", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsFavoritingActivity.kt */
final class ControlsFavoritingActivity$bindViews$2$1 extends Lambda implements Function1<Integer, Unit> {
    final /* synthetic */ ControlsFavoritingActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ControlsFavoritingActivity$bindViews$2$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        super(1);
        this.this$0 = controlsFavoritingActivity;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke(((Number) obj).intValue());
        return Unit.INSTANCE;
    }

    public final void invoke(int i) {
        TooltipManager access$getMTooltipManager$p;
        if (i != 0 && (access$getMTooltipManager$p = this.this$0.mTooltipManager) != null) {
            access$getMTooltipManager$p.hide(true);
        }
    }
}
