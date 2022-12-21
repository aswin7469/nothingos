package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.TaskViewFactory;
import com.android.p019wm.shell.TaskViewFactoryController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideTaskViewFactoryFactory */
public final class WMShellBaseModule_ProvideTaskViewFactoryFactory implements Factory<Optional<TaskViewFactory>> {
    private final Provider<TaskViewFactoryController> taskViewFactoryControllerProvider;

    public WMShellBaseModule_ProvideTaskViewFactoryFactory(Provider<TaskViewFactoryController> provider) {
        this.taskViewFactoryControllerProvider = provider;
    }

    public Optional<TaskViewFactory> get() {
        return provideTaskViewFactory(this.taskViewFactoryControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideTaskViewFactoryFactory create(Provider<TaskViewFactoryController> provider) {
        return new WMShellBaseModule_ProvideTaskViewFactoryFactory(provider);
    }

    public static Optional<TaskViewFactory> provideTaskViewFactory(TaskViewFactoryController taskViewFactoryController) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideTaskViewFactory(taskViewFactoryController));
    }
}
