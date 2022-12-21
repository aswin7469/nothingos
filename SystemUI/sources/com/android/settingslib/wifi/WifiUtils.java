package com.android.settingslib.wifi;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.text.MessageFormat;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import com.android.settingslib.C1757R;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class WifiUtils {
    public static final String ACTION_WIFI_DETAILS_SETTINGS = "android.settings.WIFI_DETAILS_SETTINGS";
    static final String ACTION_WIFI_DIALOG = "com.android.settings.WIFI_DIALOG";
    static final String EXTRA_CHOSEN_WIFI_ENTRY_KEY = "key_chosen_wifientry_key";
    static final String EXTRA_CONNECT_FOR_CALLER = "connect_for_caller";
    public static final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":settings:show_fragment_args";
    private static final int INVALID_RSSI = -127;
    public static final String KEY_CHOSEN_WIFIENTRY_KEY = "key_chosen_wifientry_key";
    static final int[] NO_INTERNET_WIFI_PIE = {C1757R.C1759drawable.ic_no_internet_wifi_signal_0, C1757R.C1759drawable.ic_no_internet_wifi_signal_1, C1757R.C1759drawable.ic_no_internet_wifi_signal_2, C1757R.C1759drawable.ic_no_internet_wifi_signal_3, C1757R.C1759drawable.ic_no_internet_wifi_signal_4};
    private static final String TAG = "WifiUtils";
    static final int[] WIFI_4_PIE = {17302898, 17302899, 17302900, 17302901, 17302902};
    static final int[] WIFI_5_PIE = {17302903, 17302904, 17302905, 17302906, 17302907};
    static final int[] WIFI_6_PIE = {17302908, 17302909, 17302910, 17302911, 17302912};
    static final int[] WIFI_PIE = {17302913, 17302914, 17302915, 17302916, 17302917};

    public static String buildLoggingSummary(AccessPoint accessPoint, WifiConfiguration wifiConfiguration) {
        StringBuilder sb = new StringBuilder();
        WifiInfo info = accessPoint.getInfo();
        if (accessPoint.isActive() && info != null) {
            sb.append(" f=" + Integer.toString(info.getFrequency()));
        }
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + getVisibilityStatus(accessPoint));
        if (!(wifiConfiguration == null || wifiConfiguration.getNetworkSelectionStatus().getNetworkSelectionStatus() == 0)) {
            sb.append(" (" + wifiConfiguration.getNetworkSelectionStatus().getNetworkStatusString());
            if (wifiConfiguration.getNetworkSelectionStatus().getDisableTime() > 0) {
                long currentTimeMillis = (System.currentTimeMillis() - wifiConfiguration.getNetworkSelectionStatus().getDisableTime()) / 1000;
                long j = currentTimeMillis % 60;
                long j2 = (currentTimeMillis / 60) % 60;
                long j3 = (j2 / 60) % 60;
                sb.append(", ");
                if (j3 > 0) {
                    sb.append(Long.toString(j3) + "h ");
                }
                sb.append(Long.toString(j2) + "m ");
                sb.append(Long.toString(j) + "s ");
            }
            sb.append(NavigationBarInflaterView.KEY_CODE_END);
        }
        if (wifiConfiguration != null) {
            WifiConfiguration.NetworkSelectionStatus networkSelectionStatus = wifiConfiguration.getNetworkSelectionStatus();
            for (int i = 0; i <= WifiConfiguration.NetworkSelectionStatus.getMaxNetworkSelectionDisableReason(); i++) {
                if (networkSelectionStatus.getDisableReasonCounter(i) != 0) {
                    sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER).append(WifiConfiguration.NetworkSelectionStatus.getNetworkSelectionDisableReasonString(i)).append("=").append(networkSelectionStatus.getDisableReasonCounter(i));
                }
            }
        }
        return sb.toString();
    }

    static String getVisibilityStatus(AccessPoint accessPoint) {
        String str;
        int i;
        StringBuilder sb;
        StringBuilder sb2;
        StringBuilder sb3;
        AccessPoint accessPoint2 = accessPoint;
        WifiInfo info = accessPoint.getInfo();
        StringBuilder sb4 = new StringBuilder();
        StringBuilder sb5 = new StringBuilder();
        StringBuilder sb6 = new StringBuilder();
        StringBuilder sb7 = new StringBuilder();
        StringBuilder sb8 = new StringBuilder();
        int i2 = 0;
        if (!accessPoint.isActive() || info == null) {
            str = null;
        } else {
            str = info.getBSSID();
            if (str != null) {
                sb4.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER).append(str);
            }
            sb4.append(" standard = ").append(info.getWifiStandard());
            sb4.append(" rssi=").append(info.getRssi());
            sb4.append("  score=");
            sb4.append(info.getScore());
            if (accessPoint.getSpeed() != 0) {
                sb4.append(" speed=").append(accessPoint.getSpeedLabel());
            }
            sb4.append(String.format(" tx=%.1f,", Double.valueOf(info.getSuccessfulTxPacketsPerSecond())));
            sb4.append(String.format("%.1f,", Double.valueOf(info.getRetriedTxPacketsPerSecond())));
            sb4.append(String.format("%.1f ", Double.valueOf(info.getLostTxPacketsPerSecond())));
            sb4.append(String.format("rx=%.1f", Double.valueOf(info.getSuccessfulRxPacketsPerSecond())));
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Iterator<ScanResult> it = accessPoint.getScanResults().iterator();
        StringBuilder sb9 = sb4;
        StringBuilder sb10 = sb7;
        int i3 = 0;
        int i4 = 0;
        int i5 = -127;
        int i6 = -127;
        int i7 = -127;
        int i8 = -127;
        int i9 = 0;
        while (true) {
            i = i6;
            if (!it.hasNext()) {
                break;
            }
            ScanResult next = it.next();
            if (next == null) {
                i6 = i;
            } else {
                Iterator<ScanResult> it2 = it;
                int i10 = i3;
                if (next.frequency >= 5935 && next.frequency <= 7115) {
                    i4++;
                    if (next.level > i5) {
                        i5 = next.level;
                    }
                    if (i4 <= 4) {
                        sb8.append(verboseScanResultSummary(accessPoint2, next, str, elapsedRealtime));
                    }
                } else if (next.frequency >= 4900 && next.frequency <= 5900) {
                    i9++;
                    if (next.level > i8) {
                        i8 = next.level;
                    }
                    if (i9 <= 4) {
                        sb6.append(verboseScanResultSummary(accessPoint2, next, str, elapsedRealtime));
                    }
                } else if (next.frequency >= 2400 && next.frequency <= 2500) {
                    i2++;
                    if (next.level > i7) {
                        i7 = next.level;
                    }
                    if (i2 <= 4) {
                        sb5.append(verboseScanResultSummary(accessPoint2, next, str, elapsedRealtime));
                    }
                } else if (next.frequency < 58320 || next.frequency > 70200) {
                    sb = sb8;
                    sb2 = sb10;
                    i6 = i;
                    i3 = i10;
                    sb10 = sb2;
                    it = it2;
                    sb8 = sb;
                } else {
                    i3 = i10 + 1;
                    sb = sb8;
                    int i11 = i;
                    int i12 = next.level > i11 ? next.level : i11;
                    if (i3 <= 4) {
                        sb3 = sb10;
                        sb3.append(verboseScanResultSummary(accessPoint2, next, str, elapsedRealtime));
                    } else {
                        sb3 = sb10;
                    }
                    i6 = i12;
                    sb2 = sb3;
                    sb10 = sb2;
                    it = it2;
                    sb8 = sb;
                }
                sb = sb8;
                sb2 = sb10;
                i6 = i;
                i3 = i10;
                sb10 = sb2;
                it = it2;
                sb8 = sb;
            }
        }
        StringBuilder sb11 = sb8;
        int i13 = i3;
        StringBuilder sb12 = sb10;
        int i14 = i;
        StringBuilder sb13 = sb9;
        sb13.append(" [");
        if (i2 > 0) {
            sb13.append(NavigationBarInflaterView.KEY_CODE_START).append(i2).append(NavigationBarInflaterView.KEY_CODE_END);
            if (i2 > 4) {
                sb13.append("max=").append(i7).append(NavigationBarInflaterView.BUTTON_SEPARATOR);
            }
            sb13.append(sb5.toString());
        }
        sb13.append(NavigationBarInflaterView.GRAVITY_SEPARATOR);
        if (i9 > 0) {
            sb13.append(NavigationBarInflaterView.KEY_CODE_START).append(i9).append(NavigationBarInflaterView.KEY_CODE_END);
            if (i9 > 4) {
                sb13.append("max=").append(i8).append(NavigationBarInflaterView.BUTTON_SEPARATOR);
            }
            sb13.append(sb6.toString());
        }
        sb13.append(NavigationBarInflaterView.GRAVITY_SEPARATOR);
        if (i13 > 0) {
            int i15 = i13;
            sb13.append(NavigationBarInflaterView.KEY_CODE_START).append(i15).append(NavigationBarInflaterView.KEY_CODE_END);
            if (i15 > 4) {
                sb13.append("max=").append(i14).append(NavigationBarInflaterView.BUTTON_SEPARATOR);
            }
            sb13.append(sb12.toString());
        }
        sb13.append(NavigationBarInflaterView.GRAVITY_SEPARATOR);
        if (i4 > 0) {
            sb13.append(NavigationBarInflaterView.KEY_CODE_START).append(i4).append(NavigationBarInflaterView.KEY_CODE_END);
            if (i4 > 4) {
                sb13.append("max=").append(i5).append(NavigationBarInflaterView.BUTTON_SEPARATOR);
            }
            sb13.append(sb11.toString());
        }
        sb13.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb13.toString();
    }

    static String verboseScanResultSummary(AccessPoint accessPoint, ScanResult scanResult, String str, long j) {
        StringBuilder sb = new StringBuilder(" \n{");
        sb.append(scanResult.BSSID);
        if (scanResult.BSSID.equals(str)) {
            sb.append("*");
        }
        sb.append("=").append(scanResult.frequency);
        sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR).append(scanResult.level);
        int specificApSpeed = getSpecificApSpeed(scanResult, accessPoint.getScoredNetworkCache());
        if (specificApSpeed != 0) {
            sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR).append(accessPoint.getSpeedLabel(specificApSpeed));
        }
        sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR).append(((int) (j - (scanResult.timestamp / 1000))) / 1000).append("s}");
        return sb.toString();
    }

    private static int getSpecificApSpeed(ScanResult scanResult, Map<String, TimestampedScoredNetwork> map) {
        TimestampedScoredNetwork timestampedScoredNetwork = map.get(scanResult.BSSID);
        if (timestampedScoredNetwork == null) {
            return 0;
        }
        return timestampedScoredNetwork.getScore().calculateBadge(scanResult.level);
    }

    public static String getMeteredLabel(Context context, WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.meteredOverride == 1 || (wifiConfiguration.meteredHint && !isMeteredOverridden(wifiConfiguration))) {
            return context.getString(C1757R.string.wifi_metered_label);
        }
        return context.getString(C1757R.string.wifi_unmetered_label);
    }

    public static int getInternetIconResource(int i, boolean z) {
        return getInternetIconResource(i, z, 0);
    }

    public static int getInternetIconResource(int i, boolean z, int i2) {
        if (i < 0) {
            Log.e(TAG, "Wi-Fi level is out of range! level:" + i);
            i = 0;
        } else {
            int[] iArr = WIFI_PIE;
            if (i >= iArr.length) {
                Log.e(TAG, "Wi-Fi level is out of range! level:" + i);
                i = iArr.length - 1;
            }
        }
        if (z) {
            return NO_INTERNET_WIFI_PIE[i];
        }
        if (i2 == 4) {
            return WIFI_4_PIE[i];
        }
        if (i2 == 5) {
            return WIFI_5_PIE[i];
        }
        if (i2 != 6) {
            return WIFI_PIE[i];
        }
        return WIFI_6_PIE[i];
    }

    public static class InternetIconInjector {
        protected final Context mContext;

        public InternetIconInjector(Context context) {
            this.mContext = context;
        }

        public Drawable getIcon(boolean z, int i) {
            return this.mContext.getDrawable(WifiUtils.getInternetIconResource(i, z));
        }
    }

    public static boolean isMeteredOverridden(WifiConfiguration wifiConfiguration) {
        return wifiConfiguration.meteredOverride != 0;
    }

    public static Intent getWifiDialogIntent(String str, boolean z) {
        Intent intent = new Intent(ACTION_WIFI_DIALOG);
        intent.putExtra("key_chosen_wifientry_key", str);
        intent.putExtra(EXTRA_CONNECT_FOR_CALLER, z);
        return intent;
    }

    public static Intent getWifiDetailsSettingsIntent(String str) {
        Intent intent = new Intent(ACTION_WIFI_DETAILS_SETTINGS);
        Bundle bundle = new Bundle();
        bundle.putString("key_chosen_wifientry_key", str);
        intent.putExtra(":settings:show_fragment_args", bundle);
        return intent;
    }

    public static String getWifiTetherSummaryForConnectedDevices(Context context, int i) {
        MessageFormat messageFormat = new MessageFormat(context.getResources().getString(C1757R.string.wifi_tether_connected_summary), Locale.getDefault());
        HashMap hashMap = new HashMap();
        hashMap.put("count", Integer.valueOf(i));
        return messageFormat.format(hashMap);
    }
}
