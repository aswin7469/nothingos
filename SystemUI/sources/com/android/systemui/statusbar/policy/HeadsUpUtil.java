package com.android.systemui.statusbar.policy;

import android.view.View;
import com.android.systemui.C1893R;

public final class HeadsUpUtil {
    private static final int TAG_CLICKED_NOTIFICATION = 2131428109;

    public static void setNeedsHeadsUpDisappearAnimationAfterClick(View view, boolean z) {
        view.setTag(C1893R.C1897id.is_clicked_heads_up_tag, z ? true : null);
    }

    public static boolean isClickedHeadsUpNotification(View view) {
        Boolean bool = (Boolean) view.getTag(C1893R.C1897id.is_clicked_heads_up_tag);
        return bool != null && bool.booleanValue();
    }
}
