package com.android.systemui.dreams.complication;

import android.content.Context;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.DreamWeatherComplication;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamWeatherComplication_Registrant_Factory implements Factory<DreamWeatherComplication.Registrant> {
    private final Provider<Context> contextProvider;
    private final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
    private final Provider<DreamWeatherComplication> dreamWeatherComplicationProvider;
    private final Provider<LockscreenSmartspaceController> smartspaceControllerProvider;

    public DreamWeatherComplication_Registrant_Factory(Provider<Context> provider, Provider<LockscreenSmartspaceController> provider2, Provider<DreamOverlayStateController> provider3, Provider<DreamWeatherComplication> provider4) {
        this.contextProvider = provider;
        this.smartspaceControllerProvider = provider2;
        this.dreamOverlayStateControllerProvider = provider3;
        this.dreamWeatherComplicationProvider = provider4;
    }

    public DreamWeatherComplication.Registrant get() {
        return newInstance(this.contextProvider.get(), this.smartspaceControllerProvider.get(), this.dreamOverlayStateControllerProvider.get(), this.dreamWeatherComplicationProvider.get());
    }

    public static DreamWeatherComplication_Registrant_Factory create(Provider<Context> provider, Provider<LockscreenSmartspaceController> provider2, Provider<DreamOverlayStateController> provider3, Provider<DreamWeatherComplication> provider4) {
        return new DreamWeatherComplication_Registrant_Factory(provider, provider2, provider3, provider4);
    }

    public static DreamWeatherComplication.Registrant newInstance(Context context, LockscreenSmartspaceController lockscreenSmartspaceController, DreamOverlayStateController dreamOverlayStateController, DreamWeatherComplication dreamWeatherComplication) {
        return new DreamWeatherComplication.Registrant(context, lockscreenSmartspaceController, dreamOverlayStateController, dreamWeatherComplication);
    }
}
