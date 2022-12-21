package com.android.systemui.globalactions;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.helper.widget.Flow;
import com.android.systemui.C1893R;
import com.android.systemui.HardwareBgDrawable;

public class GlobalActionsLayoutLite extends GlobalActionsLayout {
    static /* synthetic */ void lambda$new$0(View view) {
    }

    public float getAnimationOffsetY() {
        return 0.0f;
    }

    /* access modifiers changed from: protected */
    public HardwareBgDrawable getBackgroundDrawable(int i) {
        return null;
    }

    /* access modifiers changed from: protected */
    public boolean shouldReverseListItems() {
        return false;
    }

    public GlobalActionsLayoutLite(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnClickListener(new GlobalActionsLayoutLite$$ExternalSyntheticLambda0());
    }

    public void onUpdateList() {
        super.onUpdateList();
        int integer = getResources().getInteger(C1893R.integer.power_menu_lite_max_columns);
        if (getListView().getChildCount() - 1 == integer + 1 && integer > 2) {
            integer--;
        }
        ((Flow) findViewById(C1893R.C1897id.list_flow)).setMaxElementsWrap(integer);
    }

    /* access modifiers changed from: protected */
    public void addToListView(View view, boolean z) {
        super.addToListView(view, z);
        ((Flow) findViewById(C1893R.C1897id.list_flow)).addView(view);
    }

    /* access modifiers changed from: protected */
    public void removeAllListViews() {
        View findViewById = findViewById(C1893R.C1897id.list_flow);
        super.removeAllListViews();
        super.addToListView(findViewById, false);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        ViewGroup listView = getListView();
        boolean z2 = false;
        for (int i5 = 0; i5 < listView.getChildCount(); i5++) {
            View childAt = listView.getChildAt(i5);
            if (childAt instanceof GlobalActionsItem) {
                z2 = z2 || ((GlobalActionsItem) childAt).isTruncated();
            }
        }
        if (z2) {
            for (int i6 = 0; i6 < listView.getChildCount(); i6++) {
                View childAt2 = listView.getChildAt(i6);
                if (childAt2 instanceof GlobalActionsItem) {
                    ((GlobalActionsItem) childAt2).setMarquee(true);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public float getGridItemSize() {
        return getContext().getResources().getDimension(C1893R.dimen.global_actions_grid_item_height);
    }

    /* access modifiers changed from: protected */
    public float getAnimationDistance() {
        return getGridItemSize() / 2.0f;
    }

    public float getAnimationOffsetX() {
        return getAnimationDistance();
    }
}
