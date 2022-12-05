package com.android.systemui.controls.management;

import com.android.systemui.controls.TooltipManager;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlsFavoritingActivity.kt */
/* loaded from: classes.dex */
public final class ControlsFavoritingActivity$bindViews$2$1 extends Lambda implements Function1<Integer, Unit> {
    final /* synthetic */ ControlsFavoritingActivity this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlsFavoritingActivity$bindViews$2$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        super(1);
        this.this$0 = controlsFavoritingActivity;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1949invoke(Integer num) {
        invoke(num.intValue());
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code restructure failed: missing block: B:2:0x0002, code lost:
        r0 = r0.this$0.mTooltipManager;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void invoke(int i) {
        TooltipManager tooltipManager;
        if (i == 0 || tooltipManager == null) {
            return;
        }
        tooltipManager.hide(true);
    }
}
