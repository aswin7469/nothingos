package com.android.systemui.statusbar;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/LockscreenShadeKeyguardTransitionController;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.LockscreenShadeTransitionController$keyguardTransitionController$2 */
/* compiled from: LockscreenShadeTransitionController.kt */
final class C2557x794b57c2 extends Lambda implements Function0<LockscreenShadeKeyguardTransitionController> {
    final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C2557x794b57c2(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(0);
        this.this$0 = lockscreenShadeTransitionController;
    }

    public final LockscreenShadeKeyguardTransitionController invoke() {
        return this.this$0.keyguardTransitionControllerFactory.create(this.this$0.getNotificationPanelController());
    }
}
