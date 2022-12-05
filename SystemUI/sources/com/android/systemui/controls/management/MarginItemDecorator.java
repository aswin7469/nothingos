package com.android.systemui.controls.management;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class MarginItemDecorator extends RecyclerView.ItemDecoration {
    private final int sideMargins;
    private final int topMargin;

    public MarginItemDecorator(int i, int i2) {
        this.topMargin = i;
        this.sideMargins = i2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        Intrinsics.checkNotNullParameter(outRect, "outRect");
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(state, "state");
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (childAdapterPosition == -1) {
            return;
        }
        RecyclerView.Adapter adapter = parent.getAdapter();
        Integer valueOf = adapter == null ? null : Integer.valueOf(adapter.getItemViewType(childAdapterPosition));
        if (valueOf != null && valueOf.intValue() == 1) {
            outRect.top = this.topMargin * 2;
            int i = this.sideMargins;
            outRect.left = i;
            outRect.right = i;
            outRect.bottom = 0;
        } else if (valueOf == null || valueOf.intValue() != 0 || childAdapterPosition != 0) {
        } else {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            outRect.top = -((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
            outRect.left = 0;
            outRect.right = 0;
            outRect.bottom = 0;
        }
    }
}
