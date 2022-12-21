package com.android.systemui.dreams.complication.dagger;

import android.view.LayoutInflater;
import android.widget.TextView;
import com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent_DreamWeatherComplicationModule_ProvideComplicationViewFactory */
public final class C2093x7019a799 implements Factory<TextView> {
    private final Provider<LayoutInflater> layoutInflaterProvider;

    public C2093x7019a799(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }

    public TextView get() {
        return provideComplicationView(this.layoutInflaterProvider.get());
    }

    public static C2093x7019a799 create(Provider<LayoutInflater> provider) {
        return new C2093x7019a799(provider);
    }

    public static TextView provideComplicationView(LayoutInflater layoutInflater) {
        return (TextView) Preconditions.checkNotNullFromProvides(DreamWeatherComplicationComponent.DreamWeatherComplicationModule.provideComplicationView(layoutInflater));
    }
}
