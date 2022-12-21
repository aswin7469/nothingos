package com.android.systemui.dreams.smartspace;

import android.app.smartspace.SmartspaceManager;
import android.content.Context;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.smartspace.SmartspacePrecondition;
import com.android.systemui.smartspace.SmartspaceTargetFilter;
import com.android.systemui.smartspace.dagger.SmartspaceViewComponent;
import com.android.systemui.util.concurrency.Execution;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DreamsSmartspaceController_Factory implements Factory<DreamsSmartspaceController> {
    private final Provider<Context> contextProvider;
    private final Provider<Execution> executionProvider;
    private final Provider<Optional<BcSmartspaceDataPlugin>> optionalPluginProvider;
    private final Provider<Optional<SmartspaceTargetFilter>> optionalTargetFilterProvider;
    private final Provider<SmartspacePrecondition> preconditionProvider;
    private final Provider<SmartspaceManager> smartspaceManagerProvider;
    private final Provider<SmartspaceViewComponent.Factory> smartspaceViewComponentFactoryProvider;
    private final Provider<Executor> uiExecutorProvider;

    public DreamsSmartspaceController_Factory(Provider<Context> provider, Provider<SmartspaceManager> provider2, Provider<Execution> provider3, Provider<Executor> provider4, Provider<SmartspaceViewComponent.Factory> provider5, Provider<SmartspacePrecondition> provider6, Provider<Optional<SmartspaceTargetFilter>> provider7, Provider<Optional<BcSmartspaceDataPlugin>> provider8) {
        this.contextProvider = provider;
        this.smartspaceManagerProvider = provider2;
        this.executionProvider = provider3;
        this.uiExecutorProvider = provider4;
        this.smartspaceViewComponentFactoryProvider = provider5;
        this.preconditionProvider = provider6;
        this.optionalTargetFilterProvider = provider7;
        this.optionalPluginProvider = provider8;
    }

    public DreamsSmartspaceController get() {
        return newInstance(this.contextProvider.get(), this.smartspaceManagerProvider.get(), this.executionProvider.get(), this.uiExecutorProvider.get(), this.smartspaceViewComponentFactoryProvider.get(), this.preconditionProvider.get(), this.optionalTargetFilterProvider.get(), this.optionalPluginProvider.get());
    }

    public static DreamsSmartspaceController_Factory create(Provider<Context> provider, Provider<SmartspaceManager> provider2, Provider<Execution> provider3, Provider<Executor> provider4, Provider<SmartspaceViewComponent.Factory> provider5, Provider<SmartspacePrecondition> provider6, Provider<Optional<SmartspaceTargetFilter>> provider7, Provider<Optional<BcSmartspaceDataPlugin>> provider8) {
        return new DreamsSmartspaceController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static DreamsSmartspaceController newInstance(Context context, SmartspaceManager smartspaceManager, Execution execution, Executor executor, SmartspaceViewComponent.Factory factory, SmartspacePrecondition smartspacePrecondition, Optional<SmartspaceTargetFilter> optional, Optional<BcSmartspaceDataPlugin> optional2) {
        return new DreamsSmartspaceController(context, smartspaceManager, execution, executor, factory, smartspacePrecondition, optional, optional2);
    }
}
