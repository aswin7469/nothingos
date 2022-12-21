package com.android.systemui.dagger;

import com.android.internal.jank.InteractionJankMonitor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FrameworkServicesModule_ProvideInteractionJankMonitorFactory implements Factory<InteractionJankMonitor> {
    public InteractionJankMonitor get() {
        return provideInteractionJankMonitor();
    }

    public static FrameworkServicesModule_ProvideInteractionJankMonitorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static InteractionJankMonitor provideInteractionJankMonitor() {
        return (InteractionJankMonitor) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideInteractionJankMonitor());
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final FrameworkServicesModule_ProvideInteractionJankMonitorFactory INSTANCE = new FrameworkServicesModule_ProvideInteractionJankMonitorFactory();

        private InstanceHolder() {
        }
    }
}
