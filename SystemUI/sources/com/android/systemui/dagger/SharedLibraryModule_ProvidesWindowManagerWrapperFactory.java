package com.android.systemui.dagger;

import com.android.systemui.shared.system.WindowManagerWrapper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class SharedLibraryModule_ProvidesWindowManagerWrapperFactory implements Factory<WindowManagerWrapper> {
    private final SharedLibraryModule module;

    public SharedLibraryModule_ProvidesWindowManagerWrapperFactory(SharedLibraryModule sharedLibraryModule) {
        this.module = sharedLibraryModule;
    }

    public WindowManagerWrapper get() {
        return providesWindowManagerWrapper(this.module);
    }

    public static SharedLibraryModule_ProvidesWindowManagerWrapperFactory create(SharedLibraryModule sharedLibraryModule) {
        return new SharedLibraryModule_ProvidesWindowManagerWrapperFactory(sharedLibraryModule);
    }

    public static WindowManagerWrapper providesWindowManagerWrapper(SharedLibraryModule sharedLibraryModule) {
        return (WindowManagerWrapper) Preconditions.checkNotNullFromProvides(sharedLibraryModule.providesWindowManagerWrapper());
    }
}
