package com.android.systemui.p012qs.dagger;

import android.view.View;
import com.nothing.systemui.p024qs.NTQSStatusBar;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.dagger.QSFragmentModule_ProvidesNTQSStatusBarFactory */
public final class QSFragmentModule_ProvidesNTQSStatusBarFactory implements Factory<NTQSStatusBar> {
    private final Provider<View> viewProvider;

    public QSFragmentModule_ProvidesNTQSStatusBarFactory(Provider<View> provider) {
        this.viewProvider = provider;
    }

    public NTQSStatusBar get() {
        return providesNTQSStatusBar(this.viewProvider.get());
    }

    public static QSFragmentModule_ProvidesNTQSStatusBarFactory create(Provider<View> provider) {
        return new QSFragmentModule_ProvidesNTQSStatusBarFactory(provider);
    }

    public static NTQSStatusBar providesNTQSStatusBar(View view) {
        return (NTQSStatusBar) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesNTQSStatusBar(view));
    }
}
