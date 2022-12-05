package com.android.systemui.controls.management;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class ControlAdapter$onCreateViewHolder$2 extends Lambda implements Function2<String, Boolean, Unit> {
    final /* synthetic */ ControlAdapter this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlAdapter$onCreateViewHolder$2(ControlAdapter controlAdapter) {
        super(2);
        this.this$0 = controlAdapter;
    }

    @Override // kotlin.jvm.functions.Function2
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1950invoke(String str, Boolean bool) {
        invoke(str, bool.booleanValue());
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull String id, boolean z) {
        ControlsModel controlsModel;
        Intrinsics.checkNotNullParameter(id, "id");
        controlsModel = this.this$0.model;
        if (controlsModel == null) {
            return;
        }
        controlsModel.changeFavoriteStatus(id, z);
    }
}
