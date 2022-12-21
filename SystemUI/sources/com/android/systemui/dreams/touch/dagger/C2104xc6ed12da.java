package com.android.systemui.dreams.touch.dagger;

import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.dreams.touch.HideComplicationTouchHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.dreams.touch.dagger.HideComplicationModule_ProvidesHideComplicationTouchHandlerFactory */
public final class C2104xc6ed12da implements Factory<DreamTouchHandler> {
    private final Provider<HideComplicationTouchHandler> touchHandlerProvider;

    public C2104xc6ed12da(Provider<HideComplicationTouchHandler> provider) {
        this.touchHandlerProvider = provider;
    }

    public DreamTouchHandler get() {
        return providesHideComplicationTouchHandler(this.touchHandlerProvider.get());
    }

    public static C2104xc6ed12da create(Provider<HideComplicationTouchHandler> provider) {
        return new C2104xc6ed12da(provider);
    }

    public static DreamTouchHandler providesHideComplicationTouchHandler(HideComplicationTouchHandler hideComplicationTouchHandler) {
        return (DreamTouchHandler) Preconditions.checkNotNullFromProvides(HideComplicationModule.providesHideComplicationTouchHandler(hideComplicationTouchHandler));
    }
}
