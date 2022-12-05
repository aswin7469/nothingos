package com.android.systemui.statusbar.policy;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: BatteryStateNotifier.kt */
/* loaded from: classes2.dex */
public final class BatteryStateNotifier$scheduleNotificationCancel$r$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ BatteryStateNotifier this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BatteryStateNotifier$scheduleNotificationCancel$r$1(BatteryStateNotifier batteryStateNotifier) {
        super(0);
        this.this$0 = batteryStateNotifier;
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
        if (!this.this$0.getStateUnknown()) {
            this.this$0.getNoMan().cancel(666);
        }
    }
}
