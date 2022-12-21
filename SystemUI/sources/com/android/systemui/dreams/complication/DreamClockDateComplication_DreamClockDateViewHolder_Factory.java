package com.android.systemui.dreams.complication;

import android.view.View;
import com.android.systemui.dreams.complication.DreamClockDateComplication;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamClockDateComplication_DreamClockDateViewHolder_Factory implements Factory<DreamClockDateComplication.DreamClockDateViewHolder> {
    private final Provider<ComplicationLayoutParams> layoutParamsProvider;
    private final Provider<View> viewProvider;

    public DreamClockDateComplication_DreamClockDateViewHolder_Factory(Provider<View> provider, Provider<ComplicationLayoutParams> provider2) {
        this.viewProvider = provider;
        this.layoutParamsProvider = provider2;
    }

    public DreamClockDateComplication.DreamClockDateViewHolder get() {
        return newInstance(this.viewProvider.get(), this.layoutParamsProvider.get());
    }

    public static DreamClockDateComplication_DreamClockDateViewHolder_Factory create(Provider<View> provider, Provider<ComplicationLayoutParams> provider2) {
        return new DreamClockDateComplication_DreamClockDateViewHolder_Factory(provider, provider2);
    }

    public static DreamClockDateComplication.DreamClockDateViewHolder newInstance(View view, ComplicationLayoutParams complicationLayoutParams) {
        return new DreamClockDateComplication.DreamClockDateViewHolder(view, complicationLayoutParams);
    }
}
