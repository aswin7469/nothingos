package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class TetheredClient implements Parcelable {
    public static final Parcelable.Creator<TetheredClient> CREATOR = new Parcelable.Creator<TetheredClient>() {
        public TetheredClient createFromParcel(Parcel parcel) {
            return new TetheredClient(parcel);
        }

        public TetheredClient[] newArray(int i) {
            return new TetheredClient[i];
        }
    };
    private final List<AddressInfo> mAddresses;
    private final MacAddress mMacAddress;
    private final int mTetheringType;

    public int describeContents() {
        return 0;
    }

    public TetheredClient(MacAddress macAddress, Collection<AddressInfo> collection, int i) {
        this.mMacAddress = macAddress;
        this.mAddresses = new ArrayList(collection);
        this.mTetheringType = i;
    }

    private TetheredClient(Parcel parcel) {
        this((MacAddress) parcel.readParcelable((ClassLoader) null), parcel.createTypedArrayList(AddressInfo.CREATOR), parcel.readInt());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mMacAddress, i);
        parcel.writeTypedList(this.mAddresses);
        parcel.writeInt(this.mTetheringType);
    }

    public MacAddress getMacAddress() {
        return this.mMacAddress;
    }

    public List<AddressInfo> getAddresses() {
        return new ArrayList(this.mAddresses);
    }

    public int getTetheringType() {
        return this.mTetheringType;
    }

    public TetheredClient addAddresses(TetheredClient tetheredClient) {
        LinkedHashSet linkedHashSet = new LinkedHashSet(this.mAddresses.size() + tetheredClient.mAddresses.size());
        linkedHashSet.addAll(this.mAddresses);
        linkedHashSet.addAll(tetheredClient.mAddresses);
        return new TetheredClient(this.mMacAddress, linkedHashSet, this.mTetheringType);
    }

    public int hashCode() {
        return Objects.hash(this.mMacAddress, this.mAddresses, Integer.valueOf(this.mTetheringType));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof TetheredClient)) {
            return false;
        }
        TetheredClient tetheredClient = (TetheredClient) obj;
        if (!this.mMacAddress.equals(tetheredClient.mMacAddress) || !this.mAddresses.equals(tetheredClient.mAddresses) || this.mTetheringType != tetheredClient.mTetheringType) {
            return false;
        }
        return true;
    }

    public static final class AddressInfo implements Parcelable {
        public static final Parcelable.Creator<AddressInfo> CREATOR = new Parcelable.Creator<AddressInfo>() {
            public AddressInfo createFromParcel(Parcel parcel) {
                return new AddressInfo(parcel);
            }

            public AddressInfo[] newArray(int i) {
                return new AddressInfo[i];
            }
        };
        private final LinkAddress mAddress;
        private final String mHostname;

        public int describeContents() {
            return 0;
        }

        public AddressInfo(LinkAddress linkAddress, String str) {
            this.mAddress = linkAddress;
            this.mHostname = str;
        }

        private AddressInfo(Parcel parcel) {
            this((LinkAddress) parcel.readParcelable((ClassLoader) null), parcel.readString());
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mAddress, i);
            parcel.writeString(this.mHostname);
        }

        public LinkAddress getAddress() {
            return this.mAddress;
        }

        public String getHostname() {
            return this.mHostname;
        }

        public long getExpirationTime() {
            return this.mAddress.getExpirationTime();
        }

        public int hashCode() {
            return Objects.hash(this.mAddress, this.mHostname);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof AddressInfo)) {
                return false;
            }
            AddressInfo addressInfo = (AddressInfo) obj;
            if (!addressInfo.mAddress.equals(this.mAddress) || !Objects.equals(this.mHostname, addressInfo.mHostname)) {
                return false;
            }
            return true;
        }

        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder("AddressInfo {");
            sb.append((Object) this.mAddress);
            if (this.mHostname != null) {
                str = ", hostname " + this.mHostname;
            } else {
                str = "";
            }
            sb.append(str);
            sb.append("}");
            return sb.toString();
        }
    }

    public String toString() {
        return "TetheredClient {hwAddr " + this.mMacAddress + ", addresses " + this.mAddresses + ", tetheringType " + this.mTetheringType + "}";
    }
}
