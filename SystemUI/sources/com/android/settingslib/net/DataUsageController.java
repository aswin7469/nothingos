package com.android.settingslib.net;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.NetworkPolicy;
import android.net.NetworkPolicyManager;
import android.net.NetworkTemplate;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Range;
import com.android.internal.util.ArrayUtils;
import java.time.ZonedDateTime;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;

public class DataUsageController {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final long MB_IN_BYTES = 1048576;
    private static final StringBuilder PERIOD_BUILDER;
    private static final Formatter PERIOD_FORMATTER;
    private static final String TAG = "DataUsageController";
    private Callback mCallback;
    private final Context mContext;
    private NetworkNameProvider mNetworkController;
    private final NetworkStatsManager mNetworkStatsManager;
    private final NetworkPolicyManager mPolicyManager;
    private int mSubscriptionId = -1;

    public interface Callback {
        void onMobileDataEnabled(boolean z);
    }

    public static class DataUsageInfo {
        public String carrier;
        public long cycleEnd;
        public long cycleStart;
        public long limitLevel;
        public String period;
        public long startDate;
        public long usageLevel;
        public long warningLevel;
    }

    public interface NetworkNameProvider {
        String getMobileDataNetworkName();
    }

    static {
        StringBuilder sb = new StringBuilder(50);
        PERIOD_BUILDER = sb;
        PERIOD_FORMATTER = new Formatter((Appendable) sb, Locale.getDefault());
    }

    public DataUsageController(Context context) {
        this.mContext = context;
        this.mPolicyManager = NetworkPolicyManager.from(context);
        this.mNetworkStatsManager = (NetworkStatsManager) context.getSystemService(NetworkStatsManager.class);
    }

    public void setNetworkController(NetworkNameProvider networkNameProvider) {
        this.mNetworkController = networkNameProvider;
    }

    public void setSubscriptionId(int i) {
        this.mSubscriptionId = i;
    }

    public long getDefaultWarningLevel() {
        return ((long) this.mContext.getResources().getInteger(17694978)) * 1048576;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    private DataUsageInfo warn(String str) {
        Log.w(TAG, "Failed to get data usage, " + str);
        return null;
    }

    public DataUsageInfo getDataUsageInfo() {
        return getDataUsageInfo(DataUsageUtils.getMobileTemplate(this.mContext, this.mSubscriptionId));
    }

    public DataUsageInfo getWifiDataUsageInfo() {
        return getDataUsageInfo(new NetworkTemplate.Builder(4).build());
    }

    public DataUsageInfo getDataUsageInfo(NetworkTemplate networkTemplate) {
        long j;
        NetworkPolicy findNetworkPolicy = findNetworkPolicy(networkTemplate);
        long currentTimeMillis = System.currentTimeMillis();
        Iterator cycleIterator = findNetworkPolicy != null ? findNetworkPolicy.cycleIterator() : null;
        if (cycleIterator == null || !cycleIterator.hasNext()) {
            j = currentTimeMillis - 2419200000L;
        } else {
            Range range = (Range) cycleIterator.next();
            long epochMilli = ((ZonedDateTime) range.getLower()).toInstant().toEpochMilli();
            currentTimeMillis = ((ZonedDateTime) range.getUpper()).toInstant().toEpochMilli();
            j = epochMilli;
        }
        long usageLevel = getUsageLevel(networkTemplate, j, currentTimeMillis);
        long j2 = 0;
        if (usageLevel < 0) {
            return warn("no entry data");
        }
        DataUsageInfo dataUsageInfo = new DataUsageInfo();
        dataUsageInfo.startDate = j;
        dataUsageInfo.usageLevel = usageLevel;
        dataUsageInfo.period = formatDateRange(j, currentTimeMillis);
        dataUsageInfo.cycleStart = j;
        dataUsageInfo.cycleEnd = currentTimeMillis;
        if (findNetworkPolicy != null) {
            dataUsageInfo.limitLevel = findNetworkPolicy.limitBytes > 0 ? findNetworkPolicy.limitBytes : 0;
            if (findNetworkPolicy.warningBytes > 0) {
                j2 = findNetworkPolicy.warningBytes;
            }
            dataUsageInfo.warningLevel = j2;
        } else {
            dataUsageInfo.warningLevel = getDefaultWarningLevel();
        }
        NetworkNameProvider networkNameProvider = this.mNetworkController;
        if (networkNameProvider != null) {
            dataUsageInfo.carrier = networkNameProvider.getMobileDataNetworkName();
        }
        return dataUsageInfo;
    }

    public long getHistoricalUsageLevel(NetworkTemplate networkTemplate) {
        return getUsageLevel(networkTemplate, 0, System.currentTimeMillis());
    }

    private long getUsageLevel(NetworkTemplate networkTemplate, long j, long j2) {
        try {
            NetworkStats.Bucket querySummaryForDevice = this.mNetworkStatsManager.querySummaryForDevice(networkTemplate, j, j2);
            if (querySummaryForDevice != null) {
                return querySummaryForDevice.getRxBytes() + querySummaryForDevice.getTxBytes();
            }
            Log.w(TAG, "Failed to get data usage, no entry data");
            return -1;
        } catch (RuntimeException unused) {
            Log.w(TAG, "Failed to get data usage, remote call failed");
            return -1;
        }
    }

    private NetworkPolicy findNetworkPolicy(NetworkTemplate networkTemplate) {
        NetworkPolicy[] networkPolicies;
        NetworkPolicyManager networkPolicyManager = this.mPolicyManager;
        if (networkPolicyManager == null || networkTemplate == null || (networkPolicies = networkPolicyManager.getNetworkPolicies()) == null) {
            return null;
        }
        for (NetworkPolicy networkPolicy : networkPolicies) {
            if (networkPolicy != null && networkTemplate.equals(networkPolicy.template)) {
                return networkPolicy;
            }
        }
        return null;
    }

    private static String statsBucketToString(NetworkStats.Bucket bucket) {
        if (bucket == null) {
            return null;
        }
        return "Entry[bucketDuration=" + (bucket.getEndTimeStamp() - bucket.getStartTimeStamp()) + ",bucketStart=" + bucket.getStartTimeStamp() + ",rxBytes=" + bucket.getRxBytes() + ",rxPackets=" + bucket.getRxPackets() + ",txBytes=" + bucket.getTxBytes() + ",txPackets=" + bucket.getTxPackets() + ']';
    }

    public TelephonyManager getTelephonyManager() {
        int i = this.mSubscriptionId;
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            i = SubscriptionManager.getDefaultDataSubscriptionId();
        }
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            int[] activeSubscriptionIdList = SubscriptionManager.from(this.mContext).getActiveSubscriptionIdList();
            if (!ArrayUtils.isEmpty(activeSubscriptionIdList)) {
                i = activeSubscriptionIdList[0];
            }
        }
        return ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
    }

    public void setMobileDataEnabled(boolean z) {
        Log.d(TAG, "setMobileDataEnabled: enabled=" + z);
        getTelephonyManager().setDataEnabled(z);
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onMobileDataEnabled(z);
        }
    }

    public boolean isMobileDataSupported() {
        return getTelephonyManager().isDataCapable() && getTelephonyManager().getSimState() == 5;
    }

    public boolean isMobileDataEnabled() {
        return getTelephonyManager().isDataEnabled();
    }

    static int getNetworkType(NetworkTemplate networkTemplate) {
        if (networkTemplate == null) {
            return -1;
        }
        int matchRule = networkTemplate.getMatchRule();
        if (matchRule != 4) {
            return matchRule != 5 ? 0 : 9;
        }
        return 1;
    }

    private String getActiveSubscriberId() {
        return getTelephonyManager().getSubscriberId();
    }

    private String formatDateRange(long j, long j2) {
        String formatter;
        StringBuilder sb = PERIOD_BUILDER;
        synchronized (sb) {
            sb.setLength(0);
            formatter = DateUtils.formatDateRange(this.mContext, PERIOD_FORMATTER, j, j2, 65552, (String) null).toString();
        }
        return formatter;
    }
}
