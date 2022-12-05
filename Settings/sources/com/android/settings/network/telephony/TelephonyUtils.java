package com.android.settings.network.telephony;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.qti.extphone.ExtTelephonyManager;
import com.qti.extphone.QtiImeiInfo;
import com.qti.extphone.ServiceCallback;
import java.util.Optional;
/* loaded from: classes.dex */
public final class TelephonyUtils {
    private static ExtTelephonyManager mExtTelephonyManager;
    private static boolean mIsServiceBound;
    public static boolean DBG = Log.isLoggable("TelephonyUtils", 3);
    private static Optional<Boolean> mIsSubsidyFeatureEnabled = Optional.empty();
    private static ServiceCallback mServiceCallback = new ServiceCallback() { // from class: com.android.settings.network.telephony.TelephonyUtils.1
        public void onConnected() {
            Log.d("TelephonyUtils", "ExtTelephony Service connected");
            boolean unused = TelephonyUtils.mIsServiceBound = true;
        }

        public void onDisconnected() {
            Log.d("TelephonyUtils", "ExtTelephony Service disconnected...");
            boolean unused = TelephonyUtils.mIsServiceBound = false;
        }
    };

    public static boolean isAdvancedPlmnScanSupported(Context context) {
        if (mIsServiceBound) {
            try {
                return mExtTelephonyManager.getPropertyValueBool("persist.vendor.radio.enableadvancedscan", false);
            } catch (NullPointerException e) {
                Log.e("TelephonyUtils", "isAdvancedPlmnScanSupported: , Exception: ", e);
                return false;
            }
        }
        Log.e("TelephonyUtils", "isAdvancedPlmnScanSupported: ExtTelephony Service not connected!");
        return false;
    }

    public static boolean performIncrementalScan(Context context, int i) {
        if (mIsServiceBound) {
            return mExtTelephonyManager.performIncrementalScan(i);
        }
        Log.e("TelephonyUtils", "performIncrementalScan: ExtTelephony Service not connected!");
        return false;
    }

    public static void abortIncrementalScan(Context context, int i) {
        if (mIsServiceBound) {
            mExtTelephonyManager.abortIncrementalScan(i);
        } else {
            Log.e("TelephonyUtils", "abortIncrementalScan: ExtTelephony Service not connected!");
        }
    }

    public static boolean isDual5gSupported(TelephonyManager telephonyManager) {
        String[] split;
        if (telephonyManager == null) {
            Log.e("TelephonyUtils", "telephonyManager is null");
            return false;
        }
        String basebandVersion = telephonyManager.getBasebandVersion();
        Log.d("TelephonyUtils", "Base band version = " + basebandVersion);
        if (!TextUtils.isEmpty(basebandVersion) && (split = basebandVersion.split("-")) != null) {
            for (String str : split) {
                if (str != null && str.startsWith("MPSS.HI.")) {
                    String substring = str.substring(8, str.length());
                    Log.d("TelephonyUtils", "verCode = " + substring);
                    if (substring != null && substring.length() > 2) {
                        String[] split2 = substring.split("\\.");
                        try {
                            int parseInt = Integer.parseInt(split2[0]);
                            int parseInt2 = Integer.parseInt(split2[1]);
                            Log.d("TelephonyUtils", "Ver major = " + parseInt + " minor = " + parseInt2);
                            if (parseInt >= 4 && parseInt2 >= 3) {
                                return true;
                            }
                        } catch (NumberFormatException unused) {
                            Log.e("TelephonyUtils", "Fail to parse version");
                            return false;
                        }
                    }
                } else if (str != null && str.startsWith("MPSS.DE.")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSubsidyFeatureEnabled(Context context) {
        if (!mIsSubsidyFeatureEnabled.isPresent()) {
            if (!mIsServiceBound) {
                Log.e("TelephonyUtils", "isSubsidyFeatureEnabled: ExtTelephony Service not connected!");
                connectExtTelephonyService(context);
            }
            try {
                mIsSubsidyFeatureEnabled = Optional.of(Boolean.valueOf(mExtTelephonyManager.getPropertyValueBool("persist.vendor.radio.subsidydevice", false)));
            } catch (NullPointerException e) {
                Log.e("TelephonyUtils", "isSubsidyFeatureEnabled: , Exception: ", e);
            }
        }
        return mIsSubsidyFeatureEnabled.get().booleanValue();
    }

    public static boolean allowUsertoSetDDS(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "allow_user_select_dds", 0) == 1;
    }

    public static boolean isSubsidySimCard(Context context, int i) {
        if (!mIsServiceBound) {
            Log.e("TelephonyUtils", "isSubsidySimCard: ExtTelephony Service not connected!");
            connectExtTelephonyService(context);
        }
        try {
            return mExtTelephonyManager.isPrimaryCarrierSlotId(i);
        } catch (NullPointerException e) {
            Log.e("TelephonyUtils", "isSubsidySimCard: , Exception: ", e);
            return false;
        }
    }

    public static QtiImeiInfo[] getImeiInfo() {
        if (isServiceConnected()) {
            return mExtTelephonyManager.getImeiInfo();
        }
        Log.e("TelephonyUtils", "getImeiInfo: ExtTelephony Service not connected!");
        return null;
    }

    public static void connectExtTelephonyService(Context context) {
        if (!mIsServiceBound) {
            Log.d("TelephonyUtils", "Connect to ExtTelephonyService...");
            ExtTelephonyManager extTelephonyManager = ExtTelephonyManager.getInstance(context);
            mExtTelephonyManager = extTelephonyManager;
            extTelephonyManager.connectService(mServiceCallback);
        }
    }

    public static boolean isServiceConnected() {
        return mIsServiceBound;
    }
}
