package com.android.systemui.dreams;

import android.content.Context;
import com.android.systemui.dreams.SmartSpaceComplication;
import com.android.systemui.dreams.smartspace.DreamsSmartspaceController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SmartSpaceComplication_Registrant_Factory implements Factory<SmartSpaceComplication.Registrant> {
    private final Provider<Context> contextProvider;
    private final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
    private final Provider<SmartSpaceComplication> smartSpaceComplicationProvider;
    private final Provider<DreamsSmartspaceController> smartSpaceControllerProvider;

    public SmartSpaceComplication_Registrant_Factory(Provider<Context> provider, Provider<DreamOverlayStateController> provider2, Provider<SmartSpaceComplication> provider3, Provider<DreamsSmartspaceController> provider4) {
        this.contextProvider = provider;
        this.dreamOverlayStateControllerProvider = provider2;
        this.smartSpaceComplicationProvider = provider3;
        this.smartSpaceControllerProvider = provider4;
    }

    public SmartSpaceComplication.Registrant get() {
        return newInstance(this.contextProvider.get(), this.dreamOverlayStateControllerProvider.get(), this.smartSpaceComplicationProvider.get(), this.smartSpaceControllerProvider.get());
    }

    public static SmartSpaceComplication_Registrant_Factory create(Provider<Context> provider, Provider<DreamOverlayStateController> provider2, Provider<SmartSpaceComplication> provider3, Provider<DreamsSmartspaceController> provider4) {
        return new SmartSpaceComplication_Registrant_Factory(provider, provider2, provider3, provider4);
    }

    public static SmartSpaceComplication.Registrant newInstance(Context context, DreamOverlayStateController dreamOverlayStateController, SmartSpaceComplication smartSpaceComplication, DreamsSmartspaceController dreamsSmartspaceController) {
        return new SmartSpaceComplication.Registrant(context, dreamOverlayStateController, smartSpaceComplication, dreamsSmartspaceController);
    }
}
