package com.android.systemui.p012qs.dagger;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSUsingCollapsedLandscapeMediaFactory */
public final class QSFragmentModule_ProvidesQSUsingCollapsedLandscapeMediaFactory implements Factory<Boolean> {
    private final Provider<Context> contextProvider;

    public QSFragmentModule_ProvidesQSUsingCollapsedLandscapeMediaFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public Boolean get() {
        return Boolean.valueOf(providesQSUsingCollapsedLandscapeMedia(this.contextProvider.get()));
    }

    public static QSFragmentModule_ProvidesQSUsingCollapsedLandscapeMediaFactory create(Provider<Context> provider) {
        return new QSFragmentModule_ProvidesQSUsingCollapsedLandscapeMediaFactory(provider);
    }

    public static boolean providesQSUsingCollapsedLandscapeMedia(Context context) {
        return QSFragmentModule.providesQSUsingCollapsedLandscapeMedia(context);
    }
}
