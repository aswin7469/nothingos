package com.android.systemui.dreams.complication.dagger;

import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent_DreamWeatherComplicationModule_ProvideLayoutParamsFactory */
public final class C2094x56e4d2ac implements Factory<ComplicationLayoutParams> {
    public ComplicationLayoutParams get() {
        return provideLayoutParams();
    }

    public static C2094x56e4d2ac create() {
        return InstanceHolder.INSTANCE;
    }

    public static ComplicationLayoutParams provideLayoutParams() {
        return (ComplicationLayoutParams) Preconditions.checkNotNullFromProvides(DreamWeatherComplicationComponent.DreamWeatherComplicationModule.provideLayoutParams());
    }

    /* renamed from: com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent_DreamWeatherComplicationModule_ProvideLayoutParamsFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final C2094x56e4d2ac INSTANCE = new C2094x56e4d2ac();

        private InstanceHolder() {
        }
    }
}
