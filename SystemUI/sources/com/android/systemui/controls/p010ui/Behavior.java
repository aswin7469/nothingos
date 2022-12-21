package com.android.systemui.controls.p010ui;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u000bÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/Behavior;", "", "bind", "", "cws", "Lcom/android/systemui/controls/ui/ControlWithState;", "colorOffset", "", "initialize", "cvh", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.Behavior */
/* compiled from: Behavior.kt */
public interface Behavior {
    void bind(ControlWithState controlWithState, int i);

    void initialize(ControlViewHolder controlViewHolder);

    static /* synthetic */ void bind$default(Behavior behavior, ControlWithState controlWithState, int i, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 2) != 0) {
                i = 0;
            }
            behavior.bind(controlWithState, i);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: bind");
    }
}
