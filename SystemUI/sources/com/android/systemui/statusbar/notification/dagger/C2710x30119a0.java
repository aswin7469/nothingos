package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesAlertingHeaderNodeControllerFactory */
public final class C2710x30119a0 implements Factory<NodeController> {
    private final Provider<SectionHeaderControllerSubcomponent> subcomponentProvider;

    public C2710x30119a0(Provider<SectionHeaderControllerSubcomponent> provider) {
        this.subcomponentProvider = provider;
    }

    public NodeController get() {
        return providesAlertingHeaderNodeController(this.subcomponentProvider.get());
    }

    public static C2710x30119a0 create(Provider<SectionHeaderControllerSubcomponent> provider) {
        return new C2710x30119a0(provider);
    }

    public static NodeController providesAlertingHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        return (NodeController) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesAlertingHeaderNodeController(sectionHeaderControllerSubcomponent));
    }
}
