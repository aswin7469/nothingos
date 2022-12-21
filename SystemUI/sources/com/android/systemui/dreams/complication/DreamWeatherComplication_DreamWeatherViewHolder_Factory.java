package com.android.systemui.dreams.complication;

import android.widget.TextView;
import com.android.systemui.dreams.complication.DreamWeatherComplication;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamWeatherComplication_DreamWeatherViewHolder_Factory implements Factory<DreamWeatherComplication.DreamWeatherViewHolder> {
    private final Provider<DreamWeatherComplication.DreamWeatherViewController> controllerProvider;
    private final Provider<ComplicationLayoutParams> layoutParamsProvider;
    private final Provider<TextView> viewProvider;

    public DreamWeatherComplication_DreamWeatherViewHolder_Factory(Provider<TextView> provider, Provider<DreamWeatherComplication.DreamWeatherViewController> provider2, Provider<ComplicationLayoutParams> provider3) {
        this.viewProvider = provider;
        this.controllerProvider = provider2;
        this.layoutParamsProvider = provider3;
    }

    public DreamWeatherComplication.DreamWeatherViewHolder get() {
        return newInstance(this.viewProvider.get(), this.controllerProvider.get(), this.layoutParamsProvider.get());
    }

    public static DreamWeatherComplication_DreamWeatherViewHolder_Factory create(Provider<TextView> provider, Provider<DreamWeatherComplication.DreamWeatherViewController> provider2, Provider<ComplicationLayoutParams> provider3) {
        return new DreamWeatherComplication_DreamWeatherViewHolder_Factory(provider, provider2, provider3);
    }

    public static DreamWeatherComplication.DreamWeatherViewHolder newInstance(TextView textView, Object obj, ComplicationLayoutParams complicationLayoutParams) {
        return new DreamWeatherComplication.DreamWeatherViewHolder(textView, (DreamWeatherComplication.DreamWeatherViewController) obj, complicationLayoutParams);
    }
}
