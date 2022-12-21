package com.android.settingslib.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import com.android.settingslib.C1757R;

public class PhoneMediaDevice extends MediaDevice {
    public static final String PHONE_ID = "phone_media_device_id";
    private static final String TAG = "PhoneMediaDevice";
    public static final String USB_HEADSET_ID = "usb_headset_media_device_id";
    public static final String WIRED_HEADSET_ID = "wired_headset_media_device_id";
    private final DeviceIconUtil mDeviceIconUtil = new DeviceIconUtil();
    private String mSummary = "";

    public boolean isConnected() {
        return true;
    }

    PhoneMediaDevice(Context context, MediaRouter2Manager mediaRouter2Manager, MediaRoute2Info mediaRoute2Info, String str) {
        super(context, mediaRouter2Manager, mediaRoute2Info, str);
        initDeviceRecord();
    }

    public String getName() {
        CharSequence charSequence;
        int type = this.mRouteInfo.getType();
        if (!(type == 3 || type == 4)) {
            if (type != 9) {
                if (type != 22) {
                    switch (type) {
                        case 11:
                        case 12:
                            break;
                        case 13:
                            break;
                        default:
                            charSequence = this.mContext.getString(C1757R.string.media_transfer_this_device_name);
                            break;
                    }
                }
            }
            charSequence = this.mRouteInfo.getName();
            return charSequence.toString();
        }
        charSequence = this.mContext.getString(C1757R.string.media_transfer_wired_usb_device_name);
        return charSequence.toString();
    }

    public String getSummary() {
        return this.mSummary;
    }

    public Drawable getIcon() {
        return getIconWithoutBackground();
    }

    public Drawable getIconWithoutBackground() {
        return this.mContext.getDrawable(getDrawableResId());
    }

    /* access modifiers changed from: package-private */
    public int getDrawableResId() {
        return this.mDeviceIconUtil.getIconResIdFromMediaRouteType(this.mRouteInfo.getType());
    }

    public String getId() {
        int type = this.mRouteInfo.getType();
        if (type == 3 || type == 4) {
            return WIRED_HEADSET_ID;
        }
        if (!(type == 9 || type == 22)) {
            switch (type) {
                case 11:
                case 12:
                case 13:
                    break;
                default:
                    return PHONE_ID;
            }
        }
        return USB_HEADSET_ID;
    }

    public void updateSummary(boolean z) {
        this.mSummary = z ? this.mContext.getString(C1757R.string.bluetooth_active_no_battery_level) : "";
    }
}
