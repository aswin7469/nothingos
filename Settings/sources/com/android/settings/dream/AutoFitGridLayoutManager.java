package com.android.settings.dream;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R$dimen;

public final class AutoFitGridLayoutManager extends GridLayoutManager {
    private final float mColumnWidth;

    public AutoFitGridLayoutManager(Context context) {
        super(context, 1);
        this.mColumnWidth = (float) context.getResources().getDimensionPixelSize(R$dimen.dream_item_min_column_width);
    }

    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        setSpanCount(Math.max(1, (int) (((float) ((getWidth() - getPaddingRight()) - getPaddingLeft())) / this.mColumnWidth)));
        super.onLayoutChildren(recycler, state);
    }
}
