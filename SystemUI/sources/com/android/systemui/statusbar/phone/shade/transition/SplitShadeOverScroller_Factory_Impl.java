package com.android.systemui.statusbar.phone.shade.transition;

import com.android.systemui.plugins.p011qs.C2301QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.shade.transition.SplitShadeOverScroller;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;

public final class SplitShadeOverScroller_Factory_Impl implements SplitShadeOverScroller.Factory {
    private final C4829SplitShadeOverScroller_Factory delegateFactory;

    SplitShadeOverScroller_Factory_Impl(C4829SplitShadeOverScroller_Factory splitShadeOverScroller_Factory) {
        this.delegateFactory = splitShadeOverScroller_Factory;
    }

    public SplitShadeOverScroller create(C2301QS qs, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return this.delegateFactory.get(qs, notificationStackScrollLayoutController);
    }

    public static Provider<SplitShadeOverScroller.Factory> create(C4829SplitShadeOverScroller_Factory splitShadeOverScroller_Factory) {
        return InstanceFactory.create(new SplitShadeOverScroller_Factory_Impl(splitShadeOverScroller_Factory));
    }
}