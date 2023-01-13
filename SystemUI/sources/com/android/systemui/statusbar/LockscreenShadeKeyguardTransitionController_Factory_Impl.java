package com.android.systemui.statusbar;

import com.android.systemui.statusbar.LockscreenShadeKeyguardTransitionController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;

public final class LockscreenShadeKeyguardTransitionController_Factory_Impl implements LockscreenShadeKeyguardTransitionController.Factory {
    private final C4835LockscreenShadeKeyguardTransitionController_Factory delegateFactory;

    LockscreenShadeKeyguardTransitionController_Factory_Impl(C4835LockscreenShadeKeyguardTransitionController_Factory lockscreenShadeKeyguardTransitionController_Factory) {
        this.delegateFactory = lockscreenShadeKeyguardTransitionController_Factory;
    }

    public LockscreenShadeKeyguardTransitionController create(NotificationPanelViewController notificationPanelViewController) {
        return this.delegateFactory.get(notificationPanelViewController);
    }

    public static Provider<LockscreenShadeKeyguardTransitionController.Factory> create(C4835LockscreenShadeKeyguardTransitionController_Factory lockscreenShadeKeyguardTransitionController_Factory) {
        return InstanceFactory.create(new LockscreenShadeKeyguardTransitionController_Factory_Impl(lockscreenShadeKeyguardTransitionController_Factory));
    }
}
