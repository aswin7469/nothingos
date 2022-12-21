package androidx.mediarouter.media;

import androidx.mediarouter.media.MediaRouteProvider;
import androidx.mediarouter.media.MediaRouteProviderService;
import java.util.Collection;

/* renamed from: androidx.mediarouter.media.MediaRouteProviderService$MediaRouteProviderServiceImplApi30$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C1198x2643a00c implements MediaRouteProvider.DynamicGroupRouteController.OnDynamicRoutesChangedListener {
    public final /* synthetic */ MediaRouteProviderService.MediaRouteProviderServiceImplApi30 f$0;

    public /* synthetic */ C1198x2643a00c(MediaRouteProviderService.MediaRouteProviderServiceImplApi30 mediaRouteProviderServiceImplApi30) {
        this.f$0 = mediaRouteProviderServiceImplApi30;
    }

    public final void onRoutesChanged(MediaRouteProvider.DynamicGroupRouteController dynamicGroupRouteController, MediaRouteDescriptor mediaRouteDescriptor, Collection collection) {
        this.f$0.mo19552xa3de1435(dynamicGroupRouteController, mediaRouteDescriptor, collection);
    }
}
