package com.android.systemui.statusbar.window;

import android.view.LayoutInflater;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.window.StatusBarWindowModule_Companion_ProvidesStatusBarWindowViewFactory */
public final class C3217x1f42e6fb implements Factory<StatusBarWindowView> {
    private final Provider<LayoutInflater> layoutInflaterProvider;

    public C3217x1f42e6fb(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }

    public StatusBarWindowView get() {
        return providesStatusBarWindowView(this.layoutInflaterProvider.get());
    }

    public static C3217x1f42e6fb create(Provider<LayoutInflater> provider) {
        return new C3217x1f42e6fb(provider);
    }

    public static StatusBarWindowView providesStatusBarWindowView(LayoutInflater layoutInflater) {
        return (StatusBarWindowView) Preconditions.checkNotNullFromProvides(StatusBarWindowModule.Companion.providesStatusBarWindowView(layoutInflater));
    }
}
