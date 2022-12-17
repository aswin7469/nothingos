package com.android.settingslib.media;

import android.content.Context;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import android.media.NearbyDevice;
import android.text.TextUtils;
import android.util.Log;

public abstract class MediaDevice implements Comparable<MediaDevice> {
    private int mConnectedRecord;
    protected final Context mContext;
    protected final String mPackageName;
    private int mRangeZone = 0;
    protected final MediaRoute2Info mRouteInfo;
    protected final MediaRouter2Manager mRouterManager;
    private int mState;
    int mType;

    public void disconnect() {
    }

    public abstract String getId();

    public abstract String getName();

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

    /* access modifiers changed from: package-private */
    public void setConnectedRecord() {
        this.mConnectedRecord++;
        ConnectionRecordManager.getInstance().setConnectionRecord(this.mContext, getId(), this.mConnectedRecord);
    }

    public boolean isBLEDevice() {
        return this.mRouteInfo.getType() == 26;
    }

    public int getDeviceType() {
        return this.mType;
    }

    public boolean connect() {
        if (this.mRouteInfo == null) {
            Log.w("MediaDevice", "Unable to connect. RouteInfo is empty");
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

    public boolean equals(Object obj) {
        if (!(obj instanceof MediaDevice)) {
            return false;
        }
        return ((MediaDevice) obj).getId().equals(getId());
    }
}
