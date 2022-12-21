package com.android.systemui.dreams.complication.dagger;

import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class DreamClockTimeComplicationModule_ProvideLayoutParamsFactory implements Factory<ComplicationLayoutParams> {
    public ComplicationLayoutParams get() {
        return provideLayoutParams();
    }

    public static DreamClockTimeComplicationModule_ProvideLayoutParamsFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ComplicationLayoutParams provideLayoutParams() {
        return (ComplicationLayoutParams) Preconditions.checkNotNullFromProvides(DreamClockTimeComplicationModule.provideLayoutParams());
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final DreamClockTimeComplicationModule_ProvideLayoutParamsFactory INSTANCE = new DreamClockTimeComplicationModule_ProvideLayoutParamsFactory();

        private InstanceHolder() {
        }
    }
}
