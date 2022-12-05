package com.android.systemui.dagger;

import android.content.Context;
import android.content.om.OverlayManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.theme.ThemeOverlayApplier;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideThemeOverlayManagerFactory implements Factory<ThemeOverlayApplier> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<OverlayManager> overlayManagerProvider;

    public DependencyProvider_ProvideThemeOverlayManagerFactory(Provider<Context> provider, Provider<Executor> provider2, Provider<Executor> provider3, Provider<OverlayManager> provider4, Provider<DumpManager> provider5) {
        this.contextProvider = provider;
        this.bgExecutorProvider = provider2;
        this.mainExecutorProvider = provider3;
        this.overlayManagerProvider = provider4;
        this.dumpManagerProvider = provider5;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public ThemeOverlayApplier mo1933get() {
        return provideThemeOverlayManager(this.contextProvider.mo1933get(), this.bgExecutorProvider.mo1933get(), this.mainExecutorProvider.mo1933get(), this.overlayManagerProvider.mo1933get(), this.dumpManagerProvider.mo1933get());
    }

    public static DependencyProvider_ProvideThemeOverlayManagerFactory create(Provider<Context> provider, Provider<Executor> provider2, Provider<Executor> provider3, Provider<OverlayManager> provider4, Provider<DumpManager> provider5) {
        return new DependencyProvider_ProvideThemeOverlayManagerFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static ThemeOverlayApplier provideThemeOverlayManager(Context context, Executor executor, Executor executor2, OverlayManager overlayManager, DumpManager dumpManager) {
        return (ThemeOverlayApplier) Preconditions.checkNotNullFromProvides(DependencyProvider.provideThemeOverlayManager(context, executor, executor2, overlayManager, dumpManager));
    }
}
