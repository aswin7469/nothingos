package com.android.systemui.controls.management;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000-\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J \u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016J\u0018\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\tH\u0016J\b\u0010\r\u001a\u00020\u0005H\u0016J \u0010\u000e\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u0003H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003XD¢\u0006\u0002\n\u0000¨\u0006\u0012"}, mo64987d2 = {"com/android/systemui/controls/management/FavoritesModel$itemTouchHelperCallback$1", "Landroidx/recyclerview/widget/ItemTouchHelper$SimpleCallback;", "MOVEMENT", "", "canDropOver", "", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "current", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "target", "getMovementFlags", "viewHolder", "isItemViewSwipeEnabled", "onMove", "onSwiped", "", "direction", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: FavoritesModel.kt */
public final class FavoritesModel$itemTouchHelperCallback$1 extends ItemTouchHelper.SimpleCallback {
    private final int MOVEMENT = 15;
    final /* synthetic */ FavoritesModel this$0;

    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        Intrinsics.checkNotNullParameter(viewHolder, "viewHolder");
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    FavoritesModel$itemTouchHelperCallback$1(FavoritesModel favoritesModel) {
        super(0, 0);
        this.this$0 = favoritesModel;
    }

    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
        Intrinsics.checkNotNullParameter(viewHolder, "viewHolder");
        Intrinsics.checkNotNullParameter(viewHolder2, "target");
        this.this$0.onMoveItem(viewHolder.getBindingAdapterPosition(), viewHolder2.getBindingAdapterPosition());
        return true;
    }

    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
        Intrinsics.checkNotNullParameter(viewHolder, "viewHolder");
        if (viewHolder.getBindingAdapterPosition() < this.this$0.dividerPosition) {
            return ItemTouchHelper.Callback.makeMovementFlags(this.MOVEMENT, 0);
        }
        return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
    }

    public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
        Intrinsics.checkNotNullParameter(viewHolder, "current");
        Intrinsics.checkNotNullParameter(viewHolder2, "target");
        return viewHolder2.getBindingAdapterPosition() < this.this$0.dividerPosition;
    }
}
