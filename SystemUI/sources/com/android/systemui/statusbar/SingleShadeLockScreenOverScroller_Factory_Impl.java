package com.android.systemui.statusbar;

import com.android.systemui.statusbar.SingleShadeLockScreenOverScroller;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;

public final class SingleShadeLockScreenOverScroller_Factory_Impl implements SingleShadeLockScreenOverScroller.Factory {
    private final C4824SingleShadeLockScreenOverScroller_Factory delegateFactory;

    SingleShadeLockScreenOverScroller_Factory_Impl(C4824SingleShadeLockScreenOverScroller_Factory singleShadeLockScreenOverScroller_Factory) {
        this.delegateFactory = singleShadeLockScreenOverScroller_Factory;
    }

    public SingleShadeLockScreenOverScroller create(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return this.delegateFactory.get(notificationStackScrollLayoutController);
    }

    public static Provider<SingleShadeLockScreenOverScroller.Factory> create(C4824SingleShadeLockScreenOverScroller_Factory singleShadeLockScreenOverScroller_Factory) {
        return InstanceFactory.create(new SingleShadeLockScreenOverScroller_Factory_Impl(singleShadeLockScreenOverScroller_Factory));
    }
}
