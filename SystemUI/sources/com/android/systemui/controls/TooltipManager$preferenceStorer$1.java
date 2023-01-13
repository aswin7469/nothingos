package com.android.systemui.controls;

import android.content.Context;
import com.android.systemui.Prefs;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "", "num", "", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: TooltipManager.kt */
final class TooltipManager$preferenceStorer$1 extends Lambda implements Function1<Integer, Unit> {
    final /* synthetic */ Context $context;
    final /* synthetic */ TooltipManager this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    TooltipManager$preferenceStorer$1(Context context, TooltipManager tooltipManager) {
        super(1);
        this.$context = context;
        this.this$0 = tooltipManager;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke(((Number) obj).intValue());
        return Unit.INSTANCE;
    }

    public final void invoke(int i) {
        Prefs.putInt(this.$context, this.this$0.preferenceName, i);
    }
}
