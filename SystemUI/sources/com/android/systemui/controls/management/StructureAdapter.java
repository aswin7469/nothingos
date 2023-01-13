package com.android.systemui.controls.management;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.C1894R;
import com.android.systemui.controls.management.ControlAdapter;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0011B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\bH\u0016J\u0018\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bH\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, mo65043d2 = {"Lcom/android/systemui/controls/management/StructureAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/android/systemui/controls/management/StructureAdapter$StructureHolder;", "models", "", "Lcom/android/systemui/controls/management/StructureContainer;", "(Ljava/util/List;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "index", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "p1", "StructureHolder", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StructureAdapter.kt */
public final class StructureAdapter extends RecyclerView.Adapter<StructureHolder> {
    private final List<StructureContainer> models;

    public StructureAdapter(List<StructureContainer> list) {
        Intrinsics.checkNotNullParameter(list, "models");
        this.models = list;
    }

    public StructureHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(C1894R.layout.controls_structure_page, viewGroup, false);
        Intrinsics.checkNotNullExpressionValue(inflate, "layoutInflater.inflate(R…ture_page, parent, false)");
        return new StructureHolder(inflate);
    }

    public int getItemCount() {
        return this.models.size();
    }

    public void onBindViewHolder(StructureHolder structureHolder, int i) {
        Intrinsics.checkNotNullParameter(structureHolder, "holder");
        structureHolder.bind(this.models.get(i).getModel());
    }

    @Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\b\u0010\r\u001a\u00020\nH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, mo65043d2 = {"Lcom/android/systemui/controls/management/StructureAdapter$StructureHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "controlAdapter", "Lcom/android/systemui/controls/management/ControlAdapter;", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "bind", "", "model", "Lcom/android/systemui/controls/management/ControlsModel;", "setUpRecyclerView", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: StructureAdapter.kt */
    public static final class StructureHolder extends RecyclerView.ViewHolder {
        private final ControlAdapter controlAdapter = new ControlAdapter(this.itemView.getContext().getResources().getFloat(C1894R.dimen.control_card_elevation));
        private final RecyclerView recyclerView;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public StructureHolder(View view) {
            super(view);
            Intrinsics.checkNotNullParameter(view, "view");
            View requireViewById = this.itemView.requireViewById(C1894R.C1898id.listAll);
            Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById…cyclerView>(R.id.listAll)");
            this.recyclerView = (RecyclerView) requireViewById;
            setUpRecyclerView();
        }

        public final void bind(ControlsModel controlsModel) {
            Intrinsics.checkNotNullParameter(controlsModel, "model");
            this.controlAdapter.changeModel(controlsModel);
        }

        private final void setUpRecyclerView() {
            int dimensionPixelSize = this.itemView.getContext().getResources().getDimensionPixelSize(C1894R.dimen.controls_card_margin);
            MarginItemDecorator marginItemDecorator = new MarginItemDecorator(dimensionPixelSize, dimensionPixelSize);
            ControlAdapter.Companion companion = ControlAdapter.Companion;
            Resources resources = this.itemView.getResources();
            Intrinsics.checkNotNullExpressionValue(resources, "itemView.resources");
            int findMaxColumns = companion.findMaxColumns(resources);
            RecyclerView recyclerView2 = this.recyclerView;
            recyclerView2.setAdapter(this.controlAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this.recyclerView.getContext(), findMaxColumns);
            gridLayoutManager.setSpanSizeLookup(new StructureAdapter$StructureHolder$setUpRecyclerView$1$1$1(recyclerView2, findMaxColumns));
            recyclerView2.setLayoutManager(gridLayoutManager);
            recyclerView2.addItemDecoration(marginItemDecorator);
        }
    }
}
