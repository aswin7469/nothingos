package com.android.systemui.p012qs.external;

import com.android.systemui.statusbar.phone.SystemUIDialog;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo64987d2 = {"<anonymous>", "", "it", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.external.TileServiceRequestController$requestTileAdd$1$1 */
/* compiled from: TileServiceRequestController.kt */
final class TileServiceRequestController$requestTileAdd$1$1 extends Lambda implements Function1<String, Unit> {
    final /* synthetic */ SystemUIDialog $dialog;
    final /* synthetic */ String $packageName;
    final /* synthetic */ TileServiceRequestController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    TileServiceRequestController$requestTileAdd$1$1(String str, SystemUIDialog systemUIDialog, TileServiceRequestController tileServiceRequestController) {
        super(1);
        this.$packageName = str;
        this.$dialog = systemUIDialog;
        this.this$0 = tileServiceRequestController;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((String) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(String str) {
        Intrinsics.checkNotNullParameter(str, "it");
        if (Intrinsics.areEqual((Object) this.$packageName, (Object) str)) {
            this.$dialog.cancel();
        }
        this.this$0.dialogCanceller = null;
    }
}
