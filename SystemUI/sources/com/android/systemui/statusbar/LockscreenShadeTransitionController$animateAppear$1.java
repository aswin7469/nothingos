package com.android.systemui.statusbar;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LockscreenShadeTransitionController.kt */
/* loaded from: classes.dex */
public final class LockscreenShadeTransitionController$animateAppear$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LockscreenShadeTransitionController$animateAppear$1(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(0);
        this.this$0 = lockscreenShadeTransitionController;
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
        this.this$0.setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(0.0f);
        this.this$0.forceApplyAmount = false;
    }
}
