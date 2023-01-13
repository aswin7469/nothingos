package com.android.systemui.statusbar;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo65043d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/LockscreenShadeKeyguardTransitionController;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.LockscreenShadeTransitionController$keyguardTransitionController$2 */
/* compiled from: LockscreenShadeTransitionController.kt */
final class C2563x794b57c2 extends Lambda implements Function0<LockscreenShadeKeyguardTransitionController> {
    final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C2563x794b57c2(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(0);
        this.this$0 = lockscreenShadeTransitionController;
    }

    public final LockscreenShadeKeyguardTransitionController invoke() {
        return this.this$0.keyguardTransitionControllerFactory.create(this.this$0.getNotificationPanelController());
    }
}
