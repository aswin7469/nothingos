package com.android.systemui.controls.management;

import androidx.recyclerview.widget.GridLayoutManager;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0016¨\u0006\u0005"}, mo64987d2 = {"com/android/systemui/controls/management/ControlsEditingActivity$setUpList$1$2$1", "Landroidx/recyclerview/widget/GridLayoutManager$SpanSizeLookup;", "getSpanSize", "", "position", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsEditingActivity.kt */
public final class ControlsEditingActivity$setUpList$1$2$1 extends GridLayoutManager.SpanSizeLookup {
    final /* synthetic */ ControlAdapter $adapter;
    final /* synthetic */ int $spanCount;

    ControlsEditingActivity$setUpList$1$2$1(ControlAdapter controlAdapter, int i) {
        this.$adapter = controlAdapter;
        this.$spanCount = i;
    }

    public int getSpanSize(int i) {
        ControlAdapter controlAdapter = this.$adapter;
        boolean z = false;
        if (controlAdapter != null && controlAdapter.getItemViewType(i) == 1) {
            z = true;
        }
        if (!z) {
            return this.$spanCount;
        }
        return 1;
    }
}