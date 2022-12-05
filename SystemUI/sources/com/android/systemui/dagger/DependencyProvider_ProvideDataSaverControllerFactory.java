package com.android.systemui.dagger;

import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.NetworkController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideDataSaverControllerFactory implements Factory<DataSaverController> {
    private final DependencyProvider module;
    private final Provider<NetworkController> networkControllerProvider;

    public DependencyProvider_ProvideDataSaverControllerFactory(DependencyProvider dependencyProvider, Provider<NetworkController> provider) {
        this.module = dependencyProvider;
        this.networkControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DataSaverController mo1933get() {
        return provideDataSaverController(this.module, this.networkControllerProvider.mo1933get());
    }

    public static DependencyProvider_ProvideDataSaverControllerFactory create(DependencyProvider dependencyProvider, Provider<NetworkController> provider) {
        return new DependencyProvider_ProvideDataSaverControllerFactory(dependencyProvider, provider);
    }

    public static DataSaverController provideDataSaverController(DependencyProvider dependencyProvider, NetworkController networkController) {
        return (DataSaverController) Preconditions.checkNotNullFromProvides(dependencyProvider.provideDataSaverController(networkController));
    }
}
