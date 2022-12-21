package com.android.settingslib.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import android.media.NearbyDevice;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class MediaDevice implements Comparable<MediaDevice> {
    private static final String TAG = "MediaDevice";
    private int mConnectedRecord;
    protected final Context mContext;
    protected final String mPackageName;
    private int mRangeZone = 0;
    protected final MediaRoute2Info mRouteInfo;
    protected final MediaRouter2Manager mRouterManager;
    private int mState;
    int mType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaDeviceType {
        public static final int TYPE_3POINT5_MM_AUDIO_DEVICE = 3;
        public static final int TYPE_BLUETOOTH_DEVICE = 5;
        public static final int TYPE_CAST_DEVICE = 6;
        public static final int TYPE_CAST_GROUP_DEVICE = 7;
        public static final int TYPE_FAST_PAIR_BLUETOOTH_DEVICE = 4;
        public static final int TYPE_PHONE_DEVICE = 1;
        public static final int TYPE_UNKNOWN = 0;
        public static final int TYPE_USB_C_AUDIO_DEVICE = 2;
    }

    public void disconnect() {
    }

    public abstract Drawable getIcon();

    public abstract Drawable getIconWithoutBackground();

    public abstract String getId();

    public abstract String getName();

    public abstract String getSummary();

    /* access modifiers changed from: protected */
    public boolean isCarKitDevice() {
        return false;
    }

    public abstract boolean isConnected();

    /* access modifiers changed from: protected */
    public boolean isFastPairDevice() {
        return false;
    }

    public boolean isMutingExpectedDevice() {
        return false;
    }

    MediaDevice(Context context, MediaRouter2Manager mediaRouter2Manager, MediaRoute2Info mediaRoute2Info, String str) {
        this.mContext = context;
        this.mRouteInfo = mediaRoute2Info;
        this.mRouterManager = mediaRouter2Manager;
        this.mPackageName = str;
        setType(mediaRoute2Info);
    }

    private void setType(MediaRoute2Info mediaRoute2Info) {
        if (mediaRoute2Info == null) {
            this.mType = 5;
            return;
        }
        int type = mediaRoute2Info.getType();
        if (type == 2) {
            this.mType = 1;
        } else if (type == 3 || type == 4) {
            this.mType = 3;
        } else {
            if (type != 8) {
                if (!(type == 9 || type == 22)) {
                    if (!(type == 23 || type == 26)) {
                        if (type != 2000) {
                            switch (type) {
                                case 11:
                                case 12:
                                case 13:
                                    break;
                                default:
                                    this.mType = 6;
                                    return;
                            }
                        } else {
                            this.mType = 7;
                            return;
                        }
                    }
                }
                this.mType = 2;
                return;
            }
            this.mType = 5;
        }
    }

    /* access modifiers changed from: package-private */
    public void initDeviceRecord() {
        ConnectionRecordManager.getInstance().fetchLastSelectedDevice(this.mContext);
        this.mConnectedRecord = ConnectionRecordManager.getInstance().fetchConnectionRecord(this.mContext, getId());
    }

    public int getRangeZone() {
        return this.mRangeZone;
    }

    public void setRangeZone(int i) {
        this.mRangeZone = i;
    }

    /* access modifiers changed from: package-private */
    public void setConnectedRecord() {
        this.mConnectedRecord++;
        ConnectionRecordManager.getInstance().setConnectionRecord(this.mContext, getId(), this.mConnectedRecord);
    }

    public void requestSetVolume(int i) {
        MediaRoute2Info mediaRoute2Info = this.mRouteInfo;
        if (mediaRoute2Info == null) {
            Log.w(TAG, "Unable to set volume. RouteInfo is empty");
        } else {
            this.mRouterManager.setRouteVolume(mediaRoute2Info, i);
        }
    }

    public int getMaxVolume() {
        MediaRoute2Info mediaRoute2Info = this.mRouteInfo;
        if (mediaRoute2Info != null) {
            return mediaRoute2Info.getVolumeMax();
        }
        Log.w(TAG, "Unable to get max volume. RouteInfo is empty");
        return 0;
    }

    public int getCurrentVolume() {
        MediaRoute2Info mediaRoute2Info = this.mRouteInfo;
        if (mediaRoute2Info != null) {
            return mediaRoute2Info.getVolume();
        }
        Log.w(TAG, "Unable to get current volume. RouteInfo is empty");
        return 0;
    }

    public String getClientPackageName() {
        MediaRoute2Info mediaRoute2Info = this.mRouteInfo;
        if (mediaRoute2Info != null) {
            return mediaRoute2Info.getClientPackageName();
        }
        Log.w(TAG, "Unable to get client package name. RouteInfo is empty");
        return null;
    }

    public boolean isBLEDevice() {
        return this.mRouteInfo.getType() == 26;
    }

    public int getDeviceType() {
        return this.mType;
    }

    public boolean isVolumeFixed() {
        MediaRoute2Info mediaRoute2Info = this.mRouteInfo;
        if (mediaRoute2Info == null) {
            Log.w(TAG, "RouteInfo is empty, regarded as volume fixed.");
            return true;
        } else if (mediaRoute2Info.getVolumeHandling() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean connect() {
        if (this.mRouteInfo == null) {
            Log.w(TAG, "Unable to connect. RouteInfo is empty");
            return false;
        }
        setConnectedRecord();
        this.mRouterManager.selectRoute(this.mPackageName, this.mRouteInfo);
        return true;
    }

    public void setState(int i) {
        this.mState = i;
    }

    public int getState() {
        return this.mState;
    }

    public int compareTo(MediaDevice mediaDevice) {
        if (mediaDevice == null) {
            return -1;
        }
        if (isConnected() ^ mediaDevice.isConnected()) {
            return isConnected() ? -1 : 1;
        }
        if (getState() == 4) {
            return -1;
        }
        if (mediaDevice.getState() == 4) {
            return 1;
        }
        int i = this.mType;
        int i2 = mediaDevice.mType;
        if (i == i2) {
            if (isMutingExpectedDevice()) {
                return -1;
            }
            if (mediaDevice.isMutingExpectedDevice()) {
                return 1;
            }
            if (isFastPairDevice()) {
                return -1;
            }
            if (mediaDevice.isFastPairDevice()) {
                return 1;
            }
            if (isCarKitDevice()) {
                return -1;
            }
            if (mediaDevice.isCarKitDevice()) {
                return 1;
            }
            if (NearbyDevice.compareRangeZones(getRangeZone(), mediaDevice.getRangeZone()) != 0) {
                return NearbyDevice.compareRangeZones(getRangeZone(), mediaDevice.getRangeZone());
            }
            String lastSelectedDevice = ConnectionRecordManager.getInstance().getLastSelectedDevice();
            if (TextUtils.equals(lastSelectedDevice, getId())) {
                return -1;
            }
            if (TextUtils.equals(lastSelectedDevice, mediaDevice.getId())) {
                return 1;
            }
            int i3 = this.mConnectedRecord;
            int i4 = mediaDevice.mConnectedRecord;
            if (i3 == i4 || (i4 <= 0 && i3 <= 0)) {
                return getName().compareToIgnoreCase(mediaDevice.getName());
            }
            return i4 - i3;
        } else if (i < i2) {
            return -1;
        } else {
            return 1;
        }
    }

    public List<String> getFeatures() {
        MediaRoute2Info mediaRoute2Info = this.mRouteInfo;
        if (mediaRoute2Info != null) {
            return mediaRoute2Info.getFeatures();
        }
        Log.w(TAG, "Unable to get features. RouteInfo is empty");
        return new ArrayList();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MediaDevice)) {
            return false;
        }
        return ((MediaDevice) obj).getId().equals(getId());
    }
}
