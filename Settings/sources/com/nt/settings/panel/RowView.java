package com.nt.settings.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class RowView extends LinearLayout {
    public RowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int dimensionPixelOffset;
        View findViewById = findViewById(R.id.summary);
        if (findViewById != null && (findViewById.getVisibility() == 0 || findViewById.getVisibility() == 4)) {
            dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.row_view_height);
        } else {
            dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.row_view_height_small);
        }
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(dimensionPixelOffset, View.MeasureSpec.getMode(i2)));
    }
}
