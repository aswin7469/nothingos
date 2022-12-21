package androidx.mediarouter.media;

import android.content.IntentFilter;
import android.media.MediaRoute2Info;
import android.media.RouteDiscoveryPreference;
import android.net.Uri;
import android.os.Bundle;
import androidx.mediarouter.media.MediaRouteDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

class MediaRouter2Utils {
    static final String FEATURE_EMPTY = "android.media.route.feature.EMPTY";
    static final String FEATURE_REMOTE_GROUP_PLAYBACK = "android.media.route.feature.REMOTE_GROUP_PLAYBACK";
    static final String KEY_CONTROL_FILTERS = "androidx.mediarouter.media.KEY_CONTROL_FILTERS";
    static final String KEY_DEVICE_TYPE = "androidx.mediarouter.media.KEY_DEVICE_TYPE";
    static final String KEY_EXTRAS = "androidx.mediarouter.media.KEY_EXTRAS";
    static final String KEY_GROUP_ROUTE = "androidx.mediarouter.media.KEY_GROUP_ROUTE";
    static final String KEY_MESSENGER = "androidx.mediarouter.media.KEY_MESSENGER";
    static final String KEY_ORIGINAL_ROUTE_ID = "androidx.mediarouter.media.KEY_ORIGINAL_ROUTE_ID";
    static final String KEY_PLAYBACK_TYPE = "androidx.mediarouter.media.KEY_PLAYBACK_TYPE";
    static final String KEY_SESSION_NAME = "androidx.mediarouter.media.KEY_SESSION_NAME";

    private MediaRouter2Utils() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0055, code lost:
        if (r1 != 2) goto L_0x0062;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.media.MediaRoute2Info toFwkMediaRoute2Info(androidx.mediarouter.media.MediaRouteDescriptor r4) {
        /*
            if (r4 != 0) goto L_0x0004
            r4 = 0
            return r4
        L_0x0004:
            android.media.MediaRoute2Info$Builder r0 = new android.media.MediaRoute2Info$Builder
            java.lang.String r1 = r4.getId()
            java.lang.String r2 = r4.getName()
            r0.<init>(r1, r2)
            java.lang.String r1 = r4.getDescription()
            android.media.MediaRoute2Info$Builder r0 = r0.setDescription(r1)
            int r1 = r4.getConnectionState()
            android.media.MediaRoute2Info$Builder r0 = r0.setConnectionState(r1)
            int r1 = r4.getVolumeHandling()
            android.media.MediaRoute2Info$Builder r0 = r0.setVolumeHandling(r1)
            int r1 = r4.getVolume()
            android.media.MediaRoute2Info$Builder r0 = r0.setVolume(r1)
            int r1 = r4.getVolumeMax()
            android.media.MediaRoute2Info$Builder r0 = r0.setVolumeMax(r1)
            java.util.List r1 = r4.getControlFilters()
            java.util.Collection r1 = toFeatures(r1)
            android.media.MediaRoute2Info$Builder r0 = r0.addFeatures(r1)
            android.net.Uri r1 = r4.getIconUri()
            android.media.MediaRoute2Info$Builder r0 = r0.setIconUri(r1)
            int r1 = r4.getDeviceType()
            r2 = 1
            if (r1 == r2) goto L_0x0058
            r2 = 2
            if (r1 == r2) goto L_0x005d
            goto L_0x0062
        L_0x0058:
            java.lang.String r1 = "android.media.route.feature.REMOTE_VIDEO_PLAYBACK"
            r0.addFeature(r1)
        L_0x005d:
            java.lang.String r1 = "android.media.route.feature.REMOTE_AUDIO_PLAYBACK"
            r0.addFeature(r1)
        L_0x0062:
            java.util.List r1 = r4.getGroupMemberIds()
            boolean r1 = r1.isEmpty()
            if (r1 != 0) goto L_0x0071
            java.lang.String r1 = "android.media.route.feature.REMOTE_GROUP_PLAYBACK"
            r0.addFeature(r1)
        L_0x0071:
            android.os.Bundle r1 = new android.os.Bundle
            r1.<init>()
            java.lang.String r2 = "androidx.mediarouter.media.KEY_EXTRAS"
            android.os.Bundle r3 = r4.getExtras()
            r1.putBundle(r2, r3)
            java.util.ArrayList r2 = new java.util.ArrayList
            java.util.List r3 = r4.getControlFilters()
            r2.<init>(r3)
            java.lang.String r3 = "androidx.mediarouter.media.KEY_CONTROL_FILTERS"
            r1.putParcelableArrayList(r3, r2)
            java.lang.String r2 = "androidx.mediarouter.media.KEY_DEVICE_TYPE"
            int r3 = r4.getDeviceType()
            r1.putInt(r2, r3)
            java.lang.String r2 = "androidx.mediarouter.media.KEY_PLAYBACK_TYPE"
            int r3 = r4.getPlaybackType()
            r1.putInt(r2, r3)
            java.lang.String r2 = "androidx.mediarouter.media.KEY_ORIGINAL_ROUTE_ID"
            java.lang.String r3 = r4.getId()
            r1.putString(r2, r3)
            r0.setExtras(r1)
            java.util.List r4 = r4.getControlFilters()
            boolean r4 = r4.isEmpty()
            if (r4 == 0) goto L_0x00ba
            java.lang.String r4 = "android.media.route.feature.EMPTY"
            r0.addFeature(r4)
        L_0x00ba:
            android.media.MediaRoute2Info r4 = r0.build()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.media.MediaRouter2Utils.toFwkMediaRoute2Info(androidx.mediarouter.media.MediaRouteDescriptor):android.media.MediaRoute2Info");
    }

    public static MediaRouteDescriptor toMediaRouteDescriptor(MediaRoute2Info mediaRoute2Info) {
        if (mediaRoute2Info == null) {
            return null;
        }
        MediaRouteDescriptor.Builder canDisconnect = new MediaRouteDescriptor.Builder(mediaRoute2Info.getId(), mediaRoute2Info.getName().toString()).setConnectionState(mediaRoute2Info.getConnectionState()).setVolumeHandling(mediaRoute2Info.getVolumeHandling()).setVolumeMax(mediaRoute2Info.getVolumeMax()).setVolume(mediaRoute2Info.getVolume()).setExtras(mediaRoute2Info.getExtras()).setEnabled(true).setCanDisconnect(false);
        CharSequence description = mediaRoute2Info.getDescription();
        if (description != null) {
            canDisconnect.setDescription(description.toString());
        }
        Uri iconUri = mediaRoute2Info.getIconUri();
        if (iconUri != null) {
            canDisconnect.setIconUri(iconUri);
        }
        Bundle extras = mediaRoute2Info.getExtras();
        if (extras == null || !extras.containsKey(KEY_EXTRAS) || !extras.containsKey(KEY_DEVICE_TYPE) || !extras.containsKey(KEY_CONTROL_FILTERS)) {
            return null;
        }
        canDisconnect.setExtras(extras.getBundle(KEY_EXTRAS));
        canDisconnect.setDeviceType(extras.getInt(KEY_DEVICE_TYPE, 0));
        canDisconnect.setPlaybackType(extras.getInt(KEY_PLAYBACK_TYPE, 1));
        ArrayList parcelableArrayList = extras.getParcelableArrayList(KEY_CONTROL_FILTERS);
        if (parcelableArrayList != null) {
            canDisconnect.addControlFilters(parcelableArrayList);
        }
        return canDisconnect.build();
    }

    static Collection<String> toFeatures(List<IntentFilter> list) {
        HashSet hashSet = new HashSet();
        for (IntentFilter next : list) {
            int countCategories = next.countCategories();
            for (int i = 0; i < countCategories; i++) {
                hashSet.add(toRouteFeature(next.getCategory(i)));
            }
        }
        return hashSet;
    }

    static List<IntentFilter> toControlFilters(Collection<String> collection) {
        if (collection == null) {
            return new ArrayList();
        }
        return (List) collection.stream().distinct().map(new MediaRouter2Utils$$ExternalSyntheticLambda3()).collect(Collectors.toList());
    }

    static /* synthetic */ IntentFilter lambda$toControlFilters$0(String str) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(toControlCategory(str));
        return intentFilter;
    }

    static List<String> getRouteIds(List<MediaRoute2Info> list) {
        if (list == null) {
            return new ArrayList();
        }
        return (List) list.stream().filter(new MediaRouter2Utils$$ExternalSyntheticLambda0()).map(new MediaRouter2Utils$$ExternalSyntheticLambda1()).collect(Collectors.toList());
    }

    static RouteDiscoveryPreference toDiscoveryPreference(MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
        if (mediaRouteDiscoveryRequest == null || !mediaRouteDiscoveryRequest.isValid()) {
            return new RouteDiscoveryPreference.Builder(new ArrayList(), false).build();
        }
        return new RouteDiscoveryPreference.Builder((List) mediaRouteDiscoveryRequest.getSelector().getControlCategories().stream().map(new MediaRouter2Utils$$ExternalSyntheticLambda2()).collect(Collectors.toList()), mediaRouteDiscoveryRequest.isActiveScan()).build();
    }

    static String toRouteFeature(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2065577523:
                if (str.equals(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)) {
                    c = 0;
                    break;
                }
                break;
            case 956939050:
                if (str.equals(MediaControlIntent.CATEGORY_LIVE_AUDIO)) {
                    c = 1;
                    break;
                }
                break;
            case 975975375:
                if (str.equals(MediaControlIntent.CATEGORY_LIVE_VIDEO)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return "android.media.route.feature.REMOTE_PLAYBACK";
            case 1:
                return "android.media.route.feature.LIVE_AUDIO";
            case 2:
                return "android.media.route.feature.LIVE_VIDEO";
            default:
                return str;
        }
    }

    static String toControlCategory(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 94496206:
                if (str.equals("android.media.route.feature.REMOTE_PLAYBACK")) {
                    c = 0;
                    break;
                }
                break;
            case 1328964233:
                if (str.equals("android.media.route.feature.LIVE_AUDIO")) {
                    c = 1;
                    break;
                }
                break;
            case 1348000558:
                if (str.equals("android.media.route.feature.LIVE_VIDEO")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return MediaControlIntent.CATEGORY_REMOTE_PLAYBACK;
            case 1:
                return MediaControlIntent.CATEGORY_LIVE_AUDIO;
            case 2:
                return MediaControlIntent.CATEGORY_LIVE_VIDEO;
            default:
                return str;
        }
    }
}
