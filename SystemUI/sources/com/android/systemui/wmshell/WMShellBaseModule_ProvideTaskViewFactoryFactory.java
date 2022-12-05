package com.android.systemui.wmshell;

import com.android.wm.shell.TaskViewFactory;
import com.android.wm.shell.TaskViewFactoryController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideTaskViewFactoryFactory implements Factory<Optional<TaskViewFactory>> {
    private final Provider<TaskViewFactoryController> taskViewFactoryControllerProvider;

    public WMShellBaseModule_ProvideTaskViewFactoryFactory(Provider<TaskViewFactoryController> provider) {
        this.taskViewFactoryControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public Optional<TaskViewFactory> mo1933get() {
        return provideTaskViewFactory(this.taskViewFactoryControllerProvider.mo1933get());
    }

    public static WMShellBaseModule_ProvideTaskViewFactoryFactory create(Provider<TaskViewFactoryController> provider) {
        return new WMShellBaseModule_ProvideTaskViewFactoryFactory(provider);
    }

    public static Optional<TaskViewFactory> provideTaskViewFactory(TaskViewFactoryController taskViewFactoryController) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideTaskViewFactory(taskViewFactoryController));
    }
}
