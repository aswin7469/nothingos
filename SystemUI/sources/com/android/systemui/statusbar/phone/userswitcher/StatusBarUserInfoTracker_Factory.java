package com.android.systemui.statusbar.phone.userswitcher;

import android.os.UserManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.UserInfoController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class StatusBarUserInfoTracker_Factory implements Factory<StatusBarUserInfoTracker> {
    private final Provider<Executor> backgroundExecutorProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<UserInfoController> userInfoControllerProvider;
    private final Provider<UserManager> userManagerProvider;

    public StatusBarUserInfoTracker_Factory(Provider<UserInfoController> provider, Provider<UserManager> provider2, Provider<DumpManager> provider3, Provider<Executor> provider4, Provider<Executor> provider5) {
        this.userInfoControllerProvider = provider;
        this.userManagerProvider = provider2;
        this.dumpManagerProvider = provider3;
        this.mainExecutorProvider = provider4;
        this.backgroundExecutorProvider = provider5;
    }

    public StatusBarUserInfoTracker get() {
        return newInstance(this.userInfoControllerProvider.get(), this.userManagerProvider.get(), this.dumpManagerProvider.get(), this.mainExecutorProvider.get(), this.backgroundExecutorProvider.get());
    }

    public static StatusBarUserInfoTracker_Factory create(Provider<UserInfoController> provider, Provider<UserManager> provider2, Provider<DumpManager> provider3, Provider<Executor> provider4, Provider<Executor> provider5) {
        return new StatusBarUserInfoTracker_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static StatusBarUserInfoTracker newInstance(UserInfoController userInfoController, UserManager userManager, DumpManager dumpManager, Executor executor, Executor executor2) {
        return new StatusBarUserInfoTracker(userInfoController, userManager, dumpManager, executor, executor2);
    }
}
