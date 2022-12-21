package com.android.systemui.volume;

import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import java.util.List;

public class MediaRouterWrapper {
    private final MediaRouter mRouter;

    public MediaRouterWrapper(MediaRouter mediaRouter) {
        this.mRouter = mediaRouter;
    }

    public void addCallback(MediaRouteSelector mediaRouteSelector, MediaRouter.Callback callback, int i) {
        this.mRouter.addCallback(mediaRouteSelector, callback, i);
    }

    public void removeCallback(MediaRouter.Callback callback) {
        this.mRouter.removeCallback(callback);
    }

    public void unselect(int i) {
        this.mRouter.unselect(i);
    }

    public List<MediaRouter.RouteInfo> getRoutes() {
        return this.mRouter.getRoutes();
    }
}
