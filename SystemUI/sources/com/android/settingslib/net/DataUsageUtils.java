package com.android.settingslib.net;

import android.content.Context;
import android.net.NetworkTemplate;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.util.ArrayUtils;
import java.util.List;
import java.util.Set;

public class DataUsageUtils {
    private static final String TAG = "DataUsageUtils";

    public static NetworkTemplate getMobileTemplate(Context context, int i) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        int subscriptionId = telephonyManager.getSubscriptionId();
        List<SubscriptionInfo> availableSubscriptionInfoList = ((SubscriptionManager) context.getSystemService(SubscriptionManager.class)).getAvailableSubscriptionInfoList();
        if (availableSubscriptionInfoList == null) {
            Log.i(TAG, "Subscription is not inited: " + i);
            return getMobileTemplateForSubId(telephonyManager, subscriptionId);
        }
        for (SubscriptionInfo subscriptionInfo : availableSubscriptionInfoList) {
            if (subscriptionInfo != null && subscriptionInfo.getSubscriptionId() == i) {
                return getNormalizedMobileTemplate(telephonyManager, i);
            }
        }
        Log.i(TAG, "Subscription is not active: " + i);
        return getMobileTemplateForSubId(telephonyManager, subscriptionId);
    }

    private static NetworkTemplate getNormalizedMobileTemplate(TelephonyManager telephonyManager, int i) {
        NetworkTemplate mobileTemplateForSubId = getMobileTemplateForSubId(telephonyManager, i);
        Set of = Set.m1761of((E[]) telephonyManager.createForSubscriptionId(i).getMergedImsisFromGroup());
        if (!ArrayUtils.isEmpty(of)) {
            return normalizeMobileTemplate(mobileTemplateForSubId, of);
        }
        Log.i(TAG, "mergedSubscriberIds is null.");
        return mobileTemplateForSubId;
    }

    private static NetworkTemplate normalizeMobileTemplate(NetworkTemplate networkTemplate, Set<String> set) {
        return (!networkTemplate.getSubscriberIds().isEmpty() && set.contains(networkTemplate.getSubscriberIds().iterator().next())) ? new NetworkTemplate.Builder(networkTemplate.getMatchRule()).setSubscriberIds(set).setWifiNetworkKeys(networkTemplate.getWifiNetworkKeys()).setMeteredness(1).build() : networkTemplate;
    }

    private static NetworkTemplate getMobileTemplateForSubId(TelephonyManager telephonyManager, int i) {
        String subscriberId = telephonyManager.getSubscriberId(i);
        if (subscriberId != null) {
            return new NetworkTemplate.Builder(10).setSubscriberIds(Set.m1751of(subscriberId)).setMeteredness(1).build();
        }
        return new NetworkTemplate.Builder(1).setMeteredness(1).build();
    }
}
