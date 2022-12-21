package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesSilentHeaderNodeControllerFactory */
public final class C2719x9d7acab1 implements Factory<NodeController> {
    private final Provider<SectionHeaderControllerSubcomponent> subcomponentProvider;

    public C2719x9d7acab1(Provider<SectionHeaderControllerSubcomponent> provider) {
        this.subcomponentProvider = provider;
    }

    public NodeController get() {
        return providesSilentHeaderNodeController(this.subcomponentProvider.get());
    }

    public static C2719x9d7acab1 create(Provider<SectionHeaderControllerSubcomponent> provider) {
        return new C2719x9d7acab1(provider);
    }

    public static NodeController providesSilentHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        return (NodeController) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesSilentHeaderNodeController(sectionHeaderControllerSubcomponent));
    }
}
