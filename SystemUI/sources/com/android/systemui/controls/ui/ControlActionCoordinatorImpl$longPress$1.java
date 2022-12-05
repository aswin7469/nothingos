package com.android.systemui.controls.ui;

import android.app.PendingIntent;
import android.service.controls.Control;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ControlActionCoordinatorImpl.kt */
/* loaded from: classes.dex */
final class ControlActionCoordinatorImpl$longPress$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ ControlViewHolder $cvh;
    final /* synthetic */ ControlActionCoordinatorImpl this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlActionCoordinatorImpl$longPress$1(ControlViewHolder controlViewHolder, ControlActionCoordinatorImpl controlActionCoordinatorImpl) {
        super(0);
        this.$cvh = controlViewHolder;
        this.this$0 = controlActionCoordinatorImpl;
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
        Control control = this.$cvh.getCws().getControl();
        if (control == null) {
            return;
        }
        ControlViewHolder controlViewHolder = this.$cvh;
        ControlActionCoordinatorImpl controlActionCoordinatorImpl = this.this$0;
        controlViewHolder.getLayout().performHapticFeedback(0);
        PendingIntent appIntent = control.getAppIntent();
        Intrinsics.checkNotNullExpressionValue(appIntent, "it.getAppIntent()");
        controlActionCoordinatorImpl.showDetail(controlViewHolder, appIntent);
    }
}
