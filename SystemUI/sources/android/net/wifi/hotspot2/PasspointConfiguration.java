package android.net.wifi.hotspot2;

import android.annotation.SystemApi;
import android.net.wifi.hotspot2.pps.Credential;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.net.wifi.hotspot2.pps.Policy;
import android.net.wifi.hotspot2.pps.UpdateParameter;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class PasspointConfiguration implements Parcelable {
    private static final int CERTIFICATE_SHA256_BYTES = 32;
    public static final Parcelable.Creator<PasspointConfiguration> CREATOR = new Parcelable.Creator<PasspointConfiguration>() {
        public PasspointConfiguration createFromParcel(Parcel parcel) {
            PasspointConfiguration passpointConfiguration = new PasspointConfiguration();
            passpointConfiguration.setHomeSp((HomeSp) parcel.readParcelable((ClassLoader) null));
            passpointConfiguration.setCredential((Credential) parcel.readParcelable((ClassLoader) null));
            passpointConfiguration.setPolicy((Policy) parcel.readParcelable((ClassLoader) null));
            passpointConfiguration.setSubscriptionUpdate((UpdateParameter) parcel.readParcelable((ClassLoader) null));
            passpointConfiguration.setTrustRootCertList(readTrustRootCerts(parcel));
            passpointConfiguration.setUpdateIdentifier(parcel.readInt());
            passpointConfiguration.setCredentialPriority(parcel.readInt());
            passpointConfiguration.setSubscriptionCreationTimeInMillis(parcel.readLong());
            passpointConfiguration.setSubscriptionExpirationTimeInMillis(parcel.readLong());
            passpointConfiguration.setSubscriptionType(parcel.readString());
            passpointConfiguration.setUsageLimitUsageTimePeriodInMinutes(parcel.readLong());
            passpointConfiguration.setUsageLimitStartTimeInMillis(parcel.readLong());
            passpointConfiguration.setUsageLimitDataLimit(parcel.readLong());
            passpointConfiguration.setUsageLimitTimeLimitInMinutes(parcel.readLong());
            passpointConfiguration.setAaaServerTrustedNames(parcel.createStringArray());
            passpointConfiguration.setServiceFriendlyNames((HashMap) parcel.readBundle().getSerializable("serviceFriendlyNames"));
            passpointConfiguration.mCarrierId = parcel.readInt();
            passpointConfiguration.mIsAutojoinEnabled = parcel.readBoolean();
            passpointConfiguration.mIsMacRandomizationEnabled = parcel.readBoolean();
            passpointConfiguration.mIsNonPersistentMacRandomizationEnabled = parcel.readBoolean();
            passpointConfiguration.mMeteredOverride = parcel.readInt();
            passpointConfiguration.mSubscriptionId = parcel.readInt();
            passpointConfiguration.mIsCarrierMerged = parcel.readBoolean();
            passpointConfiguration.mIsOemPaid = parcel.readBoolean();
            passpointConfiguration.mIsOemPrivate = parcel.readBoolean();
            passpointConfiguration.mDecoratedIdentityPrefix = parcel.readString();
            passpointConfiguration.mSubscriptionGroup = (ParcelUuid) parcel.readParcelable((ClassLoader) null);
            return passpointConfiguration;
        }

        public PasspointConfiguration[] newArray(int i) {
            return new PasspointConfiguration[i];
        }

        private Map<String, byte[]> readTrustRootCerts(Parcel parcel) {
            int readInt = parcel.readInt();
            if (readInt == -1) {
                return null;
            }
            HashMap hashMap = new HashMap(readInt);
            for (int i = 0; i < readInt; i++) {
                hashMap.put(parcel.readString(), parcel.createByteArray());
            }
            return hashMap;
        }
    };
    private static final int MAX_URL_BYTES = 1023;
    private static final int NULL_VALUE = -1;
    private static final String TAG = "PasspointConfiguration";
    private String[] mAaaServerTrustedNames = null;
    /* access modifiers changed from: private */
    public int mCarrierId = -1;
    private Credential mCredential = null;
    private int mCredentialPriority = Integer.MIN_VALUE;
    /* access modifiers changed from: private */
    public String mDecoratedIdentityPrefix;
    private HomeSp mHomeSp = null;
    /* access modifiers changed from: private */
    public boolean mIsAutojoinEnabled = true;
    /* access modifiers changed from: private */
    public boolean mIsCarrierMerged;
    /* access modifiers changed from: private */
    public boolean mIsMacRandomizationEnabled = true;
    /* access modifiers changed from: private */
    public boolean mIsNonPersistentMacRandomizationEnabled = false;
    /* access modifiers changed from: private */
    public boolean mIsOemPaid;
    /* access modifiers changed from: private */
    public boolean mIsOemPrivate;
    /* access modifiers changed from: private */
    public int mMeteredOverride = 0;
    private Policy mPolicy = null;
    private Map<String, String> mServiceFriendlyNames = null;
    private long mSubscriptionCreationTimeInMillis = Long.MIN_VALUE;
    private long mSubscriptionExpirationTimeMillis = Long.MIN_VALUE;
    /* access modifiers changed from: private */
    public ParcelUuid mSubscriptionGroup = null;
    /* access modifiers changed from: private */
    public int mSubscriptionId = -1;
    private String mSubscriptionType = null;
    private UpdateParameter mSubscriptionUpdate = null;
    private Map<String, byte[]> mTrustRootCertList = null;
    private int mUpdateIdentifier = Integer.MIN_VALUE;
    private long mUsageLimitDataLimit = Long.MIN_VALUE;
    private long mUsageLimitStartTimeInMillis = Long.MIN_VALUE;
    private long mUsageLimitTimeLimitInMinutes = Long.MIN_VALUE;
    private long mUsageLimitUsageTimePeriodInMinutes = Long.MIN_VALUE;

    public int describeContents() {
        return 0;
    }

    public void setHomeSp(HomeSp homeSp) {
        this.mHomeSp = homeSp;
    }

    public HomeSp getHomeSp() {
        return this.mHomeSp;
    }

    public void setAaaServerTrustedNames(String[] strArr) {
        this.mAaaServerTrustedNames = strArr;
    }

    public String[] getAaaServerTrustedNames() {
        return this.mAaaServerTrustedNames;
    }

    public void setCredential(Credential credential) {
        this.mCredential = credential;
    }

    public Credential getCredential() {
        return this.mCredential;
    }

    public void setPolicy(Policy policy) {
        this.mPolicy = policy;
    }

    public Policy getPolicy() {
        return this.mPolicy;
    }

    public void setSubscriptionUpdate(UpdateParameter updateParameter) {
        this.mSubscriptionUpdate = updateParameter;
    }

    public UpdateParameter getSubscriptionUpdate() {
        return this.mSubscriptionUpdate;
    }

    public void setTrustRootCertList(Map<String, byte[]> map) {
        this.mTrustRootCertList = map;
    }

    public Map<String, byte[]> getTrustRootCertList() {
        return this.mTrustRootCertList;
    }

    public void setUpdateIdentifier(int i) {
        this.mUpdateIdentifier = i;
    }

    public int getUpdateIdentifier() {
        return this.mUpdateIdentifier;
    }

    public void setCredentialPriority(int i) {
        this.mCredentialPriority = i;
    }

    public int getCredentialPriority() {
        return this.mCredentialPriority;
    }

    public void setSubscriptionCreationTimeInMillis(long j) {
        this.mSubscriptionCreationTimeInMillis = j;
    }

    public long getSubscriptionCreationTimeInMillis() {
        return this.mSubscriptionCreationTimeInMillis;
    }

    public void setSubscriptionExpirationTimeInMillis(long j) {
        this.mSubscriptionExpirationTimeMillis = j;
    }

    public long getSubscriptionExpirationTimeMillis() {
        return this.mSubscriptionExpirationTimeMillis;
    }

    public void setSubscriptionType(String str) {
        this.mSubscriptionType = str;
    }

    public String getSubscriptionType() {
        return this.mSubscriptionType;
    }

    public void setUsageLimitUsageTimePeriodInMinutes(long j) {
        this.mUsageLimitUsageTimePeriodInMinutes = j;
    }

    public long getUsageLimitUsageTimePeriodInMinutes() {
        return this.mUsageLimitUsageTimePeriodInMinutes;
    }

    public void setUsageLimitStartTimeInMillis(long j) {
        this.mUsageLimitStartTimeInMillis = j;
    }

    public long getUsageLimitStartTimeInMillis() {
        return this.mUsageLimitStartTimeInMillis;
    }

    public void setUsageLimitDataLimit(long j) {
        this.mUsageLimitDataLimit = j;
    }

    public long getUsageLimitDataLimit() {
        return this.mUsageLimitDataLimit;
    }

    public void setUsageLimitTimeLimitInMinutes(long j) {
        this.mUsageLimitTimeLimitInMinutes = j;
    }

    public long getUsageLimitTimeLimitInMinutes() {
        return this.mUsageLimitTimeLimitInMinutes;
    }

    public void setServiceFriendlyNames(Map<String, String> map) {
        this.mServiceFriendlyNames = map;
    }

    public Map<String, String> getServiceFriendlyNames() {
        return this.mServiceFriendlyNames;
    }

    public String getServiceFriendlyName() {
        Map<String, String> map = this.mServiceFriendlyNames;
        if (map == null || map.isEmpty()) {
            return null;
        }
        String str = this.mServiceFriendlyNames.get(Locale.getDefault().getLanguage());
        if (str != null) {
            return str;
        }
        String str2 = this.mServiceFriendlyNames.get("en");
        if (str2 != null) {
            return str2;
        }
        Map<String, String> map2 = this.mServiceFriendlyNames;
        return map2.get(map2.keySet().stream().findFirst().get());
    }

    public void setCarrierId(int i) {
        this.mCarrierId = i;
    }

    public int getCarrierId() {
        return this.mCarrierId;
    }

    public void setSubscriptionId(int i) {
        this.mSubscriptionId = i;
    }

    public int getSubscriptionId() {
        return this.mSubscriptionId;
    }

    public void setSubscriptionGroup(ParcelUuid parcelUuid) {
        this.mSubscriptionGroup = parcelUuid;
    }

    public ParcelUuid getSubscriptionGroup() {
        return this.mSubscriptionGroup;
    }

    public void setAutojoinEnabled(boolean z) {
        this.mIsAutojoinEnabled = z;
    }

    public void setMacRandomizationEnabled(boolean z) {
        this.mIsMacRandomizationEnabled = z;
    }

    public void setNonPersistentMacRandomizationEnabled(boolean z) {
        this.mIsNonPersistentMacRandomizationEnabled = z;
    }

    public void setMeteredOverride(int i) {
        this.mMeteredOverride = i;
    }

    @SystemApi
    public boolean isAutojoinEnabled() {
        return this.mIsAutojoinEnabled;
    }

    @SystemApi
    public int getMeteredOverride() {
        return this.mMeteredOverride;
    }

    @SystemApi
    public boolean isMacRandomizationEnabled() {
        return this.mIsMacRandomizationEnabled;
    }

    public boolean isNonPersistentMacRandomizationEnabled() {
        return this.mIsNonPersistentMacRandomizationEnabled;
    }

    public void setOemPaid(boolean z) {
        this.mIsOemPaid = z;
    }

    public boolean isOemPaid() {
        return this.mIsOemPaid;
    }

    public void setOemPrivate(boolean z) {
        this.mIsOemPrivate = z;
    }

    public boolean isOemPrivate() {
        return this.mIsOemPrivate;
    }

    public void setCarrierMerged(boolean z) {
        this.mIsCarrierMerged = z;
    }

    public boolean isCarrierMerged() {
        return this.mIsCarrierMerged;
    }

    public PasspointConfiguration() {
    }

    public PasspointConfiguration(PasspointConfiguration passpointConfiguration) {
        if (passpointConfiguration != null) {
            if (passpointConfiguration.mHomeSp != null) {
                this.mHomeSp = new HomeSp(passpointConfiguration.mHomeSp);
            }
            if (passpointConfiguration.mCredential != null) {
                this.mCredential = new Credential(passpointConfiguration.mCredential);
            }
            if (passpointConfiguration.mPolicy != null) {
                this.mPolicy = new Policy(passpointConfiguration.mPolicy);
            }
            Map<String, byte[]> map = passpointConfiguration.mTrustRootCertList;
            if (map != null) {
                this.mTrustRootCertList = Collections.unmodifiableMap(map);
            }
            if (passpointConfiguration.mSubscriptionUpdate != null) {
                this.mSubscriptionUpdate = new UpdateParameter(passpointConfiguration.mSubscriptionUpdate);
            }
            this.mUpdateIdentifier = passpointConfiguration.mUpdateIdentifier;
            this.mCredentialPriority = passpointConfiguration.mCredentialPriority;
            this.mSubscriptionCreationTimeInMillis = passpointConfiguration.mSubscriptionCreationTimeInMillis;
            this.mSubscriptionExpirationTimeMillis = passpointConfiguration.mSubscriptionExpirationTimeMillis;
            this.mSubscriptionType = passpointConfiguration.mSubscriptionType;
            this.mUsageLimitDataLimit = passpointConfiguration.mUsageLimitDataLimit;
            this.mUsageLimitStartTimeInMillis = passpointConfiguration.mUsageLimitStartTimeInMillis;
            this.mUsageLimitTimeLimitInMinutes = passpointConfiguration.mUsageLimitTimeLimitInMinutes;
            this.mUsageLimitUsageTimePeriodInMinutes = passpointConfiguration.mUsageLimitUsageTimePeriodInMinutes;
            this.mServiceFriendlyNames = passpointConfiguration.mServiceFriendlyNames;
            this.mAaaServerTrustedNames = passpointConfiguration.mAaaServerTrustedNames;
            this.mCarrierId = passpointConfiguration.mCarrierId;
            this.mSubscriptionId = passpointConfiguration.mSubscriptionId;
            this.mIsAutojoinEnabled = passpointConfiguration.mIsAutojoinEnabled;
            this.mIsMacRandomizationEnabled = passpointConfiguration.mIsMacRandomizationEnabled;
            this.mIsNonPersistentMacRandomizationEnabled = passpointConfiguration.mIsNonPersistentMacRandomizationEnabled;
            this.mMeteredOverride = passpointConfiguration.mMeteredOverride;
            this.mIsCarrierMerged = passpointConfiguration.mIsCarrierMerged;
            this.mIsOemPaid = passpointConfiguration.mIsOemPaid;
            this.mIsOemPrivate = passpointConfiguration.mIsOemPrivate;
            this.mDecoratedIdentityPrefix = passpointConfiguration.mDecoratedIdentityPrefix;
            this.mSubscriptionGroup = passpointConfiguration.mSubscriptionGroup;
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mHomeSp, i);
        parcel.writeParcelable(this.mCredential, i);
        parcel.writeParcelable(this.mPolicy, i);
        parcel.writeParcelable(this.mSubscriptionUpdate, i);
        writeTrustRootCerts(parcel, this.mTrustRootCertList);
        parcel.writeInt(this.mUpdateIdentifier);
        parcel.writeInt(this.mCredentialPriority);
        parcel.writeLong(this.mSubscriptionCreationTimeInMillis);
        parcel.writeLong(this.mSubscriptionExpirationTimeMillis);
        parcel.writeString(this.mSubscriptionType);
        parcel.writeLong(this.mUsageLimitUsageTimePeriodInMinutes);
        parcel.writeLong(this.mUsageLimitStartTimeInMillis);
        parcel.writeLong(this.mUsageLimitDataLimit);
        parcel.writeLong(this.mUsageLimitTimeLimitInMinutes);
        parcel.writeStringArray(this.mAaaServerTrustedNames);
        Bundle bundle = new Bundle();
        bundle.putSerializable("serviceFriendlyNames", (HashMap) this.mServiceFriendlyNames);
        parcel.writeBundle(bundle);
        parcel.writeInt(this.mCarrierId);
        parcel.writeBoolean(this.mIsAutojoinEnabled);
        parcel.writeBoolean(this.mIsMacRandomizationEnabled);
        parcel.writeBoolean(this.mIsNonPersistentMacRandomizationEnabled);
        parcel.writeInt(this.mMeteredOverride);
        parcel.writeInt(this.mSubscriptionId);
        parcel.writeBoolean(this.mIsCarrierMerged);
        parcel.writeBoolean(this.mIsOemPaid);
        parcel.writeBoolean(this.mIsOemPrivate);
        parcel.writeString(this.mDecoratedIdentityPrefix);
        parcel.writeParcelable(this.mSubscriptionGroup, i);
    }

    public boolean equals(Object obj) {
        Map<String, String> map;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PasspointConfiguration)) {
            return false;
        }
        PasspointConfiguration passpointConfiguration = (PasspointConfiguration) obj;
        HomeSp homeSp = this.mHomeSp;
        if (homeSp != null ? homeSp.equals(passpointConfiguration.mHomeSp) : passpointConfiguration.mHomeSp == null) {
            String[] strArr = this.mAaaServerTrustedNames;
            if (strArr != null ? Arrays.equals((Object[]) strArr, (Object[]) passpointConfiguration.mAaaServerTrustedNames) : passpointConfiguration.mAaaServerTrustedNames == null) {
                Credential credential = this.mCredential;
                if (credential != null ? credential.equals(passpointConfiguration.mCredential) : passpointConfiguration.mCredential == null) {
                    Policy policy = this.mPolicy;
                    if (policy != null ? policy.equals(passpointConfiguration.mPolicy) : passpointConfiguration.mPolicy == null) {
                        UpdateParameter updateParameter = this.mSubscriptionUpdate;
                        if (updateParameter != null ? updateParameter.equals(passpointConfiguration.mSubscriptionUpdate) : passpointConfiguration.mSubscriptionUpdate == null) {
                            if (isTrustRootCertListEquals(this.mTrustRootCertList, passpointConfiguration.mTrustRootCertList) && this.mUpdateIdentifier == passpointConfiguration.mUpdateIdentifier && this.mCredentialPriority == passpointConfiguration.mCredentialPriority && this.mSubscriptionCreationTimeInMillis == passpointConfiguration.mSubscriptionCreationTimeInMillis && this.mSubscriptionExpirationTimeMillis == passpointConfiguration.mSubscriptionExpirationTimeMillis && TextUtils.equals(this.mSubscriptionType, passpointConfiguration.mSubscriptionType) && this.mUsageLimitUsageTimePeriodInMinutes == passpointConfiguration.mUsageLimitUsageTimePeriodInMinutes && this.mUsageLimitStartTimeInMillis == passpointConfiguration.mUsageLimitStartTimeInMillis && this.mUsageLimitDataLimit == passpointConfiguration.mUsageLimitDataLimit && this.mUsageLimitTimeLimitInMinutes == passpointConfiguration.mUsageLimitTimeLimitInMinutes && this.mCarrierId == passpointConfiguration.mCarrierId && this.mSubscriptionId == passpointConfiguration.mSubscriptionId && this.mIsOemPrivate == passpointConfiguration.mIsOemPrivate && this.mIsOemPaid == passpointConfiguration.mIsOemPaid && this.mIsCarrierMerged == passpointConfiguration.mIsCarrierMerged && this.mIsAutojoinEnabled == passpointConfiguration.mIsAutojoinEnabled && this.mIsMacRandomizationEnabled == passpointConfiguration.mIsMacRandomizationEnabled && this.mIsNonPersistentMacRandomizationEnabled == passpointConfiguration.mIsNonPersistentMacRandomizationEnabled && this.mMeteredOverride == passpointConfiguration.mMeteredOverride && ((map = this.mServiceFriendlyNames) != null ? map.equals(passpointConfiguration.mServiceFriendlyNames) : passpointConfiguration.mServiceFriendlyNames == null) && Objects.equals(this.mDecoratedIdentityPrefix, passpointConfiguration.mDecoratedIdentityPrefix) && Objects.equals(this.mSubscriptionGroup, passpointConfiguration.mSubscriptionGroup)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mHomeSp, this.mCredential, this.mPolicy, this.mSubscriptionUpdate, this.mTrustRootCertList, Integer.valueOf(this.mUpdateIdentifier), Integer.valueOf(this.mCredentialPriority), Long.valueOf(this.mSubscriptionCreationTimeInMillis), Long.valueOf(this.mSubscriptionExpirationTimeMillis), Long.valueOf(this.mUsageLimitUsageTimePeriodInMinutes), Long.valueOf(this.mUsageLimitStartTimeInMillis), Long.valueOf(this.mUsageLimitDataLimit), Long.valueOf(this.mUsageLimitTimeLimitInMinutes), this.mServiceFriendlyNames, Integer.valueOf(this.mCarrierId), Boolean.valueOf(this.mIsAutojoinEnabled), Boolean.valueOf(this.mIsMacRandomizationEnabled), Boolean.valueOf(this.mIsNonPersistentMacRandomizationEnabled), Integer.valueOf(this.mMeteredOverride), Integer.valueOf(this.mSubscriptionId), Boolean.valueOf(this.mIsCarrierMerged), Boolean.valueOf(this.mIsOemPaid), Boolean.valueOf(this.mIsOemPrivate), this.mDecoratedIdentityPrefix, this.mSubscriptionGroup);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("UpdateIdentifier: ");
        sb.append(this.mUpdateIdentifier);
        sb.append("\nCredentialPriority: ");
        sb.append(this.mCredentialPriority);
        sb.append("\nSubscriptionCreationTime: ");
        Object obj = "Not specified";
        sb.append(this.mSubscriptionCreationTimeInMillis != Long.MIN_VALUE ? new Date(this.mSubscriptionCreationTimeInMillis) : obj);
        sb.append("\nSubscriptionExpirationTime: ");
        sb.append(this.mSubscriptionExpirationTimeMillis != Long.MIN_VALUE ? new Date(this.mSubscriptionExpirationTimeMillis) : obj);
        sb.append("\nUsageLimitStartTime: ");
        if (this.mUsageLimitStartTimeInMillis != Long.MIN_VALUE) {
            obj = new Date(this.mUsageLimitStartTimeInMillis);
        }
        sb.append(obj);
        sb.append("\nUsageTimePeriod: ");
        sb.append(this.mUsageLimitUsageTimePeriodInMinutes);
        sb.append("\nUsageLimitDataLimit: ");
        sb.append(this.mUsageLimitDataLimit);
        sb.append("\nUsageLimitTimeLimit: ");
        sb.append(this.mUsageLimitTimeLimitInMinutes);
        sb.append("\nProvisioned by a subscription server: ");
        sb.append(isOsuProvisioned() ? "Yes" : "No");
        sb.append("\n");
        if (this.mHomeSp != null) {
            sb.append("HomeSP Begin ---\n");
            sb.append((Object) this.mHomeSp);
            sb.append("HomeSP End ---\n");
        }
        if (this.mCredential != null) {
            sb.append("Credential Begin ---\n");
            sb.append((Object) this.mCredential);
            sb.append("Credential End ---\n");
        }
        if (this.mPolicy != null) {
            sb.append("Policy Begin ---\n");
            sb.append((Object) this.mPolicy);
            sb.append("Policy End ---\n");
        }
        if (this.mSubscriptionUpdate != null) {
            sb.append("SubscriptionUpdate Begin ---\n");
            sb.append((Object) this.mSubscriptionUpdate);
            sb.append("SubscriptionUpdate End ---\n");
        }
        if (this.mTrustRootCertList != null) {
            sb.append("TrustRootCertServers: ");
            sb.append((Object) this.mTrustRootCertList.keySet());
            sb.append("\n");
        }
        if (this.mAaaServerTrustedNames != null) {
            sb.append("AAAServerTrustedNames: ");
            sb.append(String.join((CharSequence) NavigationBarInflaterView.GRAVITY_SEPARATOR, (CharSequence[]) this.mAaaServerTrustedNames));
            sb.append("\n");
        }
        if (this.mServiceFriendlyNames != null) {
            sb.append("ServiceFriendlyNames: ");
            sb.append((Object) this.mServiceFriendlyNames);
        }
        sb.append("CarrierId:" + this.mCarrierId);
        sb.append("SubscriptionId:" + this.mSubscriptionId);
        sb.append("IsAutojoinEnabled:" + this.mIsAutojoinEnabled);
        sb.append("mIsMacRandomizationEnabled:" + this.mIsMacRandomizationEnabled);
        sb.append("mIsNonPersistentMacRandomizationEnabled:" + this.mIsNonPersistentMacRandomizationEnabled);
        sb.append("mMeteredOverride:" + this.mMeteredOverride);
        sb.append("mIsCarrierMerged:" + this.mIsCarrierMerged);
        sb.append("mIsOemPaid:" + this.mIsOemPaid);
        sb.append("mIsOemPrivate:" + this.mIsOemPrivate);
        sb.append("mDecoratedUsernamePrefix:" + this.mDecoratedIdentityPrefix);
        sb.append("mSubscriptionGroup:" + this.mSubscriptionGroup);
        return sb.toString();
    }

    public boolean validate() {
        UpdateParameter updateParameter = this.mSubscriptionUpdate;
        if (updateParameter == null || updateParameter.validate()) {
            return validateForCommonR1andR2();
        }
        return false;
    }

    public boolean validateForR2() {
        UpdateParameter updateParameter;
        if (this.mUpdateIdentifier == Integer.MIN_VALUE || (updateParameter = this.mSubscriptionUpdate) == null || !updateParameter.validate()) {
            return false;
        }
        return validateForCommonR1andR2();
    }

    private boolean validateForCommonR1andR2() {
        Credential credential;
        HomeSp homeSp = this.mHomeSp;
        if (homeSp == null || !homeSp.validate() || (credential = this.mCredential) == null || !credential.validate()) {
            return false;
        }
        Policy policy = this.mPolicy;
        if (policy != null && !policy.validate()) {
            return false;
        }
        Map<String, byte[]> map = this.mTrustRootCertList;
        if (map == null) {
            return true;
        }
        for (Map.Entry next : map.entrySet()) {
            String str = (String) next.getKey();
            byte[] bArr = (byte[]) next.getValue();
            if (TextUtils.isEmpty(str)) {
                Log.d(TAG, "Empty URL");
                return false;
            } else if (str.getBytes(StandardCharsets.UTF_8).length > 1023) {
                Log.d(TAG, "URL bytes exceeded the max: " + str.getBytes(StandardCharsets.UTF_8).length);
                return false;
            } else if (bArr == null) {
                Log.d(TAG, "Fingerprint not specified");
                return false;
            } else if (bArr.length != 32) {
                Log.d(TAG, "Incorrect size of trust root certificate SHA-256 fingerprint: " + bArr.length);
                return false;
            }
        }
        return true;
    }

    private static void writeTrustRootCerts(Parcel parcel, Map<String, byte[]> map) {
        if (map == null) {
            parcel.writeInt(-1);
            return;
        }
        parcel.writeInt(map.size());
        for (Map.Entry next : map.entrySet()) {
            parcel.writeString((String) next.getKey());
            parcel.writeByteArray((byte[]) next.getValue());
        }
    }

    private static boolean isTrustRootCertListEquals(Map<String, byte[]> map, Map<String, byte[]> map2) {
        if (map == null || map2 == null) {
            if (map == map2) {
                return true;
            }
            return false;
        } else if (map.size() != map2.size()) {
            return false;
        } else {
            for (Map.Entry next : map.entrySet()) {
                if (!Arrays.equals((byte[]) next.getValue(), map2.get(next.getKey()))) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean isOsuProvisioned() {
        return getUpdateIdentifier() != Integer.MIN_VALUE;
    }

    public String getUniqueId() {
        HomeSp homeSp;
        if (this.mCredential == null || (homeSp = this.mHomeSp) == null || TextUtils.isEmpty(homeSp.getFqdn())) {
            throw new IllegalStateException("Credential or HomeSP are not initialized");
        }
        return String.format("%s_%x%x", this.mHomeSp.getFqdn(), Integer.valueOf(this.mHomeSp.getUniqueId()), Integer.valueOf(this.mCredential.getUniqueId()));
    }

    public void setDecoratedIdentityPrefix(String str) {
        if (!SdkLevel.isAtLeastS()) {
            throw new UnsupportedOperationException();
        } else if (TextUtils.isEmpty(str) || str.endsWith("!")) {
            this.mDecoratedIdentityPrefix = str;
        } else {
            throw new IllegalArgumentException("Decorated identity prefix must be delimited by '!'");
        }
    }

    public String getDecoratedIdentityPrefix() {
        if (SdkLevel.isAtLeastS()) {
            return this.mDecoratedIdentityPrefix;
        }
        throw new UnsupportedOperationException();
    }
}
