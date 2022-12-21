package com.android.keyguard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import com.android.systemui.C1893R;
import com.android.systemui.plugins.FalsingManager;

public class KeyguardUserSwitcherPopupMenu extends ListPopupWindow {
    private Context mContext;
    private FalsingManager mFalsingManager;

    public KeyguardUserSwitcherPopupMenu(Context context, FalsingManager falsingManager) {
        super(context);
        this.mContext = context;
        this.mFalsingManager = falsingManager;
        setBackgroundDrawable(context.getResources().getDrawable(C1893R.C1895drawable.bouncer_user_switcher_popup_bg, context.getTheme()));
        setModal(true);
        setOverlapAnchor(true);
    }

    public void show() {
        super.show();
        ListView listView = getListView();
        listView.setVerticalScrollBarEnabled(false);
        listView.setHorizontalScrollBarEnabled(false);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setAlpha(0);
        listView.setDivider(shapeDrawable);
        listView.setDividerHeight(this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.bouncer_user_switcher_popup_divider_height));
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.bouncer_user_switcher_popup_header_height);
        listView.addHeaderView(createSpacer(dimensionPixelSize), (Object) null, false);
        listView.addFooterView(createSpacer(dimensionPixelSize), (Object) null, false);
        listView.setOnTouchListener(new KeyguardUserSwitcherPopupMenu$$ExternalSyntheticLambda0(this));
        super.show();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$show$0$com-android-keyguard-KeyguardUserSwitcherPopupMenu  reason: not valid java name */
    public /* synthetic */ boolean m2298lambda$show$0$comandroidkeyguardKeyguardUserSwitcherPopupMenu(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            return this.mFalsingManager.isFalseTap(1);
        }
        return false;
    }

    private View createSpacer(final int i) {
        return new View(this.mContext) {
            public void draw(Canvas canvas) {
            }

            /* access modifiers changed from: protected */
            public void onMeasure(int i, int i2) {
                setMeasuredDimension(1, i);
            }
        };
    }
}
