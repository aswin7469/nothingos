package com.android.systemui.controls.p010ui;

import android.service.controls.actions.FloatAction;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo65043d2 = {"<anonymous>", "", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$setValue$1 */
/* compiled from: ControlActionCoordinatorImpl.kt */
final class ControlActionCoordinatorImpl$setValue$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ ControlViewHolder $cvh;
    final /* synthetic */ float $newValue;
    final /* synthetic */ String $templateId;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ControlActionCoordinatorImpl$setValue$1(ControlViewHolder controlViewHolder, String str, float f) {
        super(0);
        this.$cvh = controlViewHolder;
        this.$templateId = str;
        this.$newValue = f;
    }

    public final void invoke() {
        this.$cvh.action(new FloatAction(this.$templateId, this.$newValue));
    }
}
