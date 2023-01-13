package com.android.systemui.statusbar.core;

import android.app.Fragment;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.statusbar.core.StatusBarInitializer;
import com.android.systemui.statusbar.phone.PhoneStatusBarTransitions;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u001c\u0010\b\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016¨\u0006\t"}, mo65043d2 = {"com/android/systemui/statusbar/core/StatusBarInitializer$initializeStatusBar$1", "Lcom/android/systemui/fragments/FragmentHostManager$FragmentListener;", "onFragmentViewCreated", "", "tag", "", "fragment", "Landroid/app/Fragment;", "onFragmentViewDestroyed", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusBarInitializer.kt */
public final class StatusBarInitializer$initializeStatusBar$1 implements FragmentHostManager.FragmentListener {
    final /* synthetic */ StatusBarInitializer this$0;

    public void onFragmentViewDestroyed(String str, Fragment fragment) {
    }

    StatusBarInitializer$initializeStatusBar$1(StatusBarInitializer statusBarInitializer) {
        this.this$0 = statusBarInitializer;
    }

    public void onFragmentViewCreated(String str, Fragment fragment) {
        Intrinsics.checkNotNullParameter(str, "tag");
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        StatusBarFragmentComponent statusBarFragmentComponent = ((CollapsedStatusBarFragment) fragment).getStatusBarFragmentComponent();
        if (statusBarFragmentComponent != null) {
            StatusBarInitializer.OnStatusBarViewUpdatedListener statusBarViewUpdatedListener = this.this$0.getStatusBarViewUpdatedListener();
            if (statusBarViewUpdatedListener != null) {
                PhoneStatusBarView phoneStatusBarView = statusBarFragmentComponent.getPhoneStatusBarView();
                Intrinsics.checkNotNullExpressionValue(phoneStatusBarView, "statusBarFragmentComponent.phoneStatusBarView");
                PhoneStatusBarViewController phoneStatusBarViewController = statusBarFragmentComponent.getPhoneStatusBarViewController();
                Intrinsics.checkNotNullExpressionValue(phoneStatusBarViewController, "statusBarFragmentCompone…neStatusBarViewController");
                PhoneStatusBarTransitions phoneStatusBarTransitions = statusBarFragmentComponent.getPhoneStatusBarTransitions();
                Intrinsics.checkNotNullExpressionValue(phoneStatusBarTransitions, "statusBarFragmentCompone…phoneStatusBarTransitions");
                statusBarViewUpdatedListener.onStatusBarViewUpdated(phoneStatusBarView, phoneStatusBarViewController, phoneStatusBarTransitions);
                return;
            }
            return;
        }
        throw new IllegalStateException();
    }
}
