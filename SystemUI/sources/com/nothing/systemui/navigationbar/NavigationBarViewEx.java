package com.nothing.systemui.navigationbar;

import com.android.systemui.navigationbar.NavigationBarInflaterView;

public class NavigationBarViewEx {
    private static final String TAG = "NavigationBarViewEx";

    public void updateNavBarCombination(NavigationBarInflaterView navigationBarInflaterView) {
        if (navigationBarInflaterView != null) {
            navigationBarInflaterView.onLikelyDefaultLayoutChange();
        }
    }
}
