package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.complication.Complication;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ComplicationViewModel_Factory implements Factory<ComplicationViewModel> {
    private final Provider<Complication> complicationProvider;
    private final Provider<Complication.Host> hostProvider;
    private final Provider<ComplicationId> idProvider;

    public ComplicationViewModel_Factory(Provider<Complication> provider, Provider<ComplicationId> provider2, Provider<Complication.Host> provider3) {
        this.complicationProvider = provider;
        this.idProvider = provider2;
        this.hostProvider = provider3;
    }

    public ComplicationViewModel get() {
        return newInstance(this.complicationProvider.get(), this.idProvider.get(), this.hostProvider.get());
    }

    public static ComplicationViewModel_Factory create(Provider<Complication> provider, Provider<ComplicationId> provider2, Provider<Complication.Host> provider3) {
        return new ComplicationViewModel_Factory(provider, provider2, provider3);
    }

    public static ComplicationViewModel newInstance(Complication complication, ComplicationId complicationId, Complication.Host host) {
        return new ComplicationViewModel(complication, complicationId, host);
    }
}
