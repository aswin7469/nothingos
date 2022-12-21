package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.compatui.CompatUI;
import com.android.p019wm.shell.compatui.CompatUIController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideCompatUIFactory */
public final class WMShellBaseModule_ProvideCompatUIFactory implements Factory<Optional<CompatUI>> {
    private final Provider<CompatUIController> compatUIControllerProvider;

    public WMShellBaseModule_ProvideCompatUIFactory(Provider<CompatUIController> provider) {
        this.compatUIControllerProvider = provider;
    }

    public Optional<CompatUI> get() {
        return provideCompatUI(this.compatUIControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideCompatUIFactory create(Provider<CompatUIController> provider) {
        return new WMShellBaseModule_ProvideCompatUIFactory(provider);
    }

    public static Optional<CompatUI> provideCompatUI(CompatUIController compatUIController) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideCompatUI(compatUIController));
    }
}
