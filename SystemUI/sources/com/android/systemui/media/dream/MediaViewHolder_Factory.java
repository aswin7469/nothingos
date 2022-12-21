package com.android.systemui.media.dream;

import android.widget.FrameLayout;
import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaViewHolder_Factory implements Factory<MediaViewHolder> {
    private final Provider<FrameLayout> containerProvider;
    private final Provider<MediaComplicationViewController> controllerProvider;
    private final Provider<ComplicationLayoutParams> layoutParamsProvider;

    public MediaViewHolder_Factory(Provider<FrameLayout> provider, Provider<MediaComplicationViewController> provider2, Provider<ComplicationLayoutParams> provider3) {
        this.containerProvider = provider;
        this.controllerProvider = provider2;
        this.layoutParamsProvider = provider3;
    }

    public MediaViewHolder get() {
        return newInstance(this.containerProvider.get(), this.controllerProvider.get(), this.layoutParamsProvider.get());
    }

    public static MediaViewHolder_Factory create(Provider<FrameLayout> provider, Provider<MediaComplicationViewController> provider2, Provider<ComplicationLayoutParams> provider3) {
        return new MediaViewHolder_Factory(provider, provider2, provider3);
    }

    public static MediaViewHolder newInstance(FrameLayout frameLayout, MediaComplicationViewController mediaComplicationViewController, ComplicationLayoutParams complicationLayoutParams) {
        return new MediaViewHolder(frameLayout, mediaComplicationViewController, complicationLayoutParams);
    }
}
