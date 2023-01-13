package com.android.systemui.globalactions;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import com.android.systemui.C1894R;

public class GlobalActionsPopupMenu extends ListPopupWindow {
    private ListAdapter mAdapter;
    private Context mContext;
    private int mGlobalActionsSidePadding = 0;
    private boolean mIsDropDownMode;
    private int mMaximumWidthThresholdDp = 800;
    private int mMenuVerticalPadding = 0;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    public GlobalActionsPopupMenu(Context context, boolean z) {
        super(context);
        this.mContext = context;
        Resources resources = context.getResources();
        setBackgroundDrawable(resources.getDrawable(C1894R.C1896drawable.global_actions_popup_bg, context.getTheme()));
        this.mIsDropDownMode = z;
        setInputMethodMode(2);
        setModal(true);
        this.mGlobalActionsSidePadding = resources.getDimensionPixelSize(C1894R.dimen.global_actions_side_margin);
        if (!z) {
            this.mMenuVerticalPadding = resources.getDimensionPixelSize(C1894R.dimen.control_menu_vertical_padding);
        }
    }

    public void setAdapter(ListAdapter listAdapter) {
        this.mAdapter = listAdapter;
        super.setAdapter(listAdapter);
    }

    public void show() {
        super.show();
        if (this.mOnItemLongClickListener != null) {
            getListView().setOnItemLongClickListener(this.mOnItemLongClickListener);
        }
        ListView listView = getListView();
        Resources resources = this.mContext.getResources();
        setVerticalOffset((-getAnchorView().getHeight()) / 2);
        if (this.mIsDropDownMode) {
            listView.setDividerHeight(resources.getDimensionPixelSize(C1894R.dimen.control_list_divider));
            listView.setDivider(resources.getDrawable(C1894R.C1896drawable.controls_list_divider_inset));
        } else if (this.mAdapter != null) {
            int i = Resources.getSystem().getDisplayMetrics().widthPixels;
            float f = ((float) i) / Resources.getSystem().getDisplayMetrics().density;
            double d = (double) i;
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) (0.9d * d), Integer.MIN_VALUE);
            int i2 = 0;
            for (int i3 = 0; i3 < this.mAdapter.getCount(); i3++) {
                View view = this.mAdapter.getView(i3, (View) null, listView);
                view.measure(makeMeasureSpec, 0);
                i2 = Math.max(view.getMeasuredWidth(), i2);
            }
            if (f < ((float) this.mMaximumWidthThresholdDp)) {
                i2 = Math.max(i2, (int) (d * 0.5d));
            }
            int i4 = this.mMenuVerticalPadding;
            listView.setPadding(0, i4, 0, i4);
            setWidth(i2);
            if (getAnchorView().getLayoutDirection() == 0) {
                setHorizontalOffset((getAnchorView().getWidth() - this.mGlobalActionsSidePadding) - i2);
            } else {
                setHorizontalOffset(this.mGlobalActionsSidePadding);
            }
        } else {
            return;
        }
        super.show();
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
}
