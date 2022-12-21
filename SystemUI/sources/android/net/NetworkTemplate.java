package android.net;

import android.annotation.SystemApi;
import android.app.usage.NetworkStatsManager;
import android.net.connectivity.com.android.net.module.util.CollectionUtils;
import android.net.connectivity.com.android.net.module.util.NetworkIdentityUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.ArraySet;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class NetworkTemplate implements Parcelable {
    public static final Parcelable.Creator<NetworkTemplate> CREATOR = new Parcelable.Creator<NetworkTemplate>() {
        public NetworkTemplate createFromParcel(Parcel parcel) {
            return new NetworkTemplate(parcel);
        }

        public NetworkTemplate[] newArray(int i) {
            return new NetworkTemplate[i];
        }
    };
    public static final int MATCH_BLUETOOTH = 8;
    public static final int MATCH_CARRIER = 10;
    public static final int MATCH_ETHERNET = 5;
    public static final int MATCH_MOBILE = 1;
    public static final int MATCH_MOBILE_WILDCARD = 6;
    public static final int MATCH_PROXY = 9;
    public static final int MATCH_WIFI = 4;
    public static final int MATCH_WIFI_WILDCARD = 7;
    public static final int NETWORK_TYPE_ALL = -1;
    public static final int OEM_MANAGED_ALL = -1;
    public static final int OEM_MANAGED_NO = 0;
    public static final int OEM_MANAGED_PAID = 1;
    public static final int OEM_MANAGED_PRIVATE = 2;
    public static final int OEM_MANAGED_YES = -2;
    public static final String WIFI_NETWORKID_ALL = null;
    public static final String WIFI_NETWORK_KEY_ALL = null;
    private final int mDefaultNetwork;
    private final int mMatchRule;
    private final String[] mMatchSubscriberIds;
    private final String[] mMatchWifiNetworkKeys;
    private final int mMetered;
    private final int mOemManaged;
    private final int mRatType;
    private final int mRoaming;
    private final String mSubscriberId;
    private final int mSubscriberIdMatchRule;

    @Retention(RetentionPolicy.SOURCE)
    public @interface OemManaged {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TemplateMatchRule {
    }

    /* access modifiers changed from: private */
    public static boolean isKnownMatchRule(int i) {
        if (i != 1) {
            switch (i) {
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public static NetworkTemplate buildTemplateMobileAll(String str) {
        return new NetworkTemplate(1, str, (String) null);
    }

    public static NetworkTemplate buildTemplateMobileWithRatType(String str, int i, int i2) {
        if (TextUtils.isEmpty(str)) {
            return new NetworkTemplate(6, (String) null, (String[]) null, new String[0], i2, -1, -1, i, -1, 0);
        }
        return new NetworkTemplate(1, str, new String[]{str}, new String[0], i2, -1, -1, i, -1, 0);
    }

    public static NetworkTemplate buildTemplateMobileWildcard() {
        return new NetworkTemplate(6, (String) null, (String) null);
    }

    public static NetworkTemplate buildTemplateWifiWildcard() {
        return new NetworkTemplate(7, (String) null, (String) null);
    }

    @Deprecated
    public static NetworkTemplate buildTemplateWifi() {
        return buildTemplateWifiWildcard();
    }

    public static NetworkTemplate buildTemplateWifi(String str) {
        Objects.requireNonNull(str);
        return new NetworkTemplate(4, (String) null, new String[]{null}, new String[]{str}, -1, -1, -1, -1, -1, 1);
    }

    public static NetworkTemplate buildTemplateWifi(String str, String str2) {
        String[] strArr;
        String[] strArr2 = {str2};
        if (str != null) {
            strArr = new String[]{str};
        } else {
            strArr = new String[0];
        }
        return new NetworkTemplate(4, str2, strArr2, strArr, -1, -1, -1, -1, -1, 0);
    }

    public static NetworkTemplate buildTemplateEthernet() {
        return new NetworkTemplate(5, (String) null, (String) null);
    }

    public static NetworkTemplate buildTemplateBluetooth() {
        return new NetworkTemplate(8, (String) null, (String) null);
    }

    public static NetworkTemplate buildTemplateProxy() {
        return new NetworkTemplate(9, (String) null, (String) null);
    }

    public static NetworkTemplate buildTemplateCarrierMetered(String str) {
        Objects.requireNonNull(str);
        return new NetworkTemplate(10, str, new String[]{str}, new String[0], 1, -1, -1, -1, -1, 0);
    }

    private static void checkValidSubscriberIdMatchRule(int i, int i2) {
        if ((i == 1 || i == 10) && i2 == 1) {
            throw new IllegalArgumentException("Invalid SubscriberIdMatchRule on match rule: " + getMatchRuleName(i));
        }
    }

    public NetworkTemplate(int i, String str, String str2) {
        this(i, str, new String[]{str}, str2);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public NetworkTemplate(int i, String str, String[] strArr, String str2) {
        this(i, str, strArr, str2 != null ? new String[]{str2} : new String[0], (i == 1 || i == 6 || i == 10) ? 1 : -1, -1, -1, -1, -1, 0);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public NetworkTemplate(int i, String str, String[] strArr, String str2, int i2, int i3, int i4, int i5, int i6) {
        this(i, str, strArr, str2 != null ? new String[]{str2} : new String[0], i2, i3, i4, i5, i6, 0);
    }

    public NetworkTemplate(int i, String str, String[] strArr, String[] strArr2, int i2, int i3, int i4, int i5, int i6, int i7) {
        Objects.requireNonNull(strArr2);
        this.mMatchRule = i;
        this.mSubscriberId = str;
        this.mMatchSubscriberIds = strArr;
        this.mMatchWifiNetworkKeys = strArr2;
        this.mMetered = i2;
        this.mRoaming = i3;
        this.mDefaultNetwork = i4;
        this.mRatType = i5;
        this.mOemManaged = i6;
        this.mSubscriberIdMatchRule = i7;
        checkValidSubscriberIdMatchRule(i, i7);
        if (!isKnownMatchRule(i)) {
            throw new IllegalArgumentException("Unknown network template rule " + i + " will not match any identity.");
        }
    }

    private NetworkTemplate(Parcel parcel) {
        this.mMatchRule = parcel.readInt();
        this.mSubscriberId = parcel.readString();
        this.mMatchSubscriberIds = parcel.createStringArray();
        this.mMatchWifiNetworkKeys = parcel.createStringArray();
        this.mMetered = parcel.readInt();
        this.mRoaming = parcel.readInt();
        this.mDefaultNetwork = parcel.readInt();
        this.mRatType = parcel.readInt();
        this.mOemManaged = parcel.readInt();
        this.mSubscriberIdMatchRule = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mMatchRule);
        parcel.writeString(this.mSubscriberId);
        parcel.writeStringArray(this.mMatchSubscriberIds);
        parcel.writeStringArray(this.mMatchWifiNetworkKeys);
        parcel.writeInt(this.mMetered);
        parcel.writeInt(this.mRoaming);
        parcel.writeInt(this.mDefaultNetwork);
        parcel.writeInt(this.mRatType);
        parcel.writeInt(this.mOemManaged);
        parcel.writeInt(this.mSubscriberIdMatchRule);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("NetworkTemplate: matchRule=");
        sb.append(getMatchRuleName(this.mMatchRule));
        if (this.mSubscriberId != null) {
            sb.append(", subscriberId=");
            sb.append(NetworkIdentityUtils.scrubSubscriberId(this.mSubscriberId));
        }
        if (this.mMatchSubscriberIds != null) {
            sb.append(", matchSubscriberIds=");
            sb.append(Arrays.toString((Object[]) NetworkIdentityUtils.scrubSubscriberIds(this.mMatchSubscriberIds)));
        }
        sb.append(", matchWifiNetworkKeys=");
        sb.append(Arrays.toString((Object[]) this.mMatchWifiNetworkKeys));
        if (this.mMetered != -1) {
            sb.append(", metered=");
            sb.append(NetworkStats.meteredToString(this.mMetered));
        }
        if (this.mRoaming != -1) {
            sb.append(", roaming=");
            sb.append(NetworkStats.roamingToString(this.mRoaming));
        }
        if (this.mDefaultNetwork != -1) {
            sb.append(", defaultNetwork=");
            sb.append(NetworkStats.defaultNetworkToString(this.mDefaultNetwork));
        }
        if (this.mRatType != -1) {
            sb.append(", ratType=");
            sb.append(this.mRatType);
        }
        if (this.mOemManaged != -1) {
            sb.append(", oemManaged=");
            sb.append(getOemManagedNames(this.mOemManaged));
        }
        sb.append(", subscriberIdMatchRule=");
        sb.append(subscriberIdMatchRuleToString(this.mSubscriberIdMatchRule));
        return sb.toString();
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mMatchRule), this.mSubscriberId, Integer.valueOf(Arrays.hashCode((Object[]) this.mMatchWifiNetworkKeys)), Integer.valueOf(this.mMetered), Integer.valueOf(this.mRoaming), Integer.valueOf(this.mDefaultNetwork), Integer.valueOf(this.mRatType), Integer.valueOf(this.mOemManaged), Integer.valueOf(this.mSubscriberIdMatchRule));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NetworkTemplate)) {
            return false;
        }
        NetworkTemplate networkTemplate = (NetworkTemplate) obj;
        if (this.mMatchRule == networkTemplate.mMatchRule && Objects.equals(this.mSubscriberId, networkTemplate.mSubscriberId) && this.mMetered == networkTemplate.mMetered && this.mRoaming == networkTemplate.mRoaming && this.mDefaultNetwork == networkTemplate.mDefaultNetwork && this.mRatType == networkTemplate.mRatType && this.mOemManaged == networkTemplate.mOemManaged && this.mSubscriberIdMatchRule == networkTemplate.mSubscriberIdMatchRule && Arrays.equals((Object[]) this.mMatchWifiNetworkKeys, (Object[]) networkTemplate.mMatchWifiNetworkKeys)) {
            return true;
        }
        return false;
    }

    private static String subscriberIdMatchRuleToString(int i) {
        if (i == 0) {
            return "EXACT_MATCH";
        }
        if (i == 1) {
            return "ALL";
        }
        return "Unknown rule " + i;
    }

    public boolean isMatchRuleMobile() {
        int i = this.mMatchRule;
        return i == 1 || i == 6;
    }

    public int getMatchRule() {
        int i = this.mMatchRule;
        if (i == 6) {
            return 1;
        }
        if (i != 7) {
            return i;
        }
        return 4;
    }

    public String getSubscriberId() {
        return this.mSubscriberId;
    }

    public Set<String> getSubscriberIds() {
        return new ArraySet(Arrays.asList(this.mMatchSubscriberIds));
    }

    public Set<String> getWifiNetworkKeys() {
        return new ArraySet(Arrays.asList(this.mMatchWifiNetworkKeys));
    }

    public String getNetworkId() {
        if (getWifiNetworkKeys().isEmpty()) {
            return null;
        }
        return getWifiNetworkKeys().iterator().next();
    }

    public int getMeteredness() {
        return this.mMetered;
    }

    public int getRoaming() {
        return this.mRoaming;
    }

    public int getDefaultNetworkStatus() {
        return this.mDefaultNetwork;
    }

    public int getRatType() {
        return this.mRatType;
    }

    public int getOemManaged() {
        return this.mOemManaged;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public boolean matches(NetworkIdentity networkIdentity) {
        Objects.requireNonNull(networkIdentity);
        if (!matchesMetered(networkIdentity) || !matchesRoaming(networkIdentity) || !matchesDefaultNetwork(networkIdentity) || !matchesOemNetwork(networkIdentity)) {
            return false;
        }
        int i = this.mMatchRule;
        if (i == 1) {
            return matchesMobile(networkIdentity);
        }
        switch (i) {
            case 4:
                return matchesWifi(networkIdentity);
            case 5:
                return matchesEthernet(networkIdentity);
            case 6:
                return matchesMobileWildcard(networkIdentity);
            case 7:
                return matchesWifiWildcard(networkIdentity);
            case 8:
                return matchesBluetooth(networkIdentity);
            case 9:
                return matchesProxy(networkIdentity);
            case 10:
                return matchesCarrier(networkIdentity);
            default:
                return false;
        }
    }

    private boolean matchesMetered(NetworkIdentity networkIdentity) {
        int i = this.mMetered;
        if (i == -1) {
            return true;
        }
        if (i != 1 || !networkIdentity.mMetered) {
            return this.mMetered == 0 && !networkIdentity.mMetered;
        }
        return true;
    }

    private boolean matchesRoaming(NetworkIdentity networkIdentity) {
        int i = this.mRoaming;
        if (i == -1) {
            return true;
        }
        if (i != 1 || !networkIdentity.mRoaming) {
            return this.mRoaming == 0 && !networkIdentity.mRoaming;
        }
        return true;
    }

    private boolean matchesDefaultNetwork(NetworkIdentity networkIdentity) {
        int i = this.mDefaultNetwork;
        if (i == -1) {
            return true;
        }
        if (i != 1 || !networkIdentity.mDefaultNetwork) {
            return this.mDefaultNetwork == 0 && !networkIdentity.mDefaultNetwork;
        }
        return true;
    }

    private boolean matchesOemNetwork(NetworkIdentity networkIdentity) {
        int i = this.mOemManaged;
        return i == -1 || (i == -2 && networkIdentity.mOemManaged != 0) || this.mOemManaged == networkIdentity.mOemManaged;
    }

    private boolean matchesCollapsedRatType(NetworkIdentity networkIdentity) {
        int i = this.mRatType;
        return i == -1 || NetworkStatsManager.getCollapsedRatType(i) == NetworkStatsManager.getCollapsedRatType(networkIdentity.mRatType);
    }

    public boolean matchesSubscriberId(String str) {
        if (this.mSubscriberIdMatchRule == 1 || CollectionUtils.contains((T[]) this.mMatchSubscriberIds, str)) {
            return true;
        }
        return false;
    }

    private boolean matchesWifiNetworkKey(String str) {
        Objects.requireNonNull(str);
        return CollectionUtils.isEmpty((T[]) this.mMatchWifiNetworkKeys) || CollectionUtils.contains((T[]) this.mMatchWifiNetworkKeys, str);
    }

    private boolean matchesMobile(NetworkIdentity networkIdentity) {
        if (networkIdentity.mType == 6) {
            return true;
        }
        if (networkIdentity.mType != 0 || CollectionUtils.isEmpty((T[]) this.mMatchSubscriberIds) || !CollectionUtils.contains((T[]) this.mMatchSubscriberIds, networkIdentity.mSubscriberId) || !matchesCollapsedRatType(networkIdentity)) {
            return false;
        }
        return true;
    }

    private boolean matchesWifi(NetworkIdentity networkIdentity) {
        if (networkIdentity.mType == 1 && matchesSubscriberId(networkIdentity.mSubscriberId) && matchesWifiNetworkKey(networkIdentity.mWifiNetworkKey)) {
            return true;
        }
        return false;
    }

    private boolean matchesEthernet(NetworkIdentity networkIdentity) {
        return networkIdentity.mType == 9;
    }

    private boolean matchesCarrier(NetworkIdentity networkIdentity) {
        return networkIdentity.mSubscriberId != null && !CollectionUtils.isEmpty((T[]) this.mMatchSubscriberIds) && CollectionUtils.contains((T[]) this.mMatchSubscriberIds, networkIdentity.mSubscriberId);
    }

    private boolean matchesMobileWildcard(NetworkIdentity networkIdentity) {
        if (networkIdentity.mType == 6) {
            return true;
        }
        if (networkIdentity.mType != 0 || !matchesCollapsedRatType(networkIdentity)) {
            return false;
        }
        return true;
    }

    private boolean matchesWifiWildcard(NetworkIdentity networkIdentity) {
        int i = networkIdentity.mType;
        return i == 1 || i == 13;
    }

    private boolean matchesBluetooth(NetworkIdentity networkIdentity) {
        return networkIdentity.mType == 7;
    }

    private boolean matchesProxy(NetworkIdentity networkIdentity) {
        return networkIdentity.mType == 16;
    }

    /* access modifiers changed from: private */
    public static String getMatchRuleName(int i) {
        if (i == 1) {
            return "MOBILE";
        }
        switch (i) {
            case 4:
                return "WIFI";
            case 5:
                return "ETHERNET";
            case 6:
                return "MOBILE_WILDCARD";
            case 7:
                return "WIFI_WILDCARD";
            case 8:
                return "BLUETOOTH";
            case 9:
                return "PROXY";
            case 10:
                return "CARRIER";
            default:
                return "UNKNOWN(" + i + NavigationBarInflaterView.KEY_CODE_END;
        }
    }

    private static String getOemManagedNames(int i) {
        if (i == -2) {
            return "OEM_MANAGED_YES";
        }
        if (i != -1) {
            return i != 0 ? NetworkIdentity.getOemManagedNames(i) : "OEM_MANAGED_NO";
        }
        return "OEM_MANAGED_ALL";
    }

    public static NetworkTemplate normalize(NetworkTemplate networkTemplate, String[] strArr) {
        return normalize(networkTemplate, (List<String[]>) Arrays.asList(strArr));
    }

    public static NetworkTemplate normalize(NetworkTemplate networkTemplate, List<String[]> list) {
        String str;
        if (networkTemplate.mSubscriberId == null) {
            return networkTemplate;
        }
        for (String[] next : list) {
            if (CollectionUtils.contains((T[]) next, networkTemplate.mSubscriberId)) {
                String[] strArr = networkTemplate.mMatchWifiNetworkKeys;
                int i = networkTemplate.mMatchRule;
                String str2 = next[0];
                if (CollectionUtils.isEmpty((T[]) strArr)) {
                    str = null;
                } else {
                    str = strArr[0];
                }
                return new NetworkTemplate(i, str2, next, str);
            }
        }
        return networkTemplate;
    }

    public static final class Builder {
        private int mDefaultNetwork;
        private final int mMatchRule;
        private final SortedSet<String> mMatchSubscriberIds = new TreeSet(Comparator.nullsFirst(Comparator.naturalOrder()));
        private final SortedSet<String> mMatchWifiNetworkKeys = new TreeSet();
        private int mMetered;
        private int mOemManaged;
        private int mRatType;
        private int mRoaming;

        public Builder(int i) {
            assertRequestableMatchRule(i);
            this.mMatchRule = i;
            this.mMetered = -1;
            this.mRoaming = -1;
            this.mDefaultNetwork = -1;
            this.mRatType = -1;
            this.mOemManaged = -1;
        }

        public Builder setSubscriberIds(Set<String> set) {
            Objects.requireNonNull(set);
            this.mMatchSubscriberIds.clear();
            this.mMatchSubscriberIds.addAll(set);
            return this;
        }

        public Builder setWifiNetworkKeys(Set<String> set) {
            Objects.requireNonNull(set);
            for (String str : set) {
                if (str == null) {
                    throw new IllegalArgumentException("Null is not a valid key");
                }
            }
            this.mMatchWifiNetworkKeys.clear();
            this.mMatchWifiNetworkKeys.addAll(set);
            return this;
        }

        public Builder setMeteredness(int i) {
            this.mMetered = i;
            return this;
        }

        public Builder setRoaming(int i) {
            this.mRoaming = i;
            return this;
        }

        public Builder setDefaultNetworkStatus(int i) {
            this.mDefaultNetwork = i;
            return this;
        }

        public Builder setRatType(int i) {
            this.mRatType = i;
            return this;
        }

        public Builder setOemManaged(int i) {
            this.mOemManaged = i;
            return this;
        }

        private static void assertRequestableMatchRule(int i) {
            if (!NetworkTemplate.isKnownMatchRule(i) || i == 9 || i == 6 || i == 7) {
                throw new IllegalArgumentException("Invalid match rule: " + NetworkTemplate.getMatchRuleName(i));
            }
        }

        private void assertRequestableParameters() {
            validateWifiNetworkKeys();
        }

        private void validateWifiNetworkKeys() {
            if (this.mMatchRule != 4 && !this.mMatchWifiNetworkKeys.isEmpty()) {
                throw new IllegalArgumentException("Trying to build non wifi match rule: " + this.mMatchRule + " with wifi network keys");
            }
        }

        private int getWildcardDeducedMatchRule() {
            if (this.mMatchRule == 1 && this.mMatchSubscriberIds.isEmpty()) {
                return 6;
            }
            if (this.mMatchRule != 4 || !this.mMatchSubscriberIds.isEmpty() || !this.mMatchWifiNetworkKeys.isEmpty()) {
                return this.mMatchRule;
            }
            return 7;
        }

        public NetworkTemplate build() {
            assertRequestableParameters();
            return new NetworkTemplate(getWildcardDeducedMatchRule(), this.mMatchSubscriberIds.isEmpty() ? null : this.mMatchSubscriberIds.iterator().next(), (String[]) this.mMatchSubscriberIds.toArray(new String[0]), (String[]) this.mMatchWifiNetworkKeys.toArray(new String[0]), this.mMetered, this.mRoaming, this.mDefaultNetwork, this.mRatType, this.mOemManaged, this.mMatchSubscriberIds.isEmpty() ? 1 : 0);
        }
    }
}
