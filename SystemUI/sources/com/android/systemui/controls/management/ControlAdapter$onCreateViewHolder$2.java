package com.android.systemui.controls.management;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\n¢\u0006\u0002\b\u0006"}, mo64987d2 = {"<anonymous>", "", "id", "", "favorite", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlAdapter.kt */
final class ControlAdapter$onCreateViewHolder$2 extends Lambda implements Function2<String, Boolean, Unit> {
    final /* synthetic */ ControlAdapter this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ControlAdapter$onCreateViewHolder$2(ControlAdapter controlAdapter) {
        super(2);
        this.this$0 = controlAdapter;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        invoke((String) obj, ((Boolean) obj2).booleanValue());
        return Unit.INSTANCE;
    }

    public final void invoke(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "id");
        ControlsModel access$getModel$p = this.this$0.model;
        if (access$getModel$p != null) {
            access$getModel$p.changeFavoriteStatus(str, z);
        }
    }
}
