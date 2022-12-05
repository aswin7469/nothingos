package com.android.systemui.controls.ui;

import android.app.PendingIntent;
import android.service.controls.Control;
import android.service.controls.actions.CommandAction;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ControlActionCoordinatorImpl.kt */
/* loaded from: classes.dex */
final class ControlActionCoordinatorImpl$touch$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ Control $control;
    final /* synthetic */ ControlViewHolder $cvh;
    final /* synthetic */ String $templateId;
    final /* synthetic */ ControlActionCoordinatorImpl this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlActionCoordinatorImpl$touch$1(ControlViewHolder controlViewHolder, ControlActionCoordinatorImpl controlActionCoordinatorImpl, Control control, String str) {
        super(0);
        this.$cvh = controlViewHolder;
        this.this$0 = controlActionCoordinatorImpl;
        this.$control = control;
        this.$templateId = str;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1951invoke() {
        mo1951invoke();
        return Unit.INSTANCE;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final void mo1951invoke() {
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
