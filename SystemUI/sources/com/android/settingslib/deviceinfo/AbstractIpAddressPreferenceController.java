package com.android.settingslib.deviceinfo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.wifi.WifiManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.C1757R;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.Iterator;

public abstract class AbstractIpAddressPreferenceController extends AbstractConnectivityPreferenceController {
    private static final String[] CONNECTIVITY_INTENTS = {ConnectivityManager.CONNECTIVITY_ACTION, "android.net.wifi.LINK_CONFIGURATION_CHANGED", WifiManager.NETWORK_STATE_CHANGED_ACTION};
    static final String KEY_IP_ADDRESS = "wifi_ip_address";
    private final ConnectivityManager mCM;
    private Preference mIpAddress;

    public String getPreferenceKey() {
        return KEY_IP_ADDRESS;
    }

    public boolean isAvailable() {
        return true;
    }

    public AbstractIpAddressPreferenceController(Context context, Lifecycle lifecycle) {
        super(context, lifecycle);
        this.mCM = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
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
            this.mIpAddress.setSummary((CharSequence) defaultIpAddresses);
        } else {
            this.mIpAddress.setSummary(C1757R.string.status_unavailable);
        }
    }

    private static String getDefaultIpAddresses(ConnectivityManager connectivityManager) {
        return formatIpAddresses(connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork()));
    }

    private static String formatIpAddresses(LinkProperties linkProperties) {
        if (linkProperties == null) {
            return null;
        }
        Iterator<LinkAddress> it = linkProperties.getAllLinkAddresses().iterator();
        if (!it.hasNext()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            sb.append(it.next().getAddress().getHostAddress());
            if (it.hasNext()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
