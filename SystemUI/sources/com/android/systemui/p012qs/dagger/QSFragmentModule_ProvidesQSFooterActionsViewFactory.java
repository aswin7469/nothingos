package com.android.systemui.p012qs.dagger;

import android.view.View;
import com.android.systemui.p012qs.FooterActionsView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSFooterActionsViewFactory */
public final class QSFragmentModule_ProvidesQSFooterActionsViewFactory implements Factory<FooterActionsView> {
    private final Provider<View> viewProvider;

    public QSFragmentModule_ProvidesQSFooterActionsViewFactory(Provider<View> provider) {
        this.viewProvider = provider;
    }

    public FooterActionsView get() {
        return providesQSFooterActionsView(this.viewProvider.get());
    }

    public static QSFragmentModule_ProvidesQSFooterActionsViewFactory create(Provider<View> provider) {
        return new QSFragmentModule_ProvidesQSFooterActionsViewFactory(provider);
    }

    public static FooterActionsView providesQSFooterActionsView(View view) {
        return (FooterActionsView) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesQSFooterActionsView(view));
    }
}
