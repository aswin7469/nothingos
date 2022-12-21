package com.android.systemui.statusbar.phone.userswitcher;

import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.p012qs.user.UserSwitchDialogController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarUserSwitcherControllerImpl_Factory implements Factory<StatusBarUserSwitcherControllerImpl> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<StatusBarUserSwitcherFeatureController> featureControllerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<StatusBarUserInfoTracker> trackerProvider;
    private final Provider<UserSwitchDialogController> userSwitcherDialogControllerProvider;
    private final Provider<StatusBarUserSwitcherContainer> viewProvider;

    public StatusBarUserSwitcherControllerImpl_Factory(Provider<StatusBarUserSwitcherContainer> provider, Provider<StatusBarUserInfoTracker> provider2, Provider<StatusBarUserSwitcherFeatureController> provider3, Provider<UserSwitchDialogController> provider4, Provider<FeatureFlags> provider5, Provider<ActivityStarter> provider6, Provider<FalsingManager> provider7) {
        this.viewProvider = provider;
        this.trackerProvider = provider2;
        this.featureControllerProvider = provider3;
        this.userSwitcherDialogControllerProvider = provider4;
        this.featureFlagsProvider = provider5;
        this.activityStarterProvider = provider6;
        this.falsingManagerProvider = provider7;
    }

    public StatusBarUserSwitcherControllerImpl get() {
        return newInstance(this.viewProvider.get(), this.trackerProvider.get(), this.featureControllerProvider.get(), this.userSwitcherDialogControllerProvider.get(), this.featureFlagsProvider.get(), this.activityStarterProvider.get(), this.falsingManagerProvider.get());
    }

    public static StatusBarUserSwitcherControllerImpl_Factory create(Provider<StatusBarUserSwitcherContainer> provider, Provider<StatusBarUserInfoTracker> provider2, Provider<StatusBarUserSwitcherFeatureController> provider3, Provider<UserSwitchDialogController> provider4, Provider<FeatureFlags> provider5, Provider<ActivityStarter> provider6, Provider<FalsingManager> provider7) {
        return new StatusBarUserSwitcherControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static StatusBarUserSwitcherControllerImpl newInstance(StatusBarUserSwitcherContainer statusBarUserSwitcherContainer, StatusBarUserInfoTracker statusBarUserInfoTracker, StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController, UserSwitchDialogController userSwitchDialogController, FeatureFlags featureFlags, ActivityStarter activityStarter, FalsingManager falsingManager) {
        return new StatusBarUserSwitcherControllerImpl(statusBarUserSwitcherContainer, statusBarUserInfoTracker, statusBarUserSwitcherFeatureController, userSwitchDialogController, featureFlags, activityStarter, falsingManager);
    }
}
