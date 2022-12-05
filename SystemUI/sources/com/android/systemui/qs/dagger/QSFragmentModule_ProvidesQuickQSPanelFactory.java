package com.android.systemui.qs.dagger;

import com.android.systemui.qs.QuickQSPanel;
import com.android.systemui.qs.QuickStatusBarHeader;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFragmentModule_ProvidesQuickQSPanelFactory implements Factory<QuickQSPanel> {
    private final Provider<QuickStatusBarHeader> quickStatusBarHeaderProvider;

    public QSFragmentModule_ProvidesQuickQSPanelFactory(Provider<QuickStatusBarHeader> provider) {
        this.quickStatusBarHeaderProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public QuickQSPanel mo1933get() {
        return providesQuickQSPanel(this.quickStatusBarHeaderProvider.mo1933get());
    }

    public static QSFragmentModule_ProvidesQuickQSPanelFactory create(Provider<QuickStatusBarHeader> provider) {
        return new QSFragmentModule_ProvidesQuickQSPanelFactory(provider);
    }

    public static QuickQSPanel providesQuickQSPanel(QuickStatusBarHeader quickStatusBarHeader) {
        return (QuickQSPanel) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesQuickQSPanel(quickStatusBarHeader));
    }
}
