package com.android.systemui.statusbar.policy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface DevicePostureController extends CallbackController<Callback> {
    public static final int DEVICE_POSTURE_CLOSED = 1;
    public static final int DEVICE_POSTURE_FLIPPED = 4;
    public static final int DEVICE_POSTURE_HALF_OPENED = 2;
    public static final int DEVICE_POSTURE_OPENED = 3;
    public static final int DEVICE_POSTURE_UNKNOWN = 0;
    public static final int SUPPORTED_POSTURES_SIZE = 5;

    public interface Callback {
        void onPostureChanged(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DevicePostureInt {
    }

    int getDevicePosture();

    static String devicePostureToString(int i) {
        if (i == 0) {
            return "DEVICE_POSTURE_UNKNOWN";
        }
        if (i == 1) {
            return "DEVICE_POSTURE_CLOSED";
        }
        if (i == 2) {
            return "DEVICE_POSTURE_HALF_OPENED";
        }
        if (i != 3) {
            return i != 4 ? "UNSUPPORTED POSTURE posture=" + i : "DEVICE_POSTURE_FLIPPED";
        }
        return "DEVICE_POSTURE_OPENED";
    }
}
