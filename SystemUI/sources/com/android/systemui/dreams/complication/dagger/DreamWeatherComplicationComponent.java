package com.android.systemui.dreams.complication.dagger;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.internal.util.Preconditions;
import com.android.systemui.C1894R;
import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import com.android.systemui.dreams.complication.DreamWeatherComplication;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Named;
import javax.inject.Scope;

@Subcomponent(modules = {DreamWeatherComplicationModule.class})
@DreamWeatherComplicationScope
public interface DreamWeatherComplicationComponent {

    @Scope
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DreamWeatherComplicationScope {
    }

    @Subcomponent.Factory
    public interface Factory {
        DreamWeatherComplicationComponent create();
    }

    DreamWeatherComplication.DreamWeatherViewHolder getViewHolder();

    @Module
    public interface DreamWeatherComplicationModule {
        public static final String DREAM_WEATHER_COMPLICATION_LAYOUT_PARAMS = "weather_complication_layout_params";
        public static final String DREAM_WEATHER_COMPLICATION_VIEW = "weather_complication_view";
        public static final int INSERT_ORDER_WEIGHT = 1;

        @DreamWeatherComplicationScope
        @Provides
        @Named("weather_complication_view")
        static TextView provideComplicationView(LayoutInflater layoutInflater) {
            return (TextView) Preconditions.checkNotNull((TextView) layoutInflater.inflate(C1894R.layout.dream_overlay_complication_weather, (ViewGroup) null, false), "R.layout.dream_overlay_complication_weather did not properly inflated");
        }

        @DreamWeatherComplicationScope
        @Provides
        @Named("weather_complication_layout_params")
        static ComplicationLayoutParams provideLayoutParams() {
            return new ComplicationLayoutParams(0, -2, 6, 8, 1);
        }
    }
}
