package com.nothing.systemui.doze;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.Dependency;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.util.NTLogUtil;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.p026io.IOException;
import java.p026io.OutputStream;

public class AODGestureHelper {
    public static final int GESTURE_DISABLE_FINGER = 4;
    public static final int GESTURE_DISABLE_TOUCH = 2;
    public static final int GESTURE_ENABLE_FINGER = 3;
    public static final int GESTURE_ENABLE_TOUCH = 1;
    private static final String GESTURE_FILE_NODE = "/sys/bus/spi/devices/spi0.0/fts_gesture_mode";
    private static final String TAG = "AODGestureHelper";

    private static void writeGestureNode(String str, int i) {
        StringBuilder sb;
        OutputStream outputStream = null;
        try {
            NTLogUtil.m1680d(TAG, "writeGestureNode:" + str + ", data:" + i);
            outputStream = Files.newOutputStream(Paths.get(str, new String[0]), new OpenOption[0]);
            outputStream.write(String.valueOf(i).getBytes("US-ASCII"));
            outputStream.flush();
            if (outputStream != null) {
                try {
                    outputStream.close();
                    return;
                } catch (IOException e) {
                    e = e;
                    sb = new StringBuilder("Unable to close file: ");
                }
            } else {
                return;
            }
            NTLogUtil.m1681e(TAG, sb.append(str).append(e.getMessage()).toString());
        } catch (IOException e2) {
            NTLogUtil.m1681e(TAG, "Unable to write " + e2.getMessage());
            e2.printStackTrace();
            if (outputStream == null) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e3) {
                        NTLogUtil.m1681e(TAG, "Unable to close file: " + str + e3.getMessage());
                    }
                }
            } else if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e4) {
                    e = e4;
                    sb = new StringBuilder("Unable to close file: ");
                }
            }
        } catch (Throwable th) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e5) {
                    NTLogUtil.m1681e(TAG, "Unable to close file: " + str + e5.getMessage());
                }
            }
            throw th;
        }
    }

    public static void disableGesture() {
        boolean isTapWakeEnable = ((AODController) NTDependencyEx.get(AODController.class)).isTapWakeEnable();
        boolean isUdfpsEnrolled = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isUdfpsEnrolled();
        if (!isTapWakeEnable) {
            writeGestureNode(GESTURE_FILE_NODE, 2);
        }
        if (isUdfpsEnrolled) {
            writeGestureNode(GESTURE_FILE_NODE, 4);
        }
    }

    public static void enableGesture() {
        initGestureState();
    }

    public static void initGestureState() {
        boolean isTapWakeEnable = ((AODController) NTDependencyEx.get(AODController.class)).isTapWakeEnable();
        boolean isUdfpsEnrolled = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isUdfpsEnrolled();
        writeGestureNode(GESTURE_FILE_NODE, isTapWakeEnable ? 1 : 2);
        writeGestureNode(GESTURE_FILE_NODE, isUdfpsEnrolled ? 3 : 4);
    }
}
