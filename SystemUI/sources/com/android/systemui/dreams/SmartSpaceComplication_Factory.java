package com.android.systemui.dreams;

import android.content.Context;
import com.android.systemui.dreams.smartspace.DreamsSmartspaceController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SmartSpaceComplication_Factory implements Factory<SmartSpaceComplication> {
    private final Provider<Context> contextProvider;
    private final Provider<DreamsSmartspaceController> smartSpaceControllerProvider;

    public SmartSpaceComplication_Factory(Provider<Context> provider, Provider<DreamsSmartspaceController> provider2) {
        this.contextProvider = provider;
        this.smartSpaceControllerProvider = provider2;
    }

    public SmartSpaceComplication get() {
        return newInstance(this.contextProvider.get(), this.smartSpaceControllerProvider.get());
    }

    public static SmartSpaceComplication_Factory create(Provider<Context> provider, Provider<DreamsSmartspaceController> provider2) {
        return new SmartSpaceComplication_Factory(provider, provider2);
    }

    public static SmartSpaceComplication newInstance(Context context, DreamsSmartspaceController dreamsSmartspaceController) {
        return new SmartSpaceComplication(context, dreamsSmartspaceController);
    }
}
