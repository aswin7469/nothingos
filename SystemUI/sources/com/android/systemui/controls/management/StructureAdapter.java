package com.android.systemui.controls.management;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: StructureAdapter.kt */
/* loaded from: classes.dex */
public final class StructureAdapter extends RecyclerView.Adapter<StructureHolder> {
    @NotNull
    private final List<StructureContainer> models;

    public StructureAdapter(@NotNull List<StructureContainer> models) {
        Intrinsics.checkNotNullParameter(models, "models");
        this.models = models;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NotNull
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public StructureHolder mo1838onCreateViewHolder(@NotNull ViewGroup parent, int i) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R$layout.controls_structure_page, parent, false);
        Intrinsics.checkNotNullExpressionValue(inflate, "layoutInflater.inflate(R.layout.controls_structure_page, parent, false)");
        return new StructureHolder(inflate);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.models.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NotNull StructureHolder holder, int i) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        holder.bind(this.models.get(i).getModel());
    }

    /* compiled from: StructureAdapter.kt */
    /* loaded from: classes.dex */
    public static final class StructureHolder extends RecyclerView.ViewHolder {
        @NotNull
        private final ControlAdapter controlAdapter = new ControlAdapter(this.itemView.getContext().getResources().getFloat(R$dimen.control_card_elevation));
        @NotNull
        private final RecyclerView recyclerView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public StructureHolder(@NotNull View view) {
            super(view);
            Intrinsics.checkNotNullParameter(view, "view");
            View requireViewById = this.itemView.requireViewById(R$id.listAll);
            Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById<RecyclerView>(R.id.listAll)");
            this.recyclerView = (RecyclerView) requireViewById;
            setUpRecyclerView();
        }

        public final void bind(@NotNull ControlsModel model) {
            Intrinsics.checkNotNullParameter(model, "model");
            this.controlAdapter.changeModel(model);
        }

        private final void setUpRecyclerView() {
            int dimensionPixelSize = this.itemView.getContext().getResources().getDimensionPixelSize(R$dimen.controls_card_margin);
            MarginItemDecorator marginItemDecorator = new MarginItemDecorator(dimensionPixelSize, dimensionPixelSize);
            RecyclerView recyclerView = this.recyclerView;
            recyclerView.setAdapter(this.controlAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this.recyclerView.getContext(), 2);
            gridLayoutManager.setSpanSizeLookup(this.controlAdapter.getSpanSizeLookup());
            Unit unit = Unit.INSTANCE;
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(marginItemDecorator);
        }
    }
}
