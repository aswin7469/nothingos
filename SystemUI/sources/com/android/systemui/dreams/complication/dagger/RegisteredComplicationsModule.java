package com.android.systemui.dreams.complication.dagger;

import dagger.Module;

@Module(includes = {DreamClockDateComplicationModule.class, DreamClockTimeComplicationModule.class}, subcomponents = {DreamWeatherComplicationComponent.class})
public interface RegisteredComplicationsModule {
}
