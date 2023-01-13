package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesAlertingHeaderControllerFactory */
public final class C2715x41b9fd82 implements Factory<SectionHeaderController> {
    private final Provider<SectionHeaderControllerSubcomponent> subcomponentProvider;

    public C2715x41b9fd82(Provider<SectionHeaderControllerSubcomponent> provider) {
        this.subcomponentProvider = provider;
    }

    public SectionHeaderController get() {
        return providesAlertingHeaderController(this.subcomponentProvider.get());
    }

    public static C2715x41b9fd82 create(Provider<SectionHeaderControllerSubcomponent> provider) {
        return new C2715x41b9fd82(provider);
    }

    public static SectionHeaderController providesAlertingHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        return (SectionHeaderController) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesAlertingHeaderController(sectionHeaderControllerSubcomponent));
    }
}
