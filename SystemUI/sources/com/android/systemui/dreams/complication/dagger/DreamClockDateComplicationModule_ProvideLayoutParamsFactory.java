package com.android.systemui.dreams.complication.dagger;

import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class DreamClockDateComplicationModule_ProvideLayoutParamsFactory implements Factory<ComplicationLayoutParams> {
    public ComplicationLayoutParams get() {
        return provideLayoutParams();
    }

    public static DreamClockDateComplicationModule_ProvideLayoutParamsFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ComplicationLayoutParams provideLayoutParams() {
        return (ComplicationLayoutParams) Preconditions.checkNotNullFromProvides(DreamClockDateComplicationModule.provideLayoutParams());
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final DreamClockDateComplicationModule_ProvideLayoutParamsFactory INSTANCE = new DreamClockDateComplicationModule_ProvideLayoutParamsFactory();

        private InstanceHolder() {
        }
    }
}
