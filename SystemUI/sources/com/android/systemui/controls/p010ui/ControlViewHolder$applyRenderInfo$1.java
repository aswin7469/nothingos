package com.android.systemui.controls.p010ui;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.service.controls.Control;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlViewHolder$applyRenderInfo$1 */
/* compiled from: ControlViewHolder.kt */
final class ControlViewHolder$applyRenderInfo$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ Control $control;
    final /* synthetic */ boolean $enabled;
    final /* synthetic */ ColorStateList $fg;
    final /* synthetic */ CharSequence $newText;
    final /* synthetic */ RenderInfo $ri;
    final /* synthetic */ ControlViewHolder this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ControlViewHolder$applyRenderInfo$1(ControlViewHolder controlViewHolder, boolean z, CharSequence charSequence, RenderInfo renderInfo, ColorStateList colorStateList, Control control) {
        super(0);
        this.this$0 = controlViewHolder;
        this.$enabled = z;
        this.$newText = charSequence;
        this.$ri = renderInfo;
        this.$fg = colorStateList;
        this.$control = control;
    }

    public final void invoke() {
        ControlViewHolder controlViewHolder = this.this$0;
        boolean z = this.$enabled;
        CharSequence charSequence = this.$newText;
        Drawable icon = this.$ri.getIcon();
        ColorStateList colorStateList = this.$fg;
        Intrinsics.checkNotNullExpressionValue(colorStateList, "fg");
        controlViewHolder.updateStatusRow$SystemUI_nothingRelease(z, charSequence, icon, colorStateList, this.$control);
    }
}
