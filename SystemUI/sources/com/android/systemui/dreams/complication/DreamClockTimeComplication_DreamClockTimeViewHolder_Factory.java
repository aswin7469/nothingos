package com.android.systemui.dreams.complication;

import android.view.View;
import com.android.systemui.dreams.complication.DreamClockTimeComplication;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamClockTimeComplication_DreamClockTimeViewHolder_Factory implements Factory<DreamClockTimeComplication.DreamClockTimeViewHolder> {
    private final Provider<ComplicationLayoutParams> layoutParamsProvider;
    private final Provider<View> viewProvider;

    public DreamClockTimeComplication_DreamClockTimeViewHolder_Factory(Provider<View> provider, Provider<ComplicationLayoutParams> provider2) {
        this.viewProvider = provider;
        this.layoutParamsProvider = provider2;
    }

    public DreamClockTimeComplication.DreamClockTimeViewHolder get() {
        return newInstance(this.viewProvider.get(), this.layoutParamsProvider.get());
    }

    public static DreamClockTimeComplication_DreamClockTimeViewHolder_Factory create(Provider<View> provider, Provider<ComplicationLayoutParams> provider2) {
        return new DreamClockTimeComplication_DreamClockTimeViewHolder_Factory(provider, provider2);
    }

    public static DreamClockTimeComplication.DreamClockTimeViewHolder newInstance(View view, ComplicationLayoutParams complicationLayoutParams) {
        return new DreamClockTimeComplication.DreamClockTimeViewHolder(view, complicationLayoutParams);
    }
}
