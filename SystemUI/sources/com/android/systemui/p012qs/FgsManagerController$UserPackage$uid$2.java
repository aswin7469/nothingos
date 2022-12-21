package com.android.systemui.p012qs;

import com.android.systemui.p012qs.FgsManagerController;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, mo64987d2 = {"<anonymous>", "", "invoke", "()Ljava/lang/Integer;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.FgsManagerController$UserPackage$uid$2 */
/* compiled from: FgsManagerController.kt */
final class FgsManagerController$UserPackage$uid$2 extends Lambda implements Function0<Integer> {
    final /* synthetic */ FgsManagerController this$0;
    final /* synthetic */ FgsManagerController.UserPackage this$1;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    FgsManagerController$UserPackage$uid$2(FgsManagerController fgsManagerController, FgsManagerController.UserPackage userPackage) {
        super(0);
        this.this$0 = fgsManagerController;
        this.this$1 = userPackage;
    }

    public final Integer invoke() {
        return Integer.valueOf(this.this$0.packageManager.getPackageUidAsUser(this.this$1.getPackageName(), this.this$1.getUserId()));
    }
}
