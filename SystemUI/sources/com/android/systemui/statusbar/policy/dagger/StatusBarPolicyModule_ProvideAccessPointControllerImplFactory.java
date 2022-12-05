package com.android.systemui.statusbar.policy.dagger;

import android.os.UserManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.AccessPointControllerImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class StatusBarPolicyModule_ProvideAccessPointControllerImplFactory implements Factory<AccessPointControllerImpl> {
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<UserManager> userManagerProvider;
    private final Provider<UserTracker> userTrackerProvider;
    private final Provider<AccessPointControllerImpl.WifiPickerTrackerFactory> wifiPickerTrackerFactoryProvider;

    public StatusBarPolicyModule_ProvideAccessPointControllerImplFactory(Provider<UserManager> provider, Provider<UserTracker> provider2, Provider<Executor> provider3, Provider<AccessPointControllerImpl.WifiPickerTrackerFactory> provider4) {
        this.userManagerProvider = provider;
        this.userTrackerProvider = provider2;
        this.mainExecutorProvider = provider3;
        this.wifiPickerTrackerFactoryProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public AccessPointControllerImpl mo1933get() {
        return provideAccessPointControllerImpl(this.userManagerProvider.mo1933get(), this.userTrackerProvider.mo1933get(), this.mainExecutorProvider.mo1933get(), this.wifiPickerTrackerFactoryProvider.mo1933get());
    }

    public static StatusBarPolicyModule_ProvideAccessPointControllerImplFactory create(Provider<UserManager> provider, Provider<UserTracker> provider2, Provider<Executor> provider3, Provider<AccessPointControllerImpl.WifiPickerTrackerFactory> provider4) {
        return new StatusBarPolicyModule_ProvideAccessPointControllerImplFactory(provider, provider2, provider3, provider4);
    }

    public static AccessPointControllerImpl provideAccessPointControllerImpl(UserManager userManager, UserTracker userTracker, Executor executor, AccessPointControllerImpl.WifiPickerTrackerFactory wifiPickerTrackerFactory) {
        return (AccessPointControllerImpl) Preconditions.checkNotNullFromProvides(StatusBarPolicyModule.provideAccessPointControllerImpl(userManager, userTracker, executor, wifiPickerTrackerFactory));
    }
}
