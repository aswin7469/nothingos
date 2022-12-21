package com.nothing.systemui.statusbar.phone;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.util.leak.RotationUtils;

public class PhoneStatusBarViewEx {
    public static Pair<Integer, Integer> getNTStatusBarContentInsets(Context context, StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        return statusBarContentInsetsProvider.getStatusBarContentInsetsForRotation(RotationUtils.getExactRotation(context));
    }

    public static void updateSbContentPadding(Context context, View view, int i, int i2, int i3) {
        if (context != null && view != null) {
            if (context.getDisplay().getRotation() == 3) {
                view.setPaddingRelative(i3, i2, i, 0);
            } else {
                view.setPaddingRelative(i, i2, i3, 0);
            }
        }
    }
}
