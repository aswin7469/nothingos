package com.android.systemui.controls.ui;

import android.service.controls.Control;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: DefaultBehavior.kt */
/* loaded from: classes.dex */
public final class DefaultBehavior implements Behavior {
    public ControlViewHolder cvh;

    @NotNull
    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        Intrinsics.throwUninitializedPropertyAccessException("cvh");
        throw null;
    }

    public final void setCvh(@NotNull ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "<set-?>");
        this.cvh = controlViewHolder;
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void initialize(@NotNull ControlViewHolder cvh) {
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        setCvh(cvh);
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void bind(@NotNull ControlWithState cws, int i) {
        CharSequence statusText;
        Intrinsics.checkNotNullParameter(cws, "cws");
        ControlViewHolder cvh = getCvh();
        Control control = cws.getControl();
        CharSequence charSequence = "";
        if (control != null && (statusText = control.getStatusText()) != null) {
            charSequence = statusText;
        }
        ControlViewHolder.setStatusText$default(cvh, charSequence, false, 2, null);
        ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), false, i, false, 4, null);
    }
}
