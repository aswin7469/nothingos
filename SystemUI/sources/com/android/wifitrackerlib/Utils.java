package com.android.wifitrackerlib;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Pair;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

public class Utils {
    public static final int DPM_SECURITY_TYPE_UNKNOWN = -1;

    public static int convertSecurityTypeToDpmWifiSecurity(int i) {
        switch (i) {
            case 0:
            case 6:
                return 0;
            case 1:
            case 2:
            case 4:
            case 7:
                return 1;
            case 3:
            case 8:
            case 9:
            case 11:
            case 12:
                return 2;
            case 5:
                return 3;
            default:
                return -1;
        }
    }

    public static ScanResult getBestScanResultByLevel(List<ScanResult> list) {
        if (list.isEmpty()) {
            return null;
        }
        return (ScanResult) Collections.max(list, Comparator.comparingInt(new Utils$$ExternalSyntheticLambda0()));
    }

    static List<Integer> getSecurityTypesFromScanResult(ScanResult scanResult) {
        ArrayList arrayList = new ArrayList();
        for (int valueOf : scanResult.getSecurityTypes()) {
            arrayList.add(Integer.valueOf(valueOf));
        }
        return arrayList;
    }

    static List<Integer> getSecurityTypesFromWifiConfiguration(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.allowedKeyManagement.get(14)) {
            return Arrays.asList(8);
        } else if (wifiConfiguration.allowedKeyManagement.get(13)) {
            return Arrays.asList(7);
        } else if (wifiConfiguration.allowedKeyManagement.get(10)) {
            return Arrays.asList(5);
        } else if (wifiConfiguration.allowedKeyManagement.get(9)) {
            return Arrays.asList(6);
        } else if (wifiConfiguration.allowedKeyManagement.get(8)) {
            return Arrays.asList(4);
        } else if (wifiConfiguration.allowedKeyManagement.get(4)) {
            return Arrays.asList(2);
        } else if (wifiConfiguration.allowedKeyManagement.get(2)) {
            if (!wifiConfiguration.requirePmf || wifiConfiguration.allowedPairwiseCiphers.get(1) || !wifiConfiguration.allowedProtocols.get(1)) {
                return Arrays.asList(3, 9);
            }
            return Arrays.asList(9);
        } else if (wifiConfiguration.allowedKeyManagement.get(1)) {
            return Arrays.asList(2);
        } else {
            if (wifiConfiguration.allowedKeyManagement.get(0) && wifiConfiguration.wepKeys != null) {
                for (String str : wifiConfiguration.wepKeys) {
                    if (str != null) {
                        return Arrays.asList(1);
                    }
                }
            }
            return Arrays.asList(0);
        }
    }

    static int getSingleSecurityTypeFromMultipleSecurityTypes(List<Integer> list) {
        if (list.size() == 1) {
            return list.get(0).intValue();
        }
        if (list.size() != 2) {
            return -1;
        }
        if (list.contains(0)) {
            return 0;
        }
        if (list.contains(2)) {
            return 2;
        }
        if (list.contains(3)) {
            return 3;
        }
        return -1;
    }

    private static int toDigit(char[] cArr, int i) throws IllegalArgumentException {
        char c = cArr[i];
        if ('0' <= c && c <= '9') {
            return c - '0';
        }
        char c2 = 'a';
        if ('a' > c || c > 'f') {
            c2 = 'A';
            if ('A' > c || c > 'F') {
                throw new IllegalArgumentException("Illegal char: " + cArr[i] + " at offset " + i);
            }
        }
        return (c - c2) + 10;
    }

    private static byte[] decode(char[] cArr, boolean z) throws IllegalArgumentException {
        int length = cArr.length;
        byte[] bArr = new byte[((length + 1) / 2)];
        int i = 0;
        if (z) {
            if (length % 2 != 0) {
                bArr[0] = (byte) toDigit(cArr, 0);
                i = 1;
            }
        } else if (length % 2 != 0) {
            throw new IllegalArgumentException("Invalid input length: " + length);
        }
        int i2 = i;
        while (i < length) {
            bArr[i2] = (byte) ((toDigit(cArr, i) << 4) | toDigit(cArr, i + 1));
            i += 2;
            i2++;
        }
        return bArr;
    }

    private static String decodeSsid(byte[] bArr, Charset charset) {
        CharsetDecoder onUnmappableCharacter = charset.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        CharBuffer allocate = CharBuffer.allocate(32);
        CoderResult decode = onUnmappableCharacter.decode(ByteBuffer.wrap(bArr), allocate, true);
        allocate.flip();
        if (decode.isError()) {
            return null;
        }
        return allocate.toString();
    }

    private static String removeEnclosingQuotes(String str) {
        int length = str.length();
        if (length < 2 || str.charAt(0) != '\"') {
            return str;
        }
        int i = length - 1;
        return str.charAt(i) == '\"' ? str.substring(1, i) : str;
    }

    static String getReadableText(String str) {
        if (!TextUtils.isEmpty(str) && str.charAt(0) != '\"') {
            try {
                byte[] decode = decode(str.toCharArray(), false);
                Charset forName = Charset.forName("UTF-8");
                if (forName != null) {
                    String decodeSsid = decodeSsid(decode, forName);
                    if (!TextUtils.isEmpty(decodeSsid)) {
                        return decodeSsid;
                    }
                }
                Charset forName2 = Charset.forName("GBK");
                if (forName2 != null) {
                    String decodeSsid2 = decodeSsid(decode, forName2);
                    if (!TextUtils.isEmpty(decodeSsid2)) {
                        return decodeSsid2;
                    }
                }
            } catch (IllegalArgumentException unused) {
            }
        }
        return removeEnclosingQuotes(str);
    }

    static String getAppLabel(Context context, String str) {
        try {
            return context.getPackageManager().getApplicationInfo(str, 0).loadLabel(context.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException unused) {
            return "";
        }
    }

    static String getConnectedDescription(Context context, WifiConfiguration wifiConfiguration, NetworkCapabilities networkCapabilities, boolean z, boolean z2) {
        StringJoiner stringJoiner = new StringJoiner(context.getString(C3351R.string.wifitrackerlib_summary_separator));
        if (wifiConfiguration != null && (wifiConfiguration.fromWifiNetworkSuggestion || wifiConfiguration.fromWifiNetworkSpecifier)) {
            String suggestionOrSpecifierLabel = getSuggestionOrSpecifierLabel(context, wifiConfiguration);
            if (!TextUtils.isEmpty(suggestionOrSpecifierLabel)) {
                if (!z) {
                    stringJoiner.add(context.getString(C3351R.string.wifitrackerlib_available_via_app, new Object[]{suggestionOrSpecifierLabel}));
                } else {
                    stringJoiner.add(context.getString(C3351R.string.wifitrackerlib_connected_via_app, new Object[]{suggestionOrSpecifierLabel}));
                }
            }
        }
        if (z2) {
            stringJoiner.add(context.getString(C3351R.string.wifi_connected_low_quality));
        }
        String currentNetworkCapabilitiesInformation = getCurrentNetworkCapabilitiesInformation(context, networkCapabilities);
        if (!TextUtils.isEmpty(currentNetworkCapabilitiesInformation)) {
            stringJoiner.add(currentNetworkCapabilitiesInformation);
        }
        if (stringJoiner.length() != 0 || !z) {
            return stringJoiner.toString();
        }
        return context.getResources().getStringArray(C3351R.array.wifitrackerlib_wifi_status)[NetworkInfo.DetailedState.CONNECTED.ordinal()];
    }

    static String getConnectingDescription(Context context, NetworkInfo networkInfo) {
        NetworkInfo.DetailedState detailedState;
        if (context == null || networkInfo == null || (detailedState = networkInfo.getDetailedState()) == null) {
            return "";
        }
        String[] stringArray = context.getResources().getStringArray(C3351R.array.wifitrackerlib_wifi_status);
        int ordinal = detailedState.ordinal();
        if (ordinal >= stringArray.length) {
            return "";
        }
        return stringArray[ordinal];
    }

    static String getDisconnectedDescription(WifiTrackerInjector wifiTrackerInjector, Context context, WifiConfiguration wifiConfiguration, boolean z, boolean z2) {
        if (context == null || wifiConfiguration == null) {
            return "";
        }
        StringJoiner stringJoiner = new StringJoiner(context.getString(C3351R.string.wifitrackerlib_summary_separator));
        if (z2) {
            stringJoiner.add(context.getString(C3351R.string.wifitrackerlib_wifi_disconnected));
        } else if (!z || wifiConfiguration.isPasspoint()) {
            if (wifiConfiguration.fromWifiNetworkSuggestion) {
                String suggestionOrSpecifierLabel = getSuggestionOrSpecifierLabel(context, wifiConfiguration);
                if (!TextUtils.isEmpty(suggestionOrSpecifierLabel)) {
                    stringJoiner.add(context.getString(C3351R.string.wifitrackerlib_available_via_app, new Object[]{suggestionOrSpecifierLabel}));
                }
            } else {
                stringJoiner.add(context.getString(C3351R.string.wifitrackerlib_wifi_remembered));
            }
        } else if (!wifiTrackerInjector.getNoAttributionAnnotationPackages().contains(wifiConfiguration.creatorName)) {
            String appLabel = getAppLabel(context, wifiConfiguration.creatorName);
            if (!TextUtils.isEmpty(appLabel)) {
                stringJoiner.add(context.getString(C3351R.string.wifitrackerlib_saved_network, new Object[]{appLabel}));
            }
        }
        String wifiConfigurationFailureMessage = getWifiConfigurationFailureMessage(context, wifiConfiguration);
        if (!TextUtils.isEmpty(wifiConfigurationFailureMessage)) {
            stringJoiner.add(wifiConfigurationFailureMessage);
        }
        return stringJoiner.toString();
    }

    private static String getSuggestionOrSpecifierLabel(Context context, WifiConfiguration wifiConfiguration) {
        if (context == null || wifiConfiguration == null) {
            return "";
        }
        String carrierNameForSubId = getCarrierNameForSubId(context, getSubIdForConfig(context, wifiConfiguration));
        if (!TextUtils.isEmpty(carrierNameForSubId)) {
            return carrierNameForSubId;
        }
        String appLabel = getAppLabel(context, wifiConfiguration.creatorName);
        if (!TextUtils.isEmpty(appLabel)) {
            return appLabel;
        }
        return wifiConfiguration.creatorName;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0049, code lost:
        if (r1 != 9) goto L_0x006f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getWifiConfigurationFailureMessage(android.content.Context r4, android.net.wifi.WifiConfiguration r5) {
        /*
            java.lang.String r0 = ""
            if (r4 == 0) goto L_0x00a5
            if (r5 != 0) goto L_0x0008
            goto L_0x00a5
        L_0x0008:
            boolean r1 = r5.hasNoInternetAccess()
            r2 = 2
            if (r1 == 0) goto L_0x0023
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r5 = r5.getNetworkSelectionStatus()
            int r5 = r5.getNetworkSelectionStatus()
            if (r5 != r2) goto L_0x001c
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_no_internet_no_reconnect
            goto L_0x001e
        L_0x001c:
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_no_internet
        L_0x001e:
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0023:
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r1 = r5.getNetworkSelectionStatus()
            int r1 = r1.getNetworkSelectionStatus()
            if (r1 == 0) goto L_0x006f
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r1 = r5.getNetworkSelectionStatus()
            int r1 = r1.getNetworkSelectionDisableReason()
            r3 = 1
            if (r1 == r3) goto L_0x0068
            if (r1 == r2) goto L_0x0061
            r2 = 3
            if (r1 == r2) goto L_0x005a
            r2 = 4
            if (r1 == r2) goto L_0x0053
            r2 = 6
            if (r1 == r2) goto L_0x0053
            r2 = 8
            if (r1 == r2) goto L_0x004c
            r2 = 9
            if (r1 == r2) goto L_0x0061
            goto L_0x006f
        L_0x004c:
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_check_password_try_again
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0053:
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_no_internet_no_reconnect
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x005a:
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_disabled_network_failure
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0061:
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_disabled_password_failure
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0068:
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_disabled_generic
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x006f:
            int r5 = r5.getRecentFailureReason()
            r1 = 17
            if (r5 == r1) goto L_0x009e
            switch(r5) {
                case 1002: goto L_0x009e;
                case 1003: goto L_0x0097;
                case 1004: goto L_0x009e;
                case 1005: goto L_0x0090;
                case 1006: goto L_0x0089;
                case 1007: goto L_0x0090;
                case 1008: goto L_0x0090;
                case 1009: goto L_0x0082;
                case 1010: goto L_0x0082;
                case 1011: goto L_0x007b;
                default: goto L_0x007a;
            }
        L_0x007a:
            return r0
        L_0x007b:
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_network_not_found
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0082:
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_mbo_oce_assoc_disallowed_insufficient_rssi
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0089:
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_mbo_assoc_disallowed_max_num_sta_associated
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0090:
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_mbo_assoc_disallowed_cannot_connect
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0097:
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_poor_channel_conditions
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x009e:
            int r5 = com.android.wifitrackerlib.C3351R.string.wifitrackerlib_wifi_ap_unable_to_handle_new_sta
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x00a5:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.Utils.getWifiConfigurationFailureMessage(android.content.Context, android.net.wifi.WifiConfiguration):java.lang.String");
    }

    static String getAutoConnectDescription(Context context, WifiEntry wifiEntry) {
        if (context == null || wifiEntry == null || !wifiEntry.canSetAutoJoinEnabled() || wifiEntry.isAutoJoinEnabled()) {
            return "";
        }
        return context.getString(C3351R.string.wifitrackerlib_auto_connect_disable);
    }

    static String getMeteredDescription(Context context, WifiEntry wifiEntry) {
        if (context == null || wifiEntry == null) {
            return "";
        }
        if (!wifiEntry.canSetMeteredChoice() && wifiEntry.getMeteredChoice() != 1) {
            return "";
        }
        if (wifiEntry.getMeteredChoice() == 1) {
            return context.getString(C3351R.string.wifitrackerlib_wifi_metered_label);
        }
        if (wifiEntry.getMeteredChoice() == 2) {
            return context.getString(C3351R.string.wifitrackerlib_wifi_unmetered_label);
        }
        if (wifiEntry.isMetered()) {
            return context.getString(C3351R.string.wifitrackerlib_wifi_metered_label);
        }
        return "";
    }

    static String getVerboseLoggingDescription(WifiEntry wifiEntry) {
        if (!BaseWifiTracker.isVerboseLoggingEnabled() || wifiEntry == null) {
            return "";
        }
        StringJoiner stringJoiner = new StringJoiner(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        String wifiInfoDescription = wifiEntry.getWifiInfoDescription();
        if (!TextUtils.isEmpty(wifiInfoDescription)) {
            stringJoiner.add(wifiInfoDescription);
        }
        String networkCapabilityDescription = wifiEntry.getNetworkCapabilityDescription();
        if (!TextUtils.isEmpty(networkCapabilityDescription)) {
            stringJoiner.add(networkCapabilityDescription);
        }
        String scanResultDescription = wifiEntry.getScanResultDescription();
        if (!TextUtils.isEmpty(scanResultDescription)) {
            stringJoiner.add(scanResultDescription);
        }
        String networkSelectionDescription = wifiEntry.getNetworkSelectionDescription();
        if (!TextUtils.isEmpty(networkSelectionDescription)) {
            stringJoiner.add(networkSelectionDescription);
        }
        return stringJoiner.toString();
    }

    static String getNetworkSelectionDescription(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        WifiConfiguration.NetworkSelectionStatus networkSelectionStatus = wifiConfiguration.getNetworkSelectionStatus();
        if (networkSelectionStatus.getNetworkSelectionStatus() != 0) {
            sb.append(" (" + networkSelectionStatus.getNetworkStatusString());
            if (networkSelectionStatus.getDisableTime() > 0) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + DateUtils.formatElapsedTime((System.currentTimeMillis() - networkSelectionStatus.getDisableTime()) / 1000));
            }
            sb.append(NavigationBarInflaterView.KEY_CODE_END);
        }
        int maxNetworkSelectionDisableReason = WifiConfiguration.NetworkSelectionStatus.getMaxNetworkSelectionDisableReason();
        for (int i = 0; i <= maxNetworkSelectionDisableReason; i++) {
            int disableReasonCounter = networkSelectionStatus.getDisableReasonCounter(i);
            if (disableReasonCounter != 0) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER).append(WifiConfiguration.NetworkSelectionStatus.getNetworkSelectionDisableReasonString(i)).append("=").append(disableReasonCounter);
            }
        }
        return sb.toString();
    }

    static String getCurrentNetworkCapabilitiesInformation(Context context, NetworkCapabilities networkCapabilities) {
        if (!(context == null || networkCapabilities == null)) {
            if (networkCapabilities.hasCapability(17)) {
                return context.getString(context.getResources().getIdentifier("network_available_sign_in", "string", "android"));
            }
            if (networkCapabilities.hasCapability(24)) {
                return context.getString(C3351R.string.wifitrackerlib_wifi_limited_connection);
            }
            if (!networkCapabilities.hasCapability(16)) {
                if (networkCapabilities.isPrivateDnsBroken()) {
                    return context.getString(C3351R.string.wifitrackerlib_private_dns_broken);
                }
                return context.getString(C3351R.string.wifitrackerlib_wifi_connected_cannot_provide_internet);
            }
        }
        return "";
    }

    static String getNetworkDetailedState(Context context, NetworkInfo networkInfo) {
        NetworkInfo.DetailedState detailedState;
        if (context == null || networkInfo == null || (detailedState = networkInfo.getDetailedState()) == null) {
            return "";
        }
        String[] stringArray = context.getResources().getStringArray(C3351R.array.wifitrackerlib_wifi_status);
        int ordinal = detailedState.ordinal();
        if (ordinal >= stringArray.length) {
            return "";
        }
        return stringArray[ordinal];
    }

    static boolean isSimPresent(Context context, int i) {
        List<SubscriptionInfo> activeSubscriptionInfoList;
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
        if (subscriptionManager == null || (activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList()) == null || activeSubscriptionInfoList.isEmpty()) {
            return false;
        }
        if (i == -1) {
            return true;
        }
        return activeSubscriptionInfoList.stream().anyMatch(new Utils$$ExternalSyntheticLambda1(i));
    }

    static /* synthetic */ boolean lambda$isSimPresent$1(int i, SubscriptionInfo subscriptionInfo) {
        return subscriptionInfo.getCarrierId() == i;
    }

    static String getCarrierNameForSubId(Context context, int i) {
        TelephonyManager telephonyManager;
        TelephonyManager createForSubscriptionId;
        CharSequence simCarrierIdName;
        if (i == -1 || (telephonyManager = (TelephonyManager) context.getSystemService("phone")) == null || (createForSubscriptionId = telephonyManager.createForSubscriptionId(i)) == null || (simCarrierIdName = createForSubscriptionId.getSimCarrierIdName()) == null) {
            return null;
        }
        return simCarrierIdName.toString();
    }

    static boolean isServerCertUsedNetwork(WifiConfiguration wifiConfiguration) {
        return wifiConfiguration.enterpriseConfig != null && wifiConfiguration.enterpriseConfig.isEapMethodServerCertUsed();
    }

    static boolean isSimCredential(WifiConfiguration wifiConfiguration) {
        return wifiConfiguration.enterpriseConfig != null && wifiConfiguration.enterpriseConfig.isAuthenticationSimBased();
    }

    static int getSubIdForConfig(Context context, WifiConfiguration wifiConfiguration) {
        SubscriptionManager subscriptionManager;
        int i = -1;
        if (wifiConfiguration.carrierId == -1 || (subscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service")) == null) {
            return -1;
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList != null && !activeSubscriptionInfoList.isEmpty()) {
            int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            for (SubscriptionInfo next : activeSubscriptionInfoList) {
                if (next.getCarrierId() == wifiConfiguration.carrierId && (i = next.getSubscriptionId()) == defaultDataSubscriptionId) {
                    break;
                }
            }
        }
        return i;
    }

    static boolean isImsiPrivacyProtectionProvided(Context context, int i) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null || (configForSubId.getInt("imsi_key_availability_int") & 2) == 0) {
            return false;
        }
        return true;
    }

    static CharSequence getImsiProtectionDescription(Context context, WifiConfiguration wifiConfiguration) {
        int i;
        if (context != null && wifiConfiguration != null && isSimCredential(wifiConfiguration) && !isServerCertUsedNetwork(wifiConfiguration)) {
            if (wifiConfiguration.carrierId == -1) {
                i = SubscriptionManager.getDefaultSubscriptionId();
            } else {
                i = getSubIdForConfig(context, wifiConfiguration);
            }
            if (i != -1 && !isImsiPrivacyProtectionProvided(context, i)) {
                return NonSdkApiWrapper.linkifyAnnotation(context, context.getText(C3351R.string.wifitrackerlib_imsi_protection_warning), "url", context.getString(C3351R.string.wifitrackerlib_help_url_imsi_protection));
            }
        }
        return "";
    }

    private static boolean isScanResultForPskNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("PSK");
    }

    private static boolean isScanResultForWapiPskNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("WAPI-PSK");
    }

    private static boolean isScanResultForWapiCertNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("WAPI-CERT");
    }

    private static boolean isScanResultForEapNetwork(ScanResult scanResult) {
        return (scanResult.capabilities.contains("EAP/SHA1") || scanResult.capabilities.contains("EAP/SHA256") || scanResult.capabilities.contains("FT/EAP") || scanResult.capabilities.contains("EAP-FILS")) && !isScanResultForWpa3EnterpriseOnlyNetwork(scanResult) && !isScanResultForWpa3EnterpriseTransitionNetwork(scanResult);
    }

    private static boolean isScanResultForPmfMandatoryNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("[MFPR]");
    }

    private static boolean isScanResultForPmfCapableNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("[MFPC]");
    }

    private static boolean isScanResultForWpa3EnterpriseTransitionNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("EAP/SHA1") && scanResult.capabilities.contains("EAP/SHA256") && scanResult.capabilities.contains("RSN") && !scanResult.capabilities.contains("WEP") && !scanResult.capabilities.contains("TKIP") && !isScanResultForPmfMandatoryNetwork(scanResult) && isScanResultForPmfCapableNetwork(scanResult);
    }

    private static boolean isScanResultForWpa3EnterpriseOnlyNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("EAP/SHA256") && !scanResult.capabilities.contains("EAP/SHA1") && scanResult.capabilities.contains("RSN") && !scanResult.capabilities.contains("WEP") && !scanResult.capabilities.contains("TKIP") && isScanResultForPmfMandatoryNetwork(scanResult) && isScanResultForPmfCapableNetwork(scanResult);
    }

    private static boolean isScanResultForEapSuiteBNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("SUITE_B_192") && scanResult.capabilities.contains("RSN") && !scanResult.capabilities.contains("WEP") && !scanResult.capabilities.contains("TKIP") && isScanResultForPmfMandatoryNetwork(scanResult);
    }

    private static boolean isScanResultForWepNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("WEP");
    }

    private static boolean isScanResultForOweNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("OWE");
    }

    private static boolean isScanResultForOweTransitionNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("OWE_TRANSITION");
    }

    private static boolean isScanResultForSaeNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("SAE");
    }

    private static boolean isScanResultForPskSaeTransitionNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("PSK") && scanResult.capabilities.contains("SAE");
    }

    private static boolean isScanResultForUnknownAkmNetwork(ScanResult scanResult) {
        return scanResult.capabilities.contains("?");
    }

    private static boolean isScanResultForOpenNetwork(ScanResult scanResult) {
        return !isScanResultForWepNetwork(scanResult) && !isScanResultForPskNetwork(scanResult) && !isScanResultForEapNetwork(scanResult) && !isScanResultForSaeNetwork(scanResult) && !isScanResultForWpa3EnterpriseTransitionNetwork(scanResult) && !isScanResultForWpa3EnterpriseOnlyNetwork(scanResult) && !isScanResultForWapiPskNetwork(scanResult) && !isScanResultForWapiCertNetwork(scanResult) && !isScanResultForEapSuiteBNetwork(scanResult) && !isScanResultForUnknownAkmNetwork(scanResult);
    }

    public static InetAddress getNetworkPart(InetAddress inetAddress, int i) {
        byte[] address = inetAddress.getAddress();
        maskRawAddress(address, i);
        try {
            return InetAddress.getByAddress(address);
        } catch (UnknownHostException e) {
            throw new RuntimeException("getNetworkPart error - " + e.toString());
        }
    }

    public static void maskRawAddress(byte[] bArr, int i) {
        if (i < 0 || i > bArr.length * 8) {
            throw new RuntimeException("IP address with " + bArr.length + " bytes has invalid prefix length " + i);
        }
        int i2 = i / 8;
        byte b = (byte) (255 << (8 - (i % 8)));
        if (i2 < bArr.length) {
            bArr[i2] = (byte) (b & bArr[i2]);
        }
        while (true) {
            i2++;
            if (i2 < bArr.length) {
                bArr[i2] = 0;
            } else {
                return;
            }
        }
    }

    private static Context createPackageContextAsUser(int i, Context context) {
        try {
            return context.createPackageContextAsUser(context.getPackageName(), 0, UserHandle.getUserHandleForUid(i));
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    private static DevicePolicyManager retrieveDevicePolicyManagerFromUserContext(int i, Context context) {
        Context createPackageContextAsUser = createPackageContextAsUser(i, context);
        if (createPackageContextAsUser == null) {
            return null;
        }
        return (DevicePolicyManager) createPackageContextAsUser.getSystemService(DevicePolicyManager.class);
    }

    private static Pair<UserHandle, ComponentName> getDeviceOwner(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        if (devicePolicyManager == null) {
            return null;
        }
        try {
            UserHandle deviceOwnerUser = devicePolicyManager.getDeviceOwnerUser();
            ComponentName deviceOwnerComponentOnAnyUser = devicePolicyManager.getDeviceOwnerComponentOnAnyUser();
            if (deviceOwnerUser == null || deviceOwnerComponentOnAnyUser == null || deviceOwnerComponentOnAnyUser.getPackageName() == null) {
                return null;
            }
            return new Pair<>(deviceOwnerUser, deviceOwnerComponentOnAnyUser);
        } catch (Exception e) {
            throw new RuntimeException("getDeviceOwner error - " + e.toString());
        }
    }

    public static boolean isDeviceOwner(int i, String str, Context context) {
        Pair<UserHandle, ComponentName> deviceOwner;
        if (str != null && (deviceOwner = getDeviceOwner(context)) != null && ((UserHandle) deviceOwner.first).equals(UserHandle.getUserHandleForUid(i)) && ((ComponentName) deviceOwner.second).getPackageName().equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isProfileOwner(int i, String str, Context context) {
        DevicePolicyManager retrieveDevicePolicyManagerFromUserContext;
        if (str == null || (retrieveDevicePolicyManagerFromUserContext = retrieveDevicePolicyManagerFromUserContext(i, context)) == null) {
            return false;
        }
        return retrieveDevicePolicyManagerFromUserContext.isProfileOwnerApp(str);
    }

    public static boolean isDeviceOrProfileOwner(int i, String str, Context context) {
        return isDeviceOwner(i, str, context) || isProfileOwner(i, str, context);
    }

    public static String getStandardString(Context context, int i) {
        if (i == 1) {
            return context.getString(C3351R.string.wifitrackerlib_wifi_standard_legacy);
        }
        switch (i) {
            case 4:
                return context.getString(C3351R.string.wifitrackerlib_wifi_standard_11n);
            case 5:
                return context.getString(C3351R.string.wifitrackerlib_wifi_standard_11ac);
            case 6:
                return context.getString(C3351R.string.wifitrackerlib_wifi_standard_11ax);
            case 7:
                return context.getString(C3351R.string.wifitrackerlib_wifi_standard_11ad);
            case 8:
                return context.getString(C3351R.string.wifitrackerlib_wifi_standard_11be);
            default:
                return context.getString(C3351R.string.wifitrackerlib_wifi_standard_unknown);
        }
    }
}
