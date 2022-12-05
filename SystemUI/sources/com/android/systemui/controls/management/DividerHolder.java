package com.android.systemui.controls.management;

import android.view.View;
import com.android.systemui.R$id;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class DividerHolder extends Holder {
    @NotNull
    private final View divider;
    @NotNull
    private final View frame;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DividerHolder(@NotNull View view) {
        super(view, null);
        Intrinsics.checkNotNullParameter(view, "view");
        View requireViewById = this.itemView.requireViewById(R$id.frame);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById(R.id.frame)");
        this.frame = requireViewById;
        View requireViewById2 = this.itemView.requireViewById(R$id.divider);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "itemView.requireViewById(R.id.divider)");
        this.divider = requireViewById2;
    }

    @Override // com.android.systemui.controls.management.Holder
    public void bindData(@NotNull ElementWrapper wrapper) {
        Intrinsics.checkNotNullParameter(wrapper, "wrapper");
        DividerWrapper dividerWrapper = (DividerWrapper) wrapper;
        int i = 0;
        this.frame.setVisibility(dividerWrapper.getShowNone() ? 0 : 8);
        View view = this.divider;
        if (!dividerWrapper.getShowDivider()) {
            i = 8;
        }
        view.setVisibility(i);
    }
}
