package com.android.systemui.dreams.complication;

import android.content.Context;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.DreamClockDateComplication;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamClockDateComplication_Registrant_Factory implements Factory<DreamClockDateComplication.Registrant> {
    private final Provider<Context> contextProvider;
    private final Provider<DreamClockDateComplication> dreamClockDateComplicationProvider;
    private final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;

    public DreamClockDateComplication_Registrant_Factory(Provider<Context> provider, Provider<DreamOverlayStateController> provider2, Provider<DreamClockDateComplication> provider3) {
        this.contextProvider = provider;
        this.dreamOverlayStateControllerProvider = provider2;
        this.dreamClockDateComplicationProvider = provider3;
    }

    public DreamClockDateComplication.Registrant get() {
        return newInstance(this.contextProvider.get(), this.dreamOverlayStateControllerProvider.get(), this.dreamClockDateComplicationProvider.get());
    }

    public static DreamClockDateComplication_Registrant_Factory create(Provider<Context> provider, Provider<DreamOverlayStateController> provider2, Provider<DreamClockDateComplication> provider3) {
        return new DreamClockDateComplication_Registrant_Factory(provider, provider2, provider3);
    }

    public static DreamClockDateComplication.Registrant newInstance(Context context, DreamOverlayStateController dreamOverlayStateController, DreamClockDateComplication dreamClockDateComplication) {
        return new DreamClockDateComplication.Registrant(context, dreamOverlayStateController, dreamClockDateComplication);
    }
}
