package com.android.systemui.media.dream;

import com.android.systemui.media.dream.dagger.MediaComplicationComponent;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaDreamComplication_Factory implements Factory<MediaDreamComplication> {
    private final Provider<MediaComplicationComponent.Factory> componentFactoryProvider;

    public MediaDreamComplication_Factory(Provider<MediaComplicationComponent.Factory> provider) {
        this.componentFactoryProvider = provider;
    }

    public MediaDreamComplication get() {
        return newInstance(this.componentFactoryProvider.get());
    }

    public static MediaDreamComplication_Factory create(Provider<MediaComplicationComponent.Factory> provider) {
        return new MediaDreamComplication_Factory(provider);
    }

    public static MediaDreamComplication newInstance(MediaComplicationComponent.Factory factory) {
        return new MediaDreamComplication(factory);
    }
}
