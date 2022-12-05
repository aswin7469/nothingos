package com.nt.settings.glyphs.utils;

import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/* loaded from: classes2.dex */
public class LedBrightnessUtils {
    private static int sCurrentBrightnessMax = -1;
    private static int sCurrentBrightnessMin = -1;

    public static int getLedBrightnessMax() {
        int i = sCurrentBrightnessMax;
        if (i != -1) {
            return i;
        }
        int i2 = TextUtils.equals(SystemProperties.get("ro.phone.shell.color"), "white") ? 1306 : 3840;
        sCurrentBrightnessMax = i2;
        return i2;
    }

    public static int getLedBrightnessMin() {
        int i = sCurrentBrightnessMin;
        if (i != -1) {
            return i;
        }
        int i2 = TextUtils.equals(SystemProperties.get("ro.phone.shell.color"), "white") ? 241 : 750;
        sCurrentBrightnessMin = i2;
        return i2;
    }

    public static void writeLedBrightness(int i) {
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                try {
                    fileOutputStream = new FileOutputStream("/sys/devices/platform/soc/984000.i2c/i2c-0/0-0020/leds/aw210xx_led/setting_br");
                } catch (IOException unused) {
                    return;
                }
            } catch (IOException e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            fileOutputStream.write(String.valueOf(i).getBytes());
            fileOutputStream.close();
        } catch (IOException e2) {
            e = e2;
            fileOutputStream2 = fileOutputStream;
            Log.e("LedBrightnessUtils", "Unable to write /sys/devices/platform/soc/984000.i2c/i2c-0/0-0020/leds/aw210xx_led/setting_br" + e.getMessage());
            e.printStackTrace();
            if (fileOutputStream2 == null) {
                return;
            }
            fileOutputStream2.close();
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream2 = fileOutputStream;
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.close();
                } catch (IOException unused2) {
                }
            }
            throw th;
        }
    }

    public static int readLedBrightness() {
        StringBuilder sb = new StringBuilder();
        FileInputStream fileInputStream = null;
        try {
            try {
                FileInputStream fileInputStream2 = new FileInputStream("/sys/devices/platform/soc/984000.i2c/i2c-0/0-0020/leds/aw210xx_led/setting_br");
                while (true) {
                    try {
                        int read = fileInputStream2.read();
                        if (read == -1) {
                            break;
                        }
                        sb.append((char) read);
                    } catch (Exception e) {
                        e = e;
                        fileInputStream = fileInputStream2;
                        Log.e("LedBrightnessUtils", "Unable to read /sys/devices/platform/soc/984000.i2c/i2c-0/0-0020/leds/aw210xx_led/setting_br" + e.getMessage());
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException unused) {
                            }
                        }
                        return 0;
                    } catch (Throwable th) {
                        th = th;
                        fileInputStream = fileInputStream2;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException unused2) {
                            }
                        }
                        throw th;
                    }
                }
                fileInputStream2.close();
                String sb2 = sb.toString();
                if (!TextUtils.isEmpty(sb2)) {
                    int parseInt = Integer.parseInt(sb2.trim());
                    try {
                        fileInputStream2.close();
                    } catch (IOException unused3) {
                    }
                    return parseInt;
                }
                try {
                    fileInputStream2.close();
                } catch (IOException unused4) {
                }
                return 0;
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
