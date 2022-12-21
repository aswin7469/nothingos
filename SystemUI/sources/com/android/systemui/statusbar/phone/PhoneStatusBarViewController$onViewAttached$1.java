package com.android.systemui.statusbar.phone;

import android.view.View;
import android.view.ViewTreeObserver;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/android/systemui/statusbar/phone/PhoneStatusBarViewController$onViewAttached$1", "Landroid/view/ViewTreeObserver$OnPreDrawListener;", "onPreDraw", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PhoneStatusBarViewController.kt */
public final class PhoneStatusBarViewController$onViewAttached$1 implements ViewTreeObserver.OnPreDrawListener {
    final /* synthetic */ View[] $viewsToAnimate;
    final /* synthetic */ PhoneStatusBarViewController this$0;

    PhoneStatusBarViewController$onViewAttached$1(PhoneStatusBarViewController phoneStatusBarViewController, View[] viewArr) {
        this.this$0 = phoneStatusBarViewController;
        this.$viewsToAnimate = viewArr;
    }

    public boolean onPreDraw() {
        this.this$0.moveFromCenterAnimationController.onViewsReady(this.$viewsToAnimate);
        ((PhoneStatusBarView) this.this$0.mView).getViewTreeObserver().removeOnPreDrawListener(this);
        return true;
    }
}
