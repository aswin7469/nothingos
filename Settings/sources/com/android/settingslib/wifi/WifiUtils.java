package com.android.settingslib.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.SystemClock;
import com.android.settingslib.R$drawable;
import java.util.Iterator;
import java.util.Map;
/* loaded from: classes.dex */
public class WifiUtils {
    static final int[] WIFI_PIE = {17302902, 17302903, 17302904, 17302905, 17302906};
    static final int[] NO_INTERNET_WIFI_PIE = {R$drawable.ic_no_internet_wifi_signal_0, R$drawable.ic_no_internet_wifi_signal_1, R$drawable.ic_no_internet_wifi_signal_2, R$drawable.ic_no_internet_wifi_signal_3, R$drawable.ic_no_internet_wifi_signal_4};
    static final int[] WIFI_4_PIE = {17302887, 17302888, 17302889, 17302890, 17302891};
    static final int[] WIFI_5_PIE = {17302892, 17302893, 17302894, 17302895, 17302896};
    static final int[] WIFI_6_PIE = {17302897, 17302898, 17302899, 17302900, 17302901};

    static String getVisibilityStatus(AccessPoint accessPoint) {
        String str;
        int i;
        StringBuilder sb;
        StringBuilder sb2;
        StringBuilder sb3;
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
                sb4.append(" ");
                sb4.append(str);
            }
            sb4.append(" standard = ");
            sb4.append(info.getWifiStandard());
            sb4.append(" rssi=");
            sb4.append(info.getRssi());
            sb4.append(" ");
            sb4.append(" score=");
            sb4.append(info.getScore());
            if (accessPoint.getSpeed() != 0) {
                sb4.append(" speed=");
                sb4.append(accessPoint.getSpeedLabel());
            }
            sb4.append(String.format(" tx=%.1f,", Double.valueOf(info.getSuccessfulTxPacketsPerSecond())));
            sb4.append(String.format("%.1f,", Double.valueOf(info.getRetriedTxPacketsPerSecond())));
            sb4.append(String.format("%.1f ", Double.valueOf(info.getLostTxPacketsPerSecond())));
            sb4.append(String.format("rx=%.1f", Double.valueOf(info.getSuccessfulRxPacketsPerSecond())));
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Iterator<ScanResult> it = accessPoint.getScanResults().iterator();
        StringBuilder sb9 = sb7;
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
                int i10 = next.frequency;
                int i11 = i3;
                if (i10 >= 5935 && i10 <= 7115) {
                    i4++;
                    int i12 = next.level;
                    if (i12 > i5) {
                        i5 = i12;
                    }
                    if (i4 <= 4) {
                        sb8.append(verboseScanResultSummary(accessPoint, next, str, elapsedRealtime));
                    }
                } else if (i10 >= 4900 && i10 <= 5900) {
                    i9++;
                    int i13 = next.level;
                    if (i13 > i8) {
                        i8 = i13;
                    }
                    if (i9 <= 4) {
                        sb6.append(verboseScanResultSummary(accessPoint, next, str, elapsedRealtime));
                    }
                } else if (i10 >= 2400 && i10 <= 2500) {
                    i2++;
                    int i14 = next.level;
                    if (i14 > i7) {
                        i7 = i14;
                    }
                    if (i2 <= 4) {
                        sb5.append(verboseScanResultSummary(accessPoint, next, str, elapsedRealtime));
                    }
                } else if (i10 < 58320 || i10 > 70200) {
                    sb = sb8;
                    sb2 = sb9;
                    i6 = i;
                    i3 = i11;
                    sb9 = sb2;
                    it = it2;
                    sb8 = sb;
                } else {
                    i3 = i11 + 1;
                    int i15 = next.level;
                    sb = sb8;
                    if (i15 <= i) {
                        i15 = i;
                    }
                    if (i3 <= 4) {
                        sb3 = sb9;
                        sb3.append(verboseScanResultSummary(accessPoint, next, str, elapsedRealtime));
                    } else {
                        sb3 = sb9;
                    }
                    i6 = i15;
                    sb2 = sb3;
                    sb9 = sb2;
                    it = it2;
                    sb8 = sb;
                }
                sb = sb8;
                sb2 = sb9;
                i6 = i;
                i3 = i11;
                sb9 = sb2;
                it = it2;
                sb8 = sb;
            }
        }
        StringBuilder sb10 = sb8;
        int i16 = i3;
        StringBuilder sb11 = sb9;
        sb4.append(" [");
        if (i2 > 0) {
            sb4.append("(");
            sb4.append(i2);
            sb4.append(")");
            if (i2 > 4) {
                sb4.append("max=");
                sb4.append(i7);
                sb4.append(",");
            }
            sb4.append(sb5.toString());
        }
        sb4.append(";");
        if (i9 > 0) {
            sb4.append("(");
            sb4.append(i9);
            sb4.append(")");
            if (i9 > 4) {
                sb4.append("max=");
                sb4.append(i8);
                sb4.append(",");
            }
            sb4.append(sb6.toString());
        }
        sb4.append(";");
        if (i16 > 0) {
            sb4.append("(");
            sb4.append(i16);
            sb4.append(")");
            if (i16 > 4) {
                sb4.append("max=");
                sb4.append(i);
                sb4.append(",");
            }
            sb4.append(sb11.toString());
        }
        sb4.append(";");
        if (i4 > 0) {
            sb4.append("(");
            sb4.append(i4);
            sb4.append(")");
            if (i4 > 4) {
                sb4.append("max=");
                sb4.append(i5);
                sb4.append(",");
            }
            sb4.append(sb10.toString());
        }
        sb4.append("]");
        return sb4.toString();
    }

    static String verboseScanResultSummary(AccessPoint accessPoint, ScanResult scanResult, String str, long j) {
        StringBuilder sb = new StringBuilder();
        sb.append(" \n{");
        sb.append(scanResult.BSSID);
        if (scanResult.BSSID.equals(str)) {
            sb.append("*");
        }
        sb.append("=");
        sb.append(scanResult.frequency);
        sb.append(",");
        sb.append(scanResult.level);
        int specificApSpeed = getSpecificApSpeed(scanResult, accessPoint.getScoredNetworkCache());
        if (specificApSpeed != 0) {
            sb.append(",");
            sb.append(accessPoint.getSpeedLabel(specificApSpeed));
        }
        sb.append(",");
        sb.append(((int) (j - (scanResult.timestamp / 1000))) / 1000);
        sb.append("s");
        sb.append("}");
        return sb.toString();
    }

    private static int getSpecificApSpeed(ScanResult scanResult, Map<String, TimestampedScoredNetwork> map) {
        TimestampedScoredNetwork timestampedScoredNetwork = map.get(scanResult.BSSID);
        if (timestampedScoredNetwork == null) {
            return 0;
        }
        return timestampedScoredNetwork.getScore().calculateBadge(scanResult.level);
    }

    public static int getInternetIconResource(int i, boolean z) {
        return getInternetIconResource(i, z, 0, false);
    }

    public static int getInternetIconResource(int i, boolean z, int i2, boolean z2) {
        if (i < 0) {
            i = 0;
        }
        int[] iArr = WIFI_PIE;
        if (i >= iArr.length) {
            i = iArr.length - 1;
        }
        if (i < 0 || i >= iArr.length) {
            throw new IllegalArgumentException("No Wifi icon found for level: " + i);
        } else if (z) {
            return NO_INTERNET_WIFI_PIE[i];
        } else {
            if (i2 == 4) {
                return WIFI_4_PIE[i];
            }
            if (i2 != 5) {
                if (i2 == 6) {
                    return WIFI_6_PIE[i];
                }
                return iArr[i];
            } else if (z2) {
                return WIFI_6_PIE[i];
            } else {
                return WIFI_5_PIE[i];
            }
        }
    }
}
