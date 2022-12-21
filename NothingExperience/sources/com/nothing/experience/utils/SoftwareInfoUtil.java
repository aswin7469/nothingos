package com.nothing.experience.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import androidx.core.p003os.EnvironmentCompat;
import com.nothing.experience.internalapi.SystemPropertiesWrapper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Locale;
import java.util.UUID;
import okhttp3.HttpUrl;

public class SoftwareInfoUtil {
    public static final String KEY_DEVICE_COLOR = "ro.phone.shell.color";
    public static final String KEY_OS_SW_VERSION = "ro.nothing.version.id";
    public static final String KEY_RAM_TOTAL_SIZE = "ext_ram_total_size";
    public static final String KEY_ROM_TOTAL_SIZE = "ext_rom_total_size";
    private static final String KEY_SYSTEM_BUILD_VERSION_TYPE = "ro.build.version.type";
    private static final String KEY_SYSTEM_DEVICE_TYPE = "sys.device.type";
    private static final String SYSTEM_TYPE_CBT = "CBT";
    private static final String SYSTEM_TYPE_DAILY = "daily";
    private static final String SYSTEM_TYPE_MP = "MP";
    private static final String SYSTEM_TYPE_OBT = "OBT";

    public static synchronized String getAppName(Context context) {
        String string;
        synchronized (SoftwareInfoUtil.class) {
            try {
                string = context.getResources().getString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.labelRes);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return string;
    }

    public static String getSystemModel() {
        return Build.MODEL;
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static String getDeviceName() {
        return Build.DEVICE;
    }

    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getAndroidVersion() {
        return "Android " + Build.VERSION.RELEASE;
    }

    public static String getCurrentLocale() {
        return Locale.getDefault().getCountry();
    }

    public static String getNonPreciseCountryCode(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getNetworkCountryIso();
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getAppVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return "-1";
        }
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (packageInfo != null) {
                return packageInfo.versionName;
            }
            return "-1";
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return "-1";
        }
    }

    public static String getString(Context context, String str) {
        return Settings.Global.getString(context.getContentResolver(), str);
    }

    public static String getRamSize(Context context) {
        String string = getString(context, KEY_RAM_TOTAL_SIZE);
        return TextUtils.isEmpty(string) ? getTotalRam() : string;
    }

    public static String getTotalRam() {
        String str = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 4096);
            str = bufferedReader.readLine().split("\\s+")[1];
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int ceil = str != null ? (int) Math.ceil(new Float(Float.valueOf(str).floatValue() / 1048576.0f).doubleValue()) : 0;
        return ceil + "GB";
    }

    private static String getTotalRom() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long blockCountLong = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
        int i = 0;
        long[] jArr = {2147483648L, 4294967296L, 8589934592L, 17179869184L, 34359738368L, 68719476736L, 137438953472L, 274877906944L, 549755813888L, 1099511627776L, 2199023255552L};
        String[] strArr = {"2GB", "4GB", "8GB", "16GB", "32GB", "64GB", "128GB", "256GB", "512GB", "1024GB", "2048GB"};
        while (i < 11 && blockCountLong > jArr[i]) {
            if (i == 11) {
                i--;
            }
            i++;
        }
        return strArr[i];
    }

    public static String getRomSize(Context context) {
        String string = getString(context, KEY_ROM_TOTAL_SIZE);
        return TextUtils.isEmpty(string) ? getTotalRom() : string;
    }

    public static String getDeviceColor() {
        return SystemPropertiesWrapper.get(KEY_DEVICE_COLOR, "not_set");
    }

    public static String getDisplayBuildNumber() {
        return SystemPropertiesWrapper.get(KEY_OS_SW_VERSION, Build.DISPLAY);
    }

    public static String getReadableBuildInfo() {
        return Build.DISPLAY;
    }

    public static boolean shouldEncryptId() {
        if (isNewOsVersion()) {
            return isOBTVersion() || isMPVersion();
        }
        return isLocked();
    }

    public static boolean isLocked() {
        return "green".equals(SystemPropertiesWrapper.get("ro.boot.verifiedbootstate"));
    }

    public static boolean isNewOsVersion() {
        String str = SystemPropertiesWrapper.get("ro.build.display.id");
        return !TextUtils.isEmpty(str) && str.toLowerCase().startsWith("t");
    }

    public static boolean isInternalTrial() {
        return SYSTEM_TYPE_CBT.equalsIgnoreCase(SystemPropertiesWrapper.get(KEY_SYSTEM_DEVICE_TYPE));
    }

    public static boolean isOBTVersion() {
        return SYSTEM_TYPE_OBT.equalsIgnoreCase(SystemPropertiesWrapper.get(KEY_SYSTEM_BUILD_VERSION_TYPE));
    }

    public static boolean isMPVersion() {
        return SYSTEM_TYPE_MP.equalsIgnoreCase(SystemPropertiesWrapper.get(KEY_SYSTEM_BUILD_VERSION_TYPE));
    }

    public static String getOSCategory() {
        String str = SystemPropertiesWrapper.get(KEY_SYSTEM_DEVICE_TYPE, SYSTEM_TYPE_DAILY);
        if (SYSTEM_TYPE_CBT.equalsIgnoreCase(str)) {
            return str;
        }
        return SystemPropertiesWrapper.get(KEY_SYSTEM_BUILD_VERSION_TYPE, SYSTEM_TYPE_DAILY);
    }

    public static String generateUniqueDeviceId(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String string = contentResolver != null ? Settings.Secure.getString(contentResolver, "android_id") : HttpUrl.FRAGMENT_ENCODE_SET;
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
        String macAddress = wifiManager != null ? wifiManager.getConnectionInfo().getMacAddress() : HttpUrl.FRAGMENT_ENCODE_SET;
        String hardwareDeviceId = getHardwareDeviceId();
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(string)) {
            sb.append(string);
        }
        if (!TextUtils.isEmpty(macAddress)) {
            sb.append(macAddress);
        }
        if (!TextUtils.isEmpty(hardwareDeviceId)) {
            sb.append(hardwareDeviceId);
        }
        String encryptForString = EncryptUtils.getEncryptForString(sb.toString());
        return TextUtils.isEmpty(encryptForString) ? UUID.randomUUID().toString().replace("-", HttpUrl.FRAGMENT_ENCODE_SET).replace(" ", HttpUrl.FRAGMENT_ENCODE_SET) : encryptForString;
    }

    public static String getHardwareDeviceId() {
        String str = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.DISPLAY.length() % 10) + (Build.HOST.length() % 10) + (Build.ID.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10) + (Build.TAGS.length() % 10) + (Build.TYPE.length() % 10) + (Build.USER.length() % 10);
        try {
            return new UUID((long) str.hashCode(), (long) getSN().hashCode()).toString().replace("-", HttpUrl.FRAGMENT_ENCODE_SET);
        } catch (Exception unused) {
            return new UUID((long) str.hashCode(), (long) -905839116).toString().replace("-", HttpUrl.FRAGMENT_ENCODE_SET);
        }
    }

    private static String getSN() {
        return SystemPropertiesWrapper.get("ro.serialno", EnvironmentCompat.MEDIA_UNKNOWN);
    }
}
