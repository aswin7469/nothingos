package com.android.systemui.statusbar.dagger;

import android.service.dreams.IDreamManager;
import com.android.systemui.animation.DialogLaunchAnimator;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideDialogLaunchAnimatorFactory */
public final class C2626x721ac0b6 implements Factory<DialogLaunchAnimator> {
    private final Provider<IDreamManager> dreamManagerProvider;

    public C2626x721ac0b6(Provider<IDreamManager> provider) {
        this.dreamManagerProvider = provider;
    }

    public DialogLaunchAnimator get() {
        return provideDialogLaunchAnimator(this.dreamManagerProvider.get());
    }

    public static C2626x721ac0b6 create(Provider<IDreamManager> provider) {
        return new C2626x721ac0b6(provider);
    }

    public static DialogLaunchAnimator provideDialogLaunchAnimator(IDreamManager iDreamManager) {
        return (DialogLaunchAnimator) Preconditions.checkNotNullFromProvides(CentralSurfacesDependenciesModule.provideDialogLaunchAnimator(iDreamManager));
    }
}
