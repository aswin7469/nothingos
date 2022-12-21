package android.net;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.connectivity.android.service.NetworkIdentityProto;
import android.net.connectivity.com.android.net.module.util.CollectionUtils;
import android.net.connectivity.com.android.net.module.util.NetworkCapabilitiesUtils;
import android.net.connectivity.com.android.net.module.util.NetworkIdentityUtils;
import android.net.wifi.WifiInfo;
import android.telephony.TelephonyManager;
import android.util.proto.ProtoOutputStream;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class NetworkIdentity {
    public static final int OEM_NONE = 0;
    public static final int OEM_PAID = 1;
    public static final int OEM_PRIVATE = 2;
    public static final int SUBTYPE_COMBINED = -1;
    private static final long SUPPORTED_OEM_MANAGED_TYPES = 3;
    private static final String TAG = "NetworkIdentity";
    final boolean mDefaultNetwork;
    final boolean mMetered;
    final int mOemManaged;
    final int mRatType;
    final boolean mRoaming;
    final int mSubId;
    final String mSubscriberId;
    final int mType;
    final String mWifiNetworkKey;

    @Retention(RetentionPolicy.SOURCE)
    public @interface OemManaged {
    }

    public NetworkIdentity(int i, int i2, String str, String str2, boolean z, boolean z2, boolean z3, int i3, int i4) {
        this.mType = i;
        this.mRatType = i2;
        this.mSubscriberId = str;
        this.mWifiNetworkKey = str2;
        this.mRoaming = z;
        this.mMetered = z2;
        this.mDefaultNetwork = z3;
        this.mOemManaged = i3;
        this.mSubId = i4;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mType), Integer.valueOf(this.mRatType), this.mSubscriberId, this.mWifiNetworkKey, Boolean.valueOf(this.mRoaming), Boolean.valueOf(this.mMetered), Boolean.valueOf(this.mDefaultNetwork), Integer.valueOf(this.mOemManaged), Integer.valueOf(this.mSubId));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NetworkIdentity)) {
            return false;
        }
        NetworkIdentity networkIdentity = (NetworkIdentity) obj;
        if (this.mType == networkIdentity.mType && this.mRatType == networkIdentity.mRatType && this.mRoaming == networkIdentity.mRoaming && Objects.equals(this.mSubscriberId, networkIdentity.mSubscriberId) && Objects.equals(this.mWifiNetworkKey, networkIdentity.mWifiNetworkKey) && this.mMetered == networkIdentity.mMetered && this.mDefaultNetwork == networkIdentity.mDefaultNetwork && this.mOemManaged == networkIdentity.mOemManaged && this.mSubId == networkIdentity.mSubId) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{type=");
        sb.append(this.mType);
        sb.append(", ratType=");
        int i = this.mRatType;
        if (i == -1) {
            sb.append("COMBINED");
        } else {
            sb.append(i);
        }
        if (this.mSubscriberId != null) {
            sb.append(", subscriberId=");
            sb.append(NetworkIdentityUtils.scrubSubscriberId(this.mSubscriberId));
        }
        if (this.mWifiNetworkKey != null) {
            sb.append(", wifiNetworkKey=");
            sb.append(this.mWifiNetworkKey);
        }
        if (this.mRoaming) {
            sb.append(", ROAMING");
        }
        sb.append(", metered=");
        sb.append(this.mMetered);
        sb.append(", defaultNetwork=");
        sb.append(this.mDefaultNetwork);
        sb.append(", oemManaged=");
        sb.append(getOemManagedNames(this.mOemManaged));
        sb.append(", subId=");
        sb.append(this.mSubId);
        sb.append("}");
        return sb.toString();
    }

    static String getOemManagedNames(int i) {
        if (i == 0) {
            return "OEM_NONE";
        }
        int[] unpackBits = NetworkCapabilitiesUtils.unpackBits((long) i);
        ArrayList arrayList = new ArrayList();
        for (int i2 : unpackBits) {
            arrayList.add(nameOfOemManaged(1 << i2));
        }
        return String.join((CharSequence) NavigationBarInflaterView.BUTTON_SEPARATOR, (Iterable<? extends CharSequence>) arrayList);
    }

    private static String nameOfOemManaged(int i) {
        if (i == 1) {
            return "OEM_PAID";
        }
        if (i == 2) {
            return "OEM_PRIVATE";
        }
        return "Invalid(" + i + NavigationBarInflaterView.KEY_CODE_END;
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(NetworkIdentityProto.TYPE, this.mType);
        protoOutputStream.write(NetworkIdentityProto.ROAMING, this.mRoaming);
        protoOutputStream.write(NetworkIdentityProto.METERED, this.mMetered);
        protoOutputStream.write(NetworkIdentityProto.DEFAULT_NETWORK, this.mDefaultNetwork);
        protoOutputStream.write(NetworkIdentityProto.OEM_MANAGED_NETWORK, this.mOemManaged);
        protoOutputStream.end(start);
    }

    public int getType() {
        return this.mType;
    }

    public int getRatType() {
        return this.mRatType;
    }

    public String getSubscriberId() {
        return this.mSubscriberId;
    }

    public String getWifiNetworkKey() {
        return this.mWifiNetworkKey;
    }

    public boolean getRoaming() {
        return this.mRoaming;
    }

    public boolean isRoaming() {
        return this.mRoaming;
    }

    public boolean getMetered() {
        return this.mMetered;
    }

    public boolean isMetered() {
        return this.mMetered;
    }

    public boolean getDefaultNetwork() {
        return this.mDefaultNetwork;
    }

    public boolean isDefaultNetwork() {
        return this.mDefaultNetwork;
    }

    public int getOemManaged() {
        return this.mOemManaged;
    }

    public int getSubId() {
        return this.mSubId;
    }

    @Deprecated
    public static NetworkIdentity buildNetworkIdentity(Context context, NetworkStateSnapshot networkStateSnapshot, boolean z, int i) {
        Builder subId = new Builder().setNetworkStateSnapshot(networkStateSnapshot).setDefaultNetwork(z).setSubId(networkStateSnapshot.getSubId());
        if (networkStateSnapshot.getLegacyType() == 0 && i != -1) {
            subId.setRatType(i);
        }
        return subId.build();
    }

    public static int getOemBitfield(NetworkCapabilities networkCapabilities) {
        boolean hasCapability = networkCapabilities.hasCapability(22);
        return networkCapabilities.hasCapability(26) ? hasCapability | true ? 1 : 0 : hasCapability ? 1 : 0;
    }

    public static int compare(NetworkIdentity networkIdentity, NetworkIdentity networkIdentity2) {
        String str;
        String str2;
        String str3;
        String str4;
        Objects.requireNonNull(networkIdentity2);
        int compare = Integer.compare(networkIdentity.mType, networkIdentity2.mType);
        if (compare == 0) {
            compare = Integer.compare(networkIdentity.mRatType, networkIdentity2.mRatType);
        }
        if (!(compare != 0 || (str3 = networkIdentity.mSubscriberId) == null || (str4 = networkIdentity2.mSubscriberId) == null)) {
            compare = str3.compareTo(str4);
        }
        if (!(compare != 0 || (str = networkIdentity.mWifiNetworkKey) == null || (str2 = networkIdentity2.mWifiNetworkKey) == null)) {
            compare = str.compareTo(str2);
        }
        if (compare == 0) {
            compare = Boolean.compare(networkIdentity.mRoaming, networkIdentity2.mRoaming);
        }
        if (compare == 0) {
            compare = Boolean.compare(networkIdentity.mMetered, networkIdentity2.mMetered);
        }
        if (compare == 0) {
            compare = Boolean.compare(networkIdentity.mDefaultNetwork, networkIdentity2.mDefaultNetwork);
        }
        if (compare == 0) {
            compare = Integer.compare(networkIdentity.mOemManaged, networkIdentity2.mOemManaged);
        }
        return compare == 0 ? Integer.compare(networkIdentity.mSubId, networkIdentity2.mSubId) : compare;
    }

    public static final class Builder {
        private static final int MAX_NETWORK_TYPE = 18;
        private static final int MIN_NETWORK_TYPE = 0;
        private boolean mDefaultNetwork = false;
        private boolean mMetered = false;
        private int mOemManaged = 0;
        private int mRatType = -1;
        private boolean mRoaming = false;
        private int mSubId = -1;
        private String mSubscriberId = null;
        private int mType = -1;
        private String mWifiNetworkKey = null;

        public Builder setNetworkStateSnapshot(NetworkStateSnapshot networkStateSnapshot) {
            setType(networkStateSnapshot.getLegacyType());
            setSubscriberId(networkStateSnapshot.getSubscriberId());
            setRoaming(!networkStateSnapshot.getNetworkCapabilities().hasCapability(18));
            setMetered(!networkStateSnapshot.getNetworkCapabilities().hasCapability(11) && !networkStateSnapshot.getNetworkCapabilities().hasCapability(25));
            setOemManaged(NetworkIdentity.getOemBitfield(networkStateSnapshot.getNetworkCapabilities()));
            if (this.mType == 1) {
                TransportInfo transportInfo = networkStateSnapshot.getNetworkCapabilities().getTransportInfo();
                if (transportInfo instanceof WifiInfo) {
                    setWifiNetworkKey(((WifiInfo) transportInfo).getNetworkKey());
                }
            }
            return this;
        }

        public Builder setType(int i) {
            if ((i < 0 || 18 < i) && i != -1) {
                throw new IllegalArgumentException("Invalid network type: " + i);
            }
            this.mType = i;
            return this;
        }

        public Builder setRatType(int i) {
            if (CollectionUtils.contains(TelephonyManager.getAllNetworkTypes(), i) || i == 0 || i == -2) {
                this.mRatType = i;
                return this;
            }
            throw new IllegalArgumentException("Invalid ratType " + i);
        }

        public Builder clearRatType() {
            this.mRatType = -1;
            return this;
        }

        public Builder setSubscriberId(String str) {
            this.mSubscriberId = str;
            return this;
        }

        public Builder setWifiNetworkKey(String str) {
            this.mWifiNetworkKey = str;
            return this;
        }

        public Builder setRoaming(boolean z) {
            this.mRoaming = z;
            return this;
        }

        public Builder setMetered(boolean z) {
            this.mMetered = z;
            return this;
        }

        public Builder setDefaultNetwork(boolean z) {
            this.mDefaultNetwork = z;
            return this;
        }

        public Builder setOemManaged(int i) {
            if ((-4 & ((long) i)) == 0) {
                this.mOemManaged = i;
                return this;
            }
            throw new IllegalArgumentException("Invalid value for OemManaged : " + i);
        }

        public Builder setSubId(int i) {
            this.mSubId = i;
            return this;
        }

        private void ensureValidParameters() {
            int i = this.mType;
            if (i != 0 && this.mRatType != -1) {
                throw new IllegalArgumentException("Invalid ratType " + this.mRatType + " for type " + this.mType);
            } else if (i != 1 && this.mWifiNetworkKey != null) {
                throw new IllegalArgumentException("Invalid wifi network key for type " + this.mType);
            }
        }

        public NetworkIdentity build() {
            ensureValidParameters();
            return new NetworkIdentity(this.mType, this.mRatType, this.mSubscriberId, this.mWifiNetworkKey, this.mRoaming, this.mMetered, this.mDefaultNetwork, this.mOemManaged, this.mSubId);
        }
    }
}
