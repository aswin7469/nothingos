package com.android.systemui.dreams.complication.dagger;

import android.view.LayoutInflater;
import android.view.View;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DreamClockDateComplicationModule_ProvideComplicationViewFactory implements Factory<View> {
    private final Provider<LayoutInflater> layoutInflaterProvider;

    public DreamClockDateComplicationModule_ProvideComplicationViewFactory(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }

    public View get() {
        return provideComplicationView(this.layoutInflaterProvider.get());
    }

    public static DreamClockDateComplicationModule_ProvideComplicationViewFactory create(Provider<LayoutInflater> provider) {
        return new DreamClockDateComplicationModule_ProvideComplicationViewFactory(provider);
    }

    public static View provideComplicationView(LayoutInflater layoutInflater) {
        return (View) Preconditions.checkNotNullFromProvides(DreamClockDateComplicationModule.provideComplicationView(layoutInflater));
    }
}
