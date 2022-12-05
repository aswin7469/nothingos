package com.android.systemui.controls.management;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$drawable;
import com.android.systemui.R$layout;
import com.android.systemui.controls.ControlInterface;
import java.util.List;
import java.util.Objects;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class ControlAdapter extends RecyclerView.Adapter<Holder> {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private final float elevation;
    @Nullable
    private ControlsModel model;
    @NotNull
    private final GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() { // from class: com.android.systemui.controls.management.ControlAdapter$spanSizeLookup$1
        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanSize(int i) {
            return ControlAdapter.this.getItemViewType(i) != 1 ? 2 : 1;
        }
    };

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(Holder holder, int i, List list) {
        onBindViewHolder2(holder, i, (List<Object>) list);
    }

    public ControlAdapter(float f) {
        this.elevation = f;
    }

    /* compiled from: ControlAdapter.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @NotNull
    public final GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return this.spanSizeLookup;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NotNull
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public Holder mo1838onCreateViewHolder(@NotNull ViewGroup parent, int i) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        LayoutInflater from = LayoutInflater.from(parent.getContext());
        if (i == 0) {
            View inflate = from.inflate(R$layout.controls_zone_header, parent, false);
            Intrinsics.checkNotNullExpressionValue(inflate, "layoutInflater.inflate(R.layout.controls_zone_header, parent, false)");
            return new ZoneHolder(inflate);
        } else if (i != 1) {
            if (i == 2) {
                View inflate2 = from.inflate(R$layout.controls_horizontal_divider_with_empty, parent, false);
                Intrinsics.checkNotNullExpressionValue(inflate2, "layoutInflater.inflate(\n                        R.layout.controls_horizontal_divider_with_empty, parent, false)");
                return new DividerHolder(inflate2);
            }
            throw new IllegalStateException(Intrinsics.stringPlus("Wrong viewType: ", Integer.valueOf(i)));
        } else {
            View inflate3 = from.inflate(R$layout.controls_base_item, parent, false);
            ViewGroup.LayoutParams layoutParams = inflate3.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.width = -1;
            marginLayoutParams.topMargin = 0;
            marginLayoutParams.bottomMargin = 0;
            marginLayoutParams.leftMargin = 0;
            marginLayoutParams.rightMargin = 0;
            inflate3.setElevation(this.elevation);
            inflate3.setBackground(parent.getContext().getDrawable(R$drawable.control_background_ripple));
            Unit unit = Unit.INSTANCE;
            Intrinsics.checkNotNullExpressionValue(inflate3, "layoutInflater.inflate(R.layout.controls_base_item, parent, false).apply {\n                        (layoutParams as ViewGroup.MarginLayoutParams).apply {\n                            width = ViewGroup.LayoutParams.MATCH_PARENT\n                            // Reset margins as they will be set through the decoration\n                            topMargin = 0\n                            bottomMargin = 0\n                            leftMargin = 0\n                            rightMargin = 0\n                        }\n                        elevation = this@ControlAdapter.elevation\n                        background = parent.context.getDrawable(\n                                R.drawable.control_background_ripple)\n                    }");
            ControlsModel controlsModel = this.model;
            return new ControlHolder(inflate3, controlsModel == null ? null : controlsModel.mo411getMoveHelper(), new ControlAdapter$onCreateViewHolder$2(this));
        }
    }

    public final void changeModel(@NotNull ControlsModel model) {
        Intrinsics.checkNotNullParameter(model, "model");
        this.model = model;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        ControlsModel controlsModel = this.model;
        List<ElementWrapper> elements = controlsModel == null ? null : controlsModel.getElements();
        if (elements == null) {
            return 0;
        }
        return elements.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NotNull Holder holder, int i) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        ControlsModel controlsModel = this.model;
        if (controlsModel == null) {
            return;
        }
        holder.bindData(controlsModel.getElements().get(i));
    }

    /* renamed from: onBindViewHolder  reason: avoid collision after fix types in other method */
    public void onBindViewHolder2(@NotNull Holder holder, int i, @NotNull List<Object> payloads) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        Intrinsics.checkNotNullParameter(payloads, "payloads");
        if (payloads.isEmpty()) {
            super.onBindViewHolder((ControlAdapter) holder, i, payloads);
            return;
        }
        ControlsModel controlsModel = this.model;
        if (controlsModel == null) {
            return;
        }
        ElementWrapper elementWrapper = controlsModel.getElements().get(i);
        if (!(elementWrapper instanceof ControlInterface)) {
            return;
        }
        holder.updateFavorite(((ControlInterface) elementWrapper).getFavorite());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        ControlsModel controlsModel = this.model;
        if (controlsModel != null) {
            ElementWrapper elementWrapper = controlsModel.getElements().get(i);
            if (elementWrapper instanceof ZoneNameWrapper) {
                return 0;
            }
            if ((elementWrapper instanceof ControlStatusWrapper) || (elementWrapper instanceof ControlInfoWrapper)) {
                return 1;
            }
            if (!(elementWrapper instanceof DividerWrapper)) {
                throw new NoWhenBranchMatchedException();
            }
            return 2;
        }
        throw new IllegalStateException("Getting item type for null model");
    }
}
