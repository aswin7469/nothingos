package com.android.systemui.controls.p010ui;

import android.app.PendingIntent;
import android.service.controls.Control;
import android.service.controls.actions.CommandAction;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$touch$1 */
/* compiled from: ControlActionCoordinatorImpl.kt */
final class ControlActionCoordinatorImpl$touch$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ Control $control;
    final /* synthetic */ ControlViewHolder $cvh;
    final /* synthetic */ String $templateId;
    final /* synthetic */ ControlActionCoordinatorImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ControlActionCoordinatorImpl$touch$1(ControlViewHolder controlViewHolder, ControlActionCoordinatorImpl controlActionCoordinatorImpl, Control control, String str) {
        super(0);
        this.$cvh = controlViewHolder;
        this.this$0 = controlActionCoordinatorImpl;
        this.$control = control;
        this.$templateId = str;
    }

    public final void invoke() {
        this.$cvh.getLayout().performHapticFeedback(6);
        if (this.$cvh.usePanel()) {
            ControlActionCoordinatorImpl controlActionCoordinatorImpl = this.this$0;
            ControlViewHolder controlViewHolder = this.$cvh;
            PendingIntent appIntent = this.$control.getAppIntent();
            Intrinsics.checkNotNullExpressionValue(appIntent, "control.getAppIntent()");
            controlActionCoordinatorImpl.showDetail(controlViewHolder, appIntent);
            return;
        }
        this.$cvh.action(new CommandAction(this.$templateId));
    }
}
