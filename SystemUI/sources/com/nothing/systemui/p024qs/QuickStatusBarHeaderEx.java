package com.nothing.systemui.p024qs;

import android.content.Context;
import android.util.IndentingPrintWriter;
import android.view.View;
import android.widget.RelativeLayout;
import com.android.systemui.C1893R;
import com.android.systemui.p012qs.QuickStatusBarHeader;

/* renamed from: com.nothing.systemui.qs.QuickStatusBarHeaderEx */
public class QuickStatusBarHeaderEx {
    private boolean mMediaVisible;
    private View mPullDownArrow;
    private RelativeLayout mQSStatusBarLayout;

    private static String visibilityToString(int i) {
        return i == 0 ? "VISIBLE" : i == 4 ? "INVISIBLE" : "GONE";
    }

    public void init(Context context, QuickStatusBarHeader quickStatusBarHeader) {
        this.mQSStatusBarLayout = (RelativeLayout) quickStatusBarHeader.findViewById(C1893R.C1897id.qs_status_bar_layout);
        this.mPullDownArrow = quickStatusBarHeader.findViewById(C1893R.C1897id.arrow_down);
    }

    public void updateMediaVisibility(Context context, boolean z, boolean z2) {
        this.mMediaVisible = z;
        updatePullDownArrowVisibility(context, z2);
    }

    public void updatePullDownArrowVisibility(Context context, boolean z) {
        View view = this.mPullDownArrow;
        if (view != null) {
            view.setVisibility((this.mMediaVisible || !context.getResources().getBoolean(C1893R.bool.config_use_split_notification_shade) || z) ? 8 : 0);
        }
    }

    public void updatePortQSStatusBarVisibility(Context context) {
        this.mQSStatusBarLayout.setVisibility(context.getResources().getBoolean(C1893R.bool.config_use_split_notification_shade) ? 8 : 0);
    }

    public void setScrollY(int i) {
        this.mQSStatusBarLayout.setScrollY(i);
    }

    public RelativeLayout getQSStatusBar() {
        return this.mQSStatusBarLayout;
    }

    public void dump(IndentingPrintWriter indentingPrintWriter, View view, View view2) {
        indentingPrintWriter.println("mDatePrivacyView visibility: " + visibilityToString(view.getVisibility()));
        indentingPrintWriter.println("mStatusIconsView visibility: " + visibilityToString(view2.getVisibility()));
    }
}
