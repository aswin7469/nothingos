package com.android.systemui.statusbar;

import com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationShelfController_Factory implements Factory<NotificationShelfController> {
    private final Provider<ActivatableNotificationViewController> activatableNotificationViewControllerProvider;
    private final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    private final Provider<NotificationShelf> notificationShelfProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public NotificationShelfController_Factory(Provider<NotificationShelf> provider, Provider<ActivatableNotificationViewController> provider2, Provider<KeyguardBypassController> provider3, Provider<SysuiStatusBarStateController> provider4) {
        this.notificationShelfProvider = provider;
        this.activatableNotificationViewControllerProvider = provider2;
        this.keyguardBypassControllerProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public NotificationShelfController mo1933get() {
        return newInstance(this.notificationShelfProvider.mo1933get(), this.activatableNotificationViewControllerProvider.mo1933get(), this.keyguardBypassControllerProvider.mo1933get(), this.statusBarStateControllerProvider.mo1933get());
    }

    public static NotificationShelfController_Factory create(Provider<NotificationShelf> provider, Provider<ActivatableNotificationViewController> provider2, Provider<KeyguardBypassController> provider3, Provider<SysuiStatusBarStateController> provider4) {
        return new NotificationShelfController_Factory(provider, provider2, provider3, provider4);
    }

    public static NotificationShelfController newInstance(NotificationShelf notificationShelf, ActivatableNotificationViewController activatableNotificationViewController, KeyguardBypassController keyguardBypassController, SysuiStatusBarStateController sysuiStatusBarStateController) {
        return new NotificationShelfController(notificationShelf, activatableNotificationViewController, keyguardBypassController, sysuiStatusBarStateController);
    }
}
