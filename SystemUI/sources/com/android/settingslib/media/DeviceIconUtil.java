package com.android.settingslib.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.android.settingslib.C1757R;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceIconUtil {
    private static final int DEFAULT_ICON = C1757R.C1759drawable.ic_smartphone;
    private final Map<Integer, Device> mAudioDeviceTypeToIconMap = new HashMap();
    private final Map<Integer, Device> mMediaRouteTypeToIconMap = new HashMap();

    public DeviceIconUtil() {
        List asList = Arrays.asList(new Device(11, 11, C1757R.C1759drawable.ic_headphone), new Device(22, 22, C1757R.C1759drawable.ic_headphone), new Device(12, 12, C1757R.C1759drawable.ic_headphone), new Device(13, 13, C1757R.C1759drawable.ic_headphone), new Device(9, 9, C1757R.C1759drawable.ic_headphone), new Device(3, 3, C1757R.C1759drawable.ic_headphone), new Device(4, 4, C1757R.C1759drawable.ic_headphone), new Device(2, 2, C1757R.C1759drawable.ic_smartphone));
        for (int i = 0; i < asList.size(); i++) {
            Device device = (Device) asList.get(i);
            this.mAudioDeviceTypeToIconMap.put(Integer.valueOf(device.mAudioDeviceType), device);
            this.mMediaRouteTypeToIconMap.put(Integer.valueOf(device.mMediaRouteType), device);
        }
    }

    public Drawable getIconFromAudioDeviceType(int i, Context context) {
        return context.getDrawable(getIconResIdFromAudioDeviceType(i));
    }

    public int getIconResIdFromAudioDeviceType(int i) {
        if (this.mAudioDeviceTypeToIconMap.containsKey(Integer.valueOf(i))) {
            return this.mAudioDeviceTypeToIconMap.get(Integer.valueOf(i)).mIconDrawableRes;
        }
        return DEFAULT_ICON;
    }

    public int getIconResIdFromMediaRouteType(int i) {
        if (this.mMediaRouteTypeToIconMap.containsKey(Integer.valueOf(i))) {
            return this.mMediaRouteTypeToIconMap.get(Integer.valueOf(i)).mIconDrawableRes;
        }
        return DEFAULT_ICON;
    }

    private static class Device {
        /* access modifiers changed from: private */
        public final int mAudioDeviceType;
        /* access modifiers changed from: private */
        public final int mIconDrawableRes;
        /* access modifiers changed from: private */
        public final int mMediaRouteType;

        Device(int i, int i2, int i3) {
            this.mAudioDeviceType = i;
            this.mMediaRouteType = i2;
            this.mIconDrawableRes = i3;
        }
    }
}
