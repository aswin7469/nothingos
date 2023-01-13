package com.android.systemui.controls.management;

import androidx.recyclerview.widget.RecyclerView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\b\u001a\u00020\tH\u0016R\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007¨\u0006\n"}, mo65043d2 = {"com/android/systemui/controls/management/ControlsProviderSelectorActivity$onStart$3$1", "Landroidx/recyclerview/widget/RecyclerView$AdapterDataObserver;", "hasAnimated", "", "getHasAnimated", "()Z", "setHasAnimated", "(Z)V", "onChanged", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsProviderSelectorActivity.kt */
public final class ControlsProviderSelectorActivity$onStart$3$1 extends RecyclerView.AdapterDataObserver {
    private boolean hasAnimated;
    final /* synthetic */ ControlsProviderSelectorActivity this$0;

    ControlsProviderSelectorActivity$onStart$3$1(ControlsProviderSelectorActivity controlsProviderSelectorActivity) {
        this.this$0 = controlsProviderSelectorActivity;
    }

    public final boolean getHasAnimated() {
        return this.hasAnimated;
    }

    public final void setHasAnimated(boolean z) {
        this.hasAnimated = z;
    }

    public void onChanged() {
        if (!this.hasAnimated) {
            this.hasAnimated = true;
            ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
            RecyclerView access$getRecyclerView$p = this.this$0.recyclerView;
            if (access$getRecyclerView$p == null) {
                Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
                access$getRecyclerView$p = null;
            }
            controlsAnimations.enterAnimation(access$getRecyclerView$p).start();
        }
    }
}
