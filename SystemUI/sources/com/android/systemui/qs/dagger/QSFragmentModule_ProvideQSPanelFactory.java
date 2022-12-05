package com.android.systemui.qs.dagger;

import android.view.View;
import com.android.systemui.qs.QSPanel;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFragmentModule_ProvideQSPanelFactory implements Factory<QSPanel> {
    private final Provider<View> viewProvider;

    public QSFragmentModule_ProvideQSPanelFactory(Provider<View> provider) {
        this.viewProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public QSPanel mo1933get() {
        return provideQSPanel(this.viewProvider.mo1933get());
    }

    public static QSFragmentModule_ProvideQSPanelFactory create(Provider<View> provider) {
        return new QSFragmentModule_ProvideQSPanelFactory(provider);
    }

    public static QSPanel provideQSPanel(View view) {
        return (QSPanel) Preconditions.checkNotNullFromProvides(QSFragmentModule.provideQSPanel(view));
    }
}
