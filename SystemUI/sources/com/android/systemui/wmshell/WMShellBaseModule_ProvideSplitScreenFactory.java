package com.android.systemui.wmshell;

import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.splitscreen.SplitScreenController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideSplitScreenFactory implements Factory<Optional<SplitScreen>> {
    private final Provider<Optional<SplitScreenController>> splitScreenControllerProvider;

    public WMShellBaseModule_ProvideSplitScreenFactory(Provider<Optional<SplitScreenController>> provider) {
        this.splitScreenControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public Optional<SplitScreen> mo1933get() {
        return provideSplitScreen(this.splitScreenControllerProvider.mo1933get());
    }

    public static WMShellBaseModule_ProvideSplitScreenFactory create(Provider<Optional<SplitScreenController>> provider) {
        return new WMShellBaseModule_ProvideSplitScreenFactory(provider);
    }

    public static Optional<SplitScreen> provideSplitScreen(Optional<SplitScreenController> optional) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideSplitScreen(optional));
    }
}
