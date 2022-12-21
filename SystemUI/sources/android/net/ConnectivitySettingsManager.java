package android.net;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.connectivity.com.android.net.module.util.ConnectivitySettingsUtils;
import android.net.connectivity.com.android.net.module.util.ProxyUtils;
import android.os.Binder;
import android.os.Build;
import android.os.Process;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Range;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.Duration;
import java.util.Set;
import java.util.StringJoiner;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class ConnectivitySettingsManager {
    public static final String CAPTIVE_PORTAL_HTTP_URL = "captive_portal_http_url";
    public static final String CAPTIVE_PORTAL_MODE = "captive_portal_mode";
    public static final int CAPTIVE_PORTAL_MODE_AVOID = 2;
    public static final int CAPTIVE_PORTAL_MODE_IGNORE = 0;
    public static final int CAPTIVE_PORTAL_MODE_PROMPT = 1;
    public static final String CONNECTIVITY_RELEASE_PENDING_INTENT_DELAY_MS = "connectivity_release_pending_intent_delay_ms";
    public static final String DATA_ACTIVITY_TIMEOUT_MOBILE = "data_activity_timeout_mobile";
    public static final String DATA_ACTIVITY_TIMEOUT_WIFI = "data_activity_timeout_wifi";
    private static final int DNS_RESOLVER_DEFAULT_MAX_SAMPLES = 64;
    private static final int DNS_RESOLVER_DEFAULT_MIN_SAMPLES = 8;
    public static final String DNS_RESOLVER_MAX_SAMPLES = "dns_resolver_max_samples";
    public static final String DNS_RESOLVER_MIN_SAMPLES = "dns_resolver_min_samples";
    public static final String DNS_RESOLVER_SAMPLE_VALIDITY_SECONDS = "dns_resolver_sample_validity_seconds";
    public static final String DNS_RESOLVER_SUCCESS_THRESHOLD_PERCENT = "dns_resolver_success_threshold_percent";
    public static final String GLOBAL_HTTP_PROXY_EXCLUSION_LIST = "global_http_proxy_exclusion_list";
    public static final String GLOBAL_HTTP_PROXY_HOST = "global_http_proxy_host";
    public static final String GLOBAL_HTTP_PROXY_PAC = "global_proxy_pac_url";
    public static final String GLOBAL_HTTP_PROXY_PORT = "global_http_proxy_port";
    public static final String INGRESS_RATE_LIMIT_BYTES_PER_SECOND = "ingress_rate_limit_bytes_per_second";
    public static final String MOBILE_DATA_ALWAYS_ON = "mobile_data_always_on";
    public static final String MOBILE_DATA_PREFERRED_UIDS = "mobile_data_preferred_uids";
    public static final String NETWORK_AVOID_BAD_WIFI = "network_avoid_bad_wifi";
    public static final int NETWORK_AVOID_BAD_WIFI_AVOID = 2;
    public static final int NETWORK_AVOID_BAD_WIFI_IGNORE = 0;
    public static final int NETWORK_AVOID_BAD_WIFI_PROMPT = 1;
    public static final String NETWORK_METERED_MULTIPATH_PREFERENCE = "network_metered_multipath_preference";
    public static final String NETWORK_SWITCH_NOTIFICATION_DAILY_LIMIT = "network_switch_notification_daily_limit";
    public static final String NETWORK_SWITCH_NOTIFICATION_RATE_LIMIT_MILLIS = "network_switch_notification_rate_limit_millis";
    public static final String PRIVATE_DNS_DEFAULT_MODE = "private_dns_default_mode";
    public static final String PRIVATE_DNS_MODE = "private_dns_mode";
    public static final int PRIVATE_DNS_MODE_OFF = 1;
    public static final int PRIVATE_DNS_MODE_OPPORTUNISTIC = 2;
    public static final int PRIVATE_DNS_MODE_PROVIDER_HOSTNAME = 3;
    public static final String PRIVATE_DNS_SPECIFIER = "private_dns_specifier";
    public static final String UIDS_ALLOWED_ON_RESTRICTED_NETWORKS = "uids_allowed_on_restricted_networks";
    public static final String WIFI_ALWAYS_REQUESTED = "wifi_always_requested";

    @Retention(RetentionPolicy.SOURCE)
    public @interface CaptivePortalMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkAvoidBadWifi {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PrivateDnsMode {
    }

    private ConnectivitySettingsManager() {
    }

    public static Duration getMobileDataActivityTimeout(Context context, Duration duration) {
        return Duration.ofSeconds((long) Settings.Global.getInt(context.getContentResolver(), DATA_ACTIVITY_TIMEOUT_MOBILE, (int) duration.getSeconds()));
    }

    public static void setMobileDataActivityTimeout(Context context, Duration duration) {
        Settings.Global.putInt(context.getContentResolver(), DATA_ACTIVITY_TIMEOUT_MOBILE, (int) duration.getSeconds());
    }

    public static Duration getWifiDataActivityTimeout(Context context, Duration duration) {
        return Duration.ofSeconds((long) Settings.Global.getInt(context.getContentResolver(), DATA_ACTIVITY_TIMEOUT_WIFI, (int) duration.getSeconds()));
    }

    public static void setWifiDataActivityTimeout(Context context, Duration duration) {
        Settings.Global.putInt(context.getContentResolver(), DATA_ACTIVITY_TIMEOUT_WIFI, (int) duration.getSeconds());
    }

    public static Duration getDnsResolverSampleValidityDuration(Context context, Duration duration) {
        return Duration.ofSeconds((long) Settings.Global.getInt(context.getContentResolver(), DNS_RESOLVER_SAMPLE_VALIDITY_SECONDS, (int) duration.getSeconds()));
    }

    public static void setDnsResolverSampleValidityDuration(Context context, Duration duration) {
        int seconds = (int) duration.getSeconds();
        if (seconds > 0) {
            Settings.Global.putInt(context.getContentResolver(), DNS_RESOLVER_SAMPLE_VALIDITY_SECONDS, seconds);
            return;
        }
        throw new IllegalArgumentException("Invalid duration");
    }

    public static int getDnsResolverSuccessThresholdPercent(Context context, int i) {
        return Settings.Global.getInt(context.getContentResolver(), DNS_RESOLVER_SUCCESS_THRESHOLD_PERCENT, i);
    }

    public static void setDnsResolverSuccessThresholdPercent(Context context, int i) {
        if (i < 0 || i > 100) {
            throw new IllegalArgumentException("Percent must be 0~100");
        }
        Settings.Global.putInt(context.getContentResolver(), DNS_RESOLVER_SUCCESS_THRESHOLD_PERCENT, i);
    }

    public static Range<Integer> getDnsResolverSampleRanges(Context context) {
        return new Range<>(Integer.valueOf(Settings.Global.getInt(context.getContentResolver(), DNS_RESOLVER_MIN_SAMPLES, 8)), Integer.valueOf(Settings.Global.getInt(context.getContentResolver(), DNS_RESOLVER_MAX_SAMPLES, 64)));
    }

    public static void setDnsResolverSampleRanges(Context context, Range<Integer> range) {
        if (range.getLower().intValue() < 0 || range.getUpper().intValue() > 64) {
            throw new IllegalArgumentException("Argument must be 0~64");
        }
        Settings.Global.putInt(context.getContentResolver(), DNS_RESOLVER_MIN_SAMPLES, range.getLower().intValue());
        Settings.Global.putInt(context.getContentResolver(), DNS_RESOLVER_MAX_SAMPLES, range.getUpper().intValue());
    }

    public static int getNetworkSwitchNotificationMaximumDailyCount(Context context, int i) {
        return Settings.Global.getInt(context.getContentResolver(), NETWORK_SWITCH_NOTIFICATION_DAILY_LIMIT, i);
    }

    public static void setNetworkSwitchNotificationMaximumDailyCount(Context context, int i) {
        if (i >= 0) {
            Settings.Global.putInt(context.getContentResolver(), NETWORK_SWITCH_NOTIFICATION_DAILY_LIMIT, i);
            return;
        }
        throw new IllegalArgumentException("Count must be more than 0.");
    }

    public static Duration getNetworkSwitchNotificationRateDuration(Context context, Duration duration) {
        return Duration.ofMillis((long) Settings.Global.getInt(context.getContentResolver(), NETWORK_SWITCH_NOTIFICATION_RATE_LIMIT_MILLIS, (int) duration.toMillis()));
    }

    public static void setNetworkSwitchNotificationRateDuration(Context context, Duration duration) {
        int millis = (int) duration.toMillis();
        if (millis >= 0) {
            Settings.Global.putInt(context.getContentResolver(), NETWORK_SWITCH_NOTIFICATION_RATE_LIMIT_MILLIS, millis);
            return;
        }
        throw new IllegalArgumentException("Invalid duration.");
    }

    public static String getCaptivePortalHttpUrl(Context context) {
        return Settings.Global.getString(context.getContentResolver(), CAPTIVE_PORTAL_HTTP_URL);
    }

    public static void setCaptivePortalHttpUrl(Context context, String str) {
        Settings.Global.putString(context.getContentResolver(), CAPTIVE_PORTAL_HTTP_URL, str);
    }

    public static int getCaptivePortalMode(Context context, int i) {
        return Settings.Global.getInt(context.getContentResolver(), CAPTIVE_PORTAL_MODE, i);
    }

    public static void setCaptivePortalMode(Context context, int i) {
        if (i == 0 || i == 1 || i == 2) {
            Settings.Global.putInt(context.getContentResolver(), CAPTIVE_PORTAL_MODE, i);
            return;
        }
        throw new IllegalArgumentException("Invalid captive portal mode");
    }

    public static ProxyInfo getGlobalProxy(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), GLOBAL_HTTP_PROXY_HOST);
        int i = Settings.Global.getInt(context.getContentResolver(), GLOBAL_HTTP_PROXY_PORT, 0);
        String string2 = Settings.Global.getString(context.getContentResolver(), GLOBAL_HTTP_PROXY_EXCLUSION_LIST);
        String string3 = Settings.Global.getString(context.getContentResolver(), GLOBAL_HTTP_PROXY_PAC);
        if (TextUtils.isEmpty(string) && TextUtils.isEmpty(string3)) {
            return null;
        }
        if (TextUtils.isEmpty(string3)) {
            return ProxyInfo.buildDirectProxy(string, i, ProxyUtils.exclusionStringAsList(string2));
        }
        return ProxyInfo.buildPacProxy(Uri.parse(string3));
    }

    public static void setGlobalProxy(Context context, ProxyInfo proxyInfo) {
        String host = proxyInfo.getHost();
        int port = proxyInfo.getPort();
        String exclusionListAsString = proxyInfo.getExclusionListAsString();
        String uri = proxyInfo.getPacFileUrl().toString();
        if (TextUtils.isEmpty(uri)) {
            Settings.Global.putString(context.getContentResolver(), GLOBAL_HTTP_PROXY_HOST, host);
            Settings.Global.putInt(context.getContentResolver(), GLOBAL_HTTP_PROXY_PORT, port);
            Settings.Global.putString(context.getContentResolver(), GLOBAL_HTTP_PROXY_EXCLUSION_LIST, exclusionListAsString);
            Settings.Global.putString(context.getContentResolver(), GLOBAL_HTTP_PROXY_PAC, "");
            return;
        }
        Settings.Global.putString(context.getContentResolver(), GLOBAL_HTTP_PROXY_PAC, uri);
        Settings.Global.putString(context.getContentResolver(), GLOBAL_HTTP_PROXY_HOST, "");
        Settings.Global.putInt(context.getContentResolver(), GLOBAL_HTTP_PROXY_PORT, 0);
        Settings.Global.putString(context.getContentResolver(), GLOBAL_HTTP_PROXY_EXCLUSION_LIST, "");
    }

    public static void clearGlobalProxy(Context context) {
        Settings.Global.putString(context.getContentResolver(), GLOBAL_HTTP_PROXY_HOST, "");
        Settings.Global.putInt(context.getContentResolver(), GLOBAL_HTTP_PROXY_PORT, 0);
        Settings.Global.putString(context.getContentResolver(), GLOBAL_HTTP_PROXY_EXCLUSION_LIST, "");
        Settings.Global.putString(context.getContentResolver(), GLOBAL_HTTP_PROXY_PAC, "");
    }

    public static int getPrivateDnsMode(Context context) {
        return ConnectivitySettingsUtils.getPrivateDnsMode(context);
    }

    public static void setPrivateDnsMode(Context context, int i) {
        ConnectivitySettingsUtils.setPrivateDnsMode(context, i);
    }

    public static String getPrivateDnsHostname(Context context) {
        return ConnectivitySettingsUtils.getPrivateDnsHostname(context);
    }

    public static void setPrivateDnsHostname(Context context, String str) {
        ConnectivitySettingsUtils.setPrivateDnsHostname(context, str);
    }

    public static String getPrivateDnsDefaultMode(Context context) {
        return Settings.Global.getString(context.getContentResolver(), "private_dns_default_mode");
    }

    public static void setPrivateDnsDefaultMode(Context context, int i) {
        if (i == 1 || i == 2 || i == 3) {
            Settings.Global.putString(context.getContentResolver(), "private_dns_default_mode", ConnectivitySettingsUtils.getPrivateDnsModeAsString(i));
            return;
        }
        throw new IllegalArgumentException("Invalid private dns mode");
    }

    public static Duration getConnectivityKeepPendingIntentDuration(Context context, Duration duration) {
        return Duration.ofMillis((long) Settings.Secure.getInt(context.getContentResolver(), CONNECTIVITY_RELEASE_PENDING_INTENT_DELAY_MS, (int) duration.toMillis()));
    }

    public static void setConnectivityKeepPendingIntentDuration(Context context, Duration duration) {
        int millis = (int) duration.toMillis();
        if (millis >= 0) {
            Settings.Secure.putInt(context.getContentResolver(), CONNECTIVITY_RELEASE_PENDING_INTENT_DELAY_MS, millis);
            return;
        }
        throw new IllegalArgumentException("Invalid duration.");
    }

    public static boolean getMobileDataAlwaysOn(Context context, boolean z) {
        return Settings.Global.getInt(context.getContentResolver(), MOBILE_DATA_ALWAYS_ON, z ? 1 : 0) != 0;
    }

    public static void setMobileDataAlwaysOn(Context context, boolean z) {
        Settings.Global.putInt(context.getContentResolver(), MOBILE_DATA_ALWAYS_ON, z ? 1 : 0);
    }

    public static boolean getWifiAlwaysRequested(Context context, boolean z) {
        return Settings.Global.getInt(context.getContentResolver(), WIFI_ALWAYS_REQUESTED, z ? 1 : 0) != 0;
    }

    public static void setWifiAlwaysRequested(Context context, boolean z) {
        Settings.Global.putInt(context.getContentResolver(), WIFI_ALWAYS_REQUESTED, z ? 1 : 0);
    }

    public static int getNetworkAvoidBadWifi(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), NETWORK_AVOID_BAD_WIFI);
        if ("0".equals(string)) {
            return 0;
        }
        return "1".equals(string) ? 2 : 1;
    }

    public static void setNetworkAvoidBadWifi(Context context, int i) {
        String str;
        if (i == 0) {
            str = "0";
        } else if (i == 2) {
            str = "1";
        } else if (i == 1) {
            str = null;
        } else {
            throw new IllegalArgumentException("Invalid avoid bad wifi setting");
        }
        Settings.Global.putString(context.getContentResolver(), NETWORK_AVOID_BAD_WIFI, str);
    }

    public static String getNetworkMeteredMultipathPreference(Context context) {
        return Settings.Global.getString(context.getContentResolver(), NETWORK_METERED_MULTIPATH_PREFERENCE);
    }

    public static void setNetworkMeteredMultipathPreference(Context context, String str) {
        if (Integer.valueOf(str).intValue() == 1 || Integer.valueOf(str).intValue() == 2 || Integer.valueOf(str).intValue() == 4) {
            Settings.Global.putString(context.getContentResolver(), NETWORK_METERED_MULTIPATH_PREFERENCE, str);
            return;
        }
        throw new IllegalArgumentException("Invalid private dns mode");
    }

    private static Set<Integer> getUidSetFromString(String str) {
        ArraySet arraySet = new ArraySet();
        if (TextUtils.isEmpty(str)) {
            return arraySet;
        }
        for (String valueOf : str.split(NavigationBarInflaterView.GRAVITY_SEPARATOR)) {
            arraySet.add(Integer.valueOf(valueOf));
        }
        return arraySet;
    }

    private static String getUidStringFromSet(Set<Integer> set) {
        StringJoiner stringJoiner = new StringJoiner(NavigationBarInflaterView.GRAVITY_SEPARATOR);
        for (Integer next : set) {
            if (next.intValue() < 0 || UserHandle.getAppId(next.intValue()) > 19999) {
                throw new IllegalArgumentException("Invalid uid");
            }
            stringJoiner.add(next.toString());
        }
        return stringJoiner.toString();
    }

    public static Set<Integer> getMobileDataPreferredUids(Context context) {
        return getUidSetFromString(Settings.Secure.getString(context.getContentResolver(), MOBILE_DATA_PREFERRED_UIDS));
    }

    public static void setMobileDataPreferredUids(Context context, Set<Integer> set) {
        Settings.Secure.putString(context.getContentResolver(), MOBILE_DATA_PREFERRED_UIDS, getUidStringFromSet(set));
    }

    public static Set<Integer> getUidsAllowedOnRestrictedNetworks(Context context) {
        return getUidSetFromString(Settings.Global.getString(context.getContentResolver(), UIDS_ALLOWED_ON_RESTRICTED_NETWORKS));
    }

    private static boolean isCallingFromSystem() {
        return Binder.getCallingUid() == 1000 && Binder.getCallingPid() == Process.myPid();
    }

    public static void setUidsAllowedOnRestrictedNetworks(Context context, Set<Integer> set) {
        if (!isCallingFromSystem()) {
            if (Build.isDebuggable()) {
                context.enforceCallingOrSelfPermission("android.permission.NETWORK_SETTINGS", "Requires NETWORK_SETTINGS permission");
            } else {
                throw new SecurityException("Only system can set this setting.");
            }
        }
        Settings.Global.putString(context.getContentResolver(), UIDS_ALLOWED_ON_RESTRICTED_NETWORKS, getUidStringFromSet(set));
    }

    public static long getIngressRateLimitInBytesPerSecond(Context context) {
        return Settings.Global.getLong(context.getContentResolver(), INGRESS_RATE_LIMIT_BYTES_PER_SECOND, -1);
    }

    public static void setIngressRateLimitInBytesPerSecond(Context context, long j) {
        if (j >= -1) {
            Settings.Global.putLong(context.getContentResolver(), INGRESS_RATE_LIMIT_BYTES_PER_SECOND, j);
            return;
        }
        throw new IllegalArgumentException("Rate limit must be within the range [-1, Integer.MAX_VALUE]");
    }
}
