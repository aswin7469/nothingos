package com.android.systemui.qs.dagger;

import android.content.Context;
import android.view.View;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFragmentModule_ProvideThemedContextFactory implements Factory<Context> {
    private final Provider<View> viewProvider;

    public QSFragmentModule_ProvideThemedContextFactory(Provider<View> provider) {
        this.viewProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public Context mo1933get() {
        return provideThemedContext(this.viewProvider.mo1933get());
    }

    public static QSFragmentModule_ProvideThemedContextFactory create(Provider<View> provider) {
        return new QSFragmentModule_ProvideThemedContextFactory(provider);
    }

    public static Context provideThemedContext(View view) {
        return (Context) Preconditions.checkNotNullFromProvides(QSFragmentModule.provideThemedContext(view));
    }
}
