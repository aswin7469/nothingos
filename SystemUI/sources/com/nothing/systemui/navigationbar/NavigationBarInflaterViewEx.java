package com.nothing.systemui.navigationbar;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import com.android.systemui.C1893R;
import com.nothing.NtFeaturesUtils;

public class NavigationBarInflaterViewEx {
    public static final String NAV_BAR_COMBINATION = "settings_navi_bar_combination";
    private static final int NAV_BAR_COMBINATION_TYPE_RECENT_LEFT_OF_HOME = 1;
    private static final int NAV_BAR_COMBINATION_TYPE_RECENT_RIGHT_OF_HOME = 0;
    private static final String TAG = "NavigationBarInflaterViewEx";

    public String getDefaultLayout(Context context, boolean z, boolean z2) {
        int intForUser = Settings.Secure.getIntForUser(context.getContentResolver(), NAV_BAR_COMBINATION, 0, -2);
        Log.d(TAG, "navbarCombination = " + intForUser);
        int i = (!NtFeaturesUtils.isSupport(new int[]{0}) || intForUser != 1) ? C1893R.string.config_navBarLayout : C1893R.string.config_navBarLayout_recent_left;
        if (z) {
            i = C1893R.string.config_navBarLayoutHandle;
        } else if (z2) {
            i = C1893R.string.config_navBarLayoutQuickstep;
        }
        return context.getString(i);
    }
}
