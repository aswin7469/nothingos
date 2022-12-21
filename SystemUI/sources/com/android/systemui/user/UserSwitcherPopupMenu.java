package com.android.systemui.user;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import com.android.systemui.C1893R;
import com.android.systemui.plugins.FalsingManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0010\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0012\u0010\u0013\u001a\u00020\u00142\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016J\b\u0010\u0015\u001a\u00020\u0014H\u0016R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, mo64987d2 = {"Lcom/android/systemui/user/UserSwitcherPopupMenu;", "Landroid/widget/ListPopupWindow;", "context", "Landroid/content/Context;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "(Landroid/content/Context;Lcom/android/systemui/plugins/FalsingManager;)V", "adapter", "Landroid/widget/ListAdapter;", "res", "Landroid/content/res/Resources;", "kotlin.jvm.PlatformType", "createSpacer", "Landroid/view/View;", "height", "", "findMaxWidth", "listView", "Landroid/widget/ListView;", "setAdapter", "", "show", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UserSwitcherPopupMenu.kt */
public final class UserSwitcherPopupMenu extends ListPopupWindow {
    private ListAdapter adapter;
    private final Context context;
    private final FalsingManager falsingManager;
    private final Resources res;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UserSwitcherPopupMenu(Context context2, FalsingManager falsingManager2) {
        super(context2);
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(falsingManager2, "falsingManager");
        this.context = context2;
        this.falsingManager = falsingManager2;
        Resources resources = context2.getResources();
        this.res = resources;
        setBackgroundDrawable(resources.getDrawable(C1893R.C1895drawable.bouncer_user_switcher_popup_bg, context2.getTheme()));
        setModal(false);
        setOverlapAnchor(true);
    }

    public void setAdapter(ListAdapter listAdapter) {
        super.setAdapter(listAdapter);
        this.adapter = listAdapter;
    }

    public void show() {
        super.show();
        ListView listView = getListView();
        listView.setVerticalScrollBarEnabled(false);
        listView.setHorizontalScrollBarEnabled(false);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setAlpha(0);
        listView.setDivider(shapeDrawable);
        listView.setDividerHeight(this.res.getDimensionPixelSize(C1893R.dimen.bouncer_user_switcher_popup_divider_height));
        int dimensionPixelSize = this.res.getDimensionPixelSize(C1893R.dimen.bouncer_user_switcher_popup_header_height);
        listView.addHeaderView(createSpacer(dimensionPixelSize), (Object) null, false);
        listView.addFooterView(createSpacer(dimensionPixelSize), (Object) null, false);
        Intrinsics.checkNotNullExpressionValue(listView, "listView");
        setWidth(findMaxWidth(listView));
        super.show();
    }

    private final int findMaxWidth(ListView listView) {
        ListAdapter listAdapter = this.adapter;
        if (listAdapter == null) {
            return 0;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) (((double) this.res.getDisplayMetrics().widthPixels) * 0.25d), Integer.MIN_VALUE);
        int count = listAdapter.getCount();
        int i = 0;
        for (int i2 = 0; i2 < count; i2++) {
            View view = listAdapter.getView(i2, (View) null, listView);
            view.measure(makeMeasureSpec, 0);
            i = Math.max(view.getMeasuredWidth(), i);
        }
        return i;
    }

    private final View createSpacer(int i) {
        return new UserSwitcherPopupMenu$createSpacer$1(i, this.context);
    }
}
