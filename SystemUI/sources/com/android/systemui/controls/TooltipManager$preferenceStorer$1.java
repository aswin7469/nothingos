package com.android.systemui.controls;

import android.content.Context;
import com.android.systemui.Prefs;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: TooltipManager.kt */
/* loaded from: classes.dex */
public final class TooltipManager$preferenceStorer$1 extends Lambda implements Function1<Integer, Unit> {
    final /* synthetic */ Context $context;
    final /* synthetic */ TooltipManager this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TooltipManager$preferenceStorer$1(Context context, TooltipManager tooltipManager) {
        super(1);
        this.$context = context;
        this.this$0 = tooltipManager;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1949invoke(Integer num) {
        invoke(num.intValue());
        return Unit.INSTANCE;
    }

    public final void invoke(int i) {
        String str;
        Context context = this.$context;
        str = this.this$0.preferenceName;
        Prefs.putInt(context, str, i);
    }
}
