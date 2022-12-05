package com.android.systemui.statusbar;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LockscreenShadeTransitionController.kt */
/* loaded from: classes.dex */
public final class LockscreenShadeTransitionController$goToLockedShade$1 extends Lambda implements Function1<Long, Unit> {
    final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LockscreenShadeTransitionController$goToLockedShade$1(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(1);
        this.this$0 = lockscreenShadeTransitionController;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1949invoke(Long l) {
        invoke(l.longValue());
        return Unit.INSTANCE;
    }

    public final void invoke(long j) {
        this.this$0.getNotificationPanelController().animateToFullShade(j);
    }
}
