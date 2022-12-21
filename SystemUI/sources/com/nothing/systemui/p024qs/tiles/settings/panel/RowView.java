package com.nothing.systemui.p024qs.tiles.settings.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.LinearLayout;
import com.android.systemui.C1893R;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.RowView */
public class RowView extends LinearLayout {
    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public RowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        View findViewById = findViewById(C1893R.C1897id.summary);
        if (findViewById == null || !(findViewById.getVisibility() == 0 || findViewById.getVisibility() == 4)) {
            i3 = getResources().getDimensionPixelOffset(C1893R.dimen.nt_row_view_height_small);
        } else {
            i3 = getResources().getDimensionPixelOffset(C1893R.dimen.nt_row_view_height);
        }
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(i3, View.MeasureSpec.getMode(i2)));
    }
}
