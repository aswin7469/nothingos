package com.android.systemui.dreams.complication;

import android.content.Context;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.DreamClockTimeComplication;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamClockTimeComplication_Registrant_Factory implements Factory<DreamClockTimeComplication.Registrant> {
    private final Provider<Context> contextProvider;
    private final Provider<DreamClockTimeComplication> dreamClockTimeComplicationProvider;
    private final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;

    public DreamClockTimeComplication_Registrant_Factory(Provider<Context> provider, Provider<DreamOverlayStateController> provider2, Provider<DreamClockTimeComplication> provider3) {
        this.contextProvider = provider;
        this.dreamOverlayStateControllerProvider = provider2;
        this.dreamClockTimeComplicationProvider = provider3;
    }

    public DreamClockTimeComplication.Registrant get() {
        return newInstance(this.contextProvider.get(), this.dreamOverlayStateControllerProvider.get(), this.dreamClockTimeComplicationProvider.get());
    }

    public static DreamClockTimeComplication_Registrant_Factory create(Provider<Context> provider, Provider<DreamOverlayStateController> provider2, Provider<DreamClockTimeComplication> provider3) {
        return new DreamClockTimeComplication_Registrant_Factory(provider, provider2, provider3);
    }

    public static DreamClockTimeComplication.Registrant newInstance(Context context, DreamOverlayStateController dreamOverlayStateController, DreamClockTimeComplication dreamClockTimeComplication) {
        return new DreamClockTimeComplication.Registrant(context, dreamOverlayStateController, dreamClockTimeComplication);
    }
}
