package androidx.mediarouter.media;

import android.media.MediaRouter;

final class MediaRouterApi24 {

    public static final class RouteInfo {
        public static int getDeviceType(Object obj) {
            return ((MediaRouter.RouteInfo) obj).getDeviceType();
        }

        private RouteInfo() {
        }
    }

    private MediaRouterApi24() {
    }
}
