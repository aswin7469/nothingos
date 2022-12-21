package android.net;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.net.module.util.NetworkIdentityUtils;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class NetworkStateSnapshot implements Parcelable {
    public static final Parcelable.Creator<NetworkStateSnapshot> CREATOR = new Parcelable.Creator<NetworkStateSnapshot>() {
        public NetworkStateSnapshot createFromParcel(Parcel parcel) {
            return new NetworkStateSnapshot(parcel);
        }

        public NetworkStateSnapshot[] newArray(int i) {
            return new NetworkStateSnapshot[i];
        }
    };
    private final int mLegacyType;
    private final LinkProperties mLinkProperties;
    private final Network mNetwork;
    private final NetworkCapabilities mNetworkCapabilities;
    private final String mSubscriberId;

    public int describeContents() {
        return 0;
    }

    public NetworkStateSnapshot(Network network, NetworkCapabilities networkCapabilities, LinkProperties linkProperties, String str, int i) {
        this.mNetwork = (Network) Objects.requireNonNull(network);
        this.mNetworkCapabilities = (NetworkCapabilities) Objects.requireNonNull(networkCapabilities);
        this.mLinkProperties = (LinkProperties) Objects.requireNonNull(linkProperties);
        this.mSubscriberId = str;
        this.mLegacyType = i;
    }

    public NetworkStateSnapshot(Parcel parcel) {
        this.mNetwork = (Network) parcel.readParcelable((ClassLoader) null, Network.class);
        this.mNetworkCapabilities = (NetworkCapabilities) parcel.readParcelable((ClassLoader) null, NetworkCapabilities.class);
        this.mLinkProperties = (LinkProperties) parcel.readParcelable((ClassLoader) null, LinkProperties.class);
        this.mSubscriberId = parcel.readString();
        this.mLegacyType = parcel.readInt();
    }

    public Network getNetwork() {
        return this.mNetwork;
    }

    public NetworkCapabilities getNetworkCapabilities() {
        return this.mNetworkCapabilities;
    }

    public LinkProperties getLinkProperties() {
        return this.mLinkProperties;
    }

    @Deprecated
    public String getSubscriberId() {
        return this.mSubscriberId;
    }

    public int getSubId() {
        if (!this.mNetworkCapabilities.hasTransport(0)) {
            return -1;
        }
        TelephonyNetworkSpecifier networkSpecifier = this.mNetworkCapabilities.getNetworkSpecifier();
        if (networkSpecifier instanceof TelephonyNetworkSpecifier) {
            return networkSpecifier.getSubscriptionId();
        }
        return -1;
    }

    public int getLegacyType() {
        return this.mLegacyType;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mNetwork, i);
        parcel.writeParcelable(this.mNetworkCapabilities, i);
        parcel.writeParcelable(this.mLinkProperties, i);
        parcel.writeString(this.mSubscriberId);
        parcel.writeInt(this.mLegacyType);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NetworkStateSnapshot)) {
            return false;
        }
        NetworkStateSnapshot networkStateSnapshot = (NetworkStateSnapshot) obj;
        if (this.mLegacyType != networkStateSnapshot.mLegacyType || !Objects.equals(this.mNetwork, networkStateSnapshot.mNetwork) || !Objects.equals(this.mNetworkCapabilities, networkStateSnapshot.mNetworkCapabilities) || !Objects.equals(this.mLinkProperties, networkStateSnapshot.mLinkProperties) || !Objects.equals(this.mSubscriberId, networkStateSnapshot.mSubscriberId)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mNetwork, this.mNetworkCapabilities, this.mLinkProperties, this.mSubscriberId, Integer.valueOf(this.mLegacyType));
    }

    public String toString() {
        return "NetworkStateSnapshot{network=" + this.mNetwork + ", networkCapabilities=" + this.mNetworkCapabilities + ", linkProperties=" + this.mLinkProperties + ", subscriberId='" + NetworkIdentityUtils.scrubSubscriberId(this.mSubscriberId) + "', legacyType=" + this.mLegacyType + '}';
    }
}
