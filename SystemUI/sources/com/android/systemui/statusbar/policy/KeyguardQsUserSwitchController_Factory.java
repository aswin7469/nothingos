package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.Resources;
import android.widget.FrameLayout;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.UserDetailView;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class KeyguardQsUserSwitchController_Factory implements Factory<KeyguardQsUserSwitchController> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<ScreenLifecycle> screenLifecycleProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    private final Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;
    private final Provider<UserDetailView.Adapter> userDetailViewAdapterProvider;
    private final Provider<UserSwitcherController> userSwitcherControllerProvider;
    private final Provider<FrameLayout> viewProvider;

    public KeyguardQsUserSwitchController_Factory(Provider<FrameLayout> provider, Provider<Context> provider2, Provider<Resources> provider3, Provider<ScreenLifecycle> provider4, Provider<UserSwitcherController> provider5, Provider<KeyguardStateController> provider6, Provider<FalsingManager> provider7, Provider<ConfigurationController> provider8, Provider<SysuiStatusBarStateController> provider9, Provider<DozeParameters> provider10, Provider<UserDetailView.Adapter> provider11, Provider<UnlockedScreenOffAnimationController> provider12) {
        this.viewProvider = provider;
        this.contextProvider = provider2;
        this.resourcesProvider = provider3;
        this.screenLifecycleProvider = provider4;
        this.userSwitcherControllerProvider = provider5;
        this.keyguardStateControllerProvider = provider6;
        this.falsingManagerProvider = provider7;
        this.configurationControllerProvider = provider8;
        this.statusBarStateControllerProvider = provider9;
        this.dozeParametersProvider = provider10;
        this.userDetailViewAdapterProvider = provider11;
        this.unlockedScreenOffAnimationControllerProvider = provider12;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public KeyguardQsUserSwitchController mo1933get() {
        return newInstance(this.viewProvider.mo1933get(), this.contextProvider.mo1933get(), this.resourcesProvider.mo1933get(), this.screenLifecycleProvider.mo1933get(), this.userSwitcherControllerProvider.mo1933get(), this.keyguardStateControllerProvider.mo1933get(), this.falsingManagerProvider.mo1933get(), this.configurationControllerProvider.mo1933get(), this.statusBarStateControllerProvider.mo1933get(), this.dozeParametersProvider.mo1933get(), this.userDetailViewAdapterProvider, this.unlockedScreenOffAnimationControllerProvider.mo1933get());
    }

    public static KeyguardQsUserSwitchController_Factory create(Provider<FrameLayout> provider, Provider<Context> provider2, Provider<Resources> provider3, Provider<ScreenLifecycle> provider4, Provider<UserSwitcherController> provider5, Provider<KeyguardStateController> provider6, Provider<FalsingManager> provider7, Provider<ConfigurationController> provider8, Provider<SysuiStatusBarStateController> provider9, Provider<DozeParameters> provider10, Provider<UserDetailView.Adapter> provider11, Provider<UnlockedScreenOffAnimationController> provider12) {
        return new KeyguardQsUserSwitchController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static KeyguardQsUserSwitchController newInstance(FrameLayout frameLayout, Context context, Resources resources, ScreenLifecycle screenLifecycle, UserSwitcherController userSwitcherController, KeyguardStateController keyguardStateController, FalsingManager falsingManager, ConfigurationController configurationController, SysuiStatusBarStateController sysuiStatusBarStateController, DozeParameters dozeParameters, Provider<UserDetailView.Adapter> provider, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        return new KeyguardQsUserSwitchController(frameLayout, context, resources, screenLifecycle, userSwitcherController, keyguardStateController, falsingManager, configurationController, sysuiStatusBarStateController, dozeParameters, provider, unlockedScreenOffAnimationController);
    }
}
