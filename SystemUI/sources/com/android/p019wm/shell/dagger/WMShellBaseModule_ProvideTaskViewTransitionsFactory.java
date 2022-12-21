package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.TaskViewTransitions;
import com.android.p019wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideTaskViewTransitionsFactory */
public final class WMShellBaseModule_ProvideTaskViewTransitionsFactory implements Factory<TaskViewTransitions> {
    private final Provider<Transitions> transitionsProvider;

    public WMShellBaseModule_ProvideTaskViewTransitionsFactory(Provider<Transitions> provider) {
        this.transitionsProvider = provider;
    }

    public TaskViewTransitions get() {
        return provideTaskViewTransitions(this.transitionsProvider.get());
    }

    public static WMShellBaseModule_ProvideTaskViewTransitionsFactory create(Provider<Transitions> provider) {
        return new WMShellBaseModule_ProvideTaskViewTransitionsFactory(provider);
    }

    public static TaskViewTransitions provideTaskViewTransitions(Transitions transitions) {
        return (TaskViewTransitions) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideTaskViewTransitions(transitions));
    }
}
