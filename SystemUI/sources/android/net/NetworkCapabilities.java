package android.net;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.net.module.util.CollectionUtils;
import android.net.connectivity.com.android.net.module.util.NetworkCapabilitiesUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Range;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public final class NetworkCapabilities implements Parcelable {
    private static final long CONNECTIVITY_MANAGED_CAPABILITIES = NetworkCapabilitiesUtils.packBitList(16, 17, 19, 24);
    public static final Parcelable.Creator<NetworkCapabilities> CREATOR = new Parcelable.Creator<NetworkCapabilities>() {
        public NetworkCapabilities createFromParcel(Parcel parcel) {
            NetworkCapabilities networkCapabilities = new NetworkCapabilities();
            networkCapabilities.mNetworkCapabilities = parcel.readLong();
            networkCapabilities.mForbiddenNetworkCapabilities = parcel.readLong();
            networkCapabilities.mTransportTypes = parcel.readLong();
            networkCapabilities.mLinkUpBandwidthKbps = parcel.readInt();
            networkCapabilities.mLinkDownBandwidthKbps = parcel.readInt();
            networkCapabilities.mNetworkSpecifier = (NetworkSpecifier) parcel.readParcelable((ClassLoader) null);
            networkCapabilities.mTransportInfo = (TransportInfo) parcel.readParcelable((ClassLoader) null);
            networkCapabilities.mSignalStrength = parcel.readInt();
            networkCapabilities.mUids = readParcelableArraySet(parcel, (ClassLoader) null);
            int[] createIntArray = parcel.createIntArray();
            networkCapabilities.mAllowedUids.ensureCapacity(createIntArray.length);
            for (int valueOf : createIntArray) {
                networkCapabilities.mAllowedUids.add(Integer.valueOf(valueOf));
            }
            networkCapabilities.mSSID = parcel.readString();
            networkCapabilities.mPrivateDnsBroken = parcel.readBoolean();
            networkCapabilities.setAdministratorUids(parcel.createIntArray());
            networkCapabilities.mOwnerUid = parcel.readInt();
            networkCapabilities.mRequestorUid = parcel.readInt();
            networkCapabilities.mRequestorPackageName = parcel.readString();
            networkCapabilities.mSubIds = new ArraySet();
            int[] iArr = (int[]) Objects.requireNonNull(parcel.createIntArray());
            for (int valueOf2 : iArr) {
                networkCapabilities.mSubIds.add(Integer.valueOf(valueOf2));
            }
            networkCapabilities.setUnderlyingNetworks(parcel.createTypedArrayList(Network.CREATOR));
            networkCapabilities.mEnterpriseId = parcel.readInt();
            return networkCapabilities;
        }

        public NetworkCapabilities[] newArray(int i) {
            return new NetworkCapabilities[i];
        }

        private <T extends Parcelable> ArraySet<T> readParcelableArraySet(Parcel parcel, ClassLoader classLoader) {
            int readInt = parcel.readInt();
            if (readInt < 0) {
                return null;
            }
            ArraySet<T> arraySet = new ArraySet<>(readInt);
            for (int i = 0; i < readInt; i++) {
                arraySet.add(parcel.readParcelable(classLoader));
            }
            return arraySet;
        }
    };
    private static final long DEFAULT_CAPABILITIES = NetworkCapabilitiesUtils.packBitList(13, 14, 15);
    public static final int LINK_BANDWIDTH_UNSPECIFIED = 0;
    private static final int MAX_NET_CAPABILITY = 35;
    public static final int MAX_TRANSPORT = 8;
    private static final int MIN_NET_CAPABILITY = 0;
    public static final int MIN_TRANSPORT = 0;
    private static final long MUTABLE_CAPABILITIES;
    @SystemApi
    public static final int NET_CAPABILITY_BIP = 31;
    public static final int NET_CAPABILITY_CAPTIVE_PORTAL = 17;
    public static final int NET_CAPABILITY_CBS = 5;
    public static final int NET_CAPABILITY_DUN = 2;
    public static final int NET_CAPABILITY_EIMS = 10;
    public static final int NET_CAPABILITY_ENTERPRISE = 29;
    public static final int NET_CAPABILITY_FOREGROUND = 19;
    public static final int NET_CAPABILITY_FOTA = 3;
    public static final int NET_CAPABILITY_HEAD_UNIT = 32;
    public static final int NET_CAPABILITY_IA = 7;
    public static final int NET_CAPABILITY_IMS = 4;
    public static final int NET_CAPABILITY_INTERNET = 12;
    public static final int NET_CAPABILITY_MCX = 23;
    public static final int NET_CAPABILITY_MMS = 0;
    public static final int NET_CAPABILITY_MMTEL = 33;
    public static final int NET_CAPABILITY_NOT_CONGESTED = 20;
    public static final int NET_CAPABILITY_NOT_METERED = 11;
    public static final int NET_CAPABILITY_NOT_RESTRICTED = 13;
    public static final int NET_CAPABILITY_NOT_ROAMING = 18;
    public static final int NET_CAPABILITY_NOT_SUSPENDED = 21;
    @SystemApi
    public static final int NET_CAPABILITY_NOT_VCN_MANAGED = 28;
    public static final int NET_CAPABILITY_NOT_VPN = 15;
    @SystemApi
    public static final int NET_CAPABILITY_OEM_PAID = 22;
    @SystemApi
    public static final int NET_CAPABILITY_OEM_PRIVATE = 26;
    @SystemApi
    public static final int NET_CAPABILITY_PARTIAL_CONNECTIVITY = 24;
    public static final int NET_CAPABILITY_PRIORITIZE_BANDWIDTH = 35;
    public static final int NET_CAPABILITY_PRIORITIZE_LATENCY = 34;
    public static final int NET_CAPABILITY_RCS = 8;
    public static final int NET_CAPABILITY_SUPL = 1;
    public static final int NET_CAPABILITY_TEMPORARILY_NOT_METERED = 25;
    public static final int NET_CAPABILITY_TRUSTED = 14;
    public static final int NET_CAPABILITY_VALIDATED = 16;
    @SystemApi
    public static final int NET_CAPABILITY_VEHICLE_INTERNAL = 27;
    @SystemApi
    public static final int NET_CAPABILITY_VSIM = 30;
    public static final int NET_CAPABILITY_WIFI_P2P = 6;
    public static final int NET_CAPABILITY_XCAP = 9;
    public static final int NET_ENTERPRISE_ID_1 = 1;
    public static final int NET_ENTERPRISE_ID_2 = 2;
    public static final int NET_ENTERPRISE_ID_3 = 3;
    public static final int NET_ENTERPRISE_ID_4 = 4;
    public static final int NET_ENTERPRISE_ID_5 = 5;
    private static final long NON_REQUESTABLE_CAPABILITIES;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final long REDACT_ALL = -1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final long REDACT_FOR_ACCESS_FINE_LOCATION = 1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final long REDACT_FOR_LOCAL_MAC_ADDRESS = 2;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final long REDACT_FOR_NETWORK_SETTINGS = 4;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final long REDACT_NONE = 0;
    public static final int SIGNAL_STRENGTH_UNSPECIFIED = Integer.MIN_VALUE;
    private static final String TAG = "NetworkCapabilities";
    private static final long TEST_NETWORKS_ALLOWED_CAPABILITIES = NetworkCapabilitiesUtils.packBitList(11, 25, 13, 15, 18, 20, 21, 28);
    public static final int TRANSPORT_BLUETOOTH = 2;
    public static final int TRANSPORT_CELLULAR = 0;
    public static final int TRANSPORT_ETHERNET = 3;
    public static final int TRANSPORT_LOWPAN = 6;
    private static final String[] TRANSPORT_NAMES = {"CELLULAR", "WIFI", "BLUETOOTH", "ETHERNET", "VPN", "WIFI_AWARE", "LOWPAN", "TEST", "USB"};
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int TRANSPORT_TEST = 7;
    public static final int TRANSPORT_USB = 8;
    public static final int TRANSPORT_VPN = 4;
    public static final int TRANSPORT_WIFI = 1;
    public static final int TRANSPORT_WIFI_AWARE = 5;
    private static final long UNRESTRICTED_TEST_NETWORKS_ALLOWED_TRANSPORTS = NetworkCapabilitiesUtils.packBitList(7, 3, 4);
    private int[] mAdministratorUids;
    /* access modifiers changed from: private */
    public final ArraySet<Integer> mAllowedUids;
    /* access modifiers changed from: private */
    public int mEnterpriseId;
    /* access modifiers changed from: private */
    public long mForbiddenNetworkCapabilities;
    /* access modifiers changed from: private */
    public int mLinkDownBandwidthKbps;
    /* access modifiers changed from: private */
    public int mLinkUpBandwidthKbps;
    /* access modifiers changed from: private */
    public long mNetworkCapabilities;
    /* access modifiers changed from: private */
    public NetworkSpecifier mNetworkSpecifier;
    /* access modifiers changed from: private */
    public int mOwnerUid;
    /* access modifiers changed from: private */
    public boolean mPrivateDnsBroken;
    /* access modifiers changed from: private */
    public String mRequestorPackageName;
    /* access modifiers changed from: private */
    public int mRequestorUid;
    /* access modifiers changed from: private */
    public String mSSID;
    /* access modifiers changed from: private */
    public int mSignalStrength;
    /* access modifiers changed from: private */
    public ArraySet<Integer> mSubIds;
    /* access modifiers changed from: private */
    public TransportInfo mTransportInfo;
    /* access modifiers changed from: private */
    public long mTransportTypes;
    /* access modifiers changed from: private */
    public ArraySet<UidRange> mUids;
    private List<Network> mUnderlyingNetworks;

    @Retention(RetentionPolicy.SOURCE)
    public @interface EnterpriseId {
    }

    private interface NameOf {
        String nameOf(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NetCapability {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RedactionType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Transport {
    }

    private static boolean isValidCapability(int i) {
        return i >= 0 && i <= 35;
    }

    private static boolean isValidEnterpriseId(int i) {
        return i >= 1 && i <= 5;
    }

    public static boolean isValidTransport(int i) {
        return i >= 0 && i <= 8;
    }

    public int describeContents() {
        return 0;
    }

    public int[] getEnterpriseIds() {
        if (!hasCapability(29) || this.mEnterpriseId != 0) {
            return NetworkCapabilitiesUtils.unpackBits((long) this.mEnterpriseId);
        }
        return new int[]{1};
    }

    public boolean hasEnterpriseId(int i) {
        if (i == 1 && hasCapability(29) && this.mEnterpriseId == 0) {
            return true;
        }
        if (isValidEnterpriseId(i)) {
            if (((1 << i) & ((long) this.mEnterpriseId)) != 0) {
                return true;
            }
        }
        return false;
    }

    public NetworkCapabilities() {
        this.mOwnerUid = -1;
        this.mAdministratorUids = new int[0];
        this.mLinkUpBandwidthKbps = 0;
        this.mLinkDownBandwidthKbps = 0;
        this.mNetworkSpecifier = null;
        this.mTransportInfo = null;
        this.mSignalStrength = Integer.MIN_VALUE;
        this.mUids = null;
        this.mAllowedUids = new ArraySet<>();
        this.mSubIds = new ArraySet<>();
        clearAll();
        this.mNetworkCapabilities = DEFAULT_CAPABILITIES;
    }

    public NetworkCapabilities(NetworkCapabilities networkCapabilities) {
        this(networkCapabilities, 0);
    }

    public NetworkCapabilities(NetworkCapabilities networkCapabilities, long j) {
        this.mOwnerUid = -1;
        this.mAdministratorUids = new int[0];
        this.mLinkUpBandwidthKbps = 0;
        this.mLinkDownBandwidthKbps = 0;
        this.mNetworkSpecifier = null;
        this.mTransportInfo = null;
        this.mSignalStrength = Integer.MIN_VALUE;
        this.mUids = null;
        this.mAllowedUids = new ArraySet<>();
        this.mSubIds = new ArraySet<>();
        if (networkCapabilities != null) {
            set(networkCapabilities);
        }
        if (this.mTransportInfo != null) {
            this.mTransportInfo = networkCapabilities.mTransportInfo.makeCopy(j);
        }
    }

    public void clearAll() {
        this.mForbiddenNetworkCapabilities = 0;
        this.mTransportTypes = 0;
        this.mNetworkCapabilities = 0;
        this.mLinkDownBandwidthKbps = 0;
        this.mLinkUpBandwidthKbps = 0;
        this.mNetworkSpecifier = null;
        this.mTransportInfo = null;
        this.mSignalStrength = Integer.MIN_VALUE;
        this.mUids = null;
        this.mAllowedUids.clear();
        this.mAdministratorUids = new int[0];
        this.mOwnerUid = -1;
        this.mSSID = null;
        this.mPrivateDnsBroken = false;
        this.mRequestorUid = -1;
        this.mRequestorPackageName = null;
        this.mSubIds = new ArraySet<>();
        this.mUnderlyingNetworks = null;
        this.mEnterpriseId = 0;
    }

    public void set(NetworkCapabilities networkCapabilities) {
        this.mNetworkCapabilities = networkCapabilities.mNetworkCapabilities;
        this.mTransportTypes = networkCapabilities.mTransportTypes;
        this.mLinkUpBandwidthKbps = networkCapabilities.mLinkUpBandwidthKbps;
        this.mLinkDownBandwidthKbps = networkCapabilities.mLinkDownBandwidthKbps;
        this.mNetworkSpecifier = networkCapabilities.mNetworkSpecifier;
        ArraySet<UidRange> arraySet = null;
        if (networkCapabilities.getTransportInfo() != null) {
            setTransportInfo(networkCapabilities.getTransportInfo());
        } else {
            setTransportInfo((TransportInfo) null);
        }
        this.mSignalStrength = networkCapabilities.mSignalStrength;
        if (networkCapabilities.mUids != null) {
            arraySet = new ArraySet<>(networkCapabilities.mUids);
        }
        this.mUids = arraySet;
        setAllowedUids(networkCapabilities.mAllowedUids);
        setAdministratorUids(networkCapabilities.getAdministratorUids());
        this.mOwnerUid = networkCapabilities.mOwnerUid;
        this.mForbiddenNetworkCapabilities = networkCapabilities.mForbiddenNetworkCapabilities;
        this.mSSID = networkCapabilities.mSSID;
        this.mPrivateDnsBroken = networkCapabilities.mPrivateDnsBroken;
        this.mRequestorUid = networkCapabilities.mRequestorUid;
        this.mRequestorPackageName = networkCapabilities.mRequestorPackageName;
        this.mSubIds = new ArraySet<>(networkCapabilities.mSubIds);
        this.mUnderlyingNetworks = networkCapabilities.mUnderlyingNetworks;
        this.mEnterpriseId = networkCapabilities.mEnterpriseId;
    }

    static {
        long packBitList = NetworkCapabilitiesUtils.packBitList(14, 16, 17, 18, 19, 20, 21, 24, 25, 28, 32);
        MUTABLE_CAPABILITIES = packBitList;
        NON_REQUESTABLE_CAPABILITIES = packBitList & -16385 & -268435457;
    }

    public NetworkCapabilities addCapability(int i) {
        checkValidCapability(i);
        long j = 1 << i;
        this.mNetworkCapabilities |= j;
        this.mForbiddenNetworkCapabilities &= ~j;
        return this;
    }

    public void addForbiddenCapability(int i) {
        checkValidCapability(i);
        long j = 1 << i;
        this.mForbiddenNetworkCapabilities |= j;
        this.mNetworkCapabilities &= ~j;
    }

    public NetworkCapabilities removeCapability(int i) {
        checkValidCapability(i);
        this.mNetworkCapabilities = (~(1 << i)) & this.mNetworkCapabilities;
        return this;
    }

    public NetworkCapabilities removeForbiddenCapability(int i) {
        checkValidCapability(i);
        this.mForbiddenNetworkCapabilities &= ~(1 << i);
        return this;
    }

    public NetworkCapabilities setCapability(int i, boolean z) {
        if (z) {
            addCapability(i);
        } else {
            removeCapability(i);
        }
        return this;
    }

    public int[] getCapabilities() {
        return NetworkCapabilitiesUtils.unpackBits(this.mNetworkCapabilities);
    }

    public int[] getForbiddenCapabilities() {
        return NetworkCapabilitiesUtils.unpackBits(this.mForbiddenNetworkCapabilities);
    }

    public void setCapabilities(int[] iArr, int[] iArr2) {
        this.mNetworkCapabilities = NetworkCapabilitiesUtils.packBits(iArr);
        this.mForbiddenNetworkCapabilities = NetworkCapabilitiesUtils.packBits(iArr2);
    }

    @Deprecated
    public void setCapabilities(int[] iArr) {
        setCapabilities(iArr, new int[0]);
    }

    public NetworkCapabilities addEnterpriseId(int i) {
        checkValidEnterpriseId(i);
        this.mEnterpriseId = (1 << i) | this.mEnterpriseId;
        return this;
    }

    /* access modifiers changed from: private */
    public NetworkCapabilities removeEnterpriseId(int i) {
        checkValidEnterpriseId(i);
        this.mEnterpriseId = (~(1 << i)) & this.mEnterpriseId;
        return this;
    }

    public void setUnderlyingNetworks(List<Network> list) {
        this.mUnderlyingNetworks = list == null ? null : Collections.unmodifiableList(new ArrayList(list));
    }

    @SystemApi
    public List<Network> getUnderlyingNetworks() {
        return this.mUnderlyingNetworks;
    }

    private boolean equalsUnderlyingNetworks(NetworkCapabilities networkCapabilities) {
        return Objects.equals(getUnderlyingNetworks(), networkCapabilities.getUnderlyingNetworks());
    }

    public boolean hasCapability(int i) {
        if (isValidCapability(i)) {
            if (((1 << i) & this.mNetworkCapabilities) != 0) {
                return true;
            }
        }
        return false;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public boolean hasForbiddenCapability(int i) {
        if (isValidCapability(i)) {
            if (((1 << i) & this.mForbiddenNetworkCapabilities) != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean hasConnectivityManagedCapability() {
        return (this.mNetworkCapabilities & CONNECTIVITY_MANAGED_CAPABILITIES) != 0;
    }

    @SystemApi
    public static String getCapabilityCarrierName(int i) {
        if (i == 29) {
            return capabilityNameOf(i);
        }
        return null;
    }

    public String describeFirstNonRequestableCapability() {
        long j = (this.mNetworkCapabilities | this.mForbiddenNetworkCapabilities) & NON_REQUESTABLE_CAPABILITIES;
        if (j != 0) {
            return capabilityNameOf(NetworkCapabilitiesUtils.unpackBits(j)[0]);
        }
        if (this.mLinkUpBandwidthKbps != 0 || this.mLinkDownBandwidthKbps != 0) {
            return "link bandwidth";
        }
        if (hasSignalStrength()) {
            return "signalStrength";
        }
        if (isPrivateDnsBroken()) {
            return "privateDnsBroken";
        }
        return null;
    }

    private boolean equalsEnterpriseCapabilitiesId(NetworkCapabilities networkCapabilities) {
        return networkCapabilities.mEnterpriseId == this.mEnterpriseId;
    }

    private boolean satisfiedByEnterpriseCapabilitiesId(NetworkCapabilities networkCapabilities) {
        int i = this.mEnterpriseId;
        int i2 = networkCapabilities.mEnterpriseId;
        if ((i2 & i) == i) {
            return true;
        }
        return i2 == 0 && ((long) i) == 2;
    }

    private boolean satisfiedByNetCapabilities(NetworkCapabilities networkCapabilities, boolean z) {
        long j = this.mNetworkCapabilities;
        long j2 = this.mForbiddenNetworkCapabilities;
        long j3 = networkCapabilities.mNetworkCapabilities;
        if (z) {
            long j4 = MUTABLE_CAPABILITIES;
            j &= ~j4;
            j2 &= ~j4;
        }
        return (j3 & j) == j && (j3 & j2) == 0;
    }

    public boolean equalsNetCapabilities(NetworkCapabilities networkCapabilities) {
        return networkCapabilities.mNetworkCapabilities == this.mNetworkCapabilities && networkCapabilities.mForbiddenNetworkCapabilities == this.mForbiddenNetworkCapabilities;
    }

    private boolean equalsNetCapabilitiesRequestable(NetworkCapabilities networkCapabilities) {
        long j = this.mNetworkCapabilities;
        long j2 = NON_REQUESTABLE_CAPABILITIES;
        return (j & (~j2)) == (networkCapabilities.mNetworkCapabilities & (~j2)) && (this.mForbiddenNetworkCapabilities & (~j2)) == (networkCapabilities.mForbiddenNetworkCapabilities & (~j2));
    }

    public void maybeMarkCapabilitiesRestricted() {
        if (NetworkCapabilitiesUtils.inferRestrictedCapability(this)) {
            removeCapability(13);
        }
    }

    @Deprecated
    public void restrictCapabilitesForTestNetwork(int i) {
        restrictCapabilitiesForTestNetwork(i);
    }

    public void restrictCapabilitiesForTestNetwork(int i) {
        int i2 = i;
        long j = this.mNetworkCapabilities;
        long j2 = this.mTransportTypes;
        NetworkSpecifier networkSpecifier = this.mNetworkSpecifier;
        int i3 = this.mSignalStrength;
        int ownerUid = getOwnerUid();
        int[] administratorUids = getAdministratorUids();
        TransportInfo transportInfo = getTransportInfo();
        Set<Integer> subscriptionIds = getSubscriptionIds();
        ArraySet arraySet = new ArraySet(this.mAllowedUids);
        clearAll();
        if (0 != (8192 & j)) {
            this.mTransportTypes = (j2 & UNRESTRICTED_TEST_NETWORKS_ALLOWED_TRANSPORTS) | 128;
            setSubscriptionIds(subscriptionIds);
        } else {
            this.mTransportTypes = j2 | 128;
        }
        this.mNetworkCapabilities = j & TEST_NETWORKS_ALLOWED_CAPABILITIES;
        this.mNetworkSpecifier = networkSpecifier;
        this.mSignalStrength = i3;
        this.mTransportInfo = transportInfo;
        this.mAllowedUids.addAll(arraySet);
        if (ownerUid == i2) {
            setOwnerUid(i);
        }
        if (CollectionUtils.contains(administratorUids, i2)) {
            setAdministratorUids(new int[]{i2});
        }
    }

    public NetworkCapabilities addTransportType(int i) {
        checkValidTransportType(i);
        this.mTransportTypes |= (long) (1 << i);
        setNetworkSpecifier(this.mNetworkSpecifier);
        return this;
    }

    public NetworkCapabilities removeTransportType(int i) {
        checkValidTransportType(i);
        this.mTransportTypes &= (long) (~(1 << i));
        setNetworkSpecifier(this.mNetworkSpecifier);
        return this;
    }

    public NetworkCapabilities setTransportType(int i, boolean z) {
        if (z) {
            addTransportType(i);
        } else {
            removeTransportType(i);
        }
        return this;
    }

    @SystemApi
    public int[] getTransportTypes() {
        return NetworkCapabilitiesUtils.unpackBits(this.mTransportTypes);
    }

    public void setTransportTypes(int[] iArr) {
        this.mTransportTypes = NetworkCapabilitiesUtils.packBits(iArr);
    }

    public boolean hasTransport(int i) {
        return isValidTransport(i) && (this.mTransportTypes & ((long) (1 << i))) != 0;
    }

    public boolean hasSingleTransport(int i) {
        return this.mTransportTypes == ((long) (1 << i));
    }

    private boolean satisfiedByTransportTypes(NetworkCapabilities networkCapabilities) {
        long j = this.mTransportTypes;
        return j == 0 || (networkCapabilities.mTransportTypes & j) != 0;
    }

    public boolean equalsTransportTypes(NetworkCapabilities networkCapabilities) {
        return networkCapabilities.mTransportTypes == this.mTransportTypes;
    }

    public NetworkCapabilities setOwnerUid(int i) {
        this.mOwnerUid = i;
        return this;
    }

    public int getOwnerUid() {
        return this.mOwnerUid;
    }

    private boolean equalsOwnerUid(NetworkCapabilities networkCapabilities) {
        return this.mOwnerUid == networkCapabilities.mOwnerUid;
    }

    public NetworkCapabilities setAdministratorUids(int[] iArr) {
        int[] iArr2;
        int i;
        int[] copyOf = Arrays.copyOf(iArr, iArr.length);
        this.mAdministratorUids = copyOf;
        Arrays.sort(copyOf);
        int i2 = 0;
        do {
            iArr2 = this.mAdministratorUids;
            if (i2 >= iArr2.length - 1) {
                return this;
            }
            i = iArr2[i2];
            i2++;
        } while (i < iArr2[i2]);
        throw new IllegalArgumentException("All administrator UIDs must be unique");
    }

    @SystemApi
    public int[] getAdministratorUids() {
        int[] iArr = this.mAdministratorUids;
        return Arrays.copyOf(iArr, iArr.length);
    }

    public boolean equalsAdministratorUids(NetworkCapabilities networkCapabilities) {
        return Arrays.equals(this.mAdministratorUids, networkCapabilities.mAdministratorUids);
    }

    public NetworkCapabilities setLinkUpstreamBandwidthKbps(int i) {
        this.mLinkUpBandwidthKbps = i;
        return this;
    }

    public int getLinkUpstreamBandwidthKbps() {
        return this.mLinkUpBandwidthKbps;
    }

    public NetworkCapabilities setLinkDownstreamBandwidthKbps(int i) {
        this.mLinkDownBandwidthKbps = i;
        return this;
    }

    public int getLinkDownstreamBandwidthKbps() {
        return this.mLinkDownBandwidthKbps;
    }

    private boolean satisfiedByLinkBandwidths(NetworkCapabilities networkCapabilities) {
        return this.mLinkUpBandwidthKbps <= networkCapabilities.mLinkUpBandwidthKbps && this.mLinkDownBandwidthKbps <= networkCapabilities.mLinkDownBandwidthKbps;
    }

    private boolean equalsLinkBandwidths(NetworkCapabilities networkCapabilities) {
        return this.mLinkUpBandwidthKbps == networkCapabilities.mLinkUpBandwidthKbps && this.mLinkDownBandwidthKbps == networkCapabilities.mLinkDownBandwidthKbps;
    }

    public static int minBandwidth(int i, int i2) {
        if (i == 0) {
            return i2;
        }
        return i2 == 0 ? i : Math.min(i, i2);
    }

    public static int maxBandwidth(int i, int i2) {
        return Math.max(i, i2);
    }

    public NetworkCapabilities setNetworkSpecifier(NetworkSpecifier networkSpecifier) {
        if (networkSpecifier != null) {
            long j = this.mTransportTypes;
            if (!(j == 128 || Long.bitCount(j & -129) == 1)) {
                throw new IllegalStateException("Must have a single non-test transport specified to use setNetworkSpecifier");
            }
        }
        this.mNetworkSpecifier = networkSpecifier;
        return this;
    }

    public NetworkCapabilities setTransportInfo(TransportInfo transportInfo) {
        this.mTransportInfo = transportInfo;
        return this;
    }

    public NetworkSpecifier getNetworkSpecifier() {
        return this.mNetworkSpecifier;
    }

    public TransportInfo getTransportInfo() {
        return this.mTransportInfo;
    }

    private boolean satisfiedBySpecifier(NetworkCapabilities networkCapabilities) {
        NetworkSpecifier networkSpecifier = this.mNetworkSpecifier;
        return networkSpecifier == null || networkSpecifier.canBeSatisfiedBy(networkCapabilities.mNetworkSpecifier) || (networkCapabilities.mNetworkSpecifier instanceof MatchAllNetworkSpecifier);
    }

    private boolean equalsSpecifier(NetworkCapabilities networkCapabilities) {
        return Objects.equals(this.mNetworkSpecifier, networkCapabilities.mNetworkSpecifier);
    }

    private boolean equalsTransportInfo(NetworkCapabilities networkCapabilities) {
        return Objects.equals(this.mTransportInfo, networkCapabilities.mTransportInfo);
    }

    public NetworkCapabilities setSignalStrength(int i) {
        this.mSignalStrength = i;
        return this;
    }

    public boolean hasSignalStrength() {
        return this.mSignalStrength > Integer.MIN_VALUE;
    }

    public int getSignalStrength() {
        return this.mSignalStrength;
    }

    private boolean satisfiedBySignalStrength(NetworkCapabilities networkCapabilities) {
        return this.mSignalStrength <= networkCapabilities.mSignalStrength;
    }

    private boolean equalsSignalStrength(NetworkCapabilities networkCapabilities) {
        return this.mSignalStrength == networkCapabilities.mSignalStrength;
    }

    public NetworkCapabilities setSingleUid(int i) {
        ArraySet<UidRange> arraySet = new ArraySet<>(1);
        this.mUids = arraySet;
        arraySet.add(new UidRange(i, i));
        return this;
    }

    public NetworkCapabilities setUids(Set<Range<Integer>> set) {
        this.mUids = UidRange.fromIntRanges(set);
        return this;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public Set<Range<Integer>> getUids() {
        return UidRange.toIntRanges(this.mUids);
    }

    public Set<UidRange> getUidRanges() {
        if (this.mUids == null) {
            return null;
        }
        return new ArraySet(this.mUids);
    }

    public boolean appliesToUid(int i) {
        ArraySet<UidRange> arraySet = this.mUids;
        if (arraySet == null) {
            return true;
        }
        Iterator<UidRange> it = arraySet.iterator();
        while (it.hasNext()) {
            if (it.next().contains(i)) {
                return true;
            }
        }
        return false;
    }

    public boolean equalsUids(NetworkCapabilities networkCapabilities) {
        return UidRange.hasSameUids(networkCapabilities.mUids, this.mUids);
    }

    public boolean satisfiedByUids(NetworkCapabilities networkCapabilities) {
        ArraySet<UidRange> arraySet;
        if (!(networkCapabilities.mUids == null || (arraySet = this.mUids) == null)) {
            Iterator<UidRange> it = arraySet.iterator();
            while (it.hasNext()) {
                UidRange next = it.next();
                if (next.contains(networkCapabilities.mOwnerUid)) {
                    return true;
                }
                if (!networkCapabilities.appliesToUidRange(next)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean appliesToUidRange(UidRange uidRange) {
        ArraySet<UidRange> arraySet = this.mUids;
        if (arraySet == null) {
            return true;
        }
        Iterator<UidRange> it = arraySet.iterator();
        while (it.hasNext()) {
            if (it.next().containsRange(uidRange)) {
                return true;
            }
        }
        return false;
    }

    public void setAllowedUids(Set<Integer> set) {
        if (set != this.mAllowedUids) {
            Objects.requireNonNull(set);
            this.mAllowedUids.clear();
            this.mAllowedUids.addAll(set);
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public Set<Integer> getAllowedUids() {
        return new ArraySet(this.mAllowedUids);
    }

    public ArraySet<Integer> getAllowedUidsNoCopy() {
        return this.mAllowedUids;
    }

    public boolean isUidWithAccess(int i) {
        return this.mAllowedUids.contains(Integer.valueOf(i));
    }

    public boolean hasAllowedUids() {
        return !this.mAllowedUids.isEmpty();
    }

    private boolean equalsAllowedUids(NetworkCapabilities networkCapabilities) {
        return this.mAllowedUids.equals(networkCapabilities.mAllowedUids);
    }

    public NetworkCapabilities setSSID(String str) {
        this.mSSID = str;
        return this;
    }

    @SystemApi
    public String getSsid() {
        return this.mSSID;
    }

    public boolean equalsSSID(NetworkCapabilities networkCapabilities) {
        return Objects.equals(this.mSSID, networkCapabilities.mSSID);
    }

    public boolean satisfiedBySSID(NetworkCapabilities networkCapabilities) {
        String str = this.mSSID;
        return str == null || str.equals(networkCapabilities.mSSID);
    }

    private boolean satisfiedByNetworkCapabilities(NetworkCapabilities networkCapabilities, boolean z) {
        return networkCapabilities != null && satisfiedByNetCapabilities(networkCapabilities, z) && satisfiedByTransportTypes(networkCapabilities) && (z || satisfiedByLinkBandwidths(networkCapabilities)) && satisfiedBySpecifier(networkCapabilities) && satisfiedByEnterpriseCapabilitiesId(networkCapabilities) && ((z || satisfiedBySignalStrength(networkCapabilities)) && ((z || satisfiedByUids(networkCapabilities)) && ((z || satisfiedBySSID(networkCapabilities)) && ((z || satisfiedByRequestor(networkCapabilities)) && (z || satisfiedBySubscriptionIds(networkCapabilities))))));
    }

    @SystemApi
    public boolean satisfiedByNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        return satisfiedByNetworkCapabilities(networkCapabilities, false);
    }

    public boolean satisfiedByImmutableNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        return satisfiedByNetworkCapabilities(networkCapabilities, true);
    }

    public String describeImmutableDifferences(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities == null) {
            return "other NetworkCapabilities was null";
        }
        StringJoiner stringJoiner = new StringJoiner(", ");
        long j = (~MUTABLE_CAPABILITIES) & -2049;
        long j2 = this.mNetworkCapabilities & j;
        long j3 = j & networkCapabilities.mNetworkCapabilities;
        if (j2 != j3) {
            stringJoiner.add(String.format("immutable capabilities changed: %s -> %s", capabilityNamesOf(NetworkCapabilitiesUtils.unpackBits(j2)), capabilityNamesOf(NetworkCapabilitiesUtils.unpackBits(j3))));
        }
        if (!equalsSpecifier(networkCapabilities)) {
            stringJoiner.add(String.format("specifier changed: %s -> %s", getNetworkSpecifier(), networkCapabilities.getNetworkSpecifier()));
        }
        if (!equalsTransportTypes(networkCapabilities)) {
            stringJoiner.add(String.format("transports changed: %s -> %s", transportNamesOf(getTransportTypes()), transportNamesOf(networkCapabilities.getTransportTypes())));
        }
        return stringJoiner.toString();
    }

    public boolean equalRequestableCapabilities(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities != null && equalsNetCapabilitiesRequestable(networkCapabilities) && equalsTransportTypes(networkCapabilities) && equalsSpecifier(networkCapabilities)) {
            return true;
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof NetworkCapabilities)) {
            return false;
        }
        NetworkCapabilities networkCapabilities = (NetworkCapabilities) obj;
        if (!equalsNetCapabilities(networkCapabilities) || !equalsTransportTypes(networkCapabilities) || !equalsLinkBandwidths(networkCapabilities) || !equalsSignalStrength(networkCapabilities) || !equalsSpecifier(networkCapabilities) || !equalsTransportInfo(networkCapabilities) || !equalsUids(networkCapabilities) || !equalsAllowedUids(networkCapabilities) || !equalsSSID(networkCapabilities) || !equalsOwnerUid(networkCapabilities) || !equalsPrivateDnsBroken(networkCapabilities) || !equalsRequestor(networkCapabilities) || !equalsAdministratorUids(networkCapabilities) || !equalsSubscriptionIds(networkCapabilities) || !equalsUnderlyingNetworks(networkCapabilities) || !equalsEnterpriseCapabilitiesId(networkCapabilities)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long j = this.mNetworkCapabilities;
        int i = ((int) (j & -1)) + (((int) (j >> 32)) * 3);
        long j2 = this.mForbiddenNetworkCapabilities;
        int i2 = i + (((int) (j2 & -1)) * 5) + (((int) (j2 >> 32)) * 7);
        long j3 = this.mTransportTypes;
        return i2 + (((int) (-1 & j3)) * 11) + (((int) (j3 >> 32)) * 13) + (this.mLinkUpBandwidthKbps * 17) + (this.mLinkDownBandwidthKbps * 19) + (Objects.hashCode(this.mNetworkSpecifier) * 23) + (this.mSignalStrength * 29) + (this.mOwnerUid * 31) + (Objects.hashCode(this.mUids) * 37) + (Objects.hashCode(this.mAllowedUids) * 41) + (Objects.hashCode(this.mSSID) * 43) + (Objects.hashCode(this.mTransportInfo) * 47) + (Objects.hashCode(Boolean.valueOf(this.mPrivateDnsBroken)) * 53) + (Objects.hashCode(Integer.valueOf(this.mRequestorUid)) * 59) + (Objects.hashCode(this.mRequestorPackageName) * 61) + (Arrays.hashCode(this.mAdministratorUids) * 67) + (Objects.hashCode(this.mSubIds) * 71) + (Objects.hashCode(this.mUnderlyingNetworks) * 73) + (this.mEnterpriseId * 79);
    }

    private <T extends Parcelable> void writeParcelableArraySet(Parcel parcel, ArraySet<T> arraySet, int i) {
        int size = arraySet != null ? arraySet.size() : -1;
        parcel.writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            parcel.writeParcelable((Parcelable) arraySet.valueAt(i2), i);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mNetworkCapabilities);
        parcel.writeLong(this.mForbiddenNetworkCapabilities);
        parcel.writeLong(this.mTransportTypes);
        parcel.writeInt(this.mLinkUpBandwidthKbps);
        parcel.writeInt(this.mLinkDownBandwidthKbps);
        parcel.writeParcelable((Parcelable) this.mNetworkSpecifier, i);
        parcel.writeParcelable((Parcelable) this.mTransportInfo, i);
        parcel.writeInt(this.mSignalStrength);
        writeParcelableArraySet(parcel, this.mUids, i);
        parcel.writeIntArray(CollectionUtils.toIntArray(this.mAllowedUids));
        parcel.writeString(this.mSSID);
        parcel.writeBoolean(this.mPrivateDnsBroken);
        parcel.writeIntArray(getAdministratorUids());
        parcel.writeInt(this.mOwnerUid);
        parcel.writeInt(this.mRequestorUid);
        parcel.writeString(this.mRequestorPackageName);
        parcel.writeIntArray(CollectionUtils.toIntArray(this.mSubIds));
        parcel.writeTypedList(this.mUnderlyingNetworks);
        parcel.writeInt(this.mEnterpriseId);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        if (0 != this.mTransportTypes) {
            sb.append(" Transports: ");
            appendStringRepresentationOfBitMaskToStringBuilder(sb, this.mTransportTypes, new NetworkCapabilities$$ExternalSyntheticLambda0(), "|");
        }
        if (0 != this.mNetworkCapabilities) {
            sb.append(" Capabilities: ");
            appendStringRepresentationOfBitMaskToStringBuilder(sb, this.mNetworkCapabilities, new NetworkCapabilities$$ExternalSyntheticLambda1(), "&");
        }
        if (0 != this.mForbiddenNetworkCapabilities) {
            sb.append(" Forbidden: ");
            appendStringRepresentationOfBitMaskToStringBuilder(sb, this.mForbiddenNetworkCapabilities, new NetworkCapabilities$$ExternalSyntheticLambda1(), "&");
        }
        if (this.mLinkUpBandwidthKbps > 0) {
            sb.append(" LinkUpBandwidth>=");
            sb.append(this.mLinkUpBandwidthKbps);
            sb.append("Kbps");
        }
        if (this.mLinkDownBandwidthKbps > 0) {
            sb.append(" LinkDnBandwidth>=");
            sb.append(this.mLinkDownBandwidthKbps);
            sb.append("Kbps");
        }
        if (this.mNetworkSpecifier != null) {
            sb.append(" Specifier: <");
            sb.append((Object) this.mNetworkSpecifier);
            sb.append(">");
        }
        if (this.mTransportInfo != null) {
            sb.append(" TransportInfo: <");
            sb.append((Object) this.mTransportInfo);
            sb.append(">");
        }
        if (hasSignalStrength()) {
            sb.append(" SignalStrength: ");
            sb.append(this.mSignalStrength);
        }
        ArraySet<UidRange> arraySet = this.mUids;
        if (arraySet != null) {
            if (1 == arraySet.size() && this.mUids.valueAt(0).count() == 1) {
                sb.append(" Uid: ");
                sb.append(this.mUids.valueAt(0).start);
            } else {
                sb.append(" Uids: <");
                sb.append((Object) this.mUids);
                sb.append(">");
            }
        }
        if (hasAllowedUids()) {
            sb.append(" AllowedUids: <");
            sb.append((Object) this.mAllowedUids);
            sb.append(">");
        }
        if (this.mOwnerUid != -1) {
            sb.append(" OwnerUid: ");
            sb.append(this.mOwnerUid);
        }
        int[] iArr = this.mAdministratorUids;
        if (!(iArr == null || iArr.length == 0)) {
            sb.append(" AdminUids: ");
            sb.append(Arrays.toString(this.mAdministratorUids));
        }
        if (this.mRequestorUid != -1) {
            sb.append(" RequestorUid: ");
            sb.append(this.mRequestorUid);
        }
        if (this.mRequestorPackageName != null) {
            sb.append(" RequestorPkg: ");
            sb.append(this.mRequestorPackageName);
        }
        if (this.mSSID != null) {
            sb.append(" SSID: ");
            sb.append(this.mSSID);
        }
        if (this.mPrivateDnsBroken) {
            sb.append(" PrivateDnsBroken");
        }
        if (!this.mSubIds.isEmpty()) {
            sb.append(" SubscriptionIds: ");
            sb.append((Object) this.mSubIds);
        }
        if (this.mEnterpriseId != 0) {
            sb.append(" EnterpriseId: ");
            appendStringRepresentationOfBitMaskToStringBuilder(sb, (long) this.mEnterpriseId, new NetworkCapabilities$$ExternalSyntheticLambda2(), "&");
        }
        sb.append(" UnderlyingNetworks: ");
        if (this.mUnderlyingNetworks != null) {
            sb.append(NavigationBarInflaterView.SIZE_MOD_START);
            StringJoiner stringJoiner = new StringJoiner(NavigationBarInflaterView.BUTTON_SEPARATOR);
            for (int i = 0; i < this.mUnderlyingNetworks.size(); i++) {
                stringJoiner.add(this.mUnderlyingNetworks.get(i).toString());
            }
            sb.append(stringJoiner.toString());
            sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        } else {
            sb.append("Null");
        }
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }

    public static void appendStringRepresentationOfBitMaskToStringBuilder(StringBuilder sb, long j, NameOf nameOf, String str) {
        boolean z = false;
        int i = 0;
        while (j != 0) {
            if ((1 & j) != 0) {
                if (z) {
                    sb.append(str);
                } else {
                    z = true;
                }
                sb.append(nameOf.nameOf(i));
            }
            j >>= 1;
            i++;
        }
    }

    public static String capabilityNamesOf(int[] iArr) {
        StringJoiner stringJoiner = new StringJoiner("|");
        if (iArr != null) {
            for (int capabilityNameOf : iArr) {
                stringJoiner.add(capabilityNameOf(capabilityNameOf));
            }
        }
        return stringJoiner.toString();
    }

    public static String capabilityNameOf(int i) {
        switch (i) {
            case 0:
                return "MMS";
            case 1:
                return "SUPL";
            case 2:
                return "DUN";
            case 3:
                return "FOTA";
            case 4:
                return "IMS";
            case 5:
                return "CBS";
            case 6:
                return "WIFI_P2P";
            case 7:
                return "IA";
            case 8:
                return "RCS";
            case 9:
                return "XCAP";
            case 10:
                return "EIMS";
            case 11:
                return "NOT_METERED";
            case 12:
                return "INTERNET";
            case 13:
                return "NOT_RESTRICTED";
            case 14:
                return "TRUSTED";
            case 15:
                return "NOT_VPN";
            case 16:
                return "VALIDATED";
            case 17:
                return "CAPTIVE_PORTAL";
            case 18:
                return "NOT_ROAMING";
            case 19:
                return "FOREGROUND";
            case 20:
                return "NOT_CONGESTED";
            case 21:
                return "NOT_SUSPENDED";
            case 22:
                return "OEM_PAID";
            case 23:
                return "MCX";
            case 24:
                return "PARTIAL_CONNECTIVITY";
            case 25:
                return "TEMPORARILY_NOT_METERED";
            case 26:
                return "OEM_PRIVATE";
            case 27:
                return "VEHICLE_INTERNAL";
            case 28:
                return "NOT_VCN_MANAGED";
            case 29:
                return "ENTERPRISE";
            case 30:
                return "VSIM";
            case 31:
                return "BIP";
            case 32:
                return "HEAD_UNIT";
            case 33:
                return "MMTEL";
            case 34:
                return "PRIORITIZE_LATENCY";
            case 35:
                return "PRIORITIZE_BANDWIDTH";
            default:
                return Integer.toString(i);
        }
    }

    /* access modifiers changed from: private */
    public static String enterpriseIdNameOf(int i) {
        return Integer.toString(i);
    }

    public static String transportNamesOf(int[] iArr) {
        StringJoiner stringJoiner = new StringJoiner("|");
        if (iArr != null) {
            for (int transportNameOf : iArr) {
                stringJoiner.add(transportNameOf(transportNameOf));
            }
        }
        return stringJoiner.toString();
    }

    public static String transportNameOf(int i) {
        if (!isValidTransport(i)) {
            return "UNKNOWN";
        }
        return TRANSPORT_NAMES[i];
    }

    /* access modifiers changed from: private */
    public static void checkValidTransportType(int i) {
        if (!isValidTransport(i)) {
            throw new IllegalArgumentException("Invalid TransportType " + i);
        }
    }

    private static void checkValidCapability(int i) {
        if (!isValidCapability(i)) {
            throw new IllegalArgumentException("NetworkCapability " + i + " out of range");
        }
    }

    private static void checkValidEnterpriseId(int i) {
        if (!isValidEnterpriseId(i)) {
            throw new IllegalArgumentException("enterprise capability identifier " + i + " is out of range");
        }
    }

    public boolean isMetered() {
        return !hasCapability(11);
    }

    @SystemApi
    public boolean isPrivateDnsBroken() {
        return this.mPrivateDnsBroken;
    }

    public void setPrivateDnsBroken(boolean z) {
        this.mPrivateDnsBroken = z;
    }

    private boolean equalsPrivateDnsBroken(NetworkCapabilities networkCapabilities) {
        return this.mPrivateDnsBroken == networkCapabilities.mPrivateDnsBroken;
    }

    public NetworkCapabilities setRequestorUid(int i) {
        this.mRequestorUid = i;
        return this;
    }

    public int getRequestorUid() {
        return this.mRequestorUid;
    }

    public NetworkCapabilities setRequestorPackageName(String str) {
        this.mRequestorPackageName = str;
        return this;
    }

    public String getRequestorPackageName() {
        return this.mRequestorPackageName;
    }

    public NetworkCapabilities setRequestorUidAndPackageName(int i, String str) {
        return setRequestorUid(i).setRequestorPackageName(str);
    }

    private boolean satisfiedByRequestor(NetworkCapabilities networkCapabilities) {
        int i;
        String str;
        int i2 = this.mRequestorUid;
        if (!(i2 == -1 || (i = networkCapabilities.mRequestorUid) == -1)) {
            if (i2 != i) {
                return false;
            }
            String str2 = networkCapabilities.mRequestorPackageName;
            if (!(str2 == null || (str = this.mRequestorPackageName) == null)) {
                return TextUtils.equals(str, str2);
            }
        }
        return true;
    }

    private boolean equalsRequestor(NetworkCapabilities networkCapabilities) {
        return this.mRequestorUid == networkCapabilities.mRequestorUid && TextUtils.equals(this.mRequestorPackageName, networkCapabilities.mRequestorPackageName);
    }

    public NetworkCapabilities setSubscriptionIds(Set<Integer> set) {
        this.mSubIds = new ArraySet<>((Collection) Objects.requireNonNull(set));
        return this;
    }

    @SystemApi
    public Set<Integer> getSubscriptionIds() {
        return new ArraySet(this.mSubIds);
    }

    private boolean equalsSubscriptionIds(NetworkCapabilities networkCapabilities) {
        return Objects.equals(this.mSubIds, networkCapabilities.mSubIds);
    }

    private boolean satisfiedBySubscriptionIds(NetworkCapabilities networkCapabilities) {
        if (this.mSubIds.isEmpty()) {
            return true;
        }
        if (networkCapabilities.mSubIds.isEmpty()) {
            return false;
        }
        Iterator<Integer> it = networkCapabilities.mSubIds.iterator();
        while (it.hasNext()) {
            if (this.mSubIds.contains(it.next())) {
                return true;
            }
        }
        return false;
    }

    public long getApplicableRedactions() {
        TransportInfo transportInfo = this.mTransportInfo;
        if (transportInfo == null) {
            return 0;
        }
        return transportInfo.getApplicableRedactions();
    }

    /* access modifiers changed from: private */
    public NetworkCapabilities removeDefaultCapabilites() {
        this.mNetworkCapabilities &= ~DEFAULT_CAPABILITIES;
        return this;
    }

    @SystemApi
    public static final class Builder {
        private final NetworkCapabilities mCaps;

        public Builder() {
            this.mCaps = new NetworkCapabilities();
        }

        public Builder(NetworkCapabilities networkCapabilities) {
            Objects.requireNonNull(networkCapabilities);
            this.mCaps = new NetworkCapabilities(networkCapabilities);
        }

        public static Builder withoutDefaultCapabilities() {
            NetworkCapabilities networkCapabilities = new NetworkCapabilities();
            NetworkCapabilities unused = networkCapabilities.removeDefaultCapabilites();
            return new Builder(networkCapabilities);
        }

        public Builder addTransportType(int i) {
            NetworkCapabilities.checkValidTransportType(i);
            this.mCaps.addTransportType(i);
            return this;
        }

        public Builder removeTransportType(int i) {
            NetworkCapabilities.checkValidTransportType(i);
            this.mCaps.removeTransportType(i);
            return this;
        }

        public Builder addCapability(int i) {
            this.mCaps.setCapability(i, true);
            return this;
        }

        public Builder removeCapability(int i) {
            this.mCaps.setCapability(i, false);
            return this;
        }

        public Builder addEnterpriseId(int i) {
            this.mCaps.addEnterpriseId(i);
            return this;
        }

        public Builder removeEnterpriseId(int i) {
            NetworkCapabilities unused = this.mCaps.removeEnterpriseId(i);
            return this;
        }

        public Builder setOwnerUid(int i) {
            this.mCaps.setOwnerUid(i);
            return this;
        }

        public Builder setAdministratorUids(int[] iArr) {
            Objects.requireNonNull(iArr);
            this.mCaps.setAdministratorUids(iArr);
            return this;
        }

        public Builder setLinkUpstreamBandwidthKbps(int i) {
            this.mCaps.setLinkUpstreamBandwidthKbps(i);
            return this;
        }

        public Builder setLinkDownstreamBandwidthKbps(int i) {
            this.mCaps.setLinkDownstreamBandwidthKbps(i);
            return this;
        }

        public Builder setNetworkSpecifier(NetworkSpecifier networkSpecifier) {
            this.mCaps.setNetworkSpecifier(networkSpecifier);
            return this;
        }

        public Builder setTransportInfo(TransportInfo transportInfo) {
            this.mCaps.setTransportInfo(transportInfo);
            return this;
        }

        public Builder setSignalStrength(int i) {
            this.mCaps.setSignalStrength(i);
            return this;
        }

        public Builder setSsid(String str) {
            this.mCaps.setSSID(str);
            return this;
        }

        public Builder setRequestorUid(int i) {
            this.mCaps.setRequestorUid(i);
            return this;
        }

        public Builder setRequestorPackageName(String str) {
            this.mCaps.setRequestorPackageName(str);
            return this;
        }

        @SystemApi
        public Builder setSubscriptionIds(Set<Integer> set) {
            this.mCaps.setSubscriptionIds(set);
            return this;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public Builder setUids(Set<Range<Integer>> set) {
            this.mCaps.setUids(set);
            return this;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public Builder setAllowedUids(Set<Integer> set) {
            Objects.requireNonNull(set);
            this.mCaps.setAllowedUids(set);
            return this;
        }

        public Builder setUnderlyingNetworks(List<Network> list) {
            this.mCaps.setUnderlyingNetworks(list);
            return this;
        }

        public NetworkCapabilities build() {
            if (this.mCaps.getOwnerUid() != -1 && !CollectionUtils.contains(this.mCaps.getAdministratorUids(), this.mCaps.getOwnerUid())) {
                throw new IllegalStateException("The owner UID must be included in  administrator UIDs.");
            } else if (this.mCaps.getEnterpriseIds().length == 0 || this.mCaps.hasCapability(29)) {
                return new NetworkCapabilities(this.mCaps);
            } else {
                throw new IllegalStateException("Enterprise capability identifier is applicable only with ENTERPRISE capability.");
            }
        }
    }
}
