package com.android.systemui.p012qs.dagger;

import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.p012qs.FooterActionsView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSFgsManagerFooterViewFactory */
public final class QSFragmentModule_ProvidesQSFgsManagerFooterViewFactory implements Factory<View> {
    private final Provider<FooterActionsView> footerActionsViewProvider;
    private final Provider<LayoutInflater> layoutInflaterProvider;

    public QSFragmentModule_ProvidesQSFgsManagerFooterViewFactory(Provider<LayoutInflater> provider, Provider<FooterActionsView> provider2) {
        this.layoutInflaterProvider = provider;
        this.footerActionsViewProvider = provider2;
    }

    public View get() {
        return providesQSFgsManagerFooterView(this.layoutInflaterProvider.get(), this.footerActionsViewProvider.get());
    }

    public static QSFragmentModule_ProvidesQSFgsManagerFooterViewFactory create(Provider<LayoutInflater> provider, Provider<FooterActionsView> provider2) {
        return new QSFragmentModule_ProvidesQSFgsManagerFooterViewFactory(provider, provider2);
    }

    public static View providesQSFgsManagerFooterView(LayoutInflater layoutInflater, FooterActionsView footerActionsView) {
        return (View) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesQSFgsManagerFooterView(layoutInflater, footerActionsView));
    }
}