package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.complication.DreamClockTimeComplication;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamClockTimeComplication_Factory implements Factory<DreamClockTimeComplication> {
    private final Provider<DreamClockTimeComplication.DreamClockTimeViewHolder> dreamClockTimeViewHolderProvider;

    public DreamClockTimeComplication_Factory(Provider<DreamClockTimeComplication.DreamClockTimeViewHolder> provider) {
        this.dreamClockTimeViewHolderProvider = provider;
    }

    public DreamClockTimeComplication get() {
        return newInstance(this.dreamClockTimeViewHolderProvider);
    }

    public static DreamClockTimeComplication_Factory create(Provider<DreamClockTimeComplication.DreamClockTimeViewHolder> provider) {
        return new DreamClockTimeComplication_Factory(provider);
    }

    public static DreamClockTimeComplication newInstance(Provider<DreamClockTimeComplication.DreamClockTimeViewHolder> provider) {
        return new DreamClockTimeComplication(provider);
    }
}
