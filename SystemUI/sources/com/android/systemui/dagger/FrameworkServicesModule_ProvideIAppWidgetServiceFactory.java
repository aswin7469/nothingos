package com.android.systemui.dagger;

import com.android.internal.appwidget.IAppWidgetService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FrameworkServicesModule_ProvideIAppWidgetServiceFactory implements Factory<IAppWidgetService> {
    public IAppWidgetService get() {
        return provideIAppWidgetService();
    }

    public static FrameworkServicesModule_ProvideIAppWidgetServiceFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IAppWidgetService provideIAppWidgetService() {
        return (IAppWidgetService) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIAppWidgetService());
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final FrameworkServicesModule_ProvideIAppWidgetServiceFactory INSTANCE = new FrameworkServicesModule_ProvideIAppWidgetServiceFactory();

        private InstanceHolder() {
        }
    }
}
