package com.android.systemui.statusbar;

import com.android.systemui.plugins.p011qs.C2301QS;
import com.android.systemui.statusbar.SplitShadeLockScreenOverScroller;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;

public final class SplitShadeLockScreenOverScroller_Factory_Impl implements SplitShadeLockScreenOverScroller.Factory {
    private final C4825SplitShadeLockScreenOverScroller_Factory delegateFactory;

    SplitShadeLockScreenOverScroller_Factory_Impl(C4825SplitShadeLockScreenOverScroller_Factory splitShadeLockScreenOverScroller_Factory) {
        this.delegateFactory = splitShadeLockScreenOverScroller_Factory;
    }

    public SplitShadeLockScreenOverScroller create(C2301QS qs, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return this.delegateFactory.get(qs, notificationStackScrollLayoutController);
    }

    public static Provider<SplitShadeLockScreenOverScroller.Factory> create(C4825SplitShadeLockScreenOverScroller_Factory splitShadeLockScreenOverScroller_Factory) {
        return InstanceFactory.create(new SplitShadeLockScreenOverScroller_Factory_Impl(splitShadeLockScreenOverScroller_Factory));
    }
}
