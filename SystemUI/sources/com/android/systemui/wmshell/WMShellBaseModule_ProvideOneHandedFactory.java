package com.android.systemui.wmshell;

import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.onehanded.OneHandedController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideOneHandedFactory implements Factory<Optional<OneHanded>> {
    private final Provider<Optional<OneHandedController>> oneHandedControllerProvider;

    public WMShellBaseModule_ProvideOneHandedFactory(Provider<Optional<OneHandedController>> provider) {
        this.oneHandedControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public Optional<OneHanded> mo1933get() {
        return provideOneHanded(this.oneHandedControllerProvider.mo1933get());
    }

    public static WMShellBaseModule_ProvideOneHandedFactory create(Provider<Optional<OneHandedController>> provider) {
        return new WMShellBaseModule_ProvideOneHandedFactory(provider);
    }

    public static Optional<OneHanded> provideOneHanded(Optional<OneHandedController> optional) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideOneHanded(optional));
    }
}
