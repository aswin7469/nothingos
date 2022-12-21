package com.nothing.systemui.statusbar.notification.interruption;

import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class HeadsUpControllerEx_Factory implements Factory<HeadsUpControllerEx> {
    private final Provider<HeadsUpController> controllerProvider;
    private final Provider<HeadsUpManager> headsUpManagerProvider;
    private final Provider<HeadsUpViewBinder> headsUpViewBinderProvider;

    public HeadsUpControllerEx_Factory(Provider<HeadsUpController> provider, Provider<HeadsUpManager> provider2, Provider<HeadsUpViewBinder> provider3) {
        this.controllerProvider = provider;
        this.headsUpManagerProvider = provider2;
        this.headsUpViewBinderProvider = provider3;
    }

    public HeadsUpControllerEx get() {
        return newInstance(this.controllerProvider.get(), this.headsUpManagerProvider.get(), this.headsUpViewBinderProvider.get());
    }

    public static HeadsUpControllerEx_Factory create(Provider<HeadsUpController> provider, Provider<HeadsUpManager> provider2, Provider<HeadsUpViewBinder> provider3) {
        return new HeadsUpControllerEx_Factory(provider, provider2, provider3);
    }

    public static HeadsUpControllerEx newInstance(HeadsUpController headsUpController, HeadsUpManager headsUpManager, HeadsUpViewBinder headsUpViewBinder) {
        return new HeadsUpControllerEx(headsUpController, headsUpManager, headsUpViewBinder);
    }
}
