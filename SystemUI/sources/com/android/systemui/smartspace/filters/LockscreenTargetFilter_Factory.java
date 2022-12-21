package com.android.systemui.smartspace.filters;

import android.content.ContentResolver;
import android.os.Handler;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class LockscreenTargetFilter_Factory implements Factory<LockscreenTargetFilter> {
    private final Provider<ContentResolver> contentResolverProvider;
    private final Provider<Execution> executionProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<Executor> uiExecutorProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public LockscreenTargetFilter_Factory(Provider<SecureSettings> provider, Provider<UserTracker> provider2, Provider<Execution> provider3, Provider<Handler> provider4, Provider<ContentResolver> provider5, Provider<Executor> provider6) {
        this.secureSettingsProvider = provider;
        this.userTrackerProvider = provider2;
        this.executionProvider = provider3;
        this.handlerProvider = provider4;
        this.contentResolverProvider = provider5;
        this.uiExecutorProvider = provider6;
    }

    public LockscreenTargetFilter get() {
        return newInstance(this.secureSettingsProvider.get(), this.userTrackerProvider.get(), this.executionProvider.get(), this.handlerProvider.get(), this.contentResolverProvider.get(), this.uiExecutorProvider.get());
    }

    public static LockscreenTargetFilter_Factory create(Provider<SecureSettings> provider, Provider<UserTracker> provider2, Provider<Execution> provider3, Provider<Handler> provider4, Provider<ContentResolver> provider5, Provider<Executor> provider6) {
        return new LockscreenTargetFilter_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static LockscreenTargetFilter newInstance(SecureSettings secureSettings, UserTracker userTracker, Execution execution, Handler handler, ContentResolver contentResolver, Executor executor) {
        return new LockscreenTargetFilter(secureSettings, userTracker, execution, handler, contentResolver, executor);
    }
}
