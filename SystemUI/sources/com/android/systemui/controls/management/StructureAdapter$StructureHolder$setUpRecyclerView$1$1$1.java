package com.android.systemui.controls.management;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0016¨\u0006\u0005"}, mo65043d2 = {"com/android/systemui/controls/management/StructureAdapter$StructureHolder$setUpRecyclerView$1$1$1", "Landroidx/recyclerview/widget/GridLayoutManager$SpanSizeLookup;", "getSpanSize", "", "position", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StructureAdapter.kt */
public final class StructureAdapter$StructureHolder$setUpRecyclerView$1$1$1 extends GridLayoutManager.SpanSizeLookup {
    final /* synthetic */ int $spanCount;
    final /* synthetic */ RecyclerView $this_apply;

    StructureAdapter$StructureHolder$setUpRecyclerView$1$1$1(RecyclerView recyclerView, int i) {
        this.$this_apply = recyclerView;
        this.$spanCount = i;
    }

    public int getSpanSize(int i) {
        RecyclerView.Adapter adapter = this.$this_apply.getAdapter();
        boolean z = false;
        if (adapter != null && adapter.getItemViewType(i) == 1) {
            z = true;
        }
        if (!z) {
            return this.$spanCount;
        }
        return 1;
    }
}
