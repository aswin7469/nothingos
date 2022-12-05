package com.android.systemui.controls.management;

import android.view.View;
import android.widget.TextView;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class ZoneHolder extends Holder {
    @NotNull
    private final TextView zone = (TextView) this.itemView;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ZoneHolder(@NotNull View view) {
        super(view, null);
        Intrinsics.checkNotNullParameter(view, "view");
    }

    @Override // com.android.systemui.controls.management.Holder
    public void bindData(@NotNull ElementWrapper wrapper) {
        Intrinsics.checkNotNullParameter(wrapper, "wrapper");
        this.zone.setText(((ZoneNameWrapper) wrapper).getZoneName());
    }
}
