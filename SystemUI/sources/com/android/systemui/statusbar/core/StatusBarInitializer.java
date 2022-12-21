package com.android.systemui.statusbar.core;

import com.android.systemui.C1893R;
import com.android.systemui.statusbar.phone.PhoneStatusBarTransitions;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001\u000fB\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, mo64987d2 = {"Lcom/android/systemui/statusbar/core/StatusBarInitializer;", "", "windowController", "Lcom/android/systemui/statusbar/window/StatusBarWindowController;", "(Lcom/android/systemui/statusbar/window/StatusBarWindowController;)V", "statusBarViewUpdatedListener", "Lcom/android/systemui/statusbar/core/StatusBarInitializer$OnStatusBarViewUpdatedListener;", "getStatusBarViewUpdatedListener", "()Lcom/android/systemui/statusbar/core/StatusBarInitializer$OnStatusBarViewUpdatedListener;", "setStatusBarViewUpdatedListener", "(Lcom/android/systemui/statusbar/core/StatusBarInitializer$OnStatusBarViewUpdatedListener;)V", "initializeStatusBar", "", "centralSurfacesComponent", "Lcom/android/systemui/statusbar/phone/dagger/CentralSurfacesComponent;", "OnStatusBarViewUpdatedListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@CentralSurfacesComponent.CentralSurfacesScope
/* compiled from: StatusBarInitializer.kt */
public final class StatusBarInitializer {
    private OnStatusBarViewUpdatedListener statusBarViewUpdatedListener;
    private final StatusBarWindowController windowController;

    @Metadata(mo64986d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\nÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/core/StatusBarInitializer$OnStatusBarViewUpdatedListener;", "", "onStatusBarViewUpdated", "", "statusBarView", "Lcom/android/systemui/statusbar/phone/PhoneStatusBarView;", "statusBarViewController", "Lcom/android/systemui/statusbar/phone/PhoneStatusBarViewController;", "statusBarTransitions", "Lcom/android/systemui/statusbar/phone/PhoneStatusBarTransitions;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: StatusBarInitializer.kt */
    public interface OnStatusBarViewUpdatedListener {
        void onStatusBarViewUpdated(PhoneStatusBarView phoneStatusBarView, PhoneStatusBarViewController phoneStatusBarViewController, PhoneStatusBarTransitions phoneStatusBarTransitions);
    }

    @Inject
    public StatusBarInitializer(StatusBarWindowController statusBarWindowController) {
        Intrinsics.checkNotNullParameter(statusBarWindowController, "windowController");
        this.windowController = statusBarWindowController;
    }

    public final OnStatusBarViewUpdatedListener getStatusBarViewUpdatedListener() {
        return this.statusBarViewUpdatedListener;
    }

    public final void setStatusBarViewUpdatedListener(OnStatusBarViewUpdatedListener onStatusBarViewUpdatedListener) {
        this.statusBarViewUpdatedListener = onStatusBarViewUpdatedListener;
    }

    public final void initializeStatusBar(CentralSurfacesComponent centralSurfacesComponent) {
        Intrinsics.checkNotNullParameter(centralSurfacesComponent, "centralSurfacesComponent");
        this.windowController.getFragmentHostManager().addTagListener(CollapsedStatusBarFragment.TAG, new StatusBarInitializer$initializeStatusBar$1(this)).getFragmentManager().beginTransaction().replace(C1893R.C1897id.status_bar_container, centralSurfacesComponent.createCollapsedStatusBarFragment(), CollapsedStatusBarFragment.TAG).commit();
    }
}
