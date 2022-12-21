package com.android.systemui.media.dream;

import android.content.Context;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.media.MediaDataManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaDreamSentinel_Factory implements Factory<MediaDreamSentinel> {
    private final Provider<MediaDreamComplication> complicationProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
    private final Provider<MediaDataManager> mediaDataManagerProvider;

    public MediaDreamSentinel_Factory(Provider<Context> provider, Provider<MediaDataManager> provider2, Provider<DreamOverlayStateController> provider3, Provider<MediaDreamComplication> provider4) {
        this.contextProvider = provider;
        this.mediaDataManagerProvider = provider2;
        this.dreamOverlayStateControllerProvider = provider3;
        this.complicationProvider = provider4;
    }

    public MediaDreamSentinel get() {
        return newInstance(this.contextProvider.get(), this.mediaDataManagerProvider.get(), this.dreamOverlayStateControllerProvider.get(), this.complicationProvider.get());
    }

    public static MediaDreamSentinel_Factory create(Provider<Context> provider, Provider<MediaDataManager> provider2, Provider<DreamOverlayStateController> provider3, Provider<MediaDreamComplication> provider4) {
        return new MediaDreamSentinel_Factory(provider, provider2, provider3, provider4);
    }

    public static MediaDreamSentinel newInstance(Context context, MediaDataManager mediaDataManager, DreamOverlayStateController dreamOverlayStateController, MediaDreamComplication mediaDreamComplication) {
        return new MediaDreamSentinel(context, mediaDataManager, dreamOverlayStateController, mediaDreamComplication);
    }
}
