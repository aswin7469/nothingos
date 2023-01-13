package com.android.systemui.statusbar.phone.fragment.dagger;

import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherContainer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvideStatusBarUserSwitcherContainerFactory */
public final class C3138x6673e9b7 implements Factory<StatusBarUserSwitcherContainer> {
    private final Provider<PhoneStatusBarView> viewProvider;

    public C3138x6673e9b7(Provider<PhoneStatusBarView> provider) {
        this.viewProvider = provider;
    }

    public StatusBarUserSwitcherContainer get() {
        return provideStatusBarUserSwitcherContainer(this.viewProvider.get());
    }

    public static C3138x6673e9b7 create(Provider<PhoneStatusBarView> provider) {
        return new C3138x6673e9b7(provider);
    }

    public static StatusBarUserSwitcherContainer provideStatusBarUserSwitcherContainer(PhoneStatusBarView phoneStatusBarView) {
        return (StatusBarUserSwitcherContainer) Preconditions.checkNotNullFromProvides(StatusBarFragmentModule.provideStatusBarUserSwitcherContainer(phoneStatusBarView));
    }
}
