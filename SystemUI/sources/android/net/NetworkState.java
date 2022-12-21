package android.net;

import android.os.Parcel;
import android.os.Parcelable;

public class NetworkState implements Parcelable {
    public static final Parcelable.Creator<NetworkState> CREATOR = new Parcelable.Creator<NetworkState>() {
        public NetworkState createFromParcel(Parcel parcel) {
            return new NetworkState(parcel);
        }

        public NetworkState[] newArray(int i) {
            return new NetworkState[i];
        }
    };
    public static final NetworkState EMPTY = new NetworkState();
    private static final boolean VALIDATE_ROAMING_STATE = false;
    public final int legacyNetworkType;
    public final LinkProperties linkProperties;
    public final Network network;
    public final NetworkCapabilities networkCapabilities;
    public final NetworkInfo networkInfo;
    public final String subscriberId;

    public int describeContents() {
        return 0;
    }

    private NetworkState() {
        this.networkInfo = null;
        this.linkProperties = null;
        this.networkCapabilities = null;
        this.network = null;
        this.subscriberId = null;
        this.legacyNetworkType = 0;
    }

    public NetworkState(int i, LinkProperties linkProperties2, NetworkCapabilities networkCapabilities2, Network network2, String str) {
        this(i, new NetworkInfo(i, 0, (String) null, (String) null), linkProperties2, networkCapabilities2, network2, str);
    }

    public NetworkState(NetworkInfo networkInfo2, LinkProperties linkProperties2, NetworkCapabilities networkCapabilities2, Network network2, String str) {
        this(networkInfo2.getType(), networkInfo2, linkProperties2, networkCapabilities2, network2, str);
    }

    public NetworkState(int i, NetworkInfo networkInfo2, LinkProperties linkProperties2, NetworkCapabilities networkCapabilities2, Network network2, String str) {
        this.networkInfo = networkInfo2;
        this.linkProperties = linkProperties2;
        this.networkCapabilities = networkCapabilities2;
        this.network = network2;
        this.subscriberId = str;
        this.legacyNetworkType = i;
    }

    public NetworkState(Parcel parcel) {
        this.networkInfo = (NetworkInfo) parcel.readParcelable((ClassLoader) null);
        this.linkProperties = (LinkProperties) parcel.readParcelable((ClassLoader) null);
        this.networkCapabilities = (NetworkCapabilities) parcel.readParcelable((ClassLoader) null);
        this.network = (Network) parcel.readParcelable((ClassLoader) null);
        this.subscriberId = parcel.readString();
        this.legacyNetworkType = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.networkInfo, i);
        parcel.writeParcelable(this.linkProperties, i);
        parcel.writeParcelable(this.networkCapabilities, i);
        parcel.writeParcelable(this.network, i);
        parcel.writeString(this.subscriberId);
        parcel.writeInt(this.legacyNetworkType);
    }
}
