package com.android.systemui.statusbar.phone.fragment.dagger;

import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvidePhoneStatusBarViewControllerFactory */
public final class C3137xfe780fd7 implements Factory<PhoneStatusBarViewController> {
    private final Provider<NotificationPanelViewController> notificationPanelViewControllerProvider;
    private final Provider<PhoneStatusBarViewController.Factory> phoneStatusBarViewControllerFactoryProvider;
    private final Provider<PhoneStatusBarView> phoneStatusBarViewProvider;

    public C3137xfe780fd7(Provider<PhoneStatusBarViewController.Factory> provider, Provider<PhoneStatusBarView> provider2, Provider<NotificationPanelViewController> provider3) {
        this.phoneStatusBarViewControllerFactoryProvider = provider;
        this.phoneStatusBarViewProvider = provider2;
        this.notificationPanelViewControllerProvider = provider3;
    }

    public PhoneStatusBarViewController get() {
        return providePhoneStatusBarViewController(this.phoneStatusBarViewControllerFactoryProvider.get(), this.phoneStatusBarViewProvider.get(), this.notificationPanelViewControllerProvider.get());
    }

    public static C3137xfe780fd7 create(Provider<PhoneStatusBarViewController.Factory> provider, Provider<PhoneStatusBarView> provider2, Provider<NotificationPanelViewController> provider3) {
        return new C3137xfe780fd7(provider, provider2, provider3);
    }

    public static PhoneStatusBarViewController providePhoneStatusBarViewController(PhoneStatusBarViewController.Factory factory, PhoneStatusBarView phoneStatusBarView, NotificationPanelViewController notificationPanelViewController) {
        return (PhoneStatusBarViewController) Preconditions.checkNotNullFromProvides(StatusBarFragmentModule.providePhoneStatusBarViewController(factory, phoneStatusBarView, notificationPanelViewController));
    }
}
