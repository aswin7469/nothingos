package android.net.wifi.hotspot2.pps;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Policy implements Parcelable {
    public static final Parcelable.Creator<Policy> CREATOR = new Parcelable.Creator<Policy>() {
        public Policy createFromParcel(Parcel parcel) {
            Policy policy = new Policy();
            policy.setMinHomeDownlinkBandwidth(parcel.readLong());
            policy.setMinHomeUplinkBandwidth(parcel.readLong());
            policy.setMinRoamingDownlinkBandwidth(parcel.readLong());
            policy.setMinRoamingUplinkBandwidth(parcel.readLong());
            policy.setExcludedSsidList(parcel.createStringArray());
            policy.setRequiredProtoPortMap(readProtoPortMap(parcel));
            policy.setMaximumBssLoadValue(parcel.readInt());
            policy.setPreferredRoamingPartnerList(readRoamingPartnerList(parcel));
            policy.setPolicyUpdate((UpdateParameter) parcel.readParcelable((ClassLoader) null));
            return policy;
        }

        public Policy[] newArray(int i) {
            return new Policy[i];
        }

        private Map<Integer, String> readProtoPortMap(Parcel parcel) {
            int readInt = parcel.readInt();
            if (readInt == -1) {
                return null;
            }
            HashMap hashMap = new HashMap(readInt);
            for (int i = 0; i < readInt; i++) {
                int readInt2 = parcel.readInt();
                hashMap.put(Integer.valueOf(readInt2), parcel.readString());
            }
            return hashMap;
        }

        private List<RoamingPartner> readRoamingPartnerList(Parcel parcel) {
            int readInt = parcel.readInt();
            if (readInt == -1) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < readInt; i++) {
                arrayList.add((RoamingPartner) parcel.readParcelable((ClassLoader) null));
            }
            return arrayList;
        }
    };
    private static final int MAX_EXCLUSION_SSIDS = 128;
    private static final int MAX_PORT_STRING_BYTES = 64;
    private static final int MAX_SSID_BYTES = 32;
    private static final int NULL_VALUE = -1;
    private static final String TAG = "Policy";
    private String[] mExcludedSsidList = null;
    private int mMaximumBssLoadValue = Integer.MIN_VALUE;
    private long mMinHomeDownlinkBandwidth = Long.MIN_VALUE;
    private long mMinHomeUplinkBandwidth = Long.MIN_VALUE;
    private long mMinRoamingDownlinkBandwidth = Long.MIN_VALUE;
    private long mMinRoamingUplinkBandwidth = Long.MIN_VALUE;
    private UpdateParameter mPolicyUpdate = null;
    private List<RoamingPartner> mPreferredRoamingPartnerList = null;
    private Map<Integer, String> mRequiredProtoPortMap = null;

    public int describeContents() {
        return 0;
    }

    public void setMinHomeDownlinkBandwidth(long j) {
        this.mMinHomeDownlinkBandwidth = j;
    }

    public long getMinHomeDownlinkBandwidth() {
        return this.mMinHomeDownlinkBandwidth;
    }

    public void setMinHomeUplinkBandwidth(long j) {
        this.mMinHomeUplinkBandwidth = j;
    }

    public long getMinHomeUplinkBandwidth() {
        return this.mMinHomeUplinkBandwidth;
    }

    public void setMinRoamingDownlinkBandwidth(long j) {
        this.mMinRoamingDownlinkBandwidth = j;
    }

    public long getMinRoamingDownlinkBandwidth() {
        return this.mMinRoamingDownlinkBandwidth;
    }

    public void setMinRoamingUplinkBandwidth(long j) {
        this.mMinRoamingUplinkBandwidth = j;
    }

    public long getMinRoamingUplinkBandwidth() {
        return this.mMinRoamingUplinkBandwidth;
    }

    public void setExcludedSsidList(String[] strArr) {
        this.mExcludedSsidList = strArr;
    }

    public String[] getExcludedSsidList() {
        return this.mExcludedSsidList;
    }

    public void setRequiredProtoPortMap(Map<Integer, String> map) {
        this.mRequiredProtoPortMap = map;
    }

    public Map<Integer, String> getRequiredProtoPortMap() {
        return this.mRequiredProtoPortMap;
    }

    public void setMaximumBssLoadValue(int i) {
        this.mMaximumBssLoadValue = i;
    }

    public int getMaximumBssLoadValue() {
        return this.mMaximumBssLoadValue;
    }

    public static final class RoamingPartner implements Parcelable {
        public static final Parcelable.Creator<RoamingPartner> CREATOR = new Parcelable.Creator<RoamingPartner>() {
            public RoamingPartner createFromParcel(Parcel parcel) {
                RoamingPartner roamingPartner = new RoamingPartner();
                roamingPartner.setFqdn(parcel.readString());
                roamingPartner.setFqdnExactMatch(parcel.readInt() != 0);
                roamingPartner.setPriority(parcel.readInt());
                roamingPartner.setCountries(parcel.readString());
                return roamingPartner;
            }

            public RoamingPartner[] newArray(int i) {
                return new RoamingPartner[i];
            }
        };
        private String mCountries = null;
        private String mFqdn = null;
        private boolean mFqdnExactMatch = false;
        private int mPriority = Integer.MIN_VALUE;

        public int describeContents() {
            return 0;
        }

        public void setFqdn(String str) {
            this.mFqdn = str;
        }

        public String getFqdn() {
            return this.mFqdn;
        }

        public void setFqdnExactMatch(boolean z) {
            this.mFqdnExactMatch = z;
        }

        public boolean getFqdnExactMatch() {
            return this.mFqdnExactMatch;
        }

        public void setPriority(int i) {
            this.mPriority = i;
        }

        public int getPriority() {
            return this.mPriority;
        }

        public void setCountries(String str) {
            this.mCountries = str;
        }

        public String getCountries() {
            return this.mCountries;
        }

        public RoamingPartner() {
        }

        public RoamingPartner(RoamingPartner roamingPartner) {
            if (roamingPartner != null) {
                this.mFqdn = roamingPartner.mFqdn;
                this.mFqdnExactMatch = roamingPartner.mFqdnExactMatch;
                this.mPriority = roamingPartner.mPriority;
                this.mCountries = roamingPartner.mCountries;
            }
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mFqdn);
            parcel.writeInt(this.mFqdnExactMatch ? 1 : 0);
            parcel.writeInt(this.mPriority);
            parcel.writeString(this.mCountries);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof RoamingPartner)) {
                return false;
            }
            RoamingPartner roamingPartner = (RoamingPartner) obj;
            if (!TextUtils.equals(this.mFqdn, roamingPartner.mFqdn) || this.mFqdnExactMatch != roamingPartner.mFqdnExactMatch || this.mPriority != roamingPartner.mPriority || !TextUtils.equals(this.mCountries, roamingPartner.mCountries)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.mFqdn, Boolean.valueOf(this.mFqdnExactMatch), Integer.valueOf(this.mPriority), this.mCountries);
        }

        public String toString() {
            return "FQDN: " + this.mFqdn + "\nExactMatch: mFqdnExactMatch\nPriority: " + this.mPriority + "\nCountries: " + this.mCountries + "\n";
        }

        public boolean validate() {
            if (TextUtils.isEmpty(this.mFqdn)) {
                Log.d(Policy.TAG, "Missing FQDN");
                return false;
            } else if (!TextUtils.isEmpty(this.mCountries)) {
                return true;
            } else {
                Log.d(Policy.TAG, "Missing countries");
                return false;
            }
        }
    }

    public void setPreferredRoamingPartnerList(List<RoamingPartner> list) {
        this.mPreferredRoamingPartnerList = list;
    }

    public List<RoamingPartner> getPreferredRoamingPartnerList() {
        return this.mPreferredRoamingPartnerList;
    }

    public void setPolicyUpdate(UpdateParameter updateParameter) {
        this.mPolicyUpdate = updateParameter;
    }

    public UpdateParameter getPolicyUpdate() {
        return this.mPolicyUpdate;
    }

    public Policy() {
    }

    public Policy(Policy policy) {
        if (policy != null) {
            this.mMinHomeDownlinkBandwidth = policy.mMinHomeDownlinkBandwidth;
            this.mMinHomeUplinkBandwidth = policy.mMinHomeUplinkBandwidth;
            this.mMinRoamingDownlinkBandwidth = policy.mMinRoamingDownlinkBandwidth;
            this.mMinRoamingUplinkBandwidth = policy.mMinRoamingUplinkBandwidth;
            this.mMaximumBssLoadValue = policy.mMaximumBssLoadValue;
            String[] strArr = policy.mExcludedSsidList;
            if (strArr != null) {
                this.mExcludedSsidList = (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
            }
            Map<Integer, String> map = policy.mRequiredProtoPortMap;
            if (map != null) {
                this.mRequiredProtoPortMap = Collections.unmodifiableMap(map);
            }
            List<RoamingPartner> list = policy.mPreferredRoamingPartnerList;
            if (list != null) {
                this.mPreferredRoamingPartnerList = Collections.unmodifiableList(list);
            }
            if (policy.mPolicyUpdate != null) {
                this.mPolicyUpdate = new UpdateParameter(policy.mPolicyUpdate);
            }
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mMinHomeDownlinkBandwidth);
        parcel.writeLong(this.mMinHomeUplinkBandwidth);
        parcel.writeLong(this.mMinRoamingDownlinkBandwidth);
        parcel.writeLong(this.mMinRoamingUplinkBandwidth);
        parcel.writeStringArray(this.mExcludedSsidList);
        writeProtoPortMap(parcel, this.mRequiredProtoPortMap);
        parcel.writeInt(this.mMaximumBssLoadValue);
        writeRoamingPartnerList(parcel, i, this.mPreferredRoamingPartnerList);
        parcel.writeParcelable(this.mPolicyUpdate, i);
    }

    public boolean equals(Object obj) {
        Map<Integer, String> map;
        List<RoamingPartner> list;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Policy)) {
            return false;
        }
        Policy policy = (Policy) obj;
        if (this.mMinHomeDownlinkBandwidth == policy.mMinHomeDownlinkBandwidth && this.mMinHomeUplinkBandwidth == policy.mMinHomeUplinkBandwidth && this.mMinRoamingDownlinkBandwidth == policy.mMinRoamingDownlinkBandwidth && this.mMinRoamingUplinkBandwidth == policy.mMinRoamingUplinkBandwidth && Arrays.equals((Object[]) this.mExcludedSsidList, (Object[]) policy.mExcludedSsidList) && ((map = this.mRequiredProtoPortMap) != null ? map.equals(policy.mRequiredProtoPortMap) : policy.mRequiredProtoPortMap == null) && this.mMaximumBssLoadValue == policy.mMaximumBssLoadValue && ((list = this.mPreferredRoamingPartnerList) != null ? list.equals(policy.mPreferredRoamingPartnerList) : policy.mPreferredRoamingPartnerList == null)) {
            UpdateParameter updateParameter = this.mPolicyUpdate;
            if (updateParameter == null) {
                if (policy.mPolicyUpdate == null) {
                    return true;
                }
            } else if (updateParameter.equals(policy.mPolicyUpdate)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.mMinHomeDownlinkBandwidth), Long.valueOf(this.mMinHomeUplinkBandwidth), Long.valueOf(this.mMinRoamingDownlinkBandwidth), Long.valueOf(this.mMinRoamingUplinkBandwidth), this.mExcludedSsidList, this.mRequiredProtoPortMap, Integer.valueOf(this.mMaximumBssLoadValue), this.mPreferredRoamingPartnerList, this.mPolicyUpdate);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("MinHomeDownlinkBandwidth: ");
        sb.append(this.mMinHomeDownlinkBandwidth);
        sb.append("\nMinHomeUplinkBandwidth: ");
        sb.append(this.mMinHomeUplinkBandwidth);
        sb.append("\nMinRoamingDownlinkBandwidth: ");
        sb.append(this.mMinRoamingDownlinkBandwidth);
        sb.append("\nMinRoamingUplinkBandwidth: ");
        sb.append(this.mMinRoamingUplinkBandwidth);
        sb.append("\nExcludedSSIDList: ");
        sb.append((Object) this.mExcludedSsidList);
        sb.append("\nRequiredProtoPortMap: ");
        sb.append((Object) this.mRequiredProtoPortMap);
        sb.append("\nMaximumBSSLoadValue: ");
        sb.append(this.mMaximumBssLoadValue);
        sb.append("\nPreferredRoamingPartnerList: ");
        sb.append((Object) this.mPreferredRoamingPartnerList);
        sb.append("\n");
        if (this.mPolicyUpdate != null) {
            sb.append("PolicyUpdate Begin ---\n");
            sb.append((Object) this.mPolicyUpdate);
            sb.append("PolicyUpdate End ---\n");
        }
        return sb.toString();
    }

    public boolean validate() {
        UpdateParameter updateParameter = this.mPolicyUpdate;
        if (updateParameter == null) {
            Log.d(TAG, "PolicyUpdate not specified");
            return false;
        } else if (!updateParameter.validate()) {
            return false;
        } else {
            String[] strArr = this.mExcludedSsidList;
            if (strArr != null) {
                if (strArr.length > 128) {
                    Log.d(TAG, "SSID exclusion list size exceeded the max: " + this.mExcludedSsidList.length);
                    return false;
                }
                for (String str : strArr) {
                    if (str.getBytes(StandardCharsets.UTF_8).length > 32) {
                        Log.d(TAG, "Invalid SSID: " + str);
                        return false;
                    }
                }
            }
            Map<Integer, String> map = this.mRequiredProtoPortMap;
            if (map != null) {
                for (Map.Entry<Integer, String> value : map.entrySet()) {
                    String str2 = (String) value.getValue();
                    if (str2.getBytes(StandardCharsets.UTF_8).length > 64) {
                        Log.d(TAG, "PortNumber string bytes exceeded the max: " + str2);
                        return false;
                    }
                }
            }
            List<RoamingPartner> list = this.mPreferredRoamingPartnerList;
            if (list == null) {
                return true;
            }
            for (RoamingPartner validate : list) {
                if (!validate.validate()) {
                    return false;
                }
            }
            return true;
        }
    }

    private static void writeProtoPortMap(Parcel parcel, Map<Integer, String> map) {
        if (map == null) {
            parcel.writeInt(-1);
            return;
        }
        parcel.writeInt(map.size());
        for (Map.Entry next : map.entrySet()) {
            parcel.writeInt(((Integer) next.getKey()).intValue());
            parcel.writeString((String) next.getValue());
        }
    }

    private static void writeRoamingPartnerList(Parcel parcel, int i, List<RoamingPartner> list) {
        if (list == null) {
            parcel.writeInt(-1);
            return;
        }
        parcel.writeInt(list.size());
        for (RoamingPartner writeParcelable : list) {
            parcel.writeParcelable(writeParcelable, i);
        }
    }
}
