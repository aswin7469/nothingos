package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.complication.DreamClockDateComplication;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamClockDateComplication_Factory implements Factory<DreamClockDateComplication> {
    private final Provider<DreamClockDateComplication.DreamClockDateViewHolder> dreamClockDateViewHolderProvider;

    public DreamClockDateComplication_Factory(Provider<DreamClockDateComplication.DreamClockDateViewHolder> provider) {
        this.dreamClockDateViewHolderProvider = provider;
    }

    public DreamClockDateComplication get() {
        return newInstance(this.dreamClockDateViewHolderProvider);
    }

    public static DreamClockDateComplication_Factory create(Provider<DreamClockDateComplication.DreamClockDateViewHolder> provider) {
        return new DreamClockDateComplication_Factory(provider);
    }

    public static DreamClockDateComplication newInstance(Provider<DreamClockDateComplication.DreamClockDateViewHolder> provider) {
        return new DreamClockDateComplication(provider);
    }
}
