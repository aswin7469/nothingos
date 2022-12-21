package android.net.wifi.util;

import android.net.MacAddress;
import android.net.wifi.ScanResult;
import android.net.wifi.SecurityParams;
import android.net.wifi.WifiConfiguration;
import android.util.Log;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ScanResultUtil {
    private static final String TAG = "ScanResultUtil";

    private ScanResultUtil() {
    }

    public static boolean isScanResultForPskNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("PSK");
    }

    public static boolean isScanResultForWapiPskNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("WAPI-PSK");
    }

    public static boolean isScanResultForWapiCertNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("WAPI-CERT");
    }

    public static boolean isScanResultForEapNetwork(ScanResult scanResult) {
        return (scanResult.capabilities.contains("EAP/SHA1") || scanResult.capabilities.contains("EAP/SHA256") || scanResult.capabilities.contains("FT/EAP") || scanResult.capabilities.contains("EAP-FILS")) && !isScanResultForWpa3EnterpriseOnlyNetwork(scanResult) && !isScanResultForWpa3EnterpriseTransitionNetwork(scanResult);
    }

    private static boolean isScanResultForPmfMandatoryNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("[MFPR]");
    }

    private static boolean isScanResultForPmfCapableNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("[MFPC]");
    }

    public static boolean isScanResultForPasspointR1R2Network(ScanResult scanResult) {
        if (!isScanResultForEapNetwork(scanResult)) {
            return false;
        }
        return scanResult.isPasspointNetwork();
    }

    public static boolean isScanResultForPasspointR3Network(ScanResult scanResult) {
        if ((isScanResultForEapNetwork(scanResult) || isScanResultForWpa3EnterpriseOnlyNetwork(scanResult) || isScanResultForEapSuiteBNetwork(scanResult)) && isScanResultForPmfMandatoryNetwork(scanResult)) {
            return scanResult.isPasspointNetwork();
        }
        return false;
    }

    public static boolean isScanResultForWpa3EnterpriseTransitionNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("EAP/SHA1") && scanResult.capabilities.contains("EAP/SHA256") && scanResult.capabilities.contains("RSN") && !scanResult.capabilities.contains("WEP") && !scanResult.capabilities.contains("TKIP") && !isScanResultForPmfMandatoryNetwork(scanResult) && isScanResultForPmfCapableNetwork(scanResult);
    }

    public static boolean isScanResultForWpa3EnterpriseOnlyNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("EAP/SHA256") && !scanResult.capabilities.contains("EAP/SHA1") && scanResult.capabilities.contains("RSN") && !scanResult.capabilities.contains("WEP") && !scanResult.capabilities.contains("TKIP") && isScanResultForPmfMandatoryNetwork(scanResult) && isScanResultForPmfCapableNetwork(scanResult);
    }

    public static boolean isScanResultForEapSuiteBNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("SUITE_B_192") && scanResult.capabilities.contains("RSN") && !scanResult.capabilities.contains("WEP") && !scanResult.capabilities.contains("TKIP") && isScanResultForPmfMandatoryNetwork(scanResult);
    }

    public static boolean isScanResultForWepNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("WEP");
    }

    public static boolean isScanResultForOweNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("OWE");
    }

    public static boolean isScanResultForOweTransitionNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("OWE_TRANSITION");
    }

    public static boolean isScanResultForSaeNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("SAE");
    }

    public static boolean isScanResultForPskSaeTransitionNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("PSK") && scanResult.capabilities.contains("SAE");
    }

    public static boolean isScanResultForFilsSha256Network(ScanResult scanResult) {
        return scanResult.capabilities.contains("FILS-SHA256");
    }

    public static boolean isScanResultForFilsSha384Network(ScanResult scanResult) {
        return scanResult.capabilities.contains("FILS-SHA384");
    }

    public static boolean isScanResultForDppNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("DPP");
    }

    public static boolean isScanResultForUnknownAkmNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("?");
    }

    public static boolean isScanResultForOpenNetwork(ScanResult scanResult) {
        return !isScanResultForWepNetwork(scanResult) && !isScanResultForPskNetwork(scanResult) && !isScanResultForEapNetwork(scanResult) && !isScanResultForSaeNetwork(scanResult) && !isScanResultForWpa3EnterpriseTransitionNetwork(scanResult) && !isScanResultForWpa3EnterpriseOnlyNetwork(scanResult) && !isScanResultForWapiPskNetwork(scanResult) && !isScanResultForWapiCertNetwork(scanResult) && !isScanResultForEapSuiteBNetwork(scanResult) && !isScanResultForDppNetwork(scanResult) && !isScanResultForUnknownAkmNetwork(scanResult);
    }

    public static String createQuotedSsid(String str) {
        return "\"" + str + "\"";
    }

    public static WifiConfiguration createNetworkFromScanResult(ScanResult scanResult) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = createQuotedSsid(scanResult.SSID);
        List<SecurityParams> generateSecurityParamsListFromScanResult = generateSecurityParamsListFromScanResult(scanResult);
        if (generateSecurityParamsListFromScanResult.isEmpty()) {
            return null;
        }
        wifiConfiguration.setSecurityParams(generateSecurityParamsListFromScanResult);
        return wifiConfiguration;
    }

    public static List<SecurityParams> generateSecurityParamsListFromScanResult(ScanResult scanResult) {
        ArrayList arrayList = new ArrayList();
        if (isScanResultForOweTransitionNetwork(scanResult)) {
            arrayList.add(SecurityParams.createSecurityParamsBySecurityType(0));
            arrayList.add(SecurityParams.createSecurityParamsBySecurityType(6));
            return arrayList;
        } else if (isScanResultForOweNetwork(scanResult)) {
            arrayList.add(SecurityParams.createSecurityParamsBySecurityType(6));
            return arrayList;
        } else if (isScanResultForOpenNetwork(scanResult)) {
            arrayList.add(SecurityParams.createSecurityParamsBySecurityType(0));
            return arrayList;
        } else if (isScanResultForWepNetwork(scanResult)) {
            arrayList.add(SecurityParams.createSecurityParamsBySecurityType(1));
            return arrayList;
        } else if (isScanResultForWapiPskNetwork(scanResult)) {
            arrayList.add(SecurityParams.createSecurityParamsBySecurityType(7));
            return arrayList;
        } else if (isScanResultForWapiCertNetwork(scanResult)) {
            arrayList.add(SecurityParams.createSecurityParamsBySecurityType(8));
            return arrayList;
        } else if (isScanResultForPskNetwork(scanResult) && isScanResultForSaeNetwork(scanResult)) {
            arrayList.add(SecurityParams.createSecurityParamsBySecurityType(2));
            arrayList.add(SecurityParams.createSecurityParamsBySecurityType(4));
            return arrayList;
        } else if (isScanResultForPskNetwork(scanResult)) {
            arrayList.add(SecurityParams.createSecurityParamsBySecurityType(2));
            return arrayList;
        } else if (isScanResultForSaeNetwork(scanResult)) {
            arrayList.add(SecurityParams.createSecurityParamsBySecurityType(4));
            return arrayList;
        } else if (isScanResultForDppNetwork(scanResult)) {
            arrayList.add(SecurityParams.createSecurityParamsBySecurityType(13));
            return arrayList;
        } else {
            if (isScanResultForEapSuiteBNetwork(scanResult)) {
                arrayList.add(SecurityParams.createSecurityParamsBySecurityType(5));
            } else if (isScanResultForWpa3EnterpriseTransitionNetwork(scanResult)) {
                arrayList.add(SecurityParams.createSecurityParamsBySecurityType(3));
                arrayList.add(SecurityParams.createSecurityParamsBySecurityType(9));
            } else if (isScanResultForWpa3EnterpriseOnlyNetwork(scanResult)) {
                arrayList.add(SecurityParams.createSecurityParamsBySecurityType(9));
            } else if (isScanResultForEapNetwork(scanResult)) {
                arrayList.add(SecurityParams.createSecurityParamsBySecurityType(3));
            }
            if (isScanResultForPasspointR1R2Network(scanResult)) {
                arrayList.add(SecurityParams.createSecurityParamsBySecurityType(11));
            }
            if (isScanResultForPasspointR3Network(scanResult)) {
                arrayList.add(SecurityParams.createSecurityParamsBySecurityType(12));
            }
            return arrayList;
        }
    }

    public static void dumpScanResults(PrintWriter printWriter, List<ScanResult> list, long j) {
        String str;
        PrintWriter printWriter2 = printWriter;
        if (list != null && list.size() != 0) {
            printWriter2.println("    BSSID              Frequency      RSSI           Age(sec)     SSID                                 Flags");
            for (ScanResult next : list) {
                long j2 = next.timestamp / 1000;
                String format = j2 <= 0 ? "___?___" : j < j2 ? "  0.000" : j2 < j - 1000000 ? ">1000.0" : String.format("%3.3f", Double.valueOf(((double) (j - j2)) / 1000.0d));
                String str2 = next.SSID == null ? "" : next.SSID;
                int length = next.radioChainInfos == null ? 0 : next.radioChainInfos.length;
                if (length == 1) {
                    str = String.format("%5d(%1d:%3d)       ", Integer.valueOf(next.level), Integer.valueOf(next.radioChainInfos[0].f55id), Integer.valueOf(next.radioChainInfos[0].level));
                } else if (length == 2) {
                    str = String.format("%5d(%1d:%3d/%1d:%3d)", Integer.valueOf(next.level), Integer.valueOf(next.radioChainInfos[0].f55id), Integer.valueOf(next.radioChainInfos[0].level), Integer.valueOf(next.radioChainInfos[1].f55id), Integer.valueOf(next.radioChainInfos[1].level));
                } else {
                    str = String.format("%9d         ", Integer.valueOf(next.level));
                }
                if ((next.flags & 1) == 1) {
                    next.capabilities += "[PASSPOINT]";
                }
                printWriter2.printf("  %17s  %9d  %18s   %7s    %-32s  %s\n", next.BSSID, Integer.valueOf(next.frequency), str, format, String.format("%1.32s", str2), next.capabilities);
            }
        }
    }

    public static boolean validateScanResultList(List<ScanResult> list) {
        if (list == null || list.isEmpty()) {
            Log.w(TAG, "Empty or null ScanResult list");
            return false;
        }
        for (ScanResult next : list) {
            if (!validate(next)) {
                Log.w(TAG, "Invalid ScanResult: " + next);
                return false;
            }
        }
        return true;
    }

    private static boolean validate(ScanResult scanResult) {
        return (scanResult == null || scanResult.SSID == null || scanResult.capabilities == null || scanResult.BSSID == null) ? false : true;
    }

    public static String redactBssid(MacAddress macAddress, int i) {
        if (macAddress == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        byte[] byteArray = macAddress.toByteArray();
        if (i < 0 || i > 6) {
            i = 4;
        }
        for (int i2 = 0; i2 < 6; i2++) {
            if (i2 < i) {
                sb.append("xx");
            } else {
                sb.append(String.format("%02X", Byte.valueOf(byteArray[i2])));
            }
            if (i2 != 5) {
                sb.append(":");
            }
        }
        return sb.toString();
    }
}
