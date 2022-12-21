package com.android.settingslib.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import com.android.settingslib.C1757R;
import java.util.List;

public class InfoMediaDevice extends MediaDevice {
    private static final String TAG = "InfoMediaDevice";

    public boolean isConnected() {
        return true;
    }

    InfoMediaDevice(Context context, MediaRouter2Manager mediaRouter2Manager, MediaRoute2Info mediaRoute2Info, String str) {
        super(context, mediaRouter2Manager, mediaRoute2Info, str);
        initDeviceRecord();
    }

    public String getName() {
        return this.mRouteInfo.getName().toString();
    }

    public String getSummary() {
        if (this.mRouteInfo.getClientPackageName() != null) {
            return this.mContext.getString(C1757R.string.bluetooth_active_no_battery_level);
        }
        return null;
    }

    public Drawable getIcon() {
        return getIconWithoutBackground();
    }

    public Drawable getIconWithoutBackground() {
        return this.mContext.getDrawable(getDrawableResIdByFeature());
    }

    /* access modifiers changed from: package-private */
    public int getDrawableResId() {
        int type = this.mRouteInfo.getType();
        if (type == 1001) {
            return C1757R.C1759drawable.ic_media_display_device;
        }
        if (type != 2000) {
            return C1757R.C1759drawable.ic_media_speaker_device;
        }
        return C1757R.C1759drawable.ic_media_group_device;
    }

    /* access modifiers changed from: package-private */
    public int getDrawableResIdByFeature() {
        List features = this.mRouteInfo.getFeatures();
        if (features.contains("android.media.route.feature.REMOTE_GROUP_PLAYBACK")) {
            return C1757R.C1759drawable.ic_media_group_device;
        }
        if (features.contains("android.media.route.feature.REMOTE_VIDEO_PLAYBACK")) {
            return C1757R.C1759drawable.ic_media_display_device;
        }
        return C1757R.C1759drawable.ic_media_speaker_device;
    }

    public String getId() {
        return MediaDeviceUtils.getId(this.mRouteInfo);
    }
}
