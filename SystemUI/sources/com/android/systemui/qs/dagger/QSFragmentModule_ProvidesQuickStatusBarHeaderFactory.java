package com.android.systemui.qs.dagger;

import android.view.View;
import com.android.systemui.qs.QuickStatusBarHeader;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFragmentModule_ProvidesQuickStatusBarHeaderFactory implements Factory<QuickStatusBarHeader> {
    private final Provider<View> viewProvider;

    public QSFragmentModule_ProvidesQuickStatusBarHeaderFactory(Provider<View> provider) {
        this.viewProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public QuickStatusBarHeader mo1933get() {
        return providesQuickStatusBarHeader(this.viewProvider.mo1933get());
    }

    public static QSFragmentModule_ProvidesQuickStatusBarHeaderFactory create(Provider<View> provider) {
        return new QSFragmentModule_ProvidesQuickStatusBarHeaderFactory(provider);
    }

    public static QuickStatusBarHeader providesQuickStatusBarHeader(View view) {
        return (QuickStatusBarHeader) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesQuickStatusBarHeader(view));
    }
}
