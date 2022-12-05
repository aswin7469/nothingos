package com.android.systemui.controls.ui;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.service.controls.Control;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlViewHolder.kt */
/* loaded from: classes.dex */
public final class ControlViewHolder$applyRenderInfo$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ Control $control;
    final /* synthetic */ boolean $enabled;
    final /* synthetic */ ColorStateList $fg;
    final /* synthetic */ CharSequence $newText;
    final /* synthetic */ RenderInfo $ri;
    final /* synthetic */ ControlViewHolder this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlViewHolder$applyRenderInfo$1(ControlViewHolder controlViewHolder, boolean z, CharSequence charSequence, RenderInfo renderInfo, ColorStateList colorStateList, Control control) {
        super(0);
        this.this$0 = controlViewHolder;
        this.$enabled = z;
        this.$newText = charSequence;
        this.$ri = renderInfo;
        this.$fg = colorStateList;
        this.$control = control;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1951invoke() {
        mo1951invoke();
        return Unit.INSTANCE;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final void mo1951invoke() {
        ControlViewHolder controlViewHolder = this.this$0;
        boolean z = this.$enabled;
        CharSequence charSequence = this.$newText;
        Drawable icon = this.$ri.getIcon();
        ColorStateList fg = this.$fg;
        Intrinsics.checkNotNullExpressionValue(fg, "fg");
        controlViewHolder.updateStatusRow$frameworks__base__packages__SystemUI__android_common__SystemUI_core(z, charSequence, icon, fg, this.$control);
    }
}
