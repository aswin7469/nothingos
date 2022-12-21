package com.android.settingslib.mobile;

import android.content.Context;
import android.content.res.Resources;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyDisplayInfo;
import com.android.settingslib.C1757R;
import com.android.settingslib.SignalIcon;
import java.util.HashMap;
import java.util.Map;

public class MobileMappings {
    public static String getIconKey(TelephonyDisplayInfo telephonyDisplayInfo) {
        if (telephonyDisplayInfo.getOverrideNetworkType() == 0) {
            return toIconKey(telephonyDisplayInfo.getNetworkType());
        }
        return toDisplayIconKey(telephonyDisplayInfo.getOverrideNetworkType());
    }

    public static String toIconKey(int i) {
        return Integer.toString(i);
    }

    public static String toDisplayIconKey(int i) {
        if (i == 1) {
            return toIconKey(13) + "_CA";
        }
        if (i == 2) {
            return toIconKey(13) + "_CA_Plus";
        }
        if (i != 3) {
            return i != 5 ? "unsupported" : toIconKey(20) + "_Plus";
        }
        return toIconKey(20);
    }

    public static SignalIcon.MobileIconGroup getDefaultIcons(Config config) {
        if (!config.showAtLeast3G) {
            return TelephonyIcons.f250G;
        }
        return TelephonyIcons.THREE_G;
    }

    public static Map<String, SignalIcon.MobileIconGroup> mapIconSets(Config config) {
        HashMap hashMap = new HashMap();
        hashMap.put(toIconKey(5), TelephonyIcons.THREE_G);
        hashMap.put(toIconKey(6), TelephonyIcons.THREE_G);
        hashMap.put(toIconKey(12), TelephonyIcons.THREE_G);
        hashMap.put(toIconKey(14), TelephonyIcons.THREE_G);
        if (config.show4gFor3g) {
            hashMap.put(toIconKey(3), TelephonyIcons.FOUR_G);
        } else {
            hashMap.put(toIconKey(3), TelephonyIcons.THREE_G);
        }
        hashMap.put(toIconKey(17), TelephonyIcons.THREE_G);
        if (!config.showAtLeast3G) {
            hashMap.put(toIconKey(0), TelephonyIcons.UNKNOWN);
            hashMap.put(toIconKey(2), TelephonyIcons.f249E);
            hashMap.put(toIconKey(1), TelephonyIcons.f250G);
            hashMap.put(toIconKey(4), TelephonyIcons.ONE_X);
            hashMap.put(toIconKey(7), TelephonyIcons.ONE_X);
        } else {
            hashMap.put(toIconKey(0), TelephonyIcons.THREE_G);
            hashMap.put(toIconKey(2), TelephonyIcons.THREE_G);
            hashMap.put(toIconKey(1), TelephonyIcons.THREE_G);
            hashMap.put(toIconKey(4), TelephonyIcons.THREE_G);
            hashMap.put(toIconKey(7), TelephonyIcons.THREE_G);
        }
        SignalIcon.MobileIconGroup mobileIconGroup = TelephonyIcons.THREE_G;
        SignalIcon.MobileIconGroup mobileIconGroup2 = TelephonyIcons.THREE_G;
        if (config.show4gFor3g) {
            mobileIconGroup = TelephonyIcons.FOUR_G;
            mobileIconGroup2 = TelephonyIcons.FOUR_G;
        } else if (config.hspaDataDistinguishable) {
            mobileIconGroup = TelephonyIcons.f251H;
            mobileIconGroup2 = TelephonyIcons.H_PLUS;
        }
        hashMap.put(toIconKey(8), mobileIconGroup);
        hashMap.put(toIconKey(9), mobileIconGroup);
        hashMap.put(toIconKey(10), mobileIconGroup);
        hashMap.put(toIconKey(15), mobileIconGroup2);
        if (config.show4gForLte) {
            hashMap.put(toIconKey(13), TelephonyIcons.FOUR_G);
            if (config.hideLtePlus) {
                hashMap.put(toDisplayIconKey(1), TelephonyIcons.FOUR_G);
            } else {
                hashMap.put(toDisplayIconKey(1), TelephonyIcons.FOUR_G_PLUS);
            }
        } else if (config.show4glteForLte) {
            hashMap.put(toIconKey(13), TelephonyIcons.FOUR_G_LTE);
            if (config.hideLtePlus) {
                hashMap.put(toDisplayIconKey(1), TelephonyIcons.FOUR_G_LTE);
            } else {
                hashMap.put(toDisplayIconKey(1), TelephonyIcons.FOUR_G_LTE_PLUS);
            }
        } else {
            hashMap.put(toIconKey(13), TelephonyIcons.LTE);
            if (config.hideLtePlus) {
                hashMap.put(toDisplayIconKey(1), TelephonyIcons.LTE);
            } else {
                hashMap.put(toDisplayIconKey(1), TelephonyIcons.LTE_PLUS);
            }
        }
        hashMap.put(toIconKey(18), TelephonyIcons.WFC);
        hashMap.put(toDisplayIconKey(2), TelephonyIcons.LTE_CA_5G_E);
        hashMap.put(toDisplayIconKey(3), TelephonyIcons.NR_5G);
        hashMap.put(toDisplayIconKey(5), TelephonyIcons.NR_5G_PLUS);
        hashMap.put(toIconKey(20), TelephonyIcons.NR_5G);
        return hashMap;
    }

    public static class Config {
        public boolean alwaysShowCdmaRssi = false;
        public boolean alwaysShowDataRatIcon = false;
        public boolean alwaysShowNetworkTypeIcon = false;
        public boolean enableDdsRatIconEnhancement = false;
        public boolean enableRatIconEnhancement = false;
        public boolean hideLtePlus = false;
        public boolean hideNoInternetState = false;
        public boolean hspaDataDistinguishable;
        public boolean show4gFor3g = false;
        public boolean show4gForLte = false;
        public boolean show4glteForLte = false;
        public boolean showAtLeast3G = false;
        public boolean showRsrpSignalLevelforLTE = false;
        public boolean showVolteIcon = false;
        public boolean showVowifiIcon = false;
        public boolean signalSmooth = true;

        public static Config readConfig(Context context) {
            Config config = new Config();
            Resources resources = context.getResources();
            config.showAtLeast3G = resources.getBoolean(C1757R.bool.config_showMin3G);
            config.alwaysShowCdmaRssi = resources.getBoolean(17891366);
            config.hspaDataDistinguishable = resources.getBoolean(C1757R.bool.config_hspa_data_distinguishable);
            SubscriptionManager.from(context);
            PersistableBundle configForSubId = ((CarrierConfigManager) context.getSystemService("carrier_config")).getConfigForSubId(SubscriptionManager.getDefaultDataSubscriptionId());
            if (configForSubId != null) {
                config.alwaysShowDataRatIcon = configForSubId.getBoolean("always_show_data_rat_icon_bool");
                config.show4gForLte = configForSubId.getBoolean("show_4g_for_lte_data_icon_bool");
                config.show4glteForLte = configForSubId.getBoolean("show_4glte_for_lte_data_icon_bool");
                config.show4gFor3g = configForSubId.getBoolean("show_4g_for_3g_data_icon_bool");
                config.hideLtePlus = configForSubId.getBoolean("hide_lte_plus_data_icon_bool");
            }
            config.alwaysShowNetworkTypeIcon = resources.getBoolean(C1757R.bool.config_alwaysShowTypeIcon);
            config.showRsrpSignalLevelforLTE = resources.getBoolean(C1757R.bool.config_showRsrpSignalLevelforLTE);
            config.hideNoInternetState = resources.getBoolean(C1757R.bool.config_hideNoInternetState);
            config.showVolteIcon = resources.getBoolean(C1757R.bool.config_display_volte);
            config.showVowifiIcon = resources.getBoolean(C1757R.bool.config_display_vowifi);
            config.signalSmooth = resources.getBoolean(C1757R.bool.config_signal_smooth);
            if (config.alwaysShowNetworkTypeIcon) {
                config.hideLtePlus = false;
            }
            config.enableRatIconEnhancement = SystemProperties.getBoolean("persist.sysui.rat_icon_enhancement", false);
            config.enableDdsRatIconEnhancement = SystemProperties.getBoolean("persist.sysui.dds_rat_icon_enhancement", false);
            config.showVowifiIcon |= SystemProperties.getBoolean("persist.sysui.enable_vowifi_icon", false);
            return config;
        }
    }
}
