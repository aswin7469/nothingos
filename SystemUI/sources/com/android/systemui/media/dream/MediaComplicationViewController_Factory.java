package com.android.systemui.media.dream;

import android.widget.FrameLayout;
import com.android.systemui.media.MediaHost;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaComplicationViewController_Factory implements Factory<MediaComplicationViewController> {
    private final Provider<MediaHost> mediaHostProvider;
    private final Provider<FrameLayout> viewProvider;

    public MediaComplicationViewController_Factory(Provider<FrameLayout> provider, Provider<MediaHost> provider2) {
        this.viewProvider = provider;
        this.mediaHostProvider = provider2;
    }

    public MediaComplicationViewController get() {
        return newInstance(this.viewProvider.get(), this.mediaHostProvider.get());
    }

    public static MediaComplicationViewController_Factory create(Provider<FrameLayout> provider, Provider<MediaHost> provider2) {
        return new MediaComplicationViewController_Factory(provider, provider2);
    }

    public static MediaComplicationViewController newInstance(FrameLayout frameLayout, MediaHost mediaHost) {
        return new MediaComplicationViewController(frameLayout, mediaHost);
    }
}
