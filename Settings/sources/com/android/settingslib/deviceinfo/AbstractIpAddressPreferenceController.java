package com.android.settingslib.deviceinfo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.R$string;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.Iterator;

public abstract class AbstractIpAddressPreferenceController extends AbstractConnectivityPreferenceController {
    private static final String[] CONNECTIVITY_INTENTS = {"android.net.conn.CONNECTIVITY_CHANGE", "android.net.wifi.LINK_CONFIGURATION_CHANGED", "android.net.wifi.STATE_CHANGE"};
    static final String KEY_IP_ADDRESS = "wifi_ip_address";
    private final ConnectivityManager mCM;
    private Preference mIpAddress;
    private String mPrefixIPv4 = "IPv4:";
    private String mPrefixIPv6 = "IPv6:";

    public String getPreferenceKey() {
        return KEY_IP_ADDRESS;
    }

    public boolean isAvailable() {
        return true;
    }

    public AbstractIpAddressPreferenceController(Context context, Lifecycle lifecycle) {
        super(context, lifecycle);
        this.mCM = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
        this.mPrefixIPv4 = context.getResources().getString(R$string.nt_ip_address_ipv4_prefix);
        this.mPrefixIPv6 = context.getResources().getString(R$string.nt_ip_address_ipv6_prefix);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mIpAddress = preferenceScreen.findPreference(KEY_IP_ADDRESS);
        updateConnectivity();
    }

    /* access modifiers changed from: protected */
    public String[] getConnectivityIntents() {
        return CONNECTIVITY_INTENTS;
    }

    /* access modifiers changed from: protected */
    public void updateConnectivity() {
        String defaultIpAddresses = getDefaultIpAddresses(this.mCM);
        if (defaultIpAddresses != null) {
            this.mIpAddress.setSummary((CharSequence) reformatAddressInfo(defaultIpAddresses.split("\n"), this.mPrefixIPv4, this.mPrefixIPv6));
            return;
        }
        this.mIpAddress.setSummary(R$string.status_unavailable);
    }

    private static String getDefaultIpAddresses(ConnectivityManager connectivityManager) {
        return formatIpAddresses(connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork()));
    }

    private static String formatIpAddresses(LinkProperties linkProperties) {
        if (linkProperties == null) {
            return null;
        }
        Iterator it = linkProperties.getAllLinkAddresses().iterator();
        if (!it.hasNext()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            sb.append(((LinkAddress) it.next()).getAddress().getHostAddress());
            if (it.hasNext()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private static String reformatAddressInfo(String[] strArr, String str, String str2) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        for (String str3 : strArr) {
            if (isIPv4(str3)) {
                buildAddressInfo(sb2, str, str3);
            } else if (isIPv6(str3)) {
                buildAddressInfo(sb3, str2, str3);
            } else {
                buildAddressInfo(sb, "", str3);
            }
        }
        if (sb3.length() != 0) {
            sb2.append("\n");
            sb2.append(sb3);
        }
        if (sb.length() != 0) {
            sb2.append("\n");
            sb2.append(sb);
        }
        return sb2.toString();
    }

    private static void buildAddressInfo(StringBuilder sb, String str, String str2) {
        if (sb.length() != 0) {
            sb.append("\n");
        }
        sb.append(str);
        sb.append(str2);
    }

    private static boolean isIPv4(String str) {
        return str != null && !str.isEmpty() && str.contains(".") && !str.contains(":");
    }

    private static boolean isIPv6(String str) {
        return str != null && !str.isEmpty() && str.contains(":");
    }
}
