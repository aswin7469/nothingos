package com.android.systemui.media.dialog;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.shared.system.SysUiStatsLog;
import java.util.List;

public class MediaOutputMetricLogger {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "MediaOutputMetricLogger";
    private int mAppliedDeviceCountWithinRemoteGroup;
    private int mConnectedBluetoothDeviceCount;
    private final Context mContext;
    private final String mPackageName;
    private int mRemoteDeviceCount;
    private MediaDevice mSourceDevice;
    private MediaDevice mTargetDevice;
    private int mWiredDeviceCount;

    private int getLoggingSwitchOpSubResult(int i) {
        if (i == 1) {
            return 2;
        }
        if (i == 2) {
            return 3;
        }
        if (i != 3) {
            return i != 4 ? 0 : 5;
        }
        return 4;
    }

    public MediaOutputMetricLogger(Context context, String str) {
        this.mContext = context;
        this.mPackageName = str;
    }

    public void updateOutputEndPoints(MediaDevice mediaDevice, MediaDevice mediaDevice2) {
        this.mSourceDevice = mediaDevice;
        this.mTargetDevice = mediaDevice2;
        if (DEBUG) {
            Log.d(TAG, "updateOutputEndPoints - source:" + this.mSourceDevice.toString() + " target:" + this.mTargetDevice.toString());
        }
    }

    public void logOutputSuccess(String str, List<MediaDevice> list) {
        if (DEBUG) {
            Log.d(TAG, "logOutputSuccess - selected device: " + str);
        }
        updateLoggingDeviceCount(list);
        SysUiStatsLog.write(277, getLoggingDeviceType(this.mSourceDevice, true), getLoggingDeviceType(this.mTargetDevice, false), 1, 1, getLoggingPackageName(), this.mWiredDeviceCount, this.mConnectedBluetoothDeviceCount, this.mRemoteDeviceCount, this.mAppliedDeviceCountWithinRemoteGroup);
    }

    public void logInteractionAdjustVolume(MediaDevice mediaDevice) {
        if (DEBUG) {
            Log.d(TAG, "logInteraction - AdjustVolume");
        }
        SysUiStatsLog.write(SysUiStatsLog.MEDIAOUTPUT_OP_INTERACTION_REPORT, 1, getInteractionDeviceType(mediaDevice));
    }

    public void logInteractionStopCasting() {
        if (DEBUG) {
            Log.d(TAG, "logInteraction - Stop casting");
        }
        SysUiStatsLog.write(SysUiStatsLog.MEDIAOUTPUT_OP_INTERACTION_REPORT, 2, 0);
    }

    public void logInteractionExpansion(MediaDevice mediaDevice) {
        if (DEBUG) {
            Log.d(TAG, "logInteraction - Expansion");
        }
        SysUiStatsLog.write(SysUiStatsLog.MEDIAOUTPUT_OP_INTERACTION_REPORT, 0, getInteractionDeviceType(mediaDevice));
    }

    public void logOutputFailure(List<MediaDevice> list, int i) {
        if (DEBUG) {
            Log.e(TAG, "logRequestFailed - " + i);
        }
        updateLoggingDeviceCount(list);
        SysUiStatsLog.write(277, getLoggingDeviceType(this.mSourceDevice, true), getLoggingDeviceType(this.mTargetDevice, false), 0, getLoggingSwitchOpSubResult(i), getLoggingPackageName(), this.mWiredDeviceCount, this.mConnectedBluetoothDeviceCount, this.mRemoteDeviceCount, this.mAppliedDeviceCountWithinRemoteGroup);
    }

    private void updateLoggingDeviceCount(List<MediaDevice> list) {
        this.mRemoteDeviceCount = 0;
        this.mConnectedBluetoothDeviceCount = 0;
        this.mWiredDeviceCount = 0;
        this.mAppliedDeviceCountWithinRemoteGroup = 0;
        for (MediaDevice next : list) {
            if (next.isConnected()) {
                int deviceType = next.getDeviceType();
                if (deviceType == 2 || deviceType == 3) {
                    this.mWiredDeviceCount++;
                } else if (deviceType == 5) {
                    this.mConnectedBluetoothDeviceCount++;
                } else if (deviceType == 6 || deviceType == 7) {
                    this.mRemoteDeviceCount++;
                }
            }
        }
        if (DEBUG) {
            Log.d(TAG, "connected devices: wired: " + this.mWiredDeviceCount + " bluetooth: " + this.mConnectedBluetoothDeviceCount + " remote: " + this.mRemoteDeviceCount);
        }
    }

    private int getLoggingDeviceType(MediaDevice mediaDevice, boolean z) {
        if (mediaDevice == null) {
            return 0;
        }
        int deviceType = mediaDevice.getDeviceType();
        if (deviceType == 1) {
            return 1;
        }
        if (deviceType == 2) {
            return 200;
        }
        if (deviceType == 3) {
            return 100;
        }
        if (deviceType == 5) {
            return 300;
        }
        if (deviceType != 6) {
            return deviceType != 7 ? 0 : 500;
        }
        return 400;
    }

    private int getInteractionDeviceType(MediaDevice mediaDevice) {
        if (mediaDevice == null) {
            return 0;
        }
        int deviceType = mediaDevice.getDeviceType();
        if (deviceType == 1) {
            return 1;
        }
        if (deviceType == 2) {
            return 200;
        }
        if (deviceType == 3) {
            return 100;
        }
        if (deviceType == 5) {
            return 300;
        }
        if (deviceType != 6) {
            return deviceType != 7 ? 0 : 500;
        }
        return 400;
    }

    private String getLoggingPackageName() {
        String str = this.mPackageName;
        if (str == null || str.isEmpty()) {
            return "";
        }
        try {
            ApplicationInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfo(this.mPackageName, 0);
            if ((applicationInfo.flags & 1) == 0 && (applicationInfo.flags & 128) == 0) {
                return "";
            }
            return this.mPackageName;
        } catch (Exception unused) {
            Log.e(TAG, this.mPackageName + " is invalid.");
            return "";
        }
    }
}
