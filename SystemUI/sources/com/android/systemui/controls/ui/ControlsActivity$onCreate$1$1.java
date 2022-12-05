package com.android.systemui.controls.ui;

import android.view.View;
import android.view.WindowInsets;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ControlsActivity.kt */
/* loaded from: classes.dex */
final class ControlsActivity$onCreate$1$1 implements View.OnApplyWindowInsetsListener {
    public static final ControlsActivity$onCreate$1$1 INSTANCE = new ControlsActivity$onCreate$1$1();

    ControlsActivity$onCreate$1$1() {
    }

    @Override // android.view.View.OnApplyWindowInsetsListener
    public final WindowInsets onApplyWindowInsets(@NotNull View v, @NotNull WindowInsets insets) {
        Intrinsics.checkNotNullParameter(v, "v");
        Intrinsics.checkNotNullParameter(insets, "insets");
        v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), insets.getInsets(WindowInsets.Type.systemBars()).bottom);
        return WindowInsets.CONSUMED;
    }
}
