package com.nt.ledsettings;

import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/* loaded from: classes4.dex */
public class LedBrightnessUtils {
    private static final int DEFAULT_LED_BRIGHTNESS_VALUE_OF_BLACK = 2625;
    private static final int DEFAULT_LED_BRIGHTNESS_VALUE_OF_WHITE = 688;
    private static final String DEVICE_WHITE = "white";
    private static final String LED_WHITE_BR_PATH = "/sys/devices/platform/soc/984000.i2c/i2c-0/0-0020/leds/aw210xx_led/setting_br";
    private static final String RO_PHONE_SHELL_COLOR = "ro.phone.shell.color";
    private static final String TAG = "LedBrightnessUtils";
    private static int sDefaultBrightness = -1;

    public static int getLedDefaultBrightness() {
        int i = sDefaultBrightness;
        if (i != -1) {
            return i;
        }
        int i2 = TextUtils.equals(SystemProperties.get(RO_PHONE_SHELL_COLOR), DEVICE_WHITE) ? 688 : DEFAULT_LED_BRIGHTNESS_VALUE_OF_BLACK;
        sDefaultBrightness = i2;
        return i2;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:12:0x0019 -> B:6:0x0041). Please submit an issue!!! */
    public static void writeLedBrightness(int brightness) {
        FileOutputStream fos = null;
        try {
            try {
                try {
                    fos = new FileOutputStream(LED_WHITE_BR_PATH);
                    fos.write(String.valueOf(brightness).getBytes());
                    fos.close();
                } catch (IOException e) {
                }
            } catch (IOException e2) {
                Log.e(TAG, "Unable to write /sys/devices/platform/soc/984000.i2c/i2c-0/0-0020/leds/aw210xx_led/setting_br" + e2.getMessage());
                e2.printStackTrace();
                if (fos != null) {
                    fos.close();
                }
            }
        } catch (Throwable th) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e3) {
                }
            }
            throw th;
        }
    }

    public static int readLedBrightness() {
        FileInputStream in = null;
        StringBuilder builder = new StringBuilder();
        try {
            try {
                in = new FileInputStream(LED_WHITE_BR_PATH);
                while (true) {
                    int tempbyte = in.read();
                    if (tempbyte == -1) {
                        break;
                    }
                    builder.append((char) tempbyte);
                }
                in.close();
                String content = builder.toString();
                if (TextUtils.isEmpty(content)) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                    return 0;
                }
                int parseInt = Integer.parseInt(content.trim());
                try {
                    in.close();
                } catch (IOException e2) {
                }
                return parseInt;
            } catch (Exception e3) {
                Log.e(TAG, "Unable to read /sys/devices/platform/soc/984000.i2c/i2c-0/0-0020/leds/aw210xx_led/setting_br" + e3.getMessage());
                e3.printStackTrace();
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e4) {
                    }
                }
                return 0;
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e5) {
                }
            }
            throw th;
        }
    }
}
