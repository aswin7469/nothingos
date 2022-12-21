package android.net.wifi;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

@SystemApi
public final class WifiClient implements Parcelable {
    public static final Parcelable.Creator<WifiClient> CREATOR = new Parcelable.Creator<WifiClient>() {
        public WifiClient createFromParcel(Parcel parcel) {
            return new WifiClient(parcel);
        }

        public WifiClient[] newArray(int i) {
            return new WifiClient[i];
        }
    };
    private final String mApInstanceIdentifier;
    private final MacAddress mMacAddress;

    public int describeContents() {
        return 0;
    }

    public MacAddress getMacAddress() {
        return this.mMacAddress;
    }

    public String getApInstanceIdentifier() {
        return this.mApInstanceIdentifier;
    }

    private WifiClient(Parcel parcel) {
        this.mMacAddress = (MacAddress) parcel.readParcelable((ClassLoader) null);
        this.mApInstanceIdentifier = parcel.readString();
    }

    public WifiClient(MacAddress macAddress, String str) {
        Objects.requireNonNull(macAddress, "mMacAddress must not be null.");
        this.mMacAddress = macAddress;
        this.mApInstanceIdentifier = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mMacAddress, i);
        parcel.writeString(this.mApInstanceIdentifier);
    }

    public String toString() {
        return "WifiClient{mMacAddress=" + this.mMacAddress + "mApInstanceIdentifier=" + this.mApInstanceIdentifier + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiClient)) {
            return false;
        }
        WifiClient wifiClient = (WifiClient) obj;
        if (!Objects.equals(this.mMacAddress, wifiClient.mMacAddress) || !this.mApInstanceIdentifier.equals(wifiClient.mApInstanceIdentifier)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mMacAddress, this.mApInstanceIdentifier);
    }
}
