package android.net;

import android.annotation.SystemApi;
import android.net.TelephonyNetworkSpecifier;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.text.TextUtils;
import android.util.Range;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NetworkRequest implements Parcelable {
    public static final Parcelable.Creator<NetworkRequest> CREATOR = new Parcelable.Creator<NetworkRequest>() {
        public NetworkRequest createFromParcel(Parcel parcel) {
            return new NetworkRequest(NetworkCapabilities.CREATOR.createFromParcel(parcel), parcel.readInt(), parcel.readInt(), Type.valueOf(parcel.readString()));
        }

        public NetworkRequest[] newArray(int i) {
            return new NetworkRequest[i];
        }
    };
    public static final int FIRST_REQUEST_ID = 1;
    public static final int REQUEST_ID_NONE = -1;
    public final int legacyType;
    public final NetworkCapabilities networkCapabilities;
    public final int requestId;
    public final Type type;

    public enum Type {
        NONE,
        LISTEN,
        TRACK_DEFAULT,
        REQUEST,
        BACKGROUND_REQUEST,
        TRACK_SYSTEM_DEFAULT,
        LISTEN_FOR_BEST
    }

    public int describeContents() {
        return 0;
    }

    public NetworkRequest(NetworkCapabilities networkCapabilities2, int i, int i2, Type type2) {
        networkCapabilities2.getClass();
        this.requestId = i2;
        this.networkCapabilities = networkCapabilities2;
        this.legacyType = i;
        this.type = type2;
    }

    public NetworkRequest(NetworkRequest networkRequest) {
        this.networkCapabilities = new NetworkCapabilities(networkRequest.networkCapabilities);
        this.requestId = networkRequest.requestId;
        this.legacyType = networkRequest.legacyType;
        this.type = networkRequest.type;
    }

    public static class Builder {
        private static final List<Integer> VCN_SUPPORTED_CAPABILITIES = Arrays.asList(17, 2, 19, 12, 20, 11, 13, 18, 21, 15, 24, 25, 14, 16);
        private final NetworkCapabilities mNetworkCapabilities;
        private boolean mShouldDeduceNotVcnManaged = true;

        public Builder() {
            NetworkCapabilities networkCapabilities = new NetworkCapabilities();
            this.mNetworkCapabilities = networkCapabilities;
            networkCapabilities.setSingleUid(Process.myUid());
        }

        public Builder(NetworkRequest networkRequest) {
            Objects.requireNonNull(networkRequest);
            this.mNetworkCapabilities = networkRequest.networkCapabilities;
            this.mShouldDeduceNotVcnManaged = false;
        }

        public NetworkRequest build() {
            NetworkCapabilities networkCapabilities = new NetworkCapabilities(this.mNetworkCapabilities);
            networkCapabilities.maybeMarkCapabilitiesRestricted();
            deduceNotVcnManagedCapability(networkCapabilities);
            return new NetworkRequest(networkCapabilities, -1, 0, Type.NONE);
        }

        public Builder addCapability(int i) {
            this.mNetworkCapabilities.addCapability(i);
            if (i == 28) {
                this.mShouldDeduceNotVcnManaged = false;
            }
            return this;
        }

        public Builder removeCapability(int i) {
            this.mNetworkCapabilities.removeCapability(i);
            if (i == 28) {
                this.mShouldDeduceNotVcnManaged = false;
            }
            return this;
        }

        public Builder setCapabilities(NetworkCapabilities networkCapabilities) {
            this.mNetworkCapabilities.set(networkCapabilities);
            return this;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public Builder setUids(Set<Range<Integer>> set) {
            this.mNetworkCapabilities.setUids(set);
            return this;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public Builder addForbiddenCapability(int i) {
            this.mNetworkCapabilities.addForbiddenCapability(i);
            return this;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public Builder removeForbiddenCapability(int i) {
            this.mNetworkCapabilities.removeForbiddenCapability(i);
            return this;
        }

        public Builder clearCapabilities() {
            this.mNetworkCapabilities.clearAll();
            this.mShouldDeduceNotVcnManaged = false;
            return this;
        }

        public Builder addTransportType(int i) {
            this.mNetworkCapabilities.addTransportType(i);
            return this;
        }

        public Builder removeTransportType(int i) {
            this.mNetworkCapabilities.removeTransportType(i);
            return this;
        }

        public Builder setLinkUpstreamBandwidthKbps(int i) {
            this.mNetworkCapabilities.setLinkUpstreamBandwidthKbps(i);
            return this;
        }

        public Builder setLinkDownstreamBandwidthKbps(int i) {
            this.mNetworkCapabilities.setLinkDownstreamBandwidthKbps(i);
            return this;
        }

        @Deprecated
        public Builder setNetworkSpecifier(String str) {
            try {
                return setNetworkSpecifier((NetworkSpecifier) new TelephonyNetworkSpecifier.Builder().setSubscriptionId(Integer.parseInt(str)).build());
            } catch (NumberFormatException unused) {
                if (!TextUtils.isEmpty(str)) {
                    return this.mNetworkCapabilities.hasTransport(7) ? setNetworkSpecifier((NetworkSpecifier) new TestNetworkSpecifier(str)) : setNetworkSpecifier((NetworkSpecifier) new EthernetNetworkSpecifier(str));
                }
                NetworkSpecifier networkSpecifier = null;
                return setNetworkSpecifier((NetworkSpecifier) null);
            }
        }

        public Builder setNetworkSpecifier(NetworkSpecifier networkSpecifier) {
            if (!(networkSpecifier instanceof MatchAllNetworkSpecifier)) {
                this.mNetworkCapabilities.setNetworkSpecifier(networkSpecifier);
                this.mShouldDeduceNotVcnManaged = false;
                return this;
            }
            throw new IllegalArgumentException("A MatchAllNetworkSpecifier is not permitted");
        }

        @SystemApi
        public Builder setSignalStrength(int i) {
            this.mNetworkCapabilities.setSignalStrength(i);
            return this;
        }

        private void deduceNotVcnManagedCapability(NetworkCapabilities networkCapabilities) {
            if (this.mShouldDeduceNotVcnManaged) {
                int[] capabilities = networkCapabilities.getCapabilities();
                int length = capabilities.length;
                int i = 0;
                while (i < length) {
                    if (VCN_SUPPORTED_CAPABILITIES.contains(Integer.valueOf(capabilities[i]))) {
                        i++;
                    } else {
                        return;
                    }
                }
                networkCapabilities.addCapability(28);
            }
        }

        @SystemApi
        public Builder setSubscriptionIds(Set<Integer> set) {
            this.mNetworkCapabilities.setSubscriptionIds(set);
            return this;
        }

        public Builder setIncludeOtherUidNetworks(boolean z) {
            if (z) {
                this.mNetworkCapabilities.setUids((Set<Range<Integer>>) null);
            } else {
                this.mNetworkCapabilities.setSingleUid(Process.myUid());
            }
            return this;
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        this.networkCapabilities.writeToParcel(parcel, i);
        parcel.writeInt(this.legacyType);
        parcel.writeInt(this.requestId);
        parcel.writeString(this.type.name());
    }

    public boolean isListen() {
        return this.type == Type.LISTEN;
    }

    public boolean isListenForBest() {
        return this.type == Type.LISTEN_FOR_BEST;
    }

    public boolean isRequest() {
        return this.type == Type.REQUEST || this.type == Type.BACKGROUND_REQUEST;
    }

    public boolean isBackgroundRequest() {
        return this.type == Type.BACKGROUND_REQUEST;
    }

    public boolean hasCapability(int i) {
        return this.networkCapabilities.hasCapability(i);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public boolean hasForbiddenCapability(int i) {
        return this.networkCapabilities.hasForbiddenCapability(i);
    }

    public boolean canBeSatisfiedBy(NetworkCapabilities networkCapabilities2) {
        return this.networkCapabilities.satisfiedByNetworkCapabilities(networkCapabilities2);
    }

    public boolean hasTransport(int i) {
        return this.networkCapabilities.hasTransport(i);
    }

    public NetworkSpecifier getNetworkSpecifier() {
        return this.networkCapabilities.getNetworkSpecifier();
    }

    @SystemApi
    public int getRequestorUid() {
        return this.networkCapabilities.getRequestorUid();
    }

    @SystemApi
    public String getRequestorPackageName() {
        return this.networkCapabilities.getRequestorPackageName();
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder("NetworkRequest [ ");
        sb.append((Object) this.type);
        sb.append(" id=");
        sb.append(this.requestId);
        if (this.legacyType != -1) {
            str = ", legacyType=" + this.legacyType;
        } else {
            str = "";
        }
        sb.append(str);
        sb.append(", ");
        sb.append(this.networkCapabilities.toString());
        sb.append(" ]");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NetworkRequest)) {
            return false;
        }
        NetworkRequest networkRequest = (NetworkRequest) obj;
        if (networkRequest.legacyType == this.legacyType && networkRequest.requestId == this.requestId && networkRequest.type == this.type && Objects.equals(networkRequest.networkCapabilities, this.networkCapabilities)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.requestId), Integer.valueOf(this.legacyType), this.networkCapabilities, this.type);
    }

    public int[] getCapabilities() {
        return this.networkCapabilities.getCapabilities();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public int[] getEnterpriseIds() {
        return this.networkCapabilities.getEnterpriseIds();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public boolean hasEnterpriseId(int i) {
        return this.networkCapabilities.hasEnterpriseId(i);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public int[] getForbiddenCapabilities() {
        return this.networkCapabilities.getForbiddenCapabilities();
    }

    public int[] getTransportTypes() {
        return this.networkCapabilities.getTransportTypes();
    }
}
