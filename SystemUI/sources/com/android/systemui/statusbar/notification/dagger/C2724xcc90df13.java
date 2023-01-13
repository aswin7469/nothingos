package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory */
public final class C2724xcc90df13 implements Factory<SectionHeaderController> {
    private final Provider<SectionHeaderControllerSubcomponent> subcomponentProvider;

    public C2724xcc90df13(Provider<SectionHeaderControllerSubcomponent> provider) {
        this.subcomponentProvider = provider;
    }

    public SectionHeaderController get() {
        return providesSilentHeaderController(this.subcomponentProvider.get());
    }

    public static C2724xcc90df13 create(Provider<SectionHeaderControllerSubcomponent> provider) {
        return new C2724xcc90df13(provider);
    }

    public static SectionHeaderController providesSilentHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        return (SectionHeaderController) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesSilentHeaderController(sectionHeaderControllerSubcomponent));
    }
}
