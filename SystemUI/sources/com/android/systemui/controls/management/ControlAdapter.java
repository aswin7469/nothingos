package com.android.systemui.controls.management;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.C1894R;
import com.android.systemui.controls.ControlInterface;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010!\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u00182\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0018B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0007J\b\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000bH\u0016J\u0018\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u000bH\u0016J&\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u000b2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u0016J\u0018\u0010\u0014\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000bH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/android/systemui/controls/management/Holder;", "elevation", "", "(F)V", "model", "Lcom/android/systemui/controls/management/ControlsModel;", "changeModel", "", "getItemCount", "", "getItemViewType", "position", "onBindViewHolder", "holder", "index", "payloads", "", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlAdapter.kt */
public final class ControlAdapter extends RecyclerView.Adapter<Holder> {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int TYPE_CONTROL = 1;
    public static final int TYPE_DIVIDER = 2;
    public static final int TYPE_ZONE = 0;
    private final float elevation;
    /* access modifiers changed from: private */
    public ControlsModel model;

    @JvmStatic
    public static final int findMaxColumns(Resources resources) {
        return Companion.findMaxColumns(resources);
    }

    public ControlAdapter(float f) {
        this.elevation = f;
    }

    @Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlAdapter$Companion;", "", "()V", "TYPE_CONTROL", "", "TYPE_DIVIDER", "TYPE_ZONE", "findMaxColumns", "res", "Landroid/content/res/Resources;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ControlAdapter.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final int findMaxColumns(Resources resources) {
            Intrinsics.checkNotNullParameter(resources, "res");
            int integer = resources.getInteger(C1894R.integer.controls_max_columns);
            int integer2 = resources.getInteger(C1894R.integer.controls_max_columns_adjust_below_width_dp);
            TypedValue typedValue = new TypedValue();
            boolean z = true;
            resources.getValue(C1894R.dimen.controls_max_columns_adjust_above_font_scale, typedValue, true);
            float f = typedValue.getFloat();
            Configuration configuration = resources.getConfiguration();
            if (configuration.orientation != 1) {
                z = false;
            }
            return (!z || configuration.screenWidthDp == 0 || configuration.screenWidthDp > integer2 || configuration.fontScale < f) ? integer : integer - 1;
        }
    }

    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        if (i == 0) {
            View inflate = from.inflate(C1894R.layout.controls_zone_header, viewGroup, false);
            Intrinsics.checkNotNullExpressionValue(inflate, "layoutInflater.inflate(R…ne_header, parent, false)");
            return new ZoneHolder(inflate);
        } else if (i == 1) {
            View inflate2 = from.inflate(C1894R.layout.controls_base_item, viewGroup, false);
            ViewGroup.LayoutParams layoutParams = inflate2.getLayoutParams();
            if (layoutParams != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.width = -1;
                marginLayoutParams.topMargin = 0;
                marginLayoutParams.bottomMargin = 0;
                marginLayoutParams.leftMargin = 0;
                marginLayoutParams.rightMargin = 0;
                inflate2.setElevation(this.elevation);
                inflate2.setBackground(viewGroup.getContext().getDrawable(C1894R.C1896drawable.control_background_ripple));
                Intrinsics.checkNotNullExpressionValue(inflate2, "layoutInflater.inflate(R…le)\n                    }");
                ControlsModel controlsModel = this.model;
                return new ControlHolder(inflate2, controlsModel != null ? controlsModel.getMoveHelper() : null, new ControlAdapter$onCreateViewHolder$2(this));
            }
            throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        } else if (i == 2) {
            View inflate3 = from.inflate(C1894R.layout.controls_horizontal_divider_with_empty, viewGroup, false);
            Intrinsics.checkNotNullExpressionValue(inflate3, "layoutInflater.inflate(\n…ith_empty, parent, false)");
            return new DividerHolder(inflate3);
        } else {
            throw new IllegalStateException("Wrong viewType: " + i);
        }
    }

    public final void changeModel(ControlsModel controlsModel) {
        Intrinsics.checkNotNullParameter(controlsModel, "model");
        this.model = controlsModel;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        List<ElementWrapper> elements;
        ControlsModel controlsModel = this.model;
        if (controlsModel == null || (elements = controlsModel.getElements()) == null) {
            return 0;
        }
        return elements.size();
    }

    public void onBindViewHolder(Holder holder, int i) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        ControlsModel controlsModel = this.model;
        if (controlsModel != null) {
            holder.bindData(controlsModel.getElements().get(i));
        }
    }

    public void onBindViewHolder(Holder holder, int i, List<Object> list) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        Intrinsics.checkNotNullParameter(list, "payloads");
        if (list.isEmpty()) {
            super.onBindViewHolder(holder, i, list);
            return;
        }
        ControlsModel controlsModel = this.model;
        if (controlsModel != null) {
            ElementWrapper elementWrapper = controlsModel.getElements().get(i);
            if (elementWrapper instanceof ControlInterface) {
                holder.updateFavorite(((ControlInterface) elementWrapper).getFavorite());
            }
        }
    }

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
            if (elementWrapper instanceof DividerWrapper) {
                return 2;
            }
            throw new NoWhenBranchMatchedException();
        }
        throw new IllegalStateException("Getting item type for null model");
    }
}
